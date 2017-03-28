package com.globalsoft.ServicePro;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Item.SOActivityClass;
import com.globalsoft.ServicePro.Item.SOFaultClass;

public class ServiceProFaultScreen extends Activity {
	
	private ImageButton[] editbtn, delbtn;
	private CheckBox[] optioncheckBox;		
	private ServiceOrdDocOpConstraints docsCategory = null;
	private ServiceProVectSerializable serObj1, serObj2;
	private SOActivityClass actvObj = null;
	private Vector documentsVect, confVector;
	private int actSelIndex = 0;
	public TextView customer_address, customer_duedate, customer_so;
	//private Button activitybtn, sparesbtn;
	private ImageButton activitybtn, sparesbtn;
	
    String actValstr = "";
    String actMatchStr = "";
    private int selIndex = -1;
    private ImageButton addbtn;
    private TableLayout faulttl;
    
    private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5;
	private TextView tableHeaderTV6, tableHeaderTV7, tableHeaderTV8, tableHeaderTV9, tableHeaderTV10;
    private int dispwidth = 300;
    private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5, headerWidth6, headerWidth7, headerWidth8, headerWidth9, headerWidth10;
	private boolean faultEditScreenCalling = true;
	
	private static final int MAIN_ID = Menu.FIRST;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		ServiceProConstants.setWindowTitleTheme(this);
		/*setContentView(R.layout.faultscreen);	
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.faultscreen); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SAPTASK_FAULTSCR_TITLE));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
		
		try{
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			serObj1 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("docVectorObj");	
		    serObj2 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("confVectorObj");
		    actSelIndex = this.getIntent().getIntExtra("radioChkIndex", 0);
		    actvObj = (SOActivityClass)this.getIntent().getSerializableExtra("actvObj");
		    
		    
		    if(serObj1 != null){
		    	documentsVect = serObj1.getVector();
		    	ServiceProConstants.showLog("docVector size : "+documentsVect.size());
		    }
		    
		    if(serObj2 != null){
		    	confVector = serObj2.getVector();
		    	ServiceProConstants.showLog("confVector size : "+confVector.size());
		    }	    
		    //ServiceProConstants.showLog("actSelIndex : "+actSelIndex);
			
		    //setBannerHeaderValues
		    customer_address = (TextView) findViewById(R.id.customer_address);
		    customer_duedate = (TextView) findViewById(R.id.customer_duedate);
		    customer_so = (TextView) findViewById(R.id.customer_so);
		    try{
	            if(documentsVect != null){
	                if(documentsVect.size() > 0){
	                    docsCategory = (ServiceOrdDocOpConstraints)documentsVect.elementAt(0);
	                    if(docsCategory != null){
	                    	docsCategory.getNumberExt().trim();
	                    	
	                        customer_address.setText(" "+docsCategory.getNameOrg1()+", "+docsCategory.getCity());
	                        
	                        String dateStr = docsCategory.getZZKEeyDate().toString();
	                    	dateStr = dateStr.trim();
	            			//ServiceProConstants.showLog("DateStr1 : "+dateStr);
	                    	if(!dateStr.equalsIgnoreCase("")){	                    		
	                        	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
	                			//ServiceProConstants.showLog("DateStr2 : "+newdatestr);
	                			customer_duedate.setText(" "+newdatestr);
	                    	}
	                    	
	                        //customer_duedate.setText(" "+docsCategory.getZZKEeyDate());
	                        customer_so.setText(" "+docsCategory.getObjectId()+" "+ServiceProConstants.PRODUCT_DESC);                        
	                    }
	                }
	            }
	        }
	        catch(Exception asf){
	        	ServiceProConstants.showErrorLog("Error in setBannerHeaderValues : "+asf.toString());
	        }
		    
	        /*
	        addbtn = (ImageButton) findViewById(R.id.addBtn);
	        addbtn.setOnClickListener(addbtnListener); 
	        */
		    
