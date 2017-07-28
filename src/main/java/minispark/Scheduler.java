package minispark;

import org.apache.thrift.TException;
import tutorial.DoJobArgs;
import tutorial.DoJobReply;
import tutorial.StringNumPair;
import tutorial.WorkerOpType;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static minispark.Common.getPartitionId;

/**
 * Created by lzb on 4/8/17.
 */
public class Scheduler {
    final Master master;
    private Partition[][] shufflePartitions;
    private Partition[][] shufflePartitions2;

    public Scheduler() {
        this.master = new Master();
    }

    private DoJobArgs runPartition(Rdd targetRdd, int index) {
        DoJobArgs args = null;
        Partition partition = targetRdd.partitions.get(index);
        Partition parentPartition = null;
        if (targetRdd.operationType != Common.OperationType.ReduceByKey) {
            parentPartition = targetRdd.parentRdd == null ? null : targetRdd.parentRdd.partitions.get(index);
        }
        switch (targetRdd.operationType) {
            case Parallelize:
                args = new DoJobArgs();
                args.workerOpType = WorkerOpType.ParaJob;
                args.partitionId = partition.partitionId;
                int size = targetRdd.partitions.size();
                args.inputHostNames = targetRdd.paraArr.subList(
                        Math.min(targetRdd.paraArr.size(), index * ((targetRdd.paraArr.size() + size - 1) / size)),
                        Math.min(targetRdd.paraArr.size(),
                                (index + 1) * ((targetRdd.paraArr.size() + size - 1) / size)));
                targetRdd.partitions.get(index).hostName = master.findLeastLoaded(Arrays.asList(Master.workerDNSs));
                break;
            case HdfsFile:
                args = new DoJobArgs(WorkerOpType.ReadHdfsSplit, partition.partitionId, -1, index, targetRdd.filePath,
                        "", null, null, null, null, null);
                ArrayList<String> serverList = targetRdd.hdfsSplitInfo.get(index);
                targetRdd.partitions.get(index).hostName = master.findLeastLoaded(serverList);
                break;
            case Map:
                assert (targetRdd.function.length() != 0);
                assert parentPartition != null;
                args = new DoJobArgs(WorkerOpType.MapJob, partition.partitionId, parentPartition.partitionId, -1, "",
                        targetRdd.function, null, null, null, null, null);
                partition.hostName = parentPartition.hostName;
                break;
            case Filter:
                args = new DoJobArgs();
                args.workerOpType = WorkerOpType.FilterJob;
                args.partitionId = partition.partitionId;
                assert parentPartition != null;
                args.inputId = parentPartition.partitionId;
                args.funcName = targetRdd.function;
                partition.hostName = parentPartition.hostName;
                break;
            case FilterPair:
                args = new DoJobArgs();
                args.workerOpType = WorkerOpType.FilterPairJob;
                args.partitionId = partition.partitionId;
                assert parentPartition != null;
                args.inputId = parentPartition.partitionId;
                args.funcName = targetRdd.function;
                partition.hostName = parentPartition.hostName;
                break;
            case MapPair:
                assert (targetRdd.function.length() != 0);
                assert parentPartition != null;
                args = new DoJobArgs(WorkerOpType.MapPairJob, partition.partitionId, parentPartition.partitionId, -1,
                        "", targetRdd.function, null, null, null, null, null);
                partition.hostName = parentPartition.hostName;
                break;
            case FlatMap:
                assert (targetRdd.function.length() != 0);
                assert parentPartition != null;
                args = new DoJobArgs(WorkerOpType.FlatMapJob, partition.partitionId, parentPartition.partitionId, -1,
                        "", targetRdd.function, null, null, null, null, null);
                partition.hostName = parentPartition.hostName;
                break;
            default:
                assert false;
        }
        return args;
    }

