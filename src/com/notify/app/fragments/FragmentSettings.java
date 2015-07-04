package com.notify.app.fragments;

import com.notify.app.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.notify.app.adapters.CheckBox;
import com.notify.app.adapters.MenuItem;
//import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.PopupMenu.OnItemSelectedListener;
import com.notify.app.adapters.UserFunctions;
import com.notify.app.adapters.CheckBox.OnCheckListener;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.dialogs.AddChangeProfilePics;
import com.notify.app.dialogs.ChangeProfileDiags;
import com.notify.app.dialogs.PopupMenuCompat;
import com.notify.app.volley.AppController;

public class FragmentSettings extends Fragment implements OnCheckListener,
		OnClickListener, OnItemSelectedListener {
	// Button butProfile;
	// LinearLayout hiddenlay;
	CheckBox cbNoti;
	SharedPreferences prefs;
	LogInfo info;
	MySQLAdapter adap;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	FragmentManager manager;
	Context con;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frags_settings, container, false);
		final Animation anim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.zoom);
		prefs = getActivity().getSharedPreferences(UserFunctions.fileName,
				Context.MODE_PRIVATE);
		con = getActivity();
		manager = getActivity().getSupportFragmentManager();
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		// butProfile = (Button) view.findViewById(R.id.btnSetts);
		// hiddenlay = (LinearLayout) view.findViewById(R.id.hidProfs);
		adap = new MySQLAdapter(getActivity());
		info = adap.getUser();
		initializers(view);
		if (URLUtil
				.isValidUrl(UserFunctions.picUrl + adap.getUserID() + ".jpg")) {
			// Message.message(con, "true");
		} else {
			// Message.message(con, "false");
		}

		return view;
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			TextView tv = (TextView) rootView.findViewById(R.id.textView1);
			tv.setText("asaddf");
			return rootView;
		}
	}

	@Override
	public void onCheck(boolean check) {
		SharedPreferences.Editor editor = prefs.edit();

		if (check) {
			editor.putString(UserFunctions.fileNoti, "");
		} else {
			editor.putString(UserFunctions.fileNoti, "no");
		}
		editor.commit();

	}

	public void initializers(View v) {

		cbNoti = (CheckBox) v.findViewById(R.id.cBNotification);
		imv = (NetworkImageView) v.findViewById(R.id.ivPhotoChange);
		tvDOB = (TextView) v.findViewById(R.id.etSet_dob);
		tvFname = (TextView) v.findViewById(R.id.etSet_fname);
		tvLname = (TextView) v.findViewById(R.id.etSet_lname);
		tvCity = (TextView) v.findViewById(R.id.etSet_city);
		tvCoun = (TextView) v.findViewById(R.id.etSet_country);
		tvUname = (TextView) v.findViewById(R.id.etSet_uname);
		tvEmail = (TextView) v.findViewById(R.id.etSet_email);
		tvJoinedNotify = (TextView) v.findViewById(R.id.etSet_joined);
		proSett = (ImageButton) v.findViewById(R.id.butproSett);

		tvCoun.setText(info.country);
		tvFname.setText(info.fname);
		tvLname.setText(info.lname);
		tvCity.setText(info.city);
		tvDOB.setText(info.dob);
		tvEmail.setText(info.email);
		tvUname.setText(info.uname);
		tvJoinedNotify.setText(UserFunctions.formatTime(info.created.split(" ")[0]));
		

		cbNoti.setOncheckListener(this);
		proSett.setOnClickListener(this);
		imv.setImageUrl(UserFunctions.picUrl + info.id + ".jpg", imageLoader);

		if (prefs.getString(UserFunctions.fileNoti, "").equals("no")) {
			cbNoti.setChecked(false);
		} else {
			cbNoti.setChecked(true);
		}
	}

	NetworkImageView imv;
	TextView tvFname, tvLname, tvCoun, tvCity, tvDOB, tvUname, tvEmail,
			tvJoinedNotify;
	ImageButton proSett;
	private final static int PLAY_SELECTION = 0;
	private final static int ADD_TO_PLAYLIST = 1;
	private final static int SEARCH = 2;

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.butproSett) {

			PopupMenuCompat menu = PopupMenuCompat.newInstance(con, v);

			menu.getMenu().add(1, 2, 1, "Change Profile Info");
			if (URLUtil.isValidUrl(UserFunctions.picUrl + adap.getUserID()
					+ ".jpg")) {
				menu.getMenu().add(1, 3, 2, "Remove Profile Pics");
			} else {
				menu.getMenu().add(1, 4, 2, "Add/Change Pics");
			}
			menu.getMenu().add(1, 5, 3, "Add/Change Pics");

			menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(android.view.MenuItem item) {
					// TODO Auto-generated method stub
					if (item.getItemId() == 2) {
						ChangeProfileDiags diag = new ChangeProfileDiags();
						diag.setValues(info);
						diag.setCancelable(false);
						diag.show(manager, "changeeproff");
					} else if (item.getItemId() == 5) {
						AddChangeProfilePics diag = new AddChangeProfilePics(info.id);
						diag.setCancelable(false);
						diag.show(manager, "changeeproffPics");
					}
					return true;
				}

			});

			menu.show();

		}
	}

	public void refreshView(LogInfo info) {
		this.info = info;
		tvCoun.setText(info.country);
		tvFname.setText(info.fname);
		tvLname.setText(info.lname);
		tvCity.setText(info.city);
		tvDOB.setText(info.dob);
		tvEmail.setText(info.email);
		tvUname.setText(info.uname);
		tvJoinedNotify.setText(UserFunctions.formatTime(info.created.split(" ")[0]));
	}

	@Override
	public void onItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

	}

}