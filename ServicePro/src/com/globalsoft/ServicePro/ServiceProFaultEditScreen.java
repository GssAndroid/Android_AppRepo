package com.globalsoft.ServicePro;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.SOCodeGroupOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOCodeListOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Item.SOActivityClass;
import com.globalsoft.ServicePro.Item.SOFaultClass;

public class ServiceProFaultEditScreen extends Activity {
	
	public Spinner symptom_grp_sp, symptom_code_sp, problem_grp_sp, problem_code_sp, 
	cause_grp_sp, cause_code_sp;
	public String selected_symptom_grp, selected_symptom_code, selected_problem_grp, selected_problem_code, 
	selected_cause_grp, selected_cause_code;
	public EditText symptom_desc, problem_desc, cause_desc;
	public Button done_bt;
	
	private boolean editModeFlag = true;
    private int selIndex = 0, sympGrpMthIndex = 0, probGrpMthIndex = 0, causeGrpMthIndex = 0;    
    private int sympLstMthIndex = 0, probLstMthIndex = 0, causeLstMthIndex = 0;   
    private String[] arrChoices = {"------------------"};
    private String[] sympGrpArr, sympCodeArr, probGrpArr, probCodeArr, causeGrpArr, causeCodeArr;
    
    private SOFaultClass faultObj = null;
    private SOActivityClass actvObj = null;
    private ServiceProVectSerializable serObj1, serObj2;
    private Vector documentsVect, confVector;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*requestWindowFeature(Window.FEATURE_LEFT_ICON);*/
    	ServiceProConstants.setWindowTitleTheme(this);
		/*setContentView(R.layout.fault_edit);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.fault_edit); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SERVICEORD_FLTSCR_TITLE));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
		
		try{		
			serObj1 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("docVectorObj");	
		    serObj2 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("confVectorObj");
			selIndex = this.getIntent().getIntExtra("selIndex", -1);
	        editModeFlag = this.getIntent().getBooleanExtra("editflag", true);
	        actvObj = (SOActivityClass)this.getIntent().getSerializableExtra("actvObj");
	        
	        if(serObj1 != null){
		    	documentsVect = serObj1.getVector();
		    	ServiceProConstants.showLog("docVector size : "+documentsVect.size());
		    }
		    
		    if(serObj2 != null){
		    	confVector = serObj2.getVector();
		    	ServiceProConstants.showLog("confVector size : "+confVector.size());
		    }	            
	        getFaultCodeDetails();
	        
	        String editMatchStr1 = "", editMatchStr2 = "", editMatchStr3 = "";
	        if(editModeFlag == true){
	            if(faultObj != null){
	                editMatchStr1 = faultObj.getSymptomCodeStr().trim();
	                editMatchStr2 = faultObj.getProblemCodeStr().trim();
	                editMatchStr3 = faultObj.getCauseCodeStr().trim();
	            }
	        }
	        
	        sympGrpArr = getSymptomGroupChoices();
	        sympCodeArr = getCodeChoicesForFields(sympGrpArr, sympGrpMthIndex, ServiceProConstants.symptmCodeListVect, editMatchStr1, 0);
	        probGrpArr = getProblemGroupChoices();
	        probCodeArr = getCodeChoicesForFields(probGrpArr, probGrpMthIndex, ServiceProConstants.probCodeListVect, editMatchStr2, 1);
	        causeGrpArr = getCauseGroupChoices();
	        causeCodeArr = getCodeChoicesForFields(causeGrpArr, causeGrpMthIndex, ServiceProConstants.causeCodeListVect, editMatchStr3, 2);
	        
	        symptom_grp_sp = (Spinner) findViewById(R.id.symptom_grp_sp);
	        symptom_code_sp = (Spinner) findViewById(R.id.symptom_code_sp);
	        if(sympGrpArr != null){
	        	ArrayAdapter<String> symptom_grp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sympGrpArr);
	        	symptom_grp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        symptom_grp_sp.setAdapter(symptom_grp_adapter);
		        symptom_grp_sp.setSelection(sympGrpMthIndex);
		        symptom_grp_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_symptom_grp = item.toString();
		            	//sympLstMthIndex = 0;
		            	String match= ""; 
	                    sympGrpMthIndex = pos;
	                    sympCodeArr = getCodeChoicesForFields(sympGrpArr, sympGrpMthIndex, ServiceProConstants.symptmCodeListVect, match, 0);
	                    sympcodespCall();
	                    symptom_code_sp.invalidate();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        }); 
	        }
	        else{
	        	ArrayAdapter<String> symptom_grp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	        	symptom_grp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	symptom_grp_sp.setAdapter(symptom_grp_adapter);            
	        }        
	        
	        symptom_desc = (EditText) findViewById(R.id.symptom_desc); 
	        
	        problem_grp_sp = (Spinner) findViewById(R.id.problem_grp_sp);
	        problem_code_sp = (Spinner) findViewById(R.id.problem_code_sp);
	        if(probGrpArr != null){        	
	        	ArrayAdapter<String> problem_grp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, probGrpArr);
	        	problem_grp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	problem_grp_sp.setAdapter(problem_grp_adapter);
	        	problem_grp_sp.setSelection(probGrpMthIndex);
	        	problem_grp_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_problem_grp = item.toString();
		            	String match= ""; 
		            	//probLstMthIndex = 0;
	                    probGrpMthIndex = pos;
	                    probCodeArr = getCodeChoicesForFields(probGrpArr, probGrpMthIndex, ServiceProConstants.probCodeListVect, match, 1);
	                    probcodespCall();
	                    problem_code_sp.invalidate();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        });
	        }
	        else{
	        	ArrayAdapter<String> problem_grp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	        	problem_grp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	problem_grp_sp.setAdapter(problem_grp_adapter);
	        }        
	                
	        problem_desc = (EditText) findViewById(R.id.problem_desc); 
	                
	        cause_grp_sp = (Spinner) findViewById(R.id.cause_grp_sp);
	        cause_code_sp = (Spinner) findViewById(R.id.cause_code_sp); 
	        if(causeGrpArr != null){
	        	ArrayAdapter<String> cause_grp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, causeGrpArr);
	        	cause_grp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	cause_grp_sp.setAdapter(cause_grp_adapter);
	        	cause_grp_sp.setSelection(causeGrpMthIndex);
	        	cause_grp_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_cause_grp = item.toString();
		            	String match= "";
		            	//causeLstMthIndex = 0;
	                    causeGrpMthIndex = pos;
	                    causeCodeArr = getCodeChoicesForFields(causeGrpArr, causeGrpMthIndex, ServiceProConstants.causeCodeListVect, match, 2);
	                    causecodespCall();
	                    cause_code_sp.invalidate();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        });
	        }
	        else{
	        	ArrayAdapter<String> cause_grp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	        	cause_grp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	cause_grp_sp.setAdapter(cause_grp_adapter);
	        }        
	        cause_desc = (EditText) findViewById(R.id.cause_desc);
	        
	        done_bt = (Button) findViewById(R.id.done_bt);
	        done_bt.setOnClickListener(done_btListener); 
	        
	        if(editModeFlag == true)
	            prefillEditFaultValues();
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in oncreate : "+de.toString());
	    }
	}
	
	private void prefillEditFaultValues(){
        try{
            if(faultObj != null){
	            symptom_desc.setText(faultObj.getSymptomDescStr().trim());
	            problem_desc.setText(faultObj.getProblemDescStr().trim());
	            cause_desc.setText(faultObj.getCauseDescStr().trim());
            }
        }
        catch(Exception adfw){
        	ServiceProConstants.showErrorLog("Error in prefillEditFaultValues : "+adfw.toString());
        }
    }//fn prefillEditFaultValues
	
	private void sympcodespCall(){
		try{
			if(sympCodeArr != null){
				//ServiceProConstants.showLog("Match Index for Code : "+sympLstMthIndex+" : Size : "+sympCodeArr.length);
	            if((sympLstMthIndex < 0) || (sympLstMthIndex > sympCodeArr.length))
	                sympLstMthIndex = 0;
	            
	            ArrayAdapter<String> symptom_code_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sympCodeArr);
	            symptom_code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            symptom_code_sp.setAdapter(symptom_code_adapter);
	            symptom_code_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_symptom_code = item.toString();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        }); 
	        }
	        else{
	            sympLstMthIndex = 0;
	            ArrayAdapter<String> symptom_code_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	            symptom_code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            symptom_code_sp.setAdapter(symptom_code_adapter);   
	        }		
	        symptom_code_sp.setSelection(sympLstMthIndex);
	        symptom_code_sp.setSelected(true);
	        symptom_code_sp.invalidate();
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in sympcodespCall : "+de.toString());
	    }
	}//fn sympcodespCall
	
	private void probcodespCall(){
		try{
			if(probCodeArr != null){
	            if((probLstMthIndex < 0) || (probLstMthIndex > probCodeArr.length))
	                probLstMthIndex = 0;            
	            //ServiceProConstants.showLog("Match Index for Code : "+probLstMthIndex+" : Size : "+probCodeArr.length);
	            ArrayAdapter<String> problem_code_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, probCodeArr);
	            problem_code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            problem_code_sp.setAdapter(problem_code_adapter);
	            problem_code_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_problem_code = item.toString();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        });
	        }
	        else{
	            probLstMthIndex = 0;
	            ArrayAdapter<String> problem_code_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	            problem_code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            problem_code_sp.setAdapter(problem_code_adapter);
	        }
	        problem_code_sp.setSelection(probLstMthIndex);
			problem_code_sp.invalidate();
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in probcodespCall : "+de.toString());
	    }
	}//fn probcodespCall
	
	private void causecodespCall(){
		try{
		    if(causeCodeArr != null){
		        if((causeLstMthIndex < 0) || (causeLstMthIndex > causeCodeArr.length))
		            causeLstMthIndex = 0;
		        //ServiceProConstants.showLog("Match Index for Code : "+causeLstMthIndex+" : Size : "+causeCodeArr.length);
		        ArrayAdapter<String> cause_code_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, causeCodeArr);
		        cause_code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        cause_code_sp.setAdapter(cause_code_adapter);	        
		        cause_code_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_cause_code = item.toString();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        });    
		    }
		    else{
		        causeLstMthIndex = 0;
		        ArrayAdapter<String> cause_code_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
		        cause_code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        cause_code_sp.setAdapter(cause_code_adapter);            
		    }
		    cause_code_sp.setSelection(causeLstMthIndex);
		    cause_code_sp.invalidate();
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in causecodespCall : "+de.toString());
	    }
	}
	
	//done btn click listener
    private OnClickListener done_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            String sympGrpStr = "", sympCodeStr = "", sympDescStr = "", probGrpStr = "", probCodeStr = "", probDescStr = "";
            String causeGrpStr = "", causeCodeStr = "", causeDescStr = "";
            int index1 = 0, index2 = 0, index3 = 0, index4 = 0;
            try{                
                if(sympGrpArr != null){
                    if(sympGrpArr.length > sympGrpMthIndex)
                        sympGrpStr = sympGrpArr[sympGrpMthIndex];
                }
                
                index1 = symptom_code_sp.getSelectedItemPosition();
                if(index1 < 0)
                    index1 = 0;
                    
                if(sympCodeArr != null){
                    if(sympCodeArr.length > index1)
                        sympCodeStr = sympCodeArr[index1];
                }
                
                sympDescStr = symptom_desc.getText().toString().trim();
                sympDescStr = sympDescStr.trim();
                
                if(probGrpArr != null){
                    if(probGrpArr.length > probGrpMthIndex)
                        probGrpStr = probGrpArr[probGrpMthIndex];
                }
                
                index2 = problem_code_sp.getSelectedItemPosition();
                if(index2 < 0)
                    index2 = 0;
                    
                if(probCodeArr != null){
                    if(probCodeArr.length > index2)
                        probCodeStr = probCodeArr[index2];
                }
                
                probDescStr = problem_desc.getText().toString().trim();
                probDescStr = probDescStr.trim();
                
                if(causeGrpArr != null){
                    if(causeGrpArr.length > causeGrpMthIndex)
                        causeGrpStr = causeGrpArr[causeGrpMthIndex];
                }
                
                index3 = cause_code_sp.getSelectedItemPosition();
                if(index3 < 0)
                    index3 = 0;
                    
                if(causeCodeArr != null){
                    if(causeCodeArr.length > index3)
                        causeCodeStr = causeCodeArr[index3];
                }
                
                index4 = sympGrpStr.indexOf(':');
                if(index4 >= 0)
                    sympGrpStr = sympGrpStr.substring(0, index4);
                    
                index4 = sympCodeStr.indexOf(':');
                if(index4 >= 0)
                    sympCodeStr = sympCodeStr.substring(0, index4);
                    
                index4 = probGrpStr.indexOf(':');
                if(index4 >= 0)
                    probGrpStr = probGrpStr.substring(0, index4);
                System.out.println("prob Grp index4 : "+index4);
                    
                index4 = probCodeStr.indexOf(':');
                if(index4 >= 0)
                    probCodeStr = probCodeStr.substring(0, index4);
                    
                index4 = causeGrpStr.indexOf(':');
                System.out.println("Cause Grp index4 : "+index4);
                if(index4 >= 0)
                    causeGrpStr = causeGrpStr.substring(0, index4);
                    
                index4 = causeCodeStr.indexOf(':');
                if(index4 >= 0)
                    causeCodeStr = causeCodeStr.substring(0, index4);
                    
                causeDescStr = cause_desc.getText().toString();
                causeDescStr = causeDescStr.trim();                     
                
                if(ServiceProConstants.faultColltVect != null){    
                    if(faultObj == null)
                        faultObj = new SOFaultClass();
                    
                    faultObj.setActivityStr(getActivityValue());
                        
                    faultObj.setSymptomGroupStr(sympGrpStr);
                    faultObj.setSymptomCodeStr(sympCodeStr);
                    faultObj.setSymptomDescStr(sympDescStr);
                    faultObj.setProblemGroupStr(probGrpStr);
                    faultObj.setProblemCodeStr(probCodeStr);
                    faultObj.setProblemDescStr(probDescStr);
                    faultObj.setCauseGroupStr(causeGrpStr);
                    faultObj.setCauseCodeStr(causeCodeStr);
                    faultObj.setCauseDescStr(causeDescStr);
                    
                    if(editModeFlag == true){
                    	ServiceProConstants.faultColltVect.setElementAt(faultObj, selIndex);
                    }
                    else{
                    	ServiceProConstants.faultColltVect.addElement(faultObj);
                    }
                }
                
                finish();
                
                ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
    			ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
                 
                Intent fault_intent = new Intent(ServiceProFaultEditScreen.this, ServiceProFaultScreen.class);
                fault_intent.putExtra("docVectorObj", serVectObj1);
                fault_intent.putExtra("confVectorObj", serVectObj2);
                fault_intent.putExtra("radioChkIndex", selIndex);
                fault_intent.putExtra("actvObj", actvObj);		   
                fault_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    
    			startActivity(fault_intent);
            }
            catch(Exception asf){
            	ServiceProConstants.showErrorLog("Error in onSaveAction : "+asf.toString());
            }
        }
    };
    
    public String getActivityValue(){
        String actStr = "";
        try{
            if(actvObj != null){
                actStr = actvObj.getNumberExtnStr().trim();
            }            
            System.out.println("actStr value : "+actStr);
        }
        catch(Exception safg){
        	ServiceProConstants.showErrorLog("Error in getActivityValue : "+safg.toString());
        }
        return actStr;
    }//fn getActivityValue
    
    private void getFaultCodeDetails(){
        try{
            if(editModeFlag == true){
                if(ServiceProConstants.faultColltVect != null){
                    if(ServiceProConstants.faultColltVect.size() > selIndex){
                        faultObj = (SOFaultClass)ServiceProConstants.faultColltVect.elementAt(selIndex);
                    }
                }
            }            
            if(faultObj == null)
                faultObj = new SOFaultClass();
        }
        catch(Exception adf){
        	ServiceProConstants.showErrorLog("Error in getFaultCodeDetails : "+adf.toString());
        }
    }//fn getFaultCodeDetails
    
    private String[] getSymptomGroupChoices(){
        String choices[] = null;
        try{
            System.out.println("Size of choices : "+ServiceProConstants.symptmCodeGroupVect.size());
            if(ServiceProConstants.symptmCodeGroupVect != null){
                SOCodeGroupOpConstraints grpObj = null;
                String sympGrpStr = "", codeGrpStr="", codeGrpDescStr="", combStr="";
                if(faultObj != null){
                    sympGrpStr = faultObj.getSymptomGroupStr().trim();
                }
                
                choices = new String[ServiceProConstants.symptmCodeGroupVect.size()];
                
                for(int h=0; h<ServiceProConstants.symptmCodeGroupVect.size(); h++){
                    grpObj = (SOCodeGroupOpConstraints)ServiceProConstants.symptmCodeGroupVect.elementAt(h);
                    if(grpObj != null){
                        codeGrpStr = grpObj.getCodeGroup().trim();
                        codeGrpDescStr = grpObj.getCodeGroupDesc().trim();
                        combStr = codeGrpStr+":"+codeGrpDescStr;
                        if(editModeFlag == true){
                            if(sympGrpStr.equalsIgnoreCase(codeGrpStr))
                                sympGrpMthIndex = h;
                            else if(sympGrpStr.equalsIgnoreCase(combStr))
                                sympGrpMthIndex = h;
                        }
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
    }//fn getSymptomGroupChoices
    
    
    private String[] getProblemGroupChoices(){
        String choices[] = null;
        try{
            System.out.println("Size of choices : "+ServiceProConstants.probCodeGroupVect.size());
            if(ServiceProConstants.probCodeGroupVect != null){
                SOCodeGroupOpConstraints grpObj = null;
                String sympGrpStr = "", codeGrpStr="", codeGrpDescStr="", combStr="";
                if(faultObj != null){
                    sympGrpStr = faultObj.getProblemGroupStr().trim();
                }
                
                choices = new String[ServiceProConstants.probCodeGroupVect.size()];
                
                for(int h=0; h<ServiceProConstants.probCodeGroupVect.size(); h++){
                    grpObj = (SOCodeGroupOpConstraints)ServiceProConstants.probCodeGroupVect.elementAt(h);
                    if(grpObj != null){
                        codeGrpStr = grpObj.getCodeGroup().trim();
                        codeGrpDescStr = grpObj.getCodeGroupDesc().trim();
                        combStr = codeGrpStr+":"+codeGrpDescStr;
                        if(editModeFlag == true){
                            if(sympGrpStr.equalsIgnoreCase(codeGrpStr))
                                probGrpMthIndex = h;
                            else if(sympGrpStr.equalsIgnoreCase(combStr))
                                probGrpMthIndex = h;
                        }
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
    }//fn getProblemGroupChoices
    
    
    private String[] getCauseGroupChoices(){
        String choices[] = null;
        try{
            System.out.println("Size of choices : "+ServiceProConstants.causeCodeGroupVect.size());
            if(ServiceProConstants.causeCodeGroupVect != null){
                SOCodeGroupOpConstraints grpObj = null;
                String sympGrpStr = "", codeGrpStr="", codeGrpDescStr="", combStr="";
                if(faultObj != null){
                    sympGrpStr = faultObj.getCauseGroupStr().trim();
                }
                
                choices = new String[ServiceProConstants.causeCodeGroupVect.size()];
                
                for(int h=0; h<ServiceProConstants.causeCodeGroupVect.size(); h++){
                    grpObj = (SOCodeGroupOpConstraints)ServiceProConstants.causeCodeGroupVect.elementAt(h);
                    if(grpObj != null){
                        codeGrpStr = grpObj.getCodeGroup().trim();
                        codeGrpDescStr = grpObj.getCodeGroupDesc().trim();
                        combStr = codeGrpStr+":"+codeGrpDescStr;
                        if(editModeFlag == true){
                            if(sympGrpStr.equalsIgnoreCase(codeGrpStr))
                                causeGrpMthIndex = h;
                            else if(sympGrpStr.equalsIgnoreCase(combStr))
                                causeGrpMthIndex = h;
                        }
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
    }//fn getCauseGroupChoices
    
    private String[] getCodeChoicesForFields(String grpChoices[], int matIndex, Vector listVect, String editMatchStr, int valIndex){
        String choices[] = null;
        String grpStr = "", codeStr="";
        Vector subListVect = new Vector();
        try{
            if(grpChoices != null){
                if(matIndex < grpChoices.length){
                    grpStr = grpChoices[matIndex].trim();
                    System.out.println("Selected Grpstr 1 : "+grpStr);
                    String[] grps = grpStr.split(":");
                    grpStr = grps[0].trim();
                    System.out.println("Selected Grpstr 2 : "+grpStr);
                    
                    SOCodeListOpConstraints codeListObj = null;
                    String grpCode = "", combStr = "";
                    if(listVect != null){
                        for(int i=0; i<listVect.size(); i++){
                            codeListObj = (SOCodeListOpConstraints)listVect.elementAt(i);
                            if(codeListObj != null){
                                grpCode = codeListObj.getCodeGroup().trim();
                                if(grpStr.equalsIgnoreCase(grpCode)){
                                    codeStr = codeListObj.getCode().trim();
                                    combStr = codeStr+":"+codeListObj.getCodeGroupDesc().trim();
                                    System.out.println("combStr : "+combStr);
                                    subListVect.addElement(combStr);
                                    if(editModeFlag == true){
                                        if(!editMatchStr.equalsIgnoreCase("")){
                                            if(editMatchStr.equalsIgnoreCase(codeStr)){
                                                setMatchedIndices(valIndex, (subListVect.size()-1));
                                            }
                                            else if(editMatchStr.equalsIgnoreCase(combStr)){
                                                setMatchedIndices(valIndex, (subListVect.size()-1));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            choices = new String[subListVect.size()];
            subListVect.copyInto(choices);
            System.out.println("Size : "+subListVect.size());
        }
        catch(Exception sgf){
        	ServiceProConstants.showErrorLog("Error in getCodeChoicesForFields : "+sgf.toString());
        }
        return choices;
    }//fn getCodeChoicesForFields
    
    private void setMatchedIndices(int index, int value){
        try{
            switch(index){
                case 0: 
                    sympLstMthIndex = value;
                    break;
                case 1:
                    probLstMthIndex = value;
                    break;
                case 2:
                    causeLstMthIndex = value;
                    break;
                default:
                    sympLstMthIndex = 0;
                    probLstMthIndex = 0;
                    causeLstMthIndex = 0;   
                    break;
            }
        }
        catch(Exception dgd){
        	ServiceProConstants.showErrorLog("Error in setMatchedIndices : "+dgd.toString());
        }
    }//fn setMatchedIndices    
    
}//End of class ServiceProFaultEditScreen