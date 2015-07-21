package com.litan;

public class Log {
	private static final boolean DBG = true;

	static void v(String msg) {
		if (DBG) {
			System.out.println("VERBOSE:" + msg);
		}
	}

	static void d(String msg) {
		System.out.println(msg);
	}

	static void e(String msg) {
		System.err.println(msg);
	}

	static void w(String msg) {
		System.out.println("!!!WARNING!!!:" + msg);
	}
}
