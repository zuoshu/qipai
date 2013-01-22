package com.oneguy.qipai.qianfen.logic;

import com.oneguy.qipai.qianfen.game.Player;
import com.oneguy.qipai.qianfen.game.QianfenDirector;

public class Judge {
	private QianfenDirector mDirector;
	// 轮到谁出牌
	private int mPlayerSeatInAction;
	private Player[] players;

	public Judge(QianfenDirector director) {
		mDirector = director;
		players = mDirector.getPlayers();
		if(players == null || players.length<4) {
			throw new IllegalArgumentException("players not ready!");
		}
		// not started
		mPlayerSeatInAction = -1;
	}

	public void setWhoIsFirst(int seat) {
		this.mPlayerSeatInAction = seat;
	}
	
	public int getInActionPlayerSeat() {
		return mPlayerSeatInAction;
	}

}
