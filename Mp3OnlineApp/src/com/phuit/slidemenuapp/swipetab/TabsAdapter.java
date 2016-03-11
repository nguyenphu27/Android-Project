package com.phuit.slidemenuapp.swipetab;

import com.phuit.slidemenuapp.mainapp.Tab1;
import com.phuit.slidemenuapp.mainapp.Tab2;
import com.phuit.slidemenuapp.mainapp.Tab3;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter{

	private int NUMBER_TABS = 3;	
	private String tabs[] = new String[] { "Nhạc trẻ", "Nhạc Remix", "Tuyệt phẩm song ca" };
	
	public TabsAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 0: return new Tab1();
		case 1:	return new Tab2();
		case 2:	return new Tab3();
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
