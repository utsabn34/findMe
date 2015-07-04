package com.notify.app.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.notify.app.R;
import com.notify.app.adapters.Classes.CommentBlock;
import com.notify.app.adapters.Classes.Users;
import com.notify.app.dialogs.PopupMenuCompat;
import com.notify.app.dialogs.ShowFull.updateComment;
import com.notify.app.volley.AppController;

public class PingAdapter extends BaseAdapter implements OnClickListener {
	private Context activity;
	private LayoutInflater inflater;
	private List<Users> users;
	private boolean isDeletable = false;
	// private ImageButton ib_overflw;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	PopupMenuCompat menu;
	updateComment deleteTrigger;
	public static boolean IS_COMMENT_DELETABLE = true;
	public long doublePressToConfirm = 0;

	public PingAdapter(Context activity,
			List<Users> users) {
		this.activity = activity;
		this.users = users;
	}

	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public Object getItem(int location) {
		return users.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.itemping, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView uname = (TextView) convertView
				.findViewById(R.id.tVnamme);
		final TextView email = (TextView) convertView
				.findViewById(R.id.emaills);


			

		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.pingPics);
		Users itemUser = users.get(position);

		
		
		uname.setText(itemUser.username);
		email.setText(itemUser.email);
		profilePic.setImageUrl(UserFunctions.picUrl + itemUser.id + ".jpg",
				imageLoader);

		return convertView;
	}

	public static String formatTime(String time) {
		String tt[] = time.split("-");
		String posts = "";
		if (tt[2].equals("01") || tt[2].equals("1")) {
			posts = "st";
		} else if (tt[2].equals("02") || tt[2].equals("2")) {
			posts = "nd";
		} else if (tt[2].equals("03") || tt[2].equals("3")) {
			posts = "rd";
		} else {
			posts = "th";
		}
		return tt[2] + posts + " " + month[Integer.parseInt(tt[1]) - 1] + " "
				+ tt[0];

	}

	static String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sept", "Oct", "Nov", "Dec" };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
