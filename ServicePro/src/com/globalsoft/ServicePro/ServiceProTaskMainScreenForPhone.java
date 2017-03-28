package com.globalsoft.ServicePro;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.globalsoft.ServicePro.Constraints.ServiceFollDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProAttachmentOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Constraints.StatusFollowOpConstraints;
import com.globalsoft.ServicePro.Constraints.StatusOpConstraints;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Database.ServiceProOfflineContraintsCP;

public class ServiceProTaskMainScreenForPhone extends ListActivity implements TextWatcher {
	
	private ListView listView;
	private SoapObject resultSoap = null;
	private ProgressDialog pdialog = null;
	private int sortIndex = -1;
	private int selected_index = -1;
	private static ArrayList taskList = new ArrayList();
	private static ArrayList allTaskList = new ArrayList();
	private static ArrayList idList = new ArrayList();
	private ArrayList taskListObj = new ArrayList();
	private ArrayList allTaskListObj = new ArrayList();
	private ArrayList taskOrderListObj = new ArrayList();
	private EditText searchET;
	private boolean internetAccess = false;
	private String searchStr = "";
	private boolean sortNameFlag = false, sortDateFlag = false, sortPriorityFlag = false, sortStatusFlag = false,
	sortSOFlag = false, sortCNameFlag = false, sortProductFlag = false, sortETAFlag = false;
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
	
	final Handler handlerForInsertExceptErrorDataCall = new Handler();
	final Handler handlerForInsertNewDataCall = new Handler();
	final Handler handler = new Handler();
	final Handler handlerRefresh = new Handler();
	private static String errRefId = "";
	private String errMsg="";
	private int colId = -1;
	
	private MyTaskListAdapter taskListAdapter;
	private ServiceProTaskDetailsPersistent taskDbObj = null;
	private static ServiceProErrorMessagePersistent errorDbObj = null;
	private ServiceProOutputConstraints category = null;
	private ServiceProOutputConstraints categoryCopy = null;
	private ServiceProDBAdapter dbAdapterObj = null;
	private static Context ctx;	
    private static int dispwidth = 300;    
    private String taskErrorMsgStr="", taskErrorType="";

