package com.oneguy.qipai.game.control;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.oneguy.qipai.BuildConfig;
import com.oneguy.qipai.Constants;
import com.oneguy.qipai.game.ai.Director;

public class AIOpponent extends Opponent {

	public static final String STATUS_TAG = "Opponent_status";

	// 牌组信息，用于洗牌，只保存序号，每个序号对应ResourceManager里面card数组
	// 洗牌时只要知道序号即可
	private int[] cards;

	public AIOpponent() {
		super();
		initCards();
	}

	@Override
	public void onEventMessage(Message msg) {
		switch (msg.what) {
		case Event.TYPE_D_WAIT_FOR_PLAYER_INFO:
			JSONObject info = generatePlayerInfo();
			if (BuildConfig.DEBUG) {
				Log.d(STATUS_TAG, "statuc==>TYPE_O_GENERATE_PLAY_INFO_COMPLETE");
			}
			deployEvent(Event.TYPE_O_GENERATE_PLAY_INFO_COMPLETE, info);
			break;
		case Event.TYPE_D_WAIT_SHUFFLE:
			shuffle();
			if (BuildConfig.DEBUG) {
				Log.d(STATUS_TAG, "statuc==>TYPE_D_SHUFFLE_COMPLETE");
			}
			deployEvent(Event.TYPE_O_SHUFFLE_COMPLETE, cards);
			break;
		default:
			// do nothing
		}

	}

	private void initCards() {
		cards = new int[Constants.CARD_COUNT];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = i;
		}
	}

	private JSONObject generatePlayerInfo() {
		int sequence = (int) ((Math.random() * 16) % 4);
		JSONObject playerInfo = new JSONObject();
		try {
			playerInfo.put(Constants.SELF_SEQUENCE, sequence);

			playerInfo.put(Constants.SELF_NAME, Constants.DEFAULT_SELF_NAME);
			playerInfo.put(Constants.RIGHT_NAME, Constants.DEFAULT_RIGHT_NAME);
			playerInfo.put(Constants.UP_NAME, Constants.DEFAULT_UP_NAME);
			playerInfo.put(Constants.LEFT_NAME, Constants.DEFAULT_LEFT_NAME);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return playerInfo;
	}

	private void shuffle() {
		for (int j = 0; j < 3; j++) {
			int tail = cards.length - 1;
			int randomVal, tmp;
			Random random = new Random();
			while (tail > 1) {
				randomVal = random.nextInt(tail);
				// swap
				tmp = cards[tail];
				cards[tail] = cards[randomVal];
				cards[randomVal] = tmp;
				tail--;
			}
		}
	}

}
