package com.example.bluetoothexample.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer.GridStyle;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluetoothexample.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class BodyTemperatureDetail extends Activity{

	TextView title;
	TextView tvBody;
	
	private static String bodyValue;
	private static double _bodyGraph;
	private static String address;
	
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothAdapter btAdapter;
	private BluetoothSocket btSocket;
	
	private Handler handlerGetData;
	private ConnectedThread mConnectedThread;
	
	//
	Handler mHandler = new Handler();
	Runnable mTimer;
	LineGraphSeries<DataPoint> mSeries1;
	double graph2LastXValue = 10d;
	double mLastRandom = 0;
    Random mRand = new Random();
	
	public BodyTemperatureDetail() {
		// TODO Auto-generated constructor stub
		this.btAdapter = null;
		this.btSocket = null;
	}
	
	@SuppressWarnings("static-access")
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_body_temperature_detail_layout);
		tvBody = (TextView)findViewById(R.id.tv_bodyValue);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar);
		title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
		title.setText("Body temperature");
		
		ImageView bHome = (ImageView)findViewById(R.id.imHome);
		bHome.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				finish();
			}
		});
		
		GraphView graph = (GraphView) findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>();
        mSeries1.setColor(new Color().parseColor("#F57C00"));
        mSeries1.setThickness(10);
        graph.addSeries(mSeries1);
        graph.getGridLabelRenderer().setGridStyle(GridStyle.NONE);
        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(60);
       
        mSeries1.setOnDataPointTapListener(new OnDataPointTapListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onTap(Series arg0, DataPointInterface datapoint) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), datapoint.getY() + "", Toast.LENGTH_SHORT).show();
			}
		});
        
        mTimer = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 1d;
                mSeries1.appendData(new DataPoint(graph2LastXValue, _bodyGraph), true, 60);
                tvBody.setText(bodyValue);
                mHandler.postDelayed(this, 300);          	
            }
        };
        mHandler.postDelayed(mTimer, 300);       
        
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBTState();				
		
		handlerGetData = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if (msg.what == 1) {
					byte[] buf = (byte[]) msg.obj;
					int begin = (int) msg.arg1;
					int end = (int) msg.arg2;

					if (begin == 0 && end == 19) {
						String readmsg = new String(buf);
						readmsg = readmsg.substring(begin, end);

						bodyValue = readmsg.substring(6, 11);
						_bodyGraph = Double.parseDouble(bodyValue);
//						pulseValue = readmsg.substring(3, 5);
//						temperatureValue = readmsg.substring(6, 11);
//						ambientValue = readmsg.substring(12, 17);

						readmsg = "";
						
					}
				}
			}
		};
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
	
	private void checkBTState() {
		// TODO Auto-generated method stub
		if (this.btAdapter == null) {
			Toast.makeText(getBaseContext(), "Not support!", Toast.LENGTH_SHORT).show();
			finish();
		} else if (!this.btAdapter.isEnabled()) {
			startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
		}
	}

	private void connectSocket() {
		// TODO Auto-generated method stub
//		SharedPreferences prefs = getSharedPreferences("ListPrefMacAddr", Context.MODE_PRIVATE);
//		address = prefs.getString("addressKey", null);
		
		Intent getAddrress = getIntent();
		address = getAddrress.getStringExtra("_keyaddress");
		
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
							handlerGetData.obtainMessage(1, begin, i, buffer).sendToTarget();
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
