package com.clouddemo.mainapp;


import java.io.IOException;
import java.util.Date;

import com.clouddemo.noteendpoint.Noteendpoint;
import com.clouddemo.noteendpoint.model.Note;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.os.AsyncTask;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;

import com.clouddemo.CloudEndpointUtils;
import com.clouddemo.R;

public class AddNodeActivity extends Activity {
	String mail;
	String des;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final EditText etMail = (EditText)findViewById(R.id.etEmail);
        final EditText etDes = (EditText)findViewById(R.id.etDes);
        Button bPush = (Button)findViewById(R.id.bPush);
        
        bPush.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mail = etMail.getText().toString();
				des = etDes.getText().toString();
				
				new EndpointsTask().execute(getApplicationContext());
				Toast.makeText(getApplicationContext(), "Push OK!", Toast.LENGTH_SHORT).show();
			}
		});
    }
    
    public class EndpointsTask extends AsyncTask<Context, Integer, Long> {
        protected Long doInBackground(Context... contexts) {

          Noteendpoint.Builder endpointBuilder = new Noteendpoint.Builder(
              AndroidHttp.newCompatibleTransport(),
              new JacksonFactory(),
              new HttpRequestInitializer() {
              public void initialize(HttpRequest httpRequest) { }
              });
      Noteendpoint endpoint = CloudEndpointUtils.updateBuilder(
      endpointBuilder).build();
      try {
          Note note = new Note().setDescription(des);
          String noteID = new Date().toString();
          note.setId(noteID);

          note.setEmailAddress(mail);      
          Note result = endpoint.insertNote(note).execute();
      } catch (IOException e) {
        e.printStackTrace();
      }
          return (long) 0;
        }
    }
}