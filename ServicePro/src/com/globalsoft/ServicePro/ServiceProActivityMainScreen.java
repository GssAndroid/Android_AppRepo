package com.globalsoft.ServicePro;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.SapQueueProcessorHelper.Utils.SapQueueProcessorHelperConstants;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;
import com.globalsoft.ServicePro.Item.SOActivityClass;
import com.globalsoft.ServicePro.Item.SOFaultClass;
import com.globalsoft.ServicePro.Item.SOSparesClass;

public class ServiceProActivityMainScreen extends Activity {

	private Vector documentsVect, confVector;
	public TextView customer_address, customer_duedate, customer_so;
	private ServiceProVectSerializable serObj1, serObj2;
	private ServiceOrdDocOpConstraints docsCategory = null;
	private String SrcdocObjIdStr = "", SrcdocPrcsTypeStr = "", NumberExtStr = "", prodDesc = "";
    private String editMatchStr = "";
    private ImageButton[] editbtn, delbtn;
    private ImageButton attaBtn, signAttsBtn;
	private CheckBox[] optioncheckBox;
	private ImageButton faultbtn, sparesbtn, save_submit;
	private TableLayout activitytl;
	private ProgressDialog pdialog = null;
	private SoapObject resultSoap = null;
	private String taskErrorMsgStr = "", taskErrorType="";
	private String encodedImageStr = "";
	private String encodedSignImageStr = "";
	private AlertDialog alertDialog;
	final Handler handler = new Handler();
	private boolean orientChange = false;
	private int selIndex = -1;
	private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5, 
	tableHeaderTV6, tableHeaderTV7;
    private int dispwidth = 300;
    private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5, headerWidth6,
    headerWidth7;
    private String path = "", objId = "";
	private String imgCapPath = "", signPath = "";

