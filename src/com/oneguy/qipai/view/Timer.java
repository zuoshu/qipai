package com.oneguy.qipai.view;

public interface Timer {
	public void onStart();
	public void onTimeChange(long duration);
	public void onTimeOut();
}
