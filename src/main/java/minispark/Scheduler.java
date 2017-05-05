package minispark;

import minispark.Common.*;
import java.util.ArrayList;

/**
 * Created by lzb on 4/8/17.
 */
public class Scheduler {
  Master master;

  public Scheduler() {
    String masterAddress = "";
    String masterPort = "";
    this.master = new Master(masterAddress, masterPort);
  }

  public void runPartition(Rdd targetRdd, int index) {
    switch (targetRdd.operationType) {
      case HdfsFile:
        Partition partition = targetRdd.partitions.get(index);
        assert(partition.hostName.equals(""));

        DoJobArgs args = new DoJobArgs(WorkerOpType.ReadHDFSSplit, partition.partitionId, index, targetRdd.filePath);
        DoJobReply reply = new DoJobReply();

        ArrayList<String> serverList = targetRdd.hdfsSplitInfo.get(index);

        // TODO: should pick a random server here
        this.master.assignJob(serverList.get(0), args, reply);
        targetRdd.partitions.get(index).hostName = serverList.get(0);
        break;
      case Map:

        break;

      case Reduce:

        break;

      case ReduceByKey:

        break;
    }
  }

  public void runPartitionRecursively(Rdd targetRdd, int index) {
    // Should be multithreaded
    // TODO: deal with WIDE dependency here
    if (targetRdd.dependencyType != Common.DependencyType.Wide) {
      if (targetRdd.parentRdd != null) {
        runPartitionRecursively(targetRdd.parentRdd, index);
      }
      runPartition(targetRdd, index);
    }
  }

  public void runRddInStage(Rdd targetRdd) {
    // TODO: should be multithreaded
    for (int i = 0; i < targetRdd.numPartitions; ++i) {
      runPartitionRecursively(targetRdd, i);
    }
  }

  public void computeRddByStage(Rdd targetRdd) {
    // Because MiniSpark doesn't support opeartors like join that involves multiple RDDs, therefore
    // we omit building a DAG here.
    // 多线程情况下wide dependency一定要等前面的依赖全都执行完成了才能继续
    /*ArrayList<Rdd> sortedRddList = new ArrayList<Rdd>();
    Rdd tmpRdd = targetRdd;
    while (tmpRdd != null) {
      sortedRddList.add(tmpRdd);
      tmpRdd = tmpRdd.parentRdd;
    }
    Collections.reverse(sortedRddList);
    for (Rdd rdd: sortedRddList) {
      runRddInStage(rdd);
    }*/
    runRddInStage(targetRdd);
  }

  public Object computeRdd(Rdd rdd, OperationType operationType, Function function) {
    computeRddByStage(rdd);
    switch (operationType) {
      case Collect:
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < rdd.numPartitions; ++i) {
          Partition partition = rdd.partitions.get(i);
          DoJobArgs args = new DoJobArgs(WorkerOpType.GetSplit, partition.partitionId);
          DoJobReply reply = new DoJobReply();

          this.master.assignJob(partition.hostName, args, reply);
          result.addAll(reply.lines);
        }
        return result;
      case Reduce:
        break;
    }


    return rdd;
  }
}
