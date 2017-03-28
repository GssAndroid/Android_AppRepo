package com.globalsoft.ServicePro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.apache.http.impl.cookie.DateUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.ReportOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Database.ReportOfflineCP;
import com.globalsoft.ServicePro.Database.ReportOfflineDB;
import com.globalsoft.ServicePro.SoapConnection.HttpTransportSE;

public class ReportMainScreen extends Activity {
	
	private ArrayList reportsList = new ArrayList();
	private SoapObject resultSoap = null;
	private ProgressDialog pdialog = null;
	
	private boolean sortFlag = false, sortCustFlag = false, sortDateFlag = false, sortSoFlag = false;
    private int sortIndex = -1, selIndex = 0;
    private int headerWidth1, headerWidth2, headerWidth3;
    
    private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3;
    private int dispwidth = 300;
    private static boolean isConnAvail = true;

    private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private SoapObject requestSoapObj = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	//requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
			/*setContentView(R.layout.reportmain);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.reportmain); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SERVICEPRO_RPDTSCR_TITLE));
			
			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			isConnAvail = ServiceProConstants.checkInternetConn(this);
			if(isConnAvail ==  true){
    			initSoapConnection();
    		}
    		else{
    			//Read from Database
    			initDBConnection();
    		}	
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	private void initLayout(){
		try {        	
			setContentView(R.layout.reportmain);
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			tableHeaderTV1 = (TextView)findViewById(R.id.TableHeaderTV1);
			tableHeaderTV1.setGravity(Gravity.LEFT);
			tableHeaderTV1.setPadding(10,5,5,5);
			tableHeaderTV1.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.REPORTS1_SORT_DATE);
				}
			});
			
			tableHeaderTV2 = (TextView)findViewById(R.id.TableHeaderTV2);
			tableHeaderTV2.setGravity(Gravity.LEFT);
			tableHeaderTV2.setPadding(10,5,5,5);
			tableHeaderTV2.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.REPORTS1_SORT_SERVORD);
				}
			});						
			SpannableString underlinedStr = ServiceProConstants.getUnderlinedString(tableHeaderTV2.getText().toString());
		    tableHeaderTV2.setText(underlinedStr);

			
			tableHeaderTV3 = (TextView)findViewById(R.id.TableHeaderTV3);
			tableHeaderTV3.setGravity(Gravity.LEFT);
			tableHeaderTV3.setPadding(10,5,5,5);
			tableHeaderTV3.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.REPORTS1_SORT_CUSTOMER);
				}
			});
			
			ViewTreeObserver vto1 = tableHeaderTV1.getViewTreeObserver();
	        vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV1.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth1 = tableHeaderTV1.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV1 Width1 : "+headerWidth1+" : "+tableHeaderTV1.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto2 = tableHeaderTV2.getViewTreeObserver();
	        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV2.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth2 = tableHeaderTV2.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV2 Width1 : "+headerWidth2+" : "+tableHeaderTV2.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto3 = tableHeaderTV3.getViewTreeObserver();
	        vto3.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV3.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth3 = tableHeaderTV3.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV3 Width1 : "+headerWidth3+" : "+tableHeaderTV3.getMeasuredWidth());
	                drawSubLayout();
	            }
	        });
			
			//View.invalidate();
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	private void drawSubLayout(){
		try{
			TableLayout tl = (TableLayout)findViewById(R.id.rprtmaintbllayout2);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			ServiceProConstants.showLog("Reports List Size after : "+reportsList.size());
			if(reportsList != null){
				ReportOpConstraints repCategory = null;
                String workenddate = "", objectidStr = "", customerStr = "";
				for (int i =0; i < reportsList.size(); i++) {
					repCategory = (ReportOpConstraints)reportsList.get(i);
                    if(repCategory != null){
                    	workenddate = repCategory.getReqStartDate().trim();    
                        objectidStr = repCategory.getObjectId().trim();
                        customerStr = repCategory.getSoldToPartyList();
                        
                        tr = new TableRow(this);
                    	
    					TextView dateTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					dateTxtView.setText(workenddate);
    					dateTxtView.setWidth(headerWidth1);
    					dateTxtView.setGravity(Gravity.LEFT);
    					dateTxtView.setPadding(10,0,0,0);
    					dateTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					TextView serialTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);   					
    					serialTxtView.setText(objectidStr);
    					serialTxtView.setWidth(headerWidth2);
    					serialTxtView.setGravity(Gravity.LEFT);
    					serialTxtView.setPadding(10,0,0,0);
    					serialTxtView.setId(i);
    					serialTxtView.setOnClickListener(new View.OnClickListener() {	
    						public void onClick(View v) {
    							showReportDetailScreen(v.getId());
    						}
    					});
    					serialTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					TextView custTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);	
    					custTxtView.setText(customerStr);
    					custTxtView.setWidth(headerWidth3);
    					custTxtView.setGravity(Gravity.LEFT);
    					custTxtView.setPadding(10,0,0,0);		   					
    					custTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
    						dateTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						serialTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						custTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    					}
                        
    					tr.addView(dateTxtView);
    					tr.addView(serialTxtView);
    					tr.addView(custTxtView);
    					
    					if(i%2 == 0)
    						tr.setBackgroundResource(R.color.item_even_color);
			            else
			            	tr.setBackgroundResource(R.color.item_odd_color);
    					
    					tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                    }					
				}
			}
		}
		catch(Exception asgf){
			ServiceProConstants.showErrorLog("Error in drawSubLayout : "+asgf.toString());
		}
	}//fn drawSubLayout
		
	
	private void initDBConnection(){
		try {
			reportsList = readAllReportDataFromDB();
			
			Collections.sort(reportsList, reportSortComparator);  
		} catch (Exception sse) {
			ServiceProConstants.showErrorLog("Error on initDBConnection: "+sse.toString());
		}
		finally{
        	try {
				initLayout();
			} catch (Exception e) {}
		}
	}//fn initDBConnection
	
	private void sortItemsAction(int sortInd){
		try{
			 sortFlag = true;
			 sortIndex = sortInd;
			 if(sortInd == ServiceProConstants.REPORTS1_SORT_DATE)
				 sortDateFlag = !sortDateFlag;
			 else if(sortInd == ServiceProConstants.REPORTS1_SORT_SERVORD)
				 sortSoFlag = !sortSoFlag;
			 else 
				 sortCustFlag = !sortCustFlag;
			 
			/* ServiceProConstants.showLog("Selected Sort Index : "+sortInd);
			 ServiceProConstants.showLog("Reports List Size before : "+reportsList.size());*/
			 Collections.sort(reportsList, reportSortComparator); 
			 //ServiceProConstants.showLog("Reports List Size after : "+reportsList.size());
					 
