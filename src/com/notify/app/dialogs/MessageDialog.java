package com.notify.app.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
import com.notify.app.adapters.MySQLAdapter;

public class MessageDialog extends DialogFragment implements OnClickListener {

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
		View root = inflater.inflate(R.layout.message_dialog, container, false);

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
		if (arg0.getId() == R.id.mdialog_close) {
			getDialog().dismiss();

		}
	}
	public MessageDialog(String title, String message) {
		this.titleText = title;
		this.msgText = message;

	}
	public void setValues(String title, String message){
		this.titleText = title;
		this.msgText = message;
	}


	public void initializers(View root) {
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();
		mySQLAdapter = new MySQLAdapter(con);

		tvMessgs = (TextView) root.findViewById(R.id.tvMdialogText);
		tvTitle = (TextView) root.findViewById(R.id.tvMdialogTText);
		viewClose = root.findViewById(R.id.mdialog_close);
		
		tvTitle.setText(titleText);
		tvMessgs.setText(msgText);

		viewClose.setOnClickListener(this);

	}

	Context con;
	FragmentManager manager;
	Button butClose;
	TextView tvMessgs,tvTitle;
	String titleText,msgText;
	View viewClose;
	MySQLAdapter mySQLAdapter;

}