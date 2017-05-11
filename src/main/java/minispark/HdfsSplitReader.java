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
    Long a = 0L;
    Long b = 0L;
    for (int k = 0; k < 10; ++k) {
      final int num = 1000000;
      ArrayList<String> arr = new ArrayList<>();
      ArrayList<String> result = new ArrayList<>();
      for (int i = 0; i < num; ++i) {
        arr.add(String.valueOf(i));
      }

      /*
      Long start = System.currentTimeMillis();
      ArrayList<String> arr1 = new ArrayList<>();
      for (String str: arr) {
        arr1.add(str.replace('a', 'b'));
      }
      ArrayList<String> arr2 = new ArrayList<>();
      for (String str: arr1) {
        arr2.add(str.toLowerCase());
      }
      ArrayList<String> arr3 = new ArrayList<>();
      for (String str: arr2) {
        arr3.add(str.toUpperCase());
      }
      Long end = System.currentTimeMillis();
      b += end - start;
      */


      Long start = System.currentTimeMillis();
      for (int i = 0; i < num; ++i) {
        result.add(i, arr.get(i).replace('a', 'b').toLowerCase().toUpperCase());
      }
      Long end = System.currentTimeMillis();
      a += end - start;
    }
    System.out.println(a);
    System.out.println(b);
  }
}
