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
import com.globalsoft.ServicePro.Constraints.ServiceFollDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Item.SOSparesClass;

public class ServiceProSparesScreen extends Activity {
	public TextView customer_address, customer_duedate, customer_so;
	private ImageButton[] editbtn, delbtn;
	private CheckBox[] optioncheckBox;		
	private String SrcdocObjIdStr = "", SrcdocPrcsTypeStr = "", NumberExtStr = "";
    private ServiceProVectSerializable serObj1, serObj2;
	private Vector documentsVect, confVector;
	private ServiceOrdDocOpConstraints docsCategory = null;
	//private Button activitybtn;
	private ImageButton activitybtn;
	private int selIndex = -1;
	private ImageButton addbtn;
	private TableLayout materialtl;
	
	private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5, tableHeaderTV6;
    private int dispwidth = 300;
    private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5, headerWidth6;
	
	private static final int MAIN_ID = Menu.FIRST;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
		ServiceProConstants.setWindowTitleTheme(this);
		setContentView(R.layout.sparesscreen);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
		
		ServiceProConstants.setWindowTitleTheme(this);   
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.sparesscreen); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SAPTASK_SPARESCR_TITLE));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
		
		try{
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			activitybtn = (ImageButton)findViewById(R.id.activitybtn); 
	        activitybtn.setOnClickListener(activity_btListener);
	        
			serObj1 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("docVectorObj");	
		    serObj2 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("confVectorObj");
		    
		    if(serObj1 != null){
		    	documentsVect = serObj1.getVector();
		    	ServiceProConstants.showLog("docVector size : "+documentsVect.size());
		    }
		    
		    if(serObj2 != null){
		    	confVector = serObj2.getVector();
		    	ServiceProConstants.showLog("confVector size : "+confVector.size());
		    }	
		    
		    if(ServiceProConstants.spares_recall_flag == true){
		    	checkSparesAvailability();
        		ServiceProConstants.spares_recall_flag = false;
        	}		    
		    
		    //setBannerHeaderValues
		    customer_address = (TextView) findViewById(R.id.customer_address);
		    customer_duedate = (TextView) findViewById(R.id.customer_duedate);
		    customer_so = (TextView) findViewById(R.id.customer_so);
		    try{
	            if(documentsVect != null){
	                if(documentsVect.size() > 0){
	                    docsCategory = (ServiceOrdDocOpConstraints)documentsVect.elementAt(0);
	                    if(docsCategory != null){
	                        SrcdocObjIdStr = docsCategory.getObjectId().trim();
	                        SrcdocPrcsTypeStr = docsCategory.getProcessType().trim();
	                        NumberExtStr = docsCategory.getNumberExt().trim();
	                        docsCategory.getProductId().trim();
	                        System.out.println("Values : "+docsCategory.getRefObjProductId()+" : "+SrcdocObjIdStr+" : "+NumberExtStr+" : "+SrcdocPrcsTypeStr);
	                        
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
	                        customer_so.setText(" "+SrcdocObjIdStr+" "+ServiceProConstants.PRODUCT_DESC);
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
		    TableLayout tl = (TableLayout)findViewById(R.id.middletbllayout1);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));		
		    int count = 0;
		    	    
		    for(int j=0; j<documentsVect.size(); j++){
	            ServiceOrdDocOpConstraints docsMainCategory = (ServiceOrdDocOpConstraints)documentsVect.elementAt(j);
	            if(docsMainCategory != null){
	            	String serialNos = docsMainCategory.getSerialNumber();
	                String[] serialNoArr = null;
	                serialNoArr = serialNos.split(",");
	                String productStr = docsMainCategory.getRefObjProductId();
	                String[] productArr = null;
	                productArr = productStr.split(",");
	                count = 0;
	                
	                tr = new TableRow(this);	        
	                TextView tv1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	    			TextView tv2 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	    			TextView tv3 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	    			TextView tv4 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                               
	                for(count=0; count<serialNoArr.length; count++){
	                    if(count == 0){                     
	                        tv1.setGravity(Gravity.LEFT);
	                        tv1.setText(""+docsMainCategory.getNumberExt());
	                        tv1.setTypeface(null, Typeface.NORMAL);
	                        tv1.setPadding(5,5,5,5);
	                        
	                        tv2.setGravity(Gravity.LEFT);
	                        tv2.setText(ServiceProConstants.PRODUCT_DESC+ " " +docsMainCategory.getProductId());
	                        tv2.setTypeface(null, Typeface.NORMAL);
	                        tv2.setPadding(5,5,5,5);
	                        
	                        if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
 	                        	tv1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
 	                        	tv2.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                        }
	                        
	                        tr.addView(tv1);
	                        tr.addView(tv2);
	                        try{
	                        	System.out.println("serialNoArr[count]: "+serialNoArr[count]);
	                        	System.out.println("productArr[count]: "+productArr[count]);
	                            tv3.setGravity(Gravity.LEFT);
	                            tv3.setText(""+serialNoArr[count]);
	                            tv3.setTypeface(null, Typeface.NORMAL);
	                            tv3.setPadding(5,5,5,5);
	                            
	                            tv4.setGravity(Gravity.LEFT);
	                            tv4.setText(""+productArr[count]);
	                            tv4.setTypeface(null, Typeface.NORMAL);
	                            tv4.setPadding(5,5,5,5);
	                            
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
	                             tv3.setTypeface(null, Typeface.NORMAL);
	                             tv3.setPadding(5,5,5,5);
	                             
	                             tv4.setGravity(Gravity.LEFT);
	                             tv4.setText(""+productArr[count]);
	                             tv4.setTypeface(null, Typeface.NORMAL);
	                             tv4.setPadding(5,5,5,5);
	                             
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
	
			editbtn = new ImageButton[ServiceProConstants.sparesColltVect.size()];
			delbtn = new ImageButton[ServiceProConstants.sparesColltVect.size()];
			//optioncheckBox = new CheckBox[ServiceProConstants.sparesColltVect.size()];
	                
	        if(ServiceProConstants.sparesColltVect.size() == 0){
	        	finish();
	        	Intent spares_edit_intent = new Intent(ServiceProSparesScreen.this, ServiceProSparesEditScreen.class);				        		
	    		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	            spares_edit_intent.putExtra("docVectorObj", serVectObj1);
	            spares_edit_intent.putExtra("confVectorObj", serVectObj2);
	            spares_edit_intent.putExtra("selIndex", -1);  
	            spares_edit_intent.putExtra("editflag", false);
	        	startActivity(spares_edit_intent);
	        } 
	        
	        laySparesHeader();
	        //displaySparesRows();
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in oncreate : "+de.toString());
	    }
	}
	
	private void laySparesHeader(){
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
	                displaySparesRows();
	            }
	        });	        	        
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in laySparesHeader : "+sfg.toString());
		}
	}//fn laySparesHeader
	
	private void displaySparesRows(){
		String matid = "", matdesc = "", qty = "", unit = "", othrunit = "", serial = "";     
		try{
	        SOSparesClass spareObj = null;
			
			/*
			TableRow materialtiletr = (TableRow) getLayoutInflater().inflate(R.layout.template_tr, null);
			materialtiletr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	                
			TextView optionstvHeader = (TextView) getLayoutInflater().inflate(R.layout.template_tblheadtv, null);
	        TextView matidtvHeader = (TextView) getLayoutInflater().inflate(R.layout.template_tblheadtv, null);
			TextView matdesctvHeader = (TextView) getLayoutInflater().inflate(R.layout.template_tblheadtv, null);
			TextView qtytvHeader = (TextView) getLayoutInflater().inflate(R.layout.template_tblheadtv, null);
			TextView unittvHeader = (TextView) getLayoutInflater().inflate(R.layout.template_tblheadtv, null);
			TextView serialtvHeader = (TextView) getLayoutInflater().inflate(R.layout.template_tblheadtv, null);
			
			optionstvHeader.setGravity(Gravity.CENTER);
			optionstvHeader.setText(getString(R.string.SERVICEPRO_OPTIONS));
			optionstvHeader.setPadding(5,5,5,5);
			optionstvHeader.setTypeface(null, Typeface.BOLD); 
			optionstvHeader.setClickable(true);
			optionstvHeader.setEnabled(false);
			
			matidtvHeader.setGravity(Gravity.LEFT);
			matidtvHeader.setText(getString(R.string.SERVICEORD_MATSCR_LBL_MATID));
			matidtvHeader.setPadding(5,5,5,5);
			matidtvHeader.setTypeface(null, Typeface.BOLD); 
			matidtvHeader.setClickable(true);
			matidtvHeader.setEnabled(false);
			
			matdesctvHeader.setGravity(Gravity.LEFT);
			matdesctvHeader.setText(getString(R.string.SERVICEORD_MATSCR_LBL_MATDESC));
			matdesctvHeader.setPadding(5,5,5,5);
			matdesctvHeader.setTypeface(null, Typeface.BOLD); 
			matdesctvHeader.setClickable(true);
			matdesctvHeader.setEnabled(false);
			
			qtytvHeader.setGravity(Gravity.LEFT);
			qtytvHeader.setText(getString(R.string.SERVICEORD_MATSCR_LBL_QTY));
			qtytvHeader.setPadding(5,5,5,5);
			qtytvHeader.setTypeface(null, Typeface.BOLD); 
			qtytvHeader.setClickable(true);
			qtytvHeader.setEnabled(false);
			
			unittvHeader.setGravity(Gravity.LEFT);
			unittvHeader.setText(getString(R.string.SERVICEORD_MATSCR_LBL_UNIT));
			unittvHeader.setPadding(5,5,5,5); 
			unittvHeader.setClickable(true);
			unittvHeader.setEnabled(false);
			
			serialtvHeader.setGravity(Gravity.LEFT);
			serialtvHeader.setText(getString(R.string.SERVICEORD_MATSCR_LBL_SERL));
			serialtvHeader.setPadding(5,5,5,5);
			serialtvHeader.setTypeface(null, Typeface.BOLD); 
			serialtvHeader.setClickable(true);
			serialtvHeader.setEnabled(false);
			
			materialtiletr.addView(optionstvHeader);
			materialtiletr.addView(matidtvHeader);
			materialtiletr.addView(matdesctvHeader);
			materialtiletr.addView(qtytvHeader);
			materialtiletr.addView(unittvHeader);
			materialtiletr.addView(serialtvHeader);
			
			materialtl.addView(materialtiletr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			*/
			
			materialtl = (TableLayout)findViewById(R.id.sparesmaintbllayout1);
			TableRow materialtr = new TableRow(this);
			materialtr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			linparams.leftMargin = 5; 
			linparams.rightMargin = 5; 
			linparams.gravity = Gravity.CENTER_VERTICAL;
				
			for (int i =0; i < ServiceProConstants.sparesColltVect.size(); i++) {
	        	spareObj = (SOSparesClass)ServiceProConstants.sparesColltVect.elementAt(i);
		        matid = spareObj.getMaterialStr();    
		        matdesc = spareObj.getMaterialDescStr();
		        qty = spareObj.getQuantityStr();
		        unit = spareObj.getUnitsStr();
		        othrunit = "";
		        serial = spareObj.getSerialNoStr();
		        
		        materialtr = new TableRow(this);
		    	
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
						Intent spares_edit_intent = new Intent(ServiceProSparesScreen.this, ServiceProSparesEditScreen.class);				        		
		        		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
		                ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
		                spares_edit_intent.putExtra("docVectorObj", serVectObj1);
		                spares_edit_intent.putExtra("confVectorObj", serVectObj2);
		                spares_edit_intent.putExtra("selIndex", selIndex);  
		                spares_edit_intent.putExtra("editflag", true);
		            	startActivity(spares_edit_intent);						
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
						/*if(optioncheckBox[check].isChecked()){
							System.out.println("Call btn click action here..");									
							for(int k1=0; k1<optioncheckBox.length; k1++){
			                    if(optioncheckBox[k1] != null){
			                        if(optioncheckBox[k1].isChecked() == true){
			                        	selIndex = k1;
			                            break;
			                        }
			                    }
			                }
			        		if(selIndex >= 0){	
			        			int chkbokclicksize = 0;
			    	        	for(int k1=0; k1<optioncheckBox.length; k1++){
			                        if(optioncheckBox[k1] != null){
			                            if(optioncheckBox[k1].isChecked() == true){
			                            	chkbokclicksize++;
			                            }
			                        }
			                    }
			    	        	if(chkbokclicksize == 1){
			    	        		clickAction(selIndex);
				        		}
					        	else{
					        		ServiceProConstants.showErrorDialog(ServiceProSparesScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELMULCHEHBOXACTVY));
					        	}
			        		}
			        		else{
			        			ServiceProConstants.showErrorDialog(ServiceProSparesScreen.this, getString(R.string.SERVICEORD_COMMON_DG_SELACTVY));
			        		}	
						}
						else{
							System.out.println("Action disable");
						}*/
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
		        optioncheckBox[i].setId(0);
		        optioncheckBox[i].setLayoutParams(linparams); 
		        optioncheckBox[i].setOnClickListener(new View.OnClickListener() {	
					public void onClick(View v) {
						System.out.println("Id: "+v.getId());
						int check = v.getId();
						System.out.println("Checkbox checked : "+optioncheckBox[check].isChecked());
					}
				});
		        linearLayout.addView(optioncheckBox[i]);*/
		        /*
		        LinearLayout.LayoutParams optioncheckBoxparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
		        optioncheckBoxparams.leftMargin = 5; 
		        optioncheckBoxparams.rightMargin = 5; 
		        optioncheckBoxparams.gravity = Gravity.CENTER_VERTICAL;
		        optioncheckBox[i].setLayoutParams(optioncheckBoxparams); 
		        */
		        
		               
				TextView matidTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView matdescTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView qtyTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView unitTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				//TextView othrunitTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
				TextView serialTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);

				matidTxtView.setText(matid);
				matidTxtView.setGravity(Gravity.LEFT);
				matidTxtView.setWidth(headerWidth2);
				matidTxtView.setPadding(10,0,0,0);
				//matidTxtView.setTypeface(null, Typeface.NORMAL);
					
				matdescTxtView.setText(matdesc);
				matdescTxtView.setGravity(Gravity.LEFT); 
				matdescTxtView.setWidth(headerWidth3);
				matdescTxtView.setPadding(10,0,0,0);
				//matdescTxtView.setTypeface(null, Typeface.NORMAL);

				qtyTxtView.setText(qty);
				qtyTxtView.setGravity(Gravity.LEFT);
				qtyTxtView.setWidth(headerWidth4);
				qtyTxtView.setPadding(10,0,0,0);
				//qtyTxtView.setTypeface(null, Typeface.NORMAL);

				unitTxtView.setText(unit+othrunit);
				unitTxtView.setGravity(Gravity.LEFT);
				unitTxtView.setWidth(headerWidth5);
				unitTxtView.setPadding(10,0,0,0);
				//unitTxtView.setTypeface(null, Typeface.NORMAL);
				
				/*
				othrunitTxtView.setText(othrunit);
				othrunitTxtView.setGravity(Gravity.LEFT);
				othrunitTxtView.setWidth(headerWidth6);
				othrunitTxtView.setPadding(10,0,0,0);
				//othrunitTxtView.setTypeface(null, Typeface.NORMAL);
			 	*/
				serialTxtView.setText(serial);
				serialTxtView.setGravity(Gravity.LEFT);
				serialTxtView.setWidth(headerWidth6);
				serialTxtView.setPadding(10,0,0,0);
				//serialTxtView.setTypeface(null, Typeface.NORMAL);
						
				if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
					matidTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					matdescTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					qtyTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					unitTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
					serialTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
				}
				
				materialtr.addView(linearLayout);
				materialtr.addView(matidTxtView);
				materialtr.addView(matdescTxtView);
				materialtr.addView(qtyTxtView);		
				materialtr.addView(unitTxtView);
				materialtr.addView(serialTxtView);
						
				if(i%2 == 0)
					materialtr.setBackgroundResource(R.color.item_even_color);
	            else
	            	materialtr.setBackgroundResource(R.color.item_odd_color);
				
				materialtl.addView(materialtr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			}
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in displaySparesRows : "+de.toString());
	    }
	}//fn displaySparesRows
	
	//activity btn click listener
	private OnClickListener activity_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {	
        	try{
	        	finish();
				ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
				ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	             
	            Intent activity_intent = new Intent(ServiceProSparesScreen.this, ServiceProActivityMainScreen.class);
	            activity_intent.putExtra("docVectorObj", serVectObj1);
	            activity_intent.putExtra("confVectorObj", serVectObj2);
	         	activity_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);    
				startActivity(activity_intent);
	        }
			catch (Exception de) {
		    	ServiceProConstants.showErrorLog("Error in activity_btListener : "+de.toString());
		    }
        }
    };
    
    //add btn click listener
    private OnClickListener addbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	Intent spares_intent = new Intent(ServiceProSparesScreen.this, ServiceProSparesEditScreen.class);				        		
	    		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	            spares_intent.putExtra("docVectorObj", serVectObj1);
	            spares_intent.putExtra("confVectorObj", serVectObj2);
	            spares_intent.putExtra("selIndex", -1);  
	            spares_intent.putExtra("editflag", false);
	        	startActivity(spares_intent);
	        }
			catch (Exception de) {
		    	ServiceProConstants.showErrorLog("Error in addbtnListener : "+de.toString());
		    }
        }
    };
	
    public void clickAction(final int position){
    	try{
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(R.string.SERVICEORD_COMMON_DG_DELTHIS);
	        builder.setPositiveButton(R.string.SERVICEPRO_OK, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	spareDeleteAction(position);
	            }
	        });                
	        builder.setNegativeButton(R.string.SERVICEPRO_CANCEL, null);
	        builder.show();
	    }
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in clickAction : "+de.toString());
	    }
    }//fn clickAction
    
    private void spareDeleteAction(int index){
        try{            
        	if(ServiceProConstants.sparesColltVect != null){    
                if(ServiceProConstants.sparesColltVect.size() > index){
                	ServiceProConstants.sparesColltVect.removeElementAt(index);
                	materialtl.removeAllViews();
                	displaySparesRows();
                }
            }
        }
        catch(Exception dsgf){
        	ServiceProConstants.showErrorLog("Error in activityDeleteAction : "+dsgf.toString());
        }
    }//fn activityDeleteAction
    
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        menu.add(0, MAIN_ID, 0, "Add new Spare");
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
		        	Intent spares_intent = new Intent(ServiceProSparesScreen.this, ServiceProSparesEditScreen.class);				        		
		    		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
		            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
		            spares_intent.putExtra("docVectorObj", serVectObj1);
		            spares_intent.putExtra("confVectorObj", serVectObj2);
		            spares_intent.putExtra("selIndex", -1);  
		            spares_intent.putExtra("editflag", false);
		        	startActivity(spares_intent);
		        }
				catch (Exception de) {
			    	ServiceProConstants.showErrorLog("Error in addbtnListener : "+de.toString());
			    }
		        break;
			}
	    }
		return super.onOptionsItemSelected(item);
	}  

	private void checkSparesAvailability(){
        try{
            ServiceOrdDocOpConstraints docsMainCategory = null;
            SOSparesClass spareObj = null;
            ServiceFollDocOpConstraints confirmMainCategory = null;
            String numberMainExt = "", numberSubExt = "";   
            boolean matchFlag = false;
            if(ServiceProConstants.sparesCollectionVect != null){
                for(int k2=0; k2<ServiceProConstants.sparesCollectionVect.size(); k2++){
                    docsMainCategory = (ServiceOrdDocOpConstraints)ServiceProConstants.sparesCollectionVect.elementAt(k2);
                    if(docsMainCategory != null){
                        numberMainExt = "";
                        matchFlag = false;
                        numberMainExt = docsMainCategory.getNumberExt().trim();
                        System.out.println("numberMainExt : "+numberMainExt+" : "+ServiceProConstants.confirmCollectionVect.size());
                        
                        if(ServiceProConstants.confirmCollectionVect != null){
                            for(int j=0; j<ServiceProConstants.confirmCollectionVect.size(); j++){
                                confirmMainCategory = (ServiceFollDocOpConstraints)ServiceProConstants.confirmCollectionVect.elementAt(j);
                                numberSubExt = "";
                                numberSubExt = confirmMainCategory.getSRCDocNumberExt().trim();
                                System.out.println("Matching : "+numberMainExt+" : "+numberSubExt);
                                if(numberMainExt.equalsIgnoreCase(numberSubExt)){
                                    matchFlag = true;
                                }
                            }
                            
                            if(matchFlag == false){
                                spareObj = new SOSparesClass();
                                spareObj.setMaterialDescStr(docsMainCategory.getZZItemDesc());
                                spareObj.setMaterialStr(docsMainCategory.getProductId());
                                spareObj.setQuantityStr(docsMainCategory.getQuantity());
                                spareObj.setSerialNoStr(docsMainCategory.getSerialNumber());
                                spareObj.setUnitsStr(docsMainCategory.getProcessQtyUnit());    
                                spareObj.setNumberExtStr(docsMainCategory.getNumberExt());                
                                
                                ServiceProConstants.sparesColltVect.addElement(spareObj);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception sfrg){
        	ServiceProConstants.showErrorLog("Error in checkSparesAvailability : "+sfrg.toString());
        }
    }//fn checkSparesAvailability
	    
}//End of class ServiceProSparesScreen
