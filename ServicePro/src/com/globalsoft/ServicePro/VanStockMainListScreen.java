package com.globalsoft.ServicePro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.globalsoft.SapLibSoap.SoapConnection.StartNetworkTask;
import com.globalsoft.SapLibSoap.Utils.SapGenConstants;
import com.globalsoft.ServicePro.Constraints.SapTasksInputConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkColOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkColStrOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkSerOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkTransOpConstraints;
import com.globalsoft.ServicePro.Database.VanStkDBOperations;
import com.globalsoft.ServicePro.Database.VanStkOfflineContraintsCP;

public class VanStockMainListScreen extends ListActivity implements TextWatcher {
	
	private ProgressDialog pdialog = null;
	private EditText searchET;	
	private ListView listView;
	private vanStockListAdapter stockListAdapter;
	
	private ArrayList stocksArrList = new ArrayList();
	private ArrayList stocksCopyArrList = new ArrayList();
	private ArrayList stocksSerArrList = new ArrayList();
    private ArrayList stocksTransArrList = new ArrayList();
    private ArrayList stocksTransArrListCopy = new ArrayList();
	private ArrayList stocksCollArrList = new ArrayList();
	private ArrayList stocksCollSrlArrList = new ArrayList();
	
	private int stocksListCount = 0;
	private int stocksSerListCount = 0;
	private int stocksTransListCount = 0;
	private int stocksCollListCount = 0;
	private int stocksCollSrlListCount = 0;
	
	private String stocksListType = "";
	private String stocksSerListType = "";
	private String stocksTransListType = "";
	private String stocksCollListType = "";
	private String stocksCollSrlListType = "";
	public boolean flag_pref = false;
	
	private CharSequence[] colItems;
	private SoapObject resultSoap = null;
	
	private boolean sortFlag = false, sortMattFlag = false, sortMattDescFlag = false, searchflag = true;
    private int sortIndex = -1;
    private String searchStr = "", taskErrorMsgStr = "";
    private boolean diagdispFlag = false;
    private int mattHWidth, buttonHWidth, qtyHWidth, unitsHWidth, transitHWidth;
    private int matDesHWidth, batchHWidth, plantHWidth, strLocHWidth;
    private int dispwidth = 300;
    private static boolean isConnAvail = true;
    private static String labelStkStr = "", labelTransitStr = "";
    
    private static final int MENU_ID_FIRST = Menu.FIRST;
    private static final int MENU_ID = Menu.FIRST+1;
    private static final int MENU_ID_SORT1 = MENU_ID+2;
    private static final int MENU_ID_SORT2 = MENU_ID+3;
    private static final int MENU_ID_VIEW = MENU_ID+4;

	private StartNetworkTask soapTask = null;
	private final Handler ntwrkHandler = new Handler();
	private static int respType = 0;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	/*requestWindowFeature(Window.FEATURE_LEFT_ICON);
        	ServiceProConstants.setWindowTitleTheme(this);
			this.setTitle(R.string.SERVICEPRO_VANSTOCK_OVRVIEW_TITLE);
			setContentView(R.layout.vanstocklistmain);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleappicon);*/
			
