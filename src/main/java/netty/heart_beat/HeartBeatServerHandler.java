package netty.heart_beat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-13
 * Time: 20:23
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        System.out.println("心跳超时触发！");
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.WRITER_IDLE){
                ctx.writeAndFlush(new UserInfo("写超时","1"));
            }else{
                ctx.pipeline().writeAndFlush(new UserInfo("读超时","2"));
            }
        }

        super.userEventTriggered(ctx, evt);
    }
}