	private static final int MENU_NEW_SERVICE = Menu.FIRST;
	private static final int MENU_ADD_CAPTURE = Menu.FIRST+1;
	private static final int MENU_ADD_SIGNATURE = Menu.FIRST+2;
	private boolean internetAccess = false;
	
	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
	private SoapObject requestSoapObj = null;
	private ServiceProErrorMessagePersistent errorDbObj = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_LEFT_ICON);
		ServiceProConstants.setWindowTitleTheme(this);
	    /*setContentView(R.layout.activitytabmain);
	    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
	    
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.activitytabmain); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SAPTASK_ACTIVITYMAINSCR_TITLE));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
	    
		try{
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
		    serObj1 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("docVectorObj");	
		    serObj2 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("confVectorObj");
		    prodDesc = (String)this.getIntent().getStringExtra("prodDesc");	
		    
		    if(prodDesc == null){
		    	prodDesc = "";
		    }
		    ServiceProConstants.PRODUCT_DESC = prodDesc;
		    		
		    if(serObj1 != null){
		    	documentsVect = serObj1.getVector();
		    	ServiceProConstants.showLog("docVector size : "+documentsVect.size());
		    }
		    
		    if(serObj2 != null){
		    	confVector = serObj2.getVector();
		    	ServiceProConstants.showLog("confVector size : "+confVector.size());
		    }
		    
		    customer_address = (TextView) findViewById(R.id.customer_address);
		    customer_duedate = (TextView) findViewById(R.id.customer_duedate);
		    customer_so = (TextView) findViewById(R.id.customer_so);
		    try{
	            if(documentsVect != null){
	                if(documentsVect.size() > 0){
	                    docsCategory = (ServiceOrdDocOpConstraints)documentsVect.elementAt(0);
	                    if(docsCategory != null){
	                        SrcdocObjIdStr = docsCategory.getObjectId().trim();
	                        //SrcdocPrcsTypeStr = docsCategory.getProcessType().trim();
	                        NumberExtStr = docsCategory.getNumberExt().trim();
	                        
	                        customer_address.setText(" "+docsCategory.getNameOrg1()+", "+docsCategory.getCity());
	                        
	                        String dateStr = docsCategory.getZZKEeyDate().toString();
	                    	dateStr = dateStr.trim();
	                    	if(!dateStr.equalsIgnoreCase("")){	                    		
	                        	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
	                			customer_duedate.setText(" "+newdatestr);
	                    	}	                      
	                        customer_so.setText(" "+SrcdocObjIdStr+" "+ServiceProConstants.PRODUCT_DESC);
	                    }
	                }
	            }
	        }
	        catch(Exception asf){
	        	ServiceProConstants.showErrorLog("Error in setBannerHeaderValues : "+asf.toString());
	        }		    
		    
			path = ServiceProConstants.getCapturedBigImagePathConfirScr(SrcdocObjIdStr);
            signPath = ServiceProConstants.getSignatureSmallImagePathConfirScr(SrcdocObjIdStr);
			imgCapPath = ServiceProConstants.getCapturedSmallImagePathConfirScr(SrcdocObjIdStr);
			
		    attaBtn = (ImageButton) findViewById(R.id.attaBtn);
	        attaBtn.setOnClickListener(attaBtnListener); 
	        
	        signAttsBtn = (ImageButton) findViewById(R.id.signAttsBtn);
	        signAttsBtn.setOnClickListener(signAttsBtnListener);
	        
	        TableLayout tl = (TableLayout)findViewById(R.id.middletbllayout1);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));		
		    int count = 0;
		    
		    //Empty all the collection Vectors
		    if(savedInstanceState != null){
		    	orientChange = savedInstanceState.getBoolean("orientChange"); 
		    	if(!orientChange){
				    if(ServiceProConstants.activity_recall_flag == 0){
				    	emptyAllCollectionVectors();
				    }
		    	}
		    }
		    else{
		    	if(ServiceProConstants.activity_recall_flag == 0){
			    	emptyAllCollectionVectors();
			    }
		    }
		    
		    for(int j=0; j<documentsVect.size(); j++){
	            ServiceOrdDocOpConstraints docsMainCategory = (ServiceOrdDocOpConstraints)documentsVect.elementAt(j);	            
	            if(docsMainCategory != null){
	            	if(ServiceProConstants.activity_recall_flag == 0)
	            		addToActivityCollectionVect(docsMainCategory.getProductId(), docsMainCategory.getNumberExt(), docsMainCategory.getZZItemText(), docsMainCategory.getZZItemDesc().toString().trim());
	                String serialNos = docsMainCategory.getSerialNumber();
	                String[] serialNoArr = null;
	                serialNoArr = serialNos.split(",");
	                String productStr = docsMainCategory.getRefObjProductId();
	                System.out.println("Ref Object Nos : "+productStr);
	                String[] productArr = null;
	                productArr = productStr.split(",");
	                count = 0;
	                
	                tr = new TableRow(this);	        
	                TextView tv1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	    			TextView tv2 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	    			TextView tv3 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	    			TextView tv4 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                               
	                for(count=0; count<serialNoArr.length; count++){
	                    System.out.println("count VAl : "+count);
	                    if(count == 0){
	                        editMatchStr = docsMainCategory.getProductId();
	                        tv1.setGravity(Gravity.LEFT);
	                        tv1.setText(""+docsMainCategory.getNumberExt());
	                        tv1.setPadding(5,5,5,5);
	                        tv1.setTypeface(null, Typeface.NORMAL); 
	                        
	                        tv2.setGravity(Gravity.LEFT);
	                        tv2.setText(ServiceProConstants.PRODUCT_DESC + " " +docsMainCategory.getProductId());
	                        tv2.setPadding(5,5,5,5);
	                        tv2.setTypeface(null, Typeface.NORMAL); 
	                        
	                        if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
 	                        	tv1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
 	                        	tv2.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
 	                         }
	                        
	                        tr.addView(tv1);
	                        tr.addView(tv2);
	                        try{
	                            tv3.setGravity(Gravity.LEFT);
	                            tv3.setText(""+serialNoArr[count]);
	                            tv3.setPadding(5,5,5,5);
		                        tv3.setTypeface(null, Typeface.NORMAL); 
	                            
	                            tv4.setGravity(Gravity.LEFT);
	                            tv4.setText(""+productArr[count]);
	                            tv4.setPadding(5,5,5,5);
		                        tv4.setTypeface(null, Typeface.NORMAL); 
	                            
		                        if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
	 	                        	tv3.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	 	                        	tv4.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
		                        }
		                        
	                            tr.addView(tv3);
	                            tr.addView(tv4);
	                        }
	                        catch(Exception df){
	                            System.out.println("Error in Count == 0 : "+df.toString());
	                        }                        
	                    }
	                    else{
	                        try{      
	                        	 tv1.setGravity(Gravity.LEFT);
	                             tv1.setText("");
	                             tv1.setPadding(5,5,5,5);
 	                        	 tv1.setTypeface(null, Typeface.NORMAL); 
	                             
	                             tv2.setGravity(Gravity.LEFT);
	                             tv2.setText("");
	                             tv2.setPadding(5,5,5,5);
	 	                         tv2.setTypeface(null, Typeface.NORMAL); 
	                             
	                             tv3.setGravity(Gravity.LEFT);
	                             tv3.setText(""+serialNoArr[count]);
	                             tv3.setPadding(5,5,5,5);
	 	                         tv3.setTypeface(null, Typeface.NORMAL); 
	                             
	                             tv4.setGravity(Gravity.LEFT);
	                             tv4.setText(""+productArr[count]);
	                             tv4.setPadding(5,5,5,5);
	 	                         tv4.setTypeface(null, Typeface.NORMAL); 

	 	                         if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
	 	                        	tv1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	 	                        	tv2.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	 	                        	tv3.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	 	                        	tv4.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	 	                         }
	 	                         
	                             tr.addView(tv1);
	                             tr.addView(tv2);                             
	                             tr.addView(tv3);
	                             tr.addView(tv4);
	                        }
	                        catch(Exception df1){
	                            System.out.println("Error in Count != 0 : "+df1.toString());
	                        }
	                    }
	                }
	            }
	            tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));          
	        }
		    	    
		    faultbtn = (ImageButton)findViewById(R.id.faultbtn); 
		    faultbtn.setOnClickListener(fault_btListener); 
		    faultbtn.setClickable(true);
		    
		    sparesbtn = (ImageButton)findViewById(R.id.sparesbtn); 
		    sparesbtn.setOnClickListener(spares_btListener);	
		    
		    save_submit = (ImageButton)findViewById(R.id.save_submit); 
		    save_submit.setOnClickListener(save_submit_btListener);
		    
		    activitytl = (TableLayout)findViewById(R.id.actymaintbllayout1);
	        
		    layActivityHeader();
		}
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in oncreate : "+asf.toString());
	    }
	}
	
	protected void onSaveInstanceState(Bundle outState) {     
    	super.onSaveInstanceState(outState);   
    	outState.putBoolean("orientChange", orientChange); 
    } 
	
	public void onConfigurationChanged(Configuration newConfig) { 
        //don't reload the current page when the orientation is changed 
        super.onConfigurationChanged(newConfig); 
        orientChange = true;
        System.out.println("onConfigurationChanged() Called"); 
    } 
	
	private void layActivityHeader(){
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
	        vto6.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV7.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth7 = tableHeaderTV7.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV6 Width1 : "+headerWidth6+" : "+tableHeaderTV6.getMeasuredWidth());
	                displayActivityRows();
	            }
	        });
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in layActivityHeader : "+sfg.toString());
		}
	}//fn layActivityHeader
	
	private void displayActivityRows(){
		try{
			editbtn = new ImageButton[ServiceProConstants.actColltVect.size()];
			delbtn = new ImageButton[ServiceProConstants.actColltVect.size()];
			optioncheckBox = new CheckBox[ServiceProConstants.actColltVect.size()];
			SOActivityClass actvObj = null;
	        String startDateStr = "", endDateStr = "";	      
	        
	        activitytl = (TableLayout)findViewById(R.id.actymaintbllayout1);
	        TableRow activitytr = new TableRow(this);
	        activitytr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));				
			
	        LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			linparams.leftMargin = 5; 
			linparams.rightMargin = 5; 
			linparams.gravity = Gravity.CENTER_VERTICAL;
			
	        String activityname = "", durhours = "", strtdatetime = "", enddatetime = "", notes = "", prodesc="";
	        for (int i =0; i < ServiceProConstants.actColltVect.size(); i++) {	
	        	actvObj = (SOActivityClass)ServiceProConstants.actColltVect.elementAt(i);
		        activityname = actvObj.getActivityStr();  
		        prodesc = actvObj.getServiceDescStr();
		        durhours = actvObj.getDurationStr();
		        startDateStr = ServiceProConstants.getTaskDateString(actvObj.getStartTimeStr(), false);	        
		        //strtdatetime = startDateStr;
		        
		        //UI Part Start for StartDate
			        String startDateStrvalue = ServiceProConstants.getTaskDateStringFormat(actvObj.getStartTimeStr(), false);
			        //ServiceProConstants.showLog("startDateStrvalue :"+startDateStrvalue); 
			        startDateStrvalue = startDateStrvalue.toString();
			        int dat1 = startDateStrvalue.indexOf(" ");
		            String dateFromSAPValue = startDateStrvalue.substring(0, dat1);
		            //ServiceProConstants.showLog("dateFromSAPValue :"+dateFromSAPValue);  
		            String dateStr = dateFromSAPValue.trim();
		        	dateStr = dateStr.trim();
					//ServiceProConstants.showLog("DateStr1 : "+dateStr);
		        	if(!dateStr.equalsIgnoreCase("")){	                    		
		            	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
		    			//ServiceProConstants.showLog("DateStr2 : "+newdatestr);
		    			startDateStrvalue = newdatestr;
		        	}
		        	String startTimeStrvalue = ServiceProConstants.getTaskDateStringFormat(actvObj.getStartTimeStr(), false);
		        	String start_time_value = startTimeStrvalue.substring(dat1+1, startTimeStrvalue.length()).trim();
		        	//ServiceProConstants.showLog("dateValue :"+startDateStrvalue+ " " +start_time_value); 
		        	strtdatetime = startDateStrvalue+ " " +ServiceProConstants.getSecondsRemovedTimeStr(start_time_value);
		        //End
		        
		        endDateStr = ServiceProConstants.getTaskDateString(actvObj.getEndTimeStr(), false);
		        //enddatetime = endDateStr;
		        
			    //UI Part Start for StartDate
			        String endDateStrvalue = ServiceProConstants.getTaskDateStringFormat(actvObj.getEndTimeStr(), false);
			        //ServiceProConstants.showLog("endDateStrvalue :"+endDateStrvalue); 
			        endDateStrvalue = endDateStrvalue.toString();
			        int enddat1 = endDateStrvalue.indexOf(" ");
		            String enddateFromSAPValue = endDateStrvalue.substring(0, enddat1);
		            //ServiceProConstants.showLog("enddateFromSAPValue :"+enddateFromSAPValue);  
		            String enddateStr = enddateFromSAPValue.trim();
		            enddateStr = enddateStr.trim();
					//ServiceProConstants.showLog("DateStr1 : "+enddateStr);
		        	if(!enddateStr.equalsIgnoreCase("")){	                    		
		            	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, enddateStr);
		    			//ServiceProConstants.showLog("DateStr2 : "+newdatestr);
		    			endDateStrvalue = newdatestr;
		        	}
		        	String endTimeStrvalue = ServiceProConstants.getTaskDateStringFormat(actvObj.getEndTimeStr(), false);
		        	String end_time_value = endTimeStrvalue.substring(enddat1+1, endTimeStrvalue.length()).trim();
		        	//ServiceProConstants.showLog("enddateValue :"+endDateStrvalue+ " " +end_time_value); 
		        	enddatetime = endDateStrvalue+ " " +ServiceProConstants.getSecondsRemovedTimeStr(end_time_value);
		        //End
		        notes = actvObj.getNotesStr();
		        
		        activitytr = new TableRow(this);
		        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linear, null);
		        linearLayout.setPadding(0, 0, 0, 0);
		        
		        optioncheckBox[i] = new CheckBox(this);
		        
		        editbtn[i] = new ImageButton(this); 
		        editbtn[i].setId(i);
		        editbtn[i].setBackgroundResource(R.drawable.editpencil);
		        editbtn[i].setLayoutParams(linparams);   
		        editbtn[i].setOnClickListener(new View.OnClickListener() {	
					public void onClick(View v) {
						System.out.println("edit btn Id: "+v.getId());
						int check = v.getId();				
						selIndex = check;
						ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	                    ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	                    
		        		Intent activity_edit_intent = new Intent(ServiceProActivityMainScreen.this, ServiceProActivityEditScreen.class);
		        		activity_edit_intent.putExtra("docVectorObj", serVectObj1);
		        		activity_edit_intent.putExtra("confVectorObj", serVectObj2);
		        		activity_edit_intent.putExtra("selIndex", selIndex);  
		                activity_edit_intent.putExtra("editflag", true);
		                activity_edit_intent.putExtra("editMatchStr", editMatchStr);
		            	startActivity(activity_edit_intent);	            	
					}
				});		
		        linearLayout.addView(editbtn[i]);
		        
		        delbtn[i] = new ImageButton(this);
		        delbtn[i].setId(i);
		        delbtn[i].setBackgroundResource(R.drawable.delete);
		        delbtn[i].setLayoutParams(linparams);   
		        delbtn[i].setOnClickListener(new View.OnClickListener() {	
					public void onClick(View v) {
						System.out.println("del btn Id: "+v.getId());
						int check = v.getId();
						selIndex = check;
						clickAction(selIndex);
					}
				});
		        linearLayout.addView(delbtn[i]);	    
		        
		        optioncheckBox[i].setId(i);
		        optioncheckBox[i].setLayoutParams(linparams);   
		        optioncheckBox[i].setOnClickListener(new View.OnClickListener() {	
					public void onClick(View v) {
						System.out.println("Id: "+v.getId());
						checkboxchecking();
					}
				});
		        linearLayout.addView(optioncheckBox[i]);		        
		        
		        TextView proDescTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView activityTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView durhoursTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView strtdatetimeTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView enddatetimeTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView notesTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
									
				/*ServiceProConstants.showLog("activityname : "+activityname);
		        ServiceProConstants.showLog("durhours : "+durhours);
	            ServiceProConstants.showLog("getStartTimeStr : "+startDateStr);
	            ServiceProConstants.showLog("getEndTimeStr : "+endDateStr);
		        ServiceProConstants.showLog("notes : "+notes);*/

				proDescTxtView.setText(prodesc);
				proDescTxtView.setGravity(Gravity.LEFT);
				proDescTxtView.setWidth(headerWidth2);
				proDescTxtView.setPadding(10,0,0,0);
		        
				activityTxtView.setText(activityname);
		        activityTxtView.setGravity(Gravity.LEFT);
		        activityTxtView.setWidth(headerWidth7);
		        activityTxtView.setPadding(10,0,0,0);
					
				durhoursTxtView.setText(durhours);
				durhoursTxtView.setGravity(Gravity.LEFT); 
				durhoursTxtView.setWidth(headerWidth3);
				durhoursTxtView.setPadding(10,0,0,0);

				strtdatetimeTxtView.setText(strtdatetime);
				strtdatetimeTxtView.setGravity(Gravity.LEFT);
				strtdatetimeTxtView.setWidth(headerWidth4);
				strtdatetimeTxtView.setPadding(10,0,0,0);

				enddatetimeTxtView.setText(enddatetime);
				enddatetimeTxtView.setGravity(Gravity.LEFT);
				enddatetimeTxtView.setWidth(headerWidth5);
				enddatetimeTxtView.setPadding(10,0,0,0);

				notesTxtView.setText(notes);
				notesTxtView.setGravity(Gravity.LEFT);
				notesTxtView.setWidth(headerWidth6);
				notesTxtView.setPadding(10,0,0,0);
				
				if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
					proDescTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					activityTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					durhoursTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					strtdatetimeTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					enddatetimeTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					notesTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
				}
				
				activitytr.addView(linearLayout);
				activitytr.addView(proDescTxtView);
				activitytr.addView(durhoursTxtView);
				activitytr.addView(strtdatetimeTxtView);
				activitytr.addView(enddatetimeTxtView);
				activitytr.addView(activityTxtView);
				activitytr.addView(notesTxtView);
				
				if(i%2 == 0)
					activitytr.setBackgroundResource(R.color.item_even_color);
	            else
	            	activitytr.setBackgroundResource(R.color.item_odd_color);
				
				activitytl.addView(activitytr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			}
		}
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in displayActivityRows : "+asf.toString());
	    }
	}//fn displayActivityRows
		
	private void displayDialog(){
		try{
			alertDialog = new AlertDialog.Builder(this)
			.setMessage("Data entered so far will be lost! Are you sure you want to exit?")
			.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					ServiceProConstants.activity_recall_flag = 0;
					ServiceProConstants.spares_recall_flag = true;
					File f_del_imageCap = new File(imgCapPath);
					if(f_del_imageCap.exists()){
						f_del_imageCap.delete();
					}						
					File f_del_sign = new File(signPath);
					if(f_del_sign.exists()){
						f_del_sign.delete();
					}	
					finish();
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
			ServiceProConstants.showErrorLog("Error in displaySaveDialog : "+asfg.toString());
		}
	}//fn displayDialog
	
	private void emptyAllCollectionVectors(){
        try{
            if(ServiceProConstants.actColltVect == null){
            	ServiceProConstants.actColltVect = new Vector();
            }
            else
            	ServiceProConstants.actColltVect.removeAllElements();                
            
            if(ServiceProConstants.faultColltVect == null){
                ServiceProConstants.faultColltVect = new Vector();
            }
            else
                ServiceProConstants.faultColltVect.removeAllElements();
                
            if(ServiceProConstants.sparesColltVect == null){
                ServiceProConstants.sparesColltVect = new Vector();
            }
            else
                ServiceProConstants.sparesColltVect.removeAllElements();
        }
        catch(Exception dgg){
            ServiceProConstants.showErrorLog("Error in ServiceOrdCfrmTimeScreen Init : "+dgg.toString());
        }
    }//fn emptyAllCollectionVectors
	
	private void addToActivityCollectionVect(String str, String noStr, String zzItemTextStr, String zzItemProdDesc){
        SOActivityClass actObj = null;
        try{
            if(ServiceProConstants.actColltVect != null){
                actObj = new SOActivityClass();
                actObj.setActivityStr(str);
                actObj.setNumberExtnStr(noStr);
                actObj.setNotesStr(zzItemTextStr);
                actObj.setServiceDescStr(zzItemProdDesc);
                //ServiceProConstants.showLog("zzItemTextStr : "+zzItemTextStr);
                ServiceProConstants.actColltVect.addElement(actObj);
                actObj = null;
            }
        }
        catch(Exception sfg){
        	ServiceProConstants.showErrorLog("Error in addToActivityCollectionVect : "+sfg.toString());
        }
    }//fn addToActivityCollectionVect
	
	private void checkboxchecking(){
		try{
			boolean check = false;
			for(int k1=0; k1<optioncheckBox.length; k1++){
	            if(optioncheckBox[k1] != null){
	                if(optioncheckBox[k1].isChecked() == true){
	                	check = true;
	                    break;
	                }
	            }
	        }
			/*if(check)
				faultbtn.setClickable(true);
			else
				faultbtn.setClickable(false);*/
		}
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in checkboxchecking : "+asf.toString());
	    }
	}//fn checkboxchecking
	
	//fault btn click listener
	private OnClickListener fault_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	ServiceProConstants.showLog("fault btn : "+faultbtn.isClickable());
	        	faultbtn.setClickable(true);
	        	
	        	if(ServiceProConstants.actColltVect.size() == 1){
	        		for(int k1=0; k1<optioncheckBox.length; k1++){
	        			optioncheckBox[k1].setChecked(true);
	        			break;
	        		}	        		
	        		ServiceProConstants.showLog("actColltVect : "+ServiceProConstants.actColltVect.size());
		        	int chkbokclicksize = 0;
		        	for(int k1=0; k1<optioncheckBox.length; k1++){
	                    if(optioncheckBox[k1] != null){
	                        if(optioncheckBox[k1].isChecked() == true){
	                        	chkbokclicksize++;
	                        }
	                    }
	                }
		        	
		        	ServiceProConstants.showLog("optioncheckBox.length : "+optioncheckBox.length);
		        	ServiceProConstants.showLog("chkbokclicksize : "+chkbokclicksize);
		        	
		        	if(chkbokclicksize == 1){
			        	ServiceProConstants.showLog("chkbokclicksize: "+chkbokclicksize);
			        	int radioChkIndex = -1;       	
			        	SOActivityClass actvObj2 = null;
			        	if(faultbtn.isClickable()){
			        		for(int k1=0; k1<optioncheckBox.length; k1++){
			                    if(optioncheckBox[k1] != null){
			                        if(optioncheckBox[k1].isChecked() == true){
			                            radioChkIndex = k1;
			                            break;
			                        }
			                    }
			                }
			        		if(radioChkIndex >= 0){	        		
				                if(ServiceProConstants.actColltVect != null){
				                    if(ServiceProConstants.actColltVect.size() > radioChkIndex)
				                        actvObj2 = (SOActivityClass)ServiceProConstants.actColltVect.elementAt(radioChkIndex);
				                }  
				                ServiceProConstants.showLog("prodDesc : "+prodDesc);
				        		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
				                ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);		                
				                Intent fault_intent = new Intent(ServiceProActivityMainScreen.this, ServiceProFaultScreen.class);
				                fault_intent.putExtra("docVectorObj", serVectObj1);
				                fault_intent.putExtra("confVectorObj", serVectObj2);
				                fault_intent.putExtra("radioChkIndex", radioChkIndex);
				                fault_intent.putExtra("actvObj", actvObj2);		            	
				            	startActivity(fault_intent);
			        		}
			        		else{
			        			ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELACTVY));
			        		}
						}
			        	else{
			        		ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELACTVY));
			        	}
		        	}
		        	else{
		        		ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELMULCHEHBOXACTVY));
		        	}
	        	}else{
		        	ServiceProConstants.showLog("actColltVect : "+ServiceProConstants.actColltVect.size());
		        	int chkbokclicksize = 0;
		        	for(int k1=0; k1<optioncheckBox.length; k1++){
	                    if(optioncheckBox[k1] != null){
	                        if(optioncheckBox[k1].isChecked() == true){
	                        	chkbokclicksize++;
	                        }
	                    }
	                }
		        	
		        	ServiceProConstants.showLog("optioncheckBox.length : "+optioncheckBox.length);
		        	ServiceProConstants.showLog("chkbokclicksize : "+chkbokclicksize);
		        	
		        	if(chkbokclicksize == 1){
			        	ServiceProConstants.showLog("chkbokclicksize: "+chkbokclicksize);
			        	int radioChkIndex = -1;       	
			        	SOActivityClass actvObj2 = null;
			        	if(faultbtn.isClickable()){
			        		for(int k1=0; k1<optioncheckBox.length; k1++){
			                    if(optioncheckBox[k1] != null){
			                        if(optioncheckBox[k1].isChecked() == true){
			                            radioChkIndex = k1;
			                            break;
			                        }
			                    }
			                }
			        		if(radioChkIndex >= 0){	        		
				                if(ServiceProConstants.actColltVect != null){
				                    if(ServiceProConstants.actColltVect.size() > radioChkIndex)
				                        actvObj2 = (SOActivityClass)ServiceProConstants.actColltVect.elementAt(radioChkIndex);
				                }  
				                ServiceProConstants.showLog("prodDesc : "+prodDesc);
				        		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
				                ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);		                
				                Intent fault_intent = new Intent(ServiceProActivityMainScreen.this, ServiceProFaultScreen.class);
				                fault_intent.putExtra("docVectorObj", serVectObj1);
				                fault_intent.putExtra("confVectorObj", serVectObj2);
				                fault_intent.putExtra("radioChkIndex", radioChkIndex);
				                fault_intent.putExtra("actvObj", actvObj2);		            	
				            	startActivity(fault_intent);
			        		}
			        		else{
			        			ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELACTVY));
			        		}
						}
			        	else{
			        		ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELACTVY));
			        	}
		        	}
		        	else{
		        		ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELMULCHEHBOXACTVY));
		        	}
	        	}
        	}
            catch(Exception sfg){
            	ServiceProConstants.showErrorLog("Error in fault_btListener : "+sfg.toString());
            }
        }
    };
    
    //spares btn click listener
	private OnClickListener spares_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		ServiceProConstants.showLog("prodDesc : "+prodDesc);
	        	Intent spares_intent = new Intent(ServiceProActivityMainScreen.this, ServiceProSparesScreen.class);				        		
	    		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	            spares_intent.putExtra("docVectorObj", serVectObj1);
	            spares_intent.putExtra("confVectorObj", serVectObj2);
	        	startActivity(spares_intent);
	        }
		    catch(Exception asf){
		    	ServiceProConstants.showErrorLog("Error in spares_btListener : "+asf.toString());
		    }
        }
    };
    
    public void clickAction(final int position){
    	try{
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(R.string.SERVICEORD_COMMON_DG_DELTHIS);
	        builder.setPositiveButton(R.string.SERVICEPRO_OK, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	activityDeleteAction(position);
	            }
	        });                
	        builder.setNegativeButton(R.string.SERVICEPRO_CANCEL, null);
	        builder.show();
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in clickAction : "+asf.toString());
	    }
    }//fn clickAction
    
    private void activityDeleteAction(int index){
        try{           
            if(ServiceProConstants.actColltVect != null){    
                if(ServiceProConstants.actColltVect.size() > index){
                	ServiceProConstants.actColltVect.removeElementAt(index);        			
        			activitytl.removeAllViews();
        			displayActivityRows();
                }
            }
        }
        catch(Exception dsgf){
        	ServiceProConstants.showErrorLog("Error in activityDeleteAction : "+dsgf.toString());
        }
    }//fn activityDeleteAction
   
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			displayDialog();
		}
		return super.onKeyDown(keyCode, event);
	}    
    
    //attachment btn click listener
    private OnClickListener attaBtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	File f = new File(imgCapPath);
			    if(f.exists()){
			    	Intent intent = new Intent(ServiceProActivityMainScreen.this, ImageDislayActivity.class);
			    	//if 1 is Capture Image, 2 is Signature Image
			    	intent.putExtra("image_display", 1);
		        	intent.putExtra("objectId", SrcdocObjIdStr.toString().trim());
		        	intent.putExtra("confirmation_scr", true);
					startActivityForResult(intent, ServiceProConstants.CAPTURED_IMAGE_DIS_SCREEN);
			    }
			    else{
			    	ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_MAIN_ACTSCR_ATTACHMENT_ALERT));
			    }
	        }
	        catch(Exception sfg){
	        	ServiceProConstants.showErrorLog("Error in attaBtnListener : "+sfg.toString());
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
    
    private void signatureAction(){
        try{      
        	Intent intent = new Intent(ServiceProActivityMainScreen.this, FingerPaint.class);
        	intent.putExtra("objectId", SrcdocObjIdStr.toString().trim());
        	intent.putExtra("confirmation_scr", true);
			startActivityForResult(intent, ServiceProConstants.SIGNATURE_DIS_SCREEN);
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On signatureAction : "+asf.toString());
        }
    }//fn signatureAction
    
    private void signatureReviewAction(){
    	try{              	
        	File f = new File(signPath);
		    if(f.exists()){
		    	Intent intent = new Intent(ServiceProActivityMainScreen.this, ImageDislayActivity.class);
		    	//if 1 is Capture Image, 2 is Signature Image
		    	intent.putExtra("image_display", 2);
	        	intent.putExtra("objectId", SrcdocObjIdStr.toString().trim());
	        	intent.putExtra("confirmation_scr", true);
				startActivityForResult(intent, ServiceProConstants.CAPTURED_IMAGE_DIS_SCREEN);
		    }
		    else{
		    	ServiceProConstants.showErrorDialog(ServiceProActivityMainScreen.this, getString(R.string.SERVICEORD_MAIN_ACTSCR_ATTACHMENT_SIGN_ALERT));
		    }
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On signatureAction : "+asf.toString());
        }
    }//fn signatureReviewAction
    
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
    
    private void addBtnAction(){
        try{        
        	ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);            
    		Intent activity_edit_intent = new Intent(ServiceProActivityMainScreen.this, ServiceProActivityEditScreen.class);
    		activity_edit_intent.putExtra("docVectorObj", serVectObj1);
    		activity_edit_intent.putExtra("confVectorObj", serVectObj2);
    		activity_edit_intent.putExtra("selIndex", 0);  
            activity_edit_intent.putExtra("editflag", false);
            activity_edit_intent.putExtra("editMatchStr", editMatchStr);
        	startActivity(activity_edit_intent);
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On addBtnAction : "+asf.toString());
        }
    }//fn addBtnAction
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
		  	if(requestCode==ServiceProConstants.IMAGE_CAPTURE_SCREEN){		
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
						encodedSignImageStr = Base64.encodeBytes(b); //Base64.encodeToString(b, Base64.DEFAULT);//encodeBytes(b);
						File f_sign = new File(signPath);
						if(f_sign.exists()){
							signAttsBtn.setVisibility(View.VISIBLE);
						}						
						else{
							signAttsBtn.setVisibility(View.GONE);
						}		
				  	}
				    catch(Exception asf){
				    	ServiceProConstants.showErrorLog("Error in onActivityResultSIGNATURE_DIS_SCREEN : "+asf.toString());
				    }
			  }
			  else{
				  try{
					  	File f_sign = new File(signPath);
						if(f_sign.exists()){
							signAttsBtn.setVisibility(View.VISIBLE);
						}						
						else{
							signAttsBtn.setVisibility(View.GONE);
						}	
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
                    	pdialog = ProgressDialog.show(ServiceProActivityMainScreen.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
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
			ServiceProConstants.showErrorLog("Error in image capture attach image display:"+ae.toString());
		}	
    }//fn attachImageDisplay
    
    //save_submit btn click listener
    private OnClickListener save_submit_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		File file = new File(signPath); 
            	if (file.exists()) {     
            		Bitmap bm = BitmapFactory.decodeFile(signPath);
    			  	ByteArrayOutputStream baos = new ByteArrayOutputStream();   
    				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object    
    				byte[] b = baos.toByteArray();
    				encodedSignImageStr = Base64.encodeBytes(b); //Base64.encodeToString(b, Base64.DEFAULT); //Base64.encodeBytes(b);
            	} 	        	
	        	onSaveAndSubmitAction();      
	        }
	        catch(Exception asf){
	        	ServiceProConstants.showErrorLog("Error On save_submit_btListener : "+asf.toString());
	        }
        }
    };
    
    private void onSaveAndSubmitAction(){
        try{
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.SERVICEORD_COMMON_DG_FULLDONE);
            builder.setPositiveButton(R.string.SERVICEPRO_OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	initSoapConnection();  
                }
            });                
            builder.setNegativeButton(R.string.SERVICEPRO_CANCEL, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                		
                }
            });                
            builder.show();                
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error On onSaveAndSubmitAction : "+asf.toString());
        }
    }//fn onSaveAndSubmitAction
    
    private void initSoapConnection(){   
    	SoapSerializationEnvelope envelopeC = null;
        try{
        	SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME);    
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            if(encodedImageStr.equalsIgnoreCase(encodedSignImageStr)){
            	ServiceProConstants.showLog("True");
            }else{
            	ServiceProConstants.showLog("False");
            }                      
                        
            ServiceProInputConstraints C0[], C1[], C2[], C3[];
            C0 = new ServiceProInputConstraints[10];
            for(int k=0; k<10; k++){
                C0[k] = new ServiceProInputConstraints(); 
            }
            
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]SERVICE-CONF-CREATE[.]VERSION[.]0";
            C0[3].Cdata = "DATA-TYPE[.]ZGSXSMST_SRVCCNFRMTN20[.]SRCDOC_OBJECT_ID[.]SRCDOC_PROCESS_TYPE[.]ZZSRVCORDRCMPLT";
            C0[4].Cdata = "DATA-TYPE[.]ZGSXSMST_SRVCCNFRMTNACTVTY20[.]SRCDOC_NUMBER_EXT[.]NUMBER_EXT[.]PRODUCT_ID[.]QUANTITY[.]PROCESS_QTY_UNIT[.]ZZITEM_DESCRIPTION[.]ZZITEM_TEXT[.]DATE_FROM[.]DATE_TO[.]TIME_FROM[.]TIME_TO[.]TIMEZONE_FROM";
            C0[5].Cdata = "DATA-TYPE[.]ZGSXSMST_SRVCCNFRMTNFAULT20[.]NUMBER_EXT[.]ZZSYMPTMCODEGROUP[.]ZZSYMPTMCODE[.]ZZSYMPTMTEXT[.]ZZPRBLMCODEGROUP[.]ZZPRBLMCODE[.]ZZPRBLMTEXT[.]ZZCAUSECODEGROUP[.]ZZCAUSECODE[.]ZZCAUSETEXT";
            C0[6].Cdata = "DATA-TYPE[.]ZGSXSMST_SRVCCNFRMTNMTRL20[.]SRCDOC_NUMBER_EXT[.]NUMBER_EXT[.]PRODUCT_ID[.]QUANTITY[.]PROCESS_QTY_UNIT[.]ZZITEM_DESCRIPTION[.]ZZITEM_TEXT";
            C0[7].Cdata = "ZGSXSMST_SRVCCNFRMTN20[.]"+SrcdocObjIdStr+"[.]"+SrcdocPrcsTypeStr+"[.]";
            C0[8].Cdata = "ZGSXCAST_ATTCHMNT01[.]"+SrcdocObjIdStr+"[.]"+SrcdocPrcsTypeStr+"[.][.]"+imgCapPath+"[.]"+encodedImageStr;
            C0[9].Cdata = "ZGSXCAST_ATTCHMNT01[.]"+SrcdocObjIdStr+"[.]"+SrcdocPrcsTypeStr+"[.][.]"+signPath+"[.]"+encodedSignImageStr;
            
            Vector listVect = new Vector();
            for(int k1=0; k1<C0.length; k1++){
                listVect.addElement(C0[k1]);
            }
            
            long startTiming = 0, endTiming = 0;
            String startDateStr = "", endDateStr = "", numbetExn = "", srcNumExn = "";
            // Adding Activities to confirmation
            if(ServiceProConstants.actColltVect != null){
                int actCollVectSize = ServiceProConstants.actColltVect.size();
                if(actCollVectSize > 0){
                    C1 = new ServiceProInputConstraints[actCollVectSize];
                    SOActivityClass activityObj = null;
                    for(int m1=0; m1<actCollVectSize; m1++){
                        activityObj = (SOActivityClass)ServiceProConstants.actColltVect.elementAt(m1);
                        C1[m1] = new ServiceProInputConstraints(); 
                        C1[m1].Cdata = "ZGSXSMST_SRVCCNFRMTNACTVTY20[.]";
                        if(activityObj != null){
                            try{
                                numbetExn = activityObj.getNumberExtnStr().trim();
                                if(numbetExn.startsWith("50"))
                                    srcNumExn = "";
                                else                                    
                                    srcNumExn = numbetExn;
                                    
                                startTiming = activityObj.getStartTimeStr();
                                endTiming = activityObj.getEndTimeStr();
                                startDateStr = ServiceProConstants.getTaskDateString(startTiming, false).trim();
                                endDateStr = ServiceProConstants.getTaskDateString(endTiming, false).trim();
                                String[] strDatArr = startDateStr.split(" ");
                                String[] endDatArr = endDateStr.split(" ");
                                C1[m1].Cdata += srcNumExn+"[.]"+numbetExn+"[.]"+activityObj.getActivityStr()+"[.]";
                                C1[m1].Cdata += activityObj.getDurationStr()+"[.][.][.]"+activityObj.getNotesStr()+"[.]";
                                C1[m1].Cdata += strDatArr[0]+"[.]"+endDatArr[0]+"[.]"+strDatArr[1]+"[.]"+endDatArr[1]+"[.]"+ServiceProConstants.activityTimeZoneSelRawoffset;
                            }
                            catch(Exception sdf){
                            	ServiceProConstants.showErrorLog("Error in Service Confirm Date Manipulations : "+sdf.toString());
                            }
                        }
                        listVect.addElement(C1[m1]);
                    }
                }
            }
            
            // Adding Faults to confirmation
            if(ServiceProConstants.faultColltVect != null){    
                int faultCollVectSize = ServiceProConstants.faultColltVect.size();
                System.out.println("No of faults : "+faultCollVectSize);
                if(faultCollVectSize > 0){
                    C2 = new ServiceProInputConstraints[faultCollVectSize];
                    SOFaultClass faultObj = null;
                    for(int m2=0; m2<faultCollVectSize; m2++){
                        faultObj = (SOFaultClass)ServiceProConstants.faultColltVect.elementAt(m2);
                        C2[m2] = new ServiceProInputConstraints(); 
                        C2[m2].Cdata = "ZGSXSMST_SRVCCNFRMTNFAULT20[.]";
                        if(faultObj != null){
                            C2[m2].Cdata += faultObj.getActivityStr()+"[.]"+faultObj.getSymptomGroupStr()+"[.]"+faultObj.getSymptomCodeStr()+"[.]"+faultObj.getSymptomDescStr()+"[.]";
                            C2[m2].Cdata += faultObj.getProblemGroupStr()+"[.]"+faultObj.getProblemCodeStr()+"[.]"+faultObj.getProblemDescStr()+"[.]";
                            C2[m2].Cdata += faultObj.getCauseGroupStr()+"[.]"+faultObj.getCauseCodeStr()+"[.]"+faultObj.getCauseDescStr();
                        }
                        listVect.addElement(C2[m2]);
                    }
                }
            }
            
            // Adding Spares to confirmation
            if(ServiceProConstants.sparesColltVect != null){    
                int spareCollVectSize = ServiceProConstants.sparesColltVect.size();
                if(spareCollVectSize > 0){
                    C3 = new ServiceProInputConstraints[spareCollVectSize];
                    SOSparesClass spareObj = null;
                    String indexStr = "";
                    for(int m3=0; m3<spareCollVectSize; m3++){
                        spareObj = (SOSparesClass)ServiceProConstants.sparesColltVect.elementAt(m3);
                        C3[m3] = new ServiceProInputConstraints(); 
                        //C3[m3].Cdata = "ZGSXSMST_SRVCCNFRMTNMTRL20[.]"+SrcdocPrcsTypeStr+"[.]"+SrcdocPrdIdStr+"[.]";
                        indexStr = "1000"+String.valueOf(m3+1);
                        C3[m3].Cdata = "ZGSXSMST_SRVCCNFRMTNMTRL20[.]";
                        if(spareObj != null){
                            C3[m3].Cdata += spareObj.getNumberExtStr()+"[.]"+indexStr+"[.]";
                            C3[m3].Cdata += spareObj.getMaterialStr()+"[.]"+spareObj.getQuantityStr()+"[.]";
                            C3[m3].Cdata += spareObj.getUnitsStr()+"[.]"+spareObj.getMaterialDescStr()+"[.]";
                        }
                        listVect.addElement(C3[m3]);
                    }
                }
            }
            
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);
            envelopeC.setOutputSoapObject(request);        
            ServiceProConstants.showLog("Request:" +request.toString());
        	respType = ServiceProConstants.TASK_CONFIRMATION_TYPE;
    		requestSoapObj = request;
            internetAccess = ServiceProConstants.networkAvailableCheck(ServiceProActivityMainScreen.this);
            if(internetAccess){
                //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL, request);   
                doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
            }else{
            	saveToLDB();
            }
        }
        catch(Exception asd){
        	ServiceProConstants.showErrorLog("Error in Service Confirm initSoapConnection : "+asd.toString());
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
	    			if(respType == ServiceProConstants.TASK_CONFIRMATION_TYPE)
	    				updateResponse(resultSoap);
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
    		//SapQueueProcessorHelperConstants.saveOfflineContentToQueueProcessor(this, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.APPLN_PACKAGE_NAME, objectIdStr, ServiceProConstants.APPLN_BGSERVICE_NAME, ServiceProConstants.STATUS_UPDATE_API, requestSoapObj);
		} catch (Exception errg) {
			SapGenConstants.showErrorLog("Error in sendToQueueProcessor : "+errg.toString());
		}
    }//fn sendToQueueProcessor
    
    private void saveToLDB(){
		try{
        	ServiceProConstants.showLog("SrcdocObjIdStr:" +SrcdocObjIdStr.toString());
        	ServiceProConstants.showLog("NumberExtStr:" +NumberExtStr);
        	ServiceProDBOperations.insertConfCollecListDataInToDBForOfflineMode(this, SrcdocObjIdStr.toString().trim(), NumberExtStr.toString().trim());
        	ServiceProConstants.activity_recall_flag = 0;
			ServiceProConstants.spares_recall_flag = true;  
        	Long now = Long.valueOf(System.currentTimeMillis());     
			//SapQueueProcessorHelperConstants.saveOfflineContentToQueueProcessor(this, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.APPLN_PACKAGE_NAME, SrcdocObjIdStr.toString(), ServiceProConstants.APPLN_BGSERVICE_NAME, ServiceProConstants.TASK_CONFIRMATION_API, requestSoapObj, now);            	
			refreshScreen();
		}
	    catch (Throwable e) {
	        ServiceProConstants.showErrorLog("Error in saveToLDB : "+e.toString());
	    }
	}//fn saveToLDB
    
    /*private void startNetworkConnection(final Context ctxAct, final SoapSerializationEnvelope envelopeCE, final String url, final SoapObject request){
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
                    				getSOAPViaHTTP(envelopeCE, url, request);
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
    
    private void getSOAPViaHTTP(SoapSerializationEnvelope envelopeCE, String url, final SoapObject request){		
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
                	updateResponse(resultSoap, request);
                }
            });
        }
    }//fn getSOAPViaHTTP
*/        
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
    
    /*public void updateResponse(SoapObject soap){
    	String soapMsg="";
    	if(soap != null){
    		soapMsg = soap.toString();
    		ServiceProOutputConstraints category = null;
            try{ 
                String delimeter = "[.]", result="", res="";
                SoapObject pii = null;
                String[] resArray = new String[37];
                int propsCount = 0, errCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0;
                for (int i = 0; i < soap.getPropertyCount(); i++) {                
                    pii = (SoapObject)soap.getProperty(i);
                    propsCount = pii.getPropertyCount();
                    //ServiceProConstants.showLog("propsCount : "+propsCount);
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                        	//ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
                            if(j >= 3){
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
            catch(Exception sff){
            	ServiceProConstants.showErrorLog("On updateSOCResponse : "+sff.toString());
            }
            finally{
            	boolean resMsgErr = false; // false -> Success Msg, true - > Error Msg
        		resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg);
                try{
                    if(!resMsgErr){
                    	File f_del = new File(imgCapPath);
            			if(f_del.exists()){
            				f_del.delete();
            			}		
            			File f_del1 = new File(signPath);
            			if(f_del1.exists()){
            				f_del1.delete();
            			}
            			ServiceProConstants.activity_recall_flag = 0;
            			ServiceProConstants.spares_recall_flag = true;   			
            			refreshScreen();
                    }
                    else{
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
                    	ServiceProConstants.activity_recall_flag = 0;
                    	ServiceProConstants.spares_recall_flag = true;
                    	File f_del = new File(imgCapPath);
            			if(f_del.exists()){
            				f_del.delete();
            			}		
            			File f_del1 = new File(signPath);
            			if(f_del1.exists()){
            				f_del1.delete();
            			}
                        finish();
                    }
                }
                catch(Exception asf){
                	ServiceProConstants.showErrorLog("On finally : "+asf.toString());
                }
            }
        }
        
    }//fn updateTaskResponse
*/    
    public void updateResponse(SoapObject soap){
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
            		File f_del = new File(imgCapPath);
        			if(f_del.exists()){
        				f_del.delete();
        			}		
        			File f_del1 = new File(signPath);
        			if(f_del1.exists()){
        				f_del1.delete();
        			}
        			ServiceProConstants.activity_recall_flag = 0;
        			ServiceProConstants.spares_recall_flag = true;   			
        			refreshScreen();
            	}
            	else{
            		boolean apiExists = false;
            		if(errorDbObj == null)
	        			errorDbObj = new ServiceProErrorMessagePersistent(this.getApplicationContext());
            		if(errorDbObj != null){
            			apiExists = errorDbObj.checkTrancIdApiExists(SrcdocObjIdStr.trim(), ServiceProConstants.TASK_CONFIRMATION_API.trim());
				    	String tracId = SrcdocObjIdStr.trim();
				    	String apiName = ServiceProConstants.TASK_CONFIRMATION_API.trim();
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
                	ServiceProConstants.activity_recall_flag = 0;
                	ServiceProConstants.spares_recall_flag = true;
                	File f_del = new File(imgCapPath);
        			if(f_del.exists()){
        				f_del.delete();
        			}		
        			File f_del1 = new File(signPath);
        			if(f_del1.exists()){
        				f_del1.delete();
        			}
                    finish();
            	}
            }
            catch(Exception asf){}
        }
    }//fn updateResponse
    
    private void refreshScreen(){
    	try {
			Intent activitymain_intent = new Intent(this, ServiceProConfirmationScreen.class);	
			activitymain_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activitymain_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);			
			activitymain_intent.putExtra("refresh", true);
			startActivity(activitymain_intent);
			finish();
		} catch (Exception easf) {
			ServiceProConstants.showErrorLog("On refreshScreen : "+easf.toString());
		}
    }//fn refreshScreen

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_NEW_SERVICE, 0, "Add new Service");
		menu.add(0, MENU_ADD_CAPTURE, 0, "Capture Image");		
		menu.add(0, MENU_ADD_SIGNATURE, 0, "Add Signature");
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
			case MENU_NEW_SERVICE: {
				addBtnAction();
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
	    }
		return super.onOptionsItemSelected(item);
	}
    
}//End of class ServiceProActivityMainScreen
