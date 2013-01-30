package com.oneguy.qipai.game.control;

import android.os.Handler;
import android.os.Message;

public abstract class Opponent implements EventListener {
	protected Handler mHandler;
	private Handler directorHandler;

	protected abstract void onEventMessage(Message msg);

	public Opponent() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				onEventMessage(msg);
			}
		};
	}

	public void setDirectorHandler(Handler handler) {
		directorHandler = handler;
	}

	public void deployEvent(Message msg) {
		directorHandler.sendMessage(msg);
	}

	public void deployEvent(Message msg, long delayMillis) {
		directorHandler.sendMessageDelayed(msg, delayMillis);
	}

	public void deployEvent(int what, Object data) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = data;
		deployEvent(msg);
	}

	public void deployEvent(int what, Object data, long delayMillis) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = data;
		deployEvent(msg, delayMillis);
	}

	public void deployEvent(int what) {
		deployEvent(what, null);
	}

	public void sendEventMessage(Message message) {
		mHandler.sendMessage(message);
	}

	public Handler getHandler() {
		return mHandler;
	}
}
