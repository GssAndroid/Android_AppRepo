package com.globalsoft.ServicePro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


import com.globalsoft.SalesOrderLib.Utils.SalesOrderConstants;
import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.SapQueueProcessorHelper.Utils.SapQueueProcessorHelperConstants;
import com.globalsoft.ServicePro.Constraints.SOCodeGroupOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOCodeListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOMattEmpListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOServiceActListOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceFollDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProAttachmentOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Constraints.StatusFollowOpConstraints;
import com.globalsoft.ServicePro.Constraints.StatusOpConstraints;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Database.ServiceProOfflineContraintsCP;
import com.globalsoft.ServicePro.SoapConnection.HttpTransportSE;

public class ServiceProTaskMainScreenForTablet  extends Activity implements TextWatcher,OnTouchListener {
	
	private ListView listView;
	private SoapObject resultSoap = null;
	private ProgressDialog pdialog = null;
	private ImageView settingsbtn;
	private int sortIndex = -1;
	float c=10;
	static int a=0;
	int b=10;
	private int selected_index = -1;
	private static ArrayList taskList = new ArrayList();
	private static ArrayList allTaskList = new ArrayList();
	private static ArrayList idList = new ArrayList();
	private ArrayList taskListObj = new ArrayList();
	private ArrayList allTaskListObj = new ArrayList();
	private ArrayList taskOrderListObj = new ArrayList();
	ArrayList diogList =new ArrayList();
	private String dateStr= " ";
	private EditText searchET;
	private boolean internetAccess = false;
	private SOCustomerListAdapter SOCustomerListAdapter;
	private AlertDialog alertDialog;
	private AlertDialog.Builder builder;
	float olddistanc;
	private String searchStr = "",currDateTime =" ",statusendtimeStr=" ",linkText="",strtStr= "";
	private boolean sortNameFlag = false, sortDateFlag = false, sortPriorityFlag = false, sortStatusFlag = false,
	sortSOFlag = false, sortCNameFlag = false, sortProductFlag = false, sortETAFlag = false,flag_pref = false;
	private boolean sortCityFlag = false;
	private static final int MENU_COLLLIST = Menu.FIRST;
	private static final int MENU_SORT_NAME = Menu.FIRST+2;
	private static final int MENU_SORT_CITY = Menu.FIRST+3;
	private static final int MENU_SORT_DATE = Menu.FIRST+4;
	private static final int MENU_SORT_PRIORITY = Menu.FIRST+5;
	private static final int MENU_SORT_STATUS = Menu.FIRST+6;	
	private static final int MENU_SORT_SO = Menu.FIRST+7;
	private static final int MENU_SORT_CNAME = Menu.FIRST+8;
	private static final int MENU_SORT_PRODUCT = Menu.FIRST+9;
	private static final int MENU_SORT_ETA = Menu.FIRST+10;
	private static final int MENU_MORE= Menu.FIRST+11;
	
	final Handler handlerForInsertExceptErrorDataCall = new Handler();
	final Handler handlerForInsertNewDataCall = new Handler();
	final Handler handlerRefresh = new Handler();
	private static String errRefId = "";
	private String errMsg="";
	private int colId = -1;
	
	//private MyTaskListAdapter taskListAdapter;
	private ServiceProTaskDetailsPersistent taskDbObj = null;
	private static ServiceProErrorMessagePersistent errorDbObj = null;
	private ServiceProOutputConstraints category = null;
	private ServiceProOutputConstraints categoryCopy = null;
	private ServiceProDBAdapter dbAdapterObj = null;
	private static Context ctx;
	
	private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5, tableHeaderTV6, tableHeaderTV7;
    private static int dispwidth = 300;
    private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5, headerWidth6, headerWidth7;
    private ImageView[] priorityIcon;
    private ImageView[] statusIcon;
    private ImageView[] errstatusIcon;
    private TextView[] custLocTxtView;
    private TableLayout colTableLayout = null;
    private String taskErrorMsgStr="", taskErrorType="";
    
    private ArrayList statusListObj = new ArrayList();
    private ArrayList statusFollowListObj = new ArrayList();
    private ArrayList ConfList = new ArrayList();
    private Vector documentsVect = new Vector();
    private Vector confCollecVect = new Vector();
    private Vector confSparesVect = new Vector();
    private Vector confProductVect = new Vector();
    private Vector confCauseCodeVect = new Vector();
    private Vector confCauseGroupVect = new Vector();
    private Vector confSympGroupVect = new Vector();
    private Vector confSympCodeVect = new Vector();
    private Vector confProbGroupVect = new Vector();
    private Vector confProbCodeVect = new Vector();
    private Vector confMattEmpVect = new Vector();
    private ArrayList attachVect = new ArrayList();
    
    final Handler handlerForLayout = new Handler();
    
	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
	private SoapObject requestSoapObj = null;
	private int statuslistCount = 0,statusflowlistCount = 0,activityListCount = 0 ,codegrplistCount = 0,codelistListCount = 0,probcodegrpCount = 0,probcodeListCount = 0, causecodegrplistCount = 0,causecodeListCount = 0,empmattListCount = 0;
	private String  statuslistType = "",statusflowlistType = "",activityListType = "" ,codegrplistType = "",codelistListType = "",probcodegrpType= "",probcodeListType = "",diogstatusType= "";	
	private String  causecodegrplistType = "",causecodeListType = "",empmattListType = "";
	private String  doclistType = "",attchmntType = "" ,actvtyType = "",spareType = "",cnfrmtnType= "",diogtaskType="";
	private int  doclistcount = 0,attchmntcount = 0 ,actvtycount = 0,sparecount = 0,cnfrmtncount= 0,diogtaskcount= 0,diogstatuscount= 0;
	
