package com.globalsoft.ServicePro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.globalsoft.ServicePro.Constraints.EntitlementOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Database.ServiceProOfflineContraintsCP;
import com.globalsoft.ServicePro.SoapConnection.HttpTransportSE;

public class ServiceProEntitlementScreen extends Activity {
	
	public TextView entServiceOrdTV, entProfileDescTV;
	private ArrayList entitlementsList = new ArrayList();
	private SoapObject resultSoap = null;
	private ProgressDialog pdialog = null;
	
	private boolean sortFlag = false, sortEntTypeFlag = false, sortLabourFlag = false, sortPartsFlag = false;
	private boolean sortTravelFlag = false, sortFrequencyFlag = false, sortDurationFlag = false;
    private int sortIndex = -1;
    private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5, headerWidth6;
    
    private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5, tableHeaderTV6;
    private int dispwidth = 300;
    
    private static final int ENTITLEMENT_SORT_ORDER_ENTTYPE = 1001;
    private static final int ENTITLEMENT_SORT_ORDER_LABOUR = 1002;
    private static final int ENTITLEMENT_SORT_ORDER_PARTS = 1003;
    private static final int ENTITLEMENT_SORT_ORDER_TRAVEL = 1004;
    private static final int ENTITLEMENT_SORT_ORDER_FREQUENCY = 1005;
    private static final int ENTITLEMENT_SORT_ORDER_DURATION = 1006;
    
