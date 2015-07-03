package com.notify.app.adapters;

import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.adapters.Classes.NotiItem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLAdapter extends Activity {

	private MyDbHelp ourHelper;
	Context context;

	public MySQLAdapter(Context context) {
		this.context = context;
		ourHelper = new MyDbHelp(context);
		// try {
		// ourHelper.createDataBase();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	// public void delete_Favs(int id) {
	// SQLiteDatabase db1 = ourHelper.getWritableDatabase();
	// String[] whereArgs = { id + "" };
	// db1.delete(MyDbHelp.DATABASE_TABLE_FAVS,
	// MyDbHelp.KEY_SONGID_FAV + "=?", whereArgs);
	// }
	public void logOut() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		// Delete All Rows
		db.delete(MyDbHelp.DATABASE_TABLE_users, null, null);
		db.close();
	}

	public void addUser(LogInfo info) {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MyDbHelp.KEY_LOG_FIRSTNAME, info.fname); // FirstName
		values.put(MyDbHelp.KEY_LOG_LASTNAME, info.lname); // LastName
		values.put(MyDbHelp.KEY_LOG_EMAIL, info.email); // Email
		values.put(MyDbHelp.KEY_LOG_uname, info.uname); // UserName
		values.put(MyDbHelp.KEY_ID, info.id);
		values.put(MyDbHelp.KEY_LOG_Country, info.country);
		values.put(MyDbHelp.KEY_LOG_City, info.city);
		values.put(MyDbHelp.KEY_LOG_CREATEDAT, info.created);
		values.put(MyDbHelp.KEY_LOG_DOB, info.dob);// Created At
		// Inserting Row
		db.insert(MyDbHelp.DATABASE_TABLE_users, null, values);
		db.close(); // Closing database connection
	}

	public long createAlarm(NotiItem model) {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MyDbHelp.KEY_ID, model.id); // FirstName
		values.put(MyDbHelp.KEY_NOTI_comments, model.no_comm); // LastName
		values.put(MyDbHelp.KEY_NOTI_desc, model.desc); // Email
		values.put(MyDbHelp.KEY_NOTI_lati, model.lat); // UserName
		values.put(MyDbHelp.KEY_NOTI_longi, model.longi);
		values.put(MyDbHelp.KEY_NOTI_posted, model.posted_date);
		values.put(MyDbHelp.KEY_NOTI_time, model.time);
		values.put(MyDbHelp.KEY_NOTI_title, model.title);
		values.put(MyDbHelp.KEY_NOTI_user, model.username);
		values.put(MyDbHelp.KEY_NOTI_userid, model.user_id);
		values.put(MyDbHelp.KEY_NOTI_before, model.before_time);
		// Inserting Row
		long result = db.insert(MyDbHelp.DATABASE_TABLE_noti, null, values);
		// db.close();
		return result;
	}

	public void createAlarms(ArrayList<NotiItem> data) {

		// String initday[] = {"0", "1", "2", "3", "4", "5", "6"};
		ContentValues values = new ContentValues();

		SQLiteDatabase ourDatabase = ourHelper.getWritableDatabase();
		// int count = getRoutine(1000000);
		for (int i = 0; i < data.size(); i++) {

			values.put(MyDbHelp.KEY_ID, data.get(i).id); // FirstName
			values.put(MyDbHelp.KEY_NOTI_comments, data.get(i).no_comm); // LastName
			values.put(MyDbHelp.KEY_NOTI_desc, data.get(i).desc); // Email
			values.put(MyDbHelp.KEY_NOTI_lati, data.get(i).lat); // UserName
			values.put(MyDbHelp.KEY_NOTI_longi, data.get(i).longi);
			values.put(MyDbHelp.KEY_NOTI_posted, data.get(i).posted_date);
			values.put(MyDbHelp.KEY_NOTI_time, data.get(i).time);
			values.put(MyDbHelp.KEY_NOTI_title, data.get(i).title);
			values.put(MyDbHelp.KEY_NOTI_user, data.get(i).username);
			values.put(MyDbHelp.KEY_NOTI_userid, data.get(i).user_id);
			values.put(MyDbHelp.KEY_NOTI_before, data.get(i).before_time);
			ourDatabase.insert(MyDbHelp.DATABASE_TABLE_noti, null, values);
			Message.message(context, "inserted" + i);

		}
	}

	public void addNotification(LogInfo info) {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MyDbHelp.KEY_LOG_FIRSTNAME, info.fname); // FirstName
		values.put(MyDbHelp.KEY_LOG_LASTNAME, info.lname); // LastName
		values.put(MyDbHelp.KEY_LOG_EMAIL, info.email); // Email
		values.put(MyDbHelp.KEY_LOG_uname, info.uname); // UserName
		values.put(MyDbHelp.KEY_ID, info.id);
		values.put(MyDbHelp.KEY_LOG_Country, info.country);
		values.put(MyDbHelp.KEY_LOG_City, info.city);
		values.put(MyDbHelp.KEY_LOG_CREATEDAT, info.created);
		values.put(MyDbHelp.KEY_LOG_DOB, info.dob);// Created At
		// Inserting Row
		db.insert(MyDbHelp.DATABASE_TABLE_users, null, values);
		db.close(); // Closing database connection
	}

	public boolean isLoggedIn() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_LOG_FIRSTNAME };
		cursor = db.query(MyDbHelp.DATABASE_TABLE_users, columns, null, null,
				null, null, null);
		// db.close();
		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public LogInfo getUser() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_ID, MyDbHelp.KEY_LOG_uname,
				MyDbHelp.KEY_LOG_FIRSTNAME, MyDbHelp.KEY_LOG_LASTNAME,
				MyDbHelp.KEY_LOG_EMAIL, MyDbHelp.KEY_LOG_Country,
				MyDbHelp.KEY_LOG_DOB, MyDbHelp.KEY_LOG_City,
				MyDbHelp.KEY_LOG_CREATEDAT };
		LogInfo data = new LogInfo(0 + "", "", "", "", "", "", "", "", "", "",
				"");
		cursor = db.query(MyDbHelp.DATABASE_TABLE_users, columns, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID);
			data.id = cursor.getString(index1);

			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_uname);
			data.uname = cursor.getString(index2);

			int index3 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_FIRSTNAME);
			data.fname = cursor.getString(index3);

			int index4 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_LASTNAME);
			data.lname = cursor.getString(index4);

			int index5 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_EMAIL);
			data.email = cursor.getString(index5);

			int index6 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_Country);
			data.country = cursor.getString(index6);

			int index7 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_City);
			data.city = cursor.getString(index7);

			int index8 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_DOB);
			data.dob = cursor.getString(index8);

			int index9 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_CREATEDAT);
			data.created = cursor.getString(index9);

		}
		return data;

	}

	public ArrayList<NotiItem> getAlarms() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_ID, MyDbHelp.KEY_NOTI_title,
				MyDbHelp.KEY_NOTI_desc, MyDbHelp.KEY_NOTI_time,
				MyDbHelp.KEY_NOTI_posted, MyDbHelp.KEY_NOTI_user,
				MyDbHelp.KEY_NOTI_userid, MyDbHelp.KEY_NOTI_lati,
				MyDbHelp.KEY_NOTI_longi, MyDbHelp.KEY_NOTI_comments,
				MyDbHelp.KEY_NOTI_before };
		ArrayList<NotiItem> data = new ArrayList<NotiItem>();
		cursor = db.query(MyDbHelp.DATABASE_TABLE_noti, columns, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID);
			int id = cursor.getInt(index1);

			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_title);
			String title = cursor.getString(index2);

			int index3 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_desc);
			String desc = cursor.getString(index3);

			int index4 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_time);
			String time = cursor.getString(index4);

			int index5 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_posted);
			String posted_date = cursor.getString(index5);

			int index6 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_user);
			String username = cursor.getString(index6);

			int index7 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_userid);
			String user_id = cursor.getString(index7);

			int index8 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_lati);
			String lat = cursor.getString(index8);

			int index9 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_longi);
			String longi = cursor.getString(index9);
			int index10 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_comments);
			int no_comm = cursor.getInt(index10);
			int index11 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_before);
			int before = cursor.getInt(index11);
			data.add(new NotiItem(id, title, desc, time, posted_date, username,
					user_id, lat, longi, no_comm, before));

		}
		return data;

	}

	public void deleteallRecent() {
		SQLiteDatabase db1 = ourHelper.getWritableDatabase();
		// String[] whereArgs = {"0","1"};
		db1.delete(MyDbHelp.DATABASE_TABLE_songsRec, null, null);
	}

	public void deleteallAlarms() {
		SQLiteDatabase db1 = ourHelper.getWritableDatabase();
		// String[] whereArgs = {"0","1"};
		db1.delete(MyDbHelp.DATABASE_TABLE_noti, null, null);
	}

	public void deleteAlarm(long id) {
		SQLiteDatabase db1 = ourHelper.getWritableDatabase();
		String[] whereArgs = { id + "" };
		db1.delete(MyDbHelp.DATABASE_TABLE_noti, MyDbHelp.KEY_ID + "=?",
				whereArgs);
	}

	public void deletePassedAlarm() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_ID, MyDbHelp.KEY_NOTI_time };
		cursor = db.query(MyDbHelp.DATABASE_TABLE_noti, columns, null, null,
				null, null, null);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		int index1 = -1;
		long id = -1;
		int index2 = -1;
		String time = "";
		Date mDate = null;
		while (cursor.moveToNext()) {
			index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID);
			id = cursor.getInt(index1);

			index2 = cursor.getColumnIndex(MyDbHelp.KEY_NOTI_time);
			time = cursor.getString(index2);
			mDate = null;
			try {
				mDate = sdf.parse(time);
			} catch (ParseException e) {
				Log.d("Parse Error", e + "");
			}
			if (System.currentTimeMillis() > mDate.getTime()) {
				deleteAlarm(id);
				Message.message(context, "Removed " + time);
			} else {
				Message.message(context, "UnRemoved " + time);
			}
		}
	}
	
	public void UpdateUserInfo(LogInfo info) {

		// deleteall();
		// String initday[] = {"0", "1", "2", "3", "4", "5", "6"};
		ContentValues cv = new ContentValues();

		SQLiteDatabase ourDatabase = ourHelper.getWritableDatabase();

		cv.put(MyDbHelp.KEY_LOG_FIRSTNAME, info.fname);
		cv.put(MyDbHelp.KEY_LOG_LASTNAME, info.lname);
		cv.put(MyDbHelp.KEY_LOG_DOB, info.dob);
		cv.put(MyDbHelp.KEY_LOG_Country, info.country);
		cv.put(MyDbHelp.KEY_LOG_City, info.city);
		
		String whereArgs[] = { info.id};
		ourDatabase.update(MyDbHelp.DATABASE_TABLE_users, cv, MyDbHelp.KEY_ID
				+ "= ?", whereArgs);

	}

	// public void UpgradeSQ_SLB(ArrayList<SLB> data) {
	//
	// // String initday[] = {"0", "1", "2", "3", "4", "5", "6"};
	// ContentValues cv = new ContentValues();
	//
	// SQLiteDatabase ourDatabase = ourHelper.getWritableDatabase();
	// // int count = getRoutine(1000000);
	// for (int i = 0; i < data.size(); i++) {
	//
	// cv.put(MyDbHelp.KEY_ID_SLB, data.get(i).id);
	// cv.put(MyDbHelp.KEY_TITLE_SLB, data.get(i).title);
	// cv.put(MyDbHelp.KEY_HRS_SLB, data.get(i).tch_hrs);
	// cv.put(MyDbHelp.KEY_MKS_SLB, data.get(i).marks);
	// cv.put(MyDbHelp.KEY_SLBWHO_SLB, data.get(i).slb_whoid);
	// ourDatabase.insert(MyDbHelp.DATABASE_TABLE_SLB, null, cv);
	//
	// }
	// }

	// public ArrayList<Subjects> SubjectSearch(String keyword) {
	// SQLiteDatabase db = ourHelper.getWritableDatabase();
	// Cursor cursor;
	// String columns[] = { MyDbHelp.KEY_ID_SUB, MyDbHelp.KEY_TITLE_SUB,
	// MyDbHelp.KEY_SUBCODE_SUB };
	//
	// cursor = db.query(MyDbHelp.DATABASE_TABLE_Subject, columns,
	// MyDbHelp.KEY_TITLE_SUB + " like " + "'%" + keyword + "%'",
	// null, null, null, null);
	//
	// ArrayList<Subjects> data = new ArrayList<Subjects>();
	//
	// while (cursor.moveToNext()) {
	// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_SUB);
	// int s_id = cursor.getInt(index1);
	// int index2 = cursor.getColumnIndex(MyDbHelp.KEY_TITLE_SUB);
	// String title = cursor.getString(index2);
	//
	// int index3 = cursor.getColumnIndex(MyDbHelp.KEY_SUBCODE_SUB);
	// String subCode = cursor.getString(index3);
	//
	// // Items_clss fg = new Items_clss(catid, title, add, cty, country,
	// // phone, mobile, emil, fx, pob);
	//
	// data.add(new Subjects(s_id, title, subCode));
	// // buffer.append(id + "&" + group + "&" + g_day + "&" + start_time +
	// // "&" + end_time +"&");
	// }
	//
	// return data;
	//
	// }

	// public void UpgradeSQ_Fav(int id) {
	//
	// // String initday[] = {"0", "1", "2", "3", "4", "5", "6"};
	// ContentValues cv = new ContentValues();
	//
	// SQLiteDatabase ourDatabase = ourHelper.getWritableDatabase();
	// // int count = getRoutine(1000000);
	// for (int i = 0; i < 1; i++) {
	//
	// cv.put(MyDbHelp.KEY_SONGID_FAV, id);
	// ourDatabase.insert(MyDbHelp.DATABASE_TABLE_FAVS, null, cv);
	//
	// }
	// }

	// public void UpgradeSQ_RecentTen(Songs_rec block) {
	//
	// // String initday[] = {"0", "1", "2", "3", "4", "5", "6"};
	// // ContentValues cv = new ContentValues();
	// //
	// // SQLiteDatabase ourDatabase = ourHelper.getWritableDatabase();
	// // int count = getRoutine(1000000);
	// if (DecideRecent(block.song_id)) {
	// delete_Recent(block.song_id);
	// UpgradeSQ_Recent(block);
	// } else {
	// if (getRecentCounts() == 10) {
	// delete_RecentOld();
	// UpgradeSQ_Recent(block);
	// } else {
	// UpgradeSQ_Recent(block);
	// }
	// }
	// // cv.put(MyDbHelp.KEY_REC_songid, block.song_id);
	// // cv.put(MyDbHelp.KEY_REC_title, block.title);
	// // cv.put(MyDbHelp.KEY_REC_isTab, block.isTab);
	// // ourDatabase.insert(MyDbHelp.DATABASE_TABLE_songsRec, null, cv);
	//
	// }

	// public void UpgradeSQ_Recent(Songs_rec block) {
	//
	// // String initday[] = {"0", "1", "2", "3", "4", "5", "6"};
	// ContentValues cv = new ContentValues();
	//
	// SQLiteDatabase ourDatabase = ourHelper.getWritableDatabase();
	//
	// cv.put(MyDbHelp.KEY_REC_songid, block.song_id);
	// cv.put(MyDbHelp.KEY_REC_title, block.title);
	// cv.put(MyDbHelp.KEY_REC_isTab, block.isTab);
	// ourDatabase.insert(MyDbHelp.DATABASE_TABLE_songsRec, null, cv);
	//
	// }
	public String getUserID() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_ID };
		cursor = db.query(MyDbHelp.DATABASE_TABLE_users, columns, null, null,
				null, null, null);
		String id = "";
		while (cursor.moveToNext()) {
			int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID);
			id = cursor.getString(index1);

		}
		return id;

	}

	public String getUserName() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_LOG_uname };
		cursor = db.query(MyDbHelp.DATABASE_TABLE_users, columns, null, null,
				null, null, null);
		String name = "";
		while (cursor.moveToNext()) {
			int index1 = cursor.getColumnIndex(MyDbHelp.KEY_LOG_uname);
			name = cursor.getString(index1);

		}
		return name;

	}

	public boolean DecideRecent(int id) {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		boolean is = false;
		String columns[] = { MyDbHelp.KEY_REC_songid };
		cursor = db.query(MyDbHelp.DATABASE_TABLE_songsRec, columns,
				MyDbHelp.KEY_REC_songid + "= '" + id + "'", null, null, null,
				null);
		while (cursor.moveToNext()) {
			is = true;

		}
		return is;
	}

	public void delete_Recent(int id) {
		SQLiteDatabase db1 = ourHelper.getWritableDatabase();
		String[] whereArgs = { id + "" };
		db1.delete(MyDbHelp.DATABASE_TABLE_songsRec, MyDbHelp.KEY_REC_songid
				+ "=?", whereArgs);
	}

	public void delete_RecentOld() {
		SQLiteDatabase db1 = ourHelper.getWritableDatabase();
		String[] whereArgs = { getOldRecent() + "" };
		db1.delete(MyDbHelp.DATABASE_TABLE_songsRec,
				MyDbHelp.KEY_REC_id + "=?", whereArgs);
	}

	public Integer getOldRecent() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_REC_id };
		cursor = db.query(MyDbHelp.DATABASE_TABLE_songsRec, columns, null,
				null, null, null, MyDbHelp.KEY_REC_id);
		cursor.moveToNext();

		int index1 = cursor.getColumnIndex(MyDbHelp.KEY_REC_id);
		int song_id = cursor.getInt(index1);

		return song_id;

	}

	// public ArrayList<Songs_rec> getAllRecent() {
	// SQLiteDatabase db = ourHelper.getWritableDatabase();
	// Cursor cursor;
	// String columns[] = { MyDbHelp.KEY_REC_id, MyDbHelp.KEY_REC_songid,
	// MyDbHelp.KEY_REC_title, MyDbHelp.KEY_REC_isTab };
	// ArrayList<Songs_rec> data = new ArrayList<Songs_rec>();
	//
	// cursor = db.query(MyDbHelp.DATABASE_TABLE_songsRec, columns, null,
	// null, null, null, MyDbHelp.KEY_REC_id + " DESC");
	// while (cursor.moveToNext()) {
	// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_REC_id);
	// int _id = cursor.getInt(index1);
	// int index2 = cursor.getColumnIndex(MyDbHelp.KEY_REC_songid);
	// int song_id = cursor.getInt(index2);
	// int index3 = cursor.getColumnIndex(MyDbHelp.KEY_REC_title);
	// String title = cursor.getString(index3);
	// int index4 = cursor.getColumnIndex(MyDbHelp.KEY_REC_isTab);
	// String istab = cursor.getString(index4);
	// data.add(new Songs_rec(_id, song_id, title, istab));
	//
	// }
	// return data;
	//
	// }

	// public void UpgradeSQ_Dum(ArrayList<Songs> block) {
	//
	// // String initday[] = {"0", "1", "2", "3", "4", "5", "6"};
	// ContentValues cv = new ContentValues();
	//
	// SQLiteDatabase ourDatabase = ourHelper.getWritableDatabase();
	// // int count = getRoutine(1000000);
	// for (int i = 0; i < block.size(); i++) {
	//
	// cv.put(MyDbHelp.KEY_ID_DUM, block.get(i).id);
	// cv.put(MyDbHelp.KEY_name_DUM, block.get(i).title);
	// ourDatabase.insert(MyDbHelp.DATABASE_TABLE_musician, null, cv);
	//
	// }
	// }

	// public boolean DecideFavs(int id, boolean doit) {
	// SQLiteDatabase db = ourHelper.getWritableDatabase();
	// Cursor cursor;
	// boolean is = false;
	// String columns[] = { MyDbHelp.KEY_SONGID_FAV };
	// cursor = db.query(MyDbHelp.DATABASE_TABLE_FAVS, columns,
	// MyDbHelp.KEY_SONGID_FAV + "= '" + id + "'", null, null, null,
	// null);
	// while (cursor.moveToNext()) {
	// is = true;
	//
	// }
	// if (doit == true) {
	// if (is == true) {
	// delete_Favs(id);
	// } else {
	// UpgradeSQ_Fav(id);
	// }
	// }
	// return is;
	// }

	//

	public ArrayList<String> getAllSBand() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_name_DUM };
		ArrayList<String> data = new ArrayList<String>();
		data.add("Any Singer/Band");
		cursor = db.query(MyDbHelp.DATABASE_TABLE_singband, columns, null,
				null, null, null, "name");
		while (cursor.moveToNext()) {
			// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			// int _id = cursor.getInt(index1);
			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_name_DUM);
			String name = cursor.getString(index2);
			data.add(name);

		}
		return data;

	}

	public ArrayList<Integer> getAllSBandId() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_ID_DUM };
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(-1);
		cursor = db.query(MyDbHelp.DATABASE_TABLE_singband, columns, null,
				null, null, null, "name");
		while (cursor.moveToNext()) {
			// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			// int _id = cursor.getInt(index1);
			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			int id = cursor.getInt(index2);
			data.add(id);

		}
		return data;

	}

	public ArrayList<String> getAllLy() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_name_DUM };
		ArrayList<String> data = new ArrayList<String>();
		data.add("Any Lyricists");
		cursor = db.query(MyDbHelp.DATABASE_TABLE_lyri, columns, null, null,
				null, null, "name");
		while (cursor.moveToNext()) {
			// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			// int _id = cursor.getInt(index1);
			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_name_DUM);
			String name = cursor.getString(index2);
			data.add(name);

		}
		return data;

	}

	public ArrayList<Integer> getAlllyId() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_ID_DUM };
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(-1);
		cursor = db.query(MyDbHelp.DATABASE_TABLE_lyri, columns, null, null,
				null, null, "name");
		while (cursor.moveToNext()) {
			// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			// int _id = cursor.getInt(index1);
			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			int id = cursor.getInt(index2);
			data.add(id);

		}
		return data;

	}

	public ArrayList<String> getAllMus() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_name_DUM };
		ArrayList<String> data = new ArrayList<String>();
		data.add("Any Musicians");
		cursor = db.query(MyDbHelp.DATABASE_TABLE_musician, columns, null,
				null, null, null, "name");
		while (cursor.moveToNext()) {
			// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			// int _id = cursor.getInt(index1);
			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_name_DUM);
			String name = cursor.getString(index2);
			data.add(name);

		}
		return data;

	}

	public ArrayList<Integer> getAllMusId() {
		SQLiteDatabase db = ourHelper.getWritableDatabase();
		Cursor cursor;
		String columns[] = { MyDbHelp.KEY_ID_DUM };
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(-1);
		cursor = db.query(MyDbHelp.DATABASE_TABLE_musician, columns, null,
				null, null, null, "name");
		while (cursor.moveToNext()) {
			// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			// int _id = cursor.getInt(index1);
			int index2 = cursor.getColumnIndex(MyDbHelp.KEY_ID_DUM);
			int id = cursor.getInt(index2);
			data.add(id);

		}
		return data;

	}

	//
	// public SLB_Who getSLBWHOBYSUB_ID(int sub_ids) {
	// SQLiteDatabase db = ourHelper.getWritableDatabase();
	// Cursor cursor;
	// SLB_Who who = null;
	// String columns[] = { MyDbHelp.KEY_ID_SLBWHO, MyDbHelp.KEY_SUBID_SLBWHO,
	// MyDbHelp.KEY_SUBBY_SLBWHO, MyDbHelp.KEY_YEAR_SLBWHO,
	// MyDbHelp.KEY_VERI_SLBWHO };
	//
	// cursor = db.query(MyDbHelp.DATABASE_TABLE_SLBWHO, columns,
	// MyDbHelp.KEY_SUBID_SLBWHO + "= '" + sub_ids + "'", null, null,
	// null, null);
	// while (cursor.moveToNext()) {
	// int index1 = cursor.getColumnIndex(MyDbHelp.KEY_ID_SLBWHO);
	// int _id = cursor.getInt(index1);
	// int index2 = cursor.getColumnIndex(MyDbHelp.KEY_SUBBY_SLBWHO);
	// String subBy = cursor.getString(index2);
	// int index3 = cursor.getColumnIndex(MyDbHelp.KEY_SUBID_SLBWHO);
	// int subID = cursor.getInt(index3);
	// int index4 = cursor.getColumnIndex(MyDbHelp.KEY_YEAR_SLBWHO);
	// int year = cursor.getInt(index4);
	// int index5 = cursor.getColumnIndex(MyDbHelp.KEY_VERI_SLBWHO);
	// int is_veri = cursor.getInt(index5);
	// who = new SLB_Who(_id, subBy, subID, year, is_veri);
	// }
	// return who;
	//
	// }

	static class MyDbHelp extends SQLiteOpenHelper {

		// The Android's default system path of your application database.
		private static String DB_PATH = "/data/data/com.notify.app/databases/";

		private static String DB_NAME = "db_notified";

		private SQLiteDatabase myDataBase;
		private static final int DATABASE_VERSION = 5;
		private final Context myContext;

		public MyDbHelp(Context context) {

			super(context, DB_NAME, null, DATABASE_VERSION);
			this.myContext = context;
		}

		/**
		 * Creates a empty database on the system and rewrites it with your own
		 * database.
		 * */
		public void createDataBase() throws IOException {

			boolean dbExist = checkDataBase();

			if (dbExist) {
				// do nothing - database already exist
			} else {

				// By calling this method and empty database will be created
				// into the default system path
				// of your application so we are gonna be able to overwrite that
				// database with our database.
				this.getReadableDatabase();

				try {

					copyDataBase();

				} catch (IOException e) {

					throw new Error("Error copying database");

				}
			}

		}

		/**
		 * Check if the database already exist to avoid re-copying the file each
		 * time you open the application.
		 * 
		 * @return true if it exists, false if it doesn't
		 */
		private boolean checkDataBase() {

			SQLiteDatabase checkDB = null;

			try {
				String myPath = DB_PATH + DB_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);

			} catch (SQLiteException e) {

				// database does't exist yet.

			}

			if (checkDB != null) {

				checkDB.close();

			}

			return checkDB != null ? true : false;
		}

		/**
		 * Copies your database from your local assets-folder to the just
		 * created empty database in the system folder, from where it can be
		 * accessed and handled. This is done by transfering bytestream.
		 * */
		private void copyDataBase() throws IOException {

			// Open your local db as the input stream
			InputStream myInput = myContext.getAssets().open(DB_NAME);

			// Path to the just created empty db
			String outFileName = DB_PATH + DB_NAME;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

		}

		public void openDataBase() throws SQLException {

			// Open the database
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		}

		@Override
		public synchronized void close() {

			if (myDataBase != null)
				myDataBase.close();

			super.close();

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// // TODO Auto-generated method stub
			// Message.message(myContext, "Hyyyy he he");

			try {
				// db.execSQL("CREATE TABLE " + DATABASE_TABLE_users + " ("
				// + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				// + KEY_LOG_uname + " VARCHAR, " + KEY_LOG_upass
				// + " VARCHAR, " + KEY_LOG_FIRSTNAME + " VARCHAR, "
				// + KEY_LOG_LASTNAME + " VARCHAR, " + KEY_LOG_EMAIL
				// + " VARCHAR, " + KEY_LOG_CREATEDAT + " VARCHAR, "
				// + KEY_LOG_DOB + " VARCHAR, " + KEY_LOG_Country
				// + " VARCHAR, " + KEY_LOG_City + " VARCHAR, "
				// + KEY_LOG_LstLogin + " VARCHAR);"
				//
				// );

				db.execSQL("CREATE TABLE " + DATABASE_TABLE_noti + " ("
						+ KEY_ID + " INTEGER, " + KEY_NOTI_title + " VARCHAR, "
						+ KEY_NOTI_lati + " VARCHAR, " + KEY_NOTI_longi
						+ " VARCHAR, " + KEY_NOTI_desc + " VARCHAR, "
						+ KEY_NOTI_time + " VARCHAR, " + KEY_NOTI_posted
						+ " VARCHAR, " + KEY_NOTI_user + " VARCHAR, "
						+ KEY_NOTI_userid + " VARCHAR, " + KEY_NOTI_before
						+ " INTEGER, " + KEY_NOTI_comments + " INTEGER);"

				);
				db.execSQL("CREATE TABLE " + DATABASE_TABLE_users + " ("
						+ KEY_ID + " VARCHAR, " + KEY_LOG_uname + " VARCHAR, "
						+ KEY_LOG_upass + " VARCHAR, " + KEY_LOG_FIRSTNAME
						+ " VARCHAR, " + KEY_LOG_LASTNAME + " VARCHAR, "
						+ KEY_LOG_EMAIL + " VARCHAR, " + KEY_LOG_CREATEDAT
						+ " VARCHAR, " + KEY_LOG_DOB + " VARCHAR, "
						+ KEY_LOG_Country + " VARCHAR, " + KEY_LOG_City
						+ " VARCHAR, " + KEY_LOG_LstLogin + " VARCHAR);"

				);

				// db.execSQL("CREATE TABLE " + DATABASE_TABLE_users + " ("
				// + KEY_ID + " VARCHAR, "
				// + KEY_LOG_LstLogin + " VARCHAR);"
				//
				// );

				// arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_Subject + " ("
				// + KEY_ID_SUB + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				// + KEY_TITLE_SUB + " TEXT, "
				// + KEY_PM_SUB + " INTEGER, "
				// + KEY_FM_SUB + " INTEGER, " + KEY_HRS_SUB + " INTEGER);"
				//
				// );
				//
				// arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_WHO + " ("
				// + KEY_ID_WHO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				// + KEY_FACULTY_WHO + " TEXT, "
				// + KEY_YEAR_WHO + " TEXT, "
				// + KEY_SEC_WHO + " TEXT, " + KEY_SUBMIT_WHO + " TEXT);"
				//
				// );
				//
				// arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_CLG + " ("
				// + KEY_ID_CLG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				// + KEY_RID_CLG + " Integer);"
				//
				// );
				//
				// arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_SLB + " ("
				// + KEY_ID_SLB + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				// + KEY_SUBID_SLB + " INTEGER, "
				// + KEY_title_SLB + " TEXT, " + KEY_HRS_SLB + " INTEGER);"
				//
				// );

				Message.message(myContext, "Database Created");

				// set.setValues();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// Message.message(context, ""+e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			try {
				db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

				Message.message(myContext, "Database Altered");
				onCreate(db);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// Message.message(context, ""+e);
			}
		}

		// Add your public helper methods to access and get content from the
		// database.
		// You could return cursors by doing "return myDataBase.query(....)" so
		// it'd be easy
		// to you to create adapters for your views.

		public static final String KEY_ID = "_id";

		public static final String KEY_LOG_uname = "uname";
		public static final String KEY_LOG_upass = "upass";
		private static String KEY_LOG_FIRSTNAME = "fname";
		private static String KEY_LOG_LASTNAME = "lname";
		private static String KEY_LOG_EMAIL = "email";
		private static String KEY_LOG_CREATEDAT = "created_at";
		private static String KEY_LOG_DOB = "dob";
		private static String KEY_LOG_Country = "country";
		private static String KEY_LOG_City = "city";
		private static String KEY_LOG_LstLogin = "last_login";

		public static final String KEY_NOTI_title = "title";
		public static final String KEY_NOTI_desc = "desc";
		private static final String KEY_NOTI_time = "time";
		public static final String KEY_NOTI_user = "uname";
		public static final String KEY_NOTI_userid = "uid";
		public static final String KEY_NOTI_lati = "lati";
		public static final String KEY_NOTI_longi = "longi";
		public static final String KEY_NOTI_posted = "posted";
		private static final String KEY_NOTI_comments = "nocomm";
		private static final String KEY_NOTI_before = "before_time";

		private static String KEY_BOOK_postid = "post_id";

		public static final String KEY_ID_DUM = "_id";
		public static final String KEY_name_DUM = "name";

		public static final String KEY_REC_id = "_id";
		public static final String KEY_REC_songid = "song_id";
		public static final String KEY_REC_title = "title";
		public static final String KEY_REC_isTab = "istab";

		// public static final String KEY_DAY = "day";
		// public static final String KEY_STIME = "stime";
		// public static final String KEY_ETIME = "etime";

		private static final String DATABASE_NAME = "db_notified";

		private static final String DATABASE_TABLE_users = "tbl_users";
		private static final String DATABASE_TABLE_noti = "tbl_noti";
		private static final String DATABASE_TABLE_bookmarked = "tbl_bookmarked";

		private static final String DATABASE_TABLE_lyri = "tbl_lyricists";
		private static final String DATABASE_TABLE_singband = "tbl_singer_band";
		private static final String DATABASE_TABLE_musician = "tbl_musician";
		private static final String DATABASE_TABLE_songsRec = "tbl_songs_recent";
		// private static final String DATABASE_TABLE_SLB = "tbl_slb";

		// private static String DB_PATH =
		// "/data/data/com.bijnass.demos/databases/";
		// private final Context context;

		// private MySQLAdapter set;

		// public MyDbHelp(Context context) {
		// super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// // TODO Auto-generated constructor stub
		// this.context = context;
		// //Message.message(context, "Constructer called");
		// }
		// @Override
		// public void onCreate(SQLiteDatabase arg0) {
		// // TODO Auto-generated method stub
		// Message.message(myContext, "Hyyyy he he");
		//
		// try {
		// // // Create a Category table
		// // arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_Routine + " ("
		// // + KEY_ID_R + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		// // + KEY_DAY_R + " INTEGER, "
		// // + KEY_PER_R + " INTEGER, "
		// // + KEY_SUBID_R + " INTEGER, "
		// // + KEY_TEAHR_R + " TEXT, "
		// // + KEY_STIME_R + " TEXT, "
		// // + KEY_ETIME_R + " TEXT, " + KEY_WHOID_R + " INTEGER);"
		// //
		// // );
		// //
		// // arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_Subject + " ("
		// // + KEY_ID_SUB + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		// // + KEY_TITLE_SUB + " TEXT, "
		// // + KEY_PM_SUB + " INTEGER, "
		// // + KEY_FM_SUB + " INTEGER, " + KEY_HRS_SUB + " INTEGER);"
		// //
		// // );
		// //
		// // arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_WHO + " ("
		// // + KEY_ID_WHO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		// // + KEY_FACULTY_WHO + " TEXT, "
		// // + KEY_YEAR_WHO + " TEXT, "
		// // + KEY_SEC_WHO + " TEXT, " + KEY_SUBMIT_WHO + " TEXT);"
		// //
		// // );
		// //
		// // arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_CLG + " ("
		// // + KEY_ID_CLG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		// // + KEY_RID_CLG + " Integer);"
		// //
		// // );
		// //
		// // arg0.execSQL("CREATE TABLE " + DATABASE_TABLE_SLB + " ("
		// // + KEY_ID_SLB + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		// // + KEY_SUBID_SLB + " INTEGER, "
		// // + KEY_title_SLB + " TEXT, " + KEY_HRS_SLB + " INTEGER);"
		// //
		// // );
		//
		// Message.message(myContext, "Database Created");
		//
		// // set.setValues();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // Message.message(context, ""+e);
		// }
		//
		// }

		// @Override
		// public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// // TODO Auto-generated method stub
		// try {
		// arg0.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_Routine);
		// arg0.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_Subject);
		// arg0.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_WHO);
		//
		// // Message.message(context, "Database Altered");
		// onCreate(arg0);
		//
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // Message.message(context, ""+e);
		// }
		// }

	}

}
