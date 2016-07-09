package com.ducphu.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {

	public static final String DATABASE_NAME = "notesdata";
	public static final String DATABASE_TABLE = "notes";
	public static final int DATABASE_VERSION = 2;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_DATE = "date";
	
	private static final String DATABASE_CREATE = 
			"create table notes (_id integer primary key autoincrement, "
			+ "title text not null, body text not null, date text not null);";
	
	public static final String TAG = "NotesDbAdapter";
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
			db.execSQL("drop table if exists notes");
			onCreate(db);
		}
		
	}
	
	public NotesDbAdapter(Context context){
		this.mContext = context;
	}
	
	//Open database
	public NotesDbAdapter open() throws SQLException{
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
	public long createNote(String title, String body, String date){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_DATE, date);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public Cursor readAllNotes(String sortOrder){
		if(sortOrder.equals("DESC")){
			return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_DATE}, 
					null, null, null, null, KEY_ROWID + " " + sortOrder);
		}else{
			return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_DATE}, 
					null, null, null, null, KEY_TITLE + " " + sortOrder);
		}
	}
	
	public Cursor readSingleNote(long rowId) throws Exception{
		 Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_DATE}, 
				 KEY_ROWID + "=" + rowId, null, null, null, null);
		 if(mCursor != null){
			 mCursor.moveToFirst();
		 }
		 return mCursor;
	}
	
	public boolean updateNote(long rowId, String title, String body, String date){
		ContentValues mvalue = new ContentValues();
		mvalue.put(KEY_TITLE, title);
		mvalue.put(KEY_BODY, body);
		mvalue.put(KEY_DATE, date);
		return mDb.update(DATABASE_TABLE, mvalue, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteNote(long rowId){
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean delete(){
		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}
}
