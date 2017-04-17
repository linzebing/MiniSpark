package minispark;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzb on 4/16/17.
 */
public class App {
  public static void main(String[] args) throws IOException {
    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("hdfs://ec2-34-200-235-149.compute-1.amazonaws.com:54310/test.txt");
    List<String> output = lines.collect();
    for (String line: output) {
      System.out.println(line);
    }
  }
}
