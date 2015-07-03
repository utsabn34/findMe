package com.notify.app.adapters;

import com.notify.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.notify.app.adapters.Classes.CommentBlock;
import com.notify.app.dialogs.PopupMenuCompat;
import com.notify.app.dialogs.ShowFull.updateComment;
import com.notify.app.volley.AppController;

public class FeedCommentAdapter extends BaseAdapter implements OnClickListener {
	private Context activity;
	private LayoutInflater inflater;
	private List<CommentBlock> commentItems;
	private boolean isDeletable = false;
	// private ImageButton ib_overflw;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	PopupMenuCompat menu;
	updateComment deleteTrigger;
	public static boolean IS_COMMENT_DELETABLE = true;
	public long doublePressToConfirm = 0;

	public FeedCommentAdapter(Context activity,
			List<CommentBlock> commentsItems, boolean isDeletable,
			updateComment deleteTrigger) {
		this.activity = activity;
		this.commentItems = commentsItems;
		this.isDeletable = isDeletable;
		this.deleteTrigger = deleteTrigger;
	}

	@Override
	public int getCount() {
		return commentItems.size();
	}

	@Override
	public Object getItem(int location) {
		return commentItems.get(location);
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
			convertView = inflater.inflate(R.layout.item_comment, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView uname = (TextView) convertView
				.findViewById(R.id.tVdssc_comment);
		final TextView timeAgo = (TextView) convertView
				.findViewById(R.id.tVtimeAgo_comment);

		if (isDeletable) {
			final int pos = position;
			ImageButton butDeleteComment = (ImageButton) convertView
					.findViewById(R.id.ibDeleteComment);
			butDeleteComment.setVisibility(View.VISIBLE);
			butDeleteComment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (IS_COMMENT_DELETABLE) {
						// if(doublePressToConfirm+2000>System.currentTimeMillis()){
						// InputMethodManager imm = (InputMethodManager)
						// activity
						// .getSystemService(Context.INPUT_METHOD_SERVICE);
						// imm.hideSoftInputFromWindow(timeAgo.getWindowToken(),
						// 0);
						// // new
						// //
						// NetCheck().execute(et_comment.getText().toString().trim());
						// //IS_COMMENT_DELETABLE = false;
						// doublePressToConfirm=0;
						// deleteTrigger.deleteSingleComment(
						// Integer.parseInt(commentItems.get(pos).id), pos);
						// }else{
						// Message.message(activity,
						// "Press once more to delete");
						// }
						// doublePressToConfirm=System.currentTimeMillis();

						Message.message(activity, "Hold the button to delete");

					}
				}
			});

			butDeleteComment.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (IS_COMMENT_DELETABLE) {

						InputMethodManager imm = (InputMethodManager) activity
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(timeAgo.getWindowToken(), 0);
						// new
						// NetCheck().execute(et_comment.getText().toString().trim());
						IS_COMMENT_DELETABLE = false;
						deleteTrigger.deleteSingleComment(commentItems.get(pos).id, pos);

					}
					return false;
				}
			});
		}

		// ib_overflw = (ImageButton) convertView.findViewById(R.id.ivoverFlow);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.commImagePics);

		// ib_overflw.setOnClickListener(this);

		// FeedImageView feedImageView = (FeedImageView) convertView
		// .findViewById(R.id.feedImage1);

		CommentBlock item = commentItems.get(position);

		String[] time = item.date.split(" ");
		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mDate = null;
		try {
			mDate = sdf.parse(item.date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timeInMilliseconds = mDate.getTime();
		Log.d("asasasasass", timeInMilliseconds + "");

		// comment.setText(item.comment);

		String tmm[] = time[1].split(":");

		String what_what = UserFunctions.get_RelativeTime(
				System.currentTimeMillis(), timeInMilliseconds);
		if (what_what.equals("what what")) {
			what_what = formatTime(time[0]);
		}
		// uname.setText(item.postedName + "\n" + what_what);
		uname.setText(Html.fromHtml("<b>" + item.postedName
				+ ":</b><font color='#3b3b3b'>" + item.comment + "</font>"));
		timeAgo.setText(what_what);
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

		// user profile pic
		// profilePic.setImageDrawable(activity.getResources().getDrawable(
		// R.drawable.users));

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
