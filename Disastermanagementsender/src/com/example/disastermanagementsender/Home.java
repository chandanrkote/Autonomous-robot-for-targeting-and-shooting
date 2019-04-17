package com.example.disastermanagementsender;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Home extends Activity {

	Button btnContactAdmin, btnDepartment, btnContact, btnChatHistory;
	EditText e1, e2;
	EditText e3;
	Socket ss;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		btnContactAdmin = (Button) findViewById(R.id.button1);
		btnContact = (Button) findViewById(R.id.button2);
		btnDepartment = (Button) findViewById(R.id.button3);
		btnChatHistory = (Button) findViewById(R.id.button4);

		// e1.setText("daya");
		// e2.setText("daya");
		// TelephonyManager telephonyManager =
		// (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		// String imei = telephonyManager.getDeviceId();

		// if((!imei.contains("911304153844521"))){
		// TextView tv=null;
		// tv.setText("");
		// }
		//
		btnContactAdmin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// MatchUser();
				Intent intent = new Intent(Home.this, MainActivity.class);
				startActivity(intent);

			}
		});

		btnDepartment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this,
						DepartmentListActivity.class);
				startActivity(intent);
			}
		});

		btnChatHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this, MainActivity.class);
				intent.putExtra("chat", true);
				startActivity(intent);
			}
		});

		btnContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			
			return true;
		case R.id.item2:
		
			return true;
		case R.id.item3:
	
			return true;
		case R.id.item4:
			finish();
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
