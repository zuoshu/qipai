package com.oneguy.qipai.game.ai;

public class DiscardRecord {
	private DiscardCombo discardCombo;
	private int round;
	private int sequence;
	public DiscardCombo getDiscardCombo() {
		return discardCombo;
	}
	public void setDiscardCombo(DiscardCombo discardCombo) {
		this.discardCombo = discardCombo;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}
