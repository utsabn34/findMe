package com.notify.app.dialogs;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

@SuppressLint("NewApi")
class HoneycombHelper extends PopupMenuCompat {
	private PopupMenu menu;

	@SuppressLint("NewApi")
	public HoneycombHelper(Context context, View view) {
		super(context);
		this.menu = new PopupMenu(context, view);
	}

	@Override
	public void dismiss() {
		menu.dismiss();
	}

	@Override
	public Menu getMenu() {
		return menu.getMenu();
	}

	@Override
	public MenuInflater getMenuInflater() {
		return menu.getMenuInflater();
	}

	@Override
	public void inflate(int menuResourceId) {
		getMenuInflater().inflate(menuResourceId, getMenu());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void setOnMenuItemClickListener(
			final OnMenuItemClickListener menuItemClickListener) {
		menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return menuItemClickListener.onMenuItemClick(item);
			}
		});
	}

	@Override
	public void show() {
		menu.show();
	}
}