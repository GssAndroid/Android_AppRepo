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

public class ServiceProOfflineContraintsCP extends ContentProvider {	
		
	private ServiceProOfflineConstraintsDB mDB;	
	public static final String AUTHORITY = "com.globalsoft.ServicePro.ServiceProOfflineContraintsCP";	
	
	public static final int MATCH_ALL1 = 10001;	
	public static final int MATCH_ALL2 = 10002;
	public static final int MATCH_ALL3 = 10003;
	public static final int MATCH_ALL4 = 10004;
	public static final int MATCH_ALL5 = 10005;
	public static final int MATCH_ALL6 = 10006;
	public static final int MATCH_ALL7 = 10007;
	public static final int MATCH_ALL8 = 10008;
	public static final int MATCH_ALL9 = 10009;
	public static final int MATCH_ALL10 = 100010;
	public static final int MATCH_ALL11 = 100011;
	public static final int MATCH_ALL12 = 100012;
	public static final int MATCH_ALL13 = 100013;
	public static final int MATCH_ALL14 = 100014;
	public static final int MATCH_ALL15 = 100015;
	public static final int MATCH_ALL16 = 100016;
	public static final int MATCH_ALL17 = 100017;
	
	public static final int MATCH_ID1 = 20001;
	public static final int MATCH_ID2 = 20002;
	public static final int MATCH_ID3 = 20003;
	public static final int MATCH_ID4 = 20004;
	public static final int MATCH_ID5 = 20005;
	public static final int MATCH_ID6 = 20006;
	public static final int MATCH_ID7 = 20007;
	public static final int MATCH_ID8 = 20008;
	public static final int MATCH_ID9 = 20009;
	public static final int MATCH_ID10 = 200010;
	public static final int MATCH_ID11 = 200011;
	public static final int MATCH_ID12 = 200012;
	public static final int MATCH_ID13 = 200013;
	public static final int MATCH_ID14 = 200014;
	public static final int MATCH_ID15 = 200015;
	public static final int MATCH_ID16 = 200016;
	public static final int MATCH_ID17 = 200017;
	
	private static final String OBJECTS_BASE_PATH1 = "objects1";
	private static final String OBJECTS_BASE_PATH2 = "objects2";
	private static final String OBJECTS_BASE_PATH3 = "objects3";
	private static final String OBJECTS_BASE_PATH4 = "objects4";
	private static final String OBJECTS_BASE_PATH5 = "objects5";
	private static final String OBJECTS_BASE_PATH6 = "objects6";
	private static final String OBJECTS_BASE_PATH7 = "objects7";
	private static final String OBJECTS_BASE_PATH8 = "objects8";
	private static final String OBJECTS_BASE_PATH9 = "objects9";
	private static final String OBJECTS_BASE_PATH10 = "objects10";
	private static final String OBJECTS_BASE_PATH11 = "objects11";
	private static final String OBJECTS_BASE_PATH12 = "objects12";
	private static final String OBJECTS_BASE_PATH13 = "objects13";
	private static final String OBJECTS_BASE_PATH14 = "objects14";
	private static final String OBJECTS_BASE_PATH15 = "objects15";
	private static final String OBJECTS_BASE_PATH16 = "objects16";
	private static final String OBJECTS_BASE_PATH17 = "objects17";
	
