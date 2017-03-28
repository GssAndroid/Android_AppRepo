package com.globalsoft.ServicePro.Database;

import com.globalsoft.ServicePro.ServiceProConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServiceProOfflineConstraintsDB extends SQLiteOpenHelper {
	
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "OFFLINEDB2";
    
    private static final String CREATE_TABLE_SERPRO_STATUS_LIST = "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_STATUSLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_STATUS_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_STATUS_COL_STATUS + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_STATUS_COL_TXT30 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_STATUS_COL_ZZSTATUS_ICON + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_STATUS_COL_ZZSTATUS_POSTSETACTION + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_STATUS_FOLLOW_LIST = "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_PROCESS_TYPE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STATUS + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STONR_NEXT + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STATUS_NEXT + " text NOT NULL"+ ");";

    private static final String CREATE_TABLE_SERPRO_ATTACHLIST = "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_ATTACH_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_TYPE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_ATTACH_COL_NUMBER_EXT + " text NOT NULL, " 
    	    + ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_ID + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_CNTNT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_COLTASK= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_COLTASK + " (" 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_OBJECT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_PROCESS_TYPE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_NUMBER_EXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_ZZKEYDATE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_PARTNER + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_NAME_ORG1 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_NAME_ORG2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_STRAS + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_ORT01 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_REGIO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_PSTLZ + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_LAND1 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS_TXT30 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS_REASON + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_PARTNER + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_NAME1_TEXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_TEL_NO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_TEL_NO2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_PARTNER + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_NAME1_TEXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_TEL_NO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_TEL_NO2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_DESCRIPTION + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_PRIORITY + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_IB_IBASE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_IB_INSTANCE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_SERIAL_NUMBER + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_REFOBJ_PRODUCT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_IB_DESCR + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_IB_INST_DESCR + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_REFOBJ_PRODUCT_DESCR + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_TIMEZONE_FROM + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_ZZETADATE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_ZZETATIME + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_PROCESS_TYPE_DESCR + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLTASK_COL_NOTE + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCT + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCTDESCR + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_COLL_ID + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_COL_FIELD_NOTES + " text " +");";
    
    private static final String CREATE_TABLE_SERPRO_CONFLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_TYPE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZKEYDATE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_PARTNER + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG1 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_NICKNAME + " text NOT NULL, "    		
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_STRAS + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_ORT01 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_REGIO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_PSTLZ + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_LAND1 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_TXT30 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_REASON + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_PARTNER + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_NAME1_TEXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_PARTNER + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_NAME1_TEXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_DESCRIPTION + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_PRIORITY + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_IBASE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INSTANCE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_SERIAL_NUMBER + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_DESCR + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INST_DESCR + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_DESCR + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_TIMEZONE_FROM + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_NUMBER_EXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_PRODUCT_ID + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_QUANTITY + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_QTY_UNIT + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_DESCRIPTION + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_TEXT + " text NOT NULL " +");";
            //+ ServiceProDBConstants.SERPRO_CONFLIST_COL_FIELD_NOTES + " text , "+ ServiceProDBConstants.SERPRO_CONFLIST_COL_ITEM_NUMBER + " text " +");";
    
    private static final String CREATE_TABLE_SERPRO_CONFSPARELIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_OBJECT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PROCESS_TYPE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ZZKEYDATE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PARTNER + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_NAME_ORG1 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_NAME_ORG2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_NICKNAME + " text NOT NULL, "    	
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STRAS + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ORT01 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_REGIO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PSTLZ + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_LAND1 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STATUS + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STATUS_TXT30 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STATUS_REASON + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_PARTNER + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_NAME1_TEXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_TEL_NO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_TEL_NO2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_PARTNER + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_NAME1_TEXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_TEL_NO + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_TEL_NO2 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_DESCRIPTION + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PRIORITY + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_IBASE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_INSTANCE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_SERIAL_NUMBER + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_REFOBJ_PRODUCT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_DESCR + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_INST_DESCR + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_REFOBJ_PRODUCT_DESCR + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_TIMEZONE_FROM + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_NUMBER_EXT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PRODUCT_ID + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_QUANTITY + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PROCESS_QTY_UNIT + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ZZITEM_DESCRIPTION + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ZZITEM_TEXT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_CONFCOLLECLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_OBJECT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_PROCESS_TYPE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_NUMBER_EXT + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ERDAT + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_STATUS + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_TXT30 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_OBJECT_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_PROCESS_TYPE + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_NUMBER_EXT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_CONFPRODUCTLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_PRODUCT_ID + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_SHORT_TEXT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_CONFCAUSEGROUPLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_CODEGRUPPE + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_KURZTEXT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_CONFCAUSECODELIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_CODEGRUPPE + " text NOT NULL, " 
    	   	+ ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_CODE + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_KURZTEXT + " text NOT NULL"+ ");";

    private static final String CREATE_TABLE_SERPRO_CONFSYMPGROUPLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_CODEGRUPPE + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_KURZTEXT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_CONFSYMPCODELIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_CODEGRUPPE + " text NOT NULL, " 
    	   	+ ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_CODE + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_KURZTEXT + " text NOT NULL"+ ");";

    private static final String CREATE_TABLE_SERPRO_CONFPROBGROUPLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_CODEGRUPPE + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_KURZTEXT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_CONFPROBCODELIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_CODEGRUPPE + " text NOT NULL, " 
    	   	+ ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_CODE + " text NOT NULL, " 
            + ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_KURZTEXT + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_CONFMATTEMPLLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_BP_UNAME + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MATNR + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MAKTX_INSYLANGU + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_VRKME + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MEINS + " text NOT NULL"+ ");";
    
    private static final String CREATE_TABLE_SERPRO_COLLEAGUELIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST + " (" 
    		+ ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_PARTNER + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_MC_NAME1 + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_MC_NAME2 + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_TEL_NO + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_TEL_NO2 + " text NOT NULL, " 
    	    + ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_UNAME + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_PLANT + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_STORAGE_LOC + " text NOT NULL"+ ");";
            
    private static final String CREATE_TABLE_SERPRO_ENTITLEMENTLIST= "CREATE TABLE IF NOT EXISTS "
            + ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST + " (" 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ORDER_ID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ITEM_ID + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ITEM_GUID + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROCESS_TYPE + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROFILE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROF_DESC + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ENT_TYPE + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ENT_DESC + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_LABOUR + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_PARTS + " text NOT NULL, "
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_TRAVEL + " text NOT NULL, " 
    		+ ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_FREQUENCY + " text NOT NULL, "
            + ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_TIMEPERIOD + " text NOT NULL"+ ");";    
    
    private static final String DB_SCHEMA1 = CREATE_TABLE_SERPRO_STATUS_LIST;
    private static final String DB_SCHEMA2 = CREATE_TABLE_SERPRO_COLTASK;
    private static final String DB_SCHEMA3 = CREATE_TABLE_SERPRO_CONFLIST;
    private static final String DB_SCHEMA4 = CREATE_TABLE_SERPRO_CONFCOLLECLIST;
    private static final String DB_SCHEMA5 = CREATE_TABLE_SERPRO_CONFSPARELIST;
    private static final String DB_SCHEMA6 = CREATE_TABLE_SERPRO_CONFPRODUCTLIST;
    private static final String DB_SCHEMA7 = CREATE_TABLE_SERPRO_CONFCAUSECODELIST;
    private static final String DB_SCHEMA8 = CREATE_TABLE_SERPRO_CONFCAUSEGROUPLIST;
    private static final String DB_SCHEMA9 = CREATE_TABLE_SERPRO_CONFSYMPGROUPLIST;
    private static final String DB_SCHEMA10 = CREATE_TABLE_SERPRO_CONFSYMPCODELIST;
    private static final String DB_SCHEMA11 = CREATE_TABLE_SERPRO_CONFPROBGROUPLIST;
    private static final String DB_SCHEMA12 = CREATE_TABLE_SERPRO_CONFPROBCODELIST;
    private static final String DB_SCHEMA13 = CREATE_TABLE_SERPRO_CONFMATTEMPLLIST;
    private static final String DB_SCHEMA14 = CREATE_TABLE_SERPRO_COLLEAGUELIST;
    private static final String DB_SCHEMA15 = CREATE_TABLE_SERPRO_ENTITLEMENTLIST;
    private static final String DB_SCHEMA16 = CREATE_TABLE_SERPRO_ATTACHLIST;
    private static final String DB_SCHEMA17 = CREATE_TABLE_SERPRO_STATUS_FOLLOW_LIST;    
    
    public ServiceProOfflineConstraintsDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_SCHEMA1);
        db.execSQL(DB_SCHEMA2);
        db.execSQL(DB_SCHEMA3);
        db.execSQL(DB_SCHEMA4);
        db.execSQL(DB_SCHEMA5);
        db.execSQL(DB_SCHEMA6);
        db.execSQL(DB_SCHEMA7);
        db.execSQL(DB_SCHEMA8);
        db.execSQL(DB_SCHEMA9);
        db.execSQL(DB_SCHEMA10);
        db.execSQL(DB_SCHEMA11);
        db.execSQL(DB_SCHEMA12);
        db.execSQL(DB_SCHEMA13);
        db.execSQL(DB_SCHEMA14);
        db.execSQL(DB_SCHEMA15);
        db.execSQL(DB_SCHEMA16);
        db.execSQL(DB_SCHEMA17);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	 if(oldVersion < newVersion){
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_STATUSLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_COLTASK);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST);
    		 db.execSQL("DROP TABLE IF EXISTS " + ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST);
             onCreate(db);
             ServiceProConstants.showLog("Upgrading database. Existing contents will be lost. ["
                     + oldVersion + "]->[" + newVersion + "]");
         }      
    }
    
}//End of class ServiceProOfflineConstraintsDB