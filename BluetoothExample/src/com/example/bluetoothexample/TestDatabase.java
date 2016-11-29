package com.example.bluetoothexample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class TestDatabase extends Activity{

	private DatabaseAdapter mDbHelper;
	
	private ListView listHistory;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		
		listHistory = (ListView)findViewById(R.id.listHistory);
		
		mDbHelper = new DatabaseAdapter(this);
		mDbHelper.open();	
		
		mDbHelper.deleteAllData();
		//mDbHelper.insertData("98 %", "75 bpm", "36.5 \u00b0C", "29 \u00b0C", dateCur);
		//mDbHelper.insertData("97 %", "74 bpm", "35.5 \u00b0C", "28 \u00b0C", dateCur);
		//mDbHelper.insertData("96 %", "73 bpm", "37.5 \u00b0C", "27 \u00b0C", dateCur);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fillData();
	}

	@SuppressWarnings("deprecation")
	private void fillData() {
		// TODO Auto-generated method stub
		Cursor mCursor = mDbHelper.readAllData();
		startManagingCursor(mCursor);
		
		String[] from = new String[] {DatabaseAdapter.KEY_SPO2, DatabaseAdapter.KEY_PULSE,
				DatabaseAdapter.KEY_BODY,DatabaseAdapter.KEY_AMBIENT, DatabaseAdapter.KEY_DATE};
		
		int[] to = new int[]{R.id.tv_spo2History, R.id.tv_pulseHistory,
				R.id.tv_bodyHistory, R.id.tv_ambientHistory, R.id.tv_dateHistory};
	
		SimpleCursorAdapter listdata = new SimpleCursorAdapter(this, 
				R.layout.single_row_history, mCursor, from, to);
		listHistory.setAdapter(listdata);		
	}
}
