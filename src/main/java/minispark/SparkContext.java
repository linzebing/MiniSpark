package minispark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lzb on 4/8/17.
 */
public class SparkContext {

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
    Rdd rdd = new Rdd(this, Common.DependencyType.Narrow, Common.OperationType.HdfsFile, null, hdfsSplitInfo.size(), null, hdfsSplitInfo, hdfsAddr);

    return rdd;
  }

  public void stop() {

  }
}
