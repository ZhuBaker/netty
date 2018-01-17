package com.timer.netty.gui;

public class Util2Bytes {
    public static int bytes2smallint(byte stream[]) {
		int value = 0;
		int temp = 0;
		for (int i = 3; i >= 0; i--) {
			if ((stream[i]) >= 0) {
				temp = stream[i];
			} else {
				temp = stream[i] + 256;
			}
			temp <<= (i * 8);
			value += temp;
		}
		return value;
	}

	public static short bytes2smallshort(byte stream[]) {
		short value = 0;
		int temp = 0;
		for (int i = 1; i >= 0; i--) {
			if ((stream[i]) >= 0) {
				temp = stream[i];
			} else {
				temp = stream[i] + 256;
			}
			temp <<= (i * 8);
			value += temp;
		}
		return value;
	}

	public static int bytes2bigint(byte stream[]) {
		int value = 0;
		int temp = 0;
		for (int i = 0; i < 4; i++) {
			if ((stream[i]) >= 0) {
				temp = stream[i];
			} else {
				temp = stream[i] + 256;
			}
			temp <<= ((4 - i - 1) * 8);
			value += temp;
		}
		return value;
	}

	public static short bytes2bigshort(byte stream[]) {
		short value = 0;
		int temp = 0;
		for (int i = 0; i < 2; i++) {
			if ((stream[i]) >= 0) {
				temp = stream[i];
			} else {
				temp = stream[i] + 256;
			}
			temp <<= ((2 - i - 1) * 8);
			value += temp;
		}
		return value;
	}
}
