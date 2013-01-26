package com.oneguy.qipai.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.oneguy.qipai.R;
import com.oneguy.qipai.entity.CardInfo;
import com.oneguy.qipai.entity.Player;

public class Poker extends Sprite implements Comparable<Poker> {

	private static final String TAG = "Card";
	private Bitmap mCardFace;
	private boolean mIsSelected;
	protected Rect mCardFaceDestRect;
	protected int mCardFaceWidth;
	protected int mCardFaceHeight;
	private CardInfo mCardInfo;
	private Player mPlayer;

	// 相当于唯一标识

	public Poker(Context context, CardInfo card, Bitmap cardFace, int width,
			int height, int cardFaceWidth, int cardFaceHeight) {
		super(context, width, height);
		mCardInfo = card;
		setBackgroundResource(R.drawable.lord_card_backface_big);
		mCardFace = cardFace;
		mCardFaceWidth = cardFaceWidth;
		mCardFaceHeight = cardFaceHeight;
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
			// Log.d(TAG, "onTouchEvent");
			setSelected(!mIsSelected);
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mCardFace, null, mCardFaceDestRect, null);
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

	public void reset() {
		setVisibility(View.GONE);
		mIsSelected = false;
	}
}
