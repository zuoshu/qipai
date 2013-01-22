package com.oneguy.qipai.qianfen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oneguy.qipai.hall.R;
import com.oneguy.qipai.qianfen.resource.ResourceManger;

public class QianfenActivity extends Activity {
	Button singlePlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qianfen);
		singlePlay = (Button) findViewById(R.id.singlePlayButton);
		singlePlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(QianfenActivity.this, GameActivity.class);
				i.putExtra(Constants.MODE, Constants.MODE_SIGNLE);
				QianfenActivity.this.startActivity(i);
				// QianfenActivity.this.finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		ResourceManger.getInstance().clear();
		super.onDestroy();
	}

}
