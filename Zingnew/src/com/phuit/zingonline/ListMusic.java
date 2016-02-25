package com.phuit.zingonline;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListMusic extends Activity{
WebView weblist;
ListView lv_music;
Song song = null;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_music);
		weblist = (WebView)findViewById(R.id.weblist);
		weblist.getSettings().setJavaScriptEnabled(true);
		//weblist.loadUrl("file:///data/data/com.phuit.zingonline/file/musicpro.gif");
		weblist.loadUrl("file:///android_asset/musicpro.gif");
		final ArrayList<Song> arr = new ArrayList<Song>();
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(getAssets().open("listsong.xml"));
			
			NodeList nodelist = doc.getElementsByTagName("song");
			for(int i =0; i < nodelist.getLength();i++){
				Node node = nodelist.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE){
					song = new Song();
					Element elm = (Element)node;
					//ID
					NodeList idlist = elm.getElementsByTagName("id");
					Element id_Elm = (Element)idlist.item(0);
					song.setId(id_Elm.getTextContent().trim());
					//title
					NodeList titlelist = elm.getElementsByTagName("title");
					Element title_Elm = (Element)titlelist.item(0);
					song.setTitle(title_Elm.getTextContent().trim());
					//singer
					NodeList singerlist = elm.getElementsByTagName("singer");
					Element singer_Elm = (Element)singerlist.item(0);
					song.setSinger(singer_Elm.getTextContent().trim());
					
					arr.add(song);
				}
			}
		}catch(Exception e){
			
		}
		ListView lv = (ListView)findViewById(R.id.lv_music);		
		List_Adapter lArr = new List_Adapter(this, R.layout.list_view_ani,arr);
		lv.setAdapter(lArr);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
					String song_name = ((TextView) view.findViewById(R.id.tv_songs)).getText().toString();
					String singer_name = ((TextView) view.findViewById(R.id.tv_singers)).getText().toString();		
							
					Intent position_new = new Intent(getApplicationContext(), MainActivity.class);
					position_new.putExtra("Po", position);
					position_new.putExtra("music_name", song_name);
					position_new.putExtra("singer_name", singer_name);
					startActivity(position_new);															
			}													
		});
		
	}
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	    	   .setTitle("Thoát!")
	           .setMessage("Bạn chắc chắn muốn thoát?")
	           .setCancelable(false)
	           .setPositiveButton("Có", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    
	            	    moveTaskToBack(true);
	                    
	                    System.exit(1);
	                    }
	               }) 
	           .setNegativeButton("Không", null)
	           .show();
	}
	
}				
