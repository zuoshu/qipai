package com.oneguy.qipai.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oneguy.qipai.QianfenApplication;
import com.oneguy.qipai.R;

public class PlayerControlPanel extends AbstractView {

	private View mControlPanel;
	private Button mBtnDiscard;
	private Button mBtnHint;

	public PlayerControlPanel() {
		mControlPanel = inflateView();
		mBtnDiscard = (Button) mControlPanel.findViewById(R.id.btnDiscard);
		mBtnHint = (Button) mControlPanel.findViewById(R.id.btnHint);
	}

	private View inflateView() {
		View v = View.inflate(QianfenApplication.getInstance(),
				R.layout.player_control_panel, null);
		return v;
	}

	@Override
	public View getView() {
		return mControlPanel;
	}

	public void setDiscardOnClickListener(OnClickListener listener) {
		mBtnDiscard.setOnClickListener(listener);
	}

	public void setHintOnClickListener(OnClickListener listener) {
		mBtnHint.setOnClickListener(listener);
	}

	public void show() {
		mControlPanel.setVisibility(View.VISIBLE);
		mControlPanel.bringToFront();
		mControlPanel.invalidate();
	}

	public void hide() {
		mControlPanel.setVisibility(View.GONE);
		mControlPanel.invalidate();
	}
}
