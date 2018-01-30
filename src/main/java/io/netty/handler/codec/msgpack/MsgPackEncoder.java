package io.netty.handler.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by zhubo on 2018/1/30.
 * MsgpackEncoder 继承 MessageToByteEncoder 它负责将 Object类型的Object 对象编码为byte数组，
 * 然后写入到ByteBuf中
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object>{

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        MessagePack msgpack = new MessagePack();
        byte[] write = msgpack.write(o);
        byteBuf.writeBytes(write);

    }
}