	        TableLayout tl_middle = (TableLayout)findViewById(R.id.middletbllayout1);
			TableRow tr_middle = new TableRow(this);
			tr_middle.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));	
		    
			try{
	            if(actvObj != null){                                  
	                TextView actyTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView durTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					
					actyTxtView.setGravity(Gravity.LEFT);
					actyTxtView.setTypeface(null, Typeface.NORMAL); 
					actyTxtView.setText(ServiceProConstants.PRODUCT_DESC + " " +actvObj.getActivityStr());
					actyTxtView.setPadding(2,2,2,2);
					
					durTxtView.setGravity(Gravity.LEFT);
					durTxtView.setTypeface(null, Typeface.NORMAL); 
					durTxtView.setText(actvObj.getDurationStr());
					durTxtView.setPadding(2,2,2,2);
					
					ServiceProConstants.showLog("getActivityStrValues : "+actvObj.getActivityStr());
					ServiceProConstants.showLog("getDurationStrValues : "+actvObj.getDurationStr());
					
					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
						actyTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						durTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					}
					
					tr_middle.addView(actyTxtView);
					tr_middle.addView(durTxtView);
					
					tl_middle.addView(tr_middle,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	            }   
	        }
	        catch(Exception df1){
	            System.out.println("Error in grid2 : "+df1.toString());
	        }
			
	        activitybtn = (ImageButton)findViewById(R.id.activitybtn); 
	        activitybtn.setOnClickListener(activity_btListener); 
		    
		    sparesbtn = (ImageButton)findViewById(R.id.sparesbtn); 
		    sparesbtn.setOnClickListener(spares_btListener);
	        		
	        actValstr = getActivityValue();
	        actMatchStr = "";
	        ServiceProConstants.showLog("faultColltVect size : "+ServiceProConstants.faultColltVect.size());
	        ServiceProConstants.showLog("actValstr: "+actValstr);
	        	        
	        SOFaultClass faultObj = null;
			for (int i =0; i < ServiceProConstants.faultColltVect.size(); i++) {
	        	faultObj = (SOFaultClass)ServiceProConstants.faultColltVect.elementAt(i);
	            actMatchStr = faultObj.getActivityStr().trim();                    	            
	            if(actValstr.equalsIgnoreCase(actMatchStr)){
	            	ServiceProConstants.showLog("Exits");
	            	faultEditScreenCalling = false;
	            	break;
	            }
			}
	        
	        
	        editbtn = new ImageButton[ServiceProConstants.faultColltVect.size()];
			delbtn = new ImageButton[ServiceProConstants.faultColltVect.size()];
			//optioncheckBox = new CheckBox[ServiceProConstants.faultColltVect.size()];
			
	        //if(ServiceProConstants.faultColltVect.size() <= 0){
	        if(faultEditScreenCalling){
	        	finish();
		        Intent faultedit_intent = new Intent(ServiceProFaultScreen.this, ServiceProFaultEditScreen.class);
		        ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	            faultedit_intent.putExtra("docVectorObj", serVectObj1);
	            faultedit_intent.putExtra("confVectorObj", serVectObj2);
		        faultedit_intent.putExtra("selIndex", -1);  
		        faultedit_intent.putExtra("editflag", false);
		        faultedit_intent.putExtra("actvObj", actvObj);
		    	startActivity(faultedit_intent);
	        }       
	        
	        //displayFaultRows();
	        layFaultHeader();
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in oncreate : "+de.toString());
	    }
	}
	
	private void layFaultHeader(){
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
			
			tableHeaderTV8 = (TextView)findViewById(R.id.TableHeaderTV8);
			tableHeaderTV8.setGravity(Gravity.LEFT);
			tableHeaderTV8.setPadding(10,5,5,5);
			
			tableHeaderTV9 = (TextView)findViewById(R.id.TableHeaderTV9);
			tableHeaderTV9.setGravity(Gravity.LEFT);
			tableHeaderTV9.setPadding(10,5,5,5);
			
			tableHeaderTV10 = (TextView)findViewById(R.id.TableHeaderTV10);
			tableHeaderTV10.setGravity(Gravity.LEFT);
			tableHeaderTV10.setPadding(10,5,5,5);
			
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
	            }
	        });
	        
	        ViewTreeObserver vto8 = tableHeaderTV8.getViewTreeObserver();
	        vto8.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV8.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth8 = tableHeaderTV8.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV8 Width1 : "+headerWidth8+" : "+tableHeaderTV8.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto9 = tableHeaderTV9.getViewTreeObserver();
	        vto9.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV9.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth9 = tableHeaderTV9.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV9 Width1 : "+headerWidth9+" : "+tableHeaderTV9.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto10 = tableHeaderTV10.getViewTreeObserver();
	        vto10.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = tableHeaderTV10.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                headerWidth10 = tableHeaderTV10.getWidth();
	                //ServiceProConstants.showLog("tableHeaderTV10 Width1 : "+headerWidth10+" : "+tableHeaderTV10.getMeasuredWidth());
	    	        displayFaultRows();
	            }
	        });
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in layFaultHeader : "+sfg.toString());
		}
	}//fn layFaultHeader
	
	
	private void displayFaultRows(){
		String symgrp = "", symcode = "", symdesc = "", pblgrp = "", pblcode = "", pbldesc = "", causegrp = "", causecode = "", causedesc = "";
		try{
			/*
			faulttl = (TableLayout)findViewById(R.id.faultmaintbllayout1);
			
			TableRow faulttiletr = new TableRow(this);
			faulttiletr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	                
			TextView optionstvHeader = new TextView(this);
	        TextView symgrptvHeader = new TextView(this);
			TextView symcodetvHeader = new TextView(this);
			TextView symdesctvHeader = new TextView(this);
			TextView pblgrptvHeader = new TextView(this);
			TextView pblcodetvHeader = new TextView(this);
			TextView pbldesctvHeader = new TextView(this);
			TextView causegrptvHeader = new TextView(this);
			TextView causecodetvHeader = new TextView(this);
			TextView causedesctvHeader = new TextView(this);
			
			optionstvHeader.setGravity(Gravity.CENTER);
			optionstvHeader.setText(getString(R.string.SERVICEPRO_OPTIONS));
			optionstvHeader.setTextColor(Color.WHITE);
			optionstvHeader.setPadding(5,5,5,5);
			optionstvHeader.setTypeface(null, Typeface.BOLD); 
			optionstvHeader.setClickable(true);
			optionstvHeader.setEnabled(false);
			
			symgrptvHeader.setGravity(Gravity.LEFT);
			symgrptvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_SYMGRP));
			symgrptvHeader.setTextColor(Color.WHITE);
			symgrptvHeader.setPadding(5,5,5,5);
			symgrptvHeader.setTypeface(null, Typeface.BOLD); 
			symgrptvHeader.setClickable(true);
			symgrptvHeader.setEnabled(false);
			
			symcodetvHeader.setGravity(Gravity.LEFT);
			symcodetvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_SYMCD));
			symcodetvHeader.setTextColor(Color.WHITE);
			symcodetvHeader.setPadding(5,5,5,5);
			symcodetvHeader.setTypeface(null, Typeface.BOLD); 
			symcodetvHeader.setClickable(true);
			symcodetvHeader.setEnabled(false);
			
			symdesctvHeader.setGravity(Gravity.LEFT);
			symdesctvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_SYMDESC));
			symdesctvHeader.setTextColor(Color.WHITE);
			symdesctvHeader.setPadding(5,5,5,5);
			symdesctvHeader.setTypeface(null, Typeface.BOLD); 
			symdesctvHeader.setClickable(true);
			symdesctvHeader.setEnabled(false);
			
			pblgrptvHeader.setGravity(Gravity.LEFT);
			pblgrptvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_PRBGRP));
			pblgrptvHeader.setTextColor(Color.WHITE);
			pblgrptvHeader.setPadding(5,5,5,5);
			pblgrptvHeader.setTypeface(null, Typeface.BOLD); 
			pblgrptvHeader.setClickable(true);
			pblgrptvHeader.setEnabled(false);
			
			pblcodetvHeader.setGravity(Gravity.LEFT);
			pblcodetvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_PRBCD));
			pblcodetvHeader.setTextColor(Color.WHITE);
			pblcodetvHeader.setPadding(5,5,5,5);
			pblcodetvHeader.setTypeface(null, Typeface.BOLD); 
			pblcodetvHeader.setClickable(true);
			pblcodetvHeader.setEnabled(false);		
			
			pbldesctvHeader.setGravity(Gravity.LEFT);
			pbldesctvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_PROBDESC));
			pbldesctvHeader.setTextColor(Color.WHITE);
			pbldesctvHeader.setPadding(5,5,5,5);
			pbldesctvHeader.setTypeface(null, Typeface.BOLD); 
			pbldesctvHeader.setClickable(true);
			pbldesctvHeader.setEnabled(false);
			
			causegrptvHeader.setGravity(Gravity.LEFT);
			causegrptvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_CAUSGRP));
			causegrptvHeader.setTextColor(Color.WHITE);
			causegrptvHeader.setPadding(5,5,5,5);
			causegrptvHeader.setTypeface(null, Typeface.BOLD); 
			causegrptvHeader.setClickable(true);
			causegrptvHeader.setEnabled(false);
			
			causecodetvHeader.setGravity(Gravity.LEFT);
			causecodetvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_CAUSCD));
			causecodetvHeader.setTextColor(Color.WHITE);
			causecodetvHeader.setPadding(5,5,5,5);
			causecodetvHeader.setTypeface(null, Typeface.BOLD); 
			causecodetvHeader.setClickable(true);
			causecodetvHeader.setEnabled(false);		
			
			causedesctvHeader.setGravity(Gravity.LEFT);
			causedesctvHeader.setText(getString(R.string.SERVICEORD_FLTSCR_CAUSDESC));
			causedesctvHeader.setTextColor(Color.WHITE);
			causedesctvHeader.setPadding(5,5,5,5);
			causedesctvHeader.setTypeface(null, Typeface.BOLD); 
			causedesctvHeader.setClickable(true);
			causedesctvHeader.setEnabled(false);
			
			faulttiletr.addView(optionstvHeader);
			faulttiletr.addView(symgrptvHeader);
			faulttiletr.addView(symcodetvHeader);
			faulttiletr.addView(symdesctvHeader);
			faulttiletr.addView(pblgrptvHeader);
			faulttiletr.addView(pblcodetvHeader);
			faulttiletr.addView(pbldesctvHeader);
			faulttiletr.addView(causegrptvHeader);
			faulttiletr.addView(causecodetvHeader);
			faulttiletr.addView(causedesctvHeader);
			
			faulttl.addView(faulttiletr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			*/
			
			faulttl = (TableLayout)findViewById(R.id.faultmaintbllayout1);
			TableRow faulttr = new TableRow(this);
			faulttr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			linparams.leftMargin = 5; 
			linparams.rightMargin = 5; 
			linparams.gravity = Gravity.CENTER_VERTICAL;
			
	        SOFaultClass faultObj = null;
			for (int i =0; i < ServiceProConstants.faultColltVect.size(); i++) {
	        	faultObj = (SOFaultClass)ServiceProConstants.faultColltVect.elementAt(i);
	            actMatchStr = faultObj.getActivityStr().trim();                    
	            //ServiceProConstants.showLog("Act value "+i+" : "+actMatchStr);
	            
	            if(actValstr.equalsIgnoreCase(actMatchStr)){
	                symgrp = faultObj.getSymptomGroupStr();    
	    	        symcode = faultObj.getSymptomCodeStr();
	    	        symdesc = faultObj.getSymptomDescStr();
	    	        pblgrp = faultObj.getProblemGroupStr();
	    	        pblcode = faultObj.getProblemCodeStr();
	    	        pbldesc = faultObj.getProblemDescStr();
	    	        causegrp = faultObj.getCauseGroupStr();
	    	        causecode = faultObj.getCauseCodeStr();
	    	        causedesc = faultObj.getCauseDescStr();  
	            	        
	    	        faulttr = new TableRow(this);
			    	
	    	        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linear, null);
			        linearLayout.setPadding(0, 0, 0, 0);
			        
			        editbtn[i] = new ImageButton(this); 
			        editbtn[i].setId(i);
			        editbtn[i].setBackgroundResource(R.drawable.editpencil);
			        editbtn[i].setLayoutParams(linparams); 
			        editbtn[i].setOnClickListener(new View.OnClickListener() {	
						public void onClick(View v) {
							System.out.println("edit btn Id: "+v.getId());
							int check = v.getId();	
							selIndex = check;
							Intent fault_edit_intent = new Intent(ServiceProFaultScreen.this, ServiceProFaultEditScreen.class);				        		
			        		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
			                ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
			                fault_edit_intent.putExtra("docVectorObj", serVectObj1);
			                fault_edit_intent.putExtra("confVectorObj", serVectObj2);
			        		fault_edit_intent.putExtra("selIndex", selIndex);  
			        		fault_edit_intent.putExtra("editflag", true);
			        		fault_edit_intent.putExtra("actvObj", actvObj);
			            	startActivity(fault_edit_intent);							
						}
					});
			        /*
			        LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			        editparams.leftMargin = 5; 
			        editparams.rightMargin = 5; 
			        editparams.gravity = Gravity.CENTER_VERTICAL;
			        editbtn[i].setLayoutParams(editparams); 
			        */
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
			        /*
			        LinearLayout.LayoutParams delparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			        delparams.leftMargin = 5; 
			        delparams.rightMargin = 5; 
			        delparams.gravity = Gravity.CENTER_VERTICAL;
			        delbtn[i].setLayoutParams(delparams);   
			        */
			        linearLayout.addView(delbtn[i]);
			                      
			        /*optioncheckBox[i] = new CheckBox(this);
			        optioncheckBox[i].setId(i);
			        optioncheckBox[i].setLayoutParams(linparams); 
			        optioncheckBox[i].setOnClickListener(new View.OnClickListener() {	
						public void onClick(View v) {
							System.out.println("Id: "+v.getId());
							int check = v.getId();
							System.out.println("Checkbox checked : "+optioncheckBox[check].isChecked());
						}
					});
			        linearLayout.addView(optioncheckBox[i]);   */
			        /*
			        LinearLayout.LayoutParams optioncheckBoxparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			        optioncheckBoxparams.leftMargin = 5; 
			        optioncheckBoxparams.rightMargin = 5; 
			        optioncheckBoxparams.gravity = Gravity.CENTER_VERTICAL;
			        optioncheckBox[i].setLayoutParams(optioncheckBoxparams); 
			        */
			        
			        
					TextView symgrpTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView symcodeTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView symdescTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView pblgrpTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView pblcodeTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView pbldescTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView causegrpTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView causecodeTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
					TextView causedescTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);

					symgrpTxtView.setText(symgrp);
					symgrpTxtView.setGravity(Gravity.LEFT);
					symgrpTxtView.setWidth(headerWidth2);
					symgrpTxtView.setPadding(10,0,0,0);
					
					symcodeTxtView.setText(symcode);
					symcodeTxtView.setGravity(Gravity.LEFT);
					symcodeTxtView.setWidth(headerWidth3);
					symcodeTxtView.setPadding(10,0,0,0);

					symdescTxtView.setText(symdesc);
					symdescTxtView.setGravity(Gravity.LEFT);
					symdescTxtView.setWidth(headerWidth4);
					symdescTxtView.setPadding(10,0,0,0);

					pblgrpTxtView.setText(pblgrp);
					pblgrpTxtView.setGravity(Gravity.LEFT);
					pblgrpTxtView.setWidth(headerWidth5);
					pblgrpTxtView.setPadding(10,0,0,0);

					pblcodeTxtView.setText(pblcode);
					pblcodeTxtView.setGravity(Gravity.LEFT);
					pblcodeTxtView.setWidth(headerWidth6);
					pblcodeTxtView.setPadding(10,0,0,0);

					pbldescTxtView.setText(pbldesc);
					pbldescTxtView.setGravity(Gravity.LEFT);
					pbldescTxtView.setWidth(headerWidth7);
					pbldescTxtView.setPadding(10,0,0,0);

					causegrpTxtView.setText(causegrp);
					causegrpTxtView.setGravity(Gravity.LEFT);
					causegrpTxtView.setWidth(headerWidth8);
					causegrpTxtView.setPadding(10,0,0,0);

					causecodeTxtView.setText(causecode);
					causecodeTxtView.setGravity(Gravity.LEFT);
					causecodeTxtView.setWidth(headerWidth9);
					causecodeTxtView.setPadding(10,0,0,0);

					causedescTxtView.setText(causedesc);
					causedescTxtView.setGravity(Gravity.LEFT);
					causedescTxtView.setWidth(headerWidth10);
					causedescTxtView.setPadding(10,0,0,0);
					
					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
						symgrpTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						symcodeTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						symdescTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						pblgrpTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						pblcodeTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						pbldescTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						causegrpTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						causecodeTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
						causedescTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					}
					
					faulttr.addView(linearLayout);
					faulttr.addView(symgrpTxtView);
					faulttr.addView(symcodeTxtView);
					faulttr.addView(symdescTxtView);
					
					faulttr.addView(pblgrpTxtView);
					faulttr.addView(pblcodeTxtView);
					faulttr.addView(pbldescTxtView);
					
					faulttr.addView(causegrpTxtView);
					faulttr.addView(causecodeTxtView);
					faulttr.addView(causedescTxtView);
					
					if(i%2 == 0)
						faulttr.setBackgroundResource(R.color.item_even_color);
		            else
		            	faulttr.setBackgroundResource(R.color.item_odd_color);
					
					faulttl.addView(faulttr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
					faultObj = null;
	            }
			}
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in displayFaultRows : "+de.toString());
	    }
	}//fn displayFaultRows
	
	public String getActivityValue(){
        String actStr = "";
        try{
            if(actvObj != null){
                actStr = actvObj.getNumberExtnStr().trim();
            }            
            System.out.println("actStr value : "+actStr);
        }
        catch(Exception safg){
        	ServiceProConstants.showErrorLog("Error in getActivityValue : "+safg.toString());
        }
        return actStr;
    }//fn getActivityValue
	
	//activity btn click listener
	private OnClickListener activity_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {	
			finish();
        }
    };
	
   //add btn click listener
    private OnClickListener addbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	Intent faultedit_intent = new Intent(ServiceProFaultScreen.this, ServiceProFaultEditScreen.class);
		        ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	            faultedit_intent.putExtra("docVectorObj", serVectObj1);
	            faultedit_intent.putExtra("confVectorObj", serVectObj2);
		        faultedit_intent.putExtra("selIndex", -1);  
		        faultedit_intent.putExtra("editflag", false);
		        faultedit_intent.putExtra("actvObj", actvObj);
		    	startActivity(faultedit_intent);
	        }
			catch (Exception de) {
		    	ServiceProConstants.showErrorLog("Error in addbtnListener : "+de.toString());
		    }
        }
    };
    
	//spares btn click listener
	private OnClickListener spares_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	            Intent spares_intent = new Intent(ServiceProFaultScreen.this, ServiceProSparesScreen.class);
	            spares_intent.putExtra("docVectorObj", serVectObj1);
	            spares_intent.putExtra("confVectorObj", serVectObj2);        	
	        	startActivity(spares_intent);
	        	finish();
	        }
			catch (Exception de) {
		    	ServiceProConstants.showErrorLog("Error in spares_btListener : "+de.toString());
		    }
        }
    };
    
    public void clickAction(final int position){
    	try{
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(getString(R.string.SERVICEORD_COMMON_DG_DELTHIS));
	        builder.setPositiveButton(R.string.SERVICEPRO_OK, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	faultDeleteAction(position);
	            }
	        });                
	        builder.setNegativeButton(R.string.SERVICEPRO_CANCEL, null);
	        builder.show();
	    }
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in clickAction : "+de.toString());
	    }
    }//fn clickAction
    
    private void faultDeleteAction(int index){
        try{            
            if(ServiceProConstants.faultColltVect != null){    
                if(ServiceProConstants.faultColltVect.size() > index){
                	ServiceProConstants.faultColltVect.removeElementAt(index);
                	faulttl.removeAllViews();
                	displayFaultRows();
                }
            }
        }
        catch(Exception dsgf){
        	ServiceProConstants.showErrorLog("Error in activityDeleteAction : "+dsgf.toString());
        }
    }//fn activityDeleteAction
    
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        menu.add(0, MAIN_ID, 0, "Add new Fault");
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
		        	Intent faultedit_intent = new Intent(ServiceProFaultScreen.this, ServiceProFaultEditScreen.class);
			        ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
		            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
		            faultedit_intent.putExtra("docVectorObj", serVectObj1);
		            faultedit_intent.putExtra("confVectorObj", serVectObj2);
			        faultedit_intent.putExtra("selIndex", -1);  
			        faultedit_intent.putExtra("editflag", false);
			        faultedit_intent.putExtra("actvObj", actvObj);
			    	startActivity(faultedit_intent);
		        }
				catch (Exception de) {
			    	ServiceProConstants.showErrorLog("Error in addbtnListener : "+de.toString());
			    }
		        break;
			}
	    }
		return super.onOptionsItemSelected(item);
	}
	
}//End of class ServiceProFaultScreen