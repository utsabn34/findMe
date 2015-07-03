package com.notify.app.adapters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.dialogs.Delete;
import com.notify.app.signups.SignUps;

//
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.telephony.TelephonyManager;
//import android.widget.TextView;
//
@TargetApi(Build.VERSION_CODES.KITKAT)
public class UserFunctions {
	Context con;
	private JSONParser jsonParser;
	// String numpos, title, code;
	// String fac, sec, yearpart, year, college;
	// ArrayList<Artists> ret_artists = new ArrayList<Artists>();

	//
	public static String nsc_tag = "nnscc";
	public static String fileName = "nnscc";
	public static String fileNoti = "notii";
	static String ip = "192.168.56.1";
	// public static String Comment_FEED =
	// "http://"+ip+"/notified/gettin_comment.php";
	// public static String B_FEED
	// ="http://"+ip+"/notified/gettin_bookmark.php";
	// public static String URL_FEED = "http://"+ip+"/notified/gettin.php";
	// public static String loginURL = "http://"+ip+"/notified/all_con.php";
	// public static String chkConnURL = "http://"+ip+"/notified/all_con.php";
	// public static String picUrl = "http://"+ip+"//notified/images/";
	// public static final String SERVER_URL =
	// "http://"+ip+"/notified/register.php";

	// public static String URL_FEED = "http://10.0.2.2/notified/gettin.php";
	// public static String loginURL = "http://10.0.2.2/notified/all_con.php";
	// public static String chkConnURL = "http://10.0.2.2/notified/all_con.php";
	// public static String picUrl =
	// "http://10.0.2.2/notified/images/";
	// public static String Comment_FEED =
	// "http://10.0.2.2/notified/gettin_comment.php";
	// public static String B_FEED =
	// "http://10.0.2.2/notified/gettin_bookmark.php";
	// public static final String SERVER_URL =
	// "http://10.0.2.2/notified/register.php";

	public static String chkConnURL = "http://www.sanjibmaharjan.com.np";
	public static String URL_FEED = "http://www.sanjibmaharjan.com.np/notifies/gettin.php";
	public static String loginURL = "http://www.sanjibmaharjan.com.np/notifies/all_con.php";
	public static String picUrl = "http://www.sanjibmaharjan.com.np/notifies/images/";
	public static String Comment_FEED = "http://www.sanjibmaharjan.com.np/notifies/gettin_comment.php";
	public static String B_FEED = "http://www.sanjibmaharjan.com.np/notifies/gettin_bookmark.php";
	public static String MY_POSTS_FEED = "http://www.sanjibmaharjan.com.np/notifies/gettin_myposts.php";
	public static String FUser_FEED = "http://www.sanjibmaharjan.com.np/notifies/gettin_fusers.php";
	public static final String SERVER_URL = "http://www.sanjibmaharjan.com.np/notifies/register.php";

	// private static String loginURL = "http://10.0.2.2/notified/all_con.php";
	private static String login_tag = "login";
	public static String conn_chk_string = "Checking connection...";
	private static String register_tag = "register";
	private static String book_tag = "bookmark";
	private static final String LOG_OUT_TAG = "logoutt";
	private static String follow_tag = "follow";
	private static String deletepost_tag = "delete_post";
	private static String deleteallcomments_tag = "delete_all_comment";
	private static String block_tag = "blocking";
	private static String add_tag = "post_notify";
	private static String report_tag = "report_notify";
	private static String MODIFY_TAG = "modify_userinfo";
	private static String forpass_tag = "forpass";
	private static String chgpass_tag = "chgpass";
	private static String changePics_tag = "changePics_tag";
	public static String KEY_SUCCESS = "success";
	public static String KEY_ERROR = "error";
	FragmentManager manager;

	//
	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	public UserFunctions(Context con) {
		jsonParser = new JSONParser();
		this.con = con;
	}

