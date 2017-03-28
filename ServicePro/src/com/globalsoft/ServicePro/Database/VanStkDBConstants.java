package com.globalsoft.ServicePro.Database;

import android.provider.BaseColumns;

public final class VanStkDBConstants {
	
	//Vanstock Database Related Constants
	
	 //Categories Table Definitions
    public static final String TABLE_VANSTK_EMP = "vs_emp_table";
    public static final String TABLE_VANSTK_EMP_SERIAL = "vs_emp_srl_table";
    public static final String TABLE_VANSTK_EMP_STO = "vs_emp_sto_table";
    public static final String TABLE_VANSTK_COL = "vs_col_table";
    public static final String TABLE_VANSTK_COL_SERIAL = "vs_col_srl_table";
    public static final String TABLE_VANSTK_COL_STOCK = "vs_col_stock_table";
    public static final String TABLE_VANSTK_COL_STOCK_SERIAL = "vs_col_stock_srl_table";
    
    
    public static final String VAN_EMP_COL_ID = "COL_ID";
    
    //Vanstock Table Definitions
    public static final String VAN_COL_ID = BaseColumns._ID;
    public static final String VAN_COL_BP_UNAME = "BP_UNAME";
    public static final String VAN_COL_MATNR = "MATNR";
    public static final String VAN_COL_WERKS = "WERKS";
    public static final String VAN_COL_LGORT = "LGORT";
    public static final String VAN_COL_CHARG = "CHARG";
    public static final String VAN_COL_MAKTX = "MAKTX_INSYLANGU";
    public static final String VAN_COL_MEINS = "MEINS";
    public static final String VAN_COL_LABST = "LABST";
    public static final String VAN_COL_TRAME = "TRAME";
    public static final String VAN_COL_ZZSERNR_EXIST = "ZZSERNR_EXIST";
    
    
    //Vanstock Serial Table Definitions
    public static final String VAN_SER_COL_ID = BaseColumns._ID;
    public static final String VAN_SER_COL_BP_UNAME = "BP_UNAME";
    public static final String VAN_SER_COL_MATNR = "MATNR";
    public static final String VAN_SER_COL_WERKS = "WERKS";
    public static final String VAN_SER_COL_LGORT = "LGORT";
    public static final String VAN_SER_COL_MAKTX = "MAKTX_INSYLANGU";
    public static final String VAN_SER_COL_EQUNR = "EQUNR";
    public static final String VAN_SER_COL_SERNR = "SERNR";
    
    
    //Vanstock STO's Table Definitions
    public static final String VAN_STO_COL_ID = BaseColumns._ID;
    public static final String VAN_STO_COL_EBELN = "EBELN";
    public static final String VAN_STO_COL_EBELP = "EBELP";
    public static final String VAN_STO_COL_BEDAT = "BEDAT";
    public static final String VAN_STO_COL_LIFNR = "LIFNR";
    public static final String VAN_STO_COL_SPRAS = "SPRAS";
    public static final String VAN_STO_COL_ZTERM = "ZTERM";
    public static final String VAN_STO_COL_EKORG = "EKORG";
    public static final String VAN_STO_COL_WAERS = "WAERS";
    public static final String VAN_STO_COL_INCO1 = "INCO1";
    public static final String VAN_STO_COL_INCO2 = "INCO2";
    public static final String VAN_STO_COL_RESWK = "RESWK";
    public static final String VAN_STO_COL_RESLO = "RESLO";
    public static final String VAN_STO_COL_MATNR = "MATNR";
    public static final String VAN_STO_COL_TXZ01 = "TXZ01";
    public static final String VAN_STO_COL_BUKRS = "BUKRS";
    public static final String VAN_STO_COL_WERKS = "WERKS";
    public static final String VAN_STO_COL_LGORT = "LGORT";
    public static final String VAN_STO_COL_BEDNR = "BEDNR";
    public static final String VAN_STO_COL_MATKL = "MATKL";
    public static final String VAN_STO_COL_PSTYP = "PSTYP";
    public static final String VAN_STO_COL_MENGE = "MENGE";
    public static final String VAN_STO_COL_MEINS = "MEINS";
    public static final String VAN_STO_COL_NETPR = "NETPR";
    public static final String VAN_STO_COL_NETWR = "NETWR";
    public static final String VAN_STO_COL_WEMNG = "WEMNG";
    public static final String VAN_STO_COL_WAMNG = "WAMNG";
    
    //Vanstock Colleague Table Definitions
    public static final String VAN_CLG_COL_ID = BaseColumns._ID;
    public static final String VAN_CLG_COL_PARTNER = "PARTNER";
    public static final String VAN_CLG_COL_MC_NAME1 = "MC_NAME1";
    public static final String VAN_CLG_COL_MC_NAME2 = "MC_NAME2";
    public static final String VAN_CLG_COL_TEL_NO = "TEL_NO";
    public static final String VAN_CLG_COL_TEL_NO2 = "TEL_NO2";
  	public static final String VAN_CLG_COL_UNAME = "UNAME";
    public static final String VAN_CLG_COL_PLANT = "PLANT";
    public static final String VAN_CLG_COL_STORAGE_LOC = "STORAGE_LOC";
    
   //Vanstock Colleague Serial Table Definitions
    public static final String VAN_CLG_SRL_COL_ID = BaseColumns._ID;
    public static final String VAN_CLG_SRL_COL_BP_UNAME = "BP_UNAME";
    public static final String VAN_CLG_SRL_COL_WERKS = "WERKS";
    public static final String VAN_CLG_SRL_COL_LGORT = "LGORT";
    public static final String VAN_CLG_SRL_COL_LGOBE = "LGOBE";
    
}//End of class VanStkDBConstants