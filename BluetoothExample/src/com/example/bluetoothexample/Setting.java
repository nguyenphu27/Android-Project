package com.example.bluetoothexample;

import com.example.bluetoothexample.customadapter.CustomGrid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Setting extends Activity {

	ListView listSetting;
	String[] title = new String[] { "Bluetooth", "Xóa dữ liệu","Giúp đỡ", "Liên hệ"};
	int[] icon = {R.drawable.ic_bluetooth_setting,
				  R.drawable.ic_clear_setting,
				  R.drawable.ic_help_setting,
				  R.drawable.ic_about_setting};
	
	private DatabaseAdapter mDbHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		listSetting = (ListView) findViewById(R.id.listSetting);
		
		mDbHelper = new DatabaseAdapter(this);
		mDbHelper.open();	
		
		listSetting.setAdapter(new CustomGrid(getApplicationContext(), title, icon, 1));

		listSetting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					Intent ibluetooth = new Intent(getApplicationContext(), BluetoothSetting.class);
					startActivity(ibluetooth);
					break;
				case 1:						
					new AlertDialog.Builder(getParent())
						.setTitle("Delete Data")
						.setMessage("Are you sure?")
						.setPositiveButton("Yes", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								mDbHelper.deleteAllData();
								Toast.makeText(getApplicationContext(), "Delete data success!", Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton("Cancle", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
					
					break;
				case 2:

					break;
				case 3:

					break;
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.DONUT && keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		finish();
	}
}
