package com.oneguy.qipai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QianfenActivity extends Activity {
	Button singlePlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qianfen);
		ResourceManger.getInstance();
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
}
