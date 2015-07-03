package com.notify.app.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class WorkAroundMapFragment extends SupportMapFragment {
	private OnTouchListener mListener;

	public GoogleMap mapView;
	private Context context;
	private MapCallback callback;

	public void setMapCallback(MapCallback callback) {
		this.callback = callback;
	}

	public static interface MapCallback {
		public void onMapReady(GoogleMap map);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle savedInstance) {
		View layout = super.onCreateView(layoutInflater, viewGroup,
				savedInstance);

		TouchableWrapper frameLayout = new TouchableWrapper(getActivity());

		frameLayout.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

		((ViewGroup) layout).addView(frameLayout, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		return layout;
	}

	public void setListener(OnTouchListener listener) {
		mListener = listener;
	}

	public interface OnTouchListener {
		public abstract void onTouch();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (callback != null)
			callback.onMapReady(getMap());
	}

	public class TouchableWrapper extends FrameLayout {

		public TouchableWrapper(Context context) {
			super(context);
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mListener.onTouch();
				break;
			case MotionEvent.ACTION_UP:
				mListener.onTouch();
				break;
			}
			return super.dispatchTouchEvent(event);
		}

		// public boolean onTouchEvent(MotionEvent ev) {
		// int action = ev.getAction();
		// switch (action) {
		// case MotionEvent.ACTION_DOWN:
		// // Disallow ScrollView to intercept touch events.
		// this.getParent().requestDisallowInterceptTouchEvent(true);
		// break;
		//
		// case MotionEvent.ACTION_UP:
		// // Allow ScrollView to intercept touch events.
		// this.getParent().requestDisallowInterceptTouchEvent(false);
		// break;
		// }
		//
		// // Handle MapView's touch events.
		// super.onTouchEvent(ev);
		// return true;
		// }

	}

}