package com.notify.app.dialogs;
import com.notify.app.R;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Classes.FeedItem;

public class DummyDialog extends DialogFragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);

			getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		}
		if (savedInstanceState == null) {

		}
		View root = inflater.inflate(R.layout.bookmarking, container, false);

		initializers(root);

		return root;
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
		if (arg0.getId() == R.id.bookmarking_close) {
			getDialog().dismiss();

		}
	}

	public void NetAsync() {
		new NetCheck().execute();
	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// nDialog = new ProgressDialog(MainActivity.this);
			// textLoads.setText(con.getResources().getString(
			// R.string.chek_conn_message));

		}

		@Override
		protected Boolean doInBackground(String... args) {
			/**
			 * Gets current device state and checks for working internet
			 * connection by trying Google.
			 **/
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
				// tvMessage.setText("");
				new ProcessPost().execute();
			} else {
				// textLoads.setText("");
				// textLoads.setVisibility(View.GONE);
				// viewScroll.setVisibility(View.VISIBLE);

				Message.message(
						con,
						con.getResources().getString(
								R.string.error_conn_message));

			}
		}

	}

	private class ProcessPost extends AsyncTask<String, JSONObject, JSONObject> {
		// private ProgressDialog pDialog;
		String uname, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// textLoads.setText("Posting in....");
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();

			FeedItem items = new FeedItem(0, Long.parseLong(mySQLAdapter
					.getUser().id), "", "", "", "", "", "", "", 0, false,false);
			JSONObject json = userFunction.AddNewNotify(items);
			// JSONObject json_user = null;
			// Log.d("LOGGGG", json.toString()+info.email);
			// try {
			// if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
			// String res = json.getString(UserFunctions.KEY_SUCCESS);
			Log.d("LOGGGG", json.toString());
			//
			// if (Integer.parseInt(res) == 1) {
			// // pDialog.setMessage("Loading User Space");
			// // pDialog.setTitle("Getting Data");
			// json_user = json.getJSONObject("user");
			// /**
			// * Clear all previous data in SQlite database.
			// **/
			//
			// }
			// }
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }

			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
					// tvMessage.setText("Registering...");
					String res = json.getString(UserFunctions.KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// registerErrorMsg.setText("Successfully Registered");

						JSONArray json_added = json.getJSONArray("feeds");
						JSONObject c = json_added.getJSONObject(0);
						// int _id = c.getInt("_id");
						// String title = c.getString("title");
						// String isTab = c.getString("isTabs");

						Message.message(con,
								"Your Post is added..Refresh the list to verify");
						getDialog().dismiss();
						// r = 12;
						// vallus = json_user.getString("uname");
						// email = json_user.getString("email");
						// dialog.dismiss();
						// getActivity().finish();
					} else {
						// dialog.dismiss();
						// AlertDialog diag = new AlertDialog();
						// diag.setValue("Email address already taken");
						// diag.show(manager, "adsasEmail");
					}
				} else {
					// dialog.dismiss();
					AlertDialog diag = new AlertDialog();
					diag.setValue("Error Occured in Registration.");
					diag.show(manager, "adsas");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	public void initializers(View root) {
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();
		mySQLAdapter = new MySQLAdapter(con);

		tv_messgs = (TextView) root.findViewById(R.id.tvBookText);
		viewClose = root.findViewById(R.id.bookmarking_close);

		viewClose.setOnClickListener(this);

	}

	Context con;
	FragmentManager manager;
	Button butClose;
	TextView tv_messgs;

	View viewClose;
	MySQLAdapter mySQLAdapter;

}
