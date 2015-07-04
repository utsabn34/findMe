package com.notify.app.adapters;

import android.os.Parcel;
import android.os.Parcelable;

public class Classes {

	public static class LogInfo implements Parcelable {
		public String uname, fname, lname, upass, created, email, dob, country,
				city, lastLogin;
		public String id;

		public LogInfo() {
		}

		public LogInfo(String id, String uname, String fname, String lname,
				String upass, String email, String created, String dob,
				String country, String city, String lastLogin) {
			this.id = id;
			this.uname = uname;
			this.fname = fname;
			this.lname = lname;
			this.upass = upass;
			this.created = created;
			this.email = email;
			this.dob = dob;
			this.country = country;
			this.city = city;
			this.lastLogin = lastLogin;

		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(id);
			dest.writeString(uname);
			dest.writeString(fname);
			dest.writeString(lname);
			dest.writeString(upass);
			dest.writeString(created);
			dest.writeString(email);
			dest.writeString(dob);
			dest.writeString(country);
			dest.writeString(city);
			dest.writeString(lastLogin);

		}
	}

	public static class CommentBlock implements Parcelable {

		public String id, post_id, user_id, comment, date, postedName;

		public CommentBlock(String id, String pid, String uid, String comment,
				String date, String postedName) {
			this.id = id;
			this.post_id = pid;
			this.user_id = uid;
			this.comment = comment;
			this.date = date;
			this.postedName = postedName;

		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(id);
			dest.writeString(post_id);
			dest.writeString(user_id);
			dest.writeString(comment);
			dest.writeString(date);
			dest.writeString(postedName);

		}
	}

	public static class FeedItem implements Parcelable {
		public long id, user_id;
		public String title, desc, posted_date, date, lat, longi, username;
		public boolean isBookmarked, isFollowed;
		public int no_comm;

		public FeedItem() {
		}

		public FeedItem(long id, long user_id, String title, String desc,
				String posted_date, String date, String lat, String longi,
				String username, int no_comm, boolean isBookmarked,
				boolean isFollowed) {
			super();
			this.id = id;
			this.username = username;
			this.title = title;
			this.user_id = user_id;
			this.desc = desc;
			this.posted_date = posted_date;
			this.date = date;
			this.lat = lat;
			this.longi = longi;
			this.no_comm = no_comm;
			this.isBookmarked = isBookmarked;
			this.isFollowed = isFollowed;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeLong(id);
			dest.writeLong(user_id);
			dest.writeString(title);
			dest.writeString(desc);
			dest.writeString(posted_date);
			dest.writeString(date);
			dest.writeString(lat);
			dest.writeString(longi);
			dest.writeString(username);
			dest.writeInt(no_comm);
			dest.writeBooleanArray(new boolean[] { isBookmarked });
			dest.writeBooleanArray(new boolean[] { isFollowed });

		}
	}

	public static class NotiItem implements Parcelable {
		public String user_id;
		public String title, desc, posted_date, time, lat, longi, username;
		public boolean isBookmarked, isFollowed;
		public int no_comm, id, before_time;

		public NotiItem() {
		}

		public NotiItem(int id, String title, String desc, String time,
				String posted_date, String username, String user_id,
				String lat, String longi, int no_comm, int before_time) {

			this.id = id;
			this.username = username;
			this.title = title;
			this.user_id = user_id;
			this.desc = desc;
			this.posted_date = posted_date;
			this.time = time;
			this.lat = lat;
			this.longi = longi;
			this.no_comm = no_comm;
			this.before_time = before_time;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(id);
			dest.writeString(user_id);
			dest.writeString(title);
			dest.writeString(desc);
			dest.writeString(posted_date);
			dest.writeString(time);
			dest.writeString(lat);
			dest.writeString(longi);
			dest.writeString(username);
			dest.writeInt(no_comm);
			dest.writeInt(before_time);

		}
	}
	
	
	public static class Users implements Parcelable {
		public String id,email,firstname,lastname,username;

		public Users() {
		}

		public Users(String id, String email,String firstname,String lastname,String username) {

			this.id = id;
			this.username = username;
			this.email = email;
			this.firstname = firstname;
			this.lastname = lastname;
			this.username = username;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(id);
			dest.writeString(username);
			dest.writeString(email);
			dest.writeString(firstname);
			dest.writeString(lastname);

		}
	}

}
