package com.example.disastermanagementsender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivitycallreceive extends Activity {
	int frequency = 11025;  //1025
	@SuppressWarnings("deprecation")
	int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	AudioRecord audioRecord;
	boolean isRecording=false;  
	Socket socket;
	ObjectOutputStream oos;
	AudioTrack audioTrack;  
	EditText et;
	String ipaddress;   
	int count=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call);
		Button start = (Button) findViewById(R.id.button1);
		Button stop = (Button) findViewById(R.id.button2);
		Button play = (Button) findViewById(R.id.button3);
		et = (EditText) findViewById(R.id.et);
		
		int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
				frequency, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 
				bufferSize, 
				AudioTrack.MODE_STREAM);
		//audioTrack.play();
		  
		
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){
					@Override
					public void run(){
						 FileInputStream fis;
							try {
								fis = new FileInputStream(et.getText().toString());
								 byte[] buf = new byte[fis.available()];
								 fis.read(buf);     
								 fis.close(); 
								 audioTrack.write(buf, 0, buf.length);
								 audioTrack.play();
								 
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				}.start();
			}
		});

		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isRecording=true;
				ipaddress = et.getText().toString();
				   
				new Thread(){         
					@Override
					public void run(){
						   
						
						try {
							Displaytost(MainActivity.senderIP);
							socket = new Socket(MainActivity.senderIP,2000);
							Displaytost("Socket connected");
							oos = new ObjectOutputStream(socket.getOutputStream());
							//Readfile();
							
							Record();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}   
				}.start();
				
				
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRecording=false;				
				audioRecord.stop();
				audioTrack.stop();  
				audioTrack.release();
				
				if(socket!=null||oos!=null){
					try {
						socket.close();
						oos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void storeinfile(byte[] bytedata,String file){  
		try {
			   
 			FileOutputStream fs = new FileOutputStream(file,true);
 			fs.write(bytedata); 
 			     
 			                
			fs.close();  
			
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
	}
	
	public void Record(){
		int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
		frequency, channelConfiguration, 
		audioEncoding, bufferSize);
		byte[] buffer = new byte[bufferSize]; 
		audioRecord.startRecording();
		    
		long time1 = System.currentTimeMillis();
		while (isRecording) {
			  
		int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
		long time2 = System.currentTimeMillis();
		       
		long diff = time2 - time1;   
		
		long diffSeconds = diff / 1000 % 60;
		System.out.println("seconds"+diffSeconds);
		if(diffSeconds==2){
			// write to other file
			time1=System.currentTimeMillis();
			Readfile("/sdcard/disastervoice/temp"+count+".pcm");   
		    
			count++;
			storeinfile(buffer,"/sdcard/disastervoice/temp"+count+".pcm");
		}else{
			// write write
			storeinfile(buffer,"/sdcard/disastervoice/temp"+count+".pcm");
		}
		//audioTrack.write(buffer, 0, buffer.length);   
		//storeinfile(buffer);  
		System.out.println(""+buffer.length+" "+buffer);
		         
		//Readfile();  
		try {     
			oos.writeObject(buffer);
			//oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         
	                 
		}
		audioRecord.stop();
	}
	
	public void Readfile(String file){
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			 byte[] buf = new byte[fis.available()];
			 fis.read(buf);     
			 fis.close();    
			 oos.writeObject(buf);
//			 oos.close();
//			 socket.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Displaytost(final String message){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), message, 60000000).show();
			}
		});
	}
}
