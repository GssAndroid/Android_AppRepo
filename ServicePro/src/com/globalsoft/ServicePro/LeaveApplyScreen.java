package com.globalsoft.ServicePro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.LeaveTypeOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProInputConstraints;

public class LeaveApplyScreen  extends Activity {
	
	private ArrayList leaveTypesList = new ArrayList();
	private String taskErrorMsgStr = "", selected_category = "";
	private String emailValueStr = "", leaveNotesStr = "";
	private boolean diagdispFlag = false, isConnAvail = true;
	private int selectedPos = 0;
	private long fromDatTime = 0, toDatTime = 0;
	
    private String[] arrChoices = {"Please Select"}, choicesArr;
    
	private ProgressDialog pdialog = null;		
	private SoapObject resultSoap = null;
	
	private Button doneBtn, cancelBtn;
	private EditText descEditText, notesEditText;
	public Spinner category_sp;
	public TextView start_date, end_date, start_time, end_time;
	
	private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMinute;
    /*
    private int startYear_dp;
    private int startMonth_dp;
    private int startDay_dp;
    private int startHour_dp;
    private int startMinute_dp;*/
    
    private int endYear;
    private int endMonth;
    private int endDay;
    private int endHour;
    private int endMinute;
    /*
    private int endYear_dp;
    private int endMonth_dp;
    private int endDay_dp;
    private int endHour_dp;
    private int endMinute_dp;*/
    
    private static final int START_DATE_DIALOG_ID = 1;
    private static final int START_TIME_DIALOG_ID = 2;    
    private static final int END_DATE_DIALOG_ID = 3;
    private static final int END_TIME_DIALOG_ID = 4;
	
    private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private SoapObject requestSoapObj = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	//requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
        	/*setTitle("Request Absence");
        	setContentView(R.layout.leavecreate);	
        	setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
        	
        	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.leavecreate); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText("Request Absence");

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
        	
			//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        	leaveTypesList = (ArrayList)this.getIntent().getSerializableExtra("leaveTypesList");
        	if(leaveTypesList != null)
        		ServiceProConstants.showLog("Leave Type Size : "+leaveTypesList.size());
        	
			initLayout();
			
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog("Error in Application Init: "+de.toString());
        }
    }
    
    private void initLayout(){
    	try{			
			doneBtn = (Button) findViewById(R.id.saveBtn);
			doneBtn.setOnClickListener(sendSAPBtnListener); 
			
			cancelBtn = (Button) findViewById(R.id.cancelBtn);
			cancelBtn.setOnClickListener(cancelBtnListener); 
						
			descEditText = (EditText)findViewById(R.id.emailET);
			notesEditText = (EditText)findViewById(R.id.notesET);			
			
			category_sp = (Spinner) findViewById(R.id.activity_sp);
			/*
			ArrayAdapter<String> leave_adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
			leave_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			category_sp.setAdapter(leave_adapter);
			category_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
	            	Object item = parent.getItemAtPosition(pos);
	            	selected_category = item.toString();
	            	selectedPos = pos;
	            } 
	
	            public void onNothingSelected(AdapterView<?> adapterView) {
	                return;
	            } 
	        }); 
	        */
			
			start_date = (TextView) findViewById(R.id.start_date);
	        start_date.setOnClickListener(start_dateListener);
	        
	        start_time = (TextView) findViewById(R.id.start_time);
	        start_time.setOnClickListener(start_timeListener);
	        
	        end_date = (TextView) findViewById(R.id.end_date);
	        end_date.setOnClickListener(end_dateListener);
	        
	        end_time = (TextView) findViewById(R.id.end_time);
	        end_time.setOnClickListener(end_timeListener);
	        
	        final Calendar c = Calendar.getInstance();
	        //initialize for start date
	        startYear = c.get(Calendar.YEAR);
	        startMonth = c.get(Calendar.MONTH);
	        startDay = c.get(Calendar.DAY_OF_MONTH);
	        startHour = c.get(Calendar.HOUR_OF_DAY);
	        startMinute = c.get(Calendar.MINUTE);
	        startHour = 00;
	        startMinute = 00;
	        /*
	        startYear_dp = startYear;
	        startMonth_dp = startMonth;
	        startDay_dp = startDay;
	        startHour_dp = startHour;
	        startMinute_dp = startMinute;
	        */	        
	        updateDisplay_start_date();
	        updateDisplay_start_time();
	        
