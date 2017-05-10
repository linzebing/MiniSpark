package minispark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lzb on 4/8/17.
 */
public class SparkContext {

  public static final int numCores = 8;
  public String appName;
  public Scheduler scheduler;
  public Date startTime;

  public SparkContext(String _appName) {
    this.appName = _appName;
    this.scheduler = new Scheduler();
    this.startTime = new Date();
  }

  public Rdd textFile(String hdfsAddr) throws IOException {
    ArrayList<ArrayList<String>> hdfsSplitInfo = HdfsSplitReader.HdfsGetSplitInfo(hdfsAddr);
    Rdd rdd = new Rdd(this, Common.DependencyType.Narrow, Common.OperationType.HdfsFile, null, hdfsSplitInfo.size(), null, hdfsSplitInfo, hdfsAddr, false);

    return rdd;
  }

  public Rdd parallelize(ArrayList<String> arr) {
    Rdd rdd = new Rdd(this, Common.DependencyType.Narrow, Common.OperationType.Parallelize, null, numCores, null, null, null, false);
    rdd.paraArr = arr;
    return rdd;
  }

  public void stop() {

  }
}
