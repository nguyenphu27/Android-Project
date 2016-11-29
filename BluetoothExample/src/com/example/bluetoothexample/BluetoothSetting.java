package com.example.bluetoothexample;

import java.util.Set;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class BluetoothSetting extends Activity{

	BluetoothAdapter btAdapter;
	ListView listdevice;
	ArrayAdapter<String> adapter;
	SharedPreferences sharedpreferences;
	TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devicelist);
		listdevice = (ListView) findViewById(R.id.devicelist);
		
		sharedpreferences = getSharedPreferences("ListPrefMacAddr", Context.MODE_PRIVATE);
		registerForContextMenu(listdevice);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar);
		title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
		title.setText("Bluetooth");
		
		ImageView bHome = (ImageView)findViewById(R.id.imHome);
		bHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkBluetoothState();

		adapter = new ArrayAdapter<String>(this, R.layout.device_name);
		listdevice.setAdapter(adapter);
		
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairDevice = btAdapter.getBondedDevices();
		if (pairDevice.size() > 0) {
			for (BluetoothDevice device : pairDevice) {
				adapter.add(device.getName() + "\n" + device.getAddress());
			}
			return;
		}
		String noDevice = "No device!";
		adapter.add(noDevice);
	}

	private void checkBluetoothState() {
		// TODO Auto-generated method stub
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (this.btAdapter == null) {
			Toast.makeText(getApplicationContext(), "Device not support bluetooth!", Toast.LENGTH_SHORT).show();
		} else if (!this.btAdapter.isEnabled()) {
			Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(btIntent, 1);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, "Get Address");
		menu.add(0, v.getId(), 0, "Clear Address");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getTitle() == "Get Address"){
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			String _address = adapter.getItem(info.position);
			String address = _address.substring(_address.length() - 17);
			
			SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putString("addressKey", address);			
			editor.commit();
			
			Toast.makeText(getApplicationContext(), "Get address: " + address +" /n Success!", Toast.LENGTH_SHORT).show();
		}else if(item.getTitle() == "Clear Address"){
			SharedPreferences.Editor editor = sharedpreferences.edit();			
			editor.clear();
			editor.commit();
			
			Toast.makeText(getApplicationContext(), "Clear address success!", Toast.LENGTH_SHORT).show();
		}else{
			return false;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}

}
