package com.oneguy.qipai;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.oneguy.qipai.BuildConfig;

public class QianfenApplication extends Application {
	private static QianfenApplication mInstance;
	public static int displayWidth;
	public static int displayHeight;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		setDisplayInfo();
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

	public static synchronized QianfenApplication getInstance() {
		if (mInstance == null) {
			mInstance = new QianfenApplication();
		}
		return mInstance;
	}

}
