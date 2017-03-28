package com.globalsoft.ServicePro.Database;

import android.provider.BaseColumns;

public class ServiceProDBConstants {
	     
	//ServicePro Status List Table Definitions
	public static final String TABLE_SERPRO_STATUSLIST = "sp_statuslist_table";   
	
    public static final String SERPRO_STATUS_COL_ID = BaseColumns._ID;
    public static final String SERPRO_STATUS_COL_STATUS = "STATUS";
    public static final String SERPRO_STATUS_COL_TXT30 = "TXT30";
    public static final String SERPRO_STATUS_COL_ZZSTATUS_ICON = "ZZSTATUS_ICON";
    public static final String SERPRO_STATUS_COL_ZZSTATUS_POSTSETACTION = "ZZSTATUS_POSTSETACTION";
    
    //ServicePro Status Follow List Table Definitions
  	public static final String TABLE_SERPRO_STATUSFOLLOWLIST = "sp_statusfollowlist_table";   
  	
  	public static final String SERPRO_STATUS_FOLLOW_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_STATUS_FOLLOW_COL_PROCESS_TYPE = "PROCESS_TYPE";
  	public static final String SERPRO_STATUS_FOLLOW_COL_STATUS = "STATUS";
  	public static final String SERPRO_STATUS_FOLLOW_COL_STONR_NEXT = "STONR_NEXT";
  	public static final String SERPRO_STATUS_FOLLOW_COL_STATUS_NEXT = "STATUS_NEXT";
    
    //ServicePro Attachment List Table Definitions
  	public static final String TABLE_SERPRO_ATTACHLIST = "sp_attachlist_table";   
  	
  	public static final String SERPRO_ATTACH_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_ATTACH_COL_OBJECT_ID = "OBJECT_ID";
  	public static final String SERPRO_ATTACH_COL_OBJECT_TYPE = "OBJECT_TYPE";
  	public static final String SERPRO_ATTACH_COL_NUMBER_EXT = "NUMBER_EXT";
  	public static final String SERPRO_ATTACH_COL_ATTCHMNT_ID = "ATTCHMNT_ID";
  	public static final String SERPRO_ATTACH_COL_ATTCHMNT_CNTNT = "ATTCHMNT_CNTNT";
	    
	//ServicePro Colleague Task List Table Definitions
	public static final String TABLE_SERPRO_COLTASK = "sp_colltask_table";   
	
