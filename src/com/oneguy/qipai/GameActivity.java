package com.oneguy.qipai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.oneguy.qipai.game.Director;
import com.oneguy.qipai.game.QianfenDirector;
import com.oneguy.qipai.view.QianfenStage;

public class GameActivity extends Activity {
	private Director mDirector;
	private QianfenStage mQianfenStage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		String mode = i.getStringExtra(Constants.MODE);
		if (mode.equals(Constants.MODE_SIGNLE)) {
			mDirector = new QianfenDirector(this, Director.PLAY_WITH_AI);
		} else {
			mDirector = new QianfenDirector(this, Director.PLAY_WITH_HUMAN);
		}
		mQianfenStage = new QianfenStage(this);
		mDirector.setStage(mQianfenStage);
		mDirector.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDirector.onStop();
	}
	

}
