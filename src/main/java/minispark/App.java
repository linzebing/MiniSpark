package minispark;

import org.apache.thrift.TException;
import tutorial.StringIntPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lzb on 4/16/17.
 */
public class App {

  public static String mapTest(String s) {
    return s.toLowerCase();
  }

  public static ArrayList<String> flatMapTest(String s) {
    return new ArrayList<String>(Arrays.asList(s.split(" ")));
  }

  public static StringIntPair mapCount(String s) {
    return new StringIntPair(s, 1);
  }

  public static void main(String[] args) throws IOException, TException {
    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("webhdfs://ec2-34-201-31-106.compute-1.amazonaws.com/test.txt");
    Rdd pairs = lines.flatMap("flatMapTest").map("mapTest").mapPair("mapCount");

    List<StringIntPair> output = (List<StringIntPair>) pairs.collect();

    for (StringIntPair pair: output) {
      System.out.println(pair.toString());
    }

    //List<StringIntPair> output = (List<StringIntPair>) pairs.collect();

    //for (StringIntPair pair: output) {
      //System.out.println(pair.str + " " + pair.num);
    //}
  }
}
