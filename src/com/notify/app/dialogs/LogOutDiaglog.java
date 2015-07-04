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
import android.content.SharedPreferences;
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

public class LogOutDiaglog extends DialogFragment implements OnClickListener {

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
		arg0.putBoolean("isLoggngout", isLoggingOut);
	}

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

		} else {
			isLoggingOut = savedInstanceState.getBoolean("isLoggngout");

		}

		View root = inflater.inflate(R.layout.log_out, container, false);
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();

		mySQLAdapter = new MySQLAdapter(con);
		// Message.message(con, mySQLAdapter.getUser().id + "");
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
		if (arg0.getId() == R.id.butCancelLog) {
			if (!isLoggingOut) {
				getDialog().dismiss();
			}

		} else if (arg0.getId() == R.id.butLogout) {
			if (!isLoggingOut) {
				NetAsync();
			}

		}
	}

	public void NetAsync() {
		new NetCheck().execute();
	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvLogoutMess.setText(UserFunctions.conn_chk_string);
			isLoggingOut = true;
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
				Log.d("nhjk", "connected");
				mySQLAdapter = new MySQLAdapter(con);
				prefs = con.getSharedPreferences(UserFunctions.fileName,
						Context.MODE_PRIVATE);
				String[] params = new String[]{prefs.getString(
						UserFunctions.REG_ID,""), mySQLAdapter.getUserID()};
				new ProcessLogOut().execute(params);
			} else {
				isLoggingOut = false;
				tvLogoutMess.setText("No connection detected..Try again");
				Message.message(
						con,
						con.getResources().getString(
								R.string.error_conn_message));

			}
		}

	}

	private class ProcessLogOut extends AsyncTask<String, Void, Boolean> {
		// private ProgressDialog pDialog;
		String uname, password;
		boolean success = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			tvLogoutMess.setText(logMess);
		}

		@Override
		protected Boolean doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			Log.d("LOGGGG", args[0]+"  "+args[1]);
			JSONObject json = userFunction.processLogout(args[0], args[1]);
			Log.d("LOGGGG", json.toString());
			try {
				if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
					String res = json.getString(UserFunctions.KEY_SUCCESS);
					Log.d("LOGGGG", json.toString());

					if (Integer.parseInt(res) == 1) {
						success = true;

					} else {
						success = false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return success;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				if (mySQLAdapter == null) {
					mySQLAdapter = new MySQLAdapter(con);
				}
				mySQLAdapter.logOut();
				mySQLAdapter.deleteallAlarms();
				if(prefs == null){
					prefs = con.getSharedPreferences(UserFunctions.fileName,
							Context.MODE_PRIVATE);
				}
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(UserFunctions.fileNoti, "");
				editor.commit();
				
				Message.message(con, "Logging out successful.");
				getDialog().dismiss();
				logOut.logOut();
			} else {
				isLoggingOut = false;
				tvLogoutMess.setText("Something went wrong.Please try Again.");
				Message.message(con, "Oops! Something went wrong.");
			}

		}

		

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		logOut = (LogOut) activity;
	}

	public void initializers(View root) {
		butCancel = root.findViewById(R.id.butCancelLog);
		butLogout = (Button) root.findViewById(R.id.butLogout);
		tvLogoutMess = (TextView) root.findViewById(R.id.tvLogoutMess);

		butCancel.setOnClickListener(this);
		butLogout.setOnClickListener(this);
	}

	public interface LogOut {
		public void logOut();
	}

	SharedPreferences prefs;
	boolean isLoggingOut = false;
	FragmentManager manager;
	View butCancel;
	LogOut logOut;
	Button butLogout;
	private final String logOutMess = "Are you sure you want to log out?";
	private final String logMess = "logging out...please wait";
	TextView tvLogoutMess;
	MySQLAdapter mySQLAdapter;
	Context con;

}
