package at.abraxas.amarino;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity12 extends Activity {

	protected static final int RESULT_SPEECH = 1;

	private ImageButton btnSpeak;
	private EditText txtText;
	EditText etPhone;
	Button btnSend;
	String[] addresses; // connected devices
	String[] flags;
	char selectedFlag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtText = (EditText) findViewById(R.id.txtText);
        btnSend = (Button) findViewById(R.id.submit);
        etPhone = (EditText) findViewById(R.id.no);

		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		
		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					txtText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Ops! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

		IntentFilter intentFilter = new IntentFilter(
				AmarinoIntent.ACTION_CONNECTED_DEVICES);
		registerReceiver(receiver, intentFilter);

		Intent intent = new Intent(MainActivity12.this, AmarinoService.class);
		intent.setAction(AmarinoIntent.ACTION_GET_CONNECTED_DEVICES);
		MainActivity12.this.startService(intent);

		btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhone.getText().toString();
                if(phone.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter phone", 600000).show();
                    return;
                }
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, "#"+txtText.getText().toString(), null, null);
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {

			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				String voice = text.get(0);
				txtText.setText(text.get(0));
			}
			break;
		}

		}
	}


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String voice = text.get(0);

                    for (int i = 0; i < text.size(); i++) {

                        if (text.get(i).contains("left")) {

                            voice = text.get(i);
                            sendData(addresses[0], "2");
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;

                        } else if (text.get(i).contains("right")) {
                            voice = text.get(i);
                            sendData(addresses[0], "3");
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;

                        } else if (text.get(i).contains("go")) {
                            voice = text.get(i);
                            sendData(addresses[0], "1");
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;

                        } else if (text.get(i).contains("back")) {

                            sendData(addresses[0], "4");
                            voice = text.get(i);
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;

                        } else if (text.get(i).contains("op")
                                || text.get(i).contains("OP")) {
                            sendData(addresses[0], "5");
                            voice = text.get(i);
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;
                        } else if (text.get(i).contains("dw")
                                || text.get(i).contains("DW")
                                || text.get(i).contains("2")) {
                            sendData(addresses[0], "6");
                            voice = text.get(i);
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;
                        } else if (text.get(i).contains("in")) {
                            sendData(addresses[0], "7");
                            voice = text.get(i);
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;
                        } else if (text.get(i).contains("out")) {
                            sendData(addresses[0], "8");
                            voice = text.get(i);
                            Toast.makeText(getApplicationContext(), "" + voice, 600)
                                    .show();
                            break;
                        }

                    }

                    txtText.setText(text.get(0));
                }
                break;
            }

        }
    }*/

	private void sendData(String address, String msg) {
		Intent intent = new Intent(MainActivity12.this, AmarinoService.class);
		intent.setAction(AmarinoIntent.ACTION_SEND);
		intent.putExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS, address);
		intent.putExtra(AmarinoIntent.EXTRA_FLAG, selectedFlag);
		intent.putExtra(AmarinoIntent.EXTRA_DATA_TYPE,
				AmarinoIntent.STRING_EXTRA);
		intent.putExtra(AmarinoIntent.EXTRA_DATA, msg);
		MainActivity12.this.startService(intent);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action == null)
				return;

			if (AmarinoIntent.ACTION_CONNECTED_DEVICES.equals(action)) {
				addresses = intent
						.getStringArrayExtra(AmarinoIntent.EXTRA_CONNECTED_DEVICE_ADDRESSES);
			}
		}
	};

	private void setupFlagsArray() {
		flags = new String[52];
		for (int i = 0; i < 52; i++) {
			char flag;
			if (i < 26)
				flag = (char) ('A' + i);
			else
				flag = (char) ('a' + i - 26);
			flags[i] = String.valueOf(flag);
		}
	}

}
