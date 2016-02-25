package com.clouddemo.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.clouddemo.R;

public class MainApp extends Activity implements OnClickListener{

	Button bAdd;
	Button bList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainapp);
		
		bAdd = (Button)findViewById(R.id.bAddNote);
		bList = (Button)findViewById(R.id.bListNote);
		
		bAdd.setOnClickListener(this);
		bList.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bAddNote:
			Intent inAdd = new Intent(getApplicationContext(), AddNodeActivity.class);
			startActivity(inAdd);
			break;
		case R.id.bListNote:
			Intent inList = new Intent(getApplicationContext(), ListNoteActivity.class);
			startActivity(inList);
			break;
		default:
			break;
		}
	}

}
