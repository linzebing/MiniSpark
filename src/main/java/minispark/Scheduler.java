package minispark;

import org.apache.thrift.TException;
import tutorial.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static minispark.Common.getPartitionId;

/**
 * Created by lzb on 4/8/17.
 */
public class Scheduler {
  Master master;
  Partition[][] shufflePartitions;

  public Scheduler() {
    this.master = new Master();
  }

  public DoJobArgs runPartition(Rdd targetRdd, int index) throws TException {
    DoJobArgs args = null;
    Partition partition = targetRdd.partitions.get(index);
    Partition parentPartition = null;
    if (targetRdd.operationType != Common.OperationType.ReduceByKey) {
      parentPartition = targetRdd.parentRdd == null ? null: targetRdd.parentRdd.partitions.get(index);
    }
    switch (targetRdd.operationType) {
      case Parallelize:
        args = new DoJobArgs();
        args.workerOpType = WorkerOpType.ParaJob;
        args.partitionId = partition.partitionId;
        int size = targetRdd.partitions.size();
        args.inputHostNames = targetRdd.paraArr.subList(index * ((targetRdd.paraArr.size() + size - 1) / size), Math.min(targetRdd.paraArr.size(), (index + 1) * ((targetRdd.paraArr.size() + size - 1) / size)));
        targetRdd.partitions.get(index).hostName = master.findLeastLoaded(Arrays.asList(Master.workerDNSs));
        break;
      case HdfsFile:
        args = new DoJobArgs(WorkerOpType.ReadHdfsSplit, partition.partitionId, -1, index, targetRdd.filePath, "", null, null, null);
        ArrayList<String> serverList = targetRdd.hdfsSplitInfo.get(index);
        targetRdd.partitions.get(index).hostName = master.findLeastLoaded(serverList);
        break;
      case Map:
        assert(targetRdd.function.length() != 0);
        args = new DoJobArgs(WorkerOpType.MapJob, partition.partitionId, parentPartition.partitionId, -1, "", targetRdd.function, null, null, null);
        partition.hostName = parentPartition.hostName;
        break;
      case Filter:
        args = new DoJobArgs();
        args.workerOpType = WorkerOpType.FilterJob;
        args.partitionId = partition.partitionId;
        args.inputId = parentPartition.partitionId;
        args.funcName = targetRdd.function;
        partition.hostName = parentPartition.hostName;
        break;
      case FilterPair:
        args = new DoJobArgs();
        args.workerOpType = WorkerOpType.FilterPairJob;
        args.partitionId = partition.partitionId;
        args.inputId = parentPartition.partitionId;
        args.funcName = targetRdd.function;
        partition.hostName = parentPartition.hostName;
        break;
      case MapPair:
        assert(targetRdd.function.length() != 0);
        args = new DoJobArgs(WorkerOpType.MapPairJob, partition.partitionId, parentPartition.partitionId, -1, "", targetRdd.function, null, null, null);
        partition.hostName = parentPartition.hostName;
        break;
      case FlatMap:
        assert(targetRdd.function.length() != 0);
        args = new DoJobArgs(WorkerOpType.FlatMapJob, partition.partitionId, parentPartition.partitionId, -1, "", targetRdd.function, null, null, null);
        partition.hostName = parentPartition.hostName;
        break;
      default:
        assert false;
    }
    return args;
  }

  public void runWide(Rdd targetRdd, int index) throws TException {
    DoJobArgs args = null;
    Partition partition = targetRdd.partitions.get(index);
    switch (targetRdd.operationType) {
      case ReduceByKey:
        int prevNumPartitions = targetRdd.parentRdd.numPartitions;
        ArrayList<Integer> inputIds = new ArrayList<>();
        ArrayList<String> inputHostNames = new ArrayList<>();
        for (int i = 0; i < prevNumPartitions; ++i) {
          inputIds.add(shufflePartitions[i][index].partitionId);
          inputHostNames.add(shufflePartitions[i][index].hostName);
        }
        args = new DoJobArgs();
        args.workerOpType = WorkerOpType.ReduceByKeyJob;
        args.inputIds = inputIds;
        args.inputHostNames = inputHostNames;
        args.partitionId = partition.partitionId;
        args.funcName = targetRdd.function;
        args.hdfsSplitId = index;
        partition.hostName = master.findLeastLoaded(Arrays.asList(Master.workerDNSs));
        this.master.assignJob(partition.hostName, new ArrayList<DoJobArgs>(Arrays. asList(args)));
        break;
      default:
        assert false;
    }
  }

  public void runPartitionRecursively(Rdd targetRdd, int index, ArrayList<DoJobArgs> doJobArgsArrayList) throws IOException, TException {
    if (targetRdd.dependencyType != Common.DependencyType.Wide) {
      if (targetRdd.parentRdd != null) {
        runPartitionRecursively(targetRdd.parentRdd, index, doJobArgsArrayList);
      }
    }
    doJobArgsArrayList.add(runPartition(targetRdd, index));
  }

