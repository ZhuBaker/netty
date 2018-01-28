/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phei.netty.frame.correct;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
public class TimeServer {

    public void bind(int port) throws Exception {
	// 配置服务端的NIO线程组
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    .option(ChannelOption.SO_BACKLOG, 1024)
		    .childHandler(new ChildChannelHandler());
	    // 绑定端口，同步等待成功
	    ChannelFuture f = b.bind(port).sync();

	    // 等待服务端监听端口关闭
	    f.channel().closeFuture().sync();
	} finally {
	    // 优雅退出，释放线程池资源
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }

	/**
	 * 我们通过LineBasedFrameDecoder 和 StringDecoder 成功解决了TCP粘包导致的读半包问题，对于使用者来说
	 * 只要将支持半包解码的Handler添加到ChannelPipeline中即可，不需要写额外的代码
	 * LineBasedFrameDecoder的工作原理是它依次遍历ByteBuffer中可读字节，判断是否有"\n"或者"\r\n",如果有，
	 * 就以此位置为技术位置，从可读索引到结束位置区间的字节就组成一行，它是以换行符为结束标志的解码器，
	 * 支持携带结束符或者不携带结束符两种解码方式，同时支持配置单行的最大长度。如果连续读取到最大长度后仍然没有发现换行符，
	 * 就会抛出异常，同时忽略之前读到的异常码流，
	 * StringDecoder的功能非常简单，就是将接收到的对象转换成字符串，然后继续调用后面的Handler，
	 * LineBasedFrameDecoder + StringDecoder 的组合就是按行切换的文本解码器，它被设计用来支持TCP 的粘包和拆包
	 */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
	    arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
	    arg0.pipeline().addLast(new StringDecoder());
	    arg0.pipeline().addLast(new TimeServerHandler());
	}
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	int port = 8080;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		// 采用默认值
	    }
	}
	new TimeServer().bind(port);
    }
}
