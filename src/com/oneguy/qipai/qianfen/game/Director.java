package com.oneguy.qipai.qianfen.game;

import android.app.Activity;
import android.content.Context;

import com.oneguy.qipai.ui.Stage;

public abstract class Director extends EventGenerator implements EventListener {
	public static final int PLAY_WITH_AI = 0;
	public static final int PLAY_WITH_HUMAN = 1;

	public abstract void onStart();

	public abstract void onPause();

	public abstract void onStop();

	private Stage mStage;
	private Activity mActivity;
	// 对手
	private Opponent mOpponent;

	public Director(Activity activity) {
		mActivity = activity;
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

	public Opponent getOpponent() {
		return mOpponent;
	}

	public void setOpponent(Opponent opponent) {
		mOpponent = opponent;
	}

	public void deployEvent(Event event) {
		setCurrentEvent(event);
		mOpponent.onEvent(event);
	}

	public void deployEvent(int what, Object data) {
		Event event = new Event(what, data);
		deployEvent(event);
	}

	public void deployEvent(int what) {
		deployEvent(what, null);
	}
}
