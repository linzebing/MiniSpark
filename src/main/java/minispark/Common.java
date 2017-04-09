package minispark;

/**
 * Created by lzb on 4/8/17.
 */



public class Common {
  public enum DependencyType {
    Wide, Narrow
  }

  public enum OperationType {
    Map, FlatMap, Reduce, ReduceByKey, Filter, Collect, HdfsFile
  }
}