			ServiceProConstants.setWindowTitleTheme(this);   
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
	        setContentView(R.layout.vanstocklistmain); 
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle); 	    	
			TextView myTitle = (TextView) findViewById(R.id.myTitle);
			myTitle.setText(getResources().getString(R.string.SERVICEPRO_VANSTOCK_OVRVIEW_TITLE));

			int dispwidthTitle = SapGenConstants.getDisplayWidth(this);	
			if(dispwidthTitle > SapGenConstants.SCREEN_CHK_DISPLAY_WIDTH){
				myTitle.setTextAppearance(this, R.style.titleTextStyleBig); 
			}
			
			SharedPreferences settings = getSharedPreferences(ServiceProConstants.PREFS_NAME_FOR_VAN_STOCK, 0);      
			flag_pref = settings.getBoolean(ServiceProConstants.PREFS_KEY_VAN_STOCK_FOR_MYSELF_GET, false);
			
			isConnAvail = ServiceProConstants.checkInternetConn(this);
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
	
	 public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ID_FIRST, 0, "Replenishments");
        menu.add(0, MENU_ID, 0, "Stock with other Rep");
        if(dispwidth < ServiceProConstants.SCREEN_CHK_DISPLAY_WIDTH)
        	menu.add(0, MENU_ID_VIEW, 0, "Change View");
        menu.add(1, MENU_ID_SORT1, 1, "Sort by Material");
        menu.add(1, MENU_ID_SORT2, 2, "Sort by Matt. Desc");
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
			case MENU_ID_FIRST: {
				showVanStockScreen();
				break;
			}
			case MENU_ID: {
				//ServiceProConstants.showLog("Menu Clicked !");
				displayStockColleagues();
		        break;
			}
			case MENU_ID_SORT1: {
				sortItemsAction(ServiceProConstants.VANSTOCK_SORT_MATERIAL);
				break;
			}
			case MENU_ID_SORT2: {
				sortItemsAction(ServiceProConstants.VANSTOCK_SORT_MATDESC);
				break;
			}
			case MENU_ID_VIEW: {
				onClose();
				break;
			}
	    }
		return super.onOptionsItemSelected(item);
	}
		
	private void initLayout(){
		try {        	
			setContentView(R.layout.vanstocklistmain);
			dispwidth = ServiceProConstants.getDisplayWidth(this);
			
			 if(stocksTransArrListCopy != null)
				 stocksTransArrListCopy.clear();
			 
			searchET = (EditText)findViewById(R.id.searchBEF);
			searchET.setText(searchStr);
			searchET.addTextChangedListener(this);
			
			listView = (ListView)findViewById(android.R.id.list);
			listView.setTextFilterEnabled(true);
			listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1));
			listView.setOnItemClickListener(listItemClickListener);    
			stockListAdapter = new vanStockListAdapter(this);
			setListAdapter(stockListAdapter);
	        
			searchET.setFocusable(true);
			searchET.setFocusableInTouchMode(true);
			searchET.requestFocus();
			labelStkStr = getResources().getString(R.string.SERVICEPRO_VANSTOCK_STOCK); 
			labelTransitStr = getResources().getString(R.string.SERVICEPRO_VANSTOCK_INTRANSIT); 
		}catch (Exception ssdf) {
			ServiceProConstants.showErrorLog("Error in initLayout : "+ssdf.toString());
		}
	}//fn initLayout
	
	
	public class vanStockListAdapter extends BaseAdapter {      
        
    	private LayoutInflater mInflater;
    	private VanStkOpConstraints stkCategory;
    	String matterialStr = "", syncStr = "", transitStr = "";
        String mattDescStr = "", unitsStr = "", quantityStr = "";
    	
        public vanStockListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
        	try {
				if(stocksArrList != null)
					return stocksArrList.size();
			}
        	catch (Exception e) {
        		ServiceProConstants.showErrorLog(e.getMessage());
			}
        	return 0;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;            
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.vanstocklistmain_list, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.custloc);
                holder.taskid = (TextView) convertView.findViewById(R.id.taskid);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
                holder.errstatus = (ImageView) convertView.findViewById(R.id.errstatus); 
                holder.llitembg = (LinearLayout) convertView.findViewById(R.id.llitembg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
                       
            if(position%2 == 0)
            	holder.llitembg.setBackgroundResource(R.color.item_even_color);
            else
            	holder.llitembg.setBackgroundResource(R.color.item_odd_color);
            
            String data = "";
            if(stocksArrList != null){
            	stkCategory = ((VanStkOpConstraints)stocksArrList.get(position));
            	if(stkCategory != null){
            		try {
						matterialStr = stkCategory.getMaterial().trim();                            
						mattDescStr = stkCategory.getMattDesc().trim();
						unitsStr = stkCategory.getMeasureUnits().trim();
						quantityStr = stkCategory.getStockQty().trim();        
						transitStr = stkCategory.getMatQtyTransit().trim();              
						syncStr = stkCategory.getSerAvailable().trim();
					} catch (Exception e) {}     
                    
	        		 if(!syncStr.equalsIgnoreCase(ServiceProConstants.VANSTOCK_SYNC_VAL)){
	        			 holder.indicator.setVisibility(View.INVISIBLE);
	                 }
	        		 else
	        			 holder.indicator.setVisibility(View.VISIBLE);
            		
                    holder.date.setText(matterialStr);
                    holder.text.setText(mattDescStr);
                    holder.taskid.setText(labelStkStr+": "+quantityStr+" "+unitsStr+"\n"+labelTransitStr+": "+transitStr);
                    
            		//boolean isExits = ServiceProConstants.errorTaskIdForStatus.contains(category.getObjectId().toString().trim());
            		boolean isExits = false;
            		if(isExits){
            			holder.errstatus.setVisibility(View.VISIBLE);
            		}
            		else{
            			holder.errstatus.setVisibility(View.GONE);
            		}
            	}
            }          
            
            holder.indicator.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	try {
                		showVanStockSerialDetailScreen(position);
					} catch (Exception ae) {
						ServiceProConstants.showErrorLog(ae.getMessage());
					}
                }
            });
            
            return convertView;
        }
        
        
        class ViewHolder {
            TextView text;
            TextView taskid;
            TextView date;
            ImageView indicator;
            ImageView status;
            ImageView errstatus;
            LinearLayout llitembg;
        }
        
        public void removeAllTasks() {
            notifyDataSetChanged();
        }        
    }//End of vanStockListAdapter

	
	OnItemClickListener listItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			try{
				//ServiceProConstants.showLog("Selected Item "+arg2);
				showVanStockDetailScreen(arg2);
			}
			catch (Exception dee) {
		    	ServiceProConstants.showErrorLog("Error in listItemClickListener:"+dee.toString());
		    }
		}
	};
	
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
	
	private void showVanStockScreen(){
		try {
			Intent intent = new Intent(this, VanStockOverviewScreen.class);
			intent.putExtra("stocksArrList", stocksArrList);
			intent.putExtra("stocksCopyArrList", stocksCopyArrList);
			intent.putExtra("stocksSerArrList", stocksSerArrList);
			intent.putExtra("stocksTransArrList", stocksTransArrList);
			intent.putExtra("stocksCollArrList", stocksCollArrList);
			intent.putExtra("stocksCollSrlArrList", stocksCollSrlArrList);
			startActivityForResult(intent, ServiceProConstants.VANSTOCK_OVERVIEW_SCREEN);
		} 
		catch (Exception e) {
			ServiceProConstants.showErrorLog(e.getMessage());
		}
	}//fn showVanStockScreen 
	
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
	
	
	private void showVanStockDetailScreen(int selIndex){
        try{
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
        	isConnAvail = ServiceProConstants.checkInternetConn(this);
        	if(isConnAvail ==  true)
        		initSoapConnection();
        	else
        		initDBConnection(); 
        }
        catch(Exception sgfg){
        	ServiceProConstants.showErrorLog("Error in refreshDataAfterStockCreation : "+sgfg.toString());
        }
    }//fn refreshDataAfterStockCreation
	
	
	private void initDBConnection(){
		try {
			stocksArrList = VanStkDBOperations.readAllVanSTkEmpDataFromDB(this);
			if(stocksArrList != null)
				stocksCopyArrList = (ArrayList)stocksArrList.clone();
				//stocksCopyArrList = new ArrayList(stocksArrList);
			
			stocksSerArrList = VanStkDBOperations.readAllVanSTkEmpSrlDataFromDB(this);
			stocksTransArrList = VanStkDBOperations.readAllVanSTkEmpSTODataFromDB(this);
			if(stocksTransArrList != null)
				stocksTransArrListCopy = (ArrayList)stocksTransArrList.clone();
			
			stocksCollArrList = VanStkDBOperations.readAllVanSTkColDataFromDB(this);
			stocksCollSrlArrList = VanStkDBOperations.readAllVanSTkColSrlDataFromDB(this);
			
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

			if(!flag_pref){
				C0[2].Cdata = "EVENT[.]VAN-STOCK-FOR-MYSELF-GET[.]VERSION[.]0[.]RESPONSE-TYPE[.]FULL-SETS";
			}else{
				C0[2].Cdata = "EVENT[.]VAN-STOCK-FOR-MYSELF-GET[.]VERSION[.]0";
			}
            //C0[2].Cdata = "EVENT[.]VAN-STOCK-FOR-MYSELF-GET[.]VERSION[.]0";
            //C0[2].Cdata = "EVENT[.]VAN-STOCK-FOR-MYSELF-GET[.]VERSION[.]0[.]RESPONSE-TYPE[.]FULL-SETS";
        
            Vector listVect = new Vector();
            for(int k=0; k<C0.length; k++){
                listVect.addElement(C0[k]);
            }
        
            request.addProperty (ServiceProConstants.SOAP_INPUTPARAM_NAME, listVect);            
            envelopeC.setOutputSoapObject(request);                    
            ServiceProConstants.showLog(request.toString());
          
            //startNetworkConnection(this, envelopeC, ServiceProConstants.SOAP_SERVICE_URL, true);
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
    				VanStockMainListScreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                        	pdialog = ProgressDialog.show(VanStockMainListScreen.this, "", getString(R.string.SERVICEPRO_WAIT_TEXTS),true);
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
                }
            });
        }
    }//fn getSOAPViaHTTP
