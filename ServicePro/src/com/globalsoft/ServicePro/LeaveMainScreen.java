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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.LeaveTypeOpConstraints;
import com.globalsoft.ServicePro.Constraints.LeavesOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.SoapConnection.HttpTransportSE;

public class LeaveMainScreen extends Activity {
	
	private ArrayList leavesList = new ArrayList();
	private ArrayList leaveTypesList = new ArrayList();
	private SoapObject resultSoap = null;
	private ProgressDialog pdialog = null;
	
	private boolean sortFlag = false, sortFromFlag = false, sortToFlag = false, sortStatusFlag = false;
	private boolean sortTypeFlag = false, sortSubmittedFlag = false, sortLeaveNoFlag = false;
    private int sortIndex = -1;
    private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5, headerWidth6, headerWidth7;
    
    private ImageButton[] deleteBMF;
    private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5, tableHeaderTV6, tableHeaderTV7;
    private int dispwidth = 300;
    
    private static final int MAIN_ID = Menu.FIRST;
    
    private static final int LEAVE_SORT_ORDER_FROM = 1001;
    private static final int LEAVE_SORT_ORDER_TO = 1002;
    private static final int LEAVE_SORT_ORDER_STATUS = 1003;
    private static final int LEAVE_SORT_ORDER_TYPE = 1004;
    private static final int LEAVE_SORT_ORDER_SUBMIT = 1005;
    private static final int LEAVE_SORT_ORDER_LEAVENO = 1006;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	//requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
        	/*this.setTitle("Status of Absences requested");
			setContentView(R.layout.leavemain);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.leavemain); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText("Status of Absences requested");

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			initSoapConnection();	
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	private void initLayout(){
		try {        	
			setContentView(R.layout.leavemain);
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			tableHeaderTV7 = (TextView)findViewById(R.id.TableHeaderTV7);
			tableHeaderTV7.setGravity(Gravity.LEFT);
			tableHeaderTV7.setPadding(10,5,5,5);
			
			tableHeaderTV1 = (TextView)findViewById(R.id.TableHeaderTV1);
			tableHeaderTV1.setGravity(Gravity.LEFT);
			tableHeaderTV1.setPadding(10,5,5,5);
			tableHeaderTV1.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(LEAVE_SORT_ORDER_FROM);
				}
			});
			
			tableHeaderTV2 = (TextView)findViewById(R.id.TableHeaderTV2);
			tableHeaderTV2.setGravity(Gravity.LEFT);
			tableHeaderTV2.setPadding(10,5,5,5);
			tableHeaderTV2.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(LEAVE_SORT_ORDER_TO);
				}
			});
			
			tableHeaderTV3 = (TextView)findViewById(R.id.TableHeaderTV3);
			tableHeaderTV3.setGravity(Gravity.LEFT);
			tableHeaderTV3.setPadding(10,5,5,5);
			tableHeaderTV3.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(LEAVE_SORT_ORDER_TYPE);
				}
			});
			
			tableHeaderTV4 = (TextView)findViewById(R.id.TableHeaderTV4);
			tableHeaderTV4.setGravity(Gravity.LEFT);
			tableHeaderTV4.setPadding(10,5,5,5);
			tableHeaderTV4.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(LEAVE_SORT_ORDER_STATUS);
				}
			});
			
			tableHeaderTV5 = (TextView)findViewById(R.id.TableHeaderTV5);
			tableHeaderTV5.setGravity(Gravity.LEFT);
			tableHeaderTV5.setPadding(10,5,5,5);
			tableHeaderTV5.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(LEAVE_SORT_ORDER_SUBMIT);
				}
			});
			
			tableHeaderTV6 = (TextView)findViewById(R.id.TableHeaderTV6);
			tableHeaderTV6.setGravity(Gravity.LEFT);
			tableHeaderTV6.setPadding(10,5,5,5);
			tableHeaderTV6.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(LEAVE_SORT_ORDER_LEAVENO);
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
	            }
	        });
			
	        ViewTreeObserver vto4 = tableHeaderTV4.getViewTreeObserver();
	        vto4.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV4.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth4 = tableHeaderTV4.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV4 Width1 : "+headerWidth4+" : "+tableHeaderTV4.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto5 = tableHeaderTV5.getViewTreeObserver();
	        vto5.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV5.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth5 = tableHeaderTV5.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV5 Width1 : "+headerWidth5+" : "+tableHeaderTV5.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto6 = tableHeaderTV6.getViewTreeObserver();
	        vto6.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV6.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth6 = tableHeaderTV6.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV6 Width1 : "+headerWidth6+" : "+tableHeaderTV6.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto7 = tableHeaderTV7.getViewTreeObserver();
	        vto7.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV7.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth7 = tableHeaderTV7.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV7 Width1 : "+headerWidth7+" : "+tableHeaderTV7.getMeasuredWidth());
	                drawSubLayout();	                
	            }
	        });
	        
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	
	private void drawSubLayout(){
		try{
			TableLayout tl = (TableLayout)findViewById(R.id.leavemaintbllayout2);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			if(leavesList != null){
				ServiceProConstants.showLog("Reports List Size after : "+leavesList.size());
				LeavesOpConstraints repCategory = null;
                String fromDateStr = "", toDateStr = "", statusStr = "", typeStr = "", submtDateStr = "";
                String fromTimeStr = "", toTimeStr = "", submtTimeStr = "", leaveNoStr = "";
                
                int rowSize = leavesList.size();      
                deleteBMF = new ImageButton[rowSize];
                
                TableRow.LayoutParams linparams12 = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				linparams12.width = headerWidth7;
				/*linparams12.topMargin = 5;
				linparams12.bottomMargin = 5;*/
				
