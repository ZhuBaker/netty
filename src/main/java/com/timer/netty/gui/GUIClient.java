package com.timer.netty.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUIClient {

    protected static ChatServer server;
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

	private String userName;
	private String hostName;
	private int port;

	public GUIClient(final String userName, final String hostName, final int port) {
		this.userName = userName;
		this.hostName = hostName;
		this.port = port;

		JFrame frame = new JFrame("客户端");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 500);
		frame.setLocation(300, 300);

		// 上
		JPanel pane = new JPanel(new FlowLayout());
		JLabel label = new JLabel("客户端");
		pane.add(label);
		frame.add(pane, BorderLayout.NORTH);

		// 下
		JPanel pane2 = new JPanel(new FlowLayout());
		final JTextArea area = new JTextArea(2, 35);
		JScrollPane sp = new JScrollPane(area);
		JButton bu = new JButton("发送");
		bu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ss = area.getText();
				ChatClient.setWriter(ss);
				area.setText("");
			}
		});

		final JButton start = new JButton("连接");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						ChatClient client = new ChatClient(userName, hostName, port);
						client.listen();
					}
				}).start();
			}
		});
		pane2.add(sp);
		pane2.add(bu);
		pane2.add(start);
		frame.add(pane2, BorderLayout.SOUTH);

		// 中间
		JPanel pane4 = new JPanel(new FlowLayout());
		final JTextArea mainTxt = new JTextArea(23, 35);
		JScrollPane sp3 = new JScrollPane(mainTxt);
		pane4.add(sp3);
		frame.add(pane4, BorderLayout.CENTER);

		frame.setVisible(true);
		frame.setLayout(new BorderLayout(2, 2));

		// 更新聊天記錄
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				List<String> clientMessageList = OnlineMan.getClientMsg();
				for (String s : clientMessageList) {
					mainTxt.setText(mainTxt.getText() + s + "\n");
				}
				clientMessageList.clear();
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
