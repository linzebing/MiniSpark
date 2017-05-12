package minispark;

import org.apache.thrift.TException;
import tutorial.StringNumPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lzb on 4/16/17.
 */
public class App {
  /******* WordCount begins *******/
  public static String toLower(String s) {
    return s.toLowerCase();
  }

  public static ArrayList<String> flatMapTest(String s) {
    return new ArrayList<String>(Arrays.asList(s.split(" ")));
  }

  public static double add(double a, double b) {
    return a + b;
  }

  public static boolean InstagramOnly(String s) {
    return s.endsWith("15618") || s.startsWith("15618");
  }

  public static void WordCount() throws IOException, TException {
    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("webhdfs://ec2-54-208-160-33.compute-1.amazonaws.com/test.txt").flatMap("flatMapTest")
        .map("toLower").filter("InstagramOnly").mapPair("mapCount").reduceByKey("add");
    Long start = System.currentTimeMillis();
    List<StringNumPair> output = (List<StringNumPair>) lines.collect();
    for (StringNumPair pair: output) {
      System.out.println(pair.str + " " + (int) pair.num);
    }
    Long end = System.currentTimeMillis();
    System.out.println("Time elapsed: " + (end - start) / 1000 + "seconds");
    sc.stop();
  }

  public static StringNumPair mapCount(String s) {
    return new StringNumPair(s, 1);
  }

  /******* WordCount ends *******/

  /******* SparkPi begins *******/
  public static StringNumPair monteCarlo(String s) {
    //int total = 250000000;
    int total = 25000;
    int cnt = 0;
    for (int i = 0; i < total; ++i) {
      double x = Math.random();
      double y = Math.random();
      if (x * x + y * y < 1) {
        ++cnt;
      }
    }
    return new StringNumPair(s, 4.0 * cnt / total);
  }

  public static void SparkPi() throws IOException, TException {
    int NUM_SAMPLES = 8;
    SparkContext sc = new SparkContext("SparkPi");
    ArrayList<String> l = new ArrayList<>(NUM_SAMPLES);
    for (int i = 0; i < NUM_SAMPLES; ++i) {
      l.add(String.valueOf(i));
    }
    Long start = System.currentTimeMillis();
    double sum = sc.parallelize(l).mapPair("monteCarlo").reduce("add");
    Long end = System.currentTimeMillis();
    System.out.println("Estimation of pi is: " + sum / NUM_SAMPLES);
    System.out.println("Time elapsed: " + (end - start) / 1000 + "seconds");
    sc.stop();
  }
  /******* SparkPi ends *******/


  public static void main(String[] args) throws IOException, TException {
    //SparkPi();
    WordCount();
  }
}