    private String servOrdNoStr = "", processTypeStr = "", profileDescStr = "";
    final Handler handlerForLayout = new Handler();
	private boolean internetAccess = false;
    
	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
	private SoapObject requestSoapObj = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	//requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
        	/*this.setTitle("Entitlements for Service Order");
			setContentView(R.layout.entitlement);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/

			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.entitlement); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText("Entitlements for Service Order");

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			servOrdNoStr = (String)this.getIntent().getStringExtra("servOrdNoStr");	
			processTypeStr = (String)this.getIntent().getStringExtra("processTypeStr");	
			ServiceProConstants.showLog("Service Order : "+servOrdNoStr+" : "+processTypeStr);
			
			//initSoapConnection();	
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
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	final Runnable displayData = new Runnable(){
	    public void run()
	    {
	    	try{
	    		getEntitlementList();	
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }	    
    };
	
	private void initLayout(){
		try {        	
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			entServiceOrdTV = (TextView) findViewById(R.id.entServiceOrdTV);
			entProfileDescTV = (TextView) findViewById(R.id.entProfileDescTV);
			
			tableHeaderTV1 = (TextView)findViewById(R.id.TableHeaderTV1);
			tableHeaderTV1.setGravity(Gravity.LEFT);
			tableHeaderTV1.setPadding(10,5,5,5);
			tableHeaderTV1.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ENTITLEMENT_SORT_ORDER_ENTTYPE);
				}
			});
			
			tableHeaderTV2 = (TextView)findViewById(R.id.TableHeaderTV2);
			tableHeaderTV2.setGravity(Gravity.LEFT);
			tableHeaderTV2.setPadding(10,5,5,5);
			tableHeaderTV2.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ENTITLEMENT_SORT_ORDER_LABOUR);
				}
			});
			
			tableHeaderTV3 = (TextView)findViewById(R.id.TableHeaderTV3);
			tableHeaderTV3.setGravity(Gravity.LEFT);
			tableHeaderTV3.setPadding(10,5,5,5);
			tableHeaderTV3.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ENTITLEMENT_SORT_ORDER_PARTS);
				}
			});
			
			tableHeaderTV4 = (TextView)findViewById(R.id.TableHeaderTV4);
			tableHeaderTV4.setGravity(Gravity.LEFT);
			tableHeaderTV4.setPadding(10,5,5,5);
			tableHeaderTV4.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ENTITLEMENT_SORT_ORDER_TRAVEL);
				}
			});
			
			tableHeaderTV5 = (TextView)findViewById(R.id.TableHeaderTV5);
			tableHeaderTV5.setGravity(Gravity.LEFT);
			tableHeaderTV5.setPadding(10,5,5,5);
			tableHeaderTV5.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ENTITLEMENT_SORT_ORDER_FREQUENCY);
				}
			});
			
			tableHeaderTV6 = (TextView)findViewById(R.id.TableHeaderTV6);
			tableHeaderTV6.setGravity(Gravity.LEFT);
			tableHeaderTV6.setPadding(10,5,5,5);
			tableHeaderTV6.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ENTITLEMENT_SORT_ORDER_DURATION);
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
	                //drawSubLayout();	
	                updateHeaderElements();                
	            }
	        });	        
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	private void updateHeaderElements(){
		try{
			if(entServiceOrdTV != null){
				entServiceOrdTV.setText(entServiceOrdTV.getText().toString()+" "+servOrdNoStr);
			}
			
			if(entProfileDescTV != null){
				entProfileDescTV.setText(entProfileDescTV.getText().toString()+" "+profileDescStr);
			}
		}
		catch(Exception sfgg){
			ServiceProConstants.showErrorLog("Error in updateHeaderElements : "+sfgg.toString());
		}
	}//fn updateHeaderElements
    
    private void getEntitlementList(){
		try{					
			internetAccess = ServiceProConstants.checkInternetConn(ServiceProEntitlementScreen.this);
			if(internetAccess){
				initSoapConnection();
			}else{
				getLDBEntitlementList();
            	Collections.sort(entitlementsList, reportSortComparator);
				drawSubLayout();
			}			
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in getEntitlementList : "+sfg.toString());
		}
	}//fn getEntitlementList
	
	public void getLDBEntitlementList(){
		try {			  
			EntitlementOpConstraints repCategory = null;
			if(entitlementsList != null)
            	entitlementsList.clear();
			ArrayList entitlementsListObj = ServiceProDBOperations.readAllCollTasksDataFromDB(this, servOrdNoStr);		
						
			for(int i = 0; i < entitlementsListObj.size(); i++){  
				repCategory = ((EntitlementOpConstraints)entitlementsListObj.get(i));
		        if(repCategory != null){
		        	entitlementsList.add(repCategory);
		        }
		    }
			ServiceProConstants.showLog("entitlementsList size:"+entitlementsList.size());
			
		} catch (Exception esgg) {
			ServiceProConstants.showErrorLog("Error in getLDBEntitlementList :"+esgg.toString());
		}
	}//fn getLDBEntitlementList
	
	private void drawSubLayout(){
		try{
			TableLayout tl = (TableLayout)findViewById(R.id.entitlementtbllayout2);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			ServiceProConstants.showLog("Reports List Size after : "+entitlementsList.size());
			if(entitlementsList != null){
				EntitlementOpConstraints repCategory = null;
                String entTypeStr = "", labourStr = "", partsStr = "", travelStr = "", durationStr = "", frequencystr = "";
				for (int i =0; i < entitlementsList.size(); i++) {
					repCategory = (EntitlementOpConstraints)entitlementsList.get(i);
                    if(repCategory != null){
                    	entTypeStr = repCategory.getEntDesc().toLowerCase().trim(); 
                    	labourStr = repCategory.getDiscLabour().toString().trim();
                    	partsStr = repCategory.getDiscParts().toString().trim();
                    	travelStr = repCategory.getDiscTravel().toString().trim();  
                    	frequencystr = repCategory.getFrequency().toLowerCase().trim();
                    	durationStr = repCategory.getTimePeriod().toString().trim();
                    	profileDescStr = repCategory.getProfileDesc().toString().trim();
                    	/*
                    	if(!durationStr.equalsIgnoreCase(""))
                    		durationStr = ServiceProConstants.getSystemDateFormatString(this, durationStr);
                    	*/
                        tr = new TableRow(this);
                    	
