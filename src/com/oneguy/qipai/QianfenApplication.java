package com.oneguy.qipai;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.oneguy.qipai.entity.Player;
import com.oneguy.qipai.game.Recorder;
import com.oneguy.qipai.game.ai.AutoPlay;
import com.oneguy.qipai.game.ai.Judge;

public class QianfenApplication extends Application {
	private static QianfenApplication mInstance;
	public static int displayWidth;
	public static int displayHeight;
	public static Player[] players;
	public static Judge judge;
	public static Recorder recorder;
	// 托管出牌
	public static AutoPlay autoPlayer;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		setDisplayInfo();
		initPlayers();
		initJudge();
		initRecorder();
		initAutoPlayer();
	}

	public static synchronized QianfenApplication getInstance() {
		if (mInstance == null) {
			mInstance = new QianfenApplication();
		}
		return mInstance;
	}

	private void setDisplayInfo() {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) this.getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		displayWidth = Math.max(dm.widthPixels, dm.heightPixels);
		displayHeight = Math.min(dm.widthPixels, dm.heightPixels);
		if (BuildConfig.DEBUG) {
			Log.d("ScreenInfo", dm.toString());
		}
	}

	private void initPlayers() {
		players = new Player[4];
		players[Player.SEAT_BOTTOM] = new Player();
		players[Player.SEAT_RIGHT] = new Player();
		players[Player.SEAT_UP] = new Player();
		players[Player.SEAT_LEFT] = new Player();
	}

	private void initJudge() {
		judge = new Judge(players);
	}

	private void initRecorder() {
		recorder = new Recorder(players);
	}

	private void initAutoPlayer() {
		autoPlayer = new AutoPlay(recorder, players);
	}
}
