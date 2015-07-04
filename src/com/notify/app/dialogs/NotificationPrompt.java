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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.notify.app.R;
import com.notify.app.adapters.AlarmManagerHelper;
import com.notify.app.adapters.Classes.NotiItem;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.MySQLAdapter;
import com.notify.app.adapters.Utils;

public class NotificationPrompt extends DialogFragment implements
		OnClickListener, OnCheckedChangeListener {

	public NotificationPrompt() {
	}

	public NotificationPrompt(NotiItem item) {
		this.notiItem = item;
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

		}
		View root = inflater.inflate(R.layout.notification_prompt, container,
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
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.butNoNoti) {
			getDialog().dismiss();
			// AlarmManagerHelper.cancelAlarms(con);

			// mySQLAdapter.createAlarm(new NotiItem(1, "Working 1",
			// "Yeah your",
			// "2015-01-04 01:15:00", "posted", "sad", "sds", "211", "45",
			// 34));
			// mySQLAdapter.createAlarm(new NotiItem(100, "Working 1",
			// "Yeah your",
			// "2015-01-04 01:16:00", "posted", "sad", "sds", "211", "45",
			// 34));
			// ArrayList<NotiItem> item = new ArrayList<NotiItem>();
			// item.add(new NotiItem(11, "Working 1 COme", "Yeah your",
			// "2015-01-04 03:34:00", "posted", "sad", "sds", "211", "45",
			// 34, 30));
			//
			// item.add(new NotiItem(18, "Working 1 COme", "Yeah your",
			// "2015-01-04 03:54:00", "posted", "sad", "sds", "211", "45",
			// 34, 30));
			// mySQLAdapter.createAlarm(notiItem);
			//
			// AlarmManagerHelper.setAlarms(con);
			// Message.message(con, "hell");

		} else if (arg0.getId() == R.id.butYesNoti) {
			// getDialog().dismiss();
			mySQLAdapter.createAlarm(notiItem);

			AlarmManagerHelper.setAlarms(con);
			Message.message(con, "hell");
			// AlarmManagerHelper.cancelAlarms(con);
			// mySQLAdapter.deleteAlarm(1);
			// AlarmManagerHelper.setAlarms(con);
			// Message.message(con, "Alarm deleted");
			getDialog().dismiss();

		}
	}

	public void initializers(View root) {
		manager = getActivity().getSupportFragmentManager();
		con = getActivity();
		mySQLAdapter = new MySQLAdapter(con);

		tv_messgs = (TextView) root.findViewById(R.id.tvNotiMess);
		butNo = (Button) root.findViewById(R.id.butNoNoti);
		butYes = (Button) root.findViewById(R.id.butYesNoti);
		rG = (RadioGroup) root.findViewById(R.id.rGTime);

		tv_messgs.setText("Do you want notification for this post at "
				+ Utils.formatTime(notiItem.time) + " " + notiItem.time);
		butNo.setOnClickListener(this);
		butYes.setOnClickListener(this);
		rG.setOnCheckedChangeListener(this);
		rG.getCheckedRadioButtonId();
	}

	Context con;
	FragmentManager manager;
	Button butYes, butNo;
	TextView tv_messgs;
	RadioGroup rG;
	View viewClose;
	MySQLAdapter mySQLAdapter;
	NotiItem notiItem;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group.getId() == R.id.rGTime) {
			if (checkedId == R.id.rBSame) {
				Message.message(con, "SAme");
			} else if (checkedId == R.id.rB30) {
				Message.message(con, "30000");
			} else if (checkedId == R.id.rB1hr) {
				Message.message(con, "1hr");
			}

		}

	}

}