	public JSONObject loginUser(String uname, String password, String id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("uname", uname));
		params.add(new BasicNameValuePair("gcm_id", id));
		params.add(new BasicNameValuePair("upass", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject changeProfileInfo(String fname, String lname,
			String city, String country, String dob, String uid) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", MODIFY_TAG));
		params.add(new BasicNameValuePair("fname", fname));
		params.add(new BasicNameValuePair("lname", lname));
		params.add(new BasicNameValuePair("dob", dob));
		params.add(new BasicNameValuePair("city", city));
		params.add(new BasicNameValuePair("country", country));
		params.add(new BasicNameValuePair("uid", uid));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject processBookmark(String post_id, String user_id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", book_tag));
		params.add(new BasicNameValuePair("uid", user_id));
		params.add(new BasicNameValuePair("pid", post_id));
		// params.add(new BasicNameValuePair("booked", booked+""));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject processLogout(String device_id, String user_id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", LOG_OUT_TAG));
		params.add(new BasicNameValuePair("uid", user_id));
		params.add(new BasicNameValuePair("did", device_id));
		// params.add(new BasicNameValuePair("booked", booked+""));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject processDelete(String post_id, String user_id, int type) {
		// Building Parameters

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (type == Delete.TYPE_DELETE_POST) {
			params.add(new BasicNameValuePair("tag", deletepost_tag));
		} else if (type == Delete.TYPE_DELETE_ALL_COMMENTS) {
			params.add(new BasicNameValuePair("tag", deleteallcomments_tag));
		}

		params.add(new BasicNameValuePair("uid", user_id));
		params.add(new BasicNameValuePair("pid", post_id));
		// params.add(new BasicNameValuePair("booked", booked+""));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject processFollow(String upid, String user_id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", follow_tag));
		params.add(new BasicNameValuePair("uid", user_id));
		params.add(new BasicNameValuePair("upid", upid));
		// params.add(new BasicNameValuePair("booked", booked+""));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject processBlock(String post_id, String user_id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", block_tag));
		params.add(new BasicNameValuePair("uid", user_id));
		params.add(new BasicNameValuePair("pid", post_id));
		// params.add(new BasicNameValuePair("booked", booked+""));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject postComment(String post_id, String user_id, String comment) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "postcomm"));
		params.add(new BasicNameValuePair("pid", post_id));
		params.add(new BasicNameValuePair("uid", user_id));
		params.add(new BasicNameValuePair("comment", comment));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject deleteSingleComment(String comment_id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "dscomm"));
		params.add(new BasicNameValuePair("cid", comment_id));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject AddNewNotify(FeedItem info) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", add_tag));
		params.add(new BasicNameValuePair("u_id", info.user_id + ""));
		params.add(new BasicNameValuePair("title", info.title));
		params.add(new BasicNameValuePair("desc", info.desc));
		params.add(new BasicNameValuePair("date", info.date));
		params.add(new BasicNameValuePair("lat", info.lat));
		params.add(new BasicNameValuePair("longi", info.longi));

		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject ReportPost(long post_id, String uid, String rptmsg) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", report_tag));
		params.add(new BasicNameValuePair("u_id", uid));
		params.add(new BasicNameValuePair("p_id", post_id + ""));
		params.add(new BasicNameValuePair("reportmsg", rptmsg));

		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject SignUpUser(LogInfo info) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("uname", info.uname
				.toLowerCase(Locale.ENGLISH)));
		params.add(new BasicNameValuePair("upass", info.upass));
		params.add(new BasicNameValuePair("email", info.email
				.toLowerCase(Locale.ENGLISH)));
		params.add(new BasicNameValuePair("fname", info.fname));
		params.add(new BasicNameValuePair("lname", info.lname));
		params.add(new BasicNameValuePair("country", info.country));
		params.add(new BasicNameValuePair("dob", info.dob));
		params.add(new BasicNameValuePair("city", info.city));

		if (SignUps.bitmap2 != null) {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			// Here you can define .PNG as well
			SignUps.bitmap2.compress(Bitmap.CompressFormat.JPEG, 95, bao);
			byte[] ba = bao.toByteArray();

			String ba1 = Base64.encodeToString(ba, 0);
			params.add(new BasicNameValuePair("image", ba1));
		} else {
			params.add(new BasicNameValuePair("image", "none"));
		}

		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	public JSONObject processChangePics(String userId, Bitmap bitmap) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", changePics_tag));
		params.add(new BasicNameValuePair("uid", userId));

		if (bitmap != null) {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			// Here you can define .PNG as well
			bitmap.compress(Bitmap.CompressFormat.JPEG, 95, bao);
			byte[] ba = bao.toByteArray();

			String ba1 = Base64.encodeToString(ba, 0);
			params.add(new BasicNameValuePair("image", ba1));
		} else {
			params.add(new BasicNameValuePair("image", "none"));
		}

		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		return json;
	}

	//
	// /**
	// * function make Login Request
	// *
	// * @param email
	// * @param password
	// * *
	// */
	//
	// public void REcentChords(FragmentManager manager, String[] params) {
	//
	// this.manager = manager;
	// this.params = params;
	//
	// new GETChords_OnlineforRecent().execute(params);
	//
	// // this.manager = manager;
	// // return ret_artists;
	// }
	//

	public boolean logoutUser(Context context) {
		MySQLAdapter db = new MySQLAdapter(context);
		db.logOut();
		return true;
	}

