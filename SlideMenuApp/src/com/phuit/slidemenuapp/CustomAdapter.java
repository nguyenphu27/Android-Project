package com.phuit.slidemenuapp;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{

	Context context;
	List<RowItem> rowItem;
	
	CustomAdapter(Context context, List<RowItem> rowItem) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.rowItem = rowItem;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rowItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return rowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return rowItem.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			LayoutInflater mInflater = (LayoutInflater)context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_item, null);
			
		}
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		RowItem row_pos = rowItem.get(position);
		
		imgIcon.setImageResource(row_pos.getIcon());
		txtTitle.setText(row_pos.getTitle());
		return convertView;
	}

}
