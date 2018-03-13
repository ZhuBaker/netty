package netty.heart_beat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description: 解码器
 * User: zhubo
 * Date: 2018-02-08
 * Time: 11:23
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final int length = msg.readableBytes();
        byte[] b = new byte[length];
        msg.getBytes(msg.readerIndex(),b,0,length);
        MessagePack msgpack = new MessagePack();
        out.add(msgpack.read(b));
    }
}
