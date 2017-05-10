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

  public static boolean filterTest(String s) {
    return (s.hashCode() % 2 == 1);
  }

  public static String mapTest(String s) {
    return s.toLowerCase();
  }

  public static ArrayList<String> flatMapTest(String s) {
    return new ArrayList<String>(Arrays.asList(s.split(" ")));
  }

  public static int reduceByKeyTest(int a, int b) {
    return a + b;
  }

  public static StringIntPair mapCount(String s) {
    return new StringIntPair(s, 1);
  }

  public static boolean InstagramOnly(String s) {
    return s.endsWith("instagram") || s.startsWith("instagram");
  }

  public static void wordCount() throws IOException, TException {
    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("webhdfs://ec2-34-201-24-238.compute-1.amazonaws.com/test.txt").flatMap("flatMapTest")
        .map("mapTest").filter("InstagramOnly").mapPair("mapCount").reduceByKey("reduceByKeyTest");
    List<String> output = (List<String>) lines.collect();
    for (String pair: output) {
      System.out.println(pair.toString());
    }
  }

  public static void main(String[] args) throws IOException, TException {
    wordCount();
    // SparkContext sc = new SparkContext("Example");
    /*
    Rdd lines = sc.textFile("webhdfs://ec2-34-201-24-238.compute-1.amazonaws.com/test.txt");
    Rdd pairs = lines.flatMap("flatMapTest").map("mapTest");

    System.out.println("Count result: " + pairs.mapPair("mapCount").reduceByKey("reduceByKeyTest").count());

    Rdd filteredPair = pairs.filter("filterTest");

    System.out.println("FIltered Count result: " + pairs.count());

    List<String> output = (List<String>) filteredPair.collect();

    for (String pair: output) {
      System.out.println(pair.toString());
    }

    System.out.println(pairs.mapPair("mapCount").reduce("reduceByKeyTest"));
    */
  }
}
