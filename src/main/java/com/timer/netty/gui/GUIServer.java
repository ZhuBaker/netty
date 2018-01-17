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

public class GUIServer {

    protected static ChatServer server;
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame("服务器");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 500);
		frame.setLocation(300, 300);

		// 上
		JPanel pane = new JPanel(new FlowLayout());
		JLabel label = new JLabel("服务器");
		pane.add(label);
		frame.add(pane, BorderLayout.NORTH);

		// 下
		JPanel pane2 = new JPanel(new FlowLayout());
		final JTextArea area = new JTextArea(2, 35);
		JScrollPane sp = new JScrollPane(area);
		JButton bu = new JButton("广播");
		bu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ss = area.getText();
				ChatServer.setWriter(ss);
				area.setText("");
			}
		});

		final JButton start = new JButton("启动");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						server = new ChatServer(9999);
						server.listen();
					}
				}).start();
				start.setEnabled(false);
			}
		});
		pane2.add(sp);
		pane2.add(bu);
		pane2.add(start);
		frame.add(pane2, BorderLayout.SOUTH);

		// 右
		JPanel pane3 = new JPanel(new FlowLayout());
		final JTextArea userTxt = new JTextArea(23, 12);
		JScrollPane sp2 = new JScrollPane(userTxt);
		pane3.add(sp2);
		frame.add(pane3, BorderLayout.EAST);

		// 中间
		JPanel pane4 = new JPanel(new FlowLayout());
		final JTextArea mainTxt = new JTextArea(23, 35);
		JScrollPane sp3 = new JScrollPane(mainTxt);
		pane4.add(sp3);
		frame.add(pane4, BorderLayout.CENTER);

		// 更新聊天記錄
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				List<String> messageList = OnlineMan.getMsg();
				for (String s : messageList) {
					mainTxt.setText(mainTxt.getText() + s + "\n");
				}
				messageList.clear();
			}
		}, 1, 1, TimeUnit.SECONDS);

		// 更新在线人员
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				List<String> userList = OnlineMan.getName();
				for (String s : userList) {
					userTxt.setText(userTxt.getText() + s + "\n");
				}
				userList.clear();
			}
		}, 2, 2, TimeUnit.SECONDS);

		frame.setVisible(true);
		frame.setLayout(new BorderLayout(2, 2));
	}
}
