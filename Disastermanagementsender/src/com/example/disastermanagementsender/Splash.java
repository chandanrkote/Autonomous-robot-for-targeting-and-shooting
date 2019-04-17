package com.example.disastermanagementsender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.TextView;

public class Splash extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		// not necessary
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();

		// if((!imei.contains("356893050278736"))){
		// TextView tv=null;
		// tv.setText("");
		// }

		Handler handler = new Handler();

		// run a thread after 2 seconds to start the home screen
		handler.postDelayed(new Runnable() {

			public void run() {

				// make sure we close the splash screen so the user won't come
				// back when it presses back key

				finish();
				// start the home screen
				Intent intent = new Intent(Splash.this, Login.class);
				startActivity(intent);
			}

		}, 3000);

	}

	

}
