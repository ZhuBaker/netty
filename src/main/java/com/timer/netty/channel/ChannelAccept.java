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
 * Time: 14:00
 */
public class ChannelAccept {
    private static final String GREETING="Hello I must be going.\r\n";

    public static void main(String[] args) throws Exception{
        int port = 1234;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        while(true){
            System.out.println("Waiting for connections");
            SocketChannel sc = ssc.accept();
            if(sc == null){
                System.out.println("==========Sleeping==========");
                Thread.sleep(2000);
            }else{
                System.out.println("Incoming connection from : " +
                sc.socket().getRemoteSocketAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }
}
