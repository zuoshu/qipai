package com.oneguy.qipai.view;

import android.content.Context;
import android.widget.RelativeLayout;

import com.oneguy.qipai.game.Director;

public class Stage extends RelativeLayout {

	private Director mDirector;

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
