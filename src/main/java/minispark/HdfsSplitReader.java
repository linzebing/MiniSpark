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
    double t = 0.0;
    for (int i = 0; i < 80; ++i) {
      double num = App.monteCarlo("1").num;
      t += num;
      System.out.println(num);
    }
    System.out.println(t / 80);
    Long end = System.currentTimeMillis();
    System.out.println("Elapsed " + (end - start) / 1000.0);
  }
}

