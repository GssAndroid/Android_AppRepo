package com.globalsoft.ServicePro;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class ServiceProShowMap extends MapActivity {
	private MapView mpview;
	private MyLocationOverlay myLocOverlay;
	private MapController mc;
	private GeoPoint p,present_location;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String mapAddress;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try{
	        mapAddress = this.getIntent().getStringExtra("mapAddress");
	        setContentView(R.layout.mapview);
	        mpview = (MapView)findViewById(R.id.mapView);
	        
			mpview.setBuiltInZoomControls(true);
			mpview.displayZoomControls(true);
			mc = mpview.getController();
			
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 13,14, new GeoUpdateHandler());
	      
			Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());    
	        try { 
	        	//String s = "New York, NY, Perkin Elmer, 3rd  Floor 140 Sylvan Ave, 24510, US";
	        	//String s = "Vijayanagar 2nd Stage, Mysore, Karnataka 570 017,india";        	
	        	String addrs = mapAddress;        	
	        	ServiceProConstants.showLog("Map Address:"+addrs);//city,region,postal code,country
	        	Geocoder coder = new Geocoder(this); 
				List<Address> address;   
				address = coder.getFromLocationName(addrs,5);             
				if (address.size() == 0) {                 
					 ServiceProConstants.showLog("address null"); 
					 ServiceProConstants.showErrorDialog(this, "Can't find this location for this address!"); 
				}
				else{
					Address location = address.get(0);             
					 
					/*ServiceProConstants.showLog("getLatitude3:"+location.getLatitude());
	            	ServiceProConstants.showLog("getLongitude3"+location.getLongitude());*/
	            	String lats = String.valueOf(location.getLatitude());
	            	String lons = String.valueOf(location.getLongitude());
	            	String coordinates[] = {lats, lons};
	                double lat = Double.parseDouble(coordinates[0]);
	                double lng = Double.parseDouble(coordinates[1]);
	         
	                p = new GeoPoint(
	                    (int) (lat * 1E6), 
	                    (int) (lng * 1E6));
	                mc.animateTo(p);
	            	mc.setCenter(p);
	                mc.setZoom(15);
				}                          	
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
	        //---Add a location marker---
	        MapOverlay mapOverlay = new MapOverlay();
	        List<Overlay> listOfOverlays = mpview.getOverlays();
	        listOfOverlays.clear();
	        listOfOverlays.add(mapOverlay); 
	        mpview.invalidate();
	    }
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in oncreate : "+de.toString());
	    }
    }
    
    public class GeoUpdateHandler implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            int lat = (int) (location.getLatitude() * 1E6);
            int lng = (int) (location.getLongitude() * 1E6);
            present_location = new GeoPoint(lat, lng);
            mc.animateTo(present_location); //  mapController.setCenter(point);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
    
    class MapOverlay extends com.google.android.maps.Overlay
    {        
        public boolean draw(Canvas canvas, MapView mapView,boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
            Paint paint = new Paint();
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.map_icon);            
            canvas.drawBitmap(bmp, screenPts.x-20, screenPts.y-36, paint);      
            //canvas.drawText("Restaurant ..", screenPts.x, screenPts.y, paint);
            return true;
        }
    } 

    protected boolean isRouteDisplayed() {
    		 return false;
    }
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {	
		    finish();
		    return true;
		}
		return super.onKeyDown(keyCode, event);
    }
}