  public void runRddInStage(final Rdd targetRdd) throws TException, IOException {
    Thread[] threads = new Thread[targetRdd.numPartitions];
    for (int i = 0; i < targetRdd.numPartitions; ++i) {
      final int index = i;
      threads[i] = new Thread(new Runnable() {
        public void run() {
          ArrayList<DoJobArgs> doJobArgsArrayList = new ArrayList<>();
          try {
            runPartitionRecursively(targetRdd, index, doJobArgsArrayList);
          } catch (Exception e) {
            e.printStackTrace();
          }
          try {
            master.assignJob(targetRdd.partitions.get(index).hostName, doJobArgsArrayList);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      threads[i].start();
    }
    for (int i = 0; i < targetRdd.numPartitions; ++i) {
      try {
        threads[i].join();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void computeRddByStage(final Rdd targetRdd) throws IOException, TException {
    // Because MiniSpark doesn't support opeartors like join that involves multiple RDDs, therefore
    // we omit building a DAG here.
    if (targetRdd.dependencyType == Common.DependencyType.Wide) {
      runRddInStage(targetRdd.parentRdd); // Wide dependency, have to materialize parent RDD first

      int prevNumPartitions = targetRdd.parentRdd.numPartitions;
      shufflePartitions = new Partition[prevNumPartitions][targetRdd.numPartitions];
      for (int i = 0; i < prevNumPartitions; ++i) {
        for (int j = 0; j < targetRdd.numPartitions; ++j) {
          shufflePartitions[i][j] = new Partition(getPartitionId(), targetRdd.parentRdd.partitions.get(i).hostName);
        }
      }
      Thread[] threads = new Thread[prevNumPartitions];
      for (int i = 0; i < prevNumPartitions; ++i) {
        final int index = i;
        threads[i] = new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              Partition parentPartition = targetRdd.parentRdd.partitions.get(index);
              ArrayList<Integer> shufflePartitionIds = new ArrayList<>();
              for (int j = 0; j < targetRdd.numPartitions; ++j) {
                shufflePartitionIds.add(shufflePartitions[index][j].partitionId);
              }
              DoJobArgs args = new DoJobArgs(WorkerOpType.HashSplit,  -1, parentPartition.partitionId, -1, "", targetRdd.function, shufflePartitionIds, null, null);
              master.assignJob(parentPartition.hostName, new ArrayList<DoJobArgs>(Arrays. asList(args)));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        threads[i].start();
      }
      for (int i = 0; i < prevNumPartitions; ++i) {
        try {
          threads[i].join();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

     threads = new Thread[targetRdd.numPartitions];
      for (int i = 0; i < targetRdd.numPartitions; ++i) {
        final int index = i;
        threads[i] = new Thread(new Runnable() {
          public void run() {
            try {
              runWide(targetRdd, index);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        threads[i].start();
      }
      for (int i = 0; i < targetRdd.numPartitions; ++i) {
        try {
          threads[i].join();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else {
      runRddInStage(targetRdd);
    }
  }

  public Object computeRdd(Rdd rdd, Common.OperationType operationType, String function) throws TException, IOException {
    computeRddByStage(rdd);
    switch (operationType) {
      case Collect:
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < rdd.numPartitions; ++i) {
          Partition partition = rdd.partitions.get(i);
          DoJobArgs args = new DoJobArgs(WorkerOpType.GetSplit, partition.partitionId, -1, -1, "", "", null, null, null);

          DoJobReply reply = this.master.assignJob(partition.hostName, new ArrayList<DoJobArgs>(Arrays. asList(args)));
          result.addAll(reply.lines);
        }
        return result;
      case PairCollect:
        ArrayList<StringNumPair> pairResult = new ArrayList<StringNumPair>();
        for (int i = 0; i < rdd.numPartitions; ++i) {
          Partition partition = rdd.partitions.get(i);
          DoJobArgs args = new DoJobArgs(WorkerOpType.GetPairSplit, partition.partitionId, -1, -1, "", "", null, null, null);

          DoJobReply reply = this.master.assignJob(partition.hostName, new ArrayList<DoJobArgs>(Arrays. asList(args)));
          pairResult.addAll(reply.pairs);
        }
        return pairResult;
      case Reduce:
        ArrayList<Double> reduceResults = new ArrayList<>();
        for (int i = 0; i < rdd.numPartitions; ++i) {
          Partition partition = rdd.partitions.get(i);
          DoJobArgs args = new DoJobArgs();
          args.funcName = function;
          args.partitionId = partition.partitionId;
          args.workerOpType = WorkerOpType.ReduceJob;
          DoJobReply reply = this.master.assignJob(partition.hostName, new ArrayList<DoJobArgs>(Arrays. asList(args)));
          reduceResults.add(reply.reduceResult);
        }
        double reduceResult = reduceResults.get(0);
        for (int i = 1; i < reduceResults.size(); ++i) {
          try {
            Method method = App.class.getMethod(function, double.class, double.class);
            reduceResult = (int) method.invoke(null, reduceResult, reduceResults.get(i));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return reduceResult;
      case Count:
        int countResult = 0;
        for (int i = 0; i < rdd.numPartitions; ++i) {
          Partition partition = rdd.partitions.get(i);
          DoJobArgs args = new DoJobArgs();
          args.workerOpType = rdd.isPairRdd? WorkerOpType.CountPairJob: WorkerOpType.CountJob;
          args.partitionId = partition.partitionId;
          DoJobReply reply = this.master.assignJob(partition.hostName, new ArrayList<DoJobArgs>(Arrays. asList(args)));
          countResult += reply.reduceResult;
        }
        return countResult;
    }

    return null;
  }
}
