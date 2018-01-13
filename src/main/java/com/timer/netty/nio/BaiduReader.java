package com.timer.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-12
 * Time: 18:09
 */
public class BaiduReader {

    private Charset charset = Charset.forName("GBK");
    private SocketChannel channel;

    public void readHTMLConnect(){
        try{
            InetSocketAddress socketAddress = new InetSocketAddress("www.baidu.com",80);
            //打开连接
            channel = SocketChannel.open(socketAddress);
            //发送请求使用GBK编码
            channel.write(charset.encode("GET" + "/HTTP/1.1" + "\r\n\r\n"));
            //读取数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);//创建1024字节的缓冲区
            while(channel.read(buffer) != -1){
                buffer.flip();//切换为读模式
                System.out.println(charset.decode(buffer));
                buffer.clear();//清空缓冲区切换为写模式
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(channel != null){
                try {
                    channel.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        new BaiduReader().readHTMLConnect();
    }
}
