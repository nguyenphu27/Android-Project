package com.example.bluetoothexample;

import com.example.bluetoothexample.circularprogressbar.CircleProgressBar;

import android.app.Activity;
import android.os.Bundle;

public class TestLayout extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		
		CircleProgressBar circlebar = (CircleProgressBar) findViewById(R.id.custom_progressBar);
		circlebar.setProgress(13);
		circlebar.setLabelCircle("07/20");
	}
}
