package com.notify.app;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.adapters.MyPagerAdapter;
import com.notify.app.adapters.PagerSlidingTabStrip;
import com.notify.app.dialogs.Bookmarking.BookMarkTrig;
import com.notify.app.dialogs.ChangeProfileDiags.ProfileViewUpdater;
import com.notify.app.dialogs.Delete.DeleteTrig;
import com.notify.app.dialogs.Following.FollowTrig;
import com.notify.app.dialogs.ShowFull.updateComment;
import com.notify.app.fragments.FragmentFUsers;
import com.notify.app.fragments.FragmentMyBookmarks;
import com.notify.app.fragments.FragmentMyPosts;
import com.notify.app.fragments.FragmentSettings;

public class Tabs_Profile extends SherlockFragmentActivity implements
		BookMarkTrig, updateComment, OnClickListener, DeleteTrig, FollowTrig,
		ProfileViewUpdater {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.tags_activity);
		manager = getSupportFragmentManager();
		// FirstPage firstCheck = (FirstPage) manager
		// .findFragmentByTag("firstPage");

		// mPager.setCurrentItem(positions);
		init();

	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getAssets(), "ff.ttf");
		viewTitle = (TextView) findViewById(R.id.tvTitletoptags);
		viewTitle.setTypeface(tf);
		pagerTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
		ib_back = (ImageButton) findViewById(R.id.ibtags_back);
		ib_back.setOnClickListener(this);

		mPager = (ViewPager) findViewById(R.id.main_pager);
		adapter = new MyPagerAdapter(manager);

		mPager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		mPager.setPageMargin(pageMargin);

		mPager.setOffscreenPageLimit(3);
		pagerTabStrip.setViewPager(mPager);
	}

	FragmentManager manager;

	PagerSlidingTabStrip pagerTabStrip;
	ViewPager mPager;
	Context context;
	MyPagerAdapter adapter;
	TextView viewTitle;
	ImageButton ib_back;
	ArrayList<String> unBookmarkedPostId = new ArrayList<String>();
	ArrayList<String> deletedPostId = new ArrayList<String>();
	ArrayList<String> unFollowedUId = new ArrayList<String>();

	@Override
	public void refreshFromBookmark(int position, String pid) {
		unBookmarkedPostId.add(pid);
		FragmentMyBookmarks fragBk = (FragmentMyBookmarks) adapter
				.getRegisteredFragment(mPager.getCurrentItem());
		fragBk.removeItems(position);
	}

	@Override
	public void update_comment(int pos, int no_comm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ibtags_back) {
			refreshingMainPageEvent();
			this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		refreshingMainPageEvent();
		super.onBackPressed();
	}

	private void refreshingMainPageEvent() {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("ubookList", unBookmarkedPostId);
		returnIntent.putExtra("deletesList", deletedPostId);
		returnIntent.putExtra("unffollowList", unFollowedUId);
		setResult(RESULT_OK, returnIntent);
	}

	@Override
	public void refreshFromDelete(int position, String pid) {
		FragmentMyPosts fragMypost = (FragmentMyPosts) adapter
				.getRegisteredFragment(mPager.getCurrentItem());
		fragMypost.removeItems(position);
		deletedPostId.add(pid);
	}

	@Override
	public void refreshFromDeleteComments(int position) {
		FragmentMyPosts fragMypost = (FragmentMyPosts) adapter
				.getRegisteredFragment(mPager.getCurrentItem());
		fragMypost.refresh(position);

	}

	@Override
	public void refreshFromFollow(int position, boolean follow, String fuid) {
		FragmentFUsers usersFollow = (FragmentFUsers) adapter
				.getRegisteredFragment(mPager.getCurrentItem());
		usersFollow.refreshList(position);
		unFollowedUId.add(fuid);
	}

	@Override
	public void updateProfileView(LogInfo info) {
		FragmentSettings sett = (FragmentSettings) adapter
				.getRegisteredFragment(mPager.getCurrentItem());
		sett.refreshView(info);
	}

	@Override
	public void deleteSingleComment(String comment_id,int position) {
		FragmentMyPosts myPosts = (FragmentMyPosts) adapter
				.getRegisteredFragment(mPager.getCurrentItem());
		myPosts.deleteSingleComment(comment_id, position);
	}
}
