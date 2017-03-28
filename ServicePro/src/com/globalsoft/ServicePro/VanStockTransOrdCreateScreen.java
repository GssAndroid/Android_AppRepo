package com.globalsoft.ServicePro;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.SapTasksInputConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkColStrOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkOpConstraints;

public class VanStockTransOrdCreateScreen extends Activity {
	
	public Spinner plantLocSP;
	public TextView reqBy_date, reqBy_time, materialIDTV, unitTV;
	public Button createBtn;
	public EditText quantityET, serialET, unitET;
	private ProgressDialog pdialog = null;
	
	private SoapObject resultSoap = null;
	
	// date and time
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMinute;
    
    static final int START_DATE_DIALOG_ID = 1;
    static final int START_TIME_DIALOG_ID = 2;    
    
    private VanStkOpConstraints stkCategory = null;
    private ArrayList stocksCollSrlVect = new ArrayList();
    private ArrayAdapter <String> stocksCollAdapter;
    
    private String[] arrChoices = {"------------------"};
    private String[] mattCodeArr;
    private String taskErrorMsgStr = "", mattPlantStr = "", matStrLocStr = "",  mattStr = "", mattQtyStr = "", reqByDatStr = "", prcsUnitStr = "";
    private boolean diagdispFlag = false;
    
