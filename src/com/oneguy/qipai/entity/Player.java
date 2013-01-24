package com.oneguy.qipai.entity;

import java.util.ArrayList;
import java.util.Collections;

import android.view.View;
import android.widget.TextView;

import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;
import com.oneguy.qipai.view.Poker;
import com.oneguy.qipai.view.Stage;

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

	public static final int CARD_COUNT = 27;
	private int sequence;
	private int seat;
	private ArrayList<Poker> cards;
	private int score;
	private String name;
	private View mInfoView;
	private TextView mPlayerName;
	private TextView mPlayerScore;

	private Stage mStage;

	public Player(Stage stage) {
		cards = new ArrayList<Poker>(CARD_COUNT);
		score = 0;
		mStage = stage;
	}

	private void generateInfoView(int res) {
		mInfoView = View.inflate(QianfenApplication.getInstance(), res, null);
		mPlayerName = (TextView) mInfoView.findViewById(R.id.playerName);
		mPlayerScore = (TextView) mInfoView.findViewById(R.id.playerScore);
	}

	public void addCard(Poker c) {
		cards.add(c);
	}

	public void sortCards() {
		Collections.sort(cards);
	}

	public void beginMatch() {
		score = 0;
		cards.clear();
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
				+ " sequence:" + sequence + " card:" + cards.size();
	}

	public void clearCards() {
		if (cards != null) {
			cards.clear();
		}
	}

	public String getCardSequence() {
		if (cards == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Poker p : cards) {
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

	public ArrayList<Poker> getCards() {
		return cards;
	}

	public void layoutCards() {
		ResourceManger resourceManager = ResourceManger.getInstance();
		if (seat != SEAT_BOTTOM && seat != SEAT_RIGHT && seat != SEAT_UP
				&& seat != SEAT_LEFT && (cards == null || cards.size() == 0)) {
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
				/ cards.size());
		for (int i = 0; i < cards.size(); i++) {
			cards.get(i).set(startX + i * cardSpacing, y);
		}
	}

	public void showCards() {
		for (Poker p : cards) {
			p.setVisibility(View.VISIBLE);
			p.bringToFront();
		}
	}
}
