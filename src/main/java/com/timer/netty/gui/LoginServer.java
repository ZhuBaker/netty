package com.timer.netty.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginServer {
    public static void main(String[] args) {
		final JFrame frame = new JFrame("客户端");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 230);
		frame.setLocation(300, 300);
		GridLayout table = new GridLayout(4, 1);

		JPanel pane = new JPanel();
		JLabel label = new JLabel("服务器:");
		final JTextField txt = new JTextField(20);
		pane.add(label);
		pane.add(txt);

		JPanel pane2 = new JPanel();
		JLabel label2 = new JLabel("端口:");
		final JTextField txt2 = new JTextField(20);
		pane2.add(label2);
		pane2.add(txt2);

		JPanel pane3 = new JPanel();
		JLabel label3 = new JLabel("马甲:");
		final JTextField txt3 = new JTextField(20);
		pane2.add(label3);
		pane2.add(txt3);

		JPanel pane4 = new JPanel();
		final JButton connect = new JButton("连接");
		final JButton cancel = new JButton("取消");
		pane4.add(connect);
		pane4.add(cancel);

		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String host = txt.getText().trim();
				int port = Integer.parseInt(txt2.getText().trim());
				String userName = txt3.getText().trim();
				GUIClient guiClient = new GUIClient(userName, host, port);
				frame.dispose();
			}
		});

		frame.add(pane);
		frame.add(pane2);
		frame.add(pane3);
		frame.add(pane4);

		frame.setVisible(true);
		frame.setLayout(table);
	}
}
