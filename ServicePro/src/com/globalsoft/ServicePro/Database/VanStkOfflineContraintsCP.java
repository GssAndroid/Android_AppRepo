package com.globalsoft.ServicePro.Database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.globalsoft.ServicePro.ServiceProConstants;

public class VanStkOfflineContraintsCP  extends ContentProvider {	
	
	private VanStkOfflineConstraintsDB mDB;
	
	public static final String AUTHORITY = "com.globalsoft.ServicePro.VanStkOfflineContraintsCP";
		
	public static final int MATCH_ALL1 = 1001;
	public static final int MATCH_ALL2 = 1002;
	public static final int MATCH_ALL3 = 1003;
	public static final int MATCH_ALL4 = 1004;
	public static final int MATCH_ALL5 = 1005;
	public static final int MATCH_ALL6 = 1006;
	public static final int MATCH_ALL7 = 1007;
	
	public static final int MATCH_ID1 = 2001;
	public static final int MATCH_ID2 = 2002;
	public static final int MATCH_ID3 = 2003;
	public static final int MATCH_ID4 = 2004;
	public static final int MATCH_ID5 = 2005;
	public static final int MATCH_ID6 = 2006;
	public static final int MATCH_ID7 = 2007;
	
	private static final String OBJECTS_BASE_PATH1 = "objects1";
	private static final String OBJECTS_BASE_PATH2 = "objects2";
	private static final String OBJECTS_BASE_PATH3 = "objects3";
	private static final String OBJECTS_BASE_PATH4 = "objects4";
	private static final String OBJECTS_BASE_PATH5 = "objects5";
	private static final String OBJECTS_BASE_PATH6 = "objects6";
	private static final String OBJECTS_BASE_PATH7 = "objects7";
	
