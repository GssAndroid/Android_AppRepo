package com.globalsoft.ServicePro;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.globalsoft.ServicePro.GraphicsFile.ColorPickerDialog;

public class FingerPaint extends GraphicsActivity
        implements ColorPickerDialog.OnColorChangedListener {    
	private Bitmap  mBitmap;
	private AlertDialog alertDialog;
	private String signPath = "", objId = "", path = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_LEFT_ICON);
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);
        objId = this.getIntent().getStringExtra("objectId");
        boolean confirmation_scr = this.getIntent().getBooleanExtra("confirmation_scr", false);
        if(confirmation_scr){
            path = ServiceProConstants.getSignatureBigmagePathForConfirScr(objId);
            signPath = ServiceProConstants.getSignatureSmallImagePathConfirScr(objId);        	
        }else{
            path = ServiceProConstants.getSignatureBigmagePath(objId);
            signPath = ServiceProConstants.getSignatureSmallImagePath(objId);        	
        }
        drawPointValue();        
    }
    
    private void drawPointValue(){
    	try{
    		mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFFCCCCCC);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(5);            
            mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                           0.4f, 6, 3.5f);
            mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    	}
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in refreshActivity : "+asf.toString());
	    }
    }//fn drawPointValue
    
    private Paint       mPaint;
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;
    
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    public class MyView extends View {        
        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        
        public MyView(Context c) {
            super(c);            
            Display display = getWindowManager().getDefaultDisplay(); 
            int width = display.getWidth();  
            int height = display.getHeight();              
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);            
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);            
            canvas.drawPath(mPath, mPaint);
        }
        
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        
        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
    
    //private static final int COLOR_MENU_ID = Menu.FIRST;
    //private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    //private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    //private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;
    private static final int DONE_MENU_ID = Menu.FIRST + 5;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        //menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        //menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        //menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        menu.add(0, DONE_MENU_ID, 0, "Done").setShortcut('6', 'd');
        //menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');

        /****   Is this the mechanism to extend with filter effects?
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(
                              Menu.ALTERNATIVE, 0,
                              new ComponentName(this, NotesList.class),
                              null, intent, 0, null);
        *****/
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
        /*case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;*/
            case ERASE_MENU_ID:
                /*mPaint.setXfermode(new PorterDuffXfermode(
                                                        PorterDuff.Mode.CLEAR));*/
                refreshActivity();
                return true;
            /*case SRCATOP_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                    PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;*/
            case DONE_MENU_ID:
            	imageSaveAction();
                return true;
                
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void refreshActivity(){
    	try{
	    	startActivity(getIntent()); 
	    	this.finish(); 
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in refreshActivity : "+asf.toString());
	    }
    }//fn refreshActivity
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			displayDialog();			
		}
		return super.onKeyDown(keyCode, event);
	}    
    
    private void displayDialog(){
		try{
			alertDialog = new AlertDialog.Builder(this)
			.setMessage("Signature Confirmation")
			.setPositiveButton("Save", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					imageSaveAction();
				}
				})
			.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
				})
				.create();
				alertDialog.show();
		}
		catch(Exception asfg){
			ServiceProConstants.showErrorLog("Error in displaySaveDialog : "+asfg.toString());
		}
	}//fn displayDialog
    
    private void imageSaveAction(){
		try{
			FileOutputStream fo = null;
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes); 
			File f = new File(path);
		    try {
				f.createNewFile();
				fo = new FileOutputStream(f); 
			    fo.write(bytes.toByteArray());
			    fo.close();	
			    imageResize(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					fo.close();
				} catch (IOException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}						
			} 		    	
			finish();
		}
		catch(Exception asfg){
			ServiceProConstants.showErrorLog("Error in displaySaveDialog : "+asfg.toString());
		}
	}//fn displayDialog    	
    
    private void imageResize(String filepathStr){
    	ServiceProConstants.showLog("imagepath: "+filepathStr);
  		FileInputStream in = null; 
		BufferedInputStream buf = null; 
		FileOutputStream fo = null;
		try { 
		    in = new FileInputStream(filepathStr); 
		    buf = new BufferedInputStream(in); 
		    Bitmap _bitmapPreScale = BitmapFactory.decodeStream(buf); 
		    int oldWidth = _bitmapPreScale.getWidth(); 
		    int oldHeight = _bitmapPreScale.getHeight(); 
		    Log.e("oldWidth:",""+oldWidth);
		    Log.e("oldHeight:",""+oldHeight);
		    
		    int newWidth = 100; 
		    int newHeight = 100; 
		
		    float scaleWidth = ((float) newWidth) / oldWidth; 
		    float scaleHeight = ((float) newHeight) / oldHeight; 
		
		    Matrix matrix = new Matrix(); 
		    // resize the bit map 
		    matrix.postScale(scaleWidth, scaleHeight); 
		    Bitmap _bitmapScaled = Bitmap.createBitmap(_bitmapPreScale, 0, 0, oldWidth, oldHeight, matrix, true);
		    
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
		    _bitmapScaled.compress(Bitmap.CompressFormat.JPEG, 40, bytes); 
		     
		    //you can create a new file name "test.jpg" in sdcard folder. 
		    File f = new File(signPath);
		    Log.e(" ", signPath);
		    f.createNewFile(); 
		    fo = new FileOutputStream(f); 
		    fo.write(bytes.toByteArray());
		    buf.close();			    
		    in.close();	
		    fo.close();				    
		    File file = new File(filepathStr); 
        	if (file.exists()) {     
        		file.delete();
        	} 
        	else{
        		Log.e("Alert","file is not there");
        	}
		} 
		catch(Exception ex ){
			Log.e("resize:",ex.toString());
			try {
				buf.close();
				in.close();
				fo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
    }
}