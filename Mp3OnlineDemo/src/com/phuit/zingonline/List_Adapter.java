package com.phuit.zingonline;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class List_Adapter extends ArrayAdapter<Song>{
	Activity context = null;
	int layout_Id;
	ArrayList<Song> arr = null;
	
	public List_Adapter(Activity context, int layout_Id, ArrayList<Song> list){
		super(context, layout_Id, list);
		this.context = context;
		this.layout_Id = layout_Id;
		this.arr = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub				
		if(convertView == null){
			LayoutInflater inf = context.getLayoutInflater();
			convertView = inf.inflate(layout_Id, null);
		}
		Song song = arr.get(position);
		TextView title = (TextView)convertView.findViewById(R.id.tv_songs);
		TextView singer = (TextView)convertView.findViewById(R.id.tv_singers);
		
		title.setText(song.getTitle());
		singer.setText(song.getSinger());
		
		return convertView;
	}

}
