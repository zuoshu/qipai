package com.oneguy.qipai.hall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class HallActivity extends Activity {

	private static final String QIANFEN_ACTION = "com.oneguy.qianfen";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(QIANFEN_ACTION);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hall, menu);
        return true;
    }
}
