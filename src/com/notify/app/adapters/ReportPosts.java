package com.notify.app.adapters;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.notify.app.R;
import com.notify.app.signups.Form;
import com.notify.app.signups.RangeValidator;
import com.notify.app.signups.Validate;

public class ReportPosts extends DialogFragment implements OnClickListener {

	public ReportPosts(long post_id, String postTitle) {
		this.postTitle = postTitle;
		this.post_id = post_id;

	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
		arg0.putString("postTTitle", postTitle);
		arg0.putLong("postIId", post_id);
		arg0.putBoolean("reportingidd", isReporting);
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
			postTitle = savedInstanceState.getString("postTTitle");
			post_id = savedInstanceState.getInt("postIId");
			isReporting = savedInstanceState.getBoolean("reportingidd");
		}
		View root = inflater.inflate(R.layout.report_post, container, false);

		initializers(root);
		if (isReporting) {
			viewScroll.setVisibility(View.GONE);
			tv_messgs.setVisibility(View.VISIBLE);
		} else {
			viewScroll.setVisibility(View.VISIBLE);
			tv_messgs.setVisibility(View.GONE);
		}

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
		if (arg0.getId() == R.id.cancel_report) {
			getDialog().dismiss();

		}else if (arg0.getId() == R.id.reportNotify) {
			RangeValidator range_validator = new RangeValidator(con, 3, 1000,
					R.string.validator_range2);

			Validate et_msg = new Validate(et_report);
			et_msg.addValidator(range_validator);

			Form mForm = new Form();
			mForm.addValidates(et_msg);

			if (mForm.validate()) {
				
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
			// nDialog = new ProgressDialog(MainActivity.this);
			tv_messgs.setVisibility(View.VISIBLE);
			viewScroll.setVisibility(View.GONE);
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
				new ProcessReport().execute();
			} else {
				tv_messgs.setText("");
				tv_messgs.setVisibility(View.GONE);
				viewScroll.setVisibility(View.VISIBLE);

				Message.message(
						con,
						con.getResources().getString(
								R.string.error_conn_message));

			}
		}

	}

	private class ProcessReport extends
			AsyncTask<String, JSONObject, JSONObject> {
		// private ProgressDialog pDialog;
		String uname, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			tv_messgs.setText("processing request....");
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.ReportPost(post_id,
					mySQLAdapter.getUserID(), et_report.getText().toString()
							.trim());
			// JSONObject json_user = null;
			// Log.d("LOGGGG", json.toString()+info.email);
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

//						JSONArray json_added = json.getJSONArray("feeds");
//						JSONObject c = json_added.getJSONObject(0);
						// int _id = c.getInt("_id");
						// String title = c.getString("title");
						// String isTab = c.getString("isTabs");

						Message.message(con,
								"Succesfully Reported");
						getDialog().dismiss();
						// r = 12;
						// vallus = json_user.getString("uname");
						// email = json_user.getString("email");
						// dialog.dismiss();
						// getActivity().finish();
					} else {
						Message.message(con,
								"Error! Try Again.");
						
						tv_messgs.setText("");
						tv_messgs.setVisibility(View.GONE);
						viewScroll.setVisibility(View.VISIBLE);
						
					}
				} else {

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

		viewClose = root.findViewById(R.id.cancel_report);
		butClose = (Button) root.findViewById(R.id.reportNotify);
		tv_pstdesc = (TextView) root.findViewById(R.id.tv_pstdesc);
		tv_messgs = (TextView) root.findViewById(R.id.textloadd_rep);
		et_report = (EditText) root.findViewById(R.id.etDett_report);
		viewScroll = (ScrollView) root.findViewById(R.id.scroool_report);
		tv_pstdesc.setText("Post: " + postTitle);

		viewClose.setOnClickListener(this);
		butClose.setOnClickListener(this);

	}

	Context con;
	FragmentManager manager;
	Button butClose;
	TextView tv_messgs, tv_pstdesc;
	String postTitle;
	View viewClose;
	EditText et_report;
	ScrollView viewScroll;
	boolean isReporting = false;
	MySQLAdapter mySQLAdapter;
	long post_id;

}
