package minispark;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import tutorial.StringNumPair;
import tutorial.WorkerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lzb on 5/4/17.
 */
public class Worker {
    private static final String[] workerDNSs = {
            "ip-172-31-75-241.ec2.internal",
            "ip-172-31-64-149.ec2.internal",
            "ip-172-31-69-142.ec2.internal",
    };
    private static HashMap<String, WorkerService.Client> clients;
    private static WorkerServiceHandler handler;
    private static WorkerService.Processor processor;

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
        Thread.sleep(10000);

        for (String workerDNS : workerDNSs) {
            TTransport transport = new TSocket(workerDNS, 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            clients.put(workerDNS, new WorkerService.Client(protocol));
        }
    }

    public static ArrayList<StringNumPair> readPartitions(List<Integer> inputIds,
            List<String> inputHostNames) throws TException {
        assert inputIds.size() == inputHostNames.size();
        int size = inputIds.size();
        ArrayList<StringNumPair> everything = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            synchronized (clients.get(inputHostNames.get(i))) {
                everything.addAll(clients.get(inputHostNames.get(i)).readPartition(inputIds.get(i)));
            }
        }
        return everything;
    }

    private static void simple(WorkerService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);

            // Use this for a multithreaded server
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
