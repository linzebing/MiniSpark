package minispark;

import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzb on 4/16/17.
 */
public class App {
  public static void main(String[] args) throws IOException, TException {
    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("webhdfs://ec2-34-200-250-79.compute-1.amazonaws.com/test.txt");
    List<String> output = lines.collect();


    for (String line: output) {
      System.out.println(line);
    }
  }
}
