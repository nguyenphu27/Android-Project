package com.example.bluetoothexample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class SensorActivity extends Activity implements OnItemClickListener {
	
	private static String oxygenValue;
	private static String pulseValue;
	private static String temperatureValue;
	private static String ambientValue;
	private static String address;
	
	private ListView listSensor;

	private String[] array = new String[] { "%SpO2", "PR BPM", "Body \u00b0C", "Ambient \u00b0C" };
	private String[] data = new String[] { "..", "..", "...", "..." };

	private ArrayList<HashMap<String, String>> listdataSensor;

	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothAdapter btAdapter;
	private BluetoothSocket btSocket;
	
	private Handler handlerGetData;
	private ConnectedThread mConnectedThread;
	
	Timer timerSetListView;
	private DatabaseAdapter mDbHelper;
	private static int counterForSave;

	SimpleDateFormat dateformat;
	
	public SensorActivity() {
		// TODO Auto-generated constructor stub
		this.btAdapter = null;
		this.btSocket = null;
	}

	@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_layout);
		listSensor = (ListView) findViewById(R.id.listSensorValue);		
		listdataSensor = new ArrayList<HashMap<String, String>>();
		
		dateformat = new SimpleDateFormat("h':'m':'s d'-'M'-'y");		
		
		for (int i = 0; i < array.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("namesensor", array[i]);
			map.put("valuesensor", data[i]);
			listdataSensor.add(map);
		}

		ListAdapter adapter = new SimpleAdapter(getApplicationContext(), listdataSensor, R.layout.single_row_listsensor,
				new String[] { "namesensor", "valuesensor" }, new int[] { R.id.tv_namesensor, R.id.tv_valuesensor });
		listSensor.setAdapter(adapter);
		listSensor.setOnItemClickListener(this);

		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBTState();
			
		handlerGetData = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0) {
					byte[] buf = (byte[]) msg.obj;
					int begin = (int) msg.arg1;
					int end = (int) msg.arg2;

					if (begin == 0 && end == 19) {
						String readmsg = new String(buf);
						readmsg = readmsg.substring(begin, end);

						oxygenValue = readmsg.substring(0, 2);
						pulseValue = readmsg.substring(3, 5);
						temperatureValue = readmsg.substring(6, 11);
						ambientValue = readmsg.substring(12, 17);

						readmsg = "";
						//tvSensor.setText(oxygenValue + " " + pulseValue + " " + temperatureValue + " " + ambientValue);
						Log.d("OxygenValueSensor", oxygenValue);
					}
				}
			}
		};

		Timer timerSetListView = new Timer();
		timerSetListView.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() { // TODO Auto-generated method stub

				runOnUiThread(new Runnable() {

					@Override
					public void run() { // TODO Auto-generated method stub
						if (address != null) {
							listdataSensor.clear();
							counterForSave++;
							if(counterForSave >= 37){
								mDbHelper.insertData(oxygenValue+" %", pulseValue +" bpm", temperatureValue+" \u00b0C", ambientValue+" \u00b0C", "" + dateformat.format(new Date(System.currentTimeMillis())));
								counterForSave = 0;								
							}
							
							for (int i = 0; i < 4; i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("namesensor", array[i]);
								switch (i) {
								case 0:
									map.put("valuesensor", oxygenValue);
									break;
								case 1:
									map.put("valuesensor", pulseValue);
									break;
								case 2:
									map.put("valuesensor", temperatureValue);
									break;
								case 3:
									map.put("valuesensor", ambientValue);
									break;
								}				
								listdataSensor.add(map);
							}
							((BaseAdapter) listSensor.getAdapter()).notifyDataSetChanged();
							
						}
					}
				});
			}
		}, 0, 300);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		if (address != null) {
			switch (position) {
			case 0:
				Intent iOxygen = new Intent("com.example.bluetoothexample.bluetooth.BLOOD");
				iOxygen.putExtra("_keyaddress", address);
				startActivity(iOxygen);
				break;
			case 1:
				Intent ipulse = new Intent("com.example.bluetoothexample.bluetooth.PULSERATE");
				ipulse.putExtra("_keyaddress", address);
				startActivity(ipulse);
				break;
			case 2:
				Intent iBody = new Intent("com.example.bluetoothexample.bluetooth.TEMPERATURE");
				iBody.putExtra("_keyaddress", address);
				startActivity(iBody);
				break;
			case 3:
				Intent iamb = new Intent("com.example.bluetoothexample.bluetooth.AMBIENT");
				iamb.putExtra("_keyaddress", address);
				startActivity(iamb);
				break;
			}
		}
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

	private void checkBTState() {
		// TODO Auto-generated method stub
		if (this.btAdapter == null) {
			Toast.makeText(getBaseContext(), "Not support!", Toast.LENGTH_SHORT).show();
			finish();
		} else if (!this.btAdapter.isEnabled()) {
			startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mDbHelper = new DatabaseAdapter(this);
		mDbHelper.open();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectSocket();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		disconnectSocket();	
	}

	private void disconnectSocket() {
		// TODO Auto-generated method stub
		if (address != null) {
			try {
				this.btSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void connectSocket() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = getSharedPreferences("ListPrefMacAddr", Context.MODE_PRIVATE);
		address = prefs.getString("addressKey", null);
		if (address != null) {
			Toast.makeText(getApplicationContext(), "Connected address: " + address, Toast.LENGTH_SHORT).show();
			try {
				this.btSocket = createBluetoothSocket(this.btAdapter.getRemoteDevice(address));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.btAdapter.cancelDiscovery();
			try {
				this.btSocket.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					this.btSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			this.mConnectedThread = new ConnectedThread(this.btSocket);
			this.mConnectedThread.start();
		} else {
			Toast.makeText(getApplicationContext(), "Device not available!", Toast.LENGTH_LONG).show();
			return;
		}
	}


	@SuppressLint("NewApi")
	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
		if (VERSION.SDK_INT >= 10) {
			try {
				return (BluetoothSocket) device.getClass()
						.getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class })
						.invoke(device, new Object[] { MY_UUID });
			} catch (Exception e) {
			}
		}
		return device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
	}

	private class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		@SuppressWarnings("unused")
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.mmInStream = tmpIn;
			this.mmOutStream = tmpOut;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			byte[] buffer = new byte[1024];
			int bytes = 0;
			int begin = 0;

			while (true) {
				try {
					bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);

					for (int i = begin; i < bytes; i++) {
						if (buffer[i] == "#".getBytes()[0]) {
							handlerGetData.obtainMessage(0, begin, i, buffer).sendToTarget();		
							begin = i + 1;
							if (i == bytes - 1) {
								bytes = 0;
								begin = 0;
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					break;
				}
			}
		}
	}
	
}
