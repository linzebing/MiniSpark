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

public class Master {

  // TODO: use multiple clients
  WorkerService.Client client;


  public Master(String address, String port) {
    try {
      TTransport transport = new TSocket("localhost", 9090);
      transport.open();

      TProtocol protocol = new  TBinaryProtocol(transport);
      client = new WorkerService.Client(protocol);

      //transport.close();
    } catch (TException x) {
      x.printStackTrace();
    }
  }

  public void assignJob(String hostName, DoJobArgs args,  DoJobReply reply) throws TException {
    System.out.println(args.toString());
    System.out.println(reply.toString());
    client.doJob(args, reply);
  }
}
