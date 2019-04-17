package com.example.disastermanagementsender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity1 extends Activity {
	private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;
	Intent i;
	String imgPath = "";
	static String ip = "192.168.1.35";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
		startActivityForResult(i, CAPTURE_IMAGE_CAPTURE_CODE);

	}   
    
	public Uri setImageUri() {
		// Store image in dcim
		File file = new File("/sdcard/disaster.png");
		if (file.exists()) {
			file.delete();
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Uri imgUri = Uri.fromFile(file);
		this.imgPath = file.getAbsolutePath();
		return imgUri;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAPTURE_IMAGE_CAPTURE_CODE) {
			if (resultCode == RESULT_OK) {

				Toast.makeText(this, "Image Captured", Toast.LENGTH_LONG)
						.show();
				new Thread() {
					@Override
					public void run() {
						Readfile("/sdcard/disaster.png");
					}
				}.start();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void Readfile(String path) {

		try {

			File file = new File(path);
			FileInputStream fin = new FileInputStream(file);

			byte[] buf = new byte[fin.available()];
			fin.read(buf);
			fin.close();

			DisplayToast(MainActivity.senderIP);
			ConnectsocketChat(MainActivity.senderIP, buf, file.getName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void DisplayToast(final String message) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), message, 600000000)
						.show();
			}
		});
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

}
