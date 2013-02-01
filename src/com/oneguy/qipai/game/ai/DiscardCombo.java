package com.oneguy.qipai.game.ai;

import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.oneguy.qipai.Constants;
import com.oneguy.qipai.game.CardInfo;

/**
 * 牌组，记录一次出牌信息
 * 
 * @author Zuoshu
 * 
 */
public class DiscardCombo {
	public static final int MAX_CARD_LENGTH = 8;
	// 非法牌组
	public static final int ATTRIBUTE_INVALID = -1;

	// 将“不要”也表示成一次出牌
	public static final int ATTRIBUTE_PASS = -2;

	// 无，默认属性
	public static final int ATTRIBUTE_NONE = -3;

	// 单张
	public static final int ATTRIBUTE_SINGLE = 1;
	// 对子
	public static final int ATTRIBUTE_PAIR = 2;
	// 三张
	public static final int ATTRIBUTE_THREE = 3;
	// 假5 10 k
	public static final int ATTRIBUTE_510K_FAKE = 4;
	// 4张炸
	public static final int ATTRIBUTE_FOUR = 5;
	// 真5 10 k
	public static final int ATTRIBUTE_510K_FLUSH_SPADE = 6;
	public static final int ATTRIBUTE_510K_FLUSH_HEART = 7;
	public static final int ATTRIBUTE_510K_FLUSH_CLUB = 8;
	public static final int ATTRIBUTE_510K_FLUSH_DIAMOND = 9;
	// 5张炸弹
	public static final int ATTRIBUTE_FIVE = 10;
	// 6张炸弹
	public static final int ATTRIBUTE_SIX = 11;
	// 7张炸弹
	public static final int ATTRIBUTE_SEVEN = 12;
	// 8张炸弹
	public static final int ATTRIBUTE_EIGHT = 13;
	private List<CardInfo> cards;
	private int mAttribute;
	// 单张，对子，三张，炸弹的时候记录比较大小的顺序，一般为点数
	private int mOrder;

	// 出牌玩家座次，记录出牌顺序
	private int mPlayerSeat;

	public DiscardCombo(int seat) {
		this(null, seat, ATTRIBUTE_NONE);
	}

	public DiscardCombo(List<CardInfo> cards, int seat, int attribute) {
		mPlayerSeat = seat;
		this.cards = cards;
		if (cards == null || cards.size() == 0
				|| cards.size() > MAX_CARD_LENGTH) {
			mAttribute = ATTRIBUTE_NONE;
		} else {
			mOrder = cards.get(0).getOrder();
			mAttribute = attribute;
		}
		// Collections.sort(cards);
		if (mAttribute == ATTRIBUTE_INVALID) {
			String str = "";
			for (CardInfo card : cards) {
				str += card.getName() + "|";
			}
			Log.d("DiscardCombo", "invalid:" + str);
			throw new IllegalArgumentException("牌组不合法");

		} else if (mAttribute == ATTRIBUTE_NONE) {
			mOrder = Constants.ORDER_NONE;
		}
	}

	public int getArrtibute() {
		return mAttribute;
	}

	public int getOrder() {
		return mOrder;
	}

	public int getSeat() {
		return mPlayerSeat;
	}

	public List<CardInfo> getCards() {
		return cards;
	}

	public void setAttribute(int attribute) {
		mAttribute = attribute;
	}
}
