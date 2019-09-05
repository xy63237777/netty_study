package com.thesevensky.netty.thrift.mycode;

import com.thesevensky.netty.thrift.generated.PersonService;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class ThriftServer {
    public static void main(String[] args) throws Exception {
        //socket对象 表示客户端和服务端建立的socket

        TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(8899);
        THsHaServer.Args arg = new THsHaServer.Args(serverSocket).minWorkerThreads(2).maxWorkerThreads(4);
        PersonService.Processor<PersonServiceImpl> processor =
                new PersonService.Processor<>(new PersonServiceImpl());

        //下面是几个比较重要的工厂对象 而且和Client相对应
        //传输的时候协议一样 才可以成功
        //属于协议层TCompactProtocol 二进制压缩协议
        //还有TBinaryProtocol 二进制格式
        //TJSONProtocol json格式 等等
        arg.protocolFactory(new TCompactProtocol.Factory());
        //表示的是传输层需要的对象
        //TFileTransport 文件形式进行传输
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        //HsHa 表示半同步半异步
        //TNonblockingServer多线程服务模型 使用非阻塞IO 需使用TFramedTransport
        /**
         * An extension of the TNonblockingServer to a Half-Sync/Half-Async server.
         * Like TNonblockingServer, it relies on the use of TFramedTransport.
         */
        TServer server = new THsHaServer(arg);

        System.out.println("Thrift Server Started!");

        server.serve();
    }
}
