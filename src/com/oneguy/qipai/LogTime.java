package com.oneguy.qipai;

import android.util.Log;

public class LogTime {

	static long time;
	final static String TIME_TAG = "timer";

	public static void timerInit() {
		Log.d(TIME_TAG, "timer init");
		time = System.currentTimeMillis();
	}

	public static void logTime(String content) {
		Log.d(TIME_TAG, content + ":" + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
	}

}
