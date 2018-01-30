package io.netty.handler.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by zhubo on 2018/1/30.
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 首先从数据报byteBuf中获取需要解码的byte数组，然后调用MessagePack 的read方法 将其反序列化为 Object对象，
     * 将解码后的对象加入到解码列表 list中 这样就完成了 MessagePack的解码操作。
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(),array,0,length);
        MessagePack msgpack = new MessagePack();
        list.add(msgpack.read(array));
    }
}