    public static final Uri SERPRO_COLL_TASKS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH1);	
    public static final Uri SERPRO_CONFLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH2);
    public static final Uri SERPRO_CONFCOLLECLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH3);
    public static final Uri SERPRO_CONFSPARELIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH4);
    public static final Uri SERPRO_CONFPRODUCTLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH5);
    public static final Uri SERPRO_CONFCAUSECODELIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH6);
    public static final Uri SERPRO_CONFCAUSEGROUPLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH7);
    public static final Uri SERPRO_CONFSYMPGROUPLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH8);
    public static final Uri SERPRO_CONFSYMPCODELIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH9);
    public static final Uri SERPRO_CONFPROBGROUPLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH10);
    public static final Uri SERPRO_CONFPROBCODELIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH11);
    public static final Uri SERPRO_CONFMATTEMPLLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH12);
    public static final Uri SERPRO_COLLEAGUELIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH13);
    public static final Uri SERPRO_ENTITLEMENTLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH14);
    public static final Uri SERPRO_STATUSLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH15);
    public static final Uri SERPRO_ATTACHLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH16);
    public static final Uri SERPRO_STATUSFOLLOWLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_BASE_PATH17);
    
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/servicepro-details";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/servicepro-details";
    
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
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH8, MATCH_ALL8);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH8 + "/#", MATCH_ID8);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH9, MATCH_ALL9);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH9 + "/#", MATCH_ID9);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH10, MATCH_ALL10);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH10 + "/#", MATCH_ID10);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH11, MATCH_ALL11);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH11 + "/#", MATCH_ID11);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH12, MATCH_ALL12);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH12 + "/#", MATCH_ID12);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH13, MATCH_ALL13);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH13 + "/#", MATCH_ID13);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH14, MATCH_ALL14);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH14 + "/#", MATCH_ID14);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH15, MATCH_ALL15);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH15 + "/#", MATCH_ID15);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH16, MATCH_ALL16);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH16 + "/#", MATCH_ID16);
		
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH17, MATCH_ALL17);
		sURIMatcher.addURI(AUTHORITY, OBJECTS_BASE_PATH17 + "/#", MATCH_ID17);
	}
	
	@Override
	public boolean onCreate() {
		Context ctx = getContext();
		mDB = new ServiceProOfflineConstraintsDB(ctx);		
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        if( (uriType == MATCH_ALL1) || (uriType == MATCH_ALL2) || (uriType == MATCH_ALL3) || (uriType == MATCH_ALL4) || (uriType == MATCH_ALL5) 
        		|| (uriType == MATCH_ALL6) || (uriType == MATCH_ALL7) || (uriType == MATCH_ALL8) || (uriType == MATCH_ALL9) || (uriType == MATCH_ALL10) 
        		|| (uriType == MATCH_ALL11) || (uriType == MATCH_ALL12) || (uriType == MATCH_ALL13) || (uriType == MATCH_ALL14) || (uriType == MATCH_ALL15) 
        		|| (uriType == MATCH_ALL16) || (uriType == MATCH_ALL17) ){
        	return CONTENT_TYPE;
        }
        else if( (uriType == MATCH_ID1) || (uriType == MATCH_ID2) || (uriType == MATCH_ID3) || (uriType == MATCH_ID4) || (uriType == MATCH_ID5) 
        		|| (uriType == MATCH_ID6) || (uriType == MATCH_ID7) || (uriType == MATCH_ID8) || (uriType == MATCH_ID9) || (uriType == MATCH_ID10) 
        		|| (uriType == MATCH_ID11) || (uriType == MATCH_ID12) || (uriType == MATCH_ID13) || (uriType == MATCH_ID14) || (uriType == MATCH_ID15) 
        		|| (uriType == MATCH_ID16) || (uriType == MATCH_ID17) ){
        	return CONTENT_ITEM_TYPE;
        }
        else
        	return null;
    }
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        //ServiceProConstants.showLog("URI : "+uri+" : "+uriType+" : "+uri.getLastPathSegment());
        switch (uriType) {
	        case MATCH_ID1:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_COLTASK);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_COLTASK_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL1:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_COLTASK);
	            // no filter
	            break;
	        case MATCH_ID2:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL2:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFLIST);
	            // no filter
	            break;
	        case MATCH_ID3:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL3:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST);
	            // no filter
	            break;
	        case MATCH_ID4:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL4:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST);
	            // no filter
	            break;
	        case MATCH_ID5:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL5:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST);
	            // no filter
	            break;
	        case MATCH_ID6:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL6:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST);
	            // no filter
	            break;
	        case MATCH_ID7:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL7:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST);
	            // no filter
	            break;
	        case MATCH_ID8:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL8:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST);
	            // no filter
	            break;
	        case MATCH_ID9:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL9:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST);
	            // no filter
	            break;
	        case MATCH_ID10:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL10:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST);
	            // no filter
	            break;
	        case MATCH_ID11:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL11:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST);
	            // no filter
	            break;
	        case MATCH_ID12:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL12:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST);
	            // no filter
	            break;
	        case MATCH_ID13:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL13:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST);
	            // no filter
	            break;
	        case MATCH_ID14:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL14:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST);
	            // no filter
	            break;
	        case MATCH_ID15:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_STATUS_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL15:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST);
	            // no filter
	            break;
	        case MATCH_ID16:
	            queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST);
	            queryBuilder.appendWhere(ServiceProDBConstants.SERPRO_ATTACH_COL_ID + "=" + uri.getLastPathSegment());
	            break;
	        case MATCH_ALL16:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST);
	            // no filter
	            break;
	        case MATCH_ALL17:
	        	 queryBuilder.setTables(ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST);
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
        if( (uriType != MATCH_ALL1) && (uriType != MATCH_ALL2) && (uriType != MATCH_ALL3) && (uriType != MATCH_ALL4) && (uriType != MATCH_ALL5) 
        		&& (uriType != MATCH_ALL6) && (uriType != MATCH_ALL7) && (uriType != MATCH_ALL8) && (uriType != MATCH_ALL9) && (uriType != MATCH_ALL10) 
        		&& (uriType != MATCH_ALL11) && (uriType != MATCH_ALL12) && (uriType != MATCH_ALL13) && (uriType != MATCH_ALL14) && (uriType != MATCH_ALL15) 
        		&& (uriType != MATCH_ALL16) && (uriType != MATCH_ALL17) ){
        	throw new IllegalArgumentException("Invalid URI for insert");
        }
        
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        try {
            long newID = -1;
            if(uriType == MATCH_ALL1)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_COLTASK, null, values);
            else if(uriType == MATCH_ALL2)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFLIST, null, values);
            else if(uriType == MATCH_ALL3)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST, null, values);
            else if(uriType == MATCH_ALL4)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST, null, values);
            else if(uriType == MATCH_ALL5)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST, null, values);
            else if(uriType == MATCH_ALL6)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST, null, values);
            else if(uriType == MATCH_ALL7)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST, null, values);
            else if(uriType == MATCH_ALL8)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST, null, values);
            else if(uriType == MATCH_ALL9)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST, null, values);
            else if(uriType == MATCH_ALL10)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST, null, values);
            else if(uriType == MATCH_ALL11)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST, null, values);
            else if(uriType == MATCH_ALL12)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST, null, values);
            else if(uriType == MATCH_ALL13)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST, null, values);
            else if(uriType == MATCH_ALL14)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST, null, values);
            else if(uriType == MATCH_ALL15)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST, null, values);
            else if(uriType == MATCH_ALL16)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST, null, values);
            else if(uriType == MATCH_ALL17)
            	newID = sqlDB.insertOrThrow(ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST, null, values);
            
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
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_COLTASK_COL_ID + "=" + id);	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_COLTASK, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL1:
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_COLTASK, values, selection, selectionArgs);
	            break;
	        case MATCH_ID2:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL2:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID3:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL3:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID4:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL4:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID5:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL5:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID6:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL6:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID7:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL7:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID8:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL8:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID9:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL9:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID10:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL10:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID11:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL11:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID12:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL12:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID13:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL13:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID14:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL14:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID15:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_STATUS_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL15:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID16:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_ATTACH_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL16:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST, values, selection, selectionArgs);
	            break;
	        case MATCH_ID17:
	        	id = uri.getLastPathSegment();
	            modSelection = new StringBuilder(ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_ID + "=" + id);
	
	            if (!TextUtils.isEmpty(selection)) {
	                modSelection.append(" AND " + selection);
	            }
	
	            rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST, values, modSelection.toString(), null);
	            break;
	        case MATCH_ALL17:
	        	rowsAffected = sqlDB.update(ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST, values, selection, selectionArgs);
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
	            rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_COLTASK, selection, selectionArgs);
	            break;
	        case MATCH_ID1:
	            id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_COLTASK, ServiceProDBConstants.SERPRO_COLTASK_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_COLTASK,
	                        selection + " and " + ServiceProDBConstants.SERPRO_COLTASK_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL2:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID2:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFLIST, ServiceProDBConstants.SERPRO_CONFLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL3:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID3:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST, ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCOLLECLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL4:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST, selection, selectionArgs);
	            break;
	        case MATCH_ID4:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST, ServiceProDBConstants.SERPRO_CONFCOLLECLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSPARELIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFSPARELIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL5:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID5:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST, ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPRODUCTLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFPRODUCTLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL6:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST, selection, selectionArgs);
	            break;
	        case MATCH_ID6:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST, ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSECODELIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFCAUSECODELIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL7:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID7:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST, ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFCAUSEGROUPLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFCAUSEGROUPLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL8:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID8:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST, ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPGROUPLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFSYMPGROUPLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL9:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST, selection, selectionArgs);
	            break;
	        case MATCH_ID9:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST, ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFSYMPCODELIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFSYMPCODELIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL10:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID10:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST, ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPROBGROUPLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFPROBGROUPLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL11:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST, selection, selectionArgs);
	            break;
	        case MATCH_ID11:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST, ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFPROBCODELIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFPROBCODELIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL12:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID12:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST, ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_CONFMATTEMPLLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_CONFMATTEMPLLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL13:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST, selection, selectionArgs);
	            break;
	        case MATCH_ID13:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST, ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_COLLEAGUELIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_COLLEAGUELIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL14:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID14:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST, ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_ENTITLEMENTLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_ENTITLEMENTLIST_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL15:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID15:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST, ServiceProDBConstants.SERPRO_STATUS_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_STATUSLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_STATUS_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL16:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID16:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST, ServiceProDBConstants.SERPRO_ATTACH_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_ATTACHLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_ATTACH_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        case MATCH_ALL17:
	        	rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST, selection, selectionArgs);
	            break;
	        case MATCH_ID17:
	        	id = uri.getLastPathSegment();
	            if (TextUtils.isEmpty(selection)) {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST, ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_ID + "=" + id, null);
	            } 
	            else {
	                rowsAffected = sqlDB.delete(ServiceProDBConstants.TABLE_SERPRO_STATUSFOLLOWLIST,
	                        selection + " and " + ServiceProDBConstants.SERPRO_STATUS_FOLLOW_COL_ID + "=" + id, selectionArgs);
	            }
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }        
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }	
}//End of class ServiceProOfflineContraintsCP