package com.ducphu.note.backupsd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.ducphu.note.NotesDbAdapter;
import com.ducphu.note.R;

@SuppressLint("SimpleDateFormat")
public class BackupToSD extends SherlockListActivity{
	
	private static final int RESTORE_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private String DB_PATH = "/data/com.ducphu.note/databases/";
	private String newFolderSDBackup = Environment.getExternalStorageDirectory() +"/NoteBackup";	
	private File SDBackupFolder_PATH;
	
	private String DB_TEMP_NAME = NotesDbAdapter.DATABASE_NAME + "_temp";
	private String DB_TEMP_PATH = DB_PATH + DB_TEMP_NAME;
	
	private String[] nameFiles;
	private File[] filelist;
	private Button backup_btn;
	private SQLiteDatabase database_temp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup_layout);
		
		backup_btn = (Button)findViewById(R.id.backup_btn);
		createOrCheckFolderBackup();
		registerForContextMenu(getListView());
		
		backup_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					backupDatabase();
					showlistFileBackupDrirectory();					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void createOrCheckFolderBackup(){		          
		SDBackupFolder_PATH = new File(newFolderSDBackup);
	       if(!SDBackupFolder_PATH.exists()){
	    	   SDBackupFolder_PATH.mkdir();
	       }	       
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showlistFileBackupDrirectory();
	}

	private void showlistFileBackupDrirectory() {
		// TODO Auto-generated method stub
		filelist = SDBackupFolder_PATH.listFiles();
		nameFiles = new String[filelist.length];
		for(int i = 0; i<nameFiles.length;i++){
			nameFiles[i] = filelist[i].getName();
		}
		
		ArrayAdapter<String> data = 
				new ArrayAdapter<String>(getApplicationContext(), 
						android.R.layout.simple_list_item_1, nameFiles);
		setListAdapter(data);				
	}

	@SuppressWarnings("resource")
	private void backupDatabase() throws IOException{
		// TODO Auto-generated method stub
	
		if(isSDCanWrite()){
			   SimpleDateFormat formatTime = new SimpleDateFormat("d_M_y__h'h'm");
		       String filenameBackup = NotesDbAdapter.DATABASE_NAME + "_" + formatTime.format(new Date());
		       
		       File inputDB = new File(Environment.getDataDirectory(), DB_PATH + NotesDbAdapter.DATABASE_NAME);
		       File outputDB = new File(SDBackupFolder_PATH, filenameBackup);
		       
		       FileChannel src = new FileInputStream(inputDB).getChannel();
		       FileChannel dst = new FileOutputStream(outputDB).getChannel();
		       
		       dst.transferFrom(src, 0, src.size());
		       src.close();
		       dst.close();
		       Toast.makeText(getApplicationContext(), R.string.toast_backupSuccessfully, Toast.LENGTH_SHORT).show();
		}else{
				Toast.makeText(getApplicationContext(), R.string.toast_backupFailed, Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean isSDCanWrite(){
		boolean status = false;
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			status = true;
		}
		return status;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, RESTORE_ID, 0, R.string.contextmenu_restore);
		menu.add(0, DELETE_ID, 0, R.string.contextmenu_deletefile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case RESTORE_ID:
			final AdapterContextMenuInfo minfo = (AdapterContextMenuInfo) item.getMenuInfo();
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.dialog_title)
			.setMessage(R.string.dialog_message)
			.setIcon(R.drawable.ic_dialog_warning)
			.setPositiveButton(R.string.dialog_OK, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					try {
						importData(nameFiles[minfo.position]);
						copyDBTempToDBMain(database_temp, BackupToSD.this);				
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			})
			.setNegativeButton(R.string.dialog_Cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
			.show();
			return true;
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			filelist[(int) info.id].delete();
			showlistFileBackupDrirectory();		
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/* Copy database from SD card to device
	 * and set name database: datanote_temp*/
	@SuppressWarnings("resource")
	private void importData(String dbFileSelect) throws IOException {
		// TODO Auto-generated method stub
		database_temp = openOrCreateDatabase(DB_TEMP_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		if(isSDCanWrite()){
			File inputDB = new File(Environment.getDataDirectory(), DB_TEMP_PATH);
		    File outputDB = new File(SDBackupFolder_PATH, dbFileSelect);
		       
		    FileChannel src = new FileInputStream(outputDB).getChannel();
		    FileChannel dst = new FileOutputStream(inputDB).getChannel();
		       
		    dst.transferFrom(src, 0, src.size());
		    src.close();
		    dst.close();		    
		}else{
			Toast.makeText(getApplicationContext(), R.string.toast_backupFailed, Toast.LENGTH_SHORT).show();
		}
	}
	
	/* Copy database_temp to datanote on device*/
	public void copyDBTempToDBMain(SQLiteDatabase db, Context context) {
		// TODO Auto-generated method stub
		String title_temp;
		String body_temp;
		String date_temp;
		NotesDbAdapter mDbHelper = new NotesDbAdapter(context);
		mDbHelper.open();
		mDbHelper.delete();
		
		Cursor cursor = db.query(true, NotesDbAdapter.DATABASE_TABLE, null, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			title_temp = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
			body_temp = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));
			date_temp = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_DATE));
			mDbHelper.createNote(title_temp, body_temp, date_temp);
			cursor.moveToNext();
		}
		cursor.close();	
		db.close();
		mDbHelper.close();
		deleteDatabase(DB_TEMP_NAME);
		Toast.makeText(getApplicationContext(), R.string.toast_restoreSuccessfully, Toast.LENGTH_LONG).show();
	}
}
