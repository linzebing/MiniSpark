package minispark;

import minispark.Common.DependencyType;
import minispark.Common.OperationType;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.ArrayList;

import static minispark.Common.getPartitionId;

class Partition {

    // Different from partitionIndex. partitionId uniquely identifies a partition on the worker node
    public int partitionId;
    public String hostName;

    public Partition(int _partitionId, String _hostName) {
        this.partitionId = _partitionId;
        this.hostName = _hostName;
    }

    public Partition() {

    }
}

public class Rdd {
    // By default, use hash partition

    public SparkContext sparkContext;

    public boolean cacheHint;

    public DependencyType dependencyType; // Wide or Narrow
    public OperationType operationType; // Map, Reduce, FlatMap...
    public Rdd parentRdd; // Rdd that this current Rdd is derived from
    public Rdd parentRdd2; // for Join Operations
    public int numPartitions; // Number of Partitions
    public String function;

    public boolean isPairRdd;

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
    public ArrayList<String> paraArr;

    public Rdd(SparkContext _sparkContext, DependencyType _dependencyType, OperationType _operationType, Rdd _parentRdd,
            int _numPartitions, final String _function, ArrayList<ArrayList<String>> _hdfsSplitInfo, String _filePath,
            boolean _isPairRdd) {
        this.sparkContext = _sparkContext;
        this.dependencyType = _dependencyType;
        this.operationType = _operationType;
        this.parentRdd = _parentRdd;
        this.numPartitions = _numPartitions;
        this.function = _function;
        this.hdfsSplitInfo = _hdfsSplitInfo;
        this.filePath = _filePath;
        this.partitions = new ArrayList<Partition>();

        for (int i = 0; i < this.numPartitions; ++i) {
            partitions.add(new Partition(getPartitionId(), ""));
        }

        this.cacheHint = false;
        this.isPairRdd = _isPairRdd;
        this.parentRdd2 = null;
    }

    public Rdd cache() {
        this.cacheHint = true;
        return this;
    }

    public Rdd map(String _function) {
        return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.Map, this, this.numPartitions, _function,
                this.hdfsSplitInfo, this.filePath, this.isPairRdd);
    }

    public Rdd mapPair(String _function) {
        return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.MapPair, this, this.numPartitions,
                _function, this.hdfsSplitInfo, this.filePath, true);
    }

    public Rdd flatMap(String _function) {
        return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.FlatMap, this, this.numPartitions,
                _function, this.hdfsSplitInfo, this.filePath, this.isPairRdd);
    }

    public Rdd reduceByKey(String _function) {
        assert this.isPairRdd;
        return new Rdd(this.sparkContext, DependencyType.Wide, OperationType.ReduceByKey, this, SparkContext.numCores,
                _function, this.hdfsSplitInfo, this.filePath, this.isPairRdd);
    }

    public Rdd filter(String _function) {
        assert !this.isPairRdd;
        return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.Filter, this, this.numPartitions,
                _function, this.hdfsSplitInfo, this.filePath, this.isPairRdd);
    }

    public Rdd filterPair(String _function) {
        assert this.isPairRdd;
        return new Rdd(this.sparkContext, DependencyType.Narrow, OperationType.FilterPair, this, this.numPartitions,
                _function, this.hdfsSplitInfo, this.filePath, this.isPairRdd);
    }

    public int count() throws IOException, TException {
        return (int) this.sparkContext.scheduler.computeRdd(this, OperationType.Count, null);
    }

    public double reduce(String _function) throws IOException, TException {
        return (double) this.sparkContext.scheduler.computeRdd(this, OperationType.Reduce, _function);
    }

    public Rdd join(Rdd rdd) {
        Rdd joinedRdd = new Rdd(this.sparkContext, DependencyType.Wide, OperationType.Join, this, this.numPartitions,
                null, this.hdfsSplitInfo, this.filePath, this.isPairRdd);
        joinedRdd.parentRdd2 = rdd;
        return joinedRdd;
    }

    public Object collect() throws IOException, TException {
        return this.sparkContext.scheduler.computeRdd(this,
                this.isPairRdd ? OperationType.PairCollect : OperationType.Collect, null);
    }
}
