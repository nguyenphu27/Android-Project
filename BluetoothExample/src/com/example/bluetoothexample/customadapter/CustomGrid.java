package com.example.bluetoothexample.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bluetoothexample.R;

public class CustomGrid extends BaseAdapter{

	private Context mContext;
	private final String[] title;
	private final String[] value;
	private final String[] unit;
	private final int[] iconId;
	private final int mode;
	
	public CustomGrid(Context c, String[] title, int[] iconId, int mode) {
		// TODO Auto-generated constructor stub
		mContext = c;
		this.title = title;
		this.iconId = iconId;
		this.mode = mode;
		this.value = null;
		this.unit = null;
	}
	
	public CustomGrid(Context c, String[] title, String[] value, String[] unit, int[] iconId) {
		// TODO Auto-generated constructor stub
		mContext = c;
		this.title = title;
		this.value = value;
		this.unit = unit;
		this.iconId = iconId;
		this.mode = 2;
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

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(view == null){
			grid = new View(mContext);
			if(this.mode == 0){
				grid = inflater.inflate(R.layout.single_grid, null);
				TextView mtitle = (TextView) grid.findViewById(R.id.tv_gridTitleCustom);
				ImageView mIcon = (ImageView) grid.findViewById(R.id.imv_gridIconCustom);
				
				mtitle.setText(title[position]);
				mIcon.setImageResource(iconId[position]);
			}else if(this.mode == 1){
				grid = inflater.inflate(R.layout.single_menu, parent, false);
				
				TextView stitle = (TextView) grid.findViewById(R.id.tv_titleMenu);
				stitle.setText(title[position]);
				
				ImageView sIcon = (ImageView) grid.findViewById(R.id.imv_iconmenu);	
				sIcon.setImageResource(iconId[position]);
				
			}else if(this.mode == 2){
				grid = inflater.inflate(R.layout.single_ews_main_activity, null);
				TextView etitle = (TextView) grid.findViewById(R.id.tv_title_ews);
				TextView evalue = (TextView) grid.findViewById(R.id.tv_value_ews);
				TextView eunit = (TextView) grid.findViewById(R.id.tv_unit_ews);
				ImageView eIcon = (ImageView) grid.findViewById(R.id.imv_icon_ews);
				
				etitle.setText(title[position]);
				evalue.setText(value[position]);
				eunit.setText(unit[position]);
				eIcon.setImageResource(iconId[position]);
			}
			
	 	}else{
	 		grid = (View) view;
	 	}
		
		return grid;
	}

}
