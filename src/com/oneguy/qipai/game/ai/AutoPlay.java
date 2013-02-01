package com.oneguy.qipai.game.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.oneguy.qipai.BuildConfig;
import com.oneguy.qipai.Constants;
import com.oneguy.qipai.game.CardInfo;
import com.oneguy.qipai.game.Player;
import com.oneguy.qipai.game.Recorder;
import com.oneguy.qipai.game.Recorder.PlayerStatus;
import com.oneguy.qipai.game.control.QianfenDirector;
import com.oneguy.qipai.view.Poker;

public class AutoPlay {
	private static final String TAG = "AI";
	private QianfenDirector mDirector;
	// 轮到谁出牌
	private int mPlayerSeatInAction;
	private Player[] mPlayers;
	private Recorder mRecorder;

	public AutoPlay(Recorder recorder, Player[] players) {
		mRecorder = recorder;
		mPlayers = players;
	}

	public AutoPlay(QianfenDirector director) {
		mDirector = director;
		mPlayers = mDirector.getPlayers();
		if (mPlayers == null || mPlayers.length < 4) {
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

	public void takeTurns() {
		if (isOneRoundFinish()) {
			mPlayerSeatInAction = pickStartOfNewRound();
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "mPlayerSeatInAction->" + mPlayerSeatInAction);
			}
		} else {
			mPlayerSeatInAction = (mPlayerSeatInAction + 1) % 4;
			while (mRecorder.getPlayerStatus(mPlayerSeatInAction).equals(
					PlayerStatus.RUN_OUT)) {
				mPlayerSeatInAction = (mPlayerSeatInAction + 1) % 4;
			}
		}
	}

	public boolean isOneRoundFinish() {
		return mRecorder.getCurrentSequence() == 0
				&& mRecorder.getCurrentRound() > 0;
	}