	//PINCH GESTURE
	private static final String TAG = "Touch";

	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
			ServiceProConstants.setWindowTitleTheme(this);   
			setContentView(R.layout.taskmaintbl);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.taskmaintbl); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SAPTASK_MAINSCR_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			if(ServiceProConstants.docvectlist!=null){
				ServiceProConstants.docvectlist.clear();
			}
			SharedPreferences settings = getSharedPreferences(ServiceProConstants.PREFS_NAME_FOR_SERVICE_TASKS, 0);      
			flag_pref = settings.getBoolean(ServiceProConstants.PREFS_KEY_SERVICE_TASKS_FOR_MYSELF_GET, false);		
			
			
			searchET = (EditText)findViewById(R.id.searchBEF);
			searchET.setText(searchStr);
			searchET.addTextChangedListener(this);  
						
			searchET = (EditText)findViewById(R.id.searchBEF);
			searchET.setText(searchStr);
			searchET.addTextChangedListener(this);  
			ctx = this.getApplicationContext();
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			ServiceProConstants.SERVICEPRO_MOBILE_IMEI = ServiceProConstants.getMobileIMEI(this);
			
			clearAllTaskListArray();
			
			ServiceProConstants.getMobileIMEI(ServiceProTaskMainScreenForTablet.this);
			internetAccess = ServiceProConstants.checkInternetConn(ServiceProTaskMainScreenForTablet.this);
			
			if(dbAdapterObj == null)
				dbAdapterObj = new ServiceProDBAdapter(this);			
			try{
				if(this.getIntent() != null){
					errRefId = this.getIntent().getStringExtra(ServiceProConstants.QUEUE_ERR_APPREFID);
					errMsg = this.getIntent().getStringExtra(ServiceProConstants.QUEUE_ERR_MSG);
					colId = this.getIntent().getIntExtra(ServiceProConstants.QUEUE_COLID, -1);
					if((errRefId != null && errRefId.length() > 0) && (errMsg != null && errMsg.length() > 0)){
						ServiceProConstants.showLog("errRefId:"+errRefId);
						ServiceProConstants.showLog("errMsg:"+errMsg);
						ServiceProConstants.showLog("colId:"+colId);
					}
					ServiceProConstants.showLog("colId:"+colId);	
                	if(colId != -1){
                		ServiceProConstants.stopNotificationAlert(colId);
                	}
				}
			}
			catch (Exception de) {
	        	ServiceProConstants.showErrorLog("Error in getIntent:"+de.toString());
	        }
			
			Thread t = new Thread() 
			{
	            public void run() 
				{
        			try{
        				layTaskListTableHeader();
        			} catch (Exception e) {
        				ServiceProConstants.showErrorLog("Error in oncreate layTaskListTableHeader:"+e.toString());
        			}
        			handlerForLayout.post(displayData);
				}
			};
	        t.start();				
		}
		catch (Exception de) {
        	ServiceProConstants.showErrorLog("Error in Taskmainscreen oncreate:"+de.toString());
        	if(taskDbObj != null)
		    	taskDbObj.closeDBHelper();
        }
	}
	
	final Runnable displayData = new Runnable(){
	    public void run()
	    {
	    	try{
	    		if(internetAccess){
					if(category != null)
	                    category = null;								
					/* DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");	                        				   
					   Calendar cal = Calendar.getInstance();	                        				 
					   SapGenConstants.showLog(" current date : "+dateFormat.format(cal.getTime()));
					   dateStr = dateFormat.format(cal.getTime());
					   strtStr = "+"+"START PROCESSING DEVICE"+dateStr+"\n"+"EVENT:SERVICE-DOX-CONTEXT-DATA-GET"+"\n"+"API-BEGIN-TIME DEVICE"+dateStr;
					   diogList.add(strtStr); */
			    		initStatusSoapConnection();										
				}
				else{
					getLDBData();
				}		
	    		/*if(internetAccess){
    				if(category != null)
                        category = null;    				
    				initSoapConnection();				
    			}
    			else{
    				getLDBData();
    			}*/			
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }	    
    };
       
    private void DiogAction() {
    	final CharSequence[] items = {" Refresh "};
    	builder = new AlertDialog.Builder(this);
    	builder.setTitle("Options");
    	    //alert.setMessage("Refresh");
    	   	   
    	builder.setSingleChoiceItems(items, -1,new DialogInterface.OnClickListener() { 
	        		public void onClick(DialogInterface dialog, int which) {	  	        			
	        			SalesOrderConstants.showLog("which : "+which);
	        			alertDialog.dismiss();
	        			flag_pref =false;
	        			initStatusSoapConnection();
	        			//initSoapConnection();	        			
	        		}
	        	});
    	    
    	  /*  alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int whichButton) {
    	         input.setText("hi");
    	        // Do something with value!
    	      }
    	    });

    	      alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	      public void onClick(DialogInterface dialog, int whichButton) {
    	          // Canceled.
    	      }
    	    });*/
    	alertDialog = builder.create();    		  
 		alertDialog.show();
		
	}//DiogAction
    
    
	public void getLDBTaskList(){
		try {
			if(taskDbObj != null)
				taskDbObj =null;			
				taskDbObj = new ServiceProTaskDetailsPersistent(this);		
			
			if(taskDbObj != null){				
				clearAllTaskListArray();
			    
				allTaskList = taskDbObj.getTaskDetails();
				ServiceProConstants.showLog("allTaskList size from database:"+allTaskList.size());			
				if(categoryCopy != null)
					categoryCopy = null;	
				
				for(int i = 0; i < allTaskList.size(); i++){  
			    	categoryCopy = ((ServiceProOutputConstraints)allTaskList.get(i));
			        if(categoryCopy != null){
			        	taskList.add(categoryCopy);
			        }
			    }			
				categoryCopy = (ServiceProOutputConstraints)taskList.get(0);
				String etadateStr = categoryCopy.getZzetaDate().toString().trim();
				ServiceProConstants.showLog("etadateStr: "+etadateStr);
				errorAvalForThisUserTask();
				ServiceProConstants.showLog("taskList size:"+taskList.size());
			}			
			if(taskDbObj != null)
				taskDbObj.closeDBHelper();
			
			if(taskList.size() == 0)
				ServiceProConstants.showErrorDialog(this, getResources().getString(R.string.SERVICEPRO_TASK_LIST_ERROR_MSG_OFFLINE));
		} catch (Exception esgg) {
			ServiceProConstants.showErrorLog("Error in getLDBTaskList :"+esgg.toString());
		}
	}//fn getLDBTaskList
	
	private void clearAllTaskListArray(){
		try{
	    	if(taskList != null)
            	taskList.clear();
	    	
            if(allTaskList != null)
            	allTaskList.clear();
		}
		catch(Exception sgg){
			ServiceProConstants.showErrorLog("Error in clearAllTaskListArray :"+sgg.toString());
		}
	}//fn clearAllTaskListArray
	
	private void layTaskListTableHeader(){
		try{
			tableHeaderTV1 = (TextView)findViewById(R.id.TableHeaderTV1);
			tableHeaderTV1.setGravity(Gravity.LEFT);
			tableHeaderTV1.setPadding(10,5,5,5);
			
			tableHeaderTV2 = (TextView)findViewById(R.id.TableHeaderTV2);
			tableHeaderTV2.setGravity(Gravity.LEFT);
			tableHeaderTV2.setPadding(10,5,5,5);
			tableHeaderTV2.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.TASK_SORT_NAME);
				}
			});
			SpannableString underlinedStr = ServiceProConstants.getUnderlinedString(tableHeaderTV2.getText().toString());
		    tableHeaderTV2.setText(underlinedStr);
		    
			tableHeaderTV3 = (TextView)findViewById(R.id.TableHeaderTV3);
			//tableHeaderTV3.setGravity(Gravity.CENTER);
			tableHeaderTV3.setGravity(Gravity.LEFT);
			tableHeaderTV3.setPadding(10,5,5,5);
			tableHeaderTV3.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.TASK_SORT_ETA);
				}
			});
			
			tableHeaderTV4 = (TextView)findViewById(R.id.TableHeaderTV4);
			tableHeaderTV4.setGravity(Gravity.LEFT);
			tableHeaderTV4.setPadding(10,5,5,5);
			tableHeaderTV4.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.TASK_SORT_SO);
				}
			});
			SpannableString underlinedStr1 = ServiceProConstants.getUnderlinedString(tableHeaderTV4.getText().toString());
			tableHeaderTV4.setText(underlinedStr1);
			
			tableHeaderTV5 = (TextView)findViewById(R.id.TableHeaderTV5);
			tableHeaderTV5.setGravity(Gravity.LEFT);
			tableHeaderTV5.setPadding(10,5,5,5);
			tableHeaderTV5.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.TASK_SORT_CNAME);
				}
			});
			
			tableHeaderTV6 = (TextView)findViewById(R.id.TableHeaderTV6);
			tableHeaderTV6.setGravity(Gravity.LEFT);
			tableHeaderTV6.setPadding(10,5,5,5);
			tableHeaderTV6.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.TASK_SORT_PRODUCT);
				}
			});
			
			tableHeaderTV7 = (TextView)findViewById(R.id.TableHeaderTV7);
			tableHeaderTV7.setGravity(Gravity.LEFT);
			tableHeaderTV7.setPadding(10,5,5,5);
			tableHeaderTV7.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.TASK_SORT_DATE);
				}
			});
						
			ViewTreeObserver vto1 = tableHeaderTV1.getViewTreeObserver();
	        vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV1.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth1 = tableHeaderTV1.getWidth();
	                ServiceProConstants.showLog("tableHeaderTV1 Width1 : "+headerWidth1+" : "+tableHeaderTV1.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto2 = tableHeaderTV2.getViewTreeObserver();
	        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV2.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth2 = tableHeaderTV2.getWidth();
	                ServiceProConstants.showLog("tableHeaderTV2 Width1 : "+headerWidth2+" : "+tableHeaderTV2.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto3 = tableHeaderTV3.getViewTreeObserver();
	        vto3.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV3.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth3 = tableHeaderTV3.getWidth();
	                ServiceProConstants.showLog("tableHeaderTV3 Width1 : "+headerWidth3+" : "+tableHeaderTV3.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto4 = tableHeaderTV4.getViewTreeObserver();
	        vto4.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV4.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth4 = tableHeaderTV4.getWidth();
	                ServiceProConstants.showLog("tableHeaderTV4 Width1 : "+headerWidth4+" : "+tableHeaderTV4.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto5 = tableHeaderTV5.getViewTreeObserver();
	        vto5.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV5.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth5 = tableHeaderTV5.getWidth();
	                ServiceProConstants.showLog("tableHeaderTV5 Width1 : "+headerWidth5+" : "+tableHeaderTV5.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto6 = tableHeaderTV6.getViewTreeObserver();
	        vto6.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV6.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth6 = tableHeaderTV6.getWidth();
	                ServiceProConstants.showLog("tableHeaderTV6 Width1 : "+headerWidth6+" : "+tableHeaderTV6.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto7 = tableHeaderTV7.getViewTreeObserver();
	        vto7.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV7.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth7 = tableHeaderTV7.getWidth();
	                ServiceProConstants.showLog("tableHeaderTV7 Width1 : "+headerWidth7+" : "+tableHeaderTV7.getMeasuredWidth());	                		
	            }
	        });
	        
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in layTaskListTableHeader : "+sfg.toString());
		}
	}//fn layTaskListTableHeader
	
	
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub	
	}
	
	public void afterTextChanged(Editable s) { 
		searchItemsAction(s.toString());
	} 
	
	private void searchItemsAction(String match){  
        try{       
            searchStr = match;
            String mattStr = "";
            String strValue = null, data = "";
            ServiceProOutputConstraints category = null;
            if((allTaskList != null) && (allTaskList.size() > 0)){
                if((!match.equalsIgnoreCase("")) && (match.length() >= 1)){
                	if((taskList != null) && (taskList.size() > 0)){
                		taskList.clear();
                	}                	
                    for(int i = 0; i < allTaskList.size(); i++){ 
                    	strValue = null;
                    	data = "";
                        mattStr = "";
                        category = null;
    					category = (ServiceProOutputConstraints)allTaskList.get(i);
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
                    	strValue = data;
                        if(category != null){
                            mattStr = strValue.trim().toLowerCase();
                            match = match.toLowerCase();
                            if((mattStr.indexOf(match) >= 0)){
                            	taskList.add(category);
                            }
                            else{
                            	data = "";
                            	if(category.getObjectId().toString().length() > 0){
                            		data += category.getObjectId().toString();
                            	}  
                            	strValue = data;
                            	mattStr = strValue.trim().toLowerCase();
                                match = match.toLowerCase();
                                if((mattStr.indexOf(match) >= 0)){
                                	taskList.add(category);
                                }
                            }
                        }
                    }//for 
                    drawSubLayout();
                }
                else{
                	taskList.clear();
                    for(int i = 0; i < allTaskList.size(); i++){  
                    	category = null;
    					category = (ServiceProOutputConstraints)allTaskList.get(i);
                        
                        if(category != null){
                        	taskList.add(category);
                        }
                    }
                    drawSubLayout();
                }
            }//if
            else
                return;
        	//ServiceProConstants.showLog("Calling find!");
        }//try
        catch(Exception we){
            we.printStackTrace();
        }
    }//fn searchItemsAction  
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_SORT_CITY, 0, "Sort by City");		
		menu.add(0, MENU_SORT_DATE, 0, "Sort by Start Date");
		menu.add(0, MENU_SORT_PRIORITY, 0, "Sort by Priority");
		menu.add(0, MENU_SORT_STATUS, 0, "Sort by Status");
		menu.add(0, MENU_COLLLIST, 0, "Tasks for other Rep.");
		menu.add(0, MENU_MORE, 0, "More..");
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
			case MENU_COLLLIST: {
				gotoColleagueList();
		        break;
			}	
			case MENU_SORT_CITY: {
				sortItemsAction(ServiceProConstants.TASK_SORT_CITY);
		        break;
			}
			case MENU_SORT_NAME: {
				sortItemsAction(ServiceProConstants.TASK_SORT_NAME);
		        break;
			}
			case MENU_SORT_DATE: {
				sortItemsAction(ServiceProConstants.TASK_SORT_DATE);
		        break;
			}
			case MENU_SORT_PRIORITY: {
				sortItemsAction(ServiceProConstants.TASK_SORT_PRIORITY);
		        break;
			}
			case MENU_SORT_STATUS: {
				sortItemsAction(ServiceProConstants.TASK_SORT_STATUS);
		        break;
			}
			case MENU_SORT_SO: {
				sortItemsAction(ServiceProConstants.TASK_SORT_SO);
		        break;
			}
			case MENU_SORT_CNAME: {
				sortItemsAction(ServiceProConstants.TASK_SORT_CNAME);
		        break;
			}
			case MENU_SORT_PRODUCT: {
				sortItemsAction(ServiceProConstants.TASK_SORT_PRODUCT);
		        break;
			}
			case MENU_SORT_ETA: {
				sortItemsAction(ServiceProConstants.TASK_SORT_ETA);
		        break;
			}
			case MENU_MORE: {
				DiogAction();
		        break;
			}
			
	    }
		return super.onOptionsItemSelected(item);
	}
	
	private void gotoColleagueList(){
		try {
        	Intent intent = new Intent(this, ServiceProColleagueListScreen.class);
        	intent.putExtra("ColleagueViewOption", ServiceProConstants.VIEWCOLLEAGUELIST);
			startActivityForResult(intent, ServiceProConstants.COLLEAGUE_LIST_SCREEN);
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in gotoColleagueList:"+e.getMessage());
		}
	}//fn gotoColleagueList
			
	private void initStatusSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
        	String saptimeStr = getDateTime();
        	String sapcntxttimeStr = "+"+"START PROCESSING DEVICE"+saptimeStr+"\n"+"EVENT:SERVICE-DOX-CONTEXT-DATA-GET"+"\n"+"API-BEGIN-TIME DEVICE"+saptimeStr;
 		   	diogList.add(sapcntxttimeStr);	
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[4];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
                        
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            if(!flag_pref){           	
            	C0[2].Cdata = "EVENT[.]SERVICE-DOX-CONTEXT-DATA-GET[.]VERSION[.]0[.]RESPONSE-TYPE[.]FULL-SETS";	
			}else{				
				C0[2].Cdata = "EVENT[.]SERVICE-DOX-CONTEXT-DATA-GET[.]VERSION[.]0";
			}
            C0[3].Cdata = saptimeStr ;
           // C0[2].Cdata = "EVENT[.]SERVICE-DOX-CONTEXT-DATA-GET[.]VERSION[.]0";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog("Request:"+request.toString());
            
            respType = ServiceProConstants.RESP_TYPE_GET_STATUS;
            doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in initStatusSoapConnection : "+asd.toString());
        }
    }//fn initStatusSoapConnection
	
	private void initSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
        	String saptimeStr = getDateTime();
        	String soapbeginStr = "+"+"API-FOR-EVENT:SERVICE-DOX-FOR-EMPLY-BP-GET"+"\n"+"API-BEGIN-TIME DEVICE"+saptimeStr;
 		   	diogList.add(soapbeginStr);	
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[3];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
                        
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameterForDiagnosis(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            if(!flag_pref){           	
            	C0[2].Cdata = "EVENT[.]SERVICE-DOX-FOR-EMPLY-BP-GET[.]VERSION[.]0[.]RESPONSE-TYPE[.]FULL-SETS";	
			}else{				
				C0[2].Cdata = "EVENT[.]SERVICE-DOX-FOR-EMPLY-BP-GET[.]VERSION[.]0";
			}
            //C0[2].Cdata = "EVENT[.]SERVICE-DOX-FOR-EMPLY-BP-GET[.]VERSION[.]0";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog("Request:"+request.toString());
            respType = ServiceProConstants.RESP_TYPE_GET_TASK;
            //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL);
            doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
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
       
    /*final Runnable getNetworkResponseRunnable = new Runnable(){
	    public void run(){
	    	try{
	    		SapGenConstants.showLog("Soap Env value : "+resultSoap);
	    		if(resultSoap != null){
	    			updateReportsConfirmResponse(resultSoap);
	    		}
	    		else{
	    			getLDBTaskList();
        			ServiceProConstants.showLog("Task from LDB");    			
	    		}
	    		if(pdialog != null)
        			pdialog = null;
        		
    			if(pdialog == null)
    				ServiceProTaskMainScreenForTablet.this.runOnUiThread(new Runnable() {
                        public void run() {
                        	pdialog = ProgressDialog.show(ServiceProTaskMainScreenForTablet.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
                        	new Thread() {
                        		public void run() {
                        			try{
                        				updateReportsConfirmResponse(resultSoap);
                        				sleep(2000);
                        			} catch (Exception e) {  }
                 				}
                        	}.start();
                        }
                    });
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };*/
    
    final Runnable getNetworkResponseRunnable = new Runnable(){
	    public void run(){
	    	try{
	    		SapGenConstants.showLog("Soap Env value : "+resultSoap);
	    		if(respType == ServiceProConstants.RESP_TYPE_GET_STATUS){
	    			if(pdialog != null)
	        			pdialog = null;
	        		
	    			if(pdialog == null){
	    				ServiceProTaskMainScreenForTablet.this.runOnUiThread(new Runnable() {
	                        public void run() {
	                        	pdialog = ProgressDialog.show(ServiceProTaskMainScreenForTablet.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
	                        	new Thread() {
	                        		public void run() {
	                        			try{                        				
	                    	    			updateStatusConfirmResponseForRefresh(resultSoap);	                        				
	                        				sleep(2000);
	                        			} catch (Exception e) {  }
	                 				}
	                        	}.start();
	                        }
	                    });
	    			}
	    		}
		    	else if(respType == ServiceProConstants.RESP_TYPE_GET_TASK){
		    		if(pdialog != null)
	        			pdialog = null;
	        		
	    			if(pdialog == null){
	    				ServiceProTaskMainScreenForTablet.this.runOnUiThread(new Runnable() {
	                        public void run() {
	                        	pdialog = ProgressDialog.show(ServiceProTaskMainScreenForTablet.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
	                        	new Thread() {
	                        		public void run() {
	                        			try{                        				
	                        				updateReportsConfirmResponse(resultSoap);                           				
	                        				sleep(2000);
	                        			} catch (Exception e) {  }
	                 				}
	                        	}.start();
	                        }
	                    });
	    			}
				}
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };
    
    public void updateStatusConfirmResponseForRefresh(SoapObject soap){	   	
		boolean errorflag = false, resMsgErr = false;
		String finalString2="";
        try{                 	
            emptyAllActivityVectors();
            
            String saptimeStr = getDateTime();
            String strtparsStr="Start Parsing- "+saptimeStr;	
 		   	diogList.add(strtparsStr);	           
        	if(soap != null){
        		ServiceProConstants.soapResponse(this, soap, false);
            	taskErrorMsgStr = ServiceProConstants.SOAP_RESP_MSG;
            	taskErrorType = ServiceProConstants.SOAP_ERR_TYPE;
            	ServiceProConstants.showLog("On soap response : "+soap.toString());
            	String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg);   
            	ServiceProConstants.showLog("resMsgErr : "+resMsgErr);

            	StatusOpConstraints serStatusListObj = null;
            	StatusFollowOpConstraints serStatusFollowListObj = null;   
    	        SOServiceActListOpConstraints serActListObj = null;
    	        SOCodeGroupOpConstraints codeGroupObj = null;
    	        SOCodeListOpConstraints codeListObj = null;
    	        SOMattEmpListOpConstraints mattEmpObj = null;    	
    	        	
    	        	            	            
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
	                    	 ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
	                        if(j > 1 && j<=12){
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
	                            int index1 = 0;
	                            while (indexB != -1) {
	                                res = result.substring(indexA, indexB);
	                                resArray[resC] = res;
	                                indexA = indexB + delimeter.length();
	                                indexB = result.indexOf(delimeter, indexA);
	                                if(resC == 0){
                                    	docTypeStr = res;
                                    }
	                                if(resC == 1){
	                                	  String[] respStr = res.split(";");
	                                	  if(respStr.length >= 1){
	                                		  String respTypeData = respStr[0];
	 	                                    	ServiceProConstants.showLog("respTypeData : "+respTypeData);
	 	                                    	index1 = respTypeData.indexOf("=");
	 	                                    	index1 = index1+1;
	 	                                        String respType = respTypeData.substring(index1, respTypeData.length());
	 	                                    	ServiceProConstants.showLog("respType : "+respType);
	 	                                    	
	 	                                    	String rowCountStrData = respStr[1];
	 	                                    	ServiceProConstants.showLog("rowCountStrData : "+rowCountStrData);
	 	                                    	index1 = rowCountStrData.indexOf("=");
	 	                                    	index1 = index1+1;
	 	                                        String rowCount = rowCountStrData.substring(index1, rowCountStrData.length());
	                                	  
	                                //resC++;
	                            //}
	                            int endIndex = result.lastIndexOf(';');
	                            resArray[resC] = result.substring(indexA,endIndex);
	                            if(docTypeStr.equalsIgnoreCase("ZGSXCAST_STTS10")){
	                            	
	                            	statuslistCount = Integer.parseInt(rowCount);
                             		statuslistType = respType;
	                                
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXCAST_STTSFLOW10")){
	                            	
	                            	statusflowlistCount = Integer.parseInt(rowCount);
	                            	statusflowlistType = respType;
	                                
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCACTVTYLIST10")){
	                            	activityListCount = Integer.parseInt(rowCount);
                             		activityListType = respType;
	                               
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SYMPTMCODEGROUPLIST10")){
	                            	codegrplistCount = Integer.parseInt(rowCount);
                             		codegrplistType = respType;
	                               
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SYMPTMCODELIST10")){
	                            	codelistListCount = Integer.parseInt(rowCount);
                             		codelistListType = respType;
	                               
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_PRBLMCODEGROUPLIST10")){
	                            	probcodegrpCount = Integer.parseInt(rowCount);
                             		probcodegrpType = respType;
	                                
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_PRBLMCODELIST10")){
	                            	probcodeListCount = Integer.parseInt(rowCount);
                             		probcodeListType = respType;
	                               
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_CAUSECODEGROUPLIST10")){
	                            	causecodegrplistCount = Integer.parseInt(rowCount);
                             		causecodegrplistType = respType;
	                               
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_CAUSECODELIST10")){
	                            	causecodeListCount = Integer.parseInt(rowCount);
                             		causecodeListType = respType;
	                               
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLLIST10")){
	                            	empmattListCount = Integer.parseInt(rowCount);
                             		empmattListType = respType;
	                                
	                            }
	                                	  }
	                                }
	                            resC++;
                                if(resC == 2)
                                	break;
	                            } 
	                        }
	                        if(j > 12){
	                        	 result = pii.getProperty(j).toString();
                                 //SapGenConstants.showLog("Result j>4 : "+result);       
 	                            firstIndex = result.indexOf(delimeter);
 	                            eqIndex = result.indexOf("=");
 	                            eqIndex = eqIndex+1;
 	                            firstIndex = firstIndex + 3;
 	                            docTypeStr = result.substring(eqIndex, (firstIndex-3));	   
 	                           result = result.substring(firstIndex);
 	                          String finalString= result.replace(";", " "); 
	                            finalString2= finalString.replace("}", " "); 
	                           ServiceProConstants.showLog("finalString2"+finalString2);
                               //ServiceProConstants.showLog("Document Type : "+docTypeStr);
                               //ServiceProConstants.showLog("Result : "+result);
                               
                               resC = 0;
                               indexA = 0;
                               indexB = result.indexOf(delimeter);
                               while (indexB != -1) {
                                   res = result.substring(indexA, indexB);
                                   //ServiceProConstants.showLog(resC+" : "+res);
                                   resArray[resC] = res;
                                   indexA = indexB + delimeter.length();
                                   indexB = result.indexOf(delimeter, indexA);
                                   resC++;
                               }
                               
                               int endIndex = result.lastIndexOf(';');
                               resArray[resC] = result.substring(indexA, endIndex);
                                if(docTypeStr.equalsIgnoreCase("ZGSXCAST_STTS10")){
	                                if(serStatusListObj != null)
	                                	serStatusListObj = null;	                                    
	                                serStatusListObj = new StatusOpConstraints(resArray);	  
	                                if(serStatusListObj != null)
	                                	statusListObj.add(serStatusListObj);
	                            }
                                else if(docTypeStr.equalsIgnoreCase("ZGSXCAST_STTSFLOW10")){
	                                if(serStatusFollowListObj != null)
	                                	serStatusFollowListObj = null;	                                    
	                                serStatusFollowListObj = new StatusFollowOpConstraints(resArray);	  
	                                if(serStatusFollowListObj != null)
	                                	statusFollowListObj.add(serStatusFollowListObj);
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCACTVTYLIST10")){
	                            	if(serActListObj  != null)
	                            		serActListObj  = null;
	                                    
	                            	serActListObj  = new SOServiceActListOpConstraints (resArray);
	                                
	                            	if(serActListObj != null)
	                            		confProductVect.add(serActListObj);	  
	                            }
	                            else  if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SYMPTMCODEGROUPLIST10")){
	                                if(codeGroupObj  != null)
	                                	codeGroupObj  = null;
	                                    
	                                codeGroupObj  = new SOCodeGroupOpConstraints (resArray);
	                                
	                                if(confSympGroupVect != null)
	                                	confSympGroupVect.add(codeGroupObj);	  	                                	                              
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SYMPTMCODELIST10")){
	                            	if(codeListObj  != null)
	                            		codeListObj  = null;
	                                    
	                            	codeListObj  = new SOCodeListOpConstraints (resArray);
	                                
	                            	if(confSympCodeVect != null)
	                            		confSympCodeVect.add(codeListObj);	  
	                            } else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_PRBLMCODEGROUPLIST10")){
	                                if(codeGroupObj != null)
	                                	codeGroupObj = null;
	                                    
	                                codeGroupObj = new SOCodeGroupOpConstraints(resArray);
	                                
	                                if(confProbGroupVect != null)
	                                	confProbGroupVect.add(codeGroupObj);	  	                                	                               
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_PRBLMCODELIST10")){
	                            	if(codeListObj != null)
	                            		codeListObj = null;
	                                    
	                            	codeListObj = new SOCodeListOpConstraints(resArray);
	                                
	                            	if(confProbCodeVect != null)
	                            		confProbCodeVect.add(codeListObj);	  
	                            }else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_CAUSECODEGROUPLIST10")){
	                                if(codeGroupObj != null)
	                                	codeGroupObj = null;
	                                    
	                                codeGroupObj = new SOCodeGroupOpConstraints(resArray);
	                                
	                                if(codeGroupObj != null)
	                                	confCauseGroupVect.add(codeGroupObj);	  	                                	                              
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_CAUSECODELIST10")){
	                            	if(codeListObj != null)
	                            		codeListObj = null;
	                                    
	                            	codeListObj = new SOCodeListOpConstraints(resArray);
	                                
	                            	if(codeListObj != null)
	                            		confCauseCodeVect.add(codeListObj);	  
	                            } else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLLIST10")){
	                                if(mattEmpObj  != null)
	                                	mattEmpObj  = null;
	                                    
	                                mattEmpObj = new SOMattEmpListOpConstraints (resArray);
	                                
	                                if(mattEmpObj != null)
	                                	confMattEmpVect.add(mattEmpObj);	  	                               
	                            } else if(docTypeStr.equalsIgnoreCase("ZGSSMWST_DIAGNOSYSINFO01")){		                            		   
		                                diogList.add(finalString2);                                              	                               
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
            ServiceProConstants.showErrorLog("On updateTaskResponse : "+sff.toString());
            errorflag = true;
            stopProgressDialog();
        } 
        finally{ 
        	try{      
        		ServiceProConstants.showErrorLog("statusFollowListObj size : "+statusFollowListObj.size());
        		
	        	if(!errorflag){	        		
	        		if(statusListObj != null){
    	    			if(statuslistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((statuslistCount == 0) && statusListObj == null){
    	    					ServiceProDBOperations.deleteAllStatusListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI);
    	        			}
    	    				else if((statuslistCount > 0) && statusListObj != null){
    	    					ServiceProDBOperations.deleteAllStatusListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI);
    	    					insertStatusListDataIntoDB();
    	        			}
    	    			}
    	    			if(statuslistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((statuslistCount == 0) && statusListObj == null){
    	    					ServiceProDBOperations.deleteAllStatusListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI);
    	        			}
    	    				else if((statuslistCount > 0) && statusListObj == null){
    	    					//initDBConnection();
    	        			}
    	    				else if((statuslistCount > 0) && statusListObj != null){
    	    					ServiceProDBOperations.deleteAllStatusListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI);
    	    					insertStatusListDataIntoDB();
    	        			}
    	    			}
    	    		}//status full-delta sets
	        		if(statusFollowListObj.size()!=0){
    	    			if(statusflowlistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((statusflowlistCount == 0) && statusFollowListObj == null){
    	    					ServiceProDBOperations.deleteAllStatusListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSFOLLOWLIST_CONTENT_URI);
    	        			}
    	    				else if((statusflowlistCount > 0) && statusFollowListObj != null){
    	    					ServiceProDBOperations.deleteAllStatusListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSFOLLOWLIST_CONTENT_URI);
    	    					insertStatusListDataIntoDB();
    	        			}
    	    			}
    	    			if(statusflowlistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((statusflowlistCount == 0) && statusFollowListObj == null){
    	    					ServiceProDBOperations.deleteAllStatusFollowListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSFOLLOWLIST_CONTENT_URI);
    	        			}
    	    				else if((statusflowlistCount > 0) && statusFollowListObj == null){
    	    					//initDBConnection();
    	        			}
    	    				else if((statusflowlistCount > 0) && statusFollowListObj != null){
    	    					ServiceProDBOperations.deleteAllStatusFollowListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSFOLLOWLIST_CONTENT_URI);
    	    					insertStatusFollowListDataInToDB();
    	        			}
    	    			}
    	    		}//status follow full-delta sets	           
	        		if(confProductVect != null){
    	    			if(activityListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((activityListCount == 0) && (confProductVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfProductDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPRODUCTLIST_CONTENT_URI);
    	        			}
    	    				else if((activityListCount > 0) && (confProductVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfProductDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPRODUCTLIST_CONTENT_URI);
    	    					insertConfProductListDataIntoDB();
    	        			}
    	    			}
    	    			if(activityListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((activityListCount == 0) && (confProductVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfProductDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPRODUCTLIST_CONTENT_URI);
    	        			}
    	    				else if((activityListCount > 0) && (confProductVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((activityListCount > 0) && (confProductVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfProductDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPRODUCTLIST_CONTENT_URI);
    	    					insertConfProductListDataIntoDB();
    	        			}
    	    			}
    	    		}//confProductVect full-delta sets
	        		
	        		if(confSympGroupVect != null){
    	    			if(codegrplistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((codegrplistCount == 0) && (confSympGroupVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPGROUPLIST_CONTENT_URI);
    	        			}
    	    				else if((codegrplistCount > 0) && (confSympGroupVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPGROUPLIST_CONTENT_URI);
    	    					insertConfSympGroupListDataIntoDB();   	
    	        			}
    	    			}
    	    			if(codegrplistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((codegrplistCount == 0) && (confSympGroupVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPGROUPLIST_CONTENT_URI);
    	        			}
    	    				else if((codegrplistCount > 0) && (confSympGroupVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((codegrplistCount > 0) && (confSympGroupVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPGROUPLIST_CONTENT_URI);
    	    					insertConfSympGroupListDataIntoDB();   	
    	        			}
    	    			}
    	    		}//confSympCodeVect full-delta sets
		            
	        		if(confSympCodeVect != null){
    	    			if(codelistListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((codelistListCount == 0) && (confSympCodeVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfSympCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPCODELIST_CONTENT_URI);
    	        			}
    	    				else if((codelistListCount > 0) && (confSympCodeVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfSympCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPCODELIST_CONTENT_URI);
    	    					insertConfSympCodeListDataIntoDB();
    	        			}
    	    			}
    	    			if(codelistListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((codelistListCount == 0) && (confSympCodeVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfSympCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPCODELIST_CONTENT_URI);
    	        			}
    	    				else if((codelistListCount > 0) && (confSympCodeVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((codelistListCount > 0) && (confSympCodeVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfSympCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPCODELIST_CONTENT_URI);
    	    					insertConfSympCodeListDataIntoDB();
    	        			}
    	    			}
    	    		}//confSympCodeVect full-delta sets
	        		
	        		if(confProbGroupVect != null){
    	    			if(probcodegrpType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((probcodegrpCount == 0) && (confProbGroupVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBGROUPLIST_CONTENT_URI);
    	        			}
    	    				else if((probcodegrpCount > 0) && (confProbGroupVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBGROUPLIST_CONTENT_URI);
    	    					insertConfProbGroupListDataIntoDB();
    	        			}
    	    			}
    	    			if(probcodegrpType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((probcodegrpCount == 0) && (confProbGroupVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBGROUPLIST_CONTENT_URI);
    	        			}
    	    				else if((probcodegrpCount > 0) && (confProbGroupVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((probcodegrpCount > 0) && (confProbGroupVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBGROUPLIST_CONTENT_URI);
    	    					insertConfProbGroupListDataIntoDB();
    	        			}
    	    			}
    	    		}//confProbGroupVect full-delta sets
		            
	        		if(confProbCodeVect != null){
    	    			if(probcodeListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((probcodeListCount == 0) && (confProbCodeVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfProbCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBCODELIST_CONTENT_URI);
    	        			}
    	    				else if((probcodeListCount > 0) && (confProbCodeVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfProbCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBCODELIST_CONTENT_URI);
    	    					insertConfProbGroupListDataIntoDB();
    	        			}
    	    			}
    	    			if(probcodeListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((probcodeListCount == 0) && (confProbCodeVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfProbCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBCODELIST_CONTENT_URI);
    	        			}
    	    				else if((probcodeListCount > 0) && (confProbCodeVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((probcodeListCount > 0) && (confProbCodeVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfProbCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBCODELIST_CONTENT_URI);
    	    					insertConfProbGroupListDataIntoDB();
    	        			}
    	    			}
    	    		}//confProbCodeVect full-delta sets
	        		
	        		if(confCauseGroupVect != null){
    	    			if(causecodegrplistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((causecodegrplistCount == 0) && (confCauseGroupVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfCauseGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI);
    	        			}
    	    				else if((causecodegrplistCount > 0) && (confCauseGroupVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfCauseGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI);
    	    					insertConfCauseGroupListDataIntoDB();
    	        			}
    	    			}
    	    			if(causecodegrplistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((causecodegrplistCount == 0) && (confCauseGroupVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfCauseGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI);
    	        			}
    	    				else if((causecodegrplistCount > 0) && (confCauseGroupVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((causecodegrplistCount > 0) && (confCauseGroupVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfCauseGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI);
    	    					insertConfCauseGroupListDataIntoDB();
    	        			}
    	    			}
    	    		}//confProbCodeVect full-delta sets
	        		
	        		if(confCauseCodeVect != null){
    	    			if(causecodeListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((causecodeListCount == 0) && (confCauseCodeVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfCauseCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSECODELIST_CONTENT_URI);
    	        			}
    	    				else if((causecodeListCount > 0) && (confCauseCodeVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfCauseCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSECODELIST_CONTENT_URI);
    	    					insertConfCauseCodeListDataIntoDB();
    	        			}
    	    			}
    	    			if(causecodeListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((causecodeListCount == 0) && (confCauseCodeVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfCauseCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSECODELIST_CONTENT_URI);
    	        			}
    	    				else if((causecodeListCount > 0) && (confCauseCodeVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((causecodeListCount > 0) && (confCauseCodeVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfCauseCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSECODELIST_CONTENT_URI);
    	    					insertConfCauseCodeListDataIntoDB();
    	        			}
    	    			}
    	    		}//confProbCodeVect full-delta sets
	        		
	        		if(confMattEmpVect != null){
    	    			if(empmattListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((empmattListCount == 0) && (confMattEmpVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfMattEmpDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFMATTEMPLLIST_CONTENT_URI);
    	        			}
    	    				else if((empmattListCount > 0) && (confMattEmpVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfMattEmpDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFMATTEMPLLIST_CONTENT_URI);
    	    					insertConfMattEmpListDataIntoDB();
    	        			}
    	    			}
    	    			if(empmattListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((empmattListCount == 0) && (confMattEmpVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfMattEmpDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFMATTEMPLLIST_CONTENT_URI);
    	        			}
    	    				else if((empmattListCount > 0) && (confMattEmpVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((empmattListCount > 0) && (confMattEmpVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfMattEmpDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFMATTEMPLLIST_CONTENT_URI);
    	    					insertConfMattEmpListDataIntoDB();
    	        			}
    	    			}
    	    		}//confMattEmpVect full-delta sets
	        		/* ServiceOrdDocOpConstraints docsCategory = null;
		            if(documentsVect.size() > 0){
			            for(int i = 0; i < documentsVect.size(); i++){  
			            	docsCategory = ((ServiceOrdDocOpConstraints)documentsVect.get(i));
		                    if(docsCategory != null){
		    	        		ServiceProDBOperations.deleteAllDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI, docsCategory.getObjectId().toString().trim());
		    	        	}
		                }  	
		            }
		            insertConfListDataIntoDB();
		            	    	            
		            ServiceOrdDocOpConstraints spareDocsCategory = null;
		            if(confSparesVect.size() > 0){
			            for(int i = 0; i < confSparesVect.size(); i++){  
			            	spareDocsCategory = ((ServiceOrdDocOpConstraints)confSparesVect.get(i));
		                    if(docsCategory != null){
		    	        		ServiceProDBOperations.deleteAllConfSpareDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI, spareDocsCategory.getObjectId().toString().trim());
		    	        	}
		                }  	
		            }
		            insertConfSpareListDataIntoDB();
		            
		            ServiceFollDocOpConstraints followCategory = null;
		            if(confCollecVect.size() > 0){
			            for(int i = 0; i < confCollecVect.size(); i++){
			            	followCategory = ((ServiceFollDocOpConstraints)confCollecVect.get(i));
		                    if(followCategory != null){
		    	        		ServiceProDBOperations.deleteAllConfCollecDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, followCategory.getSRCDocObjId().toString().trim());
		    	        	}
		                }  	
		            }
		            insertConfCollecListDataIntoDB();*/
	        	}  
	            stopProgressDialog();
	            String saptimeStr = getDateTime();
	            String strtparsStr="Stop Parsing- "+saptimeStr;	
	 		   	diogList.add(strtparsStr);	  	
	 		  
	            this.runOnUiThread(new Runnable() {
	                public void run() {
	                	 DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");	                        				   
						   Calendar cal = Calendar.getInstance();	                        				 
						   SapGenConstants.showLog(" current date : "+dateFormat.format(cal.getTime()));
						   String date = dateFormat.format(cal.getTime());
						   String soapbeginStr = "+"+"API-FOR-EVENT:SERVICE-DOX-FOR-EMPLY-BP-GET"+"\n"+"API-BEGIN-TIME DEVICE"+date;
						   diogList.add(soapbeginStr); 
	                	initSoapConnection();
	                }
	            });
        	}
			catch (Exception de) {
				stopProgressDialog();
				this.runOnUiThread(new Runnable() {
	                public void run() {
	                	initSoapConnection();
	                }
	            });
	        	ServiceProConstants.showErrorLog("Error in Database Insert/update in updateStatusConfirmResponseForRefresh:"+de.toString());
	        }
        }
    }//fn updateStatusConfirmResponseForRefresh
    
	private void getSOAPViaHTTP(SoapSerializationEnvelope envelopeCE, String url){		
        try {                
            HttpTransportSE  androidHttpTransport = new HttpTransportSE (url);
            try{
            	androidHttpTransport.call(ServiceProConstants.SOAP_ACTION_URL, envelopeCE);
            }
            catch(org.xmlpull.v1.XmlPullParserException ex2){
                ServiceProConstants.showErrorLog("Data handling error : "+ex2);
                ServiceProConstants.showErrorDialog(this, ex2.toString());
                envelopeCE = null;
                return;
            }
            catch(IOException oex){
                final String extStr = oex.toString();
                ServiceProConstants.showErrorLog("Network error : "+extStr);
                envelopeCE = null;
                return;
            }
            catch(Exception ex){
            	final String extStr = ex.toString();
                ServiceProConstants.showErrorLog("Error in Sap Resp : "+ex.toString());
                final Context ctx = this;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        ServiceProConstants.showErrorDialog(ctx, extStr.toString());
                    }
                });
                envelopeCE = null;
                return;
            }
            
            if(envelopeCE != null){
                try{
                    if(envelopeCE.bodyIn != null)
                    	resultSoap = (SoapObject)envelopeCE.bodyIn; 
                    ServiceProConstants.showLog("Result : "+resultSoap.toString());
                }
                catch(Exception dgg){
                    ServiceProConstants.showErrorLog("Error in Envelope Null : "+dgg.toString());
                }
            }
        }
        catch (Throwable e) {
            ServiceProConstants.showErrorLog("Error in Soap Conn : "+e.toString());
        }
        finally {                     
            ServiceProConstants.showLog("========END OF LOG========");    
            this.runOnUiThread(new Runnable() {
                public void run() {
                    updateReportsConfirmResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP	
	
	
	public void updateReportsConfirmResponse(SoapObject soap){	
		boolean errorflag = false, resMsgErr = false;
		String finalString2 = "";
		ServiceFollDocOpConstraints followCategory = null;
		ServiceProOutputConstraints categoryObj = null;
		ServiceOrdDocOpConstraints docsCategory = null;	 	          
        ServiceProAttachmentOpConstraints attachCategory = null; 
        ServiceOrdDocOpConstraints spareDocsCategory = null;
        try{         	           	
            if(taskListObj != null)
            	taskListObj.clear();
            if(allTaskListObj != null)
            	allTaskListObj.clear();
            
            //emptyAllActivityVectors();
            emptyAllTaskRelatedVectors();
                
            String saptimeStr = getDateTime();
            String strtparsStr="Start Parsing- "+saptimeStr;	
 		   	diogList.add(strtparsStr);	
        	if(soap != null){
        		ServiceProConstants.soapResponse(this, soap, false);
            	taskErrorMsgStr = ServiceProConstants.SOAP_RESP_MSG;
            	taskErrorType = ServiceProConstants.SOAP_ERR_TYPE;
            	ServiceProConstants.showLog("On soap response : "+soap.toString());
            	String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg);   
            	ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
        		            		            
 	            clearAllTaskListArray();
	            
	            if(category != null)
                    category = null;	    
	            if(taskOrderListObj != null)
	            	taskOrderListObj.clear();
	            
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
	                    	 ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
	                        if(j > 1 && j<=6){
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
		                            int index1 = 0;
		                            while (indexB != -1) {
		                                res = result.substring(indexA, indexB);
		                                resArray[resC] = res;
		                                indexA = indexB + delimeter.length();
		                                indexB = result.indexOf(delimeter, indexA);
		                                if(resC == 0){
	                                    	docTypeStr = res;
	                                    }
		                                if(resC == 1){
		                                	  String[] respStr = res.split(";");
		                                	  if(respStr.length >= 1){
		                                		  String respTypeData = respStr[0];
		 	                                    	ServiceProConstants.showLog("respTypeData : "+respTypeData);
		 	                                    	index1 = respTypeData.indexOf("=");
		 	                                    	index1 = index1+1;
		 	                                        String respType = respTypeData.substring(index1, respTypeData.length());
		 	                                    	ServiceProConstants.showLog("respType : "+respType);
		 	                                    	
		 	                                    	String rowCountStrData = respStr[1];
		 	                                    	ServiceProConstants.showLog("rowCountStrData : "+rowCountStrData);
		 	                                    	index1 = rowCountStrData.indexOf("=");
		 	                                    	index1 = index1+1;
		 	                                        String rowCount = rowCountStrData.substring(index1, rowCountStrData.length());
		                                	  
		                                //resC++;
		                            //}
		                            int endIndex = result.lastIndexOf(';');
		                            resArray[resC] = result.substring(indexA,endIndex);
		                            if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCDCMNT10")){
	                                    	//ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
                                 		doclistcount = Integer.parseInt(rowCount);
                                 		doclistType = respType;
                                     }	                            
                                 	
		                            else if(docTypeStr.equalsIgnoreCase("ZGSXCAST_ATTCHMNT01")){
	                                    	//ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
                                 		attchmntcount = Integer.parseInt(rowCount);
                                 		attchmntType = respType;
                                     }	 
                                 	
		                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCACTVTY10")){
	                                    	//ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
                                 		actvtycount = Integer.parseInt(rowCount);
                                 		actvtyType = respType;
                                     }	                             
                                 	
		                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCSPARE10")){
	                                    	//ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
                                 		sparecount = Integer.parseInt(rowCount);
                                 		spareType = respType;
                                     }	
                                 	
		                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCCNFRMTN12")){
	                                    	//ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
                                 		cnfrmtncount = Integer.parseInt(rowCount);
                                 		cnfrmtnType = respType;
                                     }	  
                                 /*	
                                 	else if(docTypeStr.equalsIgnoreCase("ZGSSMWST_DIAGNOSYSINFO01")){
	                                    	//ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
                                 		diogtaskcount = Integer.parseInt(rowCount);
                                 		diogtaskType = respType;
                                     }	*/
		                                	  }
		                                }
		                            resC++;
	                                if(resC == 2)
	                                	break;
		                            } 
	                        } if(j > 6){
	                        	 result = pii.getProperty(j).toString();
                                 //SapGenConstants.showLog("Result j>4 : "+result);       
 	                            firstIndex = result.indexOf(delimeter);
 	                            eqIndex = result.indexOf("=");
 	                            eqIndex = eqIndex+1;
 	                            firstIndex = firstIndex + 3;
 	                            docTypeStr = result.substring(eqIndex, (firstIndex-3));	   
 	                           result = result.substring(firstIndex);
 	                          String finalString= result.replace(";", " "); 
	                            finalString2= finalString.replace("}", " "); 
	                           ServiceProConstants.showLog("finalString2"+finalString2);
                               //ServiceProConstants.showLog("Document Type : "+docTypeStr);
                               //ServiceProConstants.showLog("Result : "+result);
                               
                               resC = 0;
                               indexA = 0;
                               indexB = result.indexOf(delimeter);
                               while (indexB != -1) {
                                   res = result.substring(indexA, indexB);
                                   //ServiceProConstants.showLog(resC+" : "+res);
                                   resArray[resC] = res;
                                   indexA = indexB + delimeter.length();
                                   indexB = result.indexOf(delimeter, indexA);
                                   resC++;
                               }
                               
                               int endIndex = result.lastIndexOf(';');
                               resArray[resC] = result.substring(indexA, endIndex);
                               if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCDCMNT10")){
  	                        	 categoryObj = new ServiceProOutputConstraints(resArray);
  	                            	category = categoryObj;
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
  	                            	if(category.getNumberExtn().toString().length() > 0){
  	                            		data += ","+category.getNumberExtn().toString();
  	                            	}
  	                            	
  	                            		                            	
  	                            	ServiceProConstants.showLog("data: "+data);
                           		String desc;
                           		desc = "Status: "+category.getStatus()+"\nPriority: "+category.getPriority();
                           		desc += "\n"+category.getProcessTypeDescr()+": "+category.getProcessType()+" - "+category.getObjectId().trim()+" - Customer# "+category.getPartner().trim()+"\n";
  	                            	
                           		String eta_date = category.getZzetaDate().toString().trim();
                           		ServiceProConstants.showLog("eta_date!"+eta_date);
                           		if(eta_date != null && eta_date.length() > 0){
                               		if(!eta_date.equalsIgnoreCase("0000-00-00")){
                               			String eta_dat_time_value = category.getZzetaDate().toString().trim()+" "+category.getZzetaTime().toString().trim();
                               			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
       	                            	Date dateObj = curFormater.parse(eta_dat_time_value);
       	                            	Long dateLongValue = dateObj.getTime();    	                            	//Adding Calendar Event
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
       		                        				ServiceProConstants.addToCalendar(ServiceProTaskMainScreenForTablet.this, data, desc, dateLongValue, dateLongValue);
       		                        			}	      
       		                            	}
       	                            	}
                               		}
                           		}                	
  	                                if(category != null){
  	                                	taskListObj.add(category);
  	                                	allTaskListObj.add(category);
  	                                	taskOrderListObj.add(category.getObjectId().toString().trim());
  	                                } 
  		                            }
  		                            else  if(docTypeStr.equalsIgnoreCase("ZGSXCAST_ATTCHMNT01")){
  		                            	  if(attachCategory != null)
  			                                	attachCategory = null;
  			                                    
  			                                attachCategory = new ServiceProAttachmentOpConstraints(resArray);
  			                                if(attachCategory != null)
  			                                    attachVect.add(attachCategory);                          	                              
  		                            }
  		                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCACTVTY10")){
  		                            	 if(docsCategory != null)
  			                                    docsCategory = null;
  			                                    
  			                                docsCategory = new ServiceOrdDocOpConstraints(resArray);
  			                                if(docsCategory != null)
  			                                    documentsVect.add(docsCategory);
  			                              ServiceProConstants.docvectlist.addAll(documentsVect);
  		                            } else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCSPARE10")){
  		                            	 if(spareDocsCategory != null)
  		                            		spareDocsCategory = null;
  			                                    
  		                            	spareDocsCategory = new ServiceOrdDocOpConstraints(resArray);	                                
  			                                if(spareDocsCategory != null)
  			                                	confSparesVect.add(spareDocsCategory);	                                	                               
  		                            }
  		                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCCNFRMTN12")){
  		                            	 if(followCategory != null)
  			                                    followCategory = null;	                                    
  			                                
  			                                followCategory = new ServiceFollDocOpConstraints(resArray);
  			                                if(confCollecVect != null)
  			                                	confCollecVect.add(followCategory);                                              	                               
  	                            }
  		                        else if(docTypeStr.equalsIgnoreCase("ZGSSMWST_DIAGNOSYSINFO01")){		                        	
  			                               diogList.add(finalString2);                                              	                               
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
            ServiceProConstants.showErrorLog("On updateTaskResponse : "+sff.toString());
            errorflag = true;
        } 
        finally{        	
        	try{      
	        	if(!errorflag){
	        		SharedPreferences sharedPreferences = getSharedPreferences(ServiceProConstants.PREFS_NAME_FOR_SERVICE_TASKS, 0);    
	    			SharedPreferences.Editor editor = sharedPreferences.edit();    
	    			editor.putBoolean(ServiceProConstants.PREFS_KEY_SERVICE_TASKS_FOR_MYSELF_GET, true);    
	    			editor.commit();   	    
	        		//if(taskListObj.size() > 0){
	        		ServiceProConstants.showLog("taskListObj Size : "+taskListObj.size());
	        		ServiceProConstants.showLog("attachVect Size : "+attachVect.size());
	        		ServiceProConstants.showLog("documentsVect Size : "+documentsVect.size());
	        		ServiceProConstants.showLog("confSparesVect Size : "+confSparesVect.size());
	        		ServiceProConstants.showLog("confCollecVect Size : "+confCollecVect.size());
        			clearAllTaskListArray();      
        			if(taskListObj != null){
        				 for(int i = 0; i < taskListObj.size(); i++){  
         	            	categoryCopy = ((ServiceProOutputConstraints)taskListObj.get(i));
                             if(categoryCopy != null){
                             	taskList.add(categoryCopy);
                             }
                         }
         	            for(int i = 0; i < allTaskListObj.size(); i++){  
         	            	categoryCopy = ((ServiceProOutputConstraints)allTaskListObj.get(i));
                             if(categoryCopy != null){
                             	allTaskList.add(categoryCopy);
                             }
                         }        
         	            
         	      	 String saptimeStr = getDateTime();
			         String strtparsStr="Start Database Processing- "+saptimeStr;	
					 diogList.add(strtparsStr);	
					 
        				taskDbObj = new ServiceProTaskDetailsPersistent(this);
    	    			if(doclistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((doclistcount == 0) && (taskListObj.size()==0)){
    	    					taskDbObj.clearTaskTable();	
    	        			}
    	    				else if((doclistcount > 0) && (taskListObj.size()!=0)){
    	    					taskDbObj.clearTaskTable();	
    	    					insertNewData();
    	        			}
    	    			}
    	    			if(doclistType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((doclistcount == 0) && (taskListObj.size()==0)){
    	    					taskDbObj.clearTaskTable();	
    	        			}
    	    				else if((doclistcount > 0) && (taskListObj.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((doclistcount > 0) && (taskListObj.size()!=0)){
    	    					taskDbObj.clearTaskTable();	
    	    					insertNewData();
    	        			}
    	    			}
    	    			if(taskDbObj != null)
    	    		    	taskDbObj.closeDBHelper();
    	    		}//confProductVect full-delta sets
        			ServiceProConstants.showLog("1");
	        		if(attachVect != null){
    	    			if(attchmntType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((attchmntcount == 0) && (attachVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllAttachCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI);
    	        			}
    	    				else if((attchmntcount > 0) && (attachVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllAttachCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI);
    	    					insertAttachListDataIntoDB();
    	        			}
    	    			}
    	    			if(attchmntType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((attchmntcount == 0) && (attachVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllAttachCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI);
    	        			}
    	    				else if((attchmntcount > 0) && (attachVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((attchmntcount > 0) && (attachVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllAttachCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI);
    	    					insertAttachListDataIntoDB();
    	        			}
    	    			}
    	    		}//confSympCodeVect full-delta sets
	        		ServiceProConstants.showLog("2");
	        		if(documentsVect != null){
    	    			if(actvtyType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((actvtycount == 0) && (documentsVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI, docsCategory.getObjectId().toString().trim());
    	        			}
    	    				else if((actvtycount > 0) && (documentsVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI, docsCategory.getObjectId().toString().trim());
    	    					insertConfListDataIntoDB();
    	        			}
    	    			}
    	    			if(actvtyType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((actvtycount == 0) && (documentsVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI, docsCategory.getObjectId().toString().trim());
    	        			}
    	    				else if((actvtycount > 0) && (documentsVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((actvtycount > 0) && (documentsVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI, docsCategory.getObjectId().toString().trim());
    	    					insertConfListDataIntoDB();
    	        			}
    	    			}
    	    		}//confSympCodeVect full-delta sets
	        		ServiceProConstants.showLog("3");
	        		if(confSparesVect.size()!=0){	        				        			
    	    			if(spareType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((sparecount == 0) && (confSparesVect.size()==0)){    	    					
    	    					ServiceProDBOperations.deleteAllConfSpareDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI, spareDocsCategory.getObjectId().toString().trim());
    	        			}
    	    				else if((sparecount > 0) && (confSparesVect.size()!=0)){   	    					
    	    					ServiceProDBOperations.deleteAllConfSpareDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI, spareDocsCategory.getObjectId().toString().trim());
    	    					insertConfSpareListDataIntoDB();
    	        			}
    	    			}
    	    			if(spareType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((sparecount == 0) && (confSparesVect.size()==0)){   	    					
    	    					ServiceProDBOperations.deleteAllConfSpareDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI, spareDocsCategory.getObjectId().toString().trim());
    	        			}
    	    				else if((sparecount > 0) && (confSparesVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((sparecount > 0) && (confSparesVect.size()!=0)){    	    						
    	    					ServiceProDBOperations.deleteAllConfSpareDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI, spareDocsCategory.getObjectId().toString().trim());
    	    					insertConfSpareListDataIntoDB();
    	        			}
    	    			}
    	    		}//confProbGroupVect full-delta sets
	        		ServiceProConstants.showLog("4");
	        		if(confCollecVect.size()!=0){
    	    			if(cnfrmtnType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
    	    				if((cnfrmtncount == 0) && (confCollecVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfCollecDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, followCategory.getSRCDocObjId().toString().trim());
    	        			}
    	    				else if((cnfrmtncount > 0) && (confCollecVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfCollecDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, followCategory.getSRCDocObjId().toString().trim());
    	    					 insertConfCollecListDataIntoDB();
    	        			}
    	    			}
    	    			if(cnfrmtnType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){    	    				    	    				
    	    				if((cnfrmtncount == 0) && (confCollecVect.size()==0)){
    	    					ServiceProDBOperations.deleteAllConfCollecDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, followCategory.getSRCDocObjId().toString().trim());
    	        			}
    	    				else if((cnfrmtncount > 0) && (confCollecVect.size()==0)){
    	    					//initDBConnection();
    	        			}
    	    				else if((cnfrmtncount > 0) && (confCollecVect.size()!=0)){
    	    					ServiceProDBOperations.deleteAllConfCollecDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, followCategory.getSRCDocObjId().toString().trim());
    	    					 insertConfCollecListDataIntoDB();
    	        			}
    	    			}
    	    		}//confProbCodeVect full-delta sets
	        		 String saptimeStr = getDateTime();
			         String strtparsStr="Stop Database Processing- "+saptimeStr;	
					 diogList.add(strtparsStr);	
	        		ServiceProConstants.showLog("5");
    	          /* for(int i = 0; i < taskListObj.size(); i++){  
    	            	categoryCopy = ((ServiceProOutputConstraints)taskListObj.get(i));
                        if(categoryCopy != null){
                        	taskList.add(categoryCopy);
                        }
                    }
    	            for(int i = 0; i < allTaskListObj.size(); i++){  
    	            	categoryCopy = ((ServiceProOutputConstraints)allTaskListObj.get(i));
                        if(categoryCopy != null){
                        	allTaskList.add(categoryCopy);
                        }
                    }        		  */      	
    	            ServiceProConstants.showLog("6");
            		if(idList != null){
            			idList.clear();
            		}        		
            		ServiceProConstants.showLog("taskList Size : "+taskList.size());
            		ServiceProConstants.showLog("allTaskList Size : "+allTaskList.size());
            		ServiceProConstants.showLog("AttachList Size : "+attachVect.size());
            		
            	/*	ServiceProAttachmentOpConstraints attachCategory = null;
		            if(attachVect.size() > 0){
    	        		ServiceProDBOperations.deleteAllAttachCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI);
		            }
		            insertAttachListDataIntoDB();
            		
            		ServiceOrdDocOpConstraints docsCategory = null;
		            if(documentsVect.size() > 0){
			            for(int i = 0; i < documentsVect.size(); i++){  
			            	docsCategory = ((ServiceOrdDocOpConstraints)documentsVect.get(i));
		                    if(docsCategory != null){
		    	        		ServiceProDBOperations.deleteAllDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI, docsCategory.getObjectId().toString().trim());
		    	        	}
		                }  	
		            }
		            insertConfListDataIntoDB();
		            	    	            
		            ServiceOrdDocOpConstraints spareDocsCategory = null;
		            if(confSparesVect.size() > 0){
			            for(int i = 0; i < confSparesVect.size(); i++){  
			            	spareDocsCategory = ((ServiceOrdDocOpConstraints)confSparesVect.get(i));
		                    if(docsCategory != null){
		    	        		ServiceProDBOperations.deleteAllConfSpareDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI, spareDocsCategory.getObjectId().toString().trim());
		    	        	}
		                }  	
		            }
		            insertConfSpareListDataIntoDB();
		            
		            ServiceFollDocOpConstraints followCategory = null;
		            if(confCollecVect.size() > 0){
			            for(int i = 0; i < confCollecVect.size(); i++){
			            	followCategory = ((ServiceFollDocOpConstraints)confCollecVect.get(i));
		                    if(followCategory != null){
		    	        		ServiceProDBOperations.deleteAllConfCollecDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, followCategory.getSRCDocObjId().toString().trim());
		    	        	}
		                }  	
		            }
		            insertConfCollecListDataIntoDB();*/
            		
            		if(taskList.size() > 0){
    	        		for(int i=0;i<taskList.size();i++){
    	    				category = ((ServiceProOutputConstraints)taskList.get(i));
    	    	        	if(category != null){   
    	    	        		idList.add(category.getObjectId().toString().trim());
    	    	        	}	        		
    	    	    	}	        		
            		}        		        		
    	        	if(taskList.size() > 0){	        		
    	        		if(errorDbObj == null)
    	        			errorDbObj = new ServiceProErrorMessagePersistent(this);
    	        		if(taskDbObj == null)
    						taskDbObj = new ServiceProTaskDetailsPersistent(this);
    	        		
    	        		//deletion for error table, if error task id not available from SAP list.
    	        		if(idList != null && idList.size() > 0){
    	        			String[] idListStr = errorDbObj.getIdList();
    		        		if(idListStr != null && idListStr.length > 0){
    		        			for(int id=0;id<idListStr.length;id++){
    		        				if ( !idList.contains(idListStr[id]) ){
    		        					errorDbObj.deleteRow(idListStr[id]);
    		        					int colIdVal = Integer.parseInt(idListStr[id]);
    		        					try{
    		                    			if(colIdVal > 0){
    		                    				//setting status to queue process DB, if error task id not available from SAP list.
    		                    				SapQueueProcessorHelperConstants.updateSelectedRowStatusForServicePro(this, colIdVal, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_COMPLETED, ServiceProConstants.STATUS_UPDATE_API);
    		                    			}
    		                    		}
    		                            catch(Exception sff1){
    		                            	ServiceProConstants.showErrorLog("Error in updating queue process database: "+sff1.toString());
    		                            }      
    		        				}
    			        		}
    		        		}	   
    	        		}	        		     		
    	        		
    	        		if(idList != null && idList.size() > 0){
    	        			String[] idListStr = getImageFileList();
    		        		if(idListStr != null && idListStr.length > 0){
    		        			for(int id=0;id<idListStr.length;id++){
    		        				if(idListStr[id] != null){
	    		        				if ( !idList.contains(idListStr[id]) ){
	    		        					try{
	    		        						String imgId = idListStr[id].toString().trim();
	    		        						if(imgId != null){
			    		        					deleteImgFiles(imgId);
	    		        						}
		    		        				} catch (Exception e) {
		                        				ServiceProConstants.showErrorLog("Error in deleteImgFiles func calling:"+e.toString());
		                        			}
	    		        				}
    		        				}
    			        		}
    		        		}	   	    
    	        		}      		     		
    	        		
    	        		//int rowcount = errorDbObj.checkTableRow();
    	        		int rowcount = errorDbObj.checkTableRow();
    	        		if(rowcount > 0){
    	        			ServiceProConstants.showLog("Data available in Error Message Table!");
    	        			Thread t = new Thread() 
    	        			{
    	        	            public void run() 
    	        				{
                        			try{
                        				taskDbObj.deleteQueryExceptErrorId();
                        			} catch (Exception e) {
                        				ServiceProConstants.showErrorLog("Error in clearTaskTable:"+e.toString());
                        			}
                        			handlerForInsertExceptErrorDataCall.post(insertExceptErrorDataCall);
                 				}
    	        			};
    	        	        t.start();		
    	        		}/*else{
    	        			ServiceProConstants.showLog("No Data available in Error Message Table!");
    	        			Thread t = new Thread() 
    	        			{
    	        	            public void run() 
    	        				{
                        			try{
                        				taskDbObj.clearTaskTable();	
                        			} catch (Exception e) {
                        				ServiceProConstants.showErrorLog("Error in clearTaskTable:"+e.toString());
                        			}
                        			handlerForInsertNewDataCall.post(insertNewDataCall);
    	        				}
    	        			};
    	        	        t.start();			        			
    	        		}	   */
    	        		if(taskDbObj != null)
    	        			taskDbObj.closeDBHelper();
    	        		if(errorDbObj != null)
    	        			errorDbObj.closeDBHelper();	        		
    	        	}
    	        	errorAvalForThisUserTask();	
    	        	 String saptimeStr2 = getDateTime();
    	             String strtparsStr2="Stop Parsing- "+saptimeStr2;	
    	  		   	diogList.add(strtparsStr2);	
    	        	ServiceProTaskMainScreenForTablet.this.runOnUiThread(new Runnable() {
        	            public void run() {	 	        	            	
	    	        		 getLDBData();	  
	    	        		 String saptimeStr = getDateTime();
	    			         String strtparsStr="Stop Rendering- "+saptimeStr;	
	    					 diogList.add(strtparsStr);	
	    					 
	    						String sapendtimeStr = getDateTime();
	    			    		String endsaptimeStr="- API-END-TIME DEVICE"+sapendtimeStr+"\n"+"-"+"Stop PROCESSING DEVICE"+sapendtimeStr;	
	    					   diogList.add(endsaptimeStr);
        	            	//refreshList();	
        	            	if(ServiceProConstants.DIOG_FLAG==1)
        	            		DisplayDiogPopUp();
        	            }
        	        });   
    	        	ServiceProConstants.showLog("Task from SAP");
        		}else{
        			ServiceProTaskMainScreenForTablet.this.runOnUiThread(new Runnable() {
                        public void run() {
        		        	pdialog.setMessage(getResources().getString(R.string.SERVICEPRO_WAIT_TEXTS_UI));	        		        	
            				stopProgressDialog();	            				            				
            				 getLDBData();
            				 if(ServiceProConstants.DIOG_FLAG==1)
	        	            		DisplayDiogPopUp();	
    	        			//ServiceProConstants.showErrorDialog(ServiceProTaskMainScreenForTablet.this, getResources().getString(R.string.SERVICEPRO_TASK_LIST_ERROR_MSG_OFFLINE));
                        }
                    });
        		}
        	}
			catch (Exception de) {
				stopProgressDialog();
	        	ServiceProConstants.showErrorLog("Error in Database Insert/update in Taskmainscreen:"+de.toString());
	        }               
        }            		
    }//fn updateReportsConfirmResponse  
	
	public String getDateTime(){
		 String soapendtimeStr = "";
		try{			
			DateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd HHmmss");	                        				   
			Calendar cal3 = Calendar.getInstance();	                        				 
			SapGenConstants.showLog(" current date : "+dateFormat3.format(cal3.getTime()));
		    soapendtimeStr = dateFormat3.format(cal3.getTime());
		   // soapendtimeStr="- API-END-TIME DEVICE"+soapendtimeStr+"\n"+"-"+"Stop PROCESSING DEVICE"+soapendtimeStr;	
		}catch (Exception def) {			
        	ServiceProConstants.showErrorLog("Error in getDateTime:"+def.toString());
        }  		
		   return soapendtimeStr;
	}//getDateTime
	
	public class SOCustomerListAdapter extends BaseAdapter {	    			
	    LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    HashMap<String, String> checkdCustmap = null;
	    	
	    public SOCustomerListAdapter(Context context) {
	    	// Cache the LayoutInflate to avoid asking for a new one each time.
	    	mInflater = LayoutInflater.from(context);   
	    }
	    
	    public int getCount() {
	    	try {
	    		if(diogList != null)
	    			return diogList.size();
	    		//SalesOrderConstants.showLog("SOCheckedList size in adapter "+SOCheckedList.size());
	    	}
	    	catch (Exception e) {
	    		SalesOrderConstants.showErrorLog(e.getMessage());
	    	}
	    	return 0;
	    }
	        
        public Object getItem(int position) {
            return position;
        }
        
        public long getItemId(int position) {
            return position;
        }
		
        public View getView(final int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls        	
            // to findViewById() on each row.
            class ViewHolder {
            	TextView ctname;   
            	LinearLayout llitembg1;
            }
            
            ViewHolder holder;
            convertView = mInflater.inflate(R.layout.salesorderdialog, null);
            holder = new ViewHolder();
            holder.ctname = (TextView) convertView.findViewById(R.id.ctname);              
            holder.llitembg1 = (LinearLayout) convertView.findViewById(R.id.llitembg1);

            if(position%2 == 0)
				holder.llitembg1.setBackgroundResource(R.color.item_even_color);
			else
				holder.llitembg1.setBackgroundResource(R.color.item_odd_color);
            
            try {
            	if(diogList != null){              		
            		String spname = (String) diogList.get(position).toString();            		
            		 holder.ctname.setText(spname);			           	
            	} 
            }catch (Exception qw) {
            	SalesOrderConstants.showErrorLog("Error in ContactListAdapter : "+qw.toString());
			}
            
           return convertView;	           
		}
		
		public void removeAllTasks() {
            notifyDataSetChanged();
        } 		
	}//End of ContactListAdapter
	
	private void DisplayDiogPopUp() {
		TextView tv;
		ImageButton emailbtn;
		 ServiceProConstants.showLog("diogList size"+diogList.size());
    	 LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		  View layout;
		  
		  layout = inflater.inflate(R.layout.servicepro_dialog,
				  (ViewGroup) findViewById(R.id.listviewlineardialog2));		        		       		  
		 
		  ListView listview = (ListView)layout.findViewById(R.id.list3);
		  tv = (TextView)layout.findViewById(R.id.diogTV);
		  tv.setOnClickListener(tv_btnListener); 	
		  emailbtn = (ImageButton)layout.findViewById(R.id.showemailbtn);
		  emailbtn.setOnClickListener(email_btnListener); 	   
		  SOCustomerListAdapter = new SOCustomerListAdapter(this);
		  
			        		  
		  builder = new AlertDialog.Builder(this).setTitle("Gss Mobile Diognosis & Checks");	        		  	        		 
		  builder.setInverseBackgroundForced(true);
		  View view=inflater.inflate(R.layout.custom_servicpr_popup, null);
		  builder.setCustomTitle(view);	        		 
		  builder.setView(layout); 	        		
		  builder.setSingleChoiceItems(SOCustomerListAdapter, -1,new DialogInterface.OnClickListener() { 
        		public void onClick(DialogInterface dialog, int which) {	  	        			
        			ServiceProConstants.showLog("which : "+which);
        			/*selctdPos=which;
        			if(flagPos==1)
        				flagPos=0;
        			SalesOrderConstants.showErrorLog("Selected Position : "+selctdPos);	  	        			
        			SOCreationScreen(selctdPos);
        			SalesOrderConstants.showLog("selctdPos : "+selctdPos);
        			
        			alertDialog.dismiss();*/
        		}
        	});
		  alertDialog = builder.create();    		  
		  alertDialog.show();
		
	}//DisplayDiogPopUp
	
	 private OnClickListener tv_btnListener = new OnClickListener() {
	        public void onClick(View v) {  
	        	if(ServiceProConstants.DIOG_FLAG!=0)
	        		ServiceProConstants.DIOG_FLAG=0;
	        	alertDialog.dismiss();	
	        	
	        }
	    };
	    
	    private OnClickListener email_btnListener = new OnClickListener() {
	        public void onClick(View v) {          	        	
	        	showEmailActivity();   			
	        }
	    };
	 public void showEmailActivity() {
	    	try{    				
				for(int i=0;i<diogList.size();i++){
					linkText = linkText+diogList.get(i).toString()+"\n";
				}				
				String to = "gss.mobile@globalsoft-solutions.com";
				String subject = "GSS Mobile Diognosis & Checks";
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
				email.putExtra(Intent.EXTRA_SUBJECT, subject);
				email.putExtra(Intent.EXTRA_TEXT, linkText);
				email.setType("message/rfc822");   		 
				startActivity(Intent.createChooser(email, "Choose an Email client"));
	    	}
	    	catch(Exception adf){
	    		ServiceProConstants.showErrorLog("Error in showEmailActivity : "+adf.getMessage());
	    	}		
		}       
	 
	private void getLDBData(){
		try {
			 String saptimeStr = getDateTime();
	            String strtparsStr="Start Rendering- "+saptimeStr;	
	 		   	diogList.add(strtparsStr);	
			ServiceProTaskMainScreenForTablet.this.runOnUiThread(new Runnable() {
	            public void run() {	   
					getLDBTaskList();
					refreshList();	
					 if(ServiceProConstants.UPDATE_ERROR==1){
						  DisplayDiogPopUpOnError();
					  }
					ServiceProConstants.showLog("Task from LDB");
	            }
	        });   
		} catch (Exception de) {
			ServiceProConstants.showErrorLog("Error in getLDBData:"+de.toString());
		}
		
	}//fn updateReportsConfirmResponse
	
	final Runnable refreshCall = new Runnable(){
	    public void run()
	    {
	    	try{
	    		refreshList();
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }	   
    };
    
    private void refreshList(){
    	try{
    		/*ServiceProTaskMainScreenForTablet.this.runOnUiThread(new Runnable() {
	            public void run() {	*/
		    		stopProgressDialog();
			    	ServiceProConstants.showLog("Task List Size : "+taskList.size());
			        drawSubLayout();
	           /* }
	        });  */
	    }
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in refreshList:"+de.toString());
		}
    }//fn refreshList
	
	final Runnable insertNewDataCall = new Runnable(){
	    public void run()
	    {
	    	try{
	    		insertNewData();
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }	    
    };
    
	private void insertNewData(){
		ServiceProOutputConstraints category=null;
		try{
			if(taskDbObj != null)
				taskDbObj=null;
				taskDbObj = new ServiceProTaskDetailsPersistent(this);	
			int pos = -1;
			for(int i=0;i<taskList.size();i++){
				category = ((ServiceProOutputConstraints)taskList.get(i));
				pos = -1;
	        	if(category != null){   
				    if(taskDbObj != null){	
				    	//ServiceProConstants.showLog(" insert data 1");
				    	if(taskOrderListObj.contains(category.getObjectId().toString().trim())){
				    		int position=taskOrderListObj.indexOf(category.getObjectId().toString().trim()); 
				    		pos = position;
				    	}
				    	//ServiceProConstants.showLog(" insert data 2");
				    	if(pos >= 0)
			    			taskDbObj.insertTaskDetails(category, pos);
				    	//ServiceProConstants.showLog(" insert data 3");
					}
	        	}	        		
	    	}
			if(taskDbObj != null)
		    	taskDbObj.closeDBHelper();
		    if(errorDbObj != null)
		    	errorDbObj.closeDBHelper();
		}
		catch (Exception de) {
        	ServiceProConstants.showErrorLog("Error in insertData:"+de.toString());
        	if(taskDbObj != null)
		    	taskDbObj.closeDBHelper();
		    if(errorDbObj != null)
		    	errorDbObj.closeDBHelper();
        } 
	}//fn insertData
	
	final Runnable insertExceptErrorDataCall = new Runnable(){
	    public void run()
	    {
	    	try{
	    		insertExceptErrorData();
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }	    
    };
	
	private void insertExceptErrorData(){
		ServiceProOutputConstraints category;
		try{
			if(taskDbObj == null)
				taskDbObj = new ServiceProTaskDetailsPersistent(this);				
			boolean idexists = false;
			int pos = -1;
			for(int i=0;i<taskList.size();i++){
				category = ((ServiceProOutputConstraints)taskList.get(i));
				pos = -1;
	        	if(category != null){   
				    if(taskDbObj != null){	
				    	idexists = taskDbObj.checkTaskExists(category.getObjectId().toString().trim());
				    	if(!idexists){
				    		if(taskOrderListObj.contains(category.getObjectId().toString().trim())){
					    		int position=taskOrderListObj.indexOf(category.getObjectId().toString().trim()); 
					    		pos = position;
					    	}
				    		if(pos >= 0)
				    			taskDbObj.insertTaskDetails(category, pos);
				    	}  				    
				    	else{
				    		if(taskOrderListObj.contains(category.getObjectId().toString().trim())){
					    		int position=taskOrderListObj.indexOf(category.getObjectId().toString().trim()); 
					    	}
				    		if(pos >= 0)
				    			taskDbObj.updateOrderValue(pos, category.getObjectId().toString().trim());
				    	}
					}
	        	}	        		
	    	}    
		    if(taskDbObj != null){
		    	clearAllTaskListArray();
		    	allTaskList = taskDbObj.getTaskDetails();		
		    	
		    	if(categoryCopy != null)
		    		categoryCopy = null;	
		    	for(int i = 0; i < allTaskList.size(); i++){  
	            	categoryCopy = ((ServiceProOutputConstraints)allTaskList.get(i));
                    if(categoryCopy != null){
                    	taskList.add(categoryCopy);
                    }
                }
			}		    
		    
		    drawSubLayout();
		    
		    if(taskDbObj != null)
		    	taskDbObj.closeDBHelper();
		    if(errorDbObj != null)
		    	errorDbObj.closeDBHelper(); 
		}
		catch (Exception de) {
        	ServiceProConstants.showErrorLog("Error in insertData:"+de.toString());
        	if(taskDbObj != null)
		    	taskDbObj.closeDBHelper();
		    if(errorDbObj != null)
		    	errorDbObj.closeDBHelper();
        } 
	}//fn insertData
		
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
        
    private void startNetworkConnection(final Context ctxAct, final SoapSerializationEnvelope envelopeCE, final String url){
    	try {
    		if(pdialog != null)
    			pdialog = null;
    		
			if(pdialog == null)
				this.runOnUiThread(new Runnable() {
                    public void run() {
                    	pdialog = ProgressDialog.show(ctxAct, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
                    	new Thread() {
                    		public void run() {
                    			try{
                    				getSOAPViaHTTP(envelopeCE, url);
                    				sleep(2000);
                    			} catch (Exception e) {  }
             				}
                    	}.start();
                    }
                });
		} catch (Exception ae) {
			ServiceProConstants.showErrorLog(ae.toString());
		}
    }//fn startNetworkConnection
	    
    private void showEditTaskScreen(int index){
		try {
			selected_index = index;
			ServiceProConstants.showLog("selected_index "+selected_index);
			ServiceProOutputConstraints categoryobj2 = null;
			ServiceOrdDocOpConstraints categoryDocobj = null;
            boolean errFlag = false;
            String errorMsgs = "";
            String statusErrMsg = "";
            String confErrorMsg = "";
            String taskTransferErrorMsg = "";
            ServiceProConstants.showLog("taskList size "+taskList.size());
           
            ServiceProConstants.showLog("documentsVect size "+documentsVect.size());
			if(taskList.size() != 0){
				if(taskList.size() > selected_index){
					//category = null;
					categoryobj2 = (ServiceProOutputConstraints)taskList.get(selected_index);
					String odjIDStr = categoryobj2.getObjectId().trim();  
					if(documentsVect.size()==0){
						 ConfList =ServiceProDBOperations.readAllConfListDataFromDB(this,odjIDStr);
						 categoryDocobj = (ServiceOrdDocOpConstraints)ConfList.get(selected_index);
			            }
					 else{
						 categoryDocobj = (ServiceOrdDocOpConstraints)documentsVect.get(selected_index);
					 }
					//categoryDocobj = (ServiceOrdDocOpConstraints)documentsVect.get(selected_index);										 
                    if(categoryobj2 != null){
                    	try{                    	 
	                    	if(errorDbObj == null)
	            				errorDbObj = new ServiceProErrorMessagePersistent(ctx);
	                    	//errMsg = errorDbObj.getErrorMsg(category.getObjectId().toString().trim(), ServiceProConstants.STATUS_UPDATE_API);			
	                    	
	                    	statusErrMsg = errorDbObj.getErrorMsg(categoryobj2.getObjectId().toString().trim(), ServiceProConstants.STATUS_UPDATE_API);
	                    	if(statusErrMsg.length() > 0){
	                    		statusErrMsg = "Task Updation Error: "+ statusErrMsg;
	                    	}
	                    	
	                    	confErrorMsg = errorDbObj.getErrorMsg(categoryobj2.getObjectId().toString().trim(), ServiceProConstants.TASK_CONFIRMATION_API);
	                    	if(confErrorMsg.length() > 0){
	                    		confErrorMsg = "Service Confirmation Error: "+ confErrorMsg;
	                    	}
	                    	
	                    	taskTransferErrorMsg = errorDbObj.getErrorMsg(categoryobj2.getObjectId().toString().trim(), ServiceProConstants.TASK_TRANS_API);
	                    	if(taskTransferErrorMsg.length() > 0){
	                    		taskTransferErrorMsg = "Service Confirmation Error: "+ taskTransferErrorMsg;
	                    	}
	                    	
	                    	ServiceProConstants.showLog("errMsg before:"+errorMsgs);
	                    	errorMsgs = statusErrMsg;
	                    	ServiceProConstants.showLog("errMsg before:"+errorMsgs);
	                    	
	                    	if(errorMsgs.length() > 0){
	                    		if(confErrorMsg.length() > 0){
		                    		errorMsgs += "\n"+confErrorMsg;	                    			
	                    		}
	                    	}else{
	                    		errorMsgs += confErrorMsg;
	                    	}
	                    	
	                    	if(errorMsgs.length() > 0){
	                    		if(taskTransferErrorMsg.length() > 0){
		                    		errorMsgs += "\n"+taskTransferErrorMsg;	                    			
	                    		}
	                    	}else{
	                    		errorMsgs += taskTransferErrorMsg;
	                    	}
	                    	errMsg = errorMsgs;
	            			if(errorDbObj != null)
	            		    	errorDbObj.closeDBHelper();
                    	}
                    	catch(Exception e){
                    		ServiceProConstants.showErrorLog("Error in getting error msg : "+e.toString());
                    	}            			
                    	Intent intent = new Intent(this, ServiceProTaskEditScreen.class);                    	
                    	intent.putExtra("errMsg", errMsg);
                    	intent.putExtra("taskobj", categoryobj2);
                    	intent.putExtra("docobj", categoryDocobj);
                    	//intent.putExtra("index", index);
            			startActivityForResult(intent,ServiceProConstants.TASK_EDIT_SCREEN);
                    }
                    else
                        errFlag = true;
				}
				else{
                    errFlag = true;
                }
			}
			else{
                errFlag = true;
            }
                
            if(errFlag == true)
            	ServiceProConstants.showErrorDialog(this, getString(R.string.SERVICEORD_CNFSCR_ERR_CANT_EDIT));
			
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showEditTaskScreen      
    
	OnItemClickListener listItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			try{
				//ServiceProConstants.showLog("Selected Item "+arg2);
				selected_index = arg2;
				//checkErrorFromOffline(arg2);
				showEditTaskScreen(arg2);
			}
			catch (Exception dee) {
		    	ServiceProConstants.showErrorLog("Error in listItemClickListener:"+dee.toString());
		    }
		}
	};
	
	public void checkErrorFromOffline(final int index){
		try{
			if((errRefId != null && errRefId.length() > 0) && (errMsg != null && errMsg.length() > 0)){				
				ServiceProOutputConstraints category = null;	            
				if(taskList != null){
					if(taskList.size() > index){
						category = null;
						category = (ServiceProOutputConstraints)taskList.get(index);
	                    if(category != null){
	                    	if(category.getObjectId().toString().trim().equals(errRefId.toString().trim())){
								AlertDialog.Builder builder = new AlertDialog.Builder(this);
					            builder.setMessage(errMsg);
					            builder.setPositiveButton(R.string.SERVICEPRO_OK, new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int whichButton) {
					                	dialog.dismiss();					                	
					                	showEditTaskScreen(index);
					                }
					            });                
					            builder.show(); 
		                    }
	                    	else{
	                    		showEditTaskScreen(index);
	                    	}
	                    }
	                }
	            }
			}
			else{
				showEditTaskScreen(index);				
			}
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in checkErrorFromOffline:"+de.toString());
	    }
	}//fn checkErrorFromOffline
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
		  ServiceProOutputConstraints category = null;
		  if(resultCode==RESULT_OK && requestCode==ServiceProConstants.TASK_EDIT_SCREEN){
			  if(data != null){
				  String msg = data.getStringExtra("selected_status");
				  String date = data.getStringExtra("taskDateStr");
				  String etadate = data.getStringExtra("taskETADateStr");
				  String etatime = data.getStringExtra("taskETATimeStr");
				  category = (ServiceProOutputConstraints)taskList.get(selected_index);			  
				  category.setZZKEeyDate(date);
				  category.setZzetaDate(etadate);
				  category.setZzetaTime(etatime);
				  if(msg.length() > 0 && msg != null){
					  category.setStatus(msg);
					  StatusOpConstraints statusObj = getStatusDetails(msg);
					  if(statusObj != null){
	            			String statusPSAction = statusObj.getZZStatusPostSetAction().toString().toLowerCase().trim();
	            			if(statusPSAction != null && statusPSAction.length() > 0){
	            				if(statusPSAction.contains(ServiceProConstants.TASK_STATUS_POST_ACTION_DROPFDEVICE)){
	            					taskList.remove(selected_index);	
	            				}
	            			}           			
	            	  }
					  /*if(msg.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_DECLINED_STR_FOR_SAP) || msg.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_COMPLETED_STR_FOR_SAP)){
						  taskList.remove(selected_index);		
					  }	*/   
				  }
			  }
			  //errorAvalForThisUserTask();
			  //drawSubLayout();
			  getLDBTaskList();
			  refreshList();			 
				  
		  }	
	}
	
	private void DisplayDiogPopUpOnError() {
		TextView tv;
		//String msgStr = ""
		
		ImageButton emailbtn,skypebtn;		
    	 LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		  View layout;
		  
		  layout = inflater.inflate(R.layout.salesorder_email_error,
				  (ViewGroup) findViewById(R.id.listviewlineardialog3));		        		       		  
		 		 		 
		  emailbtn = (ImageButton)layout.findViewById(R.id.showemailbtn2);
		  emailbtn.setOnClickListener(email_btnListener2); 	   
		  		
		  builder = new AlertDialog.Builder(this).setTitle("Email on Error");	        		  	        		 
		  builder.setInverseBackgroundForced(true);
		  //View view=inflater.inflate(R.layout.activity_diag_popup, null);
		  //builder.setTitle("Email on Error");	        		 
		  builder.setView(layout); 	        		
		  builder.setMessage(ServiceProConstants.ERROR_MSG);
		  alertDialog = builder.create();    		  
		  alertDialog.show();
		
	}//DisplayDiogPopUp
	
	private OnClickListener email_btnListener2 = new OnClickListener() {
        public void onClick(View v) {          	        	
        	showEmailActivity2();   			
        }
    };
    
    public void showEmailActivity2() {
    	try{   
    		String  linkText ="";
    		String  last = "", urlName = "", androidOS = "", manufacturer = "", editionStr = "",imeno =""; 		
    		Elements name, edition, device, version, deviceType, server, url;
    		imeno = SapGenConstants.getMobileIMEI(this);
    		AssetManager assetManager = getAssets();
    		InputStream inputstrm;    		
    		inputstrm = assetManager.open("aboutscreen.xml");  
    		int size = inputstrm.available();    		     
			byte[] buffer = new byte[size];   		
			inputstrm.read(buffer);   		
			inputstrm.close();
			String text = new String(buffer);   		    		    	
			Document doc = Jsoup.parse(text);
			name = doc.getElementsByTag("name");
			edition = doc.getElementsByTag("edition");
			device = doc.getElementsByTag("device");
			version = doc.getElementsByTag("version");
			deviceType = doc.getElementsByTag("devicetype");
			server = doc.getElementsByTag("server");
			url = doc.getElementsByTag("SOAP_SERVICE_URL");
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			androidOS = Build.VERSION.RELEASE;
			manufacturer = Build.MANUFACTURER;
			editionStr = android.os.Build.MODEL;
			String urlStr = url.text().toString().trim();
			SapGenConstants.showLog(" urlStr: "+urlStr);
            int firstIndex = urlStr.indexOf("//");
			SapGenConstants.showLog(" firstIndex: "+firstIndex);
            String urlName1 = urlStr.substring(firstIndex+2, urlStr.length());
			SapGenConstants.showLog(" urlName1: "+urlName1);
            int thirdIndex = urlName1.indexOf(":");
            if(thirdIndex != -1){
    			SapGenConstants.showLog(" thirdIndex: "+thirdIndex);
    			urlName = urlName1.substring(0, thirdIndex);
    			SapGenConstants.showLog(" urlName: "+urlName);
            }else{
                int thirdIndex1 = urlName1.indexOf("/");
    			SapGenConstants.showLog(" thirdIndex1: "+thirdIndex1);
    			urlName = urlName1.substring(0, thirdIndex1);
    			SapGenConstants.showLog(" urlName: "+urlName);            	
            }
    		linkText = "File Name: "+name.text()+"\n"+edition.text()+":"+" "+device.text()+"\n"+deviceType.text()+" "+manufacturer+" "+editionStr+"\n"+version.text()+" "+androidOS+"\n"+"GDID: "+imeno+"\n"+"Server: "+urlName+"\n";
    	   linkText =linkText+"\n"+ "Event Name: "+"SERVICE-DOX-STATUS-UPDATE"+ "\n" +"DATA-TYPE: "+"ZGSXSMST_SRVCDCMNT20";  		
			String to = "gss.mobile@globalsoft-solutions.com";
			String subject = "Email on Error";
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.putExtra(Intent.EXTRA_TEXT, linkText);
			email.setType("message/rfc822");   		 
			startActivity(Intent.createChooser(email, "Choose an Email client"));
    	}
    	catch(Exception adf){
    		SapGenConstants.showErrorLog("Error in showEmailActivity : "+adf.getMessage());
    	}		
	}       
    
	public void onClose(){
		try {
			System.gc();
			setResult(RESULT_OK); 
			this.finish();
		} catch (Exception e) {
		}
	}//fn onClose
		
	
	private void drawSubLayout(){
		try{	
			colTableLayout = (TableLayout)findViewById(R.id.taskmaintbllayout3);
			if(colTableLayout != null)
				colTableLayout.removeAllViews();
			
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			linparams.leftMargin = 10; 
			linparams.rightMargin = 10; 
			linparams.gravity = Gravity.LEFT; //CENTER_VERTICAL;	
			
			if(taskList != null){
				ServiceProOutputConstraints category = null;
				ServiceOrdDocOpConstraints category2 = null;
				String statusStr = "", priorityStr = "", custLocStr = "";
                String serOrdStr = "", contNameStr = "", productDescStr = "", serviceDescStr = "";
                String estDateStr = "", batchStr = "", syncStr = "", startDatStr = "", itemnoStr= " ";
                
                int rowSize = taskList.size();                
                //ServiceProConstants.showLog("Stocks List Size  : "+rowSize);
                
                priorityIcon = new ImageView[rowSize];
                statusIcon = new ImageView[rowSize];
                errstatusIcon = new ImageView[rowSize];
                custLocTxtView = new TextView[rowSize];
                
				for (int i =0; i < taskList.size(); i++) {
					category = (ServiceProOutputConstraints)taskList.get(i);
					
                    if(category != null){
                    	custLocStr = "";                    	
                    	try {
							priorityStr = category.getPriority().trim();
							statusStr = category.getStatus().trim();
							serOrdStr = category.getObjectId().trim();  
							//productStr = category.getRefObjProductId().toString().trim();
							serviceDescStr = category.getDescription().toString().trim();
							productDescStr = category.getRefObj_Product_Descr().toString().trim();
							/*itemnoStr = category2.getNumberExt().toString().trim();
							ServiceProConstants.showLog("itemnoStr : "+itemnoStr);
*/							
							if(category.getCp1Name().toString().trim().length() != 0){
								contNameStr = category.getCp1Name();
							}
							
							if(category.getNameOrg1().toString().length() > 0){
								custLocStr += category.getNameOrg1().toString();
		                		//ServiceProConstants.showLog("getNameOrg1 : "+category.getNameOrg1().toString());
		                	}
		                	if(category.getNameOrg2().toString().length() > 0){
		                		custLocStr += " "+category.getNameOrg2().toString();
		                		//ServiceProConstants.showLog("getNameOrg2 : "+category.getNameOrg2().toString());
		                	}
		                	if(category.getStreet().toString().length() > 0){
		                		custLocStr += "\n"+category.getStreet().toString();
		                		//ServiceProConstants.showLog("getStreet : "+category.getStreet().toString());
		                	}
		                	if(category.getCity().toString().length() > 0){
		                		custLocStr += "\n"+category.getCity().toString();
		                		//ServiceProConstants.showLog("getCity : "+category.getCity().toString());
		                	}
		                	if(category.getRegion().toString().length() > 0){
		                		custLocStr += ","+category.getRegion().toString();
		                		//ServiceProConstants.showLog("getRegion : "+category.getRegion().toString());
		                	}                	                	
		                	if(category.getPostalCode1().toString().length() > 0){
		                		custLocStr += ","+category.getPostalCode1().toString();
		                		//ServiceProConstants.showLog("getPostalCode1 : "+category.getPostalCode1().toString());
		                	}
		                	if(category.getCountryIso().toString().length() > 0){
		                		custLocStr += ","+category.getCountryIso().toString();
		                		//ServiceProConstants.showLog("getCountryIso : "+category.getCountryIso().toString());
		                	} 
							
							startDatStr = category.getZZKEeyDate().trim();
							if(!startDatStr.equalsIgnoreCase("")){
								String[] date_value = startDatStr.split("-");
								startDatStr = ServiceProConstants.getMonthValue(Integer.parseInt(date_value[1]))+" "+date_value[2];
							}							

                    		if(category.getZzetaDate() != null && category.getZzetaDate().length() > 0){
                        		if(!category.getZzetaDate().equalsIgnoreCase("0000-00-00")){
        							estDateStr = category.getZzetaDate().trim()+" "+category.getZzetaTime().trim(); 
        							ServiceProConstants.showLog("estDateStr : "+estDateStr);
                        		}
                    		}else{
                    			estDateStr = "";
                    		}
						}
                    	catch (Exception e1) {
                    		ServiceProConstants.showErrorLog("On drawSubLayout Assignment : "+e1.toString());
						}           
                    	
                        tr = new TableRow(this);
                        
                        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linear, null);
                        linearLayout.setPadding(0, 10, 0, 10);
                        linearLayout.setGravity(Gravity.LEFT);
                        linearLayout.setOrientation(LinearLayout.VERTICAL); 
                        
                        LinearLayout linearLayout1 = new LinearLayout(this);
                        linearLayout1.setOrientation(LinearLayout.HORIZONTAL); 
                        linearLayout1.setLayoutParams(linparams);
                        linearLayout1.setGravity(Gravity.LEFT);
                        
                    	priorityIcon[i] = new ImageView(this); 
                    	priorityIcon[i].setLayoutParams(linparams);   
                        if(priorityStr.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_LOW_STR))
                        	priorityIcon[i].setImageResource(R.drawable.low_icon);
	            		else if(priorityStr.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_HIGH_STR))
	            			priorityIcon[i].setImageResource(R.drawable.vhigh_icon);
	            		else if(priorityStr.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_NORMAL_STR))
	            			priorityIcon[i].setImageResource(R.drawable.high_icon);
                        
                        linearLayout1.addView(priorityIcon[i]);
                        
                        statusIcon[i] = new ImageView(this); 
                        statusIcon[i].setLayoutParams(linparams);   
                        String status = category.getStatus();            		
                		StatusOpConstraints statusObj = getStatusDetails(statusStr);
                		if(statusObj != null){
                			String imageName = statusObj.getZZStatusIcon().toString().toLowerCase().trim();
                			if(imageName != null){
                				int resID = getResources().getIdentifier(imageName , "drawable", getPackageName());
                				statusIcon[i].setImageResource(resID);
                			}else{
                				//statusIcon[i].setImageResource(R.drawable.light_grey);
                				statusIcon[i].setVisibility(View.GONE);
                			}            			
                		}else{
                			//statusIcon[i].setImageResource(R.drawable.light_grey);
                			statusIcon[i].setVisibility(View.GONE);
                		}
                        
                        /*if(statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_READY_STR_FOR_SAP)){
                			statusIcon[i].setImageResource(R.drawable.t1_grey);
        				}
        				else if(statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
        					statusIcon[i].setImageResource(R.drawable.tl_green);
        				}
        				else if(statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_DEFERRED_STR_FOR_SAP)){
        					statusIcon[i].setImageResource(R.drawable.tl_yellow);
        				}	
        				else if(statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_DECLINED_STR_FOR_SAP)){
        					statusIcon[i].setImageResource(R.drawable.tl_red);
        				}
        				else if(statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_COMPLETED_STR_FOR_SAP)){
        					statusIcon[i].setImageResource(R.drawable.tl_blue);
        				}*/
                        
                        linearLayout1.addView(statusIcon[i]);
                		linearLayout.addView(linearLayout1);
                		
                        LinearLayout linearLayout2 = new LinearLayout(this);
                        linearLayout2.setOrientation(LinearLayout.HORIZONTAL); 
                        linearLayout2.setLayoutParams(linparams); 
                        linearLayout2.setPadding(20, 5, 0, 0);
                        linearLayout2.setGravity(Gravity.CENTER);
                        
                        errstatusIcon[i] = new ImageView(this); 
                        errstatusIcon[i].setLayoutParams(linparams);
                        boolean isExits = ServiceProConstants.errorTaskIdForStatus.contains(category.getObjectId().toString().trim());
                        errstatusIcon[i].setImageResource(R.drawable.notify);
                        //ServiceProConstants.showLog("Error isExits : "+isExits);
                		linearLayout2.addView(errstatusIcon[i]);                		
                		linearLayout.addView(linearLayout2);
                		if(isExits){
                			errstatusIcon[i].setVisibility(View.VISIBLE);
                			linearLayout2.setVisibility(View.VISIBLE);
                		}
                		else{
                			errstatusIcon[i].setVisibility(View.GONE);
                			linearLayout2.setVisibility(View.GONE);
                		}
                		final int k=i;
                        custLocTxtView[i] = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					custLocTxtView[i].setText(custLocStr);
    					custLocTxtView[i].setId(i);
    					custLocTxtView[i].setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								//int id = view.getId();
								int id = k;
								showEditTaskScreen(id);
							}	
                        });
    					custLocTxtView[i].setWidth(headerWidth2);
    					custLocTxtView[i].setGravity(Gravity.LEFT);
    					custLocTxtView[i].setPadding(10,0,0,0);
    					
    					
    					TextView strDateTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					strDateTxtView.setText(startDatStr);
    					strDateTxtView.setWidth(headerWidth7);
    					strDateTxtView.setGravity(Gravity.LEFT);
    					strDateTxtView.setPadding(10,0,0,0);
    					
    					TextView estArrivalTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					try {
							if((!estDateStr.equalsIgnoreCase("")) && (!estDateStr.startsWith("00"))){
								String[] timeArr = estDateStr.split(" ");
								ServiceProConstants.showLog("Time : "+timeArr.length+" : "+timeArr[0]+" : "+timeArr[1]);
								estDateStr = ServiceProConstants.getSystemDateFormatString(this, timeArr[0])+" "+ServiceProConstants.getSecondsRemovedTimeStr(timeArr[1]);
							}
							else
								estDateStr = "";
						} catch (Exception e) {
							e.printStackTrace();
						}
    					
    					estArrivalTxtView.setText(estDateStr);
    					estArrivalTxtView.setWidth(headerWidth3);
    					estArrivalTxtView.setGravity(Gravity.LEFT);
    					estArrivalTxtView.setPadding(10,0,0,0);
    					
    					TextView serOrdTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					serOrdTxtView.setText(serOrdStr);
    					serOrdTxtView.setWidth(headerWidth4);
    					serOrdTxtView.setGravity(Gravity.LEFT);
    					serOrdTxtView.setPadding(10,0,0,0);
    					serOrdTxtView.setId(i);
    					serOrdTxtView.setOnClickListener(new View.OnClickListener() {	
    						public void onClick(View v) {
    							int id = k;
    							showEditTaskScreen(id);
    						}
    					});
    					
    					TextView contNameTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					contNameTxtView.setText(contNameStr);
    					contNameTxtView.setWidth(headerWidth5);
    					contNameTxtView.setGravity(Gravity.LEFT);
    					contNameTxtView.setPadding(10,0,0,0);
    					
    					TextView prdTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);					
    					prdTxtView.setText(serviceDescStr);
    					prdTxtView.setWidth(headerWidth6);
    					prdTxtView.setGravity(Gravity.LEFT);
    					prdTxtView.setPadding(10,0,0,0);
    					
    					if(!statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
    						strDateTxtView.setTypeface(null, Typeface.NORMAL); 
    						custLocTxtView[i].setTypeface(null, Typeface.NORMAL); 
    						estArrivalTxtView.setTypeface(null, Typeface.NORMAL); 
    						serOrdTxtView.setTypeface(null, Typeface.NORMAL); 
    						contNameTxtView.setTypeface(null, Typeface.NORMAL); 
    						prdTxtView.setTypeface(null, Typeface.NORMAL); 
        				}
    					
    					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
    						strDateTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						custLocTxtView[i].setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						estArrivalTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						serOrdTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						contNameTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						prdTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    					}
    					
    					tr.addView(linearLayout);
    					tr.addView(strDateTxtView);
    					tr.addView(custLocTxtView[i]);
    					tr.addView(estArrivalTxtView);
    					tr.addView(serOrdTxtView);
    					tr.addView(contNameTxtView);
    					tr.addView(prdTxtView);
    					
    					if(i%2 == 0)
    						tr.setBackgroundResource(R.color.item_even_color);
			            else
			            	tr.setBackgroundResource(R.color.item_odd_color);
    					colTableLayout.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                    }					
				}
			}
		}
		catch(Exception asgf){
			ServiceProConstants.showErrorLog("On drawSubLayout : "+asgf.toString());
		}
	}//fn drawSubLayout
	
	public StatusOpConstraints getStatusDetails(String status){
		StatusOpConstraints statusObj = null;
		try {	
			ArrayList statusListArray = ServiceProDBOperations.readAllStatusDataFromDB(this);
			if(statusListArray.size() != 0){				
				for(int i = 0; i < statusListArray.size(); i++){
					statusObj = ((StatusOpConstraints)statusListArray.get(i));
			        if(statusObj != null){
			        	String statusVal = statusObj.getStatus().toString().trim();
			        	if(statusVal.equalsIgnoreCase(status)){
			        		return statusObj;
			        	}
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for status!");
        		return statusObj;
			}  
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in getStatusDetails : "+e.toString());
    		return statusObj;
		}
		return statusObj;
	}//fn getStatusDetails
	
	public void errorAvalForThisUserTask(){
		try {					
			if(errorDbObj == null)
				errorDbObj = new ServiceProErrorMessagePersistent(this);
			ServiceProConstants.errorTaskIdForStatus = errorDbObj.checkErrorTrancIdApiExists();		
			if(errorDbObj != null)
				errorDbObj.closeDBHelper();		
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in errorAvalForThisUserTask : "+e.toString());
		}
	}//fn errorAvalForThisUserTask
	
	private void orderSettingForTasks(ArrayList taskOrderListObj){
		try {
			if(taskDbObj == null)
				taskDbObj = new ServiceProTaskDetailsPersistent(this);
			
			if(taskOrderListObj != null && taskOrderListObj.size() > 0){
				for(int i = 0; i < taskOrderListObj.size(); i++){ 
	            	String tracId = taskOrderListObj.get(i).toString().trim();
	            	taskDbObj.updateOrderValue(i, tracId);	            	
                }	
			}
				
			if(taskDbObj != null)
				taskDbObj.closeDBHelper();
			
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in orderSettingForTasks : "+e.toString());
		}
	}//fn orderSettingForTasks
	
	private void sortItemsAction(int sortInd){
		try{
			sortIndex = sortInd;
			if(sortInd == ServiceProConstants.TASK_SORT_CITY)
				 sortCityFlag = !sortCityFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_NAME)
				sortNameFlag = !sortNameFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_DATE)
				 sortDateFlag = !sortDateFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_PRIORITY)
				sortPriorityFlag = !sortPriorityFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_STATUS)
				sortStatusFlag = !sortStatusFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_SO)
				sortSOFlag = !sortSOFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_CNAME)
				sortCNameFlag = !sortCNameFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_PRODUCT)
				sortProductFlag = !sortProductFlag;
			else if(sortInd == ServiceProConstants.TASK_SORT_ETA)
				sortETAFlag = !sortETAFlag;
						
			//ServiceProConstants.showLog("Selected Sort Index : "+sortInd);			
			Collections.sort(taskList, reportSortComparator);
			ServiceProConstants.showLog("taskList:"+taskList.size()); 
			//getListView().invalidateViews();
			drawSubLayout();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in sortItemsAction : "+sfg.toString());
		}
	}//fn sortItemsAction
		
	private final Comparator reportSortComparator =  new Comparator() {
        public int compare(Object o1, Object o2){ 
        	int comp = 0;
            String strObj1 = "0", strObj2="0";
            int intObj1 = 0, intObj2 = 0;
            ServiceProOutputConstraints spOCObj1, spOCObj2;
            try{            	
                if (o1 == null || o2 == null){
                }
                else{            
                    spOCObj1 = (ServiceProOutputConstraints)o1;
                    spOCObj2 = (ServiceProOutputConstraints)o2;                                        
                    //ServiceProConstants.showLog("Comparator called for "+sortIndex);                    
                    if(sortIndex == ServiceProConstants.TASK_SORT_CITY){                    	
                        if(spOCObj1 != null){
                            strObj1 = spOCObj1.getCity();
                    	}                        
                        if(spOCObj2 != null){                            
                    		strObj2 = spOCObj2.getCity();
                        }                        
                        if(sortCityFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == ServiceProConstants.TASK_SORT_NAME){                    	
                        if(spOCObj1 != null){
                            strObj1 = spOCObj1.getNameOrg1()+", "+spOCObj1.getNameOrg2();
                    	}                        
                        if(spOCObj2 != null){                            
                    		strObj2 = spOCObj2.getNameOrg1()+", "+spOCObj2.getNameOrg2();
                        }                        
                        if(sortNameFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == ServiceProConstants.TASK_SORT_DATE){                    	
                    	if(spOCObj1 != null){
                    		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateObj = curFormater.parse(spOCObj1.getZZKEeyDate().trim());
                        	Long dateLongValue = dateObj.getTime();
                        	strObj1 = String.valueOf(dateLongValue);
                        }                        
                        if(spOCObj2 != null){
                        	SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateObj = curFormater.parse(spOCObj2.getZZKEeyDate().trim());
                        	Long dateLongValue = dateObj.getTime();
                        	strObj2 = String.valueOf(dateLongValue);
                        }                        
                        if(sortDateFlag == true)
                            comp =  strObj1.compareTo(strObj2);
                        else
                            comp =  strObj2.compareTo(strObj1);                        
                    }
                    else if(sortIndex == ServiceProConstants.TASK_SORT_PRIORITY){
                    	//1 = Low, 2 = Normal , 3= High priorities for internal value. 
                    	if(spOCObj1 != null){
                    		if(spOCObj1.getPriority().trim().equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_LOW_STR))                            
                    			intObj1 = 1;            
                    		else if(spOCObj1.getPriority().trim().equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_NORMAL_STR))                            
                    			intObj1 = 2;
                    		else if(spOCObj1.getPriority().trim().equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_HIGH_STR))                            
                    			intObj1 = 3;
                        }                        
                        if(spOCObj2 != null){
                        	if(spOCObj2.getPriority().trim().equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_LOW_STR))                            
                    			intObj2 = 1;                            
                    		else if(spOCObj2.getPriority().trim().equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_NORMAL_STR))                            
                    			intObj2 = 2;
                    		else if(spOCObj2.getPriority().trim().equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_HIGH_STR))                            
                    			intObj2 = 3;
                        }
                        if(sortPriorityFlag == true)
                            comp = (int)(intObj1 - intObj2);
                        else
                            comp = (int)(intObj2 - intObj1);
                    }
                    else if(sortIndex == ServiceProConstants.TASK_SORT_STATUS){
                        if(spOCObj1 != null)
                            strObj1 = spOCObj1.getStatus().trim();                        
                        if(spOCObj2 != null)
                            strObj2 = spOCObj2.getStatus().trim();                        
                        if(sortStatusFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }                
                    else if(sortIndex == ServiceProConstants.TASK_SORT_SO){
                        if(spOCObj1 != null)
                            strObj1 = spOCObj1.getObjectId().trim();                        
                        if(spOCObj2 != null)
                            strObj2 = spOCObj2.getObjectId().trim();                        
                        if(sortSOFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }     
                    else if(sortIndex == ServiceProConstants.TASK_SORT_CNAME){
                        if(spOCObj1 != null)
                            strObj1 = spOCObj1.getCp1Name().trim();                        
                        if(spOCObj2 != null)
                            strObj2 = spOCObj2.getCp1Name().trim();                        
                        if(sortCNameFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }    
                    else if(sortIndex == ServiceProConstants.TASK_SORT_PRODUCT){
                        if(spOCObj1 != null)
                            strObj1 = spOCObj1.getRefObj_Product_Descr().trim();                        
                        if(spOCObj2 != null)
                            strObj2 = spOCObj2.getRefObj_Product_Descr().trim();                        
                        if(sortProductFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }    
                    else if(sortIndex == ServiceProConstants.TASK_SORT_ETA){                    	
                    	if(spOCObj1 != null){
                    		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateObj = curFormater.parse(spOCObj1.getZzetaDate().trim()+" "+spOCObj1.getZzetaTime());
                        	Long dateLongValue = dateObj.getTime();
                        	strObj1 = String.valueOf(dateLongValue);
                        }                        
                        if(spOCObj2 != null){
                        	SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateObj = curFormater.parse(spOCObj2.getZzetaDate().trim()+" "+spOCObj2.getZzetaTime());
                        	Long dateLongValue = dateObj.getTime();
                        	strObj2 = String.valueOf(dateLongValue);
                        }                        
                        if(sortETAFlag == true)
                            comp =  strObj1.compareTo(strObj2);
                        else
                            comp =  strObj2.compareTo(strObj1);                        
                    }
        			//sortSOFlag = false, sortCNameFlag = false, sortProductFlag = false, sortETAFlag
                }
             }
             catch(Exception qw){
            	 ServiceProConstants.showErrorLog("Error in Report Sort Comparator : "+qw.toString());
             }                 
             if (comp != 0) {            
                return comp;
             } 
             else {            
                return 0;
             }
        }
    };
    
    private String[] getImageFileList()
    {   	
    	String[] resArray = null;
    	try {
    		String files;
		    int ind1 = 0, ind2 = 0;
		    File folder = new File(Environment.getExternalStorageDirectory() + "/Imgs");
		    File[] listOfFiles = folder.listFiles(); 
		    int ii=0;
		    if(listOfFiles != null && listOfFiles.length > 0){
			    resArray = new String[listOfFiles.length];
			    for (int i = 0; i < listOfFiles.length; i++) 
			    {		   
			    	if (listOfFiles[i].isFile()) 
			    	{
			    		files = listOfFiles[i].getName();
			    		if (files.endsWith(".jpg") || files.endsWith(".JPG"))
			    		{
			    			ServiceProConstants.showLog(files);
			    			ind1 = files.indexOf("_");
			    			ind2 = ind1;
			    			ServiceProConstants.showLog(files.substring(0, ind2));
			    			String ids = files.substring(0, ind2);	
			    			resArray[ii] = ids;
			    			ii++;
			    		}
			    	}
			    }
		    }
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in getImageFileList:"+e.toString());
		}
		return resArray;   
    }//fn getImageFileList 
    
    private void deleteImgFiles(String id){
    	try {
    		String capImagePath = ServiceProConstants.getCapturedSmallImagePath(id);
    		if(capImagePath != null && capImagePath.length() > 0){
				File capFile = new File(capImagePath);		
				if(capFile != null){
					if(capFile.exists()){
						capFile.delete();
					}
				}
    		}
			String signImagePath = ServiceProConstants.getSignatureSmallImagePath(id);
    		if(signImagePath != null && signImagePath.length() > 0){
				File signFile = new File(signImagePath);		   
				if(signFile != null){
					if(signFile.exists()){
						signFile.delete();
					}
				}
    		}
		} catch (Exception eff) {
    		ServiceProConstants.showErrorLog("Error in deleteImgFiles:"+eff.toString());
		}
    }//fn deleteImgFiles 
    
    private void insertConfListDataIntoDB(){
    	ServiceOrdDocOpConstraints docsCategory;
    	try {
			if(documentsVect != null && documentsVect.size() > 0){
				for(int k=0; k<documentsVect.size(); k++){
					docsCategory = (ServiceOrdDocOpConstraints) documentsVect.get(k);
					if(docsCategory != null){
						ServiceProDBOperations.insertConfListDataInToDB(this, docsCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfListDataIntoDB 

    private void insertAttachListDataIntoDB(){
    	ServiceProAttachmentOpConstraints attachCategory;
    	try {
			if(attachVect != null && attachVect.size() > 0){
				for(int k=0; k<attachVect.size(); k++){
					attachCategory = (ServiceProAttachmentOpConstraints) attachVect.get(k);
					if(attachCategory != null){
						ServiceProDBOperations.insertAttachListDataInToDB(this, attachCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertAttachListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertAttachListDataIntoDB

    private void insertConfSpareListDataIntoDB(){
    	ServiceOrdDocOpConstraints docsCategory;
    	try {
			if(confSparesVect != null && confSparesVect.size() > 0){
				for(int k=0; k<confSparesVect.size(); k++){
					docsCategory = (ServiceOrdDocOpConstraints) confSparesVect.get(k);
					if(docsCategory != null){
						ServiceProDBOperations.insertConfSpareListDataInToDB(this, docsCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfSpareListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfSpareListDataIntoDB
    
    private void insertConfCollecListDataIntoDB(){
    	ServiceFollDocOpConstraints followCategory;
    	try {
			if(confCollecVect != null && confCollecVect.size() > 0){
				for(int k=0; k<confCollecVect.size(); k++){
					followCategory = (ServiceFollDocOpConstraints) confCollecVect.get(k);
					if(followCategory != null){
						ServiceProDBOperations.insertConfCollecListDataInToDB(this, followCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfCollecListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfCollecListDataIntoDB
    
    private void insertStatusListDataIntoDB(){    
    	StatusOpConstraints serStatusListObj;
    	try {
			if(statusListObj != null && statusListObj.size() > 0){
				for(int k=0; k<statusListObj.size(); k++){
					serStatusListObj = (StatusOpConstraints) statusListObj.get(k);
					if(serStatusListObj != null){
						ServiceProDBOperations.insertStatusListDataInToDB(this, serStatusListObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertStatusListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertStatusListDataIntoDB
    
    private void insertStatusFollowListDataInToDB(){    
    	StatusFollowOpConstraints serStatusListObj;
    	try {
			if(statusFollowListObj != null && statusFollowListObj.size() > 0){
				for(int k=0; k<statusFollowListObj.size(); k++){
					serStatusListObj = (StatusFollowOpConstraints) statusFollowListObj.get(k);
					if(serStatusListObj != null){
						ServiceProDBOperations.insertStatusFollowListDataInToDB(this, serStatusListObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertStatusListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertStatusListDataIntoDB
    
    private void insertConfProductListDataIntoDB(){
    	SOServiceActListOpConstraints serActListObj;
    	try {
			if(confProductVect != null && confProductVect.size() > 0){
				for(int k=0; k<confProductVect.size(); k++){
					serActListObj = (SOServiceActListOpConstraints) confProductVect.get(k);
					if(serActListObj != null){
						ServiceProDBOperations.insertConfProductListDataInToDB(this, serActListObj);
					}
				}
			}
			ServiceProConstants.showLog("confProductVect.size() insertConfProductListDataIntoDB: "+confProductVect.size());
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfProductListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfProductListDataIntoDB
    
    private void insertConfCauseCodeListDataIntoDB(){
    	SOCodeListOpConstraints codeListObj;
    	try {
			if(confCauseCodeVect != null && confCauseCodeVect.size() > 0){
				for(int k=0; k<confCauseCodeVect.size(); k++){
					codeListObj = (SOCodeListOpConstraints) confCauseCodeVect.get(k);
					if(codeListObj != null){
						ServiceProDBOperations.insertConfCauseCodeListDataInToDB(this, codeListObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfCauseCodeListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfCauseCodeListDataIntoDB 

    private void insertConfCauseGroupListDataIntoDB(){
    	SOCodeGroupOpConstraints codeGroupObj;
    	try {
			if(confCauseGroupVect != null && confCauseGroupVect.size() > 0){
				for(int k=0; k<confCauseGroupVect.size(); k++){
					codeGroupObj = (SOCodeGroupOpConstraints) confCauseGroupVect.get(k);
					if(codeGroupObj != null){
						ServiceProDBOperations.insertConfCauseGroupListDataInToDB(this, codeGroupObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfCauseGroupListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfCauseGroupListDataIntoDB
    
    private void insertConfSympGroupListDataIntoDB(){
    	SOCodeGroupOpConstraints codeGroupObj;
    	try {
			if(confSympGroupVect != null && confSympGroupVect.size() > 0){
				for(int k=0; k<confSympGroupVect.size(); k++){
					codeGroupObj = (SOCodeGroupOpConstraints) confSympGroupVect.get(k);
					if(codeGroupObj != null){
						ServiceProDBOperations.insertConfSympGroupListDataInToDB(this, codeGroupObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfSympGroupListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfSympGroupListDataIntoDB
    
    private void insertConfSympCodeListDataIntoDB(){
    	SOCodeListOpConstraints sympCodeObj;
    	try {
			if(confSympCodeVect != null && confSympCodeVect.size() > 0){
				for(int k=0; k<confSympCodeVect.size(); k++){
					sympCodeObj = (SOCodeListOpConstraints) confSympCodeVect.get(k);
					if(sympCodeObj != null){
						ServiceProDBOperations.insertConfSympCodeListDataInToDB(this, sympCodeObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfSympCodeListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfSympCodeListDataIntoDB
    
    private void insertConfProbGroupListDataIntoDB(){
    	SOCodeGroupOpConstraints codeGroupObj;
    	try {
			if(confProbGroupVect != null && confProbGroupVect.size() > 0){
				for(int k=0; k<confProbGroupVect.size(); k++){
					codeGroupObj = (SOCodeGroupOpConstraints) confProbGroupVect.get(k);
					if(codeGroupObj != null){
						ServiceProDBOperations.insertConfProbGroupListDataInToDB(this, codeGroupObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfProbGroupListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfProbGroupListDataIntoDB
        
    private void insertConfProbCodeListDataIntoDB(){
    	SOCodeListOpConstraints probCodeObj;
    	try {
			if(confProbCodeVect != null && confProbCodeVect.size() > 0){
				for(int k=0; k<confProbCodeVect.size(); k++){
					probCodeObj = (SOCodeListOpConstraints) confProbCodeVect.get(k);
					if(probCodeObj != null){
						ServiceProDBOperations.insertConfProbCodeListDataInToDB(this, probCodeObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfProbCodeListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertConfProbCodeListDataIntoDB
    
	private void insertConfMattEmpListDataIntoDB(){
		SOMattEmpListOpConstraints mattEmpObj;
		try {
			if(confMattEmpVect != null && confMattEmpVect.size() > 0){
				for(int k=0; k<confMattEmpVect.size(); k++){
					mattEmpObj = (SOMattEmpListOpConstraints) confMattEmpVect.get(k);
					if(mattEmpObj != null){
						ServiceProDBOperations.insertConfMattEmpListDataInToDB(this, mattEmpObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertConfMattEmpListDataIntoDB func: "+ewe.toString());
		}
	}//fn insertConfMattEmpListDataIntoDB	

	private void emptyAllTaskRelatedVectors(){
        try{
            if(documentsVect != null)
            	documentsVect.clear();
            if(confCollecVect != null)
            	confCollecVect.clear();       
            if(confSparesVect != null)
            	confSparesVect.clear();      
            if(attachVect != null)
            	attachVect.clear();   		
        }
        catch(Exception sfg){
            ServiceProConstants.showErrorLog("Error in emptyActivityVectors : "+sfg.toString());
        }
    }//fn emptyAllActivityVectors    
	
	private void emptyAllActivityVectors(){
        try{
            if(documentsVect != null)
            	documentsVect.clear();
            if(confCollecVect != null)
            	confCollecVect.clear();       
            if(confSparesVect != null)
            	confSparesVect.clear();
            if(confProductVect != null)
            	confProductVect.clear();
            if(confCauseCodeVect != null)
            	confCauseCodeVect.clear();
			if(confCauseGroupVect != null)
				confCauseGroupVect.clear();
			if(confSympGroupVect != null)
				confSympGroupVect.clear();
			if(confSympCodeVect != null)
				confSympCodeVect.clear();
			if(confProbGroupVect != null)
				confProbGroupVect.clear();
			if(confProbCodeVect != null)
				confProbCodeVect.clear();
			if(confMattEmpVect != null)
				confMattEmpVect.clear();        		
        }
        catch(Exception sfg){
            ServiceProConstants.showErrorLog("Error in emptyActivityVectors : "+sfg.toString());
        }
    }//fn emptyAllActivityVectors
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 // Handle touch events here...

        switch(event.getAction()&MotionEvent.ACTION_MASK)
        {
    case MotionEvent.ACTION_DOWN:
            Log.d("ok", "i am motion down event");
            Log.d("ok", "i a==ACTION_DOWN=="+a);
            mode= DRAG;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            Log.d("ok", "i am in pointer down listener");
            a++;
            Log.d("ok", "i a=ACTION_POINTER_DOWN==="+a);
            olddistanc= spacing(event);
            //oldDistance.set(olddistanc);
            Log.d("dis", "i get the old Distance"+olddistanc);
            if(olddistanc>10f)
            {
                mode=ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
            Log.d("ok", "i am in the action up listener");
            a++;
            Log.d("ok", "i a=ACTION_UP==="+a);
            break;
        case MotionEvent.ACTION_POINTER_UP:
            a++;
            Log.d("ok", "i a==ACTION_POINTER_UP=="+a);
            Log.d("ok", "i am in the  actioon pointer up listener");
            mode=NONE;

            break;
        case MotionEvent.ACTION_MOVE:
            if(mode==DRAG)
            {
            Log.d("ok", "the value of the mode is ="+mode); 
            }
            else if(mode==ZOOM)
            {
                Log.d("ok", "the value of the mode is ="+mode);
            Log.d("ok", "i am move gesture in the touch event");
            a++;
            Log.d("ok", "i a==ACTION_MOVE=="+a);
            float ndistance= spacing(event);
            Log.d("dis", "i get the new Distance"+ndistance);
            Log.d("dis", "i get the old Distance"+olddistanc);
            c = ndistance - olddistanc;
            Log.d("dis", "number of pointers = "+event.getPointerCount());
            if(event.getPointerCount() < 2)
            break;
                if((c < 0 && custLocTxtView[0].getWidth()+ c <= 30) || (c >= 0 && custLocTxtView[0].getWidth() + c >= 100)){
                }
            else
            {
                int incordec = 10;
                if(c < 0)
                    incordec = -10;
                if(incordec==10)
                    Log.d("dis", "zoooooom ++++");
                else
                    Log.d("dis", "zoom outtttttt");
                for(int i=0;i<20;i++)
                {
                    for(Integer j = 0; j < 24; j++)
                    {
                    	custLocTxtView[i].setLayoutParams(new TableRow.LayoutParams(custLocTxtView[i].getWidth()+incordec, custLocTxtView[i].getHeight()+incordec));
                    }
                }
            }
            olddistanc= ndistance;
            break;
        }
        }
        return false;
}
    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }


    private void midPoint(PointF point, MotionEvent event) {
           float x = event.getX(0) + event.getX(1);
           float y = event.getY(0) + event.getY(1);
           point.set(x / 2, y / 2);
        }
		
}//End of class ServiceProTaskMainScreenForTablet