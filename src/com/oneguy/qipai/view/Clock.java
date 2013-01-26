package com.oneguy.qipai.view;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.oneguy.qipai.BuildConfig;

public class Clock extends Button implements Timer {

	private long mStartTime;
	// 计时总时间
	private long mTotalTime;
	private long mLastupdateTime;
	static final long CLOCK_UPDATE_DURATION = 10;
	private Runnable mClockUpdateRunnable;
	private OnTimeOutListener mListener;
	private static final String TAG = "Clock";

	public Clock(Context context) {
		super(context);
		mClockUpdateRunnable = new Runnable() {

			@Override
			public void run() {
				long currentTime = System.currentTimeMillis();

				long duration = currentTime - mStartTime;

				if (BuildConfig.DEBUG) {
//					Log.d(TAG, duration + "");
				}

				mTotalTime -= (currentTime - mLastupdateTime);
				mLastupdateTime = currentTime;
				if (mTotalTime <= 0) {
					mTotalTime = 0;
					onTimeChange(duration);
					onTimeOut();
				} else {
					Clock.this.postDelayed(this, CLOCK_UPDATE_DURATION);
					onTimeChange(duration);
				}
			}
		};
	}

	/**
	 * 设置定时事件
	 * 
	 * @param time
	 *            定时时间，单位毫秒
	 */
	public Clock setClock(long time) {
		if (time > 0) {
			mTotalTime = time;
		}
		return this;
	}

	public void start(OnTimeOutListener listener, long time) {
		setClock(time);
		setOnTimeOutListener(listener);
		start();
	}

	public void start() {
		if (mTotalTime <= 0) {
			return;
		}
		onStart();
		mStartTime = System.currentTimeMillis();
		mLastupdateTime = mStartTime;
		this.postDelayed(mClockUpdateRunnable, CLOCK_UPDATE_DURATION);
	}

	@Override
	public void onStart() {
		setText(String.valueOf((int) (mTotalTime / 1000)));
	}

	@Override
	public void onTimeChange(long duration) {
		setText(String.valueOf((int) (mTotalTime / 1000)));
	}

	@Override
	public void onTimeOut() {
		if (mListener != null) {
			mListener.onTimeOut();
		}
	}

	public void setOnTimeOutListener(OnTimeOutListener listener) {
		mListener = listener;
	}

	public interface OnTimeOutListener {
		public void onTimeOut();
	}
	
	public void cancleTiming() {
		this.removeCallbacks(mClockUpdateRunnable);
	}
}
