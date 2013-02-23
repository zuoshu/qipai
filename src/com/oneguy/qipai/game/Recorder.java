package com.oneguy.qipai.game;

import java.util.ArrayList;

import android.util.Log;

import com.oneguy.qipai.game.ai.DiscardCombo;
import com.oneguy.qipai.game.ai.DiscardRecord;

public class Recorder {
	private static final String TAG = "Recorder";
	// 轮到谁出牌
	private Player[] mPlayers;
	private ArrayList<DiscardRecord> mRecords;
	private ArrayList<DiscardRecord> mLastRoundRecords;
	// 当前是第几轮出牌
	private int mCurrentRound;
	// 当前是本轮出牌的第几手0,1,2,3
	private int mCurrentSequence;
	private static final DiscardCombo CARD_COMBO_NONE = new DiscardCombo(
			Player.SEAT_NONE);
	private int mCurrentRank;

	public enum PlayerStatus {
		RUN_OUT // 出完了
		, KEEP // 没出完
	}

	private int[] mPlayerRanking;// 记录大游，二游，三游，四游
	private PlayerStatus[] mPlayerStatus;// 记录每个玩家是否已出完

	public Recorder(Player[] players) {
		mPlayers = players;
		// not started
		mRecords = new ArrayList<DiscardRecord>();
		mLastRoundRecords = new ArrayList<DiscardRecord>();
		mPlayerRanking = new int[4];
		mPlayerStatus = new PlayerStatus[4];
		resetRanking();
		resetStatus();
	}

	private void resetRanking() {
		mCurrentRank = 1;
		for (int i = 0; i < mPlayerRanking.length; i++) {
			mPlayerRanking[i] = -1;
		}
	}

	private void resetStatus() {
		for (int i = 0; i < mPlayerStatus.length; i++) {
			mPlayerStatus[i] = PlayerStatus.KEEP;
		}
	}

	/**
	 * 
	 * @return 返回上家出牌，如果一轮已经结束，则返回null.如果上家不要，则返回上上家出牌，依次类推
	 */
	public DiscardCombo getLastDiscard() {
		int size = mLastRoundRecords.size();
		for (int i = size - 1; i >= 0; i--) {
			int arribute = mLastRoundRecords.get(i).getDiscardCombo().getArrtibute();
			if (arribute != DiscardCombo.ATTRIBUTE_NONE
					&& arribute != DiscardCombo.ATTRIBUTE_INVALID
					&& arribute != DiscardCombo.ATTRIBUTE_PASS) {
				return mLastRoundRecords.get(i).getDiscardCombo();
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
		mLastRoundRecords.add(record);
		increaseSequence();
	}

	private void increaseSequence() {
		if (mCurrentSequence == getCurrentPlayerCount() - 1) {
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
	public ArrayList<DiscardRecord> getLastRoundRecord() {
		return mLastRoundRecords;
	}

	public int countLastRoundScore() {
		int score = 0;
		int size = mLastRoundRecords.size();
		if (size <= 0) {
			return score;
		}
		for (DiscardRecord record : mLastRoundRecords) {
			score += countDiscardRecordScore(record);
		}
		return score;
	}

	private int countDiscardRecordScore(DiscardRecord record) {
		int score = 0;
		if (record == null || record.getDiscardCombo() == null) {
			return score;
		}
		int att = record.getDiscardCombo().getArrtibute();
		if (att == DiscardCombo.ATTRIBUTE_NONE
				|| att == DiscardCombo.ATTRIBUTE_INVALID
				|| att == DiscardCombo.ATTRIBUTE_PASS) {
			return score;
		}
		for (CardInfo card : record.getDiscardCombo().getCards()) {
			if (card.getCount() == 5) {
				score += 5;
			} else if (card.getCount() == 10) {
				score += 10;
			} else if (card.getCount() == 13) {
				score += 10;
			}
		}
		return score;
	}

	public int getCurrentSequence() {
		return mCurrentSequence;
	}

	public int getCurrentRound() {
		return mCurrentRound;
	}

	public DiscardRecord getLastRecordNotPass() {
		for (int i = mLastRoundRecords.size() - 1; i >= 0; i--) {
			if (mLastRoundRecords.get(i).getDiscardCombo().getArrtibute() != DiscardCombo.ATTRIBUTE_PASS) {
				return mLastRoundRecords.get(i);
			}
		}
		return null;
	}

	public int getLastRoundWinner() {
		DiscardRecord record = getLastRecordNotPass();
		if (record == null) {
			Log.e(TAG, "error!round not finish!");
			return 0;
		}
		return record.getDiscardCombo().getSeat();
	}

	public int getCurrentPlayerCount() {
		int i = 0;
		for (PlayerStatus ps : mPlayerStatus) {
			if (ps.equals(PlayerStatus.KEEP)) {
				i++;
			}
		}
		return i;
	}

	public void markPlayerRunout(int seat) {
		mPlayerStatus[seat] = PlayerStatus.RUN_OUT;
		mPlayerRanking[seat] = mCurrentRank;
		mCurrentRank++;
		if (mCurrentSequence == 0) {
			mCurrentSequence = getCurrentPlayerCount() - 1;
		} else {
			mCurrentSequence--;
		}
	}

	public PlayerStatus getPlayerStatus(int seat) {
		return mPlayerStatus[seat];
	}

	public int getPlayerRanking(int seat) {
		if (seat < 0 || seat > 3) {
			return -1;
		}
		return mPlayerRanking[seat];
	}

	public void markLastRunout() {
		for (int i = 0; i < 4; i++) {
			if (mPlayerRanking[i] == -1) {
				mPlayerRanking[i] = mCurrentRank;
			}
		}
	}

	public void clearLastRoundRecord() {
		mLastRoundRecords.clear();
	}

	public void reset() {
		mCurrentRound = 0;
		mCurrentSequence = 0;
		mCurrentRank = 1;
		mRecords.clear();
		mLastRoundRecords.clear();
		resetStatus();
		resetRanking();
	}

	private int countContinuousLastRoundPass() {
		if (mLastRoundRecords == null || mLastRoundRecords.size() == 0) {
			return 0;
		}
		int count = 0;
		for (int i = mLastRoundRecords.size() - 1; i >= 0; i--) {
			if (mLastRoundRecords.get(i).getDiscardCombo().getArrtibute() == DiscardCombo.ATTRIBUTE_PASS) {
				count++;
			}
		}
		return count;
	}

	private int countRunout() {
		int count = 0;
		for (PlayerStatus ps : mPlayerStatus) {
			if (ps.equals(PlayerStatus.RUN_OUT)) {
				count++;
			}
		}
		return count;
	}

	public boolean isOneRoundFinish() {
		if (mLastRoundRecords == null || mLastRoundRecords.size() == 0) {
			return false;
		}
		int passCount = countContinuousLastRoundPass();
		int runOutCount = countRunout();
		return (passCount + runOutCount == 3);
	}
}
