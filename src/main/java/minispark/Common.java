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
    Map, FlatMap, Reduce, ReduceByKey, Filter, Collect, HdfsFile
  }

  /*
  static class DoJobReply {
    public ArrayList<String> lines;

    public DoJobReply() {

    }
  }

  static class DoJobArgs {
    public WorkerOpType workerOpType;
    public int partitionId;
    public int hdfsSplitId;
    public String filePath;

    public DoJobArgs(WorkerOpType _workerOpType, int _partitionId) {
      this.workerOpType = _workerOpType;
      this.partitionId = _partitionId;
    }

    public DoJobArgs(WorkerOpType _workerOpType, int _partitionId, int _hdfsSplitId, String _filePath) {
      this(_workerOpType, _partitionId);
      this.hdfsSplitId = _hdfsSplitId;
      this.filePath = _filePath;
    }
  }*/

  public static int getPartitionID() {
    return counter++;
  }
}
