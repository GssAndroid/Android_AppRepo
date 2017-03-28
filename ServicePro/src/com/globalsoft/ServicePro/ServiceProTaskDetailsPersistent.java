package com.globalsoft.ServicePro;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;

public class ServiceProTaskDetailsPersistent {
	// Task Related Database
    public static final String KEY_ROWID = "ID";  
    public static final String KEY_OBJECT_ID = "OBJECT_ID";
    public static final String KEY_PROCESS_TYPE = "PROCESS_TYPE";
    public static final String KEY_ZZKEYDATE = "ZZKEYDATE";
    public static final String KEY_PARTNER = "PARTNER";
    public static final String KEY_NAME_ORG1 = "NAME_ORG1";
    public static final String KEY_NAME_ORG2 = "NAME_ORG2";
    public static final String KEY_NICKNAME = "NICKNAME";
    public static final String KEY_CITY = "CITY";
    public static final String KEY_POSTL_COD1 = "POSTL_COD1";
    public static final String KEY_STREET = "STREET";
    public static final String KEY_COUNTRYISO = "COUNTRYISO";
    public static final String KEY_REGION = "REGION";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_TXT30= "TXT30";
    public static final String KEY_STATUS_REASON = "STATUS_REASON";
    public static final String KEY_CP1_PARTNER = "CP1_PARTNER";
    public static final String KEY_CP1_NAME1_TEXT = "CP1_NAME1_TEXT";
    public static final String KEY_CP1_TEL_NO = "CP1_TEL_NO";
    public static final String KEY_CP1_TEL_NO2 = "CP1_TEL_NO2";
    public static final String KEY_CP2_PARTNER = "CP2_PARTNER";
    public static final String KEY_CP2_NAME1_TEXT = "CP2_NAME1_TEXT";
    public static final String KEY_CP2_TEL_NO = "CP2_TEL_NO";
    public static final String KEY_CP2_TEL_NO2 = "CP2_TEL_NO2";    
    public static final String KEY_DESCRIPTION = "DESCRIPTION";
    public static final String KEY_PRIORITY = "PRIORITY";
    public static final String KEY_IB_IBASE = "IB_IBASE";
    public static final String KEY_IB_INSTANCE = "IB_INSTANCE";
    public static final String KEY_SERIAL_NUMBER = "SERIAL_NUMBER";
    public static final String KEY_REFOBJ_PRODUCT_ID = "REFOBJ_PRODUCT_ID";
    public static final String KEY_IB_DESCR = "IB_DESCR";
    public static final String KEY_IB_INST_DESCR = "IB_INST_DESCR";
    public static final String KEY_REFOBJ_PRODUCT_DESCR = "REFOBJ_PRODUCT_DESCR";
    public static final String KEY_TIMEZONE_FROM = "TIMEZONE_FROM";
    public static final String KEY_ZZETADATE = "ZZETADATE";
    public static final String KEY_ZZETATIME = "ZZETATIME";
    public static final String KEY_PROCESS_TYPE_DESCR = "PROCESS_TYPE_DESCR";
    public static final String KEY_NOTE = "NOTE";
    public static final String KEY_FIELDNOTE = "ZZFIELDNOTE"; 
    public static final String KEY_ZZFIRSTSERVICEPRODUCT = "ZZFIRSTSERVICEPRODUCT";
    public static final String KEY_ZZFIRSTSERVICEPRODUCTDESCR = "ZZFIRSTSERVICEPRODUCTDESCR";  
    public static final String KEY_ORDER = "TASK_ORDER";
    public static final String KEY_NUMBER_EXT = "ZZFIRSTSERVICEITEM";
    
        
    private static final String DATABASE_TABLE = "tasklist";
    private Cursor c;

    private  final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase sqlitedatabase;
    
    public ServiceProTaskDetailsPersistent(Context ctext){
    	context = ctext;    
    }
    
    public void insertTaskDetails(ServiceProOutputConstraints category, int pos){
    	try {
			DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();
		    insertRow(category, pos);		    		    
			sqlitedatabase.close();			
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in insertTaskDetails:"+e.toString());
		}
    }//fn insertTaskDetails
    
