package com.notify.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.notify.app.adapters.GridAdapter;
import com.notify.app.adapters.Message;

public class BookRoom extends SherlockFragmentActivity implements OnItemClickListener{
	
	GridView grid;
	String[] text = { "Muktinath", "Boudha",
			"Tudhikhel", "Manakamana", "Shyambhu",
			"Nepal" };
	GridAdapter gridAdapter; 
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.book_room);
		grid = (GridView) findViewById(R.id.gridlist);
		gridAdapter = new GridAdapter(this);
		grid.setAdapter(gridAdapter);
		grid.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Message.message(this, text[position] + " selected.");
		
	}
}
