package com.notify.app;

import com.notify.app.adapters.Message;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

	public static String TAG = AlarmService.class.getSimpleName();

	public static final String ID = "id";
	public static final String TITLE = "title";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message.message(getBaseContext(), startId + "    what"
				+ intent.getExtras().getString(TITLE));
		GCMIntentService.generateNotification(getBaseContext(), intent
				.getExtras().getString(TITLE), ":Alert");
		// Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
		// alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// alarmIntent.putExtras(intent);
		// getApplication().startActivity(alarmIntent);

		//AlarmManagerHelper.setAlarms(this);

		return super.onStartCommand(intent, flags, startId);
	}

}