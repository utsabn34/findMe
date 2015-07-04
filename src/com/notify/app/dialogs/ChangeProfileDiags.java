package com.notify.app.dialogs;

import com.notify.app.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.signups.Form;
import com.notify.app.signups.RangeValidator;
import com.notify.app.signups.Validate;

public class ChangeProfileDiags extends DialogFragment implements
		OnClickListener, OnItemSelectedListener {

	@Override
	public void onSaveInstanceState(Bundle arg0) {

		super.onSaveInstanceState(arg0);
		arg0.putParcelable("InfoinChangeprofs", info);
		arg0.putBoolean("isconnectingg", isConnecting);
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
			info = savedInstanceState.getParcelable("InfoinChangeprofs");
			isConnecting = savedInstanceState.getBoolean("isconnectingg");
		}
		View root = inflater.inflate(R.layout.change_prof_diags, container,
				false);

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
		if (arg0.getId() == R.id.butCancelProf) {
			getDialog().dismiss();

		} else if (arg0.getId() == R.id.butChangeProf && !isConnecting) {
			RangeValidator range_validator = new RangeValidator(con, 3, 30,
					R.string.validator_range3);
			Validate vdFname = new Validate(tv_fname);
			Validate vdLname = new Validate(tv_lname);
			Validate vdCoun = new Validate(tv_coun);
			Validate vdCity = new Validate(tv_city);

			vdFname.addValidator(range_validator);
			vdLname.addValidator(range_validator);
			vdCoun.addValidator(range_validator);
			vdCity.addValidator(range_validator);

			Form mForm = new Form();
			mForm.addValidates(vdFname);
			mForm.addValidates(vdLname);
			mForm.addValidates(vdCoun);
			mForm.addValidates(vdCity);
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
			isConnecting = true;
			but_change.setText("conn..");
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
				new ProcessChangeProfile().execute(tv_fname.getText()
						.toString().trim(), tv_lname.getText().toString()
						.trim(), tv_city.getText().toString().trim(), tv_coun
						.getText().toString().trim(),stoYear+"-"+stoMon+"-"+stoDay);
			} else {
				// textLoads.setText("");
				// textLoads.setVisibility(View.GONE);
				// viewScroll.setVisibility(View.VISIBLE);
				but_change.setText("change");
				isConnecting = false;
				Message.message(
						con,
						con.getResources().getString(
								R.string.error_conn_message));

			}
		}

	}

	private class ProcessChangeProfile extends
			AsyncTask<String, JSONObject, JSONObject> {
		// private ProgressDialog pDialog;
		String uname, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// textLoads.setText("Posting in....");
			but_change.setText("wait..");
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.changeProfileInfo(args[0], args[1],
					args[2], args[3], args[4], info.id);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
					String res = json.getString(UserFunctions.KEY_SUCCESS);
					Log.d("LOGGGG", json.toString());
					if (Integer.parseInt(res) == 1) {
						// pDialog.setMessage("Loading User Space");
						// pDialog.setTitle("Getting Data");
						JSONArray json_usr = json.getJSONArray("uinfo");
						JSONObject c = json_usr.getJSONObject(0);
						// int _id = c.getInt("_id");
						// String title = c.getString("title");
						// String isTab = c.getString("isTabs");
						info.fname = c.getString("firstname");
						info.lname = c.getString("lastname");
						info.dob = c.getString("dob");
						info.country = c.getString("country");
						info.city = c.getString("city");
						info.id = c.getString("id");

						mySQLAdapter.UpdateUserInfo(info);
						updater.updateProfileView(info);
						Message.message(con, "Updated Successfully");
						getDialog().dismiss();

					}
				} else {
					Message.message(con, "Updated UnSuccessful");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				isConnecting = false;
				but_change.setText("change");
			}

		}

	}

	public void initializers(View root) {
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();
		mySQLAdapter = new MySQLAdapter(con);

		year = (Spinner) root.findViewById(R.id.spinYearC);
		month = (Spinner) root.findViewById(R.id.spinMonthC);
		day = (Spinner) root.findViewById(R.id.spinDayC);
		tv_fname = (EditText) root.findViewById(R.id.etRegFName);
		tv_lname = (EditText) root.findViewById(R.id.etRegLName);
		tv_coun = (EditText) root.findViewById(R.id.etRegCountry);
		tv_city = (EditText) root.findViewById(R.id.etRegCity);
		but_cancel = (Button) root.findViewById(R.id.butCancelProf);
		but_change = (Button) root.findViewById(R.id.butChangeProf);

		but_cancel.setOnClickListener(this);
		but_change.setOnClickListener(this);

		if (isConnecting) {
			but_change.setText("wait..");
		}

		Date dt = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		int curYr = calendar.get(Calendar.YEAR);
		for (int i = 1910; i <= curYr; i++) {
			yr.add(i + "");
		}
		mn.clear();
		dy.clear();
		for (int i = 1; i <= 12; i++) {
			if (i > 9)
				mn.add(i + "");
			else
				mn.add("0" + i);
		}
		for (int i = 1; i <= 31; i++) {

			if (i > 9)
				dy.add(i + "");
			else
				dy.add("0" + i);
		}

		ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_grey, yr);
		adapterYear
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_grey, mn);
		adapterMonth
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_grey, dy);
		adapterDay
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		day.setAdapter(adapterDay);
		month.setAdapter(adapterMonth);
		year.setAdapter(adapterYear);
		day.setOnItemSelectedListener(this);
		month.setOnItemSelectedListener(this);
		year.setOnItemSelectedListener(this);
		changeProfileView(info);
	}

	public void changeProfileView(LogInfo info) {
		tv_fname.setText(info.fname);
		tv_lname.setText(info.lname);
		tv_coun.setText(info.country);
		tv_city.setText(info.city);
		String dobs[] = info.dob.split("-");
		year.setSelection(yr.indexOf(dobs[0]));
		month.setSelection(mn.indexOf(dobs[1]));
		day.setSelection(dy.indexOf(dobs[2]));
	}

	Context con;
	FragmentManager manager;
	Button butClose;
	TextView tv_messgs;
	EditText tv_fname, tv_lname, tv_coun, tv_city;
	View viewClose;
	MySQLAdapter mySQLAdapter;
	Spinner year, month, day;
	ArrayList<String> yr = new ArrayList<String>();
	ArrayList<String> dy = new ArrayList<String>();
	ArrayList<String> mn = new ArrayList<String>();
	LogInfo info;
	Button but_change, but_cancel;
	boolean isConnecting = false;
	ProfileViewUpdater updater;
	String stoYear = "";
	String stoMon = "";
	String stoDay = "";

	public void setValues(LogInfo info) {
		this.info = info;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		updater = (ProfileViewUpdater) activity;
	}

	public interface ProfileViewUpdater {
		public void updateProfileView(LogInfo info);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getId() == R.id.spinYearC) {
			stoYear = yr.get(position);
		} else if (parent.getId() == R.id.spinMonthC) {
			stoMon = mn.get(position);
		} else if (parent.getId() == R.id.spinDayC) {
			stoDay = dy.get(position);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
