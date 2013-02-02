package com.oneguy.qipai.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;
import com.oneguy.qipai.game.ai.DiscardCombo;
import com.oneguy.qipai.view.PlayerInfo;
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
	private PlayerInfo mPlayerInfoView;
	private ImageView mPassImage;
	private DiscardCombo mDiscard;

	public Player() {
		mCards = new LinkedList<Poker>();
		score = 0;
		mPlayerInfoView = new PlayerInfo();
		setPassImage();
	}

	private void setPassImage() {
		mPassImage = new ImageView(QianfenApplication.getInstance());
		mPassImage.setImageResource(R.drawable.pass);
		mPassImage.setVisibility(View.GONE);
	}

	public void addCard(Poker c) {
		mCards.add(c);
	}

	public void sortCards() {
		Collections.sort(mCards);
		mPlayerInfoView.setCardCount(mCards.size());
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
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		mPlayerInfoView.setScore(String.valueOf(score));
	}

	public void addScore(int score) {
		this.score += score;
		mPlayerInfoView.setScore(String.valueOf(this.score));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		mPlayerInfoView.setName(name);
	}

	public String toString() {
		return "name:" + name + " score:" + score + " seat:" + seat
				+ " sequence:" + sequence + " card:" + mCards.size();
	}

	public void clearCards() {
		if (mCards != null) {
			mCards.clear();
		}
		mPlayerInfoView.setCardCount(0);
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

	public void showCard(Poker poker) {
		if (poker != null) {
			poker.setVisibility(View.VISIBLE);
			poker.bringToFront();
			poker.invalidate();
		}
	}

	public void moveCardsToDeck(DiscardCombo discard) {
		ResourceManger resourceManager = ResourceManger.getInstance();
		if (seat != SEAT_BOTTOM && seat != SEAT_RIGHT && seat != SEAT_UP
				&& seat != SEAT_LEFT && (mCards == null || mCards.size() == 0)) {
			return;
		}
		if (discard.getArrtibute() == DiscardCombo.ATTRIBUTE_PASS
				|| discard.getArrtibute() == DiscardCombo.ATTRIBUTE_NONE
				|| discard.getArrtibute() == DiscardCombo.ATTRIBUTE_INVALID) {
			return;
		}
		int startX = 0, endX = 0, y = 0, cardSpacing = 0;
		if (seat == SEAT_BOTTOM) {
			startX = resourceManager
					.getHorizontalDimen(R.string.horizontal_discard_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.horizontal_discard_end_x_percent);
			y = resourceManager
					.getVerticalDimen(R.string.bottom_discard_y_percent);
		} else if (seat == SEAT_RIGHT) {
			startX = resourceManager
					.getHorizontalDimen(R.string.right_discard_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.right_discard_end_x_percent);
			y = resourceManager
					.getVerticalDimen(R.string.right_discard_y_percent);
		} else if (seat == SEAT_UP) {
			startX = resourceManager
					.getHorizontalDimen(R.string.horizontal_discard_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.horizontal_discard_end_x_percent);
			y = resourceManager.getVerticalDimen(R.string.up_discard_y_percent);
		} else if (seat == SEAT_LEFT) {
			startX = resourceManager
					.getHorizontalDimen(R.string.left_discard_start_x_percent);
			endX = resourceManager
					.getHorizontalDimen(R.string.left_discard_end_x_percent);
			y = resourceManager
					.getVerticalDimen(R.string.left_discard_y_percent);
		}
		int cardsMaxHorizontalSpacing = resourceManager
				.getHorizontalDimen(R.string.cards_horizontal_max_spacing);
		List<CardInfo> cards = discard.getCards();
		cardSpacing = Math.min(cardsMaxHorizontalSpacing, (endX - startX)
				/ cards.size());
		HashMap<String, Poker> pokers = resourceManager.getPokerMap();
		for (int i = 0; i < cards.size(); i++) {
			CardInfo card = cards.get(i);
			Poker poker = pokers.get(card.getName());
			poker.setSelected(false);
			poker.set(startX + i * cardSpacing, y);
			poker.setFrontFace(true);
			showCard(poker);
		}
	}

	public void removeCardsFromHand(DiscardCombo discard) {
		List<CardInfo> cards = discard.getCards();
		ResourceManger res = ResourceManger.getInstance();
		HashMap<String, Poker> pokers = res.getPokerMap();
		for (CardInfo ci : cards) {
			Poker p = pokers.get(ci.getName());
			mCards.remove(p);
		}
	}

	public void discard(DiscardCombo discard) {
		mDiscard = discard;
		moveCardsToDeck(mDiscard);
		removeCardsFromHand(mDiscard);
		updateCardCount();
	}

	public void removeDiscard() {
		if (mDiscard == null || mDiscard.getCards() == null
				|| mDiscard.getCards().size() == 0) {
			return;
		}
		List<CardInfo> cards = mDiscard.getCards();
		ResourceManger res = ResourceManger.getInstance();
		HashMap<String, Poker> pokers = res.getPokerMap();
		for (CardInfo ci : cards) {
			Poker p = pokers.get(ci.getName());
			p.setVisibility(View.GONE);
			p.invalidate();
		}
		hidePassLable();
	}

	public void updateCardCount() {
		mPlayerInfoView.setCardCount(mCards.size());
	}

	public View getInfoView() {
		return mPlayerInfoView.getView();
	}

	public ImageView getPassLable() {
		return mPassImage;
	}

	public void showPassLable() {
		mPassImage.setVisibility(View.VISIBLE);
		mPassImage.invalidate();
	}

	public void hidePassLable() {
		mPassImage.setVisibility(View.GONE);
		mPassImage.invalidate();
	}

	public void deselectAllCards() {
		for (Poker poker : mCards) {
			poker.setSelected(false);
		}
	}

	public boolean isRunout() {
		return mCards.size() == 0;
	}

	public void setHandCardFronFace(boolean isFrontFace) {
		if (mCards == null || mCards.size() == 0) {
			return;
		}
		for (Poker p : mCards) {
			p.setFrontFace(isFrontFace);
		}
	}

	public void selectDiscard(DiscardCombo discard) {
		if (discard == null || discard.getCards() == null
				|| discard.getCards().size() == 0) {
			return;
		}
		List<Poker> pokers = ResourceManger.getInstance().getPokers(
				discard.getCards());
		for (Poker p : pokers) {
			p.setSelected(true);
		}
	}

	public List<CardInfo> getSelectedCards() {
		List<CardInfo> cards = new ArrayList<CardInfo>();
		for (Poker p : mCards) {
			if (p.isSelected()) {
				cards.add(p.getCardInfo());
			}
		}
		return cards;
	}

	public void reset() {
		mPlayerInfoView.getView().setBackgroundColor(Color.WHITE);
		setScore(0);
		setName("");
		clearCards();
	}
}