    public void closeDBHelper(){
    	try{
	    	if(DBHelper != null)
	    		DBHelper.close();
	    	if (c != null) {
	            c.close();
	            c = null;
	    	}
	    } catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in ServiceProTaskDetailsPersistent closeDBHelper:"+e.toString());
		}
    }//fn closeDBHelper       
    public  ArrayList getAllItemData(String collId){
    	
    	ServiceProOutputConstraints serCategory = null;
    	ArrayList taskArrList = new ArrayList();
    	//String[] dbValues = new String[50]; 
    	//int[] dbIndex = new int[50];
    	int count=0;
    	String itemNo = "";
    	try {
    		DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();	
			c = sqlitedatabase.rawQuery("select * from "+DATABASE_TABLE+" where "+KEY_OBJECT_ID+"='"+collId.trim()+"'", null);
			count = c.getCount();
			ServiceProConstants.showLog("info count :"+count);
			
			if (c.moveToFirst())
			{
			     do {
			    	//int  dbIndex= c.getColumnIndex(KEY_NUMBER_EXT);
			    	 int  dbIndex= c.getColumnIndex(KEY_ORDER);			    	 
			    	itemNo = c.getString(dbIndex);				    	 
			    	taskArrList.add(itemNo);
			     } while (c.moveToNext());
			}
			ServiceProConstants.showLog("info count Array:"+taskArrList.size());
			sqlitedatabase.close();
			c.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in getAllItemData:"+e.toString());
			sqlitedatabase.close();
		}
		  
    	return taskArrList;
    }//fn getAllItemData    
    
    public  ArrayList getAllSelectdItemData(String collId){
    	
    	ServiceProOutputConstraints serCategory = null;
    	ArrayList taskArrList1 = new ArrayList();
    	String[] dbValues = new String[50]; 
    	int[] dbIndex = new int[50];
    	int count=0;
    	
    	try {
    		DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();	
			//c = sqlitedatabase.rawQuery("select * from "+DATABASE_TABLE+" where "+KEY_OBJECT_ID+"='"+collId.trim()+"' AND  "+KEY_NUMBER_EXT + "='"+itemId.trim()+"'",null);
			c = sqlitedatabase.rawQuery("select * from "+DATABASE_TABLE+" where "+KEY_ORDER+"='"+collId.trim()+"'",null);
			count = c.getCount();
			ServiceProConstants.showLog("info count :"+count);
			
			if(c.getCount() == 0){
				return taskArrList1;
			}
			
			
			    	 	dbIndex[0] = c.getColumnIndex(KEY_OBJECT_ID);
						dbIndex[1] = c.getColumnIndex(KEY_PROCESS_TYPE);					
						dbIndex[2] = c.getColumnIndex(KEY_ZZKEYDATE);
						dbIndex[3] = c.getColumnIndex(KEY_PARTNER);
						dbIndex[4] = c.getColumnIndex(KEY_NAME_ORG1);
						dbIndex[5] = c.getColumnIndex(KEY_NAME_ORG2);
						dbIndex[6] = c.getColumnIndex(KEY_NICKNAME);
						dbIndex[7] = c.getColumnIndex(KEY_CITY);
						dbIndex[8] = c.getColumnIndex(KEY_POSTL_COD1);
						dbIndex[9] = c.getColumnIndex(KEY_STREET);
						dbIndex[10] = c.getColumnIndex(KEY_COUNTRYISO);
						dbIndex[11] = c.getColumnIndex(KEY_REGION);
						dbIndex[12] = c.getColumnIndex(KEY_STATUS);
						dbIndex[13] = c.getColumnIndex(KEY_TXT30);
						dbIndex[14] = c.getColumnIndex(KEY_STATUS_REASON);
						dbIndex[15] = c.getColumnIndex(KEY_CP1_PARTNER);
						dbIndex[16] = c.getColumnIndex(KEY_CP1_NAME1_TEXT);
						dbIndex[17] = c.getColumnIndex(KEY_CP1_TEL_NO);
						dbIndex[18] = c.getColumnIndex(KEY_CP2_TEL_NO2);
						dbIndex[19] = c.getColumnIndex(KEY_CP2_PARTNER);
						dbIndex[20] = c.getColumnIndex(KEY_CP2_NAME1_TEXT);
						dbIndex[21] = c.getColumnIndex(KEY_CP2_TEL_NO);
						dbIndex[22] = c.getColumnIndex(KEY_CP2_TEL_NO2);
						dbIndex[23] = c.getColumnIndex(KEY_DESCRIPTION);
						dbIndex[24] = c.getColumnIndex(KEY_PRIORITY);
						dbIndex[25] = c.getColumnIndex(KEY_IB_IBASE);
						dbIndex[26] = c.getColumnIndex(KEY_IB_INSTANCE);
						dbIndex[27] = c.getColumnIndex(KEY_SERIAL_NUMBER);
						dbIndex[28] = c.getColumnIndex(KEY_REFOBJ_PRODUCT_ID);
						dbIndex[29] = c.getColumnIndex(KEY_IB_DESCR);
						dbIndex[30] = c.getColumnIndex(KEY_IB_INST_DESCR);
						dbIndex[31] = c.getColumnIndex(KEY_REFOBJ_PRODUCT_DESCR);
						dbIndex[32] = c.getColumnIndex(KEY_TIMEZONE_FROM);
						dbIndex[33] = c.getColumnIndex(KEY_ZZETADATE);
						dbIndex[34] = c.getColumnIndex(KEY_ZZETATIME);
						dbIndex[35] = c.getColumnIndex(KEY_PROCESS_TYPE_DESCR);
						dbIndex[36] = c.getColumnIndex(KEY_NOTE);
						dbIndex[37] = c.getColumnIndex(KEY_FIELDNOTE);
						dbIndex[38] = c.getColumnIndex(KEY_ZZFIRSTSERVICEPRODUCT);
						dbIndex[39] = c.getColumnIndex(KEY_ZZFIRSTSERVICEPRODUCTDESCR);	
						dbIndex[40] = c.getColumnIndex(KEY_NUMBER_EXT);
						//dbIndex[41] = c.getColumnIndex(KEY_ORDER);
						
						c.moveToFirst();
			    
			do{
			    dbValues[0] = c.getString(dbIndex[0]);
				dbValues[1] = c.getString(dbIndex[1]);
				dbValues[2] = c.getString(dbIndex[2]);
				dbValues[3] = c.getString(dbIndex[3]);
				dbValues[4] = c.getString(dbIndex[4]);
				dbValues[5] = c.getString(dbIndex[5]);
				dbValues[6] = c.getString(dbIndex[6]);
				dbValues[7] = c.getString(dbIndex[7]);
				dbValues[8] = c.getString(dbIndex[8]);
				dbValues[9] = c.getString(dbIndex[9]);
				dbValues[10] = c.getString(dbIndex[10]);
				dbValues[11] = c.getString(dbIndex[11]);
				dbValues[12] = c.getString(dbIndex[12]);
				dbValues[13] = c.getString(dbIndex[13]);
				dbValues[14] = c.getString(dbIndex[14]);
				dbValues[15] = c.getString(dbIndex[15]);
				dbValues[16] = c.getString(dbIndex[16]);
				dbValues[17] = c.getString(dbIndex[17]);
				dbValues[18] = c.getString(dbIndex[18]);
				dbValues[19] = c.getString(dbIndex[19]);
				dbValues[20] = c.getString(dbIndex[20]);
				dbValues[21] = c.getString(dbIndex[21]);
				dbValues[22] = c.getString(dbIndex[22]);
				dbValues[23] = c.getString(dbIndex[23]);
				dbValues[24] = c.getString(dbIndex[24]);
				dbValues[25] = c.getString(dbIndex[25]);
				dbValues[26] = c.getString(dbIndex[26]);
				dbValues[27] = c.getString(dbIndex[27]);
				dbValues[28] = c.getString(dbIndex[28]);
				dbValues[29] = c.getString(dbIndex[29]);
				dbValues[30] = c.getString(dbIndex[30]);
				dbValues[31] = c.getString(dbIndex[31]);
				dbValues[32] = c.getString(dbIndex[32]);
				dbValues[33] = c.getString(dbIndex[33]);
				dbValues[34] = c.getString(dbIndex[34]);
				dbValues[35] = c.getString(dbIndex[35]);
				dbValues[36] = c.getString(dbIndex[36]);
				dbValues[37] = c.getString(dbIndex[37]);
				dbValues[38] = c.getString(dbIndex[38]);
				dbValues[39] = c.getString(dbIndex[39]);
				dbValues[40] = c.getString(dbIndex[40]);
				//dbValues[41] = c.getString(dbIndex[41]);
				
		    	serCategory = new ServiceProOutputConstraints(dbValues);
				if(taskArrList1 != null)
					taskArrList1.add(serCategory);	
				c.moveToNext();
			}while(!c.isAfterLast());
			ServiceProConstants.showLog("info count Array:"+taskArrList1.size());
			sqlitedatabase.close();
			c.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in getAllSelectdItemData:"+e.toString());
			sqlitedatabase.close();
		}
		  
    	return taskArrList1;
    }//fn getAllItemData

    public boolean checkTaskExists(String id){
    	boolean idexists = false;
    	try {
			DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();		
			//int idValue = Integer.parseInt(id);
			c = sqlitedatabase.rawQuery("select * from "+DATABASE_TABLE+" where "+KEY_OBJECT_ID+"='"+id+"'", null);
			if (c.moveToFirst())
			{
			     do {
			    	 int index = c.getColumnIndex(KEY_OBJECT_ID);
		     		 String tranc_no = c.getString(index);		     		 
		     		 if(id.equalsIgnoreCase(tranc_no)){
		     			 idexists = true;
		     			 //ServiceProConstants.showLog("Id value:"+c.getString(index));
     				 	 sqlitedatabase.close();
     				 	 return idexists;
		     		 }
			     } while (c.moveToNext());
			}
			sqlitedatabase.close();
			c.close();
		} catch (Exception e) {
			ServiceProConstants.showErrorLog("Error in checkTaskExists:"+e.toString());
		}   
    	return idexists;
    }//fn checkTaskExists
       
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, ServiceProDBAdapter.DATABASE_NAME, null, ServiceProDBAdapter.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        }
    }//End of class DatabaseHelper 
    
    public long insertRow(ServiceProOutputConstraints category, int pos)
    {		
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_OBJECT_ID, category.getObjectId().toString().trim());
        initialValues.put(KEY_PROCESS_TYPE, category.getProcessType().toString().trim());
        initialValues.put(KEY_ZZKEYDATE, category.getZZKEeyDate().toString().trim());
        initialValues.put(KEY_PARTNER, category.getPartner().toString().trim());
        initialValues.put(KEY_NAME_ORG1, category.getNameOrg1().toString().trim());
        initialValues.put(KEY_NAME_ORG2, category.getNameOrg2().toString().trim());
        initialValues.put(KEY_NICKNAME, category.getNickName().toString().trim());
        initialValues.put(KEY_CITY, category.getCity().toString().trim());
        initialValues.put(KEY_POSTL_COD1, category.getPostalCode1().toString().trim());
        initialValues.put(KEY_STREET, category.getStreet().toString().trim());
        initialValues.put(KEY_COUNTRYISO, category.getCountryIso().toString().trim());
        initialValues.put(KEY_REGION, category.getRegion().toString().trim());
        initialValues.put(KEY_STATUS, category.getStatus().toString().trim());
        initialValues.put(KEY_TXT30, category.getTxt30().toString().trim());
        initialValues.put(KEY_STATUS_REASON, category.getStatusReason().toString().trim());
        initialValues.put(KEY_CP1_PARTNER, category.getCp1Partner().toString().trim());
        initialValues.put(KEY_CP1_NAME1_TEXT, category.getCp1Name().toString().trim());
        initialValues.put(KEY_CP1_TEL_NO, category.getCp1TelNo().toString().trim());
        initialValues.put(KEY_CP1_TEL_NO2, category.getCp1TelNo2().toString().trim());
        initialValues.put(KEY_CP2_PARTNER, category.getCp2Partner().toString().trim());
        initialValues.put(KEY_CP2_NAME1_TEXT, category.getCp2Name().toString().trim());
        initialValues.put(KEY_CP2_TEL_NO, category.getCp2TelNo().toString().trim());        
        initialValues.put(KEY_CP2_TEL_NO2, category.getCp2TelNo2().toString().trim());        
        initialValues.put(KEY_DESCRIPTION, category.getDescription().toString().trim());
        initialValues.put(KEY_PRIORITY, category.getPriority().toString().trim());
        initialValues.put(KEY_IB_IBASE, category.getIB_Base().toString().trim());
        initialValues.put(KEY_IB_INSTANCE, category.getIB_Instance().toString().trim());
        initialValues.put(KEY_SERIAL_NUMBER, category.getSerialNumber().toString().trim());
        initialValues.put(KEY_REFOBJ_PRODUCT_ID, category.getRefObjProductId().toString().trim());
        initialValues.put(KEY_IB_DESCR, category.getIB_Descr().toString().trim());
        initialValues.put(KEY_IB_INST_DESCR, category.getIB_Inst_Descr().toString().trim());
        initialValues.put(KEY_REFOBJ_PRODUCT_DESCR, category.getRefObj_Product_Descr().toString().trim());
        initialValues.put(KEY_TIMEZONE_FROM, category.getTimeZone().toString().trim());
        initialValues.put(KEY_ZZETADATE, category.getZzetaDate().toString().trim());
        initialValues.put(KEY_ZZETATIME, category.getZzetaTime().toString().trim());
        initialValues.put(KEY_PROCESS_TYPE_DESCR, category.getProcessTypeDescr().toString().trim());
        initialValues.put(KEY_NOTE, category.getNote().toString().trim());
        initialValues.put(KEY_FIELDNOTE, "");
        initialValues.put(KEY_ZZFIRSTSERVICEPRODUCT, category.getZzFirstServiceProduct().toString().trim());
        initialValues.put(KEY_ZZFIRSTSERVICEPRODUCTDESCR, category.getZzFirstServiceProductDescr().toString().trim());
        initialValues.put(KEY_ORDER, pos);
        initialValues.put(KEY_NUMBER_EXT, category.getNumberExtn().toString().trim());
        
        return sqlitedatabase.insert(DATABASE_TABLE, null, initialValues);
    }//fn insertRow
    
    public Cursor getAllRows()
    {        
        return sqlitedatabase.query(DATABASE_TABLE, new String[] {
                KEY_ROWID, KEY_OBJECT_ID, KEY_PROCESS_TYPE,KEY_ZZKEYDATE, KEY_PARTNER, KEY_NAME_ORG1, 
                KEY_NAME_ORG2, KEY_NICKNAME, KEY_CITY, KEY_POSTL_COD1, KEY_STREET, 
                KEY_COUNTRYISO, KEY_REGION, KEY_STATUS, KEY_TXT30, KEY_STATUS_REASON, KEY_CP1_PARTNER, 
                KEY_CP1_NAME1_TEXT, KEY_CP1_TEL_NO, KEY_CP1_TEL_NO2, KEY_CP2_PARTNER, KEY_CP2_NAME1_TEXT, 
                KEY_CP2_TEL_NO, KEY_CP2_TEL_NO2, KEY_DESCRIPTION, KEY_PRIORITY, KEY_IB_IBASE, KEY_IB_INSTANCE,
                KEY_SERIAL_NUMBER, KEY_REFOBJ_PRODUCT_ID, KEY_IB_DESCR, KEY_IB_INST_DESCR, KEY_REFOBJ_PRODUCT_DESCR, 
                KEY_TIMEZONE_FROM, KEY_ZZETADATE, KEY_ZZETATIME, KEY_PROCESS_TYPE_DESCR, KEY_NOTE, KEY_FIELDNOTE, KEY_ZZFIRSTSERVICEPRODUCT,
                KEY_ZZFIRSTSERVICEPRODUCTDESCR,KEY_ORDER, KEY_NUMBER_EXT},
        null, null, null, null, null);
    }//fn getAllRows
    
    public void updateTaskObject(ServiceProOutputConstraints category)
    {   	
    	try {
			DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();	
			sqlitedatabase.execSQL("update "+DATABASE_TABLE+" set "
										+KEY_OBJECT_ID+"='"+category.getObjectId().toString().trim()+"' ," 
										+KEY_PROCESS_TYPE+"='"+category.getProcessType().toString().trim()+"' ," 
										+KEY_ZZKEYDATE+"='"+category.getZZKEeyDate().toString().trim()+"' ," 
										+KEY_PARTNER+"='"+category.getPartner().toString().trim()+"' ," 
										+KEY_NAME_ORG1+"='"+category.getNameOrg1().toString().trim()+"' ," 
										+KEY_NAME_ORG2+"='"+category.getNameOrg2().toString().trim()+"' ,"
										+KEY_NICKNAME+"='"+category.getNickName().toString().trim()+"' ,"
										+KEY_CITY+"='"+category.getCity().toString().trim()+"' ,"
										+KEY_POSTL_COD1+"='"+category.getPostalCode1().toString().trim()+"' ,"
										+KEY_STREET+"='"+category.getStreet().toString().trim()+"' ,"
										+KEY_COUNTRYISO+"='"+category.getCountryIso().toString().trim()+"' ,"
										+KEY_REGION+"='"+category.getRegion().toString().trim()+"' ,"
										+KEY_STATUS+"='"+category.getStatus().toString().trim()+"' ,"
										+KEY_TXT30+"='"+category.getTxt30().toString().trim()+"' ,"
										+KEY_STATUS_REASON+"='"+category.getStatusReason().toString().trim()+"' ,"
										+KEY_CP1_PARTNER+"='"+category.getCp1Partner().toString().trim()+"' ,"
										+KEY_CP1_NAME1_TEXT+"='"+category.getCp1Name().toString().trim()+"' ,"
										+KEY_CP1_TEL_NO+"='"+category.getCp1TelNo().toString().trim()+"' ,"
										+KEY_CP1_TEL_NO2+"='"+category.getCp1TelNo2().toString().trim()+"' ,"
										+KEY_CP2_PARTNER+"='"+category.getCp2Partner().toString().trim()+"' ,"
										+KEY_CP2_NAME1_TEXT+"='"+category.getCp2Name().toString().trim()+"' ,"
										+KEY_CP2_TEL_NO+"='"+category.getCp2TelNo().toString().trim()+"' ,"
										+KEY_CP2_TEL_NO2+"='"+category.getCp2TelNo2().toString().trim()+"' ,"
										+KEY_DESCRIPTION+"='"+category.getDescription().toString().trim()+"' ,"
										+KEY_PRIORITY+"='"+category.getPriority().toString().trim()+"' ,"
										+KEY_IB_IBASE+"='"+category.getIB_Base().toString().trim()+"' ,"
										+KEY_IB_INSTANCE+"='"+category.getIB_Instance().toString().trim()+"' ,"
										+KEY_SERIAL_NUMBER+"='"+category.getSerialNumber().toString().trim()+"' ,"
										+KEY_REFOBJ_PRODUCT_ID+"='"+category.getRefObjProductId().toString().trim()+"' ,"
										+KEY_IB_DESCR+"='"+category.getIB_Descr().toString().trim()+"' ," 
										+KEY_IB_INST_DESCR+"='"+category.getIB_Inst_Descr().toString().trim()+"' ," 
										+KEY_REFOBJ_PRODUCT_DESCR+"='"+category.getRefObj_Product_Descr().toString().trim()+"' ," 
										+KEY_TIMEZONE_FROM+"='"+category.getTimeZone().toString().trim()+"' ," 
										+KEY_ZZETADATE+"='"+category.getZzetaDate().toString().trim()+"' ,"
										+KEY_ZZETATIME+"='"+category.getZzetaTime().toString().trim()+"' ,"
										+KEY_PROCESS_TYPE_DESCR+"='"+category.getProcessTypeDescr().toString().trim()+"' ,"
										+KEY_NOTE+"='"+category.getNote().toString().trim()+"' ,"
										+KEY_FIELDNOTE+"=' ' ,"
										+KEY_ZZFIRSTSERVICEPRODUCT+"='"+category.getZzFirstServiceProduct().toString().trim()+"' ,"
										+KEY_ZZFIRSTSERVICEPRODUCTDESCR+"='"+category.getZzFirstServiceProductDescr().toString().trim()+"' ,"	
										+KEY_NUMBER_EXT+"='"+category.getNumberExtn().toString().trim()+"' "
																		
										/*+KEY_STATUS_REASON+"='"+ServiceProConstants.STATUS_REASON_VALUE.toString().trim()+"' "	*/									
												+"where "+KEY_OBJECT_ID+"='"+category.getObjectId().toString().trim()+"'");
			sqlitedatabase.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in updateTaskObject:"+e.toString());
			sqlitedatabase.close();
		}   
    }//fn updateTaskObject      
    
    public void updateOrderValue(int val, String id){   	
    	try {
			DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();				
	        sqlitedatabase.execSQL("update "+DATABASE_TABLE+" set " +KEY_ORDER+"="+val+" where "+KEY_OBJECT_ID+"='"+id+"'");	            	
    	} catch (Exception e) {
			System.out.println("Error in updateOrderValue:"+e.toString());
			sqlitedatabase.close();
		}   
		sqlitedatabase.close();
    }//fn update_data
    
    public void clearTaskTable()
    {   	
    	try {
			DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();	
			c = sqlitedatabase.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='"+DATABASE_TABLE+"'", null);    
			if (c.getCount()>0){         
				sqlitedatabase.execSQL("DELETE FROM "+DATABASE_TABLE);    
			} 			
			//ServiceProConstants.showLog("DELETE FROM "+DATABASE_TABLE+"  "+c.getCount());
			c.close();
			sqlitedatabase.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in clearTaskTable:"+e.toString());
			sqlitedatabase.close();
		}   
    }//fn clearTaskTable 
    
    public void deleteQueryExceptErrorId()
    {   	
    	try {
			DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();	
			sqlitedatabase.execSQL("DELETE FROM "+ServiceProDBAdapter.TASKLIST_TABLE+" WHERE "+ServiceProTaskDetailsPersistent.KEY_OBJECT_ID+" NOT IN ( SELECT "+ServiceProErrorMessagePersistent.KEY_TRANCID+" FROM ( SELECT "+ServiceProErrorMessagePersistent.KEY_TRANCID+" FROM "+ServiceProDBAdapter.ERRLIST_TABLE+" ORDER BY "+ServiceProErrorMessagePersistent.KEY_TRANCID+" DESC LIMIT 50 ) foo );");
			//ServiceProConstants.showLog("DELETE FROM "+DATABASE_TABLE);
			sqlitedatabase.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in deleteQueryExceptErrorId:"+e.toString());
			sqlitedatabase.close();
		}   
    }//fn deleteQueryExceptErrorId
    
    public void deleteRow(String tracId)
    {   	
    	try {
			DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();	
			sqlitedatabase.execSQL("DELETE FROM "+ServiceProDBAdapter.TASKLIST_TABLE+" WHERE "+ServiceProTaskDetailsPersistent.KEY_OBJECT_ID+"='"+tracId+"'");
			sqlitedatabase.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in deleteRow:"+e.toString());
			sqlitedatabase.close();
		}   
    }//fn deleteRow
    
    public String getStatusReason(String ObjectId)
    {   	
    	String statusReason = "";    	
    	try {
    		DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();			
			c = sqlitedatabase.rawQuery("select * from "+DATABASE_TABLE+" where "+KEY_OBJECT_ID+"='"+ObjectId.toString().trim()+"'", null);
			if (c.moveToFirst())
			{
			     do {
			    	 int index0 = c.getColumnIndex(KEY_STATUS_REASON);
			    	 statusReason = c.getString(index0);	
			     } while (c.moveToNext());
			}
			sqlitedatabase.close();
			c.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in getStatusReason:"+e.toString());
			sqlitedatabase.close();
		}
		return statusReason;   
    }//fn getStatusReason 
    
    public ArrayList getTaskDetails()
    {   	
    	ArrayList taskList = new ArrayList();
    	ServiceProOutputConstraints categoryData = null;
    	String[] resArray = new String[50];
    	try {
    		DBHelper = new DatabaseHelper(context);
			sqlitedatabase = DBHelper.getWritableDatabase();	
			ServiceProConstants.showLog("select * from "+DATABASE_TABLE+" ORDER BY "+KEY_ORDER);
			c = sqlitedatabase.rawQuery("select * from "+DATABASE_TABLE+" ORDER BY "+KEY_ORDER, null);
			if (c.moveToFirst())
			{
				do {
			    	 int index0 = c.getColumnIndex(KEY_OBJECT_ID);
		     		 String KEY_OBJECT_ID = c.getString(index0);		     		 
		     		 resArray[0] = KEY_OBJECT_ID;
		     		 
		     		 int index1 = c.getColumnIndex(KEY_PROCESS_TYPE);
		     		 String KEY_PROCESS_TYPE = c.getString(index1);		     		 
		     		 resArray[1] = KEY_PROCESS_TYPE;
		     		 
		     		 int index2 = c.getColumnIndex(KEY_ZZKEYDATE);
		     		 String KEY_ZZKEYDATE = c.getString(index2);		     		 
		     		 resArray[2] = KEY_ZZKEYDATE;
		     		 
		     		 int index3 = c.getColumnIndex(KEY_PARTNER);
		     		 String KEY_PARTNER = c.getString(index3);		     		 
		     		 resArray[3] = KEY_PARTNER;
		     		 
		     		 int index4 = c.getColumnIndex(KEY_NAME_ORG1);
		     		 String KEY_NAME_ORG1 = c.getString(index4);		     		 
		     		 resArray[4] = KEY_NAME_ORG1;
		     		 
		     		 int index5 = c.getColumnIndex(KEY_NAME_ORG2);
		     		 String KEY_NAME_ORG2 = c.getString(index5);		     		 
		     		 resArray[5] = KEY_NAME_ORG2; 
		     		 
		     		 int index6 = c.getColumnIndex(KEY_NICKNAME);
		     		 String KEY_NICKNAME = c.getString(index6);		     		 
		     		 resArray[6] = KEY_NICKNAME;
		     		 
		     		 int index7 = c.getColumnIndex(KEY_STREET);
		     		 String KEY_STREET = c.getString(index7);		     		 
		     		 resArray[7] = KEY_STREET;
		     		 
		     		 int index8 = c.getColumnIndex(KEY_CITY);
		     		 String KEY_CITY = c.getString(index8);		     		 
		     		 resArray[8] = KEY_CITY;
		     		 
		     		 int index9 = c.getColumnIndex(KEY_REGION);
		     		 String KEY_REGION = c.getString(index9);		     		 
		     		 resArray[9] = KEY_REGION;
		     		 
		     		 int index10 = c.getColumnIndex(KEY_POSTL_COD1);
		     		 String KEY_POSTL_COD1 = c.getString(index10);		     		 
		     		 resArray[10] = KEY_POSTL_COD1; 
		     		 
		     		 int index11 = c.getColumnIndex(KEY_COUNTRYISO);
		     		 String KEY_COUNTRYISO = c.getString(index11);		     		 
		     		 resArray[11] = KEY_COUNTRYISO;
		     		 
		     		 int index12 = c.getColumnIndex(KEY_STATUS);
		     		 String KEY_STATUS = c.getString(index12);		     		 
		     		 resArray[12] = KEY_STATUS;
		     		 
		     		 int index13 = c.getColumnIndex(KEY_TXT30);
		     		 String KEY_TXT30 = c.getString(index13);		     		 
		     		 resArray[13] = KEY_TXT30;
		     		 
		     		 int index14 = c.getColumnIndex(KEY_STATUS_REASON);
		     		 String KEY_STATUS_REASON = c.getString(index14);		     		 
		     		 resArray[14] = KEY_STATUS_REASON;
		     		 
		     		 int index15 = c.getColumnIndex(KEY_CP1_PARTNER);
		     		 String KEY_CP1_PARTNER = c.getString(index15);		     		 
		     		 resArray[15] = KEY_CP1_PARTNER;
		     		 
		     		 int index16 = c.getColumnIndex(KEY_CP1_NAME1_TEXT);
		     		 String KEY_CP1_NAME1_TEXT = c.getString(index16);		     		 
		     		 resArray[16] = KEY_CP1_NAME1_TEXT;
		     		 
		     		 int index17 = c.getColumnIndex(KEY_CP1_TEL_NO);
		     		 String KEY_CP1_TEL_NO = c.getString(index17);		     		 
		     		 resArray[17] = KEY_CP1_TEL_NO;
		     		 
		     		 int index18 = c.getColumnIndex(KEY_CP1_TEL_NO2);
		     		 String KEY_CP1_TEL_NO2 = c.getString(index18);		     		 
		     		 resArray[18] = KEY_CP1_TEL_NO2;
		     		 
		     		 int index19 = c.getColumnIndex(KEY_CP2_PARTNER);
		     		 String KEY_CP2_PARTNER = c.getString(index19);		     		 
		     		 resArray[19] = KEY_CP2_PARTNER;
		     		 
		     		 int index20 = c.getColumnIndex(KEY_CP2_NAME1_TEXT);
		     		 String KEY_CP2_NAME1_TEXT = c.getString(index20);		     		 
		     		 resArray[20] = KEY_CP2_NAME1_TEXT;
		     		 
		     		 int index21 = c.getColumnIndex(KEY_CP2_TEL_NO);
		     		 String KEY_CP2_TEL_NO = c.getString(index21);		     		 
		     		 resArray[21] = KEY_CP2_TEL_NO;
		     		 
		     		 int index22 = c.getColumnIndex(KEY_CP2_TEL_NO2);
		     		 String KEY_CP2_TEL_NO2 = c.getString(index22);		     		 
		     		 resArray[22] = KEY_CP2_TEL_NO2;
		     		 
		     		 int index23 = c.getColumnIndex(KEY_DESCRIPTION);
		     		 String KEY_DESCRIPTION = c.getString(index23);		     		 
		     		 resArray[23] = KEY_DESCRIPTION;
		     		 
		     		 int index24 = c.getColumnIndex(KEY_PRIORITY);
		     		 String KEY_PRIORITY = c.getString(index24);		     		 
		     		 resArray[24] = KEY_PRIORITY;
		     		 
		     		 int index25 = c.getColumnIndex(KEY_IB_IBASE);
		     		 String KEY_IB_IBASE = c.getString(index25);		     		 
		     		 resArray[25] = KEY_IB_IBASE;
		     		 
		     		 int index26 = c.getColumnIndex(KEY_IB_INSTANCE);
		     		 String KEY_IB_INSTANCE = c.getString(index26);		     		 
		     		 resArray[26] = KEY_IB_INSTANCE;
		     		 
		     		 int index27 = c.getColumnIndex(KEY_SERIAL_NUMBER);
		     		 String KEY_SERIAL_NUMBER = c.getString(index27);		     		 
		     		 resArray[27] = KEY_SERIAL_NUMBER;
		     		 
		     		 int index28 = c.getColumnIndex(KEY_REFOBJ_PRODUCT_ID);
		     		 String KEY_REFOBJ_PRODUCT_ID = c.getString(index28);		     		 
		     		 resArray[28] = KEY_REFOBJ_PRODUCT_ID;
		     		 
		     		 int index29 = c.getColumnIndex(KEY_IB_DESCR);
		     		 String KEY_IB_DESCR = c.getString(index29);		     		 
		     		 resArray[29] = KEY_IB_DESCR;		 
		     		 
		     		 int index30 = c.getColumnIndex(KEY_IB_INST_DESCR);
		     		 String KEY_IB_INST_DESCR = c.getString(index30);		     		 
		     		 resArray[30] = KEY_IB_INST_DESCR;	 
		     		 
		     		 int index31 = c.getColumnIndex(KEY_REFOBJ_PRODUCT_DESCR);
		     		 String KEY_REFOBJ_PRODUCT_DESCR = c.getString(index31);		     		 
		     		 resArray[31] = KEY_REFOBJ_PRODUCT_DESCR;
		     		 
		     		 int index32 = c.getColumnIndex(KEY_TIMEZONE_FROM);
		     		 String KEY_TIMEZONE_FROM = c.getString(index32);		     		 
		     		 resArray[32] = KEY_TIMEZONE_FROM;		 
		     		 
		     		 int index33 = c.getColumnIndex(KEY_ZZETADATE);
		     		 String KEY_ZZETADATE = c.getString(index33);		     		 
		     		 resArray[33] = KEY_ZZETADATE; 
		     		 
		     		 int index34 = c.getColumnIndex(KEY_ZZETATIME);
		     		 String KEY_ZZETATIME = c.getString(index34);		     		 
		     		 resArray[34] = KEY_ZZETATIME;
		     		 
		     		 int index35 = c.getColumnIndex(KEY_PROCESS_TYPE_DESCR);
		     		 String KEY_PROCESS_TYPE_DESCR = c.getString(index35);		     		 
		     		 resArray[35] = KEY_PROCESS_TYPE_DESCR;
		     		 
		     		 int index36 = c.getColumnIndex(KEY_NOTE);
		     		 String KEY_NOTE = c.getString(index36);		     		 
		     		 resArray[36] = KEY_NOTE;
		     		 
		     		 int index37 = c.getColumnIndex(KEY_FIELDNOTE);
		     		 String KEY_FIELDNOTE = c.getString(index37);		     		 
		     		 resArray[37] = KEY_FIELDNOTE;
		     		 
		     		 int index38 = c.getColumnIndex(KEY_ZZFIRSTSERVICEPRODUCT);
		     		 String KEY_ZZFIRSTSERVICEPRODUCT = c.getString(index38);		     		 
		     		 resArray[38] = KEY_ZZFIRSTSERVICEPRODUCT;
		     		 
		     		 int index39 = c.getColumnIndex(KEY_ZZFIRSTSERVICEPRODUCTDESCR);
		     		 String KEY_ZZFIRSTSERVICEPRODUCTDESCR = c.getString(index39);		     		 
		     		 resArray[39] = KEY_ZZFIRSTSERVICEPRODUCTDESCR;
		     		 
		     		 int index40 = c.getColumnIndex(KEY_NUMBER_EXT);
		     		 String KEY_ITEM_NUMBER = c.getString(index40);		     		 
		     		 resArray[40] = KEY_ITEM_NUMBER;	
		     		 
		     		 int index41 = c.getColumnIndex(KEY_ORDER);
		     		 String KEY_ORDER = c.getString(index41);		     		 
		     		 resArray[41] = KEY_ORDER;
		     		 
					categoryData = new ServiceProOutputConstraints(resArray);
					if(categoryData != null){
						taskList.add(categoryData);	
					} 
		     		 
				} while (c.moveToNext());
			}
			sqlitedatabase.close();
			c.close();
    	} catch (Exception e) {
    		ServiceProConstants.showErrorLog("Error in getTaskDetails:"+e.toString());
			sqlitedatabase.close();
		}
		return taskList;   
    }//fn getTaskDetails 
    
}//End of class ServiceProTaskDetailsPersistent