	        //initialize for end date
	        endYear = c.get(Calendar.YEAR);
	        endMonth = c.get(Calendar.MONTH);
	        endDay = c.get(Calendar.DAY_OF_MONTH);
	        endHour = c.get(Calendar.HOUR_OF_DAY);
	        endMinute = c.get(Calendar.MINUTE);
	        endHour = 00;
	        endMinute = 00;
	        /*
	        endYear_dp = endYear;
	        endMonth_dp = endMonth;
	        endDay_dp = endDay;
	        endHour_dp = endHour;
	        endMinute_dp = endMinute;
	        */
	        updateDisplay_end_date();
	        updateDisplay_end_time();
	        
	        updateCategorySelection();
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in initLayout : "+sfg.toString());
    	}
    }//fn initLayout
    
    private void updateCategorySelection(){
    	int matchIndex = 0;
    	try{
    		choicesArr = getChoicesArray();
    	       
            if(choicesArr != null){     
            	ArrayAdapter<String> leave_adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choicesArr);
            	leave_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	        category_sp.setAdapter(leave_adapter);
    	        category_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    	            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
    	            	Object item = parent.getItemAtPosition(pos);
    	            	selected_category = item.toString();
    	            	selectedPos = pos;
    	            	ServiceProConstants.showLog("Selected Category : "+selected_category+" : "+selectedPos);
    	            } 
    	
    	            public void onNothingSelected(AdapterView<?> adapterView) {
    	                return;
    	            } 
    	        }); 
            }
            else{
            	ArrayAdapter<String> leave_adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
    			leave_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	        category_sp.setAdapter(leave_adapter);
            }
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in updateCategorySelection : "+sfg.toString());
    	}
    }//fn updateCategorySelection
    
    private String[] getChoicesArray(){
        String choices[] = null;
        try{
            if(leaveTypesList != null){
                LeaveTypeOpConstraints leaveCategory = null;
                String catg = "", combStr = "", desc="";
                choices = new String[leaveTypesList.size()+1];
                
                choices[0] = arrChoices[0];
                for(int h=0; h<leaveTypesList.size(); h++){
                	leaveCategory = (LeaveTypeOpConstraints)leaveTypesList.get(h);
                    if(leaveCategory != null){
                    	combStr = leaveCategory.getLeaveShortText().trim();
                    	/*catg = leaveCategory.getCategory().trim();
                    	desc = leaveCategory.getDescription().trim();
                    	combStr = catg+":"+desc;
                        ServiceProConstants.showLog("catg : "+catg+" : "+desc);*/
                        choices[h+1] = combStr;
                    }
                }
            }
        }
        catch(Exception sfg){
        	ServiceProConstants.showErrorLog("Error in getChoicesArray : "+sfg.toString());
        }
        return choices;
    }//fn getChoicesArray
    
    private String getSelectedLeaveTypeValue(){
    	String selected_type_code = "";
    	if(leaveTypesList != null){
    		int pos = selectedPos-1;
    		if((pos >= 0) && (pos < leaveTypesList.size())){
    			LeaveTypeOpConstraints leaveCategory = null;
    			leaveCategory = (LeaveTypeOpConstraints)leaveTypesList.get(pos);
    			if(leaveCategory != null){
    				//selected_type_code = leaveCategory.getLeaveCode().trim();
    				selected_type_code = leaveCategory.getLeaveType().trim();
    			}
    		}
    	}
    	return selected_type_code;
    }//fn getSelectedLeaveTypeValue
    
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
    
    private OnClickListener sendSAPBtnListener = new OnClickListener(){
        public void onClick(View v) {
        	//ServiceProConstants.showLog("Send Sap btn clicked");
        	doSendToSapAction();
        }
    };
    
    private OnClickListener cancelBtnListener = new OnClickListener(){
        public void onClick(View v) {
        	//ServiceProConstants.showLog("Cancel btn clicked");
        	onClose();
        }
    };
    
    private void onClose(){
    	try{
    		finish();
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in onClose : "+sfg.toString());
    	}
    }//fn onClose
    
    
    private void close(){
    	try{
    		System.gc();
			setResult(RESULT_OK); 
			this.finish();
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in close : "+sfg.toString());
    	}
    }//fn close
    
    private void doSendToSapAction(){
    	try{
    		if(selectedPos > 0){
    			selected_category = getSelectedLeaveTypeValue();
    			if(!selected_category.equalsIgnoreCase("")){
	    			if(descEditText != null)
	    				emailValueStr = descEditText.getText().toString().trim();
	    			
	    			if(notesEditText != null)
	    				leaveNotesStr = notesEditText.getText().toString().trim();
	    			
	    			if(!emailValueStr.equalsIgnoreCase("")){
	    				long smsDatTime = System.currentTimeMillis();
	    				
	    				GregorianCalendar fromCal = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMinute);
	            		fromDatTime = fromCal.getTimeInMillis();        	
	            		
	            		GregorianCalendar toCal = new GregorianCalendar(endYear, endMonth, endDay, endHour, endMinute);
	            		toDatTime = toCal.getTimeInMillis();         
	            		
	    				ServiceProConstants.showLog("Date : "+smsDatTime+" : "+fromDatTime+" : "+toDatTime);
	        	        if(fromDatTime <= toDatTime){
		    				initActivitySoapConnection();
	        	        }
	        	        else
	        	        	ServiceProConstants.showErrorDialog(this, "To date should be greater than from Date!");
	    			}
	    			else
	    				ServiceProConstants.showErrorDialog(this, "Enter Best Way to reach you!");
    			}
    			else
    				ServiceProConstants.showErrorDialog(this, "Select an Leave type to proceed!");
    		}
    		else
    			ServiceProConstants.showErrorDialog(this, "Select an Leave type to proceed!");
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in doSendToSapAction : "+sfg.toString());
    	}
    }//fn doSendToSapAction
    
    private void initActivitySoapConnection(){        
    	SoapSerializationEnvelope envelopeC = null;
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            ServiceProInputConstraints C0[];
            C0 = new ServiceProInputConstraints[5];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new ServiceProInputConstraints(); 
            }	            
            
            String fromDatStr = startYear+"-"+(startMonth+1)+"-"+startDay;
            String toDatStr  = endYear+"-"+(endMonth+1)+"-"+endDay;
            String fromHourStr = startHour+":"+startMinute+":00";
    		String toHourStr = endHour+":"+endMinute+":00";
            		
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            //C0[0].Cdata = "DEVICE-ID:200000000000000:DEVICE-TYPE:BB:APPLICATION-ID:SALESPRO";
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]EMPLOYEE-LEAVE-APPLICATION[.]VERSION[.]0";
            C0[3].Cdata = "DATA-TYPE[.]ZGSCCAST_EMPLYLEAVEAPPLCTNDATA[.]LEAVE_TYPE[.]BEGIN_DATE[.]BEGIN_TIME[.]END_DATE[.]END_TIME[.]ZZCNTCTDETAIL[.]ZZNOTE";
            C0[4].Cdata = "ZGSCCAST_EMPLYLEAVEAPPLCTNDATA[.]"+selected_category+"[.]"+fromDatStr+"[.]"+fromHourStr+"[.]"+toDatStr+"[.]"+toHourStr+"[.]"+emailValueStr+"[.]"+leaveNotesStr;
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
          
            doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
            //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL);
        }
        catch(Exception asd){
        	ServiceProConstants.showErrorLog("Error in initActivitySoapConnection : "+asd.toString());
        }
    }//fn initActivitySoapConnection
    
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
	    			updateLeaveCreationServerResponse(resultSoap);
	    		}
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };
    
    /*private void startNetworkConnection(final Context ctxAct, final SoapSerializationEnvelope envelopeCE, final String url){
    	try {
    		if(pdialog != null)
    			pdialog = null;
    		
			if(pdialog == null)
				this.runOnUiThread(new Runnable() {
                    public void run() {
                    	pdialog = ProgressDialog.show(ctxAct, "", "Please wait while processing...",true);
                    	new Thread() {
                    		public void run() {
                    			try{
                    				getSOAPViaHTTP(envelopeCE, url);
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
    
    private void getSOAPViaHTTP(SoapSerializationEnvelope envelopeCE, String url){		
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
                	String result = (envelopeCE.getResponse()).toString();
                	ServiceProConstants.showLog("Results : "+result);
                    
                    SoapObject result1 = (SoapObject)envelopeCE.bodyIn; 
                    ServiceProConstants.showLog("Results1 : "+result1.toString());
                    
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
                	updateLeaveCreationServerResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP
*/    
    
    private void updateLeaveCreationServerResponse(SoapObject soap){	
    	boolean resMsgErr = false;
        try{ 
        	if(soap != null){  
        		String delimeter = "[.]", result="", res="";
                SoapObject pii = null;
                String[] resArray = new String[37];
                int propsCount = 0, errCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0;   
                String soapMsg = soap.toString();
            	resMsgErr = ServiceProConstants.getSoapResponseSucc_Err(soapMsg); 
            	
                for (int i = 0; i < soap.getPropertyCount(); i++) {  
                    pii = (SoapObject)soap.getProperty(i);
                    propsCount = pii.getPropertyCount();
                    ServiceProConstants.showLog("propsCount : "+propsCount);
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                        	ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
                            if(j >= 4){
                                result = pii.getProperty(j).toString();
                                firstIndex = result.indexOf(delimeter);
                                firstIndex = firstIndex + 3;
                                result = result.substring(firstIndex);
                                
                                resC = 0;
                                indexA = 0;
                                indexB = result.indexOf(delimeter);
                                while (indexB != -1) {
                                    res = result.substring(indexA, indexB);
                                    resArray[resC] = res;
                                    indexA = indexB + delimeter.length();
                                    indexB = result.indexOf(delimeter, indexA);
                                    resC++;
                                }
                                int endIndex = result.lastIndexOf(';');
                                resArray[resC] = result.substring(indexA,endIndex);
                            }
                            else if(j < 4){
                                String errorMsg = pii.getProperty(j).toString();
                                ServiceProConstants.showLog("Inside J == "+j+" : "+propsCount+" : "+errorMsg);
                                int errorFstIndex = errorMsg.indexOf("Message=");
                                ServiceProConstants.showLog("Index : "+errorFstIndex+" : "+errorMsg.indexOf("Type=A")+" : "+errorMsg.indexOf("Type=E")+" : "+errorMsg.indexOf("Type=S"));
                                if(errorFstIndex > 0){
                                    if((errorMsg.indexOf("Type=A") > 0) || (errorMsg.indexOf("Type=E") > 0) || (errorMsg.indexOf("Type=S") > 0)){
                                        int errorLstIndex = errorMsg.indexOf(";", errorFstIndex);
                                        if(taskErrorMsgStr.equalsIgnoreCase(""))
                                            taskErrorMsgStr = errorMsg.substring((errorFstIndex+"Message=".length()), errorLstIndex).trim();
                                    }
                                    
                                    errCount = propsCount-2;
                                                                            
                                    ServiceProConstants.showErrorLog(taskErrorMsgStr);
                                    
                                    if(errCount < 0)
                                        errCount = 0;
                                    ServiceProConstants.showErrorLog("errCount : "+errCount+" : "+j);    
                                    
                                    if(j == errCount){
                                    	ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                                        diagdispFlag = true;
                                    }
                                }
                            }
                        }
                    }
                }
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateLeaveCreationServerResponse : "+sff.toString());
        } 
        finally{
        	try{
        		ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
            	if(!resMsgErr){
            		if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);                	
                	close();
            	}else{
            		onClose();
            	}/*
                if(taskErrorMsgStr.equalsIgnoreCase("")){
                	onClose();
                }
                else if(taskErrorMsgStr.indexOf("created") > 0){
                	if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                	
                	close();
                }*/
            }
            catch(Exception asf){}
        }
    }//fn updateLeaveCreationServerResponse 
    
    /*
    private void updateServerResponse(SoapObject soap){	
        try{ 
        	if(soap != null){  
        		String delimeter = "[.]", result="", res="";
                SoapObject pii = null;
                String[] resArray = new String[37];
                int propsCount = 0, errCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0;   
                
                for (int i = 0; i < soap.getPropertyCount(); i++) {  
                    pii = (SoapObject)soap.getProperty(i);
                    propsCount = pii.getPropertyCount();
                    ServiceProConstants.showLog("propsCount : "+propsCount);
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                        	ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
                            if(j >= 2){
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
                            else if(j < 1){
                                String errorMsg = pii.getProperty(j).toString();
                                ServiceProConstants.showLog("Inside J == "+j+" : "+propsCount+" : "+errorMsg);
                                int errorFstIndex = errorMsg.indexOf("Message=");
                                if(errorFstIndex > 0){
                                    if((errorMsg.indexOf("Type=A") > 0) || (errorMsg.indexOf("Type=E") > 0) || (errorMsg.indexOf("Type=S") > 0)){
                                        int errorLstIndex = errorMsg.indexOf(";", errorFstIndex);
                                        if(taskErrorMsgStr.equalsIgnoreCase(""))
                                            taskErrorMsgStr = errorMsg.substring((errorFstIndex+"Message=".length()), errorLstIndex).trim();
                                    }
                                    
                                    errCount = propsCount-2;
                                                                            
                                    ServiceProConstants.showErrorLog("taskErrorMsgStr : "+taskErrorMsgStr);
                                    
                                    if(errCount < 0)
                                        errCount = 0;
                                    ServiceProConstants.showErrorLog("errCount : "+errCount+" : "+j);    
                                    
                                    if(j == errCount){
                                    	ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                                        diagdispFlag = true;
                                    }
                                }
                            }
                        }
                    }
                }
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateServerResponse : "+sff.toString());
        } 
        finally{
        	try{
        		taskErrorMsgStr = taskErrorMsgStr.trim();
        		ServiceProConstants.showLog("Inside Finally : "+taskErrorMsgStr);
                if(taskErrorMsgStr.equalsIgnoreCase("")){
                	onClose();
                }
                else if((taskErrorMsgStr.indexOf("Application") > 0) || (taskErrorMsgStr.indexOf("created") > 0)){
                	if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                	
                	onClose();
                }
            }
            catch(Exception asf){}
        }
    }//fn updateServerResponse 
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
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                			startDateSetListener, startYear, startMonth, startDay);    
            case START_TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                			startTimeSetListener, startHour, startMinute, false);   
            case END_DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                            endDateSetListener, endYear, endMonth, endDay);
            case END_TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                			endTimeSetListener, endHour, endMinute, false);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(startYear, startMonth, startDay);
                break;
            case END_DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(endYear, endMonth, endDay);
                break;
        }
    }    
    
    private void updateDisplay_start_date() {    	
    	try{
	    	int month_value = startMonth + 1;
	    	//String month_dec = ServiceProConstants.getMonthValue(month_value);    
	    	String startDateStr = ServiceProConstants.getDateFormatForSystemDF(startDay,month_value,startYear);
	    	//ServiceProConstants.showLog("startDateStr1 : "+startDateStr);
	    	startDateStr = ServiceProConstants.getSystemDateFormatString(this, startDateStr);
	    	start_date.setText(startDateStr);
	    	/*start_date.setText(
		            new StringBuilder()
		                    // Month is 0 based so add 1
		            		.append(startDay).append(" ")
		                    .append(month_dec).append(" ")                    
		                    .append(startYear).append(" "));*/
    	}
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_start_date : "+asf.toString());
	    }
    }
    
    private void updateDisplay_start_time() {
    	try{
	    	start_time.setText(
		            new StringBuilder()
				            .append(pad(startHour)).append(":")
				            .append(pad(startMinute)));
    	}
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_start_time : "+asf.toString());
	    }
    }
    
    private void updateDisplay_end_date() {    	
    	try{
	    	int end_month_value = endMonth + 1;
	    	//String month_dec = ServiceProConstants.getMonthValue(end_month_value); 
	    	String endDateStr = ServiceProConstants.getDateFormatForSystemDF(endDay, end_month_value, endYear);
	    	//ServiceProConstants.showLog("startDateStr1 : "+startDateStr);
	    	endDateStr = ServiceProConstants.getSystemDateFormatString(this, endDateStr);
	    	end_date.setText(endDateStr);
	    	/*
	    	end_date.setText(
    	            new StringBuilder()
    	                    // Month is 0 based so add 1
    	            		.append(endDay).append(" ")
    	                    .append(month_dec).append(" ")                    
    	                    .append(endYear).append(" "));
	    	*/
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_end_date : "+asf.toString());
	    }
    }//fn updateDisplay_end_date
    
    private void updateDisplay_end_time() {
    	try{
	    	end_time.setText(
	            new StringBuilder()
			            .append(pad(endHour)).append(":")
			            .append(pad(endMinute)));
	    }
	    catch(Exception asf){
	    	ServiceProConstants.showErrorLog("Error in updateDisplay_end_time : "+asf.toString());
	    }
    }//fn updateDisplay_end_time
    
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    
    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	startYear = year;
        	startMonth = monthOfYear;
        	startDay = dayOfMonth;
            updateDisplay_start_date();
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	startHour = hourOfDay;
        	startMinute = minute;
            updateDisplay_start_time();
        }
    };
            
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {		
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            endYear = year;
            endMonth = monthOfYear;
            endDay = dayOfMonth;
            updateDisplay_end_date();
        }
    };         
		    
    private TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {		
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	endHour = hourOfDay;
        	endMinute = minute;
            updateDisplay_end_time();
        }
    };		
    
}//End of class LeaveApplyScreen
