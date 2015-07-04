package com.notify.app.adapters;


import com.notify.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter implements OnItemClickListener {

	Context con;
	String[] text = { "Muktinath", "Boudha",
			"Tudhikhel", "Manakamana", "Shyambhu",
			"Nepal" };
	int[] images = { R.drawable.ic_lock, R.drawable.ic_lock,
			R.drawable.ic_lock, R.drawable.ic_lock,
			R.drawable.ic_lock, R.drawable.ic_lock};
	View vws;
	// RelativeLayout colorLin;
	int prev = 0;
	int current = 0;
	RelativeLayout lay;
	int decider;
	int typeGrid;

	public GridAdapter(Context context) {
		// TODO Auto-generated constructor stub
		con = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return days.length;
		return 6;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	class ViewHolder {
		TextView tv_main;
		ImageView iv_main;
		RelativeLayout relLay;

		// RelativeLayout colorLin;

		public ViewHolder(View v) {
			// TODO Auto-generated constructor stub
			// tv_day = (TextView) v.findViewById(R.id.tv_day);
			// colorLin = (RelativeLayout) v.findViewById(R.id.colorLin);
			tv_main = (TextView) v.findViewById(R.id.textMainGrid);
			iv_main = (ImageView) v.findViewById(R.id.imagess);
			// tv_second = (TextView) v.findViewById(R.id.tvt2);

		}

	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View row = arg1;
		final int pos = arg0;
		// RelativeLayout relLay = (RelativeLayout)
		// v.findViewById(R.id.relGrid);

		ViewHolder holder = null;
		if (row == null) {
			LayoutInflater inflates = (LayoutInflater) con
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			row = inflates.inflate(R.layout.grid_defination, arg2, false);

			holder = new ViewHolder(row);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		holder.tv_main.setText(text[arg0]);
		holder.iv_main.setImageDrawable(con.getResources().getDrawable(
				images[arg0]));
		return row;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Message.message(con, "asdsdsd");
	}

}
