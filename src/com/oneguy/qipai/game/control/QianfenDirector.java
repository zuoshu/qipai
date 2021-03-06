package com.oneguy.qipai.game.control;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.oneguy.qipai.BuildConfig;
import com.oneguy.qipai.Constants;
import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;
import com.oneguy.qipai.ResourceManger;
import com.oneguy.qipai.game.CardInfo;
import com.oneguy.qipai.game.Player;
import com.oneguy.qipai.game.Recorder;
import com.oneguy.qipai.game.ai.AutoPlay;
import com.oneguy.qipai.game.ai.Director;
import com.oneguy.qipai.game.ai.DiscardCombo;
import com.oneguy.qipai.view.Clock;
import com.oneguy.qipai.view.Clock.OnTimeOutListener;
import com.oneguy.qipai.view.PlayerControlPanel;
import com.oneguy.qipai.view.Poker;
import com.oneguy.qipai.view.QianfenStage;
import com.oneguy.qipai.view.ScorePanel;
import com.oneguy.qipai.view.ScorePanel.ScorePanelButtonClickListener;

public class QianfenDirector extends Director implements OnClickListener,
		ScorePanelButtonClickListener {
	public static final String TAG = "QianfenDirector";
	public static final String STATUS_TAG = "director_status";
	private static final int WAIT_AFTER_DISCARD_FINISH = 500;
	// 出牌超时
	public static final long PLAYER_ACTION_TIME_OUT = 20 * 1000;
	static final int BOTTOM = 0;
	static final int RIGHT = 1;
	static final int UP = 2;
	static final int LEFT = 3;
	private int mPlayWith;
	// 0 自己，1右边，2上面，3左边
	private Player[] mPlayers;
	private Context mQianfen;
	private ResourceManger mResourceManager;
	private QianfenStage mStage;
	private List<Poker> mPokers;
	private AutoPlay mAI;
	private Recorder mRecorder;
	// UI refs
	private Button mStartButton;
	private Clock mClock;
	private PlayerTimeOutListener mPlayerTimeOutListener;
	private PlayerControlPanel mPlayerControlPanel;
	// 托管
	private boolean mAutoPlay = false;
	private ScorePanel mScorePanel;
	private Opponent mOpponent;

	public QianfenDirector(Activity activity, int playWith) {
		super(activity);
		mPlayWith = playWith;
	}

	@Override
	public void onStart() {
		if (mPlayWith == PLAY_WITH_AI) {
			mOpponent = new AIOpponent();
			mOpponent.setDirectorHandler(getHandler());
			setOpponentHandler(mOpponent.getHandler());
		} else {
			throw new IllegalArgumentException("暂只支持ai玩家");
		}
		mResourceManager = ResourceManger.getInstance();
		mStage = (QianfenStage) getStage();
		mStage.setDirector(this);
		getActivity().setContentView(mStage);
		mQianfen = QianfenApplication.getInstance();
		mPlayers = QianfenApplication.players;
		mRecorder = QianfenApplication.recorder;
		mAI = QianfenApplication.autoPlayer;
		mPlayerTimeOutListener = new PlayerTimeOutListener(Player.SEAT_NONE);
		initUIRefs();
		// init complete
		sendEventMessage(Event.TYPE_D_INIT_COMPLETE);
	}

	private void initUIRefs() {
		mPokers = mResourceManager.getPokers();
		for (Poker p : mPokers) {
			if (p.getParent() == null) {
				mStage.addView(p);
			}
		}
		initStartButton();
		initClock();
		initScorePanel();
		initPlayerInfoView();
		initPassLable();
		initPlayerControlPanel();
	}

	private void initStartButton() {
		// 开始按钮
		mStartButton = new Button(mActivity);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager
				.getHorizontalDimen(R.string.start_button_margin_left_percent);
		lp.topMargin = mResourceManager
				.getVerticalDimen(R.string.start_button_margin_top_percent);
		mStartButton.setLayoutParams(lp);
		mStartButton.setBackgroundResource(R.drawable.start);
		mStartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendEventMessage(Event.TYPE_D_WAIT_SHUFFLE);
				mStartButton.setVisibility(View.GONE);
			}
		});
		mStartButton.setLayoutParams(lp);
		mStage.addView(mStartButton);
	}

	private void initClock() {
		// 计时器
		mClock = new Clock(mActivity);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mClock.setLayoutParams(lp);
		mClock.setVisibility(View.GONE);
		mStage.addView(mClock);
	}

	private void initScorePanel() {
		// 记分板
		mScorePanel = new ScorePanel();
		mScorePanel.setButtonsListener(this);
		View scoreView = mScorePanel.getView();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		scoreView.setLayoutParams(lp);
		scoreView.setVisibility(View.GONE);
		mStage.addView(scoreView);
	}

	private void initPlayerInfoView() {
		// player0自己 坐在下面
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player0InfoMarginLeft;
		lp.topMargin = mResourceManager.player0InfoMarginTop;
		mStage.addView(mPlayers[0].getInfoView(), lp);

		// player1 右边
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player1InfoMarginLeft;
		lp.topMargin = mResourceManager.player1InfoMarginTop;
		mStage.addView(mPlayers[1].getInfoView(), lp);

		// player2 上面
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player2InfoMarginLeft;
		lp.topMargin = mResourceManager.player2InfoMarginTop;
		mStage.addView(mPlayers[2].getInfoView(), lp);

		// player3 左边
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager.player3InfoMarginLeft;
		lp.topMargin = mResourceManager.player3InfoMarginTop;
		mStage.addView(mPlayers[3].getInfoView(), lp);
	}

	private void initPassLable() {
		// bottom
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager
				.getHorizontalDimen(R.string.bottom_pass_lable_x);
		lp.topMargin = mResourceManager
				.getVerticalDimen(R.string.bottom_pass_lable_y);
		mStage.addView(mPlayers[Player.SEAT_BOTTOM].getPassLable(), lp);

		// right
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager
				.getHorizontalDimen(R.string.right_pass_lable_x);
		lp.topMargin = mResourceManager
				.getVerticalDimen(R.string.right_pass_lable_y);
		mStage.addView(mPlayers[Player.SEAT_RIGHT].getPassLable(), lp);

		// up
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager
				.getHorizontalDimen(R.string.up_pass_lable_x);
		lp.topMargin = mResourceManager
				.getVerticalDimen(R.string.up_pass_lable_y);
		mStage.addView(mPlayers[Player.SEAT_UP].getPassLable(), lp);

		// left
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager
				.getHorizontalDimen(R.string.left_pass_lable_x);
		lp.topMargin = mResourceManager
				.getVerticalDimen(R.string.left_pass_lable_y);
		mStage.addView(mPlayers[Player.SEAT_LEFT].getPassLable(), lp);
	}

	private void initPlayerControlPanel() {
		mPlayerControlPanel = new PlayerControlPanel();
		mPlayerControlPanel.setDiscardOnClickListener(this);
		mPlayerControlPanel.setHintOnClickListener(this);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = mResourceManager
				.getHorizontalDimen(R.string.control_panel_x_percent);
		lp.topMargin = mResourceManager
				.getVerticalDimen(R.string.control_panel_y_percent);
		View controlView = mPlayerControlPanel.getView();
		controlView.setVisibility(View.GONE);
		mStage.addView(controlView, lp);
	}

	@Override
	public void onPause() {
		// TODO
	}

	@Override
	public void onStop() {
		deployEvent(Event.TYPE_D_PLAYER_QUIT);
		reset();
		mStage.removeAllViews();
	}

	private void resetAllCards() {
		for (Poker p : mPokers) {
			p.reset();
		}
	}

	@Override
	public void onEventMessage(Message event) {
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
			setPlayerInfo(event.obj);
			logPlayerInfoIfDebug();
			// getReadyToShuffle
			mStartButton.setVisibility(View.VISIBLE);
			break;
		// 洗牌
		case Event.TYPE_D_WAIT_SHUFFLE:
			if (BuildConfig.DEBUG) {
				Log.d(STATUS_TAG, "2.请求洗牌");
			}
			deployEvent(Event.TYPE_D_WAIT_SHUFFLE);
			break;
		// 洗牌完毕
		case Event.TYPE_O_SHUFFLE_COMPLETE:
			clearPlayerCards();
			setCardSequence(event.obj);
			logCardsInfoIfDebug();
			putHandCardsOntoStage();
			// TODO 修改为按牌计算谁先出牌
			int seat = genRandomStart();
			mAI.setWhoIsFirst(seat);
			if (BuildConfig.DEBUG) {
				Log.d(STATUS_TAG, "3.等待玩家出牌：" + mAI.getInActionPlayerSeat());
			}
			waitDiscard();
			break;
		// 等待玩家出牌
		case Event.TYPE_D_WAIT_PLAYER_DISCARD:
			int seat3 = (Integer) event.obj;
			if (seat3 != mAI.getInActionPlayerSeat()) {
				Log.e(TAG, "状态错误，出牌次序不一致！");
				// 对于每轮出牌的计算和服务器出现不一致，以服务器为准！
				mAI.setWhoIsFirst(seat3);
			}
			mPlayers[seat3].removeDiscard();
			List<DiscardCombo> discardList = mAI.pickHintList();
			// 如果是自己出牌并且托管，则启动自动出牌
			if (seat3 == Player.SEAT_SELF) {
				// 自动过牌
				if (discardList == null || discardList.size() == 0) {
					DiscardCombo discard = new DiscardCombo(seat3);
					discard.setAttribute(DiscardCombo.ATTRIBUTE_PASS);
					sendEventMessage(Event.TYPE_C_DISCARD, discard);
				} else if (!mAutoPlay) {
					mPlayerControlPanel.show();
					setClock(mAI.getInActionPlayerSeat());
				} else {
					deployEvent(Event.TYPE_D_WAIT_PLAYER_DISCARD, seat3);
				}
			} else {
				deployEvent(Event.TYPE_D_WAIT_PLAYER_DISCARD, seat3);
			}
			break;
		// 等待玩家超时
		case Event.TYPE_D_PLAYER_ACTION_TIME_OUT:
			// 如果是自己超时，则托管出牌，如果是对手超时，则等待，过30秒后启动掉线检测
			int seat2 = mAI.getInActionPlayerSeat();
			if (seat2 == Player.SEAT_BOTTOM) {
				// 单机版先不托管
				setAutoPlay(true);
				waitDiscard();
			} else {
				// TODO 设定30秒后启动掉线检测
			}
			break;
		// 玩家出牌
		case Event.TYPE_C_DISCARD:
			DiscardCombo discard = (DiscardCombo) event.obj;
			if (BuildConfig.DEBUG) {
				String str = "player:" + discard.getSeat() + " discard:";
				List<CardInfo> list = discard.getCards();
				if (list != null) {
					for (CardInfo c : discard.getCards()) {
						str += c.getName() + "|";
					}
				} else {
					str += "|pass";
				}
				Log.d(TAG, str);
			}
			mPlayerControlPanel.hide();
			if (discard.getArrtibute() == DiscardCombo.ATTRIBUTE_PASS) {
				playerPass(discard);
			} else {
				playerDiscard(discard);
			}
			sendEventMessage(Event.TYPE_D_PLAYER_FINISH_DISCARD, discard,
					WAIT_AFTER_DISCARD_FINISH);
			break;
		// 出牌后清理
		case Event.TYPE_D_PLAYER_FINISH_DISCARD:
			DiscardCombo discard2 = (DiscardCombo) event.obj;
			int seat4 = discard2.getSeat();
			mRecorder.addDiscard(discard2);
			// 通知对手自己已出牌
			if (seat4 == Player.SEAT_SELF) {
				Message msg = new Message();
				msg.copyFrom(event);
				deployEvent(msg);
			}
			// 单机版只托管一轮
			setAutoPlay(false);
			mAI.resetHintList();
			if (mPlayers[seat4].isRunout()) {
				onPlayerRunout(seat4);
			}
			if (mRecorder.getCurrentPlayerCount() < 2) {
				onGameFinish();
			} else {
				mAI.takeTurns();
				if (mAI.isOneRoundFinish()) {
					updateScore();
					clearLastRoundDisacrd();
				}
				waitDiscard();
			}
			break;
		default:
			Log.e(TAG, "unhandle event:" + event.what);
			// do nothing
		}
	}

	private void onPlayerRunout(int seat) {
		mPlayers[seat].getInfoView().setBackgroundColor(Color.RED);
		mRecorder.markPlayerRunout(seat);
	}

	private void onGameFinish() {
		mScorePanel.setName(Player.SEAT_BOTTOM,
				mPlayers[Player.SEAT_BOTTOM].getName());
		mScorePanel.setName(Player.SEAT_RIGHT,
				mPlayers[Player.SEAT_RIGHT].getName());
		mScorePanel.setName(Player.SEAT_UP, mPlayers[Player.SEAT_UP].getName());
		mScorePanel.setName(Player.SEAT_LEFT,
				mPlayers[Player.SEAT_LEFT].getName());

		mScorePanel.setScore(Player.SEAT_BOTTOM,
				mPlayers[Player.SEAT_BOTTOM].getScore());
		mScorePanel.setScore(Player.SEAT_RIGHT,
				mPlayers[Player.SEAT_RIGHT].getScore());
		mScorePanel.setScore(Player.SEAT_UP,
				mPlayers[Player.SEAT_UP].getScore());
		mScorePanel.setScore(Player.SEAT_LEFT,
				mPlayers[Player.SEAT_LEFT].getScore());

		mRecorder.markLastRunout();
		mScorePanel.setRanking(Player.SEAT_BOTTOM,
				mRecorder.getPlayerRanking(Player.SEAT_BOTTOM));
		mScorePanel.setRanking(Player.SEAT_RIGHT,
				mRecorder.getPlayerRanking(Player.SEAT_RIGHT));
		mScorePanel.setRanking(Player.SEAT_UP,
				mRecorder.getPlayerRanking(Player.SEAT_UP));
		mScorePanel.setRanking(Player.SEAT_LEFT,
				mRecorder.getPlayerRanking(Player.SEAT_LEFT));
		mScorePanel.invalidate();
		mScorePanel.show();
	}

	private void clearLastRoundDisacrd() {
		// 清除上一轮出的牌
		for (Player player : mPlayers) {
			player.hidePassLable();
			player.removeDiscard();
		}
		mRecorder.clearLastRoundRecord();
	}

	private void updateScore() {
		int seat = mRecorder.getLastRoundWinner();
		int score = mRecorder.countLastRoundScore();
		mPlayers[seat].addScore(score);
	}

	private void waitDiscard() {
		waitDiscard(0);
	}

	private void waitDiscard(long delay) {
		sendEventMessage(Event.TYPE_D_WAIT_PLAYER_DISCARD,
				mAI.getInActionPlayerSeat(), delay);
	}

	private void playerDiscard(DiscardCombo discard) {
		Player player = mPlayers[discard.getSeat()];
		player.discard(discard);
		if (player.getCards().size() > 0) {
			player.layoutCards();
			player.deselectAllCards();
		}
	}

	private void playerPass(DiscardCombo discard) {
		// play some sound and animation
		if (discard == null) {
			return;
		}
		mPlayers[discard.getSeat()].showPassLable();
	}

	private void setPlayerInfo(Object data) {
		try {
			JSONObject info = (JSONObject) data;
			int sequence = info.getInt(Constants.SELF_SEQUENCE);
			mPlayers[BOTTOM].setSequence(sequence);
			mPlayers[BOTTOM].setSeat(Player.SEAT_BOTTOM);
			mPlayers[BOTTOM].setName(info.getString(Constants.SELF_NAME));

			mPlayers[RIGHT].setSequence((++sequence) % 4);
			mPlayers[RIGHT].setSeat(Player.SEAT_RIGHT);
			mPlayers[RIGHT].setName(info.getString(Constants.RIGHT_NAME));

			mPlayers[UP].setSequence((++sequence) % 4);
			mPlayers[UP].setSeat(Player.SEAT_UP);
			mPlayers[UP].setName(info.getString(Constants.UP_NAME));

			mPlayers[LEFT].setSequence((++sequence) % 4);
			mPlayers[LEFT].setSeat(Player.SEAT_LEFT);
			mPlayers[LEFT].setName(info.getString(Constants.LEFT_NAME));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void clearPlayerCards() {
		for (int i = 0; i < 4; i++) {
			mPlayers[i].clearCards();
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
			player = mPlayers[i % 4];
			poker.setPlayer(player);
			player.addCard(poker);
		}
		for (Player p : mPlayers) {
			p.sortCards();
		}

	}

	private void putHandCardsOntoStage() {
		// for(Player p:mPlayers){
		// p.showCards();
		// p.layoutCards();
		// if(p.getSeat()!=Player.SEAT_SELF){
		// p.setHandCardFronFace(false);
		// }
		// }
		// show players card only
		mPlayers[Player.SEAT_SELF].showCards();
		mPlayers[Player.SEAT_SELF].layoutCards();
	}

	public void setAutoPlay(boolean autoPlay) {
		mAutoPlay = autoPlay;
	}

	/**
	 * 计算谁先出牌，这里采用随机的方式，需要修改
	 * 
	 * @return 0-3分别代表bottom,right,top,left的玩家
	 */
	private int genRandomStart() {
		return (Math.abs((int) System.currentTimeMillis())) % 4;
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
		mClock.start(mPlayerTimeOutListener, PLAYER_ACTION_TIME_OUT);
	}

	public Player[] getPlayers() {
		return mPlayers;
	}

	private void logCardsInfoIfDebug() {
		if (BuildConfig.DEBUG) {
			for (int i = 0; i < 4; i++) {
				Log.d(TAG, "player" + i + ":" + mPlayers[i].getCardSequence());
			}
		}
	}

	private void logPlayerInfoIfDebug() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG,
					mPlayers[0].toString() + '\n' + mPlayers[1].toString()
							+ '\n' + mPlayers[2].toString() + '\n'
							+ mPlayers[3].toString());
		}
	}

	@Override
	public void onClick(View v) {
		int seat = mAI.getInActionPlayerSeat();
		if (v == mStartButton) {
			sendEventMessage(Event.TYPE_D_WAIT_SHUFFLE);
			mStartButton.setVisibility(View.GONE);
		} else {
			switch (v.getId()) {
			case R.id.btnDiscard:
				List<CardInfo> cards = mPlayers[seat].getSelectedCards();
				DiscardCombo discard = mAI.getValidDiscard(cards);
				if (discard == null
						|| discard.getArrtibute() == DiscardCombo.ATTRIBUTE_INVALID
						|| discard.getArrtibute() == DiscardCombo.ATTRIBUTE_NONE) {
					Toast.makeText(mQianfen, "不能这样出牌！", Toast.LENGTH_SHORT)
							.show();
				} else {
					mClock.cancleTiming();
					mClock.setVisibility(View.GONE);
					sendEventMessage(Event.TYPE_C_DISCARD, discard);
				}
				break;
			case R.id.btnHint:
				mPlayers[seat].deselectAllCards();
				mPlayers[seat].selectDiscard(mAI.pickHint());
				break;
			}
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
			sendEventMessage(Event.TYPE_D_PLAYER_ACTION_TIME_OUT, playerSeat);
			mClock.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNextMatchClick() {
		startNewMatch();
	}

	@Override
	public void onQuitClick() {
		quitGame();
	}

	private void startNewMatch() {
		reset();
		sendEventMessage(Event.TYPE_D_INIT_COMPLETE);
	}

	private void quitGame() {
		reset();
		mActivity.finish();
	}

	private void reset() {
		resetAllCards();
		for (Player p : mPlayers) {
			p.reset();
		}
		mRecorder.reset();
		mScorePanel.reset();
	}
}