	private int pickStartOfNewRound() {
		DiscardRecord record = mRecorder.getLastRecordNotPass();
		if (record == null) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "impossible!getLastRecordNotPass null!");
			}
			return 0;
		}
		int seat = record.getDiscardCombo().getSeat();
		while (mRecorder.getPlayerStatus(seat).equals(PlayerStatus.RUN_OUT)) {
			seat = (seat + 1) % 4;
		}
		return seat;
	}

	public DiscardCombo discard() {
		List<DiscardCombo> discardList = pickDiscardList();
		DiscardCombo discard = null;
		if (discardList == null || discardList.size() == 0) {
			// pass
			discard = new DiscardCombo(mPlayerSeatInAction);
			discard.setAttribute(DiscardCombo.ATTRIBUTE_PASS);
		} else {
			Random random = new Random();
			int index = random.nextInt(discardList.size());
			discard = discardList.get(index);
		}
		return discard;
	}

	public List<DiscardCombo> pickDiscardList() {
		List<DiscardCombo> result = new ArrayList<DiscardCombo>();
		if (mPlayerSeatInAction != Player.SEAT_LEFT
				&& mPlayerSeatInAction != Player.SEAT_BOTTOM
				&& mPlayerSeatInAction != Player.SEAT_RIGHT
				&& mPlayerSeatInAction != Player.SEAT_UP) {
			return result;
		}
		Player player = mPlayers[mPlayerSeatInAction];
		List<Poker> handCard = player.getCards();
		int seat = player.getSeat();
		int flagArrtibute = DiscardCombo.ATTRIBUTE_NONE;
		DiscardCombo lastDiscard = mRecorder.getLastDiscard();
		if (lastDiscard != null) {
			flagArrtibute = lastDiscard.getArrtibute();
		}
		if (handCard == null || handCard.size() == 0) {
			return result;
		}
		boolean end = false;
		while (!end) {
			switch (flagArrtibute) {
			case DiscardCombo.ATTRIBUTE_INVALID:
			case DiscardCombo.ATTRIBUTE_PASS:
				// TODO 不合法
				break;
			case DiscardCombo.ATTRIBUTE_NONE:
				pickSingle(seat, handCard, lastDiscard, result);
				pickPair(seat, handCard, lastDiscard, result);
				pickThree(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FAKE;
				break;
			case DiscardCombo.ATTRIBUTE_SINGLE:
				pickSingle(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FAKE;
				break;
			case DiscardCombo.ATTRIBUTE_PAIR:
				pickPair(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FAKE;
				break;
			case DiscardCombo.ATTRIBUTE_THREE:
				pickThree(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FAKE;
				break;
			case DiscardCombo.ATTRIBUTE_510K_FAKE:
				// all fake 510k are equal,skip pick fake 510k if last discard
				// is fake 510k
				if (lastDiscard.getArrtibute() != DiscardCombo.ATTRIBUTE_510K_FAKE) {
					pickFake510K(seat, handCard, lastDiscard, result);
				}
				flagArrtibute = DiscardCombo.ATTRIBUTE_FOUR;
				break;
			case DiscardCombo.ATTRIBUTE_FOUR:
				pickFour(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FLUSH_DIAMOND;
				break;
			case DiscardCombo.ATTRIBUTE_510K_FLUSH_DIAMOND:
				pickDiamond510K(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FLUSH_CLUB;
				break;
			case DiscardCombo.ATTRIBUTE_510K_FLUSH_CLUB:
				pickClub510K(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FLUSH_HEART;
				break;
			case DiscardCombo.ATTRIBUTE_510K_FLUSH_HEART:
				pickHeart510K(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_510K_FLUSH_SPADE;
				break;
			case DiscardCombo.ATTRIBUTE_510K_FLUSH_SPADE:
				pickSpade510K(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_FIVE;
				break;
			case DiscardCombo.ATTRIBUTE_FIVE:
				pickFive(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_SIX;
				break;
			case DiscardCombo.ATTRIBUTE_SIX:
				pickSix(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_SEVEN;
				break;
			case DiscardCombo.ATTRIBUTE_SEVEN:
				pickSeven(seat, handCard, lastDiscard, result);
				flagArrtibute = DiscardCombo.ATTRIBUTE_EIGHT;
				break;
			case DiscardCombo.ATTRIBUTE_EIGHT:
				pickEight(seat, handCard, lastDiscard, result);
				end = true;
				break;
			}
		}
		return result;
	}

	private void pickSingle(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickSame(seat, handCard, lastDiscard, result, 1,
				DiscardCombo.ATTRIBUTE_SINGLE);
	}

	private void pickPair(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// pickSame(seat, handCard, lastDiscard, result, 2);
	}

	private void pickThree(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// pickSame(seat, handCard, lastDiscard, result, 3);
	}

	private void pickFake510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// pick K
		List<CardInfo> listOfK = pickCardInSortedCards(handCard, 13);
		if (listOfK == null || listOfK.size() == 0) {
			return;
		}
		// pick 10
		List<CardInfo> listOf10 = pickCardInSortedCards(handCard, 10);
		if (listOf10 == null || listOf10.size() == 0) {
			return;
		}
		// pick 5
		List<CardInfo> listOf5 = pickCardInSortedCards(handCard, 5);
		if (listOf5 == null || listOf5.size() == 0) {
			return;
		}
		// combine
		for (CardInfo pokerK : listOfK) {
			for (CardInfo poker10 : listOf10) {
				for (CardInfo poker5 : listOf5) {
					// when 5 10 k suit not all equal
					if (!(pokerK.getSuit() == poker10.getSuit() && pokerK
							.getSuit() == poker5.getSuit())) {
						ArrayList<CardInfo> cards = new ArrayList<CardInfo>();
						cards.add(pokerK);
						cards.add(poker10);
						cards.add(poker5);
						result.add(new DiscardCombo(cards, seat,
								DiscardCombo.ATTRIBUTE_510K_FAKE));
					}
				}
			}
		}
	}

	private void pickFour(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// pickSame(seat, handCard, lastDiscard, result, 4);
	}

	private void pickDiamond510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickFlush510K(seat, handCard, lastDiscard, result,
				Constants.SUIT_DIAMOND);
	}

	private void pickClub510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickFlush510K(seat, handCard, lastDiscard, result, Constants.SUIT_CLUB);
	}

	private void pickHeart510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickFlush510K(seat, handCard, lastDiscard, result, Constants.SUIT_HEART);
	}

	private void pickSpade510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickFlush510K(seat, handCard, lastDiscard, result, Constants.SUIT_SPADE);
	}

	private void pickFlush510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result, int suit) {
		int att = DiscardCombo.ATTRIBUTE_510K_FLUSH_DIAMOND;
		if (suit == Constants.SUIT_DIAMOND) {
			att = DiscardCombo.ATTRIBUTE_510K_FLUSH_DIAMOND;
		} else if (suit == Constants.SUIT_CLUB) {
			att = DiscardCombo.ATTRIBUTE_510K_FLUSH_CLUB;
		} else if (suit == Constants.SUIT_HEART) {
			att = DiscardCombo.ATTRIBUTE_510K_FLUSH_HEART;
		} else if (suit == Constants.SUIT_SPADE) {
			att = DiscardCombo.ATTRIBUTE_510K_FLUSH_SPADE;
		}
		// pick K
		List<CardInfo> listOfK = pickCardInSortedCards(handCard, 13, suit);
		if (listOfK == null || listOfK.size() == 0) {
			return;
		}
		// pick 10
		List<CardInfo> listOf10 = pickCardInSortedCards(handCard, 10, suit);
		if (listOf10 == null || listOf10.size() == 0) {
			return;
		}
		// pick 5
		List<CardInfo> listOf5 = pickCardInSortedCards(handCard, 5, suit);
		if (listOf5 == null || listOf5.size() == 0) {
			return;
		}
		// combine
		for (CardInfo pokerK : listOfK) {
			for (CardInfo poker10 : listOf10) {
				for (CardInfo poker5 : listOf5) {
					ArrayList<CardInfo> cards = new ArrayList<CardInfo>();
					cards.add(pokerK);
					cards.add(poker10);
					cards.add(poker5);
					result.add(new DiscardCombo(cards, seat, att));
				}
			}
		}
	}

	private void pickFive(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickSame(seat, handCard, lastDiscard, result, 5,
				DiscardCombo.ATTRIBUTE_FIVE);
	}

	private void pickSix(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickSame(seat, handCard, lastDiscard, result, 6,
				DiscardCombo.ATTRIBUTE_SIX);
	}

	private void pickSeven(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickSame(seat, handCard, lastDiscard, result, 7,
				DiscardCombo.ATTRIBUTE_SEVEN);
	}

	private void pickEight(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		pickSame(seat, handCard, lastDiscard, result, 8,
				DiscardCombo.ATTRIBUTE_EIGHT);
	}

	private void pickSame(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result, int sameSize,
			int att) {
		ArrayList<CardInfo> cards = new ArrayList<CardInfo>();
		int lastOrder = lastDiscard.getOrder();
		int baseCount = 0;
		int sameCardSize = 0;
		for (int i = 0; i < handCard.size() - sameSize + 1; i++) {
			if (handCard.get(i).getCardInfo().getOrder() <= lastOrder) {
				continue;
			}
			sameCardSize = 0;
			baseCount = 0;
			baseCount = handCard.get(i).getCardInfo().getCount();
			cards.add(handCard.get(i).getCardInfo());
			sameCardSize++;
			for (int j = 1; j < sameSize; j++) {
				if (handCard.get(i + j).getCardInfo().getCount() == baseCount) {
					sameCardSize++;
					cards.add(handCard.get(i + j).getCardInfo());
				} else {
					break;
				}
			}
			if (sameCardSize == sameSize) {
				result.add(new DiscardCombo(cards, seat, att));
				cards = new ArrayList<CardInfo>();
			} else {
				cards.clear();
			}
		}
	}

	public List<CardInfo> pickCardInSortedCards(List<Poker> pokers, int order) {
		if (pokers == null || pokers.size() == 0) {
			return null;
		}
		List<CardInfo> result = new ArrayList<CardInfo>();
		for (int i = 0; i < pokers.size(); i++) {
			Poker p = pokers.get(i);
			if (p.getCardInfo().getOrder() == order) {
				result.add(p.getCardInfo());
			}
			if (p.getCardInfo().getOrder() < order) {
				break;
			}
		}
		return result;
	}

	public List<CardInfo> pickCardInSortedCards(List<Poker> pokers, int order,
			int suit) {
		if (pokers == null || pokers.size() == 0) {
			return null;
		}
		List<CardInfo> result = new ArrayList<CardInfo>();
		for (Poker p : pokers) {
			CardInfo card = p.getCardInfo();
			if (card.getOrder() == order && card.getSuit() == suit) {
				result.add(card);
			}
			if (p.getCardInfo().getOrder() < order) {
				break;
			}
		}
		return result;
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

	public void reset() {

	}

}
