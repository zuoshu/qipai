package com.oneguy.qipai;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.oneguy.qipai.entity.CardInfo;
import com.oneguy.qipai.view.Poker;

public final class ResourceManger {
	public static final String TAG = "ResourceManger";
	private static ResourceManger mInstance = null;
	private Resources mResources;
	// Default memory cache size
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB
	// private LruCache<String, Bitmap> mMemCache;
	private HashMap<String, Integer> mCardFaceResMap;
	// private boolean isImageCached;

	// 屏幕信息
	public int displayWidth;
	public int displayHeight;
	// 牌宽，高
	public int cardWidth;
	public int cardHeight;
	// 牌面点数宽，高
	public int cardFaceWidth;
	public int cardFaceHeight;

	// 用户信息摆放位置
	public int player0InfoMarginLeft;
	public int player0InfoMarginTop;
	public int player1InfoMarginLeft;
	public int player1InfoMarginTop;
	public int player2InfoMarginLeft;
	public int player2InfoMarginTop;
	public int player3InfoMarginLeft;
	public int player3InfoMarginTop;

	// 开始按钮的位置
	public int startButtonMarginLeft;
	public int startButtonMarginTop;

	// 所有的扑克 108张
	private static HashMap<String, CardInfo> mCardInfo;
	private static ArrayList<Poker> mPokers;

	public synchronized static ResourceManger getInstance() {
		if (mInstance == null) {
			mInstance = new ResourceManger();
		}
		return mInstance;
	}

	private ResourceManger() {
		mResources = QianfenApplication.getInstance().getResources();
		// mMemCache = new LruCache<String, Bitmap>(DEFAULT_MEM_CACHE_SIZE);
		mPokers = new ArrayList<Poker>(Constants.CARD_COUNT);
		mCardInfo = new HashMap<String, CardInfo>(Constants.CARD_COUNT);
		displayWidth = QianfenApplication.displayWidth;
		displayHeight = QianfenApplication.displayHeight;
		// isImageCached = false;
		mCardFaceResMap = new HashMap<String, Integer>(Constants.CARD_COUNT);
		initResources();
	}

	private void computeSize() {
		// card
		float cardWidthPercent = Float.valueOf(mResources
				.getString(R.string.card_width_percent));
		float cardHeightPercent = Float.valueOf(mResources
				.getString(R.string.card_height_percent));
		cardWidth = (int) (displayWidth * cardWidthPercent);
		cardHeight = (int) (displayHeight * cardHeightPercent);

		// card face
		float cardFaceWidthPercent = Float.valueOf(mResources
				.getString(R.string.card_face_width_percent));
		float cardFaceHeightPercent = Float.valueOf(mResources
				.getString(R.string.card_face_height_percent));
		cardFaceWidth = (int) (displayWidth * cardFaceWidthPercent);
		cardFaceHeight = (int) (displayHeight * cardFaceHeightPercent);

		// player info
		float player0InfoMarginLeftPercent = getFloatFromString(R.string.player0_info_margin_left_percent);
		float player0InfoMarginTopPercent = getFloatFromString(R.string.player0_info_margin_top_percent);
		float player1InfoMarginLeftPercent = getFloatFromString(R.string.player1_info_margin_left_percent);
		float player1InfoMarginTopPercent = getFloatFromString(R.string.player1_info_margin_top_percent);
		float player2InfoMarginLeftPercent = getFloatFromString(R.string.player2_info_margin_left_percent);
		float player2InfoMarginTopPercent = getFloatFromString(R.string.player2_info_margin_top_percent);
		float player3InfoMarginLeftPercent = getFloatFromString(R.string.player3_info_margin_left_percent);
		float player3InfoMarginTopPercent = getFloatFromString(R.string.player3_info_margin_top_percent);
		player0InfoMarginLeft = (int) (displayWidth * player0InfoMarginLeftPercent);
		player0InfoMarginTop = (int) (displayHeight * player0InfoMarginTopPercent);

		player1InfoMarginLeft = (int) (displayWidth * player1InfoMarginLeftPercent);
		player1InfoMarginTop = (int) (displayHeight * player1InfoMarginTopPercent);

		player2InfoMarginLeft = (int) (displayWidth * player2InfoMarginLeftPercent);
		player2InfoMarginTop = (int) (displayHeight * player2InfoMarginTopPercent);

		player3InfoMarginLeft = (int) (displayWidth * player3InfoMarginLeftPercent);
		player3InfoMarginTop = (int) (displayHeight * player3InfoMarginTopPercent);

		// start Button
		float startButtonMarginLeftPercent = getFloatFromString(R.string.start_button_margin_left_percent);
		float startButtonMarginTopPercent = getFloatFromString(R.string.start_button_margin_top_percent);
		startButtonMarginLeft = (int) (displayWidth * startButtonMarginLeftPercent);
		startButtonMarginTop = (int) (displayHeight * startButtonMarginTopPercent);

	}

