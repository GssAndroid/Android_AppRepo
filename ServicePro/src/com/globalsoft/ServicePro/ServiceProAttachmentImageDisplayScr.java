package com.globalsoft.ServicePro;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceProAttachmentImageDisplayScr extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{			
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.custom_fullimage_dialog); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SAPTASK_EDITSCR_TITLE));
			
			String imageData = this.getIntent().getStringExtra("ss");
			ServiceProConstants.showLog("length : " +imageData.length());
			ImageView image = (ImageView) findViewById(R.id.fullimage);
			/*		String imageData = "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" +
					"0J8ULMBt4ePx5Mym5ZpRdfBCDds";
			ServiceProConstants.showLog("imageData : "+imageData);
			byte[] imageAsBytes = imageData.getBytes();//(imageData.getBytes());			
			Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
			byte[] imageAsBytes = Base64.decode(imageData.getBytes("UTF-8"));
		    image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));*/
			
			/*String signPath = ServiceProConstants.getCapturedSmallImagePath("8000000275");
		    Bitmap bm = BitmapFactory.decodeFile(signPath);
		  	ByteArrayOutputStream baos = new ByteArrayOutputStream();   
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object    
			byte[] b = baos.toByteArray();
			String encodedSignImageStr = Base64.encodeBytes(b);*/
			
			
	//		String encodedSignImageStr = "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";
			//ServiceProConstants.showLog("ServiceProAttachmentImageDisplayScr : "+imageData);
			
			/*byte[] data = imageData.getBytes(); 
			Bitmap bm=null; 
			ByteArrayInputStream bytes = new ByteArrayInputStream(data); 
			BitmapDrawable bmd = new BitmapDrawable(bytes); 
			bm = bmd.getBitmap(); 

		    if(bm != null){	
				image.setImageBitmap(bm);
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
		    
			/*byte[] decodedString = Base64.decode(imageData);
			ByteArrayInputStream bis = new ByteArrayInputStream(decodedString);
		    Bitmap bp=BitmapFactory.decodeStream(bis); //decode stream to a bitmap image
		    image.setImageBitmap(bp);
		    
		    if(bp != null){	
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
						
			/*byte[] decodedString = Base64.decode(imageData);
		    File dir = Environment.getExternalStorageDirectory(); 
		    String filename = "sample.pdf"; 
		    File f = new File(dir, filename); 
		    if (f.exists()) {
		    	f.delete();
			    f.createNewFile(); 
		    }else{
			    f.createNewFile(); 		    	
		    }
		    OutputStream outStream = new FileOutputStream(f);
		    outStream.write(decodedString);
		    outStream.flush();
		    outStream.close();		    
		    File file = new File(Environment.getExternalStorageDirectory()+"/sample.pdf"); 
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivity(intent);
                } 
                catch (Exception e) {
                }
            }*/
            
		    //working code for gif
		    /*byte[] decodedString = Base64.decode(imageData);
		    File dir = Environment.getExternalStorageDirectory(); 
		    String filename = "sample.gif"; 
		    File f = new File(dir, filename); 
		    if (f.exists()) {
		        f.delete();
			    f.createNewFile(); 
		    }else{
			    f.createNewFile(); 
		    }
		    OutputStream outStream = new FileOutputStream(f);
		    outStream.write(decodedString);
		    outStream.flush();
		    outStream.close();		    
		    String imageFile = Environment.getExternalStorageDirectory()+"/sample.gif"; 
		    Bitmap bitmap = BitmapFactory.decodeFile(imageFile);
		    image.setImageBitmap(bitmap);*/
		     
		    /*byte[] imgBytes = Base64.decode(imageData);   
		    FileOutputStream osf = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/yourImage.png"));   
		    osf.write(imgBytes);   
		    osf.flush(); */
		    
		    /*byte[] imageAsBytes = Base64.decode(imageData);
		    image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));*/
		    
		    
		    /*byte[] imageBytes=Base64.decode(imageData);
		    ServiceProConstants.showLog("byte array length : "+imageBytes.length);
	        Bitmap bp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
	        image.setImageBitmap(bp);		    
		    if(bp != null){	
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
		    
			/*byte[] decodedString = Base64.decode(imageData);
	        ByteArrayInputStream in = new ByteArrayInputStream(decodedString);
		    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		    int b;
	        while((b = in.read()) != -1)
	            byteStream.write(b);
	        byte bitmapBytes[] = byteStream.toByteArray();
	        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
	        image.setImageBitmap(bitmap);*/
	        
			File dir = Environment.getExternalStorageDirectory(); 
		    String filename = "file2.txt"; 
		    File f = new File(dir, filename); 
		    if (f.exists()) {
		    	f.delete();
			    f.createNewFile(); 
		    }else{
			    f.createNewFile(); 		    	
		    }
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
            myOutWriter.append(imageData);
            myOutWriter.close();
            fOut.close();
            ServiceProConstants.showLog("writing finished  ");
            
	        FileInputStream fIn;
	        String aDataRow = "";
	        String aBuffer = "";
			try {
				fIn = new FileInputStream(f);		
		        BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
		        while ((aDataRow = myReader.readLine()) != null) 
		        {
		            aBuffer += aDataRow ;
		        }
		        myReader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ServiceProConstants.showLog("reading finished  ");
			ServiceProConstants.showLog("aBuffer length :  "+aBuffer.length());
	        byte[] imageBytes=Base64.decode(aBuffer, Base64.NO_WRAP);
		    image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
	        		    		    
		    /*byte[] bytes = Base64.decode(imageData);
		    File imagefile = new File(Environment.getExternalStorageDirectory()+"/sam.jpg");
		    File dir = new File(imagefile.getParent());
		    if (!dir.exists()) {
		        dir.mkdirs();
		    }
		    FileOutputStream fos = new FileOutputStream(imagefile);
		    fos.write(bytes);
		    fos.close();*/


		    /*String filePath = Environment.getExternalStorageDirectory()+"/samll.jpg";
		    File imageFile = new File(filePath);
		    FileOutputStream fos = null;
		    try {
		    	fos = new FileOutputStream(imageFile);
		    } catch (FileNotFoundException e1) {
		    	e1.printStackTrace();
		    }
		    //BASE64Decoder decoder = new BASE64Decoder();
		    byte[] decodedBytes = null;
		    try {
		    	decodedBytes = Base64.decode(imageData);//taking input string i.e the image contents in base 64
		    } catch (IOException e1) {
		    	e1.printStackTrace();
		    }
		    try {
		    	fos.write(decodedBytes);
		    	fos.flush();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    try {
		    	fos.flush();
		    	fos.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }*/
		    
			/*byte[] decodedString = Base64.decode(imageData);
		    File dir = Environment.getExternalStorageDirectory(); 
		    String filename = "sample.jpg"; 
		    File f = new File(dir, filename); 
		    f.createNewFile(); 
		    OutputStream outStream = new FileOutputStream(f);
		    outStream.write(decodedString);
		    outStream.flush();
		    outStream.close();		    
		    String imageFile = Environment.getExternalStorageDirectory()+"/sample.jpg"; 
		    Bitmap bitmap = BitmapFactory.decodeFile(imageFile);
		    image.setImageBitmap(bitmap);*/		    
	       	        
	        /*byte[] decodedString = Base64.decode(imageData);
	        ByteArrayInputStream imageStream = new ByteArrayInputStream(decodedString);
	        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
	        if(theImage != null){	
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
			/*byte[] decodedString = Base64.decode(imageData);
			Bitmap bitmapimage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			 String filepath = Environment.getExternalStorageDirectory()+"/sample.jpg";
			 File imagefile = new File(filepath);
			 FileOutputStream fos = new FileOutputStream(imagefile); 
			 bitmapimage.compress(CompressFormat.JPEG, 100, fos);
			 fos.flush();
			 fos.close();*/
			
					    

			/*byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT); //Base64.decode(imageData); //(encodedImage, Base64.DEFAULT);
			image.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));*/
					
		    /*byte[] imageAsBytes = imageData.getBytes();
		    image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));*/
		    
		    
			/*BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
			 */
            
			/*if(bm != null){	
				image.setImageBitmap(bm);
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
			/*byte[] imageAsBytes = Base64.decode(imageData1.getBytes());
			File photo=new File(Environment.getExternalStorageDirectory(), "photo.jpg");

			if (photo.exists()) {
				photo.delete();
			}

			try {
				FileOutputStream fos=new FileOutputStream(photo.getPath());

				fos.write(imageAsBytes);
				fos.close();
			}
			catch (java.io.IOException e) {
				Log.e("PictureDemo", "Exception in photoCallback", e);
			}*/

			
			/*byte[] imageAsBytes = Base64.decode(imageData.getBytes());
			Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
	        if(byteArray != null){	
	        	Bitmap bmpc = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		        image.setImageBitmap(bmpc);
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
		}
		catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
	}
	
	static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
