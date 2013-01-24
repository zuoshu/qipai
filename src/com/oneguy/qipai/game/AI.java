package com.oneguy.qipai.game;

import android.util.Log;

import com.oneguy.qipai.entity.Player;

public class AI {
	private static final String TAG = "AI";
	private QianfenDirector mDirector;
	// 轮到谁出牌
	private int mPlayerSeatInAction;
	private Player[] players;

	public AI(QianfenDirector director) {
		mDirector = director;
		players = mDirector.getPlayers();
		if (players == null || players.length < 4) {
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

	/**
	 * 
	 * @param seat
	 *            玩家座次
	 * @return 返回出牌的数组，数组元素为要出的牌在手牌上的位置
	 * @return
	 */
	public int[] autoPlay(int seat) {
		if (seat != Player.SEAT_LEFT && seat != Player.SEAT_BOTTOM
				&& seat != Player.SEAT_RIGHT && seat != Player.SEAT_UP) {
			Log.e(TAG, "autoPlay,seat:" + seat);
			return null;
		}
		
		return null;
	}

	/**
	 * 检查玩家出牌是否合法
	 * 
	 * @param seat
	 *            玩家座次
	 * @param cards
	 *            要出的牌在手牌的位置
	 * @return 合法true 不合法false
	 */
	public boolean isShowCardsValid(int seat, int[] cards) {
		return true;
	}

}