    public static final String SERPRO_COLTASK_COL_ID = BaseColumns._ID;
    public static final String SERPRO_COLTASK_COL_OBJECT_ID = "OBJECT_ID";
    public static final String SERPRO_COLTASK_COL_PROCESS_TYPE = "PROCESS_TYPE";
    public static final String SERPRO_COLTASK_NUMBER_EXT = "ZZFIRSTSERVICEITEM";
    public static final String SERPRO_COLTASK_COL_ZZKEYDATE = "ZZKEYDATE";
    public static final String SERPRO_COLTASK_COL_PARTNER = "PARTNER";
    public static final String SERPRO_COLTASK_COL_NAME_ORG1 = "NAME_ORG1";
    public static final String SERPRO_COLTASK_COL_NAME_ORG2 = "NAME_ORG2";
    public static final String SERPRO_COLTASK_COL_STRAS = "STRAS";
    public static final String SERPRO_COLTASK_COL_ORT01 = "ORT01";
    public static final String SERPRO_COLTASK_COL_REGIO = "REGIO";
    public static final String SERPRO_COLTASK_COL_PSTLZ = "PSTLZ";
    public static final String SERPRO_COLTASK_COL_LAND1 = "LAND1";
    public static final String SERPRO_COLTASK_COL_STATUS = "STATUS";
    public static final String SERPRO_COLTASK_COL_STATUS_TXT30 = "STATUS_TXT30";
    public static final String SERPRO_COLTASK_COL_STATUS_REASON = "STATUS_REASON";
    public static final String SERPRO_COLTASK_COL_CP1_PARTNER = "CP1_PARTNER";
    public static final String SERPRO_COLTASK_COL_CP1_NAME1_TEXT = "CP1_NAME1_TEXT";
    public static final String SERPRO_COLTASK_COL_CP1_TEL_NO = "CP1_TEL_NO";
    public static final String SERPRO_COLTASK_COL_CP1_TEL_NO2 = "CP1_TEL_NO2";
    public static final String SERPRO_COLTASK_COL_CP2_PARTNER = "CP2_PARTNER";
    public static final String SERPRO_COLTASK_COL_CP2_NAME1_TEXT = "CP2_NAME1_TEXT";
    public static final String SERPRO_COLTASK_COL_CP2_TEL_NO = "CP2_TEL_NO";
    public static final String SERPRO_COLTASK_COL_CP2_TEL_NO2 = "CP2_TEL_NO2";
    public static final String SERPRO_COLTASK_COL_DESCRIPTION = "DESCRIPTION";
    public static final String SERPRO_COLTASK_COL_PRIORITY = "PRIORITY";
    public static final String SERPRO_COLTASK_COL_IB_IBASE = "IB_IBASE";
    public static final String SERPRO_COLTASK_COL_IB_INSTANCE = "IB_INSTANCE";
    public static final String SERPRO_COLTASK_COL_SERIAL_NUMBER = "SERIAL_NUMBER";
    public static final String SERPRO_COLTASK_COL_REFOBJ_PRODUCT_ID = "REFOBJ_PRODUCT_ID";
    public static final String SERPRO_COLTASK_COL_IB_DESCR = "IB_DESCR";
    public static final String SERPRO_COLTASK_COL_IB_INST_DESCR = "IB_INST_DESCR";
    public static final String SERPRO_COLTASK_COL_REFOBJ_PRODUCT_DESCR = "REFOBJ_PRODUCT_DESCR";
    public static final String SERPRO_COLTASK_COL_TIMEZONE_FROM = "TIMEZONE_FROM";
    public static final String SERPRO_COLTASK_COL_ZZETADATE = "ZZETADATE";
    public static final String SERPRO_COLTASK_COL_ZZETATIME = "ZZETATIME";
    public static final String SERPRO_COLTASK_COL_PROCESS_TYPE_DESCR = "PROCESS_TYPE_DESCR";
    public static final String SERPRO_COLTASK_COL_NOTE = "NOTE";
    public static final String SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCT = "ZZFIRSTSERVICEPRODUCT";
    public static final String SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCTDESCR = "ZZFIRSTSERVICEPRODUCTDESCR";
    public static final String SERPRO_COLL_ID = "COLL_ID";
    public static final String SERPRO_COL_FIELD_NOTES = "ZZFIELDNOTE";
    
    
    //ServicePro Confirmation Overview List Table Definitions
  	public static final String TABLE_SERPRO_CONFLIST = "sp_conflist_table";   

