package com.oneguy.qipai.sound;

public interface SoundManager {
	public void playActionSound(String action);

	public void playBgMusic(int resId);

	public void stopBgMusic();
}
