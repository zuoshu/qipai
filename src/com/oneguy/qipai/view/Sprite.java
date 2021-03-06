package com.oneguy.qipai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Sprite extends View {
	private Stage mStage;

	public Sprite(Context context) {
		super(context);
	}

	public Sprite(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Sprite(Context context, int width, int height) {
		super(context);
		setLayoutParams(new LayoutParams(width, height));
	}

	public void setStage(Stage s) {
		mStage = s;
	}

	public Stage getStage() {
		return mStage;
	}

	public void setX(int x) {
		LayoutParams lp = (LayoutParams) getLayoutParams();
		lp.leftMargin = x;
		requestLayout();
	}

	public void setY(int y) {
		LayoutParams lp = (LayoutParams) getLayoutParams();
		lp.topMargin = y;
		requestLayout();
	}

	public void set(int x, int y) {
		LayoutParams lp = (LayoutParams) getLayoutParams();
		lp.leftMargin = x;
		lp.topMargin = y;
		requestLayout();
	}

	public int getX() {
		LayoutParams lp = (LayoutParams) getLayoutParams();
		if (lp != null) {
			return lp.leftMargin;
		} else {
			return -1;
		}
	}

	public int getY() {
		LayoutParams lp = (LayoutParams) getLayoutParams();
		if (lp != null) {
			return lp.topMargin;
		} else {
			return -1;
		}
	}

}
