package com.notify.app.dialogs;

import com.notify.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.notify.app.adapters.CheckBox;
import com.notify.app.adapters.CheckBox.OnCheckListener;
import com.notify.app.adapters.GPSTracker;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.WorkAroundMapFragment;
import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.adapters.WorkAroundMapFragment.MapCallback;
import com.notify.app.dialogs.PopupMenuCompat.OnMenuItemClickListener;
import com.notify.app.signups.Form;
import com.notify.app.signups.RangeValidator;
import com.notify.app.signups.Validate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class New_Notify extends DialogFragment implements OnClickListener,
		OnCheckListener, MapCallback, OnItemSelectedListener {

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
		arg0.putBoolean("booldatetime", booldateTime);
		arg0.putBoolean("booltime", boolTime);
		arg0.putBoolean("boollocss", boolLocs);
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
			latis = "";
			longis = "";

		} else {
			booldateTime = savedInstanceState.getBoolean("booldatetime");
			boolTime = savedInstanceState.getBoolean("booltime");
			boolLocs = savedInstanceState.getBoolean("boollocss");
		}

		View root = inflater.inflate(R.layout.new_notify, container, false);
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();

		mySQLAdapter = new MySQLAdapter(con);
		// Message.message(con, mySQLAdapter.getUser().id + "");
		initializers(root);

		if (booldateTime) {
			datetimeLay.setVisibility(View.VISIBLE);
			setUpDate();
		} else {
			datetimeLay.setVisibility(View.GONE);
		}
		if (boolLocs) {
			locsLay.setVisibility(View.VISIBLE);
		} else {
			locsLay.setVisibility(View.GONE);
		}
		if (boolTime) {
			timeLay.setVisibility(View.VISIBLE);
			chkTime.setChecked(true);
			setUpTime();
		} else {
			timeLay.setVisibility(View.GONE);
			chkTime.setChecked(false);
		}

		return root;
	}

	public void setUpDate() {
		mn.clear();
		dy.clear();
		yr.clear();
		Date dt = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		int curYr = calendar.get(Calendar.YEAR);
		for (int i = curYr; i <= curYr + 10; i++) {
			yr.add(i + "");
		}

		for (int i = 1; i <= 31; i++) {

			if (i > 9)
				dy.add(i + "");
			else
				dy.add("0" + i);
		}
		for (int i = 1; i <= 12; i++) {
			if (i > 9)
				mn.add(i + "");
			else
				mn.add("0" + i);
		}
		ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_sm, yr);
		adapterYear
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_sm, mn);
		adapterMonth
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_sm, dy);
		adapterDay
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinDay.setAdapter(adapterDay);
		spinMonth.setAdapter(adapterMonth);
		spinYear.setAdapter(adapterYear);
		spinDay.setOnItemSelectedListener(this);
		spinMonth.setOnItemSelectedListener(this);
		spinYear.setOnItemSelectedListener(this);
	}

	public void setUpTime() {
		timehrs.clear();
		timemins.clear();
		for (int i = 0; i <= 23; i++) {
			if (i > 9) {
				timehrs.add(i + " Hours");
			} else {
				timehrs.add("0" + i + " Hours");
			}

		}

		for (int i = 0; i <= 59; i++) {
			if (i > 9) {
				timemins.add(i + " Mins");
			} else {
				timemins.add("0" + i + " Mins");
			}
		}
		ArrayAdapter<String> adapterTimehrs = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_sm, timehrs);
		adapterTimehrs
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> adapterTimemins = new ArrayAdapter<String>(con,
				R.layout.spinner_layout_sm, timemins);
		adapterTimemins
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinTimeHrs.setAdapter(adapterTimehrs);
		spinTimeMins.setAdapter(adapterTimemins);
		spinTimeHrs.setOnItemSelectedListener(this);
		spinTimeMins.setOnItemSelectedListener(this);

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
		if (arg0.getId() == R.id.okBut) {
			getDialog().dismiss();

		} else if (arg0.getId() == R.id.ovrflw_newnotify) {
			PopupMenuCompat menu = PopupMenuCompat.newInstance(con, arg0);
			menu.inflate(R.menu.popup_newnotify);

			menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					if (item.getItemId() == R.id.row_addDate) {
						if (booldateTime) {
							datetimeLay.setVisibility(View.GONE);
							timeLay.setVisibility(View.GONE);
							booldateTime = false;
							boolTime = false;
							resetDateInfo();
							resetTimeInfo();
						} else {
							setUpDate();
							datetimeLay.setVisibility(View.VISIBLE);
							booldateTime = true;
						}

					} else if (item.getItemId() == R.id.row_addLocs) {
						if (boolLocs) {
							locsLay.setVisibility(View.GONE);
							boolLocs = false;
						} else {
							locsLay.setVisibility(View.VISIBLE);
							boolLocs = true;

						}
					}
					return true;
				}
			});

			menu.show();

		} else if (arg0.getId() == R.id.butGo) {

			Geocoder gc = new Geocoder(con);

			if (Geocoder.isPresent()) {
				List<Address> list = null;
				try {
					list = gc.getFromLocationName(etLocs.getText().toString()
							.trim(), 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Address address = list.get(0);

				double lat = address.getLatitude();
				double lng = address.getLongitude();
				LatLng lngs = new LatLng(lat, lng);
				mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						lngs, 16));

				textWhere.setText("WHERE:\nLatitude:" + lat + " Longitude:"
						+ lng);
				latis = lat + "";
				longis = lng + "";
			}

		} else if (arg0.getId() == R.id.postNotify) {

			RangeValidator range_validator = new RangeValidator(con, 3, 1000,
					R.string.validator_range2);

			Validate et_Happening = new Validate(etHppng);
			Validate et_Details = new Validate(etDeteils);
			et_Happening.addValidator(range_validator);
			et_Details.addValidator(range_validator);

			Form mForm = new Form();
			mForm.addValidates(et_Happening);
			mForm.addValidates(et_Details);

			if (mForm.validate()) {
				scrollView.setVisibility(View.GONE);

				textLoads.setVisibility(View.VISIBLE);
				NetAsync();
			} else {

				scrollView.post(new Runnable() {
					@Override
					public void run() {
						scrollView.fullScroll(View.FOCUS_UP);

						if (etHppng.getText().toString().trim().length() < 4) {
							etHppng.requestFocus();
						} else {
							etDeteils.requestFocus();
						}
					}
				});

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
			textLoads.setText(con.getResources().getString(
					R.string.chek_conn_message));
			// nDialog.setMessage("Loading..");
			// nDialog.setIndeterminate(false);
			// nDialog.setCancelable(true);
			// nDialog.show();
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
				textLoads.setText("");
				textLoads.setVisibility(View.GONE);
				scrollView.setVisibility(View.VISIBLE);

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
			// pDialog = new ProgressDialog(MainActivity.this);
			// pDialog.setTitle("Contacting Servers");
			// pDialog.setMessage("Logging in ...");
			textLoads.setText("Posting in....");
			// pDialog.setIndeterminate(false);
			// pDialog.setCancelable(true);
			// pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();

			FeedItem items = new FeedItem(0, Long.parseLong(mySQLAdapter
					.getUser().id), etHppng.getText().toString().trim(),
					etDeteils.getText().toString().trim(), "", saveYear + "-"
							+ saveMonth + "-" + saveDay + " " + saveHrs + ":"
							+ saveMins, latis, longis, "", 0, false, false);
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
						newAdded.rfshjustAdded(new FeedItem(c.getInt("id"), c
								.getLong("user_id"), c.getString("title"), c
								.getString("desc"), c.getString("posted_date"),
								c.getString("date"), c.getString("lat"), c
										.getString("long"), mySQLAdapter
										.getUserName(), 0, false, false));

						Message.message(con,
								"Your Post is added..Verify the changes");
						getDialog().dismiss();

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
		etHppng = (EditText) root.findViewById(R.id.etHappen);
		etDeteils = (EditText) root.findViewById(R.id.etDett);
		etLocs = (EditText) root.findViewById(R.id.etLocation);

		textLoads = (TextView) root.findViewById(R.id.textloadd);

		butGoLocs = (Button) root.findViewById(R.id.butGo);
		chkTime = (CheckBox) root.findViewById(R.id.cBIncTime);
		viewOk = root.findViewById(R.id.okBut);
		spinYear = (Spinner) root.findViewById(R.id.spinYearnew);
		spinMonth = (Spinner) root.findViewById(R.id.spinMonthnew);
		spinDay = (Spinner) root.findViewById(R.id.spinDaynew);
		spinTimeHrs = (Spinner) root.findViewById(R.id.spinTimeHrs);
		spinTimeMins = (Spinner) root.findViewById(R.id.spinTimeMin);
		datetimeLay = (LinearLayout) root.findViewById(R.id.layDateTime);
		locsLay = (LinearLayout) root.findViewById(R.id.layLocss);
		timeLay = (LinearLayout) root.findViewById(R.id.laySpintwo);
		btnpostNotify = (Button) root.findViewById(R.id.postNotify);
		ovrflw_newnotify = (ImageButton) root
				.findViewById(R.id.ovrflw_newnotify);
		scrollView = (ScrollView) root.findViewById(R.id.scroool);

		textWhere = (TextView) root.findViewById(R.id.textWhere);
		tvDateInfo = (TextView) root.findViewById(R.id.tvDateInfo);

		viewOk.setOnClickListener(this);
		chkTime.setOncheckListener(this);
		btnpostNotify.setOnClickListener(this);
		butGoLocs.setOnClickListener(this);
		ovrflw_newnotify.setOnClickListener(this);
	}

	String[] titles;
	String[] desc;
	private ViewPager pager;
	Context con;
	GoogleMap mGoogleMap;
	String latis, longis;
	FragmentManager manager;
	Button butClose;
	TextView topText, textLoads, textWhere, tvDateInfo;
	RadioGroup radioGp;
	String year;
	CheckBox chkTime;
	SharedPreferences prefs;
	private WorkAroundMapFragment fragment;
	// ScrollView viewScroll;
	Button butGoLocs;
	Boolean booldateTime = false, boolLocs = false, boolTime = false;
	EditText etHppng, etDeteils, etLocs;
	ImageButton ovrflw_newnotify;
	String years = "", months = "", days = "", mins = "", hrs = "";
	ArrayList<String> yr = new ArrayList<String>();
	ArrayList<String> dy = new ArrayList<String>();
	ArrayList<String> mn = new ArrayList<String>();
	ArrayList<String> timehrs = new ArrayList<String>();
	ArrayList<String> timemins = new ArrayList<String>();
	Button btnpostNotify;
	View viewOk;
	LinearLayout datetimeLay, locsLay, timeLay;
	Spinner spinCountry, spinDay, spinYear, spinMonth, spinTimeHrs,
			spinTimeMins;
	MySQLAdapter mySQLAdapter;
	ScrollView scrollView;
	refreshNotify newAdded;
	String saveYear = "0000";
	String saveMonth = "00";
	String saveDay = "00";
	String saveHrs = "00";
	String saveMins = "00";

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		newAdded = (refreshNotify) activity;
	}

	@Override
	public void onCheck(boolean check) {
		if (boolTime) {
			timeLay.setVisibility(View.GONE);
			boolTime = false;
			resetTimeInfo();
			updateDateTimeInfo();
		} else {
			setUpTime();
			timeLay.setVisibility(View.VISIBLE);
			boolTime = true;
		}

	}

	@Override
	public void onMapReady(GoogleMap map) {
		setUpmaps(map);

	}

	public void setUpmaps(final GoogleMap mGoogleMap) {
		// fragment = (WorkAroundMapFragment) getChildFragmentManager()
		// .findFragmentByTag("childmapp");

		if (mGoogleMap != null) {

			final LatLng latlong = gpsDecide();
			if (latlong != null) {
				mGoogleMap
						.addMarker(new MarkerOptions()
								.position(
										new LatLng(latlong.latitude,
												latlong.longitude))
								.title("Your Location").snippet("You are here"));
			}

			mGoogleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng arg0) {

					textWhere.setText("WHERE:\nLatitude:" + arg0.latitude
							+ " Longitude:" + arg0.longitude);
					latis = arg0.latitude + "";
					longis = arg0.longitude + "";

					mGoogleMap.addMarker(new MarkerOptions().position(arg0)
							.title("San Francisco")
							.snippet("Population: 776733"));

					Geocoder gc = new Geocoder(con);

					if (Geocoder.isPresent()) {
						List<Address> list = null;
						try {
							list = gc.getFromLocation(arg0.latitude,
									arg0.longitude, 1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Address address = list.get(0);

						etLocs.setText(address.getAddressLine(0));
					}

				}
			});

		} else {
			Message.message(con, "map null");
		}

		fragment.setListener(new WorkAroundMapFragment.OnTouchListener() {

			@Override
			public void onTouch() {
				// TODO Auto-generated method stub
				scrollView.requestDisallowInterceptTouchEvent(true);
			}
		});

		// FragmentTransaction transaction = getChildFragmentManager()
		// .beginTransaction();
		// transaction.add(R.id.map_full, fragments, "childmapp").commit();

	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);

		if (fragment == null) {
			// fragments = (WorkAroundMapFragment) WorkAroundMapFragment
			// .newInstance();
			fragment = new WorkAroundMapFragment();

			fragment.setMapCallback(New_Notify.this); // This activity will
														// receive
			// the
			// Map object once the map
			// fragment
			// is fully loaded

			// Then we add it using a FragmentTransaction.
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			transaction.add(R.id.map, fragment, "childmapp").commit();

		} else {
			fragment.setMapCallback(this);
		}

	}

	public LatLng gpsDecide() {
		LatLng latlng = null;
		GPSTracker gps;
		gps = new GPSTracker(con);

		// check if GPS enabled
		if (gps.canGetLocation()) {
			latlng = new LatLng(gps.getLatitude(), gps.getLongitude());

		} else {

			// latlng = null;
		}
		return latlng;
	}

	public interface refreshNotify {
		public void rfshjustAdded(FeedItem item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getId() == R.id.spinYearnew) {
			saveYear = yr.get(position);
			updateDateTimeInfo();
		} else if (parent.getId() == R.id.spinMonthnew) {
			saveMonth = mn.get(position);
			updateDateTimeInfo();
		} else if (parent.getId() == R.id.spinDaynew) {
			saveDay = dy.get(position);
			updateDateTimeInfo();
		} else if (parent.getId() == R.id.spinTimeHrs) {
			saveHrs = timehrs.get(position).split(" ")[0];
			updateDateTimeInfo();
		} else if (parent.getId() == R.id.spinTimeMin) {
			saveMins = timemins.get(position).split(" ")[0];
			updateDateTimeInfo();
		}

	}

	public void updateDateTimeInfo() {
		tvDateInfo.setText("Date:" + saveYear + "-" + saveMonth + "-" + saveDay
				+ "\nTime:" + saveHrs + ":" + saveMins);

	}

	public void resetDateInfo() {
		saveYear = "0000";
		saveMonth = "00";
		saveDay = "00";

	}

	public void resetTimeInfo() {
		saveHrs = "00";
		saveMins = "00";
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
