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



  public DoJobReply doJob(DoJobArgs args) {
    DoJobReply reply = new DoJobReply();
    switch (args.workerOpType) {
      case GetSplit:
        if (hashMap.containsKey(args.partitionId)) {
          reply.lines = (ArrayList<String>) hashMap.get(args.partitionId);
        } else {
          // TODO: I don't know

        }
        break;
      case ReadHdfsSplit:
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
        if (hashMap.containsKey(args.partitionId)) {
          reply.lines = (ArrayList<String>) hashMap.get(args.partitionId);
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
      case FlatMapJob:
        if (hashMap.containsKey(args.partitionId)) {
          reply.lines = (ArrayList<String>) hashMap.get(args.partitionId);
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
    }
    return reply;
  }
}
