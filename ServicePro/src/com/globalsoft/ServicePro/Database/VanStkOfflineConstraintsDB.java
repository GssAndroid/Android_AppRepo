package com.globalsoft.ServicePro.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.globalsoft.ServicePro.ServiceProConstants;

public class VanStkOfflineConstraintsDB extends SQLiteOpenHelper {
	
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "vanstock_db";
    

    private static final String CREATE_TABLE_VANSTK_EMP = "CREATE TABLE IF NOT EXISTS "
            + VanStkDBConstants.TABLE_VANSTK_EMP + " (" + VanStkDBConstants.VAN_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ VanStkDBConstants.VAN_COL_BP_UNAME + " text NOT NULL, " + VanStkDBConstants.VAN_COL_MATNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_COL_WERKS + " text NOT NULL, " + VanStkDBConstants.VAN_COL_LGORT + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_COL_CHARG + " text NOT NULL, " + VanStkDBConstants.VAN_COL_MAKTX + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_COL_MEINS + " text NOT NULL, " + VanStkDBConstants.VAN_COL_LABST + " text NOT NULL, "
            + VanStkDBConstants.VAN_COL_TRAME + " text NOT NULL, "+ VanStkDBConstants.VAN_COL_ZZSERNR_EXIST + " text NOT NULL"+ ");";
    
    
    
