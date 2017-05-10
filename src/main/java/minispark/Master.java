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

  HashMap<String, WorkerService.Client[]> clients;
  HashMap<WorkerService.Client, Boolean> availableMap;
  public static final int numClientsPerWorker = 4;
  public static final int sleepTime = 5000;

  Object lock;

  public static String[] workerDNSs = {
      "ip-172-31-79-126.ec2.internal",
      "ip-172-31-67-252.ec2.internal"
  };


  public Master(String address, String port) {
    try {
      lock = new Object();
      clients = new HashMap<>();

      for (String workerDNS: workerDNSs) {
        clients.put(workerDNS, new WorkerService.Client[numClientsPerWorker]);
        for (int i = 0; i < numClientsPerWorker; ++i) {
          TTransport transport = new TSocket(workerDNS, 9090);
          transport.open();
          TProtocol protocol = new  TBinaryProtocol(transport);
          clients.get(workerDNS)[i] = new WorkerService.Client(protocol);
          availableMap.put(clients.get(workerDNS)[i], true);
        }
      }
    } catch (TException x) {
      x.printStackTrace();
    }
  }

  public DoJobReply assignJob(String hostName, DoJobArgs args) throws TException {
    int index = -1;
    while (true) {
      synchronized (lock) {
        for (int i = 0; i < numClientsPerWorker; ++i) {
          if (availableMap.get(clients.get(hostName)[i])) {
            availableMap.put(clients.get(hostName)[i], false);
            index = i;
            break;
          }
        }
      }

      if (index != -1) {
        DoJobReply reply = null;
        synchronized (clients.get(hostName)[index]) {
          reply = clients.get(hostName)[index].doJob(args);
        }
        return reply;
      } else {
        try {
          Thread.sleep(sleepTime);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
