package com.example.bluetoothexample.ews;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.bluetoothexample.*;
import com.example.bluetoothexample.circularprogressbar.CircleProgressBar;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EWSTotalScoreActivity extends Activity{

	private int consciousness;
	private int respiration;
	private int suplementalOxygen;
	private String[] value_ews;
	
	private int pulse;
	private int oxygen;
	private double temperature;
	private int systolic;
	
	private int mtotalScore;
	private TextView tvDiagnostic;
	private CircleProgressBar circlebar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ews_totalscore);	
		tvDiagnostic = (TextView)findViewById(R.id.tv_diagnostic);
		circlebar = (CircleProgressBar)findViewById(R.id.custom_progressBar);
		
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_for_ews_activity);
		TextView title = (TextView) findViewById(
				getResources().getIdentifier("action_bar_title", "id", getPackageName()));
		title.setText("Tổng điểm - kết quả");
		
		ImageView bHome = (ImageView) findViewById(R.id.imHome);
		bHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		ImageButton bSendEmail = (ImageButton)findViewById(R.id.btn_sendEmail);
		bSendEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(getBaseContext(), "Send Email", Toast.LENGTH_SHORT).show();
				sendEmail();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDataBundle();
		scoreEvaluate();
		diagnostic();
	}

	private void diagnostic() {
		// TODO Auto-generated method stub
		int response = triggerClinicalResponse(mtotalScore, 
				evaluateTemperature(temperature), evaluateHeartRate(pulse), 
				evaluateOxygenSaturations(oxygen), evaluateSystolic(systolic), 
				respiration, consciousness, suplementalOxygen);
		switch (response) {
		case 0:
			tvDiagnostic.setText(R.string.lowScoreWithResponse0);
			break;
		case 1:
			tvDiagnostic.setText(R.string.lowScoreWithResponse1);
			break;
		case 2:
			tvDiagnostic.setText(R.string.mediumScore);
			break;
		case 3:
			tvDiagnostic.setText(R.string.highScore);
			break;
		default:
			tvDiagnostic.setText("Error!");
			break;
		}
	}

	private void getDataBundle() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		
		value_ews = bundle.getStringArray("value_ews");
		consciousness = bundle.getInt("mconsciousness");
		respiration = bundle.getInt("mrespiration");
		suplementalOxygen = bundle.getInt("msuplementaloxygen");
		
		pulse = Integer.parseInt(value_ews[0]);
		oxygen = Integer.parseInt(value_ews[1]);
		temperature = Double.parseDouble(value_ews[2]);
		systolic = Integer.parseInt(value_ews[3]);
	}
	
	private void scoreEvaluate() {
		// TODO Auto-generated method stub
		mtotalScore = evaluateHeartRate(pulse) + evaluateOxygenSaturations(oxygen)
						+ evaluateTemperature(temperature) + evaluateSystolic(systolic)
						+ consciousness + respiration + suplementalOxygen;
		circlebar.setProgressWithAnimation(mtotalScore);
		String score = mtotalScore + "";
		if(mtotalScore<10){
			score = "0" + mtotalScore;
		}
		circlebar.setLabelCircle(score+"/20");
	}
	
	static int triggerClinicalResponse(int totalScore, int temperatureScore, int pulseScore, int oxygenScore, int systolicScore, 
            int respirationScore, int consciousnessScore, int suplementalOxygenScore){
        if(totalScore == 0){           
            return 0;
        }
        if((totalScore >= 1) & (totalScore <= 4) & (temperatureScore < 3) & (pulseScore < 3) & (oxygenScore < 3) & (systolicScore < 3) 
                & (respirationScore < 3) & (consciousnessScore < 3) & (suplementalOxygenScore < 3) ){
            return 1;
        }
        if(((totalScore >= 5) & (totalScore <= 6)) | (((temperatureScore == 3) | (pulseScore == 3) | (oxygenScore == 3) | (systolicScore == 3)
                | (respirationScore == 3) | (consciousnessScore == 3) | (suplementalOxygenScore == 3)) & (totalScore < 5))){
            return 2;
        }
        if(totalScore >= 7){
            return 3;
        }
        return -1;
    }
	
    static int evaluateHeartRate(int heartrate){       
        if((heartrate >= 51) & (heartrate <= 90))
            return 0;
        if((heartrate >= 91) & (heartrate <= 110))
            return 1;
        if((heartrate >= 41) & (heartrate <= 50))
            return 1;
        if((heartrate >= 111) & (heartrate <= 130))
            return 2;
        if(heartrate >= 131)
            return 3;
        if(heartrate <= 40)
            return 3;
        return -1;        
    }
    
    static int evaluateOxygenSaturations(int oxygensaturations){        
        if(oxygensaturations >= 96)
            return 0;
        if((oxygensaturations >= 94) & (oxygensaturations <= 95))
            return 1;
        if((oxygensaturations >= 92) & (oxygensaturations <= 93))
            return 2;
        if(oxygensaturations <= 91)
            return 3;
        return -1;       
    }

    static int evaluateTemperature(double temperature){      
        if((temperature >= 36.1) & (temperature <= 38))
            return 0;
        if((temperature >= 38.1) & (temperature <= 39))
            return 1;
        if((temperature >= 35.1) & (temperature <= 36))
            return 1;
        if(temperature >= 39.1)
            return 2;
        if(temperature <= 35)
            return 3;
        return -1;       
    }
    
    static int evaluateSystolic(int systolic){        
        if((systolic >= 111) & (systolic <= 219))
            return 0;
        if((systolic >= 101) & (systolic <= 110))
            return 1;
        if((systolic >= 91) & (systolic <= 100))
            return 2;
        if(systolic >= 220)
            return 3;
        if(systolic <= 90)
            return 3;
        return -1;
    }
    
    public void sendEmail(){
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
}