    private void runWide(Rdd targetRdd, int index) throws TException {
        DoJobArgs args;
        Partition partition = targetRdd.partitions.get(index);
        int prevNumPartitions = targetRdd.parentRdd.numPartitions;
        ArrayList<Integer> inputIds = new ArrayList<>();
        ArrayList<String> inputHostNames = new ArrayList<>();
        switch (targetRdd.operationType) {
            case ReduceByKey:
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
                this.master.assignJob(partition.hostName, new ArrayList<>(Collections.singletonList(args)));
                break;
            case Join:
                int prevNumPartitions2 = targetRdd.parentRdd2.numPartitions;
                ArrayList<Integer> inputIds2 = new ArrayList<>();
                ArrayList<String> inputHostNames2 = new ArrayList<>();
                for (int i = 0; i < prevNumPartitions; ++i) {
                    inputIds.add(shufflePartitions[i][index].partitionId);
                    inputHostNames.add(shufflePartitions[i][index].hostName);
                }
                for (int i = 0; i < prevNumPartitions2; ++i) {
                    inputIds2.add(shufflePartitions2[i][index].partitionId);
                    inputHostNames2.add(shufflePartitions2[i][index].hostName);
                }
                args = new DoJobArgs();
                args.workerOpType = WorkerOpType.ReduceByKeyJob;
                args.inputIds = inputIds;
                args.inputHostNames = inputHostNames;
                args.inputIds2 = inputIds2;
                args.inputHostNames2 = inputHostNames2;
                args.partitionId = partition.partitionId;
                args.funcName = targetRdd.function;
                args.hdfsSplitId = index;
                partition.hostName = master.findLeastLoaded(Arrays.asList(Master.workerDNSs));
                this.master.assignJob(partition.hostName, new ArrayList<>(Collections.singletonList(args)));
                break;
            default:
                assert false;
        }
    }

    private void runPartitionRecursively(Rdd targetRdd, int index,
            ArrayList<DoJobArgs> doJobArgsArrayList) throws TException {
        if (targetRdd.dependencyType != Common.DependencyType.Wide) {
            if (targetRdd.parentRdd != null) {
                runPartitionRecursively(targetRdd.parentRdd, index, doJobArgsArrayList);
            }
            if (targetRdd.parentRdd2 != null) {
                runPartitionRecursively(targetRdd.parentRdd2, index, doJobArgsArrayList);
            }
        }
        doJobArgsArrayList.add(runPartition(targetRdd, index));
    }

