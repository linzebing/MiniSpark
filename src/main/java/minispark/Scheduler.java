package minispark;

import minispark.Common.OperationType;

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
        ArrayList<String> serverList = targetRdd.hdfsSplitInfo.get(index);
        for (String addressHdfs: serverList) {
          System.out.println(addressHdfs);

        }
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

  public Rdd computeRdd(Rdd rdd, OperationType operationType, Function function) {
    int numPartitions = rdd.numPartitions;
    computeRddByStage(rdd);
    switch (operationType) {
      case Collect:
        break;
      case Reduce:
        break;
    }


    return new Rdd();
  }
}
