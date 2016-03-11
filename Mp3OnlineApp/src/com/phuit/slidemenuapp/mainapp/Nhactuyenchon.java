package com.phuit.slidemenuapp.mainapp;

import com.phuit.slidemenuapp.R;
import com.phuit.slidemenuapp.swipetab.TabsAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Nhactuyenchon extends android.support.v4.app.Fragment{

	private ViewPager viewPager;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.nhactuyenchon, container, false);		
		viewPager = (ViewPager) rootView.findViewById(R.id.tabspager);
		viewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));	
		return rootView;
	}

}