package com.oneguy.qipai.game.ai;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.oneguy.qipai.game.control.EventListener;
import com.oneguy.qipai.game.control.Opponent;
import com.oneguy.qipai.view.Stage;

public abstract class Director implements EventListener {
	public static final int PLAY_WITH_AI = 0;
	public static final int PLAY_WITH_HUMAN = 1;

	public abstract void onStart();

	public abstract void onPause();

	public abstract void onStop();

	protected abstract void onEventMessage(Message msg);

	protected Stage mStage;
	protected Activity mActivity;
	// 对手
	protected Handler mHandler;
	private Handler opponentHandler;

	public Director(Activity activity) {
		mActivity = activity;
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				onEventMessage(msg);
			}
		};
	}

	public void setStage(Stage stage) {
		mStage = stage;
		if (mStage != null) {
			mStage.setDirector(this);
		}
	}

	public Stage getStage() {
		return mStage;
	}

	public Activity getActivity() {
		return mActivity;
	}

	public void setOpponentHandler(Handler handler) {
		opponentHandler = handler;
	}

	public void deployEvent(Message msg) {
		opponentHandler.sendMessage(msg);
	}

	public void deployEvent(int what, Object data) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = data;
		deployEvent(msg);
	}

	public void deployEvent(int what) {
		deployEvent(what, null);
	}

	public Handler getHandler() {
		return mHandler;
	}

	public void sendEventMessage(Message message) {
		mHandler.sendMessage(message);
	}

	public void sendEventMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		sendEventMessage(msg);
	}

	public void sendEventMessage(int what) {
		sendEventMessage(what, null);
	}
}
