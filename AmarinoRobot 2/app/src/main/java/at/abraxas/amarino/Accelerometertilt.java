package at.abraxas.amarino;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
	
public class Accelerometertilt extends Activity implements
SensorEventListener{

	protected static final int RESULT_SPEECH = 1;
	
	private TextView txtText;   
	String[] addresses; // connected devices
	String[] flags;  
	char selectedFlag;
	TextView title;              
	 private SensorManager mSensorManager;
	 private Sensor mAccelerometer;
	 Button mStart,mBack;
	 boolean isLeftSent =false;
	 boolean isRIghtSent = false;  
	@Override    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		txtText = (TextView) findViewById(R.id.txtText);
		 title = (TextView) findViewById(R.id.val);
		 
		

		 mStart = (Button) findViewById(R.id.start);
		 mBack = (Button) findViewById(R.id.back);
		  
		  mStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendVoice("1");
			}
		  });
		  
		  mBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					sendVoice("4");
				}
			  });
		
		
		IntentFilter intentFilter = new IntentFilter(AmarinoIntent.ACTION_CONNECTED_DEVICES);
	    registerReceiver(receiver, intentFilter);
	    
	    Intent intent = new Intent(Accelerometertilt.this, AmarinoService.class);
		intent.setAction(AmarinoIntent.ACTION_GET_CONNECTED_DEVICES);
		Accelerometertilt.this.startService(intent);
		    
	}     
  
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	getMenuInflater().inflate(R.menu.activity_main, menu);
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
				
				String voice=text.get(0);
				
				for(int i =0;i<text.size();i++){
					
					if(text.get(i).contains("left")){  
						
						 voice = text.get(i);   
						sendData(addresses[0], "2");   
						Toast.makeText(getApplicationContext(), ""+voice, 600).show();
						break;
						
					}else if(text.get(i).contains("right")){   
						 voice = text.get(i);
						sendData(addresses[0], "3");   
						Toast.makeText(getApplicationContext(), ""+voice, 600).show();
						break;
						   
					}else if(text.get(i).contains("go")){  
						 voice = text.get(i);
						sendData(addresses[0], "1");     
						Toast.makeText(getApplicationContext(), ""+voice, 600).show();
						break;       
						
					}else if(text.get(i).contains("back")){ 
						      
						//sendData(addresses[0], "4");
						 voice = text.get(i);
						Toast.makeText(getApplicationContext(), ""+voice, 600).show();
						break;   
					}else if(text.get(i).contains("stop")){
						//sendData(addresses[0], "5");
						 voice = text.get(i);  
						Toast.makeText(getApplicationContext(), ""+voice, 600).show();
						break;   
					}
					                
				}     
			
				txtText.setText(text.get(0));
			}   
			break;
		}

		}
	}
	
	  private void sendData(String address, String msg){
			Intent intent = new Intent(Accelerometertilt.this, AmarinoService.class);
			intent.setAction(AmarinoIntent.ACTION_SEND);
			intent.putExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS, address);
			intent.putExtra(AmarinoIntent.EXTRA_FLAG, selectedFlag);
			intent.putExtra(AmarinoIntent.EXTRA_DATA_TYPE, AmarinoIntent.STRING_EXTRA);
			intent.putExtra(AmarinoIntent.EXTRA_DATA, msg);
			Accelerometertilt.this.startService(intent);
		}
	  
	  BroadcastReceiver receiver = new BroadcastReceiver() {
			
			@Override  
			public void onReceive(Context context, Intent intent) {
				
				String action = intent.getAction();
				if (action == null) return;
				
				if (AmarinoIntent.ACTION_CONNECTED_DEVICES.equals(action)){
					addresses = intent.getStringArrayExtra(AmarinoIntent.EXTRA_CONNECTED_DEVICE_ADDRESSES);
					SensotRegister();
				}
			}    
		};  
		
		private void setupFlagsArray() {
				flags = new String[52];
				for (int i=0;i<52;i++){
					char flag;
					if (i<26)     
						flag = (char) ('A' + i);
					else
						flag = (char) ('a' + i-26); 
					flags[i] = String.valueOf(flag);
				}
			}
		  
		   
		  @Override
		  public void onAccuracyChanged(Sensor arg0, int arg1) {
		   // TODO Auto-generated method stub
		  }   
		        
	/*	  @Override  
		  public void onSensorChanged(SensorEvent event) {
		   float x = event.values[0];
		   float y = event.values[1];   
		   float z = event.values[2];
		   if (Math.abs(x) > Math.abs(y)) {  
		    if (x < 0) {
		 	title.setText("Right");;
		 	//UnregisterListener();
		          
		 	System.out.println(""+isLeftSent);
		 	if(!isLeftSent){   
		 		  sendVoice("3");
		 		  isLeftSent = true;         
		 		  //sendVoice("3");     
		 	}                    
		    if (x > 0) {            
		    	            
		 	   title.setText("Left");;
		 	  System.out.println(""+isLeftSent);
		 	   if(!isLeftSent){
		 		  sendVoice("2");   
		 		  isLeftSent = true;
		 	   }   
		 	         
		 	 // UnregisterListener();
		     //iv.setImageResource(R.drawable.left);
		    }         
		   } else {      
		    if (y < 0) {   
		 	   title.setText("TOP");;
		 	  isLeftSent = false;  
		 	  SensotRegister();   
		    // iv.setImageResource(R.drawable.top);
		    }  
		    if (y > 0) {
		 	   title.setText("Bottom");;   
		 	  isLeftSent = false;  
		 	 // SensotRegister();
		  //   iv.setImageResource(R.drawable.bottom);
		    }             
		   }                
		   if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
		 	  title.setText("Center");;
		 	  isLeftSent = false;      
		 	// SensotRegister();
		    //iv.setImageResource(R.drawable.center);
		   }  
		  }      
		  }*/   
		  public void SensotRegister(){
			  mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			  mAccelerometer = mSensorManager
			    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			  
			  mSensorManager.registerListener(this, mAccelerometer,
					    SensorManager.SENSOR_DELAY_NORMAL);
			     
		  }
		       
		  public void sendVoice(String command){
			  sendData(addresses[0], command);

		  }    
		  
		  public void UnregisterListener(){
			  mSensorManager.unregisterListener(this);
		  }
		  
		  
		  @Override  
		  public void onSensorChanged(SensorEvent event) {
		   float x = event.values[0];
		   float y = event.values[1];
		   float z = event.values[2];
		   if (Math.abs(x) > Math.abs(y)) {
		    if (x < 0) {
		 	title.setText("Right "+x);;  
		     //iv.setImageResource(R.drawable.right);
		 	 if(!isLeftSent){
		 		 SmsManager sms = SmsManager.getDefault();
		 		 sms.sendTextMessage("8553468857", null, "RIGHT - "+x, null, null);
//		 		  sendVoice("3");   
		 		  isLeftSent = true;
		 	   }   
		    }      
		    if (x > 0) {
		 	   title.setText("Left "+x);;  
		 	  if(!isLeftSent){
		 		 SmsManager sms = SmsManager.getDefault();
		 		 sms.sendTextMessage("8553468857", null, "LEFT - "+x, null, null);
//		 		  sendVoice("2");   
		 		  isLeftSent = true;    
		 	   }   
		 	     
		     //iv.setImageResource(R.drawable.left);
		    }   
		   } else {
		    if (y < 0) {
		 	   title.setText("TOP");;  
		 	   isLeftSent = false;
		 	   
		    // iv.setImageResource(R.drawable.top);
		    }   
		    if (y > 0) {
		 	   title.setText("Bottom");;
		  //   iv.setImageResource(R.drawable.bottom);
		 	  isLeftSent = false;
		    }     
		   }  
		   if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
		 	  title.setText("Center");;
		 	 isLeftSent = false;
		    //iv.setImageResource(R.drawable.center);
		   }
		  }
		  
}
