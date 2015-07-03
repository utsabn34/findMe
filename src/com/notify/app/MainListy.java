package com.notify.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.notify.app.adapters.AlarmManagerHelper;
import com.notify.app.adapters.Classes.NotiItem;
import com.notify.app.adapters.FeedListAdapter;
import com.notify.app.adapters.GPSTracker;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.ProgressBarCircularIndetermininate;
import com.notify.app.adapters.ServerUtilities;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Utils;
import com.notify.app.adapters.WakeLocker;
import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.crouton.Crouton;
import com.notify.app.crouton.Style;
import com.notify.app.dialogs.AlertDialog;
import com.notify.app.dialogs.Blocking.BlockTrig;
import com.notify.app.dialogs.LogOutDiaglog.LogOut;
import com.notify.app.dialogs.New_Notify;
import com.notify.app.dialogs.New_Notify.refreshNotify;
import com.notify.app.dialogs.Delete;
import com.notify.app.dialogs.LogOutDiaglog;
import com.notify.app.dialogs.NotificationPrompt;
import com.notify.app.dialogs.PopupMenuCompat;
import com.notify.app.dialogs.ShowFull;
import com.notify.app.dialogs.Signing;
import com.notify.app.dialogs.Bookmarking.BookMarkTrig;
import com.notify.app.dialogs.Following.FollowTrig;
import com.notify.app.dialogs.ShowFull.updateComment;
import com.notify.app.signups.SignUps;
import com.notify.app.volley.AppController;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainListy extends SherlockFragmentActivity implements
		OnClickListener, BookMarkTrig, updateComment, BlockTrig, refreshNotify,
		FollowTrig, LogOut {

	PullToRefreshListView listy;
	// list with the data to show in the listview
	// private LinkedList<String> mListItems;
	private FeedListAdapter listAdapter;
	private ArrayAdapter<String> mAdapter;
	TextView tv;
	GPSTracker gps;
	Context context;

	private ArrayList<FeedItem> feedItems;
	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	private boolean isLogged = false;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;
	private static boolean refreshing = false;
	private boolean downRefreshing = false;
	RelativeLayout bar;
	TextView butAgain;
	ListView actualListView;
	ProgressBarCircularIndetermininate pb;
	TextView tvTitle;
	ImageButton ib_overflow, ib_user_settings, ib_newNOtify;
	FragmentManager fm;
	// The data to be displayed in the ListView
	boolean isRel, isbut;

	// private String URL_FEED =
	// "http://www.sanjibmaharjan.com.np/notifies/gettin.php";
	private static final String TAG = MainListy.class.getSimpleName();

	MySQLAdapter adapterMy;

	/** Called when the activity is first created. */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("isrel", isRel);
		outState.putBoolean("isbut", isbut);
		outState.putParcelableArrayList("feediitems", feedItems);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listy_main);
		Window window = getWindow();
		UserFunctions.setStatusBarColor(findViewById(R.id.statusBarBackground),
				getResources().getColor(R.color.themed_dark), window, this);

		if (savedInstanceState != null) {
			feedItems = savedInstanceState.getParcelableArrayList("feediitems");

		} else {
			feedItems = new ArrayList<FeedItem>();
		}
		context = getApplicationContext();
		adapterMy = new MySQLAdapter(this);
		isLogged = adapterMy.isLoggedIn();
		tvTitle = (TextView) findViewById(R.id.tvTitletop);
		fm = getSupportFragmentManager();
		Typeface tf = Typeface.createFromAsset(getAssets(), "ff.ttf");
		tvTitle.setTypeface(tf);
		listy = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		ib_overflow = (ImageButton) findViewById(R.id.ib_overflow);
		ib_user_settings = (ImageButton) findViewById(R.id.ib_viewprofile);
		ib_newNOtify = (ImageButton) findViewById(R.id.ib_addnewpost);
		ib_newNOtify.setOnClickListener(this);
		bar = (RelativeLayout) findViewById(R.id.layPb);
		pb = (ProgressBarCircularIndetermininate) findViewById(R.id.pB);
		butAgain = (TextView) findViewById(R.id.butAgainTry);

		// feedItems = getIntent().getExtras().getParcelableArrayList("feeds");

		ib_overflow.setOnClickListener(this);
		ib_user_settings.setOnClickListener(this);
		butAgain.setOnClickListener(this);
		// Set a listener to be invoked when the list should be refreshed.
		listy.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.

				String params[] = { "up", adapterMy.getUserID() };

				new NetCheck().execute(params);
			}
		});

		// Add an end-of-list listener
		listy.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (!downRefreshing) {
					Toast.makeText(MainListy.this, "End of List!",
							Toast.LENGTH_SHORT).show();

					tv = new TextView(MainListy.this);
					tv.setText("Loading..");
					tv.setGravity(Gravity.CENTER);
					tv.setPadding(5, 5, 5, 5);

					// bar.setLayoutParams(new LayoutParams(
					// LayoutParams.MATCH_PARENT,
					// LayoutParams.WRAP_CONTENT));
					actualListView.addFooterView(tv);

					String params[] = { "down", adapterMy.getUserID() };
					downRefreshing = true;
					new NetCheck().execute(params);
				}
			}
		});

		actualListView = listy.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		/**
		 * Add Sound Event Listener
		 */
		// SoundPullEventListener<ListView> soundListener = new
		// SoundPullEventListener<ListView>(
		// this);
		// soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		// soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		// soundListener.addSoundEvent(State.REFRESHING,
		// R.raw.refreshing_sound);
		// listy.setOnPullEventListener(soundListener);

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)

		listAdapter = new FeedListAdapter(this, feedItems, 1);
		actualListView.setAdapter(listAdapter);

		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ShowFull full = new ShowFull();
				// full.setValues(feedItems.get(position - 1), position);
				Bundle args = new Bundle();
				args.putParcelable("feedsShoss", feedItems.get(position - 1));
				args.putInt("possition", position - 1);
				args.putBoolean("isDeletable", false);
				full.setArguments(args);
				full.show(fm, "justaaa");

			}
		});

		if (savedInstanceState == null) {
			butAgain.setText("");
			butAgain.setClickable(false);
			isRel = false;
			isbut = false;
			bar.setVisibility(View.VISIBLE);
			isRel = true;
			String params[] = { "all", adapterMy.getUserID() };
			new NetCheck().execute(params);

		} else {
			isRel = savedInstanceState.getBoolean("isrel");
			isbut = savedInstanceState.getBoolean("isbut");
			if (isRel) {
				bar.setVisibility(View.VISIBLE);
			} else {
				bar.setVisibility(View.GONE);
			}
			if (isbut) {
				pb.setVisibility(View.GONE);
				// butAgain.setVisibility(View.VISIBLE);
				butAgain.setText("Try Again");
				butAgain.setClickable(true);

			} else {
				// butAgain.setVisibility(View.GONE);
				butAgain.setText("");
				butAgain.setClickable(false);
				pb.setVisibility(View.VISIBLE);
			}

		}

	}

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private String parseJsonFeed(JSONObject response) {
		String what = "";
		try {
			ArrayList<FeedItem> feedItemTemp = new ArrayList<FeedItem>();
			JSONArray feedArray = response.getJSONArray("feeds");
			what = response.getString("which");
			Message.message(MainListy.this, what + "");

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
				item.isFollowed = (feedObj.getBoolean("isfollow"));

				// url might be null sometimes
				// String feedUrl = feedObj.isNull("url") ? null : feedObj
				// .getString("url");
				// item.pics = (feedUrl);

				feedItemTemp.add(item);
				// feedItems.add(item);
			}
			if (what.equals("up")) {
				feedItems.addAll(0, feedItemTemp);
			} else {
				feedItems.addAll(feedItemTemp);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
			bar.setVisibility(View.GONE);
			isRel = false;
			isbut = false;
			butAgain.setText("");
			butAgain.setClickable(false);

		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (what.equals("up")) {
				listy.onRefreshComplete();
			} else if (what.equals("down")) {
				downRefreshing = false;
				if (tv != null && actualListView != null) {
					actualListView.removeFooterView(tv);
				}

			}
		}
		return what;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ib_overflow) {
			//

			PopupMenuCompat menu = PopupMenuCompat.newInstance(MainListy.this,
					v);

			if (isLogged) {
				menu.inflate(R.menu.main_popup_logged);
				menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// Toast.makeText(MainListy.this, item.getItemId(),
						// Toast.LENGTH_SHORT).show();
						if (item.getItemId() == R.id.lrow_2) {
							LogOutDiaglog logOut = new LogOutDiaglog();
							if (fm == null) {
								fm = getSupportFragmentManager();
							}
							logOut.show(fm, "loggedoutt");

						} else if (item.getItemId() == R.id.lrow_3) {
							// NotificationPrompt promptFragment = new
							// NotificationPrompt();
							// promptFragment.show(fm, "notifyPrompts");
							// SimpleDateFormat sdf = new SimpleDateFormat(
							// "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
							// Date mDate = null;
							// Date mDate2 = null;
							// try {
							// mDate = sdf.parse("2015-01-01 19:45:00");
							// mDate2 = sdf.parse("2015-01-01 19:45:01");
							// } catch (ParseException e) {
							// e.printStackTrace();
							// }
							// long timeInMilliseconds = mDate.getTime();
							// Message.message(MainListy.this,
							// (mDate2.getTime() - mDate.getTime())
							// + " kl");

							// Delete deleteAllComments = new Delete(1 + "",
							// 2 + "", true, 1, 1);
							// deleteAllComments.setCancelable(false);
							// deleteAllComments.show(fm, "deleteAllCOmments");
						}

						return true;
					}
				});
			} else {

				menu.inflate(R.menu.main_popup);
				menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// Toast.makeText(MainListy.this, item.getItemId(),
						// Toast.LENGTH_SHORT).show();
						if (item.getItemId() == R.id.row_1) {
							Intent i = new Intent(MainListy.this,
									LogInActivity.class);
							startActivity(i);
						} else if (item.getItemId() == R.id.row_2) {
							Intent i = new Intent(MainListy.this, SignUps.class);
							startActivity(i);
						} else if (item.getItemId() == R.id.rowSettings) {
							Message.message(MainListy.this, " kl");
							//
						}

						return true;
					}
				});
			}

			menu.show();
		} else if (v.getId() == R.id.butAgainTry && isbut) {
			// butAgain.setVisibility(View.GONE);
			butAgain.setText("");
			butAgain.setClickable(false);
			pb.setVisibility(View.VISIBLE);
			isbut = false;

			String params[] = { "all", adapterMy.getUserID() };

			new NetCheck().execute(params);
		} else if (v.getId() == R.id.ib_viewprofile) {
			Intent i = new Intent(MainListy.this, Tabs_Profile.class);
			startActivityForResult(i, 2);
		} else if (v.getId() == R.id.ib_addnewpost) {
			New_Notify newNoti = new New_Notify();
			newNoti.show(fm, "newwwwNotiss");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				ArrayList<String> unbookList = data
						.getStringArrayListExtra("ubookList");
				ArrayList<String> deleteList = data
						.getStringArrayListExtra("deletesList");
				ArrayList<String> unfollowList = data
						.getStringArrayListExtra("unffollowList");

				if (deleteList.size() != 0) {
					deletePosts(deleteList);
				}
				if (unfollowList.size() != 0) {
					unFollowPosts(unfollowList);
				}
				if (unbookList.size() != 0) {
					removeBookmark(unbookList);
				}

				listAdapter.notifyDataSetChanged();

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	private void deletePosts(ArrayList<String> unbookList) {
		int sizeFeed = feedItems.size();
		for (String pid : unbookList) {
			for (int i = 0; i < sizeFeed; i++) {
				if (pid.equals(feedItems.get(i).id + "")) {
					feedItems.remove(i);
					break;
				}
			}
		}
	}

	private void unFollowPosts(ArrayList<String> unFollowList) {
		int sizeFeed = feedItems.size();
		for (String ufid : unFollowList) {
			for (int i = 0; i < sizeFeed; i++) {
				if (ufid.equals(feedItems.get(i).user_id + "")) {
					feedItems.get(i).isFollowed = false;
					break;
				}
			}
		}
	}

	private void removeBookmark(ArrayList<String> unbookList) {
		int sizeFeed = feedItems.size();
		for (String pid : unbookList) {
			for (int i = 0; i < sizeFeed; i++) {
				if (pid.equals(feedItems.get(i).id + "")) {
					feedItems.get(i).isBookmarked = false;
					break;
				}
			}
		}
	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;
		String urll = UserFunctions.URL_FEED;
		String dec = "";

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

			ConnectivityManager cm = (ConnectivityManager) MainListy.this
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
						if (args[0].equals("up")) {
							urll = urll + "?" + args[0] + "="
									+ feedItems.get(0).id + "&id=" + args[1];
						} else if (args[0].equals("down")) {
							urll = urll + "?" + args[0] + "="
									+ feedItems.get(feedItems.size() - 1).id
									+ "&id=" + args[1];
						} else {
							urll = UserFunctions.URL_FEED + "?id=" + args[1];
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

				Crouton.makeText(MainListy.this, "Inside Connection",
						Style.INFO).show();

			} else {

				Message.message(MainListy.this, "Error in Network Connection");
				// We first check for cached request
				Cache cache = AppController.getInstance().getRequestQueue()
						.getCache();
				Entry entry = cache.get(UserFunctions.URL_FEED + "?id="
						+ adapterMy.getUserID());
				if (entry != null && feedItems.size() == 0) {
					Message.message(MainListy.this, "entered ljkhj");
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
						// if (tv != null && actualListView != null) {
						// actualListView.removeFooterView(tv);
						// }
					}

				} else {
					listy.onRefreshComplete();
					pb.setVisibility(View.GONE);
					// butAgain.setVisibility(View.VISIBLE);
					butAgain.setText("Try Again");
					butAgain.setClickable(true);
					isbut = true;
					downRefreshing = false;
					if (tv != null && actualListView != null) {
						actualListView.removeFooterView(tv);
					}
				}

			}

		}

	}

	@Override
	public void refreshFromBookmark(int position, String pid) {
		// listAdapter.changeBookmarkValue(0);
		if (feedItems.get(position).isBookmarked) {
			feedItems.get(position).isBookmarked = false;
			deleteReminder((int) feedItems.get(position).id);
		} else {
			feedItems.get(position).isBookmarked = true;
			promptReminder(feedItems.get(position));
		}

		listAdapter.notifyDataSetChanged();

	}

	public void deleteReminder(int id) {
		adapterMy.deleteAlarm(id);
		AlarmManagerHelper.setAlarms(this);
	}

	public void promptReminder(FeedItem item) {
		String dateTime[] = item.date.split(" ");

		if (dateTime[0].equals("0000-00-00") || dateTime[1].equals("00:00:00")) {
			Message.message(MainListy.this, "no date");
		} else {

			if (Utils.isPassed(item.date)) {
				Message.message(MainListy.this, "Already Passed");
			} else {
				Message.message(MainListy.this, "set bookmark here");
				NotificationPrompt promptFragment = new NotificationPrompt(
						new NotiItem((int) item.id, item.title, item.desc,
								item.date, item.posted_date, item.username,
								item.user_id + "", item.lat, item.longi,
								item.no_comm, 0));
				promptFragment.show(fm, "notifyPrompts");
			}
		}

	}

	public void runGCM() {
		if (TextUtils.isEmpty(regId)) {
			regId = registerGCM();
			Log.d("RegisterActivity", "GCM RegId: " + regId);
		} else {
			Toast.makeText(getApplicationContext(),
					"Already Registered with GCM Server!", Toast.LENGTH_LONG)
					.show();

			mRegisterTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					// Register on our server
					// On server creates a new user
					ServerUtilities.register(MainListy.this, "bijz", "maha",
							regId);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					mRegisterTask = null;
				}

			};
			mRegisterTask.execute(null, null, null);

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Signing.r == 12) {
			String uname = Signing.vallus;
			String email = Signing.email;
			AlertDialog diag = new AlertDialog();

			diag.setStyle("succ", uname, email);
			diag.show(fm, "blocked");
			Signing.r = 0;
			Signing.vallus = "";
			Signing.email = "";
		}

		// startService(intent);
		registerReceiver(mHandleMessageReceivers, new IntentFilter(
				UserFunctions.UPDATE_UI_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mHandleMessageReceivers);
		// stopService(intent);
	}

	private final BroadcastReceiver mHandleMessageReceivers = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					UserFunctions.EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			Toast.makeText(getApplicationContext(),
					"New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	AsyncTask<Void, Void, Void> mRegisterTask;
	GoogleCloudMessaging gcm;
	String regId;

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(UserFunctions.SENDER_ID);

					Log.d("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(getApplicationContext(),
						"Registered with GCM Server." + msg, Toast.LENGTH_LONG)
						.show();
			}
		}.execute(null, null, null);
	}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			Toast.makeText(getApplicationContext(),
					"RegId already available. RegId: " + regId,
					Toast.LENGTH_LONG).show();

		}
		return regId;
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				LogInActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(UserFunctions.REG_ID, regId);
		editor.putInt(UserFunctions.APP_VER, appVersion);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				LogInActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(UserFunctions.REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(UserFunctions.APP_VER,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	public void insideSettings() {

		// GCMRegistrar.checkDevice(MainListy.this);
		//
		// // Make sure the manifest was properly set - comment
		// // out this line
		// // while developing the app, then uncomment it when
		// // it's ready.
		// GCMRegistrar.checkManifest(MainListy.this);
		// //
		// //
		// registerReceiver(
		// mHandleMessageReceiver,
		// new IntentFilter(
		// UserFunctions.DISPLAY_MESSAGE_ACTION));
		// //
		// final String regId = GCMRegistrar
		// .getRegistrationId(MainListy.this);
		// Message.message(MainListy.this, regId + " dfsdfds");
		//
		// // Check if regid already presents
		// if (regId.equals("")) {
		// // Registration is not present, register now
		// // with GCM
		// GCMRegistrar.register(MainListy.this,
		// UserFunctions.SENDER_ID);
		//
		// // return;
		//
		// } else {
		// // Device is already registered on GCM
		// if (GCMRegistrar
		// .isRegisteredOnServer(MainListy.this)) {
		// // Skips registration.
		//
		// Toast.makeText(
		// getApplicationContext(),
		// "Already registered with GCM with "
		// + GCMRegistrar
		// .getRegistrationId(MainListy.this),
		// Toast.LENGTH_LONG).show();
		// } else {
		// // Try to register again, but not in the UI
		// // thread.
		// // It's also necessary to cancel the thread
		// // onDestroy(),
		// // hence the use of AsyncTask instead of a
		// // raw thread.
		// final Context context = MainListy.this;
		// mRegisterTask = new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(
		// Void... params) {
		// // Register on our server
		// // On server creates a new user
		// ServerUtilities.register(context,
		// "bai", "sdsd", regId);
		// return null;
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// mRegisterTask = null;
		// }
		//
		// };
		// mRegisterTask.execute(null, null, null);
		// }
		// }
		// runGCM();
		//

	}

	@Override
	public void update_comment(int pos, int no_comm) {
		// TODO Auto-generated method stub
		feedItems.get(pos).no_comm = no_comm;
		listAdapter.notifyDataSetChanged();

	}

	@Override
	public void refreshFromBlock(int postion) {
		feedItems.remove(postion);
		listAdapter.notifyDataSetChanged();

	}

	@Override
	public void rfshjustAdded(FeedItem item) {
		feedItems.add(0, item);
		listAdapter.notifyDataSetChanged();

	}

	@Override
	public void refreshFromFollow(int position, boolean follow, String fuid) {
		Message.message(this, position + " this is the");
		String postuid = feedItems.get(position).user_id + "";
		for (int i = 0; i < feedItems.size(); i++) {
			if (postuid.equals(feedItems.get(i).user_id + "")) {
				feedItems.get(i).isFollowed = follow;

			}

		}

		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void deleteSingleComment(String comment_id, int position) {

	}

	@Override
	public void logOut() {

		AlarmManagerHelper.cancelAlarms(MainListy.this);
		GCMRegistrar.setRegisteredOnServer(MainListy.this, false);
		// unregisterReceiver();
		Intent i = new Intent(MainListy.this, LogInActivity.class);
		startActivity(i);
		MainListy.this.finish();
	}
}
