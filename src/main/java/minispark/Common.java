package minispark;

/**
 * Created by lzb on 4/8/17.
 */


public class Common {

    private static int counter = 0;

    public enum DependencyType {
        Wide, Narrow
    }

    public enum OperationType {
        Map, FlatMap, Reduce, ReduceByKey, Filter, Collect, PairCollect, HdfsFile, MapPair, FilterPair, Count, Parallelize, Join
    }

    public static synchronized int getPartitionId() {
        return counter++;
    }
}
