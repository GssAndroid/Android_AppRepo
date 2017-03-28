package com.globalsoft.ServicePro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServiceProDBAdapter {	
	//TaskList Related value			
	public static final String TASKLIST_TABLE = "tasklist";
    private static final String TASKLIST_TABLE_CREATE =
            "create table "+TASKLIST_TABLE+" (" 
             + ServiceProTaskDetailsPersistent.KEY_ROWID+" integer primary key autoincrement,"
             + ServiceProTaskDetailsPersistent.KEY_OBJECT_ID+" text  not null,"
             + ServiceProTaskDetailsPersistent.KEY_PROCESS_TYPE+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_NUMBER_EXT+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_ZZKEYDATE+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_PARTNER+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_NAME_ORG1+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_NAME_ORG2+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_NICKNAME+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CITY+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_POSTL_COD1+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_STREET+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_COUNTRYISO+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_REGION+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_STATUS+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_TXT30+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_STATUS_REASON+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP1_PARTNER+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP1_NAME1_TEXT+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP1_TEL_NO+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP1_TEL_NO2+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP2_PARTNER+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP2_NAME1_TEXT+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP2_TEL_NO+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_CP2_TEL_NO2+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_DESCRIPTION+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_PRIORITY+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_IB_IBASE+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_IB_INSTANCE+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_SERIAL_NUMBER+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_REFOBJ_PRODUCT_ID+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_IB_DESCR+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_IB_INST_DESCR+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_REFOBJ_PRODUCT_DESCR+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_TIMEZONE_FROM+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_ZZETADATE+" text not null," 
             + ServiceProTaskDetailsPersistent.KEY_ZZETATIME+" text not null,"
             + ServiceProTaskDetailsPersistent.KEY_PROCESS_TYPE_DESCR+" text not null,"
			 + ServiceProTaskDetailsPersistent.KEY_NOTE+" text not null,"
			 + ServiceProTaskDetailsPersistent.KEY_ZZFIRSTSERVICEPRODUCT+" text not null,"
			 + ServiceProTaskDetailsPersistent.KEY_ZZFIRSTSERVICEPRODUCTDESCR+" text not null,"
			 + ServiceProTaskDetailsPersistent.KEY_FIELDNOTE+" text not null,"
			 
             + ServiceProTaskDetailsPersistent.KEY_ORDER+" integer not null);";
    
    //Error Message Related value    
    public static final String ERRLIST_TABLE = "errorlist";
    private static final String ERRLIST_TABLE_CREATE =
            "create table "+ERRLIST_TABLE+" (id integer primary key autoincrement, " 
             + ServiceProErrorMessagePersistent.KEY_TRANCID+" text not null,"
             + ServiceProErrorMessagePersistent.KEY_ERRTYPE+" text not null,"
             + ServiceProErrorMessagePersistent.KEY_ERRDESC+" text not null," 
             + ServiceProErrorMessagePersistent.KEY_APINAME+" text not null);";
    
    /*//Colleague Related value    
    public static final String COLLLIST_TABLE = "colllist";
    private static final String COLLLIST_TABLE_CREATE =
            "create table "+COLLLIST_TABLE+" (id integer primary key autoincrement, " 
             + ServiceProColleagueDetailsPersistent.KEY_PARTNER+" text not null,"
             + ServiceProColleagueDetailsPersistent.KEY_MC_NAME1+" text not null,"
             + ServiceProColleagueDetailsPersistent.KEY_MC_NAME2+" text not null," 
             + ServiceProColleagueDetailsPersistent.KEY_TEL_NO+" text not null,"
             + ServiceProColleagueDetailsPersistent.KEY_TEL_NO2+" text not null,"
             + ServiceProColleagueDetailsPersistent.KEY_PLANT+" text not null," 
             + ServiceProColleagueDetailsPersistent.KEY_STORAGE_LOC+" text not null);";*/
    
    public static final String DATABASE_NAME = "OFFLINEDB1";    
    public static final int DATABASE_VERSION = 1;
    private  final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase sqlitedatabase;
    
    public ServiceProDBAdapter(Context ctx) 
    { 
    	context = ctx; 
    	DBHelper = new DatabaseHelper(context); 
    	sqlitedatabase = DBHelper.getWritableDatabase(); 
    	DBHelper.close();
		sqlitedatabase.close();
    } 

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
        	System.out.println("Table before creation.");
            db.execSQL(TASKLIST_TABLE_CREATE);
            db.execSQL(ERRLIST_TABLE_CREATE); 
            //db.execSQL(COLLLIST_TABLE_CREATE);             
            System.out.println("Table after creation.");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        	db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }//End of class DatabaseHelper
}