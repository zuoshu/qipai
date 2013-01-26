package com.oneguy.qipai.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;
import com.oneguy.qipai.game.ai.DiscardCombo;
import com.oneguy.qipai.view.Poker;

public class Player {
	// sequence
	public static final int PLAY0 = 0;
	public static final int PLAY1 = 1;
	public static final int PLAY2 = 2;
	public static final int PLAY3 = 3;
	// seating
	public static final int SEAT_NONE = -1;
	public static final int SEAT_BOTTOM = 0;
	public static final int SEAT_RIGHT = 1;
	public static final int SEAT_UP = 2;
	public static final int SEAT_LEFT = 3;
	public static final int SEAT_SELF = SEAT_BOTTOM;

	public static final int CARD_COUNT = 27;
	private int sequence;
	private int seat;
	private List<Poker> mCards;
	private int score;
	private String name;
	private View mInfoView;
	private TextView mPlayerName;
	private TextView mPlayerScore;

	public Player() {
		mCards = new LinkedList<Poker>();
		score = 0;
	}

	private void generateInfoView(int res) {
		mInfoView = View.inflate(QianfenApplication.getInstance(), res, null);
		mPlayerName = (TextView) mInfoView.findViewById(R.id.playerName);
		mPlayerScore = (TextView) mInfoView.findViewById(R.id.playerScore);
	}

	public void addCard(Poker c) {
		mCards.add(c);
	}

	public void sortCards() {
		Collections.sort(mCards);
	}

	public void beginMatch() {
		score = 0;
		mCards.clear();
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
		if (mInfoView == null && (seat == SEAT_LEFT || seat == SEAT_RIGHT)) {
			generateInfoView(R.layout.player_info_view_v);
		} else if (mInfoView == null
				&& (seat == SEAT_UP || seat == SEAT_BOTTOM)) {
			generateInfoView(R.layout.player_info_view_h);
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "name:" + name + " score:" + score + " seat:" + seat
				+ " sequence:" + sequence + " card:" + mCards.size();
	}

	public void clearCards() {
		if (mCards != null) {
			mCards.clear();
		}
	}

	public String getCardSequence() {
		if (mCards == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Poker p : mCards) {
			sb.append(p.getCardInfo().getName());
			sb.append('\n');
		}
		return sb.toString();
	}

	public View getInfoView() {
		if (mInfoView == null) {
			return null;
		}
		mPlayerName.setText(name);
		mPlayerScore.setText(String.valueOf(score));
		return mInfoView;
	}

	public List<Poker> getCards() {
		return mCards;
	}

	public void layoutCards() {
		ResourceManger resourceManager = ResourceManger.getInstance();
		if (seat != SEAT_BOTTOM && seat != SEAT_RIGHT && seat != SEAT_UP
				&& seat != SEAT_LEFT && (mCards == null || mCards.size() == 0)) {
			return;
		}
		int startX = 0, endX = 0, y = 0, cardSpacing = 0;
		if (seat == SEAT_BOTTOM) {
			startX = resourceManager
					.getHorizontalDimen(R.string.bottom_and_up_cards_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.bottom_and_up_cards_end_x_percent);
			y = resourceManager
					.getVerticalDimen(R.string.bottom_cards_y_percent);
		} else if (seat == SEAT_RIGHT) {
			startX = resourceManager
					.getHorizontalDimen(R.string.right_cards_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.right_cards_end_x_percent);
			y = resourceManager
					.getVerticalDimen(R.string.left_and_right_cards_y_percent);
		} else if (seat == SEAT_UP) {
			startX = resourceManager
					.getHorizontalDimen(R.string.bottom_and_up_cards_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.bottom_and_up_cards_end_x_percent);
			y = resourceManager.getVerticalDimen(R.string.up_cards_y_percent);
		} else if (seat == SEAT_LEFT) {
			startX = resourceManager
					.getHorizontalDimen(R.string.left_cards_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.left_cards_end_x_percent);
			y = resourceManager
					.getVerticalDimen(R.string.left_and_right_cards_y_percent);
		}
		int cardsMaxHorizontalSpacing = resourceManager
				.getHorizontalDimen(R.string.cards_horizontal_max_spacing);
		cardSpacing = Math.min(cardsMaxHorizontalSpacing, (endX - startX)
				/ mCards.size());
		for (int i = 0; i < mCards.size(); i++) {
			mCards.get(i).set(startX + i * cardSpacing, y);
		}
	}

	public void showCards() {
		for (Poker p : mCards) {
			p.setVisibility(View.VISIBLE);
			p.bringToFront();
		}
	}

	// TODO
	public void moveCardsToDeck(DiscardCombo discard) {
		List<CardInfo> cards = discard.getCards();
		ResourceManger res = ResourceManger.getInstance();
		HashMap<String, Poker> pokers = res.getPokerMap();
		for (CardInfo ci : cards) {
			Poker p = pokers.get(ci.getName());
			p.setVisibility(View.GONE);
			p.invalidate();
			mCards.remove(p);
		}
	}

	public void removeCardsFromHand(DiscardCombo discard) {
		// TODO Auto-generated method stub

	}

	public void discard(DiscardCombo discard) {
		moveCardsToDeck(discard);
		removeCardsFromHand(discard);
		sortCards();
	}
}
