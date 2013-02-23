package com.oneguy.qipai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent event) {
	// Log.d("stage",
	// "dispatchTouchEvent " + event.getAction() + ":" + event.getX()
	// + ":" + event.getY());
	// return super.dispatchTouchEvent(event);
	// }
	//
	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// Log.d("stage", "onInterceptTouchEvent " + ev.getAction() + ":" +
	// ev.getX()
	// + ":" + ev.getY());
	// return super.onInterceptTouchEvent(ev);
	// }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("stage", "onTouchEvent " + event.getAction() + ":" + event.getX()
				+ ":" + event.getY());
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (child instanceof Poker) {
				Poker poker = (Poker) child;
				if (poker.getVisibility() == View.VISIBLE
						&& poker.contains(event.getX(), event.getY())) {
					return ((Poker) child).onTouchEvent(event);
				}
			}
		}
		return true;
	}

}
