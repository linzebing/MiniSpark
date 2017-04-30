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

  /*
  class DoJobArgs {
    public OperationType operationType;
    public String hdfsFile;
    public int hdfsSplitId;

    public DoJobArgs(OperationType _operationType, String _hdfsFile, int _hdfsSplitId) {
      this.operationType = _operationType;
      this.hdfsFile = _hdfsFile;
      this.hdfsSplitId = _hdfsSplitId;
    }
  }
  */
}
