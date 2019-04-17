package com.example.disastermanagementsender;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;




   

import android.os.Bundle;
import android.preference.Preference;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends Activity {
      
	 int t,tt,o;
	 int i=1,j=2;
	 CheckBox cb;
 	 Button btn;
	 EditText et;
	 String ans,ans1="",full="";
	 CompoundButton buttonView;
	 ArrayList<String> list = new ArrayList<String>();
	 Socket ss;
	 DataInputStream ois;
	 DataOutputStream oos;   
	 String ip[] = new String[30];    
	 static String systemIP;   
	 Button Uploadbtn,downloadBtn;
	 static String userinfo="";   
	      
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
      
       super.onCreate(savedInstanceState);
     
       TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	   String imei = telephonyManager.getDeviceId();
	    
       ScrollView sv = new ScrollView(this);
            
       final LinearLayout ll = new LinearLayout(this);
       
       ll.setOrientation(LinearLayout.VERTICAL);
       sv.addView(ll);
       TextView tv = new TextView(this);
            
       tv.setText("Please enter ip Address here ");
       ll.addView(tv);
       et = new EditText(this);
      // ll.addView(et);
 	   Button b = new Button(this);
       b.setText("Register");   
     //  ll.addView(b);   
       btn = new Button(RegisterActivity.this);
       btn.setText("Streaming");   
       ll.addView(btn);             
       
       Uploadbtn = new Button(this);   
       Uploadbtn.setText("Upload");   
       Uploadbtn.setVisibility(View.GONE);
       ll.addView(Uploadbtn);    
       
       downloadBtn = new Button(this);   
       downloadBtn.setText("Download");
       ll.addView(downloadBtn);
       Uploadbtn.setVisibility(View.GONE);   
       downloadBtn.setVisibility(View.GONE);   
       	
         
       list.add(MainActivity.senderIP);
       downloadBtn.setOnClickListener(new OnClickListener() {
		   
		@Override   
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(et.getText().toString().equalsIgnoreCase("")){
				Toast.makeText(getApplicationContext(), "Enter IP address.", 6000000).show();
			}else{   
				       
			}
		}   
	});       
       
       Uploadbtn.setOnClickListener(new OnClickListener() {
		   
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(et.getText().toString().equalsIgnoreCase("")){
				Toast.makeText(getApplicationContext(), "Enter IP address.", 6000000).show();
			}else{
				
			}
		}
	});
       
       b.setOnClickListener(new View.OnClickListener() {
       public void onClick(View v) {
    	   
    	   
    	   
     // TODO Auto-generated method stub
    	       
       cb = new CheckBox(RegisterActivity.this);
       cb.setText(et.getText().toString());
       et.setText("");
       cb.setOnCheckedChangeListener(new OnCheckedChangeListener() 
       {   
            			
       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              			// TODO Auto-generated method stub
        if(isChecked)    
        ans=(String)buttonView.getText();
        ans1+=ans+"#";
        list.add(ans);
        }
        });
        ll.addView(cb);
        }
        });
        btn.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
			// TODO Auto-generated method stub
	    	
	    	 	  
	    	 Intent intent = new Intent(RegisterActivity.this, OneShotPreviewActivity.class);
	    	 intent.putExtra("key",list);
	    	 startActivity(intent);   

	      }
		
		
	});
           this.setContentView(sv);
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
	    public void ConnectSocket(){
	 	        
	    	  try {
	    		   File f = new File("/sdcard/migration.apk");
	    		                  
	    	                                         
	    		 //  Toast.makeText(getApplicationContext(), "selected ip is "+selectedip, 6000000).show();
				   Socket socket = new Socket(et.getText().toString(),4444);
				  // Socket socket = new Socket(et.getText().toString(),1234);
		           DisplayToast("Socket connected");
				   ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				   //ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
				                       
				   FileInputStream fis = new FileInputStream(f);
				   byte[] buf = new byte[fis.available()];
				                                        
				                            
				   fis.read(buf);        
				                             
				   fis.close();      
				   DisplayToast("Writing data");    
				   outputStream.writeObject(buf);
				         
				   DisplayToast("Data written");  
				           
				   outputStream.close();
		           
		                                     
		           socket.close();    
		       
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }
	    
	    ServerSocket ssreg;
		 Socket socreg;
		// ObjectOutputStream oosreg;
		 ObjectInputStream oosreg;
		 
	    public void Receive(){
			   new Thread(){
			        public void run()
			    	{
			        	
			        	while(true){
			        		
			        	
			    		try
			    		{        
			    		                                   
			    		  ssreg=new ServerSocket(6666);
			    	      socreg=ssreg.accept();
			    	      oosreg=new ObjectInputStream(socreg.getInputStream());
			    	      userinfo = (String) oosreg.readObject();
			    	      runOnUiThread(new Runnable() {   
							   
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "Data received:: "+userinfo, 600000).show();
							}
						});
			    	                              
			    	      ssreg.close();
			    	      socreg.close();
			    	      oosreg.close();
			    		}catch(Exception e){        
			    				System.out.println("EEEEEEEEEEEEE"+e.getMessage());
			    				e.printStackTrace();
			    		}   
			    	}                       
			    	                                
			    		}   
			        }.start();
			   
		                    
		
		 }    
		     
		 ServerSocket sslogin;
		 Socket soclogin;
		 ObjectOutputStream ooslogin;
		 ObjectInputStream oinlogin;
		 
		 public void Userlogin(){
			   new Thread(){
			        public void run()
			    	{   
			        	
			        	
			        	while(true){
			        		   
			        	   
			    		try
			    		{             
			    		      
			    		 sslogin=new ServerSocket(5555);
			    		 soclogin=sslogin.accept();
			    	      oinlogin=new ObjectInputStream(soclogin.getInputStream());
			    	      ooslogin=new ObjectOutputStream(soclogin.getOutputStream());
			    	      String userinfo = (String) oinlogin.readObject();
			    	      
			    	      if(userinfo.contains(RegisterActivity.userinfo)){
			    	    	  ooslogin.writeObject("success");
			    	      }else{
			    	    	  ooslogin.writeObject("fail");
			    	      }
			    	      ooslogin.close();
			    	      oinlogin.close();
				         soclogin.close();
				    	 sslogin.close();
			    		}catch(Exception e){        
			    				System.out.println("EEEEEEEEEEEEE"+e.getMessage());
			    				e.printStackTrace();
			    		} 
			    		       
			        	}
			    	                        
			    		}   
			        }.start();
			   
		                    
		
		 }
		 
   }
