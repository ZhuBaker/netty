package netty.protocal.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-14
 * Time: 16:43
 */
public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

    private int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI uri = new URI("http://127.0.0.1:8844");
        String msg = "are you ok?";
        DefaultFullHttpRequest request
                = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,uri.toASCIIString(),
                Unpooled.wrappedBuffer(msg.getBytes(Charset.forName("UTF-8"))));
        request.headers().set(HttpHeaderNames.HOST,8844);
        request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());

        ctx.channel().writeAndFlush(request).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpResponse){
            HttpResponse response = (HttpResponse) msg;
            System.out.println("CONTENT_TYPE: " + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
        }
        if(msg instanceof HttpContent){
            HttpContent content = (HttpContent)msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();
        }
    }
}