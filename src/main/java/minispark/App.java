package minispark;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzb on 4/16/17.
 */
public class App {
  public static void main(String[] args) throws IOException {
    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("webhdfs://ec2-34-205-85-106.compute-1.amazonaws.com/rel-tweets-hashtags-tags.csv");
    List<String> output = lines.collect();
    /*for (String line: output) {
      System.out.println(line);
    }*/
  }
}
