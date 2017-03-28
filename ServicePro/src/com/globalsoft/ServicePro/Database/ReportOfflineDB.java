package com.globalsoft.ServicePro.Database;

import com.globalsoft.ServicePro.ServiceProConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ReportOfflineDB extends SQLiteOpenHelper {
	
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "report_db";
    
    //Categories Table Definitions
    public static final String REPORT_TABLE_NAME = "comp_orders_table";
    public static final String COL_ID = BaseColumns._ID;
    public static final String COL_OBJECT_ID= "OBJECT_ID";
    public static final String COL_PROCESSTYPE = "PROCESS_TYPE";
    public static final String COL_PROCESS_TYPE_TXT= "PROCESS_TYPE_TXT";
    public static final String COL_SOLD_TO_PARTY_LIST = "SOLD_TO_PARTY_LIST";
    public static final String COL_SOLD_TO_PARTY = "SOLD_TO_PARTY";
    public static final String COL_CONTACT_PERSON_LIST = "CONTACT_PERSON_LIST";
    public static final String COL_NET_VALUE = "NET_VALUE";
    public static final String COL_CURRENCY = "CURRENCY";
    public static final String COL_PRIORITY_TXT = "PRIORITY_TXT";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_PO_DATE_SOLD = "PO_DATE_SOLD";
    public static final String COL_CREATED_BY= "CREATED_BY";
    public static final String COL_CONCATSTATUSER = "CONCATSTATUSER";
    public static final String COL_POSTING_DATE= "POSTING_DATE";
    public static final String COL_WRK_START_DATE= "WRK_START_DATE";
    public static final String COL_WRK_END_DATE= "WRK_END_DATE";
    public static final String COL_HRS_LABOR= "HRS_LABOR";
    public static final String COL_HRS_TRAVEL= "HRS_TRAVEL";
    public static final String COL_HRS_TOTAL= "HRS_TOTAL";
    public static final String COL_EQUIP_NO= "EQUIP_NO";
    public static final String COL_REQ_START_DT= "REQ_START_DT";
    
   
    
    

    private static final String CREATE_REPORT_TABLE_NAME = "CREATE TABLE IF NOT EXISTS "
            + REPORT_TABLE_NAME + " (" + COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ COL_OBJECT_ID + " text NOT NULL, " + COL_PROCESSTYPE + " text NOT NULL, "
    		+ COL_PROCESS_TYPE_TXT + " text NOT NULL, " + COL_SOLD_TO_PARTY_LIST + " text NOT NULL, " + COL_SOLD_TO_PARTY + " text NOT NULL, "
    		+ COL_CONTACT_PERSON_LIST + " text NOT NULL, " + COL_NET_VALUE + " text NOT NULL, "
    		+ COL_CURRENCY + " text NOT NULL, " + COL_PRIORITY_TXT + " text NOT NULL, "
    		+ COL_DESCRIPTION + " text NOT NULL, " + COL_PO_DATE_SOLD + " text NOT NULL, "
    		+ COL_CREATED_BY + " text NOT NULL, " + COL_CONCATSTATUSER + " text NOT NULL, "
    		+ COL_POSTING_DATE + " text NOT NULL, " + COL_WRK_START_DATE + " text NOT NULL, "
    		+ COL_WRK_END_DATE + " text NOT NULL, " + COL_HRS_LABOR + " text NOT NULL, "
    		+ COL_HRS_TRAVEL + " text NOT NULL, " + COL_HRS_TOTAL + " text NOT NULL, "
    		+ COL_EQUIP_NO + " text NOT NULL, " + COL_REQ_START_DT + " text NOT NULL"+ ");";
    
    private static final String DB_SCHEMA1 = CREATE_REPORT_TABLE_NAME; 

    public ReportOfflineDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_SCHEMA1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	 if(oldVersion < newVersion){
    		 db.execSQL("DROP TABLE IF EXISTS " + REPORT_TABLE_NAME);
             onCreate(db);
             ServiceProConstants.showLog("Upgrading database. Existing contents will be lost. ["
                     + oldVersion + "]->[" + newVersion + "]");
         }      
    }

    
}//End of class ReportOfflineDB
