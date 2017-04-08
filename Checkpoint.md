**Checkpoint**

Team member: Zebing Lin (zebingl)

# Progress Review
  Have finished a runnable single-threaded version that can run the following codes:

```java
SparkContext sc = new SparkContext("Example");
Rdd lines = sc.textFile("webhdfs://ec2-34-205-85-106.compute-1.amazonaws.com/test.txt");
List<String> output = lines.collect();
for (String line: output) {
  System.out.println(line);
}
```

  To be more specific, I designed the modules of MiniSpark, and finished the lineage tracking and rdd transformation. On the scheduler side, I completed logic of traversing the RDD DAG and issuing RPC calls to workers. On the worker side, currently I only implemented the *TextFile* and *Collect* operator, and can respond to remote RPC calls.

  RPC Communication is enabled by [Apache Thrift](https://thrift.apache.org/).

# Exhibits for the Parallelism Competition
  A small demo can be expected to show the functionality of MiniSpark, and various performance graphs of different applications compared to Spark will be shown as well.

# Remaining issues
  Mainly on how to leverage parallelism on both the scheduler side and worker side, with consideration of locality.

# Schedule (Updated on Apr. 25)
## By Apr. 30th
  Finish *Map*, *FlatMap*, *Reduce* and *ReduceByKey* on the worker side. Get sample apps running.

## By May. 5th
  Finish basic parallelism on both scheduler and workers.

## By May. 8th
  Optimize job assignments and scheduling.

## By May. 11th
  Benchmark and write report.
