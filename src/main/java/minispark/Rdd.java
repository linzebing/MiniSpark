package minispark;

import minispark.Common.DependencyType;
import minispark.Common.OperationType;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static minispark.Common.getPartitionID;

interface Function {

}

class Partition {

  // Different from partitionIndex. partitionId uniquely identifies a partition on the worker node
  public int partitionId;
  public String hostName;
  public Partition(int _partitionId, String _hostName) {
    this.partitionId = _partitionId;
    this.hostName = _hostName;
  }
}

public class Rdd {
  // By default, use hash partition

  public SparkContext sparkContext;

  public boolean cacheHint;

  public DependencyType dependencyType; // Wide or Narrow
  public OperationType operationType; // Map, Reduce, FlatMap...
  public Rdd parentRdd; // Rdd that this current Rdd is derived from
  public int numPartitions; // Number of Partitions
  public Object lock;
  public Function function;

  /*
    Blocks are physical division and input splits are logical division.
    One input split can be map to multiple physical blocks.
    When Hadoop submits jobs, it splits the input data logically and process by each Mapper task.
   */
  public ArrayList<ArrayList<String>> hdfsSplitInfo;
  public String filePath;

  /*
    By default, RDD is partitioned into numPartions (number of splits in HDFS)
   */
  public ArrayList<Partition> partitions;

  public Rdd(SparkContext _sparkContext, DependencyType _dependencyType, OperationType _operationType, Rdd _parentRdd, int _numPartitions, final Function _function, ArrayList<ArrayList<String>> _hdfsSplitInfo, String _filePath) {
    this.sparkContext = _sparkContext;
    this.dependencyType = _dependencyType;
    this.operationType = _operationType;
    this.parentRdd = _parentRdd;
    this.numPartitions = _numPartitions;
    this.lock = new Object();
    this.function = _function;
    this.hdfsSplitInfo = _hdfsSplitInfo;
    this.filePath = _filePath;
    this.partitions = new ArrayList<Partition>();

    for (int i = 0; i < this.numPartitions; ++i) {
      partitions.add(new Partition(getPartitionID(), ""));
    }

    this.cacheHint = false;
  }

  public Rdd cache() {
    this.cacheHint = true;
    return this;
  }

  public Rdd map(Function _function) {
    return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.Map, this, this.numPartitions, _function, this.hdfsSplitInfo, this.filePath);
  }

  public Rdd flatMap(Function _function) {
    return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.FlatMap, this, this.numPartitions, _function, this.hdfsSplitInfo, this.filePath);
  }

  public Rdd count() {
    return this;
  }

  public Rdd reduce() {
    return this;
  }

  public ArrayList<String> collect() throws IOException, TException {
    return (ArrayList<String>) this.sparkContext.scheduler.computeRdd(this, OperationType.Collect, null);
  }
}
