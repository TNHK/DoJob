package groep13.ehb.dojob.DoJob.src.com.example.notification;

import java.util.Calendar;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class ScheduleService extends Service {

	Context context;
	private final AlarmManager am;

	ScheduleService(Context context) {
		this.context = context;

		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		System.out.println("Alarm Manager : " + am);
	}

	public class ServiceBinder extends Binder {
		ScheduleService getService() {
			return ScheduleService.this;
		}
	}

	public static int generateUniqueId() {
		UUID idOne = UUID.randomUUID();
		String str = "" + idOne;
		int uid = str.hashCode();
		String filterStr = "" + uid;
		str = filterStr.replaceAll("-", "");
		return Integer.parseInt(str);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("ScheduleService", "Received start id " + startId + ": " + intent);
		System.out.println("Start ID :" + startId);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	private final IBinder mBinder = new ServiceBinder();

	@SuppressLint("NewApi")
	public void setAlarm(Calendar calendar) {
		// This starts a new thread to set the alarm
		// You want to push off your tasks onto a new thread to free up the UI
		// to carry on responding

		 Intent intent = new Intent(context, NotifyService.class);
		 intent.putExtra(NotifyService.INTENT_NOTIFY, true);

		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, 0);
		
		int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		
		int day24 = calendar.get(Calendar.DAY_OF_YEAR);
		int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
		int min24 = calendar.get(Calendar.MINUTE);
		
		
			if(hour==hour24){
				if(min>=min24){
					calendar.add(Calendar.DAY_OF_YEAR, 1);
				}
			}else
				if(hour>hour24){
					calendar.add(Calendar.DAY_OF_YEAR, 1);
				}
		
		

		am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, pendingIntent);

	}

	public void cancelDayAlarm() {
		 Intent intent = new Intent(context, NotifyService.class);
		 intent.putExtra(NotifyService.INTENT_NOTIFY, true);

		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, 0);

		am.cancel(pendingIntent);

	}

	/*
	 * @SuppressWarnings("deprecation") public void cancel(Calendar c, String
	 * taskNameString, String id, int daydates) {
	 * 
	 * // Intent intent = new Intent(context, NotifyService.class); Intent
	 * intent = new Intent(context, NotifyService.class);
	 * intent.putExtra(NotifyService.INTENT_NOTIFY, true);
	 * intent.putExtra("taskName", taskNameString); intent.putExtra("id", id);
	 * 
	 * 
	 * // if()
	 * 
	 * PendingIntent pendingIntent = PendingIntent.getService(context,
	 * Integer.parseInt(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);
	 * 
	 * // AlarmManager alarmManager = (AlarmManager) //
	 * getSystemService(ALARM_SERVICE);
	 * 
	 * // alarmManager.cancel(sender);
	 * 
	 * int date = c.get(Calendar.DATE); int month = c.get(Calendar.MONTH + 1);
	 * int hour = c.get(Calendar.HOUR); int min = c.get(Calendar.MINUTE);
	 * 
	 * int d = Calendar.getInstance().get(Calendar.DATE); int m =
	 * Calendar.getInstance().get(Calendar.MONTH + 1); int h =
	 * Calendar.getInstance().get(Calendar.HOUR); int mi =
	 * Calendar.getInstance().get(Calendar.MINUTE);
	 * 
	 * 
	 * // Toast.makeText(context,
	 * "Date ::"+date+", Month : "+month+", Hour : "+hour+", MIN : "+min,
	 * Toast.LENGTH_LONG).show(); // Toast.makeText(context,
	 * "Date ::"+d+", Month : "+m+", Hour : "+h+", MIN : "+mi,
	 * Toast.LENGTH_LONG).show();
	 * 
	 * if (month >= m) { if (d < date) { if (h > hour) {
	 * am.cancel(pendingIntent); } } else if (d > date) {
	 * am.cancel(pendingIntent); }
	 * 
	 * } else if (month > m) { am.cancel(pendingIntent); }
	 * 
	 * 
	 * if(c.after(Calendar.getInstance().getTime().getDate())){
	 * am.cancel(pendingIntent); }
	 * 
	 * 
	 * }
	 */

}