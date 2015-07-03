package com.notify.app.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.notify.app.R;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.dialogs.Following;
import com.notify.app.dialogs.PopupMenuCompat;
import com.notify.app.volley.AppController;

public class FeedFUsers extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<LogInfo> feedItems;
	private ImageButton ib_overflw;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	PopupMenuCompat menu;
	MySQLAdapter SQLadapter;
	FragmentManager manager;

	public FeedFUsers(Activity activity, List<LogInfo> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
		manager = ((FragmentActivity) activity).getSupportFragmentManager();

		SQLadapter = new MySQLAdapter(activity);

	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	public void changeBookmarkValue(int position) {
//		if (feedItems.get(position).isBookmarked) {
//			feedItems.get(position).isBookmarked = false;
//		} else {
//			feedItems.get(position).isBookmarked = true;
//		}
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.listy_items_fusers, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.tVNameF);
		TextView locs = (TextView) convertView.findViewById(R.id.tVLocsF);
		TextView tvdesc = (TextView) convertView.findViewById(R.id.tVDescF);
		TextView tv_fdate = (TextView) convertView.findViewById(R.id.tVfollowedF);
		ib_overflw = (ImageButton) convertView.findViewById(R.id.ivoverFlowF);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.ivPhotoF);

		final int pos = position;
		ib_overflw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupMenuCompat menu = PopupMenuCompat.newInstance(activity, v);
				
				menu.inflate(R.menu.listy_unfollow);
				 

				menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// Toast.makeText(MainListy.this, item.getItemId(),
						// Toast.LENGTH_SHORT).show();
						if (item.getItemId() == R.id.row_unmarki) {
							Following follow = new Following(feedItems
									.get(pos).id + "", SQLadapter
									.getUserID(),
									false, pos);
							follow.setCancelable(false);
							follow.show(manager, "ffolloww");
							
						}

						return true;
					}
				});

				menu.show();

			}
		});

		// FeedImageView feedImageView = (FeedImageView) convertView
		// .findViewById(R.id.feedImage1);

		LogInfo item = feedItems.get(position);

//		String[] time = item.posted_date.split(" ");
//		//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date mDate = null;
//		try {
//			mDate = sdf.parse(item.posted_date);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		long timeInMilliseconds = mDate.getTime();
//		Log.d("asasasasass", timeInMilliseconds + "");
//
		name.setText(item.fname+" "+item.lname+" ("+item.uname+")");
		locs.setText(item.city+", "+item.country);
		tvdesc.setText("Email:"+item.email+"\nDOB:"+item.dob+"\nJoined Notify:"+item.created.split(" ")[0]);
		tv_fdate.setText("Followed on:"+item.lastLogin.split(" ")[0]);
//		titleMsg.setText(item.title);
//		String d = "";
//		if (item.desc.length() > 100) {
//			d = item.desc.substring(0, 100) + "...";
//		} else {
//			d = item.desc;
//		}
//		content.setText(d);
//		String tmm[] = time[1].split(":");
//
//		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//				timeInMilliseconds, System.currentTimeMillis(),
//				DateUtils.SECOND_IN_MILLIS);
//		Log.d("Relative Compare", System.currentTimeMillis() + " - "
//				+ timeInMilliseconds + " = "
//				+ (System.currentTimeMillis() - timeInMilliseconds));
//
//		String what_what = UserFunctions.get_RelativeTime(
//				System.currentTimeMillis(), timeInMilliseconds);
//		if (what_what.equals("what what")) {
//			what_what = formatTime(time[0]);
//		}

		profilePic.setImageUrl(UserFunctions.picUrl + item.id + ".jpg",
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
		return (tt[2].charAt(0) == '0' ? tt[2].charAt(1) + "" : tt[2]) + posts
				+ " " + month[Integer.parseInt(tt[1]) - 1] + " " + tt[0];

	}

	static String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sept", "Oct", "Nov", "Dec" };

}
