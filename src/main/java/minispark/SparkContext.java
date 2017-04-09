package minispark;

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

  public Rdd textFile() {
    Rdd rdd = new Rdd();
    rdd.sparkContext = this;
    return rdd;
  }

  public void stop() {

  }
}