	private ArrayList statusListObj = new ArrayList();
	private ArrayList statusFollowListObj = new ArrayList();
    private ArrayList documentsVect = new ArrayList();
    private ArrayList confCollecVect = new ArrayList();
    private ArrayList confSparesVect = new ArrayList();
    private ArrayList confProductVect = new ArrayList();
    private ArrayList confCauseCodeVect = new ArrayList();
    private ArrayList confCauseGroupVect = new ArrayList();
    private ArrayList confSympGroupVect = new ArrayList();
    private ArrayList confSympCodeVect = new ArrayList();
    private ArrayList confProbGroupVect = new ArrayList();
    private ArrayList confProbCodeVect = new ArrayList();
    private ArrayList confMattEmpVect = new ArrayList();
    private ArrayList attachVect = new ArrayList();

	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			//requestWindowFeature(Window.FEATURE_LEFT_ICON);
			//setContentView(R.layout.taskmain);
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.taskmain); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SAPTASK_MAINSCR_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			//setContentView(R.layout.taskmaintbl);
			//setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);
			searchET = (EditText)findViewById(R.id.searchBEF);
			searchET.setText(searchStr);
			searchET.addTextChangedListener(this);  
			ctx = this.getApplicationContext();
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			ServiceProConstants.SERVICEPRO_MOBILE_IMEI = ServiceProConstants.getMobileIMEI(this);
			
			clearAllTaskListArray();
						
			listView = (ListView)findViewById(android.R.id.list);
			listView.setTextFilterEnabled(true);
			listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1));
			listView.setOnItemClickListener(listItemClickListener);    
			taskListAdapter = new MyTaskListAdapter(this);
			setListAdapter(taskListAdapter);
			
			ServiceProConstants.getMobileIMEI(ServiceProTaskMainScreenForPhone.this);
			internetAccess = ServiceProConstants.checkInternetConn(ServiceProTaskMainScreenForPhone.this);
						
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
			
			if(internetAccess){
				if(category != null)
                    category = null;				
				if(ServiceProConstants.FIRST_LANUCH_SCR == 0){
					ServiceProConstants.showLog("first time launch");
					ServiceProConstants.FIRST_LANUCH_SCR = 1;
		    		initStatusSoapConnection();
				}else{
					initSoapConnection();
				}
				ServiceProConstants.showLog("FIRST_LANUCH_SCR: "+ServiceProConstants.FIRST_LANUCH_SCR);		
			}
			else{
				getLDBData();
			}				
		}
		catch (Exception de) {
        	ServiceProConstants.showErrorLog("Error in Taskmainscreen oncreate:"+de.toString());
        	if(taskDbObj != null)
		    	taskDbObj.closeDBHelper();
        }
	}
	
	final Runnable handlerFnName = new Runnable(){
	    public void run(){
	    	try{
	    		initSoapConnection();
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in handlerFnName : "+asegg.toString());
	    	}
	    }	    
    };
	
	public void getLDBTaskList(){
		try {
			if(taskDbObj == null)
				taskDbObj = new ServiceProTaskDetailsPersistent(this);		
			
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
    					//ServiceProConstants.showLog("Object Id:"+category.getObjectId().toString().trim());
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
                    	//ServiceProConstants.showLog("Subject: "+data);
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
                    getListView().invalidateViews();
                    //drawSubLayout();
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
                    getListView().invalidateViews();
                    //drawSubLayout();
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
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[3];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
                        
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]SERVICE-DOX-CONTEXT-DATA-GET[.]VERSION[.]0[.]RESPONSE-TYPE[.]FULL-SETS";
        
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
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[3];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
                        
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]SERVICE-DOX-FOR-EMPLY-BP-GET[.]VERSION[.]0[.]RESPONSE-TYPE[.]FULL-SETS";
        
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
            soapTask = new StartNetworkTask(ctx);
            this.runOnUiThread(new Runnable() {
                public void run() {
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
            });    		
    	}
    	catch(Exception asgg){
    		ServiceProConstants.showErrorLog("Error in doThreadNetworkAction : "+asgg.toString());
    	}
    }//fn doThreadNetworkAction
       
    final Runnable getNetworkResponseRunnable = new Runnable(){
	    public void run(){
	    	try{
	    		SapGenConstants.showLog("Soap Env value : "+resultSoap);
	    		if(respType == ServiceProConstants.RESP_TYPE_GET_STATUS){
	    			if(pdialog != null)
	        			pdialog = null;
	        		
	    			if(pdialog == null){
	    				ServiceProTaskMainScreenForPhone.this.runOnUiThread(new Runnable() {
	                        public void run() {
	                        	pdialog = ProgressDialog.show(ServiceProTaskMainScreenForPhone.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS_AFTER_RESULT),true);
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
	    				ServiceProTaskMainScreenForPhone.this.runOnUiThread(new Runnable() {
	                        public void run() {
	                        	pdialog = ProgressDialog.show(ServiceProTaskMainScreenForPhone.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS_AFTER_RESULT),true);
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
        try{         	    	            
            emptyAllActivityVectors();
        	pdialog.setMessage(getResources().getString(R.string.SERVICEPRO_WAIT_TEXTS_AFTER_RESULT));            
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
	                                if(serActListObj != null)
	                                    serActListObj = null;
	                                    
	                                serActListObj = new SOServiceActListOpConstraints(resArray);
	                                if(serActListObj != null)
	                                	confProductVect.add(serActListObj);
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SYMPTMCODEGROUPLIST10")){
	                                if(codeGroupObj != null)
	                                    codeGroupObj = null;
	                                
	                                codeGroupObj = new SOCodeGroupOpConstraints(resArray);
	                                if(confSympGroupVect != null)
	                                	confSympGroupVect.add(codeGroupObj);	 
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SYMPTMCODELIST10")){
	                                if(codeListObj != null)
	                                    codeListObj = null;
	                                
	                                codeListObj = new SOCodeListOpConstraints(resArray);
	                                if(confSympCodeVect != null)
	                                	confSympCodeVect.add(codeListObj);	 
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_PRBLMCODEGROUPLIST10")){
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
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_CAUSECODEGROUPLIST10")){
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
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLLIST10")){
	                                if(mattEmpObj != null)
	                                    mattEmpObj = null;
	                                
	                                mattEmpObj = new SOMattEmpListOpConstraints(resArray);
	                                if(mattEmpObj != null)
	                                	confMattEmpVect.add(mattEmpObj);	
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
	        	if(!errorflag){	        		
		            if(statusListObj.size() > 0){
		            	ServiceProDBOperations.deleteAllStatusListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI);
		            }
		            insertStatusListDataIntoDB();
		            
		            if(statusFollowListObj.size() > 0){
		            	ServiceProDBOperations.deleteAllStatusFollowListDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_STATUSFOLLOWLIST_CONTENT_URI);
		            }
		            insertStatusFollowListDataIntoDB();		            
	        		
		        	SOServiceActListOpConstraints serActListObj = null;
		            if(confProductVect.size() > 0){
			            for(int i = 0; i < confProductVect.size(); i++){  
			            	serActListObj = ((SOServiceActListOpConstraints)confProductVect.get(i));
		                    if(serActListObj != null){
		    	        		ServiceProDBOperations.deleteAllConfProductDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPRODUCTLIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfProductListDataIntoDB();
		            
		            SOCodeListOpConstraints codeListObj = null;
		            if(confCauseCodeVect.size() > 0){
			            for(int i = 0; i < confCauseCodeVect.size(); i++){  
			            	codeListObj = ((SOCodeListOpConstraints)confCauseCodeVect.get(i));
		                    if(codeListObj != null){
		    	        		ServiceProDBOperations.deleteAllConfCauseCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSECODELIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfCauseCodeListDataIntoDB();
		            
		            SOCodeGroupOpConstraints codeGroupObj = null;
		            if(confCauseGroupVect.size() > 0){
			            for(int i = 0; i < confCauseGroupVect.size(); i++){  
			            	codeGroupObj = ((SOCodeGroupOpConstraints)confCauseGroupVect.get(i));
		                    if(codeGroupObj != null){
		    	        		ServiceProDBOperations.deleteAllConfCauseGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfCauseGroupListDataIntoDB();	 
		            
		            SOCodeGroupOpConstraints sympGroupObj = null;
		            if(confSympGroupVect.size() > 0){
			            for(int i = 0; i < confSympGroupVect.size(); i++){  
			            	sympGroupObj = ((SOCodeGroupOpConstraints)confSympGroupVect.get(i));
		                    if(sympGroupObj != null){
		    	        		ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPGROUPLIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfSympGroupListDataIntoDB();   	
		            
		            SOCodeListOpConstraints sympCodeObj = null;
		            if(confSympCodeVect.size() > 0){
			            for(int i = 0; i < confSympCodeVect.size(); i++){  
			            	sympCodeObj = ((SOCodeListOpConstraints)confSympCodeVect.get(i));
		                    if(sympCodeObj != null){
		    	        		ServiceProDBOperations.deleteAllConfSympCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFSYMPCODELIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfSympCodeListDataIntoDB();
		            
		            SOCodeGroupOpConstraints probGroupObj = null;
		            if(confProbGroupVect.size() > 0){
			            for(int i = 0; i < confProbGroupVect.size(); i++){  
			            	probGroupObj = ((SOCodeGroupOpConstraints)confProbGroupVect.get(i));
		                    if(probGroupObj != null){
		    	        		ServiceProDBOperations.deleteAllConfSympGroupDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBGROUPLIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfProbGroupListDataIntoDB();
		            
		            SOCodeListOpConstraints probCodeObj = null;
		            if(confProbCodeVect.size() > 0){
			            for(int i = 0; i < confProbCodeVect.size(); i++){  
			            	probCodeObj = ((SOCodeListOpConstraints)confProbCodeVect.get(i));
		                    if(probCodeObj != null){
		    	        		ServiceProDBOperations.deleteAllConfProbCodeDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFPROBCODELIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfProbCodeListDataIntoDB();
		            
		            SOMattEmpListOpConstraints mattEmpObj = null;
		            if(confMattEmpVect.size() > 0){
			            for(int i = 0; i < confMattEmpVect.size(); i++){  
			            	mattEmpObj = ((SOMattEmpListOpConstraints)confMattEmpVect.get(i));
		                    if(mattEmpObj != null){
		    	        		ServiceProDBOperations.deleteAllConfMattEmpDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_CONFMATTEMPLLIST_CONTENT_URI);
		    	        	}
		                }  	
		            }
		            insertConfMattEmpListDataIntoDB();
		            
		            /*ServiceOrdDocOpConstraints docsCategory = null;
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
	            this.runOnUiThread(new Runnable() {
	                public void run() {
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
    
	public void updateReportsConfirmResponse(SoapObject soap){	
		boolean errorflag = false, resMsgErr = false;
        try{ 
        	pdialog.setMessage(getResources().getString(R.string.SERVICEPRO_WAIT_TEXTS_AFTER_RESULT));    
            if(taskListObj != null)
            	taskListObj.clear();
            if(allTaskListObj != null)
            	allTaskListObj.clear();
            
            //emptyAllActivityVectors();
            emptyAllTaskRelatedVectors();
                        
        	if(soap != null){
        		ServiceProConstants.soapResponse(this, soap, false);
            	taskErrorMsgStr = ServiceProConstants.SOAP_RESP_MSG;
            	taskErrorType = ServiceProConstants.SOAP_ERR_TYPE;
            	ServiceProConstants.showLog("On soap response : "+soap.toString());
            	String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg);   
            	ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
        		
            	ServiceProOutputConstraints categoryObj = null;
        		ServiceOrdDocOpConstraints docsCategory = null;
 	            ServiceFollDocOpConstraints followCategory = null;
 	            ServiceProAttachmentOpConstraints attachCategory = null;
	            
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
        		                        				ServiceProConstants.addToCalendar(ServiceProTaskMainScreenForPhone.this, data, desc, dateLongValue, dateLongValue);
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
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXCAST_ATTCHMNT01")){
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
	                                if(documentsVect != null)
	                                    documentsVect.add(docsCategory);
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCSPARE10")){
	                                if(docsCategory != null)
	                                    docsCategory = null;
	                                    
	                                docsCategory = new ServiceOrdDocOpConstraints(resArray);	                                
	                                if(confSparesVect != null)
	                                	confSparesVect.add(docsCategory);
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCCNFRMTN12")){
	                                if(followCategory != null)
	                                    followCategory = null;	                                    
	                                
	                                followCategory = new ServiceFollDocOpConstraints(resArray);
	                                if(confCollecVect != null)
	                                	confCollecVect.add(followCategory);
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
	        		if(taskListObj.size() > 0){
	        			clearAllTaskListArray();        
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
	    	            
	            		if(idList != null){
	            			idList.clear();
	            		}        		
	            		ServiceProConstants.showLog("taskList Size : "+taskList.size());
	            		ServiceProConstants.showLog("allTaskList Size : "+allTaskList.size());
	            		ServiceProConstants.showLog("AttachList Size : "+attachVect.size());
	            		
	            		ServiceProAttachmentOpConstraints attachCategory = null;
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
			            insertConfCollecListDataIntoDB();
	            		
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
	    	        		}else{
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
	    	        		}	   
	    	        		if(taskDbObj != null)
	    	        			taskDbObj.closeDBHelper();
	    	        		if(errorDbObj != null)
	    	        			errorDbObj.closeDBHelper();	        		
	    	        	}
	    	        	errorAvalForThisUserTask();	    	        	
	    	        	ServiceProTaskMainScreenForPhone.this.runOnUiThread(new Runnable() {
	        	            public void run() {	      
	        	            	refreshList();
	        	            }
	        	        });   
	    	        	ServiceProConstants.showLog("Task from SAP");
	        		}else{
	        			ServiceProTaskMainScreenForPhone.this.runOnUiThread(new Runnable() {
	                        public void run() {
	        		        	pdialog.setMessage(getResources().getString(R.string.SERVICEPRO_WAIT_TEXTS_UI));   
	            				stopProgressDialog();
	    	        			ServiceProConstants.showErrorDialog(ServiceProTaskMainScreenForPhone.this, getResources().getString(R.string.SERVICEPRO_TASK_LIST_ERROR_MSG_OFFLINE));
	                        }
	                    });
	        		}
        		}else{   
        	       getLDBData();
        		}        		
        	}
			catch (Exception de) {
	        	pdialog.setMessage(getResources().getString(R.string.SERVICEPRO_WAIT_TEXTS_UI));   
				stopProgressDialog();
	        	ServiceProConstants.showErrorLog("Error in Database Insert/update in Taskmainscreen:"+de.toString());
	        }          	
        }
    }//fn updateReportsConfirmResponse  
	
	private void getLDBData(){
		try {
			ServiceProTaskMainScreenForPhone.this.runOnUiThread(new Runnable() {
	            public void run() {	   
					getLDBTaskList();
					refreshList();
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
        	stopProgressDialog();
	    	ServiceProConstants.showLog("Task List Size : "+taskList.size());
	        taskListAdapter.removeAllTasks();
	        listView.setAdapter(taskListAdapter);
	        listView.invalidate();
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
		ServiceProOutputConstraints category;
		try{
			if(taskDbObj == null)
				taskDbObj = new ServiceProTaskDetailsPersistent(this);	
			int pos = -1;
			for(int i=0;i<taskList.size();i++){
				category = ((ServiceProOutputConstraints)taskList.get(i));
				pos = -1;
	        	if(category != null){   
				    if(taskDbObj != null){	
				    	if(taskOrderListObj.contains(category.getObjectId().toString().trim())){
				    		int position=taskOrderListObj.indexOf(category.getObjectId().toString().trim()); 
				    		pos = position;
				    	}
				    	if(pos >= 0)
			    			taskDbObj.insertTaskDetails(category, pos);
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
		    
		    taskListAdapter.removeAllTasks();
            listView.setAdapter(taskListAdapter);
            listView.invalidate(); 
            		    		    
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
         
    private void showEditTaskScreen(int index){
		try {
			selected_index = index;
			ServiceProOutputConstraints category = null;
            boolean errFlag = false;
            String errorMsgs = "";
            String statusErrMsg = "";
            String confErrorMsg = "";
            String taskTransferErrorMsg = "";
			if(taskList != null){
				if(taskList.size() > index){
					category = null;
					category = (ServiceProOutputConstraints)taskList.get(index);
                    if(category != null){
                    	try{                    	 
	                    	if(errorDbObj == null)
	            				errorDbObj = new ServiceProErrorMessagePersistent(ctx);
	                    	//errMsg = errorDbObj.getErrorMsg(category.getObjectId().toString().trim(), ServiceProConstants.STATUS_UPDATE_API);	
	                    	statusErrMsg = errorDbObj.getErrorMsg(category.getObjectId().toString().trim(), ServiceProConstants.STATUS_UPDATE_API);
	                    	if(statusErrMsg.length() > 0){
	                    		statusErrMsg = "Task Updation Error: "+ statusErrMsg;
	                    	}
	                    	
	                    	confErrorMsg = errorDbObj.getErrorMsg(category.getObjectId().toString().trim(), ServiceProConstants.TASK_CONFIRMATION_API);
	                    	if(confErrorMsg.length() > 0){
	                    		confErrorMsg = "Service Confirmation Error: "+ confErrorMsg;
	                    	}
	                    	
	                    	taskTransferErrorMsg = errorDbObj.getErrorMsg(category.getObjectId().toString().trim(), ServiceProConstants.TASK_TRANS_API);
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
                    	intent.putExtra("taskobj", category);    	
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
				selected_index = arg2;
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
					  }	 */  
				  }
			  }
			  //errorAvalForThisUserTask();
			  getLDBTaskList();
			  refreshList();
			  //getListView().invalidateViews();
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
	
	public class MyTaskListAdapter extends BaseAdapter {      
        
    	private LayoutInflater mInflater;
    	private ServiceProOutputConstraints category;
        
        public MyTaskListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
        	try {
				if(taskList != null)
					return taskList.size();
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
                convertView = mInflater.inflate(R.layout.taskmain_list, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.custloc);
                holder.taskid = (TextView) convertView.findViewById(R.id.taskid);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
                holder.status = (ImageView) convertView.findViewById(R.id.status);
                holder.errstatus = (ImageView) convertView.findViewById(R.id.errstatus); 
                holder.llitembg = (LinearLayout) convertView.findViewById(R.id.llitembg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
                       
            if(position%2 == 0)
            	holder.llitembg.setBackgroundResource(R.color.item_even_color);
            else
            	holder.llitembg.setBackgroundResource(R.color.item_odd_color);
            
            String data = "";
            if(taskList != null){
            	category = ((ServiceProOutputConstraints)taskList.get(position));
            	if(category != null){
            		String priority = category.getPriority();            		
            		if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
            			if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_LOW_STR))
	            			holder.indicator.setImageResource(R.drawable.low_icon_l);
            			else if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_HIGH_STR))
	            			holder.indicator.setImageResource(R.drawable.vhigh_icon_l);
            			else if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_NORMAL_STR))
	            			holder.indicator.setImageResource(R.drawable.high_icon_l);
            		}
            		else{
	            		if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_LOW_STR))
	            			holder.indicator.setImageResource(R.drawable.low_icon);
	            		else if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_HIGH_STR))
	            			holder.indicator.setImageResource(R.drawable.vhigh_icon);
	            		else if(priority.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_NORMAL_STR))
	            			holder.indicator.setImageResource(R.drawable.high_icon);
            		}
                	
            		String status = category.getStatus();            		
            		ServiceProConstants.showLog("status : "+status+"  "+position);
            		StatusOpConstraints statusObj = getStatusDetails(status);	
            		ServiceProConstants.showLog("statusObj : "+statusObj);
            		if(statusObj != null){
            			String imageName = statusObj.getZZStatusIcon().toString().toLowerCase().trim();
            			if(imageName != null){
            				holder.status.setVisibility(View.VISIBLE);
            				int resID = getResources().getIdentifier(imageName , "drawable", getPackageName());
                			holder.status.setImageResource(resID);
            				ServiceProConstants.showLog("image 1");
            			}else{
            				holder.status.setVisibility(View.GONE);
            				ServiceProConstants.showLog("image 2");
            			}            			
            		}else{
            			holder.status.setVisibility(View.GONE);
        				ServiceProConstants.showLog("image 3");
            		}
            		
            		/*if(status.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_READY_STR_FOR_SAP)){
            			holder.status.setImageResource(R.drawable.light_grey);
    				}
    				else if(status.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
    					holder.status.setImageResource(R.drawable.tl_green);
    				}
    				else if(status.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_DEFERRED_STR_FOR_SAP)){
    					holder.status.setImageResource(R.drawable.tl_yellow);
    				}	
    				else if(status.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_DECLINED_STR_FOR_SAP)){
    					holder.status.setImageResource(R.drawable.tl_red);
    				}
    				else if(status.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_COMPLETED_STR_FOR_SAP)){
    					holder.status.setImageResource(R.drawable.tl_blue);
    				}*/
            		            		
            		String date_str = category.getZZKEeyDate();
            		String[] date_value = date_str.split("-");          
            		//ServiceProConstants.showLog("getId : "+category.getObjectId().toString().trim());
            		holder.date.setText(ServiceProConstants.getMonthValue(Integer.parseInt(date_value[1]))+" "+date_value[2]+",");
            		if(status.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
            			holder.date.setTypeface(null, Typeface.BOLD); 
    				}else{
    					holder.date.setTypeface(null, Typeface.NORMAL); 
    				}
            		
            		if(category.getNameOrg1().toString().length() > 0){
                		data += category.getNameOrg1().toString();
                	}
                	if(category.getNameOrg2().toString().length() > 0){
                		data += " "+category.getNameOrg2().toString();
                	}
                	if(category.getStreet().toString().length() > 0){
                		data += "\n"+category.getStreet().toString();
                	}
                	if(category.getCity().toString().length() > 0){
                		data += "\n"+category.getCity().toString();
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
            		holder.text.setText(data); 
            		
            		String item = category.getNumberExtn();
            		String taskid = category.getObjectId();
            		holder.taskid.setText(getResources().getString(R.string.SERVICEPRO_LBLDOC)+" "+taskid+", "+getResources().getString(R.string.SERVICEPRO_LBLITEM)+" "+item);
            		            		
            		if(status.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
            			holder.text.setTypeface(null, Typeface.BOLD);
            			holder.taskid.setTypeface(null, Typeface.BOLD);
    				}else{
    					holder.text.setTypeface(null, Typeface.NORMAL); 
    					holder.taskid.setTypeface(null, Typeface.NORMAL); 
    				}   		
            		            		
            		boolean isExits = ServiceProConstants.errorTaskIdForStatus.contains(category.getObjectId().toString().trim());
            		if(isExits){
            			holder.errstatus.setVisibility(View.VISIBLE);
            		}
            		else{
            			holder.errstatus.setVisibility(View.GONE);
            		}
            	}
            }            
            return convertView;
        }

        class ViewHolder {
            TextView text;
            TextView taskid;
            TextView date;
            ImageView indicator;
            ImageView status;
            ImageView errstatus;
            LinearLayout llitembg;
        }
        
        public void removeAllTasks() {
            notifyDataSetChanged();
        }        
    }//End of MyTaskListAdapter	
	
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
			        	}else{
			        		statusObj = null;
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
			ServiceProConstants.showLog("errorAvalForThisUserTask calling:");
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
									
			Collections.sort(taskList, reportSortComparator);
			ServiceProConstants.showLog("taskList:"+taskList.size()); 
			getListView().invalidateViews();
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
                            comp = (int)(intObj2 - intObj1);
                        else
                            comp = (int)(intObj1 - intObj2);
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

	private void insertStatusFollowListDataIntoDB(){    
		StatusFollowOpConstraints serStatusFollowListObj;
		try {
			if(statusFollowListObj != null && statusFollowListObj.size() > 0){
				for(int k=0; k<statusFollowListObj.size(); k++){
					serStatusFollowListObj = (StatusFollowOpConstraints) statusFollowListObj.get(k);
					if(serStatusFollowListObj != null){
						ServiceProDBOperations.insertStatusFollowListDataInToDB(this, serStatusFollowListObj);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertStatusFollowListDataIntoDB func: "+ewe.toString());
		}
	}//fn insertStatusFollowListDataIntoDB
    
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
			ServiceProConstants.FIRST_LANUCH_SCR = 0;
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}   
}//End of class ServiceProTaskMainScreenForPhone