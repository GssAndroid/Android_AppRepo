package com.globalsoft.ServicePro;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public final class ServiceProConstants {
	
	//Log Related Constants
    public static final String SERVICEPRO_TAG = "ServicePro ";
    public static final String SERVICEPRO_ERRORTAG = "ServicePro Error ";     
   
    //Mobile Related Constants
    public static String SERVICEPRO_MOBILE_IMEI = "";
    public static String SERVICEPRO_EMULATOR_IMEI = "000000000000000";
    
    //Method call Constants
    public static int TASKS_DOWNLOAD_METHOD = 0;
    public static int TASKS_UPDATE_METHOD = 1;
    public static int TASKS_REJECT_METHOD = 2;
    public static int DOCUMENT_CONFIRM_METHOD = 3;
    public static int REPORTS_DISPLAY_METHOD = 4;
    public static int SOC_SAVESUBMIT_METHOD = 5;
    public static int FIRST_LANUCH_SCR = 0;
    public static int DIOG_FLAG = 0;

    //Preference Related Constant
	public static String PREFS_NAME_FOR_VAN_STOCK = "VanstocksPrefs";
	public static String PREFS_KEY_VAN_STOCK_FOR_MYSELF_GET = "VAN-STOCK-FOR-MYSELF-GET";
	public static String PREFS_KEY_VAN_STOCK_FOR_COLLEAGUE_GET = "VAN-STOCK-FOR-COLLEAGUE-GET";
	public static String PREFS_NAME_FOR_SERVICE_TASKS	 = "ServiceTaskPrefs";
  	public static String PREFS_KEY_SERVICE_TASKS_FOR_MYSELF_GET = "SERVICE-TASKS-FOR-MYSELF-GET";
    
    //Colleague Related Constant
    public static int VIEWCOLLEAGUELIST = 1;
    public static int VIEWCOLLEAGUELISTANDTRANS = 2;
        
    //Task Status
    public static int TASK_STATUS_READY = 0;    
    public static int TASK_STATUS_ACCEPTED = 1;
    public static int TASK_STATUS_DEFERRED = 2;
    public static int TASK_STATUS_DECLINED = 3;
    public static int TASK_STATUS_COMPLETED = 4;   
    
    public static final String TASK_STATUS_READY_STR = "Ready";
    public static final String TASK_STATUS_ACCEPTED_STR = "Accepted";
    public static final String TASK_STATUS_DEFERRED_STR = "Deferred - no parts";
    public static final String TASK_STATUS_DECLINED_STR = "Declined";
    public static final String TASK_STATUS_COMPLETED_STR = "Completed";
        
    public static final String TASK_STATUS_READY_STR_FOR_SAP = "WAIT";
    public static final String TASK_STATUS_ACCEPTED_STR_FOR_SAP = "ACPT";
    public static final String TASK_STATUS_DEFERRED_STR_FOR_SAP = "DEFR";
    public static final String TASK_STATUS_DECLINED_STR_FOR_SAP = "RJCT";
    public static final String TASK_STATUS_COMPLETED_STR_FOR_SAP = "COMP";
    
    public static final String TASK_STATUS_POST_ACTION_DROPFDEVICE = "DROP-FROM-DEVICE-DB";    
    public static final String TASK_STATUS_POST_ACTION_ARJR = "ASK-FOR-REJECTION-REASON";    
    public static final String TASK_STATUS_POST_ACTION_AETA = "ASK-FOR-ETA";
    
    public static final String TASK_STATUS_REASON_ONLEAVE_STR = "On Leave";
    public static final String TASK_STATUS_REASON_SICK_STR = "Sick";
    public static final String TASK_STATUS_REASON_ENGGANOTJOB_STR = "Engaged in another Job";
    public static final String TASK_STATUS_REASON_TRAIN_STR = "Training";
    public static final String TASK_STATUS_REASON_TRAVEL_STR = "Travelling";
    public static final String TASK_STATUS_REASON_OTHER_STR = "Other";
    public static  String ERROR_MSG = " ";
    
    public static int TASK_STATUS_REASON_ONLEAVE = 1;    
    public static int TASK_STATUS_REASON_SICK = 2;
    public static int TASK_STATUS_REASON_ENGGANOTJOB = 3;
    public static int TASK_STATUS_REASON_TRAIN = 4;
    public static int TASK_STATUS_REASON_TRAVEL = 5;   
    public static int TASK_STATUS_REASON_OTHER = 6;
        
    //PRIORITY CONSTANTS
    public static final String TASK_PRIORITY_HIGH_STR = "HIGH";
    public static final String TASK_PRIORITY_NORMAL_STR = "NORMAL";
    public static final String TASK_PRIORITY_LOW_STR = "LOW";
    
    public static int TASK_PRIORITY_HIGH = 0;    
    public static int TASK_PRIORITY_NORMAL = 1;
    public static int TASK_PRIORITY_LOW = 2;
    
    public static final String TASK_PRIORITY_HIGH_VAL_STR = "Very High";    
    public static final String TASK_PRIORITY_NORMAL_VAL_STR = "High";
    public static final String TASK_PRIORITY_LOW_VAL_STR = "Medium";
        
    public static String INITIAL_STATUS_VALUE ="";
    public static String STATUS_REASON_VALUE = "";
    
    //QUEUE STATUS CONSTANTS
    public static final int STATUS_IDLE = 0;
    public static final int STATUS_INPROCESS = 1;
    public static  int UPDATE_ERROR = 0;
    //public static final int STATUS_SENDTOSERVER = 2;
    public static final int STATUS_COMPLETED = 3;
    public static final int STATUS_HIGHPRIORITY = 100;
    
    //QUEUE PARAMS CONSTANTS
    public static final String QUEUE_COLID = "COLID";
    public static final String QUEUE_APPREFID = "APPREFID";
    public static final String QUEUE_SOAPAPINAME = "SOAPAPINAME";
    public static final String QUEUE_APPLNAME = "APPLICATIONNAME";
    public static final String QUEUE_RESULTSOAPOBJ = "RESULTSOAPOBJ";
    public static final String QUEUE_REQUESTSOAPOBJ = "REQUESTSOAPOBJ";    
    public static final String QUEUE_ERR_APPREFID = "ERRAPPREFID";
    public static final String QUEUE_ERR_MSG = "ERRMSG";
    public static String QUEUE_NOTIFICATION = "QUEUENOTIFY";
    public static final String COL_APPNAME = "appname";
    public static final String COL_STATUS = "status";
    public static final String COL_APINAME = "apiname";
    public static final String COL_ID = BaseColumns._ID;
    
    //For API names
    public static final String STATUS_UPDATE_API = "SERVICE-DOX-STATUS-UPDATE";
    public static final String TASK_TRANS_API = "SERVICE-DOX-TRANSFER";
    public static final String TASK_CONFIRMATION_API = "SERVICE-CONF-CREATE";
    
    public static final int STATUS_UPDATE_TYPE = 801;
    public static final int TASK_TRANS_TYPE = 802;
    public static final int TASK_CONFIRMATION_TYPE = 803;
    public static final int COLLEAGE_TASK_TYPE = 804;
    public static final int ATTACH_TYPE = 805;
    
    //For Timer 
    public static int TIMER_CONST = 3000; // 10000 is 10secs or 30000 is 30secs or 60000 is 1 min
    
    //Task Related Constant
    //public static byte[] TASKOBJ = null;
    public static String IMEINO = "";
    
    public static String PRODUCT_DESC = "";
    
    //REPORTS SORT CONSTANTS
    public static int REPORTS1_SORT_DATE = 0;
    public static int REPORTS1_SORT_SERVORD = 1;
    public static int REPORTS1_SORT_CUSTOMER = 2;    
    
    //VAN STOCK SORT CONSTANTS
    public static int VANSTOCK_SORT_MATERIAL = 0;
    public static int VANSTOCK_SORT_MATDESC = 1;
    
    //TASK SORT CONSTANTS
    public static int TASK_SORT_CITY = 0;
    public static int TASK_SORT_NAME = 1;
    public static int TASK_SORT_DATE = 2;
    public static int TASK_SORT_PRIORITY = 3;    
    public static int TASK_SORT_STATUS = 4;
    public static int TASK_SORT_SO = 5;
    public static int TASK_SORT_CNAME = 6;
    public static int TASK_SORT_PRODUCT = 7;
    public static int TASK_SORT_ETA = 8;
    
    //Screen Related Constants
    public static final int MAIN_SCREEN = 1;
    public static final int REPORT1_SCREEN = 2;
    public static final int REPORT1_DETAIL_SCREEN = 3;
    public static final int TASKLIST_SCREEN = 4;
    public static final int VANSTOCK_SCREEN = 5;
    public static final int VANSTOCK_COL_SCREEN = 6;
    public static final int TASK_EDIT_SCREEN = 7;
    public static final int CNFM_MAIN_SCREEN = 8;
    public static final int BARCODE_MAIN_SCREEN = 9;
    public static final int IMAGE_CAPTURE_SCREEN = 10;
    public static final int CAPTURED_IMAGE_DIS_SCREEN = 11;   
    public static final int VANSTOCK_SRL_SCREEN = 12;
    public static final int VANSTOCK_DETL_SCREEN = 13;
    public static final int VANSTOCK_TRANSCRT_SCREEN = 14;
    public static final int SIGNATURE_DIS_SCREEN = 15;
    public static final int ABOUT_SCREEN = 16;
    public static final int COLLEAGUE_LIST_SCREEN = 17;
    public static final int COLLEAGUE_TASK_LIST_SCREEN = 18;
    public static final int ACTIVITY_MAIN_SCREEN = 119;       
    public static final int LEAVE_MAIN_SCREEN = 201;  
    public static final int LEAVE_APPLY_SCREEN = 202;
    public static final int ENTILEMENT_SCREEN = 203;
    public static final int VANSTOCK_OVERVIEW_SCREEN = 205;
    public static final int ATTACHMENT_LIST_SCREEN = 206;
    
    //Local File Path
    public static final String IMAGECAPTUREPATH = Environment.getExternalStorageDirectory() + File.separator + "small_image.jpg";
    public static final String SIGNIMAGEPATH = Environment.getExternalStorageDirectory() + File.separator + "sign_small_image.jpg";
    
    //SOAP CONSTANTS   
    public static final String SOAP_SERVICE_URL = "http://75.99.152.10:8050/sap/bc/srt/rfc/sap/z_gssmwfm_hndl_evntrqst00/110/z_gssmwfm_hndl_evntrqst00/z_gssmwfm_hndl_evntrqst00";
    public static final String SOAP_ACTION_URL = "http://75.99.152.10:8050/sap/bc/srt/wsdl/bndg_E0A8AEE275F3AEF1AE7900188B47B426/wsdl11/allinone/ws_policy/document?sap-client=110";
    public static final String SOAP_TYPE_FNAME = "ZGssmwfmHndlEvntrqst00";   
    public static final String SOAP_SERVICE_NAMESPACE = "urn:sap-com:document:sap:soap:functions:mc-style";
    public static final String SOAP_INPUTPARAM_NAME = "DpistInpt";
    public static final String SOAP_NOTATION_DELIMITER = "NOTATION:ZML:VERSION:0:DELIMITER:[.]";
    public static String SOAP_RESP_MSG = "";
    public static String SOAP_ERR_TYPE = "";    
    
    public static final String SERVICEORD_COMMON_DG_SELACTVY="Select an Activity to proceed!";
            
    //Application Name Constants
    public static String APPLN_NAME_STR = "SERVICEPRO";    
    public static String APPLN_PACKAGE_NAME = "com.globalsoft.ServicePro"; 
    public static String APPLN_BGSERVICE_NAME = "com.globalsoft.ServicePro.BackgroundService";
    public static String SERVICEORD_COMMON_OTHERS="Others";
    public static int activity_recall_flag = 0;
    public static int activityTimeZoneSelRawoffset;
    public static boolean spares_recall_flag = false;
    
    //VanStock Constants
    public static final String VANSTOCK_SYNC_VAL = "X";
    
    //Activity Fault code Constants
    public static Vector productListVect = new Vector();
    public static Vector causeCodeListVect = new Vector();
    public static Vector causeCodeGroupVect = new Vector();
    public static Vector probCodeListVect = new Vector();
    public static Vector probCodeGroupVect = new Vector();
    public static Vector symptmCodeListVect = new Vector();
    public static Vector symptmCodeGroupVect = new Vector();
    public static Vector mattEmployeeListVect = new Vector();  
    public static Vector docvectlist = new Vector();  
    
    //Used for listing Spares
    public static Vector sparesCollectionVect = new Vector();
    public static Vector confirmCollectionVect = new Vector();
    
    public static Vector actColltVect = new Vector();
    public static Vector faultColltVect = new Vector();
    public static Vector sparesColltVect = new Vector();
    
    public static ArrayList stocksCollSrlArrList = new ArrayList();
    public static ArrayList errorTaskIdForStatus = new ArrayList();
    
    //TimeZone Constants
    public static ArrayAdapter tzoneadapter;
    public static String tz_str[] = null;
        
    //Text Size related Constants
    public static final int TEXT_SIZE_LABEL = 20;
    public static final int TEXT_SIZE_BUTTON = 20;
    public static final int TEXT_SIZE_TABLE_HEADER = 16;
    public static final int TEXT_SIZE_TABLE_ROW = 18;
    
    //EditText Width Height parameters
    public static final int EDIT_TEXT_WIDTH = 250;
    public static final int EDIT_TEXT_HEIGHT = 60;
    public static final int EDIT_TEXT_RIGHTMARGIN = 10;
    
    public static int TITLE_DISPLAY_WIDTH = 300;
    public static int SCREEN_CHK_DISPLAY_WIDTH = 799; //850
    
    //Soap response related constants
    public static final int RESP_TYPE_GET_STATUS = 101;
    public static final int RESP_TYPE_GET_TASK = 102;
    
    //Log related functions
    public static void showLog(String text){
    	Log.e(ServiceProConstants.SERVICEPRO_TAG,text);
    }
    
    public static void showErrorLog(String text){
    	Log.e(ServiceProConstants.SERVICEPRO_ERRORTAG,text);
    }
    
    public static void showErrorDialog(Context ctx, String text){
    	Toast aToast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
    	ToastExpander.showFor(aToast, 5000); 
    }
    
    public static void setWindowTitleTheme(Context ctx){
    	try{
    		if(ServiceProConstants.TITLE_DISPLAY_WIDTH > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH)
    			ctx.setTheme(R.style.LargeTitleTheme);
    	}
    	catch(Exception sghh){
    		ServiceProConstants.showErrorLog("Error on setWindowTitleTheme : "+sghh.toString());
    	}
    }//fn setWindowTitleTheme
    
    /*
    public static String getMobileIMEI(Context ctx){
    	String imei = "";
    	try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			imei = mTelephonyMgr.getDeviceId(); 
			//imei = "000000000000000";
			//imei = "358472040687476";
			//imei = "358444040351783";
			//imei = "99000032030454";
			//imei = "990000320304548";
			IMEINO = imei.trim();
			ServiceProConstants.showLog("MobileIMEI: "+imei);
		} 
    	catch (Exception e) {
    		ServiceProConstants.showErrorLog(e.toString());
		}
    	finally{
    		if((imei == null)|| (imei.equalsIgnoreCase(""))){
    			imei = "000000000000000";
    		}
    	}
    	return imei;
    }//fn getMobileIMEI
    */
    
    public static String getApplicationIdentityParameterForDiagnosis(Context ctx){
    	String appParams = "";
    	try {
    		appParams = "DEVICE-ID:"+ServiceProConstants.getMobileIMEI(ctx)+":DEVICE-TYPE:ANDROID:APPLICATION-ID:"+ServiceProConstants.APPLN_NAME_STR+":MODE:D";
    		//ServiceProConstants.showLog("App param: "+appParams);
		} 
    	catch (Exception e) {
    		ServiceProConstants.showErrorLog(e.toString());
		}
    	return appParams;
    }//fn getApplicationIdentityParameter
        
    public static String getMobileIMEI(Context ctx){
    	String imenoStr = "", wifiStr = "", buildIdStr = "", buildDetStr = "";
		try{
			TelephonyManager TelephonyMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
			imenoStr = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
			//imenoStr = "358472040687476";
			//imenoStr =SERVICEPRO_EMULATOR_IMEI;
			imenoStr ="000000000000000";
        	ServiceProConstants.showLog("Mobile Imeno : "+imenoStr);
		}
		catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error on getMobileIMEI : "+sgh.toString());
    	}
		finally{
        	if((imenoStr == null) || (imenoStr.equalsIgnoreCase(""))){
        		try{
        			WifiManager wm = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        			wifiStr = wm.getConnectionInfo().getMacAddress();
        			//ServiceProConstants.showLog("Mobile Wifi Id : "+wifiStr);
        			 
        			 buildIdStr = "25" + //we make this look like a valid IMEI
		            	Build.BOARD.length()%10+ Build.BRAND.length()%10 + 
		            	Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + 
		            	Build.DISPLAY.length()%10 + Build.HOST.length()%10 + 
		            	Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + 
		            	Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + 
		            	Build.TAGS.length()%10 + Build.TYPE.length()%10 + 
		            	Build.USER.length()%10 ; //13 digits
		            //ServiceProConstants.showLog("Mobile Device Id : "+buildIdStr);
		            
		            buildDetStr = Build.FINGERPRINT+" : "+Build.HARDWARE;
		            //ServiceProConstants.showLog("Mobile Device Str : "+buildDetStr);
		            
		            String m_szLongID = wifiStr + buildIdStr+ buildDetStr;
		        	MessageDigest m = null;
		    		try {
		    			m = MessageDigest.getInstance("MD5");
		    		} catch (NoSuchAlgorithmException e) {
		    			e.printStackTrace();
		    		} 
		    		m.update(m_szLongID.getBytes(),0,m_szLongID.length());
		    		byte p_md5Data[] = m.digest();
		    		
		    		imenoStr = "";
		    		for (int i=0;i<p_md5Data.length;i++) {
		    			int b =  (0xFF & p_md5Data[i]);
		    			// if it is a single digit, make sure it have 0 in front (proper padding)
		    			if (b <= 0xF) imenoStr+="0";
		    			// add number to string
		    			imenoStr+=Integer.toHexString(b); 
		    		}
		    		
		    		imenoStr = imenoStr.toUpperCase();
		    		//ServiceProConstants.showLog("Mobile Unique Id for Imeno : "+imenoStr);
        		}
        		catch(Exception sfsg){
        			ServiceProConstants.showErrorLog("Error on getMobileIMEI 2: "+sfsg.toString());
        		}
        	}
		}
		return imenoStr;
    }//fn getMobileIMEI
    
    public static String getApplicationIdentityParameter(Context ctx){
    	String appParams = "";
    	try {
    		appParams = "DEVICE-ID:"+ServiceProConstants.getMobileIMEI(ctx)+":DEVICE-TYPE:ANDROID:APPLICATION-ID:"+ServiceProConstants.APPLN_NAME_STR;
    		//ServiceProConstants.showLog("App param: "+appParams);
		} 
    	catch (Exception e) {
    		ServiceProConstants.showErrorLog(e.toString());
		}
    	return appParams;
    }//fn getApplicationIdentityParameter
    
    public static int getDisplayWidth(Context ctx){
        int dispwidth = 300;
    	try{
	    	Display display = ((WindowManager) ctx.getSystemService(ctx.WINDOW_SERVICE)).getDefaultDisplay();
	    	dispwidth = display.getWidth();
        }
        catch (Exception e) {
    		ServiceProConstants.showErrorLog(e.toString());
		}
    	finally{
    		if(dispwidth <= 0)
    			dispwidth = 300;
    	}
    	return dispwidth;
    }
    
    public static SpannableString getUnderlinedString(String normalStr){
    	SpannableString content = null;    	
    	try {
    		if((normalStr != null) && (!normalStr.equalsIgnoreCase(""))){
				content = new SpannableString(normalStr);
				content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
    		}
		} catch (Exception aasd) {
			ServiceProConstants.showErrorLog("Error in getUnderlinedString : "+aasd.toString());
		}
    	return content;
    }//fn getUnderlinedString
    
    public static String getSecondsRemovedTimeStr(String timeStr){
    	String content = null;    	
    	try {
			content = timeStr;
    		if((timeStr != null) && (!timeStr.equalsIgnoreCase(""))){
				int lastIndex = timeStr.lastIndexOf(':');
				content = timeStr.substring(0, lastIndex);
    		}
		} catch (Exception aasd) {
			ServiceProConstants.showErrorLog("Error in getSecondsRemovedTimeStr : "+aasd.toString());
		}
    	return content;
    }//fn getSecondsRemovedTimeStr
    
    public static String getTaskDateString(long dateL, boolean noHourFlag){
        String dateStr = "0000-00-00";
        try{
            if(dateL > 0){
                Date dateT = new Date(dateL);
                if(dateT != null){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateT);
                    int month = calendar.get(Calendar.MONTH)+1;
                    if(noHourFlag == true)
                        dateStr = calendar.get(Calendar.YEAR)+"-"+String.valueOf(month)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                    else{
                        dateStr = calendar.get(Calendar.YEAR)+"-"+String.valueOf(month)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" ";
                        dateStr += calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                    }
                }
            }
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in getStatusStr() : "+asd.toString());
        }
        return dateStr;
    }//fn getTaskDateString
    
    
    public static String getDateFormatForSystemDF(int dayVal, int monthVal, int year){
    	String dateStr = "0000-00-00";
    	String monthStr = "", dayStr = ""; 
    	try {
			if(monthVal < 10){
				monthStr = "0"+monthVal;
			}
			else
				monthStr = ""+monthVal;
			
			if(dayVal < 10){
				dayStr = "0"+dayVal;
			}
			else
				dayStr = ""+dayVal;
			
			dateStr = year+"-"+monthStr+"-"+dayStr;
		} catch (Exception ase) {
			ServiceProConstants.showErrorLog("Error in getDateFormatForSystemDF : "+ase.toString());
		}
    	
    	return dateStr;
    }//fn getDateFormatForSystemDF
    
    
    public static String getTaskDateStringFormat(long dateL, boolean noHourFlag){
        String dateStr = "0000-00-00";
        try{
            if(dateL > 0){
                Date dateT = new Date(dateL);
                if(dateT != null){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateT);
                    int month = calendar.get(Calendar.MONTH)+1;
                    if(noHourFlag == true){
                    	String monthVal = String.valueOf(month);
                    	if(monthVal.length() == 1){
                    		monthVal = "0"+monthVal;
                    	}
                    	String dayVal = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                    	if(dayVal.length() == 1){
                    		dayVal = "0"+dayVal;
                    	}
                        dateStr = calendar.get(Calendar.YEAR)+"-"+monthVal+"-"+dayVal+" ";
                    }
                    else{
                    	String monthVal = String.valueOf(month);
                    	if(monthVal.length() == 1){
                    		monthVal = "0"+monthVal;
                    	}
                    	String dayVal = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                    	if(dayVal.length() == 1){
                    		dayVal = "0"+dayVal;
                    	}
                        dateStr = calendar.get(Calendar.YEAR)+"-"+monthVal+"-"+dayVal+" ";
                        dateStr += calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                    }
                }
            }
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in getTaskDateStringFormat : "+asd.toString());
        }
        return dateStr;
    }//fn getTaskDateString
    
    public static String getActivityDateString(long dateL, boolean noHourFlag){
        String dateStr = "0000-00-00";
        try{
            if(dateL > 0){
                Date dateT = new Date(dateL);
                if(dateT != null){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateT);
                    int month = calendar.get(Calendar.MONTH)+1;
                    String month_dec = getMonthValue(month);
                    if(noHourFlag == true)
                        dateStr = calendar.get(Calendar.DAY_OF_MONTH)+"-"+month_dec+"-"+calendar.get(Calendar.YEAR);
                    else{
                        dateStr = calendar.get(Calendar.DAY_OF_MONTH)+"-"+month_dec+"-"+calendar.get(Calendar.YEAR)+" ";
                        dateStr += calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
                    }
                }
            }
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in getActivityDateString : "+asd.toString());
        }
        return dateStr;
    }//fn getTaskDateString
    
    public static String getSystemDateFormatString(Context ctx, String dateStr){
    	String newDateStr = null;
    	try{
    		Time strTime = new Time();
			strTime.parse3339(dateStr);
			//strTime.parse(dateStr);
			long millis = strTime.normalize(false);
			if(millis > 0){
				Date dateObj = new Date(millis);
				
				java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(ctx.getApplicationContext());
				newDateStr = dateFormat.format(dateObj);
				//ServiceProConstants.showLog("Formatted Date  : "+newDateStr);
			}
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in getSystemDateFormatString : "+sfg.toString());
    	}
    	finally{
    		if((newDateStr == null) || (newDateStr.equalsIgnoreCase(""))){
    			newDateStr = dateStr;
    		}
    	}
		return newDateStr;
    }//fn getSystemDateFormatString
    
    /*
    public static String getSystemDateFormat(Context ctx, String format, String dateStr){
    	String formatedDateStr = "";
    	try {
    		SimpleDateFormat curFormater = new SimpleDateFormat(format); 
        	Date dateObj = curFormater.parse(dateStr); 
        	ServiceProConstants.showLog("Format dateObj : "+dateObj);
        	java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(ctx.getApplicationContext());
        	formatedDateStr = dateFormat.format(dateObj);
		} 
    	catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error on getSystemDateFormat : "+e.toString());
    		formatedDateStr = dateStr;
		}
    	return formatedDateStr;
    }//fn getSystemDateFormat
    
    
   
    public static String getSystemDateFormatString(Context ctx, String dateStr){
    	String newDateStr = null;
    	try{
    		String formatStr = "";
    		char[] dateOrder = android.text.format.DateFormat.getDateFormatOrder(ctx.getApplicationContext());
			for(int k=0; k<dateOrder.length; k++){
				ServiceProConstants.showLog("Date Order "+(k+1)+": "+dateOrder[k]);
				if((dateOrder[k] == 'M') || (dateOrder[k] == 'm'))
					formatStr += dateOrder[k]+""+dateOrder[k]+"-";
				else if((dateOrder[k] == 'D') || (dateOrder[k] == 'd'))
					formatStr += dateOrder[k]+""+dateOrder[k]+"-";
				else
					formatStr += dateOrder[k]+""+dateOrder[k]+dateOrder[k]+dateOrder[k]+"-";
			}
			formatStr = formatStr.substring(0, formatStr.length()-1);
			ServiceProConstants.showLog("Format Str : "+formatStr);
			
			
					
			ServiceProConstants.showLog("Date2  : "+strTime.parse3339(dateStr)+" : "+dateStr+" : "+millis+" : "+new Date(millis));
			String dateObj1 = ServiceProConstants.getSystemDateFormat(ctx, formatStr, dateStr);
			//String dateObj = ServiceProConstants.getSystemDateFormat(ctx, "yyyy-MM-dd", dateStr);
			ServiceProConstants.showLog("Date3  : "+dateObj1);
    		/*
    		final String format = Settings.System.getString(ctx.getContentResolver(), Settings.System.DATE_FORMAT);
    		ServiceProConstants.showLog("DateFormat : "+format);
			ServiceProConstants.showLog("Date Str : "+dateStr);
			String formatStr = "";
    		if (TextUtils.isEmpty(format)) {
    			char[] dateOrder = android.text.format.DateFormat.getDateFormatOrder(ctx.getApplicationContext());
    			for(int k=0; k<dateOrder.length; k++){
    				ServiceProConstants.showLog("Date Order "+(k+1)+": "+dateOrder[k]);
    				if((dateOrder[k] == 'M') || (dateOrder[k] == 'm'))
    					formatStr += dateOrder[k]+""+dateOrder[k]+"/";
    				else if((dateOrder[k] == 'D') || (dateOrder[k] == 'd'))
    					formatStr += dateOrder[k]+""+dateOrder[k]+"/";
    				else
    					formatStr += dateOrder[k]+""+dateOrder[k]+dateOrder[k]+dateOrder[k]+"/";
    			}
    			
    			DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(ctx.getApplicationContext());
    			ServiceProConstants.showLog("Inside 1 : "+format+" : "+formatStr);
    			if(dateFormat != null){
    				newDateStr = dateFormat.format(new java.util.Date(dateStr));
    			}
    		} else {
    			SimpleDateFormat sdateFormat = new SimpleDateFormat(format);
    			ServiceProConstants.showLog("Inside 2 : "+format);
    			if(sdateFormat != null)
    				newDateStr = sdateFormat.format(dateStr);
    		}
    		*/
    		/*
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in getSystemDateFormatString : "+sfg.toString());
    	}
    	finally{
    		if((newDateStr != null) || (!newDateStr.equalsIgnoreCase(""))){
    			newDateStr = dateStr;
    		}
    	}
		return newDateStr;
    }//fn getSystemDateFormatString
    	
		*/
    
    public static String getMonthValue(int month_value){
    	String month_dec = null;
    	if(month_value == 1){
    		month_dec = "Jan";
    	}
    	else if(month_value == 2){
    		month_dec = "Feb";
    	}
    	else if(month_value == 3){
    		month_dec = "Mar";
    	}
    	else if(month_value == 4){
    		month_dec = "Apr";
    	}
    	else if(month_value == 5){
    		month_dec = "May";
    	}
    	else if(month_value == 6){
    		month_dec = "Jun";
    	}
    	else if(month_value == 7){
    		month_dec = "Jul";
    	}
    	else if(month_value == 8){
    		month_dec = "Aug";
    	}
    	else if(month_value == 9){
    		month_dec = "Sep";
    	}
    	else if(month_value == 10){
    		month_dec = "Oct";
    	}
    	else if(month_value == 11){
    		month_dec = "Nov";
    	}
    	else if(month_value == 12){
    		month_dec = "Dec";
    	}
    	return month_dec;
    }//fn getMonthValue
    
    public static ArrayAdapter getTimeZoneAdapter(Context ctx){
    	ArrayAdapter <CharSequence> tzoneadapter = null;
    	try {
    		tzoneadapter = new ArrayAdapter <CharSequence> (ctx, android.R.layout.simple_spinner_item );
    		tzoneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		String[] TZ = TimeZone.getAvailableIDs();
	        ArrayList<String> TZ1 = new ArrayList<String>();
			if(tzoneadapter != null){
		        for(int i = 0; i < TZ.length; i++) {
		            if(!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getDisplayName()))) {
		                TZ1.add(TimeZone.getTimeZone(TZ[i]).getDisplayName());
		                tzoneadapter.add(TimeZone.getTimeZone(TZ[i]).getDisplayName());
		                //ServiceProConstants.showLog("TimeZone :"+TimeZone.getTimeZone(TZ[i]).getDisplayName());
		            }
		        }
			}
		} 
    	catch (Exception e) {
    		ServiceProConstants.showErrorLog(e.toString());
		}
    	return tzoneadapter;
    }//fn getMobileIMEI
    
    public static String getDateFormatForSAP(int mYear_dp, int mMonth_dp, int mDay_dp, int mHour_dp, int mMinute_dp){
		String taskDateStrValue = "";  
    	try {                      
        	// converting the datestring from the picker to a long: 
        	Calendar cal_date = Calendar.getInstance(); 
        	cal_date.set(Calendar.DAY_OF_MONTH, mDay_dp); 
        	cal_date.set(Calendar.MONTH, mMonth_dp-1); 
        	cal_date.set(Calendar.YEAR, mYear_dp); 
        	cal_date.set(Calendar.HOUR_OF_DAY, mHour_dp);
        	cal_date.set(Calendar.MINUTE, mMinute_dp);
        	Long lDate = cal_date.getTime().getTime(); 
        	//ServiceProConstants.showLog("lDate "+ lDate);
        	taskDateStrValue = ServiceProConstants.getTaskDateStringFormat(lDate, true).trim();
		} 
    	catch (Exception e) {
    		ServiceProConstants.showErrorLog(e.toString());
		}
    	return taskDateStrValue;
    }//fn getDateFormatForSAP
    
    //check for internet connection
    public static boolean checkInternetConn(Context ctx) 
    { 
    	ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService (Context.CONNECTIVITY_SERVICE);
    	// ARE WE CONNECTED TO THE NET
    	if (conMgr.getActiveNetworkInfo() != null
    	&& conMgr.getActiveNetworkInfo().isAvailable()
    	&& conMgr.getActiveNetworkInfo().isConnected()) {
    		return true;
    	} else {
    		Log.v("", "Internet Connection Not Present");
    		return false;
    	}
    } //End of checkConn fn
    
    
    /*public static byte[] getSerializableSoapObject(SoapObject soapObj){
    	byte[] soapBytes = null;
    	try {
    		if(soapObj != null){
	    		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    		envelope.setOutputSoapObject(soapObj);
	    		
	    		XmlSerializer aSerializer = Xml.newSerializer();
	    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    		try {
		    		aSerializer.setOutput(baos, "UTF-8");
		    		envelope.write(aSerializer);
		    		aSerializer.flush();
	    		} catch (Exception ewwe) {
	    			ServiceProConstants.showErrorLog("Error on getSerializableSoapObject1 : "+ewwe.toString());
	    		}
	    		
	    		if(baos != null){
	    			soapBytes = baos.toByteArray();
	    		}
    		}
		} 
    	catch (Exception es) {
    		ServiceProConstants.showErrorLog("Error on getSerializableSoapObject2 : "+es.toString());
		}
    	return soapBytes;
    }//fn getSerializableSoapObject
    
    public static SoapObject getDeSerializableSoapObject(byte[] soapBytes){
    	SoapObject soapObj = null;
    	try {
    		if(soapBytes != null){
    			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    		try {
	    			 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    	         factory.setNamespaceAware(true);
	    	         XmlPullParser xpp = factory.newPullParser();
	    	         String soapEnvStr = new String(soapBytes);
	    	         xpp.setInput(new StringReader(soapEnvStr));
	    	         
 	    			 ByteArrayInputStream inputStream = new ByteArrayInputStream(soapBytes);
	    	         xpp.setInput(inputStream, "UTF-8");
	    	         
	    	         envelope.parse(xpp);
	    	         soapObj = (SoapObject)envelope.bodyIn;

	    		} catch (Exception ewwe) {
	    			ServiceProConstants.showErrorLog("Error on getDeSerializableSoapObject1 : "+ewwe.toString());
	    		}
    		}
		} 
    	catch (Exception es) {
    		ServiceProConstants.showErrorLog("Error on getDeSerializableSoapObject2 : "+es.toString());
		}
    	return soapObj;
    }//fn getDeSerializableSoapObject
    
    public static void saveOfflineContentToQueueProcessor(Context ctx, String appRefId, String className, String apiName, SoapObject soapObj){
        byte[] soapBytes = null;
        try{
            if(soapObj != null){
                soapBytes = ServiceProConstants.getSerializableSoapObject(soapObj);
                if(soapBytes != null){
                    ContentResolver resolver = ctx.getContentResolver();
                    ContentValues val = new ContentValues();
                    if((appRefId != null) && (!appRefId.equalsIgnoreCase(""))){
                    	int refId = Integer.parseInt(appRefId);
                    	 val.put(SapQueueProcessorContentProvider.COL_APPREFID, appRefId);
                    }
                    val.put(SapQueueProcessorContentProvider.COL_APPNAME, ServiceProConstants.APPLN_NAME_STR);
                    val.put(SapQueueProcessorContentProvider.COL_PCKGNAME, ServiceProConstants.APPLN_PACKAGE_NAME);
                    val.put(SapQueueProcessorContentProvider.COL_CLASSNAME, className);
                    val.put(SapQueueProcessorContentProvider.COL_FUNCNAME, apiName);
                    val.put(SapQueueProcessorContentProvider.COL_SOAPDATA, soapBytes);
                    resolver.insert(SapQueueProcessorContentProvider.CONTENT_URI, val);
                }
                else
                	ServiceProConstants.showErrorLog("Soap byte Conversion is Null");
            }
            else
            	ServiceProConstants.showErrorLog("Offline Soap Object is Null");
        }
        catch(Exception sgh){
        	ServiceProConstants.showErrorLog("Error in saveOfflineContent : "+sgh.toString());
        }
    }//fn saveOfflineContentToQueueProcessor
    
    public static void updateSelectedRowStatus(Context ctx, int colId, String appName, int status, String apiname){
        Uri uri = null;
        String whereStr = null;
        String[] whereParams = null;
        try{
            if(colId > 0)
                uri = Uri.parse(SapQueueProcessorContentProvider.CONTENT_URI+"/"+colId);
            else{
                uri = Uri.parse(SapQueueProcessorContentProvider.CONTENT_URI+"");
                if(appName == null)
                    appName = "";
                
                whereStr = ServiceProConstants.COL_APPNAME + " = ? And "+ServiceProConstants.COL_STATUS+ " = ? And "+
                			ServiceProConstants.COL_APINAME+ " = ?";
                whereParams = new String[]{appName, String.valueOf(ServiceProConstants.STATUS_IDLE), apiname}; 
            }
            
            ServiceProConstants.showLog("status before : "+status);
            if(status < ServiceProConstants.STATUS_IDLE)
                status = ServiceProConstants.STATUS_IDLE;
            else if(status > ServiceProConstants.STATUS_HIGHPRIORITY)
                status = ServiceProConstants.STATUS_HIGHPRIORITY;
            ServiceProConstants.showLog("status after : "+status);
            
            ContentValues updateContent = new ContentValues();
            updateContent.put(ServiceProConstants.COL_STATUS, status);
 
            ContentResolver resolver = ctx.getContentResolver();
            int rows = resolver.update(uri, updateContent, whereStr, whereParams);
            
            ServiceProConstants.showLog("colId : "+colId);
            ServiceProConstants.showLog("appName : "+appName);
            ServiceProConstants.showLog("Row Updated : "+rows);
        }
        catch(Exception qsewe){
        	ServiceProConstants.showErrorLog("Error in updateSelectedRowStatus : "+qsewe.toString());
        }    
    }//fn updateSelectedRowStatus    

    public static void updateSelectedRowStatusToHighPriority(Context ctx, int colId, String appName, int status){
        Uri uri = null;
        String whereStr = null;
        String[] whereParams = null;
        try{
            if(colId > 0)
                uri = Uri.parse(SapQueueProcessorContentProvider.CONTENT_URI+"/"+colId);
            else{
                uri = Uri.parse(SapQueueProcessorContentProvider.CONTENT_URI+"");
                if(appName == null)
                    appName = "";
                
                whereStr = ServiceProConstants.COL_APPNAME + " = ? And "+ServiceProConstants.COL_STATUS+ " = ?";
                whereParams = new String[]{appName, String.valueOf(ServiceProConstants.STATUS_IDLE)}; 
            }
            
            //ServiceProConstants.showLog("status before : "+status);
            if(status < ServiceProConstants.STATUS_IDLE)
                status = ServiceProConstants.STATUS_IDLE;
            else if(status > ServiceProConstants.STATUS_HIGHPRIORITY)
                status = ServiceProConstants.STATUS_HIGHPRIORITY;
            //ServiceProConstants.showLog("status after : "+status);
            
            ContentValues updateContent = new ContentValues();
            updateContent.put(ServiceProConstants.COL_STATUS, status);
 
            ContentResolver resolver = ctx.getContentResolver();
            int rows = resolver.update(uri, updateContent, whereStr, whereParams);
        }
        catch(Exception qsewe){
        	ServiceProConstants.showErrorLog("Error in updateSelectedRowStatus : "+qsewe.toString());
        }    
    }//fn updateSelectedRowStatusToHighPriority
    
    public static int getApplicationQueueCount(Context ctx, String appName){
        int count = -1;
        Cursor cursor = null;
        try{
            if(appName == null)
                appName = "";
            
            Uri uri = Uri.parse(SapQueueProcessorContentProvider.CONTENT_URI+"");
            
            ContentResolver resolver = ctx.getContentResolver();
            
            String selection = ServiceProConstants.COL_APPNAME + " = ? And ("+ServiceProConstants.COL_STATUS+ " != ? And "+ServiceProConstants.COL_STATUS+ " != ? )";
            String[] selectionParams = new String[]{appName,  String.valueOf(ServiceProConstants.STATUS_SENDTOSERVER ),  String.valueOf(ServiceProConstants.STATUS_COMPLETED )};
            String[] projection = new String[]{ServiceProConstants.COL_ID}; 
            
            cursor = resolver.query(uri, projection, selection, selectionParams, null);
            
            if(cursor != null){
                count = cursor.getCount();
            }
        }
        catch(Exception sfag){
        	ServiceProConstants.showErrorLog("Error in getApplicationQueueCount : "+sfag.toString());
        }
        finally{
            if(cursor != null)
                cursor.close();    
        }
        return count;
    }//fn getApplicationQueueCount */
    
    public static void soapResponse(Context ctx, SoapObject soap, boolean offline){
		String taskErrorMsgStr="", errorDesc="", errType="";
        if(soap != null){
        	//ServiceProConstants.showLog("Count : "+soap.getPropertyCount());
            try{ 
                String delimeter = "[.]", result="", res="";
                SoapObject pii = null;
                String[] resArray = new String[50];
                int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0;
                for (int i = 0; i < soap.getPropertyCount(); i++) {                
                    pii = (SoapObject)soap.getProperty(i);
                    propsCount = pii.getPropertyCount();
                    //ServiceProConstants.showLog("propsCount : "+propsCount);
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                            if(j > 0){
                                result = pii.getProperty(j).toString();
                                firstIndex = result.indexOf(delimeter);
                                firstIndex = firstIndex + 3;
                                result = result.substring(firstIndex);
                                //SapTasksConstants.Log(result);
                                
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
                            }
                            else if(j == 0){
                                String errorMsg = pii.getProperty(j).toString();
                                //ServiceProConstants.showLog("Inside J == 0 ");
	                            int errorFstIndex = errorMsg.indexOf("Message=");
	                            if(errorFstIndex > 0){
	                            	int errorLstIndex = errorMsg.indexOf(";", errorFstIndex);
	                                taskErrorMsgStr = errorMsg.substring((errorFstIndex+"Message=".length()), errorLstIndex);
	                                //ServiceProConstants.showLog("Msg:"+taskErrorMsgStr);
	                                /*if(!offline)
	                                	ServiceProConstants.showErrorDialog(ctx, taskErrorMsgStr);*/
	                            	errorDesc = taskErrorMsgStr;	
	                            	SOAP_RESP_MSG = errorDesc; 
                                }
	                            int typeFstIndex = errorMsg.indexOf("Type=");
	                            if(typeFstIndex > 0){
	                            	int typeLstIndex = errorMsg.indexOf(";", typeFstIndex);
	                                String taskErrorTypeMsgStr = errorMsg.substring((typeFstIndex+"Type=".length()), typeLstIndex);
	                                //ServiceProConstants.showLog("Type:"+taskErrorTypeMsgStr);
	                                errType = taskErrorTypeMsgStr;
	                            	SOAP_ERR_TYPE = errType;
                                }
                            }
                        }
                    }
                }
            }
            catch(Exception sff){
            	ServiceProConstants.showErrorLog("On soapResponse : "+sff.toString());
            }
        }
    }//fn soapResponse
    
    static NotificationManager mNotificationManager = null;
    public static NotificationManager notificationAlert(Context ctx){
    	try{
	    	String ns = Context.NOTIFICATION_SERVICE;
			mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
	    }
	    catch(Exception sff){
	    	ServiceProConstants.showErrorLog("Error in notificationAlert : "+sff.toString());
	    }
    	return mNotificationManager;
    }//fn notificationAlert
    
    public static void stopNotificationAlert(int id){
    	try{
    		mNotificationManager.cancel(id);
	    }
	    catch(Exception sff){
	    	ServiceProConstants.showErrorLog("Error in stopNotificationAlert : "+sff.toString());
	    }
    }//fn stopNotificationAlert
    
    public static boolean getSoapResponseSucc_Err(String soapMsg){
    	boolean resMsgErr = false;
    	try{
        	if((soapMsg.indexOf("Type=A") > 0) || (soapMsg.indexOf("Type=E") > 0) || (soapMsg.indexOf("Type=X") > 0)){
        		resMsgErr = true;
            }else if(soapMsg.indexOf("Type=S") > 0){
            	resMsgErr = false;
            }   
	    }
	    catch(Exception sffe){
	    	ServiceProConstants.showErrorLog("Error in getSoapResponseSucc_Err : "+sffe.toString());
	    }
    	return resMsgErr;
    }//fn getSoapResponseSucc_Err
    
    public static void addToCalendar(Context ctx, final String title, final String desc, final long dtstart, final long dtend) { 
    	final ContentResolver cr = ctx.getContentResolver();
    	try{	    		    		    	
	    	// get calendar 
            Calendar cal = Calendar.getInstance();      
            // event insert 
            ContentValues values = new ContentValues(); 
            Uri event;
            values.put("calendar_id", 1); 
            values.put("title", title); 
            values.put("description", desc);             
            values.put("dtstart", dtstart); 
            values.put("dtend", dtend); 
            values.put("hasAlarm", 1);          
            
	    	if (Integer.parseInt(Build.VERSION.SDK) >= 8 ) {
	    		cr.delete( Uri.parse("content://com.android.calendar/events"), "title=? ", new String[]{String.valueOf(title)});
	    	}else{
	    		cr.delete( Uri.parse("content://calendar/events"), "title=? ", new String[]{String.valueOf(title)});
	    	}
	    	
	    	if (Integer.parseInt(Build.VERSION.SDK) >= 8 ) 
	    		event = cr.insert(Uri.parse("content://com.android.calendar/events"), values); 
	    	else
	    		event = cr.insert(Uri.parse("content://com.android.calendar/events"), values); 
	    	
	    	// reminder insert 
            values = new ContentValues(); 
            values.put( "event_id", Long.parseLong(event.getLastPathSegment())); 
            values.put( "method", 1 ); 
            values.put( "minutes", 120 ); 
            
            if (Integer.parseInt(Build.VERSION.SDK) >= 8 ) {
	    		cr.delete( Uri.parse("content://com.android.calendar/reminders"), "event_id=? ", new String[]{String.valueOf(Long.parseLong(event.getLastPathSegment()))});
	    	}else{
	    		cr.delete( Uri.parse("content://calendar/reminders"), "event_id=? ", new String[]{String.valueOf(Long.parseLong(event.getLastPathSegment()))});
	    	}
	    	
	    	if (Integer.parseInt(Build.VERSION.SDK) >= 8 ) 
	    		cr.insert(Uri.parse("content://com.android.calendar/reminders"), values); 
	    	else
	    		cr.insert(Uri.parse("content://com.android.calendar/reminders"), values); 
	    	
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in addToCalendar : "+sfg.toString());
		}
    }//fn addToCalendar
    
    public static boolean networkAvailableCheck(Context ctx){
    	boolean internetAccess = false;
    	try {
			internetAccess = ServiceProConstants.checkInternetConn(ctx);
		} catch (Exception esfg) {
			ServiceProConstants.showErrorLog("Error in networkAvailableCheck : "+esfg.toString());
			internetAccess = false;
		}
    	return internetAccess;
    }//fn networkAvailableCheck
    
    public static String getSignatureBigmagePath(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_sign_image.jpg";
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_sign_image.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getSignatureBigmagePath : "+esfgf.toString());
		}
    	return path;
    }//fn getSignatureBigmagePath
    
    public static String getSignatureSmallImagePath(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_sign_small_image.jpg";
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_sign_small_image.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getSignatureSmallImagePath : "+esfgf.toString());
		}
    	return path;
    }//fn getSignatureSmallImagePath
    
    public static String getSignatureBigmagePathForConfirScr(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_conf_sign_image.jpg";
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_conf_sign_image.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getSignatureBigmagePathForConfirScr : "+esfgf.toString());
		}
    	return path;
    }//fn getSignatureBigmagePath
    
    public static String getSignatureSmallImagePathConfirScr(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_conf_sign_small_image.jpg";
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_conf_sign_small_image.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getSignatureSmallImagePathConfirScr : "+esfgf.toString());
		}
    	return path;
    }//fn getSignatureSmallImagePath
    
    public static String getCapturedBigImagePath(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_capture_image.jpg"; 
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_Imagecapture.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getCapturedBigImagePath : "+esfgf.toString());
		}
    	return path;
    }//fn getCapturedBigImagePath
    
    public static String getCapturedSmallImagePath(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_cap_small_image.jpg";
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_cap_small_image.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getCapturedSmallImagePath : "+esfgf.toString());
		}
    	return path;
    }//fn getCapturedSmallImagePath
    
    public static String getCapturedBigImagePathConfirScr(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_conf_capture_image.jpg"; 
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_conf_Imagecapture.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getCapturedBigImagePath : "+esfgf.toString());
		}
    	return path;
    }//fn getCapturedBigImagePath
    
    public static String getCapturedSmallImagePathConfirScr(String objId){
    	String path = "";
    	try{
    		File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs"); 
			if (!folder.exists()) { 
			    folder.mkdir(); 
			} 
			if (folder.exists()) { 
				path = Environment.getExternalStorageDirectory() + "/Imgs/"+objId+"_conf_cap_small_image.jpg";
			} else { 
				path = Environment.getExternalStorageDirectory() + "/"+objId+"_conf_cap_small_image.jpg";	
			} 
    	} catch (Exception esfgf) {
			ServiceProConstants.showErrorLog("Error in getCapturedSmallImagePath : "+esfgf.toString());
		}
    	return path;
    }//fn getCapturedSmallImagePath
    
}//End of class ServiceProConstants    