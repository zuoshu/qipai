package com.oneguy.qipai.game;


public class CardInfo implements Comparable<CardInfo> {
	// 花色
	private int suit;
	// 点数
	private int count;
	// 比较大小时的顺序
	private int order;
	// 名字
	private String name;

	public CardInfo(String name, int suit, int count, int order) {
		this.name = name;
		this.suit = suit;
		this.count = count;
		this.order = order;
	}

	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int computeOrder() {
		return suit * order;
	}

	// public Bitmap getCardFace() {
	// return cardFace;
	// }
	//
	// public void setCardFace(Bitmap cardFace) {
	// this.cardFace = cardFace;
	// }

	@Override
	public int compareTo(CardInfo another) {
		if (order > another.order) {
			return -1;
		} else if (order < another.order) {
			return 1;
		} else {
			return another.suit - suit;
		}
	}
}
