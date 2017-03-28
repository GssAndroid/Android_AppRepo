package com.globalsoft.ServicePro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.SapTasksInputConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkSerOpConstraints;
import com.globalsoft.ServicePro.Database.VanStkDBOperations;
import com.globalsoft.ServicePro.Database.VanStkOfflineContraintsCP;
import com.globalsoft.ServicePro.SoapConnection.HttpTransportSE;

public class VanStockColleagueScreen extends Activity implements TextWatcher {
	
	private TextView[] mattTxtView;
	private TextView mattHeaderTV, buttonHeaderTV, qtyHeaderTV, unitsHeaderTV, transitHeaderTV;
	private TextView matDesHeaderTV, batchHeaderTV, plantHeaderTV, strLocHeaderTV;
	private ProgressDialog pdialog = null;
	private EditText searchET;
	private TextView telNo1ET, telNo2ET;
	//private EditText telNo1ET, telNo2ET;
	private ImageButton[] syncBMF;
	private ImageButton phoneBtn1, phoneBtn2;
	private LinearLayout phLinear1, phLinear2;
	
	private ArrayList stocksArrList = new ArrayList();
	private ArrayList stocksCopyArrList = new ArrayList();
	private ArrayList stocksSerArrList = new ArrayList();
	
	private int stocksListCount = 0;
	private int stocksSerListCount = 0;
	
	private String stocksListType = "";
	private String stocksSerListType = "";

	public boolean flag_pref = false;
	
	private CharSequence[] colItems;
	private SoapObject resultSoap = null;
	
