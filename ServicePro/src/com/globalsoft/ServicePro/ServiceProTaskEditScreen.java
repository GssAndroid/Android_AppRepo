package com.globalsoft.ServicePro;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceActivity.Header;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.SapQueueProcessorHelper.Utils.SapQueueProcessorHelperConstants;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Constraints.StatusOpConstraints;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Map.MyLocation;
import com.globalsoft.ServicePro.Map.MyLocation.LocationResult;


public class ServiceProTaskEditScreen extends Activity implements TextWatcher,OnGestureListener{
	
	private ServiceProOutputConstraints category = null;
	private ServiceProOutputConstraints categorydb = null;
	private ServiceOrdDocOpConstraints categoryDocobj =null;
	
	private EditText  rejectresET,fieldnotesET; 
	private ImageView ddviewimg;
	private static TextView dateTV, timeTV, errorLbl, etaDateTV, etaTimeTV, priorityVal,
	summaryTV, idTV, descTV, notesTV, productdescVal, serialTV, intbaseDescTV, compDescTV, cus1TV,
	cus1phTV, cus1ph2TV, cus2TV, cus2phTV, cus2ph2TV, serOrdTypeTV,prevTV,nextTV;
	private LinearLayout error_header_linear, etalinear,mainlayoutrl;
	private static ScrollView scrolv;
	private Spinner statusSpn, rejectSpn, tzoneSpn;
	private ImageButton attaBtn, signAttsBtn, sapAttaBtn;
	private AlertDialog alertDialog;
	private RelativeLayout reasonLL;
	private ImageButton call1_bt1, call1_bt2, call2_bt1, call2_bt2,prevbtn,nextbtn; 
	private LinearLayout cus1ll, cus1ph1ll, cus1ph2ll, cus2ll, cus2ph1ll, cus2ph2ll, 
	productlinear, seriallinear, intbaselinear, complinear, itemDesclinear, serordtyplinear;
	private String[] arrChoices = {"------------------"}, timeZoneArr, choicesArr;
	private static final int MENU_ID = Menu.FIRST;
	private static final int MENU_MAP = Menu.FIRST+1;
	private static final int MENU_MAP_DIREC = Menu.FIRST+2;
	private static final int MENU_COLLEAGUE = Menu.FIRST+3;	
	private static final int MENU_ENTITLEMENTS = Menu.FIRST+4;	
    private static final int MENU_ADD_CAPTURE = Menu.FIRST+5;
	private static final int MENU_ADD_SIGNATURE = Menu.FIRST+6;
	private static final int MENU_CONVERT_PDF = Menu.FIRST+7;
	private String path = "", objId = "";
	private String imgCapPath = "", signPath = "";
	private String encodedImageStr = "";
	private String encodedSignImageStr = "";
	final Handler handler = new Handler();
	private String taskDateStrResp = "", taskETADateStrResp = "", taskETATimeStrResp = "";
	private String due_date_value_cal = "", due_time_value_cal = "";
    private String eta_date_value_cal = "", eta_time_value_cal = "",item = "",objidStr ="";
    private static String partnerStr = " ", zzprodDcestr= " ";
    private int dispwidth = 300;
 
	
	// date and time
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    
    private int mYear_dp;
    private int mMonth_dp;
    private int mDay_dp;
    private int mHour_dp;
    private int mMinute_dp;
    
    private int mYearETA;
    private int mMonthETA;
    private int mDayETA;
    private int mHourETA;
    private int mMinuteETA;
    
    private int mYearETA_dp;
    private int mMonthETA_dp;
    private int mDayETA_dp;
    private int mHourETA_dp;
    private int mMinuteETA_dp;
        
    private String sel_status="";
    private static final int TIME_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int ETA_DATE_DIALOG_ID = 2;
    private static final int ETA_TIME_DIALOG_ID = 3;
    private String initialDueDate = "";
    private String ETA_DATE_STR = "";
    private String ETA_TIME_STR = "";
    private String initialETADate = "";
    private String initialETATime = "";    
    
    private ArrayAdapter <CharSequence> priorAdapter, rejectAdapter;
    private Button savebtn, cancelbtn;
    private SoapObject resultSoap = null;
	private ProgressDialog pdialog = null;
	private String objectIdStr="", processTypeStr="", taskDateStr="", taskStatusStr="", reason="", taskErrorMsgStr="", taskErrorType="",ItemNumberStr = "";
	private boolean internetAccess = false;
	
	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
	private SoapObject requestSoapObj = null;
	private ArrayList itemList = new ArrayList();
	private ArrayList itemdbList = new ArrayList();
	private ArrayList allTaskList = new ArrayList();
	private ArrayList TaskList = new ArrayList();	
	private ServiceProTaskDetailsPersistent taskDbObj = null;
	private ServiceProTaskDetailsPersistent itemObj = null;
	private ServiceProErrorMessagePersistent errorDbObj = null;
	private ServiceProVectSerializable serObj1;
	private ArrayAdapter<String> spinnerArrayAdapter;
			
	private static ArrayList statusArray = new ArrayList();
	private ArrayList attachList = new ArrayList();
	
	private String lat = "", lon = "", geoloc = "", taskAddress = "", fieldnotesoapStr = "";

	private String statusValue = "", statusDescValue = "";
	private boolean prevStatusACPT = false,notficationflag =false;
	private int rtol= 0,index,leftToright =0, pos1=0;
	private static Bitmap bm,resized,scaledBitmap,bitmap2,scaledBitmap2,scaledBitmap3,image;
	
