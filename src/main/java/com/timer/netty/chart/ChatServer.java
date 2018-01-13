package com.timer.netty.chart;

import org.jboss.netty.channel.ServerChannel;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-13
 * Time: 15:51
 */
public class ChatServer implements Runnable{

    private Selector selector;
    private SelectionKey selectionKey;
    private Vector<String> username;
    private static final int PORT = 9999;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ChatServer() {
        username = new Vector<>();
        init();
    }
    public void init(){
        try{
            selector = Selector.open();
            //创建serverSocketChannel
            ServerSocketChannel serverChannel =  ServerSocketChannel.open();
            ServerSocket socket = serverChannel.socket();
            socket.bind(new InetSocketAddress(PORT));
            //加入到selector中
            serverChannel.configureBlocking(false);
            selectionKey = serverChannel.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("server starting.......");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            while(true){
                int count = selector.select();
                if(count > 0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            System.out.println(key.toString() + " : 接收");
                            //一定要把这个accept状态的服务器key去掉，否则会出错
                            iterator.remove();
                            ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
                            //接受socket
                            SocketChannel socket = serverChannel.accept();
                            socket.configureBlocking(false);
                            //将channel加入到selector中，并一开始读取数据
                            socket.register(selector,SelectionKey.OP_READ);
                        }

                        if(key.isValid() && key.isReadable()){
                            System.out.println(key.toString() + " : 读");
                            
                        }
                    }
                }
            }

        }catch (Exception e){

        }
    }
}
