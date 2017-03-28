package com.globalsoft.ServicePro;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.SOServiceActListOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Item.SOActivityClass;

public class ServiceProActivityEditScreen extends Activity {
	
	public Spinner activity_sp, tzoneSpn;
	public TextView start_date, end_date, start_time, end_time;
	public Button done_bt;
	public EditText duration_hrs, notes;
	public String selected_activity;
	private boolean editModeFlag = true;
    private int selIndex = 0, matchIndex = 0; 
    private String parActMatchStr = "", curActMatchStr = "", notesMatchStr = "";
    private String[] arrChoices = {"------------------"}, choicesArr, timeZoneArr;
    private String start_date_value_cal = "", start_time_value_cal = "";
    private String end_date_value_cal = "", end_time_value_cal = "";
    private ProgressDialog pdialog = null;
    
	// date and time
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMinute;
    
    private int startYear_dp;
    private int startMonth_dp;
    private int startDay_dp;
    private int startHour_dp;
    private int startMinute_dp;
    
    private int endYear;
    private int endMonth;
    private int endDay;
    private int endHour;
    private int endMinute;
    
    private int endYear_dp;
    private int endMonth_dp;
    private int endDay_dp;
    private int endHour_dp;
    private int endMinute_dp;

    private static final int START_DATE_DIALOG_ID = 1;
    private static final int START_TIME_DIALOG_ID = 2;    
    private static final int END_DATE_DIALOG_ID = 3;
    private static final int END_TIME_DIALOG_ID = 4;
    
