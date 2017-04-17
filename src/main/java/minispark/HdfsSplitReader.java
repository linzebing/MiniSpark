package minispark;

import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;

import java.io.IOException;
import java.util.ArrayList;

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
        if (j > 0) {
          System.out.print(" ");
        }
        System.out.print(locations[j]);
      }
      System.out.println();
      result.add(arrayList);
    }
    return result;
  }

  public static void main(String[] args) throws IOException {
    HdfsGetSplitInfo("hdfs:///test.txt");
  }
}
