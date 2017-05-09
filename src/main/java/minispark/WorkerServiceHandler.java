package minispark;

/**
 * Created by lzb on 5/4/17.
 */

import org.apache.thrift.TException;
import tutorial.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WorkerServiceHandler implements WorkerService.Iface {
  public static HashMap<Integer, Object> hashMap = new HashMap<Integer, Object>();

  public ArrayList<StringIntPair> readPartition(int partitionId) {
    System.out.println("Received readPartition");
    assert hashMap.containsKey(partitionId);
    return (ArrayList<StringIntPair>)hashMap.get(partitionId);
  }

  public DoJobReply doJob(DoJobArgs args) throws TException {
    DoJobReply reply = new DoJobReply();
    switch (args.workerOpType) {
      case GetSplit:
        System.out.println("Received GetSplit");
        if (hashMap.containsKey(args.partitionId)) {
          reply.lines = (ArrayList<String>) hashMap.get(args.partitionId);
        } else {
          // TODO: I don't know
          System.out.println("GetSplit Exception");
        }
        break;
      case GetPairSplit:
        System.out.println("Received GetPairSplit");
        if (hashMap.containsKey(args.partitionId)) {
          reply.pairs = (ArrayList<StringIntPair>) hashMap.get(args.partitionId);
        } else {
          // TODO: I don't know
          System.out.println("GetPairSplit Exception");
        }
        break;
      case ReadHdfsSplit:
        System.out.println("Received ReadHdfsSplit");
        if (hashMap.containsKey(args.partitionId)) {
          // Already in memory
        } else {
          try {
            hashMap.put(args.partitionId, HdfsSplitReader.HdfsSplitRead(args.filePath, args.hdfsSplitId));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        break;
      case MapJob:
        System.out.println("Received MapJob");
        if (hashMap.containsKey(args.partitionId)) {
          // reply.lines = (ArrayList<String>) hashMap.get(args.partitionId);
        } else {
          try {
            Method method = App.class.getMethod(args.funcName, String.class);
            ArrayList<String> input = (ArrayList<String>) hashMap.get(args.inputId);
            ArrayList<String> output = new ArrayList<>();
            for (String str: input) {
              output.add((String) method.invoke(null, str));
            }
            hashMap.put(args.partitionId, output);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        break;
      case MapPairJob:
        System.out.println("Received MapPairJob");
        if (hashMap.containsKey(args.partitionId)) {
          // reply.pairs = (ArrayList<StringIntPair>) hashMap.get(args.partitionId);
        } else {
          try {
            Method method = App.class.getMethod(args.funcName, String.class);
            ArrayList<String> input = (ArrayList<String>) hashMap.get(args.inputId);
            ArrayList<StringIntPair> output = new ArrayList<>();
            for (String str: input) {
              output.add((StringIntPair) method.invoke(null, str));
            }
            hashMap.put(args.partitionId, output);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        break;
      case FlatMapJob:
        System.out.println("Received FlatMapJob");
        if (hashMap.containsKey(args.partitionId)) {
          // reply.lines = (ArrayList<String>) hashMap.get(args.partitionId);
        } else {
          try {
            Method method = App.class.getMethod(args.funcName, String.class);
            ArrayList<String> input = (ArrayList<String>) hashMap.get(args.inputId);
            ArrayList<String> output = new ArrayList<>();
            for (String str: input) {
              output.addAll((List<String>) method.invoke(null, str));
            }
            hashMap.put(args.partitionId, output);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        break;
      case HashSplit:
        System.out.println("Received HashSplit");
        // Check if already in memory first
        boolean flag = true;
        int size = args.shufflePartitionIds.size();
        for (int shufflePartitionId: args.shufflePartitionIds) {
          if (!hashMap.containsKey(shufflePartitionId)) {
            flag = false;
          }
        }
        if (flag) {
          // Already in memory
          break;
        }
        ArrayList<StringIntPair>[] hashedResults = new ArrayList[size];
        for (StringIntPair pair: (ArrayList<StringIntPair>) hashMap.get(args.inputId)) {
          hashedResults[pair.str.hashCode() % size].add(pair);
        }
        for (int i = 0; i < size; ++i) {
          hashMap.put(args.shufflePartitionIds.get(i), hashedResults[i]);
        }
        break;
      case ReduceByKeyJob:
        System.out.println("Received ReduceByKeyJob");
        if (hashMap.containsKey(args.partitionId)) {
          // Output already exists
        } else {
          ArrayList<StringIntPair> lines = Worker.readPartitions(args.inputIds, args.inputHostNames);
          HashMap<String, ArrayList<Integer>> kvStore = new HashMap<>();
          for (StringIntPair pair: lines) {
            if (kvStore.containsKey(pair.str)) {
              kvStore.get(pair.str).add(pair.num);
            } else {
              ArrayList<Integer> arrayList = new ArrayList<>();
              arrayList.add(pair.num);
              kvStore.put(pair.str, arrayList);
            }
          }
          ArrayList<StringIntPair> output = new ArrayList<>();
          try {
            Method method = App.class.getMethod(args.funcName, String.class);
            for (String key: kvStore.keySet()) {
              output.add(new StringIntPair(key, reduceHelper(method, kvStore.get(key))));
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          hashMap.put(args.partitionId, output);
        }
    }
    return reply;
  }

  public static int reduceHelper(Method method, ArrayList<Integer> values) {
    assert !values.isEmpty();
    if (values.size() == 1) {
      return values.get(0);
    } else {
      int initVal = values.get(0);
      for (int i = 1; i < values.size(); ++i) {
        try {
          initVal = (int) method.invoke(null, initVal, values.get(i));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return initVal;
    }
  }
}
