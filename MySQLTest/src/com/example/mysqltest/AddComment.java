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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddComment extends Activity implements OnClickListener{

	private EditText etitle, emessage;
	private Button bSubmit;
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
	private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/addcomment.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment);
		
		etitle = (EditText)findViewById(R.id.title);
		emessage = (EditText)findViewById(R.id.message);
		
		bSubmit = (Button)findViewById(R.id.bsubmit);
		bSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		new PostComment().execute();
	}
	
	class PostComment extends AsyncTask<String, String, String>{
		
		boolean failure = false;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(AddComment.this);
			pDialog.setMessage("Posting comment...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			int success;
			String ctitle  = etitle.getText().toString();
			String cmessage = emessage.getText().toString();
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(AddComment.this);
			String user = sp.getString("username", "xyz");
			try{
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", user));
				params.add(new BasicNameValuePair("title", ctitle));
				params.add(new BasicNameValuePair("message", cmessage));			
				//
				JSONObject json = jsonParser.makeHttpRequest(
						LOGIN_URL,"POST",params);
				//
				success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					//finish();					
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
				Toast.makeText(AddComment.this, result, Toast.LENGTH_LONG).show();
			}
		}

		
	}
}
