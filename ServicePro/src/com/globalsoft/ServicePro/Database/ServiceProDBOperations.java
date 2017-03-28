package com.globalsoft.ServicePro.Database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.globalsoft.ServicePro.ServiceProConstants;
import com.globalsoft.ServicePro.Constraints.EntitlementOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOCodeGroupOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOCodeListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOMattEmpListOpConstraints;
import com.globalsoft.ServicePro.Constraints.SOServiceActListOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceFollDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceOrdDocOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProAttachmentOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProColleaguesOpConstraints;
import com.globalsoft.ServicePro.Constraints.ServiceProOutputConstraints;
import com.globalsoft.ServicePro.Constraints.StatusFollowOpConstraints;
import com.globalsoft.ServicePro.Constraints.StatusOpConstraints;

public class ServiceProDBOperations {
	
	/*********************************************************************************************
     *     	Database Read Related Functions
     *********************************************************************************************/
    
    public static ArrayList readAllCollTasksDataFromDB(Context ctx, String collId){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceProOutputConstraints category = null;
    	ArrayList taskArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[50];
    		String[] dbValues = new String[50];
    		int colId = -1;
    		
    		if(taskArrList != null)
    			taskArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_COLL_TASKS_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_COLL_ID + " = ?"; 
			selectionParams = new String[]{collId};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for CollTasks: "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return taskArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_PROCESS_TYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_NUMBER_EXT);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZKEYDATE);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_PARTNER);
			dbIndex[5] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_NAME_ORG1);
			dbIndex[6] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_NAME_ORG2);
			dbIndex[7] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_STRAS);
			dbIndex[8] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_ORT01);
			dbIndex[9] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_REGIO);
			dbIndex[10] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_PSTLZ);
			dbIndex[11] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_LAND1);
			dbIndex[12] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS);
			dbIndex[13] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS_TXT30);
			dbIndex[14] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS_REASON);
			dbIndex[15] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_PARTNER);
			dbIndex[16] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_NAME1_TEXT);
			dbIndex[17] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_TEL_NO);
			dbIndex[18] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_TEL_NO2);
			dbIndex[19] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_PARTNER);
			dbIndex[20] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_NAME1_TEXT);
			dbIndex[21] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_TEL_NO);
			dbIndex[22] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_TEL_NO2);
			dbIndex[23] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_DESCRIPTION);
			dbIndex[24] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_PRIORITY);
			dbIndex[25] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_IBASE);
			dbIndex[26] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_INSTANCE);
			dbIndex[27] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_SERIAL_NUMBER);
			dbIndex[28] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_REFOBJ_PRODUCT_ID);
			dbIndex[29] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_DESCR);
			dbIndex[30] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_INST_DESCR);
			dbIndex[31] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_REFOBJ_PRODUCT_DESCR);
			dbIndex[32] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_TIMEZONE_FROM);
			dbIndex[33] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZETADATE);
			dbIndex[34] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZETATIME);
			dbIndex[35] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_PROCESS_TYPE_DESCR);
			dbIndex[36] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_NOTE);
			dbIndex[37] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCT);
			dbIndex[38] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCTDESCR);
			dbIndex[39] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COL_FIELD_NOTES);
			
			cursor.moveToFirst();
			
			do{
				//colId = cursor.getInt(dbIndex[3]);
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
				dbValues[26] = cursor.getString(dbIndex[26]);
				dbValues[27] = cursor.getString(dbIndex[27]);
				dbValues[28] = cursor.getString(dbIndex[28]);
				dbValues[29] = cursor.getString(dbIndex[29]);
				dbValues[30] = cursor.getString(dbIndex[30]);
				dbValues[31] = cursor.getString(dbIndex[31]);
				dbValues[32] = cursor.getString(dbIndex[32]);
				dbValues[33] = cursor.getString(dbIndex[33]);
				dbValues[34] = cursor.getString(dbIndex[34]);
				dbValues[35] = cursor.getString(dbIndex[35]);
				dbValues[36] = cursor.getString(dbIndex[36]);
				dbValues[37] = cursor.getString(dbIndex[37]);			
				dbValues[38] = cursor.getString(dbIndex[38]);
				dbValues[39] = cursor.getString(dbIndex[39]);
				dbValues[40] = collId;
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				category = new ServiceProOutputConstraints(dbValues);
				if(category != null)
					taskArrList.add(category);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllCollTasksDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return taskArrList;
    }//fn readAllCollTasksDataFromDB
    
    public static ArrayList readAllConfListDataFromDB(Context ctx, String idVal){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceOrdDocOpConstraints docsCategory = null;
    	ArrayList confListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[50];
    		String[] dbValues = new String[50];
    		int colId = -1;
    		
    		if(confListArrList != null)
    			confListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID + " = ?"; 
			selectionParams = new String[]{idVal};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_TYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZKEYDATE);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PARTNER);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG1);
			dbIndex[5] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG2);
			dbIndex[6] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NICKNAME);
			dbIndex[7] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STRAS);
			dbIndex[8] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ORT01);
			dbIndex[9] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REGIO);
			dbIndex[10] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PSTLZ);
			dbIndex[11] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_LAND1);
			dbIndex[12] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS);
			dbIndex[13] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_TXT30);
			dbIndex[14] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_REASON);
			dbIndex[15] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_PARTNER);
			dbIndex[16] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_NAME1_TEXT);
			dbIndex[17] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO);
			dbIndex[18] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO2);
			dbIndex[19] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_PARTNER);
			dbIndex[20] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_NAME1_TEXT);
			dbIndex[21] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO);
			dbIndex[22] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO2);
			dbIndex[23] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_DESCRIPTION);
			dbIndex[24] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRIORITY);
			dbIndex[25] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_IBASE);
			dbIndex[26] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INSTANCE);
			dbIndex[27] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_SERIAL_NUMBER);
			dbIndex[28] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_ID);
			dbIndex[29] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_DESCR);
			dbIndex[30] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INST_DESCR);
			dbIndex[31] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_DESCR);
			dbIndex[32] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_TIMEZONE_FROM);
			dbIndex[33] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NUMBER_EXT);
			dbIndex[34] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRODUCT_ID);
			dbIndex[35] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_QUANTITY);
			dbIndex[36] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_QTY_UNIT);
			dbIndex[37] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_DESCRIPTION);
			dbIndex[38] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_TEXT);
			/*dbIndex[38] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_FIELD_NOTES);
			dbIndex[39] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ITEM_NUMBER);*/
			cursor.moveToFirst();
			
			do{
				//colId = cursor.getInt(dbIndex[3]);
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
				dbValues[26] = cursor.getString(dbIndex[26]);
				dbValues[27] = cursor.getString(dbIndex[27]);
				dbValues[28] = cursor.getString(dbIndex[28]);
				dbValues[29] = cursor.getString(dbIndex[29]);
				dbValues[30] = cursor.getString(dbIndex[30]);
				dbValues[31] = cursor.getString(dbIndex[31]);
				dbValues[32] = cursor.getString(dbIndex[32]);
				dbValues[33] = cursor.getString(dbIndex[33]);
				dbValues[34] = cursor.getString(dbIndex[34]);
				dbValues[35] = cursor.getString(dbIndex[35]);
				dbValues[36] = cursor.getString(dbIndex[36]);
				dbValues[37] = cursor.getString(dbIndex[37]);
				dbValues[38] = cursor.getString(dbIndex[38]);
				/*dbValues[38] = cursor.getString(dbIndex[38]);
				dbValues[39] = cursor.getString(dbIndex[39]);*/
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				docsCategory = new ServiceOrdDocOpConstraints(dbValues);
				if(docsCategory != null)
					confListArrList.add(docsCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confListArrList;
    }//fn readAllConfListDataFromDB
    
    public static ArrayList readAllConfSpareListDataFromDB(Context ctx, String idVal){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceOrdDocOpConstraints docsCategory = null;
    	ArrayList confSpareListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[50];
    		String[] dbValues = new String[50];
    		int colId = -1;
    		
    		if(confSpareListArrList != null)
    			confSpareListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_OBJECT_ID + " = ?"; 
			selectionParams = new String[]{idVal};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfSpareList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confSpareListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_TYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZKEYDATE);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PARTNER);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG1);
			dbIndex[5] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG2);
			dbIndex[6] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NICKNAME);
			dbIndex[7] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STRAS);
			dbIndex[8] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ORT01);
			dbIndex[9] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REGIO);
			dbIndex[10] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PSTLZ);
			dbIndex[11] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_LAND1);
			dbIndex[12] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS);
			dbIndex[13] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_TXT30);
			dbIndex[14] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_REASON);
			dbIndex[15] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_PARTNER);
			dbIndex[16] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_NAME1_TEXT);
			dbIndex[17] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO);
			dbIndex[18] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO2);
			dbIndex[19] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_PARTNER);
			dbIndex[20] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_NAME1_TEXT);
			dbIndex[21] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO);
			dbIndex[22] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO2);
			dbIndex[23] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_DESCRIPTION);
			dbIndex[24] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRIORITY);
			dbIndex[25] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_IBASE);
			dbIndex[26] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INSTANCE);
			dbIndex[27] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_SERIAL_NUMBER);
			dbIndex[28] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_ID);
			dbIndex[29] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_DESCR);
			dbIndex[30] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INST_DESCR);
			dbIndex[31] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_DESCR);
			dbIndex[32] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_TIMEZONE_FROM);
			dbIndex[33] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NUMBER_EXT);
			dbIndex[34] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRODUCT_ID);
			dbIndex[35] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_QUANTITY);
			dbIndex[36] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_QTY_UNIT);
			dbIndex[37] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_DESCRIPTION);
			dbIndex[38] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_TEXT);
			cursor.moveToFirst();
			
			do{
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
				dbValues[26] = cursor.getString(dbIndex[26]);
				dbValues[27] = cursor.getString(dbIndex[27]);
				dbValues[28] = cursor.getString(dbIndex[28]);
				dbValues[29] = cursor.getString(dbIndex[29]);
				dbValues[30] = cursor.getString(dbIndex[30]);
				dbValues[31] = cursor.getString(dbIndex[31]);
				dbValues[32] = cursor.getString(dbIndex[32]);
				dbValues[33] = cursor.getString(dbIndex[33]);
				dbValues[34] = cursor.getString(dbIndex[34]);
				dbValues[35] = cursor.getString(dbIndex[35]);
				dbValues[36] = cursor.getString(dbIndex[36]);
				dbValues[37] = cursor.getString(dbIndex[37]);
				dbValues[38] = cursor.getString(dbIndex[38]);
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				docsCategory = new ServiceOrdDocOpConstraints(dbValues);
				if(docsCategory != null)
					confSpareListArrList.add(docsCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfSpareListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confSpareListArrList;
    }//fn readAllConfSpareListDataFromDB

    public static ArrayList readAllConfCollecListDataFromDB(Context ctx, String idVal){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceFollDocOpConstraints confirmMainCategory = null;
    	ArrayList confCollecListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confCollecListArrList != null)
    			confCollecListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_OBJECT_ID + " = ?"; 
			selectionParams = new String[]{idVal};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfCollecList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confCollecListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_PROCESS_TYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_NUMBER_EXT);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ERDAT);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_STATUS);
			dbIndex[5] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_TXT30);
			dbIndex[6] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_OBJECT_ID);
			dbIndex[7] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_PROCESS_TYPE);
			dbIndex[8] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_NUMBER_EXT);
			cursor.moveToFirst();
			
			do{
				//colId = cursor.getInt(dbIndex[3]);
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = cursor.getString(dbIndex[7]);
				dbValues[8] = cursor.getString(dbIndex[8]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				confirmMainCategory = new ServiceFollDocOpConstraints(dbValues);
				if(confirmMainCategory != null)
					confCollecListArrList.add(confirmMainCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfCollecListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confCollecListArrList;
    }//fn readAllConfCollecListDataFromDB

    public static ArrayList readAllConfProductListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOServiceActListOpConstraints serActListObj = null;
    	ArrayList confProductListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[2];
    		String[] dbValues = new String[2];
    		int colId = -1;
    		
    		if(confProductListArrList != null)
    			confProductListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFPRODUCTLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfProductList : "+cursor.getCount());
						
			if(cursor.getCount() == 0){
				return confProductListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_PRODUCT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_SHORT_TEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				serActListObj = new SOServiceActListOpConstraints(dbValues);
				if(serActListObj != null)
					confProductListArrList.add(serActListObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfProductListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confProductListArrList;
    }//fn readAllConfProductListDataFromDB

    public static ArrayList readAllConfCauseGroupListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOCodeGroupOpConstraints codeGroupObj = null;
    	ArrayList confCauseGroupListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confCauseGroupListArrList != null)
    			confCauseGroupListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfCauseGroupList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confCauseGroupListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_CODEGRUPPE);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_KURZTEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				codeGroupObj = new SOCodeGroupOpConstraints(dbValues);
				if(codeGroupObj != null)
					confCauseGroupListArrList.add(codeGroupObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfCauseGroupListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confCauseGroupListArrList;
    }//fn readAllConfCauseGroupListDataFromDB

    public static ArrayList readAllConfCauseCodeListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOCodeListOpConstraints codeListObj = null;
    	ArrayList confCauseCodeListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confCauseCodeListArrList != null)
    			confCauseCodeListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFCAUSECODELIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfCauseCodeList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confCauseCodeListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_CODEGRUPPE);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_CODE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_KURZTEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				codeListObj = new SOCodeListOpConstraints(dbValues);
				if(codeListObj != null)
					confCauseCodeListArrList.add(codeListObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfCauseCodeListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confCauseCodeListArrList;
    }//fn readAllConfCauseCodeListDataFromDB

    public static ArrayList readAllConfSympGroupListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOCodeGroupOpConstraints codeGroupObj = null;
    	ArrayList confSympGroupListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confSympGroupListArrList != null)
    			confSympGroupListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFSYMPGROUPLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfSympGroupList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confSympGroupListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_CODEGRUPPE);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_KURZTEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				codeGroupObj = new SOCodeGroupOpConstraints(dbValues);
				if(codeGroupObj != null)
					confSympGroupListArrList.add(codeGroupObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfSympGroupListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confSympGroupListArrList;
    }//fn readAllConfSympGroupListDataFromDB

    public static ArrayList readAllConfSympCodeListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOCodeListOpConstraints codeListObj = null;
    	ArrayList confSympCodeListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confSympCodeListArrList != null)
    			confSympCodeListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFSYMPCODELIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfSympCodeList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confSympCodeListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_CODEGRUPPE);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_CODE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_KURZTEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				codeListObj = new SOCodeListOpConstraints(dbValues);
				if(codeListObj != null)
					confSympCodeListArrList.add(codeListObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfSympCodeListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confSympCodeListArrList;
    }//fn readAllConfSympCodeListDataFromDB

    public static ArrayList readAllConfProbGroupListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOCodeGroupOpConstraints codeGroupObj = null;
    	ArrayList confProbGroupListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confProbGroupListArrList != null)
    			confProbGroupListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFPROBGROUPLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfProbGroupList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confProbGroupListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_CODEGRUPPE);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_KURZTEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				codeGroupObj = new SOCodeGroupOpConstraints(dbValues);
				if(codeGroupObj != null)
					confProbGroupListArrList.add(codeGroupObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfProbGroupListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confProbGroupListArrList;
    }//fn readAllConfProbGroupListDataFromDB

    public static ArrayList readAllConfProbCodeListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOCodeListOpConstraints codeListObj = null;
    	ArrayList confProbCodeListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confProbCodeListArrList != null)
    			confProbCodeListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFPROBCODELIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfProbCodeList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confProbCodeListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_CODEGRUPPE);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_CODE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_KURZTEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				codeListObj = new SOCodeListOpConstraints(dbValues);
				if(codeListObj != null)
					confProbCodeListArrList.add(codeListObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfProbCodeListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confProbCodeListArrList;
    }//fn readAllConfProbCodeListDataFromDB

    public static ArrayList readAllConfMattEmpListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	SOMattEmpListOpConstraints mattEmpObj = null;
    	ArrayList confMattEmpListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(confMattEmpListArrList != null)
    			confMattEmpListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFMATTEMPLLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfMattEmpList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confMattEmpListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_BP_UNAME);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MATNR);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MAKTX_INSYLANGU);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_VRKME);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MEINS);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				mattEmpObj = new SOMattEmpListOpConstraints(dbValues);
				if(mattEmpObj != null)
					confMattEmpListArrList.add(mattEmpObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfMattEmpListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confMattEmpListArrList;
    }//fn readAllConfMattEmpListDataFromDB

    public static ArrayList readAllColleagueListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceProColleaguesOpConstraints collCategory = null;
    	ArrayList colleagueListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[10];
    		String[] dbValues = new String[10];
    		int colId = -1;
    		
    		if(colleagueListArrList != null)
    			colleagueListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_COLLEAGUELIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfCollecList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return colleagueListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_PARTNER);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_MC_NAME1);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_MC_NAME2);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_TEL_NO);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_TEL_NO2);
			dbIndex[5] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_UNAME);
			dbIndex[6] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_PLANT);
			dbIndex[7] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_STORAGE_LOC);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
				dbValues[5] = cursor.getString(dbIndex[5]);
				dbValues[6] = cursor.getString(dbIndex[6]);
				dbValues[7] = cursor.getString(dbIndex[7]);
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				collCategory = new ServiceProColleaguesOpConstraints(dbValues);
				if(collCategory != null)
					colleagueListArrList.add(collCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllColleagueListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return colleagueListArrList;
    }//fn readAllColleagueListDataFromDB
    
    public static ArrayList readAllEntitlementListDataFromDB(Context ctx, String idVal){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	EntitlementOpConstraints repCategory = null;
    	ArrayList entitlementListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[50];
    		String[] dbValues = new String[50];
    		int colId = -1;
    		
    		if(entitlementListArrList != null)
    			entitlementListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ENTITLEMENTLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ID + " = ?"; 
			selectionParams = new String[]{idVal};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return entitlementListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ORDER_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ITEM_ID);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ITEM_GUID);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROCESS_TYPE);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROFILE);
			dbIndex[5] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROF_DESC);
			dbIndex[6] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ENT_TYPE);
			dbIndex[7] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ENT_DESC);
			dbIndex[8] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_LABOUR);
			dbIndex[9] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_PARTS);
			dbIndex[10] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_TRAVEL);
			dbIndex[11] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_FREQUENCY);
			dbIndex[12] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_TIMEPERIOD);
			cursor.moveToFirst();
			
			do{
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
								
				repCategory = new EntitlementOpConstraints(dbValues);
				if(repCategory != null)
					entitlementListArrList.add(repCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllEntitlementListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return entitlementListArrList;
    }//fn readAllEntitlementListDataFromDB
    
    public static ArrayList readAllStatusDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	StatusOpConstraints serStatusListObj = null;
    	ArrayList statusListObj = new ArrayList();
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		
    		if(statusListObj != null)
    			statusListObj.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for StatusList : "+cursor.getCount());
						
			if(cursor.getCount() == 0){
				return statusListObj;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_STATUS);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_TXT30);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_ZZSTATUS_ICON);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_ZZSTATUS_POSTSETACTION);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
								
				serStatusListObj = new StatusOpConstraints(dbValues);
				if(serStatusListObj != null)
					statusListObj.add(serStatusListObj);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllStatusDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return statusListObj;
    }//fn readAllStatusDataFromDB
    
    public static ArrayList readAllStatusListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ArrayList statusListObj = new ArrayList();
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		
    		if(statusListObj != null)
    			statusListObj.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for StatusList : "+cursor.getCount());
						
			if(cursor.getCount() == 0){
				return statusListObj;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_TXT30);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
								
				if(dbValues[0] != null)
					statusListObj.add(dbValues[0]);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllStatusListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return statusListObj;
    }//fn readAllStatusListDataFromDB
    
    public static String readStatusDescValueFromDB(Context ctx, String status){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	String statusDescObj = null;
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_STATUS_COL_STATUS + " = ?"; 
			selectionParams = new String[]{status};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for StatusList : "+cursor.getCount());
						
			if(cursor.getCount() == 0){
				return statusDescObj;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_TXT30);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
								
				if(dbValues[0] != null)
					statusDescObj = dbValues[0].toString().trim();				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readStatusDescValueFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return statusDescObj;
    }//fn readStatusDescValueFromDB

    public static String readStatusSAPValueDataFromDB(Context ctx, String stausDesc){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	String status = null;
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_STATUS_COL_TXT30 + " = ?"; 
			selectionParams = new String[]{stausDesc};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);
		
			ServiceProConstants.showLog("No of Records for StatusList : "+cursor.getCount());
						
			if(cursor.getCount() == 0){
				return status;
			}else{
				
				if (cursor.moveToFirst())
				{
					int index0 = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_STATUS);
					status = cursor.getString(index0);
				     /*do {
				    	 int index0 = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_STATUS);
				    	 status = cursor.getString(index0);	
				     } while (cursor.moveToNext());*/
				}
			
				/*dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_COL_STATUS);
				dbValues[0] = cursor.getString(dbIndex[0]);
				status = dbValues[0];*/
			}			
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readStatusSAPValueDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return status;
    }//fn readStatusSAPValueDataFromDB

    //METHOD TO GET ALL ITEMNUMBER OF SPEACIFIC OBJECT ID
    public static ArrayList readAllItemDataForNavigation(Context ctx, String objid){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceOrdDocOpConstraints docsCategory = null;
    	ArrayList<String> itemList = new ArrayList<String>();
    	String idStr = " ";
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		
    		if(itemList != null)
    			itemList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID + " = ?"; 
			selectionParams = new String[]{objid};	
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for item List : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return itemList;
			}			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NUMBER_EXT);		
			cursor.moveToFirst();
			
			do{
				idStr = cursor.getString(dbIndex[0]);																
					itemList.add(idStr);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllItemDataForNavigation : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return itemList;
    }//fn readAllAttachListDataFromDB
    
    public static ArrayList readAllAttachListDataFromDB(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceProAttachmentOpConstraints attachCategory = null;
    	ArrayList attachList = new ArrayList();
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		
    		if(attachList != null)
    			attachList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for Attach List : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return attachList;
			}

			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_TYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_NUMBER_EXT);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_ID);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_CNTNT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
								
				attachCategory = new ServiceProAttachmentOpConstraints(dbValues);
				if(attachCategory != null)
					attachList.add(attachCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllAttachListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return attachList;
    }//fn readAllAttachListDataFromDB
    
    public static ArrayList readCorrespondingFollowStatusListDataFromDB(Context ctx, String status){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ArrayList statusFollowListObj = new ArrayList();
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		
    		if(statusFollowListObj != null)
    			statusFollowListObj.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_STATUSFOLLOWLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STATUS + " = ? ";
			selectionParams = new String[]{status};
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for StatusList : "+cursor.getCount());
						
			if(cursor.getCount() == 0){
				return statusFollowListObj;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STATUS_NEXT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
								
				if(dbValues[0] != null)
					statusFollowListObj.add(dbValues[0]);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readCorrespondingFollowStatusListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return statusFollowListObj;
    }//fn readCorrespondingFollowStatusListDataFromDB
    
    public static ArrayList readAttachListDataFromDB(Context ctx, String id, String numberExt){
    	Cursor cursor = null;
    	String selection = null; 
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceProAttachmentOpConstraints attachCategory = null;
    	ArrayList attachList = new ArrayList();
    	try{
    		int[] dbIndex = new int[5];
    		String[] dbValues = new String[5];
    		
    		if(attachList != null)
    			attachList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			
			//selection = ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_ID + " = ? ";
			//selectionParams = new String[]{id};
			selection = ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_ID + " = ? AND (" + ServiceProDBConstants.SERPRO_ATTACH_COL_NUMBER_EXT + " = ? OR " + ServiceProDBConstants.SERPRO_ATTACH_COL_NUMBER_EXT + " = ? )";
			selectionParams = new String[]{id, numberExt, ""};
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for Attach List for particular task: "+cursor.getCount());
			
			ServiceProConstants.showLog("id : "+id+"  "+numberExt);
			
			if(cursor.getCount() == 0){
				return attachList;
			}

			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_TYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_NUMBER_EXT);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_ID);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_CNTNT);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				dbValues[4] = cursor.getString(dbIndex[4]);
								
				attachCategory = new ServiceProAttachmentOpConstraints(dbValues);
				if(attachCategory != null)
					attachList.add(attachCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
			
			ServiceProConstants.showLog("No of Records for attach array list: "+attachList.size());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAttachListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return attachList;
    }//fn readAttachListDataFromDB
    
    public static ArrayList readAllConfListWithItemDataFromDB(Context ctx, String idVal, String itemnoStr){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	ServiceOrdDocOpConstraints docsCategory = null;
    	ArrayList confListArrList = new ArrayList();
    	try{
    		int[] dbIndex = new int[50];
    		String[] dbValues = new String[50];
    		int colId = -1;
    		
    		if(confListArrList != null)
    			confListArrList.clear();
    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID + " = ? AND "+ServiceProDBConstants.SERPRO_CONFLIST_COL_NUMBER_EXT + " = ?"; 
			selectionParams = new String[]{idVal,itemnoStr};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for ConfList : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return confListArrList;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_TYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZKEYDATE);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PARTNER);
			dbIndex[4] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG1);
			dbIndex[5] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG2);
			dbIndex[6] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STRAS);
			dbIndex[7] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ORT01);
			dbIndex[8] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REGIO);
			dbIndex[9] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PSTLZ);
			dbIndex[10] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_LAND1);
			dbIndex[11] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS);
			dbIndex[12] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_TXT30);
			dbIndex[13] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_REASON);
			dbIndex[14] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_PARTNER);
			dbIndex[15] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_NAME1_TEXT);
			dbIndex[16] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO);
			dbIndex[17] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO2);
			dbIndex[18] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_PARTNER);
			dbIndex[19] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_NAME1_TEXT);
			dbIndex[20] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO);
			dbIndex[21] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO2);
			dbIndex[22] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_DESCRIPTION);
			dbIndex[23] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRIORITY);
			dbIndex[24] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_IBASE);
			dbIndex[25] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INSTANCE);
			dbIndex[26] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_SERIAL_NUMBER);
			dbIndex[27] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_ID);
			dbIndex[28] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_DESCR);
			dbIndex[29] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INST_DESCR);
			dbIndex[30] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_DESCR);
			dbIndex[31] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_TIMEZONE_FROM);
			dbIndex[32] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_NUMBER_EXT);
			dbIndex[33] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRODUCT_ID);
			dbIndex[34] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_QUANTITY);
			dbIndex[35] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_QTY_UNIT);
			dbIndex[36] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_DESCRIPTION);
			dbIndex[37] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_TEXT);
			/*dbIndex[38] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_FIELD_NOTES);
			dbIndex[39] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_CONFLIST_COL_ITEM_NUMBER);*/
			cursor.moveToFirst();
			
			do{
				//colId = cursor.getInt(dbIndex[3]);
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
				dbValues[26] = cursor.getString(dbIndex[26]);
				dbValues[27] = cursor.getString(dbIndex[27]);
				dbValues[28] = cursor.getString(dbIndex[28]);
				dbValues[29] = cursor.getString(dbIndex[29]);
				dbValues[30] = cursor.getString(dbIndex[30]);
				dbValues[31] = cursor.getString(dbIndex[31]);
				dbValues[32] = cursor.getString(dbIndex[32]);
				dbValues[33] = cursor.getString(dbIndex[33]);
				dbValues[34] = cursor.getString(dbIndex[34]);
				dbValues[35] = cursor.getString(dbIndex[35]);
				dbValues[36] = cursor.getString(dbIndex[36]);
				dbValues[37] = cursor.getString(dbIndex[37]);
				/*dbValues[38] = cursor.getString(dbIndex[38]);
				dbValues[39] = cursor.getString(dbIndex[39]);*/
				
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				
				docsCategory = new ServiceOrdDocOpConstraints(dbValues);
				if(docsCategory != null)
					confListArrList.add(docsCategory);				
				
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in readAllConfListDataFromDB : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return confListArrList;
    }//fn readAllConfListDataFromDB
    
    /*public static int checkTableRow(Context ctx){
    	Cursor cursor = null;
    	String selection = null;
    	String[] selectionParams = null;
    	String orderBy = null;
    	int rowcount = 0;
    	try{    		    		
    		Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ERRORLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for error list : "+cursor.getCount());
			rowcount = cursor.getCount();
    	}
    	catch(Exception sfg){
    		ServiceProConstants.showErrorLog("Error in checkTableRow : "+sfg.toString());
    	}
    	finally{
			if(cursor != null)
				cursor.close();		
    	}
    	return rowcount;
    }//fn checkTableRow
    
	public static ArrayList checkErrorTrancIdApiExists(Context ctx){
		Cursor cursor = null;
		String selection = null;
		String[] selectionParams = null;
		String orderBy = null;
		ArrayList errorTaskIdForStatus = new ArrayList();
		try{
			int[] dbIndex = new int[10];
			String[] dbValues = new String[10];
			int colId = -1;
			
			if(errorTaskIdForStatus != null)
				errorTaskIdForStatus.clear();
			
			Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ERRORLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID + " = ?"; 
			selectionParams = new String[]{ServiceProConstants.STATUS_UPDATE_API};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for error trancIdApiExists : "+cursor.getCount());
			
			if(cursor.getCount() == 0){
				return errorTaskIdForStatus;
			}
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRTYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRDESC);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				//ServiceProConstants.showLog("Id : "+colId+" : "+dbValues[0]+" : "+dbValues[1]);
				errorTaskIdForStatus.add(dbValues[0]);	
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in checkErrorTrancIdApiExists : "+sfg.toString());
		}
		finally{
			if(cursor != null)
				cursor.close();		
		}
		return errorTaskIdForStatus;
	}//fn checkErrorTrancIdApiExists
    
	public static boolean checkTrancIdApiExists(Context ctx, String id, String apiName){
		Cursor cursor = null;
		String selection = null;
		String[] selectionParams = null;
		String orderBy = null;
		boolean idexists = false;
		try{
			int[] dbIndex = new int[10];
			String[] dbValues = new String[10];
			int colId = -1;
						
			Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ERRORLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID + " = ? AND " + ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME + " = ?";
			selectionParams = new String[]{id, apiName};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for error trancIdApiExists : "+cursor.getCount());
						
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRTYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRDESC);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				
				String tranc_no = dbValues[0];
				String apiNameStr = dbValues[3];
				if(id.equals(tranc_no) && apiName.equals(apiNameStr)){
					idexists = true;
					return idexists;
				}	     		 
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in checkTrancIdApiExists : "+sfg.toString());
		}
		finally{
			if(cursor != null)
				cursor.close();		
		}
		return idexists;
	}//fn checkTrancIdApiExists
	
	public static String getErrorMsg(Context ctx, String id, String apiName){
		Cursor cursor = null;
		String selection = null;
		String[] selectionParams = null;
		String orderBy = null;
		String msg = "";
		try{
			int[] dbIndex = new int[10];
			String[] dbValues = new String[10];
			int colId = -1;
						
			Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ERRORLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			selection = ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID + " = ? AND " + ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME + " = ?";
			selectionParams = new String[]{id, apiName};			
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for error trancIdApiExists : "+cursor.getCount());
						
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRTYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRDESC);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				
				String tranc_no = dbValues[0];
				String apiNameStr = dbValues[3];
				if(id.equals(tranc_no) && apiName.equals(apiNameStr)){
					 String errorMsg = dbValues[2];
					 if(errorMsg != null && errorMsg.length() > 0){
						 msg = errorMsg;
						 return msg;
					 }
	     		 }	     		 
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in getErrorMsg : "+sfg.toString());
		}
		finally{
			if(cursor != null)
				cursor.close();		
		}
		return msg;
	}//fn getErrorMsg
	
	public static String[] getIdList(Context ctx){
		Cursor cursor = null;
		String selection = null;
		String[] selectionParams = null;
		String orderBy = null;
		String[] resArray = null;
		try{
			int[] dbIndex = new int[10];
			String[] dbValues = new String[10];
						
			Uri uri = Uri.parse(ServiceProOfflineContraintsCP.SERPRO_ERRORLIST_CONTENT_URI+"");			
			ContentResolver resolver = ctx.getContentResolver();
			cursor = resolver.query(uri, null, selection, selectionParams, orderBy);			
			ServiceProConstants.showLog("No of Records for error getIdList : "+cursor.getCount());
			resArray = new String[cursor.getCount()];
			int i=0;
			
			dbIndex[0] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID);
			dbIndex[1] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRTYPE);
			dbIndex[2] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRDESC);
			dbIndex[3] = cursor.getColumnIndex(ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME);
			cursor.moveToFirst();
			
			do{
				dbValues[0] = cursor.getString(dbIndex[0]);
				dbValues[1] = cursor.getString(dbIndex[1]);
				dbValues[2] = cursor.getString(dbIndex[2]);
				dbValues[3] = cursor.getString(dbIndex[3]);
				
				String tranc_no = dbValues[0];     		 
	     		resArray[i] = tranc_no;		     		 
	     		i++; 		 
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		catch(Exception sfg){
			ServiceProConstants.showErrorLog("Error in getErrorMsg : "+sfg.toString());
		}
		finally{
			if(cursor != null)
				cursor.close();		
		}
		return resArray;
	}//fn getErrorMsg
*/    
    /*********************************************************************************************
     *     	Database insert Related Functions
     *********************************************************************************************/
      
    public static void insertCollTasksDataInToDB(Context ctx, ServiceProOutputConstraints category, String collId){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_OBJECT_ID, category.getObjectId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_PROCESS_TYPE, category.getProcessType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZKEYDATE, category.getZZKEeyDate().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_PARTNER, category.getPartner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_NAME_ORG1, category.getNameOrg1().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_NAME_ORG2, category.getNameOrg2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_STRAS, category.getStreet().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_ORT01, category.getCity().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_REGIO, category.getRegion().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_PSTLZ, category.getPostalCode1().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_LAND1, category.getCountryIso().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS, category.getStatus().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS_TXT30, category.getTxt30().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_STATUS_REASON, category.getStatusReason().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_PARTNER, category.getCp1Partner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_NAME1_TEXT, category.getCp1Name().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_TEL_NO, category.getCp1TelNo().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP1_TEL_NO2, category.getCp1TelNo2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_PARTNER, category.getCp2Partner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_NAME1_TEXT, category.getCp2Name().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_TEL_NO, category.getCp2TelNo().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_CP2_TEL_NO2, category.getCp2TelNo2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_DESCRIPTION, category.getDescription().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_PRIORITY, category.getPriority().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_IBASE, category.getIB_Base().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_INSTANCE, category.getIB_Instance().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_SERIAL_NUMBER, category.getSerialNumber().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_REFOBJ_PRODUCT_ID, category.getRefObjProductId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_DESCR, category.getIB_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_IB_INST_DESCR, category.getIB_Inst_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_REFOBJ_PRODUCT_DESCR, category.getRefObj_Product_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_TIMEZONE_FROM, category.getTimeZone().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZETADATE, category.getZzetaDate().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZETATIME, category.getZzetaTime().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_PROCESS_TYPE_DESCR, category.getProcessTypeDescr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_NOTE, category.getNote().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCT, category.getZzFirstServiceProduct().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLTASK_COL_ZZFIRSTSERVICEPRODUCTDESCR, category.getZzFirstServiceProductDescr().toString().trim());
	    	/*val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_FIELD_NOTES, category.getNote().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_ITEM_NUMBER, category.getNumberExtn().toString().trim());*/
	    	val.put(ServiceProDBConstants.SERPRO_COLL_ID, collId.toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_COLL_TASKS_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertVanSTkEmpSTODataInToDB : "+sgh.toString());
    	}
    }//fn insertCollTasksDataInToDB
    
    public static void insertConfListDataInToDB(Context ctx, ServiceOrdDocOpConstraints docsCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID, docsCategory.getObjectId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_TYPE, docsCategory.getProcessType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZKEYDATE, docsCategory.getZZKEeyDate().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_PARTNER, docsCategory.getPartner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG1, docsCategory.getNameOrg1().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG2, docsCategory.getNameOrg2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_NICKNAME, docsCategory.getNICKNAME().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_STRAS, docsCategory.getStreet().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_ORT01, docsCategory.getCity().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_REGIO, docsCategory.getRegion().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_PSTLZ, docsCategory.getPostalCode1().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_LAND1, docsCategory.getCountryIso().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS, docsCategory.getStatus().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_TXT30, docsCategory.getTxt30().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_STATUS_REASON, docsCategory.getStatusReason().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_PARTNER, docsCategory.getCp1Partner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_NAME1_TEXT, docsCategory.getCp1Name().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO, docsCategory.getCp1TelNo().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP1_TEL_NO2, docsCategory.getCp1TelNo2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_PARTNER, docsCategory.getCp2Partner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_NAME1_TEXT, docsCategory.getCp2Name().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO, docsCategory.getCp2TelNo().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_CP2_TEL_NO2, docsCategory.getCp2TelNo2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_DESCRIPTION, docsCategory.getDescription().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRIORITY, docsCategory.getPriority().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_IBASE, docsCategory.getIB_Base().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INSTANCE, docsCategory.getIB_Instance().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_SERIAL_NUMBER, docsCategory.getSerialNumber().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_ID, docsCategory.getRefObjProductId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_DESCR, docsCategory.getIB_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_IB_INST_DESCR, docsCategory.getIB_Inst_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_REFOBJ_PRODUCT_DESCR, docsCategory.getRefObj_Product_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_TIMEZONE_FROM, docsCategory.getTimeZone().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_NUMBER_EXT, docsCategory.getNumberExt().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_PRODUCT_ID, docsCategory.getProductId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_QUANTITY, docsCategory.getQuantity().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_PROCESS_QTY_UNIT, docsCategory.getProcessQtyUnit().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_DESCRIPTION, docsCategory.getZZItemDesc().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_ZZITEM_TEXT, docsCategory.getZZItemText().toString().trim());
	    	/*val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_FIELD_NOTES, docsCategory.getFieldNotes().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_ITEM_NUMBER, docsCategory.getItemNumber().toString().trim());*/
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFLIST_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertConfListDataInToDB : "+sgh.toString());
    	}
    }//fn insertConfListDataInToDB
    
    public static void insertConfSpareListDataInToDB(Context ctx, ServiceOrdDocOpConstraints docsCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_OBJECT_ID, docsCategory.getObjectId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PROCESS_TYPE, docsCategory.getProcessType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ZZKEYDATE, docsCategory.getZZKEeyDate().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PARTNER, docsCategory.getPartner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_NAME_ORG1, docsCategory.getNameOrg1().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_NAME_ORG2, docsCategory.getNameOrg2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFLIST_COL_NICKNAME, docsCategory.getNICKNAME().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STRAS, docsCategory.getStreet().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ORT01, docsCategory.getCity().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_REGIO, docsCategory.getRegion().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PSTLZ, docsCategory.getPostalCode1().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_LAND1, docsCategory.getCountryIso().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STATUS, docsCategory.getStatus().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STATUS_TXT30, docsCategory.getTxt30().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_STATUS_REASON, docsCategory.getStatusReason().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_PARTNER, docsCategory.getCp1Partner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_NAME1_TEXT, docsCategory.getCp1Name().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_TEL_NO, docsCategory.getCp1TelNo().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP1_TEL_NO2, docsCategory.getCp1TelNo2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_PARTNER, docsCategory.getCp2Partner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_NAME1_TEXT, docsCategory.getCp2Name().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_TEL_NO, docsCategory.getCp2TelNo().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_CP2_TEL_NO2, docsCategory.getCp2TelNo2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_DESCRIPTION, docsCategory.getDescription().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PRIORITY, docsCategory.getPriority().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_IBASE, docsCategory.getIB_Base().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_INSTANCE, docsCategory.getIB_Instance().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_SERIAL_NUMBER, docsCategory.getSerialNumber().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_REFOBJ_PRODUCT_ID, docsCategory.getRefObjProductId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_DESCR, docsCategory.getIB_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_IB_INST_DESCR, docsCategory.getIB_Inst_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_REFOBJ_PRODUCT_DESCR, docsCategory.getRefObj_Product_Descr().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_TIMEZONE_FROM, docsCategory.getTimeZone().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_NUMBER_EXT, docsCategory.getNumberExt().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PRODUCT_ID, docsCategory.getProductId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_QUANTITY, docsCategory.getQuantity().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_PROCESS_QTY_UNIT, docsCategory.getProcessQtyUnit().toString().trim());
	    	/*val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ZZITEM_DESCRIPTION, docsCategory.getZZItemDesc().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ZZITEM_TEXT, docsCategory.getZZItemText().toString().trim());*/
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFSPARELIST_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertConfSpareListDataInToDB : "+sgh.toString());
    	}
    }//fn insertConfSpareListDataInToDB
        
    public static void insertConfCollecListDataInToDB(Context ctx, ServiceFollDocOpConstraints confirmMainCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_OBJECT_ID, confirmMainCategory.getObjectId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_PROCESS_TYPE, confirmMainCategory.getProcessType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_NUMBER_EXT, confirmMainCategory.getNumberExtn().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ERDAT, confirmMainCategory.getERDate().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_STATUS, confirmMainCategory.getStatus().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_TXT30, confirmMainCategory.getTXT30().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_OBJECT_ID, confirmMainCategory.getSRCDocObjId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_PROCESS_TYPE, confirmMainCategory.getSRCDocProcessType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_NUMBER_EXT, confirmMainCategory.getSRCDocNumberExt().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertConfCollecListDataInToDB : "+sgh.toString());
    	}
    }//fn insertConfCollecListDataInToDB
    
    public static void insertConfCollecListDataInToDBForOfflineMode(Context ctx, String srcDocObjectId, String numberExt){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_OBJECT_ID, "");
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_PROCESS_TYPE, "");
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_NUMBER_EXT, numberExt);
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ERDAT, "");
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_STATUS, "");
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_TXT30, "");
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_OBJECT_ID, srcDocObjectId);
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_PROCESS_TYPE, "");
	    	val.put(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_NUMBER_EXT, numberExt);
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFCOLLECLIST_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertConfCollecListDataInToDB : "+sgh.toString());
    	}
    }//fn insertConfCollecListDataInToDB
    
	public static void insertConfProductListDataInToDB(Context ctx, SOServiceActListOpConstraints serActListObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_PRODUCT_ID, serActListObj.getProductId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_SHORT_TEXT, serActListObj.getProductDesc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFPRODUCTLIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfProductListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfProductListDataInToDB
    
	public static void insertConfCauseGroupListDataInToDB(Context ctx, SOCodeGroupOpConstraints codeGroupObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_CODEGRUPPE, codeGroupObj.getCodeGroup().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_KURZTEXT, codeGroupObj.getCodeGroupDesc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfCauseGroupListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfCauseGroupListDataInToDB
    
	public static void insertConfCauseCodeListDataInToDB(Context ctx, SOCodeListOpConstraints codeListObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_CODEGRUPPE, codeListObj.getCodeGroup().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_CODE, codeListObj.getCode().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_KURZTEXT, codeListObj.getCodeGroupDesc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFCAUSECODELIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfCauseCodeListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfCauseCodeListDataInToDB
    
	public static void insertConfSympGroupListDataInToDB(Context ctx, SOCodeGroupOpConstraints codeGroupObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_CODEGRUPPE, codeGroupObj.getCodeGroup().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_KURZTEXT, codeGroupObj.getCodeGroupDesc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFSYMPGROUPLIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfSympGroupListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfSympGroupListDataInToDB
    
	public static void insertConfSympCodeListDataInToDB(Context ctx, SOCodeListOpConstraints codeListObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_CODEGRUPPE, codeListObj.getCodeGroup().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_CODE, codeListObj.getCode().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_KURZTEXT, codeListObj.getCodeGroupDesc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFSYMPCODELIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfSympCodeListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfSympCodeListDataInToDB
    
	public static void insertConfProbGroupListDataInToDB(Context ctx, SOCodeGroupOpConstraints codeGroupObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_CODEGRUPPE, codeGroupObj.getCodeGroup().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_KURZTEXT, codeGroupObj.getCodeGroupDesc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFPROBGROUPLIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfProbGroupListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfProbGroupListDataInToDB
    
	public static void insertConfProbCodeListDataInToDB(Context ctx, SOCodeListOpConstraints codeListObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_CODEGRUPPE, codeListObj.getCodeGroup().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_CODE, codeListObj.getCode().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_KURZTEXT, codeListObj.getCodeGroupDesc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFPROBCODELIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfProbCodeListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfProbCodeListDataInToDB
    
	public static void insertConfMattEmpListDataInToDB(Context ctx, SOMattEmpListOpConstraints mattEmpObj){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_BP_UNAME, mattEmpObj.getBPName().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MATNR, mattEmpObj.getMaterial().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MAKTX_INSYLANGU, mattEmpObj.getMaterialDesc().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_VRKME, mattEmpObj.getMaterialName().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_MEINS, mattEmpObj.getMaterialUnit().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_CONFMATTEMPLLIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertConfMattEmpListDataInToDB : "+sgh.toString());
		}
	}//fn insertConfMattEmpListDataInToDB
    
	public static void insertColleagueListDataInToDB(Context ctx, ServiceProColleaguesOpConstraints collCategory){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_PARTNER, collCategory.getPartner().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_MC_NAME1, collCategory.getMcName1().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_MC_NAME2, collCategory.getMcName2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_TEL_NO, collCategory.getTelNo().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_TEL_NO2, collCategory.getTelNo2().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_UNAME, collCategory.getUName().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_PLANT, collCategory.getPlant().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_STORAGE_LOC, collCategory.getStorageLoc().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_COLLEAGUELIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertColleagueListDataInToDB : "+sgh.toString());
		}
	}//fn insertColleagueListDataInToDB
    
    public static void insertEntitlementListDataInToDB(Context ctx, EntitlementOpConstraints repCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ORDER_ID, repCategory.getOrderId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ITEM_ID, repCategory.getItemId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ITEM_GUID, repCategory.getItemGuid().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROCESS_TYPE, repCategory.getProcessType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROFILE, repCategory.getProfile().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_PROF_DESC, repCategory.getProfileDesc().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ENT_TYPE, repCategory.getEntType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ENT_DESC, repCategory.getEntDesc().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_LABOUR, repCategory.getDiscLabour().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_PARTS, repCategory.getDiscParts().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_DISC_TRAVEL, repCategory.getDiscTravel().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_FREQUENCY, repCategory.getFrequency().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_TIMEPERIOD, repCategory.getTimePeriod().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_ENTITLEMENTLIST_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertEntitlementListDataInToDB : "+sgh.toString());
    	}
    }//fn insertEntitlementListDataInToDB

    public static void insertStatusListDataInToDB(Context ctx, StatusOpConstraints stCategory){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_COL_STATUS, stCategory.getStatus().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_COL_TXT30, stCategory.getTXT30().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_COL_ZZSTATUS_ICON, stCategory.getZZStatusIcon().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_COL_ZZSTATUS_POSTSETACTION, stCategory.getZZStatusPostSetAction().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_STATUSLIST_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertStatusListDataInToDB : "+sgh.toString());
    	}
    }//fn insertStatusListDataInToDB
    
    public static void insertStatusFollowListDataInToDB(Context ctx, StatusFollowOpConstraints serStatusFollowListObj){
    	try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_PROCESS_TYPE, serStatusFollowListObj.getProcessType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STATUS, serStatusFollowListObj.getStatus().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STONR_NEXT, serStatusFollowListObj.getStonr_next().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_STATUS_NEXT, serStatusFollowListObj.getStatus_next().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_STATUSFOLLOWLIST_CONTENT_URI, val);
    	}
    	catch(Exception sgh){
    		ServiceProConstants.showErrorLog("Error in insertStatusFollowListDataInToDB : "+sgh.toString());
    	}
    }//fn insertStatusFollowListDataInToDB
    
	public static void insertAttachListDataInToDB(Context ctx, ServiceProAttachmentOpConstraints attachCategory){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_ID, attachCategory.getObjectId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ATTACH_COL_OBJECT_TYPE, attachCategory.getObjectType().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ATTACH_COL_NUMBER_EXT, attachCategory.getNumberExt().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_ID, attachCategory.getAttachmentId().toString().trim());
	    	val.put(ServiceProDBConstants.SERPRO_ATTACH_COL_ATTCHMNT_CNTNT, attachCategory.getAttachmentContent().toString().trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_ATTACHLIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertAttachListDataInToDB : "+sgh.toString());
		}
	}//fn insertAttachListDataInToDB
	
	/*public static void insertErrorMsgDetails(Context ctx, String tracId, String errType, String errorDesc, String apiName){
		try{
	    	ContentResolver resolver = ctx.getContentResolver();
	    	ContentValues val = new ContentValues();
	    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID, tracId.trim());
	    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRTYPE, errType.trim());
	    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRDESC, errorDesc.trim());
	    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME, apiName.trim());
	    	resolver.insert(ServiceProOfflineContraintsCP.SERPRO_ERRORLIST_CONTENT_URI, val);
		}
		catch(Exception sgh){
			ServiceProConstants.showErrorLog("Error in insertRow : "+sgh.toString());
		}
	}//fn insertRow*/
	
    /*********************************************************************************************
     *     	Database deletion Related Functions
     *********************************************************************************************/
    //Delete all data from a selected table based on the specified Uri identifying a table
    public static void deleteAllCategoryDataFromDB(Context ctx, Uri selUri, String collId){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String delWhere = ServiceProDBConstants.SERPRO_COLL_ID + " = ?"; 
		     	String[] delWhereParams = new String[]{collId};
				//Make the invocation. # of rows deleted will be sent back
				int rows = resolver.delete(uri, delWhere, delWhereParams);					
				/*ServiceProConstants.showLog("Rows delWhere : "+delWhere);				
				ServiceProConstants.showLog("Rows delWhereParams : "+delWhereParams);*/			
				ServiceProConstants.showLog("Rows Deleted : "+rows);
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllCategoryDataFromDB
    
    public static void deleteAllDocsCategoryDataFromDB(Context ctx, Uri selUri, String idVal){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String delWhere = ServiceProDBConstants.SERPRO_CONFLIST_COL_OBJECT_ID + " = ?"; 
		     	String[] delWhereParams = new String[]{idVal};
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, delWhere, delWhereParams);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllDocsCategoryDataFromDB
    
    public static void deleteAllConfCollecDocsCategoryDataFromDB(Context ctx, Uri selUri, String idVal){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String delWhere = ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_SRCDOC_OBJECT_ID + " = ?"; 
		     	String[] delWhereParams = new String[]{idVal};
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, delWhere, delWhereParams);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfCollecDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfCollecDocsCategoryDataFromDB

    public static void deleteAllConfSpareDocsCategoryDataFromDB(Context ctx, Uri selUri, String idVal){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String delWhere = ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_OBJECT_ID + " = ?"; 
		     	String[] delWhereParams = new String[]{idVal};
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, delWhere, delWhereParams);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfSpareDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfSpareDocsCategoryDataFromDB

    public static void deleteAllConfProductDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfProductDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfProductDocsCategoryDataFromDB

    public static void deleteAllConfCauseGroupDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfCauseGroupDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfCauseGroupDocsCategoryDataFromDB
    
    public static void deleteAllConfCauseCodeDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfCauseCodeDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfCauseCodeDocsCategoryDataFromDB

    public static void deleteAllConfSympGroupDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfSympGroupDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfSympGroupDocsCategoryDataFromDB
    
    public static void deleteAllConfSympCodeDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfSympCodeDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfSympCodeDocsCategoryDataFromDB

    public static void deleteAllConfProbGroupDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfProbGroupDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfProbGroupDocsCategoryDataFromDB
    
    public static void deleteAllConfProbCodeDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfProbCodeDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfProbCodeDocsCategoryDataFromDB
    
    public static void deleteAllConfMattEmpDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllConfMattEmpDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllConfMattEmpDocsCategoryDataFromDB

    public static void deleteAllColleagueListDocsCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllColleagueListDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllColleagueListDocsCategoryDataFromDB
    
    public static void deleteAllEntitlementDocsCategoryDataFromDB(Context ctx, Uri selUri, String idVal){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String delWhere = ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ORDER_ID + " = ?"; 
		     	String[] delWhereParams = new String[]{idVal};
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, delWhere, delWhereParams);			
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllEntitlementDocsCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllEntitlementDocsCategoryDataFromDB

    public static void deleteAllStatusListDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();
				//Make the invocation. # of rows deleted will be sent back
		     	resolver.delete(uri, null, null);		
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllStatusListDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllStatusListDataFromDB

    public static void deleteAllStatusFollowListDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();
				//Make the invocation. # of rows deleted will be sent back
		     	resolver.delete(uri, null, null);		
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllStatusFollowListDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllStatusFollowListDataFromDB
    
    public static void deleteAllAttachCategoryDataFromDB(Context ctx, Uri selUri){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();	
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, null, null);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteAllAttachCategoryDataFromDB : "+sggh.toString());
    	}
    }//fn deleteAllAttachCategoryDataFromDB
    
    /*public static void deleteRow(Context ctx, Uri selUri, String tracId){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String delWhere = ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID + " = ?"; 
		     	String[] delWhereParams = new String[]{tracId};
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, delWhere, delWhereParams);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteRow : "+sggh.toString());
    	}
    }//fn deleteRow

    public static void deleteRowForStatus(Context ctx, Uri selUri, String tracId, String apiName){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String delWhere = ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID + " = ? AND " + ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME + " = ?";
		     	String[] delWhereParams = new String[]{tracId, apiName};
				//Make the invocation. # of rows deleted will be sent back
				resolver.delete(uri, delWhere, delWhereParams);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in deleteRowForStatus : "+sggh.toString());
    	}
    }//fn deleteRowForStatus
     */    
    /*********************************************************************************************
     *     	Database Updation Related Functions
     *********************************************************************************************/

    /*public static void updateValue(Context ctx, Uri selUri, String tracId, String errType, String errorDesc, String apiName){
    	try{
    		if(selUri != null){
	    		Uri uri = Uri.parse(selUri.toString());				
				//Get the Resolver
				ContentResolver resolver = ctx.getContentResolver();		
				String updateWhere = ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID + " = ? AND " + ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME + " = ?";
		     	String[] updateWhereParams = new String[]{tracId, apiName};
		     	
				//Make the invocation. # of rows deleted will be sent back
		    	ContentValues val = new ContentValues();
		    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_TRANCID, tracId.trim());
		    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRTYPE, errType.trim());
		    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_ERRDESC, errorDesc.trim());
		    	val.put(ServiceProDBConstants.SERPRO_ERRORLIST_COL_APINAME, apiName.trim());
		    	
				resolver.update(uri, val, updateWhere, updateWhereParams);	
    		}
    	}
    	catch(Exception sggh){
    		ServiceProConstants.showErrorLog("Error in updateValue : "+sggh.toString());
    	}
    }//fn updateValue
*/    
}//End of class ServiceProDBOperations