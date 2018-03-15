package netty.protocal.personal.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.protocal.personal.entity.NettyMessage;
import netty.protocal.personal.factory.MarshallingEncoder;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-15
 * Time: 15:10
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        if(null == msg || null == msg.getHeader()){
            throw new Exception("The encode message is null");
        }
        //---写入crcCode---
        out.writeInt(msg.getHeader().getCrcCode());
        //---写入length---
        out.writeInt(msg.getHeader().getLength());
        //---写入sessionId---
        out.writeLong(msg.getHeader().getSessionID());
        //---写入type---
        out.writeByte(msg.getHeader().getType());
        //---写入priority---
        out.writeByte(msg.getHeader().getPriority());
        //---写入附件大小---
        out.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;

        for(Map.Entry<String,Object> param : msg.getHeader().getAttachment().entrySet()){
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            out.writeInt(keyArray.length);
            out.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value,out);
        }

        // for gc
        key = null;
        keyArray = null;
        value = null;

        if(msg.getBody() != null){
            marshallingEncoder.encode(msg.getBody(),out);
        }else{
            out.writeInt(0);
        }
        // 之前写了crcCode 4bytes，除去crcCode和length 8bytes即为更新之后的字节
        out.setInt(4,out.readableBytes()-8);
    }
}
