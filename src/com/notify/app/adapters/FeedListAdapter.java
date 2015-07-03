package com.notify.app.adapters;

import com.notify.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.notify.app.adapters.Classes.FeedItem;
import com.notify.app.dialogs.Blocking;
import com.notify.app.dialogs.Bookmarking;
import com.notify.app.dialogs.Delete;
import com.notify.app.dialogs.Following;
import com.notify.app.dialogs.MessageDialog;
import com.notify.app.dialogs.PopupMenuCompat;
import com.notify.app.volley.AppController;

public class FeedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	private ImageButton ib_overflw;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	PopupMenuCompat menu;
	MySQLAdapter SQLadapter;
	FragmentManager manager;
	private int listType;
	public static final int MAIN_LISTY = 1;
	public static final int BOOKMARK_LISTY = 2;
	public static final int MY_POST_LISTY = 3;

	public FeedListAdapter(Activity activity, List<FeedItem> feedItems,
			int listType) {
		this.activity = activity;
		this.feedItems = feedItems;
		this.listType = listType;
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

	public void changeBookmarkValue(int position) {
		if (feedItems.get(position).isBookmarked) {
			feedItems.get(position).isBookmarked = false;
		} else {
			feedItems.get(position).isBookmarked = true;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.listy_items, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.tVposted);
		TextView timestamp = (TextView) convertView.findViewById(R.id.tVTimer);
		TextView titleMsg = (TextView) convertView.findViewById(R.id.tVTitle);
		TextView content = (TextView) convertView.findViewById(R.id.tVContent);
		ib_overflw = (ImageButton) convertView.findViewById(R.id.ivoverFlow);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.ivPhoto);

		final int pos = position;
		ib_overflw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupMenuCompat menu = PopupMenuCompat.newInstance(activity, v);
				if (listType == BOOKMARK_LISTY) {
					menu.inflate(R.menu.only_removebookmark);
				} else if (listType == MY_POST_LISTY) {
					menu.inflate(R.menu.only_myposts);
				} else {
					if (feedItems.get(pos).isBookmarked
							&& feedItems.get(pos).isFollowed) {
						menu.inflate(R.menu.listy_bookmarked_followed);
					} else if (!feedItems.get(pos).isBookmarked
							&& feedItems.get(pos).isFollowed) {
						menu.inflate(R.menu.listy_notbookmarked_followed);
					} else if (feedItems.get(pos).isBookmarked
							&& !feedItems.get(pos).isFollowed) {
						menu.inflate(R.menu.listy_bookmarked_notfollowed);
					} else {
						menu.inflate(R.menu.listy_notbookmarked_notfollowed);
					}

				}

				menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// Toast.makeText(MainListy.this, item.getItemId(),
						// Toast.LENGTH_SHORT).show();
						if (item.getItemId() == R.id.row_mark) {
							Bookmarking maki = new Bookmarking(feedItems
									.get(pos).id + "", SQLadapter.getUserID(),
									true, pos);
							maki.setCancelable(false);
							maki.show(manager, "markk");
						} else if (item.getItemId() == R.id.row_unmark) {
							Bookmarking maki = new Bookmarking(feedItems
									.get(pos).id + "", SQLadapter.getUserID(),
									false, pos);
							maki.setCancelable(false);
							maki.show(manager, "unmarkk");
						} else if (item.getItemId() == R.id.row_block) {
							// Message.message(activity, "blocked");
							Blocking blkpost = new Blocking(
									feedItems.get(pos).id + "", SQLadapter
											.getUserID(), true, pos);
							blkpost.setCancelable(false);
							blkpost.show(manager, "blockkk");

						} else if (item.getItemId() == R.id.row_share) {
							Intent sharingIntent = new Intent(
									Intent.ACTION_SEND);
							sharingIntent.setType("text/plain");
							sharingIntent.putExtra(
									android.content.Intent.EXTRA_SUBJECT,
									"Subject Here");
							sharingIntent.putExtra(
									android.content.Intent.EXTRA_TEXT,
									"http://www.google.fr/");
							activity.startActivity(Intent.createChooser(
									sharingIntent, "Share using"));

						} else if (item.getItemId() == R.id.row_follow) {
							if (SQLadapter.getUserID().equals(
									feedItems.get(pos).user_id + "")) {

								MessageDialog diag = new MessageDialog(
										"Follow Error?",
										"You cannot follow yourself.");
								diag.show(manager, "cannotfoll");
							} else {
								Following follow = new Following(feedItems
										.get(pos).user_id + "", SQLadapter
										.getUserID(),
										!feedItems.get(pos).isFollowed, pos);
								follow.setCancelable(false);
								follow.show(manager, "ffolloww");

							}

						} else if (item.getItemId() == R.id.row_report) {
							ReportPosts postRepo = new ReportPosts(feedItems
									.get(pos).id, feedItems.get(pos).title);
							postRepo.setCancelable(false);
							postRepo.show(manager, "reportPosst");

						} else if (item.getItemId() == R.id.row_deletepost) {
							Delete deletePost = new Delete(
									feedItems.get(pos).id + "", feedItems
											.get(pos).user_id + "", true, pos,
									Delete.TYPE_DELETE_POST);
							deletePost.setCancelable(false);
							deletePost.show(manager, "deleteposts");

						} else if (item.getItemId() == R.id.row_delete_all_comments) {
							Delete deleteAllComments = new Delete(feedItems
									.get(pos).id + "",
									feedItems.get(pos).user_id + "", true, pos,
									Delete.TYPE_DELETE_ALL_COMMENTS);
							deleteAllComments.setCancelable(false);
							deleteAllComments
									.show(manager, "deleteAllCOmments");

						}

						return true;
					}
				});

				menu.show();

			}
		});

		// FeedImageView feedImageView = (FeedImageView) convertView
		// .findViewById(R.id.feedImage1);

		FeedItem item = feedItems.get(position);

		String[] time = item.posted_date.split(" ");
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mDate = null;
		try {
			mDate = sdf.parse(item.posted_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long timeInMilliseconds = mDate.getTime();
		Log.d("asasasasass", timeInMilliseconds + "");

		name.setText("Posted By: " + item.username + " | Date: "
				+ formatTime(time[0]));
		titleMsg.setText(item.title);
		String d = "";
		if (item.desc.length() > 100) {
			d = item.desc.substring(0, 100) + "...";
		} else {
			d = item.desc;
		}
		content.setText(d);
		// timestamp.setText("fdgdg");

		// Converting timestamp into x ago format
		// CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
		// Long.parseLong(item.posted_date),
		// System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		String tmm[] = time[1].split(":");
		// long millis =
		// ((Integer.parseInt(tmm[0])*60*60)+(Integer.parseInt(tmm[1])*60)+Integer.parseInt(tmm[2]))*1000;

		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				timeInMilliseconds, System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		Log.d("Relative Compare", System.currentTimeMillis() + " - "
				+ timeInMilliseconds + " = "
				+ (System.currentTimeMillis() - timeInMilliseconds));

		String what_what = UserFunctions.get_RelativeTime(
				System.currentTimeMillis(), timeInMilliseconds);
		if (what_what.equals("what what")) {
			what_what = formatTime(time[0]);
		}
		timestamp.setText(what_what + " | comments (" + item.no_comm + ")");

		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.date)) {
			// statusMsg.setText(item.getStatus());
			// statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			// statusMsg.setVisibility(View.GONE);
		}

		// Checking for null feed url
		// if (item.getUrl() != null) {
		// url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
		// + item.getUrl() + "</a> "));
		//
		// // Making url clickable
		// url.setMovementMethod(LinkMovementMethod.getInstance());
		// url.setVisibility(View.VISIBLE);
		// } else {
		// // url is null, remove from the view
		// url.setVisibility(View.GONE);
		// }

		profilePic.setImageUrl(UserFunctions.picUrl + item.user_id + ".jpg",
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
