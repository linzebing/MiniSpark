package minispark;

import org.apache.thrift.TException;
import tutorial.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static minispark.Common.getPartitionId;

/**
 * Created by lzb on 4/8/17.
 */
public class Scheduler {
  Master master;
  Partition[][] shufflePartitions;

  public Scheduler() {
    String masterAddress = "";
    String masterPort = "";
    this.master = new Master(masterAddress, masterPort);
  }

  public void runPartition(Rdd targetRdd, int index) throws TException {
    DoJobArgs args = null;
    Partition partition = targetRdd.partitions.get(index);
    Partition parentPartition = targetRdd.parentRdd == null ? null: targetRdd.parentRdd.partitions.get(index);
    switch (targetRdd.operationType) {
      case Parallelize:
        args = new DoJobArgs();
        args.workerOpType = WorkerOpType.ParaJob;
        args.partitionId = partition.partitionId;
        args.inputHostNames = targetRdd.paraArr.subList(index * (targetRdd.paraArr.size() / targetRdd.partitions.size()), Math.min(targetRdd.paraArr.size(), (index + 1) * (targetRdd.paraArr.size() / targetRdd.partitions.size())));
        targetRdd.partitions.get(index).hostName = Master.workerDNSs[index % Master.workerDNSs.length];
        this.master.assignJob(Master.workerDNSs[index % Master.workerDNSs.length], args);
        break;
      case HdfsFile:
        assert(partition.hostName.equals(""));

        args = new DoJobArgs(WorkerOpType.ReadHdfsSplit, partition.partitionId, -1, index, targetRdd.filePath, "", null, null, null);

        ArrayList<String> serverList = targetRdd.hdfsSplitInfo.get(index);

        // TODO: should pick a random server here
        this.master.assignJob(serverList.get(0), args);
        targetRdd.partitions.get(index).hostName = serverList.get(0);
        break;
      case Map:
        assert(targetRdd.function.length() != 0);
        args = new DoJobArgs(WorkerOpType.MapJob, partition.partitionId, parentPartition.partitionId, -1, "", targetRdd.function, null, null, null);
        this.master.assignJob(parentPartition.hostName, args);
        partition.hostName = parentPartition.hostName;
        break;
      case Filter:
        args = new DoJobArgs();
        args.workerOpType = WorkerOpType.FilterJob;
        args.partitionId = partition.partitionId;
        args.inputId = parentPartition.partitionId;
        args.funcName = targetRdd.function;
        this.master.assignJob(parentPartition.hostName, args);
        partition.hostName = parentPartition.hostName;
        break;
      case FilterPair:
        args = new DoJobArgs();
        args.workerOpType = WorkerOpType.FilterPairJob;
        args.partitionId = partition.partitionId;
        args.inputId = parentPartition.partitionId;
        args.funcName = targetRdd.function;
        this.master.assignJob(parentPartition.hostName, args);
        partition.hostName = parentPartition.hostName;
        break;
      case MapPair:
        assert(targetRdd.function.length() != 0);
        args = new DoJobArgs(WorkerOpType.MapPairJob, partition.partitionId, parentPartition.partitionId, -1, "", targetRdd.function, null, null, null);
        this.master.assignJob(parentPartition.hostName, args);
        partition.hostName = parentPartition.hostName;
        break;
      case FlatMap:
        assert(targetRdd.function.length() != 0);
        args = new DoJobArgs(WorkerOpType.FlatMapJob, partition.partitionId, parentPartition.partitionId, -1, "", targetRdd.function, null, null, null);
        this.master.assignJob(parentPartition.hostName, args);
        partition.hostName = parentPartition.hostName;
        break;
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
        partition.hostName = Master.workerDNSs[0];
        // TODO: random pick a host name is OK
        this.master.assignJob(Master.workerDNSs[0], args);
        break;
    }
  }

  public void runPartitionRecursively(Rdd targetRdd, int index) throws IOException, TException {
    // TODO: Should be multithreaded
    if (targetRdd.dependencyType != Common.DependencyType.Wide) {
      if (targetRdd.parentRdd != null) {
        runPartitionRecursively(targetRdd.parentRdd, index);
      }
    }
    runPartition(targetRdd, index);
  }

  public void runRddInStage(final Rdd targetRdd) throws TException, IOException {
    // TODO: should be multithreaded
    Thread[] threads = new Thread[targetRdd.numPartitions];
    for (int i = 0; i < targetRdd.numPartitions; ++i) {
      final int index = i;
      threads[i] = new Thread(new Runnable() {



        public void run() {
          try {
            runPartitionRecursively(targetRdd, index);
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

  public void computeRddByStage(Rdd targetRdd) throws IOException, TException {
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
      for (int i = 0; i < prevNumPartitions; ++i) {
        Partition parentPartition = targetRdd.parentRdd.partitions.get(i);
        ArrayList<Integer> shufflePartitionIds = new ArrayList<>();
        for (int j = 0; j < targetRdd.numPartitions; ++j) {
          shufflePartitionIds.add(shufflePartitions[i][j].partitionId);
        }
        DoJobArgs args = new DoJobArgs(WorkerOpType.HashSplit,  -1, parentPartition.partitionId, -1, "", targetRdd.function, shufflePartitionIds, null, null);
        this.master.assignJob(parentPartition.hostName, args);
      }
      runRddInStage(targetRdd);
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

          DoJobReply reply = this.master.assignJob(partition.hostName, args);
          result.addAll(reply.lines);
        }
        return result;
      case PairCollect:
        ArrayList<StringIntPair> pairResult = new ArrayList<StringIntPair>();
        for (int i = 0; i < rdd.numPartitions; ++i) {
          Partition partition = rdd.partitions.get(i);
          DoJobArgs args = new DoJobArgs(WorkerOpType.GetPairSplit, partition.partitionId, -1, -1, "", "", null, null, null);

          DoJobReply reply = this.master.assignJob(partition.hostName, args);
          pairResult.addAll(reply.pairs);
        }
        return pairResult;
      case Reduce:
        ArrayList<Integer> reduceResults = new ArrayList<>();
        for (int i = 0; i < rdd.numPartitions; ++i) {
          Partition partition = rdd.partitions.get(i);
          DoJobArgs args = new DoJobArgs();
          args.funcName = function;
          args.partitionId = partition.partitionId;
          args.workerOpType = WorkerOpType.ReduceJob;
          DoJobReply reply = this.master.assignJob(partition.hostName, args);
          reduceResults.add(reply.reduceResult);
        }
        int reduceResult = reduceResults.get(0);
        for (int i = 1; i < reduceResults.size(); ++i) {
          try {
            Method method = App.class.getMethod(function, String.class);
            reduceResult = (int) method.invoke(null, int.class, int.class);
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
          DoJobReply reply = this.master.assignJob(partition.hostName, args);
          countResult += reply.reduceResult;
        }
        return countResult;
    }

    return null;
  }
}
