package com.example.bluetoothexample;

import com.example.bluetoothexample.customadapter.CustomGrid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class CloudActivity extends Activity{

	GridView grid;
	String[] title = {"Nhịp tim", "Nhiệt độ", "SpO2", "Huyết áp" ,"Chiều cao","Cân nặng", "EWS", "Chuẩn đoán \n   bệnh tim"};
	int[] icon = {R.drawable.grid_pulse,
				  R.drawable.grid_temper,
				  R.drawable.grid_oxy,			
				  R.drawable.grid_blood,
				  R.drawable.grid__height,
				  R.drawable.grid_weight,
				  R.drawable.grid_assessment,
				  R.drawable.grid_diagnostic};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cloud);
		
		CustomGrid adapter = new CustomGrid(getApplicationContext(), title, icon, 0);
		
		grid = (GridView)findViewById(R.id.gridViewCloud);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long Id) {
				// TODO Auto-generated method stub				
				switch (position) {
				case 0:
					Intent ipulse = new Intent("com.example.bluetoothexample.cloud.PULSERATE");
					startActivity(ipulse);
					break;
				case 1:
					Intent itemper = new Intent("com.example.bluetoothexample.cloud.TEMPERATURE");
					startActivity(itemper);
					break;
				case 2:
					Intent ispo2 = new Intent("com.example.bluetoothexample.cloud.SPO2");
					startActivity(ispo2);
					break;
				case 3:
					Intent iblood = new Intent("com.example.bluetoothexample.cloud.BLOOD");
					startActivity(iblood);
					break;
				case 6:
					Intent iews = new Intent("com.example.bluetoothexample.ews.EWS");
					startActivity(iews);
					break;
				default:
					Toast.makeText(getApplicationContext(), "OK! " + position, Toast.LENGTH_SHORT).show();
					break;
				}
			}
			
		});
	}
}
