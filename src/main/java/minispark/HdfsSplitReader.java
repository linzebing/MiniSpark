package minispark;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.fs.FileSystem;


/**
 * Created by lzb on 4/9/17.
 */
public class HdfsSplitReader {
  public static ArrayList<ArrayList<String>> HdfsGetSplitInfo(String fileName) throws IOException {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    JobConf conf = new JobConf();
    FileInputFormat.setInputPaths(conf, fileName);
    TextInputFormat format = new TextInputFormat();
    format.configure(conf);
    InputSplit[] splits = format.getSplits(conf, 0);
    for (int i = 0 ; i < splits.length; ++i) {
      String[] locations = splits[i].getLocations();
      ArrayList arrayList = new ArrayList<String>();
      for (int j = 0; j < locations.length; ++j) {
        arrayList.add(locations[j]);
      }
      result.add(arrayList);
    }
    System.out.println(result);
    return result;
  }

  public static ArrayList<String> HdfsSplitRead(String fileName, int index) throws IOException {
    JobConf conf = new JobConf();
    FileSystem fs = FileSystem.get(conf);
    FileInputFormat.setInputPaths(conf, fileName);
    TextInputFormat format = new TextInputFormat();
    format.configure(conf);
    InputSplit[] splits = format.getSplits(conf, 0);
    LongWritable key = new LongWritable();
    Text value = new Text();
    RecordReader<LongWritable, Text> recordReader =
        format.getRecordReader(splits[index], conf, Reporter.NULL);

    ArrayList<String> result = new ArrayList<String>();

    while (recordReader.next(key, value)) {
      result.add(value.toString());
    }
    return result;
  }

  public static void main(String[] args) throws IOException {
    Long start = System.currentTimeMillis();
    Long total = 10000 * (long)Integer.MAX_VALUE;
    double t = 0.0;
    for (int i = 0; i < 10000; ++i) {
      int cnt = 0;
      for (int j = 0; j < Integer.MAX_VALUE / 10; ++j) {
        double a = Math.random();
        double b = Math.random();
        if (a * a + b * b < 1) {
          ++cnt;
        }
      }
      t += 4.0 * cnt / Integer.MAX_VALUE;

      System.out.println(400.0 * cnt / Integer.MAX_VALUE);
    }
    System.out.println(t / 10000);
    Long end = System.currentTimeMillis();
    System.out.println("Elapsed " + (end - start) / 10000);
  }
}

