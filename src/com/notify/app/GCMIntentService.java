package com.notify.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.notify.app.adapters.ServerUtilities;
import com.notify.app.adapters.UserFunctions;

import static com.notify.app.adapters.UserFunctions.SENDER_ID;
import static com.notify.app.adapters.UserFunctions.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, "Your device registred with GCM");
		// Log.d("NAME", MainActivity.name);
		// ServerUtilities.register(context, "name", "email", registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("price");

		String messagecomm = intent.getExtras().getString("comm");

		final SharedPreferences prefs = getSharedPreferences(
				UserFunctions.fileName, Context.MODE_PRIVATE);
		String isSet = prefs.getString(UserFunctions.fileNoti, "");
		if (!isSet.equals("no")) {
			displayMessage(context, message);
			if (message != null) {
				generateNotification(context, message, ":New Post");
			} else {
				generateNotification(context, messagecomm, ":New Comment");
			}

		}

	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message, "");
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	public static void generateNotification(Context context, String message,
			String type) {
		int icon = R.drawable.ics_mycomments_green;
		// long when = System.currentTimeMillis();
		// NotificationManager notificationManager = (NotificationManager)
		// context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification notification = new Notification(icon, message, when);
		//
		 String title = context.getString(R.string.app_name)+type;
		//
		// Intent notificationIntent = new Intent(context, MainListy.class);
		// // set intent so it does not start a new activity
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// PendingIntent intent =
		// PendingIntent.getActivity(context, 0, notificationIntent, 0);
		// notification.setLatestEventInfo(context, title, message, intent);
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//
		// // Play default notification sound
		// notification.defaults |= Notification.DEFAULT_SOUND;
		//
		// // Vibrate if vibrate is enabled
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		// notificationManager.notify(0, notification);
		Notification myNotification;
		// Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
		Intent notificationIntent = new Intent(context, BookRoom.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		NotificationManager notificationManager;

		myNotification = new NotificationCompat.Builder(context)
				.setContentTitle(title)
				.setContentText(message)
				.setTicker(message).setWhen(System.currentTimeMillis())
				.setContentIntent(pendingIntent)
				.setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_VIBRATE).setAutoCancel(true)
				.setSmallIcon(icon).build();
		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
	}

	private static final int MY_NOTIFICATION_ID = 1;

}
