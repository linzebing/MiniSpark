package minispark;

import tutorial.*;

/**
 * Created by lzb on 4/16/17.
 */

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import tutorial.WorkerService;

import java.util.HashMap;

public class Master {

  // TODO: use multiple clients
  HashMap<String, WorkerService.Client> clients;

  public static String[] workerDNSs = {
      "ip-172-31-79-126.ec2.internal",
      "ip-172-31-67-252.ec2.internal"
  };


  public Master(String address, String port) {
    try {
      clients = new HashMap<>();

      for (String workerDNS: workerDNSs) {
        TTransport transport = new TSocket(workerDNS, 9090);
        transport.open();
        TProtocol protocol = new  TBinaryProtocol(transport);
        clients.put(workerDNS, new WorkerService.Client(protocol));
      }
    } catch (TException x) {
      x.printStackTrace();
    }
  }

  public DoJobReply assignJob(String hostName, DoJobArgs args) throws TException {
    //System.out.println(args.toString());
    DoJobReply reply = clients.get(hostName).doJob(args);
    //System.out.println(reply.toString());

    return reply;
  }
}