  	public static final String SERPRO_CONFLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFLIST_COL_OBJECT_ID = "OBJECT_ID";
  	public static final String SERPRO_CONFLIST_COL_PROCESS_TYPE = "PROCESS_TYPE";
  	public static final String SERPRO_CONFLIST_COL_ZZKEYDATE = "ZZKEYDATE";
  	public static final String SERPRO_CONFLIST_COL_PARTNER = "PARTNER";
  	public static final String SERPRO_CONFLIST_COL_NAME_ORG1 = "NAME_ORG1";
  	public static final String SERPRO_CONFLIST_COL_NAME_ORG2 = "NAME_ORG2";
  	public static final String SERPRO_CONFLIST_COL_NICKNAME = "NICKNAME";  
  	public static final String SERPRO_CONFLIST_COL_STRAS = "STRAS";
  	public static final String SERPRO_CONFLIST_COL_ORT01 = "ORT01";
  	public static final String SERPRO_CONFLIST_COL_REGIO = "REGIO";
  	public static final String SERPRO_CONFLIST_COL_PSTLZ = "PSTLZ";
  	public static final String SERPRO_CONFLIST_COL_LAND1 = "LAND1";
  	public static final String SERPRO_CONFLIST_COL_STATUS = "STATUS";
  	public static final String SERPRO_CONFLIST_COL_STATUS_TXT30 = "STATUS_TXT30";
  	public static final String SERPRO_CONFLIST_COL_STATUS_REASON = "STATUS_REASON";
  	public static final String SERPRO_CONFLIST_COL_CP1_PARTNER = "CP1_PARTNER";
  	public static final String SERPRO_CONFLIST_COL_CP1_NAME1_TEXT = "CP1_NAME1_TEXT";
  	public static final String SERPRO_CONFLIST_COL_CP1_TEL_NO = "CP1_TEL_NO";
  	public static final String SERPRO_CONFLIST_COL_CP1_TEL_NO2 = "CP1_TEL_NO2";
  	public static final String SERPRO_CONFLIST_COL_CP2_PARTNER = "CP2_PARTNER";
  	public static final String SERPRO_CONFLIST_COL_CP2_NAME1_TEXT = "CP2_NAME1_TEXT";
  	public static final String SERPRO_CONFLIST_COL_CP2_TEL_NO = "CP2_TEL_NO";
  	public static final String SERPRO_CONFLIST_COL_CP2_TEL_NO2 = "CP2_TEL_NO2";
  	public static final String SERPRO_CONFLIST_COL_DESCRIPTION = "DESCRIPTION";
  	public static final String SERPRO_CONFLIST_COL_PRIORITY = "PRIORITY";
  	public static final String SERPRO_CONFLIST_COL_IB_IBASE = "IB_IBASE";
  	public static final String SERPRO_CONFLIST_COL_IB_INSTANCE = "IB_INSTANCE";
  	public static final String SERPRO_CONFLIST_COL_SERIAL_NUMBER = "SERIAL_NUMBER";
  	public static final String SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_ID = "REFOBJ_PRODUCT_ID";
  	public static final String SERPRO_CONFLIST_COL_IB_DESCR = "IB_DESCR";
  	public static final String SERPRO_CONFLIST_COL_IB_INST_DESCR = "IB_INST_DESCR";
  	public static final String SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_DESCR = "REFOBJ_PRODUCT_DESCR";
  	public static final String SERPRO_CONFLIST_COL_TIMEZONE_FROM = "TIMEZONE_FROM";
  	public static final String SERPRO_CONFLIST_COL_NUMBER_EXT = "NUMBER_EXT";
  	public static final String SERPRO_CONFLIST_COL_PRODUCT_ID = "PRODUCT_ID";
  	public static final String SERPRO_CONFLIST_COL_QUANTITY = "QUANTITY";
  	public static final String SERPRO_CONFLIST_COL_PROCESS_QTY_UNIT = "PROCESS_QTY_UNIT";
  	public static final String SERPRO_CONFLIST_COL_ZZITEM_DESCRIPTION = "ZZITEM_DESCRIPTION";
  	public static final String SERPRO_CONFLIST_COL_ZZITEM_TEXT = "ZZITEM_TEXT";
  /*	public static final String SERPRO_CONFLIST_COL_FIELD_NOTES = "ZZFIELDNOTE";
  	public static final String SERPRO_CONFLIST_COL_ITEM_NUMBER = "ZZFIRSTSERVICEITEM";*/
  	
  	//ServicePro Confirmation Overview List Table Definitions
  	public static final String TABLE_SERPRO_CONFSPARELIST = "sp_confsparelist_table";   

