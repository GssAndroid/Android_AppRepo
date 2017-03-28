package com.globalsoft.ServicePro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
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
import com.globalsoft.ServicePro.Constraints.VanStkColOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkColStrOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkSerOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkTransOpConstraints;

public class VanStockOverviewScreen extends Activity implements TextWatcher {
	
	private TextView[] mattTxtView;
	private TextView mattHeaderTV, buttonHeaderTV, qtyHeaderTV, unitsHeaderTV, transitHeaderTV;
	private TextView matDesHeaderTV, batchHeaderTV, plantHeaderTV, strLocHeaderTV;
	private TextView qtyHeaderTV1, unitsHeaderTV1, transitHeaderTV1;
	private ProgressDialog pdialog = null;
	private EditText searchET;
	private ImageButton[] syncBMF, createStockBMF, qtyRecBMF, qtyCancelBMF;
	
	private ArrayList stocksArrList = new ArrayList();
	private ArrayList stocksCopyArrList = new ArrayList();
	private ArrayList stocksSerArrList = new ArrayList();
    private ArrayList stocksTransArrList = new ArrayList();
    private ArrayList stocksTransArrListCopy = new ArrayList();
	private ArrayList stocksCollArrList = new ArrayList();
	private ArrayList stocksCollSrlArrList = new ArrayList();
	
	private CharSequence[] colItems;
	private SoapObject resultSoap = null;
	boolean vanMainFlag = false;
	
	private boolean sortFlag = false, sortMattFlag = false, sortMattDescFlag = false, searchflag = true;
    private int sortIndex = -1, bmpCount = 0;
    private String searchStr = "", taskErrorMsgStr = "";
    private boolean diagdispFlag = false;
    private int mattHWidth, buttonHWidth, qtyHWidth, unitsHWidth, transitHWidth;
    private int matDesHWidth, batchHWidth, plantHWidth, strLocHWidth;
    private int dispwidth = 300;
    
    private static final int MENU_ID = Menu.FIRST;
    
