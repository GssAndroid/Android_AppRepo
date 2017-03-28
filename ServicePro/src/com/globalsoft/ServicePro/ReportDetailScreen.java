package com.globalsoft.ServicePro;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.ReportOpConstraints;

public class ReportDetailScreen extends Activity {
	
	private ReportOpConstraints repCategory = null;
	private TextView[] txtView;
	private EditText[] editTxt;
	
	private int dispwidth = 300;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	//requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
			/*setContentView(R.layout.reportdetail);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.reportdetail); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SERVICEPRO_RPDTSCR_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			repCategory = (ReportOpConstraints)this.getIntent().getSerializableExtra("report");			
			initLayout();
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	private void initLayout(){
		try { 
			int cols = 21;
			txtView = new TextView[cols];
			editTxt = new EditText[cols];
			
			dispwidth = ServiceProConstants.getDisplayWidth(this);
        	TableLayout tl = (TableLayout)findViewById(R.id.rprtdettbllayout1);
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
					labelWidth = 270;
					editWidth = 300;
				}
			}
			ServiceProConstants.showLog("labelWidth : "+labelWidth+" : editWidth : "+editWidth);
			
			LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			linparams.topMargin = 5; 
			linparams.bottomMargin = 5; 
			
			for (int i =0; i <cols; i++) {                    
				tr1 = new TableRow(this);
            	
                txtView[i] = new TextView(this);
				editTxt[i] = new EditText(this);
				
				txtView[i].setText("");
				txtView[i].setTextColor(getResources().getColor(R.color.bluelabel));
				txtView[i].setTypeface(null, 1); //0 = Normal 1 = Bold 2 = Italic
				txtView[i].setPadding(5,5,5,5); 
				txtView[i].setMinWidth(100);
				txtView[i].setWidth(labelWidth);
				if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH)
					txtView[i].setTextSize(ServiceProConstants.TEXT_SIZE_LABEL);
				    					  					
				editTxt[i].setText("");
				editTxt[i].setPadding(5,5,5,5);
				//salesOrdValET[i].setWidth(160);
				editTxt[i].setWidth(editWidth);
				editTxt[i].setEnabled(false);
				editTxt[i].setClickable(false);
				editTxt[i].setFocusable(false);
				
				tr1.addView(txtView[i]);
				tr1.addView(editTxt[i]);
				tr1.setLayoutParams(linparams);
				
				tl.addView(tr1, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            }
			
			txtView[0].setText(getString(R.string.SERVICEPRO_RPDTSCR_TRNSID));
			txtView[1].setText(getString(R.string.SERVICEPRO_RPDTSCR_TRNSTYPE));
			txtView[2].setText(getString(R.string.SERVICEPRO_RPDTSCR_TRNSTYPTXT));
			txtView[3].setText(getString(R.string.SERVICEPRO_RPDTSCR_SOLPARTXT));
			txtView[4].setText(getString(R.string.SERVICEPRO_RPDTSCR_SOLPAR));
			txtView[5].setText(getString(R.string.SERVICEPRO_RPDTSCR_CONTPER));
			txtView[6].setText(getString(R.string.SERVICEPRO_RPDTSCR_NETVAL));
			txtView[7].setText(getString(R.string.SERVICEPRO_RPDTSCR_CURRNCY));
			txtView[8].setText(getString(R.string.SERVICEPRO_RPDTSCR_PRIOP));
			txtView[9].setText(getString(R.string.SERVICEPRO_RPDTSCR_DESC));
			txtView[10].setText(getString(R.string.SERVICEPRO_RPDTSCR_PODATE));
			txtView[11].setText(getString(R.string.SERVICEPRO_RPDTSCR_CRDBY));
			txtView[12].setText(getString(R.string.SERVICEPRO_RPDTSCR_USRSTS));
			txtView[13].setText(getString(R.string.SERVICEPRO_RPDTSCR_POSTDAT));
			txtView[14].setText(getString(R.string.SERVICEPRO_RPDTSCR_REQSTRDAT));
			txtView[15].setText(getString(R.string.SERVICEPRO_RPDTSCR_WRKSTRDAT));
			txtView[16].setText(getString(R.string.SERVICEPRO_RPDTSCR_WRKENDDAT));
			txtView[17].setText(getString(R.string.SERVICEPRO_RPDTSCR_LABHRS));
			txtView[18].setText(getString(R.string.SERVICEPRO_RPDTSCR_TRVLHRS));
			txtView[19].setText(getString(R.string.SERVICEPRO_RPDTSCR_TOTHRS));
			txtView[20].setText(getString(R.string.SERVICEPRO_RPDTSCR_EQUPNO));
			
			getReportDetails();
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	private void getReportDetails(){
        try{
            if(repCategory != null){
                for(int k1=0; k1<editTxt.length; k1++){
                    if(editTxt[k1] != null){
                        switch(k1){
                            case 0:
                                editTxt[k1].setText(repCategory.getObjectId());
                                break;
                            case 1:
                                editTxt[k1].setText(repCategory.getProcessType());
                                break;
                            case 2:
                                editTxt[k1].setText(repCategory.getProcessTypeText());
                                break;
                            case 3:
                                editTxt[k1].setText(repCategory.getSoldToPartyList());
                                break;
                            case 4:
                                editTxt[k1].setText(repCategory.getSoldToParty());
                                break;
                            case 5:
                                editTxt[k1].setText(repCategory.getContactPersonList());
                                break;
                            case 6:
                                editTxt[k1].setText(repCategory.getNetValue());
                                break;
                            case 7:
                                editTxt[k1].setText(repCategory.getCurrency());
                                break;
                            case 8:
                                editTxt[k1].setText(repCategory.getPriorityText());
                                break;
                            case 9:
                                editTxt[k1].setText(repCategory.getDescription());
                                break;
                            case 10:
                                editTxt[k1].setText(repCategory.getPODateSold());
                                break;
                            case 11:
                                editTxt[k1].setText(repCategory.getCreatedBy());
                                break;
                            case 12:
                                editTxt[k1].setText(repCategory.getConcatStatUser());
                                break;
                            case 13:
                                editTxt[k1].setText(repCategory.getPostingDate());
                                break;
                            case 14:
                                editTxt[k1].setText(repCategory.getReqStartDate());
                                break;
                            case 15:
                                editTxt[k1].setText(repCategory.getWorkStartDate());
                                break;
                            case 16:
                                editTxt[k1].setText(repCategory.getWorkEndDate());
                                break;
                            case 17:
                                editTxt[k1].setText(repCategory.getHoursLabor());
                                break;
                            case 18:
                                editTxt[k1].setText(repCategory.getHoursTravel());
                                break;
                            case 19:
                                editTxt[k1].setText(repCategory.getHoursTotal());
                                break;
                            case 20:
                                editTxt[k1].setText(repCategory.getEquipNo());
                                break;
                        }
                        //editTxt[k1].setEnabled(false);
                        editTxt[k1].setTextColor(getResources().getColor(R.color.bluelabel));
                    }
                }
            }
        }
        catch(Exception adf){
        	ServiceProConstants.showErrorLog("Error in getMaterialDetails : "+adf.toString());
        }
    }//fn getReportDetails
	
	public void onClose(){
    	try {
			System.gc();
			setResult(RESULT_OK); 
			this.finish();
		} catch (Exception e) {
		}
    }//fn onClose
	 
}//End of class ReportDetailScreen