  	public static final String SERPRO_CONFSPARELIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFSPARELIST_COL_OBJECT_ID = "OBJECT_ID";
  	public static final String SERPRO_CONFSPARELIST_COL_PROCESS_TYPE = "PROCESS_TYPE";
  	public static final String SERPRO_CONFSPARELIST_COL_ZZKEYDATE = "ZZKEYDATE";
  	public static final String SERPRO_CONFSPARELIST_COL_PARTNER = "PARTNER";
  	public static final String SERPRO_CONFSPARELIST_COL_NAME_ORG1 = "NAME_ORG1";
  	public static final String SERPRO_CONFSPARELIST_COL_NAME_ORG2 = "NAME_ORG2";
  	public static final String SERPRO_CONFSPARELIST_COL_NICKNAME = "NICKNAME";  
  	public static final String SERPRO_CONFSPARELIST_COL_STRAS = "STRAS";
  	public static final String SERPRO_CONFSPARELIST_COL_ORT01 = "ORT01";
  	public static final String SERPRO_CONFSPARELIST_COL_REGIO = "REGIO";
  	public static final String SERPRO_CONFSPARELIST_COL_PSTLZ = "PSTLZ";
  	public static final String SERPRO_CONFSPARELIST_COL_LAND1 = "LAND1";
  	public static final String SERPRO_CONFSPARELIST_COL_STATUS = "STATUS";
  	public static final String SERPRO_CONFSPARELIST_COL_STATUS_TXT30 = "STATUS_TXT30";
  	public static final String SERPRO_CONFSPARELIST_COL_STATUS_REASON = "STATUS_REASON";
  	public static final String SERPRO_CONFSPARELIST_COL_CP1_PARTNER = "CP1_PARTNER";
  	public static final String SERPRO_CONFSPARELIST_COL_CP1_NAME1_TEXT = "CP1_NAME1_TEXT";
  	public static final String SERPRO_CONFSPARELIST_COL_CP1_TEL_NO = "CP1_TEL_NO";
  	public static final String SERPRO_CONFSPARELIST_COL_CP1_TEL_NO2 = "CP1_TEL_NO2";
  	public static final String SERPRO_CONFSPARELIST_COL_CP2_PARTNER = "CP2_PARTNER";
  	public static final String SERPRO_CONFSPARELIST_COL_CP2_NAME1_TEXT = "CP2_NAME1_TEXT";
  	public static final String SERPRO_CONFSPARELIST_COL_CP2_TEL_NO = "CP2_TEL_NO";
  	public static final String SERPRO_CONFSPARELIST_COL_CP2_TEL_NO2 = "CP2_TEL_NO2";
  	public static final String SERPRO_CONFSPARELIST_COL_DESCRIPTION = "DESCRIPTION";
  	public static final String SERPRO_CONFSPARELIST_COL_PRIORITY = "PRIORITY";
  	public static final String SERPRO_CONFSPARELIST_COL_IB_IBASE = "IB_IBASE";
  	public static final String SERPRO_CONFSPARELIST_COL_IB_INSTANCE = "IB_INSTANCE";
  	public static final String SERPRO_CONFSPARELIST_COL_SERIAL_NUMBER = "SERIAL_NUMBER";
  	public static final String SERPRO_CONFSPARELIST_COL_REFOBJ_PRODUCT_ID = "REFOBJ_PRODUCT_ID";
  	public static final String SERPRO_CONFSPARELIST_COL_IB_DESCR = "IB_DESCR";
  	public static final String SERPRO_CONFSPARELIST_COL_IB_INST_DESCR = "IB_INST_DESCR";
  	public static final String SERPRO_CONFSPARELIST_COL_REFOBJ_PRODUCT_DESCR = "REFOBJ_PRODUCT_DESCR";
  	public static final String SERPRO_CONFSPARELIST_COL_TIMEZONE_FROM = "TIMEZONE_FROM";
  	public static final String SERPRO_CONFSPARELIST_COL_NUMBER_EXT = "NUMBER_EXT";
  	public static final String SERPRO_CONFSPARELIST_COL_PRODUCT_ID = "PRODUCT_ID";
  	public static final String SERPRO_CONFSPARELIST_COL_QUANTITY = "QUANTITY";
  	public static final String SERPRO_CONFSPARELIST_COL_PROCESS_QTY_UNIT = "PROCESS_QTY_UNIT";
  	public static final String SERPRO_CONFSPARELIST_COL_ZZITEM_DESCRIPTION = "ZZITEM_DESCRIPTION";
  	public static final String SERPRO_CONFSPARELIST_COL_ZZITEM_TEXT = "ZZITEM_TEXT";
  	
