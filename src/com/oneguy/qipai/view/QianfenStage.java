package com.oneguy.qipai.view;

import android.content.Context;

import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;

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
