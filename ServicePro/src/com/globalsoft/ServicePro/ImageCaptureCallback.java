package com.globalsoft.ServicePro;

import java.io.OutputStream;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;

public class ImageCaptureCallback implements PictureCallback  {

	private OutputStream filoutputStream;
	
	public ImageCaptureCallback(OutputStream filoutputStream) {
		this.filoutputStream = filoutputStream;
	}
	
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
			filoutputStream.write(data);
			filoutputStream.flush();
			filoutputStream.close();
			Log.e("Alert","File was stored");
		} catch(Exception ex) {
			ex.printStackTrace();
			Log.e("Alert","Error in file storing:"+ex.toString());
		}
	}
}