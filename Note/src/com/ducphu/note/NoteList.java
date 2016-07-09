package com.ducphu.note;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ducphu.note.backupsd.BackupToSD;
import com.ducphu.note.cloud.BackupCloud;

public class NoteList extends SherlockActivity implements OnItemClickListener{

	private static final int CREATE_ID = Menu.NONE;
	private static final int DELETE_ID = Menu.FIRST;
	private static final int PIN_STATUS = Menu.FIRST + 1;
	private static final int UNPIN_STATUS = Menu.FIRST + 2;
	
	private NotesDbAdapter mDbHelper;
	private NotificationManager notifiManager;
	
	private String pinTitle;
	private String pinBody;
	
	private GridView gridview;
	
	private boolean sortOrder = false; // DESC
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_grid);
		gridview = (GridView)findViewById(R.id.grid);
		
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		
		gridview.setOnItemClickListener(this);
		registerForContextMenu(gridview);	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(sortOrder){
			fillData("ASC");
		}else{
			fillData("DESC");
		}
	}

	/* Show list note added*/
		@SuppressWarnings("deprecation")
	private void fillData(String sortOrder) {
		// TODO Auto-generated method stub
		Cursor notesCursor = mDbHelper.readAllNotes(sortOrder);
		startManagingCursor(notesCursor);
		
		String[] from = new String[]{NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_DATE, NotesDbAdapter.KEY_BODY};
		int[] to = new int[]{R.id.tv_note_grid_title, R.id.tv_note_grid_date, R.id.tv_note_grid_body};
			
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.note_colum, notesCursor, from, to);
		gridview.setAdapter(notes);	
		gridview.setEmptyView(findViewById(R.id.empty));
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, CREATE_ID, 0, "Create").setIcon(R.drawable.ic_action_add_note).
		setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		getSupportMenuInflater().inflate(R.menu.activity_item_list, menu);	
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case CREATE_ID:
			createNote();
			break;
		case R.id.add_menu:
			createNote();
			break;
		case R.id.submenu_setting_backup:
			Intent IbackupSD = new Intent(getApplicationContext(), BackupToSD.class);
			startActivity(IbackupSD);
			break;
		case R.id.submenu_setting_cloud:
			Intent IBackupCloud = new Intent(getApplicationContext(), BackupCloud.class);
			startActivity(IBackupCloud);
			break;
		case R.id.sync_menu:	
			Intent Ibackup = new Intent(getApplicationContext(), BackupCloud.class);
			startActivity(Ibackup);
			break;
		case R.id.sort_modified_time:
			sortOrder = false;
			fillData("DESC");
			break;
		case R.id.sort_alphabectically:
			sortOrder = true;
			fillData("ASC");
			break;
		case R.id.submenu_setting_about:
			aboutMe();
			break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		menu.add(0, PIN_STATUS, 0, R.string.contextmenu_pinstatus);
		menu.add(0, UNPIN_STATUS, 0, R.string.contextmenu_unpinstatus);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();		
		switch (item.getItemId()) {
		case DELETE_ID:
				mDbHelper.deleteNote(info.id);	
				if(sortOrder){
					fillData("ASC");
				}else{
					fillData("DESC");
				}
				return true;
		case PIN_STATUS:
				readSingleNote((int)info.id);
				pinNotification(pinTitle, pinBody, (int)info.id, this);
				return true;
		case UNPIN_STATUS:
				cancelNotification((int)info.id,this);
				return true;
		}
		return super.onContextItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	private void readSingleNote(int id) {
		// TODO Auto-generated method stub
		Cursor singleNote;
		try {
			singleNote = mDbHelper.readSingleNote(id);
			startManagingCursor(singleNote);
			pinTitle = singleNote.getString(singleNote.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
			pinBody = singleNote.getString(singleNote.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent_edit = new Intent(getApplicationContext(), NoteEdit.class);
		intent_edit.putExtra(NotesDbAdapter.KEY_ROWID, id);
		startActivity(intent_edit);
	}
	
	private void createNote() {
		// TODO Auto-generated method stub
		Intent intent_create = new Intent(this, NoteEdit.class);
		startActivity(intent_create);
	}
	
	@SuppressWarnings("deprecation")
	private void pinNotification(String title, String body, int id, Context context) {
		// TODO Auto-generated method stub
		notifiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_pinstatus, pinTitle,System.currentTimeMillis());
		Intent iNoteList = new Intent(this, NoteList.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, iNoteList, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(this, title, body, pIntent);
		notifiManager.notify(id, notification);
	}

	private void cancelNotification(int id, Context context) {
		// TODO Auto-generated method stub
		notifiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifiManager.cancel(id);
	}

	private void aboutMe() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(NoteList.this);
		dialog.setContentView(R.layout.about);
		dialog.setTitle("About");		
		dialog.show();
	}
}