				for (int i =0; i < rowSize; i++) {
					repCategory = (LeavesOpConstraints)leavesList.get(i);
                    if(repCategory != null){
                    	fromDateStr = repCategory.getBeginDate().toLowerCase().trim(); 
                    	fromTimeStr = repCategory.getBeginTime().toString().trim();
                    	if(!fromDateStr.equalsIgnoreCase(""))
                    		if(fromTimeStr.equalsIgnoreCase("00:00:00"))
                    			fromDateStr = ServiceProConstants.getSystemDateFormatString(this, fromDateStr);
                    		else
                    			fromDateStr = ServiceProConstants.getSystemDateFormatString(this, fromDateStr)+" "+ServiceProConstants.getSecondsRemovedTimeStr(fromTimeStr);
                    	
                    	toDateStr = repCategory.getEndDate().toString().trim();
                    	toTimeStr = repCategory.getEndTime().toString().trim();
                    	if(!toDateStr.equalsIgnoreCase(""))
                    		if(toTimeStr.equalsIgnoreCase("00:00:00"))
                    			toDateStr = ServiceProConstants.getSystemDateFormatString(this, toDateStr);
                    		else
                    			toDateStr = ServiceProConstants.getSystemDateFormatString(this, toDateStr)+" "+ServiceProConstants.getSecondsRemovedTimeStr(toTimeStr);
                    		
                    	
                    	statusStr = repCategory.getApprovalStatus().toString().trim();
                    	typeStr = repCategory.getLeaveType().toString().trim();
                    	
                    	submtDateStr = repCategory.getAppliedDate().toString().trim();
                    	submtTimeStr = repCategory.getAppliedTime().toString().trim();
                    	if(!submtDateStr.equalsIgnoreCase(""))
                    		if(submtTimeStr.equalsIgnoreCase("00:00:00"))
                    			submtDateStr = ServiceProConstants.getSystemDateFormatString(this, submtDateStr);
                    		else
                    			submtDateStr = ServiceProConstants.getSystemDateFormatString(this, submtDateStr)+" "+ServiceProConstants.getSecondsRemovedTimeStr(submtTimeStr);                    		
                    	
                    	leaveNoStr = repCategory.getLeaveRequestNo().toLowerCase().trim();
                    	
                        tr = new TableRow(this);
                        
						deleteBMF[i] = new ImageButton(this); 
						deleteBMF[i].setId(i);
						deleteBMF[i].setBackgroundResource(R.drawable.delete);
						//deleteBMF[i].setPadding(10, 5, 10, 5);
						deleteBMF[i].setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								int id = view.getId();	
								clickDeleteAction(id);
							}	
                        });
						
						LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linear_short, null);
        				linearLayout.setLayoutParams(linparams12); 
        				linearLayout.setPadding(0, 10, 0, 10);
        				linearLayout.setBackgroundResource(R.drawable.border_dashed_light);
                        linearLayout.addView(deleteBMF[i]);
                        
    					TextView fromDateTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					fromDateTxtView.setText(fromDateStr);
    					fromDateTxtView.setWidth(headerWidth1);
    					fromDateTxtView.setGravity(Gravity.LEFT);
    					fromDateTxtView.setPadding(10,7,0,0);
    					fromDateTxtView.setBackgroundResource(R.drawable.border_dashed_light);
    					
    					TextView toDateTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);   					
    					toDateTxtView.setText(toDateStr);
    					toDateTxtView.setWidth(headerWidth2);
    					toDateTxtView.setGravity(Gravity.LEFT);
    					toDateTxtView.setPadding(10,0,0,0);
    					toDateTxtView.setBackgroundResource(R.drawable.border_dashed_light);
    					
    					TextView typeTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);	
    					typeTxtView.setText(typeStr);
    					typeTxtView.setWidth(headerWidth3);
    					typeTxtView.setGravity(Gravity.LEFT);
    					typeTxtView.setPadding(10,0,0,0);	
    					typeTxtView.setBackgroundResource(R.drawable.border_dashed_light);
    					
    					TextView statusTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);	
    					statusTxtView.setText(statusStr);
    					statusTxtView.setWidth(headerWidth4);
    					statusTxtView.setGravity(Gravity.LEFT);
    					statusTxtView.setPadding(10,0,0,0);	
    					statusTxtView.setBackgroundResource(R.drawable.border_dashed_light);
    					
    					TextView submtDateTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					submtDateTxtView.setText(submtDateStr);
    					submtDateTxtView.setWidth(headerWidth5);
    					submtDateTxtView.setGravity(Gravity.LEFT);
    					submtDateTxtView.setPadding(10,0,0,0);
    					submtDateTxtView.setBackgroundResource(R.drawable.border_dashed_light);
    					
    					TextView leaveNoTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);	
    					leaveNoTxtView.setText(leaveNoStr);
    					leaveNoTxtView.setWidth(headerWidth6);
    					leaveNoTxtView.setGravity(Gravity.LEFT);
    					leaveNoTxtView.setPadding(10,0,0,0);
    					leaveNoTxtView.setBackgroundResource(R.drawable.border_dashed_light);
    					
    					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
    						fromDateTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						toDateTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						typeTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						statusTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						submtDateTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						leaveNoTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    					}
                        
    					tr.addView(linearLayout);
    					tr.addView(fromDateTxtView);
    					tr.addView(toDateTxtView);
    					tr.addView(typeTxtView);
    					tr.addView(statusTxtView);
    					tr.addView(submtDateTxtView);
    					tr.addView(leaveNoTxtView);
    					
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
		
	private void sortItemsAction(int sortInd){
		try{
			 sortFlag = true;
			 sortIndex = sortInd;
			 if(sortInd == LEAVE_SORT_ORDER_FROM)
				 sortFromFlag = !sortFromFlag;
			 else if(sortInd == LEAVE_SORT_ORDER_TO)
				 sortToFlag = !sortToFlag;
			 else if(sortInd == LEAVE_SORT_ORDER_STATUS)
				 sortStatusFlag = !sortStatusFlag;
			 else if(sortInd == LEAVE_SORT_ORDER_TYPE)
				 sortTypeFlag = !sortTypeFlag;
			 else if(sortInd == LEAVE_SORT_ORDER_LEAVENO)
				 sortLeaveNoFlag = !sortLeaveNoFlag;
			 else 
				 sortSubmittedFlag = !sortSubmittedFlag;
			 
			 ServiceProConstants.showLog("Selected Sort Index : "+sortInd);
			 ServiceProConstants.showLog("Reports List Size before : "+leavesList.size());
			 Collections.sort(leavesList, reportSortComparator); 
			
             initLayout();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in sortItemsAction : "+sfg.toString());
		}
	}//fn sortItemsAction
	
	
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
            C0[2].Cdata = "EVENT[.]EMPLOYEE-LEAVE-HISTORY[.]VERSION[.]0";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
          
            startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL, true);
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in initSoapConnection : "+asd.toString());
        }
    }//fn initSoapConnection
	
	
	private void initDeleteSoapConnection(String leaveIdStr){        
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
            C0[2].Cdata = "EVENT[.]EMPLOYEE-LEAVE-CANCELLATION[.]VERSION[.]0";
            C0[3].Cdata = "DATA-TYPE[.]ZGSCCAST_EMPLYLEAVECNCLLTNDATA[.]LEAVE_REQ_NUM[.]";
            C0[4].Cdata = "ZGSCCAST_EMPLYLEAVECNCLLTNDATA[.]"+leaveIdStr+"[.]";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
          
            startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL, false);
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in initDeleteSoapConnection : "+asd.toString());
        }
    }//fn initDeleteSoapConnection
	
	
	private void getSOAPViaHTTP(SoapSerializationEnvelope envelopeCE, String url, final boolean leaveListFlag){		
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
                	if(leaveListFlag == true)
                		updateReportsConfirmResponse(resultSoap);
                	else
                		updateLeaveCancelResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP	
	
	public void updateLeaveCancelResponse(SoapObject soap){
		boolean diagdispFlag = false;
		boolean resMsgErr = false;
		String taskErrorMsgStr = "";
		try{ 
        	if(soap != null){  
        		String delimeter = "[.]", result="", res="";
                SoapObject pii = null;
                String[] resArray = new String[37];
                int propsCount = 0, errCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0; 
                String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg); 
                for (int i = 0; i < soap.getPropertyCount(); i++) {                
                    pii = (SoapObject)soap.getProperty(i);
                    propsCount = pii.getPropertyCount();
                    //ServiceProConstants.showLog("propsCount : "+propsCount);
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                        	//ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
                            if(j >= 4){
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
                                    //SapTasksConstants.Log(resC+" : "+res);
                                    resArray[resC] = res;
                                    indexA = indexB + delimeter.length();
                                    indexB = result.indexOf(delimeter, indexA);
                                    resC++;
                                }
                                int endIndex = result.lastIndexOf(';');
                                resArray[resC] = result.substring(indexA,endIndex);
                                //SapTasksConstants.Log(resC+" : "+resArray[resC]);
                            }
                            else if(j < 3){
                                String errorMsg = pii.getProperty(j).toString();
                                //ServiceProConstants.showLog("Inside J == "+j+" : "+propsCount+" : "+errorMsg);
                                int errorFstIndex = errorMsg.indexOf("Message=");
                                if(errorFstIndex > 0){
                                    if((errorMsg.indexOf("Type=A") > 0) || (errorMsg.indexOf("Type=E") > 0) || (errorMsg.indexOf("Type=S") > 0)){
                                        int errorLstIndex = errorMsg.indexOf(";", errorFstIndex);
                                        if(taskErrorMsgStr.equalsIgnoreCase(""))
                                            taskErrorMsgStr = errorMsg.substring((errorFstIndex+"Message=".length()), errorLstIndex).trim();
                                        
                                        ServiceProConstants.showLog("taskErrorMsgStr : "+taskErrorMsgStr);
                                    }
                                    
                                    errCount = propsCount-2;
                                                                            
                                    ServiceProConstants.showErrorLog(taskErrorMsgStr);
                                    
                                    if(errCount < 0)
                                        errCount = 0;
                                    
                                    if(j == errCount){
                                    	ServiceProConstants.showErrorLog("Inside Errcount : "+taskErrorMsgStr);
                                    	ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                                        diagdispFlag = true;
                                    }
                                }
                            }
                        }
                    }
                }
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateLeaveCancelResponse : "+sff.toString());
        } 
        finally{
        	try{
        		ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
            	if(!resMsgErr){
            		if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                	
                	initSoapConnection();
            	}else{
            		onClose();
            	}
                /*if(taskErrorMsgStr.equalsIgnoreCase("")){
                    onClose();
                }
                else if((taskErrorMsgStr.indexOf("Cancel") > 0) && (taskErrorMsgStr.indexOf("created") > 0)){
                	if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                	
                	initSoapConnection();
                }*/
            }
            catch(Exception asf){}
        }
	}//fn updateLeaveCancelResponse
	
	public void updateReportsConfirmResponse(SoapObject soap){
		boolean resMsgErr = false;
        try{ 
        	if(soap != null){
	    		//ServiceProConstants.showLog("Count : "+soap.getPropertyCount());
	            LeavesOpConstraints repCategory = null;
	            LeaveTypeOpConstraints leaveCategory = null;
	            String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg); 
	            String delimeter = "[.]", result="", res="", docTypeStr = "";
	            SoapObject pii = null;
	            String[] resArray = new String[20];
	            int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;
	            if(leavesList != null)
	            	leavesList.clear();
	            
	            if(leaveTypesList != null)
	            	leaveTypesList.clear();
	            
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
	                            /*ServiceProConstants.showLog("Document Type : "+docTypeStr);
	                            ServiceProConstants.showLog("Result : "+result);*/
	                            
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
	                            
	                            
	                            if(docTypeStr.equalsIgnoreCase("ZGSCCAST_LEAVE_DETAILS_OUTPUT")){
	                                if(repCategory != null)
	                                    repCategory = null;
	                                    
	                                repCategory = new LeavesOpConstraints(resArray);
	                                if(leavesList != null)
	                                	leavesList.add(repCategory);
	                                
	                                //ServiceProConstants.showLog("Result : "+result);
	                            }
	                            else if(docTypeStr.equalsIgnoreCase("ZGSCSMDVLEAVETYP")){
	                            	if(leaveCategory != null)
	                            		leaveCategory = null;
		                                    
	                            	leaveCategory = new LeaveTypeOpConstraints(resArray);
	                                if(leaveTypesList != null)
	                                	leaveTypesList.add(leaveCategory);
	                            	 
	                            	//ServiceProConstants.showLog("Result : "+result);
	                            }
	                        }
	                        else if(j == 0){
	                            String errorMsg = pii.getProperty(j).toString();
	                            ServiceProConstants.showLog("Inside J == 0 ");
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
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateTaskResponse : "+sff.toString());
        } 
        finally{
        	ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
        	ServiceProConstants.showLog("Leaves List Size : "+leavesList.size()+" : "+leaveTypesList.size());
        	Collections.sort(leavesList, reportSortComparator);            
        	initLayout();
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
    
    
    private void startNetworkConnection(final Context ctxAct, final SoapSerializationEnvelope envelopeCE, final String url, final boolean leaveListFlag){
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
                    				getSOAPViaHTTP(envelopeCE, url, leaveListFlag);
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
    
    private void deleteItemAction(int selId){
		try{
			if(leavesList != null){
				if(leavesList.size() > selId){
					LeavesOpConstraints leaveCategory = null;
					leaveCategory = (LeavesOpConstraints) leavesList.get(selId);
					if(leaveCategory != null){
						String leaveId = leaveCategory.getLeaveRequestNo().trim();
						if(!leaveId.equalsIgnoreCase("")){
							initDeleteSoapConnection(leaveId);
							//ServiceProConstants.showLog("leaveId : "+leaveId);
						}
						else
							ServiceProConstants.showErrorDialog(this, "Leave Id is empty");
					}
				}
			}
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("On deleteItemAction : "+sfg.toString());
		}
	}//fn deleteItemAction
    
    private void clickDeleteAction(final int position){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this Item?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	deleteItemAction(position);
            }
        });                
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }//fn clickDeleteAction
    
    public void onClose(){
    	try {
			System.gc();
			setResult(RESULT_OK); 
			this.finish();
		} catch (Exception e) {
		}
    }//fn onClose
    
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        menu.add(0, MAIN_ID, 0, "Request Absence");
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
			case MAIN_ID: {
				try{
					Intent intent = new Intent(LeaveMainScreen.this, LeaveApplyScreen.class);	
					intent.putExtra("leaveTypesList", leaveTypesList);
					startActivityForResult(intent, ServiceProConstants.LEAVE_APPLY_SCREEN);
		        }
				catch (Exception de) {
			    	ServiceProConstants.showErrorLog("Error in Menu : "+de.toString());
			    }
		        break;
			}
	    }
		return super.onOptionsItemSelected(item);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	try {   			
    		/*ServiceProConstants.showLog("Request Code "+requestCode);
    		ServiceProConstants.showLog("Result Code "+resultCode);
    		ServiceProConstants.showLog("Result Code "+RESULT_OK);*/
    		
    		if(requestCode == ServiceProConstants.LEAVE_APPLY_SCREEN){
    			if(resultCode == RESULT_OK)
    				initSoapConnection();
    		}
			//finish();
		} catch (Exception e) {
			ServiceProConstants.showErrorLog(e.toString());
		}
    }//fn onActivityResult
		
    private final Comparator reportSortComparator =  new Comparator() {

        public int compare(Object o1, Object o2){ 
        	long listObj1 = 0, listObj2 = 0;
            int comp = 0;
            String strObj1 = "0", strObj2="0";
            LeavesOpConstraints repOPObj1, repOPObj2;
            String[] dateFormats = new String[1];
        	//dateFormats[0] = "yyyy-MM-dd";
        	dateFormats[0] = "yyyy-MM-dd HH:mm:ss";
            try{            	
                if (o1 == null || o2 == null){
                }
                else{            
                    repOPObj1 = (LeavesOpConstraints)o1;
                    repOPObj2 = (LeavesOpConstraints)o2;
                   // ServiceProConstants.showLog("Comparator called for "+sortIndex+" : "+sortFromFlag);
                    
                    if(sortIndex == LEAVE_SORT_ORDER_FROM){
                        if(repOPObj1 != null){
                            strObj1 = repOPObj1.getBeginDate().toString().trim()+" "+repOPObj1.getBeginTime().toString().trim();
                            if(!strObj1.equalsIgnoreCase("")){  
                                listObj1 = DateUtils.parseDate(strObj1, dateFormats).getTime();
                            }
                        }
                        
                        if(repOPObj2 != null){
                            strObj2 = repOPObj2.getBeginDate().toString().trim()+" "+repOPObj2.getBeginTime().toString().trim();
                            if(!strObj2.equalsIgnoreCase(""))
                                listObj2 = DateUtils.parseDate(strObj2, dateFormats).getTime();
                        }
                        //ServiceProConstants.showLog(listObj1+" : "+listObj2);
                        if(sortFromFlag == true)
                            comp = (int)(listObj1 - listObj2);
                        else
                            comp = (int)(listObj2 - listObj1);
                    }
                    else if(sortIndex == LEAVE_SORT_ORDER_TO){
                        if(repOPObj1 != null){
                            strObj1 = repOPObj1.getEndDate().toString().trim()+" "+repOPObj1.getEndTime().toString().trim();
                            if(!strObj1.equalsIgnoreCase("")){  
                                listObj1 = DateUtils.parseDate(strObj1, dateFormats).getTime();
                            }
                        }
                        
                        if(repOPObj2 != null){
                            strObj2 = repOPObj2.getEndDate().toString().trim()+" "+repOPObj2.getEndTime().toString().trim();
                            if(!strObj2.equalsIgnoreCase(""))
                                listObj2 = DateUtils.parseDate(strObj2, dateFormats).getTime();
                        }
                        //ServiceProConstants.showLog(listObj1+" : "+listObj2);
                        if(sortToFlag == true)
                            comp = (int)(listObj1 - listObj2);
                        else
                            comp = (int)(listObj2 - listObj1);
                    }                    
                    else if(sortIndex == LEAVE_SORT_ORDER_STATUS){
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getApprovalStatus().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getApprovalStatus().trim();
                        
                        if(sortStatusFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == LEAVE_SORT_ORDER_TYPE){
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getLeaveType().toString().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getLeaveType().toString().trim();
                        
                        if(sortTypeFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == LEAVE_SORT_ORDER_LEAVENO){
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getLeaveRequestNo().toString().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getLeaveRequestNo().toString().trim();
                        
                        if(sortLeaveNoFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else{
                    	if(repOPObj1 != null){
                            strObj1 = repOPObj1.getAppliedDate().toString().trim()+" "+repOPObj1.getAppliedTime().toString().trim();
                            if(!strObj1.equalsIgnoreCase("")){  
                                listObj1 = DateUtils.parseDate(strObj1, dateFormats).getTime();
                            }
                        }
                        
                        if(repOPObj2 != null){
                            strObj2 = repOPObj2.getAppliedDate().toString().trim()+" "+repOPObj2.getAppliedTime().toString().trim();
                            if(!strObj2.equalsIgnoreCase(""))
                                listObj2 = DateUtils.parseDate(strObj2, dateFormats).getTime();
                        }
                        ServiceProConstants.showLog(listObj1+" : "+listObj2);
                        if(sortSubmittedFlag == true)
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

}//End of class LeaveMainScreen
