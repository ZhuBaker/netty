package com.timer.netty.gui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatUtils {
    public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";

	public static SimpleDateFormat getSimpleDateFormat(String format) {
		SimpleDateFormat sdf;
		if (format == null)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else {
			sdf = new SimpleDateFormat(format);
		}

		return sdf;
	}

	public static String formatDate2Str(Date date, String format) {
		if (date == null) {
			return null;
		}

		if ((format == null) || (format.equals(""))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = getSimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String pjMessage(String name, String msg) {
		return "[" + name + "]:" + ChatUtils.formatDate2Str(new Date(), "") + "\n" + msg + "\n";
	}
}
