package com.oneguy.qipai.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;
import com.oneguy.qipai.game.CardInfo;
import com.oneguy.qipai.game.Player;

public class Poker extends Sprite implements Comparable<Poker> {

	private static final String TAG = "Poker";
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
	private static final Bitmap BITMAP_SELECTED = BitmapFactory.decodeResource(
			QianfenApplication.getInstance().getResources(),
			R.drawable.card_selected);
	private Rect mCardRect;
	private int selectedY;
	private int unselectedY;
	// 是否翻开
	private boolean mIsFrontFace;
	private static Poker mLastTouchPoker;
	private static boolean mSelectWhenTouch;

	public Poker(Context context, CardInfo card, Bitmap cardFace, int width,
			int height, int cardFaceWidth, int cardFaceHeight) {
		super(context, width, height);
		mCardInfo = card;
		setBackgroundResource(R.drawable.lord_card_bg_big);
		mCardFace = cardFace;
		mCardFaceWidth = cardFaceWidth;
		mCardFaceHeight = cardFaceHeight;
		mCardFaceDestRect = new Rect(0, 0, mCardFaceWidth, mCardFaceHeight);
		mCardRect = new Rect();
		resetCoordinate();
		mIsFrontFace = true;
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
		mIsSelected = isSelected;
		if (mIsSelected) {
			setY(selectedY);
		} else {
			setY(unselectedY);
		}
		invalidate();
	}

	public boolean isSelected() {
		return mIsSelected;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 不是自己的牌不能动
		if (getPlayer().getSeat() != Player.SEAT_BOTTOM) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastTouchPoker = this;
			mSelectWhenTouch = !isSelected();
			setSelected(mSelectWhenTouch);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mLastTouchPoker != this) {
				mLastTouchPoker = this;
				setSelected(mSelectWhenTouch);
			}
			break;
		case MotionEvent.ACTION_UP:
			mLastTouchPoker = null;
			mSelectWhenTouch = true;
			break;
		}
		Log.d("poker", "onTouchEvent " + event.getAction() + ":" + event.getX()
				+ ":" + event.getY());
		return true;
	}

	public boolean contains(float x, float y) {
		return (getLeft() <= x && x <= getRight() && getTop() <= y && y <= getBottom());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mIsFrontFace) {
			setBackgroundResource(R.drawable.lord_card_bg_big);
			canvas.setDrawFilter(FILTER);
			if (mIsSelected) {
				mCardRect.right = getWidth();
				mCardRect.bottom = getHeight();
				canvas.drawBitmap(BITMAP_SELECTED, null, mCardRect, null);
			}
			canvas.drawBitmap(mCardFace, null, mCardFaceDestRect, null);
		} else {
			setBackgroundResource(R.drawable.lord_cards_num_bg);
		}
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

	public void setFrontFace(boolean isFrontFace) {
		boolean changeFace = mIsFrontFace != isFrontFace;
		mIsFrontFace = isFrontFace;
		if (changeFace) {
			invalidate();
		}
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
		setFrontFace(true);
	}

	// called when start a new game
	public void resetCoordinate() {
		unselectedY = 0;
		selectedY = 0;
	}

}
