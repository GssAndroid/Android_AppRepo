package com.globalsoft.ServicePro;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.SOMattEmpListOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProVectSerializable;
import com.globalsoft.ServicePro.Item.SOSparesClass;

public class ServiceProSparesEditScreen extends Activity {
	
	public Spinner material_sp, unit_sp;
	public String selected_material, selected_unit;
	public Button done_bt;
	public ImageButton scan_bt;
	private TextView material_descTV, other_unitTV, material_tv, unit_tv;
	public EditText material_descET, qty, other_unitET, serial;
	private int selIndex = 0, mattCodeIndex = 0, mattUnitIndex = 0; 
	private String[] arrChoices = {"------------------"};
    private String[] mattCodeArr, mattUnitArr;
    private boolean editModeFlag = true, prevalEditFlag = false;
    private String serialValue = "";
    private LinearLayout other_unitll;
    
	private ServiceProVectSerializable serObj1, serObj2;
    private Vector documentsVect, confVector;
    private SOSparesClass spareObj = null;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
    	ServiceProConstants.setWindowTitleTheme(this);
		setContentView(R.layout.spares_edit);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
		
		ServiceProConstants.setWindowTitleTheme(this);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.spares_edit); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
		TextView myTitle = (TextView) findViewById(R.id.myTitle);
		myTitle.setText(getResources().getString(R.string.SERVICEORD_COMMON_SPARES));

		int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
		if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
			myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
		}
		
		try{
			serObj1 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("docVectorObj");	
		    serObj2 = (ServiceProVectSerializable)this.getIntent().getSerializableExtra("confVectorObj");
			selIndex = this.getIntent().getIntExtra("selIndex", -1);
	        editModeFlag = this.getIntent().getBooleanExtra("editflag", true);
	        
	        if(serObj1 != null){
		    	documentsVect = serObj1.getVector();
		    	ServiceProConstants.showLog("docVector size : "+documentsVect.size());
		    }
		    
		    if(serObj2 != null){
		    	confVector = serObj2.getVector();
		    	ServiceProConstants.showLog("confVector size : "+confVector.size());
		    }	    
	        /*ServiceProConstants.showLog("selIndex:"+selIndex);
	        ServiceProConstants.showLog("editModeFlag:"+editModeFlag);*/
	        
	        getMaterialDetails();
	        
	        mattCodeArr = getMaterialChoicesArray();    
	        mattUnitArr = getMaterialUnitChoicesArray(mattCodeArr);          
	        material_sp = (Spinner) findViewById(R.id.material_sp);
	        unit_sp = (Spinner) findViewById(R.id.unit_sp);
	        
	        if(mattCodeArr != null){
	        	ArrayAdapter<String> matt_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mattCodeArr);
	        	matt_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	material_sp.setAdapter(matt_adapter);
	        	material_sp.setSelection(mattCodeIndex);
	        	material_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_material = item.toString();
		            	String match= ""; 
		            	mattCodeIndex = pos;
		            	mattUnitArr = getMaterialUnitChoicesArray(mattCodeArr);
	                    unit_sp_call();
	                    unit_sp.invalidate();
	                    displayOtherMaterialDesc();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        });
	        }else{
	        	ArrayAdapter<String> matt_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	        	matt_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	material_sp.setAdapter(matt_adapter);
	        }        
	        material_tv = (TextView) findViewById(R.id.material_tv);
	        unit_tv = (TextView) findViewById(R.id.unit_tv);
	        material_descET = (EditText) findViewById(R.id.material_descET); 
	        material_descTV = (TextView) findViewById(R.id.material_descTV);
	        qty = (EditText) findViewById(R.id.qty); 
	        other_unitTV = (TextView) findViewById(R.id.other_unitTV);
	        other_unitET = (EditText) findViewById(R.id.other_unitET); 
	        other_unitll = (LinearLayout) findViewById(R.id.other_unitll);
	        serial = (EditText) findViewById(R.id.serial); 
	        
	        done_bt = (Button) findViewById(R.id.done_bt);
	        done_bt.setOnClickListener(done_btListener); 
	        
	        scan_bt = (ImageButton) findViewById(R.id.barcodeBtn);
	        scan_bt.setOnClickListener(scan_btListener); 
	        
	        if(editModeFlag == true)
	            prefillEditMaterialValues();
	        else{
	            displayOtherMaterialDesc();
	        }
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in oncreate : "+de.toString());
	    }
	}
	
	private void prefillEditMaterialValues(){
        try{
            if(spareObj != null){
                String quantStr = spareObj.getQuantityStr().trim();
                String materialDescStr = spareObj.getMaterialDescStr().trim();
                
                if(materialDescStr != null && !materialDescStr.equalsIgnoreCase("")){
                	material_descTV.setVisibility(View.VISIBLE);
                	material_descET.setVisibility(View.GONE);
                	material_descET.setText("");
                	material_descTV.setText(materialDescStr);
                }else{
                	material_descTV.setVisibility(View.GONE);
                	material_descTV.setText("");
                	material_descET.setVisibility(View.VISIBLE);
                }                
                
                if(!quantStr.equalsIgnoreCase(""))
                	qty.setText(quantStr);
                    
                serial.setText(spareObj.getSerialNoStr().trim());
                
                ServiceProConstants.showLog("selected index value for material: "+mattCodeArr[mattCodeIndex]);
                ServiceProConstants.showLog("selected index value for unit: "+mattUnitArr[mattUnitIndex]);
                /*if(prevalEditFlag == true){
                	material_sp.setEnabled(false);
                	unit_sp.setEnabled(false);
                }*/
                
                if(prevalEditFlag == true){
                	material_sp.setEnabled(false);                	
                	material_sp.setVisibility(View.GONE);
                	
                	unit_sp.setEnabled(false);
                	unit_sp.setVisibility(View.GONE);
                	
                	material_tv.setVisibility(View.VISIBLE);
                	material_tv.setText(mattCodeArr[mattCodeIndex]);
                	
                	unit_tv.setVisibility(View.VISIBLE);
                	unit_tv.setText(mattUnitArr[mattUnitIndex]);
                }else{                	
                	material_tv.setVisibility(View.GONE);
                	unit_tv.setVisibility(View.GONE);
                	material_sp.setVisibility(View.VISIBLE);
                	material_sp.setEnabled(true);
                	unit_sp.setVisibility(View.VISIBLE);
                	unit_sp.setEnabled(true);
                }
            }
        }
        catch(Exception adfw){
        	ServiceProConstants.showErrorLog("Error in prefillEditMaterialValues : "+adfw.toString());
        }
    }//fn prefillEditMaterialValues
	
	private void displayOtherMaterialDesc(){
        try{
            if(ServiceProConstants.mattEmployeeListVect != null){
                /*if(material_desc != null){
                	material_desc.setEnabled(true);
                	material_desc.setText("");
                	material_desc.setClickable(true);
                	material_desc.setFocusable(true);
                }*/
                material_descTV.setVisibility(View.GONE);
                material_descTV.setText("");
            	material_descET.setVisibility(View.VISIBLE);
                            	
            	other_unitTV.setVisibility(View.GONE);
            	other_unitTV.setText("");
                other_unitET.setVisibility(View.VISIBLE);
                other_unitll.setVisibility(View.VISIBLE);
                
                /*if(other_unitET != null){
                	other_unitET.setEnabled(true);
                	other_unitET.setText("");
                	other_unitET.setClickable(true);
                	other_unitET.setFocusable(true);
                }*/
                    
                String tempStr = "", subStr = "";
                System.out.println("mattCodeIndex : "+mattCodeIndex+" : "+ServiceProConstants.mattEmployeeListVect.size());
                if(mattCodeIndex != ServiceProConstants.mattEmployeeListVect.size()){
                    tempStr = mattCodeArr[mattCodeIndex];
                    int index1 = tempStr.lastIndexOf(':');
                    subStr = tempStr.substring(index1+1, tempStr.length());
                    /*if(material_desc != null){
                    	material_desc.setText(subStr);
                    	material_desc.setEnabled(false);
                    	material_desc.setClickable(false);
                    	material_desc.setFocusable(false);
                    }*/
                    material_descTV.setVisibility(View.VISIBLE);
                    material_descET.setText("");
                	material_descET.setVisibility(View.GONE);
                    material_descTV.setText(subStr);
                	
                    other_unitTV.setVisibility(View.VISIBLE);
                	other_unitTV.setText("");
                    other_unitET.setVisibility(View.GONE);
                    other_unitET.setText("");
                    other_unitll.setVisibility(View.GONE);
                    
                    /*if(other_unitET != null){
                    	other_unitET.setEnabled(false);
                    	other_unitET.setClickable(false);
                    	other_unitET.setFocusable(false);
                    }*/
                }
            }
        }
        catch(Exception asff){
        	ServiceProConstants.showErrorLog("Error in displayOtherMaterialDesc : "+asff.toString());
        }
    }//fn displayOtherMaterialDesc	
		
	private void unit_sp_call(){
		try{
			if(mattUnitArr != null){
	        	ArrayAdapter<String> unit_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mattUnitArr);
	        	unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	unit_sp.setAdapter(unit_adapter);
	        	unit_sp.setSelection(mattUnitIndex);
	        	unit_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) { 
		            	Object item = parent.getItemAtPosition(pos);
		            	selected_unit = item.toString();
		            } 
		
		            public void onNothingSelected(AdapterView<?> adapterView) {
		                return;
		            } 
		        });
	        }else{
	        	ArrayAdapter<String> unit_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoices);
	        	unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	unit_sp.setAdapter(unit_adapter); 
	        }
		}
		catch (Exception de) {
	    	ServiceProConstants.showErrorLog("Error in unit_sp_call : "+de.toString());
	    }
	}//fn unit_sp_call
		
	//barcode btn click listener
    private OnClickListener scan_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	System.out.println("Barcode btn clicked!");
	        	serialValue = serial.getText().toString().trim();
	        	Intent intent = new Intent(ServiceProSparesEditScreen.this, CaptureActivity.class);
				startActivityForResult(intent,ServiceProConstants.BARCODE_MAIN_SCREEN);
	        }
			catch (Exception de) {
		    	ServiceProConstants.showErrorLog("Error in scan_btListener : "+de.toString());
		    }
        }
    };
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
		  if(resultCode==RESULT_OK && requestCode==ServiceProConstants.BARCODE_MAIN_SCREEN){
			  try{
				  String msg = data.getStringExtra("selected_barcode");
				  if(msg.length() != 0){
					  String serialText = serial.getText().toString().trim();
					  if(serialText.length() !=0){						  
						  serial.setText(serialValue+","+msg);
					  }else{
						  serial.setText(msg);
					  }					  
				  }
				  Log.e("selected_barcode:",""+msg);
			  } 
			  catch (Exception e) {
			      Log.e("selected_barcode:", e.toString());
			  }
		  }	
	}
    
	//done btn click listener
    private OnClickListener done_btListener = new OnClickListener()
    {
        public void onClick(View v)
        {
        	try{
	        	System.out.println("Selected_material:"+selected_material);
	        	//System.out.println("Material Desc:"+material_desc.getText().toString().trim());
	        	System.out.println("Qty:"+qty.getText().toString().trim());
	        	//System.out.println("Other Unit:"+other_unitET.getText().toString().trim());
	        	System.out.println("Serial :"+serial.getText().toString().trim());
	        	
	        	String mattCodeStr = "", mattUnitStr = "", mattDescStr = "", mattQtyStr = "", serialNoStr = "", numberExtStr = "";
	            int index1=0, index2=0, index3=0; 
	            try{
	                if(prevalEditFlag == false){
	                    index1 = material_sp.getSelectedItemPosition();
	                    if(index1 < 0)
	                        index1 = 0;
	                    System.out.println("Index 1 : "+index1);
	                    if(mattCodeArr != null){
	                        if(mattCodeArr.length > index1)
	                            mattCodeStr = mattCodeArr[index1];
	                    }
	                    
	                    index2 = unit_sp.getSelectedItemPosition();            
	                    if(index2 < 0)
	                        index2 = 0;
	                        
	                    System.out.println("Index 2 : "+index2);
	                    if(mattUnitArr != null){
	                        if(mattUnitArr.length > index2)
	                            mattUnitStr = mattUnitArr[index2];
	                    }
	                    
	                    index3 = mattCodeStr.lastIndexOf(':');
	                    if(index3 >= 0)
	                        mattCodeStr = mattCodeStr.substring(0, index3);
	                        
	                    if(mattUnitStr.equalsIgnoreCase("")){
	                        //mattUnitStr = other_unitET.getText().toString();	                        
	    	                if(other_unitET.getText().toString().trim() != null && other_unitET.getText().toString().trim().length() > 0){
	    	                	mattUnitStr = other_unitET.getText().toString().trim();
	    	                }else if(other_unitTV.getText().toString().trim() != null && other_unitTV.getText().toString().trim().length() > 0){
	                        	mattUnitStr = other_unitTV.getText().toString().trim();
	    	                }else{
	    	                	mattUnitStr = "";
	    	                }
	                    }
	                }
	                else{
	                    if(spareObj != null){
	                        mattUnitStr = spareObj.getUnitsStr().trim();
	                        mattCodeStr = spareObj.getMaterialStr().trim();
	                        numberExtStr = spareObj.getNumberExtStr().trim();
	                    }
	                }
	                        
	                String materialStr = "";
	                if(material_descTV.getText().toString().trim() != null && material_descTV.getText().toString().trim().length() > 0){
	                	materialStr = material_descTV.getText().toString().trim();
	                }
	                else if(material_descET.getText().toString().trim() != null && material_descET.getText().toString().trim().length() > 0){
	                	materialStr = material_descET.getText().toString().trim();
	                }else{
	                	materialStr = "";
	                }
	                
	                mattDescStr = materialStr;
	                mattDescStr = mattDescStr.trim();
	                    
	                mattQtyStr = qty.getText().toString();
	                mattQtyStr = mattQtyStr.trim();
	                       
	                serialNoStr = serial.getText().toString();
	                serialNoStr = serialNoStr.trim();
	                    
	                System.out.println("mattCodeStr : "+mattCodeStr);
	                System.out.println("mattDescStr : "+mattDescStr);
	                System.out.println("mattQtyStr : "+mattQtyStr);
	                System.out.println("mattUnitStr : "+mattUnitStr);
	                System.out.println("serialNoStr : "+serialNoStr);
	                System.out.println("numberExtStr : "+numberExtStr);
	                
	                if(ServiceProConstants.sparesColltVect != null){    
	                    if(spareObj == null)
	                        spareObj = new SOSparesClass();
	                    
	                    spareObj.setMaterialStr(mattCodeStr);
	                    spareObj.setMaterialDescStr(mattDescStr);
	                    spareObj.setQuantityStr(mattQtyStr);
	                    spareObj.setUnitsStr(mattUnitStr);
	                    spareObj.setSerialNoStr(serialNoStr);
	                    spareObj.setNumberExtStr(numberExtStr);
	                    
	                    if(editModeFlag == true){
	                    	ServiceProConstants.sparesColltVect.setElementAt(spareObj, selIndex);
	                    }
	                    else{
	                    	ServiceProConstants.sparesColltVect.addElement(spareObj);
	                    }
	                }                
	                
	                finish();
	                                    			
	    			Intent spares_intent = new Intent(ServiceProSparesEditScreen.this, ServiceProSparesScreen.class);				        		
	        		ServiceProVectSerializable serVectObj1 = new ServiceProVectSerializable(documentsVect);
	                ServiceProVectSerializable serVectObj2 = new ServiceProVectSerializable(confVector);
	                spares_intent.putExtra("docVectorObj", serVectObj1);
	                spares_intent.putExtra("confVectorObj", serVectObj2);
	                spares_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            	startActivity(spares_intent);
	            }
	            catch(Exception asf){
	            	ServiceProConstants.showErrorLog("Error in onSaveAction : "+asf.toString());
	            }
	        }
			catch (Exception de) {
		    	ServiceProConstants.showErrorLog("Error in done_btListener : "+de.toString());
		    }
        }
    };
    
    private void getMaterialDetails(){
        try{
            if(editModeFlag == true){
                if(ServiceProConstants.sparesColltVect != null){
                    if(ServiceProConstants.sparesColltVect.size() > selIndex){
                        spareObj = (SOSparesClass)ServiceProConstants.sparesColltVect.elementAt(selIndex);
                        if(spareObj != null){
                            if((editModeFlag == true) && (!spareObj.getNumberExtStr().equalsIgnoreCase(""))){
                                prevalEditFlag = true;
                            }
                        }
                    }
                }
            }
            System.out.println("prevalEditFlag : "+prevalEditFlag);
            
            if(spareObj == null)
                spareObj = new SOSparesClass();
        }
        catch(Exception adf){
        	ServiceProConstants.showErrorLog("Error in getMaterialDetails : "+adf.toString());
        }
    }//fn getMaterialDetails
    
    private String[] getMaterialChoicesArray(){
        String choices[] = null;
        try{
            System.out.println("Size of choices : "+ServiceProConstants.mattEmployeeListVect.size());
            if(ServiceProConstants.mattEmployeeListVect != null && ServiceProConstants.mattEmployeeListVect.size() > 0){
                SOMattEmpListOpConstraints empObj = null;
                String mattGrpStr = "", spareMattStr="", spareMattDescStr="", combStr="";
                if(spareObj != null){
                    mattGrpStr = spareObj.getMaterialStr().trim();
                }
                
                int arrSize = ServiceProConstants.mattEmployeeListVect.size();
                choices = new String[arrSize+1];
                
                for(int h=0; h<ServiceProConstants.mattEmployeeListVect.size(); h++){
                    empObj = (SOMattEmpListOpConstraints)ServiceProConstants.mattEmployeeListVect.elementAt(h);
                    if(empObj != null){
                        spareMattStr = empObj.getMaterial().trim();
                        spareMattDescStr = empObj.getMaterialDesc().trim();
                        combStr = spareMattStr+":"+spareMattDescStr;
                        if(editModeFlag == true){
                            if(mattGrpStr.equalsIgnoreCase(spareMattStr))
                                mattCodeIndex = h;
                            else if(mattGrpStr.equalsIgnoreCase(combStr))
                                mattCodeIndex = h;
                        }
                        choices[h] = combStr;
                    }
                }
                
                if(editModeFlag == true){
                    if(mattGrpStr.equalsIgnoreCase(ServiceProConstants.SERVICEORD_COMMON_OTHERS))
                        mattCodeIndex = arrSize;
                        
                }
                
                choices[arrSize] = ServiceProConstants.SERVICEORD_COMMON_OTHERS;
            }
        }
        catch(Exception sfg){
        	ServiceProConstants.showErrorLog("Error in getMaterialChoicesArray : "+sfg.toString());
        }
        System.out.println("Size of choices 2: "+choices.length);
        return choices;
    }//fn getMaterialChoicesArray
    
    private String[] getMaterialUnitChoicesArray(String[] grpChoices){
        String choices[] = null;
        String grpStr = "", editMatchStr="";
        Vector subListVect = new Vector();
        try{
            if(grpChoices != null){
                if(mattCodeIndex < grpChoices.length){
                    grpStr = grpChoices[mattCodeIndex].trim();
                    System.out.println("Selected Grpstr 1 : "+grpStr);
                    String[] grps = grpStr.split(":");
                    grpStr = grps[0].trim();
                    System.out.println("Selected Grpstr 2 : "+grpStr);
                    
                    if(spareObj != null){
                        editMatchStr = spareObj.getUnitsStr().trim();
                    }
                    
                    SOMattEmpListOpConstraints empListObj = null;
                    String grpCode = "", grpCode1 = "";
                    int addCnt = 0;
                    if(ServiceProConstants.mattEmployeeListVect != null){
                        if(mattCodeIndex < ServiceProConstants.mattEmployeeListVect.size()){
                            empListObj = (SOMattEmpListOpConstraints)ServiceProConstants.mattEmployeeListVect.elementAt(mattCodeIndex);
                            if(empListObj != null){
                                grpCode = empListObj.getMaterialName().trim();
                                grpCode1 = empListObj.getMaterialUnit().trim();
                                System.out.println("grpCodes : "+grpCode+" : "+grpCode1);
                                System.out.println("editMatchStr : "+editMatchStr);
                                
                                if(!grpCode.equalsIgnoreCase("")){
                                    subListVect.addElement(grpCode);
                                    addCnt++;
                                }
                                
                                if((!grpCode.equalsIgnoreCase(grpCode1)) && (!grpCode1.equalsIgnoreCase(""))){
                                    subListVect.addElement(grpCode1);
                                    addCnt++;
                                }
                                    
                                if(editModeFlag == true){
                                    if(!editMatchStr.equalsIgnoreCase("")){
                                        if(editMatchStr.equalsIgnoreCase(grpCode)){
                                            mattUnitIndex = subListVect.size()-addCnt;
                                        }
                                        else if(editMatchStr.equalsIgnoreCase(grpCode1)){
                                           // mattUnitIndex = subListVect.size()-(addCnt-1);
                                            mattUnitIndex = subListVect.size()-addCnt;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            if(editModeFlag == true){
                if(subListVect != null)
                    if(subListVect.size() <= 0){
                    	other_unitTV.setVisibility(View.VISIBLE);
                    	other_unitET.setVisibility(View.GONE);
                    	other_unitET.setText("");
                    	if(editMatchStr != null && editMatchStr.length() > 0){
                        	other_unitll.setVisibility(View.VISIBLE);
                        	other_unitTV.setText(editMatchStr);
                    	}else{
                    		other_unitll.setVisibility(View.GONE);
                        	other_unitTV.setText("");
                    	}
                    }
            }
            
            if(subListVect != null){
                if(subListVect.size() > 0){
                    choices = new String[subListVect.size()];
                    subListVect.copyInto(choices);
                }
            }                
            System.out.println("Size : "+subListVect.size()+" : "+mattUnitIndex);
        }
        catch(Exception dfs){
        	ServiceProConstants.showErrorLog("Error in getMaterialUnitChoicesArray : "+dfs.toString());
        }
        return choices;
    }//fn getMaterialUnitChoicesArray
    
}//End of class ServiceProSparesEditScreen