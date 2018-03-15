package netty.protocal.personal.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocal.personal.entity.Header;
import netty.protocal.personal.entity.MessageType;
import netty.protocal.personal.entity.NettyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-15
 * Time: 17:40
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(HeartBeatRespHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 如果是心跳请求则 返回心跳应答消息
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            LOG.info("Receive client heart beat message : ---> " + message);
            NettyMessage heartBeat = buildHeatBeat();
            LOG.info("Send heart beat response message to client : ---> "  + heartBeat);
            ctx.pipeline().writeAndFlush(heartBeat);
        } else {// 否则继续穿透消息
            ctx.fireChannelRead(msg);
        }

    }

    private NettyMessage buildHeatBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }


}
