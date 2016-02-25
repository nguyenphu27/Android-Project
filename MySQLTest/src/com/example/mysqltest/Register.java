package com.example.mysqltest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{

	private EditText user, pass;
	private Button mRegister;
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
	private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/register.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		user = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
		
		mRegister = (Button)findViewById(R.id.register);
		mRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		new CreateUser().execute();
	}
	
	class CreateUser extends AsyncTask<String, String, String>{
		
		boolean failure = false;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Register.this);
			pDialog.setMessage("Creating User...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			int success;
			String username  = user.getText().toString();
			String password = pass.getText().toString();			
			try{
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				//
				JSONObject json = jsonParser.makeHttpRequest(
						LOGIN_URL,"POST",params);
				//
				success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					finish();					
					return json.getString(TAG_MESSAGE);
				}else{
					//login failure
					return json.getString(TAG_MESSAGE);					
				}
				
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result != null){
				Toast.makeText(Register.this, result, Toast.LENGTH_LONG).show();
			}
		}

		
	}
}
