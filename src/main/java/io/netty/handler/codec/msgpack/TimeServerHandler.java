package io.netty.handler.codec.msgpack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-02-08
 * Time: 11:33
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub
        try {
            //直接输出msg
            System.out.println(msg.toString());
            String remsg = new String("has receive");
            //回复has receive 给客户端
            ctx.write(remsg);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        ctx.flush();
    }


}
