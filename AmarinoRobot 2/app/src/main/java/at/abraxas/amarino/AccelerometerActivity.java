package at.abraxas.amarino;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AccelerometerActivity extends Activity implements
		SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	TextView title;
	Button mStart, mBack;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		title = (TextView) findViewById(R.id.val);
		mStart = (Button) findViewById(R.id.start);
		mBack = (Button) findViewById(R.id.back);

		mStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (Math.abs(x) > Math.abs(y)) {
			if (x < 0) {
				title.setText("Right");
				;
				// iv.setImageResource(R.drawable.right);
			}
			if (x > 0) {
				title.setText("Left");
				;
				// iv.setImageResource(R.drawable.left);
			}
		} else {
			if (y < 0) {
				title.setText("TOP");
				;
				// iv.setImageResource(R.drawable.top);
			}
			if (y > 0) {
				title.setText("Bottom");
				;
				// iv.setImageResource(R.drawable.bottom);
			}
		}
		if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
			title.setText("Center");
			;
			// iv.setImageResource(R.drawable.center);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
}
