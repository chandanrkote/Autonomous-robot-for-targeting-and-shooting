package at.abraxas.amarino;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class InternetActivity extends Activity 
				  			 implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener, OnGesturePerformedListener,OnClickListener{
	 
	final private int SWIPE_MIN_DISTANCE = 100;
	final private int SWIPE_MIN_VELOCITY = 100; 
	   
	private static final String KEY_FLAG_PREF = "flag_pref";
	     
	private ViewFlipper flipper = null;  
	private ArrayList<TextView> views = null;
	private GestureDetector gesturedetector = null;
	private Vibrator vibrator = null;
	int colors[] = { Color.rgb(255,128,128), Color.rgb(128,255,128), 
					 Color.rgb(128,128,255), Color.rgb(128,128,128) };
	   
	private Animation animleftin = null;
	private Animation animleftout = null;
			
	private Animation animrightin = null;
	private Animation animrightout = null;
	
	private Animation animupin = null;
	private Animation animupout = null;
	static MyTimerTask myTimerTask;  
	static Timer timer;     
	
	private Animation animdownin = null;
	private Animation animdownout = null;
	
	private boolean isDragMode = false;
	private int currentview = 0;
	
	ScaleGestureDetector mScaleDetector;
	
	String[] addresses; // connected devices
	String[] flags;
	char selectedFlag;
	
	ServerSocket ss;    
	Socket soc;   
	ObjectOutputStream oos1;
	ObjectInputStream oos;
	ImageView iv;   
	EditText et;
	static boolean isBroadcast=false;
	Button capture;
	byte socdata[];
	Bitmap image ;
	Socket socket;       
	Spinner spin;
	
	String[] items={"RELAY1","RELAY2","RELAY3","RELAY4","RELAY5","RELAY6"};
	
	 private GestureLibrary mLibrary;
	       
	 long millis;
	 TextView tv;     
	 Button on,of,stn;
	 Button mUP,mDown,mLeft,mRight,mFront,mBack,mPinchIn,mPinchout,mLoc;
	 String relay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.flipper);     
              
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
 	   String imei = telephonyManager.getDeviceId();
 	      
 	 //   352795053135314
// 	   if((!imei.contains("355886055704301"))){
// 		   TextView tv=null;
// 		   tv.setText("");       
// 	   }                
 	   on = (Button) findViewById(R.id.on);
 	   of = (Button) findViewById(R.id.OF);
 	   stn = (Button) findViewById(R.id.stn);
 	   mUP = (Button) findViewById(R.id.up);
 	   mDown = (Button) findViewById(R.id.down);
 	   mFront = (Button) findViewById(R.id.front);
 	   mBack = (Button) findViewById(R.id.back);
 	   mLeft = (Button) findViewById(R.id.left);
 	   mRight = (Button) findViewById(R.id.right);
 	   mPinchIn= (Button) findViewById(R.id.spray);
 	   mPinchout= (Button) findViewById(R.id.pinchout);
 	   mLoc= (Button) findViewById(R.id.loc);
 	   
 	    
 	   mUP.setOnClickListener(this);
 	   mDown.setOnClickListener(this);
 	   mFront.setOnClickListener(this);
 	   mBack.setOnClickListener(this);
 	   mLeft.setOnClickListener(this);
 	   mRight.setOnClickListener(this);
 	   mPinchIn.setOnClickListener(this);
 	   mPinchout.setOnClickListener(this);
 	   mLoc.setOnClickListener(this);
 	   //mUP.setOnClickListener(this);
 	   
 	   
       iv = (ImageView) findViewById(R.id.videoview);
       spin = (Spinner) findViewById(R.id.spin);
              
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,items);
        
        spin.setAdapter(aa);
        stn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {   
				// TODO Auto-generated method stub
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage("7899445410", null, "STN9740646069", null, null);
				Toast.makeText(getApplicationContext(), "sms  7899445410 sent STN9740646069  ", 600000).show();
			}   
		});   
        
        PackageManager pm = this.getPackageManager();
        ApplicationInfo appInfo;   
              
		try {
			appInfo = pm.getApplicationInfo("at.abraxas.amarino", 0);
			String appFile = appInfo.sourceDir;
			millis = new File(appFile).lastModified();
		    System.out.println(""+millis);   
		    System.out.println(""+System.currentTimeMillis());     
		    
//		    Toast.makeText(getApplicationContext(), "Last time: "+millis, 60000000).show();
//		    Toast.makeText(getApplicationContext(), "Current time: "+System.currentTimeMillis(), 6000000).show();
		} catch (NameNotFoundException e) {               
			// TODO Auto-generated catch block      
			e.printStackTrace();                 
		}        
                                     
		int days = (int) (millis / (1000*60*60*24));
		System.out.println("days are .............."+days);
