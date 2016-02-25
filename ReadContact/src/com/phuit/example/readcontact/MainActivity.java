package com.phuit.example.readcontact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Button bPhone;
	private TextView tPhone;
	private static final int REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bPhone = (Button)findViewById(R.id.bPhone);
		tPhone = (TextView)findViewById(R.id.tvphone);
		
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
	}

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

                int nameColumnIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                tPhone.setText(name+" "+number);

            }
        }
	}
}
