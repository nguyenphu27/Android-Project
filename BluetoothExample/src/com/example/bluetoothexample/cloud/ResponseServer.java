package com.example.bluetoothexample.cloud;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

@SuppressWarnings("deprecation")
public class ResponseServer {

private String URL = "http://uhealth123.96.lt/uhealth/get/getData.php";	

public String getJson(){
		
		String result = "";
    	InputStream isr = null;
    	try{
			
			HttpClient httpclient = new DefaultHttpClient();
            //http://10.0.2.2:1234/PHPSCRIPTS/getAllCustomers.php
			HttpPost httppost = new HttpPost(URL); //YOUR PHP SCRIPT ADDRESS 
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
            isr = entity.getContent();
    }
    catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());          
    }
    //convert response to string
    try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            isr.close();
     
            result=sb.toString();
    }
    catch(Exception e){
            Log.e("log_tag", "Error  converting result "+e.toString());
    }
    return result;
  }

public void getArrayList(String _json, ArrayList<HashMap<String, String>> array, String object1, String  object2) throws JSONException{
	
	JSONArray jArray = new JSONArray(_json);
	for (int i = 0; i < jArray.length(); i++) {
		JSONObject json = jArray.getJSONObject(i);
		HashMap<String, String> map = new HashMap<String, String>();

		String _object1 = json.getString(object1);
		String _object2 = json.getString(object2);

		map.put(object1, _object1);
		map.put(object2, _object2);

		array.add(map);
	}
}

public void getArrayList(String _json, ArrayList<HashMap<String, String>> array, String object1, String  object2, String object3) throws JSONException{
	
	JSONArray jArray = new JSONArray(_json);
	for (int i = 0; i < jArray.length(); i++) {
		JSONObject json = jArray.getJSONObject(i);
		HashMap<String, String> map = new HashMap<String, String>();

		String _object1 = json.getString(object1);
		String _object2 = json.getString(object2);
		String _object3 = json.getString(object3);

		map.put(object1, _object1);
		map.put(object2, _object2);
		map.put(object3, _object3);

		array.add(map);
		}
	}

public void getArrayList(String _json, ArrayList<HashMap<String, String>> array, String object1, String  object2, String object3, String object4) throws JSONException{
	
	JSONArray jArray = new JSONArray(_json);
	for (int i = 0; i < jArray.length(); i++) {
		JSONObject json = jArray.getJSONObject(i);
		HashMap<String, String> map = new HashMap<String, String>();

		String _object1 = json.getString(object1);
		String _object2 = json.getString(object2);
		String _object3 = json.getString(object3);
		String _object4 = json.getString(object4);

		map.put(object1, _object1);
		map.put(object2, _object2);
		map.put(object3, _object3);
		map.put(object4, _object4);

		array.add(map);
		}
	}
}

