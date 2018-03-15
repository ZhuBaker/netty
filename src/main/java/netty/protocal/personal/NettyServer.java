package netty.protocal.personal;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import netty.protocal.personal.codec.NettyMessageDecoder;
import netty.protocal.personal.codec.NettyMessageEncoder;
import netty.protocal.personal.entity.NettyConstant;
import netty.protocal.personal.handler.HeartBeatRespHandler;
import netty.protocal.personal.handler.LoginAuthRespHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-15
 * Time: 17:53
 */
public class NettyServer {

    private final static Logger LOG = LoggerFactory.getLogger(NettyServer.class);

    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws IOException {
                        ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                        ch.pipeline().addLast(new NettyMessageEncoder());
                        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));//控制50秒超时
                        ch.pipeline().addLast(new LoginAuthRespHandler());
                        ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
                    }
                });

        // 绑定端口，同步等待成功
        b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
        LOG.info("Netty server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }

}
