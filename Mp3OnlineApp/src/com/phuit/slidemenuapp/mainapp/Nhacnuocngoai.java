package com.phuit.slidemenuapp.mainapp;
import com.phuit.slidemenuapp.R;
import com.phuit.slidemenuapp.swipetab.TabAdapter2;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Nhacnuocngoai extends android.support.v4.app.Fragment{

	private ViewPager viewPager;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.nhacnuocngoai, container, false);		
		viewPager = (ViewPager) rootView.findViewById(R.id.tabspager2);
		viewPager.setAdapter(new TabAdapter2(getChildFragmentManager()));	
		return rootView;
	}

}