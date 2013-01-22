package com.oneguy.qipai.qianfen.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.oneguy.qipai.hall.R;
import com.oneguy.qipai.qianfen.Constants;
import com.oneguy.qipai.qianfen.game.CardInfo;
import com.oneguy.qipai.qianfen.game.Player;
import com.oneguy.qipai.qianfen.resource.ResourceManger;
import com.oneguy.qipai.ui.Sprite;

public class Poker extends Sprite implements Comparable<Poker> {

	private static final String TAG = "Card";
	private Bitmap mCardFace;
	private boolean mIsSelected;
	protected Rect mCardFaceDestRect;
	protected int mCardFaceWidth;
	protected int mCardFaceHeight;
	private ResourceManger mResourceManager;
	private CardInfo mCardInfo;
	private Player mPlayer;

	public Poker(Context context, String cardFaceName, int width, int height) {
		super(context, width, height);
		if (!genCardInfo(cardFaceName)) {
			throw new IllegalArgumentException();
		}
		mResourceManager = ResourceManger.getInstance();
		setBackgroundResource(R.drawable.lord_card_backface_big);
		mCardFace = ResourceManger.getInstance().getBitmap(cardFaceName);
		mCardFaceWidth = mResourceManager.cardFaceWidth;
		mCardFaceHeight = mResourceManager.cardFaceHeight;
		mCardFaceDestRect = new Rect(0, 0, mCardFaceWidth, mCardFaceHeight);
	}

	public void setSelected(boolean isSelected) {
		mIsSelected = isSelected;
		setBackgroundResource(mIsSelected ? R.drawable.card_selected
				: R.drawable.lord_card_backface_big);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 不是自己的牌不能动
		if (getPlayer().getSeat() != Player.SEAT_BOTTOM) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "onTouchEvent");
			setSelected(!mIsSelected);
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mCardFace, null, mCardFaceDestRect, null);
	}

	// 参考Constants CARD_SPADE_1 命名方式
	private boolean genCardInfo(String cardFaceName) {
		if (cardFaceName == null) {
			return false;
		}
		String[] names = cardFaceName.split(Constants.CARD_NAME_SPLITTER);
		if (names == null || names.length < 3) {
			return false;
		}
		int suit;
		int count;
		int order;
		if (names[1].equals(Constants.SPADE)) {
			suit = Constants.SUIT_SPADE;
		} else if (names[1].equals(Constants.HEART)) {
			suit = Constants.SUIT_HEART;
		} else if (names[1].equals(Constants.CLUB)) {
			suit = Constants.SUIT_CLUB;
		} else if (names[1].equals(Constants.DIAMOND)) {
			suit = Constants.SUIT_DIAMOND;
		} else {
			suit = Constants.SUIT_JOKER;
		}
		if (names[2].equals(Constants.RED)) {
			order = Constants.ORDER_JOKER_RED;
			count = Constants.COUNT_JOKER_RED;
		} else if (names[2].equals(Constants.BLACK)) {
			order = Constants.ORDER_JOKER_BLACK;
			count = Constants.COUNT_JOKER_BLACK;
		} else if (names[2].equals(Constants.COUNT_1)) {
			order = Constants.ORDER_1;
			count = Integer.valueOf(names[2]);
		} else if (names[2].equals(Constants.COUNT_2)) {
			order = Constants.ORDER_2;
			count = Integer.valueOf(names[2]);
		} else {
			order = Integer.valueOf(names[2]);
			count = Integer.valueOf(names[2]);
		}
		mCardInfo = new CardInfo(cardFaceName, suit, count, order);
		return true;
	}

	public CardInfo getCardInfo() {
		return mCardInfo;
	}

	public Player getPlayer() {
		return mPlayer;
	}

	public void setPlayer(Player player) {
		this.mPlayer = player;
	}

	// TODO 将逻辑和UI分离
	@Override
	public int compareTo(Poker another) {
		return mCardInfo.compareTo(another.getCardInfo());
	}
}
