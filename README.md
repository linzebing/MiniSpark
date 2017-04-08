---
layout: page
title: MiniSpark
permalink: /MiniSpark/
---

Team member: Zebing Lin (zebingl)

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

## Fault Tolerance (if time permits)
If partions of RDDs get lost, they have to be recomputed according to the lineage information.
If a worker crashes, the master has to reassign the jobs to other workers.
What's more, master may also fail, RPC calls may timeout. How to effectively deal with failures is a big challenge.

# Resources
I will start from scratch to build MiniSpark. Thanks to projects in 15619 and 15719, I'm very familiar with Spark usage, and HDFS as well.
However, it's still substantial efforts for a single-member team, therefore, I will
refer to the source code of [Apache Spark](https://github.com/apache/spark) (which is mainly written in Scala)
for high-level design decision making. I will also refer to [Phoenix](https://github.com/kozyraki/phoenix), which
is an implementation of MapReduce for shared memory systems.

Also, the following papers are valuable references that elaborate the ideas behind Spark.

[1] Zaharia M, Chowdhury M, Franklin M J, et al. Spark: Cluster Computing with Working Sets[J]. HotCloud, 2010, 10(10-10): 95.

[2] Zaharia M, Chowdhury M, Das T, et al. Resilient distributed datasets: A fault-tolerant abstraction for in-memory cluster computing[C]//Proceedings of the 9th USENIX conference on Networked Systems Design and Implementation. USENIX Association, 2012: 2-2.

AWS is an ideal platform for me as I can quickly set up HDFS on multiple nodes. Also the root privilege will allow me to configure instances as I wish.

# Goals and Deliverables

## Plan to Achieve
- Support operators including *Map*, *FlatMap*, *Reduce*, *ReduceByKey*, *Filter* and *Collect*.
- Provide easy-to-use APIs to application programmers and able to operate on a cluster.
- Design intelligent scheduling strategies and make full use of the cores of each node.
- Achieve comparable (or even better) performance as Apache Spark on sample application tests, like word counts, histograms, PageRank and logistic regression.


## Hope to Achieve (if time permits)
- Support configuration of reading from local files and communicating using shared memory instead of network messages when operating on a single node.
- Support more complicated operators.
- Support RDD fault tolerance and worker fault tolerance.

# Platform
I plan to use Java for this project. While C++ is more performant than Java, considering that I work alone, Java enables
faster development of prototypes. Also, as Apache Spark is implemented in Scala, the benchmark comparison will be more fair
as they are both JVM-based languages.

# Schedule
I have around four weeks to do this project.
## Week1
- Design APIs and overall architecture of MiniSpark.

## Week2
- Implement workers for forementioned operators.

## Week3
- Implement a scheduler with high performance.

## Week4
- Experiment implementation on AWS and adjust scheduling strategies.
- Benchmark with Apache Spark implementation.

## Rest
- Write final report.
