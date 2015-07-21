package com.litan;

public class Log {
	private static final boolean DBG = true;
	private static final String DFT_TAG = "LOG";
	private static final String VERBOSE = "VERBOSE";
	private static final String DEBUG = "DEBUG";
	private static final String WARNNING = "!!!WARNING!!!";
	private static final String ERROR = "!!!ERROR!!!";

	private static String formatMsg(String lev, String tag, String msg) {
		return String.format("%s: [%s] %s", lev, tag == null ? DFT_TAG : tag,
				msg);
	}

	public static void v(String msg) {
		v(null, msg);
	}

	public static void d(String msg) {
		d(null, msg);
	}

	public static void e(String msg) {
		e(null, msg);
	}

	public static void w(String msg) {
		w(null, msg);
	}

	public static void v(String tag, String msg) {
		if (DBG) {
			System.out.println(formatMsg(VERBOSE, tag, msg));
		}
	}

	public static void d(String tag, String msg) {
		System.out.println(formatMsg(DEBUG, tag, msg));
	}

	public static void e(String tag, String msg) {
		e(tag, msg, null);
	}

	public static void w(String tag, String msg) {
		System.out.println(formatMsg(WARNNING, tag, msg));
	}

	public static void e(String tag, String msg, Throwable throwable) {
		System.err.println(formatMsg(ERROR, tag, msg));
		if (throwable != null) {
			throwable.printStackTrace();
		}
	}
}
