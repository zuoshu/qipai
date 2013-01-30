package com.oneguy.qipai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.oneguy.qipai.game.ai.Director;

public class Stage extends RelativeLayout {
	private Director mDirector;

	public Stage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Stage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Stage(Context context) {
		super(context);
	}

	public Director getDirector() {
		return mDirector;
	}

	public void setDirector(Director director) {
		this.mDirector = director;
	}

}
