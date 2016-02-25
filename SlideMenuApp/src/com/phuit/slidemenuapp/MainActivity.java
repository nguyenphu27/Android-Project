package com.phuit.slidemenuapp;

import java.util.ArrayList;
import java.util.List;
import com.phuit.slidemenuapp.mainapp.Bangxephang;
import com.phuit.slidemenuapp.mainapp.Caidat;
import com.phuit.slidemenuapp.mainapp.NhacDj;
import com.phuit.slidemenuapp.mainapp.Nhacmoi;
import com.phuit.slidemenuapp.mainapp.Nhacnuocngoai;
import com.phuit.slidemenuapp.mainapp.Nhacthugian;
import com.phuit.slidemenuapp.mainapp.Nhactrutinh;
import com.phuit.slidemenuapp.mainapp.Nhactuyenchon;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity{
		
	//declare variable of navigation drawer--
	String[] menutitles;
	TypedArray menuIcons;
	
	private CharSequence mTitle = "Menu";	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;
	private List<RowItem> rowItems;
	private CustomAdapter adapter;
		
	//---------------------------------------
	Fragment frag1 = new Nhactuyenchon();
	Fragment frag2 = new Nhacnuocngoai();
	Fragment frag3 = new Nhacmoi();
	Fragment frag4 = new Bangxephang();
	Fragment frag5 = new NhacDj();
	Fragment frag6 = new Nhacthugian();
	Fragment frag7 = new Nhactrutinh();
	Fragment frag8 = new Caidat();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//set Navigation Drawer
		NavDrawer(savedInstanceState);			
}
	

	@SuppressLint("NewApi")
	public void NavDrawer(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		menutitles = getResources().getStringArray(R.array.titles);
		menuIcons = getResources().obtainTypedArray(R.array.icons);
		
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerList = (ListView)findViewById(R.id.slider_list);
		
		rowItems  = new ArrayList<RowItem>();
		
		for(int i = 0; i<menutitles.length;i++){
			RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(i, -1));
			rowItems.add(items);
		}
		menuIcons.recycle();
		adapter = new CustomAdapter(getApplicationContext(), rowItems);
		mDrawerList.setAdapter(adapter);		
		mDrawerList.setOnItemClickListener(new SlideItemListerner());
		
		if(savedInstanceState == null){
			updateDisplay(0);
		}
		
		//Toggle use support library v4
		mDrawerToggle = new android.support.v4.app.ActionBarDrawerToggle
				(this, mDrawerLayout,R.drawable.ic_drawer, R.string.openapp, R.string.closeapp){

					@Override
					public void onDrawerClosed(View drawerView) {
						// TODO Auto-generated method stub						
						getSupportActionBar().setTitle(getTitle());
						invalidateOptionsMenu();
					}

					@Override
					public void onDrawerOpened(View drawerView) {
						// TODO Auto-generated method stub
						getSupportActionBar().setTitle(mTitle);								
						invalidateOptionsMenu();
					}			
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);	
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 128, 0)));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
		//tao trong style icon drawer la het 
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
		//sua app:showaction trong main.xml la always
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if(mDrawerToggle.onOptionsItemSelected(item)){
			return true;
		}
		return super.onOptionsItemSelected(item);		
	
	}
	
	class SlideItemListerner implements ListView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			updateDisplay(position);
		}				
	}
	private void updateDisplay(int i) {
		// TODO Auto-generated method stub
		//Fragment fragment = null;		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		switch(i){
		case 0: 			
			ft.replace(R.id.frame_container, frag1);
			break;
		case 1: 
			ft.replace(R.id.frame_container, frag2);
			break;
		case 2: 
			ft.replace(R.id.frame_container, frag3);
			break;
		case 3: 
			ft.replace(R.id.frame_container, frag4);
			break;
		case 4: 			
			ft.replace(R.id.frame_container, frag5);
			break;
		case 5: 
			ft.replace(R.id.frame_container, frag6);
			break;
		case 6: 
			ft.replace(R.id.frame_container, frag7);
			break;
		case 7: 
			ft.replace(R.id.frame_container, frag8);
			break;
		case 8: 
		    moveTaskToBack(true);         
            System.exit(1);
			break;
		default:
				break;
		}
		ft.commit();
		mDrawerList.setItemChecked(i, true);
		setTitle(menutitles[i]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
}