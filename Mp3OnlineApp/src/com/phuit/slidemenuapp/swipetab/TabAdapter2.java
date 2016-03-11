package com.phuit.slidemenuapp.swipetab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.phuit.slidemenuapp.mainapp.Tab4;
import com.phuit.slidemenuapp.mainapp.Tab5;
import com.phuit.slidemenuapp.mainapp.Tab6;

public class TabAdapter2 extends FragmentPagerAdapter{

	private int NUMBER_TABS = 3;	
	private String tabs[] = new String[] { "Top Billboard", "Nhạc KPop", "Nhạc US" };
	
	public TabAdapter2(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 0: return new Tab4();
		case 1:	return new Tab5();
		case 2:	return new Tab6();
		}
		return null;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return tabs[position];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return NUMBER_TABS;
	}

}