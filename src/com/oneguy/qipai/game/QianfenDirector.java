package com.oneguy.qipai.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import com.oneguy.qipai.BuildConfig;
import com.oneguy.qipai.Constants;
import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;
import com.oneguy.qipai.entity.Player;
import com.oneguy.qipai.view.Clock;
import com.oneguy.qipai.view.Clock.OnTimeOutListener;
import com.oneguy.qipai.view.Poker;
import com.oneguy.qipai.view.QianfenStage;

public class QianfenDirector extends Director implements OnClickListener {

	public static final String TAG = "QianfenDirector";
	public static final String STATUS_TAG = "director_status";
	static final int BOTTOM = 0;
	static final int RIGHT = 1;
	static final int UP = 2;
	static final int LEFT = 3;
	private int mPlayWith;
	// 0 自己，1右边，2上面，3左边
	private Player[] players;
	private ResourceManger mResourceManager;
	private Context mContext;
	private QianfenStage mStage;
	private ArrayList<Poker> mPokers;
	private AI mAI;
	// UI refs
	private Button mStartButton;
	private Clock mClock;
	private PlayerTimeOutListener mPlayerTimeOutListener;

	// 不能这样出牌
	private static final int ERROR_INVALID_CARDS = 1;

	public QianfenDirector(Activity activity, int playWith) {
		super(activity);
		mPlayWith = playWith;
	}

	@Override
	public void onStart() {
		if (mPlayWith == PLAY_WITH_AI) {
			setOpponent(new AIOpponent(this));
		} else {
			throw new IllegalArgumentException("暂只支持ai玩家");
		}
		mStage = (QianfenStage) getStage();
		mStage.setDirector(this);
		getActivity().setContentView(mStage);
		mContext = QianfenApplication.getInstance();
		players = new Player[4];
		players[0] = new Player(mStage);
		players[1] = new Player(mStage);
		players[2] = new Player(mStage);
		players[3] = new Player(mStage);
		mResourceManager = ResourceManger.getInstance();
		mAI = new AI(this);
		mPlayerTimeOutListener = new PlayerTimeOutListener(Player.SEAT_NONE);
		initUIRefs();
		// init complete
		onEvent(new Event(Event.TYPE_D_INIT_COMPLETE));
	}

