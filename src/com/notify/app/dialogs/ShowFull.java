package com.notify.app.dialogs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.notify.app.adapters.FeedCommentAdapter;
import com.notify.app.adapters.FeedListAdapter;
import com.notify.app.adapters.GPSTracker;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.NestedListView;
import com.notify.app.adapters.RippleView;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.WorkAroundMapFragment;
import com.notify.app.adapters.Classes.CommentBlock;
import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.adapters.WorkAroundMapFragment.MapCallback;
import com.notify.app.fragments.FragmentSettings;
import com.notify.app.volley.AppController;
import com.notify.app.R;

public class ShowFull extends DialogFragment implements OnClickListener,
		MapCallback {

	public static ShowFull getInstance(int position, FeedItem item) {
		ShowFull fragment = new ShowFull();
		fragment.position = position;
		fragment.item = item;
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		commTrigger = (updateComment) activity;
	}

	// public void setValues(FeedItem item, int pos) {
	//
	// this.item = item;
	// this.position = pos;
	// }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);
	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
		arg0.putBoolean("looodComm", isLoading);
		arg0.putBoolean("pooostComm", isPosting);
		arg0.putBoolean("buttonss", isButVisible);
		arg0.putBoolean("isddeletable", isDeletable);
		arg0.putInt("posShowfull", position);
		arg0.putParcelable("itemShowfull", item);
		arg0.putParcelableArrayList("commShowfull", commblock);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);

			getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationPop;
		}
		if (savedInstanceState == null) {
			item = getArguments().getParcelable("feedsShoss");
			position = getArguments().getInt("possition");
			isDeletable = getArguments().getBoolean("isDeletable");
		} else {
			item = savedInstanceState.getParcelable("itemShowfull");
			isLoading = savedInstanceState.getBoolean("looodComm");
			isPosting = savedInstanceState.getBoolean("isPosting");
			isButVisible = savedInstanceState.getBoolean("buttonss");
			position = savedInstanceState.getInt("posShowfull");
			commblock = savedInstanceState
					.getParcelableArrayList("commShowfull");
			isDeletable = savedInstanceState.getBoolean("isddeletable");

		}

		View root = inflater.inflate(R.layout.show_full, container, false);
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();

		// mySQLAdapter = new MySQLAdapter(con);
		//
		// Message.message(con, mySQLAdapter.getUser().id + "");
		initializers(root);

		if (savedInstanceState == null) {
			adapArrayAdapters = new FeedCommentAdapter(con, commblock,
					isDeletable, commTrigger);
			listComments.setAdapter(adapArrayAdapters);
			listComments.setDivider(null);
			if (item.no_comm != 0) {
				isLoading = true;
				loadingCOmment.setVisibility(View.VISIBLE);
				// Helper.getListViewSize(listComments);

				new LoadComments().execute();
			} else {
				isLoading = false;
				loadingCOmment.setVisibility(View.GONE);
			}
			viewScroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					viewScroll.fullScroll(View.FOCUS_UP);
				}
			});
		} else {
			adapArrayAdapters = new FeedCommentAdapter(con, commblock,
					isDeletable, commTrigger);
			listComments.setAdapter(adapArrayAdapters);

			if (isLoading) {
				loadingCOmment.setVisibility(View.VISIBLE);
			} else {
				loadingCOmment.setVisibility(View.GONE);
			}

			if (isPosting) {
				postingComment.setVisibility(View.VISIBLE);
			} else {
				postingComment.setVisibility(View.GONE);
			}
			if (isButVisible) {
				loadCommentAgain.setVisibility(View.VISIBLE);
			} else {
				loadCommentAgain.setVisibility(View.GONE);
			}
		}

		return root;
	}

	private class LoadComments extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;
		String urll = UserFunctions.Comment_FEED;
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

						urll = urll + "?feedid" + "=" + item.id;

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
				urll = UserFunctions.Comment_FEED;

			} else {

				Message.message(con, "Error in Network Connection");
				// We first check for cached request
				Cache cache = AppController.getInstance().getRequestQueue()
						.getCache();
				Entry entry = cache.get(UserFunctions.Comment_FEED);
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
					}

				} else {

					loadingCOmment.setVisibility(View.GONE);
					isLoading = false;
					loadCommentAgain.setVisibility(View.VISIBLE);
					isButVisible = true;

				}

			}

		}

	}

	private String parseJsonFeed(JSONObject response) {
		String what = "";
		try {
			ArrayList<CommentBlock> feedItemTemp = new ArrayList<CommentBlock>();
			JSONArray feedArray = response.getJSONArray("comments");
			what = response.getString("success");
			// Message.message(con, what + "");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				CommentBlock item = new CommentBlock("", "", "", "", "", "");
				item.id = (feedObj.getString("id"));
				item.post_id = (feedObj.getString("pid"));
				item.user_id = (feedObj.getString("uid"));
				item.comment = (feedObj.getString("comment"));
				item.date = (feedObj.getString("commented_date"));
				// Image might be null sometimes
				// String image = feedObj.isNull("pics") ? null : feedObj
				// .getString("pics");

				// Log.d("asd", item.pics);
				item.postedName = (feedObj.getString("username"));

				// url might be null sometimes
				// String feedUrl = feedObj.isNull("url") ? null : feedObj
				// .getString("url");
				// item.pics = (feedUrl);

				commblock.add(item);
				// feedItems.add(item);
			}
			// if (what.equals("up")) {
			// feedItems.addAll(0, feedItemTemp);
			// } else {
			// feedItems.addAll(feedItemTemp);
			// }

			// notify data changes to list adapater

		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			adapArrayAdapters.notifyDataSetChanged();
			// Helper.getListViewSize(listComments);
			loadingCOmment.setVisibility(View.GONE);
			isLoading = false;
		}
		return what;

	}

	public void deleteSingleComment(String comment_id, int position) {
		String[] params = { comment_id, "comm", position + "" };
		new NetCheck().execute(params);
	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;
		String commment = "";
		String type = "";
		int pos = -1;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isPosting = true;
			postingComment.setVisibility(View.VISIBLE);
			butClickPost.setClickable(false);
			tv_display.setText(UserFunctions.conn_chk_string);

		}

		@Override
		protected Boolean doInBackground(String... args) {
			/**
			 * Gets current device state and checks for working internet
			 * connection by trying Google.
			 * 
			 **/
			commment = args[0];
			type = args[1];
			if (type.equals("comm")) {
				pos = Integer.parseInt(args[2]);
			}

			ConnectivityManager cm = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL(UserFunctions.chkConnURL);
					// URL url = new URL("http://www.google.com");
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
				// Log.d("nhjk", "connected");
				// tvMessgs.setText("");
				MySQLAdapter adap = new MySQLAdapter(con);
				if (type.equals("post")) {
					String[] params = { commment, adap.getUserID() };
					new ProcessPostComment().execute(params);
				} else {
					new ProcessDeleteComment().execute(commment, pos + "");
				}

			} else {
				isPosting = false;
				butClickPost.setClickable(true);
				FeedCommentAdapter.IS_COMMENT_DELETABLE=true;
				postingComment.setVisibility(View.GONE);
				Message.message(con, "Error in Network Connection");
			}
		}

	}

	private class ProcessPostComment extends
			AsyncTask<String, JSONObject, JSONObject> {
		private ProgressDialog pDialog;
		String uname, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tv_display.setText("posting comment..");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.postComment(item.id + "", args[1],
					args[0]);
			JSONObject json_user = null;
			Log.d("LOGGGG", json.toString());
			try {
				if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
					String res = json.getString(UserFunctions.KEY_SUCCESS);
					Log.d("LOGGGG", res.toString());

					if (Integer.parseInt(res) == 1) {
						// pDialog.setMessage("Loading User Space");
						// pDialog.setTitle("Getting Data");
						JSONArray json_added_comm = json
								.getJSONArray("comments");

						json_user = json_added_comm.getJSONObject(0);

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return json_user;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			if (json != null) {

				try {
					Message.message(con, "Successfully commented");
					MySQLAdapter adap = new MySQLAdapter(con);
					commblock.add(
							0,
							new CommentBlock(json.getString("id"), json
									.getString("pid"), json.getString("uid"),
									json.getString("comment"), json
											.getString("date"), adap
											.getUserName()));
					adapArrayAdapters.notifyDataSetChanged();
					// Helper.getListViewSize(listComments);

					String[] time = item.posted_date.split(" ");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					Date mDate = null;
					try {
						mDate = sdf.parse(item.posted_date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long timeInMilliseconds = mDate.getTime();
					String what_what = UserFunctions.get_RelativeTime(
							System.currentTimeMillis(), timeInMilliseconds);
					if (what_what.equals("what what")) {
						what_what = FeedListAdapter.formatTime(time[0]);
					}
					item.no_comm++;
					if (item.no_comm > 1) {
						tv_posted.setText("Posted By:" + item.username + " | "
								+ what_what + " | " + item.no_comm
								+ " comments");
					} else {
						tv_posted
								.setText("Posted By:" + item.username + " | "
										+ what_what + " | " + item.no_comm
										+ " comment");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					isPosting = false;
					postingComment.setVisibility(View.GONE);
					butClickPost.setClickable(true);
					et_comment.setText("");

					commTrigger.update_comment(position, item.no_comm);

				}

			} else {
				// butLogin.setClickable(true);
				//
				// tvSignups.setClickable(true);
				// tvMessgs.setText("");
				// Message.message(con, "Does not match");
				// pDialog.dismiss();
				// loginErrorMsg.setText("Incorrect username/password");
			}
		}

	}

	private class ProcessDeleteComment extends
			AsyncTask<String, JSONObject, String> {
		int pos = -1;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tv_display.setText("deleting comment..");
		}

		@Override
		protected String doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			String cid="";
			JSONObject json = userFunction.deleteSingleComment(args[0]);
			Log.d("LOGGGG", json.toString());
			try {
				if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
					String res = json.getString(UserFunctions.KEY_SUCCESS);
					Log.d("LOGGGG", res.toString());

					if (Integer.parseInt(res) == 1) {
						pos = Integer.parseInt(args[1]);
						cid = json.getString("cid");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return cid;
		}

		@Override
		protected void onPostExecute(String cid) {
			if (!cid.equals("")) {

				try {
					Message.message(con, "Successfully deleted comment.");
					commblock.remove(pos);
					adapArrayAdapters.notifyDataSetChanged();
					// Helper.getListViewSize(listComments);

					String[] time = item.posted_date.split(" ");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					Date mDate = null;
					try {
						mDate = sdf.parse(item.posted_date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long timeInMilliseconds = mDate.getTime();
					String what_what = UserFunctions.get_RelativeTime(
							System.currentTimeMillis(), timeInMilliseconds);
					if (what_what.equals("what what")) {
						what_what = FeedListAdapter.formatTime(time[0]);
					}
					item.no_comm--;
					if (item.no_comm > 1) {
						tv_posted.setText("Posted By:" + item.username + " | "
								+ what_what + " | " + item.no_comm
								+ " comments");
					} else {
						tv_posted
								.setText("Posted By:" + item.username + " | "
										+ what_what + " | " + item.no_comm
										+ " comment");
					}

				}finally {
					isPosting = false;
					postingComment.setVisibility(View.GONE);
					butClickPost.setClickable(true);
					FeedCommentAdapter.IS_COMMENT_DELETABLE=true;
					//et_comment.setText("");

//					commTrigger.update_comment(position, item.no_comm);

				}

			} else {
				Message.message(con, "something went wrong");
				FeedCommentAdapter.IS_COMMENT_DELETABLE=true;
				// butLogin.setClickable(true);
				//
				// tvSignups.setClickable(true);
				// tvMessgs.setText("");
				// Message.message(con, "Does not match");
				// pDialog.dismiss();
				// loginErrorMsg.setText("Incorrect username/password");
			}
		}

	}

	public LatLng gpsDecide() {
		LatLng latlng = null;
		GPSTracker gps;
		gps = new GPSTracker(con);

		// check if GPS enabled
		if (gps.canGetLocation()) {
			latlng = new LatLng(gps.getLatitude(), gps.getLongitude());
			hidelayout.setVisibility(View.GONE);

		} else {
			hidelayout.setVisibility(View.VISIBLE);
			// latlng = null;
		}
		return latlng;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (wait.equals("wait")) {
			GPSTracker gps;
			gps = new GPSTracker(con);

			if (gps.canGetLocation()) {

				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();

				hidelayout.setVisibility(View.GONE);

			} else {
				hidelayout.setVisibility(View.VISIBLE);
			}
			wait = "";
		}

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();

		// change dialog width
		if (getDialog() != null) {

			int fullWidth = getDialog().getWindow().getAttributes().width;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				Display display = getActivity().getWindowManager()
						.getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				fullWidth = size.x;
			} else {
				Display display = getActivity().getWindowManager()
						.getDefaultDisplay();
				fullWidth = display.getWidth();
			}

			final int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
							.getDisplayMetrics());

			int w = fullWidth - padding;
			int h = getDialog().getWindow().getAttributes().height;

			getDialog().getWindow().setLayout(w, h);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.okBut_Full) {
			getDialog().dismiss();

		} else if (arg0.getId() == R.id.turnGps) {
			wait = "wait";
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			con.startActivity(intent);

		} else if (arg0.getId() == R.id.postCommentFull) {

			if (et_comment.getText().toString().trim().equals("")) {
				Message.message(con, "Write something to comment");
			} else {
				InputMethodManager imm = (InputMethodManager) con
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
				new NetCheck().execute(et_comment.getText().toString().trim(),
						"post");
			}
		} else if (arg0.getId() == R.id.butLoadCommAgain) {
			isButVisible = false;
			loadCommentAgain.setVisibility(View.GONE);
			isLoading = true;
			loadingCOmment.setVisibility(View.VISIBLE);
			new LoadComments().execute();

		}
	}

	public void initializers(View root) {

		tv_title = (TextView) root.findViewById(R.id.tv_titlefull);
		tv_desc = (TextView) root.findViewById(R.id.tvHappen_full);
		tv_posted = (TextView) root.findViewById(R.id.tVposted_full);
		cancel = root.findViewById(R.id.okBut_Full);
		viewScroll = (ScrollView) root.findViewById(R.id.scroool_showfull);
		hidelayout = (LinearLayout) root.findViewById(R.id.lay_fullhide);
		viewClick = (RippleView) root.findViewById(R.id.turnGps);
		butClickPost = (RippleView) root.findViewById(R.id.postCommentFull);
		loadCommentAgain = (Button) root.findViewById(R.id.butLoadCommAgain);
		et_comment = (EditText) root.findViewById(R.id.et_postComm);
		listComments = (NestedListView) root.findViewById(R.id.listComments);
		loadingCOmment = (LinearLayout) root.findViewById(R.id.lloadComm);
		postingComment = (LinearLayout) root.findViewById(R.id.ppostComm);
		mapFrame = (FrameLayout) root.findViewById(R.id.map_full);
		tv_display = (TextView) root.findViewById(R.id.tv_disp_showfull);

		tv_title.setText(item.title);
		tv_desc.setText(item.desc);

		listComments.setScrollContainer(false);

		String[] time = item.posted_date.split(" ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		Date mDate = null;
		try {
			mDate = sdf.parse(item.posted_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timeInMilliseconds = mDate.getTime();
		String what_what = UserFunctions.get_RelativeTime(
				System.currentTimeMillis(), timeInMilliseconds);
		if (what_what.equals("what what")) {
			what_what = FeedListAdapter.formatTime(time[0]);
		}
		if (item.no_comm > 1) {
			tv_posted.setText("Posted By:" + item.username + " | " + what_what
					+ " | " + item.no_comm + " comments");
		} else {
			tv_posted.setText("Posted By:" + item.username + " | " + what_what
					+ " | " + item.no_comm + " comment");
		}

		cancel.setOnClickListener(this);
		viewClick.setOnClickListener(this);
		butClickPost.setOnClickListener(this);

		loadCommentAgain.setOnClickListener(this);
		// Message.message(con, item.lat + " " + item.longi);
	}

	public void setUpmaps(final GoogleMap mGoogleMap) {
		// fragment = (WorkAroundMapFragment) getChildFragmentManager()
		// .findFragmentByTag("childmapp");

		if (mGoogleMap != null) {

			if (!item.longi.equals("") && !item.lat.equals("")) {

				mGoogleMap.addMarker(new MarkerOptions()
						.position(
								new LatLng(Double.parseDouble(item.lat), Double
										.parseDouble(item.longi)))
						.title("Event Location")
						.snippet("Just try")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.marker_green)));

			}

			final LatLng latlong = gpsDecide();
			if (latlong != null) {
				mGoogleMap
						.addMarker(new MarkerOptions()
								.position(
										new LatLng(latlong.latitude,
												latlong.longitude))
								.title("Your Location")
								.snippet("You are here")
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.marker_blue)));
			}

			final View mapView = fragments.getView();

			if (mapView.getViewTreeObserver().isAlive() && latlong != null
					&& item.lat != null && item.longi != null) {
				mapView.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener() {
							@SuppressLint("NewApi")
							// We check which build version
							// we
							// are using.
							@Override
							public void onGlobalLayout() {
								LatLngBounds bounds = new LatLngBounds.Builder()
										.include(
												new LatLng(
														Double.parseDouble(item.lat),
														Double.parseDouble(item.longi)))
										.include(latlong).build();

								if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
									mapView.getViewTreeObserver()
											.removeGlobalOnLayoutListener(this);
								} else {
									mapView.getViewTreeObserver()
											.removeOnGlobalLayoutListener(this);
								}
								mGoogleMap.moveCamera(CameraUpdateFactory
										.newLatLngBounds(bounds, 50));
							}
						});

			}
		} else {
			Message.message(con, "map null");
		}

		fragments.setListener(new WorkAroundMapFragment.OnTouchListener() {

			@Override
			public void onTouch() {
				// TODO Auto-generated method stub
				viewScroll.requestDisallowInterceptTouchEvent(true);
			}
		});

		// FragmentTransaction transaction = getChildFragmentManager()
		// .beginTransaction();
		// transaction.add(R.id.map_full, fragments, "childmapp").commit();

	}

	public ShowFull() {
		// fragments = new WorkAroundMapFragment();

		// fragments = new WorkAroundMapFragment() {
		//
		// @Override
		// public void onActivityCreated(Bundle savedInstanceState) {
		// super.onActivityCreated(savedInstanceState);
		// // setRetainInstance(true);
		// mGoogleMap = fragments.getMap();
		// if (mGoogleMap != null) {
		// // Message.message(con, "Null");
		//
		// if (item.lat != null && !item.lat.equals("")) {
		//
		// mGoogleMap
		// .addMarker(new MarkerOptions()
		// .position(
		// new LatLng(
		// Double.parseDouble(item.lat),
		// Double.parseDouble(item.longi)))
		// .title("Event Location")
		// .snippet("Just try")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.marker_green)));
		//
		// }
		//
		// final LatLng latlong = gpsDecide();
		// if (latlong != null) {
		// mGoogleMap
		// .addMarker(new MarkerOptions()
		// .position(
		// new LatLng(
		// latlong.latitude,
		// latlong.longitude))
		// .title("Your Location")
		// .snippet("You are here")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.marker_blue)));
		// }
		//
		// final View mapView = fragments.getView();
		//
		// if (mapView.getViewTreeObserver().isAlive()
		// && latlong != null && item.lat != null
		// && item.longi != null) {
		// mapView.getViewTreeObserver()
		// .addOnGlobalLayoutListener(
		// new OnGlobalLayoutListener() {
		// @SuppressLint("NewApi")
		// // We check which build version
		// // we
		// // are using.
		// @Override
		// public void onGlobalLayout() {
		// LatLngBounds bounds = new LatLngBounds.Builder()
		// .include(
		// new LatLng(
		// Double.parseDouble(item.lat),
		// Double.parseDouble(item.longi)))
		// .include(latlong)
		// .build();
		//
		// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
		// mapView.getViewTreeObserver()
		// .removeGlobalOnLayoutListener(
		// this);
		// } else {
		// mapView.getViewTreeObserver()
		// .removeOnGlobalLayoutListener(
		// this);
		// }
		// mGoogleMap
		// .moveCamera(CameraUpdateFactory
		// .newLatLngBounds(
		// bounds,
		// 50));
		// }
		// });
		// }
		//
		//
		// fragments.setListener(new WorkAroundMapFragment.OnTouchListener() {
		//
		// @Override
		// public void onTouch() {
		// // TODO Auto-generated method stub
		// viewScroll.requestDisallowInterceptTouchEvent(true);
		// }
		// });
		//
		//
		//
		// }
		// }
		// };
		//

	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);

		if (!item.lat.equals("") && !item.longi.equals("")) {

			if (fragments == null) {
				// fragments = (WorkAroundMapFragment) WorkAroundMapFragment
				// .newInstance();
				fragments = new WorkAroundMapFragment();

				fragments.setMapCallback(this); // This activity will receive
												// the
												// Map object once the map
												// fragment
												// is fully loaded

				// Then we add it using a FragmentTransaction.
				FragmentTransaction transaction = getChildFragmentManager()
						.beginTransaction();
				transaction.add(R.id.map_full, fragments, "childmapp").commit();

			} else {
				fragments.setMapCallback(this); // This activity will receive
												// the
												// Map object once the map
												// fragment
												// is fully loaded
												// FragmentTransaction
												// transaction =
												// getChildFragmentManager()
				// .beginTransaction();
				// transaction.add(R.id.map_full, fragments,
				// "childmapp").commit();
			}
		} else {

			mapFrame.setVisibility(View.GONE);
		}

	}

	Context con;
	boolean isDeletable = false;
	FragmentSettings frags;
	FragmentManager manager;
	FrameLayout mapFrame;
	Button butClose;
	NestedListView listComments;
	ScrollView viewScroll;
	MySQLAdapter mySQLAdapter;
	View cancel;
	private WorkAroundMapFragment fragments;
	TextView tv_title, tv_desc, tv_posted;
	updateComment commTrigger;

	public interface updateComment {
		public void update_comment(int pos, int no_comm);

		public void deleteSingleComment(String comment_id, int position);
	}

	// private static WorkAroundMapFragment fragment;
	// GoogleMap mGoogleMap;
	LinearLayout hidelayout;
	RippleView viewClick, butClickPost;
	String wait = "";
	EditText et_comment;
	TextView tv_display;
	LinearLayout loadingCOmment, postingComment;
	public boolean isLoading = false;
	public boolean isButVisible = false;
	public boolean isPosting = false;
	int position;
	FeedCommentAdapter adapArrayAdapters;
	Button loadCommentAgain;
	ArrayList<CommentBlock> commblock = new ArrayList<CommentBlock>();
	FeedItem item = new FeedItem(0, 0, "", "", "", "", "", "", "", 0, false,
			false);

	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub
		setUpmaps(map);
		// Message.message(con, "Lati"+item.lat+"  Longi:" + item.longi);
	}

}