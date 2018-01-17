package com.timer.netty.gui;

import java.util.ArrayList;
import java.util.List;

public class OnlineMan {
    private static List<String> userList = new ArrayList<String>();
	private static List<String> messageList = new ArrayList<String>();
	private static List<String> clientMessageList = new ArrayList<String>();

	public static boolean addName(String name) {
		if (!userList.contains(name)) {
			userList.add(name);
			return true;
		}
		return false;
	}

	public static void addMsg(String msg) {
		messageList.add(msg);
	}

	public static List<String> getMsg() {
		return messageList;
	}

	public static List<String> getName() {
		return userList;
	}

	public static void addClientMsg(String msg) {
		clientMessageList.add(msg);
	}

	public static List<String> getClientMsg() {
		return clientMessageList;
	}
}
