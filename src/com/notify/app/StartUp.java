package com.notify.app;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.crouton.Crouton;
import com.notify.app.crouton.Style;
import com.notify.app.shimmer.Shimmer;
import com.notify.app.shimmer.ShimmerTextView;
import com.notify.app.volley.AppController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class StartUp extends Activity {
	ShimmerTextView tv;
	Shimmer shimmer;
	private ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();
	MySQLAdapter adapterMy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup);
		tv = (ShimmerTextView) findViewById(R.id.shimmer_tv);
		tv.setTypeface(Typeface.createFromAsset(getAssets(), "ff.ttf"));

		// shimmer = new Shimmer();
		// shimmer.setDuration(2000);
		// shimmer.setRepeatCount(0);
		// tv.setReflectionColor(getResources().getColor(R.color.text_shadow_white));
		// shimmer.start(tv);

		adapterMy = new MySQLAdapter(this);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// new NetCheck().execute();

				// Intent i = new Intent(StartUp.this, MainListy.class);
				// i.putParcelableArrayListExtra("feeds", feedItems);
				// startActivity(i);
				// finish();
				if (adapterMy.isLoggedIn()) {
					Intent i = new Intent(StartUp.this, MainListy.class);
					startActivity(i);
				} else {
					Intent i = new Intent(StartUp.this, LogInActivity.class);
					startActivity(i);
				}

				finish();

			}
		}, 2500);

	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// nDialog = new ProgressDialog(MainActivity.this);
			// tvMessage.setText("Checking Network...");
			// nDialog.setMessage("Loading..");
			// nDialog.setIndeterminate(false);
			// nDialog.setCancelable(true);
			// nDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			/**
			 * Gets current device state and checks for working internet
			 * connection by trying Google.
			 **/
			ConnectivityManager cm = (ConnectivityManager) StartUp.this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL(UserFunctions.chkConnURL);
					// URL url = new
					// URL("http://10.0.2.2/notified/all_con.php");
					HttpURLConnection urlc = (HttpURLConnection) url
							.openConnection();
					urlc.setConnectTimeout(3000);
					urlc.connect();
					if (urlc.getResponseCode() == 200) {

						return true;
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					// Message.message(getApplicationContext(), "malformedUrl");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// Message.message(getApplicationContext(), "exception");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// Message.message(getApplicationContext(), "exception");
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean th) {
			if (th == true) {
				// nDialog.dismiss();
				Log.d("nhjk", "connected");

				// making fresh volley request and getting json

				JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
						UserFunctions.URL_FEED, null,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Log.d("something",
										"Responses: " + response.toString());
								if (response != null) {
									// Message.message(MainListy.this,
									// "entered bel ljkhj");
									parseJsonFeed(response);
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								VolleyLog.d("volley",
										"Error: " + error.getMessage());
							}
						});

				// Adding request to volley request queue
				AppController.getInstance().addToRequestQueue(jsonReq);

				Crouton.makeText(StartUp.this, "Inside Connection", Style.INFO)
						.show();

			} else {
				Message.message(StartUp.this, "Error in Network Connection");

			}

		}

	}

	private void parseJsonFeed(JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("feeds");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				FeedItem item = new FeedItem();
				item.id = (feedObj.getInt("id"));
				item.posted_date = (feedObj.getString("posted_date"));

				// Image might be null sometimes
				// String image = feedObj.isNull("pics") ? null : feedObj
				// .getString("pics");
				//
				// item.pics = image;
				// Log.d("asd", item.pics);
				item.title = (feedObj.getString("title"));
				item.desc = (feedObj.getString("desc"));
				item.date = (feedObj.getString("date"));
				item.user_id = (feedObj.getInt("user_id"));

				// url might be null sometimes
				// String feedUrl = feedObj.isNull("url") ? null : feedObj
				// .getString("url");
				// item.pics = (feedUrl);

				feedItems.add(item);
			}

			// notify data changes to list adapater
			// listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			Message.message(StartUp.this, "feeds: " + feedItems.size());

		}
	}

}
