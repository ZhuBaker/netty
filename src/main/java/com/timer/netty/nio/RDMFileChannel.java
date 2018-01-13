package com.timer.netty.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-12
 * Time: 19:54
 */
public class RDMFileChannel {

    public static void main(String[] args)  throws Exception{
        RandomAccessFile rdf = new RandomAccessFile("D:\\applicationContext-dubbo.xml","rw");
        Charset charset = Charset.forName("UTF-8");
        FileChannel channel = rdf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = channel.read(buffer);
        while (bytesRead != -1){
            System.out.println("Read " + bytesRead);
            buffer.flip();
            CharBuffer decode = charset.decode(buffer);
            while(decode.hasRemaining()){
                System.out.print(decode.get());
            }
            buffer.clear();
            bytesRead = channel.read(buffer);
        }
        rdf.close();
    }
}
