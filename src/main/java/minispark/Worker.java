package minispark;

import minispark.Common.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import tutorial.StringIntPair;
import tutorial.WorkerService;

/**
 * Created by lzb on 5/4/17.
 */
public class Worker {

  public static String masterDNS = "ip-172-31-67-73.ec2.internal";

  public static HashMap<String, WorkerService.Client> clients;

  public static String[] workerDNSs = {
      "ip-172-31-38-114.ec2.internal",
      "ip-172-31-38-42.ec2.internal"
  };

  public static WorkerServiceHandler handler;
  public static WorkerService.Processor processor;

  public static void main(String[] args) throws TTransportException, InterruptedException {
    clients = new HashMap<>();
    handler = new WorkerServiceHandler();
    processor = new WorkerService.Processor(handler);
    Runnable simple = new Runnable() {
      public void run() {
        simple(processor);
      }
    };

    new Thread(simple).start();
    Thread.sleep(5000);

    for (String workerDNS: workerDNSs) {
      TTransport transport = new TSocket(workerDNS, 9090);
      transport.open();
      TProtocol protocol = new  TBinaryProtocol(transport);
      clients.put(workerDNS, new WorkerService.Client(protocol));
    }
  }

  public static ArrayList<StringIntPair> readPartitions(List<Integer> inputIds, List<String> inputHostNames) throws TException {
    // TODO: choose client according to hostName
    assert inputIds.size() == inputHostNames.size();
    int size = inputIds.size();
    ArrayList<StringIntPair> everything = new ArrayList<>();
    for (int i = 0; i < size; ++i) {
      synchronized (clients.get(inputHostNames.get(i))) {
        everything.addAll(clients.get(inputHostNames.get(i)).readPartition(inputIds.get(i)));
      }
    }
    return everything;
  }

  public static void simple(WorkerService.Processor processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(9090);
      //TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

      // Use this for a multithreaded server
      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

      System.out.println("Starting the simple server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
