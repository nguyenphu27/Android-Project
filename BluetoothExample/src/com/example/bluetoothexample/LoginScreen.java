package com.example.bluetoothexample;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

public class LoginScreen extends Activity{
	TextView tvHelp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		tvHelp = (TextView)findViewById(R.id.txthelp);
		tvHelp.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		
	}
}
