package com.phuit.example.autosms;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

	public String sPhone,sSms;
	private EditText etPhone,etSms;
	
	private Button bStart,bCancel,bTimeSelect,bPhone;
	
    static final int TIME_DIALOG_ID=1;
    private static final int REQUEST_CODE = 1;
	
    Calendar c;
    public int year,month,day,hour,minute;  
    private int mHour,mMinute; 
    
	private AlarmManager aManager;
	private PendingIntent pIntent;
	
	public MainActivity(){
        // Assign current Date and Time Values to Variables
        c = Calendar.getInstance();       
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		etPhone = (EditText)findViewById(R.id.etPhone);
		etSms = (EditText)findViewById(R.id.etSms);
		
		bStart = (Button)findViewById(R.id.bStart);
		bCancel = (Button)findViewById(R.id.bCancel);	
		bTimeSelect = (Button)findViewById(R.id.bTime);
		bPhone = (Button)findViewById(R.id.bCPhone);
		
		//contact
		bPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("content://contacts");
				Intent intent = new Intent(Intent.ACTION_PICK, uri);
				intent.setType(Phone.CONTENT_TYPE);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		//start schedule
		bStart.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub			
				sPhone = etPhone.getText().toString();
				sSms = etSms.getText().toString();
				etSms.getText().clear();
				
				Intent i = new Intent(MainActivity.this,AlarmService.class);
				i.putExtra("exPhone", sPhone);
				i.putExtra("exSmS", sSms);
				
				
				pIntent = PendingIntent.getService(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
				
				aManager = (AlarmManager)getSystemService(ALARM_SERVICE);
				c.setTimeInMillis(System.currentTimeMillis()); 
				c.set(Calendar.HOUR_OF_DAY, hour);
				c.set(Calendar.MINUTE, minute);
				aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pIntent);	
				Toast.makeText(getApplicationContext(), "Sms scheduled! " + sSms,Toast.LENGTH_SHORT).show();
			}
		});
		
		//set time to send
		bTimeSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
			}
		});
		
		//Cancel schedule
		bCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aManager.cancel(pIntent);
				Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
			}
		});
	}

	//Choose phone in contact and set edit text
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, i);
		
		if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = i.getData();
                String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                etPhone.setText(number);
            }
        }
	}
      // Register  TimePickerDialog listener                 
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = 
    		new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                        hour = hourOfDay;
                        minute = min;
                        // Set the Selected Date in Select date Button
                        bTimeSelect.setText(hour+":"+minute);
              }
     };

   // Method automatically gets Called when you call showDialog()  method
   @Override
   protected Dialog onCreateDialog(int id) {
             switch (id) {
             // create a new TimePickerDialog with values you want to show 
             case TIME_DIALOG_ID:
             return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
            }
            return null;
     }
}
