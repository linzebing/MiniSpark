package minispark;

import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;
import java.lang.reflect.*;

/**
 * Created by lzb on 4/16/17.
 */
public class App {

  public static String mapTest(String s) {
    return s + s;
  }

  public static void main(String[] args) throws IOException, TException {
    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("webhdfs://ec2-34-201-31-106.compute-1.amazonaws.com/test.txt");
    Rdd doubleLines = lines.map("mapMethod");
    List<String> output = lines.collect();


    for (String line: output) {
      System.out.println(line);
    }
  }
}
