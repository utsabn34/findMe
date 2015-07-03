package com.notify.app.adapters;

import com.notify.app.R;
import com.notify.app.adapters.PagerSlidingTabStrip.IconTabProvider;
import com.notify.app.fragments.FragmentFUsers;
import com.notify.app.fragments.FragmentMyBookmarks;
import com.notify.app.fragments.FragmentMyPosts;
import com.notify.app.fragments.FragmentMyComments;
import com.notify.app.fragments.FragmentSettings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class MyPagerAdapter extends FragmentStatePagerAdapter implements
		IconTabProvider {
	
	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	private final int[] ICONS = { R.drawable.tabs_settings,
			R.drawable.tabs_bookmarks,R.drawable.tabs_user_followed,
			R.drawable.tabs_myprofs, R.drawable.tabs_comments};

//	@Override
//	public CharSequence getPageTitle(int position) {
//		if (position == 0)
//			return "Sunday";
//		else if (position == 1)
//			return "Monday";
//		else if (position == 2)
//			return "Tuesday";
//		else if (position == 3)
//			return "Wednesday";
//		else if (position == 4)
//			return "Thursday";
//		else if (position == 5)
//			return "Friday";
//		else if (position == 6)
//			return "Saturday";
//		else
//			return null;
//	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if (position == 0) {
			fragment = new FragmentSettings();
			
		}
		else if (position == 1) {
			fragment = new FragmentMyBookmarks();
			
			
		}
		else if (position == 2) {
			fragment = new FragmentFUsers();
			
		}
		else if (position == 3) {
			fragment = new FragmentMyPosts();
			
		}
		else if (position == 4) {
			fragment = new FragmentMyComments();
			
		}
		/*
		 * if(position==5) { fragment=new FragmentTab1(); } if(position==6) {
		 * fragment=new FragmentTab1(); }
		 */

		return fragment;
	}

	@Override
	public int getPageIconResId(int position) {
		// TODO Auto-generated method stub
		return ICONS[position];
	}
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}