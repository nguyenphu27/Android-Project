package com.phuit.slidemenuapp.mainapp;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.phuit.slidemenuapp.R;
import com.phuit.slidemenuapp.Data.Data;

@SuppressLint("NewApi")
public class Tab5 extends Fragment{
	
	ArrayList<HashMap<String, String>> BufferData;
	
	private static final String ID_STT = "id_sothutu";
	private static final String ID_BAIHAT = "id_tenbaihat";
	private static final String ID_CASI = "id_tencasi";
	private static final String ID_LINK = "id_linkbaihat";
	private static final String url = "http://flappytap.pe.hu/ConnectAppmusic/nhackpop.php";
	private Data data;
	private String result = "";
	private ListView lv;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.tab1, container, false);
		BufferData = new ArrayList<HashMap<String, String>>();
		StrictMode.enableDefaults();
		lv = (ListView)rootView.findViewById(R.id.listtab1);
		
		final PlayMusic frag = new PlayMusic();
		final Bundle b = new Bundle();
		getdata();
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int p,
					long id) {
				// TODO Auto-generated method stub						
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
			    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    fragmentTransaction.setCustomAnimations(
			    		R.animator.enter,R.animator.enter_out, R.animator.enter, R.animator.enter_out);
			    fragmentTransaction.addToBackStack(null);
			            
			    fragmentTransaction.replace(R.id.frame_container, frag);
			    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);	           
			    fragmentTransaction.commit();
    
	            b.putInt("position", p);
	            b.putSerializable("BufferData", BufferData);
	            frag.setArguments(b);
			}});
		return rootView;
	}
	
	public void getdata(){
		data = new Data();
	    result = data.getData(url);
	    //parse json data
	   try {
		   String id_tenbaihat = "";
		   String id_tencasi = "";
		   String id_linkbaihat = "";
		   String id_sothutu = "";
		   JSONArray jArray = new JSONArray(result);
		   
		   for(int i=0; i<jArray.length();i++){
			   JSONObject json = jArray.getJSONObject(i);
			   HashMap<String, String> map = new HashMap<String, String>();			   
			   			   
			   	id_sothutu = json.getString("id") +".";
				id_tenbaihat = json.getString("tenbaihat");
				id_tencasi =   json.getString("tencasi");
				id_linkbaihat = json.getString("linkbaihat");				
				
			  map.put(ID_STT, id_sothutu);
			  map.put(ID_BAIHAT, id_tenbaihat);
			  map.put(ID_CASI, id_tencasi);
			  map.put(ID_LINK, id_linkbaihat);
			  BufferData.add(map);		  
		   }
		   
		   ListAdapter adapter = new SimpleAdapter(
					getActivity(), BufferData,
					R.layout.single_post_item, new String[]{ID_STT,ID_BAIHAT,ID_CASI},
					new int[] { R.id.id_sothutu, R.id.id_tenbaihat, R.id.id_tencasi});
			// updating listview
		   	lv.setAdapter(adapter);
		   	
	   } catch (Exception e) {
		// TODO: handle exception
		   Log.e("log_tag", "Error Parsing Data "+e.toString());
		   Toast.makeText(getActivity(), "Kiểm tra kết nối internet!", Toast.LENGTH_SHORT).show();
	   	}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View view, int k, KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getAction() == KeyEvent.ACTION_DOWN){
					if(k == KeyEvent.KEYCODE_BACK){
						new AlertDialog.Builder(getActivity())
						.setTitle("Thoát")
						.setMessage("Bạn chắc chắn muốn thoát?")
						.setCancelable(false)
						.setPositiveButton("Đúng", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								getActivity().moveTaskToBack(true);
								System.exit(1);
							}
						})
						.setNegativeButton("Không", null)
						.show();
					}	
				}	
				return false;
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