	private void executeReq(URL urlObject) throws IOException {

		// used as
		// try {
		// URL url = new URL("http://stackoverflow.com/posts/11642475/edit" );
		// //URL url = new URL("http://www.nofoundwebsite.com/" );
		// executeReq(url);
		// Toast.makeText(getApplicationContext(), "Webpage is available!",
		// Toast.LENGTH_SHORT).show();
		// }
		// catch(Exception e) {
		// Toast.makeText(getApplicationContext(),
		// "oops! webpage is not available!", Toast.LENGTH_SHORT).show();
		// }

		HttpURLConnection conn = null;
		conn = (HttpURLConnection) urlObject.openConnection();
		conn.setReadTimeout(30000);// milliseconds
		conn.setConnectTimeout(3500);// milliseconds
		conn.setRequestMethod("GET");
		conn.setDoInput(true);

		// Start connect
		conn.connect();
		InputStream response = conn.getInputStream();
		Log.d("Response:", response.toString());
	}

	public static void isNetworkAvailable(final Handler handler,
			final int timeout) {
		// ask fo message '0' (not connected) or '1' (connected) on 'handler'
		// the answer must be send before before within the 'timeout' (in
		// milliseconds)
		// used as

		// Handler h = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		//
		// if (msg.what != 1) { // code if not connected
		//
		// } else { // code if connected
		//
		// }
		// }
		// };
		//
		// ...and launch the test:

		// isNetworkAvailable(h,2000);

		new Thread() {
			private boolean responded = false;

			@Override
			public void run() {
				// set 'responded' to TRUE if is able to connect with google
				// mobile (responds fast)
				new Thread() {
					@Override
					public void run() {
						HttpGet requestForTest = new HttpGet(
								"http://m.google.com");
						try {
							new DefaultHttpClient().execute(requestForTest); // can
																				// last...
							responded = true;
						} catch (Exception e) {
						}
					}
				}.start();

				try {
					int waited = 0;
					while (!responded && (waited < timeout)) {
						sleep(100);
						if (!responded) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
				} // do nothing
				finally {
					if (!responded) {
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				}
			}
		}.start();
	}

	public static boolean isOnline() {
		try {
			InetAddress.getByName("google.com").isReachable(3);

			return true;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	private boolean isOnTheInternet() {
		try {
			URLConnection urlConnection = new URL("http://yourserver")
					.openConnection();
			urlConnection.setConnectTimeout(400);
			urlConnection.connect();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean isOnline_() {
		try {
			Process p1 = java.lang.Runtime.getRuntime().exec(
					"ping -c 1 www.google.com");
			int returnVal = p1.waitFor();
			boolean reachable = (returnVal == 0);
			return reachable;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// Google project id
	public static final String SENDER_ID = "455543702030";

	public static final String REG_ID = "gcmid";
	public static final String APP_VER = "appver";

	/**
	 * Tag used on log messages.
	 */
	public static final String TAG = "Sanjib GCM";

	public static final String DISPLAY_MESSAGE_ACTION = "com.zindex.notify.DISPLAY_MESSAGE";
	public static final String UPDATE_UI_ACTION = "com.notify.app.UPDATE_UI";

	public static final String EXTRA_MESSAGE = "message";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 *
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	public static void updateUIContent(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static String get_RelativeTime(long highdate, long lowdate) {
		long sec = highdate - lowdate;
		if (sec <= 30000)
			return "Just Now";
		if (sec > 30000 && sec < 60000)
			return sec + " sec ago";
		if (sec >= 60000 && sec < 3600000) {
			int min = (int) (sec / 60000);
			return min + " minutes ago";
		}
		if (sec >= 3600000 && sec < 86400000) {
			int hour = (int) (sec / (60000 * 60));

			if (hour == 1)
				return hour + " hour ago";
			else
				return hour + " hours ago";
		}
		if (sec >= 86400000 && sec < 864000000) {
			int days = (int) (sec / (60000 * 60 * 24));
			if (days == 1)
				return days + " day ago";
			else
				return days + " days ago";
		} else {
			return "what what";
		}
	}

	public static String formatTime(String time) {
		String tt[] = time.split("-");
		String posts = "";
		if (tt[2].equals("01") || tt[2].equals("1")) {
			posts = "st";
		} else if (tt[2].equals("02") || tt[2].equals("2")) {
			posts = "nd";
		} else if (tt[2].equals("03") || tt[2].equals("3")) {
			posts = "rd";
		} else {
			posts = "th";
		}
		return (tt[2].charAt(0) == '0' ? tt[2].charAt(1) + "" : tt[2]) + posts
				+ " " + month[Integer.parseInt(tt[1]) - 1] + " " + tt[0];

	}

	static String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sept", "Oct", "Nov", "Dec" };

	public void hjhj() {
		InputMethodManager imm = (InputMethodManager) con
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
	}

	public static int getActionBarHeight(Context context) {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize,
				tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					context.getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static void setStatusBarColor(View statusBar, int color,
			Window window, Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// status bar height
			
			int actionBarHeight = getActionBarHeight(context);
			int statusBarHeight = getStatusBarHeight(context);
			// action bar height
			statusBar.getLayoutParams().height = statusBarHeight;
			statusBar.setBackgroundColor(color);
		}
	}
}
