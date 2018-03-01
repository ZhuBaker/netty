package io.netty.handler.codec.msgpack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-02-08
 * Time: 11:29
 */
public class ClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub
        System.out.println(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        //发送50个UserInfo给服务器，由于启用了粘包/拆包支持，所以这里连续发送多个也不会出现粘包的现象。
        for (int i = 0; i < 50; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAge(i + "year");
            userInfo.setUsername("senninha");
            ctx.write(userInfo);
        }
        ctx.flush();
        System.out.println("-----------------send over-----------------");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("error");
    }

}
