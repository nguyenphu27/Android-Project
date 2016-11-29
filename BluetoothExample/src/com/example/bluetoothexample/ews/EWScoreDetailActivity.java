package com.example.bluetoothexample.ews;

import com.example.bluetoothexample.R;
import com.example.bluetoothexample.customadapter.CustomEWSList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EWScoreDetailActivity extends ListActivity{

	private TextView title;
	
	private String[] heartIndex; 
	private String[] heartScore;
	
	private String[] oxyIndex; 
	private String[] oxyScore;
	
	private String[] temperIndex; 
	private String[] temperScore;
	
	private String[] systolicIndex; 
	private String[] systolicScore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ews_score_detail);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar);
		title = (TextView) findViewById(
				getResources().getIdentifier("action_bar_title", "id", getPackageName()));	
		
		heartIndex = getResources().getStringArray(R.array.heartIndex);
		heartScore = getResources().getStringArray(R.array.heartScore);
		
		oxyIndex = getResources().getStringArray(R.array.oxyIndex);
		oxyScore = getResources().getStringArray(R.array.oxyScore);
		
		temperIndex = getResources().getStringArray(R.array.temperIndex);
		temperScore = getResources().getStringArray(R.array.temperScore);
		
		systolicIndex = getResources().getStringArray(R.array.systolicIndex);
		systolicScore = getResources().getStringArray(R.array.systolicScore);
		
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
		
		Intent i = getIntent();
		int mIntent = i.getIntExtra("mintent", 0);
		
		switch (mIntent) {
		case 0:
			title.setText("EWS - Điểm nhịp tim");	
			setListAdapter(new CustomEWSList(getApplicationContext(), heartIndex, heartScore));
			break;
		case 1:
			title.setText("EWS - Điểm nồng độ oxy máu");	
			setListAdapter(new CustomEWSList(getApplicationContext(), oxyIndex, oxyScore));
			break;
		case 2:
			title.setText("EWS - Điểm nhiệt độ");	
			setListAdapter(new CustomEWSList(getApplicationContext(), temperIndex, temperScore));
			break;
		case 3:
			title.setText("EWS - Điểm tâm thu");	
			setListAdapter(new CustomEWSList(getApplicationContext(), systolicIndex, systolicScore));
			break;
		default:
			break;
		}
	}
	
	
}