	private boolean sortFlag = false, sortMattFlag = false, sortMattDescFlag = false, searchflag = true;
    private int sortIndex = -1;
    private String searchStr = "";    
    private String partnerIdStr = "", partnerUNameStr = "", partnerNameStr = "";
    private String telNo1Str = "", telNo2Str = "";
    private int mattHWidth, buttonHWidth, qtyHWidth, unitsHWidth, transitHWidth;
    private int matDesHWidth, batchHWidth, plantHWidth, strLocHWidth;
    private int dispwidth = 300;
    private static boolean isConnAvail = true;

	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private SoapObject requestSoapObj = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
        	setContentView(R.layout.vanstockcol);
        	setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
        	
        	ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.vanstockcol); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText("VanStockColleagueScreen");

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			SharedPreferences settings = getSharedPreferences(ServiceProConstants.PREFS_NAME_FOR_VAN_STOCK, 0);  
			flag_pref = settings.getBoolean(ServiceProConstants.PREFS_KEY_VAN_STOCK_FOR_COLLEAGUE_GET, false);
			
			partnerIdStr = this.getIntent().getStringExtra("vStkColId");
			partnerUNameStr = this.getIntent().getStringExtra("vStkColUName");
			partnerNameStr = this.getIntent().getStringExtra("vStkColName");
			telNo1Str = this.getIntent().getStringExtra("vStkColTel1");
            telNo2Str = this.getIntent().getStringExtra("vStkColTel2");
			/*ServiceProConstants.showLog("partnerIdStr : "+partnerIdStr+" : "+partnerNameStr);
			ServiceProConstants.showLog("TelephoneStr : "+telNo1Str+" : "+telNo2Str);*/
			//this.setTitle("Van Stock"+" - "+partnerNameStr);
			myTitle.setText("Van Stock"+" - "+partnerNameStr);
			if(isConnAvail ==  true){
    			initSoapConnection();
    		}
    		else{
    			//Read from Database
    			initDBConnection();
    		}
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
        
    private void initLayout(){
		try {        	
			setContentView(R.layout.vanstockcol);
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			updateTelephoneDetails();
			searchET = (EditText)findViewById(R.id.searchBEF);
			searchET.setText(searchStr);
			searchET.addTextChangedListener(this);
			
			mattHeaderTV = (TextView)findViewById(R.id.mattHeaderTV3);
			mattHeaderTV.setGravity(Gravity.LEFT);
			mattHeaderTV.setPadding(10,5,5,5);
			mattHeaderTV.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.VANSTOCK_SORT_MATERIAL);
				}
			});
			SpannableString underlinedStr = ServiceProConstants.getUnderlinedString(mattHeaderTV.getText().toString());
			mattHeaderTV.setText(underlinedStr);
			
			matDesHeaderTV = (TextView)findViewById(R.id.matDesHeaderTV3);
			matDesHeaderTV.setGravity(Gravity.LEFT);
			matDesHeaderTV.setPadding(10,5,5,5);
			matDesHeaderTV.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.VANSTOCK_SORT_MATDESC);
				}
			});
			
			buttonHeaderTV = (TextView)findViewById(R.id.buttonHeaderTV3);
			buttonHeaderTV.setGravity(Gravity.LEFT);
			buttonHeaderTV.setPadding(10,5,5,5);
			
			qtyHeaderTV = (TextView)findViewById(R.id.qtyHeaderTV3);
			qtyHeaderTV.setGravity(Gravity.RIGHT);
			qtyHeaderTV.setPadding(5,5,10,5);
			
			unitsHeaderTV =(TextView)findViewById(R.id.unitsHeaderTV3);
			unitsHeaderTV.setGravity(Gravity.LEFT);
			unitsHeaderTV.setPadding(10,5,5,5);
			
			transitHeaderTV =(TextView)findViewById(R.id.transitHeaderTV3);
			transitHeaderTV.setGravity(Gravity.RIGHT);
			transitHeaderTV.setPadding(5,5,10,5);
			
			batchHeaderTV  =(TextView)findViewById(R.id.batchHeaderTV3);
			batchHeaderTV.setGravity(Gravity.LEFT);
			batchHeaderTV.setPadding(10,5,5,5);
			
			plantHeaderTV = (TextView)findViewById(R.id.plantHeaderTV3);
			plantHeaderTV.setGravity(Gravity.LEFT);
			plantHeaderTV.setPadding(10,5,5,5);
			
			strLocHeaderTV =(TextView)findViewById(R.id.strLocHeaderTV3);
			strLocHeaderTV.setGravity(Gravity.LEFT);
			strLocHeaderTV.setPadding(10,5,5,5);
			
			ViewTreeObserver vto1 = mattHeaderTV.getViewTreeObserver();
	        vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = mattHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                mattHWidth = mattHeaderTV.getWidth();
	                //ServiceProConstants.showLog("mattHeaderTV Width1 : "+mattHWidth+" : "+mattHeaderTV.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto2 = buttonHeaderTV.getViewTreeObserver();
	        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = buttonHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                buttonHWidth = buttonHeaderTV.getWidth();
	                //ServiceProConstants.showLog("buttonHeaderTV Width1 : "+buttonHWidth+" : "+buttonHeaderTV.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto3 = qtyHeaderTV.getViewTreeObserver();
	        vto3.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = qtyHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                qtyHWidth = qtyHeaderTV.getWidth();
	                //ServiceProConstants.showLog("qtyHeaderTV Width1 : "+qtyHWidth+" : "+qtyHeaderTV.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto4 = unitsHeaderTV.getViewTreeObserver();
	        vto4.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = unitsHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                unitsHWidth = unitsHeaderTV.getWidth();
	                //ServiceProConstants.showLog("unitsHeaderTV Width1 : "+unitsHWidth+" : "+unitsHeaderTV.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto5 = transitHeaderTV.getViewTreeObserver();
	        vto5.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = transitHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                transitHWidth = transitHeaderTV.getWidth();
	                //ServiceProConstants.showLog("transitHeaderTV Width1 : "+transitHWidth+" : "+transitHeaderTV.getMeasuredWidth());
	            }
	        });
			
	        ViewTreeObserver vto6 = matDesHeaderTV.getViewTreeObserver();
	        vto6.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = matDesHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                matDesHWidth = matDesHeaderTV.getWidth();
	                //ServiceProConstants.showLog("matDesHeaderTV Width1 : "+matDesHWidth+" : "+matDesHeaderTV.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto7 = batchHeaderTV.getViewTreeObserver();
	        vto7.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = batchHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                batchHWidth = batchHeaderTV.getWidth();
	                //ServiceProConstants.showLog("batchHeaderTV Width1 : "+batchHWidth+" : "+batchHeaderTV.getMeasuredWidth());
	            }
	        });
	        
	        ViewTreeObserver vto8 = plantHeaderTV.getViewTreeObserver();
	        vto8.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = plantHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                plantHWidth = plantHeaderTV.getWidth();
	                //ServiceProConstants.showLog("plantHeaderTV Width1 : "+plantHWidth+" : "+plantHeaderTV.getMeasuredWidth());
	            }
	        });        
	        	        
	        ViewTreeObserver vto9 = strLocHeaderTV.getViewTreeObserver();
	        vto9.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                ViewTreeObserver obs = strLocHeaderTV.getViewTreeObserver();
	                obs.removeGlobalOnLayoutListener(this);
	                strLocHWidth = strLocHeaderTV.getWidth();
	                //ServiceProConstants.showLog("strLocHeaderTV Width1 : "+strLocHWidth+" : "+strLocHeaderTV.getMeasuredWidth());
	                drawSubLayout();
	            }
	        });
	        
        	
			searchET.setFocusable(true);
			searchET.setFocusableInTouchMode(true);
			searchET.requestFocus();

		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
    private void drawSubLayout(){
		try{
			TableLayout tl = (TableLayout)findViewById(R.id.vstckmaintbllayout23);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			if(stocksArrList != null){
				VanStkOpConstraints stkCategory = null;
				String matterialStr = "", werksStr = "", locationStr = "";
                String mattDescStr = "", unitsStr = "", quantityStr = "";
                String transitStr = "", batchStr = "", syncStr = "";
                
                int rowSize = stocksArrList.size();                
                //ServiceProConstants.showLog("Stocks List Size  : "+rowSize);
                
                syncBMF = new ImageButton[rowSize];
                mattTxtView = new TextView[rowSize];
                
				for (int i =0; i < stocksArrList.size(); i++) {
					stkCategory = (VanStkOpConstraints)stocksArrList.get(i);
                    if(stkCategory != null){
                    	matterialStr = stkCategory.getMaterial().trim();
                        werksStr = stkCategory.getWerks().trim();
                        locationStr = stkCategory.getStorageLoc().trim();                                
                        mattDescStr = stkCategory.getMattDesc().trim();
                        unitsStr = stkCategory.getMeasureUnits().trim();
                        quantityStr = stkCategory.getStockQty().trim();        
                        transitStr = stkCategory.getMatQtyTransit().trim();
                        batchStr = stkCategory.getBatchNo().trim();                        
                        syncStr = stkCategory.getSerAvailable().trim();     
                        
                        tr = new TableRow(this);
                        
                        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linear, null);
                    	linearLayout.setPadding(0, 10, 0, 10);
                        
                        syncBMF[i] = new ImageButton(this); 
                        syncBMF[i].setId(i);
                        syncBMF[i].setBackgroundResource(R.drawable.list);
                        syncBMF[i].setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								int id = view.getId();	
								showVanStockSerialDetailScreen(id);
							}	
                        });
                        
                        if(syncStr.equalsIgnoreCase(ServiceProConstants.VANSTOCK_SYNC_VAL)){
                        	linearLayout.addView(syncBMF[i]);
                        }
                        
                        mattTxtView[i] = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					mattTxtView[i].setText(matterialStr);
    					mattTxtView[i].setId(i);
    					mattTxtView[i].setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								int id = view.getId();	
								showVanStockDetailScreen(id);
							}	
                        });
    					mattTxtView[i].setWidth(mattHWidth);
    					mattTxtView[i].setGravity(Gravity.LEFT);
    					mattTxtView[i].setPadding(10,0,0,0);
    					
    					TextView werkTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					werkTxtView.setText(werksStr);
    					werkTxtView.setWidth(plantHWidth);
    					werkTxtView.setGravity(Gravity.LEFT);
    					werkTxtView.setPadding(10,0,0,0);
    					
    					TextView lgortTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					lgortTxtView.setText(locationStr);
    					lgortTxtView.setWidth(strLocHWidth);
    					lgortTxtView.setGravity(Gravity.LEFT);
    					lgortTxtView.setPadding(10,0,0,0);
    					
    					TextView matdescTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					matdescTxtView.setText(mattDescStr);
    					matdescTxtView.setWidth(matDesHWidth);
    					matdescTxtView.setGravity(Gravity.LEFT);
    					matdescTxtView.setPadding(10,0,0,0);
    					
    					TextView unitTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);					
    					unitTxtView.setText(unitsStr);
    					unitTxtView.setWidth(unitsHWidth);
    					unitTxtView.setGravity(Gravity.LEFT);
    					unitTxtView.setPadding(10,0,0,0);
    					
    					TextView qtyTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					qtyTxtView.setText(quantityStr);
    					qtyTxtView.setWidth(qtyHWidth);
    					qtyTxtView.setGravity(Gravity.RIGHT);
    					qtyTxtView.setPadding(0,0,10,0);
    					
    					TextView transitTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					transitTxtView.setText(transitStr);
    					transitTxtView.setWidth(transitHWidth);
    					transitTxtView.setGravity(Gravity.RIGHT);
    					transitTxtView.setPadding(0,0,10,0);
    					
    					TextView batchTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					batchTxtView.setText(batchStr);		
    					batchTxtView.setWidth(batchHWidth);
    					batchTxtView.setGravity(Gravity.LEFT);
    					batchTxtView.setPadding(10,0,0,0);
    					
    					if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
    						mattTxtView[i].setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						qtyTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						unitTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						transitTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						matdescTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						batchTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						werkTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    						lgortTxtView.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
    					}
    					
    					tr.addView(mattTxtView[i]);
    					tr.addView(linearLayout);
    					tr.addView(qtyTxtView);
    					tr.addView(unitTxtView);
    					tr.addView(transitTxtView);
    					tr.addView(matdescTxtView);
    					tr.addView(batchTxtView);
    					tr.addView(werkTxtView);
    					tr.addView(lgortTxtView);
    					
    					if(i%2 == 0)
    						tr.setBackgroundResource(R.color.item_even_color);
    		            else
    		            	tr.setBackgroundResource(R.color.item_odd_color);
    					
    					tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                    }					
				}
			}
		}
		catch(Exception asgf){
			ServiceProConstants.showErrorLog("On drawSubLayout : "+asgf.toString());
		}
	}//fn drawSubLayout
    

    private void updateTelephoneDetails(){
    	try{
    		telNo1ET = (TextView)findViewById(R.id.cus1phET);
    		telNo1ET.setText(telNo1Str);
			
			telNo2ET = (TextView)findViewById(R.id.cus2phET);
			telNo2ET.setText(telNo2Str);
			
			phoneBtn1 = (ImageButton) findViewById(R.id.cus1callbtn);			
			phoneBtn2 = (ImageButton) findViewById(R.id.cus2callbtn);
			
			phLinear1 = (LinearLayout) findViewById(R.id.cus1phlinear);
			phLinear2 = (LinearLayout) findViewById(R.id.cus2phlinear);
			
			if(!telNo1Str.equalsIgnoreCase("")){
				if(telNo1Str.trim().length() > 0){
					phLinear1.setVisibility(View.VISIBLE);
					phoneBtn1.setOnClickListener(call1_btn1Listener); 
				}
			}
			
			if(!telNo2Str.equalsIgnoreCase("")){
				if(telNo2Str.trim().length() > 0){
					phLinear2.setVisibility(View.VISIBLE);
					phoneBtn2.setOnClickListener(call_btn2Listener); 
				}
			}
    	}
    	catch (Exception sfsdf) {
			ServiceProConstants.showErrorLog("Error in updateTelephoneDetails : "+sfsdf.toString());
		}
    }//fn updateTelephoneDetails
    
    private OnClickListener call1_btn1Listener = new OnClickListener() {
        public void onClick(View v){
			try{
				String telno1 = telNo1Str.replace("+","");
				//ServiceProConstants.showLog("telno: "+telno1);
				Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telno1));
				startActivity(phoneIntent);
			}
			catch(Exception e){
				e.printStackTrace();
			}
        }
    };
    
    private OnClickListener call_btn2Listener = new OnClickListener(){
        public void onClick(View v){
			try{
				String telno2 = telNo2Str.replace("+","");
				//ServiceProConstants.showLog("telno:"+telno2);
				Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telno2));
				startActivity(phoneIntent);
			}
			catch(Exception e){
				e.printStackTrace();
			}
        }
    };
    
    private void showVanStockDetailScreen(int selIndex){
        try{
            System.out.println("Selected Index : "+selIndex);
            VanStkOpConstraints stkCategory = null;
            boolean errFlag = false;
            
            if(stocksArrList != null){
                if(stocksArrList.size() > selIndex){
                    stkCategory = (VanStkOpConstraints)stocksArrList.get(selIndex);
                    if(stkCategory != null){
                    	try {
							Intent intent = new Intent(this, VanStockDetailScreen.class);
							intent.putExtra("stkCategoryObj", stkCategory);
							startActivityForResult(intent, ServiceProConstants.VANSTOCK_DETL_SCREEN);
						} 
						catch (Exception e) {
							ServiceProConstants.showErrorLog(e.getMessage());
						}
                    }
                    else
                        errFlag = true;
                }
                else{
                    errFlag = true;
                }
            }
            else{
                errFlag = true;
            }
                
            if(errFlag == true)
            	ServiceProConstants.showErrorDialog(this, ""+R.string.SERVICEPRO_VANSTOCK_ERR_DETCANT);
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("On showVanStockDetailScreen : "+asf.toString());
        }
    }//fn showVanStockDetailScreen
    
    private void showVanStockSerialDetailScreen(int selIndex){
        try{
            System.out.println("Selected Index : "+selIndex);
            VanStkOpConstraints stkCategory = null;
            Vector serCatVect = null;
            boolean errFlag = false;
            //ServiceProConstants.showErrorDialog(this, "Sel Index :"+selIndex);
            if(stocksArrList != null){
                if(stocksArrList.size() > selIndex){
                    stkCategory = (VanStkOpConstraints)stocksArrList.get(selIndex);
                    if(stkCategory != null){
                        String matStr = stkCategory.getMaterial().trim();                        
                        serCatVect = getSerialCategoryVect(matStr);
                        if(serCatVect != null){
	                        try {
								Intent intent = new Intent(this, VanStockSerialDetScreen.class);
								intent.putExtra("stkCategoryObj", stkCategory);
								intent.putExtra("serCatVectObj", serCatVect);
								startActivityForResult(intent, ServiceProConstants.VANSTOCK_SRL_SCREEN);
							} 
							catch (Exception e) {
								ServiceProConstants.showErrorLog(e.getMessage());
							}
                        }
                        else
                            errFlag = true; 
                    }	
                    else
                        errFlag = true;
                }
                else{
                    errFlag = true;
                }
            }
            else{
                errFlag = true;
            }
                
            if(errFlag == true)
            	ServiceProConstants.showErrorDialog(this, ""+R.string.SERVICEPRO_VANSTOCK_ERR_DETCANT);
        }
        catch(Exception assf){
        	ServiceProConstants.showErrorLog("On showVanStockSerialDetailScreen : "+assf.toString());
        }
    }//fn showVanStockSerialDetailScreen 
    
    private Vector getSerialCategoryVect(String matid){
        Vector serialCatVect = new Vector();
        VanStkSerOpConstraints serCategory = null;
        String serMatStr = "";
        try{
            if(stocksSerArrList != null){
                for(int q=0; q<stocksSerArrList.size(); q++){
                    serCategory = null;
                    serCategory = (VanStkSerOpConstraints)stocksSerArrList.get(q);
                    if(serCategory != null){
                        serMatStr = serCategory.getMaterial().trim();
                        if(serMatStr.equalsIgnoreCase(matid))
                            serialCatVect.addElement(serCategory);
                    }
                }
            }
        }
        catch(Exception sggh){
        	ServiceProConstants.showErrorLog("On getSerialCategoryFromVect : "+sggh.toString());
        }
        return serialCatVect;
    }//fn getSerialCategoryVect
    
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub	
	}
	
	public void afterTextChanged(Editable s) { 
		  //ServiceProConstants.showLog("Text : "+s.toString());
		  searchItemsAction(s.toString());
	} 
	
	private void searchItemsAction(String match){  
        try{
            searchflag = true;           
            searchStr = match;
            VanStkOpConstraints stkObj = null;
            String mattStr = "", mattDescStr = "";
            if((stocksCopyArrList != null) && (stocksCopyArrList.size() > 0)){
                if((!match.equalsIgnoreCase("")) && (match.length() >= 1)){                                            
                    System.out.println("Match : "+match);  
                    stocksArrList.clear();
                    for(int i = 0; i < stocksCopyArrList.size(); i++){  
                        stkObj = null;
                        mattStr = "";
                        mattDescStr = "";
                        stkObj = (VanStkOpConstraints)stocksCopyArrList.get(i);
                        if(stkObj != null){
                            mattStr = stkObj.getMaterial().trim().toLowerCase();
                            mattDescStr = stkObj.getMattDesc().trim().toLowerCase();
                            match = match.toLowerCase();
                            if((mattStr.indexOf(match) >= 0) || (mattDescStr.indexOf(match) >= 0)){
                            	stocksArrList.add(stkObj);
                            }
                        }
                    }//for 
                    initLayout();
        			//searchET.setText(searchStr);
                }
                else{
                    System.out.println("Match is empty");
                    stocksArrList.clear();
                    for(int i = 0; i < stocksCopyArrList.size(); i++){  
                        stkObj = (VanStkOpConstraints)stocksCopyArrList.get(i);
                        if(stkObj != null){
                        	stocksArrList.add(stkObj);
                        }
                    }
                    initLayout();
        			//searchET.setText(searchStr);
                }
            }//if
            else
                return;
        }//try
        catch(Exception we){
        	ServiceProConstants.showErrorLog("Error On searchItemsAction : "+we.toString());
        }
    }//fn searchItemsAction  
	
	private void sortItemsAction(int sortInd){
		try{
			 sortFlag = true;
			 sortIndex = sortInd;
			 
			 if(sortInd == ServiceProConstants.VANSTOCK_SORT_MATERIAL)
				 sortMattFlag = !sortMattFlag;
			 else if(sortInd == ServiceProConstants.VANSTOCK_SORT_MATDESC)
				 sortMattDescFlag = !sortMattDescFlag;
			 
			 //ServiceProConstants.showLog("Selected Sort Index : "+sortInd);
			 Collections.sort(stocksArrList, stockSortComparator); 
				
             initLayout();
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in sortItemsAction : "+sfg.toString());
		}
	}//fn sortItemsAction
	
	
	private void initSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            SapTasksInputConstraints C0[];
            C0 = new SapTasksInputConstraints[4];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new SapTasksInputConstraints(); 
            }            
            
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            if(!flag_pref){
                C0[2].Cdata = "EVENT[.]VAN-STOCK-FOR-COLLEAGUE-GET[.]VERSION[.]0[.]RESPONSE-TYPE[.]FULL-SETS";
            }else{
                C0[2].Cdata = "EVENT[.]VAN-STOCK-FOR-COLLEAGUE-GET[.]VERSION[.]0";
            }
            C0[3].Cdata = "SWDTUSER[.]"+partnerUNameStr;
        
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
    			if(pdialog != null)
        			pdialog = null;
        		
    			if(pdialog == null)
    				VanStockColleagueScreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                        	pdialog = ProgressDialog.show(VanStockColleagueScreen.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
                        	new Thread() {
                        		public void run() {
                        			try{
                        				updateReportsConfirmResponse(resultSoap);
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
                    updateReportsConfirmResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP
*/	
	
	public void updateReportsConfirmResponse(SoapObject soap){		
		VanStkOpConstraints stkCategory = null;
        VanStkSerOpConstraints stkSerCategory = null;	
        try{ 
        	if(soap != null){  
        		emptyAllVectors();
        		
        		String delimeter = "[.]", result="", res="", docTypeStr = "";
                SoapObject pii = null;
                String[] resArray = new String[37];
                int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;
	            	            
	            for (int i = 0; i < soap.getPropertyCount(); i++) {                
	                pii = (SoapObject)soap.getProperty(i);
	                propsCount = pii.getPropertyCount();
	                //ServiceProConstants.showLog("propsCount : "+propsCount);
	                if(propsCount > 0){
	                    for (int j = 0; j < propsCount; j++) {
	                        //ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
	                    	
	                    	if(j > 1 && j <= 3){
                            	result = pii.getProperty(j).toString();
                                firstIndex = result.indexOf(delimeter);
                                eqIndex = result.indexOf("=");
                                eqIndex = eqIndex+1;
                                firstIndex = firstIndex + 3;
                                docTypeStr = result.substring(eqIndex, (firstIndex-3));
                                result = result.substring(firstIndex);
                                ServiceProConstants.showLog("Result : "+result);                                
                                resC = 0;
                                indexA = 0;
                                indexB = result.indexOf(delimeter);
                                int index1 = 0;
                                while (indexB != -1) {
                                    res = result.substring(indexA, indexB);
                                    indexA = indexB + delimeter.length();
                                    indexB = result.indexOf(delimeter, indexA);
                                    ServiceProConstants.showLog("Result resp : "+resC+" : "+res);
                                    if(resC == 0){
                                    	docTypeStr = res;
                                    }
                                    if(resC == 1){
	                                    String[] respStr = res.split(";");
	                                    if(respStr.length >= 1){
	                                    	String respTypeData = respStr[0];
	                                    	ServiceProConstants.showLog("respTypeData : "+respTypeData);
	                                    	index1 = respTypeData.indexOf("=");
	                                    	index1 = index1+1;
	                                        String respType = respTypeData.substring(index1, respTypeData.length());
	                                    	//ServiceProConstants.showLog("respType : "+respType);
	                                    	
	                                    	String rowCountStrData = respStr[1];
	                                    	ServiceProConstants.showLog("rowCountStrData : "+rowCountStrData);
	                                    	index1 = rowCountStrData.indexOf("=");
	                                    	index1 = index1+1;
	                                        String rowCount = rowCountStrData.substring(index1, rowCountStrData.length());
	                                    	ServiceProConstants.showLog("rowCount : "+rowCount);
	                                    	if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLSTCK10C")){
		                                    	ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
		                                    	stocksListCount = Integer.parseInt(rowCount);
		                                    	stocksListType = respType;
	                                        }
	                                        else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLSTCK10S")){
		                                    	ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
		                                    	stocksSerListCount = Integer.parseInt(rowCount);
		                                    	stocksSerListType = respType;
	                                        }
	                                    }
                                    }
                                    resC++;
                                    if(resC == 2)
                                    	break;
                                }                                
                            }
	                    	
	                        if(j > 3){
	                            result = pii.getProperty(j).toString();
	                            firstIndex = result.indexOf(delimeter);
	                            eqIndex = result.indexOf("=");
	                            eqIndex = eqIndex+1;
	                            firstIndex = firstIndex + 3;
	                            docTypeStr = result.substring(eqIndex, (firstIndex-3));
	                            result = result.substring(firstIndex);
	                            //ServiceProConstants.showLog("Document Type : "+docTypeStr);
	                            ServiceProConstants.showLog("Result : "+result);
	                            
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
	                            resArray[resC] = result.substring(indexA, endIndex);	                            
	                            
	                            if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLSTCK10C")){
                                    if(stkCategory != null)
                                        stkCategory = null;
                                        
                                    stkCategory = new VanStkOpConstraints(resArray);
                                    if(stocksArrList != null)
	                                	stocksArrList.add(stkCategory);	  
	                                
	                                if(stocksCopyArrList != null)
	                                	stocksCopyArrList.add(stkCategory);
                                }
                                else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLSTCK10S")){
                                    if(stkSerCategory != null)
                                        stkSerCategory = null;
                                        
                                    stkSerCategory = new VanStkSerOpConstraints(resArray);
                                    if(stocksSerArrList != null)
                                        stocksSerArrList.add(stkSerCategory);
                                }
	                        }
	                        else if(j == 0){
	                            String errorMsg = pii.getProperty(j).toString();
	                            //ServiceProConstants.showLog("Inside J == 0 ");
	                            int errorFstIndex = errorMsg.indexOf("Message=");
	                            if(errorFstIndex > 0){
	                                int errorLstIndex = errorMsg.indexOf(";", errorFstIndex);
	                                String taskErrorMsgStr = errorMsg.substring((errorFstIndex+"Message=".length()), errorLstIndex);
	                                ServiceProConstants.showLog(taskErrorMsgStr);
	                                ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
	                            }
	                        }
	                    }
	                }
	            }
        	}
        	else{
        		initDBConnection();
        	}
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateReportsConfirmResponse : "+sff.toString());
        } 
        finally{
        	/*ServiceProConstants.showLog("Stocks List Size : "+stocksArrList.size());
        	ServiceProConstants.showLog("Stocks Serialized List Size : "+stocksSerArrList.size());*/
        	try {
				if((stocksArrList != null) && (stocksArrList.size() > 0)){
					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_CONTENT_URI, partnerIdStr);
					insertColEmployeeDataIntoDB();
				}
				
				if((stocksSerArrList != null) && (stocksSerArrList.size() > 0)){
					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_SRL_CONTENT_URI, partnerIdStr);
					insertColEmployeeSerialDataIntoDB();
				}			
				
				if(stocksArrList != null){
        			if(stocksListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
        				if((stocksListCount == 0) && (stocksArrList.size() == 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_CONTENT_URI, partnerIdStr);
            			}
        				else if((stocksListCount > 0) && (stocksArrList.size() > 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_CONTENT_URI, partnerIdStr);
        					insertColEmployeeDataIntoDB();
            			}
        			}
        			if(stocksListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){
        				if((stocksListCount == 0) && (stocksArrList.size() == 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_CONTENT_URI, partnerIdStr);
            			}
        				else if((stocksListCount > 0) && (stocksArrList.size() == 0)){
            			}
        				else if((stocksListCount > 0) && (stocksArrList.size() > 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_CONTENT_URI, partnerIdStr);
        					insertColEmployeeDataIntoDB();
            			}
        			}
        		}
        		
        		if(stocksSerArrList != null){
        			if(stocksSerListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
        				if((stocksSerListCount == 0) && (stocksSerArrList.size() == 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_SRL_CONTENT_URI, partnerIdStr);
            			}
        				else if((stocksSerListCount > 0) && (stocksSerArrList.size() > 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_SRL_CONTENT_URI, partnerIdStr);
        					insertColEmployeeSerialDataIntoDB();
            			}
        			}
        			if(stocksSerListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){
        				if((stocksSerListCount == 0) && (stocksSerArrList.size() == 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_SRL_CONTENT_URI, partnerIdStr);
            			}
        				else if((stocksSerListCount > 0) && (stocksSerArrList.size() == 0)){
            			}
        				else if((stocksSerListCount > 0) && (stocksSerArrList.size() > 0)){
        					VanStkDBOperations.deleteAllExistingColleagueData(this, VanStkOfflineContraintsCP.VAN_COL_STK_SRL_CONTENT_URI, partnerIdStr);
        					insertColEmployeeSerialDataIntoDB();
            			}
        			}
        		}
				
			} catch (Exception esf) {
				ServiceProConstants.showErrorLog("On updateCategoryServerResponse finally block: "+esf.toString());
			}
        	
        	if(stocksArrList.size() > 0){
	        	SharedPreferences sharedPreferences = getSharedPreferences(ServiceProConstants.PREFS_NAME_FOR_VAN_STOCK, 0);    
				SharedPreferences.Editor editor = sharedPreferences.edit();    
				editor.putBoolean(ServiceProConstants.PREFS_KEY_VAN_STOCK_FOR_COLLEAGUE_GET, true);    
				editor.commit();
        	}
        	
        	stopProgressDialog();
        	VanStockColleagueScreen.this.runOnUiThread(new Runnable() {
	            public void run() {	 	            	
	            	initDBConnection();
	            }
	        });  
			
        	/*Collections.sort(stocksArrList, stockSortComparator);   
        	stopProgressDialog();
        	VanStockColleagueScreen.this.runOnUiThread(new Runnable() {
	            public void run() {	 
	            	initLayout();
	            }
	        });  */
        }
    }//fn updateReportsConfirmResponse 
	
	private void initDBConnection(){
		try {
			stocksArrList = VanStkDBOperations.readVanSTkCollDataFromDB(this, partnerUNameStr);
			if(stocksArrList != null)
				stocksCopyArrList = (ArrayList)stocksArrList.clone();
			
			stocksSerArrList = VanStkDBOperations.readVanSTkCollSrlDataFromDB(this, partnerUNameStr);
						
			Collections.sort(stocksArrList, stockSortComparator);
		} catch (Exception sse) {
			ServiceProConstants.showErrorLog("Error on initDBConnection: "+sse.toString());
		}
		finally{
        	try {
				initLayout();
			} catch (Exception e) {}
		}
	}//fn initDBConnection
	
	
	private void insertColEmployeeDataIntoDB(){
		VanStkOpConstraints stkCategory;
    	try {
			if(stocksArrList != null){
				for(int k=0; k<stocksArrList.size(); k++){
					stkCategory = (VanStkOpConstraints) stocksArrList.get(k);
					if(stkCategory != null){
						VanStkDBOperations.insertVanSTkColStockDataInToDB(this, stkCategory, partnerIdStr);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertColEmployeeDataIntoDB: "+ewe.toString());
		}
    }//fn insertColEmployeeDataIntoDB
	
	
	private void insertColEmployeeSerialDataIntoDB(){
		VanStkSerOpConstraints serCategory;
    	try {
			if(stocksSerArrList != null){
				for(int k=0; k<stocksSerArrList.size(); k++){
					serCategory = (VanStkSerOpConstraints) stocksSerArrList.get(k);
					if(serCategory != null){
						VanStkDBOperations.insertVanSTkColStockSrlDataInToDB(this, serCategory, partnerIdStr);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertColEmployeeSerialDataIntoDB: "+ewe.toString());
		}
    }//fn insertColEmployeeSerialDataIntoDB
	
	
	private void emptyAllVectors(){
        try{
            searchStr = "";
            
            if(stocksArrList != null)
                stocksArrList.clear();
                
            if(stocksCopyArrList != null)
                stocksCopyArrList.clear();
            
            if(stocksSerArrList != null)
                stocksSerArrList.clear();
        }
        catch(Exception adsf){
        	ServiceProConstants.showErrorLog("On emptyAllVectors : "+adsf.toString());
        }
    }//fn emptyAllVectors
	
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
    
    public void onClose(){
    	try {
			System.gc();
			setResult(RESULT_OK); 
			this.finish();
		} catch (Exception e) {
		}
    }//fn onClose
    
    
    private final Comparator stockSortComparator =  new Comparator() {

        public int compare(Object o1, Object o2){ 
            int comp = 0;
            String strObj1 = "0", strObj2="0";
            VanStkOpConstraints repOPObj1, repOPObj2;
            try{            	
                if (o1 == null || o2 == null){
                }
                else{            
                	repOPObj1 = (VanStkOpConstraints)o1;
                    repOPObj2 = (VanStkOpConstraints)o2;
                    
                    if(sortIndex == ServiceProConstants.VANSTOCK_SORT_MATERIAL){
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getMaterial().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getMaterial().trim();
                        
                        if(sortMattFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else if(sortIndex == ServiceProConstants.VANSTOCK_SORT_MATDESC){
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getMattDesc().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getMattDesc().trim();
                        
                        if(sortMattDescFlag == true)
                            comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                        else
                            comp =  strObj2.toLowerCase().compareTo(strObj1.toLowerCase());
                    }
                    else{
                        // Code to sort by Employee name (default)
                        if(repOPObj1 != null)
                            strObj1 = repOPObj1.getBusinessPrtName().trim();
                        
                        if(repOPObj2 != null)
                            strObj2 = repOPObj2.getBusinessPrtName().trim();
                     
                        comp =  strObj1.toLowerCase().compareTo(strObj2.toLowerCase());
                    }
                }
             }
             catch(Exception qw){
            	 ServiceProConstants.showErrorLog("Error in Serv Order Comparator : "+qw.toString());
             }
                 
             if (comp != 0) {            
                return comp;
             } 
             else {            
                return 0;
            }
        }
    };
    
}//End of class VanStockColleagueScreen