    private SOActivityClass activityObj = null;
    private Vector documentsVect, confVector;
    private ServiceProVectSerializable serObj1, serObj2;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_LEFT_ICON);
    	ServiceProConstants.setWindowTitleTheme(this);
        /*setContentView(R.layout.activity_edit);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.activity_edit); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SERVICEORD_FLTSCR_LBL_ACT));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
        
	    try{    
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
		    
	        selIndex = this.getIntent().getIntExtra("selIndex", -1);
	        editModeFlag = this.getIntent().getBooleanExtra("editflag", true);
	        parActMatchStr = this.getIntent().getStringExtra("editMatchStr");
	        /*ServiceProConstants.showLog("selIndex:"+selIndex);
	        ServiceProConstants.showLog("editModeFlag:"+editModeFlag);
	        ServiceProConstants.showLog("parActMatchStr:"+parActMatchStr);*/
	        
	        if(pdialog != null)
    			pdialog = null;
	        pdialog = ProgressDialog.show(this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
			Thread t = new Thread() 
			{
	            public void run() 
				{
	            	try {
	            		getTimeZoneAdapterValue();
					} catch (Exception e) {
						e.printStackTrace();
						pdialog.dismiss();
					}	    			
	            	pdialog.dismiss();
				}    		                		            
	        };
	        t.start();	        
	       		
			timeZoneArr = ServiceProConstants.tz_str;
			tzoneSpn = (Spinner) findViewById(R.id.tzoneSpn);
			if(timeZoneArr != null){     
		        ArrayAdapter<String> timeZone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeZoneArr);
		        timeZone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        tzoneSpn.setAdapter(timeZone_adapter);		        
	        }
	        else{
	        	ArrayAdapter<String> timeZone_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	        	timeZone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        tzoneSpn.setAdapter(timeZone_adapter);
	        }
	        
	        getActivityDetails();
	        choicesArr = getChoicesArray();
	        activity_sp = (Spinner) findViewById(R.id.activity_sp);
	        if(choicesArr != null){     
		        ArrayAdapter<String> activity_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choicesArr);
		        activity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        activity_sp.setAdapter(activity_adapter);
		        activity_sp.setSelection(matchIndex);
		        activity_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_activity = item.toString();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        }); 
	        }
	        else{
	        	ArrayAdapter<String> activity_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
		        activity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        activity_sp.setAdapter(activity_adapter);
	        }
	        
	        /*ServiceProConstants.showLog("MatchIndex : "+matchIndex+" : "+selIndex);
	        ServiceProConstants.showLog("MatchString : "+parActMatchStr+" : "+curActMatchStr);*/
	        
	        if((selIndex == 0) && (editModeFlag == true) && (parActMatchStr.equalsIgnoreCase(curActMatchStr)))
	        	activity_sp.setEnabled(false);
	        
	        duration_hrs = (EditText) findViewById(R.id.duration_hrs); 
	        notes = (EditText) findViewById(R.id.notes);
	        
	        start_date = (TextView) findViewById(R.id.start_date);
	        start_date.setOnClickListener(start_dateListener);
	        
	        start_time = (TextView) findViewById(R.id.start_time);
	        start_time.setOnClickListener(start_timeListener);
	        
	        end_date = (TextView) findViewById(R.id.end_date);
	        end_date.setOnClickListener(end_dateListener);
	        
	        end_time = (TextView) findViewById(R.id.end_time);
	        end_time.setOnClickListener(end_timeListener);
	        
	        done_bt = (Button) findViewById(R.id.done_bt);
	        done_bt.setOnClickListener(done_btListener); 
	        
	        final Calendar c = Calendar.getInstance();
	        //initialize for start date
	        startYear = c.get(Calendar.YEAR);
	        startMonth = c.get(Calendar.MONTH);
	        startDay = c.get(Calendar.DAY_OF_MONTH);
	        startHour = c.get(Calendar.HOUR_OF_DAY);
	        startMinute = c.get(Calendar.MINUTE);
	        
	        startYear_dp = startYear;
	        startMonth_dp = startMonth;
	        startDay_dp = startDay;
	        startHour_dp = startHour;
	        startMinute_dp = startMinute;
	        
	        updateDisplay_start_date();
	        updateDisplay_start_time();
	        
	        //initialize for end date
	        endYear = c.get(Calendar.YEAR);
	        endMonth = c.get(Calendar.MONTH);
	        endDay = c.get(Calendar.DAY_OF_MONTH);
	        endHour = c.get(Calendar.HOUR_OF_DAY);
	        endMinute = c.get(Calendar.MINUTE);
	        
	        endYear_dp = endYear;
	        endMonth_dp = endMonth;
	        endDay_dp = endDay;
	        endHour_dp = endHour;
	        endMinute_dp = endMinute;
	        
	        updateDisplay_end_date();
	        updateDisplay_end_time();
	        
	        if(editModeFlag == true)
	            prefillEditActivityValues();
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in Oncreate : "+asf.toString());
	    }
    }
    
    private void getTimeZoneAdapterValue(){
    	try {
			if(ServiceProConstants.tzoneadapter == null)
				ServiceProConstants.tzoneadapter = ServiceProConstants.getTimeZoneAdapter(this);
		} catch (Exception easf) {
			ServiceProConstants.showErrorLog("Error in getTimeZoneAdapterValue : "+easf.toString());
		}
    }//fn getTimeZoneAdapterValue
    
    //start date click listener
    private OnClickListener start_dateListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	showDialog(START_DATE_DIALOG_ID);
        }
    };
    
    //start time click listener
    private OnClickListener start_timeListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	showDialog(START_TIME_DIALOG_ID);
        }
    };

    //end date click listener
    private OnClickListener end_dateListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	showDialog(END_DATE_DIALOG_ID);
        }
    };
        
    //end time click listener
    private OnClickListener end_timeListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	showDialog(END_TIME_DIALOG_ID);
        }
    };
    
    //done btn click listener
    private OnClickListener done_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	            String duration_hrs_value = duration_hrs.getText().toString().trim();
	            System.out.println("Duration_hrs_value:"+duration_hrs_value);
	            
	            String start_date_value = start_date.getText().toString().trim();
	            String start_time_value = start_time.getText().toString().trim();
	            System.out.println("start_date_value:"+start_date_value+" "+start_time_value);
	            
	            String end_date_value = end_date.getText().toString().trim();
	            String end_time_value = end_time.getText().toString().trim();
	            System.out.println("end_date_value:"+end_date_value+" "+end_time_value);
	            
	            String notes_value = notes.getText().toString().trim();
	            System.out.println("Notes_value:"+notes_value);  
	            
	            String tzone_sel = (String) tzoneSpn.getSelectedItem();
	            TimeZone tz = TimeZone.getTimeZone(tzone_sel);
	            ServiceProConstants.activityTimeZoneSelRawoffset = tz.getRawOffset();
	            /*ServiceProConstants.showLog("tz.getID: "+tz.getID());
	            ServiceProConstants.showLog("tz.getRawOffset: "+tz.getRawOffset());*/
				
	           	// converting the datestring from the picker to a long: 
	        	Calendar cal_stdate = Calendar.getInstance(); 
	        	cal_stdate.set(Calendar.DAY_OF_MONTH, startDay_dp); 
	        	cal_stdate.set(Calendar.MONTH, startMonth_dp-1); 
	        	cal_stdate.set(Calendar.YEAR, startYear_dp); 
	        	cal_stdate.set(Calendar.HOUR_OF_DAY, startHour_dp);
	        	cal_stdate.set(Calendar.MINUTE, startMinute_dp);
	        	Long lstartDate = cal_stdate.getTime().getTime(); 
	        	
	        	Calendar cal_enddate = Calendar.getInstance(); 
	        	cal_enddate.set(Calendar.DAY_OF_MONTH, endDay_dp); 
	        	cal_enddate.set(Calendar.MONTH, endMonth_dp-1); 
	        	cal_enddate.set(Calendar.YEAR, endYear_dp); 
	        	cal_enddate.set(Calendar.HOUR_OF_DAY, endHour_dp);
	        	cal_enddate.set(Calendar.MINUTE, endMinute_dp);
	        	Long lendDate = cal_enddate.getTime().getTime(); 
	        	/*ServiceProConstants.showLog("lendDate "+ lendDate);
	        	ServiceProConstants.showLog("end getActivityDateString "+ ServiceProConstants.getActivityDateString(lendDate, false));
	        	*/
	            String st_dat_time_value = start_date_value_cal+" "+start_time_value_cal;
	            String end_dat_time_value = end_date_value_cal+" "+end_time_value_cal;
	           
	            Date d1 = new Date(st_dat_time_value);   
	            Date d2 = new Date(end_dat_time_value); 
	            Calendar cal1 = Calendar.getInstance();cal1.setTime(d1);   
	            Calendar cal2 = Calendar.getInstance();cal2.setTime(d2); 
	            //ServiceProConstants.showLog("Calendar "+ calculateDays(d1, d2)); 
	            int datediff = (int) calculateDays(d1, d2);
	
	            int timediff = (int) ((int) cal2.getTimeInMillis() - cal1.getTimeInMillis());
	            //ServiceProConstants.showLog("Diff: "+ timediff);
	            
	            String choiceStr = "", hoursStr = "", strDateStr = "", endDateStr = "", notesStr = "", descStr ="";
	            int index4 = 0;
	            try{      
	            	if(timediff >= 0){
		                if(datediff >= 0){
		                    int index = activity_sp.getSelectedItemPosition();
		                    if(choicesArr != null){
		                        if(choicesArr.length > index)
		                            choiceStr = choicesArr[index];
		                    }
		                    
		                    index4 = choiceStr.indexOf(':');
		                    
		                    if(index4 >= 0)
		                    	descStr = choiceStr.substring(index4+1, choiceStr.length());
		                    
		                    if(index4 >= 0)
		                        choiceStr = choiceStr.substring(0, index4);

		                    System.out.println("choiceStr : "+choiceStr); 
		                    System.out.println("index4 : "+index4);  
		                    System.out.println("choiceStr.length() : "+choiceStr.length());  
		                    
		                    hoursStr = duration_hrs.getText().toString();
		                    hoursStr = hoursStr.trim();
		                    
		                    notesStr = notes.getText().toString();
		                    notesStr = notesStr.trim();
		                    
		                    System.out.println("choiceStr : "+choiceStr);
		                    System.out.println("hoursStr : "+hoursStr);
		                    System.out.println("notesStr : "+notesStr);   
		                    System.out.println("descStr : "+descStr);          
		                    
		                    if(ServiceProConstants.actColltVect != null){    
		                        if(activityObj == null)
		                            activityObj = new SOActivityClass();
		                        
		                        String NumberExtStr = activityObj.getNumberExtnStr().trim();
		                        if(NumberExtStr.equalsIgnoreCase("")){
		                            //The values 5000 are set on assumption and 10 is increment value
		                            int sizeInd = 0;
		                            if(editModeFlag == true)
		                                sizeInd = selIndex;
		                            else
		                                sizeInd = ServiceProConstants.actColltVect.size();
		                                
		                            int value = 5000 + (sizeInd * 10);
		                            NumberExtStr = String.valueOf(value);
		                        }
		                        
		                        System.out.println("Number Extn : "+NumberExtStr);
		                        
		                        activityObj.setNumberExtnStr(NumberExtStr);
		                        activityObj.setServiceDescStr(descStr);
		                        activityObj.setActivityStr(choiceStr);
		                        activityObj.setDurationStr(hoursStr);
		                        activityObj.setStartTimeStr(lstartDate);
		                        activityObj.setEndTimeStr(lendDate);
		                        activityObj.setNotesStr(notesStr);
		                        activityObj.setTimeZoneStr(tzone_sel);
		                        
		                        if(editModeFlag == true){
		                        	ServiceProConstants.actColltVect.setElementAt(activityObj, selIndex);
		                        }
		                        else{
		                        	ServiceProConstants.actColltVect.addElement(activityObj);
		                        }
		                    }   
		                    
		                    //flag setting for don't empty and adding for activity object.
		                    ServiceProConstants.activity_recall_flag = 1;
		                    finish();
		                    
		                    ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
		        			ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
		                     
		                    Intent activity_intent = new Intent(ServiceProActivityEditScreen.this, ServiceProActivityMainScreen.class);
		                    activity_intent.putExtra("docVectorObj", serVectObj1);
		                    activity_intent.putExtra("confVectorObj", serVectObj2);
		                 	activity_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    
		        			startActivity(activity_intent);
		                }
		                else{
		                    ServiceProConstants.showErrorDialog(ServiceProActivityEditScreen.this, getString(R.string.SERVICEORD_ACTSCR_ERR_DTLESSER));
		                }
	            	}
	                else{
	                    ServiceProConstants.showErrorDialog(ServiceProActivityEditScreen.this, getString(R.string.SERVICEORD_ACTSCR_ERR_DTLESSER));
	                }
	            }
	            catch(Exception asf){
	            	ServiceProConstants.showErrorLog("Error in onSaveAction : "+asf.toString());
	            }
	        }
		    catch(Exception asf){
		    	ServiceProConstants.showErrorLog("Error in done_btListener : "+asf.toString());
		    }
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                			startDateSetListener,
                			startYear_dp, startMonth_dp-1, startDay_dp);   
            case START_TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                			startTimeSetListener, startHour_dp, startMinute_dp, false);   
            case END_DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                            endDateSetListener,
                            endYear_dp, endMonth_dp-1, endDay_dp);
            case END_TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                			endTimeSetListener, endHour_dp, endMinute_dp, false);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(startYear_dp, startMonth_dp-1, startDay_dp);
                break;
            case END_DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(endYear_dp, endMonth_dp-1, endDay_dp);
                break;
        }
    }    

    private void updateDisplay_start_date() {    
    	try{
    		int month_value = startMonth + 1;
	    	String month_dec = ServiceProConstants.getMonthValue(month_value); 
	    	startDay_dp = startDay;
	    	startMonth_dp = month_value;
	    	startYear_dp = startYear;
	    	start_date_value_cal = month_value+"/"+startDay+"/"+startYear;    	
	    	
	    	String taskDateStrValue = ""; 
	    	taskDateStrValue = ServiceProConstants.getDateFormatForSAP(startYear_dp, startMonth_dp, startDay_dp, startHour_dp, startMinute_dp);
	    	if(taskDateStrValue.length() > 0 && taskDateStrValue != null){
	    		taskDateStrValue = taskDateStrValue;
	    		String dateStr = taskDateStrValue.toString();
	        	dateStr = dateStr.trim();
				//ServiceProConstants.showLog("DateStr1 : "+dateStr);
	        	if(!dateStr.equalsIgnoreCase("")){	                    		
	            	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
	    			//ServiceProConstants.showLog("DateStr2 : "+newdatestr);
	    			start_date.setText(" "+newdatestr);
	        	}
	        	else{
	        		start_date.setText(
	        	            new StringBuilder()
	        	                    // Month is 0 based so add 1
	        	            		.append(startDay).append(" ")
	        	                    .append(month_dec).append(" ")                    
	        	                    .append(startYear).append(" "));
	        	}
	    	}
	    	else{
	    		taskDateStrValue = "";
	    		start_date.setText(
	    	            new StringBuilder()
	    	                    // Month is 0 based so add 1
	    	            		.append(startDay).append(" ")
	    	                    .append(month_dec).append(" ")                    
	    	                    .append(startYear).append(" "));
	    	}	    	
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_start_date : "+asf.toString());
	    }
    }//fn updateDisplay_start_date
    
    private void updateDisplay_start_time() {
    	try{
	    	startHour_dp = startHour;
	    	startMinute_dp = startMinute;
	    	start_time_value_cal = startHour+":"+startMinute;
	    	start_time.setText(
	            new StringBuilder()
			            .append(pad(startHour)).append(":")
			            .append(pad(startMinute)));
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_start_time : "+asf.toString());
	    }
    }//fn updateDisplay_start_time
    
    private void updateDisplay_end_date() {    	
    	try{
	    	int end_month_value = endMonth + 1;
	    	String month_dec = ServiceProConstants.getMonthValue(end_month_value); 
	    	endDay_dp = endDay;
	    	endMonth_dp = end_month_value;
	    	endYear_dp = endYear;
	    	end_date_value_cal = end_month_value+"/"+endDay+"/"+endYear;    	
	    	
	    	String taskDateStrValue = ""; 
	    	taskDateStrValue = ServiceProConstants.getDateFormatForSAP(endYear_dp, endMonth_dp, endDay_dp, endHour_dp, endMinute_dp);
	    	if(taskDateStrValue.length() > 0 && taskDateStrValue != null){
	    		taskDateStrValue = taskDateStrValue;
	    		String dateStr = taskDateStrValue.toString();
	        	dateStr = dateStr.trim();
				//ServiceProConstants.showLog("DateStr1 : "+dateStr);
	        	if(!dateStr.equalsIgnoreCase("")){	                    		
	            	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
	    			//ServiceProConstants.showLog("DateStr2 : "+newdatestr);
	    			end_date.setText(" "+newdatestr);
	        	}
	        	else{
	        		end_date.setText(
	        	            new StringBuilder()
	        	                    // Month is 0 based so add 1
	        	            		.append(endDay).append(" ")
	        	                    .append(month_dec).append(" ")                    
	        	                    .append(endYear).append(" "));
	        	}
	    	}
	    	else{
	    		taskDateStrValue = "";
	    		end_date.setText(
	    	            new StringBuilder()
	    	                    // Month is 0 based so add 1
	    	            		.append(endDay).append(" ")
	    	                    .append(month_dec).append(" ")                    
	    	                    .append(endYear).append(" "));
	    	}
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_end_date : "+asf.toString());
	    }
    }//fn updateDisplay_end_date
    
    private void updateDisplay_end_time() {
    	try{
	    	endHour_dp = endHour;
	    	endMinute_dp = endMinute;
	    	end_time_value_cal = endHour+":"+endMinute;
	    	end_time.setText(
	            new StringBuilder()
			            .append(pad(endHour)).append(":")
			            .append(pad(endMinute)));
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_end_time : "+asf.toString());
	    }
    }//fn updateDisplay_end_time

    private DatePickerDialog.OnDateSetListener startDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	startYear = year;
                	startMonth = monthOfYear;
                	startDay = dayOfMonth;
                    updateDisplay_start_date();
                }
            };

    private TimePickerDialog.OnTimeSetListener startTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                	startHour = hourOfDay;
                	startMinute = minute;
                    updateDisplay_start_time();
                }
            };
            
    private DatePickerDialog.OnDateSetListener endDateSetListener =
		    new DatePickerDialog.OnDateSetListener() {		
		        public void onDateSet(DatePicker view, int year, int monthOfYear,
		                int dayOfMonth) {
		            endYear = year;
		            endMonth = monthOfYear;
		            endDay = dayOfMonth;
		            updateDisplay_end_date();
		        }
		    };         
		    
    private TimePickerDialog.OnTimeSetListener endTimeSetListener =
		    new TimePickerDialog.OnTimeSetListener() {		
		        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		        	endHour = hourOfDay;
		        	endMinute = minute;
		            updateDisplay_end_time();
		        }
		    };		 

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
        
    private void getActivityDetails(){
        try{
            if(editModeFlag == true){
                if(ServiceProConstants.actColltVect != null){
                    if(ServiceProConstants.actColltVect.size() > selIndex){
                        activityObj = (SOActivityClass)ServiceProConstants.actColltVect.elementAt(selIndex);
                    }
                }
            }            
            if(activityObj == null)
                activityObj = new SOActivityClass();
        }
        catch(Exception adf){
        	ServiceProConstants.showErrorLog("Error in getActivityDetails : "+adf.toString());
        }
    }//fn getActivityDetails
    
    private String[] getChoicesArray(){
        String choices[] = null;
        try{
            System.out.println("Size of choices : "+ServiceProConstants.productListVect.size());
            if(ServiceProConstants.productListVect != null){
                SOServiceActListOpConstraints serActListObj = null;
                String desc="", prdId = "", combStr="", activityId="";
                matchIndex = 0;
                if(activityObj != null){
                    activityId = activityObj.getActivityStr().trim();
                }
                
                choices = new String[ServiceProConstants.productListVect.size()];
                
                for(int h=0; h<ServiceProConstants.productListVect.size(); h++){
                    serActListObj = (SOServiceActListOpConstraints)ServiceProConstants.productListVect.elementAt(h);
                    if(serActListObj != null){
                        prdId = serActListObj.getProductId().trim();
                        desc = serActListObj.getProductDesc().trim();
                        combStr = prdId+":"+desc;
                        if(editModeFlag == true){
                            if(activityId.equalsIgnoreCase(prdId)){
                                matchIndex = h;
                                curActMatchStr = prdId;                                
                            }
                            else if(activityId.equalsIgnoreCase(combStr))
                                matchIndex = h;
                            notesMatchStr = desc;
                        }
                        //ServiceProConstants.showLog("combStr : "+combStr);
                        choices[h] = combStr;
                    }
                }
            }
        }
        catch(Exception sfg){
        	ServiceProConstants.showErrorLog("Error in getChoicesArray : "+sfg.toString());
        }
        System.out.println("Size of choices 2: "+choices.length);
        return choices;
    }//fn getChoicesArray
    
    private void prefillEditActivityValues(){
        try{
            if(activityObj != null){            	
            	notes.setText(activityObj.getNotesStr().trim());                
                String hourStr = activityObj.getDurationStr().trim();
                if(!hourStr.equalsIgnoreCase("")){
                	duration_hrs.setText(hourStr);
                }
                
                if(activityObj.getTimeZoneStr().length() != 0){
                	int position = 0;
                	for(int i =0;i<timeZoneArr.length;i++){
                		if(timeZoneArr[i].equals(activityObj.getTimeZoneStr().toString())){
                			position = i;
                		}
                	}
                	tzoneSpn.setSelection(position);
                }
                
                long parsedDate = activityObj.getStartTimeStr();
                if(parsedDate >= 0){
	                String startdate = ServiceProConstants.getActivityDateString(parsedDate, false);
	                int stdat1 = startdate.indexOf(" ");
	                int stdat2 = startdate.lastIndexOf(":");
	                String start_date_value = startdate.substring(0, stdat1);
	                String start_time_value = startdate.substring(stdat1+1, stdat2);
	                
	                //ServiceProConstants.showLog("getTaskDateString :"+ServiceProConstants.getTaskDateString(parsedDate, false));
	                String stdate_sav = ServiceProConstants.getTaskDateString(parsedDate, false);
	                int stdat1_sav = stdate_sav.indexOf(" ");
	                int stdat2_sav = stdate_sav.lastIndexOf(":");	                
	                String start_date_sav_value = stdate_sav.substring(0, stdat1_sav);
	                String start_time_sav_value = stdate_sav.substring(stdat1_sav+1, stdat2_sav);
	                String[] st_date_value = start_date_sav_value.split("-");	          
	                String[] st_time_value = start_time_sav_value.split(":");
	                
	                startYear_dp = Integer.parseInt(st_date_value[0]);
	                startMonth_dp = Integer.parseInt(st_date_value[1]);
	                startDay_dp = Integer.parseInt(st_date_value[2]);
	                startHour_dp = Integer.parseInt(st_time_value[0]);
	                startMinute_dp = Integer.parseInt(st_time_value[1]);
	                /*ServiceProConstants.showLog("startDay_dp :"+startDay_dp);
	                ServiceProConstants.showLog("startMonth :"+startMonth_dp);
	                ServiceProConstants.showLog("startYear_dp :"+startYear_dp);       */        
	                
	                String dateFromSAP = ServiceProConstants.getTaskDateStringFormat(parsedDate, false);
	                int dat1 = dateFromSAP.indexOf(" ");
	                String dateFromSAPValue = dateFromSAP.substring(0, dat1);
	                //ServiceProConstants.showLog("dateFromSAPValue :"+dateFromSAPValue);  
	                String dateStr = dateFromSAPValue.trim();
                	dateStr = dateStr.trim();
        			//ServiceProConstants.showLog("DateStr1 : "+dateStr);
                	if(!dateStr.equalsIgnoreCase("")){	                    		
                    	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
            			//ServiceProConstants.showLog("DateStr2 : "+newdatestr);
            			start_date.setText(" "+newdatestr);
                	}
                	start_date_value = start_date_value.replace("-", " ");
	                
	                /*ServiceProConstants.showLog("start_date :"+start_date_value);
	                ServiceProConstants.showLog("start_time :"+start_time_value);*/
	                //start_date.setText(start_date_value);
	                start_time.setText(start_time_value);
                }
                
                long parsedDate1 = activityObj.getEndTimeStr();
                if(parsedDate1 >= 1){
	                String enddate = ServiceProConstants.getActivityDateString(parsedDate1, false);
	                int enddat1 = enddate.indexOf(" ");
	                int enddat2 = enddate.lastIndexOf(":");
	                String end_time_value = enddate.substring(enddat1+1, enddat2);
	                String end_date_value = enddate.substring(0, enddat1);
	                
	                //ServiceProConstants.showLog("getTaskDateString :"+ServiceProConstants.getTaskDateString(parsedDate1, false));
	                String enddate_sav = ServiceProConstants.getTaskDateString(parsedDate1, false);
	                int enddat1_sav = enddate_sav.indexOf(" ");
	                int enddat2_sav = enddate_sav.lastIndexOf(":");	                
	                String end_date_sav_value = enddate_sav.substring(0, enddat1_sav);
	                String end_time_sav_value = enddate_sav.substring(enddat1_sav+1, enddat2_sav);
	                String[] end_date_value_spl = end_date_sav_value.split("-");	          
	                String[] end__time_value_spl = end_time_sav_value.split(":");
	               	                
	                endYear_dp = Integer.parseInt(end_date_value_spl[0]);
	                endMonth_dp = Integer.parseInt(end_date_value_spl[1]);
	                endDay_dp = Integer.parseInt(end_date_value_spl[2]);
	                endHour_dp = Integer.parseInt(end__time_value_spl[0]);
	                endMinute_dp = Integer.parseInt(end__time_value_spl[1]);
	                
	                String dateFromSAP = ServiceProConstants.getTaskDateStringFormat(parsedDate1, false);
	                int dat1 = dateFromSAP.indexOf(" ");
	                String dateFromSAPValue = dateFromSAP.substring(0, dat1);
	                //ServiceProConstants.showLog("dateFromSAPValue :"+dateFromSAPValue);  
	                String dateStr = dateFromSAPValue.trim();
                	dateStr = dateStr.trim();
        			//ServiceProConstants.showLog("DateStr1 : "+dateStr);
                	if(!dateStr.equalsIgnoreCase("")){	                    		
                    	String newdatestr = ServiceProConstants.getSystemDateFormatString(this, dateStr);
            			//ServiceProConstants.showLog("DateStr2 : "+newdatestr);
            			end_date.setText(" "+newdatestr);
                	}
	                
	                end_date_value = end_date_value.replace("-", " ");	                
	                
	                //end_date.setText(end_date_value);
	                end_time.setText(end_time_value);   
                }
            }
        }
        catch(Exception adfw){
        	ServiceProConstants.showErrorLog("Error in prefillEditActivityValues : "+adfw.toString());
        }
    }//fn prefillEditActivityValues
    
    public static long daysBetween(Calendar startDate, Calendar endDate) {  
    	Calendar date = (Calendar) startDate.clone();   
    	long daysBetween = 0;   
    	while (date.before(endDate)) {     
    		date.add(Calendar.DAY_OF_MONTH, 1);     
    		daysBetween++;   
    	}   
    	return daysBetween; 
    } 
    
    public static long calculateDays(Date dateEarly, Date dateLater) {   
    	return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000); 
    }    
    
}//End of class ServiceProActivityEditScreen