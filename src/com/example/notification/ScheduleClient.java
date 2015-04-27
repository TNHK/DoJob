package groep13.ehb.dojob.DoJob.src.com.example.notification;

import java.util.Calendar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ScheduleClient 
{
	private ScheduleService mBoundService ;
	private Context mContext;
	private boolean mIsBound;
	
	
	public ScheduleClient(Context context) {
	
		mContext = context;
		mBoundService = new ScheduleService(mContext );
	}
	
	public void doBindService()
	{
		mContext.bindService(new Intent(mContext, ScheduleService.class),mConnection,Context.BIND_AUTO_CREATE);
		mIsBound=true;
	}
	private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with our service has been established, 
            // giving us the service object we can use to interact with our service.
            mBoundService = ((ScheduleService.ServiceBinder) service).getService();
           // if ()
        }
 
        public void onServiceDisconnected(ComponentName className) {
         //   mBoundService = null;
        }
    };
 
    /**
     * Tell our service to set an alarm for the given date
     * @param c a date to set the notification for
     * @param taskNameString 
     * @param string 
     */
    public void setAlarmForNotification(Calendar calendar){
    	//mConnection.onServiceConnected(this, new IBinder());
    	//  mBoundService = ((ScheduleService.ServiceBinder) new ServiceBinder()).getService();
    	int requestCode;
    	if(mConnection == null){
    		System.out.println("mconnection is null");
    	}
    	if(mBoundService == null){
    		System.out.println("mBoundService is null");
    	}
    	
    	mBoundService.setAlarm(calendar);
    	
    }
     
    /**
     * When you have finished with the service call this method to stop it 
     * releasing your connection and resources
     */
    
    
  /*  public void raiseAlarmByDay()
    {
    	if(mConnection == null){
    		System.out.println("mconnection is null");
    	}
    	if(mBoundService == null){
    		System.out.println("mBoundService is null");
    	}
    	
    	mBoundService.setAlarm(c, taskNameString, id, daydates);
    	
    }
    */
    
    public void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }

	

	public void cancelDayAlarm() {
		if(mConnection == null){
    		System.out.println("mconnection is null");
    	}
    	if(mBoundService == null){
    		System.out.println("mBoundService is null");
    	}
    	
        mBoundService.cancelDayAlarm();
		
	}

	

}