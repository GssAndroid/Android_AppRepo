package com.globalsoft.ServicePro;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.SOCodeGroupOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOCodeListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOMattEmpListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOServiceActListOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceFollDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Database.ServiceProDBOperations;

public class ServiceProConfirmationScreen extends Activity {
	
	private ServiceProOutputConstraints category = null;	
	private String statusOrderNoStr = "", processTypeStr = "", processTypeDescStr="";        
    private ArrayList documentsList = new ArrayList();
    private Vector gridConfirmItemMainVect = new Vector();  
    private TextView customerTV, duedateTV, serviceordTV;    
	private CheckBox[] checkField;
	private Button addConfirmBtn;	
	private int dispwidth = 300;	
	private static final int MAIN_ID = Menu.FIRST;
	private TableLayout maintbl;
	private TextView tableHeaderTV1, tableHeaderTV2, tableHeaderTV3, tableHeaderTV4, tableHeaderTV5;
	private int headerWidth1, headerWidth2, headerWidth3, headerWidth4, headerWidth5;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			//requestWindowFeature(Window.FEATURE_LEFT_ICON);
			ServiceProConstants.setWindowTitleTheme(this);
			/*setContentView(R.layout.confirmmain);		
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.confirmmain); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SAPTASK_CONFSCR_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			boolean value = this.getIntent().getBooleanExtra("refresh", false);
  			if(value){
  				Intent intent = new Intent(this, ServiceProTaskEditScreen.class);
  				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
  				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);			
  				intent.putExtra("refresh", true);
  				startActivity(intent);
    			finish();
  			}else{
  				category = (ServiceProOutputConstraints)this.getIntent().getSerializableExtra("conftaskobj");  				
  				if(category != null){
  					statusOrderNoStr = category.getObjectId().toString().trim();
  		            processTypeStr = category.getProcessType().toString();
  		            processTypeStr = processTypeStr.trim();
  		            processTypeDescStr = category.getProcessTypeDescr().toString().trim();
  				}
  				
  				addConfirmBtn = (Button) findViewById(R.id.addConfirmBtn);
  				addConfirmBtn.setOnClickListener(addConfirmBtnListener);  
  				maintbl = (TableLayout)findViewById(R.id.actymaintbllayout1);			
  				
  				addConfirmBtn = (Button) findViewById(R.id.addConfirmBtn);
  				addConfirmBtn.setOnClickListener(addConfirmBtnListener);  
  				
  				customerTV = (TextView)findViewById(R.id.customerTV);			
  				duedateTV = (TextView)findViewById(R.id.duedateTV);			
  				serviceordTV =(TextView)findViewById(R.id.serviceordTV);
  				getLDBConfList();
  			}
		}
		catch (Exception de) {
        	ServiceProConstants.showErrorLog("Error in Oncreate : "+de.toString());
        }
	}
	
	private void initLayout(){
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
	                drawLayout();
	            }
	        });
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in layActivityHeader : "+sfg.toString());
		}
	}//fn layActivityHeader

	private void drawLayout(){
		try {
			ServiceOrdDocOpConstraints docsCategory = null;
	        ServiceFollDocOpConstraints followCategory = null;
	        //ServiceProConstants.showLog("documentsList.size: "+documentsList.size());
	        try{
	            if(documentsList != null){
	                if(documentsList.size() > 0){
	                    docsCategory = (ServiceOrdDocOpConstraints)documentsList.get(0);
	                    if(docsCategory != null){
	                    	String cust = docsCategory.getNameOrg1().toString()+", "+docsCategory.getCity().toString();
	                    	String sord = docsCategory.getObjectId().toString()+" "+processTypeDescStr;
	                    	customerTV.setText(" "+cust);
	                    	String dateStr = docsCategory.getZZKEeyDate().toString();
	                    	dateStr = dateStr.trim();
	                    	if(!dateStr.equalsIgnoreCase("")){	                    		
		                    	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
	                			duedateTV.setText(" "+newdatestr);
	                    	}
	                    	serviceordTV.setText(" "+sord);
	                    }
	                }
	            }
	        }
	        catch(Exception asf){
	        	ServiceProConstants.showErrorLog("Error in initLayout 1 : "+asf.toString());
	        }	        
	       
	        getGridConfirmVectorsForDisplay();	        
	        checkField = new CheckBox[documentsList.size()];
            int count=0;
	        
        	TableLayout tl = (TableLayout)findViewById(R.id.confirmationTL);
        	maintbl = (TableLayout)findViewById(R.id.actymaintbllayout1);
        	TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            for(int j=0; j<documentsList.size(); j++){
                 ServiceOrdDocOpConstraints docsMainCategory = (ServiceOrdDocOpConstraints)documentsList.get(j);
                 if(docsMainCategory != null){
                     String serialNos = docsMainCategory.getSerialNumber();
                     String[] serialNoArr = null;
                     serialNoArr = serialNos.split(",");
                     String productStr = docsMainCategory.getZZItemDesc().toString().trim();
                     String[] productArr = null;
                     productArr = productStr.split(",");
                     count = 0;                     
                     for(count=0; count<serialNoArr.length; count++){
                    	 tr = new TableRow(this);                    	 
                    	 /*ServiceProConstants.showLog("numberExtTxtView: "+docsMainCategory.getNumberExt().toString());
                    	 ServiceProConstants.showLog("prdIdTxtView: "+docsMainCategory.getProductId().toString());
                    	 ServiceProConstants.showLog("prdNoTxtView: "+productArr[count]);
                    	 ServiceProConstants.showLog("serialNoTxtView: "+serialNoArr[count]);*/
                    	 
