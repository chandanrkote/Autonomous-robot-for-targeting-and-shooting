package com.example.disastermanagementsender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button text, call, photo, video, searchIP, mFile;
	EditText mFilename;
	String ip;
	Spinner spin;
	static String senderIP;
	String filePath = "/sdcard/instapp/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		boolean isChat = getIntent().getBooleanExtra("chat", false);
		spin = (Spinner) findViewById(R.id.spinner1);
		mFilename = (EditText) findViewById(R.id.editText1);
		text = (Button) findViewById(R.id.button1);
		call = (Button) findViewById(R.id.button3);
		photo = (Button) findViewById(R.id.button2);
		searchIP = (Button) findViewById(R.id.button11);
		video = (Button) findViewById(R.id.button4);
		mFile = (Button) findViewById(R.id.file);
		
		
		if(isChat){
//			text.setVisibility(View.GONE);
			call.setVisibility(View.GONE);
			photo.setVisibility(View.GONE);
			video.setVisibility(View.GONE);
		}
		
		getIps();

		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				senderIP = ips.get(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		mFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				filePath = filePath + mFilename.getText().toString();

				new Thread() {
					@Override
					public void run() {
						Connectsocket("FILE", senderIP);
					}
				}.start();
			}
		});
		
		searchIP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread() {
					@Override
					public void run() {
						WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
						String ipaddr = Formatter.formatIpAddress(wm
								.getConnectionInfo().getIpAddress());

						ArrayList<InetAddress> ret = getConnectedDevices(ipaddr);
						final ArrayList<String> ips = new ArrayList<String>();
						for (int i = 0; i < ret.size(); i++) {

							ips.add(ret.get(i).getHostAddress());
						}
							
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ArrayAdapter<String> aa = new ArrayAdapter<String>(
										MainActivity.this,
										android.R.layout.simple_spinner_item,
										ips);
								spin.setAdapter(aa);
							}
						});
					}
				}.start();
			}
		});

		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				new Thread() {
					@Override
					public void run() {
						Connectsocket("TEXT", senderIP);
					}
				}.start();

			}
		});

		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {
					@Override
					public void run() {
						Connectsocket("CALL", senderIP);
					}

				}.start();

			}
		});

		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {
					@Override
					public void run() {
						Connectsocket("PHOTO", senderIP);
					}

				}.start();

				// Connectsocket("PHOTO");
			}
		});

		video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(MainActivity.this,Register.class);
				// startActivity(intent);
				new Thread() {
					@Override
					public void run() {
						Connectsocket("VIDEO", senderIP);
					}

				}.start();

			}
		});

	}

	public void updateSpinner() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayAdapter<String> aa = new ArrayAdapter<String>(
						MainActivity.this,
						android.R.layout.simple_spinner_item, ips);
				aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spin.setAdapter(aa);
			}
		});
	}

	ArrayList<String> ips = new ArrayList<String>();

	public void getIps() {

		new Thread() {
			@Override
			public void run() {
				WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
				String ipaddr = Formatter.formatIpAddress(wm
						.getConnectionInfo().getIpAddress());

				ArrayList<InetAddress> ret = getConnectedDevices(ipaddr);
				// final ArrayList<String> ips = new ArrayList<String>();
				ips.clear();
				for (int i = 0; i < ret.size(); i++) {

					ips.add(ret.get(i).getHostAddress());
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ArrayAdapter<String> aa = new ArrayAdapter<String>(
								MainActivity.this,
								android.R.layout.simple_spinner_item, ips);
						aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spin.setAdapter(aa);
					}
				});
			}
		}.start();

	}

	public void DisplayToast(final String message) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), message, 600).show();
			}
		});
	}

	public void Connectsocket(String message, String ip) {

		String subCmd = message;
		try {

			Socket socket = new Socket(ip, 5000);
			DisplayToast("Socket connected");
			// ObjectInputStream oin = new
			// ObjectInputStream(socket.getInputStream());
			ObjectOutputStream o1 = new ObjectOutputStream(
					socket.getOutputStream());
			DisplayToast("Writing data");

			o1.writeObject(message);

			DisplayToast("Writen data");

			if (message.equalsIgnoreCase("VIDEO")) {
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			} else if (message.equalsIgnoreCase("TEXT")) {
				Intent intent = new Intent(MainActivity.this,
						ChatActivity.class);
				
				startActivity(intent);  
			} else if (message.equalsIgnoreCase("PHOTO")) {
				Intent intent = new Intent(MainActivity.this,
						MainActivity1.class);
				startActivity(intent);
			} else if (message.equalsIgnoreCase("CALL")) {
				Intent i = new Intent(this, MainActivitycallreceive.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			} else if (message.equalsIgnoreCase("File")) {
				File file = new File(filePath);
				byte[] data = Readfile(filePath);
				o1.writeObject(file.getName());
				o1.writeObject(data);
				
			}
			
			o1.close();
			socket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DisplayToast("Main:: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			DisplayToast("Main:: " + e.getMessage());
			e.printStackTrace();
		}

	}

	
	private int LoopCurrentIP = 0;

	public ArrayList<InetAddress> getConnectedDevices(String YourPhoneIPAddress) {
		ArrayList<InetAddress> ret = new ArrayList<InetAddress>();

		LoopCurrentIP = 0;

		String IPAddress = "";
		String[] myIPArray = YourPhoneIPAddress.split("\\.");
		InetAddress currentPingAddr;
		int ite = 0;
		for (int i = 0; i <= 255; i++) {
			ite = i;
			try {

				// build the next IP address
				currentPingAddr = InetAddress.getByName(myIPArray[0] + "."
						+ myIPArray[1] + "." + myIPArray[2] + "."
						+ Integer.toString(LoopCurrentIP));

				// 50ms Timeout for the "ping"
				if (currentPingAddr.isReachable(500)) {

					ret.add(currentPingAddr);
					ips.add(currentPingAddr.getHostAddress());
					updateSpinner();
					DisplayToast("Found ip:: "
							+ currentPingAddr.getHostAddress());
				} else {
					// DisplayToast("not Found ip:: "+currentPingAddr.getHostAddress());
				}
			} catch (UnknownHostException ex) {
				DisplayToast("Ip config:: " + ex.getMessage());
			} catch (IOException ex) {
				DisplayToast("Ip config:: " + ex.getMessage());
			}

			LoopCurrentIP++;
		}
		DisplayToast("Total ip:: " + ret.size() + " total iteration:: " + ite);
		return ret;
	}

	public byte[] Readfile(String path) {

		try {

			File file = new File(path);
			FileInputStream fin = new FileInputStream(file);

			byte[] buf = new byte[fin.available()];
			fin.read(buf);
			fin.close();

			DisplayToast(MainActivity.senderIP);
			return buf;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public void ConnectsocketChat(String ip, byte[] data, String filename) {

		try {
			DisplayToast("Socket called");
			Socket socket = new Socket(ip, 6002);
			DisplayToast("Socket connected");

			ObjectOutputStream o1 = new ObjectOutputStream(
					socket.getOutputStream());
			DisplayToast("Writing data");
			o1.writeObject(data);
			o1.writeObject(filename);
			DisplayToast("Writen data");
			o1.close();
			socket.close();
			finish();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DisplayToast("Photo " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DisplayToast("Photo " + e.getMessage());
		}

	}
}
