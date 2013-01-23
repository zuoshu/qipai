package com.oneguy.qipai.game;



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
