package com.example.treasuregame;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ActivityRound1 extends ActionBarActivity implements OnClickListener{

	private static String mTitle = "Treasure Game";
	private static Button bBooty;
	private static Button bhint1;
	private static Button bhint2;
	private static Button bhint3;
	private static Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_round1);
		
		bBooty = (Button)findViewById(R.id.btn_booty);
		bhint1 = (Button)findViewById(R.id.btn_hint1);
		bhint2 = (Button)findViewById(R.id.btn_hint2);
		bhint3 = (Button)findViewById(R.id.btn_hint3);
		
		bBooty.setOnClickListener(this);
		bhint1.setOnClickListener(this);
		bhint2.setOnClickListener(this);
		bhint3.setOnClickListener(this);
		
		/*Set home button and title*/
		SpannableString s = new SpannableString(mTitle);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, mTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_booty:
			
			break;
		case R.id.btn_hint1:
			CustomDialog();
			break;
		case R.id.btn_hint2:
			CustomDialog();
			break;
		case R.id.btn_hint3:
			CustomDialog();
			break;
		default:
			break;
		}
	}
	
	private void CustomDialog(){
		dialog = new Dialog(ActivityRound1.this,R.style.BackgroundStyleDialog);
		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("HINT FOR YOU");
		dialog.show();
		Button dis = (Button)dialog.findViewById(R.id.btn_dis);
		dis.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
}
