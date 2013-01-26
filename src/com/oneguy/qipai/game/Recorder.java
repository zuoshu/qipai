package com.oneguy.qipai.game;

import java.util.ArrayList;

import com.oneguy.qipai.entity.Player;
import com.oneguy.qipai.game.ai.DiscardCombo;

public class Recorder {
	// 轮到谁出牌
	private Player[] mPlayers;
	private ArrayList<DiscardCombo> mRecords;
	private static final DiscardCombo CARD_COMBO_NONE = new DiscardCombo(
			Player.SEAT_NONE);

	public Recorder(Player[] players) {
		mPlayers = players;
		// not started
		mRecords = new ArrayList<DiscardCombo>();
	}

	/**
	 * 
	 * @return 返回上家出牌，如果一轮已经结束，则返回null.如果上家不要，则返回上上家出牌，依次类推
	 */
	public DiscardCombo getLastDiscard() {
		int count = mRecords.size() % 4;
		for (int i = count - 1; i >= 0; i--) {
			int arribute = mRecords.get(i).getArrtibute();
			if (arribute != DiscardCombo.ATTRIBUTE_NONE
					&& arribute != DiscardCombo.ATTRIBUTE_INVALID
					&& arribute != DiscardCombo.ATTRIBUTE_PASS) {
				return mRecords.get(i);
			}
		}
		return CARD_COMBO_NONE;
	}

	public void addRecord(DiscardCombo record) {
		mRecords.add(record);
	}

	/**
	 * 
	 * @return 返回上轮出牌，如果不够一轮则返回null
	 */
	public DiscardCombo[] getLastRound() {
		int size = mRecords.size();
		if (size < 4) {
			return null;
		}
		DiscardCombo[] data = new DiscardCombo[4];
		data[0] = mRecords.get(size - 4);
		data[1] = mRecords.get(size - 3);
		data[2] = mRecords.get(size - 2);
		data[3] = mRecords.get(size - 1);
		return data;
	}
}
