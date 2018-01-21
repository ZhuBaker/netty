package com.timer.netty.select;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by zhubo on 2018/1/16.
 */
public class NIOServer {

    private Selector selector;
    public void initServer(int port) throws Exception{
        //获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //设置通道为非阻塞
        serverChannel.configureBlocking(false);
        // 该通道对于serverSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一选择器
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并未该通道注册selectionKey.OP_ACCEPT事件
        //注册该事件后，当事件到达的时候，selector.select()会返回，
        // 如果事件没有到达selector.select()会一直阻塞
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void listen() throws Exception{
        System.out.println("start server ...");
        // 轮训访问seletor
        while(true){
            //当注册事件到达时，方法返回，否则该方法会一直阻塞
            selector.select();
            // 获得selector中选中项的地带器，选中项为注册事件
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            if(iterator.hasNext()){
                SelectionKey key = iterator.next();
                //删除已选的key 以防止重复处理
                iterator.remove();
                //客户端请求链接事件
                if(key.isValid() && key.isAcceptable()){
                    System.out.println("acceptable....");
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    //获取客户端链接的通道
                    SocketChannel channel = server.accept();
                    // 非阻塞模式
                    channel.configureBlocking(false);
                    //channel.write(ByteBuffer.wrap(new String("Hello Client").getBytes()));
                    // 在客户端链接成功后，为了可以连接到客户端的信息，需要给通道设置读的权限
                    channel.register(this.selector,SelectionKey.OP_READ);
                }else if(key.isValid() && key.isReadable()){
                    read(key);
                }
            }
        }
    }

    private void read(SelectionKey key) throws Exception {

        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(100);

        try{
            int read = channel.read(buffer);
            if(read > 0){
                byte[] data = buffer.array();
                String msg = new String(data).trim();
                System.out.println("server receive from client : "+msg);
                ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
                channel.write(outBuffer);
            }else if(read < 0){
                System.out.println("channel closed");
                channel.close();
                key.cancel();
            }else
                ;
        }catch (Exception e){
            System.out.println("channel exception closed");
            channel.close();
            key.cancel();
        }
        //key.cancel();
        //channel.close();
    }

    public static void main(String[] args) throws Exception{
        NIOServer server = new NIOServer();
        server.initServer(8989);
        server.listen();
    }
}
