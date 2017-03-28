package com.globalsoft.ServicePro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.ServiceProColleaguesOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Database.ServiceProOfflineContraintsCP;
import com.globalsoft.ServicePro.SoapConnection.HttpTransportSE;

public class ServiceProColleagueTaskListScreen extends Activity implements TextWatcher{
	
	private ServiceProColleaguesOpConstraints collObj = null;
	private String collName = "", collPhNo1 = "", collPhNo2 = "", collPartnerId = "", collUName = "";
	private TextView collNameTV, collPhNo1TV, collPhNo2TV;
	private ImageButton collcall1btn, collcall2btn;
	private ProgressDialog pdialog = null;
	private ServiceProOutputConstraints category = null;
	private static ArrayList collTaskList = new ArrayList();
	private static ArrayList allcollTaskList = new ArrayList();
	private SoapObject resultSoap = null;
	private EditText searchET;
	private String searchStr = "";
	private boolean internetAccess = false;
	
	private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5, tableHeaderTV6, tableHeaderTV7;
    private static int dispwidth = 300;
    private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5, headerWidth6, headerWidth7;
    private ImageView[] priorityIcon;
    private ImageView[] statusIcon;
    private TextView[] custLocTxtView;
    private TableLayout colTableLayout = null;
    final Handler handlerForLayout = new Handler();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_LEFT_ICON);
		ServiceProConstants.setWindowTitleTheme(this);
		/*setContentView(R.layout.colleaguetasktbl);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.colleaguetasktbl); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SERVICEPRO_COLLEAGUE_TASK_LIST_TITLE));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
		
		try{
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			internetAccess = ServiceProConstants.checkInternetConn(ServiceProColleagueTaskListScreen.this);
			collObj = (ServiceProColleaguesOpConstraints)this.getIntent().getSerializableExtra("collobj");
			if(collObj != null){
				collName = collObj.getMcName1().toString().trim()+ " " +collObj.getMcName2().toString().trim();
				collPartnerId = collObj.getPartner().toString().trim();
				collUName = collObj.getUName().toString().trim();
				collPhNo1 = collObj.getTelNo().toString().trim();
				collPhNo2 = collObj.getTelNo2().toString().trim();
				//setTitle("Tasks for "+collName);
				myTitle.setText("Tasks for "+collName);
			}						
			clearAllColleagueTaskListArray();		
			Thread t = new Thread() 
			{
	            public void run() 
				{
        			try{
        				initLayout();
        				sleep(500);
        			} catch (Exception e) {
        				ServiceProConstants.showErrorLog("Error in oncreate initLayout:"+e.toString());
        			}
        			handlerForLayout.post(displayData);
				}
			};
	        t.start();	
		}
		catch (Exception de) {
			ServiceProConstants.showErrorLog("Error in oncreate  ServiceProColleagueTaskListScreen: "+de.toString());
        }
	}
	
	final Runnable displayData = new Runnable(){
	    public void run()
	    {
	    	try{
	    		getColleagueTaskList();	
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }	    
    };
    
	private void initLayout(){
		try{		
			collNameTV  = (TextView) findViewById(R.id.colleagueNameTV);
			collPhNo1TV  = (TextView) findViewById(R.id.collTelNo1TV);
			collPhNo2TV  = (TextView) findViewById(R.id.collTelNo2TV);
			collNameTV.setText(collName);
			collPhNo1TV.setText(collPhNo1);
			collPhNo2TV.setText(collPhNo2);				
			collcall1btn = (ImageButton) findViewById(R.id.collcall1btn);
			collcall1btn.setOnClickListener(collcall1btnListener); 
			if(collPhNo1.length() > 0){
				collcall1btn.setVisibility(View.VISIBLE);
			}else{
				collcall1btn.setVisibility(View.GONE);
			}
			collcall2btn = (ImageButton) findViewById(R.id.collcall2btn);
			collcall2btn.setOnClickListener(collcall2btnListener); 
			if(collPhNo2.length() > 0){
				collcall2btn.setVisibility(View.VISIBLE);
			}else{
				collcall2btn.setVisibility(View.GONE);
			}			
			
			searchET = (EditText)findViewById(R.id.searchBEF);
			searchET.setText(searchStr);
			searchET.addTextChangedListener(this); 			
			layTaskListTableHeader();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in initLayout : "+sfg.toString());
		}
	}//fn initLayout
		
	private void layTaskListTableHeader(){
		try{
			tableHeaderTV1 = (TextView)findViewById(R.id.TableHeaderTV1);
			tableHeaderTV1.setGravity(Gravity.LEFT);
			tableHeaderTV1.setPadding(10,5,5,5);
			
			tableHeaderTV2 = (TextView)findViewById(R.id.TableHeaderTV2);
			tableHeaderTV2.setGravity(Gravity.LEFT);
			tableHeaderTV2.setPadding(10,5,5,5);
			
			tableHeaderTV3 = (TextView)findViewById(R.id.TableHeaderTV3);
			tableHeaderTV3.setGravity(Gravity.LEFT);
			tableHeaderTV3.setPadding(10,5,5,5);
			
			tableHeaderTV4 = (TextView)findViewById(R.id.TableHeaderTV4);
			tableHeaderTV4.setGravity(Gravity.LEFT);
			tableHeaderTV4.setPadding(10,5,5,5);
			
			tableHeaderTV5 = (TextView)findViewById(R.id.TableHeaderTV5);
			tableHeaderTV5.setGravity(Gravity.LEFT);
			tableHeaderTV5.setPadding(10,5,5,5);
			
			tableHeaderTV6 = (TextView)findViewById(R.id.TableHeaderTV6);
			tableHeaderTV6.setGravity(Gravity.LEFT);
			tableHeaderTV6.setPadding(10,5,5,5);
			
			tableHeaderTV7 = (TextView)findViewById(R.id.TableHeaderTV7);
			tableHeaderTV7.setGravity(Gravity.LEFT);
			tableHeaderTV7.setPadding(10,5,5,5);
						
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
	                //ServiceProConstants.showLog("tableHeaderTV7 Width1 : "+headerWidth7+" : "+tableHeaderTV7.getMeasuredWidth());
	            }
	        });
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in layTaskListTableHeader : "+sfg.toString());
		}
	}//fn layTaskListTableHeader	
	
	private OnClickListener collcall1btnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	if(collPhNo1.length() > 0)
			{
				try{
					String telno = collPhNo1;
					int fstIndex = telno.indexOf("+");
					if(fstIndex >= 0)
						telno = telno.replace("+","");
					startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telno)));
				}
				catch(Exception sfge){
					ServiceProConstants.showErrorLog("Error in collcall1btnbt1Listener : "+sfge.toString());
				}
			}
        }
    };
    
    private OnClickListener collcall2btnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	if(collPhNo2.length() > 0)
			{
				try{
					String telno = collPhNo2;
					int fstIndex = telno.indexOf("+");
					if(fstIndex >= 0)
						telno = telno.replace("+","");
					startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telno)));
				}
				catch(Exception sfge){
					ServiceProConstants.showErrorLog("Error in collcall2btnListener : "+sfge.toString());
				}
			}
        }
    };
    
    private void getColleagueTaskList(){
		try{					
			if(internetAccess){
				initSoapConnection();
			}else{
				getLDBColleagueTaskList();
				drawSubLayout();
				//displayDialog();
			}			
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in getColleagueTaskList : "+sfg.toString());
		}
	}//fn getColleagueTaskList
    
    private void displayDialog(){
		try{
			ServiceProConstants.showErrorDialog(this, getResources().getString(R.string.SERVICEPRO_ERROR_MSG_OFFLINE));
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in displayDialog:"+e.getMessage());
		}
	}//fn displayDialog
    
    private void initSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[4];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }
                        
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]SERVICE-DOX-FOR-COLLEAGUE-GET[.]VERSION[.]0";
            C0[3].Cdata = "SWDTUSER[.]"+collUName;
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
            
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
          
            startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL);
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in initSoapConnection : "+asd.toString());
        }
    }//fn initSoapConnection
	
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
	
	public void updateReportsConfirmResponse(SoapObject soap){	
		boolean errorflag = false;
        try{ 
        	if(soap != null){
        		if(collTaskList != null)
        			collTaskList.clear();
        		if(allcollTaskList != null)
        			allcollTaskList.clear();
	    		//ServiceProConstants.showLog("Count : "+soap.getPropertyCount());
	    		ServiceProColleaguesOpConstraints taskColCategory = null;
	            String delimeter = "[.]", result="", res="", docTypeStr = "";
	            SoapObject pii = null;
	            String[] resArray = new String[50];
	            int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;
	            
	            for (int i = 0; i < soap.getPropertyCount(); i++) {                
	                pii = (SoapObject)soap.getProperty(i);
	                propsCount = pii.getPropertyCount();
	                //ServiceProConstants.showLog("propsCount : "+propsCount);
	                if(propsCount > 0){
	                    for (int j = 0; j < propsCount; j++) {
	                    	//ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
	                        if(j > 0){
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
	                            
	                            if(docTypeStr.equalsIgnoreCase("ZGSXSMST_SRVCDCMNT01")){                                    
	                            	category = new ServiceProOutputConstraints(resArray);
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
	                            		                            		                            	
	                                if(category != null){
	                                	collTaskList.add(category);
	                                	allcollTaskList.add(category);
	                                } 
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
	            
        	}//if
        	else{
        		errorflag = true;
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateTaskResponse : "+sff.toString());
        } 
        finally{
        	if(!errorflag){
	        	if((collTaskList != null) && (collTaskList.size() > 0)){
	        		ServiceProConstants.showLog("collTaskArrList Size : "+collTaskList.size());
	        		ServiceProDBOperations.deleteAllCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_COLL_TASKS_CONTENT_URI, collPartnerId);
					insertColleagueTaskDataIntoDB();
	        	}else{
	        		ServiceProConstants.showLog("collTaskArrList Size : "+collTaskList.size());
	        		ServiceProConstants.showErrorDialog(this, getResources().getString(R.string.SERVICEPRO_COLLTASKEMPTY)+" "+collName);
	        	}        	
	        	drawSubLayout();
        	}
        	else{
            	getLDBColleagueTaskList();
    			drawSubLayout();
        	}
        }
    }//fn updateReportsConfirmResponse    
	
	public void getLDBColleagueTaskList(){
		try {		
			clearAllColleagueTaskListArray();		    
			allcollTaskList = ServiceProDBOperations.readAllCollTasksDataFromDB(this, collPartnerId);		
			if(category != null)
				category = null;	
			
			for(int i = 0; i < allcollTaskList.size(); i++){  
				category = ((ServiceProOutputConstraints)allcollTaskList.get(i));
		        if(category != null){
		        	collTaskList.add(category);
		        }
		    }
			ServiceProConstants.showLog("collTaskList size:"+collTaskList.size());
			
			if(collTaskList.size() == 0)
				ServiceProConstants.showErrorDialog(this, getResources().getString(R.string.SERVICEPRO_COLLTASKEMPTY)+collName);
		} catch (Exception esgg) {
			ServiceProConstants.showErrorLog("Error in getLDBColleagueTaskList :"+esgg.toString());
		}
	}//fn getLDBColleagueTaskList
	
	private void clearAllColleagueTaskListArray(){
		try{
	    	if(collTaskList != null)
	    		collTaskList.clear();
	    	
            if(allcollTaskList != null)
            	allcollTaskList.clear();
		}
		catch(Exception sgg){
			ServiceProConstants.showErrorLog("Error in clearAllTaskListArray :"+sgg.toString());
		}
	}//fn clearAllColleagueTaskListArray
		
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
			
			if(collTaskList != null){
				ServiceProOutputConstraints category = null;
				String statusStr = "", priorityStr = "", custLocStr = "";
                String serOrdStr = "", contNameStr = "", productDescStr = "";
                String estDateStr = "", batchStr = "", syncStr = "", startDatStr = "";
                
                int rowSize = collTaskList.size();                
                ServiceProConstants.showLog("Stocks List Size  : "+rowSize);
                
                priorityIcon = new ImageView[rowSize];
                statusIcon = new ImageView[rowSize];
                custLocTxtView = new TextView[rowSize];
               
				for (int i =0; i < collTaskList.size(); i++) {
					category = (ServiceProOutputConstraints)collTaskList.get(i);
                    if(category != null){
                    	custLocStr = "";                    	
                    	try {
							priorityStr = category.getPriority().trim();
							statusStr = category.getStatus().trim();
							serOrdStr = category.getObjectId().trim();  
							productDescStr = category.getRefObj_Product_Descr().toString().trim();
							
							if(category.getCp1Name().toString().trim().length() != 0){
								contNameStr = category.getCp1Name();
							}
							
							if(category.getCity().toString().length() > 0){
								custLocStr += category.getCity().toString();
							}
							if(category.getRegion().toString().length() > 0){
								custLocStr += ","+category.getRegion().toString();
							}
							if(category.getNameOrg1().toString().length() > 0){
								custLocStr += ","+category.getNameOrg1().toString();
							}
							if(category.getNameOrg2().toString().length() > 0){
								custLocStr += ","+category.getNameOrg2().toString();
							}
							if(category.getStreet().toString().length() > 0){
								custLocStr += ","+category.getStreet().toString();
							}
							if(category.getPostalCode1().toString().length() > 0){
								custLocStr += ","+category.getPostalCode1().toString();
							}
							if(category.getCountryIso().toString().length() > 0){
								custLocStr += ","+category.getCountryIso().toString();
							}
														
							startDatStr = category.getZZKEeyDate().trim();
							if(!startDatStr.equalsIgnoreCase("")){
								String[] date_value = startDatStr.split("-");
								startDatStr = ServiceProConstants.getMonthValue(Integer.parseInt(date_value[1]))+" "+date_value[2];
							}
							
							estDateStr = category.getZzetaDate()+" "+category.getZzetaTime(); 
						}
                    	catch (Exception e1) {
                    		ServiceProConstants.showErrorLog("On drawSubLayout Assignment : "+e1.toString());
						}           
                    	
                        tr = new TableRow(this);
                        
                        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linear, null);
                        linearLayout.setPadding(0, 10, 0, 10);
                        linearLayout.setGravity(Gravity.LEFT);
                        
                    	priorityIcon[i] = new ImageView(this); 
                    	priorityIcon[i].setLayoutParams(linparams);   
                        if(priorityStr.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_LOW_STR))
                        	priorityIcon[i].setImageResource(R.drawable.low_icon);
	            		else if(priorityStr.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_HIGH_STR))
	            			priorityIcon[i].setImageResource(R.drawable.vhigh_icon);
	            		else if(priorityStr.equalsIgnoreCase(ServiceProConstants.TASK_PRIORITY_NORMAL_STR))
	            			priorityIcon[i].setImageResource(R.drawable.high_icon);
                        
                        linearLayout.addView(priorityIcon[i]);
                        
                        statusIcon[i] = new ImageView(this); 
                        statusIcon[i].setLayoutParams(linparams);   
                        if(statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_READY_STR_FOR_SAP)){
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
        				}
                        
                        linearLayout.addView(statusIcon[i]);                        
                       
                        custLocTxtView[i] = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					custLocTxtView[i].setText(custLocStr);
    					custLocTxtView[i].setId(i);
    					custLocTxtView[i].setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								int id = view.getId();	
								//showEditTaskScreen(id);
							}	
                        });
    					custLocTxtView[i].setWidth(headerWidth2);
    					custLocTxtView[i].setGravity(Gravity.LEFT);
    					custLocTxtView[i].setPadding(10,0,0,0);    			
						ServiceProConstants.showLog("custLocStr  : "+headerWidth2);
						ServiceProConstants.showLog("custLocStr  : "+custLocStr);
						
    					TextView strDateTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);    					
    					strDateTxtView.setText(startDatStr);
    					strDateTxtView.setWidth(headerWidth7);
    					strDateTxtView.setGravity(Gravity.LEFT);
    					strDateTxtView.setPadding(10,0,0,0);
    					ServiceProConstants.showLog("strDateTxtView  : "+headerWidth7);
    					
    					TextView estArrivalTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					try {
							if((!estDateStr.equalsIgnoreCase("")) && (!estDateStr.startsWith("00"))){
								String[] timeArr = estDateStr.split(" ");
								//ServiceProConstants.showLog("Time : "+timeArr.length+" : "+timeArr[0]+" : "+timeArr[1]);
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
						ServiceProConstants.showLog("estArrivalTxtView  : "+headerWidth3);
						
    					TextView serOrdTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					serOrdTxtView.setText(serOrdStr);
    					serOrdTxtView.setWidth(headerWidth4);
    					serOrdTxtView.setGravity(Gravity.LEFT);
    					serOrdTxtView.setPadding(10,0,0,0);
    					serOrdTxtView.setId(i);
    					serOrdTxtView.setOnClickListener(new View.OnClickListener() {	
    						public void onClick(View v) {
    							//showEditTaskScreen(v.getId());
    						}
    					});
    					ServiceProConstants.showLog("serOrdTxtView  : "+headerWidth4);
    					
    					TextView contNameTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					contNameTxtView.setText(contNameStr);
    					contNameTxtView.setWidth(headerWidth5);
    					contNameTxtView.setGravity(Gravity.LEFT);
    					contNameTxtView.setPadding(10,0,0,0);
    					ServiceProConstants.showLog("contNameTxtView  : "+headerWidth5);
    					
    					TextView prdTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);					
    					prdTxtView.setText(productDescStr);
    					prdTxtView.setWidth(headerWidth6);
    					prdTxtView.setGravity(Gravity.LEFT);
    					prdTxtView.setPadding(10,0,0,0);
    					ServiceProConstants.showLog("prdTxtView  : "+headerWidth6);
    					
    					if(!statusStr.equalsIgnoreCase(ServiceProConstants.TASK_STATUS_ACCEPTED_STR_FOR_SAP)){
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
            if((allcollTaskList != null) && (allcollTaskList.size() > 0)){
                if((!match.equalsIgnoreCase("")) && (match.length() >= 1)){
                	if((collTaskList != null) && (collTaskList.size() > 0)){
                		collTaskList.clear();
                	}                	
                    for(int i = 0; i < allcollTaskList.size(); i++){ 
                    	strValue = null;
                    	data = "";
                        mattStr = "";
                        category = (ServiceProOutputConstraints)allcollTaskList.get(i);
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
                            	collTaskList.add(category);
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
                                	collTaskList.add(category);
                                }
                            }
                        }
                    }//for 
                    //getListView().invalidateViews();
                    drawSubLayout();
                }
                else{
                	collTaskList.clear();
                    for(int i = 0; i < allcollTaskList.size(); i++){  
                    	category = null;
    					category = (ServiceProOutputConstraints)allcollTaskList.get(i);
                        
                        if(category != null){
                        	collTaskList.add(category);
                        }
                    }
                   // getListView().invalidateViews();
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
	
	private void insertColleagueTaskDataIntoDB(){
		ServiceProOutputConstraints category;
    	try {
			if(collTaskList != null){
				for(int k=0; k<collTaskList.size(); k++){
					category = (ServiceProOutputConstraints) collTaskList.get(k);
					if(category != null){
						ServiceProDBOperations.insertCollTasksDataInToDB(this, category, collPartnerId);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertEmployeeDataIntoDB: "+ewe.toString());
		}
    }//fn insertColleagueTaskDataIntoDB

}//End of class ServiceProColleagueTaskListScreen