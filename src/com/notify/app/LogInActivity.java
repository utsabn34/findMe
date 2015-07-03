package com.notify.app;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gcm.GCMRegistrar;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.WakeLocker;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.dialogs.AlertDialog;
import com.notify.app.dialogs.Signing;
import com.notify.app.signups.SignUps;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends SherlockFragmentActivity implements
		OnClickListener {
	ImageView imv;
	Button butLogin;
	EditText et_uname, et_upass;
	TextView tvMessgs, tvSignups;
	FragmentManager fm;
	AsyncTask<Void, Void, Void> mRegisterTask;
	private static final String KEY_LOG_uname = "uname";
	private static String KEY_LOG_FIRSTNAME = "fname";
	private static String KEY_LOG_LASTNAME = "lname";
	private static String KEY_LOG_EMAIL = "email";
	private static String KEY_LOG_CREATED = "created_at";
	private static String KEY_LOG_DOB = "dob";
	private static String KEY_LOG_COuntry = "country";
	private static String KEY_LOG_City = "city";
	private static String KEY_LOG_LstLogin = "last_login";

	private static final String TAG = MainListy.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		butLogin = (Button) findViewById(R.id.btn);
		fm = getSupportFragmentManager();
		et_uname = (EditText) findViewById(R.id.etUname);
		et_upass = (EditText) findViewById(R.id.etPass);
		tvMessgs = (TextView) findViewById(R.id.tVmessgs);
		tvSignups = (TextView) findViewById(R.id.tvS);
		butLogin.setOnClickListener(this);
		tvSignups.setOnClickListener(this);

		et_upass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// imv = (ImageView) findViewById(R.id.imageView1);
		// SemiCircle circ = new SemiCircle(Color.BLUE, Direction.LEFT);
		// imv.setImageDrawable(circ);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.btn) {
			String uname = et_uname.getText().toString().trim();

			if ((!uname.equals(""))
					&& (!et_upass.getText().toString().trim().equals(""))) {
				butLogin.setClickable(false);
				tvSignups.setClickable(false);
				NetAsync(v);
			} else if ((!et_upass.getText().toString().trim().equals(""))) {
				Message.message(getApplicationContext(), "Username field empty");
			} else if ((!uname.equals(""))) {
				Message.message(getApplicationContext(), "Password field empty");
			} else {
				Message.message(getApplicationContext(), "The fields are empty");
			}
			// if(isOnline_()){
			// Message.message(getApplicationContext(), "On");
			// }else{
			// Message.message(getApplicationContext(), "Off");
			// }

		} else if (v.getId() == R.id.tvS) {
			Intent i = new Intent(LogInActivity.this, SignUps.class);
			startActivity(i);

			// AlertDialog diag = new AlertDialog();
			// diag.setValue("Please Check the internet Connection");
			// diag.show(getSupportFragmentManager(), "adsas");

		}

	}

	public void NetAsync(View view) {
		new NetCheck().execute();
	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// nDialog = new ProgressDialog(MainActivity.this);
			tvMessgs.setText("Checking Network...");
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
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
				Log.d("nhjk", "connected");
				tvMessgs.setText("please wait..");
				// new ProcessLogin().execute();

				runGCM();

			} else {
				// nDialog.dismiss();
				butLogin.setClickable(true);

				tvSignups.setClickable(true);
				Log.d("nhjk", "disconnected");
				tvMessgs.setText("");
				Message.message(LogInActivity.this,
						"Error in Network Connection");
			}
		}

	}

	private class ProcessLogin extends
			AsyncTask<String, JSONObject, JSONObject> {
		private ProgressDialog pDialog;
		String uname, password;
		String message = "";
		boolean success = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog = new ProgressDialog(MainActivity.this);
			// pDialog.setTitle("Contacting Servers");
			// pDialog.setMessage("Logging in ...");
			tvMessgs.setText("Logging in....");
			uname = et_uname.getText().toString().trim();
			password = et_upass.getText().toString().trim();
			// pDialog.setIndeterminate(false);
			// pDialog.setCancelable(true);
			// pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(uname, password, args[0]);
			JSONObject json_user = null;
			// Log.d("LOGGGG", json.toString());
			try {
				if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
					String res = json.getString(UserFunctions.KEY_SUCCESS);
					Log.d("LOGGGG", res.toString());

					if (Integer.parseInt(res) == 1) {
						// pDialog.setMessage("Loading User Space");
						// pDialog.setTitle("Getting Data");
						success = true;
						json_user = json.getJSONObject("user");
						/**
						 * Clear all previous data in SQlite database.
						 **/

					} else if (Integer.parseInt(res) == 2
							|| Integer.parseInt(res) == 3) {
						message = json.getString("error_msg");
					} else {
						message = json.getString("error_msg");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return json_user;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null && success) {

					MySQLAdapter db = new MySQLAdapter(LogInActivity.this);
					/**
					 * Clear all previous data in SQlite database.
					 **/
					UserFunctions logout = new UserFunctions();
					logout.logoutUser(LogInActivity.this);
					GCMRegistrar
							.setRegisteredOnServer(LogInActivity.this, true);
					Log.d("asdsadsadsad", json.toString());
					// Message.message(getApplicationContext(),
					// json.getString("id") + "");
					db.addUser(new LogInfo(json.getString("id"), json
							.getString(KEY_LOG_uname), json
							.getString(KEY_LOG_FIRSTNAME), json
							.getString(KEY_LOG_LASTNAME), "", json
							.getString(KEY_LOG_EMAIL), json
							.getString(KEY_LOG_CREATED), json
							.getString(KEY_LOG_DOB), json
							.getString(KEY_LOG_COuntry), json
							.getString(KEY_LOG_City), json
							.getString(KEY_LOG_LstLogin)));
					/**
					 * If JSON array details are stored in SQlite it launches
					 * the User Panel.
					 **/
					Intent upanel = new Intent(getApplicationContext(),
							MainListy.class);
					upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					tvMessgs.setText("");
					startActivity(upanel);
					/**
					 * Close Login Screen
					 **/
					finish();
				} else {
					butLogin.setClickable(true);

					tvSignups.setClickable(true);
					tvMessgs.setText("");
					Message.message(LogInActivity.this, message);
					// pDialog.dismiss();
					// loginErrorMsg.setText("Incorrect username/password");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
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
	}

	public void runGCM() {
		if (!TextUtils.isEmpty(getRegistrationId(LogInActivity.this))) {
			// regId = registerGCM();
			// Log.d("RegisterActivity", "GCM RegId: " + regId);
			// Message.message(LogInActivity.this,
			// getRegistrationId(LogInActivity.this) + "he he he");

			GCMRegistrar.checkDevice(LogInActivity.this);

			GCMRegistrar.checkManifest(LogInActivity.this);

			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					UserFunctions.DISPLAY_MESSAGE_ACTION));
			//
			final String regId = GCMRegistrar
					.getRegistrationId(LogInActivity.this);
			if (regId.equals("")) {
				// Registration is not present, register now
				// with GCM
				GCMRegistrar.register(LogInActivity.this,
						UserFunctions.SENDER_ID);
			} else {
				// Device is already registered on GCM
				if (GCMRegistrar.isRegisteredOnServer(LogInActivity.this)) {
					// Skips registration.
					String[] params = { "" };
					new ProcessLogin().equals(params);
					Toast.makeText(
							getApplicationContext(),
							"Already registered with GCM with "
									+ GCMRegistrar
											.getRegistrationId(LogInActivity.this),
							Toast.LENGTH_LONG).show();
				} else {
					final Context context = LogInActivity.this;

					String[] paramss = { regId };
					new ProcessLogin().execute(paramss);

					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {

							// ServerUtilities.register(context, "bai", "sdsd",
							// regId);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}

					};
					// mRegisterTask.execute(null, null, null);
				}
			}

		} else {
			GCMRegistrar.checkDevice(LogInActivity.this);

			GCMRegistrar.checkManifest(LogInActivity.this);

			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					UserFunctions.DISPLAY_MESSAGE_ACTION));
			GCMRegistrar.register(LogInActivity.this, UserFunctions.SENDER_ID);
			String id = GCMRegistrar.getRegistrationId(LogInActivity.this);
			// Message.message(LogInActivity.this, id + "   kjkjk");
			// butLogin.setClickable(true);
		}
		// else {
		// Toast.makeText(getApplicationContext(),
		// "Already Registered with GCM Server!", Toast.LENGTH_LONG)
		// .show();
		//
		// mRegisterTask = new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		// // Register on our server
		// // On server creates a new user
		// ServerUtilities.register(MainListy.this, "bijz", "maha",
		// regId);
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
		//
		// }
	}

	private void registerInBackground() {
		// new AsyncTask<Void, Void, String>() {
		// @Override
		// protected String doInBackground(Void... params) {
		// String msg = "";
		// try {
		// if (gcm == null) {
		// gcm = GoogleCloudMessaging.getInstance(context);
		// }
		// regId = gcm.register(UserFunctions.SENDER_ID);
		//
		// Log.d("RegisterActivity", "registerInBackground - regId: "
		// + regId);
		// msg = "Device registered, registration ID=" + regId;
		//
		// storeRegistrationId(context, regId);
		// } catch (IOException ex) {
		// msg = "Error :" + ex.getMessage();
		// Log.d("RegisterActivity", "Error: " + msg);
		// }
		// Log.d("RegisterActivity", "AsyncTask completed: " + msg);
		// return msg;
		// }
		//
		// @Override
		// protected void onPostExecute(String msg) {
		// Toast.makeText(getApplicationContext(),
		// "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
		// .show();
		// }
		// }.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				UserFunctions.fileName, Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(UserFunctions.REG_ID, regId);
		editor.putInt(UserFunctions.APP_VER, appVersion);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				UserFunctions.fileName, Context.MODE_PRIVATE);
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

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
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
			// Toast.makeText(getApplicationContext(),
			// "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			if (TextUtils.isEmpty(getRegistrationId(LogInActivity.this))) {
				String id = GCMRegistrar.getRegistrationId(LogInActivity.this);
				// Message.message(LogInActivity.this, id + "   kjkjk");
				storeRegistrationId(LogInActivity.this, id);
				String[] paramsss = { id };
				new ProcessLogin().execute(paramsss);
				// butLogin.setClickable(true);
			}

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