                    	 TextView numberExtTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                         numberExtTxtView.setGravity(Gravity.LEFT);
                         numberExtTxtView.setWidth(headerWidth2);
                         numberExtTxtView.setPadding(10,0,0,0);
                         
                         TextView prdNoTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                         prdNoTxtView.setGravity(Gravity.LEFT); 
                         prdNoTxtView.setWidth(headerWidth3);
                         prdNoTxtView.setPadding(10,0,0,0);
                         
                         TextView prdIdTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                         prdIdTxtView.setGravity(Gravity.LEFT); 
                         prdIdTxtView.setWidth(headerWidth4);
                         prdIdTxtView.setPadding(10,0,0,0); 
                         
                         TextView serialNoTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                         serialNoTxtView.setGravity(Gravity.LEFT);
                         serialNoTxtView.setWidth(headerWidth5);
                         serialNoTxtView.setPadding(10,0,0,0);	
                         
                         TextView checkLblTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                         checkLblTxtView.setGravity(Gravity.LEFT);
                         checkLblTxtView.setWidth(headerWidth1);
                         checkLblTxtView.setPadding(10,0,0,0);
                          	
                         if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
                        	 checkLblTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                        	 numberExtTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                        	 prdIdTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                        	 prdNoTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                        	 serialNoTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
     					}
                         
                         if(count == 0){
                             checkField[j] = new CheckBox(this);
                             checkField[j].setPadding(10,0,0,0);
                             checkField[j].setWidth(headerWidth1);
                             
                             numberExtTxtView.setText(docsMainCategory.getNumberExt().toString());
                             prdIdTxtView.setText(docsMainCategory.getProductId().toString());
                             try{
                            	 prdNoTxtView.setText(productArr[count]);
                            	 serialNoTxtView.setText(serialNoArr[count]);
                             }
                             catch(Exception df){
                            	 ServiceProConstants.showErrorLog("Error in Count == 0 : "+df.toString());
                             }
							
                             tr.addView(checkField[j]);
                             tr.addView(numberExtTxtView);
                             tr.addView(prdNoTxtView);
                             tr.addView(prdIdTxtView);
                             tr.addView(serialNoTxtView);
                         }
                         else{
                             try{
                            	 checkLblTxtView.setText("");
                            	 numberExtTxtView.setText("");
                            	 prdIdTxtView.setText("");         
                            	 prdNoTxtView.setText(serialNoArr[count]);
                            	 serialNoTxtView.setText(productArr[count]);
                            	 
                            	 tr.addView(checkLblTxtView);
                                 tr.addView(numberExtTxtView);
                                 tr.addView(prdNoTxtView);
                                 tr.addView(prdIdTxtView);
                                 tr.addView(serialNoTxtView);
                             }
                             catch(Exception df1){
                            	 ServiceProConstants.showErrorLog("Error in Count != 0 : "+df1.toString());
                             }
                         }                         
                         maintbl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                     }
                 }                 
                
                 try{
                     if(gridConfirmItemMainVect != null){
                         if(gridConfirmItemMainVect.size() > j){
                             Vector confirmSubVect = (Vector)gridConfirmItemMainVect.elementAt(j);
                             if(confirmSubVect != null){
                                 for(int c2=0; c2<confirmSubVect.size(); c2++){
                                     ServiceFollDocOpConstraints confirmMainCategory = (ServiceFollDocOpConstraints)confirmSubVect.elementAt(c2);
                                     if(confirmMainCategory != null){
                                    	 tr = new TableRow(this);
                                    	 
                                    	 TextView numberExtTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                                         numberExtTxtView.setGravity(Gravity.LEFT);
                                         numberExtTxtView.setWidth(headerWidth2);
                                         numberExtTxtView.setPadding(10,0,0,0);
                                    	 
                                         TextView prdIdTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                                         prdIdTxtView.setGravity(Gravity.LEFT); 
                                         prdIdTxtView.setWidth(headerWidth3);
                                         prdIdTxtView.setPadding(10,0,0,0); 
                                         
                                         TextView prdNoTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                                         prdNoTxtView.setGravity(Gravity.LEFT); 
                                         prdNoTxtView.setWidth(headerWidth4);
                                         prdNoTxtView.setPadding(10,0,0,0);
                                         
                                         TextView serialNoTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                                         serialNoTxtView.setGravity(Gravity.LEFT);
                                         serialNoTxtView.setWidth(headerWidth5);
                                         serialNoTxtView.setPadding(10,0,0,0);	
                                         
                                         TextView checkLblTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                                         checkLblTxtView.setGravity(Gravity.LEFT);
                                         checkLblTxtView.setPadding(10,0,0,0);
                                         checkLblTxtView.setWidth(headerWidth1);
                                         
                                         
                                    	 checkLblTxtView.setText("");
                                    	 numberExtTxtView.setText(getString(R.string.SERVICEORD_CNFSCR_LBL_CONF));
                                    	 prdIdTxtView.setText(confirmMainCategory.getObjectId().toString());         
                                    	 prdNoTxtView.setText(confirmMainCategory.getStatus().toString());
                                    	 serialNoTxtView.setText(confirmMainCategory.getERDate().toString());
                                    	 
                                    	 numberExtTxtView.setTypeface(null, Typeface.NORMAL); 
                                    	 prdIdTxtView.setTypeface(null, Typeface.NORMAL); 
                                    	 prdNoTxtView.setTypeface(null, Typeface.NORMAL); 
                                    	 serialNoTxtView.setTypeface(null, Typeface.NORMAL); 
                                    	 
                                    	 if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
                                    		checkLblTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                                    		numberExtTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                     						prdIdTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                     						prdNoTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                     						serialNoTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
                     					}
                                    	 
                                    	 tr.addView(checkLblTxtView);
                                         tr.addView(numberExtTxtView);
                                         tr.addView(prdIdTxtView);
                                         tr.addView(prdNoTxtView);
                                         tr.addView(serialNoTxtView);                                         
                                         maintbl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                                     }
                                 }
                                 
                                 if(confirmSubVect.size() > 0){
                                     if(checkField[j] != null)
                                         checkField[j].setEnabled(false);
                                 }
                             }
                         }
                     }
                 }
                 catch(Exception adsf){
                	 ServiceProConstants.showErrorLog("Error in Grid Confirm : "+adsf.toString());
                 }
             }
			
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	private OnClickListener addConfirmBtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
        		if(checkIfAnyItemSelected() == true){
        			showConfirmActivityScreen();
	        	}
	            else
	            	ServiceProConstants.showErrorDialog(ServiceProConfirmationScreen.this, getString(R.string.SERVICEORD_CNFSCR_PLZSEL));        		
	        }
			catch (Exception de) {
	        	ServiceProConstants.showErrorLog("Error in addConfirmBtnListener : "+de.toString());
	        }
        }
    };
	
    private boolean checkIfAnyItemSelected(){
        boolean checkFlag = false;
        try{
            if(checkField != null){
                for(int i=0; i<checkField.length; i++){
                    if(checkField[i].isChecked() == true){
                        checkFlag = true;
                        break;
                    }
                }
            }
        }
        catch(Exception afg){
        	ServiceProConstants.showErrorLog("On checkIfAnyItemSelected : "+afg.toString());
        }
        return checkFlag;
    }//fn checkIfAnyItemSelected
        
    private void showEntitlementScreen(){
		try {
            if(category != null){
            	String servOrdNoStr = category.getObjectId().toString().trim();
            	String processTypeStr = category.getProcessType().toString().trim();
            	//ServiceProConstants.showLog(servOrdNoStr+" : "+processTypeStr);
            	if((!servOrdNoStr.equalsIgnoreCase("")) && (!processTypeStr.equalsIgnoreCase(""))){
	            	Intent intent = new Intent(this, ServiceProEntitlementScreen.class);
	            	intent.putExtra("servOrdNoStr", servOrdNoStr);
	            	intent.putExtra("processTypeStr", processTypeStr);
	    			startActivityForResult(intent, ServiceProConstants.ENTILEMENT_SCREEN);
            	}
            	else
            		ServiceProConstants.showErrorDialog(this, "ServiceOrder No and ProcessType are not available");
            }
            else
            	ServiceProConstants.showErrorDialog(this, "ServiceOrder No is not available");			
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog("Error on showEntitlementScreen : "+e.getMessage());
		}
	}//fn showEntitlementScreen
    
    private void showConfirmActivityScreen(){
        ServiceOrdDocOpConstraints docsCategory = null;
        ServiceFollDocOpConstraints followCategory = null;
        Vector docVector = new Vector();
        Vector confVector = new Vector();
        try{
            if(checkField != null){
                for(int i=0; i<checkField.length; i++){
                    if(checkField[i].isChecked() == true){
                        if(documentsList != null){
                            if(documentsList.size() > i){
                                docsCategory = (ServiceOrdDocOpConstraints)documentsList.get(i);
                                docVector.addElement(docsCategory);
                            }
                        }
                        
                        if(gridConfirmItemMainVect != null){
                            if(gridConfirmItemMainVect.size() > i){
                                Vector confObjVect = (Vector)gridConfirmItemMainVect.elementAt(i);
                                confVector.addElement(confObjVect);
                            }
                        }
                    }
                }
            }
            
            ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(docVector);
            ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
            
            Intent intent = new Intent(this, ServiceProActivityMainScreen.class);
        	intent.putExtra("docVectorObj", serVectObj1);
        	intent.putExtra("confVectorObj", serVectObj2);
        	intent.putExtra("prodDesc", processTypeDescStr);
			startActivityForResult(intent, ServiceProConstants.ACTIVITY_MAIN_SCREEN);
        }
        catch(Exception adef){
        	ServiceProConstants.showErrorLog("On showConfirmActivityScreen : "+adef.toString());
        }
    }//fn showConfirmActivityScreen
    	
	private void getGridConfirmVectorsForDisplay(){
        try{     
            ServiceOrdDocOpConstraints docsMainCategory = null;    
            ServiceFollDocOpConstraints confirmMainCategory = null;
            String numberMainExt = "", numberSubExt = "";   
            if(documentsList != null){
                for(int i=0; i<documentsList.size(); i++){
                    docsMainCategory = (ServiceOrdDocOpConstraints)documentsList.get(i);
                    numberMainExt = "";
                    numberMainExt = docsMainCategory.getNumberExt().trim();
                    Vector subDocsVector = new Vector();
                    if(ServiceProConstants.confirmCollectionVect != null){
                        for(int j=0; j<ServiceProConstants.confirmCollectionVect.size(); j++){
                            confirmMainCategory = (ServiceFollDocOpConstraints)ServiceProConstants.confirmCollectionVect.elementAt(j);
                            numberSubExt = "";
                            numberSubExt = confirmMainCategory.getSRCDocNumberExt().trim();
                            System.out.println("Matching : "+numberMainExt+" : "+numberSubExt);
                            if(numberMainExt.equalsIgnoreCase(numberSubExt)){
                                subDocsVector.addElement(confirmMainCategory);
                            }
                        }
                    }
                    gridConfirmItemMainVect.addElement(subDocsVector);
                }
            }
            //System.out.println("gridConfirmItemMainVect Size :"+gridConfirmItemMainVect.size());
        }
        catch(Exception dfg){
        	ServiceProConstants.showErrorLog("Error in getGridVectorsForDisplay : "+dfg.toString());
        }
    }//fn getGridConfirmVectorsForDisplay	
	    
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        menu.add(0, MAIN_ID, 0, "Entitlements");
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
					showEntitlementScreen();
		        }
				catch (Exception de) {
			    	ServiceProConstants.showErrorLog("Error in Menu : "+de.toString());
			    }
		        break;
			}
	    }
		return super.onOptionsItemSelected(item);
	}
	
	public void getLDBConfList(){
		try {			
			initSparesVect();
			emptyAllActivityVectors();
			clearAllConfListArray();
			ArrayList documentsArray = ServiceProDBOperations.readAllConfListDataFromDB(this, statusOrderNoStr);
			ServiceOrdDocOpConstraints docsCategory = null;
			if(documentsArray.size() != 0){				
				for(int i = 0; i < documentsArray.size(); i++){  
					docsCategory = ((ServiceOrdDocOpConstraints)documentsArray.get(i));
			        if(docsCategory != null){
			        	documentsList.add(docsCategory);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for confirmation!");
			}
			
			ArrayList spareDocumentsArray = ServiceProDBOperations.readAllConfSpareListDataFromDB(this, statusOrderNoStr);
			if(spareDocumentsArray.size() != 0){				
				for(int i = 0; i < spareDocumentsArray.size(); i++){  
					docsCategory = ((ServiceOrdDocOpConstraints)spareDocumentsArray.get(i));
			        if(docsCategory != null){
			        	ServiceProConstants.sparesCollectionVect.add(docsCategory);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Spares!");
			}
			
			ServiceProConstants.showLog("statusOrderNoStr: "+statusOrderNoStr);
			ServiceFollDocOpConstraints followCategory = null;
			ArrayList confCollecDocumentsArray = ServiceProDBOperations.readAllConfCollecListDataFromDB(this, statusOrderNoStr);
			if(confCollecDocumentsArray.size() != 0){				
				for(int i = 0; i < confCollecDocumentsArray.size(); i++){  
					followCategory = ((ServiceFollDocOpConstraints)confCollecDocumentsArray.get(i));
			        if(followCategory != null){
			        	ServiceProConstants.confirmCollectionVect.add(followCategory);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Collection!");
			}
			
			if(ServiceProConstants.sparesCollectionVect.size() > 0){
        		ServiceProConstants.spares_recall_flag = true;
        	}
			
			SOServiceActListOpConstraints serActListObj = null;
			ArrayList confProductListArray = ServiceProDBOperations.readAllConfProductListDataFromDB(this);
			if(confProductListArray.size() != 0){				
				for(int i = 0; i < confProductListArray.size(); i++){  
					serActListObj = ((SOServiceActListOpConstraints)confProductListArray.get(i));
			        if(serActListObj != null){
			        	ServiceProConstants.productListVect.add(serActListObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Product List!");
			}
			
			SOCodeListOpConstraints codeListObj = null;
			ArrayList confCauseCodeListArray = ServiceProDBOperations.readAllConfCauseCodeListDataFromDB(this);
			if(confCauseCodeListArray.size() != 0){				
				for(int i = 0; i < confCauseCodeListArray.size(); i++){  
					codeListObj = ((SOCodeListOpConstraints)confCauseCodeListArray.get(i));
			        if(codeListObj != null){
			        	ServiceProConstants.causeCodeListVect.add(codeListObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Cause Code List!");
			}

			SOCodeGroupOpConstraints codeGroupObj = null;
			ArrayList confCauseGroupListArray = ServiceProDBOperations.readAllConfCauseGroupListDataFromDB(this);
			if(confCauseGroupListArray.size() != 0){				
				for(int i = 0; i < confCauseGroupListArray.size(); i++){  
					codeGroupObj = ((SOCodeGroupOpConstraints)confCauseGroupListArray.get(i));
			        if(codeGroupObj != null){
			        	ServiceProConstants.causeCodeGroupVect.addElement(codeGroupObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Cause Group List!");
			}  
			
			SOCodeGroupOpConstraints sympGroupObj = null;
			ArrayList confSympGroupListArray = ServiceProDBOperations.readAllConfSympGroupListDataFromDB(this);
			if(confSympGroupListArray.size() != 0){				
				for(int i = 0; i < confSympGroupListArray.size(); i++){  
					sympGroupObj = ((SOCodeGroupOpConstraints)confSympGroupListArray.get(i));
			        if(sympGroupObj != null){
			        	ServiceProConstants.symptmCodeGroupVect.addElement(sympGroupObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Symptm Group List!");
			} 
			
			SOCodeListOpConstraints sympCodeObj = null;
			ArrayList confSympCodeListArray = ServiceProDBOperations.readAllConfSympCodeListDataFromDB(this);
			if(confSympCodeListArray.size() != 0){				
				for(int i = 0; i < confSympCodeListArray.size(); i++){  
					sympCodeObj = ((SOCodeListOpConstraints)confSympCodeListArray.get(i));
			        if(sympCodeObj != null){
			        	ServiceProConstants.symptmCodeListVect.addElement(sympCodeObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Symptm Code List!");
			}
			
			SOCodeGroupOpConstraints probGroupObj = null;
			ArrayList confProbGroupListArray = ServiceProDBOperations.readAllConfProbGroupListDataFromDB(this);
			if(confProbGroupListArray.size() != 0){				
				for(int i = 0; i < confProbGroupListArray.size(); i++){  
					probGroupObj = ((SOCodeGroupOpConstraints)confProbGroupListArray.get(i));
			        if(probGroupObj != null){
			        	ServiceProConstants.probCodeGroupVect.addElement(probGroupObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Problem Group List!");
			} 
			
			SOCodeListOpConstraints probCodeObj = null;
			ArrayList confProbCodeListArray = ServiceProDBOperations.readAllConfProbCodeListDataFromDB(this);
			if(confProbCodeListArray.size() != 0){				
				for(int i = 0; i < confProbCodeListArray.size(); i++){  
					probCodeObj = ((SOCodeListOpConstraints)confProbCodeListArray.get(i));
			        if(probCodeObj != null){
			        	ServiceProConstants.probCodeListVect.addElement(probCodeObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Problem Code List!");
			}

			SOMattEmpListOpConstraints mattEmpObj = null;
			ArrayList confMattEmpListArray = ServiceProDBOperations.readAllConfMattEmpListDataFromDB(this);
			if(confMattEmpListArray.size() != 0){				
				for(int i = 0; i < confMattEmpListArray.size(); i++){  
					mattEmpObj = ((SOMattEmpListOpConstraints)confMattEmpListArray.get(i));
			        if(mattEmpObj != null){
			        	ServiceProConstants.mattEmployeeListVect.addElement(mattEmpObj);
			        }
			    }
			}else{
				ServiceProConstants.showLog("No List for Confirmation Material Employee List!");
			}
			
			initLayout();
		} catch (Exception esgg) {
			ServiceProConstants.showErrorLog("Error in getLDBConfList :"+esgg.toString());
			initLayout();
		}
	}//fn getLDBConfList
	
	private void clearAllConfListArray(){
		try{
	    	if(documentsList != null)
	    		documentsList.clear();
		}
		catch(Exception sgg){
			ServiceProConstants.showErrorLog("Error in clearAllConfListArray :"+sgg.toString());
		}
	}//fn clearAllConfListArray
    
	private void initSparesVect(){
        try{
            if(ServiceProConstants.sparesCollectionVect != null)
            	ServiceProConstants.sparesCollectionVect.removeAllElements();
            else
            	ServiceProConstants.sparesCollectionVect = new Vector();
                
            if(ServiceProConstants.confirmCollectionVect != null)
            	ServiceProConstants.confirmCollectionVect.removeAllElements();
            else
            	ServiceProConstants.confirmCollectionVect = new Vector();
        }
        catch(Exception adsf){
        	ServiceProConstants.showErrorLog("Error in initSparesVect : "+adsf.toString());
        }
    }//fn initSparesVect
	
	private void emptyAllActivityVectors(){
        try{
            if(ServiceProConstants.productListVect != null)
                ServiceProConstants.productListVect.removeAllElements();
            else
                ServiceProConstants.productListVect = new Vector();            
            
            if(ServiceProConstants.causeCodeListVect != null)
                ServiceProConstants.causeCodeListVect.removeAllElements();
            else
                ServiceProConstants.causeCodeListVect = new Vector();
            
            if(ServiceProConstants.causeCodeGroupVect != null)
                ServiceProConstants.causeCodeGroupVect.removeAllElements();
            else
                ServiceProConstants.causeCodeGroupVect = new Vector();

            if(ServiceProConstants.symptmCodeGroupVect != null)
                ServiceProConstants.symptmCodeGroupVect.removeAllElements();
            else
                ServiceProConstants.symptmCodeGroupVect = new Vector();

            if(ServiceProConstants.symptmCodeListVect != null)
                ServiceProConstants.symptmCodeListVect.removeAllElements();
            else
                ServiceProConstants.symptmCodeListVect = new Vector();

            if(ServiceProConstants.probCodeGroupVect != null)
                ServiceProConstants.probCodeGroupVect.removeAllElements();
            else
                ServiceProConstants.probCodeGroupVect = new Vector();
            
            if(ServiceProConstants.probCodeListVect != null)
                ServiceProConstants.probCodeListVect.removeAllElements();
            else
                ServiceProConstants.probCodeListVect = new Vector();
                
            if(ServiceProConstants.mattEmployeeListVect != null)
                ServiceProConstants.mattEmployeeListVect.removeAllElements();
            else
                ServiceProConstants.mattEmployeeListVect = new Vector();
        }
        catch(Exception sfg){
            ServiceProConstants.showErrorLog("Error in emptyActivityVectors : "+sfg.toString());
        }
    }//fn emptyAllActivityVectors
    
}//End of class ServiceProConfirmationScreen