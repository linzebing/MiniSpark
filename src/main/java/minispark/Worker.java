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
import tutorial.WorkerService;

/**
 * Created by lzb on 5/4/17.
 */
public class Worker {

  public static WorkerServiceHandler handler;
  public static WorkerService.Processor processor;

  public static void main(String[] args) {
    handler = new WorkerServiceHandler();
    processor = new WorkerService.Processor(handler);
    Runnable simple = new Runnable() {
      public void run() {
        simple(processor);
      }
    };

    new Thread(simple).start();
  }

  public static void simple(WorkerService.Processor processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(9090);
      TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

      // Use this for a multithreaded server
      // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

      System.out.println("Starting the simple server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
