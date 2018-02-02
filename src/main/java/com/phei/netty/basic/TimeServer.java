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
package com.phei.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

    public void bind(int port) throws Exception {
		// 配置服务端的NIO线程组 ,专门用于网络事件的处理,实际上他们就是Reactor线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();// 用户服务端接收客户端的链接
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 用户进行SocketChannel的网络读写
		try {
			// ServerBootstrap对象实际上是netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)// 将两个NIO线程组当做入参传递到ServerBootstrap中
				.channel(NioServerSocketChannel.class)//创建NioServerSocketChannel 对应JDK NIO库中的ServerSocketChannel类
				.option(ChannelOption.SO_BACKLOG, 1024)//然后配置NioServerSocketChannel的TCP参数,此处将它的backlog设置为1024
					//最后绑定IO事件的处理类ChildChannelHandler,类似于Reactor模式中的Handler类,主要用于处理网络IO事件,例如记录日志，对消息进行编解码等
				.childHandler(new ChildChannelHandler());
			// 绑定端口，同步方法等待绑定操作完成 ,返回ChannelFuture主要用于异步操作的通知回调.
			ChannelFuture f = b.bind(port).sync();

			// 等待阻塞,等待服务端链路关闭之后main函数才退出
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
    }
	// 调用childHandler 来指定连接后调用的ChannelHandler,这个方法传ChannelInitalizer类型的参数
	// ChannelInitalizer是个抽象类,所以要实现initChannel方法, 这个方法及时用来设置ChannelHandler的
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
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
