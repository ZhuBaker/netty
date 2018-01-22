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

public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger
	    .getLogger(TimeClientHandler.class.getName());

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
	byte[] req = "QUERY TIME ORDER".getBytes();
	firstMessage = Unpooled.buffer(req.length);
	firstMessage.writeBytes(req);

    }

    /**
     * 当客户端和服务端TCP链路简历成功之后,Netty的NIO线路会调用channelActive方法,发送查询时间的指令给服务器
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
	    ctx.writeAndFlush(firstMessage);// 调用 writeAndFlush 方法将请求消息发送给服务端
    }

    /**
     * 当服务端返回应答消息时,channelRead 方法被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	ByteBuf buf = (ByteBuf) msg;
	byte[] req = new byte[buf.readableBytes()];
	buf.readBytes(req);
	String body = new String(req, "UTF-8");
	System.out.println("Now is : " + body);
    }

    /**
     * 发生异常时，打印异常日志，释放客户端资源
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	// 当发生异常时，打印异常日志，释放客户端资源
	logger.warning("Unexpected exception from downstream : "
		+ cause.getMessage());
	ctx.close();
    }
}
