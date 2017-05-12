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
  /******* Histogram begins *******/
  public static ArrayList<String> extractLetters(String s) {
    ArrayList<String> result = new ArrayList<>();
    for (int i = 0; i < s.length(); ++i) {
      char ch = s.charAt(i);
      if ('A' <= ch && ch <= 'Z') {
        result.add(String.valueOf(ch + ('a' - 'A')));
      } else if ('a' <= ch && ch <= 'z') {
        result.add(String.valueOf(ch));
      }
    }
    return result;
  }

  public static double add(double a, double b) {
    return a + b;
  }

  public static StringNumPair mapCount(String s) {
    return new StringNumPair(s, 1);
  }

  public static void Histogram() throws IOException, TException {
    SparkContext sc = new SparkContext("WordCount");
    Rdd lines = sc.textFile("webhdfs://ec2-54-208-160-33.compute-1.amazonaws.com/test.txt")
        .flatMap("extractLetters")
        .mapPair("mapCount")
        .reduceByKey("add");

    Long start = System.currentTimeMillis();
    List<StringNumPair> output = (List<StringNumPair>) lines.collect();
    for (StringNumPair pair: output) {
      System.out.println(pair.str + " " + pair.num);
    }
    Long end = System.currentTimeMillis();
    System.out.println("Time elapsed: " + (end - start) / 1000 + "seconds");
    sc.stop();
  }
  /******* Histogram ends *******/

  /******* SparkPi begins *******/
  public static StringNumPair monteCarlo(String s) {
    int total = 10000;
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
    int NUM_SAMPLES = 20000;
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
    SparkPi();
  }
}