    private static final String CREATE_TABLE_VANSTK_EMP_SERIAL = "CREATE TABLE IF NOT EXISTS "
            + VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL + " (" + VanStkDBConstants.VAN_SER_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ VanStkDBConstants.VAN_SER_COL_BP_UNAME + " text NOT NULL, " + VanStkDBConstants.VAN_SER_COL_MATNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_SER_COL_WERKS + " text NOT NULL, " + VanStkDBConstants.VAN_SER_COL_LGORT + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_SER_COL_MAKTX + " text NOT NULL, " + VanStkDBConstants.VAN_SER_COL_EQUNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_SER_COL_SERNR + " text NOT NULL"+ ");";
    
    
    private static final String CREATE_TABLE_VANSTK_EMP_STO = "CREATE TABLE IF NOT EXISTS "
            + VanStkDBConstants.TABLE_VANSTK_EMP_STO + " (" + VanStkDBConstants.VAN_STO_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ VanStkDBConstants.VAN_STO_COL_EBELN + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_EBELP + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_BEDAT + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_LIFNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_SPRAS + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_ZTERM + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_EKORG + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_WAERS + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_INCO1 + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_INCO2 + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_RESWK + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_RESLO + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_MATNR + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_TXZ01 + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_BUKRS + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_WERKS + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_LGORT + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_BEDNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_MATKL + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_PSTYP + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_MENGE + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_MEINS + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_STO_COL_NETPR + " text NOT NULL, " + VanStkDBConstants.VAN_STO_COL_NETWR + " text NOT NULL, "
            + VanStkDBConstants.VAN_STO_COL_WEMNG + " text NOT NULL, "+ VanStkDBConstants.VAN_STO_COL_WAMNG + " text NOT NULL"+ ");";
    
    
    private static final String CREATE_TABLE_VANSTK_COL = "CREATE TABLE IF NOT EXISTS "
            + VanStkDBConstants.TABLE_VANSTK_COL + " (" + VanStkDBConstants.VAN_CLG_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ VanStkDBConstants.VAN_CLG_COL_PARTNER + " text NOT NULL, " + VanStkDBConstants.VAN_CLG_COL_MC_NAME1 + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_CLG_COL_MC_NAME2 + " text NOT NULL, " + VanStkDBConstants.VAN_CLG_COL_TEL_NO + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_CLG_COL_TEL_NO2 + " text NOT NULL, " + VanStkDBConstants.VAN_CLG_COL_UNAME + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_CLG_COL_PLANT + " text NOT NULL, "
            + VanStkDBConstants.VAN_CLG_COL_STORAGE_LOC + " text NOT NULL"+ ");";
    
    
    private static final String CREATE_TABLE_VANSTK_COL_SERIAL = "CREATE TABLE IF NOT EXISTS "
            + VanStkDBConstants.TABLE_VANSTK_COL_SERIAL + " (" + VanStkDBConstants.VAN_CLG_SRL_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ VanStkDBConstants.VAN_CLG_SRL_COL_BP_UNAME + " text NOT NULL, " + VanStkDBConstants.VAN_CLG_SRL_COL_WERKS + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_CLG_SRL_COL_LGORT + " text NOT NULL, "+ VanStkDBConstants.VAN_CLG_SRL_COL_LGOBE + " text NOT NULL"+ ");";
    
    
    //For Vanstock Colleagues Stock and their Serial Numbers
    private static final String CREATE_TABLE_VANSTK_COL_STOCK = "CREATE TABLE IF NOT EXISTS "
            + VanStkDBConstants.TABLE_VANSTK_COL_STOCK + " (" + VanStkDBConstants.VAN_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ VanStkDBConstants.VAN_COL_BP_UNAME + " text NOT NULL, " + VanStkDBConstants.VAN_COL_MATNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_COL_WERKS + " text NOT NULL, " + VanStkDBConstants.VAN_COL_LGORT + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_COL_CHARG + " text NOT NULL, " + VanStkDBConstants.VAN_COL_MAKTX + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_COL_MEINS + " text NOT NULL, " + VanStkDBConstants.VAN_COL_LABST + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_COL_TRAME + " text NOT NULL, " + VanStkDBConstants.VAN_COL_ZZSERNR_EXIST + " text NOT NULL, "
            + VanStkDBConstants.VAN_EMP_COL_ID + " text NOT NULL"+ ");";
    
    
    private static final String CREATE_TABLE_VANSTK_COL_STOCK_SERIAL = "CREATE TABLE IF NOT EXISTS "
            + VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL + " (" + VanStkDBConstants.VAN_SER_COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ VanStkDBConstants.VAN_SER_COL_BP_UNAME + " text NOT NULL, " + VanStkDBConstants.VAN_SER_COL_MATNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_SER_COL_WERKS + " text NOT NULL, " + VanStkDBConstants.VAN_SER_COL_LGORT + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_SER_COL_MAKTX + " text NOT NULL, " + VanStkDBConstants.VAN_SER_COL_EQUNR + " text NOT NULL, "
    		+ VanStkDBConstants.VAN_SER_COL_SERNR + " text NOT NULL, "+ VanStkDBConstants.VAN_EMP_COL_ID + " text NOT NULL"+ ");";
    
    
    private static final String DB_SCHEMA1 = CREATE_TABLE_VANSTK_EMP; 
    private static final String DB_SCHEMA2 = CREATE_TABLE_VANSTK_EMP_SERIAL;
    private static final String DB_SCHEMA3 = CREATE_TABLE_VANSTK_EMP_STO; 
    private static final String DB_SCHEMA4 = CREATE_TABLE_VANSTK_COL;
    private static final String DB_SCHEMA5 = CREATE_TABLE_VANSTK_COL_SERIAL; 
    private static final String DB_SCHEMA6 = CREATE_TABLE_VANSTK_COL_STOCK; 
    private static final String DB_SCHEMA7 = CREATE_TABLE_VANSTK_COL_STOCK_SERIAL; 

    public VanStkOfflineConstraintsDB(Context context) {
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	 if(oldVersion < newVersion){
    		 db.execSQL("DROP TABLE IF EXISTS " + VanStkDBConstants.TABLE_VANSTK_EMP);
    		 db.execSQL("DROP TABLE IF EXISTS " + VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL);
    		 db.execSQL("DROP TABLE IF EXISTS " + VanStkDBConstants.TABLE_VANSTK_EMP_STO);
    		 db.execSQL("DROP TABLE IF EXISTS " + VanStkDBConstants.TABLE_VANSTK_COL);
    		 db.execSQL("DROP TABLE IF EXISTS " + VanStkDBConstants.TABLE_VANSTK_COL_SERIAL);
    		 db.execSQL("DROP TABLE IF EXISTS " + VanStkDBConstants.TABLE_VANSTK_COL_STOCK);
    		 db.execSQL("DROP TABLE IF EXISTS " + VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL);
             onCreate(db);
             ServiceProConstants.showLog("Upgrading database. Existing contents will be lost. ["
                     + oldVersion + "]->[" + newVersion + "]");
         }      
    }

}//End of class VanStkOfflineConstraintsDB
