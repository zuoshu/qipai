package com.oneguy.qipai.view;

import android.view.View;
import android.widget.TextView;

import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;

public class PlayerInfo extends AbstractView {
	private View mInfoView;
	private TextView mPlayerName;
	private TextView mPlayerScore;
	private TextView mPlayerCardCount;

	public PlayerInfo() {
		mInfoView = inflateView();
		mPlayerName = (TextView) mInfoView.findViewById(R.id.playerName);
		mPlayerScore = (TextView) mInfoView.findViewById(R.id.playerScore);
		mPlayerCardCount = (TextView) mInfoView
				.findViewById(R.id.playerCardCount);
	}

	private View inflateView() {
		View view = View.inflate(QianfenApplication.getInstance(),
				R.layout.player_info_view_v, null);
		return view;
	}

	public void setName(String name) {
		mPlayerName.setText(name);
	}

	public void setScore(String score) {
		mPlayerScore.setText(score);
	}

	public void setCardCount(int count) {
		mPlayerCardCount.setText(String.valueOf(count));
	}

	@Override
	public View getView(){
		return mInfoView;
	}
}
