package com.example.bluetoothexample.ews;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import com.example.bluetoothexample.R;
import com.example.bluetoothexample.cloud.ResponseServer;
import com.example.bluetoothexample.customadapter.CustomGrid;
import com.example.bluetoothexample.customadapter.CustomSpinner;
import com.example.bluetoothexample.pulsatorlib.PulsatorLayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EWSMainActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnItemClickListener{

	private Spinner sConsciousness;
	private String[] title_consciousness = new String[]{"Tỉnh táo","Tiếng nói","Đau đớn","Không phản ứng"};
	private int[] icon_consciousness = {R.drawable.ic_happy_spinner2,
									    R.drawable.ic_voice_spinner2,
									    R.drawable.ic_pain_spinner2,
									    R.drawable.ic_sleep_spinner2};
	
	private Spinner sRespiration;
	private String[] title_respiration = new String[]{"<=8","9-11","12-20","21-24",">=25"};
	private int[] icon_respiration = {R.drawable.ic_number3_spinner,
									  R.drawable.ic_number1_spinner,
									  R.drawable.ic_number0_spinner,
									  R.drawable.ic_number2_spinner,
									  R.drawable.ic_number3_spinner};
	
	private RadioButton radio_yes;
	private RadioButton radio_no;
	
	private ListView listEWS;
	private String[] title_ews = new String[]{"Nhịp tim","Oxy máu","Nhiệt độ","Tâm thu"};
	private String[] value_ews = new String[4];
	private String[] unit_ews = new String[]{"bpm","%","\u00b0C","mmHg"};
	private int[] icon_ews = {R.drawable.hearts32,
							  R.drawable.oxygennew,
							  R.drawable.thermometer,
							  R.drawable.sphygmomanometer};
	
	private ArrayList<HashMap<String, String>> arrayListEws;
	private ResponseServer response;
	private static final String OBJECT1 = "nhiptim";
	private static final String OBJECT2 = "spo2";
	private static final String OBJECT3 = "nhietdo";
	private static final String OBJECT4 = "tamthu";
	
	private static int mconsciousness = 0;
	private static int mrespiration = 0;
	private static int mSuplementalOxygen = 0;
	
	private Bundle bundle;
	private ImageView imv_score;
	private PulsatorLayout mPulsator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ews_main2);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar);
		TextView title = (TextView) findViewById(
				getResources().getIdentifier("action_bar_title", "id", getPackageName()));
		title.setText("Early Warning Score");
		
		sConsciousness = (Spinner)findViewById(R.id.spiner_consciousness);
		sRespiration = (Spinner)findViewById(R.id.spiner_respiration);
		radio_yes = (RadioButton)findViewById(R.id.radio_supplemental_yes);
		radio_no = (RadioButton)findViewById(R.id.radio_supplemental_no);
		listEWS = (ListView)findViewById(R.id.list_ews_main);
		imv_score = (ImageView)findViewById(R.id.imv_score);
		
		radio_no.setChecked(true);
		arrayListEws = new ArrayList<HashMap<String, String>>();
				
		sConsciousness.setAdapter(new CustomSpinner(getApplicationContext(), title_consciousness, icon_consciousness));
		sRespiration.setAdapter(new CustomSpinner(getApplicationContext(), title_respiration, icon_respiration));	
		
		sConsciousness.setOnItemSelectedListener(this);
		sRespiration.setOnItemSelectedListener(this);
		imv_score.setOnClickListener(this);
		listEWS.setOnItemClickListener(this);
		
		mPulsator = (PulsatorLayout) findViewById(R.id.pulsator);
		mPulsator.start();
		
		ImageView bHome = (ImageView) findViewById(R.id.imHome);
		bHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(parent.equals(sConsciousness)){
			if(position == 0){ /*Mức độ ý thức*/
				mconsciousness = 0;
				//Toast.makeText(getApplicationContext(), "Điểm: (0)", Toast.LENGTH_SHORT).show();
			}else{
				mconsciousness = 3;
				//Toast.makeText(getApplicationContext(), "Điểm: (3)", Toast.LENGTH_SHORT).show();
			}			
		}
		else if(parent.equals(sRespiration)){/*Nhịp thở*/
			switch (position) {
			case 0:
				mrespiration = 3;						
				break;
			case 1:
				mrespiration = 1;				
				break;
			case 2:
				mrespiration = 0;			
				break;
			case 3:
				mrespiration = 2;				
				break;
			case 4:
				mrespiration = 3;				
				break;
			default:
				break;
			}
		}
}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	/*Thở oxygen*/
	public void onRadioButonClicked(View v){
		boolean checked = ((RadioButton) v).isChecked();
		switch (v.getId()) {
		case R.id.radio_supplemental_yes:			
			if(checked){
				mSuplementalOxygen = 2;
				radio_no.setChecked(false);
				//Toast.makeText(getApplicationContext(), "Điểm: (2)", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.radio_supplemental_no:
			if(checked){
				mSuplementalOxygen = 0;
				radio_yes.setChecked(false);
				//Toast.makeText(getApplicationContext(), "Điểm: (0)", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bundle = new Bundle();
		if(!checkNetwork(getBaseContext())){
			Toast.makeText(getApplicationContext(), "Please! Check your network.", Toast.LENGTH_LONG).show();
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
					response = new ResponseServer();
					String _json = response.getJson();
					response.getArrayList(_json, arrayListEws, OBJECT1, OBJECT2, OBJECT3, OBJECT4);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					value_ews[0] = arrayListEws.get(arrayListEws.size() -1).get(OBJECT1);
					value_ews[1] = arrayListEws.get(arrayListEws.size() -1).get(OBJECT2);
					value_ews[2] = arrayListEws.get(arrayListEws.size() -1).get(OBJECT3);
					value_ews[3] = arrayListEws.get(arrayListEws.size() -1).get(OBJECT4);
					
					bundle.putStringArray("value_ews", value_ews);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							listEWS.setAdapter(new CustomGrid(getApplicationContext(), title_ews, value_ews, unit_ews, icon_ews));
						}
					});
				}
			}).start();
		}
		
	}
	
	private boolean checkNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(!checkNetwork(getBaseContext())){
			Toast.makeText(getApplicationContext(), "Please! Check your network.", Toast.LENGTH_LONG).show();
		}else{
			bundle.putInt("mconsciousness", mconsciousness);
			bundle.putInt("mrespiration", mrespiration);
			bundle.putInt("msuplementaloxygen", mSuplementalOxygen);
			
			Intent iewsScore = new Intent("com.example.bluetoothexample.ews.EWSSCORE");
			iewsScore.putExtras(bundle);
			startActivity(iewsScore);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			Intent iheart = new Intent("com.example.bluetoothexample.ews.EWSDETAIL");
			iheart.putExtra("mintent", 0);
			startActivity(iheart);
			break;
		case 1:
			Intent ioxy = new Intent("com.example.bluetoothexample.ews.EWSDETAIL");
			ioxy.putExtra("mintent", 1);
			startActivity(ioxy);
			break;
		case 2:
			Intent itemp = new Intent("com.example.bluetoothexample.ews.EWSDETAIL");
			itemp.putExtra("mintent", 2);
			startActivity(itemp);
			break;
		case 3:
			Intent isystolic = new Intent("com.example.bluetoothexample.ews.EWSDETAIL");
			isystolic.putExtra("mintent", 3);
			startActivity(isystolic);
			break;

		default:
			break;
		}
	}
}
