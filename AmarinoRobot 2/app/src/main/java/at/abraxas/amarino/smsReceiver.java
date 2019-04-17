package at.abraxas.amarino;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class smsReceiver extends BroadcastReceiver {

	static String incomingno;
	Context cntxt;
	SharedPreferences sp;
	SmsManager smanager;
	static MediaPlayer mp;
	NotificationManager notificationManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		cntxt = context;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		smanager = SmsManager.getDefault();
		notificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		mp = new MediaPlayer();

		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				incomingno = msgs[i].getOriginatingAddress();
				String body = msgs[i].getMessageBody().toString();

				TelephonyManager TelephonyMgr = (TelephonyManager) context
						.getSystemService(context.TELEPHONY_SERVICE);
				String m_deviceId = TelephonyMgr.getDeviceId();

				Toast.makeText(context, "" + body, 60000).show();
				
				body = body.replaceAll("\n", "");
				if (body.contains("VEHICLE STARTED")
						|| body.contains("problem")
						|| body.contains("EMERGENCY")
						|| body.contains("vehicle emission")|| body.contains("POWER THEFT") || body.contains("ATM THEFT")) {

					Notification updateComplete = new Notification();
					updateComplete.icon = android.R.drawable.stat_notify_sync;
					updateComplete.tickerText = "Click here to go to alert";
					updateComplete.when = System.currentTimeMillis();
					Intent notificationIntent = new Intent(context,
							MainScreen.class);
					PendingIntent contentIntent = PendingIntent.getActivity(
							context, 0, notificationIntent, 0);
					updateComplete.setLatestEventInfo(context,
							"The Notification", "please click here",
							contentIntent);
					notificationManager.notify(100, updateComplete);

					mp = new MediaPlayer();
					try {
						Toast.makeText(context, "found", 60000).show();
						mp.setDataSource(getFilePath("prob"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
							
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("CHAIN")) {

					Notification updateComplete = new Notification();
					updateComplete.icon = android.R.drawable.stat_notify_sync;
					updateComplete.tickerText = "Click here to go to alert";
					updateComplete.when = System.currentTimeMillis();
					Intent notificationIntent = new Intent(context,
							MainScreen.class);
					PendingIntent contentIntent = PendingIntent.getActivity(
							context, 0, notificationIntent, 0);

					updateComplete.setLatestEventInfo(context,
							"The Notification", "please click here",
							contentIntent);
					notificationManager.notify(100, updateComplete);

					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("chain"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("TROUBLE")) {
					String[] temp = body.split("\\=");
					mp = new MediaPlayer();
					try {
							
						mp.setDataSource(getFilePath("trouble"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("HEART")) {
					String[] temp = body.split("\\=");
					if (Integer.parseInt(temp[1]) >= 80) {
						mp = new MediaPlayer();
						try {

							mp.setDataSource(getFilePath("heart"));
							mp.prepare();
							mp.start();

						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if (body.contains("ALCOHOL")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("alcohol"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
				if (body.contains("safe")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("safe"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("AWAKE")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("awake"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
				
				if (body.contains("SLEEP")) {
					mp = new MediaPlayer();
					try {
							
						mp.setDataSource(getFilePath("sleep"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("BAG")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("bag"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
				if (body.contains("OBSTACLE")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("obs"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
				if (body.contains("RASH")) {
					mp = new MediaPlayer();
					try {
						
						mp.setDataSource(getFilePath("rash"));
						mp.prepare();
						mp.start();
						
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("BAD SERVICE")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("bad"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("ACCIDENT")) {
					
					
					Notification updateComplete = new Notification();
					updateComplete.icon = android.R.drawable.stat_notify_sync;
					updateComplete.tickerText = "Alert, Accident detected";
					updateComplete.when = System.currentTimeMillis();
					Intent notificationIntent = new Intent(context,
							MainScreen.class);
					PendingIntent contentIntent = PendingIntent.getActivity(
							context, 0, notificationIntent, 0);
					updateComplete.setLatestEventInfo(context,
							"Alert, Accident detected", "Alert, Accident detected",
							contentIntent);
					notificationManager.notify(100, updateComplete);
					
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("prob"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("HR")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource("/sdcard/iot/hr.ogg");
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				if (body.contains("TEMP")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("temp"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (body.contains("CRACK")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("crack"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("LOAD")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("load"));
						mp.prepare();
						mp.start();
						
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (body.contains("WATER")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("water"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (body.contains("INTRUDER")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("intruder"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (body.contains("HUMAN")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("human"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (body.contains("BOMB")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("bomb"));
						mp.prepare();
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (body.contains("GAS")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("gas"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
				if (body.contains("SMOKE")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("smoke"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("POWER LINE CUT")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("line"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("OIL")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("oil"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("TEMP")) {
					mp = new MediaPlayer();
					try {
							
						mp.setDataSource(getFilePath("temp"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("FIRE")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("fire"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("LEVEL")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("level"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("ODOUR")){
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("odour"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("SEAT")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("seat"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
				if (body.contains("BIN")) {
					mp = new MediaPlayer();
					try {
						
						mp.setDataSource(getFilePath("bin"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("TREE CUT")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("tree"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("PH")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("ph"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("M2")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("m2"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("M1")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("m1"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (body.contains("HUMIDITY")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("humidity"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (body.contains("LAND WET")) {
					mp = new MediaPlayer();
					try {

						mp.setDataSource(getFilePath("land"));
						mp.prepare();
						mp.start();
							
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (body.contains("loc1234")) {

					String[] temp = body.split(" ");
					if (temp.length == 2) {

						String uri = "http://maps.google.com/maps?q=" + temp[1] +" (" + "location" + ")";
						Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
						it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(it);
						/*// LocationSaver(temp);
						Intent it = new Intent(cntxt, GMaps.class);
						it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						it.putExtra("location", temp[1]);
						cntxt.startActivity(it);*/
					}
				}
			}
		}

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp1) {
				// TODO Auto-generated method stub

				mp.stop();
				mp.release();
				/*
				 * mp = new MediaPlayer(); try {
				 * mp.setDataSource("/sdcard/voice.amr"); mp.prepare();
				 * mp.setOnCompletionListener(this); mp.start(); } catch
				 * (IllegalArgumentException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); } catch (IllegalStateException e)
				 * { // TODO Auto-generated catch block e.printStackTrace(); }
				 * catch (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
			}
		});
	}

	public void LocationSaver(String[] temp) {

		try {
			FileOutputStream mFileOutputStream = new FileOutputStream(
					"/sdcard/location.txt", true);
			mFileOutputStream.write((temp[1] + temp[2]).getBytes());
			mFileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void alertDialog(String string) {
		// TODO Auto-generated method stub
		AlertDialog.Builder d1 = new AlertDialog.Builder(cntxt);
		d1.setMessage(string);
		d1.setTitle("Warning");
		d1.setPositiveButton("OK", null);
		d1.setCancelable(true);
		d1.create().show();

	}

	public String getFilePath(String filename) {
		String filepath = "/sdcard/iot/";
		File file = new File(filepath);
		if (file.exists()) {
			File[] list = file.listFiles();
			for (File f : list) {
				if (f.getName().toLowerCase().contains(filename)) {
					return f.getAbsolutePath();
				}
			}
		}
		return "";
	}

}
