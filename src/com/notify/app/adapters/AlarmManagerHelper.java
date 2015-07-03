package com.notify.app.adapters;

import java.util.Calendar;
import java.util.List;

import com.notify.app.AlarmService;
import com.notify.app.adapters.Classes.NotiItem;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerHelper extends BroadcastReceiver {

	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String DESC = "name";
	public static final String TIME = "name";
	public static final String POSTED = "name";
	public static final String USER = "name";
	public static final String USERID = "name";
	public static final String LAT = "name";
	public static final String LONGI = "name";
	public static final String NOCOMM = "name";
	public static final String TIME_HOUR = "timeHour";
	public static final String TIME_MINUTE = "timeMinute";
	public static final String TONE = "alarmTone";

	@Override
	public void onReceive(Context context, Intent intent) {
		setAlarms(context);
		Message.message(context, "Entered manager");
	}

	public static void setAlarms(Context context) {
		cancelAlarms(context);
		
		MySQLAdapter dbHelper = new MySQLAdapter(context);
		dbHelper.deletePassedAlarm();
		List<NotiItem> alarms = dbHelper.getAlarms();
		Message.message(context, "Alarm  No: "+ alarms.size());
		//int count = 0;
		for (NotiItem alarm : alarms) {
			if (true) {
				
//				Message.message(context, alarm.time+"Alarm   "+ ++count);
				PendingIntent pIntent = createPendingIntent(context, alarm);
				String dateTime[] = alarm.time.split(" ");
				Calendar calendar = Calendar.getInstance();

				calendar.set(Calendar.YEAR,
						Integer.parseInt(dateTime[0].split("-")[0]));
				calendar.set(Calendar.MONTH,
						Integer.parseInt(dateTime[0].split("-")[1]) - 1);
				calendar.set(Calendar.DAY_OF_MONTH,
						Integer.parseInt(dateTime[0].split("-")[2]));
				calendar.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(dateTime[1].split(":")[0]));
				calendar.set(Calendar.MINUTE,
						Integer.parseInt(dateTime[1].split(":")[1]));
				calendar.set(Calendar.SECOND, 0);

				// Find next time to set
//				final int nowDay = Calendar.getInstance().get(
//						Calendar.DAY_OF_WEEK);
//				final int nowHour = Calendar.getInstance().get(
//						Calendar.HOUR_OF_DAY);
//				final int nowMinute = Calendar.getInstance().get(
//						Calendar.MINUTE);

				setAlarm(context, calendar, pIntent);

			}
		}
	}

	@SuppressLint("NewApi")
	private static void setAlarm(Context context, Calendar calendar,
			PendingIntent pIntent) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pIntent);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pIntent);
		}
	}

	public static void cancelAlarms(Context context) {
		MySQLAdapter dbHelper = new MySQLAdapter(context);

		List<NotiItem> alarms = dbHelper.getAlarms();

		if (alarms != null) {
			for (NotiItem alarm : alarms) {
				PendingIntent pIntent = createPendingIntent(context, alarm);

				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				alarmManager.cancel(pIntent);
			}
		}

	}

	private static PendingIntent createPendingIntent(Context context,
			NotiItem model) {
		Intent intent = new Intent(context, AlarmService.class);
		intent.putExtra(ID, model.id);
		intent.putExtra(TITLE, model.title);
		intent.putExtra(DESC, model.desc);
		// intent.putExtra(TIME_HOUR, model.timeHour);
		// intent.putExtra(TIME_MINUTE, model.timeMinute);
		// intent.putExtra(TONE, model.alarmTone.toString());

		return PendingIntent.getService(context, model.id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
