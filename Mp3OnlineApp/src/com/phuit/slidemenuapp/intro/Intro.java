package com.phuit.slidemenuapp.intro;

import com.phuit.slidemenuapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Intro extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent i = new Intent("com.phuit.slidemenuapp.MAINACTIVITY");
					startActivity(i);
				}
			}
		};
		timer.start();	
		
	}

}
