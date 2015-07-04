package com.notify.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.internal.lv;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.PingAdapter;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.adapters.Classes.Users;
import com.notify.app.crouton.Crouton;
import com.notify.app.crouton.Style;
import com.notify.app.dialogs.PingConfirmDialog;
import com.notify.app.volley.AppController;

public class ShowUsers extends SherlockFragmentActivity implements
		OnClickListener, OnItemSelectedListener, OnItemClickListener {
	ListView listView;
	PingAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.display);
		listView = (ListView) findViewById(R.id.viewPing);
		new NetCheck().execute();
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;
		String urll = UserFunctions.URL_FEED2;
		String dec = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			

		}

		@Override
		protected Boolean doInBackground(String... args) {

			ConnectivityManager cm = (ConnectivityManager) ShowUsers.this
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
							urll = UserFunctions.URL_FEED2;

						return true;
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					// Message.message(getApplicationContext(), "malformedUrl");
					Log.d("sljhnad", "1");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("salkmnlmd", "2");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("sadbmbmbj", "3");
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean th) {
			if (th == true) {
				// nDialog.dismiss();
				Log.d("nhjk", "connected+u" + urll);

				// making fresh volley request and getting json
				Map<String, String> jsonParams = new HashMap<String, String>();
				jsonParams.put("which", "Androidhive");

				JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
						urll, new JSONObject(jsonParams),
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
						}) {

				};

				// Adding request to volley request queue
				AppController.getInstance().addToRequestQueue(jsonReq);
				urll = UserFunctions.URL_FEED;

				Crouton.makeText(ShowUsers.this, "Inside Connection",
						Style.INFO).show();

			} else {

				Message.message(ShowUsers.this, "Error in Network Connection");
				// We first check for cached request
				
			}

		}

	}
	ArrayList<Users> feedItemTemp = new ArrayList<Users>();
	private String parseJsonFeed(JSONObject response) {
		String what = "";
		
		try {
			
			JSONArray feedArray = response.getJSONArray("feeds");
			//what = response.getString("which");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				Users item = new Users();
				item.id = (feedObj.getString("id"));
				item.firstname = (feedObj.getString("firstname"));
				item.lastname = (feedObj.getString("lastname"));
				item.username = (feedObj.getString("username"));
				item.email = (feedObj.getString("email"));
				feedItemTemp.add(item);
			}
			

		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			adapter = new PingAdapter(ShowUsers.this, feedItemTemp);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			adapter.notifyDataSetChanged();
		}
		return what;

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent.getId() == R.id.viewPing){
			Message.message(ShowUsers.this, position+"sds");
			PingConfirmDialog dialog = new PingConfirmDialog();
			MySQLAdapter adap = new MySQLAdapter(ShowUsers.this);
			dialog.setValues(feedItemTemp.get(position).username+"(" +feedItemTemp.get(position).email+ ")", adap.getUser().id, feedItemTemp.get(position).id);
			dialog.show(getSupportFragmentManager(), "pingDiaglog");
		}
		
	}

}
