package minispark;

import tutorial.WorkerOpType;

import java.util.ArrayList;

/**
 * Created by lzb on 4/8/17.
 */



public class Common {

  public static int counter = 0;

  public enum DependencyType {
    Wide, Narrow
  }

  public enum OperationType {
    Map, FlatMap, Reduce, ReduceByKey, Filter, Collect, PairCollect, HdfsFile, MapPair
  }

  public static int getPartitionID() {
    return counter++;
  }
}
