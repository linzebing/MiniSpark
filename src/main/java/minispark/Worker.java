package minispark;

import minispark.Common.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

/**
 * Created by lzb on 5/4/17.
 */
public class Worker {

  public static HashMap<Integer, Object> hashMap;

  public void doJob(DoJobArgs args, DoJobReply reply) throws IOException {
    switch (args.workerOpType) {
      case GetSplit:
        if (hashMap.containsKey(args.partitionId)) {
          reply.lines = (ArrayList<String>) hashMap.get(args.partitionId);
        } else {
          // TODO: I don't know

        }
        break;
      case ReadHDFSSplit:
        if (hashMap.containsKey(args.partitionId)) {
          // Already in memory
          return;
        } else {
          hashMap.put(args.partitionId, HdfsSplitReader.HdfsSplitRead(args.filePath, args.hdfsSplitId));
        }
        break;
    }
  }

  public static void main(String[] args) {

  }
}
