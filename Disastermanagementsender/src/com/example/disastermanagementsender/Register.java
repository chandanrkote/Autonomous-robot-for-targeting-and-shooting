package com.example.disastermanagementsender;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	EditText mUsername, mPhone, mName, mAddress, mPassword;
	Button mLogin, mRegister;
	static String strUsername, strPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mUsername = (EditText) findViewById(R.id.editText1);
		mPhone = (EditText) findViewById(R.id.editText4);
		mName = (EditText) findViewById(R.id.editText3);
		mAddress = (EditText) findViewById(R.id.editText5);
		mPassword = (EditText) findViewById(R.id.editText6);

		mRegister = (Button) findViewById(R.id.button2);

		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String serverphone = mUsername.getText().toString();
				String name = mName.getText().toString();
				String phone = mPhone.getText().toString();
				String address = mAddress.getText().toString();
				String bloodgroup = mPassword.getText().toString();

				if (serverphone.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter username", 60000).show();
				} else if (name.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter name", 60000).show();
				} else if (phone.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter phone", 60000).show();
				} else if (address.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter address", 60000).show();
				} else if (bloodgroup.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter password", 60000).show();
				} else if (phone.length() != 10) {
					Toast.makeText(getApplicationContext(), "Invalid phone",
							60000).show();
				} else {

					mName.setText("");
					mPhone.setText("");
					mPassword.setText("");
					mAddress.setText("");
					
					Register.strUsername = serverphone;
					Register.strPassword = bloodgroup;
					
					Register.saveInpreference(Register.this, serverphone, bloodgroup);
					Toast.makeText(getApplicationContext(), "Registration successful.", 60000).show();
					finish();
//					SmsManager sms = SmsManager.getDefault();
//					String message = "REGS," + name + "," + phone + ","
//							+ address + "," + bloodgroup;
//					sms.sendTextMessage(serverphone, null, message, null, null);

				}

			}
		});

	}

	public void Inserplayer() {

		ContentValues cv = new ContentValues();
		cv.put("Name", mName.getText().toString());
		cv.put("Username", mUsername.getText().toString());
		// cv.put("Password", mPassword.getText().toString());
		cv.put("Address", mAddress.getText().toString());
		cv.put("Phone", mPhone.getText().toString());
		cv.put("BloodGroup", mPassword.getText().toString());

		Database dat = new Database(Register.this);
		SQLiteDatabase db = dat.getWritableDatabase();

		db.insert("User", null, cv);
		Toast.makeText(getApplicationContext(), "Registration successfully",
				600000).show();
		db.close();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static void saveInpreference(Context context, String username, String password){
		SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString("username", username);
		edit.putString("password", password);
		edit.commit();
	}
	
	public static String getUsername(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
		return sharedPreferences.getString("username", null);
		
		
	}
	
	public static String getPassword(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
		return sharedPreferences.getString("password", null);
		
		
	}
	
}
