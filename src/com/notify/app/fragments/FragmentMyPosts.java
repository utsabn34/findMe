package com.notify.app.fragments;

import com.notify.app.R;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.notify.app.adapters.FeedListAdapter;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.ProgressBarCircularIndetermininate;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.dialogs.ShowFull;
import com.notify.app.volley.AppController;

public class FragmentMyPosts extends Fragment implements OnClickListener {
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		 outState.putBoolean("isrelmyposts", isRel);
		 outState.putBoolean("isbutmyposts", isbut);
		 outState.putBoolean("dfrshmyposts", downRefreshing);
		outState.putParcelableArrayList("feediitems_myposts", feedItems);
		super.onSaveInstanceState(outState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_myposts, container,
				false);
		fm=getActivity().getSupportFragmentManager();
		if (savedInstanceState != null) {
			feedItems = savedInstanceState
					.getParcelableArrayList("feediitems_Fusers");
			isRel = savedInstanceState.getBoolean("isrelmyposts");
			isbut = savedInstanceState.getBoolean("isbutmyposts");
			downRefreshing = savedInstanceState.getBoolean("dfrshmyposts");
			
		}
		initializers(view);

		if (savedInstanceState == null) {
			String params[] = { "all", adapter.getUserID() };

			new LoadMyPosts().execute(params);

		} else {
			if (isRel) {
				loadRel.setVisibility(View.VISIBLE);
				if (isbut) {
					errorView.setVisibility(View.VISIBLE);
				} else {
					errorView.setVisibility(View.GONE);
					bar.setVisibility(View.VISIBLE);
				}
			}  else {
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

		return view;
	}

	public void removeItems(int position) {
		feedItems.remove(position);
		listAdapter.notifyDataSetChanged();
		if (feedItems.size() == 0) {
			tvNOPosts.setVisibility(View.VISIBLE);

		}
	}

	public void refresh(int position) {
		feedItems.get(position).no_comm = 0;
		listAdapter.notifyDataSetChanged();
	}

	

	private class LoadMyPosts extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;
		String urll = UserFunctions.MY_POSTS_FEED;
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

						urll = urll + "?userid" + "=" + adapter.getUserID();

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
				Entry entry = cache.get(UserFunctions.B_FEED);
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

	public void deleteSingleComment(String comment_id, int position) {
		ShowFull fullBack = (ShowFull) getActivity()
				.getSupportFragmentManager().findFragmentByTag("justaaaFull");
		fullBack.deleteSingleComment(comment_id, position);
	}

	private String parseJsonFeed(JSONObject response) {
		String what = "";
		try {
			ArrayList<FeedItem> feedItemTemp = new ArrayList<FeedItem>();
			JSONArray feedArray = response.getJSONArray("feeds");
			what = response.getString("which");
			// Message.message(MainListy.this, what + "");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				FeedItem item = new FeedItem();
				item.id = (feedObj.getInt("id"));
				item.posted_date = (feedObj.getString("posted_date"));
				item.username = (feedObj.getString("username"));
				// Image might be null sometimes
				// String image = feedObj.isNull("pics") ? null : feedObj
				// .getString("pics");
				//
				// item.pics = image;
				// Log.d("asd", item.pics);
				item.title = (feedObj.getString("title"));
				item.desc = (feedObj.getString("desc"));
				item.lat = (feedObj.getString("lat"));
				item.longi = (feedObj.getString("long"));
				item.date = (feedObj.getString("date"));
				item.user_id = (feedObj.getLong("user_id"));
				item.no_comm = (feedObj.getInt("no_comm"));
				// String str = (feedObj.getString("booked"));
				// item.isBookmarked = Boolean.parseBoolean(str);
				item.isBookmarked = (feedObj.getBoolean("booked"));

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
//			listAdapter.notifyDataSetChanged();

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
					tvNOPosts.setVisibility(View.VISIBLE);

				} else {
					tvNOPosts.setVisibility(View.GONE);
					listAdapter.notifyDataSetChanged();
				}

			}
		}
		return what;

	}

	public void initializers(View view) {
		con = getActivity();
		fm = getActivity().getSupportFragmentManager();
		adapter = new MySQLAdapter(con);
		bar = (ProgressBarCircularIndetermininate) view.findViewById(R.id.pB_myposts);
		tvNOPosts = (TextView) view.findViewById(R.id.tvNoPosts);
		tvButAgain = (TextView) view.findViewById(R.id.butAgainTry_mypostsi);
		
		errorView = (LinearLayout) view.findViewById(R.id.layerrConnmypost);
		
		

		loadRel = (RelativeLayout) view.findViewById(R.id.layPb_LoadMyPost);
		listy = (PullToRefreshListView) view.findViewById(R.id.list_mypost);
		actualListView = listy.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		listAdapter = new FeedListAdapter((Activity) con, feedItems,
				FeedListAdapter.MY_POST_LISTY);
		actualListView.setAdapter(listAdapter);
		tvButAgain.setOnClickListener(this);

		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ShowFull full = new ShowFull();
				// full.setValues(feedItems.get(position - 1), position);
				Bundle args = new Bundle();
				args.putParcelable("feedsShoss", feedItems.get(position - 1));
				args.putInt("possition", position - 1);
				args.putBoolean("isDeletable", true);
				full.setArguments(args);
				full.show(fm, "justaaaFull");

			}
		});
	}

	Context con;
	FragmentManager fm;
	LinearLayout errorView;

	private boolean downRefreshing = false;
	private FeedListAdapter listAdapter;
	ListView actualListView;
	boolean isRel = true, isbut = false;
	TextView tvNOPosts, tvButAgain, tv;
	ProgressBarCircularIndetermininate bar;
	RelativeLayout loadRel;
	MySQLAdapter adapter;
	private ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();
	PullToRefreshListView listy;
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.butAgainTry_mypostsi) {
			if (isbut) {
				isbut = false;
				errorView.setVisibility(View.GONE);
//				tvButAgain.setText("");
				bar.setVisibility(View.VISIBLE);
				String params[] = { "all", adapter.getUserID() };

				new LoadMyPosts().execute(params);
			}

		}		
	}

}