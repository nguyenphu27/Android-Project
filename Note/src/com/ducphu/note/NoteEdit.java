package com.ducphu.note;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class NoteEdit extends SherlockActivity{

	private EditText mTitleText;
	private EditText mBodyText;
	private TextView mDateText;
	
	private NotesDbAdapter mDbHelper;
	private String dateCur = "";
	private Long mRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		
		setContentView(R.layout.note_edit);
		mTitleText = (EditText)findViewById(R.id.et_title_note);
		mBodyText = (EditText)findViewById(R.id.et_body_note);
		mDateText = (TextView)findViewById(R.id.tv_edit_date);
		
		mRowId = (savedInstanceState == null) ? null :
			(Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
		
		Bundle extras = getIntent().getExtras();
		if(mRowId == null){
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
										:null;
		}
		
		long msTime = System.currentTimeMillis();
		Date currentDateTime = new Date(msTime);
		
		SimpleDateFormat dateformat = new SimpleDateFormat("h':'m d'-'M'-'y");
		dateCur = dateformat.format(currentDateTime);
		mDateText.setText(dateCur);
	}
	
	/* Check note to edit or ignore if new create*/
	@SuppressWarnings("deprecation")
	private void populateField() throws Exception {
		// TODO Auto-generated method stub
		if(mRowId != null){
			Cursor note = mDbHelper.readSingleNote(mRowId);
			startManagingCursor(note);
			mTitleText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
			mBodyText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
	}

	/* Save data after press back button*/
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveState();
		Toast.makeText(getApplicationContext(), R.string.toast_saved, Toast.LENGTH_SHORT).show();;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			populateField();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Add or update note*/
	private void saveState() {
		// TODO Auto-generated method stub
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		
		if(title.equals("")){
			title = body;
		}

		if(mRowId == null){
			long id = mDbHelper.createNote(title, body, dateCur);
			if(id > 0){
				mRowId = id;
			}
		}else{
			mDbHelper.updateNote(mRowId, title, body, dateCur);
		}
	}
	

}
