package com.timer.netty.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-15
 * Time: 15:04
 */
public class ServerSocketChannelApp {

    private static final String MSG = "hello , I must be going \n";

    public static void main(String[] args) throws Exception {
        int port = 9990;
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));
        ByteBuffer buffer = ByteBuffer.wrap(MSG.getBytes());

        while (true){
            SocketChannel sc = ssc.accept();
            if(sc == null){
                Thread.sleep(1000);
            }else{
                System.out.println("Incoming connection from " + sc.socket().getRemoteSocketAddress());
                ByteBuffer readerBuffer = ByteBuffer.allocate(1024);
                sc.read(readerBuffer);
                readerBuffer.flip();
                out(readerBuffer);
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }

    private static void out(ByteBuffer readBuffer){
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ; i < readBuffer.limit() ; i++){
            char c = (char) readBuffer.get();
            sb.append(c);
        }
        System.out.println(sb.toString());
    }
}
