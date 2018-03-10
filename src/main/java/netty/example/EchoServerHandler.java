package netty.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 2-1 EchoServerHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
//标示一个ChannelHandler可以被多个 Channel 安全地共享
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        // 下面两个方法 则会跳过ChannelInboundHandler 将数据传给ChannelOutboundHandler来做数据处理
        //ctx.channel().write(in);
        //ctx.pipeline().writeAndFlush(Unpooled.copiedBuffer("我是中国人我爱我的祖国!",CharsetUtil.UTF_8));

        //将消息交给下一个InoundHandler类来进行处理
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        System.out.println("in1");
        //触发下个ChannelHandlerContext的channelRead方法
        ctx.fireChannelRead(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("=========================1");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("EchoServerHandler1 active");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("EchoServerHandler1 inactive");
        ctx.fireChannelInactive();
    }
}
