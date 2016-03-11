package com.phuit.slidemenuapp.service;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.phuit.slidemenuapp.R;
import com.phuit.slidemenuapp.mainapp.PlayMusic;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerService extends Service implements OnCompletionListener, OnClickListener, OnSeekBarChangeListener, OnPreparedListener {

	private WeakReference<ImageView> btn_play;
	private WeakReference<ImageView> btn_next;
	private WeakReference<ImageView> btn_back;
	private WeakReference<ImageView> btn_repeat;
	private WeakReference<ImageView> btn_shuffle;
	private WeakReference<ImageView> imv_cdmp3;
	private WeakReference<TextView> songCurrentDuration;
	private WeakReference<TextView> songTotalDuration;
	private WeakReference<TextView> songname;
	private WeakReference<TextView> artistname;
	private WeakReference<SeekBar> songProgressBar;
	
	public static MediaPlayer media;
	private Handler mHandler = new Handler();	
	private Utilities utils;
	private boolean isRepeat = false;
	private boolean isShuffle = false;
	
	Intent buffer; //buffer dialog
	public static String linkbaihat = "";
	public static int position3;
	public static ArrayList<HashMap<String, String>> BufferData3;
	private static final String ID_LINK = "id_linkbaihat";
	private static final String ID_BAIHAT = "id_tenbaihat";
	private static final String ID_CASI = "id_tencasi";
	public static int pos; //position song when random
	public static RotateAnimation rotate;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		media = new MediaPlayer();
		media.setOnCompletionListener(this);
		media.setOnPreparedListener(this);
		media.setAudioStreamType(AudioManager.STREAM_MUSIC);
		utils = new Utilities();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		btn_play = new WeakReference<ImageView>(PlayMusic.btn_play);
		btn_play.get().setImageResource(R.drawable.btn_pause);
		btn_next = new WeakReference<ImageView>(PlayMusic.btn_next);
		btn_back = new WeakReference<ImageView>(PlayMusic.btn_back);
		btn_repeat = new WeakReference<ImageView>(PlayMusic.btn_repeat);
		btn_shuffle = new WeakReference<ImageView>(PlayMusic.btn_shuffle);
		imv_cdmp3 = new WeakReference<ImageView>(PlayMusic.imv_cdmp3);
		songCurrentDuration = new WeakReference<TextView>(PlayMusic.songCurrentDuration);
		songTotalDuration = new WeakReference<TextView>(PlayMusic.songTotalDuration);
		songname = new WeakReference<TextView>(PlayMusic.songname);
		artistname = new WeakReference<TextView>(PlayMusic.artistname);
		songProgressBar = new WeakReference<SeekBar>(PlayMusic.songProgressBar);
		
		btn_play.get().setOnClickListener(this);
		btn_next.get().setOnClickListener(this);
		btn_back.get().setOnClickListener(this);
		btn_repeat.get().setOnClickListener(this);
		btn_shuffle.get().setOnClickListener(this);
		songProgressBar.get().setOnSeekBarChangeListener(this);
		
		//rotate cd mp3
		rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(15000);
		rotate.setInterpolator(new LinearInterpolator());
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setStartOffset(0);
		imv_cdmp3.get().setAnimation(rotate);
		
		
		position3 = intent.getIntExtra("mposition", 0);
		pos = position3;
		BufferData3 = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("mBufferData");
		linkbaihat = BufferData3.get(pos).get(ID_LINK);
		
		playSong(linkbaihat);
		return START_STICKY;	
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
//play song
public void playSong(String linksource){
	
	//play music
	songname.get().setText(BufferData3.get(pos).get(ID_BAIHAT));
	artistname.get().setText(BufferData3.get(pos).get(ID_CASI));
	mHandler.removeCallbacks(mUpdateTimerTask);
	media.reset();
	if(!media.isPlaying()){
		
		try {
			media.setDataSource(linksource);
			media.prepareAsync();
			songProgressBar.get().setProgress(0);
	        songProgressBar.get().setMax(100);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}	
	
//service
	@Override
	public void onPrepared(MediaPlayer media) {
		// TODO Auto-generated method stub
		if(!media.isPlaying()){
			media.start();			
			updateProgressBar();
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		if(isRepeat){
			linkbaihat = BufferData3.get(pos).get(ID_LINK);
			playSong(linkbaihat);
		}else if(isShuffle){
			Random rand = new Random();
			pos = rand.nextInt(BufferData3.size()-1) + 1;
			linkbaihat = BufferData3.get(pos).get(ID_LINK);
			playSong(linkbaihat);
		}else{
			if(pos < BufferData3.size() - 1){
				linkbaihat = BufferData3.get(pos + 1).get(ID_LINK);
				pos++;
				playSong(linkbaihat);				
			}else{
				linkbaihat = BufferData3.get(0).get(ID_LINK);	
				pos = 0;
				playSong(linkbaihat);	
			}
		}
	}

	
//on click
	@SuppressLint("NewApi")
	@Override
	public void onClick(View id) {
		// TODO Auto-generated method stub
		switch(id.getId()){
		case R.id.Btn_play:
			if(media.isPlaying()){
				media.pause();
				btn_play.get().setImageResource(R.drawable.btn_play);
				imv_cdmp3.get().clearAnimation();
			}else{
				media.start();
				btn_play.get().setImageResource(R.drawable.btn_pause);
				imv_cdmp3.get().startAnimation(rotate);
			}
			break;
		case R.id.Btn_next:	
			delay(500);
			
			if(isShuffle){
				Random rand = new Random();
				pos = rand.nextInt(BufferData3.size()-1) + 1;
				linkbaihat = BufferData3.get(pos).get(ID_LINK);
				playSong(linkbaihat);
			}else{
				if(pos < BufferData3.size() - 1){
					linkbaihat = BufferData3.get(pos + 1).get(ID_LINK);
					pos++;
					playSong(linkbaihat);	
				}else{
					linkbaihat = BufferData3.get(0).get(ID_LINK);	
					pos = 0;
					playSong(linkbaihat);						
				}	
			}	
			break;
		case R.id.Btn_back:
			delay(500); //delay 1s 
			
			if(isShuffle){
				Random rand = new Random();
				pos = rand.nextInt(BufferData3.size()-1) + 1;
				linkbaihat = BufferData3.get(pos).get(ID_LINK);
				playSong(linkbaihat);
			}else{
				if(pos > 0){
					linkbaihat = BufferData3.get(pos - 1).get(ID_LINK);
					pos--;
					playSong(linkbaihat);					
				}else{
					linkbaihat = BufferData3.get(BufferData3.size() - 1).get(ID_LINK);	
					pos = BufferData3.size() - 1;
					playSong(linkbaihat);	
				}
			}		
			break;
		case R.id.Btn_repeat:
			if(isRepeat){
				isRepeat = false;
				btn_repeat.get().setImageResource(R.drawable.img_btn_repeat);
				Toast.makeText(getApplicationContext(), "Lặp lại tắt!", Toast.LENGTH_SHORT).show();
			}else{
				isRepeat = true;
				Toast.makeText(getApplicationContext(), "Lặp lại bật!", Toast.LENGTH_SHORT).show();
				isShuffle = false;
				btn_repeat.get().setImageResource(R.drawable.img_btn_repeat_pressed);
				btn_shuffle.get().setImageResource(R.drawable.img_btn_shuffle);
			}
			break;
		case R.id.Btn_shuffle:
			if(isShuffle){
				isShuffle = false;
				btn_shuffle.get().setImageResource(R.drawable.img_btn_shuffle);
				Toast.makeText(getApplicationContext(), "Trộn bài tắt!", Toast.LENGTH_SHORT).show();
			}else{
				isShuffle = true;
				Toast.makeText(getApplicationContext(), "Trộn bài bật!", Toast.LENGTH_SHORT).show();
				isRepeat = false;
				btn_shuffle.get().setImageResource(R.drawable.img_btn_shuffle_pressed);
				btn_repeat.get().setImageResource(R.drawable.img_btn_repeat);
			}
			break;
		}
	}
	
//seek bar
	public void updateProgressBar() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(mUpdateTimerTask, 100);
	}
	
	private Runnable mUpdateTimerTask = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			long totalDuration = media.getDuration();
            long currentDuration = media.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDuration.get().setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDuration.get().setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.get().setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
			
			
		}
	};

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateTimerTask);
	}



	@Override
	public void onStopTrackingTouch(SeekBar seekbar) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateTimerTask);
		int totalDuration = media.getDuration();
		int currentPosition = utils.progressToTimer(seekbar.getProgress(), totalDuration);
		media.seekTo(currentPosition);
		updateProgressBar();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeCallbacks(mUpdateTimerTask);
		if(media !=null){
			if(media.isPlaying()){
				media.stop();
			}
			media.release();
		}
	}
	
	public void delay(int n){
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
