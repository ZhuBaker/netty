package netty.bytebufd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-02-01
 * Time: 22:25
 */
public class ByteBufDoTest {

    public static void main(String[] args) {
        do7();
    }

    private static void do1(){
        // 分配一个Byte缓冲区 长度为16字节
        ByteBuf buf = Unpooled.buffer(16);
        // 输出buffer的容量 : capacity
        System.out.println("capacity : " +buf.capacity());
        for(int i = 0 ; i < buf.capacity(); i++){
            buf.writeByte(i + 1);
        }
        // 输出 可读byte长度 writerIndex - readerIndex
        System.out.println("readableBytes : " + buf.readableBytes());

        int capacity = buf.readableBytes();

        // buf.readByte 会向后移动 readerIndex
        for(int i = 0 ; i < capacity ; i++){
            System.out.println(buf.readableBytes() +" : "+buf.readByte());
        }
    }

    private static void do2(){
        ByteBuf buf = Unpooled.buffer(16);

        System.out.println("capacity : " +buf.capacity());
        // isWritable 判断该buffer是否可写
        while(buf.isWritable()){
            buf.writeByte(9);
        }

        System.out.println("readableBytes : " + buf.readableBytes());

        // 判断该buffer 是否可读
        while(buf.isReadable()){
            System.out.print(buf.readByte() + " , ");
        }
    }

    private static void do3(){
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", Charset.forName("utf-8"));
        // 最常用的类型是ByteBuf将数据存储在JVM的堆空间，这是通过将数据存储在数组的实现。 非堆缓冲区 ByteBuf的数组会导致UnsupportedOperationException
        // 可以使用bytebuf.hasArray来检查是否支持访问数组
        byte[] array = null;
        if(buf.hasArray()){
            array = buf.array();
        }
        for(int i = 0 ; i < array.length ; i++){
            System.out.print((char)array[i]);
        }
        // 修改数组内容 byteBuf输出随之改变
        array[5] = 36;
        System.out.println();

        System.out.println(buf.toString(Charset.forName("utf-8")));
    }
    /**
     * 、slice()、slice(int index, int length)会创建一个现有缓冲区的视图,
     * 衍生的缓冲区有独立的 readerIndex、writerIndex 和标注索引
     * 如果需要现有缓冲区的全新副本，可以使用 copy()或 copy(int index, int length)获得
     */

    private static void do4(){
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", Charset.forName("utf-8"));

        System.out.println(buf);
        // 无参方法返回的是现在readerIndex、writerIndex之间可读区域的视图对象
        //ByteBuf slice = buf.slice();
        ByteBuf slice = buf.slice(0, 14);
        System.out.println("slice : "+slice);
        System.out.println(slice.toString(Charset.forName("utf-8")));

        ByteBuf copy = buf.copy(0,14);
        System.out.println("copy : "+copy);
        System.out.println(copy.toString(Charset.forName("utf-8")));

        System.out.println(buf.toString(Charset.forName("utf-8")));
    }

    /**
     * 直接缓冲区 不支持数组访问数据，但我们可以间接的访问数组数据
     * 访问直接缓冲区的数据数组需要更多的编码和更复杂的操作，建议若需要在数组访问数据使用堆缓冲区会更好。
     */
    public static void do5(){
        ByteBuf directBuf = Unpooled.directBuffer(16);
        while(directBuf.isWritable()){
            directBuf.writeByte(1);
        }
        byte[] arr = null;
        if(!directBuf.hasArray()){
            int len = directBuf.readableBytes();
            arr = new byte[len];
            directBuf.getBytes(0,arr);
        }
        for(int i = 0 ; i < arr.length ; i++){
            System.out.println(arr[i]);
        }
    }

    /**
     * Netty优化套接字读写的操作是尽可能的使用CompositeByteBuf来做的，使用CompositeByteBuf不会操作内存泄露问题。
     */
    public static void do6(){
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();

        ByteBuf heapBuf = Unpooled.buffer(8);
        while(heapBuf.isWritable()){
            heapBuf.writeByte(1);
        }
        ByteBuf directBuf = Unpooled.directBuffer(16);
        while(directBuf.isWritable()){
            directBuf.writeByte(2);
        }
        //添加ByteBuf到CompositeByteBuf ,  其中第一个参数是 true, 表示当添加新的 ByteBuf 时, 自动递增 CompositeByteBuf 的 writeIndex
        compBuf.addComponents(true,heapBuf,directBuf);

        //compBuf.removeComponent(0);

        Iterator<ByteBuf> iterator = compBuf.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
        // 使用数组访问数据
        if(!compBuf.hasArray()){
            int len = compBuf.readableBytes();
            System.out.println(len);
            byte[] arr = new byte[len];
            compBuf.getBytes(0,arr);
            for(int i = 0 ; i < arr.length ; i ++){
                System.out.println(arr[i]);
            }
        }
    }


    /**
     *  Unpooled.wrappedBuffer 可以实现上面相同的效果
     */
    public static void do7(){
        ByteBuf heapBuf = Unpooled.buffer(8);
        while(heapBuf.isWritable()){
            heapBuf.writeByte(1);
        }
        ByteBuf directBuf = Unpooled.directBuffer(16);
        while(directBuf.isWritable()){
            directBuf.writeByte(2);
        }
        ByteBuf byteBuf = Unpooled.wrappedBuffer(heapBuf, directBuf);

        while(byteBuf.isReadable()){
            System.out.println(byteBuf.readByte());
        }
    }

    /**
     * 注意通过索引访问时不会推进读索引和写索引，我们可以通过ByteBuf的readerIndex()或writerIndex()
     */
    public static void do8(){
        ByteBuf heapBuf = Unpooled.buffer(8);
        while(heapBuf.isWritable()){
            heapBuf.writeByte(1);
        }
        int len = heapBuf.readableBytes();
        for(int i = 0 ; i < len ; i ++){
            System.out.println(heapBuf.getByte(i));
        }
        System.out.println(heapBuf.readableBytes());
    }
}
