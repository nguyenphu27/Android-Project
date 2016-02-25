package com.example.spacegamepro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class IntroGame extends Activity{


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					try {
						sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}finally{
					Intent menu = new Intent("com.example.spacegamepro.MENUGAME");
					startActivity(menu);
				}
			}
		};
		timer.start();
	}

}
