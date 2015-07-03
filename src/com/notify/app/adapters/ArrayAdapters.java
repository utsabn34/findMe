package com.notify.app.adapters;



import com.notify.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArrayAdapters extends ArrayAdapter<String> {

	Context context;
	// List<String> title = new ArrayList<String>();
	// List<String> hours = new ArrayList<String>();
//	ArrayList<SLB> slb = new ArrayList<SLB>();

	public ArrayAdapters(Context context) {
		super(context, R.layout.item_comment);
		this.context = context;
		

		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	class HolderClass {
		TextView tvTitle;
		TextView tvHours;
		LinearLayout lay;

		HolderClass(View view) {
//			tvTitle = (TextView) view.findViewById(R.id.tVcomm);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		HolderClass holder = null;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.item_comment, parent, false);
			holder = new HolderClass(row);
			row.setTag(holder);
		} else {
			holder = (HolderClass) row.getTag();
		}
		
		holder.tvTitle.setText("kjh,"+position);
		

		return row;
	}
}
