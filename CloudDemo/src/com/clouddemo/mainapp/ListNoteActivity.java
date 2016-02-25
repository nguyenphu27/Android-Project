package com.clouddemo.mainapp;

import com.clouddemo.CloudEndpointUtils;
import com.clouddemo.R;
import com.clouddemo.noteendpoint.Noteendpoint;
import com.clouddemo.noteendpoint.model.CollectionResponseNote;
import com.clouddemo.noteendpoint.model.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListNoteActivity extends Activity{
	
	 private ListView listview;
     private String[] from = {"description","emailAdress"};
     private int[] to = { R.id.id_des, R.id.id_email};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.listnote);
		
		listview = (ListView)findViewById(R.id.listNoteId);
		new ListItems().execute();
	}

	private class ListItems 
    extends AsyncTask<Void, Void, CollectionResponseNote> {
		Exception exceptionThrown = null;
		protected CollectionResponseNote doInBackground(Void... args) {
		Noteendpoint.Builder endpointBuilder = new Noteendpoint.Builder(
		          AndroidHttp.newCompatibleTransport(),
		          new JacksonFactory(),
		          new HttpRequestInitializer() {
		          public void initialize(HttpRequest httpRequest) { }
		          });
		  Noteendpoint endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		  try {
			  CollectionResponseNote note=endpoint.listNote().execute();
			  return note;
		  } catch (IOException e) {
			  exceptionThrown=e;
			  e.printStackTrace();
			  return null;
		  }
		      
		    }
		protected void onPostExecute(CollectionResponseNote notes){
		
			 if (exceptionThrown != null) {
			        Log.e(ListNoteActivity.class.getName(), 
			            "Exception when listing Messages", exceptionThrown);
			        
			      }
			      else {
			    	  ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
			    	  
                      List<Note> _list = notes.getItems();
                           for (Note note : _list) {
                                   HashMap<String, String> item = new HashMap<String, String>();
                                   item.put("emailAdress", note.getEmailAddress());
                                   item.put("description", note.getDescription());
                                list.add(item);                              
                           }
                           
                     ListAdapter adapter = new SimpleAdapter(getApplicationContext(), list, 
                    		 R.layout.single_post_item, from, to);  
                     listview.setAdapter(adapter);            
			        }
			      }
		
		}
		
  }   



