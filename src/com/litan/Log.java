package com.litan;

public class Log {
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
