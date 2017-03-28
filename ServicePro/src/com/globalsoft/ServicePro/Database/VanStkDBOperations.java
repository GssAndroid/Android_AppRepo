package com.globalsoft.ServicePro.Database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.globalsoft.ServicePro.ServiceProConstants;
import com.globalsoft.ServicePro.Constraints.VanStkColOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkColStrOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkSerOpConstraints;
import com.globalsoft.ServicePro.Constraints.VanStkTransOpConstraints;

public final class VanStkDBOperations {
	
	/*********************************************************************************************
     *     	Database Read Related Functions
     *********************************************************************************************/
    
    public static ArrayList readAllVanSTkEmpDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	VanStkOpConstraints stkCategory = null;
    	ArrayList stocksArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[11];
    		String[] dbValues = new String[11];
    		int colId = -1;
    		
    		if(stocksArrList != null)
    			stocksArrList.clear();
    		
    		Uri uri = Uri.parse(VanStkOfflineContraintsCP.VAN_EMP_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			//ServiceProConstants.showLog("No of Category Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return stocksArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_BP_UNAME);
			dbIndex[1] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_MATNR);
			dbIndex[2] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_WERKS);
			dbIndex[3] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_LGORT);
			dbIndex[4] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_CHARG);
			dbIndex[5] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_MAKTX);
			dbIndex[6] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_MEINS);
			dbIndex[7] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_LABST);
			dbIndex[8] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_TRAME);
			dbIndex[9] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_ZZSERNR_EXIST);
			dbIndex[10] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_ID);
			
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[10]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = cursor.getString(dbIndex[7]);
				dbValues[8] = cursor.getString(dbIndex[8]);
				dbValues[9] = cursor.getString(dbIndex[9]);
				dbValues[10] = String.valueOf(colId);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				stkCategory = new VanStkOpConstraints(dbValues);
				if(stocksArrList != null)
					stocksArrList.add(stkCategory);
				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllVanSTkEmpDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return stocksArrList;
    }//fn readAllVanSTkEmpDataFromDB
        
    public static ArrayList readAllVanSTkEmpSrlDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	VanStkSerOpConstraints serCategory = null;
    	ArrayList stocksSerArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[8];
    		String[] dbValues = new String[8];
    		int colId = -1;
    		
    		if(stocksSerArrList != null)
    			stocksSerArrList.clear();
    		
    		Uri uri = Uri.parse(VanStkOfflineContraintsCP.VAN_EMP_SRL_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			//ServiceProConstants.showLog("No of Category Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return stocksSerArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_BP_UNAME);
			dbIndex[1] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_MATNR);
			dbIndex[2] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_WERKS);
			dbIndex[3] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_LGORT);
			dbIndex[4] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_MAKTX);
			dbIndex[5] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_EQUNR);
			dbIndex[6] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_SERNR);
			dbIndex[7] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_ID);
			
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[7]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = String.valueOf(colId);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				serCategory = new VanStkSerOpConstraints(dbValues);
				if(stocksSerArrList != null)
					stocksSerArrList.add(serCategory);
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllVanSTkEmpSrlDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return stocksSerArrList;
    }//fn readAllVanSTkEmpSrlDataFromDB    
    
    public static ArrayList readAllVanSTkEmpSTODataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	VanStkTransOpConstraints stoCategory = null;
    	ArrayList stocksTransArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[27];
    		String[] dbValues = new String[27];
    		int colId = -1;
    		
    		if(stocksTransArrList != null)
    			stocksTransArrList.clear();
    		
    		Uri uri = Uri.parse(VanStkOfflineContraintsCP.VAN_EMP_STO_CONTENT_URI+"");
			
			ContentResolver resolver = ctx.getContentResolver();
			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);
			
			//ServiceProConstants.showLog("No of Category Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return stocksTransArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_EBELN);
			dbIndex[1] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_EBELP);
			dbIndex[2] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_BEDAT);
			dbIndex[3] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_LIFNR);
			dbIndex[4] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_SPRAS);
			dbIndex[5] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_ZTERM);
			dbIndex[6] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_EKORG);
			dbIndex[7] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_WAERS);
			dbIndex[8] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_INCO1);
			dbIndex[9] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_INCO2);
			dbIndex[10] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_RESWK);
			dbIndex[11] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_RESLO);
			dbIndex[12] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_MATNR);
			dbIndex[13] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_TXZ01);
			dbIndex[14] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_BUKRS);
			dbIndex[15] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_WERKS);
			dbIndex[16] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_LGORT);
			dbIndex[17] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_BEDNR);
			dbIndex[18] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_MATKL);
			dbIndex[19] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_PSTYP);
			dbIndex[20] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_MENGE);
			dbIndex[21] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_MEINS);
			dbIndex[22] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_NETPR);
			dbIndex[23] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_NETWR);
			dbIndex[24] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_WEMNG);
			dbIndex[25] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_WAMNG);
			dbIndex[26] = cursor.getColumnIndex(VanStkDBConstants.VAN_STO_COL_ID);
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[26]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = cursor.getString(dbIndex[7]);
				dbValues[8] = cursor.getString(dbIndex[8]);
				dbValues[9] = cursor.getString(dbIndex[9]);
				dbValues[10] = cursor.getString(dbIndex[10]);
				dbValues[11] = cursor.getString(dbIndex[11]);
				dbValues[12] = cursor.getString(dbIndex[12]);
				dbValues[13] = cursor.getString(dbIndex[13]);
				dbValues[14] = cursor.getString(dbIndex[14]);
				dbValues[15] = cursor.getString(dbIndex[15]);
				dbValues[16] = cursor.getString(dbIndex[16]);
				dbValues[17] = cursor.getString(dbIndex[17]);
				dbValues[18] = cursor.getString(dbIndex[18]);
				dbValues[19] = cursor.getString(dbIndex[19]);
				dbValues[20] = cursor.getString(dbIndex[20]);
				dbValues[21] = cursor.getString(dbIndex[21]);
				dbValues[22] = cursor.getString(dbIndex[22]);
				dbValues[23] = cursor.getString(dbIndex[23]);
				dbValues[24] = cursor.getString(dbIndex[24]);
				dbValues[25] = cursor.getString(dbIndex[25]);
				dbValues[26] = String.valueOf(colId);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				stoCategory = new VanStkTransOpConstraints(dbValues);
				if(stocksTransArrList != null)
					stocksTransArrList.add(stoCategory);
				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllVanSTkEmpSTODataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return stocksTransArrList;
    }//fn readAllVanSTkEmpSTODataFromDB
    
    
    public static ArrayList readAllVanSTkColDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	VanStkColOpConstraints colStkCategory = null;
    	ArrayList stocksCollArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[9];
    		String[] dbValues = new String[9];
    		int colId = -1;
    		
    		if(stocksCollArrList != null)
    			stocksCollArrList.clear();
    		
    		Uri uri = Uri.parse(VanStkOfflineContraintsCP.VAN_COL_CONTENT_URI+"");
			
			ContentResolver resolver = ctx.getContentResolver();
			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);
			
			//ServiceProConstants.showLog("No of Category Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return stocksCollArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_PARTNER);
			dbIndex[1] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_MC_NAME1);
			dbIndex[2] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_MC_NAME2);
			dbIndex[3] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_TEL_NO);
			dbIndex[4] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_TEL_NO2);
			dbIndex[5] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_UNAME);
			dbIndex[6] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_PLANT);
			dbIndex[7] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_COL_STORAGE_LOC);
			dbIndex[8] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_ID);
			
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[8]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = cursor.getString(dbIndex[7]);
				dbValues[8] = String.valueOf(colId);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				colStkCategory = new VanStkColOpConstraints(dbValues);
				if(colStkCategory != null)
					stocksCollArrList.add(colStkCategory);
				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllVanSTkColDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return stocksCollArrList;
    }//fn readAllVanSTkColDataFromDB
    
    
    public static ArrayList readAllVanSTkColSrlDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	VanStkColStrOpConstraints colSrlCategory = null;
    	ArrayList stocksCollStrArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		int colId = -1;
    		
    		if(stocksCollStrArrList != null)
    			stocksCollStrArrList.clear();
    		
    		Uri uri = Uri.parse(VanStkOfflineContraintsCP.VAN_COL_SRL_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			//ServiceProConstants.showLog("No of Category Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return stocksCollStrArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_SRL_COL_BP_UNAME);
			dbIndex[1] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_SRL_COL_WERKS);
			dbIndex[2] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_SRL_COL_LGORT);
			dbIndex[3] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_SRL_COL_LGOBE);
			dbIndex[4] = cursor.getColumnIndex(VanStkDBConstants.VAN_CLG_SRL_COL_ID);
			
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[4]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = String.valueOf(colId);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				colSrlCategory = new VanStkColStrOpConstraints(dbValues);
				if(stocksCollStrArrList != null)
					stocksCollStrArrList.add(colSrlCategory);
				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllVanSTkColSrlDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return stocksCollStrArrList;
    }//fn readAllVanSTkColSrlDataFromDB
    
    public static ArrayList readVanSTkCollDataFromDB(Context ctx, String UName){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	VanStkOpConstraints stkCategory = null;
    	ArrayList stocksArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[11];
    		String[] dbValues = new String[11];
    		int colId = -1;
    		
    		if(stocksArrList != null)
    			stocksArrList.clear();
    		
    		Uri uri = Uri.parse(VanStkOfflineContraintsCP.VAN_COL_STK_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();	
			selection = VanStkDBConstants.VAN_COL_BP_UNAME + " = ?"; 
			selectionParams = new String[]{UName};		
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			//ServiceProConstants.showLog("No of Category Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return stocksArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_BP_UNAME);
			dbIndex[1] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_MATNR);
			dbIndex[2] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_WERKS);
			dbIndex[3] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_LGORT);
			dbIndex[4] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_CHARG);
			dbIndex[5] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_MAKTX);
			dbIndex[6] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_MEINS);
			dbIndex[7] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_LABST);
			dbIndex[8] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_TRAME);
			dbIndex[9] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_ZZSERNR_EXIST);
			dbIndex[10] = cursor.getColumnIndex(VanStkDBConstants.VAN_COL_ID);
			
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[10]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = cursor.getString(dbIndex[7]);
				dbValues[8] = cursor.getString(dbIndex[8]);
				dbValues[9] = cursor.getString(dbIndex[9]);
				dbValues[10] = String.valueOf(colId);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				stkCategory = new VanStkOpConstraints(dbValues);
				if(stocksArrList != null)
					stocksArrList.add(stkCategory);
				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readVanSTkCollDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return stocksArrList;
    }//fn readVanSTkCollDataFromDB
        
    public static ArrayList readVanSTkCollSrlDataFromDB(Context ctx, String UName){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	VanStkSerOpConstraints serCategory = null;
    	ArrayList stocksSerArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[8];
    		String[] dbValues = new String[8];
    		int colId = -1;
    		
    		if(stocksSerArrList != null)
    			stocksSerArrList.clear();
    		
    		Uri uri = Uri.parse(VanStkOfflineContraintsCP.VAN_COL_STK_SRL_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();			
			selection = VanStkDBConstants.VAN_COL_BP_UNAME + " = ?"; 
			selectionParams = new String[]{UName};		
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			//ServiceProConstants.showLog("No of Category Records : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return stocksSerArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_BP_UNAME);
			dbIndex[1] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_MATNR);
			dbIndex[2] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_WERKS);
			dbIndex[3] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_LGORT);
			dbIndex[4] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_MAKTX);
			dbIndex[5] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_EQUNR);
			dbIndex[6] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_SERNR);
			dbIndex[7] = cursor.getColumnIndex(VanStkDBConstants.VAN_SER_COL_ID);
			
			cursor.moveToFirst();
			
			do{
				colId = cursor.getInt(dbIndex[7]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = String.valueOf(colId);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				serCategory = new VanStkSerOpConstraints(dbValues);
				if(stocksSerArrList != null)
					stocksSerArrList.add(serCategory);
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readVanSTkCollSrlDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return stocksSerArrList;
    }//fn readVanSTkCollSrlDataFromDB
    
    
    /*********************************************************************************************
     *     	Database insert Related Functions
     *********************************************************************************************/
    
    public static void insertVanSTkEmpDataInToDB(Context ctx, VanStkOpConstraints stkCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(VanStkDBConstants.VAN_COL_BP_UNAME, stkCategory.getBusinessPrtName().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_MATNR, stkCategory.getMaterial().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_WERKS, stkCategory.getWerks().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_LGORT, stkCategory.getStorageLoc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_CHARG, stkCategory.getBatchNo().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_MAKTX, stkCategory.getMattDesc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_MEINS, stkCategory.getMeasureUnits().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_LABST, stkCategory.getStockQty().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_TRAME, stkCategory.getMatQtyTransit().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_ZZSERNR_EXIST, stkCategory.getSerAvailable().toString().trim());
	    	resolver.insert(VanStkOfflineContraintsCP.VAN_EMP_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkEmpDataInToDB : "+sgh.toString());
    	}
    }//fn insertVanSTkEmpDataInToDB
    
    
    
    public static void insertVanSTkEmpSrlDataInToDB(Context ctx, VanStkSerOpConstraints serCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(VanStkDBConstants.VAN_SER_COL_BP_UNAME, serCategory.getBusinessPrtName().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_MATNR, serCategory.getMaterial().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_WERKS, serCategory.getWerks().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_LGORT, serCategory.getStorageLoc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_MAKTX, serCategory.getMattDesc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_EQUNR, serCategory.getEquipment().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_SERNR, serCategory.getSerialNo().toString().trim());
	    	resolver.insert(VanStkOfflineContraintsCP.VAN_EMP_SRL_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkEmpSrlDataInToDB : "+sgh.toString());
    	}
    }//fn insertVanSTkEmpSrlDataInToDB
    
    
    public static void insertVanSTkEmpSTODataInToDB(Context ctx, VanStkTransOpConstraints stoCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(VanStkDBConstants.VAN_STO_COL_EBELN, stoCategory.getSTO().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_EBELP, stoCategory.getItem().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_BEDAT, stoCategory.getSTODate().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_LIFNR, stoCategory.getVendor().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_SPRAS, stoCategory.getLanguage().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_ZTERM, stoCategory.getPymtTermsCode().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_EKORG, stoCategory.getPurchasingOrg().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_WAERS, stoCategory.getCurrency().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_INCO1, stoCategory.getTermPart1().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_INCO2, stoCategory.getTermPart2().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_RESWK, stoCategory.getSupplyPlant().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_RESLO, stoCategory.getSupplyStrLoc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_MATNR, stoCategory.getMaterial().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_TXZ01, stoCategory.getSTOItemDesc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_BUKRS, stoCategory.getCOCode().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_WERKS, stoCategory.getRecvPlant().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_LGORT, stoCategory.getRecvStrLoc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_BEDNR, stoCategory.getSTOTracking().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_MATKL, stoCategory.getMattGroup().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_PSTYP, stoCategory.getPOItemCat().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_MENGE, stoCategory.getPurchaseQty().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_MEINS, stoCategory.getPurchaseQtyUnit().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_NETPR, stoCategory.getPurchasePrice().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_NETWR, stoCategory.getPurchaseValue().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_WEMNG, stoCategory.getQtyGoodsRecd().toString().trim());
	    	val.put(VanStkDBConstants.VAN_STO_COL_WAMNG, stoCategory.getQtyGoodsIssd().toString().trim());
	    	resolver.insert(VanStkOfflineContraintsCP.VAN_EMP_STO_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkEmpSTODataInToDB : "+sgh.toString());
    	}
    }//fn insertVanSTkEmpSTODataInToDB
    
    
    public static void insertVanSTkColDataInToDB(Context ctx, VanStkColOpConstraints colCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(VanStkDBConstants.VAN_CLG_COL_PARTNER, colCategory.getPartnerId().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_COL_MC_NAME1, colCategory.getPartnerName1().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_COL_MC_NAME2, colCategory.getPartnerName2().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_COL_TEL_NO, colCategory.getTelephoneNo1().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_COL_TEL_NO2, colCategory.getTelephoneNo2().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_COL_UNAME, colCategory.getUName().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_COL_PLANT, colCategory.getPlant().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_COL_STORAGE_LOC, colCategory.getStorageLoc().toString().trim());
	    	resolver.insert(VanStkOfflineContraintsCP.VAN_COL_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkColDataInToDB : "+sgh.toString());
    	}
    }//fn insertVanSTkColDataInToDB
    
    
    public static void insertVanSTkColSrlDataInToDB(Context ctx, VanStkColStrOpConstraints colSerCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(VanStkDBConstants.VAN_SER_COL_BP_UNAME, colSerCategory.getEmpUsername().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_SRL_COL_WERKS, colSerCategory.getEmpPlant().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_SRL_COL_LGORT, colSerCategory.getEmpStorageLoc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_CLG_SRL_COL_LGOBE, colSerCategory.getStorageLocDesc().toString().trim());
	    	resolver.insert(VanStkOfflineContraintsCP.VAN_COL_SRL_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkColDataInToDB : "+sgh.toString());
    	}
    }//fn insertVanSTkColSrlDataInToDB
    
    
    public static void insertVanSTkColStockDataInToDB(Context ctx, VanStkOpConstraints stkCategory, String ColId){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(VanStkDBConstants.VAN_COL_BP_UNAME, stkCategory.getBusinessPrtName().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_MATNR, stkCategory.getMaterial().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_WERKS, stkCategory.getWerks().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_LGORT, stkCategory.getStorageLoc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_CHARG, stkCategory.getBatchNo().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_MAKTX, stkCategory.getMattDesc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_MEINS, stkCategory.getMeasureUnits().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_LABST, stkCategory.getStockQty().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_TRAME, stkCategory.getMatQtyTransit().toString().trim());
	    	val.put(VanStkDBConstants.VAN_COL_ZZSERNR_EXIST, stkCategory.getSerAvailable().toString().trim());
	    	val.put(VanStkDBConstants.VAN_EMP_COL_ID, ColId);
	    	resolver.insert(VanStkOfflineContraintsCP.VAN_COL_STK_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkEmpDataInToDB : "+sgh.toString());
    	}
    }//fn insertVanSTkEmpDataInToDB    
    
    
    public static void insertVanSTkColStockSrlDataInToDB(Context ctx, VanStkSerOpConstraints serCategory, String ColId){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(VanStkDBConstants.VAN_SER_COL_BP_UNAME, serCategory.getBusinessPrtName().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_MATNR, serCategory.getMaterial().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_WERKS, serCategory.getWerks().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_LGORT, serCategory.getStorageLoc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_MAKTX, serCategory.getMattDesc().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_EQUNR, serCategory.getEquipment().toString().trim());
	    	val.put(VanStkDBConstants.VAN_SER_COL_SERNR, serCategory.getSerialNo().toString().trim());
	    	val.put(VanStkDBConstants.VAN_EMP_COL_ID, ColId);
	    	resolver.insert(VanStkOfflineContraintsCP.VAN_COL_STK_SRL_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkEmpSrlDataInToDB : "+sgh.toString());
    	}
    }//fn insertVanSTkEmpSrlDataInToDB
    
    
    /*********************************************************************************************
     *     	Database deletion Related Functions
     *********************************************************************************************/
    //Delete all data from a selected table based on the specified Uri identifying a table
    public static void deleteAllTableDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();
				
				//Make the invocation. # of rows deleted will be sent back
				int rows = resolver.delete(uri, null, null);
				
				ServiceProConstants.showLog("Rows Deleted : "+rows);
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllCategoryDataFromDB
   
   
    public static void deleteAllExistingColleagueData(Context ctx, Uri selUri, String colId){
    	Uri uri = null;
		String whereStr = null;
		String[] whereParams = null;
    	try{
    		uri = Uri.parse(selUri.toString());
			
			//Get the Resolver
    		ContentResolver resolver = ctx.getContentResolver();
			
			whereStr = VanStkDBConstants.VAN_EMP_COL_ID + " = ? ";
			whereParams = new String[]{String.valueOf(colId)}; 
			
			//Make the invocation. # of rows deleted will be sent back
			int rows = resolver.delete(uri, whereStr, whereParams);
			
			ServiceProConstants.showLog("AlloldData Rows Deleted : "+rows);
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAlloldData : "+sggh.toString());
    	}
    }//fn deleteAllExistingColleagueData
    
}//End of class VanStkDBOperations