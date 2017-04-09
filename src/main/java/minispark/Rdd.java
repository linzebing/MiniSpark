package minispark;

import javafx.util.Pair;
import minispark.Common.DependencyType;
import minispark.Common.OperationType;

interface Function {
  Pair<String, String> mapFunc(String Key);
}

public class Rdd {
  // By default, use hash partition

  public SparkContext sparkContext;

  public boolean isTarget;
  public boolean cacheHint;

  public DependencyType dependencyType; // Wide or Narrow
  public OperationType operationType; // Map, Reduce, FlatMap...
  public Rdd parentRdd; // Rdd that this current Rdd is derived from
  public int numPartitions; // Number of Partitions
  public Object lock;
  public Function function;


  public Rdd(SparkContext _sparkContext, DependencyType _dependencyType, OperationType _operationType, Rdd _parentRdd, int _numPartitions, final Function _function) {
    this.sparkContext = _sparkContext;
    this.dependencyType = _dependencyType;
    this.operationType = _operationType;
    this.parentRdd = _parentRdd;
    this.numPartitions = _numPartitions;
    this.lock = new Object();
    this.function = _function;

    this.isTarget = false;
    this.cacheHint = false;
  }

  public Rdd() {

  }

  public Rdd Cache() {
    this.cacheHint = true;
    return this;
  }

  public Rdd Map(Function _function) {
    return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.Map, this, this.numPartitions, _function);
  }

  public Rdd FlatMap(Function _function) {
    return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.FlatMap, this, this.numPartitions, _function);
  }

  public Rdd Count() {
    return new Rdd();
  }

  public Rdd Reduce() {
    return new Rdd();
  }

  public Rdd Collect() {
    return new Rdd();
  }
}
