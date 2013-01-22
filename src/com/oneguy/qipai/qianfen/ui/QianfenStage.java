package com.oneguy.qipai.qianfen.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.oneguy.qipai.hall.BuildConfig;
import com.oneguy.qipai.hall.R;
import com.oneguy.qipai.qianfen.game.Event;
import com.oneguy.qipai.qianfen.resource.ResourceManger;
import com.oneguy.qipai.ui.Clock;
import com.oneguy.qipai.ui.Clock.OnTimeOutListener;
import com.oneguy.qipai.ui.Stage;

public class QianfenStage extends Stage {
	private static final String TAG = "GameView";
	private Context mContext;
	private ResourceManger mResourceManager;

	public QianfenStage(Context context) {
		super(context);
		setBackgroundResource(R.drawable.deck_background);
		mContext = context;
		mResourceManager = ResourceManger.getInstance();
	}

}
