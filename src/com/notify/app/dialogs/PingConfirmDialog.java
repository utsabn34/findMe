package com.notify.app.dialogs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.notify.app.R;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.UserFunctions;

public class PingConfirmDialog extends DialogFragment {
	// String name[];
	// String title, location, phoneno, email, fax, pob;
	String alertMessage;
	// String phone;
	String name = "";
	ArrayList<String> alContactsNumber = new ArrayList<String>();
	TextView message;
	// Spinner contacts;
	Button ok, cancel;
	private String style = "";
	ArrayList<String> alContactsName = new ArrayList<String>();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			alertMessage = savedInstanceState.getString("allertmesg");
		}
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.ping_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		message = (TextView) dialog.findViewById(R.id.dialogMessagePing);
		ok = (Button) dialog.findViewById(R.id.buttonPing);
		message.setText("Do you want to ping " + pinger);
		cancel = (Button) dialog.findViewById(R.id.buttonPingCancel);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				NetAsync();

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});

		dialog.show();

		return dialog;
	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
	}

	public void setValues(String name, String pingerId, String pingerToId) {
		this.pinger = name;
		this.pingerId = pingerId;
		this.pingerToId = pingerToId;
	}

	String pinger = "";
	String pingerId = "";
	String pingerToId = "";
	
	public void NetAsync() {
		new NetCheck().execute();
	}

	private class NetCheck extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// nDialog = new ProgressDialog(MainActivity.this);
			message.setText(getActivity().getResources().getString(
					R.string.chek_conn_message));

		}

		@Override
		protected Boolean doInBackground(String... args) {
			/**
			 * Gets current device state and checks for working internet
			 * connection by trying Google.
			 **/
			ConnectivityManager cm = (ConnectivityManager) getActivity()
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
				new ProcessPing().execute();
			} else {
				getDialog().dismiss();

				Message.message(
						getActivity(),
						getActivity().getResources().getString(
								R.string.error_conn_message));

			}
		}

	}

	private class ProcessPing extends
			AsyncTask<String, JSONObject, JSONObject> {
		// private ProgressDialog pDialog;
		String uname, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			message.setText("Pinging " + pinger);

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.processPing(pingerId, pingerToId);
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
						getDialog().dismiss();

						Message.message(getActivity(), "Pinging Successful");

					} else {
						Message.message(getActivity(), "Not Successful");
					}
				} else {
					Message.message(getActivity(), "Something went wrong");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				// trigger.refreshFromDelete(position);
				getDialog().dismiss();
			}

		}

	}
}
