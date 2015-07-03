package com.notify.app.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.util.Patterns;

public class Utils {

	private static boolean isValidUrl(String url) {
	    Pattern p = Patterns.WEB_URL;
	    Matcher m = p.matcher(url);
	    if(m.matches())
	        return true;
	    else
	    return false;
	}
	
	public static boolean isPassed(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		Date mDate = null;
		try {
			mDate = sdf.parse(time);
		} catch (ParseException e) {
			Log.d("Parse Error", e + "");
		}
		if (System.currentTimeMillis() >= mDate.getTime()) {
			return true;
		}else{
			return false;
		}
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
		return (tt[2].charAt(0) == '0' ? tt[2].charAt(1) + "" : tt[2]) + posts
				+ " " + month[Integer.parseInt(tt[1]) - 1] + " " + tt[0];

	}

	static String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sept", "Oct", "Nov", "Dec" };
}
