package io.nio.concurrent;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-13
 * Time: 17:15
 */
public class NIOHandler implements Runnable{

    private SelectionKey key ;

    public NIOHandler(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(1000L);
        }catch (Exception e){
            e.printStackTrace();
        }

        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
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
            }else{

            }
            SelectionKeyHolder.remove(key);
            key.cancel();
        }catch (Exception e){
            System.out.println("channel exception closed");
            try{
                channel.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }
            key.cancel();
        }
    }
}