  	//ServicePro Confirmation collection List Table Definitions
  	public static final String TABLE_SERPRO_CONFCOLLECLIST = "sp_confcolleclist_table";   

  	public static final String SERPRO_CONFCOLLECLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFCOLLECLIST_COL_OBJECT_ID = "OBJECT_ID";
  	public static final String SERPRO_CONFCOLLECLIST_COL_PROCESS_TYPE = "PROCESS_TYPE";
  	public static final String SERPRO_CONFCOLLECLIST_COL_NUMBER_EXT = "NUMBER_EXT";
  	public static final String SERPRO_CONFCOLLECLIST_COL_ERDAT = "ERDAT";
  	public static final String SERPRO_CONFCOLLECLIST_COL_STATUS = "STATUS";
  	public static final String SERPRO_CONFCOLLECLIST_COL_TXT30 = "TXT30";
  	public static final String SERPRO_CONFCOLLECLIST_COL_SRCDOC_OBJECT_ID = "SRCDOC_OBJECT_ID";
  	public static final String SERPRO_CONFCOLLECLIST_COL_SRCDOC_PROCESS_TYPE = "SRCDOC_PROCESS_TYPE";
  	public static final String SERPRO_CONFCOLLECLIST_COL_SRCDOC_NUMBER_EXT = "SRCDOC_NUMBER_EXT";
  	
  	//ServicePro Confirmation ActProdList Table Definitions
  	public static final String TABLE_SERPRO_CONFPRODUCTLIST = "sp_confproductlist_table";   

  	public static final String SERPRO_CONFPRODUCTLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFPRODUCTLIST_COL_PRODUCT_ID = "PRODUCT_ID";
  	public static final String SERPRO_CONFPRODUCTLIST_COL_SHORT_TEXT = "SHORT_TEXT";
  	
  	//ServicePro Confirmation Cause Group Table Definitions
  	public static final String TABLE_SERPRO_CONFCAUSEGROUPLIST = "sp_confcausegrouplist_table";   

  	public static final String SERPRO_CONFCAUSEGROUPLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFCAUSEGROUPLIST_COL_CODEGRUPPE = "CODEGRUPPE";
  	public static final String SERPRO_CONFCAUSEGROUPLIST_COL_KURZTEXT = "KURZTEXT";
  	
  	//ServicePro Confirmation Cause Code Table Definitions
  	public static final String TABLE_SERPRO_CONFCAUSECODELIST = "sp_confcausecodelist_table";   

  	public static final String SERPRO_CONFCAUSECODELIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFCAUSECODELIST_COL_CODEGRUPPE = "CODEGRUPPE";
  	public static final String SERPRO_CONFCAUSECODELIST_COL_CODE = "CODE";
  	public static final String SERPRO_CONFCAUSECODELIST_COL_KURZTEXT = "KURZTEXT";
  	
  	//ServicePro Confirmation Symptm Group Table Definitions
  	public static final String TABLE_SERPRO_CONFSYMPGROUPLIST = "sp_confsympgrouplist_table";   

  	public static final String SERPRO_CONFSYMPGROUPLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFSYMPGROUPLIST_COL_CODEGRUPPE = "CODEGRUPPE";
  	public static final String SERPRO_CONFSYMPGROUPLIST_COL_KURZTEXT = "KURZTEXT";
  	
  	//ServicePro Confirmation Symptm Code Table Definitions
  	public static final String TABLE_SERPRO_CONFSYMPCODELIST = "sp_confsympcodelist_table";   

  	public static final String SERPRO_CONFSYMPCODELIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFSYMPCODELIST_COL_CODEGRUPPE = "CODEGRUPPE";
  	public static final String SERPRO_CONFSYMPCODELIST_COL_CODE = "CODE";
  	public static final String SERPRO_CONFSYMPCODELIST_COL_KURZTEXT = "KURZTEXT";
  	
