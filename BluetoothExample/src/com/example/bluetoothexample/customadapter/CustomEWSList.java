package com.example.bluetoothexample.customadapter;

import com.example.bluetoothexample.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomEWSList extends BaseAdapter{

	private String[] index;
	private String[] score;
	private Context context;
	private LayoutInflater inflater;
	
	public CustomEWSList(Context c, String[] heartindex, String[] heartscore) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.index = heartindex;
		this.score = heartscore;
		this.inflater = (LayoutInflater.from(c));
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return index.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.single_ews_score_detail, null);
		
		TextView mindex = (TextView)view.findViewById(R.id.tv_index);
		TextView mscore = (TextView)view.findViewById(R.id.tv_score);
		
		mindex.setText(index[position]);
		mscore.setText(score[position]);
		
		return view;
	}

}
