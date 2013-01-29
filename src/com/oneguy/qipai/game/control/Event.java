package com.oneguy.qipai.game.control;

public class Event{
	public static final int TYPE_IDLE = 9999;
	// Director事件
	// 等待获取玩家信息
	public static final int TYPE_D_WAIT_FOR_PLAYER_INFO = 0;
	public static final int TYPE_D_WAIT_SHUFFLE = 1;// 等待洗牌信息
	public static final int TYPE_D_INIT_COMPLETE = 2;// 初始化完毕
	public static final int TYPE_D_WAIT_PLAYER_ACTION = 3;// 等待玩家出牌
	public static final int TYPE_D_PLAYER_ACTION_TIME_OUT = 4;// 玩家出牌超时

	// Opponent事件
	// 获取玩家信息完毕
	public static final int TYPE_O_GENERATE_PLAY_INFO_COMPLETE = 5000;
	public static final int TYPE_O_SHUFFLE_COMPLETE = 5001;// 等待洗牌信息

	// Common 事件
	public static final int TYPE_C_DISCARD = 9001;// 玩家出牌

//	public static final Event EVENT_IDLE = new Event(TYPE_IDLE, null);
//	public int what;
//	public Object data;
//	public int arg;

//	public Event(int what, Object data) {
//		this.what = what;
//		this.data = data;
//	}
//
//	public Event() {
//		this(0, null);
//	}
//
//	public Event(int what) {
//		this(what, null);
//	}

}
