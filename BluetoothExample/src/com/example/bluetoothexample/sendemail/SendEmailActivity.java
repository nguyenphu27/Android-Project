package com.example.bluetoothexample.sendemail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.bluetoothexample.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendEmailActivity extends Activity{

	Button bsend;
	EditText ename;
	EditText ephone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendemail);
		
		ename = (EditText)findViewById(R.id.et_name);
		ephone = (EditText)findViewById(R.id.et_phone);
		
		bsend = (Button)findViewById(R.id.bSendEmail);
		bsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String name = ename.getText().toString();
				String phone = ephone.getText().toString();
				
				String columnString =   "\"PersonName\",\"Gender\",\"Street1\",\"postOffice\",\"Age\"";
				String dataString   =   "\"" + "ducphu" +"\",\"" + "male" + "\",\"" + "thuduc" + "\",\"" + "linhtrung"+ "\",\"" + "22" + "\"";
				String combinedString = columnString + "\n" + dataString;
				
				File file = null;
				File root = Environment.getExternalStorageDirectory();
				
				if(root.canWrite()){
					File dir = new File(root.getAbsolutePath() + "/PersonData");
					dir.mkdir();
					file = new File(dir,"Data.csv");
					FileOutputStream out = null;
					
					try {
						out = new FileOutputStream(file);
					} catch (FileNotFoundException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					try {
						out.write(combinedString.getBytes());
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					try {
						out.close();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				
				Uri uri = null;
				uri = Uri.fromFile(file);
				
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
				sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi ducphu,");
				sendIntent.setType("text/html");
				startActivity(sendIntent);
			}
		});
	}
}
