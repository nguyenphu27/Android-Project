package com.example.bluetoothexample.customadapter;

import com.example.bluetoothexample.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSpinner extends BaseAdapter{
	
	private Context context;
	private final String[] title;
	private final int[] iconId;
	private LayoutInflater inflater;
	
	public CustomSpinner(Context context, String[] title, int[] iconId) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.title = title;
		this.iconId = iconId;
		this.inflater = (LayoutInflater.from(context));
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return title.length;
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

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub		
		view = inflater.inflate(R.layout.single_menu, null);
		
		TextView mtitle = (TextView) view.findViewById(R.id.tv_titleMenu);
		mtitle.setText(title[position]);
		
		ImageView mImage = (ImageView) view.findViewById(R.id.imv_iconmenu);
		mImage.setImageResource(iconId[position]);
			
		return view;
	}

}
