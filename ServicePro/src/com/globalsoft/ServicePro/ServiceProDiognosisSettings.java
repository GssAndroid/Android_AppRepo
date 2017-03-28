package com.globalsoft.ServicePro;


import com.globalsoft.SalesOrderLib.Utils.SalesOrderConstants;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ServiceProDiognosisSettings  extends Activity{
 
	private CheckBox switchStatus;
	private ToggleButton toggleButton1, toggleButton2;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.settings_view); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			String titleStr = "Setting";
			myTitle.setText(titleStr);

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
			//toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);		
			toggleButton1.setOnClickListener(new OnClickListener() {
		 
				@Override
				public void onClick(View v) {
		 
					if (toggleButton1.isChecked()) 
				    {
						ServiceProConstants.DIOG_FLAG=1;
						
				    }
				    else
				    {
				    	ServiceProConstants.DIOG_FLAG=0;
				    	
				    }
					
				  /* StringBuffer result = new StringBuffer();
				   result.append("toggleButton1 : ").append(toggleButton1.getText());
				   //result.append("\ntoggleButton2 : ").append(toggleButton2.getText());
				   String switchStr = result.toString();
				   SalesOrderConstants.showLog("result "+switchStr);
					if(switchStr=="ON"){
						ServiceProConstants.DIOG_FLAG=1;
					}else if(switchStr=="OFF"){
						ServiceProConstants.DIOG_FLAG=0;
					}
				   Toast.makeText(ServiceProDiognosisSettings.this, result.toString(),
					Toast.LENGTH_SHORT).show();*/		 
				}
		 
			});			
			if (ServiceProConstants.DIOG_FLAG==1) //if (tgpref) may be enough, not sure
			{
				toggleButton1.setChecked(true);
			}
			else
			{
				toggleButton1.setChecked(false);
			}
			/*switchStatus=(CheckBox)findViewById(R.id.ckbox);
			
			switchStatus.setOnCheckedChangeListener(new OnCheckedChangeListener(){					
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {					
						SalesOrderConstants.showLog("boolean "+arg1);
						//CheckBox cb = (CheckBox) arg0 ; 
						if(arg1==true){
							ServiceProConstants.DIOG_FLAG=1;
						}					             							
						else{
							ServiceProConstants.DIOG_FLAG=0;
						}					             							
						//SalesOrderConstants.showLog("CompoundButton "+arg0);
						if(!selchkboxlist.contains(pId)){
				            selchkboxlist.add(pId);
		        		}
					}
				}); */
		} catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in onCreate: "+de.toString());
	    }
		
	
	}//onCreate
}//ServiceProDiognosisSettings
