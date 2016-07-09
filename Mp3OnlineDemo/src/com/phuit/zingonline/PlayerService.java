package com.phuit.zingonline;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract.Data;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerService extends Service implements OnCompletionListener,
OnErrorListener, OnPreparedListener, OnBufferingUpdateListener,
OnSeekCompleteListener, OnInfoListener, OnClickListener, OnSeekBarChangeListener{

	//public static final String BROADCAST_BUFFER = null;
	private WeakReference<ImageView> bPre, bPlay, bNext, brepeat, iv_arrow;
	private WeakReference<SeekBar> seekBarPlay;
	private WeakReference<TextView> start_time;
	private WeakReference<TextView> end_time;
	

	private boolean isRepeat = false; 
	public static MediaPlayer mp;
	
	// Xu ly viec cap nhat giao dien(thoi gian chay va seekbar chay)
	private Handler mHandler = new Handler();
	private Utilities utils;
	private int seeknext = 5000; //5 giay
	private int seekpre = 5000; 
	public static int currentSongIndex = -1;
	public static int SongIndexPause = 0;
	
	//Thanh lap broadcast identifier and intent
	public static final String BROADCAST_BUFFER = "com.phuit";
	Intent bufferIntent;

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		mp.setOnErrorListener(this);
		mp.setOnPreparedListener(this);
		mp.setOnBufferingUpdateListener(this);
		mp.setOnSeekCompleteListener(this);
		mp.setOnInfoListener(this);
		
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		utils = new Utilities();
		bufferIntent = new Intent(BROADCAST_BUFFER);
								
	}

	// xu ly su kien khi co cuoc goi den, tam dung
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub		
		sukiengoi();
		String songLink = intent.getExtras().getString("songLink");	
		Log.d("songLink", "songLink = " + songLink);
		playSong(songLink);
		//return super.onStartCommand(intent, flags, startId);
		//super.onStart(intent, startId);
		return START_STICKY;
	}

	private void sukiengoi() {
		// TODO Auto-generated method stub
		start_time = new WeakReference<TextView>(MainActivity.start_time);
		end_time = new WeakReference<TextView>(MainActivity.end_time);
		
		bPre = new WeakReference<ImageView>(MainActivity.bPre);
		bPlay = new WeakReference<ImageView>(MainActivity.bPlay);
		bNext = new WeakReference<ImageView>(MainActivity.bNext);
		brepeat = new WeakReference<ImageView>(MainActivity.brepeat);
		//iv_arrow = new WeakReference<ImageView>(MainActivity.iv_arrow);
		
		brepeat.get().setOnClickListener(this);
		bPre.get().setOnClickListener(this);
		bPlay.get().setOnClickListener(this);
		bNext.get().setOnClickListener(this);
		//iv_arrow.get().setOnClickListener(this);
		
		seekBarPlay = new WeakReference<SeekBar>(MainActivity.seekBarPlay);
		seekBarPlay.get().setOnSeekBarChangeListener(this);
	
	}
	private void sendBufferingBroadcast(){
		bufferIntent.putExtra("buffering", "1");
		sendBroadcast(bufferIntent);
	}
	private void sendBufferCompleteBroadcast(){
		bufferIntent.putExtra("buffering", "0");
		sendBroadcast(bufferIntent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imv_play:
			if(mp.isPlaying()){
				if(mp!= null){
					mp.pause();
					bPlay.get().setImageResource(R.drawable.play);
					//iv_arrow.get().setImageResource(R.drawable.arrow_play);
					//blink();
				}				
			}else {
				if(mp != null){
					mp.start();
					bPlay.get().setImageResource(R.drawable.pause);
					//blink();
				}
			}
			break;

		case R.id.imv_next:
			int cPosition = mp.getCurrentPosition();
			if(cPosition + seeknext <= mp.getDuration()){
				mp.seekTo(cPosition + seeknext);
			}else {
				mp.seekTo(mp.getDuration());
			}
			break;
		case R.id.imv_pre:
			int cPosition2 = mp.getCurrentPosition();
			if(cPosition2 - seekpre >=0 ){
				mp.seekTo(cPosition2 - seekpre);
			}else {
				mp.seekTo(0);
			}
			break;
		case R.id.iv_repeat:
			if(isRepeat){
				isRepeat = false;
			Toast.makeText(getApplicationContext(), "Lặp lại: Tắt ", Toast.LENGTH_LONG).show();
			brepeat.get().setImageResource(R.drawable.repeat);
			break;
		}else{
			isRepeat = true;
			Toast.makeText(getApplicationContext(), "Lặp lại: Bật", Toast.LENGTH_LONG).show();
			brepeat.get().setImageResource(R.drawable.repeat_pressed);
		}
			}
		
	}	    
	
	public void playSong(String songpath) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUTT);
		mp.reset();
		if(!mp.isPlaying()){
			try{
				mp.setDataSource(songpath);		
				sendBufferingBroadcast();
				mp.prepareAsync();
				bPlay.get().setImageResource(R.drawable.pause);
				seekBarPlay.get().setProgress(0);
				seekBarPlay.get().setMax(100);
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}catch(IllegalStateException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}			
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub	
		sendBufferCompleteBroadcast();
		playMedia();
	}
	public void playMedia() {
		// TODO Auto-generated method stub
		if(!mp.isPlaying()){
			mp.start();
			updateProgressBar();
		}
	}
	public void pauseMedia() {
		// TODO Auto-generated method stub
		if(!mp.isPlaying()){
			mp.pause();			
		}
	}
	public void stopMedia() {
		// TODO Auto-generated method stub
		if(!mp.isPlaying()){
			mp.stop();			
		}
	}
	
	public void updateProgressBar() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(mUTT, 100);
	}
private Runnable mUTT = new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long total = 0;
		try{
			total = mp.getDuration();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		long current = 0;
		try{
			current = mp.getCurrentPosition();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		end_time.get().setText("" + utils.milliSecondsToTimer(total));
		start_time.get().setText("" + utils.milliSecondsToTimer(current));
		int progress = (int)(utils.getProgressPercentage(current, total));
		seekBarPlay.get().setProgress(progress);
		mHandler.postDelayed(this, 100);
		
		
	}
};
@Override
public void onProgressChanged(SeekBar seekBar, int progress,
		boolean fromUser) {
	// TODO Auto-generated method stub
	
}
@Override
public void onStartTrackingTouch(SeekBar seekBar) {
	// TODO Auto-generated method stub
	mHandler.removeCallbacks(mUTT);
}

@Override
public void onStopTrackingTouch(SeekBar seekBar) {
	// TODO Auto-generated method stub
	mHandler.removeCallbacks(mUTT);
	int total = mp.getDuration();
	int currentP = utils.progressToTimer(seekBar.getProgress(), total);
	mp.seekTo(currentP);
	updateProgressBar();
}

@Override
public void onCompletion(MediaPlayer mp) {
	// TODO Auto-generated method stub
	if(isRepeat){
	mp.isLooping();
	mp.start();
	}else{		
		mp.isLooping();
	}
}

	@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	currentSongIndex = -1;
	mHandler.removeCallbacks(mUTT);
	if(mp!= null){
		if(mp.isPlaying()){
			mp.stop();
		}
		mp.release();
	}
}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
		
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}		
}
