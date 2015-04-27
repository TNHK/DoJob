package groep13.ehb.dojob.DoJob.src.com.example.notification;

import java.io.IOException;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * This service is started when an Alarm has been raised
 * 
 * We pop a notification into the status bar for the user to click on When the
 * user clicks the notification a new activity is opened
 * 
 * @author paul.blundell
 */
public class NotifyService extends Service {

	Context context;
	public static final String IDES = "IDES";
	public String taskName = "";
	public String id = "";
	Bundle bundle;
	
//	String[] str;
	

	public class ServiceBinder extends Binder {
		NotifyService getService() {
			return NotifyService.this;
		}
	}

	// Unique id to identify the notification.
	private static int NOTIFICATION = 1;
	// Name of an intent extra we can use to identify if this service was
	// started to create a notification
	public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";

	// The system notification manager
	private NotificationManager mNM;

	@Override
	public void onCreate() {
		Log.i("NotifyService", "onCreate()");
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//		str = getResources().getStringArray(R.array.postalAddressTypes);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);

		bundle = intent.getExtras();

		// If this service was started by out AlarmTask intent then we want to
		// show our notification
		if (intent.getBooleanExtra(INTENT_NOTIFY, false))
			try {
				showNotification(bundle, intent);
			} catch (IOException e) {

				e.printStackTrace();
			}

		// We don't care if this service is stopped as we have already delivered
		// our notification
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients
	private final IBinder mBinder = new ServiceBinder();

	/**
	 * Creates a notification and shows it in the OS drag-down status bar
	 * 
	 * @param bundle
	 * @throws IOException
	 */
	private void showNotification(Bundle bundle, Intent intent)
			throws IOException {

		// This is the 'title' of the notification
		CharSequence title = "Alarm!!";
		// This is the icon to use on the notification
		int icon = R.drawable.btn_star_big_on;
		// This is the scrolling text of the notification
//		CharSequence text = String.valueOf(str);
		CharSequence text = "Vertrekoptijdnaar het gesprek ";
		// What time to show on the notification
		long time = System.currentTimeMillis();

		Notification notification = new Notification(icon, text, time);
		// Notification notification = new Notification();
		notification.defaults |= Notification.DEFAULT_SOUND;

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		
		// Set the info for the views that show in the notification panel.

		notification.setLatestEventInfo(this, title, text, null);

		// Clear the notification when it is pressed
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Send the notification to the system.
		mNM.notify(NOTIFICATION++, notification);

		// Stop the service when we are finished
		stopSelf();

	}

}