    public static final Uri VAN_EMP_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH1);
    public static final Uri VAN_EMP_SRL_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH2);
    public static final Uri VAN_EMP_STO_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH3);
    public static final Uri VAN_COL_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH4);
    public static final Uri VAN_COL_SRL_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH5);
    public static final Uri VAN_COL_STK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH6);
    public static final Uri VAN_COL_STK_SRL_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH7);
    
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vanstk-details";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vanstk-details";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    
	static {
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH1, MATCH_ALL1);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH1 + "/#", MATCH_ID1);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH2, MATCH_ALL2);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH2 + "/#", MATCH_ID2);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH3, MATCH_ALL3);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH3 + "/#", MATCH_ID3);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH4, MATCH_ALL4);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH4 + "/#", MATCH_ID4);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH5, MATCH_ALL5);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH5 + "/#", MATCH_ID5);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH6, MATCH_ALL6);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH6 + "/#", MATCH_ID6);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH7, MATCH_ALL7);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH7 + "/#", MATCH_ID7);
	}
	
	@Override
	public boolean onCreate() {
		Context ctx = getContext();
		mDB = new VanStkOfflineConstraintsDB(ctx);		
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        if((uriType == MATCH_ALL1) || (uriType == MATCH_ALL2) || (uriType == MATCH_ALL3) || (uriType == MATCH_ALL4) 
        		|| (uriType == MATCH_ALL5) || (uriType == MATCH_ALL6) || (uriType == MATCH_ALL7)){
        	return CONTENT_TYPE;
        }
        else if((uriType == MATCH_ID1) || (uriType == MATCH_ID2) || (uriType == MATCH_ID3) || (uriType == MATCH_ID4) 
        		|| (uriType == MATCH_ID5) || (uriType == MATCH_ID6) || (uriType == MATCH_ID7)){
        	return CONTENT_ITEM_TYPE;
        }
        else
        	return null;
        /*
        switch (uriType) {
	        case MATCH_ALL1:
	            return CONTENT_TYPE;
	        case MATCH_ID1:
	            return CONTENT_ITEM_TYPE;
	        default:
	            return null;
        }
        */
    }
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        //ServiceProConstants.showLog("URI : "+uri+" : "+uriType+" : "+uri.getLastPathSegment());
        switch (uriType) {
	        case MATCH_ID1:
	            queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_EMP);
	            queryBuilder.appendWhere(VanStkDBConstants.VAN_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL1:
	        	 queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_EMP);
	            // no filter
	            break;
	        case MATCH_ID2:
	            queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL);
	            queryBuilder.appendWhere(VanStkDBConstants.VAN_SER_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL2:
	        	 queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL);
	            // no filter
	            break;
	        case MATCH_ID3:
	            queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_EMP_STO);
	            queryBuilder.appendWhere(VanStkDBConstants.VAN_STO_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL3:
	        	 queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_EMP_STO);
	            // no filter
	            break;
	        case MATCH_ID4:
	            queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL);
	            queryBuilder.appendWhere(VanStkDBConstants.VAN_CLG_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL4:
	        	 queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL);
	            // no filter
	            break;
	        case MATCH_ID5:
	            queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL);
	            queryBuilder.appendWhere(VanStkDBConstants.VAN_CLG_SRL_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL5:
	        	 queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL);
	            // no filter
	            break;
	        case MATCH_ID6:
	            queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL_STOCK);
	            queryBuilder.appendWhere(VanStkDBConstants.VAN_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL6:
	        	 queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL_STOCK);
	            // no filter
	            break;
	        case MATCH_ID7:
	            queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL);
	            queryBuilder.appendWhere(VanStkDBConstants.VAN_SER_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL7:
	        	 queryBuilder.setTables(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL);
	            // no filter
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        if((uriType != MATCH_ALL1) && (uriType != MATCH_ALL2) && (uriType != MATCH_ALL3) && (uriType != MATCH_ALL4) && 
        		(uriType != MATCH_ALL5) && (uriType != MATCH_ALL6) && (uriType != MATCH_ALL7)){
        	throw new IllegalArgumentException("Invalid URI for insert");
        }
        
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        try {
            long newID = -1;
            if(uriType == MATCH_ALL1)
            	newID = sqlDB.insertOrThrow(VanStkDBConstants.TABLE_VANSTK_EMP, null, values);
            else if(uriType == MATCH_ALL2)
            	newID = sqlDB.insertOrThrow(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL, null, values);
        	else if(uriType == MATCH_ALL3)
        		newID = sqlDB.insertOrThrow(VanStkDBConstants.TABLE_VANSTK_EMP_STO, null, values);
    		else if(uriType == MATCH_ALL4)
    			newID = sqlDB.insertOrThrow(VanStkDBConstants.TABLE_VANSTK_COL, null, values);
			else if(uriType == MATCH_ALL5)
				newID = sqlDB.insertOrThrow(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL, null, values);
			else if(uriType == MATCH_ALL6)
    			newID = sqlDB.insertOrThrow(VanStkDBConstants.TABLE_VANSTK_COL_STOCK, null, values);
			else if(uriType == MATCH_ALL7)
				newID = sqlDB.insertOrThrow(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL, null, values);
            
            if (newID > 0) {
                Uri newUri = ContentUris.withAppendedId(uri, newID);
                getContext().getContentResolver().notifyChange(uri, null);
                return newUri;
            } 
            else {
                throw new SQLException("Failed to insert row into " + uri);
            }
        } 
        catch (SQLiteConstraintException e) {
            ServiceProConstants.showErrorLog("Ignoring constraint failure : "+e.toString());
        }
        return null;
    }

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();

        int rowsAffected;
        String id = "";
        StringBuilder modSelection = null;
        switch (uriType) {
	        case MATCH_ID1:
	            id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(VanStkDBConstants.VAN_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_EMP, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL1:
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_EMP, values, selection, selectionArgs);
	            break;
	        case MATCH_ID2:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(VanStkDBConstants.VAN_SER_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL2:
	        	rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL, values, selection, selectionArgs);
	            break;
	        case MATCH_ID3:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(VanStkDBConstants.VAN_STO_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_EMP_STO, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL3:
	        	rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_EMP_STO, values, selection, selectionArgs);
	            break;
	        case MATCH_ID4:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(VanStkDBConstants.VAN_CLG_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL4:
	        	rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL, values, selection, selectionArgs);
	            break;
	        case MATCH_ID5:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(VanStkDBConstants.VAN_CLG_SRL_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL5:
	        	rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL, values, selection, selectionArgs);
	            break;
	        case MATCH_ID6:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(VanStkDBConstants.VAN_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL_STOCK, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL6:
	        	rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL_STOCK, values, selection, selectionArgs);
	            break;
	        case MATCH_ID7:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(VanStkDBConstants.VAN_SER_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL7:
	        	rowsAffected = sqlDB.update(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL, values, selection, selectionArgs);
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown or Invalid URI");
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsAffected = 0;
        String id = "";
        switch (uriType) {
	        case MATCH_ALL1:
	            rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP, selection, selectionArgs);
	            break;
	        case MATCH_ID1:
	            id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP, VanStkDBConstants.VAN_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP,
	                        selection + " and " + VanStkDBConstants.VAN_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL2:
	        	rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL, selection, selectionArgs);
	            break;
	        case MATCH_ID2:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL, VanStkDBConstants.VAN_SER_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP_SERIAL,
	                        selection + " and " + VanStkDBConstants.VAN_SER_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL3:
	        	rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP_STO, selection, selectionArgs);
	            break;
	        case MATCH_ID3:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP_STO, VanStkDBConstants.VAN_STO_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_EMP_STO,
	                        selection + " and " + VanStkDBConstants.VAN_STO_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL4:
	        	rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL, selection, selectionArgs);
	            break;
	        case MATCH_ID4:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL, VanStkDBConstants.VAN_CLG_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL,
	                        selection + " and " + VanStkDBConstants.VAN_CLG_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL5:
	        	rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL, selection, selectionArgs);
	            break;
	        case MATCH_ID5:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL, VanStkDBConstants.VAN_CLG_SRL_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_SERIAL,
	                        selection + " and " + VanStkDBConstants.VAN_CLG_SRL_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL6:
	        	rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_STOCK, selection, selectionArgs);
	            break;
	        case MATCH_ID6:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_STOCK, VanStkDBConstants.VAN_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_STOCK,
	                        selection + " and " + VanStkDBConstants.VAN_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL7:
	        	rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL, selection, selectionArgs);
	            break;
	        case MATCH_ID7:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL, VanStkDBConstants.VAN_SER_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(VanStkDBConstants.TABLE_VANSTK_COL_STOCK_SERIAL,
	                        selection + " and " + VanStkDBConstants.VAN_SER_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }	
	
}//End of class VanStkOfflineContraintsCP


