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
    List<StringIntPair> output = (List<StringIntPair>) lines.collect();
    for (StringIntPair pair: output) {
      System.out.println(pair.str + " " + pair.num);
    }
  }

  public static boolean monteCarlo() {
    double x = Math.random();
    double y = Math.random();
    return x * x + y * y < 1;
  }

  public static void calcPi() throws IOException, TException {
    SparkContext sc = new SparkContext("Example");
    int NUM_SAMPLES = 20170510;
    ArrayList<String> l = new ArrayList<>();
    for (int i = 0; i < NUM_SAMPLES; ++i) {
      l.add(String.valueOf(i));
    }
    System.out.println("Pi is roughly " + 4.0 * sc.parallelize(l).filter("monteCarlo").count() / NUM_SAMPLES);
    sc.stop();
  }

  public static void main(String[] args) throws IOException, TException {
    Long start = System.currentTimeMillis();
    //wordCount();
    calcPi();
    Long end = System.currentTimeMillis();
    System.out.println("Used " + (end - start) / 1000 + " seconds");

  }
}
