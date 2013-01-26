package com.oneguy.qipai.game.control;

import com.oneguy.qipai.game.ai.Director;



public abstract class Opponent extends EventGenerator implements EventListener {
	private Director mDirector;

	public Opponent(Director director) {
		mDirector = director;
	}

	public Director getDirector() {
		return mDirector;
	}

	public void deployEvent(Event event) {
		setCurrentEvent(event);
		mDirector.onEvent(event);
	}

	public void deployEvent(int what, Object data) {
		Event event = new Event(what, data);
		deployEvent(event);
	}

	public void deployEvent(int what) {
		deployEvent(what, null);
	}
}
