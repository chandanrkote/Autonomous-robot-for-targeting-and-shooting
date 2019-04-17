package com.example.disastermanagementsender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class OneShotPreviewActivity extends Activity {
    /** Called when the activity is first created. */
    private Camera _camera;
    private Activity _activity;
    byte[] mydata;
    int mPreviewImageFormat=ImageFormat.NV21;
    Intent intent;
    String ip ;   
    static ArrayList<String> list1;
    Socket s1,s2,s3;  
    ObjectOutputStream o1,o2,o3;  
    	     
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	_activity = this;
        super.onCreate(savedInstanceState);
        
        
        	
        list1 = new ArrayList<String>();
//        intent = getIntent();
//        ip = intent.getStringExtra("ip");
        list1 = (ArrayList<String>) getIntent().getSerializableExtra("key");
        
        try {   
        	          
        	if(list1.size()==3){
        		s1 =  new Socket(list1.get(0),1234);
    			s2 =  new Socket(list1.get(1),1234);
    			s3 =  new Socket(list1.get(2),1234);     
    			o1 = new ObjectOutputStream(s1.getOutputStream());
    			o2 = new ObjectOutputStream(s2.getOutputStream());
    			o3 = new ObjectOutputStream(s3.getOutputStream());
    			             
        	}else if(list1.size()==2){
        		s1 =  new Socket(list1.get(0),1234);
    			s2 =  new Socket(list1.get(1),1234);  
    			o1 = new ObjectOutputStream(s1.getOutputStream());   
    			o2 = new ObjectOutputStream(s2.getOutputStream());
    			         
    			          
        	}else if(list1.size()==1){       
        		
        		new Thread(){
        			@Override   
        			public void run(){
        				try {
							s1 =  new Socket(list1.get(0),1234);
							o1 = new ObjectOutputStream(s1.getOutputStream());
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
			
			       
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DisplayToast(""+e.getMessage());
		} catch (IOException e) {    
			// TODO Auto-generated catch block
			e.printStackTrace();
			DisplayToast(""+e.getMessage());
		}
           
        
                 
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new CameraPreview(this));
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
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	   
    	if(_camera!=null){
    		
    		_camera.stopPreview();
            _camera.release();
            _camera=null;
    	}

    	       try {
    	    	   if(s1!=null)
				s1.close();       
    	    	   if(s2!=null)           
				s2.close(); 
    	    	   if(s3!=null)
	    	     s3.close();    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	       
    }
                 
    private Camera.PreviewCallback mPreviewListener = new Camera.PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
		        
			
			
			Camera.Parameters parameters = _camera.getParameters(); 
	        Size size = parameters.getPreviewSize(); 
	        
	        YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),    
	                size.width, size.height, null); 
	                           
	        ByteArrayOutputStream bos=new ByteArrayOutputStream();       
	               
	        image.compressToJpeg( 
	                new Rect(0, 0, image.getWidth(), image.getHeight()), 90, 
	                bos); 
	        final byte[] myimage=bos.toByteArray();
	                 
	            
	        if(s1==null){      
	        	                         
	        }else{      
	        	new Thread(){
	        		@Override
	        		public void run(){
	        			
	        			ObjectOutputStream outputStream;        
	        			     
						try {
							           
							
							o1.writeObject(myimage);            
							o1.flush();                  
							                                       
		  	 		                             
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	  	 		          
	  	 				  
	        		}
	        	}.start();
	        }
	        if(s2==null){
	        	
	        }else{
	        	new Thread(){
	        		@Override
	        		public void run(){
	        			
	        			ObjectOutputStream outputStream;
						try {
							//outputStream = new ObjectOutputStream(s1.getOutputStream());
							//ObjectOutputStream o2 = new ObjectOutputStream(s2.getOutputStream());
			    			    
							o2.writeObject(myimage);
							o2.flush();
							         
		  	 		                     
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	  	 		          
	  	 				  
	        		}
	        	}.start();
	        }
	        if(s3==null){
	        	      
	        }else{
	        	new Thread(){
	        		@Override 
	        		public void run(){
	        			
	        			ObjectOutputStream outputStream;
						try {
							//outputStream = new ObjectOutputStream(s1.getOutputStream());
							//ObjectOutputStream o3 = new ObjectOutputStream(s3.getOutputStream());
							o3.writeObject(myimage);
							o3.flush();
						//	o3.close();         
		  	 		       
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	  	 		                     
	  	 				              
	        		}       
	        	}.start();
	        }
	        
	        takepicture();
			
			                   
	}       
    
    };
    
	public void takepicture(){
		_camera.setOneShotPreviewCallback(mPreviewListener);
	}
	
    @Override    
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		_camera.setOneShotPreviewCallback(mPreviewListener);
    	}
    	
		return super.onTouchEvent(event); 
	}
    
    public void setCameraDisplayOrientation(android.hardware.Camera camera, int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
             
        android.hardware.Camera.getCameraInfo(cameraId, info);
               
        int rotation = _activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        
		Log.d("camera", "result= " + result);
		camera.setDisplayOrientation(result);
    }
    
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder holder;
     
        CameraPreview(Context context) {
            super(context);
            holder = getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
     
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    		configure( format,  width, height) ;
        }
        
        public void surfaceDestroyed(SurfaceHolder holder) {
        	if(_camera!=null){
        		
        		_camera.stopPreview();
                _camera.release();
                _camera=null;
        	}
            
        }

		public void surfaceCreated(SurfaceHolder holder) {
			_camera = Camera.open();
			try {
				_camera.setPreviewDisplay(holder);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setCameraDisplayOrientation(_camera, 0);
			_camera.startPreview();

			
		}
		
		// not necessary
		protected void setPictureFormat(int format) {
			Camera.Parameters params = _camera.getParameters();
			List<Integer> supported = params.getSupportedPictureFormats();
			if (supported != null) {
				for (int f : supported) {
					if (f == format) {
						params.setPreviewFormat(format);
						_camera.setParameters(params);
						break;
					}
				}
			}
		}
		
		// not necessary
		protected void setPreviewSize(int width, int height) {
			Camera.Parameters params = _camera.getParameters();
			List<Camera.Size> supported = params.getSupportedPreviewSizes();
			if (supported != null) {
				for (Camera.Size size : supported) {
					if (size.width <= width && size.height <= height) {
						params.setPreviewSize(size.width, size.height);
						_camera.setParameters(params);
						break;
					}
				}
			}
		}
		// not necessary
		public void configure(int format, int width, int height) {
			_camera.stopPreview();
			setPictureFormat(format);
			setPreviewSize(width, height);
			_camera.startPreview();
		}
    }    
    
  
       
}