package com.notify.app.fragments;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.notify.app.R;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.adapters.FeedFUsers;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.ProgressBarCircularIndetermininate;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.volley.AppController;

public class FragmentFUsers extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_fusers, container, false);
		fm = getActivity().getSupportFragmentManager();
		if (savedInstanceState != null) {
			feedItems = savedInstanceState
					.getParcelableArrayList("feediitems_Fusers");
			isRel = savedInstanceState.getBoolean("isrelFusers");
			isbut = savedInstanceState.getBoolean("isbutFusers");
			downRefreshing = savedInstanceState.getBoolean("isdownnFusers");
			
		}

		initializers(view);

		if (savedInstanceState == null) {
			String params[] = { "all", adapter.getUserID() };

			new LoadFUsers().execute(params);

		} else {
			if (feedItems.size() == 0) {
				tvNOFUsers.setVisibility(View.VISIBLE);
				loadRel.setVisibility(View.GONE);
			} else {
				if (isRel) {
					loadRel.setVisibility(View.VISIBLE);
					if (isbut) {
						errorView.setVisibility(View.VISIBLE);
					} else {
						errorView.setVisibility(View.GONE);
						bar.setVisibility(View.VISIBLE);
					}
				} else {
					loadRel.setVisibility(View.GONE);
					
					if(downRefreshing){
						tv = new TextView(con);
						tv.setText("Loading..");
						tv.setGravity(Gravity.CENTER);
						tv.setPadding(10, 5, 10, 5);
						actualListView.addFooterView(tv);
					}else{
						if (tv != null && actualListView != null) {
							actualListView.removeFooterView(tv);
						}
					}
				}
			}

		}

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("isrelFusers", isRel);
		outState.putBoolean("isbutFusers", isbut);
		outState.putBoolean("isdownnFusers", downRefreshing);
		outState.putParcelableArrayList("feediitems_Fusers", feedItems);
		super.onSaveInstanceState(outState);
	}

	private class LoadFUsers extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;
		String urll = UserFunctions.FUser_FEED;
		String dec = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(String... args) {

			ConnectivityManager cm = (ConnectivityManager) con
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

						// urll = urll + "?userid" + "=" + adapter.getUserID();

						if (args[0].equals("up")) {
							urll = urll + "?" + args[0] + "="
									+ feedItems.get(0).id + "&userid="
									+ args[1];
						} else if (args[0].equals("down")) {
							urll = urll + "?" + args[0] + "="
									+ feedItems.get(feedItems.size() - 1).id
									+ "&userid=" + args[1];
						} else {
							urll = urll + "?userid=" + args[1];
						}

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
				jsonParams.put("which", "pp");

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
				urll = UserFunctions.B_FEED;

			} else {

				Message.message(con, "Error in Network Connection");
				// We first check for cached request
				Cache cache = AppController.getInstance().getRequestQueue()
						.getCache();
//				Entry entry = cache.get(UserFunctions.FUser_FEED + "?userid"
//						+ "=" + adapter.getUserID());
				Entry entry = cache.get(UserFunctions.FUser_FEED);
				if (entry != null) {// && feedItems.size() == 0
					// Message.message(MainListy.this, "entered ljkhj");
					// fetch the data from cache
					try {
						String data = new String(entry.data, "UTF-8");
						try {
							parseJsonFeed(new JSONObject(data));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} finally {
						// downRefreshing = false;
						// if(tv!=null && actualListView!=null){
						// actualListView.removeFooterView(tv);
						// }
						isbut = false;
						isRel = false;
						loadRel.setVisibility(View.GONE);
						errorView.setVisibility(View.GONE);
					}

				} else {

					bar.setVisibility(View.GONE);
					loadRel.setVisibility(View.VISIBLE);
					// butAgain.setVisibility(View.VISIBLE);
					errorView.setVisibility(View.VISIBLE);
//					tvButAgain.setText("Try Again");
					isbut = true;

				}

			}

		}

	}

	private String parseJsonFeed(JSONObject response) {
		String what = "";
		try {
			ArrayList<LogInfo> feedItemTemp = new ArrayList<LogInfo>();
			JSONArray feedArray = response.getJSONArray("fusers");
			what = response.getString("which");
			// Message.message(MainListy.this, what + "");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				LogInfo item = new LogInfo();
				item.id = (feedObj.getString("id"));
				item.uname = (feedObj.getString("uname"));
				item.fname = (feedObj.getString("fname"));
				item.lname = (feedObj.getString("lname"));
				item.dob = (feedObj.getString("dob"));
				item.country = (feedObj.getString("country"));
				item.city = (feedObj.getString("city"));
				item.email = (feedObj.getString("email"));
				item.created = (feedObj.getString("rdate"));
				item.lastLogin = (feedObj.getString("fdate"));

				feedItemTemp.add(item);
				// feedItems.add(item);
			}
			if (what.equals("up")) {
				feedItems.addAll(0, feedItemTemp);
			} else {
				if(feedItemTemp.size()==0 && what.equals("down")){
					downRefreshing=false;
				}
				feedItems.addAll(feedItemTemp);
			}

			// notify data changes to list adapater
			// if (feedItems.size() == 0) {
			// tvNOFUsers.setVisibility(View.VISIBLE);
			//
			// } else {
			// tvNOFUsers.setVisibility(View.GONE);
			// listAdapter.notifyDataSetChanged();
			// }

		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (what.equals("up")) {
				listy.onRefreshComplete();
			} else if (what.equals("down")) {
				downRefreshing = !downRefreshing;
				if (tv != null && actualListView != null) {
					actualListView.removeFooterView(tv);
				}

			} else {
				isRel = false;
				isbut = false;
				loadRel.setVisibility(View.GONE);
				if (feedItems.size() == 0) {
					tvNOFUsers.setVisibility(View.VISIBLE);

				} else {
					tvNOFUsers.setVisibility(View.GONE);
					listAdapter.notifyDataSetChanged();
				}

			}
		}
		return what;

	}

	public void initializers(View view) {
		con = getActivity();
		adapter = new MySQLAdapter(con);
		bar = (ProgressBarCircularIndetermininate) view
				.findViewById(R.id.pBFUsers);
		errorView = (LinearLayout) view.findViewById(R.id.layerrConn);
		loadRel = (RelativeLayout) view.findViewById(R.id.layPb_FUsers);
		tvNOFUsers = (TextView) view.findViewById(R.id.tvNoFUsers);
		tvButAgain = (TextView) view.findViewById(R.id.butAgainTryFUsers);
		listy = (PullToRefreshListView) view.findViewById(R.id.list_FUsers);
		actualListView = listy.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		// LogInfo info = new LogInfo("", "Bijznass", "Sanjib", "Maharjan", "",
		// "cshanjib@gmail.com", "", "", "Nepal", "Kathmandu", "");
		// feedItems.add(info);
		// feedItems.add(info);
		listAdapter = new FeedFUsers((Activity) con, feedItems);
		actualListView.setAdapter(listAdapter);

		tvButAgain.setOnClickListener(this);

		// Add an end-of-list listener
		listy.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (!downRefreshing) {
					Toast.makeText(con, "End of List!", Toast.LENGTH_SHORT)
							.show();

					tv = new TextView(con);
					tv.setText("Loading..");
					tv.setGravity(Gravity.CENTER);
					tv.setPadding(10, 5, 10, 5);

					// bar.setLayoutParams(new LayoutParams(
					// LayoutParams.MATCH_PARENT,
					// LayoutParams.WRAP_CONTENT));
					actualListView.addFooterView(tv);

					String params[] = { "down", adapter.getUserID() };
					downRefreshing = true;
					new LoadFUsers().execute(params);
				}
			}
		});
		// Thread.sleep(4000);

	}

	public void removeItems(int position) {
		feedItems.remove(position);
		listAdapter.notifyDataSetChanged();
		if (feedItems.size() == 0) {
			// tvNObookmarks.setVisibility(View.VISIBLE);

		}
	}

	private boolean downRefreshing = false;
	Context con;
	TextView tv;
	private FeedFUsers listAdapter;
	ListView actualListView;
	ProgressBarCircularIndetermininate bar;
	TextView tvNOFUsers,tvButAgain;
	RelativeLayout loadRel;
	LinearLayout errorView;
	MySQLAdapter adapter;
	private ArrayList<LogInfo> feedItems = new ArrayList<LogInfo>();
	PullToRefreshListView listy;
	FragmentManager fm;
	boolean isRel = true, isbut = false;
	
	public void refreshList(int pos){
		feedItems.remove(pos);
		listAdapter.notifyDataSetChanged();
		if(feedItems.size() == 0){
			tvNOFUsers.setVisibility(View.VISIBLE);
			
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.butAgainTryFUsers) {
			if (isbut) {
				isbut = false;
				errorView.setVisibility(View.GONE);
//				tvButAgain.setText("");
				bar.setVisibility(View.VISIBLE);
				String params[] = { "all", adapter.getUserID() };

				new LoadFUsers().execute(params);
			}

		}

	}
}