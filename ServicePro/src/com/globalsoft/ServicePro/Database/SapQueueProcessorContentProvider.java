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
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.globalsoft.ServicePro.ServiceProConstants;

public class SapQueueProcessorContentProvider extends ContentProvider {	
	
	//private SapQueueProcessorDatabase mDB;
	public static final String TABLE_QUEUEPRSSR = "queueprocessor_table";
    public static final String COL_ID = BaseColumns._ID;
    public static final String COL_APPREFID = "apprefid";
    public static final String COL_APPNAME = "appname";
    public static final String COL_PCKGNAME = "packagename";
    public static final String COL_CLASSNAME = "classname";
    public static final String COL_FUNCNAME = "apiname";
    public static final String COL_DATE = "queuedate";
    public static final String COL_SOAPDATA = "soapdata";
    public static final String COL_STATUS = "status";
    public static final String COL_PROCESS_TIME = "processstarttime";
	public static final String AUTHORITY = "com.globalsoft.SapQueueProcessor.SapQueueProcessorContentProvider";
	public SQLiteOpenHelper mDB;
	public static final int MATCH_ALL = 101;
	public static final int MATCH_ID = 102;
	
	private static final String OBJECTS_BASE_PATH = "objects";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH);
	
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/queue-processor";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/queue-processor";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    
	static {
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH, MATCH_ALL);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH + "/#", MATCH_ID);
	}
	
	@Override
	public boolean onCreate() {
		Context ctx = getContext();
		//mDB = new SapQueueProcessorDatabase(ctx);		
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
	        case MATCH_ALL:
	            return CONTENT_TYPE;
	        case MATCH_ID:
	            return CONTENT_ITEM_TYPE;
	        default:
	            return null;
        }
    }
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SapQueueProcessorContentProvider.TABLE_QUEUEPRSSR);

        int uriType = sURIMatcher.match(uri);
        ServiceProConstants.showLog("URI : "+uri+" : "+uriType+" : "+uri.getLastPathSegment());
        switch (uriType) {
	        case MATCH_ID:
	            queryBuilder.appendWhere(SapQueueProcessorContentProvider.COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL:
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
        if (uriType != MATCH_ALL) {
            throw new IllegalArgumentException("Invalid URI for insert");
        }
        
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        try {
            long newID = sqlDB.insertOrThrow(SapQueueProcessorContentProvider.TABLE_QUEUEPRSSR, null, values);
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

        switch (uriType) {
	        case MATCH_ID:
	            String id = uri.getLastPathSegment();
	            StringBuilder modSelection = new StringBuilder(SapQueueProcessorContentProvider.COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(SapQueueProcessorContentProvider.TABLE_QUEUEPRSSR,
	                    values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL:
	            rowsAffected = sqlDB.update(SapQueueProcessorContentProvider.TABLE_QUEUEPRSSR,
	                    values, selection, selectionArgs);
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
        
        switch (uriType) {
	        case MATCH_ALL:
	            rowsAffected = sqlDB.delete(SapQueueProcessorContentProvider.TABLE_QUEUEPRSSR,
	                    selection, selectionArgs);
	            break;
	        case MATCH_ID:
	            String id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(SapQueueProcessorContentProvider.TABLE_QUEUEPRSSR, SapQueueProcessorContentProvider.COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(SapQueueProcessorContentProvider.TABLE_QUEUEPRSSR,
	                        selection + " and " + SapQueueProcessorContentProvider.COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }	
	
}//End of class SapQueueProcessorContentProvider
