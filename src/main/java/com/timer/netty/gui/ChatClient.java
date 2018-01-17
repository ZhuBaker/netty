package com.timer.netty.gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

public class ChatClient {

    private static ByteBuffer sendbuffer = ByteBuffer.allocate(1024);
	private static ByteBuffer receivebuffer = ByteBuffer.allocate(1024);
	static Charset charset = Charset.forName("UTF-8");
	static CharsetEncoder encode = charset.newEncoder();
	static CharsetDecoder decode = charset.newDecoder();

	private static SocketChannel client;
	private static Selector selector;
	private static String msg;

	private String userName;
	private String hostName;
	private int port;

	public ChatClient(String userName, String hostName, int port) {
		this.userName = userName;
		this.hostName = hostName;
		this.port = port;
	}

	public void listen() {
		try {
			client = SocketChannel.open();
			client.configureBlocking(false);
			client.connect(new InetSocketAddress(hostName, port));
			selector = Selector.open();
			client.register(selector, SelectionKey.OP_CONNECT);

			handle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connect(SelectionKey selectionKey) {
		try {
			client = (SocketChannel) selectionKey.channel();
			// 判断此通道上是否正在进行连接操作。
			// 完成套接字通道的连接过程。
			if (client.isConnectionPending()) {
				client.finishConnect();
				sendbuffer.clear();
				sendbuffer.putInt((userName + "加入").getBytes().length);
				sendbuffer.put((userName + "加入").getBytes());
				sendbuffer.flip();
				client.write(sendbuffer);
			}
			client.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void read(SelectionKey selectionKey) {
		try {
			client = (SocketChannel) selectionKey.channel();
			receivebuffer.clear();
			int count = client.read(receivebuffer);
			if (count > 0) {
				receivebuffer.flip();
				CharBuffer cf = decode.decode(receivebuffer);
				System.out.println("客户端收到数据----->" + cf);
				String value = new String(cf.array(), 0, cf.array().length);
				OnlineMan.addClientMsg(ChatUtils.pjMessage("服务器", value));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册写事件
	 * 
	 * @param msg
	 */
	public static void setWriter(String mssssg) {
		try {
			msg = mssssg;
			if (client != null && client.isOpen()) {
				// 取消selector阻塞，立刻返回
				selector.wakeup();
				client.register(selector, SelectionKey.OP_WRITE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writer(SelectionKey selectionKey) {
		try {
			client = (SocketChannel) selectionKey.channel();
			sendbuffer.clear();
			String s=ChatUtils.pjMessage(userName, msg);
			// 包头--4个字节一个整形
			sendbuffer.putInt(s.getBytes().length);
			sendbuffer.put(s.getBytes());
			sendbuffer.flip();
			client.write(sendbuffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 取消写事件。因为该事件不阻塞，会一直调用writer方法(只要注册writer事件,就会立马调用)
			selectionKey.interestOps(SelectionKey.OP_READ);
		}
	}

	public void handle() {
		try {
			while (true) {
				int count = selector.select();
				if (count <= 0) {
					continue;
				}

				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				while (it.hasNext()) {
					SelectionKey selectionKey = it.next();
					if (selectionKey.isConnectable()) {
						connect(selectionKey);
					} else if (selectionKey.isReadable()) {
						read(selectionKey);
					} else if (selectionKey.isWritable()) {
						writer(selectionKey);
					}
					it.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
