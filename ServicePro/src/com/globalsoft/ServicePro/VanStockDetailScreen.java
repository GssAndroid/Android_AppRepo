package com.globalsoft.ServicePro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.VanStkOpConstraints;

public class VanStockDetailScreen extends Activity {
	
	private TextView[] vanStkLblTV;
	private EditText[] vanStkValET;
	
	private VanStkOpConstraints stkCategory = null;
	
	private int dispwidth = 300;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
			this.setTitle("Van Stock");
			setContentView(R.layout.vanstockdetail);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.vanstockdetail); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText("Van Stock");

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			stkCategory = (VanStkOpConstraints) this.getIntent().getSerializableExtra("stkCategoryObj");
			initLayout();
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	private void initLayout(){
		try {
			int cols = 8;
			vanStkLblTV = new TextView[cols];
			vanStkValET = new EditText[cols];
			
			try{
				dispwidth = ServiceProConstants.getDisplayWidth(this);
				TableLayout tl = (TableLayout)findViewById(R.id.vstckdettbllayout1);
				if(dispwidth < ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH)
					tl.setColumnStretchable(1, true);
				
				TableRow tr1 = new TableRow(this);
				tr1.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				int labelWidth = 100, editWidth = 200;
				ServiceProConstants.showLog("dispwidth : "+dispwidth);
				if(dispwidth > 0){
					labelWidth = dispwidth-editWidth;
					if(labelWidth < 100)
						labelWidth = 100;
					else if(labelWidth > 160)
						labelWidth = 160;
					
					editWidth = dispwidth-labelWidth;
					if(editWidth < 160)
						editWidth = 160;
					else if(editWidth > 250)
						editWidth = 250;
					
					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
						labelWidth = 220;
						editWidth = 300;
					}
				}
				ServiceProConstants.showLog("labelWidth : "+labelWidth+" : editWidth : "+editWidth);
				
				LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
				linparams.topMargin = 5; 
				linparams.bottomMargin = 5; 
				
				for(int i=0; i<cols; i++){
					tr1 = new TableRow(this);
		            	
					vanStkLblTV[i] = new TextView(this);
					vanStkValET[i] = new EditText(this);
					
					vanStkLblTV[i].setText("");
					vanStkLblTV[i].setTextColor(getResources().getColor(R.color.bluelabel));
					vanStkLblTV[i].setTypeface(null, 1); //0 = Normal 1 = Bold 2 = Italic
					vanStkLblTV[i].setPadding(5,5,5,5); 
					vanStkLblTV[i].setMinWidth(100);
					vanStkLblTV[i].setWidth(labelWidth);
					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH)
						vanStkLblTV[i].setTextSize(ServiceProConstants.TEXT_SIZE_LABEL);    					  		
					
					vanStkValET[i].setText("");
					vanStkValET[i].setPadding(5,5,5,5);
					//vanStkValET[i].setWidth(160);
					vanStkValET[i].setWidth(editWidth);
					vanStkValET[i].setEnabled(false);
                    vanStkValET[i].setClickable(false);
                    vanStkValET[i].setFocusable(false);
                    
					tr1.addView(vanStkLblTV[i]);
					tr1.addView(vanStkValET[i]);
					tr1.setLayoutParams(linparams);
					
					tl.addView(tr1, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				}
				
				ServiceProConstants.showLog("Textview width : "+vanStkLblTV[0].getWidth());
				ServiceProConstants.showLog("EditText width : "+vanStkValET[0].getWidth());
				
				vanStkLblTV[0].setText(R.string.SERVICEPRO_VANSTOCK_MATRL);
				vanStkLblTV[1].setText(R.string.SERVICEPRO_VANSTOCK_STOCK);
				vanStkLblTV[2].setText(R.string.SERVICEPRO_VANSTOCK_UNITS);
				vanStkLblTV[3].setText(R.string.SERVICEPRO_VANSTOCK_INTRANSIT);
				vanStkLblTV[4].setText(R.string.SERVICEPRO_VANSTOCK_MATDESC);
				vanStkLblTV[5].setText(R.string.SERVICEPRO_VANSTOCK_BATCH);
				vanStkLblTV[6].setText(R.string.SERVICEPRO_VANSTOCK_STRLOC);
				vanStkLblTV[7].setText(R.string.SERVICEPRO_VANSTOCK_PLANT);
				
				getVanStockDetails();
			}catch(Exception sf){}
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	
	private void getVanStockDetails(){
        try{
            if(stkCategory != null){
            	String data = "";
                for(int k1=0; k1<vanStkValET.length; k1++){
                    if(vanStkValET[k1] != null){
                    	data = "";
                        switch(k1){
                            case 0:
                            	data = stkCategory.getMaterial();
                            	vanStkValET[k1].setText(data);
                                break;
                            case 1:
                            	data = stkCategory.getStockQty();
                            	vanStkValET[k1].setText(data);
                                break;
                            case 2:
                            	data = stkCategory.getMeasureUnits();
                            	vanStkValET[k1].setText(data);
                                break;
                            case 3:
                            	data = stkCategory.getMatQtyTransit();
                            	vanStkValET[k1].setText(data);
                                break;
                            case 4:
                            	data = stkCategory.getMattDesc();
                            	vanStkValET[k1].setText(data);
                                break;
                            case 5:
                            	data = stkCategory.getBatchNo();
                            	vanStkValET[k1].setText(data);
                                break;
                            case 6:
                            	data = stkCategory.getStorageLoc();
                            	vanStkValET[k1].setText(data);
                                break;
                            case 7:
                            	data = stkCategory.getWerks();
                            	vanStkValET[k1].setText(data);
                                break;
                        }

                    	if((data == null) || (data.equalsIgnoreCase(""))){
                    		if(vanStkLblTV[k1] != null){
                    			vanStkLblTV[k1].setVisibility(View.GONE);
                        		vanStkValET[k1].setVisibility(View.GONE);
                    		}
                    	}
                        /*vanStkValET[k1].setEnabled(false);
                        vanStkValET[k1].setClickable(false);*/
                        vanStkValET[k1].setTextColor(getResources().getColor(R.color.bluelabel));
                    }
                }
            }
        }
        catch(Exception adf){
        	ServiceProConstants.showErrorLog("Error in getVanStockDetails : "+adf.toString());
        }
    }//fn getVanStockDetails
	
}//End of class VanStockSerialDetScreen
