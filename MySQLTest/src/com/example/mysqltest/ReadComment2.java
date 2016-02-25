package com.example.mysqltest;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ReadComment2 extends ListActivity{

	ArrayList<HashMap<String, String>> productsList;
	
	private static final String ID_BAIHAT = "id_tenbaihat";
	private static final String ID_CASI = "id_tencasi";
	private static final String url = "http://flappytap.pe.hu/TestDatabase/getAllCustomers.php";
	private Data data;
	private String result = "";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_comment);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }      
        //STRICT MODE ENABLEB
       //StrictMode.enableDefaults();
        productsList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();
        
        lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(ReadComment2.this, "Good job phudk", Toast.LENGTH_SHORT).show();
			}
        	
		});
        getData();
    }
    
    public void getData(){
    	
     data = new Data();
     result = data.getData(url);
    //parse json data
   try {
	   String s = "";
	   String id_tenbaihat = "";
	   String id_tencasi = "";
	   JSONArray jArray = new JSONArray(result);
	   
	   for(int i=0; i<jArray.length();i++){
		   JSONObject json = jArray.getJSONObject(i);
		   HashMap<String, String> map = new HashMap<String, String>();
		   
		   //s = s + 
			id_tenbaihat =   json.getString("id") +": "+ json.getString("tenbaihat");
			id_tencasi =   json.getString("tencasi");
			
		  map.put(ID_BAIHAT, id_tenbaihat);
		  map.put(ID_CASI, id_tencasi);
		  productsList.add(map);
	   }
	   ListAdapter adapter = new SimpleAdapter(
				ReadComment2.this, productsList,
				R.layout.single_post, new String[]{ID_BAIHAT,ID_CASI},
				new int[] { R.id.id_tenbaihat, R.id.id_tencasi});
		// updating listview
		setListAdapter(adapter);
	
   } catch (Exception e) {
	// TODO: handle exception
	   Log.e("log_tag", "Error Parsing Data "+e.toString());
   	}
  }
}