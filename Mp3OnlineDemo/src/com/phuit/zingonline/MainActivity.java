package com.phuit.zingonline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	WebView web;
	public static ImageView bPre, bPlay, bNext, brepeat, iv_arrow;
	public static SeekBar seekBarPlay;
	public static TextView start_time, end_time,tv_link,tv_tenbh,tv_tencs;
	public static ImageButton play_list;
	private Intent playservice;
	
	public final static String[] LINK_MUSIC = {"http://mp3.zing.vn/html5/song/kmxGyLnNzuSQpkCttFnZH",
											   "http://mp3.zing.vn/html5/song/ZnxHTkmNARXdXQJyyvmLn",
											   "http://mp3.zing.vn/html5/song/ZHxGyZmslJLXLiLtTDHZH",
											   "http://mp3.zing.vn/html5/song/kncntknalHiCJEdtTFHLm",
											   "http://mp3.zing.vn/html5/song/LGxGtLnsAgNQLWdyyvnLm",
											   "http://mp3.zing.vn/html5/song/LHxmyLHNzlQcEkkytFnLm",
											   "http://mp3.zing.vn/html5/song/kGcHTZmNzZzgBXCtTbHLn",
											   "http://mp3.zing.vn/html5/song/knxmyZnaliGGaNxTyFHLH",
											   "http://mp3.zing.vn/html5/song/kGcnyLHsANXRxbgTyvmZH",
											   "http://mp3.zing.vn/html5/song/LnJGtLGsVuinbNlyybGZG",
											   "http://mp3.zing.vn/html5/song/LnxGTLmNAZGLJzJtTbnkn",
											   "http://mp3.zing.vn/html5/song/LHJGTZmsduJkdFbyyDGZm",
											   "http://mp3.zing.vn/html5/song/ZnJmydNDpnSJytvGkH",
											   "http://mp3.zing.vn/html5/song/LnJmTkHszuLdJZNTybHZG",
											   "http://mp3.zing.vn/html5/song/knxHtknaSJXNRpLtybGLm",
											   "http://mp3.zing.vn/html5/song/LmcnykmNpGnGdEdTTbHLm",
											   "http://mp3.zing.vn/html5/song/LncGyZGNSuadDBktyFmkH",
											   "http://mp3.zing.vn/html5/song/LGJnykHNARpWZDpyyFHZH",
											   "http://mp3.zing.vn/html5/song/ZHJHTZGsSixdGBQytFnLn",
											   "http://mp3.zing.vn/html5/song/knxnTkHNSindDmWyTDHLn",
											   "http://mp3.zing.vn/html5/song/ZGJmTZmNAhasJnxTTFmZn",
											   "http://mp3.zing.vn/html5/song/LHJGTQHnNBmQyybmLn",
											   "http://mp3.zing.vn/html5/song/ZmxntLmaALLAlsNtTbHZH",
											   "http://mp3.zing.vn/html5/song/ZGJHTLHsShLCChbtyvmLn",
											   "http://mp3.zing.vn/html5/song/LGJmyZGslmWBiQNyTbmLm",
											   "http://mp3.zing.vn/html5/song/kGJntLnaSgxLQCzyyDGLm",
											   "http://mp3.zing.vn/html5/song/kncGyLHNQnlHhQNyyvGkH",
											   "http://mp3.zing.vn/html5/song/LmxGykHNQmSHgWAyTFmZH",
											   "http://mp3.zing.vn/html5/song/kHJHyZHsAbxdsXHtyFHLH",
											   "http://wgr.clipnhac.info/data@sd@changed6526sv6/6-2013/NhuMotGiacMo_MyTam.mp3",
											   "http://mp3.zing.vn/html5/song/LGxHyLHspnmANzBytFnLn"
											   };
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
		
		web = (WebView)findViewById(R.id.webview);
		web.getSettings().setJavaScriptEnabled(true);
		
		web.loadUrl("file:///android_asset/eq2.gif");
	
		
		
		sukien();
		Intent get = getIntent(); 
		int vitri = get.getIntExtra("Po", 0);			
		String music_name = get.getStringExtra("music_name");
		String singer = get.getStringExtra("singer_name");
		
			playservice = new Intent(MainActivity.this, PlayerService.class);
			
			if(vitri == 0){
			playservice.putExtra("songLink", LINK_MUSIC[0]);
			}else if(vitri == 1){
			playservice.putExtra("songLink", LINK_MUSIC[1]);
			}else if(vitri == 2){
				playservice.putExtra("songLink", LINK_MUSIC[2]);
			}else if(vitri == 3){
				playservice.putExtra("songLink", LINK_MUSIC[3]);
			}else if(vitri == 4){
				playservice.putExtra("songLink", LINK_MUSIC[4]);
			}else if(vitri == 5){
				playservice.putExtra("songLink", LINK_MUSIC[5]);
			}else if(vitri == 6){
				playservice.putExtra("songLink", LINK_MUSIC[6]);
			}else if(vitri == 7){
				playservice.putExtra("songLink", LINK_MUSIC[7]);
			}else if(vitri == 8){
				playservice.putExtra("songLink", LINK_MUSIC[8]);
			}else if(vitri == 9){
				playservice.putExtra("songLink", LINK_MUSIC[9]);
			}else if(vitri == 10){
				playservice.putExtra("songLink", LINK_MUSIC[10]);
			}else if(vitri == 11){
				playservice.putExtra("songLink", LINK_MUSIC[11]);
			}else if(vitri == 12){
				playservice.putExtra("songLink", LINK_MUSIC[12]);
			}else if(vitri == 13){
				playservice.putExtra("songLink", LINK_MUSIC[13]);
			}else if(vitri == 14){
				playservice.putExtra("songLink", LINK_MUSIC[14]);
			}else if(vitri == 15){
				playservice.putExtra("songLink", LINK_MUSIC[15]);
			}else if(vitri == 16){
				playservice.putExtra("songLink", LINK_MUSIC[16]);
			}else if(vitri == 17){
				playservice.putExtra("songLink", LINK_MUSIC[17]);
			}else if(vitri == 18){
				playservice.putExtra("songLink", LINK_MUSIC[18]);
			}else if(vitri == 19){
				playservice.putExtra("songLink", LINK_MUSIC[19]);
			}else if(vitri == 20){
				playservice.putExtra("songLink", LINK_MUSIC[20]);
			}else if(vitri == 21){
				playservice.putExtra("songLink", LINK_MUSIC[21]);
			}else if(vitri == 22){
				playservice.putExtra("songLink", LINK_MUSIC[22]);
			}else if(vitri == 23){
				playservice.putExtra("songLink", LINK_MUSIC[23]);
			}else if(vitri == 24){
				playservice.putExtra("songLink", LINK_MUSIC[24]);
			}else if(vitri == 25){
				playservice.putExtra("songLink", LINK_MUSIC[25]);
			}else if(vitri == 26){
				playservice.putExtra("songLink", LINK_MUSIC[26]);
			}else if(vitri == 27){
				playservice.putExtra("songLink", LINK_MUSIC[27]);
			}else if(vitri == 28){
				playservice.putExtra("songLink", LINK_MUSIC[28]);
			}else if(vitri == 29){
				playservice.putExtra("songLink", LINK_MUSIC[29]);
			}else if(vitri == 30){
				playservice.putExtra("songLink", LINK_MUSIC[30]);
			}
			
			tv_link.setText("Bạn đang nghe bài hát: " + music_name + " - " + singer +  
					" . " + "Chúc các bạn nghe nhạc vui vẻ! ^^");
			tv_tenbh.setText(music_name);
			tv_tencs.setText(singer);
	startService(playservice);		
	}

	private void sukien() {
		// TODO Auto-generated method stub
		tv_tenbh = (TextView)findViewById(R.id.tv_tenbh);
		tv_tencs = (TextView)findViewById(R.id.tv_tencs);
		tv_link = (TextView)findViewById(R.id.tvLink);
		
		seekBarPlay = (SeekBar)findViewById(R.id.seekBarmusic);
		start_time = (TextView)findViewById(R.id.tvstart_time);
		end_time = (TextView)findViewById(R.id.tvend_time);
		bPre = (ImageView)findViewById(R.id.imv_pre);
		bPlay = (ImageView)findViewById(R.id.imv_play);
		bNext = (ImageView)findViewById(R.id.imv_next);
		play_list  = (ImageButton)findViewById(R.id.bplay_list);
		brepeat = (ImageView)findViewById(R.id.iv_repeat);
		
		
		play_list.setOnClickListener(this);
		brepeat.setOnClickListener(this);
		seekBarPlay.setOnClickListener(this);
		start_time.setOnClickListener(this);
		end_time.setOnClickListener(this);
		bPre.setOnClickListener(this);
		bPlay.setOnClickListener(this);
		bNext.setOnClickListener(this);
		
	}
	
	//Dialog xu ly tien trinh tai giua PlayerService va MainActivity
	private ProgressDialog pdBuff = null;
	boolean mBBIRe;
	// Thiet lap BroadcastReceiver
	private BroadcastReceiver bcBRe = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			showPD(intent);
		}
	};
	private void showPD(Intent intent) {
		// TODO Auto-generated method stub
		String bValue = intent.getStringExtra("buffering");
		int bIValue = Integer.parseInt(bValue);
		switch(bIValue){
		case 0: 
			if(pdBuff != null){
				pdBuff.dismiss();
			}
			break;
		case 1: 
			BufferDialogue();
			break;
		}
	}

	private void BufferDialogue() {
		// TODO Auto-generated method stub
		pdBuff = ProgressDialog.show(this, "Vui lòng chờ", "Loading...", true);
		pdBuff.setCancelable(true);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Dang ki broadcast receiver
		if(!mBBIRe){
			registerReceiver(bcBRe, new IntentFilter(PlayerService.BROADCAST_BUFFER));
			mBBIRe = true;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mBBIRe){
			unregisterReceiver(bcBRe);
			mBBIRe = false;			
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (!PlayerService.mp.isPlaying()) {
			stopService(playservice);
		}	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub	
		switch (v.getId()) {
		case R.id.bplay_list:
			finish();
			break;
		}
	}
}
