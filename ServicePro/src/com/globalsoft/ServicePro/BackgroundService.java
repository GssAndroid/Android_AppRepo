package com.globalsoft.ServicePro;

import org.ksoap2.serialization.SoapObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.globalsoft.SapQueueProcessorHelper.Utils.SapQueueProcessorHelperConstants;

public class BackgroundService extends Service {	
	private ServiceProErrorMessagePersistent errorDbObj = null;
	private String tracId="", errType="", errorDesc="", apiName="";
	private int colIdVal = -1;
	int count = 0;
	int i = 0;
	private NotificationManager mNotificationManager;
	private Notification notification;
	private PendingIntent contentIntent;
	private Context context;
	private int dispwidth = 300;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		ServiceProConstants.showLog("onCreate");
		dispwidth = ServiceProConstants.getDisplayWidth(this);
	}

	@Override
	public void onDestroy() {
		ServiceProConstants.showLog("onDestroy");
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		ServiceProConstants.showLog("onStart");
		try{
			mNotificationManager = ServiceProConstants.notificationAlert(this.getApplicationContext());
    		//mNotificationManager.cancelAll();
			context = getApplicationContext();
    		int icon = R.drawable.notify;
    		CharSequence tickerText = "Hello";
    		long when = System.currentTimeMillis();
    		//ServiceProConstants.showLog("when:"+when);
    		notification = new Notification(icon, tickerText, when);
    		
			int colId = intent.getIntExtra(ServiceProConstants.QUEUE_COLID, -1);
			String applRefId = intent.getStringExtra(ServiceProConstants.QUEUE_APPREFID);
			String applName = intent.getStringExtra(ServiceProConstants.QUEUE_APPLNAME);
			String soapAPIName = intent.getStringExtra(ServiceProConstants.QUEUE_SOAPAPINAME);
			byte[] soapBytes = intent.getByteArrayExtra(ServiceProConstants.QUEUE_RESULTSOAPOBJ);
			SoapObject soapObj = SapQueueProcessorHelperConstants.getDeSerializableSoapObject(soapBytes);
			colIdVal = colId;
			tracId =  applRefId;
			apiName = soapAPIName;
			ServiceProConstants.showLog("colId:"+colIdVal);
			ServiceProConstants.showLog("applRefId:"+applRefId);
			ServiceProConstants.showLog("applName:"+applName);
			ServiceProConstants.showLog("soapAPIName:"+soapAPIName);
			ServiceProConstants.showLog("soapObj:"+soapObj.toString());
			getSoapResponse(soapObj);
			//Toast.makeText(this, "Application Name:"+applName+"\n SOAPAPI Name:"+soapAPIName+"\n ResultSoapObj:"+soapObj.toString()+"\n", Toast.LENGTH_LONG).show();
		}
		catch(Exception sff){
        	ServiceProConstants.showErrorLog("Error in BgProcess : "+sff.toString());
        }		
	}
	
	public void getSoapResponse(SoapObject soap){
		String taskErrorMsgStr="";
		boolean resMsgErr = false;
        if(soap != null){
            try{
            	ServiceProConstants.soapResponse(this, soap, true);
            	taskErrorMsgStr = ServiceProConstants.SOAP_RESP_MSG;
            	errorDesc = taskErrorMsgStr;
            	errType = ServiceProConstants.SOAP_ERR_TYPE;
            	String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg); 
            }
            catch(Exception sff){
            	ServiceProConstants.showErrorLog("On gettingSoapResponse : "+sff.toString());
            }           
            try{
    			ServiceProConstants.showLog("resMsgErr:"+resMsgErr);
            	if(!resMsgErr){
            		try{
            			if(apiName.equals(ServiceProConstants.STATUS_UPDATE_API)){
	            			if(colIdVal > 0){
	            				SapQueueProcessorHelperConstants.updateSelectedRowStatusForServicePro(this, colIdVal, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_COMPLETED, ServiceProConstants.STATUS_UPDATE_API);
	            			}else{
	            				SapQueueProcessorHelperConstants.updateSelectedRowStatusForServicePro(this, 0, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_COMPLETED, ServiceProConstants.STATUS_UPDATE_API);
	            			}
            			}else if(apiName.equals(ServiceProConstants.TASK_TRANS_API)){
            				if(colIdVal > 0){
            					SapQueueProcessorHelperConstants.updateSelectedRowStatusForServicePro(this, colIdVal, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_COMPLETED, ServiceProConstants.TASK_TRANS_API);
	            			}else{
	            				SapQueueProcessorHelperConstants.updateSelectedRowStatusForServicePro(this, 0, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_COMPLETED, ServiceProConstants.TASK_TRANS_API);
	            			}
            			}else if(apiName.equals(ServiceProConstants.TASK_CONFIRMATION_API)){
            				if(colIdVal > 0){
            					SapQueueProcessorHelperConstants.updateSelectedRowStatusForServicePro(this, colIdVal, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_COMPLETED, ServiceProConstants.TASK_CONFIRMATION_API);
	            			}else{
	            				SapQueueProcessorHelperConstants.updateSelectedRowStatusForServicePro(this, 0, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_COMPLETED, ServiceProConstants.TASK_CONFIRMATION_API);
	            			}
            			}
            		}
                    catch(Exception sff1){
                    	ServiceProConstants.showErrorLog("Error in updating queue process database: "+sff1.toString());
                    }       
            	}
            	else{            		
            		CharSequence contentTitle = "ServicePro notifications";
            		CharSequence contentText = errorDesc;    		
            		
            		if(apiName.equals(ServiceProConstants.STATUS_UPDATE_API)){
            			contentText = "Error in Status updates during queue processing!";
            		}else{
            			contentText = errorDesc;
            		}           		
            		
            		int uniqueIdVal = (int) (Integer.parseInt(tracId) + System.currentTimeMillis());
            		Intent notificationIntent = new Intent(Intent.ACTION_MAIN); 
            		
      				if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
      					notificationIntent.setClass(getApplicationContext(), ServiceProTaskMainScreenForTablet.class);
      				}else{
      					notificationIntent.setClass(getApplicationContext(), ServiceProTaskMainScreenForPhone.class);
      				} 		
            		notificationIntent.putExtra(ServiceProConstants.QUEUE_COLID, colIdVal);
            		notificationIntent.putExtra(ServiceProConstants.QUEUE_ERR_APPREFID, tracId);
            		notificationIntent.putExtra(ServiceProConstants.QUEUE_ERR_MSG, errorDesc);            		
            		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            		notificationIntent.setAction("action_id_" + uniqueIdVal);
            		contentIntent = PendingIntent.getActivity(context, uniqueIdVal, notificationIntent, 0);
            		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);            		
            		mNotificationManager.notify(colIdVal, notification);   
            		
            		boolean apiExists = false;
            		if(errorDbObj == null)
	        			errorDbObj = new ServiceProErrorMessagePersistent(this.getApplicationContext());
            		if(errorDbObj != null){
            			apiExists = errorDbObj.checkTrancIdApiExists(tracId.trim(), apiName.trim());
				    	if(!apiExists){
				    		//errorDbObj.insertErrorMsgDetails(tracId, errType, errorDesc, apiName);
				    		errorDbObj.insertErrorMsgDetails(tracId, errType, errorDesc, apiName);
				    	} 
				    	else{
				    		errorDbObj.updateValue(tracId, errType, errorDesc, apiName);
				    	}	            		
            		}
            		if(errorDbObj != null)
	        			errorDbObj.closeDBHelper();	 
            	}
            }
            catch(Exception asf){
            	ServiceProConstants.showErrorLog("Bg process in inserting databases : "+asf.toString());
            	if(errorDbObj != null)
        			errorDbObj.closeDBHelper();	 
            }
        }
    }//fn soapResponse
}