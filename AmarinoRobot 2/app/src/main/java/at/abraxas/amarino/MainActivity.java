package at.abraxas.amarino;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    protected static final int RESULT_SPEECH = 1;

    private ImageButton btnSpeak;
    private TextView txtText;
    String[] addresses; // connected devices
    String[] flags;
    char selectedFlag;
    private static final String KEY_FLAG_PREF = "flag_pref";
    public ShakeListener mShaker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtText = (TextView) findViewById(R.id.txtText);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);


        setupFlagsArray();
        selectedFlag = (char) PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_FLAG_PREF, 65);  // default 'A'


        IntentFilter intentFilter = new IntentFilter(AmarinoIntent.ACTION_CONNECTED_DEVICES);
        registerReceiver(receiver, intentFilter);

        Intent intent = new Intent(MainActivity.this, AmarinoService.class);
        intent.setAction(AmarinoIntent.ACTION_GET_CONNECTED_DEVICES);
        MainActivity.this.startService(intent);


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

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                mShaker.pause();

                EditText no = (EditText) findViewById(R.id.no);

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(no.getText().toString(), null, "i am in problem please help", null, null);
            }
        });


    }


    private void sendData(String address, String msg) {
        Intent intent = new Intent(MainActivity.this, AmarinoService.class);
        intent.setAction(AmarinoIntent.ACTION_SEND);
        intent.putExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS, address);
        intent.putExtra(AmarinoIntent.EXTRA_FLAG, selectedFlag);
        intent.putExtra(AmarinoIntent.EXTRA_DATA_TYPE, AmarinoIntent.STRING_EXTRA);
        intent.putExtra(AmarinoIntent.EXTRA_DATA, msg);
        MainActivity.this.startService(intent);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action == null) return;

            if (AmarinoIntent.ACTION_CONNECTED_DEVICES.equals(action)) {
                addresses = intent.getStringArrayExtra(AmarinoIntent.EXTRA_CONNECTED_DEVICE_ADDRESSES);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (text.contains("left") || text.contains("right") || text.contains("go") || text.contains("back") || text.contains("stop")) {

                        if (text.contains("left")) {

                            sendData(addresses[0], "1");

                        } else if (text.contains("right")) {
                            sendData(addresses[0], "2");
                        } else if (text.contains("go")) {

                            sendData(addresses[0], "3");
                        } else if (text.contains("back")) {
                            sendData(addresses[0], "4");
                        } else if (text.contains("stop")) {
                            sendData(addresses[0], "5");
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Text", 6000).show();
                    }
                    txtText.setText(text.get(0));
                }
                break;
            }

        }
    }


}
