package minispark;

import minispark.Common.OperationType;

/**
 * Created by lzb on 4/8/17.
 */
public class Scheduler {

  public Scheduler() {

  }

  public Rdd computeRddByStage(Rdd rdd) {
    rdd.isTarget = true;

    
  }

  public Rdd computeRdd(Rdd rdd, OperationType operationType, Function function) {
    int numPartitions = rdd.numPartitions;



    return new Rdd();
  }
}
