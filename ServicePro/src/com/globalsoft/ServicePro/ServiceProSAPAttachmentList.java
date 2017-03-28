package com.globalsoft.ServicePro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.SapQueueProcessorHelper.Utils.SapQueueProcessorHelperConstants;
import com.globalsoft.ServicePro.Constraints.SOCodeGroupOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOCodeListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOMattEmpListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOServiceActListOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProAttachmentOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProColleaguesOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Constraints.StatusOpConstraints;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Database.ServiceProOfflineContraintsCP;

public class ServiceProSAPAttachmentList extends ListActivity {

	private ServiceProAttachmentOpConstraints attachCategory = null;
	private ListView listView;
	private ProgressDialog pdialog = null;
	private SoapObject resultSoap = null;
	private boolean internetAccess = false;
	final Handler handlerForInsertNewDataCall = new Handler();
	private String taskErrorMsgStr="";
	
	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
	private SoapObject requestSoapObj = null;
	private ArrayList attachList = new ArrayList();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ServiceProConstants.setWindowTitleTheme(this);		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.attachmain); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SERVICEPRO_ATTACHTITLE));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
		
		Intent i = getIntent();
		if(i != null){
			attachList = i.getParcelableArrayListExtra("attachObj");
		}
		ServiceProConstants.showLog("attachList size: "+attachList.size()); 
		try{			
			initLayout();
		}
		catch (Exception de) {
			ServiceProConstants.showErrorLog("Error in oncreate  ServiceProSAPAttachmentList: "+de.toString());
        }
	}
	
	private void initLayout(){
		try{						
			listView = getListView();
			listView.setOnItemClickListener(listItemClickListener); 
			listviewcall();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in initLayout : "+sfg.toString());
		}
	}//fn initLayout
	
	private void listviewcall(){
		try{
			setListAdapter(new MyListAdapter(this));
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in listviewcall : "+sfg.toString());
		}
	}//fn listviewcall
    	
	OnItemClickListener listItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			try{		
				//loadPhoto();
				internetAccess = ServiceProConstants.checkInternetConn(ServiceProSAPAttachmentList.this);
				if(internetAccess)
					initSoapConnectionForAttachment(arg2);
				else{
					displayDialog();
				}	
			}
			catch (Exception dee) {
		    	ServiceProConstants.showErrorLog("Error in listItemClickListener:"+dee.toString());
		    }
		}
	};
		
	private void displayDialog(){
		try{
			ServiceProConstants.showErrorDialog(this, getResources().getString(R.string.SERVICEPRO_ERROR_MSG_OFFLINE));
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in displayDialog:"+e.getMessage());
		}
	}//fn displayDialog
		
	private void initSoapConnectionForAttachment(int pos){   
    	SoapSerializationEnvelope envelopeC = null;
    	String taskId = "", type = "", numberExt = "", attachmentId = "";
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            attachCategory = (ServiceProAttachmentOpConstraints)attachList.get(pos);
        	if(attachCategory != null){
        		taskId = attachCategory.getObjectId().toString().trim();
        		type = attachCategory.getObjectType().toString().trim();
        		numberExt = attachCategory.getNumberExt().toString().trim();
        		attachmentId = attachCategory.getAttachmentId().toString().trim();
        	}
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[5];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
                        
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]DOCUMENT-ATTACHMENT-GET[.]VERSION[.]0";
            C0[3].Cdata = "DATA-TYPE[.]ZGSXCAST_ATTCHMNTKEY01[.]OBJECT_ID[.]OBJECT_TYPE[.]NUMBER_EXT[.]ATTCHMNT_ID";
            C0[4].Cdata = "ZGSXCAST_ATTCHMNTKEY01[.]"+taskId+"[.]"+type+"[.]"+numberExt+"[.]"+attachmentId;            
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
            
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
            respType = ServiceProConstants.ATTACH_TYPE;
    		requestSoapObj = request;
            internetAccess = ServiceProConstants.networkAvailableCheck(ServiceProSAPAttachmentList.this);
            if(internetAccess){
                doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
            }          	
        }
        catch(Exception asd){
        	ServiceProConstants.showErrorLog("Error in initSoapConnectionForAttachment : "+asd.toString());
        }
    }//fn initSoapConnectionForAttachment
	
	private void doThreadNetworkAction(Context ctx, final Handler handler, final Runnable handlerFnName, final SoapSerializationEnvelope envelopeC, SoapObject request){
    	try{
    		requestSoapObj = request;
            soapTask = new StartNetworkTask(ctx);
    		Thread t = new Thread() {
	            public void run() {
        			try{
        				resultSoap = null;
        	            resultSoap = soapTask.execute(envelopeC).get();
        			} catch (Exception e) {
        				SapGenConstants.showErrorLog("Error in Handler : "+e.toString());
        			}        			
                    handler.post(handlerFnName);
				}
			};
	        t.start();
    	}
    	catch(Exception asgg){
    		ServiceProConstants.showErrorLog("Error in doThreadNetworkAction : "+asgg.toString());
    	}
    }//fn doThreadNetworkAction
       
    final Runnable getNetworkResponseRunnable = new Runnable(){
	    public void run(){
	    	try{
	    		SapGenConstants.showLog("Soap Env value : "+resultSoap);
	    		if(resultSoap != null){
	    			if(respType == ServiceProConstants.ATTACH_TYPE)
	    				updateAttachmentResponse(resultSoap);			
	    		}
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };
        
    public void updateAttachmentResponse(SoapObject soap){
    	boolean errorflag = false, resMsgErr = false;
    	ServiceProAttachmentOpConstraints attachImageCategory = null;
    	ArrayList attachImageList = new ArrayList();
    	String attachmentContent = "";
    	try{         	    	                                
        	if(soap != null){
        		ServiceProConstants.soapResponse(this, soap, false);
            	taskErrorMsgStr = ServiceProConstants.SOAP_RESP_MSG;
            	ServiceProConstants.showLog("On soap response : "+soap.toString());
            	String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg);   
            	ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
    	        	            	            
	            String delimeter = "[.]", result="", res="", docTypeStr = "";
	            SoapObject pii = null;
	            String[] resArray = new String[50];
	            int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;
	            for (int i = 0; i < soap.getPropertyCount(); i++) {                
	                pii = (SoapObject)soap.getProperty(i);
	                propsCount = pii.getPropertyCount();
	                ServiceProConstants.showLog("propsCount : "+propsCount);
	                if(propsCount > 0){
	                    for (int j = 0; j < propsCount; j++) {
	                        if(j > 1){
	                            result = pii.getProperty(j).toString();
	                            firstIndex = result.indexOf(delimeter);
	                            eqIndex = result.indexOf("=");
	                            eqIndex = eqIndex+1;
	                            firstIndex = firstIndex + 3;
	                            docTypeStr = result.substring(eqIndex, (firstIndex-3));
	                            result = result.substring(firstIndex);
	                            
	                            resC = 0;
	                            indexA = 0;
	                            indexB = result.indexOf(delimeter);
	                            while (indexB != -1) {
	                                res = result.substring(indexA, indexB);
	                                resArray[resC] = res;
	                                indexA = indexB + delimeter.length();
	                                indexB = result.indexOf(delimeter, indexA);
	                                resC++;
	                            }
	                            int endIndex = result.lastIndexOf(';');
	                            resArray[resC] = result.substring(indexA,endIndex);
	                            if(docTypeStr.equalsIgnoreCase("ZGSXCAST_ATTCHMNT01")){
	                                if(attachImageCategory != null)
	                                	attachImageCategory = null;
	                                    
	                                attachImageCategory = new ServiceProAttachmentOpConstraints(resArray);
	                                if(attachImageCategory != null)
	                                	attachImageList.add(attachImageCategory);
	                            }
	                        }
	                        else if(j == 0){
	                            String errorMsg = pii.getProperty(j).toString();
	                            int errorFstIndex = errorMsg.indexOf("Message=");
	                            if(errorFstIndex > 0){
	                                int errorLstIndex = errorMsg.indexOf(";", errorFstIndex);
	                                String taskErrorMsgStr = errorMsg.substring((errorFstIndex+"Message=".length()), errorLstIndex);
	                                ServiceProConstants.showLog(taskErrorMsgStr);
	                                ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
	                            }
	                        }
	                    }
	                }
	            }//for
        	}else{
        		errorflag = true;
        	}
    	}
        catch(Exception sff){
        	ServiceProConstants.showErrorLog("On updateAttachmentResponse : "+sff.toString());
        }    
    	finally{
    		try{
	    		if(!errorflag){
	    			if(attachImageList.size() > 0){
	    				attachImageCategory = (ServiceProAttachmentOpConstraints)attachImageList.get(0);
	                	if(attachImageCategory != null){
	                		attachmentContent = attachImageCategory.getAttachmentContent().toString().trim();
	                		final String ss = attachmentContent;
	                		if(attachmentContent != null && attachmentContent.length() > 0){
	                			ServiceProSAPAttachmentList.this.runOnUiThread(new Runnable() {
	                                public void run() {
	                                	//loadPhoto(ss);	  	                                	
	                                	Intent intent = new Intent(ServiceProSAPAttachmentList.this, ServiceProAttachmentImageDisplayScr.class);	                			    	
	                			    	intent.putExtra("ss", ss.toString().trim());
	                					startActivityForResult(intent, ServiceProConstants.CAPTURED_IMAGE_DIS_SCREEN);
	                                }
	                            });
	                		}
	                	}
	    			}
	    		}
	    	}
	        catch(Exception sff){
	        	ServiceProConstants.showErrorLog("On updateAttachmentResponse finally : "+sff.toString());
	        }    
    	}
    }//fn updateAttachmentResponse
        
    private void loadPhoto(String imageData) {
        try {
			AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

			View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
			        (ViewGroup) findViewById(R.id.layout_root));
			ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
			ServiceProConstants.showLog("imageData : "+imageData);
			
			/*byte[] decodedString = java.util.prefs.Base64.decode(imageData.t);
			Bitmap bmp=BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
			image.setImageBitmap(bmp); */  
			
			/*byte[] data = Base64.decode(imageData);
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			image.setImageBitmap(bmp);*/
			
			//image.setBackgroundResource(R.drawable.attach);
			/*byte[] bb = (imageData).getBytes(); 
			byte[] decodedIcon = Base64.decode(bb);
			Bitmap bmp = BitmapFactory.decodeByteArray(decodedIcon, 0, decodedIcon.length);*/

			
			/*//imgCapPath = ServiceProConstants.getCapturedSmallImagePath(objId);
			String signPath = ServiceProConstants.getCapturedSmallImagePath("8000000245");
			
			Bitmap bm = BitmapFactory.decodeFile(signPath);
		  	ByteArrayOutputStream baos = new ByteArrayOutputStream();   
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object    
			byte[] b = baos.toByteArray();
			String encodedSignImageStr = Base64.encodeBytes(b);
			
			byte[] imageAsBytes = Base64.decode(encodedSignImageStr.getBytes());
		    image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));*/
			
			byte[] imageAsBytes = imageData.getBytes();//(imageData.getBytes());
			
			Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
	        DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);

	        image.setMinimumHeight(dm.heightPixels);
	        image.setMinimumWidth(dm.widthPixels);
	        ServiceProConstants.showLog("bmp height: "+dm.heightPixels);
	        ServiceProConstants.showLog("bmp width: "+dm.widthPixels);
	        if(bm != null){	
		        image.setImageBitmap(bm);
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}
		    /*if(imageAsBytes != null){	
		    	image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
		    
			/*byte[] decodedByte = Base64.decode(imageData.getBytes(), 0);
			Bitmap bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);		    
			if(bmp != null){
				image.setImageBitmap(bmp);				
				ServiceProConstants.showLog("bmp is not null ");
			}else{
				ServiceProConstants.showLog("bmp is null ");
			}*/
			
			imageDialog.setView(layout);
			imageDialog.setTitle(getString(R.string.SERVICEPRO_VIEWATTACHTITLE));
			imageDialog.setPositiveButton(this.getString(R.string.button_ok), new DialogInterface.OnClickListener(){

			    public void onClick(DialogInterface dialog, int which) {
			        dialog.dismiss();
			    }

			});
			imageDialog.create();
			imageDialog.show();
		} catch (Exception fff) {
			ServiceProConstants.showErrorLog("On loadPhoto finally : "+fff.toString());
		}     
    }//fn loadPhoto
	    
    public class MyListAdapter extends BaseAdapter {  
    	private LayoutInflater mInflater;
    	String attachid="", taskid="", numberext="";   
        public MyListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }
        public int getCount() {
        	try {
				if(attachList != null)
					return attachList.size();
			}
        	catch (Exception e) {
        		ServiceProConstants.showErrorLog(e.getMessage());
			}
        	return 0;
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.attachmain_list, null);
                holder = new ViewHolder();
                holder.attachmentId = (TextView) convertView.findViewById(R.id.attachmentId);
                holder.taskDetails = (TextView) convertView.findViewById(R.id.taskDetails);
                holder.llitembg = (LinearLayout) convertView.findViewById(R.id.llitembg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }              
            
            if(position%2 == 0)
            	holder.llitembg.setBackgroundResource(R.color.item_even_color);
            else
            	holder.llitembg.setBackgroundResource(R.color.item_odd_color);
            
            if(attachList != null){
            	attachCategory = (ServiceProAttachmentOpConstraints)attachList.get(position);
            	if(attachCategory != null){
            		attachid = attachCategory.getAttachmentId().toString().trim();
            		taskid = attachCategory.getObjectId().toString().trim();
            		numberext = attachCategory.getNumberExt().toString().trim();
            	}
            }
            holder.attachmentId.setText(attachid); 
            
            if(numberext != null && numberext.length() > 0){
            	String data = taskid+", "+numberext;
            	holder.taskDetails.setText(data); 
            }else{
            	holder.taskDetails.setText(taskid); 
            }
                       
            return convertView;
        }
        class ViewHolder {
            TextView attachmentId;
            TextView taskDetails;
            LinearLayout llitembg;
        }        
        public void removeAllTasks() {
            notifyDataSetChanged();
        }        
    }//End of MyListAdapter
}