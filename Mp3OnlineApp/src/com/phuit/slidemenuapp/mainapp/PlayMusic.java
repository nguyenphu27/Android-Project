package com.phuit.slidemenuapp.mainapp;

import java.util.ArrayList;
import java.util.HashMap;
import com.phuit.slidemenuapp.R;
import com.phuit.slidemenuapp.service.PlayerService;
import com.phuit.slidemenuapp.service.Utilities;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayMusic extends Fragment implements OnClickListener{

	public static int position2;
	public static ArrayList<HashMap<String, String>> BufferData2;

	public static ProgressDialog pDialog;
	public static MediaPlayer media;
	public static ImageView btn_play;
	public static ImageView btn_next;
	public static ImageView btn_back;
	public static ImageView btn_repeat;
	public static ImageView btn_shuffle;
	public static ImageView imv_cdmp3;
	public static TextView songTotalDuration;
	public static TextView songCurrentDuration;
	public static TextView songname;
	public static TextView artistname;
	public static SeekBar songProgressBar;
	public static Utilities utils;
	public static Handler mHandler = new Handler();	
	Intent i;
	
	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		View rootView = inflater.inflate(R.layout.playmusic, container, false);	
		//rootView.setBackgroundResource();	

		//get link from Tab1
		Bundle b = getArguments();
		position2 = b.getInt("position");
		BufferData2 = (ArrayList<HashMap<String, String>>) b.getSerializable("BufferData");	
		
		btn_play = (ImageView)rootView.findViewById(R.id.Btn_play);
		btn_next = (ImageView)rootView.findViewById(R.id.Btn_next);
		btn_back = (ImageView)rootView.findViewById(R.id.Btn_back);
		btn_repeat = (ImageView)rootView.findViewById(R.id.Btn_repeat);
		btn_shuffle = (ImageView)rootView.findViewById(R.id.Btn_shuffle);
		imv_cdmp3 = (ImageView)rootView.findViewById(R.id.imv_cd);
		songCurrentDuration = (TextView)rootView.findViewById(R.id.songCurrentDuration);
		songTotalDuration = (TextView)rootView.findViewById(R.id.songtotalDuration);
		songname = (TextView)rootView.findViewById(R.id.tv_tenbaihat);
		artistname = (TextView)rootView.findViewById(R.id.tv_tencasi);
		songProgressBar = (SeekBar)rootView.findViewById(R.id.seekBar1);
		
		btn_play.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_repeat.setOnClickListener(this);
		btn_shuffle.setOnClickListener(this);
		songCurrentDuration.setOnClickListener(this);
		songTotalDuration.setOnClickListener(this);
		songProgressBar.setOnClickListener(this);
		
		//service
		i = new Intent(getActivity(), PlayerService.class);
		i.putExtra("mposition", position2);
		i.putExtra("mBufferData", BufferData2);
		getActivity().startService(i);

		return rootView;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(!PlayerService.media.isPlaying()){
			getActivity().stopService(i);
		}
	}
	
}
