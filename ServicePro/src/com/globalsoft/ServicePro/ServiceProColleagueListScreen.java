package com.globalsoft.ServicePro;

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
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.SapQueueProcessorHelper.Utils.SapQueueProcessorHelperConstants;
import com.globalsoft.ServicePro.Constraints.ServiceProColleaguesOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Database.ServiceProOfflineContraintsCP;
import com.globalsoft.ServicePro.SoapConnection.HttpTransportSE;

public class ServiceProColleagueListScreen  extends ListActivity {

	private ServiceProOutputConstraints category = null;
	private ListView listView;
	private ProgressDialog pdialog = null;
	private SoapObject resultSoap = null;
	private ArrayList collArrList = new ArrayList();
	private String[] colItems;
	private int colleagueSel = -1;
	private AlertDialog alertDialog;
	private String taskId = "", taskProcessType = "", serviceEmployeeId = "";
	private boolean internetAccess = false;
	private static int viewMode = -1;
	final Handler handlerForInsertNewDataCall = new Handler();
	final Handler handlerForTransfer = new Handler();
	private ServiceProTaskDetailsPersistent taskDbObj = null;
	private ServiceProColleaguesOpConstraints collCategory = null;
	private String taskErrorMsgStr="", taskErrorType="";
	
	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
	private SoapObject requestSoapObj = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_LEFT_ICON);
		ServiceProConstants.setWindowTitleTheme(this);
		/*setContentView(R.layout.colleaguelist);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.colleaguelist); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SERVICEPRO_COLLEAGUE_LIST_TITLE));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
		
		try{			
			internetAccess = ServiceProConstants.checkInternetConn(ServiceProColleagueListScreen.this);
			int viewAction = this.getIntent().getIntExtra("ColleagueViewOption", ServiceProConstants.VIEWCOLLEAGUELIST);
			if(viewAction == ServiceProConstants.VIEWCOLLEAGUELIST){
				//setTitle(getResources().getString(R.string.SERVICEPRO_COLLEAGUE_LIST_TITLE_FOR_COLLLIST));
				myTitle.setText(getResources().getString(R.string.SERVICEPRO_COLLEAGUE_LIST_TITLE_FOR_COLLLIST));
				viewMode = ServiceProConstants.VIEWCOLLEAGUELIST;
			}else if(viewAction == ServiceProConstants.VIEWCOLLEAGUELISTANDTRANS){
				//setTitle(getResources().getString(R.string.SERVICEPRO_COLLEAGUE_LIST_TITLE_FOR_TRANS));
				myTitle.setText(getResources().getString(R.string.SERVICEPRO_COLLEAGUE_LIST_TITLE_FOR_TRANS));
				viewMode = ServiceProConstants.VIEWCOLLEAGUELISTANDTRANS;
				category = (ServiceProOutputConstraints)this.getIntent().getSerializableExtra("conftaskobj");
				if(category != null){
					taskId = category.getObjectId().toString().trim();
					taskProcessType = category.getProcessType().toString().trim();						
					/*ServiceProConstants.showLog("Id : "+category.getObjectId());	
					ServiceProConstants.showLog("ProcessType : "+category.getProcessType());*/
				}
			}			
			initLayout();
			getColleagueList();
		}
		catch (Exception de) {
			ServiceProConstants.showErrorLog("Error in oncreate  ServiceProColleagueListScreen: "+de.toString());
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
    
    private void taskToColleBtnAction(){
    	try {
    		ServiceProColleaguesOpConstraints collObj = null;
			if(colleagueSel != -1){
				collObj = (ServiceProColleaguesOpConstraints)collArrList.get(colleagueSel);
				if(collObj != null){
					String name = collObj.getMcName1() + "  "+ collObj.getMcName2();
					if(name != null && name.length() > 0){
						name = name.trim();
						serviceEmployeeId = collObj.getPartner().toString().trim();
						displayDialog(name);
					}
					else{
						ServiceProConstants.showErrorDialog(this, "Please select any colleague from list!");
					}
				}
			}
			else{
				ServiceProConstants.showErrorDialog(this, "Please select any colleague from list!");
			}
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in taskToColleBtnAction"+e.toString());
		}
    }//fn taskToColleBtnAction
    
    private void colleTaskListBtnAction(){
    	try {
    		ServiceProColleaguesOpConstraints collObj = null;
			if(colleagueSel != -1){
				collObj = (ServiceProColleaguesOpConstraints)collArrList.get(colleagueSel);
				if(collObj != null){
					//ServiceProConstants.showLog("Colle Partner: "+collObj.getPartner());
					Intent intent = new Intent(this, ServiceProColleagueTaskListScreen.class);
	            	intent.putExtra("collobj", collObj);
	    			startActivityForResult(intent, ServiceProConstants.COLLEAGUE_TASK_LIST_SCREEN);
				}
			}
			else{
				ServiceProConstants.showErrorDialog(this, "Please select any colleague from list!");
			}
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in colleTaskListBtnAction"+e.toString());
		}
    }//fn colleTaskListBtnAction
    
    private void displayDialog(String name){
		try{
			alertDialog = new AlertDialog.Builder(this)
			.setMessage("Are you sure you want to transfer Task to: "+name)
			.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					transferTaskToColleague();
				}
				})			
			.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
				})
				.create();
				alertDialog.show();
		}
		catch(Exception asfg){
			ServiceProConstants.showErrorLog("Error in displayDialog : "+asfg.toString());
		}
	}//fn displayDialog	
	
	OnItemClickListener listItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			try{
				colleagueSel = arg2;
				if(viewMode == ServiceProConstants.VIEWCOLLEAGUELIST){
					colleTaskListBtnAction();
				}else if(viewMode == ServiceProConstants.VIEWCOLLEAGUELISTANDTRANS){
					taskToColleBtnAction();
				}				
			}
			catch (Exception dee) {
		    	ServiceProConstants.showErrorLog("Error in listItemClickListener:"+dee.toString());
		    }
		}
	};
	
	private void transferTaskToColleague(){
		try{						
			initSoapConnectionTTForColleague();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in transferTaskToColleague : "+sfg.toString());
		}
	}//fn transferTaskToColleague
	
	private void getColleagueList(){
		try{				
			if(internetAccess){
				if(collCategory != null)
					collCategory = null;
				initSoapConnection();				
			}
			else{
				getLDBColleagueList();
			}	
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in getColleagueList : "+sfg.toString());
		}
	}//fn getColleagueList
	
	public void getLDBColleagueList(){
		try {
			if(collArrList != null)
	    		collArrList.clear();			
			ArrayList colleagueArray = ServiceProDBOperations.readAllColleagueListDataFromDB(this);
			ServiceProColleaguesOpConstraints collCategory = null;
			if(colleagueArray.size() != 0){				
				for(int i = 0; i < colleagueArray.size(); i++){  
					collCategory = ((ServiceProColleaguesOpConstraints)colleagueArray.get(i));
			        if(collCategory != null){
			        	collArrList.add(collCategory);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for colleague!");
			}		    
		    if(collArrList.size() == 0)
		    	ServiceProConstants.showErrorDialog(this, getResources().getString(R.string.SERVICEPRO_COLLEAGUE_LIST_ERROR_MSG_OFFLINE));
		    
		    prefillCollData();
		} catch (NotFoundException esgg) {
			ServiceProConstants.showErrorLog("Error in getLDBColleagueList :"+esgg.toString());
		}
	}//fn getLDBTaskList
	
	private void initSoapConnectionTTForColleague(){   
    	SoapSerializationEnvelope envelopeC = null;
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[5];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
                        
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]SERVICE-DOX-TRANSFER[.]VERSION[.]0";
            C0[3].Cdata = "DATA-TYPE[.]ZGSXSMST_SRVCDCMNTTRNSFR21[.]OBJECT_ID[.]PROCESS_TYPE[.]SERVICE_EMPLOYEE";
            C0[4].Cdata = "ZGSXSMST_SRVCDCMNTTRNSFR21[.]"+taskId+"[.]"+taskProcessType+"[.]"+serviceEmployeeId;            
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
            
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
            respType = ServiceProConstants.TASK_TRANS_TYPE;
    		requestSoapObj = request;
            internetAccess = ServiceProConstants.networkAvailableCheck(ServiceProColleagueListScreen.this);
            if(internetAccess){
                //startNetworkConnectionTTForColleague(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL);
            	doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
            }else{
            	saveToLDB();
            }            	
        }
        catch(Exception asd){
        	ServiceProConstants.showErrorLog("Error in Service Confirm initSoapConnection : "+asd.toString());
        }
    }//fn initSoapConnectionTTForColleague
	
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
	    			if(respType == ServiceProConstants.TASK_TRANS_TYPE)
	    				updateTaskResponseTTForColleague(resultSoap);
	    			else if(respType == ServiceProConstants.COLLEAGE_TASK_TYPE)
	    				updateReportsConfirmResponse(resultSoap);	    			
	    		}
	    		else{
	    			if(respType == ServiceProConstants.TASK_TRANS_TYPE)
	    				sendToQueueProcessor();
	    			else if(respType == ServiceProConstants.COLLEAGE_TASK_TYPE)
	    				getLDBColleagueList();
	    		}
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };
    
    private void sendToQueueProcessor(){
    	try {
    		saveToLDB();
    		//SapQueueProcessorHelperConstants.saveOfflineContentToQueueProcessor(this, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.APPLN_PACKAGE_NAME, taskId, ServiceProConstants.APPLN_BGSERVICE_NAME, ServiceProConstants.TASK_TRANS_API, requestSoapObj);
		} catch (Exception errg) {
			SapGenConstants.showErrorLog("Error in sendToQueueProcessor : "+errg.toString());
		}
    }//fn sendToQueueProcessor
    
    private void saveToLDB(){
		try{
        	Long now = Long.valueOf(System.currentTimeMillis());
        	//SapQueueProcessorHelperConstants.saveOfflineContentToQueueProcessor(ServiceProColleagueListScreen.this, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.APPLN_PACKAGE_NAME, taskId, ServiceProConstants.APPLN_BGSERVICE_NAME, ServiceProConstants.TASK_TRANS_API, requestSoapObj, now);
        	Thread t = new Thread() 
			{
	            public void run() 
				{
        			try{
        				addRemoveTaskDetails();
        			} catch (Exception e) {
        				ServiceProConstants.showErrorLog("Error in saveToLDB addRemoveTaskDetails:"+e.toString());
        			}
        			handlerForTransfer.post(refreshDataCall);
				}
			};
	        t.start();	
		}
	    catch (Throwable e) {
	        ServiceProConstants.showErrorLog("Error in saveToLDB : "+e.toString());
	    }
	}//fn saveToLDB
    
    /*private void startNetworkConnectionTTForColleague(final Context ctxAct, final SoapSerializationEnvelope envelopeCE, final String url){
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
                    				getSOAPViaHTTPTTForColleague(envelopeCE, url);
                    				sleep(2000);
                    			} catch (Exception e) {  }
             				}
                    	}.start();
                    }
                });
		} catch (Exception ae) {
			ServiceProConstants.showErrorLog(ae.toString());
		}
    }//fn startNetworkConnectionTTForColleague
    
    private void getSOAPViaHTTPTTForColleague(SoapSerializationEnvelope envelopeCE, String url){		
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
            stopProgressDialog();
            this.runOnUiThread(new Runnable() {
                public void run() {
                	updateTaskResponseTTForColleague(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTPTTForColleague
*/            
    public void updateTaskResponseTTForColleague(SoapObject soap){
    	String taskErrorMsgStr = "", soapMsg="";
    	boolean errorflag = false;
    	try{
	    	if(soap != null){
	    		soapMsg = soap.toString();
                String delimeter = "[.]", result="", res="";
                SoapObject pii = null;
                String[] resArray = new String[37];
                int propsCount = 0, errCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0;
                for (int i = 0; i < soap.getPropertyCount(); i++) {                
                    pii = (SoapObject)soap.getProperty(i);
                    propsCount = pii.getPropertyCount();
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                            if(j >= 3){
                                result = pii.getProperty(j).toString();
                                firstIndex = result.indexOf(delimeter);
                                firstIndex = firstIndex + 3;
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
                            }
                            else if(j < 3){
                                String errorMsg = pii.getProperty(j).toString();
                                //ServiceProConstants.showLog("Inside J == "+j+" : "+propsCount+" : "+errorMsg);
                                int errorFstIndex = errorMsg.indexOf("Message=");
                                if(errorFstIndex > 0){
	                                int errorLstIndex = errorMsg.indexOf(";", errorFstIndex);
	                                taskErrorMsgStr = errorMsg.substring((errorFstIndex+"Message=".length()), errorLstIndex);
	                                ServiceProConstants.showLog(taskErrorMsgStr);
	                                ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
	                            }
                            }
                        }
                    }
                }	                   
	        }
	        else if(!taskErrorMsgStr.equalsIgnoreCase("")){
	            taskErrorMsgStr = "";
	            //ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
	            ServiceProConstants.showLog("taskErrorMsgStr:"+taskErrorMsgStr);
	            errorflag = true;
	        }
    	}
        catch(Exception sff){
        	ServiceProConstants.showErrorLog("On updateSOCResponse : "+sff.toString());
        }    
    	finally{
    		try{
	    		if(!errorflag){
	    			Intent coll_list_intent = getIntent();		
	    			coll_list_intent.putExtra("refresh", true);
	    			setResult(RESULT_OK,coll_list_intent);
	    			finish();
	    		}else{
	    			Thread t = new Thread() 
					{
			            public void run() 
						{
		        			try{
		        				addRemoveTaskDetails();
		        			} catch (Exception e) {
		        				ServiceProConstants.showErrorLog("Error in updateResponse addRemoveTaskDetails:"+e.toString());
		        			}
		        			handlerForTransfer.post(refreshDataCall);
						}
					};
			        t.start();	    			
	    		}
	    	}
	        catch(Exception sff){
	        	ServiceProConstants.showErrorLog("On updateResponse finally : "+sff.toString());
	        }    
    	}
    }//fn updateTaskResponseTTForColleague
    
    private void addRemoveTaskDetails(){
		try {			
			ServiceProDBOperations.insertCollTasksDataInToDB(this, category, serviceEmployeeId);
			if(taskDbObj == null)
				taskDbObj = new ServiceProTaskDetailsPersistent(this);
			String taskId = category.getObjectId().toString().trim();
			taskDbObj.deleteRow(taskId);
			if(taskDbObj != null)
    			taskDbObj.closeDBHelper();			
		} catch (Exception esff) {
			ServiceProConstants.showErrorLog("On addRemoveTaskDetails : "+esff.toString());
		}    	
    }//fn addRemoveTaskDetails
    
    final Runnable refreshDataCall = new Runnable(){
	    public void run()
	    {
	    	try{
	    		Intent coll_list_intent = getIntent();		
				coll_list_intent.putExtra("refresh", true);
				setResult(RESULT_OK,coll_list_intent);
				finish();
	    	} catch(Exception eesff){
	    		ServiceProConstants.showErrorLog("On refreshDataCall : "+eesff.toString());
	    	}
	    }	    
    };
	
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
            C0[2].Cdata = "EVENT[.]SERVICE-COLLEAGUE-LIST[.]VERSION[.]0";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
            
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
            respType = ServiceProConstants.COLLEAGE_TASK_TYPE;
            //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL);
            doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in initSoapConnection : "+asd.toString());
        }
    }//fn initSoapConnection
	
	/*private void startNetworkConnection(final Context ctxAct, final SoapSerializationEnvelope envelopeCE, final String url){
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
            stopProgressDialog();
            this.runOnUiThread(new Runnable() {
                public void run() {
                    updateReportsConfirmResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP
*/	
	public void updateReportsConfirmResponse(SoapObject soap){	
		boolean errorflag = false, resMsgErr = false;
        try{ 
        	if(soap != null){
        		ServiceProConstants.soapResponse(this, soap, false);
            	taskErrorMsgStr = ServiceProConstants.SOAP_RESP_MSG;
            	taskErrorType = ServiceProConstants.SOAP_ERR_TYPE;
            	ServiceProConstants.showLog("On soap response : "+soap.toString());
            	String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg);   
            	ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
            	if(!resMsgErr){
		    		ServiceProColleaguesOpConstraints collCategory = null;
		            String delimeter = "[.]", result="", res="", docTypeStr = "";
		            SoapObject pii = null;
		            String[] resArray = new String[37];
		            int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;
		            
		            for (int i = 0; i < soap.getPropertyCount(); i++) {                
		                pii = (SoapObject)soap.getProperty(i);
		                propsCount = pii.getPropertyCount();
		                if(propsCount > 0){
		                    for (int j = 0; j < propsCount; j++) {
		                        if(j > 2){
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
		                            resArray[resC] = result.substring(indexA, endIndex);
		                            
		                            if(docTypeStr.equalsIgnoreCase("ZGSXCAST_EMPLY01")){
		                                if(collCategory != null)
		                                	collCategory = null;
		                                    
		                                collCategory = new ServiceProColleaguesOpConstraints(resArray);
		                                if(collArrList != null)
		                                	collArrList.add(collCategory);
		                            }
		                        }
		                        else if(j == 0){
		                            String errorMsg = pii.getProperty(j).toString();
		                            //ServiceProConstants.showLog("Inside J == 0 ");
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
            		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
            	}    			
        	}else{
        		errorflag = true;
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateTaskResponse : "+sff.toString());
            errorflag = true;
        } 
        finally{        	
        	if(!errorflag){
	            if(collArrList.size() > 0){    	            
	            	ServiceProDBOperations.deleteAllColleagueListDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_COLLEAGUELIST_CONTENT_URI);        	        	
	            }
	            insertColleagueListDataIntoDB();	            
	        	getLDBColleagueList();
        	}else{
        		getLDBColleagueList();
        	}
        }
    }//fn updateReportsConfirmResponse  
	    
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
	
    private void prefillCollData(){
    	try{
			if(collArrList != null){
				 if(collArrList.size() > 0){
					 ServiceProColleaguesOpConstraints empObj = null;
					 String str1="", str2="", combStr="";   
					 colItems = new String[collArrList.size()];
					 for(int h=0; h<collArrList.size(); h++){
						 empObj = (ServiceProColleaguesOpConstraints)collArrList.get(h);
						 if(empObj != null){
							 str1 = empObj.getMcName1().trim();
							 str2 = empObj.getMcName2().trim();
							 combStr = str1+" "+str2;
							 colItems[h] = combStr;
						 }
					 }
					 listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colItems));
				 }	        	
			}
			initLayout();
    	}
    	catch(Exception adf){
    		ServiceProConstants.showErrorLog("On prefillCollData : "+adf.toString());
    	}
    }//fn prefillCollData
    
    private void insertColleagueListDataIntoDB(){
    	ServiceProColleaguesOpConstraints collCategory;
    	try {
			if(collArrList != null && collArrList.size() > 0){
				for(int k=0; k<collArrList.size(); k++){
					collCategory = (ServiceProColleaguesOpConstraints) collArrList.get(k);
					if(collCategory != null){
						ServiceProDBOperations.insertColleagueListDataInToDB(this, collCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertColleagueListDataIntoDB func: "+ewe.toString());
		}
    }//fn insertColleagueListDataIntoDB
    
    public class MyListAdapter extends BaseAdapter {  
    	private LayoutInflater mInflater;
    	private ServiceProColleaguesOpConstraints empObj = null;
    	String str1="", str2="", combStr="";   
        public MyListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }
        public int getCount() {
        	try {
				if(collArrList != null)
					return collArrList.size();
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
                convertView = mInflater.inflate(R.layout.colleaguemain_list, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }                       
            if(collArrList != null){
            	empObj = (ServiceProColleaguesOpConstraints)collArrList.get(position);
            	if(empObj != null){
            		str1 = empObj.getMcName1().trim();
            		str2 = empObj.getMcName2().trim();
					combStr = str1+" "+str2;
            	}
                holder.name.setText(combStr); 
            }
                       
            return convertView;
        }
        class ViewHolder {
            TextView name;
        }        
        public void removeAllTasks() {
            notifyDataSetChanged();
        }        
    }//End of MyListAdapter
}