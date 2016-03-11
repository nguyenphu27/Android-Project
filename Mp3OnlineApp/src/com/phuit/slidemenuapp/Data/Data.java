package com.phuit.slidemenuapp.Data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

@SuppressWarnings("deprecation")
public class Data {
		
	public String getData(String url){
		
		String result = "";
    	InputStream isr = null;
    	try{
			HttpClient httpclient = new DefaultHttpClient();
            //http://10.0.2.2:1234/PHPSCRIPTS/getAllCustomers.php
			HttpPost httppost = new HttpPost(url); //YOUR PHP SCRIPT ADDRESS 
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
}