    private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
			setContentView(R.layout.vanstocktrncrt);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);
			*/
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.vanstocktrncrt); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.VANSTO_ORDER_CRE_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			stkCategory = (VanStkOpConstraints) this.getIntent().getSerializableExtra("stkCategoryObj");
			//stocksCollSrlVect = (ArrayList)this.getIntent().getSerializableExtra("stocksCollVectObj");
			stocksCollSrlVect = ServiceProConstants.stocksCollSrlArrList;
			initLayout();
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	private void initLayout(){
		try {
			plantLocSP = (Spinner) findViewById(R.id.plantLoc_sp);
			
			quantityET = (EditText) findViewById(R.id.quantityET); 
			//serialET = (EditText) findViewById(R.id.serialET);
			//unitET = (EditText) findViewById(R.id.unitET);
			
			materialIDTV = (TextView) findViewById(R.id.materialIDTV);
			unitTV = (TextView) findViewById(R.id.unitTV);
			
			reqBy_date = (TextView) findViewById(R.id.req_date);
			reqBy_date.setOnClickListener(reqBy_dateListener);
	        
			reqBy_time = (TextView) findViewById(R.id.req_time);
			reqBy_time.setOnClickListener(reqBy_timeListener);
	        
			createBtn = (Button) findViewById(R.id.createBtn);
			createBtn.setOnClickListener(create_btnListener); 
			
			final Calendar c = Calendar.getInstance();
	        //initialize for start date
	        startYear = c.get(Calendar.YEAR);
	        startMonth = c.get(Calendar.MONTH);
	        startDay = c.get(Calendar.DAY_OF_MONTH);
	        startHour = c.get(Calendar.HOUR_OF_DAY);
	        startMinute = c.get(Calendar.MINUTE);
	        updateDisplay_start_date();
	        updateDisplay_start_time();
	        
	        mattCodeArr = getMaterialChoicesArray();
	        if(mattCodeArr != null){
	        	stocksCollAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mattCodeArr);		        
	        	stocksCollAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	plantLocSP.setAdapter(stocksCollAdapter);
	        }
	        
	        displayOtherMaterialDesc();
	        
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	private void displayOtherMaterialDesc(){
        try{
            if(stkCategory != null){
            	materialIDTV.setText(stkCategory.getMaterial().trim());
                /*if(serialET !=  null){
                	serialET.setText(stkCategory.getMaterial().trim());
                	serialET.setEnabled(false);
                }*/
            	unitTV.setText(stkCategory.getMeasureUnits().trim());
                /*if(unitET != null){
                	unitET.setText(stkCategory.getMeasureUnits().trim());  
                	unitET.setEnabled(false);
                }*/
            }
        }
        catch(Exception asff){
        	ServiceProConstants.showErrorLog("Error in displayOtherMaterialDesc : "+asff.toString());
        }
    }//fn displayOtherMaterialDesc
	
	private String[] getMaterialChoicesArray(){
        String choices[] = null;
        try{
            System.out.println("Size of choices : "+stocksCollSrlVect.size());
            if(stocksCollSrlVect != null){
                int arrSize = stocksCollSrlVect.size();
                choices = new String[arrSize];
                
                VanStkColStrOpConstraints stkColLocCategory = null;
                String combStr = "";
                for(int h=0; h<stocksCollSrlVect.size(); h++){
                    stkColLocCategory = null;
                    stkColLocCategory = (VanStkColStrOpConstraints)stocksCollSrlVect.get(h);
                    if(stkColLocCategory != null){
                        combStr = stkColLocCategory.getEmpPlant().trim()+" - "+stkColLocCategory.getEmpStorageLoc().trim()+"("+stkColLocCategory.getStorageLocDesc().trim()+")";
                    }
                    choices[h] = combStr;
                }
            }
        }
        catch(Exception sfg){
        	ServiceProConstants.showErrorLog("Error in getMaterialChoicesArray : "+sfg.toString());
        }
        
        return choices;
    }//fn getMaterialChoicesArray
	
	 //start date click listener
    private OnClickListener reqBy_dateListener = new OnClickListener(){
        public void onClick(View v){
        	showDialog(START_DATE_DIALOG_ID);
        }
    };
    
    //start time click listener
    private OnClickListener reqBy_timeListener = new OnClickListener(){
        public void onClick(View v){
        	showDialog(START_TIME_DIALOG_ID);
        }
    };

 
    //done btn click listener
    private OnClickListener create_btnListener = new OnClickListener(){
        public void onClick(View v) {
        	int index1 = 0, quantity = 0;
            VanStkColStrOpConstraints stkColLocCategory = null;
        	try{
                mattQtyStr = quantityET.getText().toString();
                mattQtyStr = mattQtyStr.trim();
                if(!mattQtyStr.equalsIgnoreCase(""))            
                    quantity = Integer.parseInt(mattQtyStr);
                    
                if(quantity > 0){
                    index1 = plantLocSP.getSelectedItemPosition();
                    if(index1 < 0)
                        index1 = 0;
                    System.out.println("Index 1 : "+index1);
                    
                    if(stocksCollSrlVect != null){
                        if(stocksCollSrlVect.size() > index1){
                            stkColLocCategory = (VanStkColStrOpConstraints)stocksCollSrlVect.get(index1);
                            if(stkColLocCategory != null){
                                mattPlantStr = stkColLocCategory.getEmpPlant().trim();
                                matStrLocStr = stkColLocCategory.getEmpStorageLoc().trim();
                            }
                        }
                    }
                    
                    mattStr = materialIDTV.getText().toString();
                    mattStr = mattStr.trim();
                    
                    prcsUnitStr = unitTV.getText().toString();
                    prcsUnitStr = prcsUnitStr.trim();
                    
                    /*
                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd H:mm");
                    String dateStr = startYear+"-"+startMonth+"-"+startDay+" "+startHour+":"+startMinute;
                    Date dateObj = curFormater.parse(dateStr);                    
                    reqByDatStr = String.valueOf(dateObj.getTime());
                    */
                    
                    reqByDatStr = startYear+"-"+(startMonth+1)+"-"+startDay;
                    //reqByDatStr = startYear+"-"+startMonth+"-"+startDay;
                    
                    initSoapConnection();
                }
                else
                	ServiceProConstants.showErrorDialog(VanStockTransOrdCreateScreen.this, "Quantity should not be empty");
                    
                /*ServiceProConstants.showLog("mattPlantStr : "+mattPlantStr);
                ServiceProConstants.showLog("matStrLocStr : "+matStrLocStr);
                ServiceProConstants.showLog("mattStr : "+mattStr);
                ServiceProConstants.showLog("mattQtyStr : "+mattQtyStr);
                ServiceProConstants.showLog("reqByDatStr : "+reqByDatStr);  
                ServiceProConstants.showLog("prcsUnitStr : "+prcsUnitStr);   */   
            }
            catch(Exception asf){
            	ServiceProConstants.showErrorLog("Error in onSaveAction : "+asf.toString());
            }
        }
    };
    
    private void initSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            SapTasksInputConstraints C0[];
            C0 = new SapTasksInputConstraints[5];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new SapTasksInputConstraints(); 
            }
            
            
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]VAN-STOCK-TRANSFER-ORDER[.]VERSION[.]0";
            C0[3].Cdata = "DATA-TYPE[.]ZGSXSMST_SRVCSTCKRQST20[.]WERKS[.]LGORT[.]NUMBER_EXT[.]PRODUCT_ID[.]QUANTITY[.]PROCESS_QTY_UNIT[.]ZZITEM_DESCRIPTI[.]ZZITEM_TEXT[.]WLDAT";
            C0[4].Cdata = "ZGSXSMST_SRVCSTCKRQST20[.]"+mattPlantStr+"[.]"+matStrLocStr+"[.][.]"+mattStr+"[.]"+mattQtyStr+"[.]"+prcsUnitStr+"[.][.][.]"+reqByDatStr;
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
          
            //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL);
            doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeC, request);
        }
        catch(Exception asd){
            ServiceProConstants.showErrorLog("Error in initSoapConnection : "+asd.toString());
        }
    }//fn initSoapConnection

    private void doThreadNetworkAction(Context ctx, final Handler handler, final Runnable handlerFnName, final SoapSerializationEnvelope envelopeC, SoapObject request){
    	try{
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
    			if(pdialog != null)
        			pdialog = null;
        		
    			if(pdialog == null)
    				VanStockTransOrdCreateScreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                        	pdialog = ProgressDialog.show(VanStockTransOrdCreateScreen.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
                        	new Thread() {
                        		public void run() {
                        			try{
                        				VanStockTransOrdCreateScreen.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                				updateStkTransResponse(resultSoap);                                               	
                                            }
                                        });
                        				sleep(2000);
                        			} catch (Exception e) {  }
                 				}
                        	}.start();
                        }
                    });
	    	} catch(Exception asegg){
	    		ServiceProConstants.showErrorLog("Error in getNetworkResponseRunnable : "+asegg.toString());
	    	}
	    }	    
    };		
    
    
    /*private void getSOAPViaHTTP(SoapSerializationEnvelope envelopeCE, String url){		
        try {                
            HttpTransportSE  androidHttpTransport = new HttpTransportSE (url);
            try{
            	androidHttpTransport.call(ServiceProConstants.SOAP_ACTION_URL, envelopeCE);
            	//androidHttpTransport.call("http://75.99.152.10:8050/sap/bc/srt/wsdl/bndg_E0B11D0D1D513AF1AE7900188B47B426/wsdl11/allinone/ws_policy/document?sap-client=110", envelopeCE);
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
                	updateStkTransResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP
*/    
    public void updateStkTransResponse(SoapObject soap){		
    	SapTasksInputConstraints category = null;
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
                   // ServiceProConstants.showLog("propsCount : "+propsCount);
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                        	//ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
                            if(j >= 4){
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
                            else if(j < 4){
                                String errorMsg = pii.getProperty(j).toString();
                                //ServiceProConstants.showLog("Inside J == "+j+" : "+propsCount+" : "+errorMsg);
                                int errorFstIndex = errorMsg.indexOf("Message=");
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
                                    System.out.println("errCount : "+errCount+" : "+j);    
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
            ServiceProConstants.showErrorLog("On updateStkTransResponse : "+sff.toString());
        } 
        finally{
        	try{
        		ServiceProConstants.showLog("resMsgErr : "+resMsgErr);
        		stopProgressDialog();
            	if(!resMsgErr){
            		if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                	
                	onClose();
            	}else{
            		onClose();
            	}
                /*if(taskErrorMsgStr.equalsIgnoreCase("")){
                    onClose();
                }
                else if(taskErrorMsgStr.indexOf("STO") == 0){
                	if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                	
                	onClose();
                }*/
            }
            catch(Exception asf){}
        }
    }//fn updateStkTransResponse  
    
    
    private void onClose(){
    	try {
			System.gc();
			setResult(RESULT_OK); 
			this.finish();
		} catch (Exception e) {
		}
    }//fn onClose
    
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
*/    
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                			startDateSetListener,
                			startYear, startMonth, startDay);   
            case START_TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                			startTimeSetListener, startHour, startMinute, false);   
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(startYear, startMonth, startDay);
                break;
        }
    }    

    private void updateDisplay_start_date() {    	
    	int month_value = startMonth + 1;
    	//String month_dec = ServiceProConstants.getMonthValue(month_value);    
    	String startDateStr = ServiceProConstants.getDateFormatForSystemDF(startDay,month_value,startYear);
    	//ServiceProConstants.showLog("startDateStr1 : "+startDateStr);
    	startDateStr = ServiceProConstants.getSystemDateFormatString(this, startDateStr);
    	reqBy_date.setText(startDateStr);
    	/*
    	reqBy_date.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
            		.append(startDay).append(" ")
                    .append(month_dec).append(" ")                    
                    .append(startYear).append(" "));
    	*/
    }
    
    private void updateDisplay_start_time() {
    	reqBy_time.setText(
            new StringBuilder()
		            .append(pad(startHour)).append(":")
		            .append(pad(startMinute)));
    }
    
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
            

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    
    public String getMonthValue(int month_value){
    	String month_dec = null;
    	if(month_value == 1){
    		month_dec = "Jan";
    	}
    	else if(month_value == 2){
    		month_dec = "Feb";
    	}
    	else if(month_value == 3){
    		month_dec = "Mar";
    	}
    	else if(month_value == 4){
    		month_dec = "Apr";
    	}
    	else if(month_value == 5){
    		month_dec = "May";
    	}
    	else if(month_value == 6){
    		month_dec = "Jun";
    	}
    	else if(month_value == 7){
    		month_dec = "Jul";
    	}
    	else if(month_value == 8){
    		month_dec = "Aug";
    	}
    	else if(month_value == 9){
    		month_dec = "Sep";
    	}
    	else if(month_value == 10){
    		month_dec = "Oct";
    	}
    	else if(month_value == 11){
    		month_dec = "Nov";
    	}
    	else if(month_value == 12){
    		month_dec = "Dec";
    	}
    	return month_dec;
    }
    
}//End of class VanStockTransOrdCreateScreen