*/	
	
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
                String[] docTypeArray = new String[5];
                int propsCount = 0, indexA = 0, indexB = 0, firstIndex = 0, resC = 0, eqIndex = 0;                
                
                for (int i = 0; i < soap.getPropertyCount(); i++) {                
                    pii = (SoapObject)soap.getProperty(i);
                    propsCount = pii.getPropertyCount();
                    //ServiceProConstants.showLog("propsCount : "+propsCount);
                    if(propsCount > 0){
                        for (int j = 0; j < propsCount; j++) {
                            ServiceProConstants.showLog(j+" : "+pii.getProperty(j).toString());
                            if(j > 1 && j <= 6){
                            	result = pii.getProperty(j).toString();
                                firstIndex = result.indexOf(delimeter);
                                eqIndex = result.indexOf("=");
                                eqIndex = eqIndex+1;
                                firstIndex = firstIndex + 3;
                                docTypeStr = result.substring(eqIndex, (firstIndex-3));
                                result = result.substring(firstIndex);
                                //ServiceProConstants.showLog("Result : "+result);                                
                                resC = 0;
                                indexA = 0;
                                indexB = result.indexOf(delimeter);
                                int index1 = 0;
                                while (indexB != -1) {
                                    res = result.substring(indexA, indexB);
                                    indexA = indexB + delimeter.length();
                                    indexB = result.indexOf(delimeter, indexA);
                                    //ServiceProConstants.showLog("Result resp : "+resC+" : "+res);
                                    if(resC == 0){
                                    	docTypeStr = res;
                                    }
                                    if(resC == 1){
	                                    String[] respStr = res.split(";");
	                                    if(respStr.length >= 1){
	                                    	String respTypeData = respStr[0];
	                                    	//ServiceProConstants.showLog("respTypeData : "+respTypeData);
	                                    	index1 = respTypeData.indexOf("=");
	                                    	index1 = index1+1;
	                                        String respType = respTypeData.substring(index1, respTypeData.length());
	                                    	//ServiceProConstants.showLog("respType : "+respType);
	                                    	
	                                    	String rowCountStrData = respStr[1];
	                                    	//ServiceProConstants.showLog("rowCountStrData : "+rowCountStrData);
	                                    	index1 = rowCountStrData.indexOf("=");
	                                    	index1 = index1+1;
	                                        String rowCount = rowCountStrData.substring(index1, rowCountStrData.length());
	                                    	//ServiceProConstants.showLog("rowCount : "+rowCount);
	                                        if(j == 2){
	                                    		//else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLSTCK10S")){
	                                    		docTypeArray[1] = docTypeStr;
		                                        ServiceProConstants.showLog("j : "+j);
		                                    	ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
		                                    	stocksSerListCount = Integer.parseInt(rowCount);
		                                    	stocksSerListType = respType;
	                                        }	                                    	
	                                    	else if(j == 3){
		                                    	//if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYMTRLSTCK10C")){
	                                    		docTypeArray[0] = docTypeStr;
		                                        ServiceProConstants.showLog("j : "+j);
		                                    	ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
		                                    	stocksListCount = Integer.parseInt(rowCount);
		                                    	stocksListType = respType;
	                                        }
	                                    	else if(j == 4){
	                                    		//else if(docTypeStr.equalsIgnoreCase("ZGSEMEST_POITEM10A")){
	                                    		docTypeArray[2] = docTypeStr;
		                                        ServiceProConstants.showLog("j : "+j);
		                                    	ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
		                                    	stocksTransListCount = Integer.parseInt(rowCount);
		                                    	stocksTransListType = respType;
	                                        }
	                                    	else if(j == 5){
	                                    		//else if(docTypeStr.equalsIgnoreCase("ZGSXCAST_EMPLY01")){
	                                    		docTypeArray[3] = docTypeStr;
		                                        ServiceProConstants.showLog("j : "+j);
		                                    	ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
		                                    	stocksCollListCount = Integer.parseInt(rowCount);
		                                    	stocksCollListType = respType;
	                                        }
	                                    	else if(j == 6){
	                                    		//else if(docTypeStr.equalsIgnoreCase("ZGSXSMST_EMPLYSTRGLCTN10")){
	                                    		docTypeArray[4] = docTypeStr;
		                                        ServiceProConstants.showLog("j : "+j);
		                                    	ServiceProConstants.showLog("docTypeStr :"+ docTypeStr+"  rowCount : "+rowCount+"  respType : "+respType);
		                                    	stocksCollSrlListCount = Integer.parseInt(rowCount);
		                                    	stocksCollSrlListType = respType;
	                                        }
	                                    }
                                    }
                                    resC++;
                                    if(resC == 2)
                                    	break;
                                }                                
                            }
                            if(j > 6){
                                result = pii.getProperty(j).toString();
                                firstIndex = result.indexOf(delimeter);
                                eqIndex = result.indexOf("=");
                                eqIndex = eqIndex+1;
                                firstIndex = firstIndex + 3;
                                docTypeStr = result.substring(eqIndex, (firstIndex-3));
                                result = result.substring(firstIndex);
                                //ServiceProConstants.showLog("Document Type : "+docTypeStr);
                                //ServiceProConstants.showLog("Result : "+result);
                                
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
                                
                                
                                if(docTypeStr.equalsIgnoreCase(docTypeArray[0])){
                                    if(stkCategory != null)
                                        stkCategory = null;
                                        
                                    stkCategory = new VanStkOpConstraints(resArray);
                                    if(stocksArrList != null)
	                                	stocksArrList.add(stkCategory);	  
	                                
	                                if(stocksCopyArrList != null)
	                                	stocksCopyArrList.add(stkCategory);
                                }
                                else if(docTypeStr.equalsIgnoreCase(docTypeArray[1])){
                                    if(stkSerCategory != null)
                                        stkSerCategory = null;
                                        
                                    stkSerCategory = new VanStkSerOpConstraints(resArray);
                                    if(stocksSerArrList != null)
                                        stocksSerArrList.add(stkSerCategory);
                                }
                                else if(docTypeStr.equalsIgnoreCase(docTypeArray[2])){
                                    if(stkTransCategory != null)
                                        stkTransCategory = null;
                                        
                                    stkTransCategory = new VanStkTransOpConstraints(resArray);
                                    if(stocksTransArrList != null)
                                        stocksTransArrList.add(stkTransCategory);
                                }
                                else if(docTypeStr.equalsIgnoreCase(docTypeArray[3])){
                                    if(stkColCategory != null)
                                        stkColCategory = null;
                                        
                                    stkColCategory = new VanStkColOpConstraints(resArray);
                                    if(stocksCollArrList != null)
	                                	stocksCollArrList.add(stkColCategory);
                                }
                                else if(docTypeStr.equalsIgnoreCase(docTypeArray[4])){
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
        	else{
        		initDBConnection();
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
        	ServiceProConstants.showLog("Stocks Colleagues Location Size : "+stocksCollSrlArrList.size());
        	try {
        		
				/*if((stocksArrList != null) && (stocksArrList.size() > 0)){
					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_CONTENT_URI);
					insertEmployeeDataIntoDB();
				}*/				
        		if(stocksArrList != null){
        			if(stocksListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
        				if((stocksListCount == 0) && (stocksArrList.size() == 0)){
            				VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_CONTENT_URI);
            			}
        				else if((stocksListCount > 0) && (stocksArrList.size() > 0)){
            				VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_CONTENT_URI);
        					insertEmployeeDataIntoDB();
            			}
        			}
        			if(stocksListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){
        				if((stocksListCount == 0) && (stocksArrList.size() == 0)){
            				VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_CONTENT_URI);
            			}
        				else if((stocksListCount > 0) && (stocksArrList.size() == 0)){
            			}
        				else if((stocksListCount > 0) && (stocksArrList.size() > 0)){
            				VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_CONTENT_URI);
        					insertEmployeeDataIntoDB();
            			}
        			}
        		}
        		
				/*if((stocksSerArrList != null) && (stocksSerArrList.size() > 0)){
					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_SRL_CONTENT_URI);
					insertEmployeeSerialDataIntoDB();
				}*/        		
        		if(stocksSerArrList != null){
        			if(stocksSerListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
        				if((stocksSerListCount == 0) && (stocksSerArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_SRL_CONTENT_URI);
            			}
        				else if((stocksSerListCount > 0) && (stocksSerArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_SRL_CONTENT_URI);
        					insertEmployeeSerialDataIntoDB();
            			}
        			}
        			if(stocksSerListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){
        				if((stocksSerListCount == 0) && (stocksSerArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_SRL_CONTENT_URI);
            			}
        				else if((stocksSerListCount > 0) && (stocksSerArrList.size() == 0)){
            			}
        				else if((stocksSerListCount > 0) && (stocksSerArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_SRL_CONTENT_URI);
        					insertEmployeeSerialDataIntoDB();
            			}
        			}
        		}
        					
				/*if((stocksTransArrList != null) && (stocksTransArrList.size() > 0)){
					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_STO_CONTENT_URI);
					insertEmployeeSTODataIntoDB();
				}*/				
				if(stocksTransArrList != null){
        			if(stocksTransListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
        				if((stocksTransListCount == 0) && (stocksTransArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_STO_CONTENT_URI);
            			}
        				else if((stocksTransListCount > 0) && (stocksTransArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_STO_CONTENT_URI);
        					insertEmployeeSTODataIntoDB();
            			}
        			}
        			if(stocksTransListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){
        				if((stocksTransListCount == 0) && (stocksTransArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_STO_CONTENT_URI);
            			}
        				else if((stocksTransListCount > 0) && (stocksTransArrList.size() == 0)){
            			}
        				else if((stocksTransListCount > 0) && (stocksTransArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_EMP_STO_CONTENT_URI);
        					insertEmployeeSTODataIntoDB();
            			}
        			}
        		}
				
				/*if((stocksCollArrList != null) && (stocksCollArrList.size() > 0)){
					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_CONTENT_URI);
					insertColleagueDataIntoDB();
				}*/				
				if(stocksCollArrList != null){
        			if(stocksCollListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
        				if((stocksCollListCount == 0) && (stocksCollArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_CONTENT_URI);
            			}
        				else if((stocksCollListCount > 0) && (stocksCollArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_CONTENT_URI);
        					insertColleagueDataIntoDB();
            			}
        			}
        			if(stocksCollListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){
        				if((stocksCollListCount == 0) && (stocksCollArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_CONTENT_URI);
            			}
        				else if((stocksCollListCount > 0) && (stocksCollArrList.size() == 0)){
            			}
        				else if((stocksCollListCount > 0) && (stocksCollArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_CONTENT_URI);
        					insertColleagueDataIntoDB();
            			}
        			}
        		}
				
				/*if((stocksCollSrlArrList != null) && (stocksCollSrlArrList.size() > 0)){
					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_SRL_CONTENT_URI);
					insertColleagueSerialDataIntoDB();
				}*/
				if(stocksCollSrlArrList != null){
        			if(stocksCollSrlListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_FULLSETS)){
        				if((stocksCollSrlListCount == 0) && (stocksCollSrlArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_SRL_CONTENT_URI);
            			}
        				else if((stocksCollSrlListCount > 0) && (stocksCollSrlArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_SRL_CONTENT_URI);
        					insertColleagueSerialDataIntoDB();
            			}
        			}
        			if(stocksCollSrlListType.equalsIgnoreCase(SapGenConstants.SAP_RESPONSE_DELTASETS)){
        				if((stocksCollSrlListCount == 0) && (stocksCollSrlArrList.size() == 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_SRL_CONTENT_URI);
            			}
        				else if((stocksCollSrlListCount > 0) && (stocksCollSrlArrList.size() == 0)){
            			}
        				else if((stocksCollSrlListCount > 0) && (stocksCollSrlArrList.size() > 0)){
        					VanStkDBOperations.deleteAllTableDataFromDB(this, VanStkOfflineContraintsCP.VAN_COL_SRL_CONTENT_URI);
        					insertColleagueSerialDataIntoDB();
            			}
        			}
        		}
				
				if(stocksArrList.size() > 0){
					SharedPreferences sharedPreferences = getSharedPreferences(ServiceProConstants.PREFS_NAME_FOR_VAN_STOCK, 0);    
	    			SharedPreferences.Editor editor = sharedPreferences.edit();    
	    			editor.putBoolean(ServiceProConstants.PREFS_KEY_VAN_STOCK_FOR_MYSELF_GET, true);    
	    			editor.commit();
				}
				
			} catch (Exception esf) {
				ServiceProConstants.showErrorLog("On updateCategoryServerResponse finally block: "+esf.toString());
			}
        	stopProgressDialog();
        	VanStockMainListScreen.this.runOnUiThread(new Runnable() {
	            public void run() {
	            	initDBConnection();
	            }
	        }); 
        	/*Collections.sort(stocksArrList, stockSortComparator);
        	stopProgressDialog();
        	VanStockMainListScreen.this.runOnUiThread(new Runnable() {
	            public void run() {
	            	initLayout();
	            }
	        });   */ 		
        }
    }//fn updateReportsConfirmResponse     
	
	private void insertEmployeeDataIntoDB(){
		VanStkOpConstraints stkCategory;
    	try {
			if(stocksArrList != null){
				for(int k=0; k<stocksArrList.size(); k++){
					stkCategory = (VanStkOpConstraints) stocksArrList.get(k);
					if(stkCategory != null){
						VanStkDBOperations.insertVanSTkEmpDataInToDB(this, stkCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertEmployeeDataIntoDB: "+ewe.toString());
		}
    }//fn insertEmployeeDataIntoDB
	
	private void insertEmployeeSerialDataIntoDB(){
		VanStkSerOpConstraints serCategory;
    	try {
			if(stocksSerArrList != null){
				for(int k=0; k<stocksSerArrList.size(); k++){
					serCategory = (VanStkSerOpConstraints) stocksSerArrList.get(k);
					if(serCategory != null){
						VanStkDBOperations.insertVanSTkEmpSrlDataInToDB(this, serCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertEmployeeSerialDataIntoDB: "+ewe.toString());
		}
    }//fn insertEmployeeSerialDataIntoDB
	
	private void insertEmployeeSTODataIntoDB(){
		VanStkTransOpConstraints stoCategory;
    	try {
			if(stocksTransArrList != null){
				for(int k=0; k<stocksTransArrList.size(); k++){
					stoCategory = (VanStkTransOpConstraints) stocksTransArrList.get(k);
					if(stoCategory != null){
						VanStkDBOperations.insertVanSTkEmpSTODataInToDB(this, stoCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertEmployeeSTODataIntoDB: "+ewe.toString());
		}
    }//fn insertEmployeeSTODataIntoDB
	
	private void insertColleagueDataIntoDB(){
		VanStkColOpConstraints colCategory;
    	try {
			if(stocksCollArrList != null){
				for(int k=0; k<stocksCollArrList.size(); k++){
					colCategory = (VanStkColOpConstraints) stocksCollArrList.get(k);
					if(colCategory != null){
						VanStkDBOperations.insertVanSTkColDataInToDB(this, colCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertColleagueDataIntoDB: "+ewe.toString());
		}
    }//fn insertColleagueDataIntoDB
	
	private void insertColleagueSerialDataIntoDB(){
		VanStkColStrOpConstraints colSerCategory;
    	try {
			if(stocksCollSrlArrList != null){
				for(int k=0; k<stocksCollSrlArrList.size(); k++){
					colSerCategory = (VanStkColStrOpConstraints) stocksCollSrlArrList.get(k);
					if(colSerCategory != null){
						VanStkDBOperations.insertVanSTkColSrlDataInToDB(this, colSerCategory);
					}
				}
			}
		} catch (Exception ewe) {
			ServiceProConstants.showErrorLog("Error On insertColleagueSerialDataIntoDB: "+ewe.toString());
		}
    }//fn insertColleagueSerialDataIntoDB
	
	
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
			Intent intent = new Intent();
			intent.putExtra("ShowListView", false);
			setResult(RESULT_OK, intent); 
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
    		boolean refreshFlag = false;
    		if(requestCode == ServiceProConstants.VANSTOCK_TRANSCRT_SCREEN){
    			if(resultCode == RESULT_OK)
    				refreshFlag = true;
    		}
    		else if(requestCode == ServiceProConstants.VANSTOCK_OVERVIEW_SCREEN){
    			refreshFlag = true;
    		}
    		
    		if(refreshFlag == true){
    			isConnAvail = ServiceProConstants.checkInternetConn(this);
            	if(isConnAvail ==  true)
            		initSoapConnection();
            	else
            		initDBConnection(); 
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
    
}//End of class VanStockMainListScreen

