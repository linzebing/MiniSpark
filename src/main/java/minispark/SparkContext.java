package minispark;

import org.apache.thrift.TException;
import tutorial.DoJobArgs;
import tutorial.WorkerOpType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static minispark.Master.workerDNSs;

/**
 * Created by lzb on 4/8/17.
 */
public class SparkContext {

    public static final int numCores = Master.numClientsPerWorker * Master.numWorkers;
    public final Scheduler scheduler;

    public SparkContext(String _appName) {
        this.scheduler = new Scheduler();
    }

    public Rdd textFile(String hdfsAddr) throws IOException {
        ArrayList<ArrayList<String>> hdfsSplitInfo = HdfsSplitReader.HdfsGetSplitInfo(hdfsAddr);

        return new Rdd(this, Common.DependencyType.Narrow, Common.OperationType.HdfsFile, null, hdfsSplitInfo.size(),
                null, hdfsSplitInfo, hdfsAddr, false);
    }

    public Rdd parallelize(ArrayList<String> arr) {
        Rdd rdd = new Rdd(this, Common.DependencyType.Narrow, Common.OperationType.Parallelize, null, numCores, null,
                null, null, false);
        rdd.paraArr = arr;
        return rdd;
    }

    public void stop() throws TException {
        DoJobArgs args = new DoJobArgs();
        args.workerOpType = WorkerOpType.DelSplit;
        for (String workerDNS : workerDNSs) {
            this.scheduler.master.assignJob(workerDNS, new ArrayList<>(Collections.singletonList(args)));
        }
    }
}