    					TextView entTypeTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					entTypeTxtView.setText(entTypeStr);
    					entTypeTxtView.setWidth(headerWidth1);
    					entTypeTxtView.setGravity(Gravity.LEFT);
    					entTypeTxtView.setPadding(10,0,0,0);
    					entTypeTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					TextView labourTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);   					
    					labourTxtView.setText(labourStr);
    					labourTxtView.setWidth(headerWidth2);
    					labourTxtView.setGravity(Gravity.LEFT);
    					labourTxtView.setPadding(10,0,0,0);    
    					labourTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					TextView partsTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);	
    					partsTxtView.setText(partsStr);
    					partsTxtView.setWidth(headerWidth3);
    					partsTxtView.setGravity(Gravity.LEFT);
    					partsTxtView.setPadding(10,0,0,0);		
    					partsTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					TextView travelTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);	
    					travelTxtView.setText(travelStr);
    					travelTxtView.setWidth(headerWidth4);
    					travelTxtView.setGravity(Gravity.LEFT);
    					travelTxtView.setPadding(10,0,0,0);	
    					travelTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					TextView frequencyTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);	
    					frequencyTxtView.setText(frequencystr);
    					frequencyTxtView.setWidth(headerWidth5);
    					frequencyTxtView.setGravity(Gravity.LEFT);
    					frequencyTxtView.setPadding(10,0,0,0);	
    					frequencyTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					TextView durationTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					durationTxtView.setText(durationStr);
    					durationTxtView.setWidth(headerWidth6);
    					durationTxtView.setGravity(Gravity.LEFT);
    					durationTxtView.setPadding(10,0,0,0);
    					durationTxtView.setBackgroundResource(R.drawable.border_dashed);
    					
    					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
    						entTypeTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						labourTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						partsTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						travelTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						frequencyTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						durationTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    					}

    					tr.addView(entTypeTxtView);
    					tr.addView(frequencyTxtView);
    					tr.addView(durationTxtView);
    					tr.addView(labourTxtView);
    					tr.addView(partsTxtView);
    					tr.addView(travelTxtView);    					

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
			 if(sortInd == ENTITLEMENT_SORT_ORDER_ENTTYPE)
				 sortEntTypeFlag = !sortEntTypeFlag;
			 else if(sortInd == ENTITLEMENT_SORT_ORDER_LABOUR)
				 sortLabourFlag = !sortLabourFlag;
			 else if(sortInd == ENTITLEMENT_SORT_ORDER_PARTS)
				 sortPartsFlag = !sortPartsFlag;
			 else if(sortInd == ENTITLEMENT_SORT_ORDER_TRAVEL)
				 sortTravelFlag = !sortTravelFlag;
			 else if(sortInd == ENTITLEMENT_SORT_ORDER_DURATION)
				 sortDurationFlag = !sortDurationFlag;
			 else 
				 sortFrequencyFlag = !sortFrequencyFlag;
			 
			 ServiceProConstants.showLog("Selected Sort Index : "+sortInd);
			 ServiceProConstants.showLog("Entitlements List Size : "+entitlementsList.size());
			 Collections.sort(entitlementsList, reportSortComparator); 
			
             initLayout();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in sortItemsAction : "+sfg.toString());
		}
	}//fn sortItemsAction
	
	
	private void initSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
        	if((!servOrdNoStr.equalsIgnoreCase("")) && (!processTypeStr.equalsIgnoreCase(""))){
	            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
	            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            
	            ServiceProInputConstraints C0[];
	            C0 = new ServiceProInputConstraints[5];
	            
	            for(int i=0; i<C0.length; i++){
	                C0[i] = new ServiceProInputConstraints(); 
	            }
	            
	            
	            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
	            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
	            C0[2].Cdata = "EVENT[.]SERVICE-DOC-ENTITLEMENT-GET[.]VERSION[.]0";
	            C0[3].Cdata = "DATA-TYPE[.]ZGSCSMST_ORDENT_INPUT[.]ORDER_ID[.]PROCESS_TYPE";
	            C0[4].Cdata = "ZGSCSMST_ORDENT_INPUT[.]"+servOrdNoStr+"[.]"+processTypeStr;
	        
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
	    			updateReportsConfirmResponse(resultSoap);
	    		}else{
                	getLDBEntitlementList();
                	Collections.sort(entitlementsList, reportSortComparator);
        			drawSubLayout();
	    		}
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
		boolean errorflag = false;
        try{ 
        	if(soap != null){
	    		ServiceProConstants.showLog("Count : "+soap.getPropertyCount());
	            EntitlementOpConstraints repCategory = null;
	            String delimeter = "[.]", result="", res="", docTypeStr = "";
	            SoapObject pii = null;
	            String[] resArray = new String[20];
	            int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;
	            if(entitlementsList != null)
	            	entitlementsList.clear();
	            
	            for (int i = 0; i < soap.getPropertyCount(); i++) {                
	                pii = (SoapObject)soap.getProperty(i);
	                propsCount = pii.getPropertyCount();
	                ServiceProConstants.showLog("propsCount : "+propsCount);
	                if(propsCount > 0){
	                    for (int j = 0; j < propsCount; j++) {
	                        ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
	                        if(j > 2){
	                            result = pii.getProperty(j).toString();
	                            firstIndex = result.indexOf(delimeter);
	                            eqIndex = result.indexOf("=");
	                            eqIndex = eqIndex+1;
	                            firstIndex = firstIndex + 3;
	                            docTypeStr = result.substring(eqIndex, (firstIndex-3));
	                            result = result.substring(firstIndex);
	                            //ServiceProConstants.showLog("Document Type : "+docTypeStr);
	                            ServiceProConstants.showLog("Result : "+result);
	                            
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
	                            
	                            
	                            if(docTypeStr.equalsIgnoreCase("ZGSCSMST_ORDENT_OUTPUT")){
	                                if(repCategory != null)
	                                    repCategory = null;
	                                    
	                                repCategory = new EntitlementOpConstraints(resArray);
	                                if(entitlementsList != null)
	                                	entitlementsList.add(repCategory);
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
        	}//if
        	else{
        		errorflag = true;
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateReportsConfirmResponse : "+sff.toString());
        } 
        finally{
        	if(!errorflag){
            	ServiceProConstants.showLog("Entitlements List Size : "+entitlementsList.size());
            	if((entitlementsList != null) && (entitlementsList.size() > 0)){
	        		ServiceProDBOperations.deleteAllEntitlementDocsCategoryDataFromDB(this, ServiceProOfflineContraintsCP.SERPRO_ENTITLEMENTLIST_CONTENT_URI, servOrdNoStr);
					insertEntitlementDataIntoDB();
	        	}       
            	Collections.sort(entitlementsList, reportSortComparator);            
            	drawSubLayout();
        	}
        	else{
            	getLDBEntitlementList();
            	Collections.sort(entitlementsList, reportSortComparator);
    			drawSubLayout();
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
            EntitlementOpConstraints repOPObj1, repOPObj2;
            String[] dateFormats = new String[1];
        	//dateFormats[0] = "yyyy-MM-dd";
        	dateFormats[0] = "yyyy-MM-dd HH:mm:ss";
            try{            	
                if (o1 == null || o2 == null){
                }
                else{            
                    repOPObj1 = (EntitlementOpConstraints)o1;
                    repOPObj2 = (EntitlementOpConstraints)o2;
                    ServiceProConstants.showLog("Comparator called for "+sortIndex+" : "+sortEntTypeFlag);
                    
                    if(sortIndex == ENTITLEMENT_SORT_ORDER_ENTTYPE){
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getEntDesc().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getEntDesc().trim();
                        
                        if(sortEntTypeFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == ENTITLEMENT_SORT_ORDER_LABOUR){
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getDiscLabour().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getDiscLabour().trim();
                        
                        if(sortLabourFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }                    
                    else if(sortIndex == ENTITLEMENT_SORT_ORDER_PARTS){
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getDiscParts().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getDiscParts().trim();
                        
                        if(sortPartsFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == ENTITLEMENT_SORT_ORDER_TRAVEL){
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getDiscTravel().toString().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getDiscTravel().toString().trim();
                        
                        if(sortTravelFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == ENTITLEMENT_SORT_ORDER_FREQUENCY){
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getFrequency().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getFrequency().trim();
                        
                        if(sortFrequencyFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == ENTITLEMENT_SORT_ORDER_DURATION){
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getTimePeriod().toString().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getTimePeriod().toString().trim();
                        
                        if(sortDurationFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else {
                    	if(repOPObj1 != null)
                            strObj1 = repOPObj1.getEntDesc().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getEntDesc().trim();
                        
                        if(sortEntTypeFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
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
	
	private void insertEntitlementDataIntoDB(){
		EntitlementOpConstraints repCategory;
    	try {
			if(entitlementsList != null){
				for(int k=0; k<entitlementsList.size(); k++){
					repCategory = (EntitlementOpConstraints) entitlementsList.get(k);
					if(repCategory != null){
						ServiceProDBOperations.insertEntitlementListDataInToDB(this, repCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertEntitlementDataIntoDB: "+ewe.toString());
		}
    }//fn insertEntitlementDataIntoDB
	
}//End of class ServiceProEntitlementScreen