			 ReportOpConstraints repCategory = null;
             String workenddate = "", objectidStr = "", customerStr = "";
			 for(int k=0; k<reportsList.size(); k++){
				 repCategory = (ReportOpConstraints)reportsList.get(k);
                 if(repCategory != null){
                	 workenddate = repCategory.getReqStartDate().trim();    
                     objectidStr = repCategory.getObjectId().trim();
                     customerStr = repCategory.getSoldToPartyList();
                     //ServiceProConstants.showLog(workenddate+" : "+objectidStr+" : "+customerStr);
                 }
			 }
             initLayout();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in sortItemsAction : "+sfg.toString());
		}
	}//fn sortItemsAction
	
	
	private void showReportDetailScreen(int index){
		try {
			ReportOpConstraints repCategory = null;
            boolean errFlag = false;
            
			if(reportsList != null){
				if(reportsList.size() > index){
					repCategory = null;
					repCategory = (ReportOpConstraints)reportsList.get(index);
                    if(repCategory != null){
                    	//ServiceProConstants.showLog("SelIndex : "+index+" : "+repCategory.getObjectId());
                    	Intent intent = new Intent(this, ReportDetailScreen.class);
                    	intent.putExtra("report", repCategory);
            			startActivityForResult(intent,ServiceProConstants.REPORT1_DETAIL_SCREEN);
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
            	ServiceProConstants.showErrorDialog(this, getString(R.string.SERVICEPRO_RPMNSCR_ERR_DETCANT));
			
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showReportDetailScreen   
	
	
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
            C0[2].Cdata = "EVENT[.]SERVICE-REPORT-COMPLETED-ORDERS[.]VERSION[.]0";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
            doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
            //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL);
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
	    		updateReportsConfirmResponse(resultSoap);
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };
	
	/*private void getSOAPViaHTTP(SoapSerializationEnvelope envelopeCE, String url){		
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
                    updateReportsConfirmResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP	
*/	
	public void updateReportsConfirmResponse(SoapObject soap){		
        try{ 
        	if(soap != null){
	    		//ServiceProConstants.showLog("Count : "+soap.getPropertyCount());
	            ReportOpConstraints repCategory = null;
	            String delimeter = "[.]", result="", res="", docTypeStr = "";
	            SoapObject pii = null;
	            String[] resArray = new String[37];
	            int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;
	            if(reportsList != null)
	            	reportsList.clear();
	            
	            for (int i = 0; i < soap.getPropertyCount(); i++) {                
	                pii = (SoapObject)soap.getProperty(i);
	                propsCount = pii.getPropertyCount();
	                //ServiceProConstants.showLog("propsCount : "+propsCount);
	                if(propsCount > 0){
	                    for (int j = 0; j < propsCount; j++) {
	                        //ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
	                        if(j > 2){
	                            result = pii.getProperty(j).toString();
	                            firstIndex = result.indexOf(delimeter);
	                            eqIndex = result.indexOf("=");
	                            eqIndex = eqIndex+1;
	                            firstIndex = firstIndex + 3;
	                            docTypeStr = result.substring(eqIndex, (firstIndex-3));
	                            result = result.substring(firstIndex);
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
	                            //ServiceProConstants.showLog(resC+" : "+resArray[resC]);
	                            
	                            
	                            if(docTypeStr.equalsIgnoreCase("ZGSCSMST_SRVCRPRTDATA10")){
	                                if(repCategory != null)
	                                    repCategory = null;
	                                    
	                                repCategory = new ReportOpConstraints(resArray);
	                                if(reportsList != null)
	                                	reportsList.add(repCategory);
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
	            }
        	}
        	else{
        		initDBConnection();
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateTaskResponse : "+sff.toString());
        } 
        finally{
        	ServiceProConstants.showLog("Reports List Size : "+reportsList.size());
        	if((reportsList != null) && (reportsList.size() > 0)){
        		deleteAllReportDataFromDB();
        		//insert the data in to db
        		insertReportsDataIntoDB();
        	}
        	Collections.sort(reportsList, reportSortComparator);            
        	initLayout();
        }
    }//fn updateReportsConfirmResponse     
	
    
	private void insertReportsDataIntoDB(){
		ReportOpConstraints repCategory;
    	try {
			if(reportsList != null){
				for(int k=0; k<reportsList.size(); k++){
					repCategory = (ReportOpConstraints) reportsList.get(k);
					if(repCategory != null){
						insertReportDataInToDB(repCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertReportsDataIntoDB: "+ewe.toString());
		}
    }//fn insertReportsDataIntoDB
	
	
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
*/    
    public void onClose(){
    	try {
			System.gc();
			setResult(RESULT_OK); 
			this.finish();
		} catch (Exception e) {
		}
    }//fn onClose
	
    private final Comparator reportSortComparator =  new Comparator() {

        public int compare(Object o1, Object o2){ 
        	long listObj1 = 0, listObj2 = 0;
            int comp = 0;
            String strObj1 = "0", strObj2="0";
            ReportOpConstraints repOPObj1, repOPObj2;
            try{            	
                if (o1 == null || o2 == null){
                }
                else{            
                    repOPObj1 = (ReportOpConstraints)o1;
                    repOPObj2 = (ReportOpConstraints)o2;
                    //ServiceProConstants.showLog("Comparator called for "+sortIndex+" : "+sortSoFlag);
                    
                    if(sortIndex == ServiceProConstants.REPORTS1_SORT_DATE){
                    	String[] dateFormats = new String[1];
                    	dateFormats[0] = "yyyy-MM-dd";
                        if(repOPObj1 != null){
                            //strObj1 = repOPObj1.getWorkEndDate().trim();
                            strObj1 = repOPObj1.getReqStartDate().trim();
                            if(!strObj1.equalsIgnoreCase("")){  
                                listObj1 = DateUtils.parseDate(strObj1, dateFormats).getTime();
                            }
                        }
                        
                        if(repOPObj2 != null){
                            //strObj2 = repOPObj2.getWorkEndDate().trim();
                            strObj2 = repOPObj2.getReqStartDate().trim();
                            if(!strObj2.equalsIgnoreCase(""))
                                listObj2 = DateUtils.parseDate(strObj2, dateFormats).getTime();
                        }
                        //ServiceProConstants.showLog(listObj1+" : "+listObj2);
                        if(sortDateFlag == true)
                            comp = (int)(listObj1 - listObj2);
                        else
                            comp = (int)(listObj2 - listObj1);
                    }
                    else if(sortIndex == ServiceProConstants.REPORTS1_SORT_CUSTOMER){
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getSoldToPartyList().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getSoldToPartyList().trim();
                        
                        if(sortCustFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else{
                        // Code to sort by service order
                        if(repOPObj1 != null){
                            strObj1 = repOPObj1.getObjectId().trim();  
                            if(!strObj1.equalsIgnoreCase(""))
                                listObj1 = Long.parseLong(strObj1);
                        }
                        
                        if(repOPObj2 != null){
                            strObj2 = repOPObj2.getObjectId().trim();
                            if(!strObj2.equalsIgnoreCase(""))
                                listObj2 = Long.parseLong(strObj2);
                        }
                        
                        //ServiceProConstants.showLog(listObj1+" : "+listObj2);
                        
                        if(sortSoFlag == true)
                            comp = (int)(listObj1 - listObj2);
                        else
                            comp = (int)(listObj2 - listObj1);
                    }
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
    
	/*********************************************************************************************
     *     	Database Related Functions
     *********************************************************************************************/
    
    private ArrayList readAllReportDataFromDB(){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ReportOpConstraints repCategory = null;
    	ArrayList repArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[22];
    		String[] dbValues = new String[22];
    		int colId = -1;
    		
    		if(repArrList != null)
    			repArrList.clear();
    		
    		Uri uri = Uri.parse(ReportOfflineCP.CONTENT_URI+"");
			
			ContentResolver resolver = this.getContentResolver();
			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);
			
			ServiceProConstants.showLog("No of Report Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return repArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ReportOfflineDB.COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ReportOfflineDB.COL_PROCESSTYPE);
			dbIndex[2] = cursor.getColumnIndex(ReportOfflineDB.COL_PROCESS_TYPE_TXT);
			dbIndex[3] = cursor.getColumnIndex(ReportOfflineDB.COL_SOLD_TO_PARTY_LIST);
			dbIndex[4] = cursor.getColumnIndex(ReportOfflineDB.COL_SOLD_TO_PARTY);
			dbIndex[5] = cursor.getColumnIndex(ReportOfflineDB.COL_CONTACT_PERSON_LIST);
			dbIndex[6] = cursor.getColumnIndex(ReportOfflineDB.COL_NET_VALUE);
			dbIndex[7] = cursor.getColumnIndex(ReportOfflineDB.COL_CURRENCY);
			dbIndex[8] = cursor.getColumnIndex(ReportOfflineDB.COL_PRIORITY_TXT);
			dbIndex[9] = cursor.getColumnIndex(ReportOfflineDB.COL_DESCRIPTION);
			dbIndex[10] = cursor.getColumnIndex(ReportOfflineDB.COL_PO_DATE_SOLD);
			dbIndex[11] = cursor.getColumnIndex(ReportOfflineDB.COL_CREATED_BY);
			dbIndex[12] = cursor.getColumnIndex(ReportOfflineDB.COL_CONCATSTATUSER);
			dbIndex[13] = cursor.getColumnIndex(ReportOfflineDB.COL_POSTING_DATE);
			dbIndex[14] = cursor.getColumnIndex(ReportOfflineDB.COL_WRK_START_DATE);
			dbIndex[15] = cursor.getColumnIndex(ReportOfflineDB.COL_WRK_END_DATE);
			dbIndex[16] = cursor.getColumnIndex(ReportOfflineDB.COL_HRS_LABOR);
			dbIndex[17] = cursor.getColumnIndex(ReportOfflineDB.COL_HRS_TRAVEL);
			dbIndex[18] = cursor.getColumnIndex(ReportOfflineDB.COL_HRS_TOTAL);
			dbIndex[19] = cursor.getColumnIndex(ReportOfflineDB.COL_EQUIP_NO);
			dbIndex[20] = cursor.getColumnIndex(ReportOfflineDB.COL_REQ_START_DT);
			dbIndex[21] = cursor.getColumnIndex(ReportOfflineDB.COL_ID);
			
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[21]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = cursor.getString(dbIndex[7]);
				dbValues[8] = cursor.getString(dbIndex[8]);
				dbValues[9] = cursor.getString(dbIndex[9]);
				dbValues[10] = cursor.getString(dbIndex[10]);
				dbValues[11] = cursor.getString(dbIndex[11]);
				dbValues[12] = cursor.getString(dbIndex[12]);
				dbValues[13] = cursor.getString(dbIndex[13]);
				dbValues[14] = cursor.getString(dbIndex[14]);
				dbValues[15] = cursor.getString(dbIndex[15]);
				dbValues[16] = cursor.getString(dbIndex[16]);
				dbValues[17] = cursor.getString(dbIndex[17]);
				dbValues[18] = cursor.getString(dbIndex[18]);
				dbValues[19] = cursor.getString(dbIndex[19]);
				dbValues[20] = cursor.getString(dbIndex[20]);
				dbValues[21] = String.valueOf(colId);
				
				ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
								
				repCategory = new ReportOpConstraints(dbValues);
				if(repArrList != null)
					repArrList.add(repCategory);
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllReportDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return repArrList;
    }//fn readAllReportDataFromDB
    
    
    private void deleteAllReportDataFromDB(){
    	try{
    		Uri uri = Uri.parse(ReportOfflineCP.CONTENT_URI.toString());
			
			//Get the Resolver
			ContentResolver resolver = this.getContentResolver();
			
			//Make the invocation. # of rows deleted will be sent back
			int rows = resolver.delete(uri, null, null);
			
			ServiceProConstants.showLog("Rows Deleted : "+rows);
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllReportDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllReportDataFromDB
    
    
    
    private void insertReportDataInToDB(ReportOpConstraints repCategory){
    	try{
	    	ContentResolver resolver = this.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ReportOfflineDB.COL_OBJECT_ID, repCategory.getObjectId());
	    	val.put(ReportOfflineDB.COL_PROCESSTYPE, repCategory.getProcessType());
	    	val.put(ReportOfflineDB.COL_PROCESS_TYPE_TXT, repCategory.getProcessTypeText());
	    	val.put(ReportOfflineDB.COL_SOLD_TO_PARTY_LIST, repCategory.getSoldToPartyList());
	    	val.put(ReportOfflineDB.COL_SOLD_TO_PARTY, repCategory.getSoldToParty());
	    	val.put(ReportOfflineDB.COL_CONTACT_PERSON_LIST, repCategory.getContactPersonList());
	    	val.put(ReportOfflineDB.COL_NET_VALUE, repCategory.getNetValue());
	    	val.put(ReportOfflineDB.COL_CURRENCY, repCategory.getCurrency());
	    	val.put(ReportOfflineDB.COL_PRIORITY_TXT, repCategory.getPriorityText());
	    	val.put(ReportOfflineDB.COL_DESCRIPTION, repCategory.getDescription());
	    	val.put(ReportOfflineDB.COL_PO_DATE_SOLD, repCategory.getPODateSold());
	    	val.put(ReportOfflineDB.COL_CREATED_BY, repCategory.getCreatedBy());
	    	val.put(ReportOfflineDB.COL_CONCATSTATUSER, repCategory.getConcatStatUser());
	    	val.put(ReportOfflineDB.COL_POSTING_DATE, repCategory.getPostingDate());
	    	val.put(ReportOfflineDB.COL_WRK_START_DATE, repCategory.getWorkStartDate());
	    	val.put(ReportOfflineDB.COL_WRK_END_DATE, repCategory.getWorkEndDate());
	    	val.put(ReportOfflineDB.COL_HRS_LABOR, repCategory.getHoursLabor());
	    	val.put(ReportOfflineDB.COL_HRS_TRAVEL, repCategory.getHoursTravel());
	    	val.put(ReportOfflineDB.COL_HRS_TOTAL, repCategory.getHoursTotal());
	    	val.put(ReportOfflineDB.COL_EQUIP_NO, repCategory.getEquipNo());
	    	val.put(ReportOfflineDB.COL_REQ_START_DT, repCategory.getReqStartDate());	
	    	resolver.insert(ReportOfflineCP.CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertReportDataInToDB : "+sgh.toString());
    	}
    }//fn insertReportDataInToDB
    
    
    /*********************************************************************************************
     *          End of Database Related Functions
     *********************************************************************************************/

}//End of class ReportMainScreen
