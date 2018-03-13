package io.nio.concurrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Created by zhubo on 2018/1/16.
 */
public class NIOServer {

    private static final int DEFAULT_PORT = 8989;
    private static Selector  selector;
    private Object           lock = new Object();
    private Boolean          isStart = false;
    private ExecutorService  threadPool = null;


    public void initServer(int port) throws IOException{
        //获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //设置通道为非阻塞
        serverChannel.configureBlocking(false);
        // 该通道对于serverSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一选择器
        selector = Selector.open();
        //将通道管理器和该通道绑定，并未该通道注册selectionKey.OP_ACCEPT事件
        //注册该事件后，当事件到达的时候，selector.select()会返回，
        // 如果事件没有到达selector.select()会一直阻塞
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        threadPool = Executors.newFixedThreadPool(10);
        //threadPool = new ThreadPoolExecutor(10,10,5,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100));

        synchronized (lock){
            isStart = true;
        }
        System.out.println("nio server init successful.");

    }


    public void run() throws IOException{
        synchronized(lock){
            if (!isStart) {
                try {
                    initServer(DEFAULT_PORT);
                } catch (IOException e) {
                    throw new IOException("nio server init failed.");
                }
            }
        }

        // 轮训访问seletor
        while(true){
            //当注册事件到达时，方法返回，否则该方法会一直阻塞
            selector.select();
            // 获得selector中选中项的地带器，选中项为注册事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
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
                    SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
                    ByteBuffer buffer = ByteBuffer.allocate(100);
                    clientKey.attach(buffer);
                }else if(key.isValid() && key.isReadable()){
                    /*if(SelectionKeyHolder.isContainKey(key)){
                        continue;
                    }
                    SelectionKeyHolder.put(key);*/
                    //如果key对应的Channel一直在操作过程中,Selector会认为 该SelectionKey一直处于可读状态,上面selector.select(); 会一直处于非阻塞状态
                    threadPool.execute(new NIOHandler(key));
                }
            }
        }

    }


    public static void main(String[] args) throws Exception{
        NIOServer server = new NIOServer();
        server.run();
    }
}