    private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
			this.setTitle(R.string.SERVICEPRO_VANSTOCK_REPVIEW_TITLE);
			setContentView(R.layout.vanstockmainoverview);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
        	ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.vanstockmainoverview); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SERVICEPRO_VANSTOCK_REPVIEW_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			stocksArrList = (ArrayList) this.getIntent().getSerializableExtra("stocksArrList");
			stocksCopyArrList = (ArrayList) this.getIntent().getSerializableExtra("stocksCopyArrList");
			stocksSerArrList = (ArrayList) this.getIntent().getSerializableExtra("stocksSerArrList");
			stocksTransArrList = (ArrayList) this.getIntent().getSerializableExtra("stocksTransArrList");
			stocksCollArrList = (ArrayList) this.getIntent().getSerializableExtra("stocksCollArrList");
			stocksCollSrlArrList = (ArrayList) this.getIntent().getSerializableExtra("stocksCollSrlArrList");
			
			if(stocksArrList != null){
				Collections.sort(stocksArrList, stockSortComparator);     
				ServiceProConstants.showLog("stocksArrList : "+stocksArrList.size());
			}
        	initLayout();
			
			//initSoapConnection();	
        } catch (Exception de) {
        	ServiceProConstants.showErrorLog(de.toString());
        }
    }
	
	 public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ID, 0, "Stock with other Rep");
	    return true;
	}

	     
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ID: {
				//ServiceProConstants.showLog("Menu Clicked !");
				displayStockColleagues();
		        break;
			}
	    }
		return super.onOptionsItemSelected(item);
	}
		
	private void initLayout(){
		try {        	
			setContentView(R.layout.vanstockmainoverview);
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			 if(stocksTransArrListCopy != null)
				 stocksTransArrListCopy.clear();
			 
			searchET = (EditText)findViewById(R.id.searchBEF1);
			searchET.setText(searchStr);
			searchET.addTextChangedListener(this);
			
			mattHeaderTV = (TextView)findViewById(R.id.mattHeaderTV);
			mattHeaderTV.setGravity(Gravity.LEFT);
			mattHeaderTV.setPadding(10,5,5,5);
			mattHeaderTV.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.VANSTOCK_SORT_MATERIAL);
				}
			});
			SpannableString underlinedStr = ServiceProConstants.getUnderlinedString(mattHeaderTV.getText().toString());
			mattHeaderTV.setText(underlinedStr);
			
			matDesHeaderTV = (TextView)findViewById(R.id.matDesHeaderTV);
			matDesHeaderTV.setGravity(Gravity.LEFT);
			matDesHeaderTV.setPadding(10,5,5,5);
			matDesHeaderTV.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					sortItemsAction(ServiceProConstants.VANSTOCK_SORT_MATDESC);
				}
			});
			
			buttonHeaderTV = (TextView)findViewById(R.id.buttonHeaderTV);
			buttonHeaderTV.setGravity(Gravity.LEFT);
			buttonHeaderTV.setPadding(10,5,5,5);
			
			qtyHeaderTV = (TextView)findViewById(R.id.qtyHeaderTV);
			qtyHeaderTV.setGravity(Gravity.RIGHT);
			qtyHeaderTV.setPadding(5,5,10,5);
			
			unitsHeaderTV =(TextView)findViewById(R.id.unitsHeaderTV);
			unitsHeaderTV.setGravity(Gravity.LEFT);
			unitsHeaderTV.setPadding(10,5,5,5);
			
			transitHeaderTV =(TextView)findViewById(R.id.transitHeaderTV);
			transitHeaderTV.setGravity(Gravity.RIGHT);
			transitHeaderTV.setPadding(5,5,10,5);
			
			batchHeaderTV  =(TextView)findViewById(R.id.batchHeaderTV);
			batchHeaderTV.setGravity(Gravity.LEFT);
			batchHeaderTV.setPadding(10,5,5,5);
			
			plantHeaderTV = (TextView)findViewById(R.id.plantHeaderTV);
			plantHeaderTV.setGravity(Gravity.LEFT);
			plantHeaderTV.setPadding(10,5,5,5);
			
			strLocHeaderTV =(TextView)findViewById(R.id.strLocHeaderTV);
			strLocHeaderTV.setGravity(Gravity.LEFT);
			strLocHeaderTV.setPadding(10,5,5,5);
			
			//Second row Header Elements
			
			qtyHeaderTV1 = (TextView)findViewById(R.id.qty1HeaderTV);
			qtyHeaderTV1.setGravity(Gravity.LEFT);
			qtyHeaderTV1.setPadding(10,5,5,5);
			
			unitsHeaderTV1 =(TextView)findViewById(R.id.units1HeaderTV);
			unitsHeaderTV1.setGravity(Gravity.RIGHT);
			unitsHeaderTV1.setPadding(5,5,10,5);
			
			transitHeaderTV1 =(TextView)findViewById(R.id.transit1HeaderTV);
			transitHeaderTV1.setGravity(Gravity.RIGHT);
			transitHeaderTV1.setPadding(5,5,10,5);
						
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
	               // ServiceProConstants.showLog("strLocHeaderTV Width1 : "+strLocHWidth+" : "+strLocHeaderTV.getMeasuredWidth());
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
			TableLayout tl = (TableLayout)findViewById(R.id.vstckmaintbllayout3);
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
			
			LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			linparams.leftMargin = 10; 
			linparams.rightMargin = 10; 
			linparams.gravity = Gravity.CENTER_VERTICAL;
	        
			if(stocksArrList != null){
				VanStkOpConstraints stkCategory = null;
				VanStkTransOpConstraints stkTransCategory = null;
				String matterialStr = "", werksStr = "", locationStr = "";
                String mattDescStr = "", unitsStr = "", quantityStr = "";
                String transitStr = "", batchStr = "", syncStr = "";
                String matrlStr = "", qtyRecStr = "", qtyIssuedStr = "";
                
                float qtyRecvd = 0, qtyIssued = 0;
                int marginTop = 5, marginBot = 5;
                
                int rowSize = stocksArrList.size();
                int subrowSize = stocksTransArrList.size();
                bmpCount = 0;
                
                /*ServiceProConstants.showLog("Stocks List Size  : "+rowSize);
                ServiceProConstants.showLog("stocksTransVect size : "+subrowSize);*/
                
                syncBMF = new ImageButton[rowSize];
                createStockBMF = new ImageButton[rowSize];
                mattTxtView = new TextView[rowSize];
                qtyRecBMF = new ImageButton[subrowSize];
                qtyCancelBMF = new ImageButton[subrowSize];
                
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
                        syncBMF[i].setLayoutParams(linparams);   
                        syncBMF[i].setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								int id = view.getId();	
								showVanStockSerialDetailScreen(id);
							}	
                        });
                        
                        createStockBMF[i] = new ImageButton(this); 
                        createStockBMF[i].setId(i);
                        createStockBMF[i].setBackgroundResource(R.drawable.stock);
                        createStockBMF[i].setLayoutParams(linparams);   
                        createStockBMF[i].setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								int id = view.getId();	
								showVanStockCreationScreen(id);
							}	
                        });
                        
                        linearLayout.addView(createStockBMF[i]);
                        if(syncStr.equalsIgnoreCase(ServiceProConstants.VANSTOCK_SYNC_VAL)){
                        	linearLayout.addView(syncBMF[i]);
                        }
                        
                        mattTxtView[i] = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					mattTxtView[i].setText(matterialStr);
    					//mattTxtView[i].setTextColor(getResources().getColor(R.color.lblue));
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
    					//werkTxtView.setTextColor(getResources().getColor(R.color.lblue));
    					werkTxtView.setWidth(plantHWidth);
    					werkTxtView.setGravity(Gravity.LEFT);
    					werkTxtView.setPadding(10,0,0,0);
    					
    					TextView lgortTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					lgortTxtView.setText(locationStr);
    					//lgortTxtView.setTextColor(getResources().getColor(R.color.lblue));
    					lgortTxtView.setWidth(strLocHWidth);
    					lgortTxtView.setGravity(Gravity.LEFT);
    					lgortTxtView.setPadding(10,0,0,0);
    					
    					TextView matdescTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					matdescTxtView.setText(mattDescStr);
    					//matdescTxtView.setTextColor(getResources().getColor(R.color.lblue));
    					matdescTxtView.setWidth(matDesHWidth);
    					matdescTxtView.setGravity(Gravity.LEFT);
    					matdescTxtView.setPadding(10,0,0,0);
    					
    					TextView unitTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);					
    					unitTxtView.setText(unitsStr);
    					//unitTxtView.setTextColor(getResources().getColor(R.color.lblue));
    					unitTxtView.setWidth(unitsHWidth);
    					unitTxtView.setGravity(Gravity.LEFT);
    					unitTxtView.setPadding(10,0,0,0);
    					
    					TextView qtyTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					qtyTxtView.setText(quantityStr);
    					//qtyTxtView.setTextColor(getResources().getColor(R.color.lblue));
    					qtyTxtView.setWidth(qtyHWidth);
    					qtyTxtView.setGravity(Gravity.RIGHT);
    					qtyTxtView.setPadding(0,0,10,0);
    					
    					TextView transitTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					transitTxtView.setText(transitStr);
    					//transitTxtView.setTextColor(getResources().getColor(R.color.lblue));
    					transitTxtView.setWidth(transitHWidth);
    					transitTxtView.setGravity(Gravity.RIGHT);
    					transitTxtView.setPadding(0,0,10,0);
    					
    					TextView batchTxtView = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
    					batchTxtView.setText(batchStr);		
    					//batchTxtView.setTextColor(getResources().getColor(R.color.lblue));
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
    					
    					try{
	    					for(int k=0; k<subrowSize; k++){
	    						tr = new TableRow(this);
	    						tr.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    						
	                            stkTransCategory = null;
	                            stkTransCategory = (VanStkTransOpConstraints)stocksTransArrList.get(k);
	                            if(stkTransCategory != null){
	                                matrlStr = stkTransCategory.getMaterial().trim();
	                                if(matrlStr.equalsIgnoreCase(matterialStr)){
	                                	stocksTransArrListCopy.add(stkTransCategory);
	                                    
	                                    qtyRecStr = stkTransCategory.getQtyGoodsRecd().trim();
	                                    qtyIssuedStr = stkTransCategory.getQtyGoodsIssd().trim();
	                                    
	                                    try{
	                                        qtyIssued = Float.parseFloat(qtyIssuedStr);
	                                        qtyRecvd = Float.parseFloat(qtyRecStr); 
	                                    }
	                                    catch(Exception sfg){}
	                                    /*ServiceProConstants.showLog("qtyIssued : "+qtyIssued+" : "+qtyIssuedStr);
	                                    ServiceProConstants.showLog("qtyRecvd : "+qtyRecvd+" : "+qtyRecStr);*/
	                                    
	                                    TextView mattTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					TextView werkTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					TextView lgortTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					TextView matdescTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					TextView unitTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					TextView qtyTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					TextView transitTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					TextView batchTxtView1 = (TextView) getLayoutInflater().inflate(R.layout.template_tv, null);
	                					
	                					LinearLayout linearLayout1 = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linear, null);
	                					linearLayout1.setPadding(0, 10, 0, 10);
	                					
	                					mattTxtView1.setText(matterialStr);
	                					mattTxtView1.setWidth(mattHWidth);
	                					mattTxtView1.setVisibility(View.INVISIBLE);
	                					mattTxtView1.setTextColor(Color.BLACK);
	                					  					
	                					werkTxtView1.setText("");
	                					werkTxtView1.setWidth(plantHWidth);
	                					werkTxtView1.setTextColor(Color.BLACK);
	                					
	                					lgortTxtView1.setText("");
	                					lgortTxtView1.setWidth(strLocHWidth);
	                					lgortTxtView1.setTextColor(Color.BLACK);
	                					
	                					matdescTxtView1.setText(mattDescStr);
	                					matdescTxtView1.setWidth(matDesHWidth);
	                					matdescTxtView1.setVisibility(View.INVISIBLE);
	                					matdescTxtView1.setGravity(Gravity.LEFT);
	                					matdescTxtView1.setPadding(10,0,0,0);
	                					matdescTxtView1.setTextColor(Color.BLACK);
	                										
	                					unitTxtView1.setText(stkTransCategory.getPurchaseQty());
	                					unitTxtView1.setWidth(unitsHWidth);
	                					unitTxtView1.setGravity(Gravity.RIGHT);
	                					unitTxtView1.setPadding(0,0,10,0);
	                					unitTxtView1.setTextColor(Color.BLACK);
	                					
	                					qtyTxtView1.setText(stkTransCategory.getSTO());
	                					qtyTxtView1.setWidth(qtyHWidth);
	                					qtyTxtView1.setGravity(Gravity.LEFT);
	                					qtyTxtView1.setPadding(10,0,0,0);
	                					qtyTxtView1.setTextColor(Color.BLACK);
	                					
	                					transitTxtView1.setText(qtyRecStr+"/"+qtyIssuedStr);
	                					transitTxtView1.setWidth(transitHWidth);
	                					transitTxtView1.setGravity(Gravity.RIGHT);
	                					transitTxtView1.setPadding(0,0,10,0);
	                					transitTxtView1.setTextColor(Color.BLACK);
	                					
	                					batchTxtView1.setText("");
	                					batchTxtView1.setWidth(batchHWidth); 
	                					batchTxtView1.setGravity(Gravity.LEFT);
	                					batchTxtView1.setPadding(10,0,0,0);
	                					batchTxtView1.setTextColor(Color.BLACK);
	                					
	                					qtyRecBMF[bmpCount] = new ImageButton(this); 
	                					qtyRecBMF[bmpCount].setId(bmpCount);
	                					qtyRecBMF[bmpCount].setBackgroundResource(R.drawable.goods_accept);
	                					qtyRecBMF[bmpCount].setLayoutParams(linparams);   
	                					qtyRecBMF[bmpCount].setOnClickListener(new View.OnClickListener() {
	            							public void onClick(View view) {
	            								int id = view.getId();	
	            								goodsReceiptAndCancelAction(id, false);
	            							}	
	                                    });
	                					
	                					qtyCancelBMF[bmpCount] = new ImageButton(this); 
	                                    qtyCancelBMF[bmpCount].setId(bmpCount);
	                                    qtyCancelBMF[bmpCount].setBackgroundResource(R.drawable.goods_cancel);
	                                    qtyCancelBMF[bmpCount].setLayoutParams(linparams);   
	                                    qtyCancelBMF[bmpCount].setOnClickListener(new View.OnClickListener() {
	            							public void onClick(View view) {
	            								int id = view.getId();	
	            								goodsReceiptAndCancelAction(id, true);
	            							}	
	                                    });
	                                    	                                
	                                    if((qtyIssued > 0) && (qtyRecvd < qtyIssued))
	                                    	linearLayout1.addView(qtyRecBMF[bmpCount]);    
	                                    else{
	                                    	linearLayout1.addView(qtyRecBMF[bmpCount]);  
	                                    	qtyRecBMF[bmpCount].setVisibility(View.INVISIBLE);
	                                    }
	                                        
	                                    if(qtyRecvd > 0){
	                                    	linearLayout1.addView(qtyCancelBMF[bmpCount]);  
	                                    }
	                                    
	                                    if(dispwidth > ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH){
	                						mattTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                						qtyTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                						unitTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                						transitTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                						matdescTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                						batchTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                						werkTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                						lgortTxtView1.setTextSize(ServiceProConstants.TEXT_SIZE_TABLE_ROW);
	                					}
	                                    
	                                    tr.addView(mattTxtView1);
	                					tr.addView(linearLayout1);
	                					tr.addView(qtyTxtView1);
	                					tr.addView(unitTxtView1);
	                					tr.addView(transitTxtView1);
	                					tr.addView(matdescTxtView1);
	                					tr.addView(batchTxtView1);
	                					tr.addView(werkTxtView1);
	                					tr.addView(lgortTxtView1);
	                					
	                					if(i%2 == 0)
	                						tr.setBackgroundResource(R.color.item_even_color);
	            			            else
	            			            	tr.setBackgroundResource(R.color.item_odd_color);
	                					
	                					tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	                                    
	                                    bmpCount++;
	                                }
	                            }
	                        }
                    	}
                    	catch(Exception sgfg){
                    		ServiceProConstants.showErrorLog("Error in initLayout SubLoop: "+sgfg.toString());
                    	}
                    }					
				}
			}
		}
		catch(Exception asgf){
			ServiceProConstants.showErrorLog("On drawSubLayout : "+asgf.toString());
		}
	}//fn drawSubLayout
	
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
	
	private void displayStockColleagues(){
		try{
            if(stocksCollArrList != null){
               if(stocksCollArrList.size() > 0){
            	   VanStkColOpConstraints empObj = null;
                   String spareMattStr="", spareMattDescStr="", combStr="";
                   
            	   colItems = new CharSequence[stocksCollArrList.size()];
            	   for(int h=0; h<stocksCollArrList.size(); h++){
                       empObj = (VanStkColOpConstraints)stocksCollArrList.get(h);
                       if(empObj != null){
                           spareMattStr = empObj.getPartnerName1().trim();
                           spareMattDescStr = empObj.getPartnerName2().trim();
                           combStr = spareMattStr+" "+spareMattDescStr;
                           colItems[h] = combStr;
                       }
                   }
            	   
            	   	AlertDialog.Builder builder = new AlertDialog.Builder(this);
       				builder.setTitle("VanStock with other Rep");
	       			builder.setSingleChoiceItems(colItems, -1, new DialogInterface.OnClickListener() {
	       			    public void onClick(DialogInterface dialog, int item) {
	       			        //Toast.makeText(getApplicationContext(), colItems[item], Toast.LENGTH_SHORT).show();
	       			        dialog.cancel(); 
	       			        doColleagueAction(item);
	       			    }
	       			});
	       			AlertDialog alert = builder.create();
	       			alert.show();
               }
               else{
               		ServiceProConstants.showErrorDialog(this, "No Colleagues to display");
               }
            }
            else{
            	ServiceProConstants.showErrorDialog(this, "No Colleagues to display");
            }
        }
        catch(Exception asf){
        	ServiceProConstants.showErrorLog("Error in displayStockColleagues : "+asf.toString());
        }
	}//fn displayStockColleagues
	
	
	private void doColleagueAction(int index){
        try{            
            String partnerName1Str="", partnerName2Str="", partnerIdStr="", partnerUNameStr="", nameStr = "";
            String telephoneNo1Str = "", telephoneNo2Str = "";
            if(index < 0)
                index = 0;
            if((stocksCollArrList != null) && (index < stocksCollArrList.size())){
                VanStkColOpConstraints empObj = null;
                empObj = (VanStkColOpConstraints)stocksCollArrList.get(index);
                if(empObj != null){
                    partnerName1Str = empObj.getPartnerName1().trim();
                    partnerName2Str = empObj.getPartnerName2().trim();
                    partnerIdStr = empObj.getPartnerId().trim();
                    partnerUNameStr = empObj.getUName().trim();
                    telephoneNo1Str = empObj.getTelephoneNo1().trim();
                    telephoneNo2Str = empObj.getTelephoneNo2().trim();
                }
            }
            
            if(!partnerIdStr.equalsIgnoreCase("")){
                nameStr = partnerName1Str+" "+partnerName2Str;
                //ServiceProConstants.showLog("partnerIdStr : "+partnerIdStr+" : "+nameStr);
                try {
        			Intent intent = new Intent(this, VanStockColleagueScreen.class);
        			intent.putExtra("vStkColId", partnerIdStr);
        			intent.putExtra("vStkColUName", partnerUNameStr);   
        			intent.putExtra("vStkColName", nameStr);
        			intent.putExtra("vStkColTel1", telephoneNo1Str);
        			intent.putExtra("vStkColTel2", telephoneNo2Str);
        			startActivityForResult(intent, ServiceProConstants.VANSTOCK_COL_SCREEN);
        		} 
        		catch (Exception e) {
        			ServiceProConstants.showErrorLog(e.getMessage());
        		}
            }
            else
            	ServiceProConstants.showErrorDialog(this, "Please select a Colleague!");
        }
        catch(Exception sfd){
        	ServiceProConstants.showErrorLog("Error in doColleagueAction : "+sfd.toString());
        }
    }//fn doColleagueAction
	
	private void goodsReceiptAndCancelAction(int selIndex, boolean cancelFlag){
        VanStkTransOpConstraints stkTransCategory1 = null;
        try{
            if(stocksTransArrListCopy != null){
                if(stocksTransArrListCopy.size() > selIndex){
                    stkTransCategory1 = (VanStkTransOpConstraints)stocksTransArrListCopy.get(selIndex);
                    if(stkTransCategory1 != null){
                        String stoId = stkTransCategory1.getSTO().toString();
                        stoId = stoId.trim();
                        //ServiceProConstants.showLog("Index : "+selIndex+" : Sto Id : "+stoId+" : "+cancelFlag);
                        if(!stoId.equalsIgnoreCase("")){
                        	initGoodsRecpAndCancelConnection(stoId, cancelFlag);
                        }
                    }
                }
            }                      
        }
        catch(Exception sdgf){
        	ServiceProConstants.showErrorLog("Error in goodsReceiptAndCancelAction : "+sdgf.toString());
        }
    }//fn goodsReceiptAndCancelAction
	
	
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
	
	
	private void showVanStockCreationScreen(int selIndex){
        try{
            System.out.println("Selected Index : "+selIndex);
            VanStkOpConstraints stkCategory = null;
            boolean errFlag = false;
            
            if(stocksArrList != null){
                if(stocksArrList.size() > selIndex){
                    stkCategory = (VanStkOpConstraints)stocksArrList.get(selIndex);
                    if(stkCategory != null){
                    	try {
							Intent intent = new Intent(this, VanStockTransOrdCreateScreen.class);
							intent.putExtra("stkCategoryObj", stkCategory);
							//intent.putExtra("stocksCollVectObj", stocksCollSrlArrList);
							ServiceProConstants.stocksCollSrlArrList = stocksCollSrlArrList;
							startActivityForResult(intent, ServiceProConstants.VANSTOCK_TRANSCRT_SCREEN);
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
        catch(Exception assf){
        	ServiceProConstants.showErrorLog("On showVanStockCreationScreen : "+assf.toString());
        }
    }//fn showVanStockCreationScreen
	
	
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
	
	public void refreshDataAfterStockCreation(){
        try{
            initSoapConnection();
        }
        catch(Exception sgfg){
        	ServiceProConstants.showErrorLog("Error in refreshDataAfterStockCreation : "+sgfg.toString());
        }
    }//fn refreshDataAfterStockCreation
	
	private void initGoodsRecpAndCancelConnection(String sid, boolean cancFlag){
		SoapSerializationEnvelope envelopeCR = null;
		//ServiceProConstants.showLog("sid : "+sid);
		try{
			SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
			envelopeCR = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            SapTasksInputConstraints C0[];
            C0 = new SapTasksInputConstraints[4];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new SapTasksInputConstraints(); 
            }
            
            
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            if(cancFlag == false)
                C0[2].Cdata = "EVENT[.]VAN-STOCK-GOODS-RECEIVE[.]VERSION[.]0";
            else
                C0[2].Cdata = "EVENT[.]VAN-STOCK-GOODS-RECEIPT-REVERSE-LAST[.]VERSION[.]0";                
            C0[3].Cdata = "ZGSXSMST_STO[.]"+sid+"[.]";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
            
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeCR.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
            vanMainFlag = false;
            doThreadNetworkAction(this, ntwrkHandler, getNetworkResponseRunnable, envelopeCR, request);
            //startNetworkConnection(this, envelopeCR, ServiceProConstants.SOAP_SERVICE_URL, false);
        }
        catch(Exception asd){
        	ServiceProConstants.showErrorLog("Error in initGoodsRecpAndCancelConnection : "+asd.toString());
        }
    }//fn initGoodsRecpAndCancelConnection
	
	
	private void initSoapConnection(){        
        SoapSerializationEnvelope envelopeC = null;
        try{
            SoapObject request = new SoapObject(ServiceProConstants.SOAP_SERVICE_NAMESPACE, ServiceProConstants.SOAP_TYPE_FNAME); 
            envelopeC = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            
            SapTasksInputConstraints C0[];
            C0 = new SapTasksInputConstraints[3];
            
            for(int i=0; i<C0.length; i++){
                C0[i] = new SapTasksInputConstraints(); 
            }
            
            
            C0[0].Cdata = ServiceProConstants.getApplicationIdentityParameter(this);
            C0[1].Cdata = ServiceProConstants.SOAP_NOTATION_DELIMITER;
            C0[2].Cdata = "EVENT[.]VAN-STOCK-FOR-MYSELF-GET[.]VERSION[.]0";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
          
            //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL, true);
            vanMainFlag = true;
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
    				VanStockOverviewScreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                        	pdialog = ProgressDialog.show(VanStockOverviewScreen.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
                        	new Thread() {
                        		public void run() {
                        			try{
                        				if(vanMainFlag == true)
                                    		updateReportsConfirmResponse(resultSoap);
                                    	else{
                                    		VanStockOverviewScreen.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                            		updateGoodsReceiptAndCancelResponse(resultSoap);                                                	
                                                }
                                            });
                                    	}
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
	
	
	/*private void getSOAPViaHTTP(SoapSerializationEnvelope envelopeCE, String url, final boolean vanMainFlag){		
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
                	//ServiceProConstants.showLog("Results : "+result);
                    
                    SoapObject result1 = (SoapObject)envelopeCE.bodyIn; 
                    //ServiceProConstants.showLog("Results1 : "+result1.toString());
                    
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
                	if(vanMainFlag == true)
                		updateReportsConfirmResponse(resultSoap);
                	else
                		updateGoodsReceiptAndCancelResponse(resultSoap);
                }
            });
        }
    }//fn getSOAPViaHTTP
*/	
	
	public void updateGoodsReceiptAndCancelResponse(SoapObject soap){
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
                    //ServiceProConstants.showLog("propsCount : "+propsCount);
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
                            else if(j < 3){
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
                                    	ServiceProConstants.showErrorLog("Inside Errcount");
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
            	if(!resMsgErr){
            		if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
            		stopProgressDialog();
            		refreshDataAfterStockCreation();
            		/*VanStockOverviewScreen.this.runOnUiThread(new Runnable() {
        	            public void run() {
        	            	
        	            	}
        	        });  */
            	}else{
            		onClose();
            	}
                /*if(taskErrorMsgStr.equalsIgnoreCase("")){
                    onClose();
                }
                else if((taskErrorMsgStr.indexOf("Doc") > 0) && (taskErrorMsgStr.indexOf("created") > 0)){
                	if(diagdispFlag == false)
                		ServiceProConstants.showErrorDialog(this, taskErrorMsgStr);
                	
                	refreshDataAfterStockCreation();
                }*/
            }
            catch(Exception asf){}
        }
	}//fn updateGoodsReceiptAndCancelResponse
	
	public void updateReportsConfirmResponse(SoapObject soap){		
		VanStkOpConstraints stkCategory = null;
        VanStkSerOpConstraints stkSerCategory = null;
        VanStkTransOpConstraints stkTransCategory = null;
        VanStkColOpConstraints stkColCategory = null;
        VanStkColStrOpConstraints stkColLocCategory = null;
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
                            if(j > 6){
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
                                    //ServiceProConstants.showLog(resC+" : "+res);
                                    resArray[resC] = res;
                                    indexA = indexB + delimeter.length();
                                    indexB = result.indexOf(delimeter, indexA);
                                    resC++;
                                }
                                
                                int endIndex = result.lastIndexOf(';');
                                resArray[resC] = result.substring(indexA, endIndex);
                                //ServiceProConstants.showLog(resC+" : "+resArray[resC]);
                                
                                
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
                                else if(docTypeStr.equalsIgnoreCase("ZGSEMEST_POITEM10A")){
                                    if(stkTransCategory != null)
                                        stkTransCategory = null;
                                        
                                    stkTransCategory = new VanStkTransOpConstraints(resArray);
                                    if(stocksTransArrList != null)
                                        stocksTransArrList.add(stkTransCategory);
                                }
                                else if(docTypeStr.equalsIgnoreCase("ZGSXCAST_EMPLY01")){
                                    if(stkColCategory != null)
                                        stkColCategory = null;
                                        
                                    stkColCategory = new VanStkColOpConstraints(resArray);
                                    if(stocksCollArrList != null)
	                                	stocksCollArrList.add(stkColCategory);
                                }
                                else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYSTRGLCTN10")){
                                    if(stkColLocCategory != null)
                                        stkColLocCategory = null;
                                        
                                    stkColLocCategory = new VanStkColStrOpConstraints(resArray);
                                    if(stocksCollSrlArrList != null)
                                        stocksCollSrlArrList.add(stkColLocCategory);
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
        }
        catch(Exception sff){
            ServiceProConstants.showErrorLog("On updateTaskResponse : "+sff.toString());
        } 
        finally{
        	ServiceProConstants.showLog("Stocks List Size : "+stocksArrList.size());
        	ServiceProConstants.showLog("Stocks Serialized List Size : "+stocksSerArrList.size());
        	ServiceProConstants.showLog("Stocks Transport List Size : "+stocksTransArrList.size());
        	ServiceProConstants.showLog("Stocks Coll List Size : "+stocksCollArrList.size());
        	ServiceProConstants.showLog("Stocks Colleagues Location Size : "+stocksCollArrList.size());
        	Collections.sort(stocksArrList, stockSortComparator);           
        	stopProgressDialog();
        	VanStockOverviewScreen.this.runOnUiThread(new Runnable() {
	            public void run() {
	            	initLayout();
	            }
	        });  
        }
    }//fn updateReportsConfirmResponse     
	
	private void emptyAllVectors(){
        try{
            searchStr = "";
            
            if(stocksArrList != null)
                stocksArrList.clear();
                
            if(stocksCopyArrList != null)
                stocksCopyArrList.clear();
            
            if(stocksSerArrList != null)
                stocksSerArrList.clear();
                
            if(stocksTransArrList != null)
                stocksTransArrList.clear();
                
            if(stocksTransArrListCopy != null)
                stocksTransArrListCopy.clear();
                
            if(stocksCollArrList != null)
                stocksCollArrList.clear();
                
            if(stocksCollSrlArrList != null)
                stocksCollSrlArrList.clear();
                
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
    
    
    /*private void startNetworkConnection(final Context ctxAct, final SoapSerializationEnvelope envelopeCE, final String url, final boolean vanMainFlag){
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
                    				getSOAPViaHTTP(envelopeCE, url, vanMainFlag);
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
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	try {   			
    		ServiceProConstants.showLog("Request Code "+requestCode);
    		ServiceProConstants.showLog("Result Code "+resultCode);
    		ServiceProConstants.showLog("Result Code "+RESULT_OK);
    		
    		if(requestCode == ServiceProConstants.VANSTOCK_TRANSCRT_SCREEN){
    			if(resultCode == RESULT_OK)
    				initSoapConnection();
    		}
			//finish();
		} catch (Exception e) {
			ServiceProConstants.showErrorLog(e.toString());
		}
    }//fn onActivityResult
    
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
    
}//End of class VanStockOverviewScreen

