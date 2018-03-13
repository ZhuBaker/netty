package netty.heart_beat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created with IntelliJ IDEA.
 * Description: 编码器
 * User: zhubo
 * Date: 2018-02-08
 * Time: 11:15
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack msgpack = new MessagePack();
        out.writeBytes(msgpack.write(msg));
    }
}
