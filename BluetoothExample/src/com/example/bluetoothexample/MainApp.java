package com.example.bluetoothexample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MainApp extends TabActivity {

	TabHost tabhost;
	Intent intent;
	TextView title;
	SharedPreferences sharepreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		sharepreferences = getSharedPreferences("ListPrefMacAddr", Context.MODE_PRIVATE);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_formain);		
		title = (TextView) findViewById(getResources().getIdentifier("action_bar_title_main", "id", getPackageName()));
		title.setText("Dữ liệu");
		
		tabhost = getTabHost();

		intent = new Intent(this, CloudActivity.class);
		setupTab(new TextView(this), "Dữ liệu", intent, 0);

		intent = new Intent(this, SensorActivity.class);
		setupTab(new TextView(this), "Cảm biến", intent, 1);

		intent = new Intent(this, History.class);
		setupTab(new TextView(this), "Lịch sử", intent, 2);

		intent = new Intent(this, Setting.class);
		setupTab(new TextView(this), "Cài đặt", intent, 3);

		tabhost.setCurrentTab(0);

		tabhost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabid) {
				// TODO Auto-generated method stub
				if (tabid.equals("Dữ liệu")) {							
					title.setText("Dữ liệu");
				}
				if (tabid.equals("Cảm biến")) {
					title.setText("Cảm biến");
				}
				if (tabid.equals("Lịch sử")) {				
					title.setText("Lịch sử");
				}
				if (tabid.equals("Cài đặt")) {
					title.setText("Cài đặt");
				}
			}
		});
	}

	private void setupTab(final View view, final String tag, final Intent myIntent, int icon) {

		View tabview = createTabView(tabhost.getContext(), tag, icon);
		TabSpec setContent = tabhost.newTabSpec(tag).setIndicator(tabview).setContent(myIntent);
		tabhost.addTab(setContent);
	}

	@SuppressLint("InflateParams")
	private static View createTabView(final Context context, final String text, int icon) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_single, null);
		TextView tv = (TextView) view.findViewById(R.id.title);
		tv.setText(text);
		if (icon == 0) {
			tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_diagnostic_selector, 0, 0);
		}
		if (icon == 1) {
			tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_sensor_selector, 0, 0);
		}
		if (icon == 2) {
			tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_history_selector, 0, 0);
		}
		if (icon == 3) {
			tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_setting_selector, 0, 0);
		}
		return view;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SharedPreferences.Editor editor = sharepreferences.edit();			
		editor.clear();
		editor.commit();
	}
}
