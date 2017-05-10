---
layout: page
title: MiniSpark
permalink: /MiniSpark/
---

Team member: Zebing Lin (zebingl)

# Final

## Summary
  I implemented a mini Spark-like framework named MiniSpark that can run on top of a HDFS cluster. MiniSpark supports operators including
*Map*, *FlatMap*, *MapPair*, *Reduce*, *ReduceByKey*, *Collect*, *Count*, *Parallelize* and *Filter*. The Github repo is [https://github.com/linzebing/MiniSpark](https://github.com/linzebing/MiniSpark).

  TODO: support for *saveAsTextFile*, *sc.stop()* (which essentially frees the workers' memory), optimization of scheduling and more extensive evaluation are still ongoing.

## Technical Challenges

### Cross-node Communication
Substantial communication is involved in Spark execution. For instance, master/scheduler has to communcate with workers to
make scheduling and task assignment decisions, master has to pass function literals to workers, workers may read data from other workers'
local memory. How to elegantly design APIs to enable these communications using RPC calls is a challenge.

Personally I used Apache Thrift to enable RPC communication. Thrift server can be declared to be multithreaded, which enables efficient use of multi cores on the server side. However, thrift client is not thread-safe, therefore it's desirable to set up serveral clients in advance depending on the number of cores on master, and apply synchronization accordingly when being called.

### Module Design
Although current implementation is only 1K lines of code (thrift-generated code doesn't count), it's substantial efforts to design a distributed program that runs on a cluster.

### Task Scheduling
When assigning a job, the master should select an available worker based on HDFS data locality, CPU utilization and current running
jobs into consideration. How to make smart scheduling decisions based on these information is essential to MiniSpark's performance,
therefore is the major challenge of this project.

Personally, my current implementation exploits data-parallelism on computation of narrowlly dependent RDDs. Schduler keeps track of current running jobs on the workers, and will try to allocate jobs to the least loaded server when possible.


## Preliminary Results
  MiniSpark is able to run data analytics applications parallelly and distributedly on top of a HDFS cluster. Current evaluation on the following applications shows better performance than python-written programs that implements the same application, but slightly worse than scala-written programs. More intensive evaluation is ongoing.

  A sample program that counts the words that starts/ends with "**instagram**" (case insensitive) is like the following. On a 2-worker (r3.xlarge) setting on a 440MB text [file](https://commoncrawl.s3.amazonaws.com/crawl-data/CC-MAIN-2016-50/segments/1480698540409.8/wet/CC-MAIN-20161202170900-00000-ip-10-31-129-80.ec2.internal.warc.wet.gz), MiniSpark used around 16 seconds while scala-written program used 10 seconds and python-written program used 45 seconds.
```java
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

    SparkContext sc = new SparkContext("Example");
    Rdd lines = sc.textFile("webhdfs://ec2-34-201-24-238.compute-1.amazonaws.com/test.txt").flatMap("flatMapTest")
        .map("mapTest").filter("InstagramOnly").mapPair("mapCount").reduceByKey("reduceByKeyTest");
    List<StringIntPair> output = (List<StringIntPair>) lines.collect();
    for (StringIntPair pair: output) {
      System.out.println(pair.str + " " + pair.num);
    }
    sc.stop();
```

  A sample program that estimates π by "throwing darts" at a circle is like the following:
```java
  SparkContext sc = new SparkContext("Example");
  int NUM_SAMPLES = 20170510;
  ArrayList<String> l = new ArrayList<>();
  for (int i = 0; i < NUM_SAMPLES; ++i) {
    l.add(String.valueOf(i));
  }
  System.out.println("Pi is roughly " + 4.0 * sc.parallelize(l).filter("monteCarlo").count() / NUM_SAMPLES);
  sc.stop();
```
## Final Numbers to Show
  Should be able to run a small demo then.

  More performance numbers to compare against Spark's performance on the same application under the same cluster configuration.

  More performance numbers to test MiniSpark's scalability, i.e., how its performance changes on the same application from 2 workers to
16 workers.

# Schedule (Updated on May 10th)
## By Apr. 30th (Done)
  Finish *Map*, *FlatMap*, *Reduce* and *ReduceByKey* on the worker side. Get sample apps running.

## By May. 5th (Done)
  Finish basic parallelism on both scheduler and workers.

## By May. 8th (Still ongoing)
  Optimize job assignments and scheduling.

## By May. 11th
  Benchmark and write report.

# Summary
Java implementation of the fast in-memory cluster computing framework [Spark](http://spark.apache.org/), supporting
a subset of Spark's operators, like *Map*, *FlatMap*, *Reduce*, *ReduceByKey*, *Collect*, etc.

# Background
Google's MapReduce has been very successful in implementing large-scale data-intensive applications, however it's
deficient in iterative jobs/analytics, due to the fact that MapReduce has to write intermeidate results to HDFS, incurring
sigificant performance penalty. By introducing the concepts of *resilient distributed dataset* (RDD)[1, 2], Spark reuses
working sets of data across multiple parallel operations, therefore enhancing the overall performance.

# The Challenge
## Lineage Tracking
Spark uses the notion of lineage to track dependencies of tasks and relies on it for reconstructing partitions of RDDs
for fault tolerance. When a RDD action is triggered, the scheduler has to examine the full lineage of RDDs related to
the target and identifies the execution order. How to elegantly represent the lineages and trace the RDDs is a challenge.

## Task Scheduling
When assigning a job, the master should select an available worker based on HDFS data locality, CPU utilization and current running
jobs into consideration. How to make smart scheduling decisions based on these information is essential to Spark's performance,
therefore is the major challenge of this project.

## Cross-node Communication
Substantial communication is involved in Spark execution. For instance, master/scheduler has to communcate with workers to
make scheduling and task assignment decisions, master has to pass function literals to workers, workers may read data from other workers'
local memory. How to elegantly design APIs to enable these communications using RPC calls is a challenge.

# Resources
I will start from scratch to build MiniSpark. Thanks to projects in 15619 and 15719, I'm very familiar with Spark usage, and HDFS as well. I will mainly refer to [Spark Documentation](http://spark.apache.org/docs/latest/programming-guide.html) to familarize myself with Spark's API interfaces.

Also, the following papers are valuable references that elaborate the ideas behind Spark.

[1] Zaharia M, Chowdhury M, Franklin M J, et al. Spark: Cluster Computing with Working Sets[J]. HotCloud, 2010, 10(10-10): 95.

[2] Zaharia M, Chowdhury M, Das T, et al. Resilient distributed datasets: A fault-tolerant abstraction for in-memory cluster computing[C]//Proceedings of the 9th USENIX conference on Networked Systems Design and Implementation. USENIX Association, 2012: 2-2.

AWS is an ideal platform for me as I can quickly set up HDFS on multiple nodes. Also the root privilege will allow me to configure instances as I wish.

# Goals and Deliverables

## Plan to Achieve
- Support operators including *Map*, *FlatMap*, *Reduce*, *ReduceByKey*, *Filter* and *Collect*.
- Provide easy-to-use APIs to application programmers and able to operate on a cluster.
- Design intelligent scheduling strategies and make full use of the cores of each node.

## Hope to Achieve (if time permits)
- Support configuration of reading from local files and communicating using shared memory instead of network messages when operating on a single node.
- Support more complicated operators.
- Support RDD fault tolerance and worker fault tolerance.

## Evaluation Plan
- Achieve **comparable performance** as Apache Spark on sample applications, like word counts, histograms, PageRank and logistic regression.
- Achieve **better performance** than Spark on a single node (data locality + communication using shared memory should be better).

# Platform
I plan to use Java for this project. While C++ is more performant than Java, considering that I work alone, Java enables
faster development of prototypes. Also, as Apache Spark is implemented in Scala, the benchmark comparison will be more fair
as they are both JVM-based languages.