    private void runRddInStage(final Rdd targetRdd) {
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

    private void computeRddByStage(final Rdd targetRdd) throws IOException, TException {
        // Because MiniSpark doesn't support operators like join that involves multiple RDDs, therefore
        // we omit building a DAG here.
        if (targetRdd.dependencyType == Common.DependencyType.Wide) {
            runRddInStage(targetRdd.parentRdd); // Wide dependency, have to materialize parent RDD first

            int prevNumPartitions = targetRdd.parentRdd.numPartitions;
            shufflePartitions = new Partition[prevNumPartitions][targetRdd.numPartitions];
            for (int i = 0; i < prevNumPartitions; ++i) {
                for (int j = 0; j < targetRdd.numPartitions; ++j) {
                    shufflePartitions[i][j] = new Partition(getPartitionId(),
                            targetRdd.parentRdd.partitions.get(i).hostName);
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
                            DoJobArgs args = new DoJobArgs(WorkerOpType.HashSplit, -1, parentPartition.partitionId, -1,
                                    "", targetRdd.function, shufflePartitionIds, null, null, null, null);
                            master.assignJob(parentPartition.hostName,
                                    new ArrayList<>(Collections.singletonList(args)));
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

            if (targetRdd.parentRdd2 != null) {
                int prevNumPartitions2 = targetRdd.parentRdd2.numPartitions;
                shufflePartitions2 = new Partition[prevNumPartitions2][targetRdd.numPartitions];
                for (int i = 0; i < prevNumPartitions2; ++i) {
                    for (int j = 0; j < targetRdd.numPartitions; ++j) {
                        shufflePartitions2[i][j] = new Partition(getPartitionId(),
                                targetRdd.parentRdd2.partitions.get(i).hostName);
                    }
                }
                threads = new Thread[prevNumPartitions2];
                for (int i = 0; i < prevNumPartitions2; ++i) {
                    final int index = i;
                    threads[i] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Partition parentPartition = targetRdd.parentRdd2.partitions.get(index);
                                ArrayList<Integer> shufflePartitionIds2 = new ArrayList<>();
                                for (int j = 0; j < targetRdd.numPartitions; ++j) {
                                    shufflePartitionIds2.add(shufflePartitions2[index][j].partitionId);
                                }
                                DoJobArgs args = new DoJobArgs(WorkerOpType.HashSplit, -1, parentPartition.partitionId,
                                        -1,
                                        "", targetRdd.function, shufflePartitionIds2, null, null, null, null);
                                master.assignJob(parentPartition.hostName, new ArrayList<>(
                                        Collections.singletonList(args)));
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

    public Object computeRdd(final Rdd rdd, Common.OperationType operationType,
            final String function) throws TException, IOException {
        computeRddByStage(rdd);
        switch (operationType) {
            case Collect:
                ArrayList<String> result = new ArrayList<>();
                for (int i = 0; i < rdd.numPartitions; ++i) {
                    Partition partition = rdd.partitions.get(i);
                    DoJobArgs args = new DoJobArgs(WorkerOpType.GetSplit, partition.partitionId, -1, -1, "", "", null,
                            null, null, null, null);

                    DoJobReply reply = this.master.assignJob(partition.hostName,
                            new ArrayList<>(Collections.singletonList(args)));
                    result.addAll(reply.lines);
                }
                return result;
            case PairCollect:
                ArrayList<StringNumPair> pairResult = new ArrayList<>();
                for (int i = 0; i < rdd.numPartitions; ++i) {
                    Partition partition = rdd.partitions.get(i);
                    DoJobArgs args = new DoJobArgs(WorkerOpType.GetPairSplit, partition.partitionId, -1, -1, "", "",
                            null, null, null, null, null);

                    DoJobReply reply = this.master.assignJob(partition.hostName,
                            new ArrayList<>(Collections.singletonList(args)));
                    pairResult.addAll(reply.pairs);
                }
                return pairResult;
            case Reduce:
                Thread[] threads = new Thread[rdd.numPartitions];
                final Double[] reduceResults = new Double[rdd.numPartitions];
                for (int i = 0; i < rdd.numPartitions; ++i) {
                    final int index = i;
                    threads[i] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Partition partition = rdd.partitions.get(index);
                                DoJobArgs args = new DoJobArgs();
                                args.funcName = function;
                                args.partitionId = partition.partitionId;
                                args.workerOpType = WorkerOpType.ReduceJob;
                                DoJobReply reply = master.assignJob(partition.hostName,
                                        new ArrayList<>(Collections.singletonList(args)));
                                reduceResults[index] = reply.reduceResult;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    threads[i].start();
                }
                for (int i = 0; i < rdd.numPartitions; ++i) {
                    try {
                        threads[i].join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                double reduceResult = reduceResults[0];
                for (int i = 1; i < reduceResults.length; ++i) {
                    try {
                        Method method = App.class.getMethod(function, double.class, double.class);
                        reduceResult = (double) method.invoke(null, reduceResult, reduceResults[i]);
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
                    args.workerOpType = rdd.isPairRdd ? WorkerOpType.CountPairJob : WorkerOpType.CountJob;
                    args.partitionId = partition.partitionId;
                    DoJobReply reply = this.master.assignJob(partition.hostName,
                            new ArrayList<>(Collections.singletonList(args)));
                    countResult += reply.reduceResult;
                }
                return countResult;
        }

        return null;
    }
}
