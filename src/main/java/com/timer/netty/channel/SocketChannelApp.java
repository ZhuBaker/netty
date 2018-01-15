package com.timer.netty.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-15
 * Time: 15:00
 */
public class SocketChannelApp {

    public static void main(String[] args) throws Exception{
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1",9990);
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(addr);
        while (!sc.finishConnect()){
            doSomethings();
        }
        if(sc.isConnectionPending()){//如果是并发连接异步，并且还正在连接时(连接还没完成时) , 返回true
            System.out.println("isConnectionPending");
        }
        if(sc.isConnected()){
            System.out.println("isConnected");
        }
        ByteBuffer buffer = ByteBuffer.wrap(new String("Hello server !").getBytes());
        sc.write(buffer);
        sc.close();
    }
    private static void doSomethings(){
        System.out.println("do someing useless ! ");
    }
}
