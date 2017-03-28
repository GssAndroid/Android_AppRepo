package com.globalsoft.ServicePro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class ImageDislayActivity extends Activity {
	private ImageView cap_img;
	private int selImage;
	String photoPath, objId;
	byte[] b1;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.imagedisplay);
	    try{
		    cap_img = (ImageView) findViewById(R.id.cap_img);
		    BitmapFactory.Options options = new BitmapFactory.Options(); 
		    options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
		    selImage = this.getIntent().getIntExtra("image_display", -1);
    		objId = this.getIntent().getStringExtra("objectId");
            boolean confirmation_scr = this.getIntent().getBooleanExtra("confirmation_scr", false);
            if(confirmation_scr){
            	if(selImage != -1){
    		    	if(selImage == 1){
    		    		photoPath = ServiceProConstants.getCapturedSmallImagePathConfirScr(objId);
    		    	}
    		    	else if(selImage == 2){
    		    		photoPath = ServiceProConstants.getSignatureSmallImagePathConfirScr(objId);
    		    	}
    		    }
            }else{
            	if(selImage != -1){
    		    	if(selImage == 1){
    		    		photoPath = ServiceProConstants.getCapturedSmallImagePath(objId);
    		    	}
    		    	else if(selImage == 2){
    		    		photoPath = ServiceProConstants.getSignatureSmallImagePath(objId);
    		    	}
    		    }
            }
            ServiceProConstants.showLog("photoPath:"+photoPath);
	    	Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options); 
		    cap_img.setImageBitmap(bitmap);
		}
	    catch(Exception sfg){
	    	ServiceProConstants.showErrorLog("Error in image preview : "+sfg.toString());
	    }
	    
	}
}
