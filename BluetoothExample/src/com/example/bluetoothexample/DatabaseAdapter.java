package com.example.bluetoothexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAdapter {

	public static final String DATABASE_NAME = "healthdb";
	public static final String DATABASE_TABLE = "health";
	public static final int DATABASE_VERSION = 2;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_SPO2 = "spo2";
	public static final String KEY_PULSE = "pulse";
	public static final String KEY_BODY = "body";
	public static final String KEY_AMBIENT = "ambient";
	public static final String KEY_DATE = "date";
	
	private static final String DATABASE_CREATE = 
			"create table health (_id integer primary key autoincrement, "
			+ "spo2 text not null, pulse text not null, body text not null, ambient text not null, date text not null);";
	
	public static final String TAG = "DatabaseAdapter";
	private SQLiteDatabase mDb;
	private DatabaseHelper mDbHelper;
	private Context mContext;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
			db.execSQL("drop table if exists health");
			onCreate(db);
		}		
	}
	
	public DatabaseAdapter(Context context){
		this.mContext = context;
	}
	
	//Open database
	public DatabaseAdapter open() throws SQLException{
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDbHelper.close();
	}
	
	public boolean isOpen(){
		return mDb.isOpen();
	}
	
	//CRUD Database
	public long insertData(String spo2, String pulse, String body, String ambient, String date){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SPO2, spo2);
		initialValues.put(KEY_PULSE, pulse);
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_AMBIENT, ambient);
		initialValues.put(KEY_DATE, date);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public Cursor readAllData(){
			return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SPO2, KEY_PULSE, KEY_BODY, KEY_AMBIENT, KEY_DATE}, 
					null, null, null, null, KEY_ROWID + " " + "DESC");		
	}
	
	public Cursor readSingleData(long rowId) throws Exception{
		 Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SPO2, KEY_PULSE, KEY_BODY, KEY_AMBIENT, KEY_DATE}, 
				 KEY_ROWID + "=" + rowId, null, null, null, null);
		 if(mCursor != null){
			 mCursor.moveToFirst();
		 }
		 return mCursor;
	}
	
	public boolean updateData(long rowId, String spo2, String pulse, String body, String ambient, String date){
		ContentValues mvalue = new ContentValues();
		mvalue.put(KEY_SPO2, spo2);
		mvalue.put(KEY_PULSE, pulse);
		mvalue.put(KEY_BODY, body);
		mvalue.put(KEY_AMBIENT, ambient);
		mvalue.put(KEY_DATE, date);
		
		return mDb.update(DATABASE_TABLE, mvalue, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteSingleData(long rowId){
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteAllData(){
		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}
}
