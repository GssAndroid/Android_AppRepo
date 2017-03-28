package com.globalsoft.ServicePro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class ImageCapActivity extends Activity implements SurfaceHolder.Callback{
	
	private Camera camera;
    private boolean isPreviewRunning = false;
    private String filename;
    FileOutputStream fos = null;
	private boolean isBackKey = false;
	ImageCaptureCallback camDemo = null;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.image_capture);
        surfaceView = (SurfaceView)findViewById(R.id.surface);
        surfaceView.setOnTouchListener(touchListener);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }    
   
    Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {
        	startMediaScanner();
        }
    };    

    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
    	public void onShutter() {
    	}
    };
    
    private OnTouchListener touchListener = new OnTouchListener()
    {    	
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {		
				imageCaptureAction();
           } 
			return false;
		} 
    };
    
    public void imageCaptureAction(){
    	try {
			//Toast.makeText(ImageCapActivity.this, "Before Storing", 2000).show();
			Log.e("Alert","Before Storing");
			Log.e("isSdPresent","isSdPresent"+isSdPresent());
			if(isSdPresent()){
				filename = Environment.getExternalStorageDirectory() + File.separator + "Imagecap.jpg";
			}else{
				filename = "Imagecap.jpg";
			}
    		
    		File file = new File(filename); 
        	if (file.exists()) {     
        		file.delete();
        	} 
        	else{
        		Log.e("Alert","file is not there");
        	}
    		fos = new FileOutputStream(filename);    		
    		camDemo = new ImageCaptureCallback(fos);  
    		
    		//Toast.makeText(ImageCapActivity.this, "After Storing", 2000).show();
    		Log.e("Alert","After Storing");
    	} 
    	catch(Exception ex ){
    		Log.e("Alert",ex.toString());
		}		    	
    	camera.takePicture(mShutterCallback, mPictureCallbackRaw, camDemo);
    }

    public static boolean isSdPresent() {     
    	return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); 
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		isBackKey = true;
        	onClose();
        	return true;
        }
        return false;
    }

    protected void onResume()
    {
        Log.e(getClass().getSimpleName(), "onResume");
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    protected void onStop()
    {
    	super.onStop();
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
        if (isPreviewRunning) {
            camera.stopPreview();
        }
        Camera.Parameters params = camera.getParameters();  
  		params.setPictureFormat(PixelFormat.JPEG);    	 
  		
  		camera.setParameters(params);
        try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        camera.startPreview();
        isPreviewRunning = true;
    }
    
    public void surfaceCreated(SurfaceHolder holder)
    {
    	camera = Camera.open();
    	List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes(); 
    	for (int i=0; i < sizes.size(); i++){
        String strSize = String.valueOf(i) + " : " 
           + String.valueOf(sizes.get(i).height)
           + " x "
           + String.valueOf(sizes.get(i).width);
        	Log.e("strSize:",strSize);
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
    	releaseCamera();
    }
    
    private void startMediaScanner(){
    	String path = filename;//targetResource.getPath();
    	//Toast.makeText(ImageCapActivity.this, path, 2000).show();
    	onClose();
    }
    
    private void releaseCamera(){
    	try {
			if(camera != null){
				camera.stopPreview();
			    isPreviewRunning = false;
			    camera.release();
			    camera = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void onClose(){
    	try{
    		releaseCamera();
		    surfaceView = null;
	        surfaceHolder = null;
	        if(isBackKey == false){
	    		Intent i = new Intent(); 
		    	i.putExtra("imagepath",filename); 
		    	setResult(RESULT_OK, i);
	        }
	        else{
	        	Intent i = new Intent(); 
		    	i.putExtra("imagepath",""); 
		    	setResult(RESULT_OK, i);
	        }
    		System.gc();
			finish();
		}
		catch(Exception asd){
			Log.e("Exception", asd.toString());
		}
    }
}