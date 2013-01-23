package com.oneguy.qipai.game;

/**
 * 
 * @author Zuoshu Director和Opponent的基类，游戏逻辑基于事件，玩家自己和对手都是一个EventGenerator
 * 
 */
public class EventGenerator {
	private Event currentEvent;

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}

	public void setCurrentEvent(int what, Object data) {
		Event event = new Event(what, data);
		setCurrentEvent(event);
	}

	public void setCurrentEvent(int what) {
		setCurrentEvent(what, null);
	}

}