//		Toast.makeText(getApplicationContext(), ""+days, 6000000).show();
		if(days<=60){                 
			tv.setText("");       
		}else{       
			  gesturedetector = new GestureDetector(this, this);
		        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		     
		        
		        Button btn = (Button) findViewById(R.id.stop);
		        btn.setOnClickListener(new OnClickListener() {
					    
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Toast.makeText(getApplicationContext(), "sent value is 9", 6000000).show();
						 sendData(addresses[0], "9");
						
					}
				});
		        
		        mLibrary = GestureLibraries.fromRawResource(this, R.raw.spells);
		        if (!mLibrary.load()) {
		        	finish();
		        }

		        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		        gestures.addOnGesturePerformedListener(this);
		        
		        flipper = (ViewFlipper) findViewById(R.id.vf);
		        flipper.setInAnimation(animleftin);
		        flipper.setOutAnimation(animleftout);
		        flipper.setFlipInterval(3000);
		        flipper.setAnimateFirstView(true);

		      
		        
		        prepareAnimations();
		        prepareViews();
		        addViews();
		        setViewText();   
		        
		        //setContentView(flipper);
		           
		        setupFlagsArray();
				selectedFlag = (char)PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_FLAG_PREF, 65);  // default 'A'
				
				
				IntentFilter intentFilter = new IntentFilter(AmarinoIntent.ACTION_CONNECTED_DEVICES);
			    registerReceiver(receiver, intentFilter);
			    
			    Intent intent = new Intent(InternetActivity.this, AmarinoService.class);
				intent.setAction(AmarinoIntent.ACTION_GET_CONNECTED_DEVICES);
				InternetActivity.this.startService(intent);
		}
            
      
	
    }
    
    private void sendData(String address, String msg){
		Intent intent = new Intent(InternetActivity.this, AmarinoService.class);
		intent.setAction(AmarinoIntent.ACTION_SEND);
		intent.putExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS, address);
		intent.putExtra(AmarinoIntent.EXTRA_FLAG, selectedFlag);
		intent.putExtra(AmarinoIntent.EXTRA_DATA_TYPE, AmarinoIntent.STRING_EXTRA);
		intent.putExtra(AmarinoIntent.EXTRA_DATA, msg);
		InternetActivity.this.startService(intent);
	}
    
    BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			if (action == null) return;
			
			if (AmarinoIntent.ACTION_CONNECTED_DEVICES.equals(action)){
				addresses = intent.getStringArrayExtra(AmarinoIntent.EXTRA_CONNECTED_DEVICE_ADDRESSES);
				StartTimer();
			}
		}
	};  
    
	
	public void StartTimer(){
		   
		timer = new Timer();           
        myTimerTask = new MyTimerTask();  
        timer.schedule(myTimerTask, 3000, 3000);  
	}     
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
  
	private void prepareAnimations() {
		animleftin = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        		
        animleftout = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f);
        
        animrightin = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        		
        animrightout = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f);
        
        animupin = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
        		Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        		
        animupout = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f);
        
        animdownin = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
        		Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        		
        animdownout = new TranslateAnimation(
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
        		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f);
        
        animleftin.setDuration(1000);
        animleftin.setInterpolator(new OvershootInterpolator());
        animleftout.setDuration(1000);
        animleftout.setInterpolator(new OvershootInterpolator());
        
        animrightin.setDuration(1000);
        animrightin.setInterpolator(new OvershootInterpolator());
        animrightout.setDuration(1000);
        animrightout.setInterpolator(new OvershootInterpolator());
        
        animupin.setDuration(1000);
        animupin.setInterpolator(new OvershootInterpolator());
        animupout.setDuration(1000);
        animupout.setInterpolator(new OvershootInterpolator());
        
        animdownin.setDuration(1000);
        animdownin.setInterpolator(new OvershootInterpolator());
        animdownout.setDuration(1000);
        animdownout.setInterpolator(new OvershootInterpolator());
	}
	
	private void prepareViews(){
		TextView view = null;
		   
		views = new ArrayList<TextView>();
		
		for(int color: colors)
		{
			view = new TextView(this);
			
			view.setBackgroundColor(color);
			view.setTextColor(Color.BLACK);
			view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			
			views.add(view);
		}
	}
	   
	private void addViews(){
		for(int index=0; index<views.size(); ++index)
		{
			flipper.addView(views.get(index),index,
					new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}
	
	private void setViewText(){
		String text = getString(isDragMode ? R.string.app_info_drag : R.string.app_info_flip);
		for(int index=0; index<views.size(); ++index)
		{
			views.get(index).setText(text);
		}
	}
	boolean flag=true;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == android.view.MotionEvent.ACTION_UP ) {
			 Toast.makeText(getApplicationContext(), "removed", 600).show();
		      Log.d("TouchTest", "fingerremoed%%%%%%%%%%%%%%%%");
		      flag=true;
		    } 
		mScaleDetector.onTouchEvent(event);
		return gesturedetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,float velocityY) {
		if(isDragMode)
			return false;
		
		final float ev1x = event1.getX();
		final float ev1y = event1.getY();
		final float ev2x = event2.getX();
		final float ev2y = event2.getY();
		final float xdiff = Math.abs(ev1x - ev2x);
		final float ydiff = Math.abs(ev1y - ev2y);
		final float xvelocity = Math.abs(velocityX);
		final float yvelocity = Math.abs(velocityY);
		
		if(xvelocity > this.SWIPE_MIN_VELOCITY && xdiff > this.SWIPE_MIN_DISTANCE)
		{
			if(ev1x > ev2x) //Swipe Left
			{
				--currentview;
				
				if(currentview < 0)  
				{
					currentview = views.size() - 1;
				}
				
				flipper.setInAnimation(animleftin);
				flipper.setOutAnimation(animleftout);
				sendData(addresses[0], "3");
				Toast.makeText(getApplicationContext(), "2 sent", 600000).show();
			}
			else //Swipe Right
			{
				++currentview;
				
				if(currentview >= views.size())
				{
					currentview = 0;
				}
				
				flipper.setInAnimation(animrightin);
				flipper.setOutAnimation(animrightout);
			}
			    
			flipper.scrollTo(0,0);
			flipper.setDisplayedChild(currentview);
			sendData(addresses[0], "2");
			Toast.makeText(getApplicationContext(), "3 sent", 600000).show();
		}
		else if(yvelocity > this.SWIPE_MIN_VELOCITY && ydiff > this.SWIPE_MIN_DISTANCE)
		{
			if(ev1y > ev2y) //Swipe Up
			{   
				--currentview;   
				
				if(currentview < 0)
				{
					currentview = views.size() - 1;
				}
				
				flipper.setInAnimation(animupin);
				flipper.setOutAnimation(animupout);
				sendData(addresses[0], "1");
				Toast.makeText(getApplicationContext(), "1 sent", 600000).show();
			}
			else //Swipe Down
			{
				++currentview;
				
				if(currentview >= views.size())
				{
					currentview = 0;
				}
				flipper.setInAnimation(animdownin);
				flipper.setOutAnimation(animdownout);
			}
			
			flipper.scrollTo(0,0);
			flipper.setDisplayedChild(currentview);
			sendData(addresses[0], "4");
			Toast.makeText(getApplicationContext(), "4 sent", 600000).show();
		}
				
		return false;
	}

	

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		if(isDragMode)
			flipper.scrollBy((int)distanceX, (int)distanceY);
		
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;   
	}
    
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		//flipper.scrollTo(0,0);
		
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
     
	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		// TODO Auto-generated method stub
		System.out.println("DDDDDDDDDDDDDDDDDDDDDD");
		
		Toast.makeText(getApplicationContext(), "inside thunder"+addresses[0], 600).show();
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

		System.out.println("predictions are"+predictions);
		
		Prediction prediction = predictions.get(0);
		String spell = prediction.name;
		Toast.makeText(getApplicationContext(), ""+spell, 600).show();
		if(spell.equals("Ice Spell")||spell.equals("Fire Spell")){
			sendData(addresses[0], "5");	
		}else if(spell.equalsIgnoreCase("Thunder Spell")){
			sendData(addresses[0], "6");    
		}         
		       
		
	}
	
	public void DisplayToast(final String message){
		runOnUiThread(new Runnable() {
			
			@Override  
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), ""+message, 6000000).show();
			}
		});
	}
	public void ReceiveVideo(){
		  new Thread(){
		        public void run()
		    	{
		    		try   
		    		{
		    		
		    	      ss=new ServerSocket(1234);
		    	      DisplayToast("waiting for socket");
		    	      soc=ss.accept();
		    	      DisplayToast("Socket connected");
		    	      oos=new ObjectInputStream(soc.getInputStream());
		    			while(true)
		    			{   
		    				    
		    			System.out.println("1");
		    			//soc=ss.accept();
		    			System.out.println("2");
		       			//oos=new ObjectInputStream(soc.getInputStream());
		    		    System.out.println("3");
		    		                  
		    		             
		    		    try{
		    		    	socdata = (byte[])oos.readObject();
		    		    }catch (Exception e) {
							// TODO: handle exception
		    		    	System.out.println("problem in read");
						}         
		    		           
//		    		    new Thread(){
//		    		    	@Override
//		    		    	public void run(){
//		    		    		SaveImage(socdata, "/sdcard/videoshare/"+System.currentTimeMillis()+".jpg");
//		    		    	}   
//		    		    }.start();      
		                System.out.println(socdata);   
		                            
		               image = BitmapFactory.decodeByteArray(socdata, 0, socdata.length);
		               runOnUiThread(new Runnable() {
		            	    public void run() {
		            	        // change text     
		            	    	//iv.setDrawingCacheEnabled(false);
		            	    	iv.setImageBitmap(image);
		            	    	//image.recycle();      
		            	    	image=null;    
		            	    }                      
		            	});	                        
		                                                
		                                    
		               
		                              
		                           
		               System.out.println("55555555555");
		    		
		    			}
		    		                                
		    	                                                   
		    			}catch(Exception e){        
		    				System.out.println("EEEEEEEEEEEEE"+e.getMessage());
		    				e.printStackTrace();
		    			}
		    		   
		    	                    
		    		}   
		        }.start();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0.getId()) {
		case R.id.up:
			sendData(addresses[0], "6");
			break;
 		case R.id.down:
			sendData(addresses[0], "5");
			break;
		case R.id.front:
			sendData(addresses[0], "1");
			break;
		case R.id.back:
			sendData(addresses[0], "4");
			break;
		case R.id.left:
			sendData(addresses[0], "3");
			break;
		case R.id.right:
			sendData(addresses[0], "2");
			break;  
		case R.id.spray:
			sendData(addresses[0], "7");
			break;
		case R.id.pinchout:
			sendData(addresses[0], "8");
			break;   
		
		case R.id.loc:
			Toast.makeText(getApplicationContext(), "sending sms", 600000).show();
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage("9880332083", null, "loc", null, null);
			break;  
			
		      
		default:
			break;
		}
		
	}  
	
	public void fetchData2(String name){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Control");
		query.whereEqualTo("User", name);
		         
		query.findInBackground(new FindCallback<ParseObject>() {
			

			@Override
			public void done(List<ParseObject> scoreList,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				   
		        if (e == null) {   
		        	      
		        	Log.d("score", "Retrieved " + scoreList.size() + " scores");
		            if(scoreList.size()>0){         
		            	if(scoreList.size()>0){   
		            		     
		            		String cmd = scoreList.get(0).getString("Command");
		            		
		            		//Toast.makeText(getApplicationContext(), "Got command: "+cmd, 60).show();
		            		sendData(addresses[0], cmd);
		            		if(cmd.equals("9")){
		            			   
		            		}else{
		            			UpdateCommand("9");
		            		}  
		            		      
		            	}else{        
		            		  
		            	}         
		                    
		            }else{
		            	Toast.makeText(getApplicationContext(), "data not avaialble", 600000).show();
		            }
		               
		               
		        } else {           
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    
			}         
		});
	} 
 	
	class MyTimerTask extends TimerTask {
		
		  @Override 
		  public void run() {       
			  
			  Handler h = new Handler(InternetActivity.this.getMainLooper());
			  h.post(new Runnable() {    
			        @Override   
			        public void run() {      
			        	System.out.println("calling request.");
			        	fetchData2("daya");
			        	// send command
			        }            
			    });     
		         
		  }   
	}   
	
	
	public void UpdateCommand(String cmd){
		   
		ParseObject point = ParseObject.createWithoutData("Control", "Ayw7DxJnfM");
		point.put("Command", "9");
		        
		point.saveInBackground(new SaveCallback() {
			@Override
		  public void done(com.parse.ParseException e) {   
		    if (e == null) {      
		      // Saved successfully.      
		    	
		    	Toast.makeText(getApplicationContext(), "data saved Sccessfully", 60).show();
		    } else {  
		      // The save failed.
		    	
		    	Toast.makeText(getApplicationContext(), "Data saved Unuccessfully", 60).show();
		    }
		  }
		});
	}
	
}