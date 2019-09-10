package com.thesevensky.netty.zerocopy;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

public class OldIOClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8899);
        String fileName = "/opt/goland-2019.1.3.tar.gz";
        InputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0L;

        long startTime = System.currentTimeMillis();
        while((readCount = inputStream.read(buffer) )>= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("发送总字节数: " + total + " , 耗时: " + (System.currentTimeMillis() - startTime) + "毫秒");
        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
