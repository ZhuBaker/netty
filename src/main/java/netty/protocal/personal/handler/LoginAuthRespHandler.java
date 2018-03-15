package netty.protocal.personal.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocal.personal.entity.Header;
import netty.protocal.personal.entity.MessageType;
import netty.protocal.personal.entity.NettyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-15
 * Time: 16:05
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(LoginAuthRespHandler.class);

    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String[] whitekList = {"127.0.0.1","192.168.1.104"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        NettyMessage message = (NettyMessage) msg;
        // 如果是握手请求消息，处理，其它消息透传
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            // 重复登陆，拒绝
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whitekList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0)
                        : buildResponse((byte) -1);
                // 如果未登录过并且是在白名单当中
                if (isOK) {
                    nodeCheck.put(nodeIndex, true);
                }
            }
            LOG.info("The login response is : " + loginResp
                    + " body [" + loginResp.getBody() + "]");
            ctx.pipeline().writeAndFlush(loginResp);
        } else { // 如果不是后台请求,则穿透
            ctx.fireChannelRead(msg);
        }

    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);

    }
}
