package netty.protocal.personal.factory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-15
 * Time: 15:01
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(100);
        int i = buf.readerIndex();
        System.out.println(i);
        buf.writeBytes(new byte[4]);
        buf.writeBytes(new byte[4]);
        buf.setInt(4,30);
        System.out.println(buf.readInt()+"   1");
        System.out.println(buf.readInt()+"   2");

    }
}
