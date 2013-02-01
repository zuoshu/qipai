package com.oneguy.qipai.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;
import com.oneguy.qipai.game.Player;

public class ScorePanel extends AbstractView {

	private View mScorePanelView;
	private TextView[] mPlayerName;
	private TextView[] mPlayerScore;
	private TextView[] mPlayerRanking;
	private Button mBtnNextMatch;
	private Button mBtnQuit;
	private ScorePanelButtonClickListener mBtnListener;

	public interface ScorePanelButtonClickListener {
		public void onNextMatchClick();

		public void onQuitClick();
	}

	public ScorePanel() {
		initUIRefs();
	}

	public void setButtonsListener(ScorePanelButtonClickListener listener) {
		mBtnListener = listener;
	}

	private void initUIRefs() {
		mScorePanelView = inflateView();
		mPlayerName = new TextView[4];
		mPlayerScore = new TextView[4];
		mPlayerRanking = new TextView[4];
		// name
		mPlayerName[Player.SEAT_BOTTOM] = (TextView) mScorePanelView
				.findViewById(R.id.name_player0);
		mPlayerName[Player.SEAT_RIGHT] = (TextView) mScorePanelView
				.findViewById(R.id.name_player1);
		mPlayerName[Player.SEAT_UP] = (TextView) mScorePanelView
				.findViewById(R.id.name_player2);
		mPlayerName[Player.SEAT_LEFT] = (TextView) mScorePanelView
				.findViewById(R.id.name_player3);
		// score
		mPlayerScore[Player.SEAT_BOTTOM] = (TextView) mScorePanelView
				.findViewById(R.id.score_player0);
		mPlayerScore[Player.SEAT_RIGHT] = (TextView) mScorePanelView
				.findViewById(R.id.score_player1);
		mPlayerScore[Player.SEAT_UP] = (TextView) mScorePanelView
				.findViewById(R.id.score_player2);
		mPlayerScore[Player.SEAT_LEFT] = (TextView) mScorePanelView
				.findViewById(R.id.score_player3);
		// ranking
		mPlayerRanking[Player.SEAT_BOTTOM] = (TextView) mScorePanelView
				.findViewById(R.id.rank_player0);
		mPlayerRanking[Player.SEAT_RIGHT] = (TextView) mScorePanelView
				.findViewById(R.id.rank_player1);
		mPlayerRanking[Player.SEAT_UP] = (TextView) mScorePanelView
				.findViewById(R.id.rank_player2);
		mPlayerRanking[Player.SEAT_LEFT] = (TextView) mScorePanelView
				.findViewById(R.id.rank_player3);
		// button
		mBtnNextMatch = (Button) mScorePanelView
				.findViewById(R.id.btnNextMatch);
		mBtnNextMatch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBtnListener != null) {
					mBtnListener.onNextMatchClick();
				}
			}
		});
		mBtnQuit = (Button) mScorePanelView.findViewById(R.id.btnQuit);
		mBtnQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBtnListener != null) {
					mBtnListener.onQuitClick();
				}
			}
		});
	}

	public void setName(int seat, String name) {
		if (seat < 0 || seat > 3) {
			return;
		}
		mPlayerName[seat].setText(name);
	}

	public void setScore(int seat, int score) {
		if (seat < 0 || seat > 3) {
			return;
		}
		mPlayerScore[seat].setText(String.valueOf(score));
	}

	public void setRanking(int seat, int ranking) {
		if (seat < 0 || seat > 3) {
			return;
		}
		mPlayerRanking[seat].setText(String.valueOf(ranking));
	}

	public View inflateView() {
		View v = View.inflate(QianfenApplication.getInstance(),
				R.layout.score_panel, null);
		return v;
	}

	@Override
	public View getView() {
		return mScorePanelView;
	}

	public void invalidate() {
		invalidateViews(mPlayerName);
		invalidateViews(mPlayerRanking);
		invalidateViews(mPlayerScore);
	}

	private void invalidateViews(View[] views) {
		for (View v : views) {
			v.invalidate();
		}
	}

	public void show() {
		if (mScorePanelView != null) {
			mScorePanelView.setVisibility(View.VISIBLE);
			mScorePanelView.bringToFront();
		}
	}

	public void reset() {
		for (TextView v : mPlayerName) {
			v.setText("");
		}

		for (TextView v : mPlayerScore) {
			v.setText("");
		}

		for (TextView v : mPlayerRanking) {
			v.setText("");
		}
		mScorePanelView.setVisibility(View.GONE);
	}

}
