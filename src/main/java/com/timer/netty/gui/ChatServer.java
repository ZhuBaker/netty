package com.timer.netty.gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChatServer {
    // 包头4个字节==int
	private final int MESSAGE_LENGTH_HEAD = 4;
	private byte[] head = new byte[4];
	private int bodylen = -1;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	// 全局
	private SelectionKey selectKey;

	private int port = 0;
	private ServerSocketChannel ssc;
	private static SocketChannel sc;
	private ServerSocket ss;
	private static Selector select;

	private static List<SocketChannel> clients = new ArrayList<SocketChannel>();
	private static List<SelectionKey> clientKeys = new ArrayList<SelectionKey>();
	private static String msg;

	public ChatServer(int port) {
		this.port = port;
	}

	public void listen() {
		System.out.println("------listen-----");
		try {
			ssc = ServerSocketChannel.open();
			ss = ssc.socket();
			ss.bind(new InetSocketAddress(port));
			ssc.configureBlocking(false);
			select = Selector.open();
			ssc.register(select, SelectionKey.OP_ACCEPT);
			System.out.println("------server------开始启动----》端口9999");

			handle();
		} catch (IOException e) {
			System.out.println("------server启动失敗------");
			e.printStackTrace();
		}
	}

	/**
	 * 关闭
	 */
	public void close() {
		System.out.println("------close-----");
		if (select != null) {
			try {
				select.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 新连接
	 * 
	 * @param key
	 */
	public void connect(SelectionKey key) {
		System.out.println("------connect-----");
		try {
			ssc = (ServerSocketChannel) key.channel();
			sc = ssc.accept();
			// 记录总的客户数
			clients.add(sc);
			OnlineMan.addName(sc.socket().getRemoteSocketAddress().toString());
			sc.configureBlocking(false);
			selectKey = sc.register(select, SelectionKey.OP_READ);
			// 记录每个客户对应的key
			clientKeys.add(selectKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void setWriter(String sss) {
		try {
			// 立刻唤醒阻塞的事件
			select.wakeup();
			msg = sss;
			//将所以的连接都设置写事件，这样就会把信息广播到所有客户端
			//若想只对其中一个设置，需要根据名字或地址选择性的设置
			for (SocketChannel sc : clients) {
				sc.register(select, SelectionKey.OP_WRITE);
			}
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写数据
	 */
	public void writer(SelectionKey key) {
		System.out.println("------writer-----");
		try {
			//不需要循环，上面已经设置每个连接都注册了写事件,所以会依次触发写事件
			SocketChannel sc = (SocketChannel) key.channel();
			sc.write(ByteBuffer.wrap(msg.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//写完后取消写事件,避免循环写
				for (SelectionKey selectKey : clientKeys) {
					selectKey.interestOps(SelectionKey.OP_READ);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读数据 不要接收到连接就注册SelectionKey.OP_WRITE事件， 那个事件基本上管道不堵塞一直就绪的,会一直调用writer处理方法
	 * 先只注册可读事件。
	 * 
	 * @return
	 */
	public String read(SelectionKey key) {
		System.out.println("------read-----");
		byte[] bs = null;
		String res = "";
		try {
			sc = (SocketChannel) key.channel();
			// 清空数据开始读取
			buffer.clear();
			int count = sc.read(buffer);
			if (count > 0) {
				buffer.flip();
				bs = handleRead(buffer);
				if (bs != null) {
					// bs=包头+包体 总的长度，需要去除包头的长度4个字节
					res = new String(bs, 4, bs.length - 4);
					OnlineMan.addMsg(res);
					System.out.println("--------------------服务段收到的数据>>>>>" + res);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 主处理器
	 */
	public void handle() {
		try {
			while (true) {
				// 阻塞
				int num = select.select();
				if (num <= 0) {
					continue;
				}
				Set<SelectionKey> keys = select.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					if (key.isAcceptable()) {
						connect(key);
					} else if (key.isReadable()) {
						read(key);
					} else if (key.isWritable()) {
						writer(key);
					}
					it.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 自定义数据传输格式：包头+包体
	public byte[] handleRead(ByteBuffer input) {
		byte[] headandbody = null;
		while (input.remaining() > 0) {
			if (bodylen < 0) {// 还没有生成完整的包头部分,
				// 该变量初始值为-1，并且在拼凑一个完整的消息包以后，再将该值设置为-1
				if (input.remaining() >= MESSAGE_LENGTH_HEAD) // ByteBuffer缓冲区的字节数够拼凑一个包头
				{
					input.get(head, 0, 4);
					bodylen = Util2Bytes.bytes2bigint(head);
				} else {
					// ByteBuffer缓冲区的字节数不够拼凑一个包头，什么操作都不做，退出这次处理，继续等待
					break;
				}
			} else if (bodylen > 0) // 包头部分已经完整生成.
			{
				if (input.remaining() >= bodylen) // 缓冲区的内容够一个包体部分
				{
					byte[] body = new byte[bodylen];
					input.get(body, 0, bodylen);
					headandbody = new byte[MESSAGE_LENGTH_HEAD + bodylen];
					System.arraycopy(head, 0, headandbody, 0, head.length);
					System.arraycopy(body, 0, headandbody, head.length, body.length);
					bodylen = -1;
				} else // /缓冲区的内容不够一个包体部分，继续等待，跳出循环等待下次再出发该函数
				{
					break;
				}
			} else if (bodylen == 0) // 没有包体部分，仅仅有包头的情况
			{
				headandbody = new byte[MESSAGE_LENGTH_HEAD + bodylen];
				System.arraycopy(head, 0, headandbody, 0, head.length);
				bodylen = -1;
			}
		}
		return headandbody == null ? null : headandbody;
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer(9999);
		server.listen();
	}
}
