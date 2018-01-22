/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.phei.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;


public class TimeServerHandler extends ChannelHandlerAdapter {
	private static final Logger logger = Logger
			.getLogger(TimeServerHandler.class.getName());
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("The time server receive order : " + body);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
				System.currentTimeMillis()).toString() : "BAD ORDER";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);// 异步发送应答消息给客户端
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// 将发送缓冲区中的消息全部写到SocketChannel中发送给对方。
		// 从性能角度考虑，为了防止频繁地唤醒Selector进行消息发送,Netty的write方法并不直接将消息写入SocketChannel中
		// 调用write方法只是把待发送的消息放到发送缓冲区数中，
		// 再通过调用flush方法,将发送缓冲区中的消息全部写到SocketChannel中
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warning("Unexpected exception from downstream : "
				+ cause.getMessage());
		/**
		 * 当发生异常时,关闭ChannelHandlerContext，释放ChannelHandlerContext相关联的句柄等资源
		 */
		ctx.close();
	}
}
