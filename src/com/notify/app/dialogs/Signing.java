package com.notify.app.dialogs;
import com.notify.app.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.notify.app.adapters.Message;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.signups.SignUps;

public class Signing extends DialogFragment implements OnClickListener {

	LogInfo info;
	Dialog dialog;
	Context context;
	TextView tvMessage;
	FragmentManager manager;

	ArrayList<String> information = new ArrayList<String>();
	public static int r = 0;
	public static String vallus = "";
	public static String email = "";

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
		arg0.putParcelable("parr_information", info);
		// arg0.putString("title", title);
	}

	public void setValues(LogInfo info) {
		this.info = info;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			info = savedInstanceState.getParcelable("parr_information");
		}
		dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.signning);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		context = getActivity();
		manager = getActivity().getSupportFragmentManager();
		// Message.message(context, "as");
		tvMessage = (TextView) dialog.findViewById(R.id.tVMessssss);
		// String[] df = { "dfsfsdf", "sdfsdfsdf", "kjhgkjh", "sdfsdfsdf",
		// "kjhgkjh", "sdfsdfsdf", "kjhgkjh", "sdfsdfsdf", "kjhgkjh",
		// "sdfsdfsdf", "kjhgkjh", "sdfsdfsdf", "kjhgkjh", "my" };
		// ArrayAdapter<String> adap = new ArrayAdapter<String>(context,
		// R.layout.listitem_signup, R.id.tVLeft, df);

		// if (Build.VERSION.SDK_INT >= 16)
		// ok.setBackground(getResources().getDrawable(
		// R.drawable._r2_c2));
		// else
		// ok.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable._r2_c2));

		// read();
		// Message.message(getActivity(), alContacts.get(0));
		NetAsync();
		dialog.show();

		return dialog;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.btnOkSUP) {
			// String save = saveData.getString("theme", "default");
			// Message.message(context, save);

			// System.exit(0);
			dialog.dismiss();
		}
	}

	public void NetAsync() {
		new NetCheck().execute();
	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {
		private ProgressDialog nDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// nDialog = new ProgressDialog(MainActivity.this);
			tvMessage.setText("Checking Network...");
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
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					 URL url = new URL(UserFunctions.chkConnURL);
//					URL url = new URL("http://10.0.2.2/notified/all_con.php");
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
				tvMessage.setText("");
				new ProcessSignUp().execute();
			} else {
				tvMessage.setText("");
				Message.message(context, "Error in Network Connection");
				dialog.dismiss();
			}
		}

	}

	private class ProcessSignUp extends
			AsyncTask<String, JSONObject, JSONObject> {
		// private ProgressDialog pDialog;
		String uname, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog = new ProgressDialog(MainActivity.this);
			// pDialog.setTitle("Contacting Servers");
			// pDialog.setMessage("Logging in ...");
			tvMessage.setText("Checking in....");
			// pDialog.setIndeterminate(false);
			// pDialog.setCancelable(true);
			// pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.SignUpUser(info);
			// JSONObject json_user = null;
			 Log.d("LOGGGG", json.toString()+info.email);
			// try {
			// if (json.getString(UserFunctions.KEY_SUCCESS) != null) {
			// String res = json.getString(UserFunctions.KEY_SUCCESS);
			// Log.d("LOGGGG", res.toString());
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
					tvMessage.setText("Registering...");
					String res = json.getString(UserFunctions.KEY_SUCCESS);
					String red = json.getString(UserFunctions.KEY_ERROR);
					if (Integer.parseInt(res) == 1) {
						// registerErrorMsg.setText("Successfully Registered");

						JSONObject json_user = json.getJSONObject("user");

						// Message.message(getActivity(),
						// json_user.getString("uname"));
						/**
						 * Removes all the previous data in the SQlite database
						 **/
						// UserFunctions logout = new UserFunctions();
						// logout.logoutUser(getApplicationContext());

						// db.addUser(json_user.getString(KEY_FIRSTNAME),
						// json_user.getString(KEY_LASTNAME),
						// json_user.getString(KEY_EMAIL),
						// json_user.getString(KEY_USERNAME),
						// json_user.getString(KEY_UID),
						// json_user.getString(KEY_CREATED_AT));
						/**
						 * Stores registered data in SQlite Database Launch
						 * Registered screen
						 **/
						/**
						 * Close all views before launching Registered screen
						 **/
						SignUps.bitmap2 = null;
						r = 12;
						vallus = json_user.getString("uname");
						email = json_user.getString("email");
						dialog.dismiss();
						getActivity().finish();
					} else if (Integer.parseInt(red) == 2) {
						dialog.dismiss();
						AlertDialog diag = new AlertDialog();
						diag.setValue("Username already Taken.");
						diag.show(manager, "adsas");
					} else if (Integer.parseInt(red) == 3) {
						dialog.dismiss();
						AlertDialog diag = new AlertDialog();
						diag.setValue("Invalid Email.");
						diag.show(manager, "adsas");
					} else if (Integer.parseInt(red) == 4) {
						dialog.dismiss();
						AlertDialog diag = new AlertDialog();
						diag.setValue("Email address already taken");
						diag.show(manager, "adsasEmail");
					}
				} else {
					dialog.dismiss();
					AlertDialog diag = new AlertDialog();
					diag.setValue("Error Occured in Registration.");
					diag.show(manager, "adsas");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

}