	//GESTURE DECLARATIONS
	 private GestureDetector gestureDetector;
	 private static int counter = 0;
	 ScaleGestureDetector scaleGestureDetector;
	 private static final String DEBUG_TAG = "Gestures";	
	 private static final int SWIPE_MIN_DISTANCE = 120;
     private static final int SWIPE_THRESHOLD_VELOCITY = 200;
     float x1,x2;
     float y1, y2;
     //private Context contxt;
	   
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{			
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.taskedit); 
	        gestureDetector = new GestureDetector(this);
	        //contxt = contxt.getApplicationContext();
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SAPTASK_EDITSCR_TITLE));
						 
			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			internetAccess = ServiceProConstants.checkInternetConn(ServiceProTaskEditScreen.this);
			//SGD = new ScaleGestureDetector(this,new ScaleListener());			
			
			boolean value = this.getIntent().getBooleanExtra("refresh", false);
			ServiceProConstants.showLog("On TASK_EDIT_SCREEN listrefresh : "+value);
  			if(value){
  				try {
					Intent intent;
					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
						intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForTablet.class);
					}else{
						intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForPhone.class);
					}  				
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);		
					startActivity(intent);
					finish();
				} catch (Exception ff) {
					ServiceProConstants.showErrorLog("Error in refresh value:"+ff.toString());
				}
  			}else{
  				category = (ServiceProOutputConstraints)this.getIntent().getSerializableExtra("taskobj");
  				categoryDocobj = (ServiceOrdDocOpConstraints)this.getIntent().getSerializableExtra("docobj");
  				index = this.getIntent().getIntExtra("index", index);
  				try{
  					String errMsg = this.getIntent().getStringExtra("errMsg");
  					if(errMsg != null && errMsg.length() > 0){
  						error_header_linear = (LinearLayout) findViewById(R.id.error_header_linear);
  						error_header_linear.setVisibility(View.VISIBLE);
  						errorLbl = (TextView) findViewById(R.id.errorLbl);			
  						errorLbl.setText(errMsg);	
  					}
  				}
  				catch (Exception de) {
  		        	ServiceProConstants.showErrorLog("Error in errMsg:"+de.toString());
  		        }

  				if(category != null){
  	  				objId = category.getObjectId().toString().trim();
  	  				path = ServiceProConstants.getCapturedBigImagePath(objId);
  	  				imgCapPath = ServiceProConstants.getCapturedSmallImagePath(objId);
  	  				signPath = ServiceProConstants.getSignatureSmallImagePath(objId);
  	  				
  					statusValue = category.getStatus().toString().trim();	
  					statusDescValue = category.getTxt30().toString().trim();
  					ServiceProConstants.INITIAL_STATUS_VALUE = statusDescValue;

  					ServiceProConstants.showLog("statusDescValue on create : "+statusDescValue);
  					prevStatusACPT = checkAcptStatus(statusDescValue);
  					
  					String numberExt = categoryDocobj.getNumberExt().toString().trim();
  					attachList = ServiceProDBOperations.readAttachListDataFromDB(this, objId, numberExt);
  					ServiceProConstants.showLog("No of attachements : "+attachList.size());
  					
  				}
  				itemObj = new ServiceProTaskDetailsPersistent(this);
  				TaskList = itemObj.getTaskDetails();
  				//mainlayoutrl = (LinearLayout) findViewById(R.id.part1);			
  				notesTV = (TextView) findViewById(R.id.notesTV);
  				rejectresET = (EditText) findViewById(R.id.rejectresET);
  				rejectresET.setEnabled(false);		
  				
  				summaryTV = (TextView) findViewById(R.id.summaryTV);  
  				//gestureDetector = new GestureDetector(this, new GestureListener()); 
  				/*gestureDetector = new GestureDetector(this, new GestureListener());  				 
  				mainlayoutrl.setOnTouchListener(new OnTouchListener() {
  		            public boolean onTouch(View v, MotionEvent event) {   		            
  		                if (gestureDetector.onTouchEvent(event)) {
  		                	ShoNextScreen();
  		                	//summaryTV.setText("Hi: " + counter++);
  		                    return true;
  		                }
  		                return false;
  		            }
  		        });*/
  		        
  				idTV = (TextView) findViewById(R.id.idTV);
  				
  				savebtn = (Button) findViewById(R.id.savebtn);
  				savebtn.setOnClickListener(savebtnListener); 
  				
  				attaBtn = (ImageButton) findViewById(R.id.attaBtn);
  		        attaBtn.setOnClickListener(attaBtnListener); 
  		        
  		        sapAttaBtn = (ImageButton) findViewById(R.id.sapAttachBtn);
  		        sapAttaBtn.setOnClickListener(sapAttaBtnListener);
  		        
  		        signAttsBtn = (ImageButton) findViewById(R.id.signAttsBtn);
  		        signAttsBtn.setOnClickListener(signAttsBtnListener);
  		        
  				cancelbtn = (Button) findViewById(R.id.cancelbtn);
  				cancelbtn.setOnClickListener(cancelbtnListener);	        
  							
  				dateTV = (TextView) findViewById(R.id.dateTV);
  				dateTV.setOnClickListener(new View.OnClickListener() {
  		            public void onClick(View v) {
  		                showDialog(DATE_DIALOG_ID);
  		            }
  		        });
  				
  				timeTV = (TextView) findViewById(R.id.timeTV);
  				timeTV.setOnClickListener(new View.OnClickListener() {
  		            public void onClick(View v) {
  		                showDialog(TIME_DIALOG_ID);
  		            }
  		        });
  				
  				etalinear = (LinearLayout) findViewById(R.id.etalinear);
  				
  				etaDateTV = (TextView) findViewById(R.id.etaDateTV);
  				etaDateTV.setOnClickListener(new View.OnClickListener() {
  		            public void onClick(View v) {
  		                showDialog(ETA_DATE_DIALOG_ID);
  		            }
  		        });
  				
  				etaTimeTV = (TextView) findViewById(R.id.etaTimeTV);
  				etaTimeTV.setOnClickListener(new View.OnClickListener() {
  		            public void onClick(View v) {
  		                showDialog(ETA_TIME_DIALOG_ID);
  		            }
  		        });
  				
  				reasonLL = (RelativeLayout) findViewById(R.id.reasonLL);
  				statusSpn = (Spinner) findViewById(R.id.statusSpn);
  				choicesArr = getChoicesArray();
  				if(choicesArr != null){
  					spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choicesArr);
  	  				spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  	  				
  	  			    statusSpn.setAdapter(spinnerArrayAdapter);
  	  			    statusSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
  	  			        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
  	  			            Object item = parent.getItemAtPosition(pos);
  	  			            sel_status = item.toString().trim();
  	  			            enableRejectSpinner(item.toString(), pos);

	            			ServiceProConstants.showLog("sel_status : "+sel_status);
  	  			            StatusOpConstraints statusObj = getStatusDescDetails(sel_status);
							if(statusObj != null){
								String statusPSAction = statusObj.getZZStatusPostSetAction().toString().toLowerCase().trim();
			            		if(statusPSAction != null && statusPSAction.length() > 0){
			            			ServiceProConstants.showLog("statusPSAction1 : "+statusPSAction);
			            			String str = ServiceProConstants.TASK_STATUS_POST_ACTION_ARJR;
			            			CharSequence cs = str.toLowerCase();
			            			ServiceProConstants.showLog("t/f : "+statusPSAction.contains(cs));			            			
			            			if(statusPSAction.contains(cs)){
			            				reasonLL.setVisibility(View.VISIBLE);
		  	  			            	setStatusReason();		
			            			}else{
			            				reasonLL.setVisibility(View.GONE);
			            			}			            			
			            		}else{
			            			reasonLL.setVisibility(View.GONE);
			            		}			            		
			            	}else{
			            		reasonLL.setVisibility(View.GONE);
			            	}     			

	            			ServiceProConstants.showLog("prevStatusACPT 2 : "+prevStatusACPT);
	            			if(!prevStatusACPT){
	            				StatusOpConstraints statusObj2 = getStatusDescDetails(sel_status);
								if(statusObj2 != null){
									String statusPSAction = statusObj2.getZZStatusPostSetAction().toString().toLowerCase().trim();
				            		if(statusPSAction != null && statusPSAction.length() > 0){
				            			String str = ServiceProConstants.TASK_STATUS_POST_ACTION_AETA;
				            			CharSequence cs = str.toLowerCase();
				            			ServiceProConstants.showLog("cs 2 : "+cs);
				            			ServiceProConstants.showLog("t/f 2: "+statusPSAction.contains(cs));	
				            			if(statusPSAction.contains(cs)){
				            				etalinear.setVisibility(View.VISIBLE);
				            				if(!statusDescValue.equals(sel_status)){
				  				            	showAcceptedAlertBox();		  
				            				}          				
				            			}else{
				            				etalinear.setVisibility(View.GONE);		            				
				            			}			            			
				            		}else{
					            		etalinear.setVisibility(View.GONE);	
					            	}       			
				            	}else{
				            		etalinear.setVisibility(View.GONE);	
				            	}
	  			            }else{
	  			            	StatusOpConstraints statusObj2 = getStatusDescDetails(sel_status);
								if(statusObj2 != null){
									String statusPSAction = statusObj2.getZZStatusPostSetAction().toString().toLowerCase().trim();
				            		if(statusPSAction != null && statusPSAction.length() > 0){
				            			ServiceProConstants.showLog("statusPSAction2 : "+statusPSAction);
				            			String str = ServiceProConstants.TASK_STATUS_POST_ACTION_AETA;
				            			CharSequence cs = str.toLowerCase();
				            			ServiceProConstants.showLog("cs : "+cs);
				            			ServiceProConstants.showLog("t/f : "+statusPSAction.contains(cs));	
				            			if(statusPSAction.contains(cs)){
				            				etalinear.setVisibility(View.VISIBLE);	            				
				            			}else{
				            				etalinear.setVisibility(View.GONE);		            				
				            			}			            			
				            		}else{
					            		etalinear.setVisibility(View.GONE);	
					            	}           			
				            	}else{
				            		etalinear.setVisibility(View.GONE);	
				            	}
	  			            }	            			      
  	  			        }
  	  			        public void onNothingSelected(AdapterView<?> parent) {
  	  			        }
  	  			    });
  				}else{
  					ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
  					spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  			        statusSpn.setAdapter(spinnerArrayAdapter);
  				}  				
  			    		    
  				rejectSpn = (Spinner) findViewById(R.id.rejectSpn);
  				rejectAdapter = ArrayAdapter.createFromResource(this, R.array.reject_status_array, android.R.layout.simple_spinner_item);
  				rejectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  				rejectSpn.setAdapter(rejectAdapter);
  				rejectSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
  			        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
  			            Object item = parent.getItemAtPosition(pos);
  			            enableRejectReason(item.toString(), pos);
  			        }
  			        public void onNothingSelected(AdapterView<?> parent) {
  			        }
  			    });
  				rejectSpn.setEnabled(false);
  			    
  				priorityVal  = (TextView) findViewById(R.id.priorityVal);
  				  			    
  				timeZoneArr = getTimeZoneArray();
  				tzoneSpn = (Spinner) findViewById(R.id.tzoneSpn);
  				if(timeZoneArr != null){     
  			        ArrayAdapter<String> timeZone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeZoneArr);
  			        timeZone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  			        tzoneSpn.setAdapter(timeZone_adapter);		        
  		        }
  		        else{
  		        	ArrayAdapter<String> timeZone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
  		        	timeZone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  			        tzoneSpn.setAdapter(timeZone_adapter);
  		        }
  				
  				final Calendar c = Calendar.getInstance();
  		        //initialize for start date and ETA date
  				mYear = c.get(Calendar.YEAR);
  				mMonth = c.get(Calendar.MONTH);
  				mDay = c.get(Calendar.DAY_OF_MONTH);
  				mHour = c.get(Calendar.HOUR_OF_DAY);
  				mMinute = c.get(Calendar.MINUTE);
  		        
  				mYear_dp = mYear;
  				mMonth_dp = mMonth;
  				mDay_dp = mDay;
  				mHour_dp = mHour;
  				mMinute_dp = mMinute;
  		        
  				updateDisplayStartDate();
  				updateDisplayStartTime();
  				
  				mYearETA_dp = mYear;
  				mMonthETA_dp = mMonth;
  				mDayETA_dp = mDay;
  				mHourETA_dp = mHour;
  				mMinuteETA_dp = mMinute;
  				updateDisplayETADate();
  				updateDisplayETATime();
  				attachImageDisplay();  
  				sapAttachImageDisplay();
  				signImageDisplay();
  				insertTasksInfo();
  				scrolv = (ScrollView) findViewById(R.id.scrollv);
  				//GeneratePdf();
  			}	  			
		}
		catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
	}	
	public void ShoNextScreen(){
		int myNum = 0;
		allTaskList = itemObj.getAllItemData(objId);
		ServiceProConstants.showLog("allTaskList : "+allTaskList);	
        for(int i=0;i<allTaskList.size();i++){
        	String id= allTaskList.get(i).toString();        	
          	try {
          	    myNum = Integer.parseInt(id.toString());
          	} catch(NumberFormatException nfe) {
          	   System.out.println("Could not parse " + nfe);
          	}
        }       
        if(leftToright ==1){
         	myNum--;
         }else{
         	 myNum++;
         }
        ServiceProConstants.showLog("myNum : "+myNum);	
        /*if(myNum==TaskList.size()){
        	Toast.makeText(getApplicationContext(), "No More Service Orders Please Swipe Left ", 1)
			.show();
        }else if(myNum< 0){ 
        	Toast.makeText(getApplicationContext(), "No More Service Orders Please Swipe Left ", 1)
			.show();        	
        }else{*/
        	categorydb= ((ServiceProOutputConstraints)TaskList.get(myNum));    	    			  	    			
          //	showEditscreen(categorydb);
        //}       
	}//
	 
	public class GestureListener extends SimpleOnGestureListener{
	       
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;
       
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, 
                                        float velocityX, float velocityY) {
           
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && 
                         Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //From Right to Left
            	Toast.makeText(getApplicationContext(), "Directory Created", 1)
				.show();
            	rtol = 1;
                return true;
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                         Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //From Left to Right
                return true;
            }
           
            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && 
                        Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //From Bottom to Top
                return true;
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && 
                        Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //From Top to Bottom
                return true;
            }
            return false;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            //always return true since all gestures always begin with onDown and<br>
            //if this returns false, the framework won't try to pick up onFling for example.
            return true;
        }
    }
	
	public StatusOpConstraints getStatusDescDetails(String statusDesc){
		StatusOpConstraints statusObj = null;
		try {	
			ArrayList statusListArray = ServiceProDBOperations.readAllStatusDataFromDB(this);
			ServiceProConstants.showLog("statusListArray size : "+statusListArray.size());
			if(statusListArray.size() != 0){				
				for(int i = 0; i < statusListArray.size(); i++){
					statusObj = ((StatusOpConstraints)statusListArray.get(i));
			        if(statusObj != null){
			        	String statusDescVal = statusObj.getTXT30().toString().trim();
			        	if(statusDescVal.equalsIgnoreCase(statusDesc)){
			        		return statusObj;
			        	}
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for getStatusDescDetails!");
        		return statusObj;
			}  
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in getStatusDescDetails : "+e.toString());
    		return statusObj;
		}
		return statusObj;
	}//fn getStatusDescDetails
		
	/*@Override
	   public boolean onTouchEvent(MotionEvent ev) {
	      SGD.onTouchEvent(ev);
	      return true;
	   }

	   private class ScaleListener extends ScaleGestureDetector.
	   SimpleOnScaleGestureListener {
	   @Override
	   public boolean onScale(ScaleGestureDetector detector) {
	      scale *= detector.getScaleFactor();
	      scale = Math.max(0.1f, Math.min(scale, 5.0f));
	      matrix.setScale(scale, scale);
	      priorityVal.setImageMatrix(matrix);
	      return true;
	   }
	}*/
	   
	public boolean checkAcptStatus(String statusDesc){
		boolean check = false;
		StatusOpConstraints statusObj = null;
		try {	
			ArrayList statusListArray = ServiceProDBOperations.readAllStatusDataFromDB(this);
			if(statusListArray.size() != 0){				
				for(int i = 0; i < statusListArray.size(); i++){
					statusObj = ((StatusOpConstraints)statusListArray.get(i));
			        if(statusObj != null){
			        	String statusDescVal = statusObj.getTXT30().toString().trim();
			        	String statusPSAction = statusObj.getZZStatusPostSetAction().toString().trim();
						ServiceProConstants.showLog(" statusDescVal "+statusDescVal);
						ServiceProConstants.showLog(" statusDesc "+statusDesc);
			        	if(statusDescVal.equalsIgnoreCase(statusDesc)){
			        		String str = ServiceProConstants.TASK_STATUS_POST_ACTION_ARJR;
	            			CharSequence cs = str.toLowerCase();
	            			ServiceProConstants.showLog("t/f : "+statusPSAction.contains(cs));			            			
	            			if(statusPSAction.contains(cs)){
				        		return true;
	            			}	            			
			        	}
			        }
			    }
			}else{
        		return check;
			}  
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in checkAcptStatus : "+e.toString());
    		return check;
		}
		return check;
	}//fn getStatusDescDetails
		
	private String[] getChoicesArray(){
        String choices[] = null;
        String statusDesc = "", statusVal = "";
        try{
        	ArrayList statusFollowList = new ArrayList();
            ServiceProConstants.showLog("statusValue: "+statusValue);
        	statusFollowList = ServiceProDBOperations.readCorrespondingFollowStatusListDataFromDB(this, statusValue);
        	
        	if(statusFollowList != null && statusFollowList.size() > 0){
        		for(int s = 0; s < statusFollowList.size(); s++){ 
        			statusVal = statusFollowList.get(s).toString().trim();
        			if(statusVal != null && statusVal.length() > 0)
        				statusDesc = ServiceProDBOperations.readStatusDescValueFromDB(this, statusVal);
        			if(statusDesc != null && statusDesc.length() > 0)
        				statusArray.add(statusDesc);
        		}        		
        	}
        	
        	String sts = statusDescValue.toString().trim();
            boolean statusCheck = statusArray.contains(sts);
            ServiceProConstants.showLog("statusCheck: "+statusCheck);
            if(!statusCheck){
            	statusArray.add(sts);
            }
        	ServiceProConstants.showLog("Size of choices : "+statusArray.size());
            if(statusArray != null){                
                choices = new String[statusArray.size()];                
                for(int h=0; h<statusArray.size(); h++){                    
                    choices[h] = statusArray.get(h).toString().trim();
                }
            }            
        }
        catch(Exception sfg){
        	ServiceProConstants.showErrorLog("Error in getChoicesArray : "+sfg.toString());
        }
        ServiceProConstants.showLog("Size of choices 2: "+choices.length);
        return choices;
    }//fn getChoicesArray
	
	//attachment btn click listener
    private OnClickListener attaBtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	File f = new File(imgCapPath);
			    if(f.exists()){
			    	Intent intent = new Intent(ServiceProTaskEditScreen.this, ImageDislayActivity.class);
			    	//if 1 is Capture Image, 2 is Signature Image
			    	intent.putExtra("image_display", 1);
			    	intent.putExtra("objectId", category.getObjectId().toString().trim());
					startActivityForResult(intent, ServiceProConstants.CAPTURED_IMAGE_DIS_SCREEN);
			    }
			    else{
			    	ServiceProConstants.showErrorDialog(ServiceProTaskEditScreen.this, getString(R.string.SERVICEORD_MAIN_ACTSCR_ATTACHMENT_ALERT));
			    }
	        }
	        catch(Exception sfg){
	        	ServiceProConstants.showErrorLog("Error in attaBtnListener : "+sfg.toString());
	        }
        }
    };

	//sap attachment btn click listener
    private OnClickListener sapAttaBtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		ServiceProConstants.showLog("sapAttaBtnListener clicked ");
        		ServiceProConstants.showLog("attachList size: "+attachList.size()); 
        		Intent intent = new Intent(ServiceProTaskEditScreen.this, ServiceProSAPAttachmentList.class);
            	intent.putExtra("attachObj", attachList);
    			startActivityForResult(intent, ServiceProConstants.ATTACHMENT_LIST_SCREEN);
	        }
	        catch(Exception sfg){
	        	ServiceProConstants.showErrorLog("Error in sapAttaBtnListener : "+sfg.toString());
	        }
        }
    };    
    
    //Signatureattachment btn click listener
    private OnClickListener signAttsBtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		signatureReviewAction();
	        }
	        catch(Exception sfg){
	        	ServiceProConstants.showErrorLog("Error in attaBtnListener : "+sfg.toString());
	        }
        }
    };
    
    private void signatureReviewAction(){
    	try{              	
        	File f = new File(signPath);
		    if(f.exists()){
		    	Intent intent = new Intent(ServiceProTaskEditScreen.this, ImageDislayActivity.class);
		    	//if 1 is Capture Image, 2 is Signature Image
		    	intent.putExtra("image_display", 2);
		    	intent.putExtra("objectId", category.getObjectId().toString().trim());
				startActivityForResult(intent, ServiceProConstants.CAPTURED_IMAGE_DIS_SCREEN);
		    }
		    else{
		    	ServiceProConstants.showErrorDialog(ServiceProTaskEditScreen.this, getString(R.string.SERVICEORD_MAIN_ACTSCR_ATTACHMENT_SIGN_ALERT));
		    }
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On signatureAction : "+asf.toString());
        }
    }//fn signatureReviewAction
    
	private void showAcceptedAlertBox(){
		try{
			alertDialog = new AlertDialog.Builder(this)
			.setTitle("Estimated arrival at customer:")
			.setPositiveButton(getETA_DATE_STR()+" "+getETA_TIME_STR(), new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					showDialog(ETA_TIME_DIALOG_ID);
					showDialog(ETA_DATE_DIALOG_ID);
				}
				})	
			.setNeutralButton("Done", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
				})
			.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					resetStatus();
				}
				})
				.create();
				alertDialog.show();
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in showAcceptedAlertBox:"+de.toString());
	    }
	}//fn showAcceptedAlertBox
	
	private void resetStatus(){
		try{            
            String status = ServiceProConstants.INITIAL_STATUS_VALUE;			
            statusSpn.setSelection(spinnerArrayAdapter.getPosition(status));

			ServiceProConstants.showErrorDialog(this, "Status change was reset.");
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in resetStatus:"+de.toString());
	    }
	}//fn resetStatus
	
	public void setStatusReason(){
		try{
			if(taskDbObj == null)
				taskDbObj = new ServiceProTaskDetailsPersistent(this);
	    	String statusReason = taskDbObj.getStatusReason(category.getObjectId());
	    	if(statusReason.equals(ServiceProConstants.TASK_STATUS_REASON_ONLEAVE_STR)){
	    		rejectSpn.setSelection(ServiceProConstants.TASK_STATUS_REASON_ONLEAVE);
			}
			else if(statusReason.equals(ServiceProConstants.TASK_STATUS_REASON_SICK_STR)){
				rejectSpn.setSelection(ServiceProConstants.TASK_STATUS_REASON_SICK);
			}
			else if(statusReason.equals(ServiceProConstants.TASK_STATUS_REASON_ENGGANOTJOB_STR)){
				rejectSpn.setSelection(ServiceProConstants.TASK_STATUS_REASON_ENGGANOTJOB);
			}
			else if(statusReason.equals(ServiceProConstants.TASK_STATUS_REASON_TRAIN_STR)){
				rejectSpn.setSelection(ServiceProConstants.TASK_STATUS_REASON_TRAIN);
			}	   
			else if(statusReason.equals(ServiceProConstants.TASK_STATUS_REASON_TRAVEL_STR)){
				rejectSpn.setSelection(ServiceProConstants.TASK_STATUS_REASON_TRAVEL);
			}
	        else{
	        	rejectSpn.setSelection(ServiceProConstants.TASK_STATUS_REASON_OTHER);
	    		rejectresET.setEnabled(true);
	    		rejectresET.setText(statusReason);
	        }	    		
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in setStatusReason:"+de.toString());
	    }
	}//fn setStatusReason
	
	private String[] getTimeZoneArray(){
		String choices[] = null;
        try{
        	int tzoffset = Integer.parseInt(category.getTimeZone());
        	String[] timezones = TimeZone.getAvailableIDs(tzoffset);
        	if(timezones.length != 0){
	        	ServiceProConstants.tz_str = new String[timezones.length];
	        	choices = new String[timezones.length];
				for (int i=0;i<timezones.length;i++) {  
					TimeZone tz = TimeZone.getTimeZone(timezones[i]);
					choices[i] = tz.getID();
				}
				ServiceProConstants.tz_str = choices;
        	}
        }
        catch(Exception sfg){
        	ServiceProConstants.showErrorLog("Error in getTimeZoneArray : "+sfg.toString());
        }   
       	return choices;
	}//fn getTimeZoneArray
	
	private OnClickListener ddviewimglistener = new OnClickListener(){
		public void onClick(View v) {
			if(internetAccess)
				gotoDrivingDirection();
			else{
				displayDialog();
			}	
        }
    };
	
	private OnClickListener call1_bt1Listener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	if(cus1phTV.getText().toString().trim().length() != 0)
			{
				try{
					String telno = cus1phTV.getText().toString().trim();
					int fstIndex = telno.indexOf("+");
					if(fstIndex >= 0)
						telno = telno.replace("+","");
					startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telno)));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
        }
    };
    
    private OnClickListener call1_bt2Listener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	if(cus1ph2TV.getText().toString().trim().length() != 0)
			{
				try{
					String telno2 = cus1ph2TV.getText().toString().trim();
					int fstIndex = telno2.indexOf("+");
					if(fstIndex >= 0)
						telno2 = telno2.replace("+","");
					startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telno2)));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
        }
    };
    
    private OnClickListener call2_bt1Listener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	if(cus2phTV.getText().toString().trim().length() != 0)
			{
				try{
					String telno = cus2phTV.getText().toString().trim();
					int fstIndex = telno.indexOf("+");
					if(fstIndex >= 0)
						telno = telno.replace("+","");
					startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telno)));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
        }
    };
    
    private OnClickListener call2_bt2Listener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	if(cus2ph2TV.getText().toString().trim().length() != 0)
			{
				try{
					String telno2 = cus2ph2TV.getText().toString().trim();
					int fstIndex = telno2.indexOf("+");
					if(fstIndex >= 0)
						telno2 = telno2.replace("+","");
					startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telno2)));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
        }
    };
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ID, 0, "Service Confirmation");
		menu.add(0, MENU_MAP, 0, "Map Customer");
		menu.add(0, MENU_MAP_DIREC, 0, "Driving Direction");
		menu.add(0, MENU_COLLEAGUE, 0, "Transfer Task");
		menu.add(0, MENU_ENTITLEMENTS, 0, "Entitlements");
		menu.add(0, MENU_ADD_CAPTURE, 0, "Capture Image");		
		menu.add(0, MENU_ADD_SIGNATURE, 0, "Add Signature");
		menu.add(0, MENU_CONVERT_PDF, 0, "Convert to PDF");
		
	    return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		  super.onPrepareOptionsMenu(menu);
		  return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ID: {
				gotoServiceConfirmation();
		        break;
			}
			case MENU_MAP: {
				if(internetAccess)
					gotoMapCustomer();
				else{
					displayDialog();
				}				
		        break;
			}	
			case MENU_MAP_DIREC: {
				if(internetAccess)
					gotoDrivingDirection();
				else{
					displayDialog();
				}				
		        break;
			}
			case MENU_COLLEAGUE: {
				gotoColleagueList();
		        break;
			}
			case MENU_ENTITLEMENTS: {
				showEntitlementScreen();
		        break;
			}
			case MENU_ADD_CAPTURE: {
				captureAction(); 
		        break;
			}
			case MENU_ADD_SIGNATURE: {
				signatureAction();
		        break;
			}
			case MENU_CONVERT_PDF: {
				GeneratePdf();
		        break;
			}
	    }
		return super.onOptionsItemSelected(item);
	}
			
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void GeneratePdf(){		
		// Create a shiny new (but blank) PDF document in memory
				PdfDocument document = new PdfDocument();				
				// crate a page description				
				//PageInfo pageInfo = new PageInfo.Builder(500, height, 1).create();
		
				// create a new page from the PageInfo
				//Page page = document.startPage(pageInfo);					          
	            partnerStr =category.getPartner().trim();
	            zzprodDcestr = category.getZzFirstServiceProductDescr().toString().trim();
	            
	          
	           int width = scrolv.getWidth();
	            
	            scrolv.setDrawingCacheEnabled(true);
	            scrolv.buildDrawingCache();
	            bm = scrolv.getDrawingCache();
	            //RESCALING BITMAP IMAGES
	          resized = Bitmap.createScaledBitmap(bm,(int)bm.getWidth()/2, (int)bm.getHeight()/2, true);
	          scaledBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.gss);
	          scaledBitmap3= Bitmap.createScaledBitmap(scaledBitmap,(int)scaledBitmap.getWidth()/2, (int)scaledBitmap.getHeight()/2, true);
	          bitmap2=BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_scale);
	          scaledBitmap2 = Bitmap.createScaledBitmap(bitmap2,(int)bitmap2.getWidth()/2, (int)bitmap2.getHeight()/2, true);
	          int height = resized.getHeight()+scaledBitmap.getHeight()+scaledBitmap2.getHeight();
	          
	          FileInputStream in;
	          BufferedInputStream buf;
	          File myFile = new File("/sdcard/gssdoc.png");
	          //String f = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TestFolder/gssdoc.png";
	          try{
	        	  in = new FileInputStream(myFile.toString());
	              buf = new BufferedInputStream(in,1070);
	              System.out.println("1.................."+buf);
	              byte[] bMapArray= new byte[buf.available()];
	             
	              buf.read(bMapArray);
	              image = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
	              image = Bitmap.createScaledBitmap(image,(int)image.getWidth()/2, (int)image.getHeight()/2, true);
	          }catch (IOException e) {
					throw new RuntimeException("Error generating image", e);
				}
	          
              
	          PageInfo pageInfo = new PageInfo.Builder(500, 1500, 1).create();
	          Page page = document.startPage(pageInfo);		
	           // resized = Bitmap.createScaledBitmap(bm,160, 160, true);	         
	           // Canvas canv = new Canvas();
	           /* View content = findViewById(R.iAd.scrollv);	        
				content.draw(page.getCanvas());	*/
			
				/*View content = findViewById(R.id.summaryTV);
				content.draw(page.getCanvas());	
					        	
				 //canvas.drawLine(0, 0, getWidth(), getHeight(), paint);
				 
				View content2 = findViewById(R.id.idTV);
				content2.draw(page.getCanvas());*/
	           
				View content =new SampleView(this);
				content.draw(page.getCanvas());		
								
				document.finishPage(page);								
				File path2 = Environment.getExternalStorageDirectory();	
				File dir = new File(path2.toString());
				if (!dir.exists()) {
					dir.mkdir();
					Toast.makeText(getApplicationContext(), "Directory Created", 1)
							.show();
				}else{					
					Toast.makeText(getApplicationContext(), "Directory exists", 1)
					.show();
				}
				 Log.d("PDFCreator", "PDF path2: " +path2);
				
				File file = new File(dir, "mysample.pdf");				
				//ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					FileOutputStream fOut = new FileOutputStream(file);					
					document.writeTo(fOut);
					document.close();
					fOut.close();
				} catch (IOException e) {
					throw new RuntimeException("Error generating file", e);
				}

				/*Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("application/pdf");
				// Assuming it may go via eMail:
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Here is a PDF from PdfSend");
				intent.putExtra(
					getClass().getPackage().getName() + "." + "SendPDF", 
					fOut));
				startActivity(intent);*/
				
	}//				
	private static class SampleView extends View {
		int x = 10, y = 380;
		int xt = 10, yt= 400;
		int x2 = 10,y2 = 520;
		// CONSTRUCTOR
		public SampleView(Context context) {
			super(context);
			setFocusable(true);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			Paint paint = new Paint();
 
            paint.setColor(Color.BLACK);
           /* Rect bounds = new Rect();
            String nulstr = " ";*/
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(9);
            //DATA
          /*  ArrayList<String> ids = new ArrayList<String>();
            String id11 = notesTV.getText().toString();			
			ids.add(id11);
			
			 String id15 = zzprodDcestr;			
		     ids.add(id15);
			
		    String id14s ="                                          ";			
		    ids.add(id14s);
					
	         String id14 = "\nCustomer#  "+partnerStr;			
	         ids.add(id14);
				
			
			String id13s ="                                          ";			
			ids.add(id13s);
			
            String id13 = "Other Details";			
			ids.add(id13);
			
			String id12s ="                                          ";			
			ids.add(id12s); 
			
            String id10 = "Alt.Tel No "+"       "+cus1ph2TV.getText().toString();			
			ids.add(id10);
			
			String id11s ="                                          ";			
			ids.add(id11s);
			
            String id9 = "Tel No "+"       "+cus1phTV.getText().toString();			
			ids.add(id9);
			
			String id10s ="                                          ";			
			ids.add(id10s);
			
            String id8 = "Contact Name "+"       "+cus1TV.getText().toString();			
			ids.add(id8);
			
			String id9s ="                                          ";			
			ids.add(id9s);
			
		    String id12 = descTV.getText().toString();			
			ids.add(id12);
				
			String id8s ="                                          ";			
			ids.add(id8s);
			
            String id7 = "Service Description ";			
			ids.add(id7);
			
			String id7s ="                                          ";			
			ids.add(id7s);
			
            String id6 = "Estimated Arrival Time "+"       "+etaTimeTV.getText().toString();			
			ids.add(id6);
			
			String id6s ="                                          ";			
			ids.add(id6s);
			
            String id5 = "Start Date "+"       "+dateTV.getText().toString();			
			ids.add(id5);
			
			String id4s ="                                          ";			
			ids.add(id4s);
			
            String id4 = "Status "+"       "+ServiceProTaskEditScreen.sel_status;			
			ids.add(id4);
			
			String id3s ="                                          ";			
			ids.add(id3s);
			
			String id3 = "Priority "+"       "+priorityVal.getText().toString();			
			ids.add(id3);
			String id1s ="                                          ";			
			ids.add(id1s);
			
			String id2 = "Service Doc."+"      "+idTV.getText().toString();			
			ids.add(id2);
			
			String id2s ="                                          ";			
			ids.add(id2s);
			
			String id1 = summaryTV.getText().toString();
			String idsub = id1.substring(28, 39);
			ids.add(idsub);	
			id1 = id1.substring(0, 28);
			ids.add(id1);		
		for(int i=0;i<ids.size();i++){			
			String str= ids.get(i).toString();				
			canvas.drawText(str,x,y,paint);				
			 y+=paint.ascent()+paint.descent();			
		}*/	                      
            canvas.drawBitmap(resized, 0, 0, paint);  
            canvas.drawText("Attachments", x, y, paint);
            canvas.drawBitmap(scaledBitmap, xt, yt, paint);  
            canvas.drawBitmap(scaledBitmap2, x2, y2, paint);  
            canvas.drawBitmap(image, 10, 600, paint);  
            
		 /* Bitmap b=BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		  paint.setColor(Color.RED);
	      canvas.drawBitmap(b, 80, 0, paint);*/
		}
	}
	@SuppressLint("NewApi")
	private void drawPage(PdfDocument.Page page) {
	    Canvas canvas = page.getCanvas();

	    // units are in points (1/72 of an inch)
	    int titleBaseLine = 72;
	    int leftMargin = 54;

	    Paint paint = new Paint();
	    paint.setColor(Color.BLACK);
	    paint.setTextSize(36);
	    canvas.drawText("Test Title", leftMargin, titleBaseLine, paint);

	    paint.setTextSize(11);
	    canvas.drawText("Test paragraph", leftMargin, titleBaseLine + 25, paint);

	    paint.setColor(Color.BLUE);
	    canvas.drawRect(100, 100, 172, 172, paint);
	}		
	
	private void captureAction(){
        try{        	    	
			File file = new File( path );
		    Uri outputFileUri = Uri.fromFile( file );
		    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
		    intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
		    startActivityForResult( intent, ServiceProConstants.IMAGE_CAPTURE_SCREEN );
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On captureAction : "+asf.toString());
        }
    }//fn captureAction
	
	private void signatureAction(){
        try{      
        	Intent intent = new Intent(ServiceProTaskEditScreen.this, FingerPaint.class);
        	intent.putExtra("objectId", category.getObjectId().toString().trim());
			startActivityForResult(intent, ServiceProConstants.SIGNATURE_DIS_SCREEN);
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On signatureAction : "+asf.toString());
        }
    }//fn signatureAction
	
	private void displayDialog(){
		try{
			ServiceProConstants.showErrorDialog(this, getResources().getString(R.string.SERVICEPRO_ERROR_MSG_OFFLINE));
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in displayDialog:"+e.getMessage());
		}
	}//fn displayDialog
	
	private void showEntitlementScreen(){
		try {
            if(category != null){
            	String servOrdNoStr = category.getObjectId().toString().trim();
            	String processTypeStr = category.getProcessType().toString().trim();
            	ServiceProConstants.showLog(servOrdNoStr+" : "+processTypeStr);
            	if((!servOrdNoStr.equalsIgnoreCase("")) && (!processTypeStr.equalsIgnoreCase(""))){
	            	Intent intent = new Intent(this, ServiceProEntitlementScreen.class);
	            	intent.putExtra("servOrdNoStr", servOrdNoStr);
	            	intent.putExtra("processTypeStr", processTypeStr);
	    			startActivityForResult(intent, ServiceProConstants.ENTILEMENT_SCREEN);
            	}
            	else
            		ServiceProConstants.showErrorDialog(this, "ServiceOrder No and ProcessType are not available");
            }
            else
            	ServiceProConstants.showErrorDialog(this, "ServiceOrder No is not available");			
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog("Error on showEntitlementScreen : "+e.getMessage());
		}
	}//fn showEntitlementScreen
	
	private void gotoServiceConfirmation(){
		int index = 0;
		try {
            boolean errFlag = false;
            if(category != null){
            	Intent intent = new Intent(this, ServiceProConfirmationScreen.class);
            	intent.putExtra("conftaskobj", category);         	
    			startActivityForResult(intent, ServiceProConstants.CNFM_MAIN_SCREEN);
            }
            else
                errFlag = true;				
                
            if(errFlag == true)
            	ServiceProConstants.showErrorDialog(this, getString(R.string.SERVICEORD_CNFSCR_SELTSK));			
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn gotoServiceConfirmation
	
	private void gotoMapCustomer(){
		int index = 0;
		try {			
            boolean errFlag = false;
            if(category != null){
            	String data = "";
            	if(category.getCity().toString().length() > 0){
            		data += category.getCity().toString();
            	}  
            	if(category.getRegion().toString().length() > 0){
            		data += ","+category.getRegion().toString();
            	}
            	if(category.getPostalCode1().toString().length() > 0){
            		data += ","+category.getPostalCode1().toString();
            	}
            	if(category.getCountryIso().toString().length() > 0){
            		data += ","+category.getCountryIso().toString();
            	}            		
            	Intent intent = new Intent(this, ServiceProShowMap.class);
            	intent.putExtra("mapAddress", data);
            	startActivity(intent);
            }
            else
                errFlag = true;				
                
            if(errFlag == true)
            	ServiceProConstants.showErrorDialog(this, getString(R.string.SERVICEORD_CNFSCR_SELTSK));
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn gotoServiceConfirmation
	
	private void gotoDrivingDirection(){
		try {			
			LocationResult locationResult = new LocationResult(){ 
	            @Override 
	            public void gotLocation(Location location){ 
	            	lat = ""+location.getLatitude();
	            	lon = ""+location.getLongitude();
	            	geoloc = "My current location is: " + 
	            	        "Latitud = " + location.getLatitude() + 
	            	        "Longitud = " + location.getLongitude(); 
	            	System.out.println("Text from inside:"+ geoloc);
	            	
	            	if(category.getStreet().toString().length() > 0){
	            		taskAddress += category.getStreet().toString();
	            	}
	            	if(category.getCity().toString().length() > 0){
	            		taskAddress += ","+category.getCity().toString();
	            	}
	            	if(category.getRegion().toString().length() > 0){
	            		taskAddress += ","+category.getRegion().toString();
	            	}                	
	            	if(category.getCountryIso().toString().length() > 0){
	            		taskAddress += ","+category.getCountryIso().toString();
	            	}      	                	
	            	if(category.getPostalCode1().toString().length() > 0){
	            		taskAddress += ","+category.getPostalCode1().toString();
	            	}
	            	
	            	System.out.println("taskAddress:"+ taskAddress);
	            	
	                //Uri uri =Uri.parse("http://maps.google.com/maps?&saddr="+lat+","+lon+"&daddr=ksrtc bus stand,mysore, karnataka, india");
	                Uri uri =Uri.parse("http://maps.google.com/maps?&saddr="+lat+","+lon+"&daddr="+taskAddress);
	        		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	                startActivity(Intent.createChooser(intent, "Directions"));
	            } 
	        }; 
	        MyLocation myLocation = new MyLocation(); 
	        myLocation.getLocation(this, locationResult); 
		}catch (Exception e) {
			ServiceProConstants.showErrorLog("gotoDrivingDirection: "+e.getMessage());
		}
	}//fn gotoServiceConfirmation
		
	private void gotoColleagueList(){
		int index = 0;
		try {
            if(category != null){
            	Intent intent = new Intent(this, ServiceProColleagueListScreen.class);
            	intent.putExtra("ColleagueViewOption", ServiceProConstants.VIEWCOLLEAGUELISTANDTRANS);
            	intent.putExtra("conftaskobj", category);
    			startActivityForResult(intent, ServiceProConstants.COLLEAGUE_LIST_SCREEN);
            }
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in gotoColleagueList:"+e.getMessage());
		}
	}//fn gotoColleagueList
	
	private void updateDisplayStartDate() {    	
    	int month_value = mMonth + 1;
    	String month_dec = ServiceProConstants.getMonthValue(month_value); 
    	try{
	    	mDay_dp = mDay;
	    	mMonth_dp = month_value;
	    	mYear_dp = mYear;
	    	due_date_value_cal = month_value+"/"+mDay+"/"+mYear;
	    	
	    	String taskDateStrValue = ""; 
	    	taskDateStrValue = ServiceProConstants.getDateFormatForSAP(mYear_dp, mMonth_dp, mDay_dp, mHour_dp, mMinute_dp);
	    	if(taskDateStrValue.length() > 0 && taskDateStrValue != null){
	    		String dateStr = taskDateStrValue.toString();
	        	dateStr = dateStr.trim();
	        	if(!dateStr.equalsIgnoreCase("")){	                    		
	            	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
	    			dateTV.setText(" "+newdatestr);
	        	}
	        	else{
	        		dateTV.setText(
        	            new StringBuilder()
	                    // Month is 0 based so add 1
	            		.append(mDay).append(" ")
	                    .append(month_dec).append(" ")                    
	                    .append(mYear).append(" "));
	        	}
	    	}
	    	else{
	    		taskDateStrValue = "";
	    		dateTV.setText(
		            new StringBuilder()
	                // Month is 0 based so add 1
	        		.append(mDay).append(" ")
	                .append(month_dec).append(" ")                    
	                .append(mYear).append(" "));
	    	}
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in updateDisplayStartDate"+de.toString());
	    }    	
    }//fn updateDisplayStartDate
    
    private void updateDisplayStartTime() {
    	try{
	    	mHour_dp = mHour;
	    	mMinute_dp = mMinute;
	    	timeTV.setText(
	            new StringBuilder()
	            .append(pad(mHour)).append(":")
	            .append(pad(mMinute)));
	    }
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in updateDisplayStartTime"+de.toString());
	    }
    }//fn updateDisplayStartTime
    
    private void updateDisplayETADate() {    	
    	int monthETA_value = mMonthETA + 1;
    	String monthETA_dec = ServiceProConstants.getMonthValue(monthETA_value); 
    	try{
	    	mDayETA_dp = mDayETA;
	    	mMonthETA_dp = monthETA_value;
	    	mYearETA_dp = mYearETA;
	    	eta_date_value_cal = monthETA_value+"/"+mDayETA+"/"+mYearETA;
	    	
	    	String taskETADateStrValue = ""; 
	    	taskETADateStrValue = ServiceProConstants.getDateFormatForSAP(mYearETA_dp, mMonthETA_dp, mDayETA_dp, mHourETA_dp, mMinuteETA_dp);
	    	if(taskETADateStrValue.length() > 0 && taskETADateStrValue != null){
	    		String dateStr = taskETADateStrValue.toString();
	        	dateStr = dateStr.trim();
	        	if(!dateStr.equalsIgnoreCase("")){	                    		
	            	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
	    			etaDateTV.setText(" "+newdatestr);
	    			setETA_DATE_STR(newdatestr);
	        	}
	        	else{
	        		etaDateTV.setText(
	    	            new StringBuilder()
	                    // Month is 0 based so add 1
	            		.append(mDayETA).append(" ")
	                    .append(monthETA_dec).append(" ")                    
	                    .append(mYearETA).append(" "));
	        		setETA_DATE_STR("" +
		    	            new StringBuilder()
		                    // Month is 0 based so add 1
		            		.append(mDayETA).append(" ")
		                    .append(monthETA_dec).append(" ")                    
		                    .append(mYearETA).append(" "));
	        	}
	    	}
	    	else{
	    		taskETADateStrValue = "";
	    		etaDateTV.setText(
    	            new StringBuilder()
                    // Month is 0 based so add 1
            		.append(mDayETA).append(" ")
                    .append(monthETA_dec).append(" ")                    
                    .append(mYearETA).append(" "));
	    		setETA_DATE_STR("" +
	    	            new StringBuilder()
	                    // Month is 0 based so add 1
	            		.append(mDayETA).append(" ")
	                    .append(monthETA_dec).append(" ")                    
	                    .append(mYearETA).append(" "));
	    	}
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in updateDisplayETADate"+de.toString());
	    }    	
    }//fn updateDisplayETADate
    
    private void updateDisplayETATime() {
    	try{
	    	mHourETA_dp = mHourETA;
	    	mMinuteETA_dp = mMinuteETA;
	    	eta_time_value_cal = mHourETA+":"+mMinuteETA;
	    	etaTimeTV.setText(
	            new StringBuilder()
	            .append(pad(mHourETA)).append(":")
	            .append(pad(mMinuteETA)));
	    	setETA_TIME_STR(""+
	            new StringBuilder()
	            .append(pad(mHourETA)).append(":")
	            .append(pad(mMinuteETA)));
	    }
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in updateDisplayETATime"+de.toString());
	    }
    }//fn updateDisplayETATime
    
	private void enableRejectSpinner(String text, int pos){
		try{
			rejectSpn.setEnabled(true);
			/*if(text.equalsIgnoreCase(getString(R.string.SERVICEPRO_TSKEDIT_DECL))){
				rejectSpn.setEnabled(true);
			}*/
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error on enableRejectSpinner : "+sfg.toString());
		}
	}//fn enableRejectSpinner
	
	private void enableRejectReason(String text, int pos){
		try{
			if(text.equals(getString(R.string.SERVICEPRO_TSKEDIT_OTHR))){
				rejectresET.setEnabled(true);				
			}
			else{
				rejectresET.setEnabled(false);
				rejectresET.setText("");
			}
		}
		catch(Exception sfgss){
			ServiceProConstants.showErrorLog("Error on enableRejectReason : "+sfgss.toString());
		}
	}//fn enableRejectReason
	
	private void insertTasksInfo(){		
		try{
			String catVal = "", taskVal = "", notesVal = "", status="", priority="",objid = "",itemnumbstr="";
			
			if(itemObj == null)
    	    	itemObj = new ServiceProTaskDetailsPersistent(this);

			if(category != null){
				status = category.getStatus();	
				String fiednote= category.getNote().trim();
				String statusDesc = category.getTxt30().toString().trim();	
				
				StatusOpConstraints statusObj2 = getStatusDescDetails(statusDesc);
				if(statusObj2 != null){
					String statusPSAction = statusObj2.getZZStatusPostSetAction().toString().toLowerCase().trim();
            		if(statusPSAction != null && statusPSAction.length() > 0){
            			String str = ServiceProConstants.TASK_STATUS_POST_ACTION_AETA;
            			CharSequence cs = str.toLowerCase();
            			ServiceProConstants.showLog("cs 2 : "+cs);
            			ServiceProConstants.showLog("t/f 2: "+statusPSAction.contains(cs));	
            			if(statusPSAction.contains(cs)){
            				etalinear.setVisibility(View.VISIBLE);	            				
            			}else{
            				etalinear.setVisibility(View.GONE);	            				
            			}			            			
            		}           			
            	}
					
	            statusSpn.setSelection(spinnerArrayAdapter.getPosition(statusDesc));
								
				fieldnotesET = (EditText) findViewById(R.id.fieldnotesET);
				fieldnotesET.setEnabled(true);				
				fieldnotesET.setText(fiednote);
				fieldnotesET.setGravity(Gravity.TOP | Gravity.LEFT);
				fieldnotesET.addTextChangedListener(this); 
				
				priority = category.getPriority();
				if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_HIGH_STR)){
					priorityVal.setText(ServiceProConstants.TASK_PRIORITY_HIGH_VAL_STR);
				}
				else if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_NORMAL_STR)){
					priorityVal.setText(ServiceProConstants.TASK_PRIORITY_NORMAL_VAL_STR);
				}				           
	            else{
	            	priorityVal.setText(ServiceProConstants.TASK_PRIORITY_LOW_VAL_STR);
	            }
				
				String dueDate = category.getZZKEeyDate();
				initialDueDate = category.getZZKEeyDate();
                if(dueDate.length() != 0){
	                String startdate = dueDate;
	                int stdat1 = startdate.indexOf(" ");
	                int stdat2 = startdate.lastIndexOf(":");
	                String start_date_value = "";
	                String start_time_value = "";
	                if(stdat1 != -1){
	                	start_date_value = startdate.substring(0, stdat1);
	                	String[] st_date_value = start_date_value.split("-");
	                	start_date_value = Integer.parseInt(st_date_value[2])+"-"+ServiceProConstants.getMonthValue(Integer.parseInt(st_date_value[1]))+"-"+Integer.parseInt(st_date_value[0]);
	                	mYear_dp = Integer.parseInt(st_date_value[0]);
						mMonth_dp = Integer.parseInt(st_date_value[1]);
						mDay_dp = Integer.parseInt(st_date_value[2]);
	                }	                
	                else{
	                	start_date_value = dueDate;
	                	String[] st_date_value = start_date_value.split("-");
	                	start_date_value = Integer.parseInt(st_date_value[2])+"-"+ServiceProConstants.getMonthValue(Integer.parseInt(st_date_value[1]))+"-"+Integer.parseInt(st_date_value[0]);
	                	mYear_dp = Integer.parseInt(st_date_value[0]);
						mMonth_dp = Integer.parseInt(st_date_value[1]);
						mDay_dp = Integer.parseInt(st_date_value[2]);
	                }
	                
	                if(stdat1 != -1 && stdat2 != -1){
	                	start_time_value = startdate.substring(stdat1+1, stdat2);
	                	String[] st_time_value = start_time_value.split(":");
	                	mHour_dp = Integer.parseInt(st_time_value[0]);
	                	mMinute_dp = Integer.parseInt(st_time_value[1]);
	                }
	                else{
	                	start_time_value = "00:00";
	                	mHour_dp = Integer.parseInt("00");
		                mMinute_dp = Integer.parseInt("00");
	                }
	                
	                String stdate_sav = dueDate;
	                int stdat1_sav = stdate_sav.indexOf(" ");
	                int stdat2_sav = stdate_sav.lastIndexOf(":");	             
	                if(stdat1_sav != -1){
	                	 String start_date_sav_value = stdate_sav.substring(0, stdat1_sav);
	                	 String[] st_date_value = start_date_sav_value.split("-");
	                	 mYear_dp = Integer.parseInt(st_date_value[0]);
						 mMonth_dp = Integer.parseInt(st_date_value[1]);
						 mDay_dp = Integer.parseInt(st_date_value[2]);
	                }
	                if(stdat2_sav != -1){
	                	String start_time_sav_value = stdate_sav.substring(stdat1_sav+1, stdat2_sav);	                	          
		                String[] st_time_value = start_time_sav_value.split(":");
		                mHour_dp = Integer.parseInt(st_time_value[0]);
		                mMinute_dp = Integer.parseInt(st_time_value[1]);
	                }
	                start_date_value = start_date_value.replace("-", " ");
	                
	                String dateStr = category.getZZKEeyDate().toString();
                	dateStr = dateStr.trim();
                	if(!dateStr.equalsIgnoreCase("")){	                    		
                    	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
            			dateTV.setText(" "+newdatestr);
                	}	                
	                timeTV.setText(start_time_value);
	                            	
	        	    due_date_value_cal = mMonth_dp+"/"+mDay_dp+"/"+mYear_dp;
                }
                
                String etaDate = category.getZzetaDate();
                String etaTime = category.getZzetaTime();
                initialETADate = category.getZzetaDate();
                initialETATime = category.getZzetaTime();

            	ServiceProConstants.showLog("initialETADate before:"+initialETADate);
            	ServiceProConstants.showLog("initialETATime before:"+initialETATime);
            	
				try{
	                if(etaDate != null && etaDate.length() > 0){
	                	if(!etaDate.equals("0000-00-00")){
	                		setETADate(etaDate, etaTime);                		
	                	}
	                	else{
	                		setETADate(category.getZZKEeyDate(), "00:00:00");
	                	}	                
	                }else{
	                	setETADate(category.getZZKEeyDate(), "00:00:00");
	                }
				}
				catch(Exception asd){
					ServiceProConstants.showErrorLog("Error in display ETA Date & Time : "+asd.toString());
				}
				              
				itemDesclinear  = (LinearLayout) findViewById(R.id.itemDesclinear);
                String descVal =  category.getDescription().toString().trim();
                if(descVal.length() != 0 && !descVal.equalsIgnoreCase("")){
                	itemDesclinear.setVisibility(View.VISIBLE);
                	descTV = (TextView) findViewById(R.id.descTV);
                	descTV.setText("Service Description \n"+descVal);        			
            	}else{
            		itemDesclinear.setVisibility(View.GONE);
            	} 
                                
                serordtyplinear  = (LinearLayout) findViewById(R.id.serordtyplinear);
                String serOrdTypeDescVal =  category.getProcessTypeDescr().toString().trim();
                if(serOrdTypeDescVal.length() != 0 && !serOrdTypeDescVal.equalsIgnoreCase("")){
                	serordtyplinear.setVisibility(View.VISIBLE);
                	serOrdTypeTV = (TextView) findViewById(R.id.serOrdTypeTV);
                	serOrdTypeTV.setText(serOrdTypeDescVal);        			
            	}else{
            		serordtyplinear.setVisibility(View.GONE);
            	}                 
				
                if(category.getNameOrg1().toString().length() > 0){
                	taskVal += category.getNameOrg1().toString();
            	}
            	if(category.getNameOrg2().toString().length() > 0){
            		taskVal += " "+category.getNameOrg2().toString();
            	}
            	if(category.getStreet().toString().length() > 0){
            		taskVal += "\n"+category.getStreet().toString();
            	}
            	if(category.getCity().toString().length() > 0){
            		taskVal += "\n"+category.getCity().toString();
            	}
            	if(category.getRegion().toString().length() > 0){
            		taskVal += ","+category.getRegion().toString();
            	}                	                	
            	if(category.getPostalCode1().toString().length() > 0){
            		taskVal += ","+category.getPostalCode1().toString();
            	}
            	if(category.getCountryIso().toString().length() > 0){
            		taskVal += ","+category.getCountryIso().toString();
            	}      	
                catVal = category.getObjectId().trim();
                
                ddviewimg = (ImageView) findViewById(R.id.ddviewimg);
                if(taskVal != null && taskVal.length() > 0){
                	ddviewimg.setVisibility(View.VISIBLE);
	  			  	ddviewimg.setOnClickListener(ddviewimglistener);
                }else{
                	ddviewimg.setVisibility(View.GONE);
                }
                
                processTypeStr = category.getProcessType().toString().trim();
                
                notesVal = "Other Details ";
                notesVal += "\nCustomer# "+category.getPartner().trim();   
                notesVal += "\n\n";      
                notesVal += "Item# "+categoryDocobj.getNumberExt().toString().trim()+"\n"+category.getZzFirstServiceProductDescr().toString().trim();
                
                String numbextn = categoryDocobj.getNumberExt().toString().trim();
                Log.e("info","numbextn value: "+numbextn);
                
                if(category.getNote().toString().trim() != null && category.getNote().toString().trim().length() > 0){
                	notesVal += "\n\n";                
                    notesVal += "Service Note ";
                	notesVal += "\n"+category.getNote().toString().trim();
                }
                
               	if((!category.getCp1Name().equalsIgnoreCase("")) || !category.getCp1TelNo().equalsIgnoreCase("") || !category.getCp1TelNo2().equalsIgnoreCase("")){
                   
                	cus1ll  = (LinearLayout) findViewById(R.id.cus1linear);
                	if(category.getCp1Name().toString().trim().length() != 0){
                		cus1ll.setVisibility(View.VISIBLE);
                		cus1TV = (TextView) findViewById(R.id.cus1TV);
                		cus1TV.setText(category.getCp1Name()); 
                	} else{
                		cus1ll.setVisibility(View.GONE);
                	}             	
                	
                	cus1ph1ll  = (LinearLayout) findViewById(R.id.cus1phlinear);
                	if(category.getCp1TelNo().toString().trim().length() != 0){
                		cus1ph1ll.setVisibility(View.VISIBLE);
	        			cus1phTV = (TextView) findViewById(R.id.cus1phTV);
	        			cus1phTV.setText(category.getCp1TelNo());
	        			call1_bt1 = (ImageButton) findViewById(R.id.cus1callbtn);
	        			call1_bt1.setOnClickListener(call1_bt1Listener); 
                	}else{
                		cus1ph1ll.setVisibility(View.GONE);
                	}   
        			
                	cus1ph2ll  = (LinearLayout) findViewById(R.id.cus1ph2linear);
                	if(category.getCp1TelNo2().toString().trim().length() != 0){
                		cus1ph2ll.setVisibility(View.VISIBLE);
	        			cus1ph2TV = (TextView) findViewById(R.id.cus1ph2TV);
	        			cus1ph2TV.setText(category.getCp1TelNo2());
	        			call1_bt2 = (ImageButton) findViewById(R.id.cus1call2btn);
	        			call1_bt2.setOnClickListener(call1_bt2Listener); 	        			
                	}else{
                		cus1ph2ll.setVisibility(View.GONE);
                	}                   	
                }
                
                if(!category.getCp2Name().equalsIgnoreCase("") || !category.getCp2TelNo().equalsIgnoreCase("") || !category.getCp2TelNo2().equalsIgnoreCase("")){
                    cus2ll  = (LinearLayout) findViewById(R.id.cus2linear);
                	if(category.getCp2Name().toString().trim().length() != 0){
                		cus2ll.setVisibility(View.VISIBLE);
                		cus2TV = (TextView) findViewById(R.id.cus2TV);
                		cus2TV.setText(category.getCp2Name()); 
                	}else{
                		cus2ll.setVisibility(View.GONE);
                	}                   	
                	
                	cus2ph1ll  = (LinearLayout) findViewById(R.id.cus2phlinear);
                	if(category.getCp2TelNo().toString().trim().length() != 0){
                		cus2ph1ll.setVisibility(View.VISIBLE);
	        			cus2phTV = (TextView) findViewById(R.id.cus2phTV);
	        			cus2phTV.setText(category.getCp2TelNo());
	        			call2_bt1 = (ImageButton) findViewById(R.id.cus2callbtn);
	        			call2_bt1.setOnClickListener(call2_bt1Listener); 
                	}else{
                		cus2ph1ll.setVisibility(View.GONE);
                	}  
        			
                	cus2ph2ll  = (LinearLayout) findViewById(R.id.cus2ph2linear);
                	if(category.getCp2TelNo2().toString().trim().length() != 0){
                		cus2ph2ll.setVisibility(View.VISIBLE);
	        			cus2ph2TV = (TextView) findViewById(R.id.cus2ph2TV);
	        			cus2ph2TV.setText(category.getCp2TelNo2());
	        			call2_bt2 = (ImageButton) findViewById(R.id.cus2call2btn);
	        			call2_bt2.setOnClickListener(call2_bt2Listener); 	        			
                	}else{
                		cus2ph2ll.setVisibility(View.GONE);
                	}   
                }
                
                productlinear  = (LinearLayout) findViewById(R.id.productlinear);
                String productDescVal = category.getRefObj_Product_Descr().toString().trim();
                if(productDescVal.length() != 0){
                	productlinear.setVisibility(View.VISIBLE);
                	productdescVal = (TextView) findViewById(R.id.productdescVal);
                	productdescVal.setText(productDescVal);
            	}else{
            		productlinear.setVisibility(View.GONE);
            	}
                
                seriallinear  = (LinearLayout) findViewById(R.id.seriallinear);
                String serialVal = category.getSerialNumber().toString().trim();
                if(serialVal.length() != 0){
                	seriallinear.setVisibility(View.VISIBLE);
                	serialTV = (TextView) findViewById(R.id.serialTV);
                	serialTV.setText(serialVal);        			
            	}else{
            		seriallinear.setVisibility(View.GONE);
            	}               
                
                intbaselinear  = (LinearLayout) findViewById(R.id.intbaselinear);
                String intbaseDescVal =  category.getIB_Descr().toString().trim();
                if(intbaseDescVal.length() != 0){
                	intbaselinear.setVisibility(View.VISIBLE);
                	intbaseDescTV = (TextView) findViewById(R.id.intbaseDescTV);
                	intbaseDescTV.setText(intbaseDescVal);        			
            	}else{
            		intbaselinear.setVisibility(View.GONE);
            	}             
                
                complinear  = (LinearLayout) findViewById(R.id.complinear);
                String compDescVal =  category.getIB_Inst_Descr().toString().trim();
                if(compDescVal.length() != 0){
                	complinear.setVisibility(View.VISIBLE);
                	compDescTV = (TextView) findViewById(R.id.compDescTV);
                	compDescTV.setText(compDescVal);        			
            	}else{
            		complinear.setVisibility(View.GONE);
            	}   
                
                notesTV.setText(notesVal);
                
                idTV.setText(catVal);  
                summaryTV.setText(""+taskVal);
                ServiceProConstants.showLog("taskVal : "+taskVal);
                
                prevbtn= (ImageButton) findViewById(R.id.prevBtn);
                prevbtn.setOnClickListener(prevbtnListener); 
                
                nextbtn= (ImageButton) findViewById(R.id.nextBtn);
                nextbtn.setOnClickListener(nextbtnListener); 
                
               /* categoryDocobj = (ServiceOrdDocOpConstraints)ServiceProConstants.docvectlist.get(index);
                itemnumbstr = categoryDocobj.getNumberExt().toString().trim();
                ServiceProConstants.showErrorLog("itemnumbstr : "+itemnumbstr);*/
                
                objid =category.getObjectId().toString();
        		//itemnumbstr = category.getNumberExtn().toString().trim();
        		
        		//Log.e("info","itemnumbstr value: "+itemnumbstr);
        		Log.e("info","objid value: "+objid);
        	
        		itemList =ServiceProDBOperations.readAllItemDataForNavigation(this,objid);
        		  ServiceProConstants.showErrorLog("itemList size : "+itemList.size());
        		  String numbExtn = itemList.get(0).toString();
				 String newcatVal = catVal+"/"+numbExtn;
					idTV.setText(newcatVal);  
    			/*if(itemList.size() != 0){				
    				for(int i = 0; i < itemList.size(); i++){
    					categoryDocobj = ((ServiceOrdDocOpConstraints)itemList.get(i));
    			        conflistobj(statusObj != null){
    			        	String statusDescVal = statusObj.getTXT30().toString().trim();
    			        	if(statusDescVal.equalsIgnoreCase(statusDesc)){
    			        		return statusObj;
    			        	}
    			        }
    			    }
    			}*/
        		/*String tablenameStr = "sp_conflist_table";
				itemList = itemObj.getAllItemDataForNavigation(objid,tablenameStr);
    	    	Log.e("info","item value: "+itemList.size());*/
    	    	
                if(itemList.size()==1 || itemList.size()==0){
                	nextbtn.setVisibility(View.GONE);
                	prevbtn.setVisibility(View.GONE);
                }
                else{
                	for(int m=0;m<itemList.size();m++){   	    				
    					String numbExt = itemList.get(m).toString();   					
    					Log.e("info"," numbExt: "+numbExt); 
    					if(numbExt.equals(itemnumbstr)){
    						pos1 = itemList.indexOf(numbExt);
    						Log.e("info"," pos1: "+pos1);
    					}
    						if(pos1==0){
    							prevbtn.setVisibility(View.GONE);
    							nextbtn.setVisibility(View.VISIBLE);
    						}
    						else if(pos1==(itemList.size()-1)){
    							nextbtn.setVisibility(View.GONE);
    							prevbtn.setVisibility(View.VISIBLE);
    						}
    						else{
    							nextbtn.setVisibility(View.VISIBLE);
    							prevbtn.setVisibility(View.VISIBLE);
    						
    						}
    					}
                	}
			}
		}
		catch(Exception asd){
			ServiceProConstants.showErrorLog("Error in insertTasksInfo : "+asd.toString());
		}
	}//fn insertTasksInfo
	
	
	
	private OnClickListener prevbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		notficationflag =true;
        		String objid = "",itemnumbstr="",itemStrdb = "";
        		boolean value=false;
        		int pos=0;
        		
        		objid =categoryDocobj.getObjectId().toString();
        		itemnumbstr = categoryDocobj.getNumberExt().toString().trim();
        		Log.e("info","itemnumbstr value: "+itemnumbstr);
        		Log.e("info","objid value: "+objid);
        		
    	    	if(itemList.size() > 0){	
    	    		Collections.sort(itemList);	
    	    		value = itemList.contains(itemnumbstr);  
    	    		Log.e("info"," boolean  value: "+value);
    	    		if(value){      		
    	    			for(int i=0;i<itemList.size();i++){   	    				
    	    					String numbExt = itemList.get(i).toString();
    	    					Log.e("info"," numbExt: "+numbExt); 
    	    					if(numbExt.equals(itemnumbstr)){
    	    					pos = itemList.indexOf(numbExt);
    	    					pos--;
    	    					   	    						    	    					
    	    					itemStrdb = itemList.get(pos).toString();
    	    					Log.e("info"," itemStrdb: "+itemStrdb); 
    	    					
    	    					if(pos<=0){
    	    						Log.e("info","NO More Data ");
    	    						prevbtn.setVisibility(View.GONE);
    	    					}
    	    				}   
    	    					
    	    			}
    	    		 
    	    			//itemdbList = itemObj.getAllSelectdItemData(objid, itemStrdb);
    	    			Log.e("info"," itemdbList sizev: "+itemdbList.size()); 
    	    			
    	    			if(categorydb!=null)
	    					categorydb = null;
    	    			for(int k=0;k<itemdbList.size();k++){    	    				
    	    				categorydb= ((ServiceProOutputConstraints)itemdbList.get(k));
    	    			}    	    			
    	    			showEditscreen(categoryDocobj);    	    			
    	    		} 
    	    	
    	    	} 		
    	    	
        	}
        	catch(Exception e){
        		ServiceProConstants.showErrorLog("Error in prevbtnListener"+e.toString());
        	}
        }
    };
    
    private OnClickListener nextbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		notficationflag =true;
        		String objid = "",itemnumbstr="",itemStrdb = "";
        		boolean value=false;
        		//int pos=0;
        		//categoryDocobj= (ServiceOrdDocOpConstraints)itemList.get(pos);
        		objid =categoryDocobj.getObjectId().toString();
        		
        		//itemnumbstr = categoryDocobj.getNumberExt().toString().trim();        	      		
    	    	if(itemList.size() > 0){	
    	    		Collections.sort(itemList);	
    	    		//value = itemList.contains(itemnumbstr);  
    	    		//Log.e("info"," boolean  value: "+value);
    	    		//if(value){    	    			   				
    						pos1++;
    						itemnumbstr = itemList.get(pos1).toString();
    						Log.e("info","itemnumbstr value: "+itemnumbstr);
    						 String newcatVal = objid+"/"+itemnumbstr;
     						idTV.setText(newcatVal);  
     						/*ArrayList confItemDataList = ServiceProDBOperations.readAllConfListWithItemDataFromDB(ServiceProTaskEditScreen.this.getApplicationContext(),objid,itemnumbstr);
     						 ServiceProConstants.showLog("confItemDataList : "+confItemDataList);
     						 
     						categoryDocobj= ((ServiceOrdDocOpConstraints)confItemDataList.get(pos1));
    						showEditscreen(categoryDocobj);  */    						
    	    	} 		
    	    
        	}
        	catch(Exception e){
        		ServiceProConstants.showErrorLog("Error in nextbtnListener"+e.toString());
        	}
        }

		
    };
    
    private void showEditscreen(ServiceOrdDocOpConstraints newobj1) {
    	Intent intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskEditScreen.class);                    	
    	intent.putExtra("docobj", newobj1);    	
		startActivityForResult(intent,ServiceProConstants.TASK_EDIT_SCREEN);
		this.finish();
    	
	}
    
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case TIME_DIALOG_ID: 
				return new TimePickerDialog(this, mTimeSetListener, mHour_dp, mMinute_dp, false);
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mDateSetListener, mYear_dp, mMonth_dp-1, mDay_dp);
			case ETA_DATE_DIALOG_ID:
				return new DatePickerDialog(this, mETADateSetListener, mYearETA_dp, mMonthETA_dp-1, mDayETA_dp);
			case ETA_TIME_DIALOG_ID:
				return new TimePickerDialog(this, mETATimeSetListener, mHourETA_dp, mMinuteETA_dp, false);
		}
		return null;
	}

 	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
			case TIME_DIALOG_ID:
				((TimePickerDialog) dialog).updateTime(mHour_dp, mMinute_dp);
				break;
			case DATE_DIALOG_ID:
				((DatePickerDialog) dialog).updateDate(mYear_dp, mMonth_dp-1, mDay_dp);
				break;
			case ETA_DATE_DIALOG_ID:
				((DatePickerDialog) dialog).updateDate(mYearETA_dp, mMonthETA_dp-1, mDayETA_dp);
				break;
			case ETA_TIME_DIALOG_ID:
				((TimePickerDialog) dialog).updateTime(mHourETA_dp, mMinuteETA_dp);
				break;
		}
    }    
 	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {	
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplayStartDate();
		}
	};
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {	
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplayStartTime();
		}
	};
	
	private DatePickerDialog.OnDateSetListener mETADateSetListener = new DatePickerDialog.OnDateSetListener() {	
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYearETA = year;
			mMonthETA = monthOfYear;
			mDayETA = dayOfMonth;
			updateDisplayETADate();
		}
	};
	
	private TimePickerDialog.OnTimeSetListener mETATimeSetListener = new TimePickerDialog.OnTimeSetListener() {	
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHourETA = hourOfDay;
			mMinuteETA = minute;
			updateDisplayETATime();
		}
	};
	
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	private void sendStatusUpdate(){
		try{
			objectIdStr = category.getObjectId().toString().trim();
			objectIdStr = objectIdStr.trim();
			
			reason = rejectSpn.getSelectedItem().toString();
			if(reason.equalsIgnoreCase(getString(R.string.SERVICEPRO_TSKEDIT_OTHR))){
				reason = rejectresET.getText().toString();
				reason = reason.trim();
			}
						
			taskStatusStr = getTaskStatusString();
			ServiceProConstants.showLog("taskStatus getSelectedItem : "+taskStatusStr);
			
			taskDateStr = mYear+"-"+mMonth+"-"+mDay;
			
			if(!taskStatusStr.equalsIgnoreCase(""))
				initSoapConnection();
		}
		catch(Exception sfgh){
			ServiceProConstants.showErrorLog("Error in sendStatusUpdate : "+sfgh.toString());
		}
	}//fn sendStatusUpdate
	
	private String getTaskStatusString(){
        String statusStr = "";
        try{
        	String statusDesc = statusSpn.getSelectedItem().toString().trim();
			ServiceProConstants.showLog("taskStatus getSelectedItem : "+statusSpn.getSelectedItem().toString());
        	statusStr = ServiceProDBOperations.readStatusSAPValueDataFromDB(ServiceProTaskEditScreen.this, statusDesc);
        	
        	if(statusStr == null){
        		statusStr = statusValue;
        	}
        	/*if(status == ServiceProConstants.TASK_STATUS_READY)
                statusStr = ServiceProConstants.TASK_STATUS_READY_STR_FOR_SAP;
            else if(status == ServiceProConstants.TASK_STATUS_ACCEPTED)
                statusStr = ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP;
            else if(status == ServiceProConstants.TASK_STATUS_DEFERRED)
                statusStr = ServiceProConstants.TASK_STATUS_DEFERRED_STR_FOR_SAP;
            else if(status == ServiceProConstants.TASK_STATUS_DECLINED)
                statusStr = ServiceProConstants.TASK_STATUS_DECLINED_STR_FOR_SAP;
            else if(status == ServiceProConstants.TASK_STATUS_COMPLETED)
                statusStr = ServiceProConstants.TASK_STATUS_COMPLETED_STR_FOR_SAP;*/
        }
        catch(Exception asd){
        	ServiceProConstants.showErrorLog("Error in getStatusStr : "+asd.toString());
        }
        return statusStr;
    }//fn getTaskStatusString
	
	public void onBackPressed() {
		checkUpdationExitsBeforSaveCall();
		//displaySaveDialog();
	}//fn onBackPressed
	
	private void enterAnyReason(){
        try{
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.SERVICEPRO_REASON_Others_DG_MSG);
            builder.setPositiveButton(R.string.SERVICEPRO_OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {  	
                }
            });                
            builder.show();                
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On onSaveAndSubmitAction : "+asf.toString());
        }
    }//fn onSaveAndSubmitAction
	
	private void onSelectedReasonError(){
        try{
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.SERVICEPRO_REASON_DG_MSG);
            builder.setPositiveButton(R.string.SERVICEPRO_OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {  	
                }
            });                
            builder.show();                
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On onSaveAndSubmitAction : "+asf.toString());
        }
    }//fn onSaveAndSubmitAction
	
	public void onClose(){
		try {
			System.gc();
			Intent task_edit_intent = getIntent();		
			ServiceProConstants.showLog("selected_status : "+taskStatusStr);
			ServiceProConstants.showLog("taskDateStrResp : "+taskDateStrResp);
			ServiceProConstants.showLog("taskETADateStrResp : "+taskETADateStrResp);
			ServiceProConstants.showLog("taskETATimeStrResp : "+taskETATimeStrResp);
			task_edit_intent.putExtra("selected_status", taskStatusStr);	
			task_edit_intent.putExtra("taskDateStr", taskDateStrResp);
			task_edit_intent.putExtra("taskETADateStr", taskETADateStrResp);
			task_edit_intent.putExtra("taskETATimeStr", taskETATimeStrResp);
			setResult(RESULT_OK, task_edit_intent);
			this.finish();
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error On onClose : "+e.toString());
		}
	}//fn onClose
	
	private void displaySaveDialog(){
		try{
			alertDialog = new AlertDialog.Builder(this)
			.setTitle("Status Update")
			.setMessage("Do you really want to update the status ?")
			.setPositiveButton("Save", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					sendStatusUpdate();
				}
				})			
			.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					cancelAction();
				}
				})
				.create();
				alertDialog.show();
		}
		catch(Exception asfg){
			ServiceProConstants.showErrorLog("Error in displaySaveDialog : "+asfg.toString());
		}
	}//fn displaySaveDialog	
	
	//save btn click listener
    private OnClickListener savebtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		boolean backBtnPress = false;    		
        		saveAction(backBtnPress);
        	}
        	catch(Exception e){
        		ServiceProConstants.showErrorLog("Error in savebtnListener"+e.toString());
        	}
        }
    };
    
    //Cancel btn click listener
    private OnClickListener cancelbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		cancelAction();
        	}
        	catch(Exception e){
        		ServiceProConstants.showErrorLog("Error in cancelbtnListener"+e.toString());
        	}
        }
    };
    
    private void checkUpdationExitsBeforSaveCall(){
    	try{
    		boolean backBtnPress = true;    		
    		saveAction(backBtnPress);
    	}
    	catch(Exception ec){
    		ServiceProConstants.showErrorLog("Error in checkUpdationExitsBeforSaveCall"+ec.toString());
    	}
    }//fn checkUpdationExitsBeforSaveCall
    
        
    private void saveAction(boolean backBtnPress){
    	try{    		
    		String st_dat_time_value = due_date_value_cal;
    		String eta_dat_time_value = eta_date_value_cal;
    		Date d1 = new Date(st_dat_time_value);   
            Date d2 = new Date(eta_dat_time_value); 
            Calendar cal1 = Calendar.getInstance();cal1.setTime(d1);   
            Calendar cal2 = Calendar.getInstance();cal2.setTime(d2); 
            int datediff = (int) calculateDays(d1, d2);
			taskStatusStr = getTaskStatusString();    
			ServiceProConstants.showLog("taskStatus getSelectedItem : "+taskStatusStr);
            if(taskStatusStr.toString().toString().trim().equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
	            if(datediff >= 0){	            	
	            	saveActionCall(backBtnPress);
	            }else{
	            	ServiceProConstants.showErrorDialog(this, getString(R.string.SERVICEORD_SERVICEPRO_TSKEDIT_ERR_DTLESSER));
	            }
            }else{
            	saveActionCall(backBtnPress);
            }      		
    	}
    	catch(Exception e){
    		ServiceProConstants.showErrorLog("Error in saveAction"+e.toString());
    	}
    }//fn saveAction
    
    @Override
	public void afterTextChanged(Editable s) {
    	fieldnotesoapStr = s.toString();
		Log.e("info","field string value"+fieldnotesoapStr);
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
	
    private void saveActionCall(boolean backBtnPress){
    	try {
			int reason_selected_id = (int) rejectSpn.getSelectedItemId();
			int statusSpn_id = (int) statusSpn.getSelectedItemId();			
			if(ServiceProConstants.INITIAL_STATUS_VALUE.length() != 0 && !ServiceProConstants.INITIAL_STATUS_VALUE.equalsIgnoreCase(sel_status)){
				if(statusSpn_id == ServiceProConstants.TASK_STATUS_DECLINED){
					if(reason_selected_id == 0){
						onSelectedReasonError();
					}
					else{
						if(reason_selected_id == 6 && rejectresET.getText().toString().trim().length() == 0){
							enterAnyReason();
						}
						else{
							if(backBtnPress)
								displaySaveDialog();
							else
								sendStatusUpdate();
						}
					}
				}		
				else{
					if(backBtnPress)
						displaySaveDialog();
					else
						sendStatusUpdate();
				}
			}
			else{
				boolean attach = false;
				File f_imageCap = new File(imgCapPath);
				if(f_imageCap.exists()){
					attach = true;
				}						
				File f_sign = new File(signPath);
				if(f_sign.exists()){
					attach = true;
				}	
				if(attach){
					if(backBtnPress)
						displaySaveDialog();
					else
						sendStatusUpdate();
				}else{
					String taskDateStrValue = ""; 
					taskDateStrValue = ServiceProConstants.getDateFormatForSAP(mYear_dp, mMonth_dp, mDay_dp, mHour_dp, mMinute_dp);
					if(taskDateStrValue == null || taskDateStrValue.length() < 0 ){
						taskDateStrValue = "";
					}
					
					String taskETADateStrValue = ""; 
					taskETADateStrValue = ServiceProConstants.getDateFormatForSAP(mYearETA_dp, mMonthETA_dp, mDayETA_dp, mHourETA_dp, mMinuteETA_dp);
					if(taskETADateStrValue == null || taskETADateStrValue.length() < 0 ){
						taskETADateStrValue = "";
					}
					String mHourETA_dp_value = getCorrectHour_N_Time(String.valueOf(mHourETA_dp));
					String mMinuteETA_dp_value = getCorrectHour_N_Time(String.valueOf(mMinuteETA_dp));       
					String taskETATimeStrValue = "";
					taskETATimeStrValue = mHourETA_dp_value+":"+mMinuteETA_dp_value+":00";
					if(taskETATimeStrValue == null || taskETATimeStrValue.length() < 0 ){
						taskETATimeStrValue = "";
					}
					
					/*if(!ServiceProConstants.INITIAL_STATUS_VALUE.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR)){
						taskETADateStrValue = "0000-00-00";
						taskETATimeStrValue = "00:00:00";
					}*/
																				        	
					if(initialDueDate.equalsIgnoreCase(taskDateStrValue)){
						taskDateStrResp = initialDueDate;
						if(initialETADate.equalsIgnoreCase(taskETADateStrValue)){
							taskETADateStrResp = initialETADate;
							taskETATimeStrResp = initialETATime;
				    		if(initialETATime.equalsIgnoreCase(taskETATimeStrValue)){
				    			taskETADateStrResp = initialETADate;
								taskETATimeStrResp = initialETATime;
				    			finish();
				    		}else{
				    			taskETADateStrResp = taskETADateStrValue;
								taskETATimeStrResp = taskETATimeStrValue;
				    			ServiceProConstants.showLog("eta time else part");
				    			ServiceProConstants.showLog("initialETATime:"+initialETATime);
				        		ServiceProConstants.showLog("taskETATimeStrValue:"+taskETATimeStrValue);
				        		if(backBtnPress)
				        			displaySaveDialog();
								else
									sendStatusUpdate();
				    		}
				    	}else{
				    		taskETADateStrResp = taskETADateStrValue;
							taskETATimeStrResp = taskETATimeStrValue;
				    		ServiceProConstants.showLog("eta date else part");
				    		ServiceProConstants.showLog("initialETADate:"+initialETADate);
				    		ServiceProConstants.showLog("taskETADateStrValue:"+taskETADateStrValue);
				    		if(backBtnPress)
								displaySaveDialog();
							else
								sendStatusUpdate();
				    	}
					}
					else{
						ServiceProConstants.showLog("start date else part");
						ServiceProConstants.showLog("initialDueDate:"+initialDueDate);
						ServiceProConstants.showLog("taskDateStrValue:"+taskDateStrValue);
						taskETADateStrResp = taskETADateStrValue;
						taskETATimeStrResp = taskETATimeStrValue;
						taskDateStrResp = taskDateStrValue;
						if(backBtnPress)
							displaySaveDialog();
						else
							sendStatusUpdate();
					}
				}				
			}
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in saveActionCall"+e.toString());
		}
    }//fn saveActionCall
    
    private void cancelAction(){
    	try{
			taskStatusStr = "";
			taskETADateStrResp = initialETADate;
			taskETATimeStrResp = initialETATime;
			taskDateStrResp = initialDueDate;
			onClose();
			//finish();
			/*System.gc();
			Intent task_edit_intent = getIntent();
			setResult(RESULT_CANCELED, task_edit_intent);
			this.finish();*/
    	}
    	catch(Exception e){
    		ServiceProConstants.showErrorLog("Error in cancelAction"+e.toString());
    	}
    }//fn cancelAction	
	
	private void initSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[7];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
            
            int tzoffset = Integer.parseInt(category.getTimeZone());
            String taskDateStrValue = ""; 
        	taskDateStrValue = ServiceProConstants.getDateFormatForSAP(mYear_dp, mMonth_dp, mDay_dp, mHour_dp, mMinute_dp);
        	if(taskDateStrValue == null || taskDateStrValue.length() < 0 ){
        		taskDateStrValue = "";
        	}
        	
        	String taskETADateStrValue = ""; 
        	String taskETADateStrValueDB = "";
        	taskETADateStrValue = ServiceProConstants.getDateFormatForSAP(mYearETA_dp, mMonthETA_dp, mDayETA_dp, mHourETA_dp, mMinuteETA_dp);
        	taskETADateStrValueDB = taskETADateStrValue;
        	if(taskETADateStrValue == null || taskETADateStrValue.length() < 0 ){
        		taskETADateStrValue = "";
        		taskETADateStrValueDB = "0000-00-00";
        	}
        	String mHourETA_dp_value = getCorrectHour_N_Time(String.valueOf(mHourETA_dp));
        	String mMinuteETA_dp_value = getCorrectHour_N_Time(String.valueOf(mMinuteETA_dp));     
        	String taskETATimeStrValue = "";
        	taskETATimeStrValue = mHourETA_dp_value+mMinuteETA_dp_value;
        	String taskETATimeStrValueDB = "";
        	taskETATimeStrValueDB = mHourETA_dp_value+":"+mMinuteETA_dp_value+":00";
        	if(taskETATimeStrValue == null || taskETATimeStrValue.length() < 0 ){
        		taskETATimeStrValue = "";
        		taskETATimeStrValueDB = "00:00:00";
        	}
        	
        	if(taskStatusStr.toString().toString().trim().equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
        		taskETADateStrValue = taskETADateStrValueDB;
        		taskETATimeStrValue = taskETATimeStrValueDB;
        		category.ZZETADATE = taskETADateStrValueDB;
        		category.ZZETATIME = taskETATimeStrValueDB;
        		taskETADateStrResp = taskETADateStrValueDB;
        		taskETATimeStrResp = taskETATimeStrValueDB;
        		
        		String data = null;
                data = category.getObjectId().trim()+"-";
				if(category.getCity().toString().length() > 0){
            		data += category.getCity().toString();
            	}
            	if(category.getRegion().toString().length() > 0){
            		data += ","+category.getRegion().toString();
            	}
            	if(category.getNameOrg1().toString().length() > 0){
            		data += ","+category.getNameOrg1().toString();
            	}
            	if(category.getNameOrg2().toString().length() > 0){
            		data += ","+category.getNameOrg2().toString();
            	}
            	if(category.getStreet().toString().length() > 0){
            		data += ","+category.getStreet().toString();
            	}
            	if(category.getPostalCode1().toString().length() > 0){
            		data += ","+category.getPostalCode1().toString();
            	}
            	if(category.getCountryIso().toString().length() > 0){
            		data += ","+category.getCountryIso().toString();
            	}
            	if(categoryDocobj.getNumberExt().toString().length() > 0){
            		data += ","+categoryDocobj.getNumberExt().toString();
            	}
            	
            	String itemSap = categoryDocobj.getNumberExt().toString();
            	
        		String desc;
        		desc = "Status: "+category.getStatus()+"\nPriority: "+category.getPriority();
        		desc += "\n"+category.getProcessType()+":"+category.getObjectId().trim()+" - Customer# "+category.getPartner().trim()+"\n";
            	
        		ServiceProConstants.showLog("getZzetaDate: "+ category.getZzetaDate().toString()+" : "+category.getZzetaTime().toString());
        		String eta_date = category.getZzetaDate().toString().trim();
        		if(!eta_date.equalsIgnoreCase("0000-00-00")){
        			String eta_dat_time_value = category.getZzetaDate().toString().trim()+" "+category.getZzetaTime().toString().trim();
        			ServiceProConstants.showLog("eta_dat_time_value: "+ eta_dat_time_value);
        			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                	Date dateObj = curFormater.parse(eta_dat_time_value);
                	Long dateLongValue = dateObj.getTime();
                	ServiceProConstants.showLog("Status: "+ category.getStatus().toString()+" : "+ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP);
                	
                	//Adding Calendar Event
                	if(!ServiceProConstants.SERVICEPRO_MOBILE_IMEI.equalsIgnoreCase(ServiceProConstants.SERVICEPRO_EMULATOR_IMEI)){
                		if(category.getStatus().toString().equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
                			String android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);    
                    		ServiceProConstants.showLog("Emulator/device"+android_id);
                			if (android_id == null) {     
                				// Emulator!    
                				ServiceProConstants.showLog("Emulator!"+android_id);
                			}
                			else {        
                				// Device	                        				
                				ServiceProConstants.showLog("Device!"+android_id);
                				ServiceProConstants.addToCalendar(ServiceProTaskEditScreen.this, data, desc, dateLongValue, dateLongValue);
                			}	      
                    	}
                	}
        		}
        		
        	}else{
        		taskETADateStrValue = "0000-00-00";
        		taskETATimeStrValue = "00:00:00";
        		category.ZZETADATE = "0000-00-00";
        		category.ZZETATIME = "00:00:00";
        		taskETADateStrResp = "0000-00-00";
        		taskETATimeStrResp = "00:00:00";
        	}
        	
        	ServiceProConstants.showLog("taskETADateStrValue:"+taskETADateStrValue+"  taskETATimeStrValue:"+taskETATimeStrValue); 
        	
        	//Removed default value for reason
        	if(reason.equals("Please select")){
        		reason = "";
        	}
        	
        	File file = new File(signPath); 
        	if (file.exists()) {     
        		Bitmap bm = BitmapFactory.decodeFile(signPath);
			  	ByteArrayOutputStream baos = new ByteArrayOutputStream();   
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object    
				byte[] b = baos.toByteArray();
				encodedSignImageStr = Base64.encodeBytes(b); //Base64.encodeToString(b, Base64.DEFAULT); //Base64.encodeBytes(b);
				
				String imageData = encodedSignImageStr;
				ServiceProConstants.showLog("Image length : " +encodedSignImageStr.length());
				ServiceProConstants.showLog("imageData:"+imageData);
				
				File dir = Environment.getExternalStorageDirectory(); 
			    String filename = "file1.txt"; 
			    File f = new File(dir, filename); 
			    if (f.exists()) {
			    	f.delete();
				    f.createNewFile(); 
			    }else{
				    f.createNewFile(); 		    	
			    }
	            FileOutputStream fOut = new FileOutputStream(f);
	            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
	            myOutWriter.append(encodedSignImageStr);
	            myOutWriter.close();
	            fOut.close();
        	} 	   
        	
        	String itemSap = category.getNumberExtn().toString();
 
        	category.ZZKEYDATE = taskDateStrValue.toString().toString();
        	category.STATUS = taskStatusStr.toString().toString();
        	category.ZZFIRSTSERVICEITEM = itemSap.toString();
        	category.TIMEZONE_FROM = ""+tzoffset;
        	category.NOTEZZFIELDNOTE = fieldnotesoapStr.toString();
        	taskDateStrResp = taskDateStrValue;
        	
        	C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]SERVICE-DOX-STATUS-UPDATE[.]VERSION[.]0";
            C0[3].Cdata = "DATA-TYPE[.]ZGSXSMST_SRVCDCMNT20[.]OBJECT_ID[.]PROCESS_TYPE[.]NUMBER_EXT[.]ZZKEYDATE[.]STATUS[.]STATUS_REASON[.]TIMEZONE_FROM[.]ZZETADATE[.]ZZETATIME[.]ZZFIELDNOTE";            
			C0[4].Cdata = "ZGSXSMST_SRVCDCMNT20[.]"+objectIdStr+"[.]"+processTypeStr+"[.]"+itemSap+"[.]"+taskDateStrValue+"[.]"+taskStatusStr+"[.]"+reason+"[.]"+tzoffset+"[.]"+taskETADateStrValue+"[.]"+taskETATimeStrValue+"[.]"+fieldnotesoapStr;
			C0[5].Cdata = "ZGSXCAST_ATTCHMNT01[.]"+objectIdStr+"[.]"+processTypeStr+"[.]"+itemSap+"[.]"+imgCapPath+"[.]"+encodedImageStr;
            C0[6].Cdata = "ZGSXCAST_ATTCHMNT01[.]"+objectIdStr+"[.]"+processTypeStr+"[.]"+itemSap+"[.]"+signPath+"[.]"+encodedSignImageStr;
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);         
                         	
        	if(taskStatusStr.toString().toString().trim().equalsIgnoreCase(ServiceProConstants.TASK_STATUS_DECLINED_STR_FOR_SAP)){
        		category.STATUS_REASON = reason;
        	}else{
        		category.STATUS_REASON = "";
        	}
        	
        	category.STATUS_TXT30 = statusSpn.getSelectedItem().toString().trim();
        	
        	try{
            	if(taskDbObj == null)
    				taskDbObj = new ServiceProTaskDetailsPersistent(this);		
            	taskDbObj.updateTaskObject(category);
            }
    		catch (Exception de) {
            	ServiceProConstants.showErrorLog("Error in updation data:"+de.toString());
            } 
        	
        	if(taskDbObj != null)
    			taskDbObj.closeDBHelper();
        	
        	respType = ServiceProConstants.STATUS_UPDATE_TYPE;
        	envelopeC.setOutputSoapObject(request);
    		requestSoapObj = request;
			internetAccess = ServiceProConstants.networkAvailableCheck(ServiceProTaskEditScreen.this);			
            if(internetAccess){
            	saveDataToLDB();
            	doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
            }
            else{   	
            	saveToLDB();
            }            	
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in initSoapConnection : "+asd.toString());
        }
    }//fn initSoapConnection	

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
	    			if(respType == ServiceProConstants.STATUS_UPDATE_TYPE)
	    				updateTaskResponse(resultSoap);
	    		}
	    		else{
	    			sendToQueueProcessor();	    			
	    		}
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };
    
    private void sendToQueueProcessor(){
    	try {
    		saveToLDB();
		} catch (Exception errg) {
			SapGenConstants.showErrorLog("Error in sendToQueueProcessor : "+errg.toString());
		}
    }//fn sendToQueueProcessor
	
	public void updateTaskResponse(SoapObject soap){
		boolean resMsgErr = false;
        if(soap != null){
        	try{
            	ServiceProConstants.soapResponse(this, soap, false);
            	taskErrorMsgStr = ServiceProConstants.SOAP_RESP_MSG;
            	taskErrorType = ServiceProConstants.SOAP_ERR_TYPE;
            	ServiceProConstants.showLog("On soap response : "+soap.toString());
            	String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg); 
            }
            catch(Exception sff){
            	ServiceProConstants.showErrorLog("On gettingSoapResponse : "+sff.toString());
            }             
            try{           
            	ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
            	ServiceProConstants.showLog(taskErrorMsgStr);
                ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
            	if(!resMsgErr){
            		if(errorDbObj == null)
	        			errorDbObj = new ServiceProErrorMessagePersistent(this.getApplicationContext());
            		if(errorDbObj != null){
            			if(errorDbObj.checkTrancIdApiExists(objectIdStr.trim(), ServiceProConstants.STATUS_UPDATE_API))
            				errorDbObj.deleteRowForStatus(objectIdStr.trim(), ServiceProConstants.STATUS_UPDATE_API);
            		}
            		if(taskDbObj != null)
            			taskDbObj.closeDBHelper();
            		onClose();
            	}
            	else{
            		boolean apiExists = false;
            		if(errorDbObj == null)
	        			errorDbObj = new ServiceProErrorMessagePersistent(this.getApplicationContext());
            		if(errorDbObj != null){
            			apiExists = errorDbObj.checkTrancIdApiExists(objectIdStr.trim(), ServiceProConstants.STATUS_UPDATE_API.trim());
				    	String tracId = objectIdStr.trim();
				    	String apiName = ServiceProConstants.STATUS_UPDATE_API.trim();
				    	String errType = taskErrorType.trim();
				    	String errorDesc = taskErrorMsgStr.trim();
				    	if(!apiExists){
				    		errorDbObj.insertErrorMsgDetails(tracId, errType, errorDesc, apiName);
				    	} 
				    	else{
				    		errorDbObj.updateValue(tracId, errType, errorDesc, apiName);
				    	}	            		
            		}
            		if(errorDbObj != null)
	        			errorDbObj.closeDBHelper();
            		
            		try{
    	            	if(taskDbObj == null)
    	    				taskDbObj = new ServiceProTaskDetailsPersistent(this);		
    	            	taskDbObj.updateTaskObject(category);
    	            }
    	    		catch (Exception de) {
    	            	ServiceProConstants.showErrorLog("Error in updation data:"+de.toString());
    	            } 
                	
                	if(taskDbObj != null)
            			taskDbObj.closeDBHelper();
					
            		//Intent intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreen.class);
            		Intent intent;
      				if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
      					intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForTablet.class);
      				}else{
      					intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForPhone.class);
      				}  	
            		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);       
        			startActivityForResult(intent,ServiceProConstants.TASKLIST_SCREEN);
            		finish();
            	}
            }
            catch(Exception asf){}
        }
    }//fn updateTaskResponse
	
	private void saveDataToLDB(){
		try{
        	if(taskDbObj == null)
				taskDbObj = new ServiceProTaskDetailsPersistent(this);		
        	taskDbObj.updateTaskObject(category);
                  	
        	if(taskDbObj != null)
    			taskDbObj.closeDBHelper();
		}catch (Throwable e) {
	        ServiceProConstants.showErrorLog("Error in saveDataToLDB : "+e.toString());
	    }
	}//fn saveDataToLDB
	
	private void saveToLDB(){
		try{
        	try{
            	if(taskDbObj == null)
    				taskDbObj = new ServiceProTaskDetailsPersistent(this);		
            	taskDbObj.updateTaskObject(category);
            }
    		catch (Exception de) {
            	ServiceProConstants.showErrorLog("Error in updation data:"+de.toString());
            } 
        	
        	if(taskDbObj != null)
    			taskDbObj.closeDBHelper();
        	
        	String trancId = objectIdStr; 
        	Long now = Long.valueOf(System.currentTimeMillis());
        	/*Calendar cal_enddate = Calendar.getInstance(); 
        	Long now = cal_enddate.getTime().getTime();*/
        	//SapQueueProcessorHelperConstants.showErrorDialog(ServiceProTaskEditScreen.this, "No connectivity:Update queued for processing later");
			//SapQueueProcessorHelperConstants.saveOfflineContentToQueueProcessor(ServiceProTaskEditScreen.this, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.APPLN_PACKAGE_NAME, trancId, ServiceProConstants.APPLN_BGSERVICE_NAME, "SERVICE-DOX-STATUS-UPDATE", requestSoapObj, now);
        	
        	Intent intent;
			if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
				intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForTablet.class);
			}else{
				intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForPhone.class);
			}  	
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);       
			startActivityForResult(intent,ServiceProConstants.TASKLIST_SCREEN);
			finish();
		}
	    catch (Throwable e) {
	        ServiceProConstants.showErrorLog("Error in saveToLDB : "+e.toString());
	    }
	}//fn saveToLDB
	
	private void stopProgressDialog(){
    	try {
			if(pdialog != null){
				pdialog.dismiss();
				pdialog = null;
			}
			else
				pdialog = null;	
		} catch (Exception ce) {
			ServiceProConstants.showErrorLog(ce.toString());
		}
    }//fn stopProgressDialog 
	
    private void setETADate(String etaDate, String etaTime){
    	try{
    		String etaDateStr = etaDate;
            int etadat1 = etaDateStr.indexOf(" ");
            int etadat2 = etaDateStr.lastIndexOf(":");
            String eta_date_value = "";
            String eta_time_value = "";
            
            if(etadat1 != -1){
            	eta_date_value = etaDateStr.substring(0, etadat1);
            	String[] et_date_value = eta_date_value.split("-");
            	eta_date_value = Integer.parseInt(et_date_value[2])+"-"+ServiceProConstants.getMonthValue(Integer.parseInt(et_date_value[1]))+"-"+Integer.parseInt(et_date_value[0]);
            	mYearETA_dp = Integer.parseInt(et_date_value[0]);
				mMonthETA_dp = Integer.parseInt(et_date_value[1]);
				mDayETA_dp = Integer.parseInt(et_date_value[2]);
            }	                
            else{
            	eta_date_value = etaDate;
            	String[] et_date_value = eta_date_value.split("-");
            	eta_date_value = Integer.parseInt(et_date_value[2])+"-"+ServiceProConstants.getMonthValue(Integer.parseInt(et_date_value[1]))+"-"+Integer.parseInt(et_date_value[0]);
            	mYearETA_dp = Integer.parseInt(et_date_value[0]);
				mMonthETA_dp = Integer.parseInt(et_date_value[1]);
				mDayETA_dp = Integer.parseInt(et_date_value[2]);
            }
            
            if(etadat1 != -1 && etadat2 != -1){
            	eta_date_value = etaDateStr.substring(etadat1+1, etadat2);
            	String[] et_time_value = eta_date_value.split(":");
            	mHourETA_dp = Integer.parseInt(et_time_value[0]);
            	mMinuteETA_dp = Integer.parseInt(et_time_value[1]);
            }
            else{
            	if(etaTime.length() > 0){
            		if(etaTime.equals("00:00:00")){
                		eta_time_value = "00:00";
                    	mHourETA_dp = Integer.parseInt("00");
                        mMinuteETA_dp = Integer.parseInt("00");
                	}else{
                		String[] et_time_value = etaTime.split(":");
                    	mHourETA_dp = Integer.parseInt(et_time_value[0]);
                        mMinuteETA_dp = Integer.parseInt(et_time_value[1]);
                		eta_time_value = et_time_value[0]+":"+et_time_value[1];
                	}           
            	}else{
            		eta_time_value = "00:00";
                	mHourETA_dp = Integer.parseInt("00");
                    mMinuteETA_dp = Integer.parseInt("00");
            	}            	 	
            }
            
            String etadate_sav = etaDate;
            int etadat1_sav = etadate_sav.indexOf(" ");
            int etadat2_sav = etadate_sav.lastIndexOf(":");	             
            if(etadat1_sav != -1){
            	 String eta_date_sav_value = etadate_sav.substring(0, etadat1_sav);
            	 String[] et_date_value = eta_date_sav_value.split("-");
            	 mYearETA_dp = Integer.parseInt(et_date_value[0]);
				 mMonthETA_dp = Integer.parseInt(et_date_value[1]);
				 mDayETA_dp = Integer.parseInt(et_date_value[2]);
            }
            
            if(etadat2_sav != -1){
            	String eta_time_sav_value = etadate_sav.substring(etadat1_sav+1, etadat2_sav);	                	          
                String[] et_time_value = eta_time_sav_value.split(":");
                mHourETA_dp = Integer.parseInt(et_time_value[0]);
                mMinuteETA_dp = Integer.parseInt(et_time_value[1]);
            }else{
        		String[] et_time_value = etaTime.split(":");
            	mHourETA_dp = Integer.parseInt(et_time_value[0]);
                mMinuteETA_dp = Integer.parseInt(et_time_value[1]);
                eta_time_value = et_time_value[0]+":"+et_time_value[1];
        	}                       	
            etaTimeTV.setText(eta_time_value);    
            setETA_TIME_STR(eta_time_value);
            eta_date_value = eta_date_value.replace("-", " ");            
            String dateStr = etaDate;
        	dateStr = dateStr.trim();
        	if(!dateStr.equalsIgnoreCase("")){	                    		
            	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
    			etaDateTV.setText(" "+newdatestr);
    			setETA_DATE_STR(newdatestr);
        	}
        	eta_date_value_cal = mMonthETA_dp+"/"+mDayETA_dp+"/"+mYearETA_dp;
    	} catch (Exception ae) {
			ServiceProConstants.showErrorLog(ae.toString());
		}
    }//fn setETADate
    
    private String getETA_DATE_STR(){
    	return ETA_DATE_STR;
    }//fn getETA_DATE_STR
    
    private void setETA_DATE_STR(String etaDateValue){
    	ETA_DATE_STR = etaDateValue;
    }//fn setETA_DATE_STR
    
    private String getETA_TIME_STR(){
    	return ETA_TIME_STR;
    }//fn getETA_TIME_STR
    
    private void setETA_TIME_STR(String etaTimeValue){
    	ETA_TIME_STR = etaTimeValue;
    }//fn setETA_TIME_STR
    
    private String getCorrectHour_N_Time(String valueStr){
    	String val = "";
    	try {
			if(valueStr.length() > 0){
				if(valueStr.length() == 1){
					val = "0"+valueStr;
				}else{
					val = valueStr;
				}				
			}
		} catch (Exception easf) {
			ServiceProConstants.showErrorLog("Error in getCorrectHour_N_Time : "+easf.toString());
		}finally{
			if((val == null)|| (val.equalsIgnoreCase(""))){
				val = "";
    		}			
		}
    	return val;
    }//fn getCorrectHour_N_Time
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
	  	if(resultCode==RESULT_OK && requestCode==ServiceProConstants.COLLEAGUE_LIST_SCREEN){		
	  		try{
	  			boolean listrefresh = data.getBooleanExtra("refresh", false);
	  			if(listrefresh){
	  				Intent intent;
	  				if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
	  					intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForTablet.class);
	  				}else{
	  					intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForPhone.class);
	  				}  	
		  			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);       
		  			startActivityForResult(intent,ServiceProConstants.TASKLIST_SCREEN);
		  			finish();
	  			}	  			
		  	}
		    catch(Exception asf){
		    	ServiceProConstants.showErrorLog("Error in COLLEAGUE_LIST_SCREEN result : "+asf.toString());
		    }
	  	}
	  	else if(resultCode==Activity.RESULT_OK && requestCode==ServiceProConstants.CNFM_MAIN_SCREEN){		
	  		try{
	  			boolean listrefresh = data.getBooleanExtra("refresh", false);
	  			if(listrefresh){
	  				Intent intent;
	  				if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
	  					intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForTablet.class);
	  				}else{
	  					intent = new Intent(ServiceProTaskEditScreen.this, ServiceProTaskMainScreenForPhone.class);
	  				}  	
	  				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);		
	  				startActivity(intent);
	    			finish();
	  			}	  			
		  	}
		    catch(Exception asf){
		    	ServiceProConstants.showErrorLog("Error in CNFM_MAIN_SCREEN result : "+asf.toString());
		    }
	  	}	
	  	else if(requestCode==ServiceProConstants.IMAGE_CAPTURE_SCREEN){		
	  		try{		  			
	  			switch( resultCode )
	  		    {
	  		    	case 0:
	  		    	ServiceProConstants.showLog( "User cancelled" );
	  		    		break;
	  		    	case -1:
	  		    		onPhotoTaken();
	  		    		break;
	  		    }		  			
		  	}
		    catch(Exception asf){
		    	ServiceProConstants.showErrorLog("Error in onActivityResultIMAGE_CAPTURE_SCREEN : "+asf.toString());
		    }
	  }	
	  else if(requestCode==ServiceProConstants.SIGNATURE_DIS_SCREEN){
		  if(resultCode==RESULT_OK){
			  	try{
					Bitmap bm = BitmapFactory.decodeFile(signPath);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();   
					bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object    
					byte[] b = baos.toByteArray();
					encodedSignImageStr = Base64.encodeBytes(b); //Base64.encodeToString(b, Base64.DEFAULT); //Base64.encodeBytes(b);
					signImageDisplay();
			  	}
			    catch(Exception asf){
			    	ServiceProConstants.showErrorLog("Error in onActivityResultSIGNATURE_DIS_SCREEN : "+asf.toString());
			    }
		  }
		  else{
			  try{
				  signImageDisplay();
		  		}
			    catch(Exception asf){
			    	ServiceProConstants.showErrorLog("Error in onActivityResultSIGNATURE_DIS_SCREEN : "+asf.toString());
			    }
		  	}
	  	}
	}
    
    protected void onPhotoTaken()
    {
    	try {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 4;
	        Bitmap bitmap = BitmapFactory.decodeFile( path, options );       		
    		if(pdialog != null)
    			pdialog = null;		  	    		
			if(pdialog == null)
				this.runOnUiThread(new Runnable() {
                    public void run() {
                    	pdialog = ProgressDialog.show(ServiceProTaskEditScreen.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
                    	new Thread() {
                    		public void run() {
                    			try{
                    				imageResize(path);		  	                    				  	              					
                    				sleep(2000);
                    			} catch (Exception e) {  }
                    			handler.post(imageResizeResp);
             				}
                    	}.start();
                    }
                });
		} catch (Exception ae) {
			ServiceProConstants.showErrorLog("onPhotoTaken :"+ae.toString());
		}		  			  	
    }//fn onPhotoTaken
    
    private void imageResize(String filepathStr){
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
		    File f = new File(imgCapPath);
		    f.getAbsolutePath();
		    Log.e(" ", imgCapPath);
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
        	
        	try {
				Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath()); 
				ByteArrayOutputStream baos = new ByteArrayOutputStream();   
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object    
				byte[] b = baos.toByteArray();
				encodedImageStr = Base64.encodeBytes(b); //Base64.encodeToString(b, Base64.DEFAULT); //Base64.encodeBytes(b);
			} catch (Exception aeeer) {
				ServiceProConstants.showErrorLog("Error in bitmap resize:"+aeeer.toString());
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
		finally {                     
            ServiceProConstants.showLog("========END OF LOG========");    
            stopProgressDialog(); 
        }
    }//fn imageResize
        
    final Runnable imageResizeResp = new Runnable(){
	    public void run()
	    {
	    	try{
	    		attachImageDisplay();
	    	} catch(Exception aeer){
	    		ServiceProConstants.showErrorLog("Error in imageResizeResp:"+aeer.toString());
	    	}
	    }	    
    };
    
    private void attachImageDisplay(){
    	try{
	  		File f_imageCap = new File(imgCapPath);
			if(f_imageCap.exists()){
				attaBtn.setVisibility(View.VISIBLE);
			}						
			else{
				attaBtn.setVisibility(View.GONE);
			}	
	  	} catch (Exception ae) {
			ServiceProConstants.showErrorLog("Error in attachImageDisplay:"+ae.toString());
		}	
    }//fn attachImageDisplay    

    private void sapAttachImageDisplay(){
    	try{
			if(attachList.size() > 0){
				sapAttaBtn.setVisibility(View.VISIBLE);
			}						
			else{
				sapAttaBtn.setVisibility(View.GONE);
			}	
	  	} catch (Exception ae) {
			ServiceProConstants.showErrorLog("Error in sapAttachImageDisplay:"+ae.toString());
		}	
    }//fn sapAttachImageDisplay
    
    private void signImageDisplay(){
    	try{
		    File f_sign = new File(signPath);		   
			if(f_sign.exists()){
				signAttsBtn.setVisibility(View.VISIBLE);
			}						
			else{
				signAttsBtn.setVisibility(View.GONE);
			}		
    	} catch (Exception ae) {
			ServiceProConstants.showErrorLog("Error in signImageDisplay:"+ae.toString());
		}	
    }//fn signImageDisplay
    
    public static long calculateDays(Date dateEarly, Date dateLater) {   
    	return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000); 
    }//fn  calculateDays  
    
  //GESTURE RELATED CODE
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
      if(this.gestureDetector.onTouchEvent(motionEvent))
      {    	  
          return true;
      }
      //no gesture detected, let Activity handle touch event
      return super.onTouchEvent(motionEvent);
    }
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {		
		  if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && 
                  Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
         //From Right to Left
			  ShoNextScreen();     	    	
         return true;
     }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                  Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
         //From Left to Right
    	 leftToright =1;
    	  ShoNextScreen();
         return true;
     }
    
     if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && 
                 Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
         //From Bottom to Top
         return true;
     }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && 
                 Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
         //From Top to Bottom
         return true;
     }
     return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}//End of class ServiceProTaskMainScreen