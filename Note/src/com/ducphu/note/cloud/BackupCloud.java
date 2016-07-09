package com.ducphu.note.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ducphu.note.NotesDbAdapter;
import com.ducphu.note.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.app.SherlockListActivity;
import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class BackupCloud extends SherlockListActivity implements OnClickListener{

	private static final String APP_KEY="0wixh4irk66myef";
    private static final String APP_SECRET="6xjc63anbhfm04g";
    private static final int REQUEST_LINK_TO_DBX = 1000;
    
    private static final int RESTORE_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST+1;
    
    private DbxAccountManager mDbxAcctMgr;
    
    private Button bSync;
    private Button bDisconnect;
    private TextView tvUsername;
    private TextView tvlastSync;
    
    private SimpleDateFormat formatTime;
    private ArrayList<String> dbxListFile;
    
    private String DB_PATH = "/data/com.ducphu.note/databases/";  
    private String DB_TEMP_NAME = NotesDbAdapter.DATABASE_NAME + "_temp";
    
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup_cloud_layout);
		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
    	
		bSync = (Button)findViewById(R.id.btnSync);
		bDisconnect = (Button)findViewById(R.id.btnDisconnect);
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvlastSync = (TextView)findViewById(R.id.tvLastSync);
		
		formatTime = new SimpleDateFormat("d-M-y__h'h'm");
		
		bSync.setOnClickListener(this);
		bDisconnect.setOnClickListener(this);	
		registerForContextMenu(getListView());
		if(mDbxAcctMgr.hasLinkedAccount()){			
			try {
				showdatabase();
				getAccountInfo();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getAccountInfo() {
		// TODO Auto-generated method stub
		DbxAccount dbaccount = mDbxAcctMgr.getLinkedAccount();
		tvUsername.setText("User name: " + dbaccount.getAccountInfo().userName);
		Toast.makeText(getApplicationContext(), R.string.toast_loginSuccessfully, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mDbxAcctMgr.hasLinkedAccount()){			
			bSync.setText(R.string.sync_button);				
		}else{
			bSync.setText(R.string.sync_button_signin);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSync:
			if(mDbxAcctMgr.hasLinkedAccount()){		
				new AsyncUpload(BackupCloud.this).execute();
			}else{
				if(checkNetwork(this)){
					connectDropbox();
				}else{
					Toast.makeText(this, "Check network connection!", Toast.LENGTH_SHORT).show();
				}
			}		
			break;
		case R.id.btnDisconnect:
			if(mDbxAcctMgr.hasLinkedAccount()){
				mDbxAcctMgr.unlink();
				Toast.makeText(getApplicationContext(), R.string.toast_disconnect, Toast.LENGTH_SHORT).show();
				tvUsername.setText(R.string.tv_User_disconnect);
			}else{
				Toast.makeText(getApplicationContext(), R.string.toast_noAccountLink, Toast.LENGTH_SHORT).show();
			}	
			break;
		}
	}
	
	private void connectDropbox() {
		// TODO Auto-generated method stub
		mDbxAcctMgr.startLink((SherlockListActivity) this, REQUEST_LINK_TO_DBX);
	}

	/*return after connect successfully with dropbox*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == REQUEST_LINK_TO_DBX){
			if(resultCode == SherlockListActivity.RESULT_OK){
				try {
					showdatabase();
					getAccountInfo();
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Toast.makeText(getApplicationContext(),R.string.toast_linkFailed, Toast.LENGTH_LONG).show();
			}
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, RESTORE_ID, 0, R.string.context_btn_cloud_restore);
		menu.add(0, DELETE_ID, 0, R.string.context_btn_cloud_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item){
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case RESTORE_ID:
			final AdapterContextMenuInfo minfo = (AdapterContextMenuInfo) item.getMenuInfo();
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.contextmenu_restoreTilteCloud)
			.setMessage(R.string.contextmenu_messageCloud)
			.setIcon(R.drawable.ic_dialog_warning)
			.setPositiveButton(R.string.dialog_OK, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new AsyncRestore(BackupCloud.this).execute(minfo.position);
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
			final AdapterContextMenuInfo dinfo = (AdapterContextMenuInfo) item.getMenuInfo();
			try {
				deleteDatabase(dinfo.position);
				showdatabase();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), R.string.toast_deleteSuccessfully, Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	/* Upload datanote to cloud 
	 * and set name database: [Cloud]notedata-system time*/
	private void uploadDatabase() throws IOException {
		// TODO Auto-generated method stub		
	    String filenameBackup = "[Cloud]notesdata-"+ formatTime.format(new Date());
	    DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		
		DbxPath dbpath = new DbxPath(DbxPath.ROOT, "NoteBackup");
		if(!dbxFs.exists(dbpath)){
			dbxFs.createFolder(dbpath);
		}
		DbxFile dbxf = dbxFs.create(new DbxPath(dbpath, filenameBackup));
		File file = new File(Environment.getDataDirectory(), DB_PATH + NotesDbAdapter.DATABASE_NAME);
		dbxf.writeFromExistingFile(file, false);
		dbxf.close();
	}
	
	/* Show list database updated successfully on dropbox*/
	private void showdatabase() throws DbxException{
		// TODO Auto-generated method stub
		DbxFileSystem mdbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		List<DbxFileInfo> infos = mdbxFs.listFolder(new DbxPath(DbxPath.ROOT, "NoteBackup"));
		dbxListFile = new ArrayList<String>();
		for(DbxFileInfo info : infos){
			dbxListFile.add(info.path.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, dbxListFile);
		setListAdapter(adapter);
	}
	
	private void deleteDatabase(int position) throws IOException{
		// TODO Auto-generated method stub
		DbxFileSystem ddbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		DbxPath path = new DbxPath(DbxPath.ROOT, "NoteBackup");
		ddbxFs.delete(new DbxPath(path, dbxListFile.get(position)));
	}

	private void restoreDatabase(int position) throws IOException{
		// TODO Auto-generated method stub
		downloadDatabase(position);
		writeDatabase();		
	}
	
	/* Download database from cloud to device
	 * (data/data/com.ducphu.note/database)
	 * database name: database_temp*/
	private void downloadDatabase(int position) throws IOException {
		// TODO Auto-generated method stub
		DbxFileSystem dbFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		DbxPath selectPath = new DbxPath(DbxPath.ROOT, "NoteBackup");
		DbxFile dbxFile = dbFs.open(new DbxPath(selectPath, dbxListFile.get(position)));
		
		FileInputStream input = dbxFile.getReadStream();
		File file = new File(Environment.getDataDirectory(), DB_PATH + NotesDbAdapter.DATABASE_NAME + "_temp");
		FileOutputStream output = new FileOutputStream(file);
		
		byte[] buffer = new byte[1024];
		int length;
		
		while((length = input.read(buffer)) > 0){
			output.write(buffer,0,length);
		}
		
		output.flush();
		output.close();
		dbxFile.close();
	}

	/* Copy database_temp to datanote of device*/
	private void writeDatabase() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openOrCreateDatabase(DB_TEMP_NAME, SQLiteDatabase.OPEN_READWRITE, null);
		String title_temp;
		String body_temp;
		String date_temp;
		NotesDbAdapter mDbHelper = new NotesDbAdapter(BackupCloud.this);
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
	}
	
	private boolean checkNetwork(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting()){
			return true;
		}
		return false;	
	}
	
	/* AsyncTask for upload database to cloud*/
	public class AsyncUpload extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		
		public AsyncUpload(BackupCloud context) {
			// TODO Auto-generated constructor stub
			pd = new ProgressDialog(context);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.setMessage("Syncing...");
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				uploadDatabase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(pd.isShowing()){
				pd.dismiss();
			}
			try {
				showdatabase();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tvlastSync.setText("Last Synced: " + formatTime.format(new Date()));
			Toast.makeText(getApplicationContext(), "Last Synced: " + formatTime.format(new Date()), Toast.LENGTH_LONG).show();
		}
	}
	
	/* AsyncTask for download and restore database*/
	private class AsyncRestore extends AsyncTask<Integer, Void, Void>{
		ProgressDialog mpd;
		
		public AsyncRestore(BackupCloud context) {
			// TODO Auto-generated constructor stub
			mpd = new ProgressDialog(context);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mpd.setMessage("Restoring...");
			mpd.show();
		}
		
		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int position = params[0].intValue();
			try {
				restoreDatabase(position);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(mpd.isShowing()){
				mpd.dismiss();
			}
			Toast.makeText(getApplicationContext(), R.string.toast_restoreSuccessfully, Toast.LENGTH_SHORT).show();
		}
	}
}