	public void initResources() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "initResources");
		}
		computeSize();
		storeCardBitmapId();
		prepareCards();
	}

	private void prepareCards() {
		// 两副牌
		for (int i = 0; i < 2; i++) {
			prepareCard(Constants.CARD_SPADE_1);
			prepareCard(Constants.CARD_SPADE_2);
			prepareCard(Constants.CARD_SPADE_3);
			prepareCard(Constants.CARD_SPADE_4);
			prepareCard(Constants.CARD_SPADE_5);
			prepareCard(Constants.CARD_SPADE_6);
			prepareCard(Constants.CARD_SPADE_7);
			prepareCard(Constants.CARD_SPADE_8);
			prepareCard(Constants.CARD_SPADE_9);
			prepareCard(Constants.CARD_SPADE_10);
			prepareCard(Constants.CARD_SPADE_11);
			prepareCard(Constants.CARD_SPADE_12);
			prepareCard(Constants.CARD_SPADE_13);

			prepareCard(Constants.CARD_HEART_1);
			prepareCard(Constants.CARD_HEART_2);
			prepareCard(Constants.CARD_HEART_3);
			prepareCard(Constants.CARD_HEART_4);
			prepareCard(Constants.CARD_HEART_5);
			prepareCard(Constants.CARD_HEART_6);
			prepareCard(Constants.CARD_HEART_7);
			prepareCard(Constants.CARD_HEART_8);
			prepareCard(Constants.CARD_HEART_9);
			prepareCard(Constants.CARD_HEART_10);
			prepareCard(Constants.CARD_HEART_11);
			prepareCard(Constants.CARD_HEART_12);
			prepareCard(Constants.CARD_HEART_13);

			prepareCard(Constants.CARD_CLUB_1);
			prepareCard(Constants.CARD_CLUB_2);
			prepareCard(Constants.CARD_CLUB_3);
			prepareCard(Constants.CARD_CLUB_4);
			prepareCard(Constants.CARD_CLUB_5);
			prepareCard(Constants.CARD_CLUB_6);
			prepareCard(Constants.CARD_CLUB_7);
			prepareCard(Constants.CARD_CLUB_8);
			prepareCard(Constants.CARD_CLUB_9);
			prepareCard(Constants.CARD_CLUB_10);
			prepareCard(Constants.CARD_CLUB_11);
			prepareCard(Constants.CARD_CLUB_12);
			prepareCard(Constants.CARD_CLUB_13);

			prepareCard(Constants.CARD_DIAMOND_1);
			prepareCard(Constants.CARD_DIAMOND_2);
			prepareCard(Constants.CARD_DIAMOND_3);
			prepareCard(Constants.CARD_DIAMOND_4);
			prepareCard(Constants.CARD_DIAMOND_5);
			prepareCard(Constants.CARD_DIAMOND_6);
			prepareCard(Constants.CARD_DIAMOND_7);
			prepareCard(Constants.CARD_DIAMOND_8);
			prepareCard(Constants.CARD_DIAMOND_9);
			prepareCard(Constants.CARD_DIAMOND_10);
			prepareCard(Constants.CARD_DIAMOND_11);
			prepareCard(Constants.CARD_DIAMOND_12);
			prepareCard(Constants.CARD_DIAMOND_13);

			prepareCard(Constants.CARD_JOKER_BLACK);
			prepareCard(Constants.CARD_JOKER_RED);
		}
	}

	private void prepareCard(String cardFaceName) {
		if (cardFaceName == null) {
			return;
		}
		String[] names = cardFaceName.split(Constants.CARD_NAME_SPLITTER);
		if (names == null || names.length < 3) {
			return;
		}
		Context context = QianfenApplication.getInstance();
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
		int resId = mCardFaceResMap.get(cardFaceName);
		CardInfo cardInfo = new CardInfo(cardFaceName, suit, count, order);
		Poker poker = new Poker(context, cardInfo, getBitmap(resId), cardWidth,
				cardHeight, cardFaceWidth, cardFaceHeight);
		poker.setVisibility(View.GONE);
		mCardInfo.put(cardFaceName, cardInfo);
		mPokers.add(poker);
	}

	private void storeCardBitmapId() {
		mCardFaceResMap.put(Constants.CARD_JOKER_BLACK,
				R.drawable.card_joker_small);
		mCardFaceResMap
				.put(Constants.CARD_JOKER_RED, R.drawable.card_joker_big);
		// 黑桃
		mCardFaceResMap.put(Constants.CARD_SPADE_1, R.drawable.card_spade_1);
		mCardFaceResMap.put(Constants.CARD_SPADE_2, R.drawable.card_spade_2);
		mCardFaceResMap.put(Constants.CARD_SPADE_3, R.drawable.card_spade_3);
		mCardFaceResMap.put(Constants.CARD_SPADE_4, R.drawable.card_spade_4);
		mCardFaceResMap.put(Constants.CARD_SPADE_5, R.drawable.card_spade_5);
		mCardFaceResMap.put(Constants.CARD_SPADE_6, R.drawable.card_spade_6);
		mCardFaceResMap.put(Constants.CARD_SPADE_7, R.drawable.card_spade_7);
		mCardFaceResMap.put(Constants.CARD_SPADE_8, R.drawable.card_spade_8);
		mCardFaceResMap.put(Constants.CARD_SPADE_9, R.drawable.card_spade_9);
		mCardFaceResMap.put(Constants.CARD_SPADE_10, R.drawable.card_spade_10);
		mCardFaceResMap.put(Constants.CARD_SPADE_11, R.drawable.card_spade_j);
		mCardFaceResMap.put(Constants.CARD_SPADE_12, R.drawable.card_spade_q);
		mCardFaceResMap.put(Constants.CARD_SPADE_13, R.drawable.card_spade_k);
		// 红桃
		mCardFaceResMap.put(Constants.CARD_HEART_1, R.drawable.card_heart_1);
		mCardFaceResMap.put(Constants.CARD_HEART_2, R.drawable.card_heart_2);
		mCardFaceResMap.put(Constants.CARD_HEART_3, R.drawable.card_heart_3);
		mCardFaceResMap.put(Constants.CARD_HEART_4, R.drawable.card_heart_4);
		mCardFaceResMap.put(Constants.CARD_HEART_5, R.drawable.card_heart_5);
		mCardFaceResMap.put(Constants.CARD_HEART_6, R.drawable.card_heart_6);
		mCardFaceResMap.put(Constants.CARD_HEART_7, R.drawable.card_heart_7);
		mCardFaceResMap.put(Constants.CARD_HEART_8, R.drawable.card_heart_8);
		mCardFaceResMap.put(Constants.CARD_HEART_9, R.drawable.card_heart_9);
		mCardFaceResMap.put(Constants.CARD_HEART_10, R.drawable.card_heart_10);
		mCardFaceResMap.put(Constants.CARD_HEART_11, R.drawable.card_heart_j);
		mCardFaceResMap.put(Constants.CARD_HEART_12, R.drawable.card_heart_q);
		mCardFaceResMap.put(Constants.CARD_HEART_13, R.drawable.card_heart_k);
		// 梅花
		mCardFaceResMap.put(Constants.CARD_CLUB_1, R.drawable.card_club_1);
		mCardFaceResMap.put(Constants.CARD_CLUB_2, R.drawable.card_club_2);
		mCardFaceResMap.put(Constants.CARD_CLUB_3, R.drawable.card_club_3);
		mCardFaceResMap.put(Constants.CARD_CLUB_4, R.drawable.card_club_4);
		mCardFaceResMap.put(Constants.CARD_CLUB_5, R.drawable.card_club_5);
		mCardFaceResMap.put(Constants.CARD_CLUB_6, R.drawable.card_club_6);
		mCardFaceResMap.put(Constants.CARD_CLUB_7, R.drawable.card_club_7);
		mCardFaceResMap.put(Constants.CARD_CLUB_8, R.drawable.card_club_8);
		mCardFaceResMap.put(Constants.CARD_CLUB_9, R.drawable.card_club_9);
		mCardFaceResMap.put(Constants.CARD_CLUB_10, R.drawable.card_club_10);
		mCardFaceResMap.put(Constants.CARD_CLUB_11, R.drawable.card_club_j);
		mCardFaceResMap.put(Constants.CARD_CLUB_12, R.drawable.card_club_q);
		mCardFaceResMap.put(Constants.CARD_CLUB_13, R.drawable.card_club_k);
		// 方块
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_1, R.drawable.card_diamond_1);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_2, R.drawable.card_diamond_2);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_3, R.drawable.card_diamond_3);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_4, R.drawable.card_diamond_4);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_5, R.drawable.card_diamond_5);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_6, R.drawable.card_diamond_6);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_7, R.drawable.card_diamond_7);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_8, R.drawable.card_diamond_8);
		mCardFaceResMap
				.put(Constants.CARD_DIAMOND_9, R.drawable.card_diamond_9);
		mCardFaceResMap.put(Constants.CARD_DIAMOND_10,
				R.drawable.card_diamond_10);
		mCardFaceResMap.put(Constants.CARD_DIAMOND_11,
				R.drawable.card_diamond_j);
		mCardFaceResMap.put(Constants.CARD_DIAMOND_12,
				R.drawable.card_diamond_q);
		mCardFaceResMap.put(Constants.CARD_DIAMOND_13,
				R.drawable.card_diamond_k);
	}

	// private void putCard(String resName, int resId) {
	// Bitmap bitmap = genCardBitmap(resId);
	// if (bitmap != null) {
	// mMemCache.put(resName, bitmap);
	// }
	// }

	// private void putResource(String resName, int resId) {
	// Bitmap bitmap = BitmapFactory.decodeResource(mResources, resId);
	// if (bitmap != null) {
	// mMemCache.put(resName, bitmap);
	// }
	// }

	// public Bitmap getBitmap(String name) {
	// if (!isImageCached) {
	// initResources();
	// }
	// int resId = mCardFaceResMap.get(name);
	// Bitmap bitmap = BitmapFactory.decodeResource(mResources, resId);
	// return mMemCache.get(name);
	// }

	public Bitmap getBitmap(String name) {
		int resId = mCardFaceResMap.get(name);
		Bitmap bitmap = BitmapFactory.decodeResource(mResources, resId);
		return bitmap;
	}

	public Bitmap getBitmap(int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(mResources, resId);
		return bitmap;
	}

	public Bitmap genCardBitmap(int resId) {
		Bitmap srcBitmap = BitmapFactory.decodeResource(mResources, resId);
		Bitmap outBitmap = Bitmap.createBitmap(cardWidth, cardHeight,
				Config.ARGB_4444);
		Canvas canvas = new Canvas(outBitmap);
		// final Rect destRect = new Rect(0, 0, outBitmap.getWidth(),
		// outBitmap.getHeight());
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(srcBitmap, 0, 0, null);
		srcBitmap.recycle();
		return outBitmap;
	}

	public void clear() {
		// mMemCache.evictAll();
		// isImageCached = false;
		mInstance = null;
	}

	public float getFloatFromString(int id) {
		String res = mResources.getString(id);
		if (res == null || res.equals("")) {
			return 0;
		}
		return Float.valueOf(res);
	}

	public ArrayList<Poker> getPokers() {
		return mPokers;
	}

	/**
	 * 根据屏幕的高度以及所指定的百分比，返回长度
	 * 
	 * @param percentResName
	 *            百分比所对应的资源的id
	 * @return 根据屏幕的高度以及所指定的百分比，返回长度
	 */
	public int getVerticalDimen(int percentResName) {
		return (int) (displayHeight * getFloatFromString(percentResName));
	}

	/**
	 * 根据屏幕的宽度以及所指定的百分比，返回长度
	 * 
	 * @param percentResName
	 *            百分比所对应的资源的id
	 * @return 根据屏幕的宽度以及所指定的百分比，返回长度
	 */
	public int getHorizontalDimen(int percentResName) {
		return (int) (displayWidth * getFloatFromString(percentResName));
	}
}
