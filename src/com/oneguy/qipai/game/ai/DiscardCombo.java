package com.oneguy.qipai.game.ai;

import java.util.Collections;
import java.util.List;

import com.oneguy.qipai.Constants;
import com.oneguy.qipai.entity.CardInfo;

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
		this(null, seat);
	}

	public DiscardCombo(List<CardInfo> cards, int seat) {
		mPlayerSeat = seat;
		this.cards = cards;
		mAttribute = generateAttribute(cards);
		if (mAttribute == ATTRIBUTE_INVALID) {
			throw new IllegalArgumentException("牌组不合法");
		}else if (mAttribute == ATTRIBUTE_NONE){
			mOrder = Constants.ORDER_NONE;
		}
	}

	private int generateAttribute(List<CardInfo> cards) {
		if (cards == null || cards.size() == 0
				|| cards.size() > MAX_CARD_LENGTH) {
			return ATTRIBUTE_NONE;
		}
//		Collections.sort(cards);
		int cardCount = cards.size();
		mOrder = cards.get(0).getOrder();
		if (cardCount == 1) {
			return ATTRIBUTE_SINGLE;
		} else if (cardCount == 2) {
			if (cards.get(0).getCount() == cards.get(1).getCount()) {
				return ATTRIBUTE_PAIR;
			} else {
				return ATTRIBUTE_INVALID;
			}
		} else if (cardCount == 3) {
			// 可能是三张也可能是 5 10 k
			int count0 = cards.get(0).getCount();
			int count1 = cards.get(1).getCount();
			int count2 = cards.get(2).getCount();
			int suit0 = cards.get(0).getSuit();
			int suit1 = cards.get(1).getSuit();
			int suit2 = cards.get(2).getSuit();
			// 三张
			if (count0 == count1 && count0 == count2) {
				return ATTRIBUTE_THREE;
			} else if (count0 == 5 && count1 == 10 && count2 == 13) {
				if (suit0 == Constants.SUIT_SPADE
						&& suit1 == Constants.SUIT_SPADE
						&& suit2 == Constants.SUIT_SPADE) {
					// 黑桃真510k
					return ATTRIBUTE_510K_FLUSH_SPADE;
				} else if (suit0 == Constants.SUIT_HEART
						&& suit1 == Constants.SUIT_HEART
						&& suit2 == Constants.SUIT_HEART) {
					// 红桃真510k
					return ATTRIBUTE_510K_FLUSH_HEART;
				} else if (suit0 == Constants.SUIT_CLUB
						&& suit1 == Constants.SUIT_CLUB
						&& suit2 == Constants.SUIT_CLUB) {
					// 梅花真510k
					return ATTRIBUTE_510K_FLUSH_CLUB;
				} else if (suit0 == Constants.SUIT_DIAMOND
						&& suit1 == Constants.SUIT_DIAMOND
						&& suit2 == Constants.SUIT_DIAMOND) {
					// 方块真510k
					return ATTRIBUTE_510K_FLUSH_DIAMOND;
				} else {
					// 假510k
					return ATTRIBUTE_510K_FAKE;
				}
			} else {
				return ATTRIBUTE_INVALID;
			}
		} else if (cardCount == 4) {
			int count0 = cards.get(0).getCount();
			int count1 = cards.get(1).getCount();
			int count2 = cards.get(2).getCount();
			int count3 = cards.get(3).getCount();
			if (count0 == count1 && count0 == count2 && count0 == count3) {
				return ATTRIBUTE_FOUR;
			} else {
				return ATTRIBUTE_INVALID;
			}
		} else if (cardCount == 5) {
			int count0 = cards.get(0).getCount();
			int count1 = cards.get(1).getCount();
			int count2 = cards.get(2).getCount();
			int count3 = cards.get(3).getCount();
			int count4 = cards.get(4).getCount();
			if (count0 == count1 && count0 == count2 && count0 == count3
					&& count0 == count4) {
				return ATTRIBUTE_FIVE;
			} else {
				return ATTRIBUTE_INVALID;
			}
		} else if (cardCount == 6) {
			int count0 = cards.get(0).getCount();
			int count1 = cards.get(1).getCount();
			int count2 = cards.get(2).getCount();
			int count3 = cards.get(3).getCount();
			int count4 = cards.get(4).getCount();
			int count5 = cards.get(5).getCount();
			if (count0 == count1 && count0 == count2 && count0 == count3
					&& count0 == count4 && count0 == count5) {
				return ATTRIBUTE_SIX;
			} else {
				return ATTRIBUTE_INVALID;
			}
		} else if (cardCount == 7) {
			int count0 = cards.get(0).getCount();
			int count1 = cards.get(1).getCount();
			int count2 = cards.get(2).getCount();
			int count3 = cards.get(3).getCount();
			int count4 = cards.get(4).getCount();
			int count5 = cards.get(5).getCount();
			int count6 = cards.get(6).getCount();
			if (count0 == count1 && count0 == count2 && count0 == count3
					&& count0 == count4 && count0 == count5 && count0 == count6) {
				return ATTRIBUTE_SEVEN;
			} else {
				return ATTRIBUTE_INVALID;
			}
		} else if (cardCount == 8) {
			int count0 = cards.get(0).getCount();
			int count1 = cards.get(1).getCount();
			int count2 = cards.get(2).getCount();
			int count3 = cards.get(3).getCount();
			int count4 = cards.get(4).getCount();
			int count5 = cards.get(5).getCount();
			int count6 = cards.get(6).getCount();
			int count7 = cards.get(7).getCount();
			if (count0 == count1 && count0 == count2 && count0 == count3
					&& count0 == count4 && count0 == count5 && count0 == count6
					&& count0 == count7) {
				return ATTRIBUTE_EIGHT;
			} else {
				return ATTRIBUTE_INVALID;
			}
		}
		return ATTRIBUTE_INVALID;
	}

	public int getArrtibute() {
		return mAttribute;
	}

	public int getOrder() {
		return mOrder;
	}
	public int getSeat(){
		return mPlayerSeat;
	}
	
	public List<CardInfo> getCards(){
		return cards;
	}
	public void setAttribute(int attribute){
		mAttribute = attribute;
	}
}
