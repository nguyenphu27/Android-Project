package com.example.bluetoothexample.cloud;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.example.bluetoothexample.cloud.ResponseServer;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluetoothexample.R;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TemperatureCloudActivity extends Activity{

	private ListView listPulse;
	private ArrayList<HashMap<String, String>> arrayListPulse;
	private ResponseServer response;
	private static final String OBJECT1 = "nhietdo";
	private static final String OBJECT2 = "moitruong";
	
	private static final String OBJECT3 = "ngay";

	private LineGraphSeries<DataPoint> mSeriesTemperature;
	private LineGraphSeries<DataPoint> mSeriesAmbient;
	
	private GraphView graph;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cloud_pulse);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar);
		TextView title = (TextView) findViewById(
				getResources().getIdentifier("action_bar_title", "id", getPackageName()));
		title.setText("Nhiệt độ - thống kê");

		listPulse = (ListView) findViewById(R.id.list_pulse_cloud);
		arrayListPulse = new ArrayList<HashMap<String, String>>();

		graph = (GraphView) findViewById(R.id.graph_pulse_cloud);

		graph.setTitle("Nhiệt độ");
		graph.setTitleColor(Color.WHITE);
		graph.getViewport().setXAxisBoundsManual(true);
		graph.getViewport().setMinX(0);
		graph.getViewport().setMaxX(10);

		graph.setBackgroundColor(new Color().parseColor("#FF9100"));
		graph.getGridLabelRenderer().setGridColor(Color.WHITE);
		graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
		graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);

		// StaticLabelsFormatter staticLabel = new StaticLabelsFormatter(graph);
		// staticLabel.setHorizontalLabels(new String[]
		// {"ko","mot","hai","ba"});
		// graph.getGridLabelRenderer().setLabelFormatter(staticLabel);

		ImageView bHome = (ImageView) findViewById(R.id.imHome);
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
		if(!checkNetwork(getBaseContext())){
			Toast.makeText(getApplicationContext(), "Please! Check your network.", Toast.LENGTH_LONG).show();
		}else{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						response = new ResponseServer();
						String _json = response.getJson();
						response.getArrayList(_json, arrayListPulse, OBJECT1, OBJECT2, OBJECT3);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ListAdapter adapter = new SimpleAdapter(getApplicationContext(), arrayListPulse,
									R.layout.single_temperature_cloud, new String[] { OBJECT1, OBJECT2 , OBJECT3},
									new int[] { R.id.tv_value_temperature_cloud,R.id.tv_value_ambient_cloud, R.id.tv_value_date_temp_cloud });
							listPulse.setAdapter(adapter);

							mSeriesTemperature = new LineGraphSeries<>(getDataSystolicPointServer());
							mSeriesTemperature.setColor(Color.CYAN);
							mSeriesTemperature.setDrawDataPoints(true);
							mSeriesTemperature.setDataPointsRadius(10);
							mSeriesTemperature.setTitle("Cơ thể");

							graph.addSeries(mSeriesTemperature);
							
							mSeriesAmbient = new LineGraphSeries<>(getDataDiastolicPointServer());
							mSeriesAmbient.setColor(Color.YELLOW);
							mSeriesAmbient.setDrawDataPoints(true);
							mSeriesAmbient.setDataPointsRadius(10);
							mSeriesAmbient.setTitle("Môi trường");

							graph.addSeries(mSeriesAmbient);
							
							graph.getLegendRenderer().setVisible(true);
							graph.getLegendRenderer().setBackgroundColor(Color.WHITE);
							graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);							
						}
					});
				}
			}).start();
		}
	}

	private DataPoint[] getDataSystolicPointServer(){
		DataPoint[] point = new DataPoint[arrayListPulse.size()];
		
		for(int i=0; i< arrayListPulse.size(); i++){
			double x = i;
			double y = Double.parseDouble(arrayListPulse.get(i).get(OBJECT1));
			
			DataPoint _point = new DataPoint(x, y);
			point[i] = _point;
		}
		return point;
	}
	
	private DataPoint[] getDataDiastolicPointServer(){
		DataPoint[] point = new DataPoint[arrayListPulse.size()];
		
		for(int i=0; i< arrayListPulse.size(); i++){
			double x = i;
			double y = Double.parseDouble(arrayListPulse.get(i).get(OBJECT2));
			
			DataPoint _point = new DataPoint(x, y);
			point[i] = _point;
		}
		return point;
	}

	private boolean checkNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}

