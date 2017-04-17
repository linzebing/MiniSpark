package minispark;

import javafx.util.Pair;
import minispark.Common.DependencyType;
import minispark.Common.OperationType;

import java.util.ArrayList;
import java.util.List;

interface Function {
  Pair<String, String> mapFunc(String Key);
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
  public ArrayList<ArrayList<String>> hdfsSplitInfo;


  public Rdd(SparkContext _sparkContext, DependencyType _dependencyType, OperationType _operationType, Rdd _parentRdd, int _numPartitions, final Function _function, ArrayList<ArrayList<String>> _hdfsSplitInfo) {
    this.sparkContext = _sparkContext;
    this.dependencyType = _dependencyType;
    this.operationType = _operationType;
    this.parentRdd = _parentRdd;
    this.numPartitions = _numPartitions;
    this.lock = new Object();
    this.function = _function;
    this.hdfsSplitInfo = _hdfsSplitInfo;

    this.cacheHint = false;
  }

  public Rdd() {

  }

  public Rdd cache() {
    this.cacheHint = true;
    return this;
  }

  public Rdd map(Function _function) {
    return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.Map, this, this.numPartitions, _function, this.hdfsSplitInfo);
  }

  public Rdd flatMap(Function _function) {
    return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.FlatMap, this, this.numPartitions, _function, this.hdfsSplitInfo);
  }

  public Rdd count() {
    return new Rdd();
  }

  public Rdd reduce() {
    return new Rdd();
  }

  public List<String> collect() {
    return null;
  }
}
