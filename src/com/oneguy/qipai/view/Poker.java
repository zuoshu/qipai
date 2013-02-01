package com.oneguy.qipai.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;
import com.oneguy.qipai.game.CardInfo;
import com.oneguy.qipai.game.Player;

public class Poker extends Sprite implements Comparable<Poker> {

	private static final String TAG = "Card";
	private static final int SELECTED_GAP = ResourceManger.getInstance()
			.getVerticalDimen(R.string.seleceted_gap);
	private static final PaintFlagsDrawFilter FILTER = new PaintFlagsDrawFilter(
			0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	private Bitmap mCardFace;
	private boolean mIsSelected;
	protected Rect mCardFaceDestRect;
	protected int mCardFaceWidth;
	protected int mCardFaceHeight;
	private CardInfo mCardInfo;
	private Player mPlayer;
	private Bitmap mSelectBitmap;
	private Rect mCardRect;
	private int selectedY;
	private int unselectedY;

	public Poker(Context context, CardInfo card, Bitmap cardFace, int width,
			int height, int cardFaceWidth, int cardFaceHeight) {
		super(context, width, height);
		mCardInfo = card;
		// setBackgroundColor(Color.WHITE);
		setBackgroundResource(R.drawable.lord_card_bg_big);
		mCardFace = cardFace;
		mCardFaceWidth = cardFaceWidth;
		mCardFaceHeight = cardFaceHeight;
		mCardFaceDestRect = new Rect(0, 0, mCardFaceWidth, mCardFaceHeight);
		mSelectBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.card_selected);
		mCardRect = new Rect();
		resetCoordinate();
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		if (selectedY == 0) {
			initBaseline(y);
		}
	}

	@Override
	public void set(int x, int y) {
		super.set(x, y);
		if (selectedY == 0) {
			initBaseline(y);
		}
	}

	private void initBaseline(int y) {
		unselectedY = y;
		selectedY = unselectedY - SELECTED_GAP;
	}

	public void setSelected(boolean isSelected) {
		// boolean changeToSelected = false;
		// boolean changeToDeselected = false;
		// if (mIsSelected && !isSelected) {
		// changeToDeselected = true;
		// } else if (!mIsSelected && isSelected) {
		// changeToSelected = true;
		// }
		// if (changeToSelected) {
		// int y = getY();
		// y -= SELECTED_GAP;
		// setY(y);
		// } else if (changeToDeselected) {
		// int y = getY();
		// y += SELECTED_GAP;
		// setY(y);
		// }
		mIsSelected = isSelected;
		if (mIsSelected) {
			setY(selectedY);
		} else {
			setY(unselectedY);
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 不是自己的牌不能动
		if (getPlayer().getSeat() != Player.SEAT_BOTTOM) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setSelected(!mIsSelected);
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(FILTER);
		if (mIsSelected) {
			mCardRect.right = getWidth();
			mCardRect.bottom = getHeight();
			canvas.drawBitmap(mSelectBitmap, null, mCardRect, null);
			Log.d(TAG, "draw select");
		}
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
		resetCoordinate();
	}

	// called when start a new game
	public void resetCoordinate() {
		unselectedY = 0;
		selectedY = 0;
	}
}