	private void initUIRefs() {
		mPokers = mResourceManager.getPokers();
		for (Poker p : mPokers) {
			if (p.getParent() == null) {
				mStage.addView(p);
			}
		}
		// 开始按钮
		mStartButton = new Button(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager
				.getHorizontalDimen(R.string.start_button_margin_left_percent);
		lp.topMargin = mResourceManager
				.getVerticalDimen(R.string.start_button_margin_top_percent);
		mStartButton.setLayoutParams(lp);
		mStartButton.setText(R.string.start);
		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onEvent(new Event(Event.TYPE_D_WAIT_SHUFFLE));
				mStartButton.setVisibility(View.GONE);

			}
		});
		mStartButton.setLayoutParams(lp);
		mStage.addView(mStartButton);

		// 计时器
		mClock = new Clock(mContext);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mClock.setLayoutParams(lp);
		mClock.setVisibility(View.GONE);
		mStage.addView(mClock);
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onStop() {
		resetAllCards();
		mStage.removeAllViews();
	}

	private void resetAllCards() {
		for (Poker p : mPokers) {
			p.reset();
		}
	}

	@Override
	public void onEvent(Event event) {
		switch (event.what) {
		// 初始化
		case Event.TYPE_D_INIT_COMPLETE:
			if (BuildConfig.DEBUG) {
				Log.d(STATUS_TAG, "1.请求玩家信息");
			}
			deployEvent(Event.TYPE_D_WAIT_FOR_PLAYER_INFO);
			break;
		// 获取玩家信息
		case Event.TYPE_O_GENERATE_PLAY_INFO_COMPLETE:
			if (getCurrentEvent().what == Event.TYPE_D_WAIT_FOR_PLAYER_INFO) {
				setPlayerInfo(event.data);
				logPlayerInfoIfDebug();
			} else if (BuildConfig.DEBUG) {
				Log.d(TAG, "游戏状态错误，期待："
						+ Event.TYPE_O_GENERATE_PLAY_INFO_COMPLETE + " 实际:"
						+ event.what);
			}
			// getReadyToShuffle
			putPlayerInfoOntoStage();
			mStartButton.setVisibility(View.VISIBLE);
			break;
		// 洗牌
		case Event.TYPE_D_WAIT_SHUFFLE:
			if (BuildConfig.DEBUG) {
				Log.d(STATUS_TAG, "2.请求洗牌");
			}
			deployEvent(Event.TYPE_D_WAIT_SHUFFLE);
			break;
		case Event.TYPE_O_SHUFFLE_COMPLETE:
			if (getCurrentEvent().what == Event.TYPE_D_WAIT_SHUFFLE) {
				clearPlayerCards();
				setCardSequence(event.data);
				logCardsInfoIfDebug();
				putHandCardsOntoStage();
				// TODO 修改为按牌计算谁先出牌
				int seat = genRandomStart();
				mAI.setWhoIsFirst(seat);
				if (BuildConfig.DEBUG) {
					Log.d(STATUS_TAG, "3.等待玩家出牌：" + mAI.getInActionPlayerSeat());
				}
				onEvent(new Event(Event.TYPE_D_WAIT_PLAYER_ACTION,
						mAI.getInActionPlayerSeat()));
			} else if (BuildConfig.DEBUG) {
				Log.d(TAG, "游戏状态错误，期待：" + Event.TYPE_D_WAIT_SHUFFLE + " 实际:"
						+ event.what);
			}
			break;
		// 等待玩家出牌
		case Event.TYPE_D_WAIT_PLAYER_ACTION:
			int seat = (Integer) event.data;
			if (seat != mAI.getInActionPlayerSeat()) {
				Log.e(TAG, "状态错误，出牌次序不一致！");
				// 对于每轮出牌的计算和服务器出现不一致，以服务器为准！
				mAI.setWhoIsFirst(seat);
			}
			setClock(mAI.getInActionPlayerSeat());
			break;
		// 玩家出牌超时
		case Event.TYPE_D_PLAYER_ACTION_TIME_OUT:
			int timeoutSeat = (Integer) event.data;
			int[] cards = mAI.autoPlay(timeoutSeat);
			Event ev = new Event(Event.TYPE_C_SHOW_CARDS, cards);
			ev.arg = timeoutSeat;
			onEvent(ev);
			break;
		// 玩家出牌
		case Event.TYPE_C_SHOW_CARDS:
			int seatShowCards = event.arg;
			int[] cardsShowing = (int[]) event.data;
			if (mAI.isShowCardsValid(seatShowCards, cardsShowing)) {
				showPlayerCards(seatShowCards, cardsShowing);
			} else {
				showError(ERROR_INVALID_CARDS);
			}
			break;
		default:
			Log.e(TAG, "unhandle event:" + event.what);
			// do nothing
		}
	}

	private void setPlayerInfo(Object data) {
		try {
			JSONObject info = (JSONObject) data;
			int sequence = info.getInt(Constants.SELF_SEQUENCE);
			players[BOTTOM].setSequence(sequence);
			players[BOTTOM].setSeat(Player.SEAT_BOTTOM);
			players[BOTTOM].setName(info.getString(Constants.SELF_NAME));

			players[RIGHT].setSequence((++sequence) % 4);
			players[RIGHT].setSeat(Player.SEAT_RIGHT);
			players[RIGHT].setName(info.getString(Constants.RIGHT_NAME));

			players[UP].setSequence((++sequence) % 4);
			players[UP].setSeat(Player.SEAT_UP);
			players[UP].setName(info.getString(Constants.UP_NAME));

			players[LEFT].setSequence((++sequence) % 4);
			players[LEFT].setSeat(Player.SEAT_LEFT);
			players[LEFT].setName(info.getString(Constants.LEFT_NAME));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// private void putActorsOntoStage() {
	// putPlayerInfoOntoStage();
	// putHandCardsOntoStage();
	// }

	private void putPlayerInfoOntoStage() {
		// player0自己 坐在下面
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player0InfoMarginLeft;
		lp.topMargin = mResourceManager.player0InfoMarginTop;
		getStage().addView(players[0].getInfoView(), lp);

		// player1 右边
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player1InfoMarginLeft;
		lp.topMargin = mResourceManager.player1InfoMarginTop;
		getStage().addView(players[1].getInfoView(), lp);

		// player2 上面
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player2InfoMarginLeft;
		lp.topMargin = mResourceManager.player2InfoMarginTop;
		getStage().addView(players[2].getInfoView(), lp);

		// player3 左边
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player3InfoMarginLeft;
		lp.topMargin = mResourceManager.player3InfoMarginTop;
		getStage().addView(players[3].getInfoView(), lp);
	}

	private void clearPlayerCards() {
		for (int i = 0; i < 4; i++) {
			players[i].clearCards();
		}
	}

	private void setCardSequence(Object data) {
		int[] cards = (int[]) data;
		if (BuildConfig.DEBUG) {
			String s = "";
			for (int i : cards) {
				s += i + ",";
			}
			Log.d(TAG, s);
		}
		Poker poker;
		Player player;
		for (int i = 0; i < cards.length; i++) {
			poker = mPokers.get(cards[i]);
			player = players[i % 4];
			poker.setPlayer(player);
			player.addCard(poker);
		}
		for (Player p : players) {
			p.sortCards();
		}

	}

	private void putHandCardsOntoStage() {
		for (Player player : players) {
			player.showCards();
			player.layoutCards();
		}
	}

	/**
	 * 计算谁先出牌，这里采用随机的方式，需要修改
	 * 
	 * @return 0-3分别代表bottom,right,top,left的玩家
	 */
	private int genRandomStart() {
		return ((int) System.currentTimeMillis()) % 4;
	}

	/**
	 * 设置定时器
	 * 
	 * @param inActionPlayerSeat
	 *            BOTTOM,RIGHT,UP,LEFT
	 */
	private void setClock(int inActionPlayerSeat) {
		// 调整clock位置
		int x, y;
		switch (inActionPlayerSeat) {
		case Player.SEAT_BOTTOM:
			x = mResourceManager
					.getHorizontalDimen(R.string.bottom_clock_x_percent);
			y = mResourceManager
					.getHorizontalDimen(R.string.bottom_clock_y_percent);
			break;
		case Player.SEAT_RIGHT:
			x = mResourceManager
					.getHorizontalDimen(R.string.right_clock_x_percent);
			y = mResourceManager
					.getHorizontalDimen(R.string.right_clock_y_percent);
			break;
		case Player.SEAT_UP:
			x = mResourceManager
					.getHorizontalDimen(R.string.up_clock_x_percent);
			y = mResourceManager
					.getHorizontalDimen(R.string.up_clock_y_percent);
			break;
		case Player.SEAT_LEFT:
			x = mResourceManager
					.getHorizontalDimen(R.string.left_clock_x_percent);
			y = mResourceManager
					.getHorizontalDimen(R.string.left_clock_y_percent);
			break;
		default:
			return;
		}
		LayoutParams lp = (LayoutParams) mClock.getLayoutParams();
		lp.leftMargin = x;
		lp.topMargin = y;
		mClock.setLayoutParams(lp);
		mClock.setVisibility(View.VISIBLE);

		mPlayerTimeOutListener.setPlayerSeat(inActionPlayerSeat);
		mClock.start(mPlayerTimeOutListener, Constants.PLAY_ACTION_TIME_OUT);
	}

	public Player[] getPlayers() {
		return players;
	}

	private void showPlayerCards(int seatShowCards, int[] cardsShowing) {
		// TODO Auto-generated method stub

	}

	private void showError(int errorInvalidCards) {
		// TODO Auto-generated method stub

	}

	private void logCardsInfoIfDebug() {
		if (BuildConfig.DEBUG) {
			for (int i = 0; i < 4; i++) {
				Log.d(TAG, "player" + i + ":" + players[i].getCardSequence());
			}
		}
	}

	private void logPlayerInfoIfDebug() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG,
					players[0].toString() + '\n' + players[1].toString() + '\n'
							+ players[2].toString() + '\n'
							+ players[3].toString());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}

	}

	class PlayerTimeOutListener implements OnTimeOutListener {

		// 定时器监听的玩家座位
		private int playerSeat;

		public PlayerTimeOutListener(int seat) {
			playerSeat = seat;
		}

		public void setPlayerSeat(int seat) {
			playerSeat = seat;
		}

		@Override
		public void onTimeOut() {
			onEvent(new Event(Event.TYPE_D_PLAYER_ACTION_TIME_OUT, playerSeat));
		}
	}
}
