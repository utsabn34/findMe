package com.notify.app.dialogs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.notify.app.R;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Classes.FeedItem;

public class Blocking extends DialogFragment implements OnClickListener {

	public Blocking(String pid, String realId, boolean mark, int position) {
		this.pid = pid;
		this.uid = realId;
		this.mark = mark;
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);

			getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
		}
		if (savedInstanceState == null) {

		}
		View root = inflater.inflate(R.layout.blocking, container, false);

		initializers(root);
		NetAsync();
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
		if (arg0.getId() == R.id.blocking_close) {
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
			tv_messgs.setText(con.getResources().getString(
					R.string.chek_conn_message));

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
				new ProcessBlocking().execute();
			} else {
				getDialog().dismiss();

				Message.message(
						con,
						con.getResources().getString(
								R.string.error_conn_message));

			}
		}

	}

	private class ProcessBlocking extends
			AsyncTask<String, JSONObject, JSONObject> {
		// private ProgressDialog pDialog;
		String uname, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (mark) {
				tv_messgs.setText("hiding from the notify...");
			} else {
				tv_messgs.setText("showing in notify...");
			}

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.processBlock(pid, uid);
			// JSONObject json_user = null;
//			 Log.d("LOGGGG", json.toString());
			// try {
			// if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
			// String res = json.getString(UserFunctions.KEY_SUCCESS);
			Log.d("LOGGGG", json.toString());

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

						if (json.getString("action").equals("b")) {
							Message.message(con, "Hidden from notify");
						} else {
							Message.message(con, "Shown to the notify");
						}

					} else {
						Message.message(con, "Not Successful");
					}
				} else {
					Message.message(con, "Something went wrong");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				trigger.refreshFromBlock(position);
				getDialog().dismiss();
			}

		}

	}

	public void initializers(View root) {
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();
		mySQLAdapter = new MySQLAdapter(con);

		tv_messgs = (TextView) root.findViewById(R.id.tvBlockText);
		viewClose = root.findViewById(R.id.blocking_close);

		viewClose.setOnClickListener(this);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		trigger = (BlockTrig) activity;
	}

	public interface BlockTrig {
		public void refreshFromBlock(int postion);
	}

	BlockTrig trigger;
	String pid = "0", uid = "0";
	Context con;
	FragmentManager manager;
	Button butClose;
	TextView tv_messgs;
	FeedItem item = new FeedItem(0, 0, "", "", "", "", "", "", "", 0, false,false);
	View viewClose;
	MySQLAdapter mySQLAdapter;
	boolean mark = false;
	int position = 0;

}
