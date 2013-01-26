package com.oneguy.qipai.game.ai;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.oneguy.qipai.BuildConfig;
import com.oneguy.qipai.entity.CardInfo;
import com.oneguy.qipai.entity.Player;
import com.oneguy.qipai.game.Recorder;
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
		mPlayerSeatInAction = (mPlayerSeatInAction + 1) % 4;
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "takeTurns->" + mPlayerSeatInAction);
		}
	}

	public List<DiscardCombo> discard() {
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
				pickFake510K(seat, handCard, lastDiscard, result);
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
		int lastOrder = lastDiscard.getOrder();
		ArrayList<CardInfo> cards;
		for (Poker p : handCard) {
			if (p.getCardInfo().getOrder() > lastOrder) {
				cards = new ArrayList<CardInfo>();
				cards.add(p.getCardInfo());
				result.add(new DiscardCombo(cards, seat));
			}
		}
	}

	private void pickPair(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {

	}

	private void pickThree(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickFake510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickFour(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickDiamond510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickClub510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickHeart510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickSpade510K(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickFive(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickSix(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickSeven(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

	}

	private void pickEight(int seat, List<Poker> handCard,
			DiscardCombo lastDiscard, List<DiscardCombo> result) {
		// TODO Auto-generated method stub

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

}