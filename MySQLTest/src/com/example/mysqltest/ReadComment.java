package com.example.mysqltest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ReadComment extends ListActivity implements OnClickListener{

		Button bPostcomment;
		
		// Progress Dialog
		private ProgressDialog pDialog;

		// Creating JSON Parser object
		JSONParser jParser = new JSONParser();

		ArrayList<HashMap<String, String>> productsList;

		// url to get all products list
		private static String url_all_products = "http://10.0.2.2:1234/PHPSCRIPTS/getAllCustomers.php";

		// JSON Node names
		private static final String TAG_SUCCESS = "success";
		private static final String TAG_PRODUCTS = "comments";
		private static final String TAG_PID = "post_id";
		//private static final String TAG_NAME = "name";

		// products JSONArray
		JSONArray products = null;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.read_comment);

			// Hashmap for ListView
			productsList = new ArrayList<HashMap<String, String>>();

			// Loading products in Background Thread
			new LoadAllProducts().execute();

			// Get listview
			ListView lv = getListView();

			// on seleting single product
			// launching Edit Product Screen
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// getting values from selected ListItem				
				}
			});

		}
		
		/**
		 * Background Async Task to Load all product by making HTTP Request
		 * */
		class LoadAllProducts extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(ReadComment.this);
				pDialog.setMessage("Loading products. Please wait...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}

			/**
			 * getting All products from url
			 * */
			protected String doInBackground(String... args) {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);				

				try {
					// Checking for SUCCESS TAG
						//int success = json.getInt(TAG_SUCCESS);
						
						// products found
						// Getting Array of Products
						products = json.getJSONArray(TAG_PRODUCTS);

						// looping through All Products
						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);

							// Storing each json item in variable
							String id = c.getString(TAG_PID);
							//String name = c.getString(TAG_NAME);

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							map.put(TAG_PID, id);
							//map.put(TAG_NAME, name);

							// adding HashList to ArrayList
							productsList.add(map);
						}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						ListAdapter adapter = new SimpleAdapter(
								ReadComment.this, productsList,
								R.layout.single_post, new String[] { TAG_PID
										},
								new int[] { R.id.id_tenbaihat});
						// updating listview
						setListAdapter(adapter);
					}
				});

			}

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
	}