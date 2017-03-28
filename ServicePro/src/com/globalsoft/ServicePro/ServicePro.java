package com.globalsoft.ServicePro;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalsoft.ContactLib.ContactMain;
import com.globalsoft.SapLibActivity.ActivityListForPhone;
import com.globalsoft.SapLibActivity.CrtGenActivity;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.SapQueueProcessorHelper.Utils.SapQueueProcessorHelperConstants;

public class ServicePro extends Activity {
	
	private Button taskbtn, reportbtn, vanstockbtn, leavebtn, myutilizationbtn, 
			billablebtn, contactsBtn, activitiesBtn;
	private ImageView infoBtn;
	private ProgressDialog pdialog = null;
	private boolean internetAccess = false;
	
	//For Timer 
	private TimerTask scanTask; 
	private final Handler handler = new Handler(); 
	private Timer t = new Timer();  
		
	private int dispwidth = 300;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	//requestWindowFeature(Window.FEATURE_LEFT_ICON);
    	    ServiceProConstants.setWindowTitleTheme(this);
			/*setContentView(R.layout.main);			
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.main); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle_i); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.app_name));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			ServiceProConstants.APPLN_PACKAGE_NAME = this.getPackageName();
			ServiceProConstants.APPLN_BGSERVICE_NAME = this.getPackageName()+".BackgroundService";
			
			taskbtn = (Button) findViewById(R.id.taskbtn);
			taskbtn.setOnClickListener(taskbtnListener);  
					     
			reportbtn = (Button) findViewById(R.id.reportbtn);
			reportbtn.setOnClickListener(reportbtnListener);  
			
			vanstockbtn = (Button) findViewById(R.id.vanstockbtn);
			vanstockbtn.setOnClickListener(vanstockbtnListener);  
		     
			leavebtn = (Button) findViewById(R.id.leavebtn);
			leavebtn.setOnClickListener(leavebtnListener); 
			
			contactsBtn = (Button) findViewById(R.id.contactsBtn);
			contactsBtn.setOnClickListener(contactsBtnListener);
			
			activitiesBtn = (Button) findViewById(R.id.activitiesBtn);
			activitiesBtn.setOnClickListener(activitiesBtnListener);
			
			myutilizationbtn = (Button) findViewById(R.id.myutilizationbtn);
			//report4Btn.setOnClickListener(registerBtnListener);  
		     
			billablebtn = (Button) findViewById(R.id.billablebtn);
			//report5Btn.setOnClickListener(loginBtnListener); 
			
			infoBtn = (ImageView) findViewById(R.id.info);
			infoBtn.setOnClickListener(infoBtnListener); 			
			
			internetAccess = ServiceProConstants.checkInternetConn(ServicePro.this);			
			if(internetAccess){
				try{
					disableBtnAction();
					int getApplQueueCount = SapQueueProcessorHelperConstants.getApplicationQueueCount(this, ServiceProConstants.APPLN_NAME_STR);
					ServiceProConstants.showLog("ServiceProConstants.APPLN_NAME_STR:"+ServiceProConstants.APPLN_NAME_STR);
					ServiceProConstants.showLog("getApplQueueCount:"+getApplQueueCount); 
					try{
						if(getApplQueueCount <= 0){
							enableBtnAction();
						}
						else{
							try{
								SapQueueProcessorHelperConstants.updateSelectedRowStatus(this, 0, ServiceProConstants.APPLN_NAME_STR, ServiceProConstants.STATUS_HIGHPRIORITY);
								startProgressDialog();
							}
				            catch(Exception sff2){
				            	ServiceProConstants.showErrorLog("Error in ServicePro for getApplicationQueueCount else part: "+sff2.toString());
				            }   
						}			
					}
		            catch(Exception sff1){
		            	ServiceProConstants.showErrorLog("Error in ServicePro for getApplicationQueueCount: "+sff1.toString());
		            }    
				}
	            catch(Exception sff2){
	            	ServiceProConstants.showErrorLog("Error in getApplQueueCount if part: "+sff2.toString());
	            }   
			}			
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
    
    private void enableBtnAction(){
    	try {
			taskbtn.setClickable(true);
			reportbtn.setClickable(true);
			vanstockbtn.setClickable(true);
			leavebtn.setClickable(true);
			myutilizationbtn.setClickable(true);
			billablebtn.setClickable(true);
		} catch (Exception ee) {
			ServiceProConstants.showErrorLog("Error in enableBtnAction: "+ee.toString());
		}
    }//fn enableBtnAction
    
    private void disableBtnAction(){
    	try {
			taskbtn.setClickable(false);
			reportbtn.setClickable(false);
			vanstockbtn.setClickable(false);
			leavebtn.setClickable(false);
			myutilizationbtn.setClickable(false);
			billablebtn.setClickable(false);
		} catch (Exception ee) {
			ServiceProConstants.showErrorLog("Error in disableBtnAction: "+ee.toString());
		}
    }//fn disableBtnAction
    
    private void startProgressDialog(){
    	try {
    		if(pdialog != null)
    			pdialog = null;
    		
			if(pdialog == null)                    
            	pdialog = ProgressDialog.show(this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS_FOR_UPDATION),true);				
				dostarttimer();
				
		} catch (Exception ae) {
			ServiceProConstants.showErrorLog("Error in startProgressDialog: "+ae.toString());
		}
    }//fn startProgressDialog
    
    public void dostarttimer(){
    	try{     	
    		scanTask = new TimerTask() { 
            public void run() { 
                handler.post(new Runnable() { 
                    public void run() { 
                    	//ServiceProConstants.showLog("TIMER: Timer started"); 
                    	checkAppQueueCount();
                    } 
               }); 
            }};
	        t.schedule(scanTask, 100, ServiceProConstants.TIMER_CONST);
    	}
    	catch(Exception qw){
    		ServiceProConstants.showErrorLog("Error in dostarttimer:"+qw.toString());
    	}  
    }//fn dostarttimer 
    
    private void checkAppQueueCount(){
    	int count = 0;
    	try {
			count = SapQueueProcessorHelperConstants.getApplicationQueueCount(this, ServiceProConstants.APPLN_NAME_STR);
			if(count == 0){
				stopTimerTask();
				stopProgressDialog();
				enableBtnAction();
			}else{
				if(count > 1){
					pdialog.setMessage(getResources().getString(R.string.SERVICEPRO_WAIT_TEXTS_FOR_UPDATION) + " " +count + " items remaining..." );
				}else{
					pdialog.setMessage(getResources().getString(R.string.SERVICEPRO_WAIT_TEXTS_FOR_UPDATION) + " " +count + " item remaining..." );
				}
			}
			
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in checkAppQueueCount:"+e.toString());
		}
    }//fn checkAppQueueCount
    
    public void stopTimerTask(){  
    	try{
	       if(scanTask!=null){ 
	    	   //ServiceProConstants.showLog("TIMER: Timer canceled"); 
	    	   scanTask.cancel(); 
	       } 
    	}
    	catch(Exception qw){
    		ServiceProConstants.showErrorLog("Error in stoptimer:"+qw.toString());
    	}  
    }//fn stoptimer
    
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
    	
    private void showServiceOrderTasksScreen(){
		try {
			//Intent intent = new Intent(this, ServiceProTaskMainScreen.class);
	    	ServiceProConstants.showLog("dispwidth: "+dispwidth); 
	    	ServiceProConstants.showLog("SCREEN_CHK_DISPLAY_WIDTH: "+ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH); 
			Intent intent;
			if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
				intent = new Intent(this, ServiceProTaskMainScreenForTablet.class);
			}else{
				intent = new Intent(this, ServiceProTaskMainScreenForPhone.class);
			}  	
			startActivityForResult(intent,ServiceProConstants.TASKLIST_SCREEN);
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showServiceOrderTasksScreen   
   

    private void showReporThisMonthScreen(){
		try {
			Intent intent = new Intent(this, ReportMainScreen.class);
			startActivityForResult(intent,ServiceProConstants.REPORT1_SCREEN);
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showReporThisMonthScreen    
    
    
    private void showVanStockScreen(boolean showListFlag){
    	Intent intent = null;
		try {
			if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
				intent = new Intent(this, VanStockMainScreen.class);
			}
			else{
				if(showListFlag == false)
					intent = new Intent(this, VanStockMainScreen.class);
				else	
					intent = new Intent(this, VanStockMainListScreen.class);
			}
			startActivityForResult(intent, ServiceProConstants.VANSTOCK_SCREEN);
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showVanStockScreen  
    
    
    private void showMyLeavesScreen(){
		try {
			Intent intent = new Intent(this, LeaveMainScreen.class);
			startActivityForResult(intent,ServiceProConstants.LEAVE_MAIN_SCREEN);
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showMyLeavesScreen 
    
    private void showEnterpriseContactsScreen(){
		try {
			Intent intent = new Intent(this, ContactMain.class);
        	intent.putExtra("app_name_options", "SERVICEPRO");
        	intent.putExtra("app_name", SapGenConstants.APPLN_NAME_STR_MOBILEPRO);
			startActivity(intent);			
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showEnterpriseContactsScreen
    
    private void showGeneralActivityScreen(){
		try {
			Intent intent = new Intent(this, ActivityListForPhone.class);
			startActivityForResult(intent,SapGenConstants.ACTION_GEN_ACTIVITY);
		} 
		catch (Exception sse) {
			ServiceProConstants.showErrorLog("Error on showGeneralActivityScreen : "+sse.getMessage());
		}
	}//fn showGeneralActivityScreen
        
    private OnClickListener taskbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	//ServiceProConstants.showLog("Task btn clicked");
        	showServiceOrderTasksScreen();
        }
    };
    
    private OnClickListener reportbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	//ServiceProConstants.showLog("report1 btn clicked");
        	showReporThisMonthScreen();
        }
    };
    
    private OnClickListener vanstockbtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	//ServiceProConstants.showLog("report2 btn clicked");
        	showVanStockScreen(true);
        }
    };
    
    private OnClickListener leavebtnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	ServiceProConstants.showLog("report3 btn clicked");
        	showMyLeavesScreen();
        }
    };
    
    private OnClickListener contactsBtnListener = new OnClickListener(){
        public void onClick(View v) {
        	ServiceProConstants.showLog("contactsBtn clicked");
        	showEnterpriseContactsScreen();
        }
    };    

    private OnClickListener activitiesBtnListener = new OnClickListener(){
        public void onClick(View v) {
        	ServiceProConstants.showLog("Activities Btn clicked");
        	showGeneralActivityScreen();
        }
    };
    
    private OnClickListener infoBtnListener = new OnClickListener(){
        public void onClick(View v) {
        	//ServiceProConstants.showLog("Info Btn clicked");
        	showAboutScreen();
        }
    };
    
    private void showAboutScreen(){
    	try {
			Intent intent = new Intent(this, About.class);
			startActivityForResult(intent,ServiceProConstants.ABOUT_SCREEN);
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
    };
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	try {   			
    		ServiceProConstants.showLog("Request Code "+requestCode);
    		ServiceProConstants.showLog("Result Code "+resultCode);
    		if((requestCode == ServiceProConstants.VANSTOCK_SCREEN) && (dispwidth < ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH)){
    			if(resultCode == RESULT_OK){
            		ServiceProConstants.showLog("Result Code "+RESULT_OK);
	        		if(data != null){
	    				boolean showListViewFlag = data.getBooleanExtra("ShowListView", true);
	    				ServiceProConstants.showLog("showListViewFlag "+showListViewFlag);
	    				showVanStockScreen(showListViewFlag);
	    			}
    			}
    		}
		} catch (Exception e) {
			ServiceProConstants.showErrorLog(e.toString());
		}
    }
}//End of class ServicePro
