package com.oneguy.qipai.game;

import java.util.ArrayList;

import com.oneguy.qipai.entity.Player;
import com.oneguy.qipai.game.ai.DiscardCombo;
import com.oneguy.qipai.game.ai.DiscardRecord;

public class Recorder {
	// 轮到谁出牌
	private Player[] mPlayers;
	private ArrayList<DiscardRecord> mRecords;
	// 当前是第几轮出牌
	private int mCurrentRound;
	// 当前是本轮出牌的第几手0,1,2,3
	private int mCurrentSequence;
	private static final DiscardCombo CARD_COMBO_NONE = new DiscardCombo(
			Player.SEAT_NONE);

	public Recorder(Player[] players) {
		mPlayers = players;
		// not started
		mRecords = new ArrayList<DiscardRecord>();
	}

	/**
	 * 
	 * @return 返回上家出牌，如果一轮已经结束，则返回null.如果上家不要，则返回上上家出牌，依次类推
	 */
	public DiscardCombo getLastDiscard() {
		int size = mRecords.size();
		for (int i = size - 1; i >= size - mCurrentSequence; i--) {
			int arribute = mRecords.get(i).getDiscardCombo().getArrtibute();
			if (arribute != DiscardCombo.ATTRIBUTE_NONE
					&& arribute != DiscardCombo.ATTRIBUTE_INVALID
					&& arribute != DiscardCombo.ATTRIBUTE_PASS) {
				return mRecords.get(i).getDiscardCombo();
			}
		}
		return CARD_COMBO_NONE;
	}

	public void addDiscard(DiscardCombo combo) {
		DiscardRecord record = new DiscardRecord();
		record.setDiscardCombo(combo);
		record.setRound(mCurrentRound);
		record.setSequence(mCurrentSequence);
		mRecords.add(record);
		increaseSequence();
	}

	private void increaseSequence() {
		if (mCurrentSequence == 3) {
			mCurrentSequence = 0;
			mCurrentRound++;
		} else {
			mCurrentSequence++;
		}
	}

	/**
	 * 
	 * @return 返回上轮出牌，如果不够一轮则返回null
	 */
	public DiscardRecord[] getLastRoundRecord() {
		int size = mRecords.size();
		if (size < 4) {
			return null;
		}
		DiscardRecord[] data = new DiscardRecord[4];
		data[0] = mRecords.get(size - 4);
		data[1] = mRecords.get(size - 3);
		data[2] = mRecords.get(size - 2);
		data[3] = mRecords.get(size - 1);
		return data;
	}

	public int getCurrentSequence() {
		return mCurrentSequence;
	}

	public int getCurrentRouns() {
		return mCurrentRound;
	}

	public DiscardRecord getLastRecordNotPass() {
		for (int i = mRecords.size() - 1; i >= 0; i--) {
			if (mRecords.get(i).getDiscardCombo().getArrtibute() != DiscardCombo.ATTRIBUTE_PASS) {
				return mRecords.get(i);
			}
		}
		return null;
	}
}
