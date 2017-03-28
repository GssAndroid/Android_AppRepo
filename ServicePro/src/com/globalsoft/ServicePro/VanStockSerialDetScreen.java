package com.globalsoft.ServicePro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.VanStkOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkSerOpConstraints;

public class VanStockSerialDetScreen extends Activity {
	
	private TextView matterialTV, batchTV, mattdescTV, strlocTV, plantTV;
	private TextView EquipTV, serialNoTV;
	
	private ArrayList serCatVect = new ArrayList();
    private VanStkOpConstraints stkCategory = null;
    private VanStkSerOpConstraints serCategory = null;  
    
    private int equipHWidth, serialNoHWidth;
    private int dispwidth = 300;
    private int sortIndex = -1;
    private boolean sortFlag = false, sortEquipFlag = false, sortSoNoFlag = false;
    private final int sortHeader1=1, sortHeader2=2;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
			setContentView(R.layout.vanstockserial);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.vanstockserial); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.VANSTO_SER_DET_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			stkCategory = (VanStkOpConstraints) this.getIntent().getSerializableExtra("stkCategoryObj");
			serCatVect = (ArrayList)this.getIntent().getSerializableExtra("serCatVectObj");
			initLayout();
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	private void initLayout(){
		try {
			setContentView(R.layout.vanstockserial);
			
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			matterialTV = (TextView)findViewById(R.id.matterialTV);			
			batchTV = (TextView)findViewById(R.id.batchTV);			
			mattdescTV =(TextView)findViewById(R.id.mattdescTV);
			strlocTV = (TextView)findViewById(R.id.strlocTV);			
			plantTV = (TextView)findViewById(R.id.plantTV);
			updateUIElements();
			
			EquipTV = (TextView)findViewById(R.id.EquipTV);
			EquipTV.setGravity(Gravity.LEFT);
			EquipTV.setPadding(10,5,5,5);
			EquipTV.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(sortHeader1);
				}
			});
			
			serialNoTV = (TextView)findViewById(R.id.serialNoTV);
			serialNoTV.setGravity(Gravity.LEFT);
			serialNoTV.setPadding(10,5,5,5);
			serialNoTV.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(sortHeader2);
				}
			});
			
			
			ViewTreeObserver vto1 = EquipTV.getViewTreeObserver();
	        vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = EquipTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                equipHWidth = EquipTV.getWidth();
	                //ServiceProConstants.showLog("EquipTV Width1 : "+equipHWidth+" : "+EquipTV.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto2 = serialNoTV.getViewTreeObserver();
	        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = serialNoTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                serialNoHWidth = serialNoTV.getWidth();
	                //ServiceProConstants.showLog("serialNoTV Width1 : "+serialNoHWidth+" : "+serialNoTV.getMeasuredWidth());
	                drawSubLayout();
	            }
	        });
			
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	private void drawSubLayout(){
		try{
			TableLayout tl = (TableLayout)findViewById(R.id.vstcksrltbllayout2);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
						
			if(serCatVect != null){
				//ServiceProConstants.showLog("Size : "+serCatVect.size());
				if(serCatVect.size() > 0){
                    serCategory = null;
                    String equipStr = "", serialNoStr = "";
                    
                    for(int h=0; h<serCatVect.size(); h++){
                        serCategory = (VanStkSerOpConstraints)serCatVect.get(h);
                        if(serCategory != null){
                            equipStr = serCategory.getEquipment().trim();
                            serialNoStr = serCategory.getSerialNo().trim();
                            
                            tr = new TableRow(this);
                            
                            TextView equipTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
                            equipTxtView.setText(equipStr);
                            equipTxtView.setWidth(equipHWidth);
                            equipTxtView.setGravity(Gravity.LEFT);
        					equipTxtView.setPadding(10,0,0,0);
        					equipTxtView.setTypeface(null, Typeface.NORMAL);
        					
        					TextView serialTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
        					serialTxtView.setText(serialNoStr);
        					serialTxtView.setWidth(serialNoHWidth);
        					serialTxtView.setGravity(Gravity.LEFT);
        					serialTxtView.setPadding(10,0,0,0);
        					serialTxtView.setTypeface(null, Typeface.NORMAL); 
                        	
        					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
        						equipTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
        						serialTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
        					}
        					tr.addView(equipTxtView);
        					tr.addView(serialTxtView);
        					
        					if(h%2 == 0)
        						tr.setBackgroundResource(R.color.item_even_color);
        		            else
        		            	tr.setBackgroundResource(R.color.item_odd_color);
        					
        					tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                        }
                    }
                }
			}
		}
		catch(Exception sf){
			ServiceProConstants.showErrorLog("Error in drawSubLayout : "+sf.toString());
		}
	}//fn drawSubLayout
	
	
	private void updateUIElements(){
        try{
            if(stkCategory != null){
            	matterialTV.setText(" :   "+stkCategory.getMaterial());
            	batchTV.setText(" :   "+stkCategory.getBatchNo());
            	mattdescTV.setText(" :   "+stkCategory.getMattDesc());
            	strlocTV.setText(" :   "+stkCategory.getStorageLoc());
            	plantTV.setText(" :   "+stkCategory.getWerks());                            
            }
            else
            	ServiceProConstants.showLog("stkCategory is null");
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error in updateUIElements : "+asf.toString());
        }
    }//fn updateUIElements
	
	
	private void sortItemsAction(int sortInd){
		try{
			 sortFlag = true;
			 sortIndex = sortInd;
			 			 
			 if(sortInd == sortHeader1)
				 sortEquipFlag = !sortEquipFlag;
			 else if(sortInd == sortHeader2)
				 sortSoNoFlag = !sortSoNoFlag;
			 
			 //ServiceProConstants.showLog("Selected Sort Index : "+sortInd);
			 Collections.sort(serCatVect, stockSortComparator); 
				
             initLayout();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in sortItemsAction : "+sfg.toString());
		}
	}//fn sortItemsAction
	
	private final Comparator stockSortComparator =  new Comparator() {

        public int compare(Object o1, Object o2){ 
            int comp = 0;
            String strObj1 = "0", strObj2="0";
            VanStkSerOpConstraints repOPObj1, repOPObj2;
            try{            	
                if (o1 == null || o2 == null){
                	ServiceProConstants.showLog("Comparator Objects null");
                }
                else{            
                	repOPObj1 = (VanStkSerOpConstraints)o1;
                    repOPObj2 = (VanStkSerOpConstraints)o2;
                    
                    if(sortIndex == sortHeader1){
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getEquipment().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getEquipment().trim();
                        
                        if(sortEquipFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == sortHeader2){
                    	if(repOPObj1 != null)
                    		strObj1 = repOPObj1.getSerialNo().trim();
                    
	                    if(repOPObj2 != null)
	                        strObj2 = repOPObj2.getSerialNo().trim();
	                    
	                    if(sortSoNoFlag == true)
	                        comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
	                    else
	                        comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else{
                        // Code to sort by Material (default)
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getMaterial().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getMaterial().trim();
                       
                    	comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                    }
                }
             }
             catch(Exception qw){
            	 ServiceProConstants.showErrorLog("Error in stock Order Comparator : "+qw.toString());
             }
                 
             if (comp != 0) {            
                return comp;
             } 
             else {            
                return 0;
            }
        }
    };
	 
}//End of class VanStockSerialDetScreen