  	//ServicePro Confirmation Problem Group Table Definitions
  	public static final String TABLE_SERPRO_CONFPROBGROUPLIST = "sp_confprobgrouplist_table";   

  	public static final String SERPRO_CONFPROBGROUPLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFPROBGROUPLIST_COL_CODEGRUPPE = "CODEGRUPPE";
  	public static final String SERPRO_CONFPROBGROUPLIST_COL_KURZTEXT = "KURZTEXT";
  	
  	//ServicePro Confirmation Problem Code Table Definitions
  	public static final String TABLE_SERPRO_CONFPROBCODELIST = "sp_confprobcodelist_table";   

  	public static final String SERPRO_CONFPROBCODELIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFPROBCODELIST_COL_CODEGRUPPE = "CODEGRUPPE";
  	public static final String SERPRO_CONFPROBCODELIST_COL_CODE = "CODE";
  	public static final String SERPRO_CONFPROBCODELIST_COL_KURZTEXT = "KURZTEXT";
  	
  	//ServicePro Confirmation MatterialEmpList List Table Definitions
  	public static final String TABLE_SERPRO_CONFMATTEMPLLIST = "sp_confmattemplist_table";   

  	public static final String SERPRO_CONFMATTEMPLLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_CONFMATTEMPLLIST_COL_BP_UNAME = "BP_UNAME";
  	public static final String SERPRO_CONFMATTEMPLLIST_COL_MATNR = "MATNR";
  	public static final String SERPRO_CONFMATTEMPLLIST_COL_MAKTX_INSYLANGU = "MAKTX_INSYLANGU";
  	public static final String SERPRO_CONFMATTEMPLLIST_COL_VRKME = "VRKME";
  	public static final String SERPRO_CONFMATTEMPLLIST_COL_MEINS = "MEINS";
  	
  	//ServicePro Colleague List Table Definitions
  	public static final String TABLE_SERPRO_COLLEAGUELIST = "sp_colleaguelist_table";   

  	public static final String SERPRO_COLLEAGUELIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_COLLEAGUELIST_COL_PARTNER = "PARTNER";
  	public static final String SERPRO_COLLEAGUELIST_COL_MC_NAME1 = "MC_NAME1";
  	public static final String SERPRO_COLLEAGUELIST_COL_MC_NAME2 = "MC_NAME2";
  	public static final String SERPRO_COLLEAGUELIST_COL_TEL_NO = "TEL_NO";
  	public static final String SERPRO_COLLEAGUELIST_COL_TEL_NO2 = "TEL_NO2";
  	public static final String SERPRO_COLLEAGUELIST_COL_UNAME = "UNAME";
  	public static final String SERPRO_COLLEAGUELIST_COL_PLANT = "PLANT";
  	public static final String SERPRO_COLLEAGUELIST_COL_STORAGE_LOC = "STORAGE_LOC";  	
  	
  	//ServicePro Entitlement Overview List Table Definitions
  	public static final String TABLE_SERPRO_ENTITLEMENTLIST = "sp_entitlementlist_table";   

  	public static final String SERPRO_ENTITLEMENTLIST_COL_ID = BaseColumns._ID;
  	public static final String SERPRO_ENTITLEMENTLIST_COL_ORDER_ID = "ORDER_ID";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_ITEM_ID = "ITEM_ID";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_ITEM_GUID = "ITEM_GUID";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_PROCESS_TYPE = "PROCESS_TYPE";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_PROFILE = "PROFILE";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_PROF_DESC = "PROF_DESC";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_ENT_TYPE = "ENT_TYPE";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_ENT_DESC = "ENT_DESC";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_DISC_LABOUR = "DISC_LABOUR";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_DISC_PARTS = "DISC_PARTS";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_DISC_TRAVEL = "DISC_TRAVEL";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_FREQUENCY = "FREQUENCY";
  	public static final String SERPRO_ENTITLEMENTLIST_COL_TIMEPERIOD = "TIMEPERIOD";
    
}//End of class ServiceProDBConstants
