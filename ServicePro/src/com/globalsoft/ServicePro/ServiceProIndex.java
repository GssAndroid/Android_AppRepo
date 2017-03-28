package com.globalsoft.ServicePro;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

public class ServiceProIndex  extends Activity {
protected int _splashTime = 2000; 
	
	private Thread splashTread;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    ServiceProConstants.TITLE_DISPLAY_WIDTH = ServiceProConstants.getDisplayWidth(this);
	    /*requestWindowFeature(Window.FEATURE_LEFT_ICON);
	    ServiceProConstants.setWindowTitleTheme(this);
	    setContentView(R.layout.splash);	    
	    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
	    ServiceProConstants.setWindowTitleTheme(this);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.splash); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.app_name));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
	    
	    final ServiceProIndex sPlashScreen = this; 	    
	    // thread for displaying the SplashScreen
	    splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {	            	
	            	synchronized(this){
	            		wait(_splashTime);
	            	}	            	
	            } catch(InterruptedException e) {} 
	            finally {
	            	try{
	            		yield();
	            	}
	            	catch(Exception dgh){}
	                finish();	                
	                Intent i = new Intent();
	                i.setClass(sPlashScreen, ServicePro.class);
	        		startActivity(i);	                
	            }
	        }
	    };	    
	    splashTread.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	try{
		    	synchronized(splashTread){
		    		splashTread.notifyAll();
		    	}
	    	}
        	catch(Exception dgh){}
	    }
	    return true;
	}
}