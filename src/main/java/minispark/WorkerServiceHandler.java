package minispark;

/**
 * Created by lzb on 5/4/17.
 */

import org.apache.thrift.TException;
import tutorial.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class WorkerServiceHandler implements WorkerService.Iface {
  public static HashMap<Integer, Object> hashMap = new HashMap<Integer, Object>();

  public void doJob(DoJobArgs args, DoJobReply reply) {
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
          return;
        } else {
          try {
            hashMap.put(args.partitionId, HdfsSplitReader.HdfsSplitRead(args.filePath, args.hdfsSplitId));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        break;
    }
  }
}
