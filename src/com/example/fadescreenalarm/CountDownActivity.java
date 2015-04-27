package groep13.ehb.dojob.DoJob.src.com.example.fadescreenalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import groep13.ehb.dojob.DoJob.src.com.example.dao.TestAdapter;
import groep13.ehb.dojob.DoJob.src.com.example.dto.AlarmDTO;
import groep13.ehb.dojob.DoJob.src.com.example.notification.ScheduleClient;

public class CountDownActivity extends ActionBarActivity {
	private GestureDetectorCompat gestureDetectorCompat;
	ArrayList<AlarmDTO> runningAlarmList = new ArrayList<AlarmDTO>();
	private CountDownTimer countDownTimer;
	private TextView leftTime;
	private ProgressBar progressBar;
	private int maxSeconds;
	private TestAdapter mDbHelper;
	private AlarmDTO closestTask;
	private AlarmDTO currentTask = null;
	private TextView jobName;
	boolean isJobExistGlobal = false;
	private ImageView deleteImageView;
	private ImageView addAlarmImageView;
	private LinearLayout touchLayout;
	CountDownActivity countDownActivity = this;
	boolean isTimerCancel = false;
	int current = 0; ;
	int noOfRunningTask;
	private CheckBox notificationCheckBox;
	ScheduleClient scheduleClient;
	private LinearLayout jobNo;
	private ImageView editAlarm;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count_down);
	
		initVar();

	
			setCountDown();

	}

	private void setCountDown() {
		if (isJobExist()) {
			// System.out.println("job exist");

			runningAlarmList = getRunningTask();
			if (runningAlarmList.isEmpty()) {
				// showToast("no running job found");
				showToast("geen stromend baan gevonden");
			} else {
				closestTask = getMinimumMinTask(runningAlarmList);
				currentTask = closestTask;
				// progressBar.setMax(getSeconds(closestTask.getMaxLeftMinute()));
				// progressBar.setMax(closestTask.getMaxLeftMinute());
				progressBar.setMax(closestTask.getMaxLeftSecond());
			}
			// getClosestTask();
		} else {
			// jobName.setText("no job");
			jobName.setText("geen baan");
			// showToast("job not exist");
			showToast("baan niet bestaat");
			// isJobExistGlobal = false;
		}


		int seconds;

		if (closestTask != null) {
			seconds = closestTask.getSecondsLeft();
			jobName.setText(closestTask.getJobName());
			startTimer(seconds);
		} else {
			seconds = 0;
		}
		
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		//setCountDown();

	}

	/**
	 * @param maxLeftMinute
	 * @return
	 */
	private int getSeconds(int maxLeftMinute) {

		return maxLeftMinute * 60;
	}

	private void initVar() {
		gestureDetectorCompat = new GestureDetectorCompat(this,
		
				new MyGestureListener());
		leftTime = (TextView) findViewById(R.id.left_time);
		jobName = (TextView) findViewById(R.id.job_name);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		deleteImageView = (ImageView) findViewById(R.id.delete_image_view);
		deleteImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*startActivity(new Intent(CountDownActivity.this,
						DeleteAlarmActivity.class));
				finish();*/
				if (currentTask != null) {
					deleteJob(Integer.parseInt(currentTask.getId()));
				} else {
					showToast("not job to delete");
				}
			}
		});
		addAlarmImageView = (ImageView) findViewById(R.id.add_alarm_image_view);
		addAlarmImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(CountDownActivity.this,
						AddAlarmActivity.class));
				finish();

			}
		});
		touchLayout = (LinearLayout) findViewById(R.id.touch_layout);
		touchLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				
				//if (countDownActivity.gestureDetectorCompat.onTouchEvent(event)) {
					countDownActivity.onTouchEvent(event);
					countDownActivity.gestureDetectorCompat.onTouchEvent(event);
					return true;
				//} 
				//return super.onTouchEvent(event);
				//return true;
				
			}
		});
		

		mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		
		editAlarm = (ImageView) findViewById(R.id.edit_alarm);
		editAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (currentTask != null) {
					Intent intent = new Intent(CountDownActivity.this,EditAlarmActivity.class);
					intent.setAction("edit");
					intent.putExtra("id", currentTask.getId());
					startActivity(intent);	
					finish();
				} else {
					showToast("not job to edit");
				}
			}
		});
		scheduleClient = new ScheduleClient(this);
		notificationCheckBox = (CheckBox) findViewById(R.id.notification_checkbox);
		notificationCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				if (notificationCheckBox.isChecked()) {
					int deviceTimeFormat;
					deviceTimeFormat = Integer.parseInt(Settings.System.getString(
							getContentResolver(), Settings.System.TIME_12_24));
					Calendar calendar = Calendar.getInstance();
					/*if (deviceTimeFormat == 24) {
						calendar.set(Calendar.HOUR_OF_DAY, 12);
					} else {*/
						calendar.set(Calendar.HOUR_OF_DAY, 12);
//						calendar.set(Calendar.AM_PM,Calendar.PM );
//					}
					calendar.set(Calendar.MINUTE, 00);
					calendar.set(Calendar.SECOND, 00);
					calendar.set(Calendar.DAY_OF_YEAR,Calendar.getInstance().get(Calendar.DAY_OF_YEAR) );
					scheduleClient.doBindService();
					
					scheduleClient.setAlarmForNotification(calendar);
					scheduleClient.doUnbindService();
				}else{
					scheduleClient.doBindService();
					scheduleClient.cancelDayAlarm();
					scheduleClient.doUnbindService();
				}
			}
		});
		jobNo = (LinearLayout) findViewById(R.id.job_no);
		runningAlarmList = getRunningTask();
		ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();;
		
		if (!runningAlarmList.isEmpty() && runningAlarmList != null ) {
			// showToast("no running job found");
			for (int i = 0; i < runningAlarmList.size();i++) {
				
				ImageView imageView = new ImageView(this);
		        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		        params.topMargin=12;
		        params.bottomMargin=12;
		        params.leftMargin = 8;
		        params.rightMargin = 8;
		        params.height=12;
		        params.width=12;
		        imageView.setId(i);
		        imageView.setLayoutParams(params);
		        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		        if (i == current) {
		        	 imageView.setImageResource(R.drawable.current_job);
		        	 params.height=20;
				        params.width=20;
		        } else {
		        	 imageView.setImageResource(R.drawable.job);
		        }
		        
		        imageViewList.add(i,imageView);
		        //jobNo.addView(imageView);
			}
			int i = 0;
			for (Iterator<ImageView> iterator = imageViewList.iterator(); iterator.hasNext(); ) {
				jobNo.addView(iterator.next());
			}
		}
		
	}

	private ArrayList<AlarmDTO> getRunningTask() {
		ArrayList<AlarmDTO> runningAlarmList = new ArrayList<AlarmDTO>();

		mDbHelper.open();
		runningAlarmList = mDbHelper.getRunningTask();
		// sort it
		Collections.sort(runningAlarmList, new AlarmComp());
		mDbHelper.close();
		return runningAlarmList;
	}

	/**
	 * @param runningAlarmList
	 * @return
	 */
	private AlarmDTO getMinimumMinTask(ArrayList<AlarmDTO> runningAlarmList) {
		AlarmDTO alarmDTO = Collections.min(runningAlarmList, new AlarmComp());
		return alarmDTO;
	}

	/**
	 * @param message
	 * 
	 */
	private void showToast(String message) {
		Toast.makeText(CountDownActivity.this, message, Toast.LENGTH_SHORT)
				.show();
	}

	private boolean isJobExist() {
		boolean exist = false;
		mDbHelper.open();
		exist = mDbHelper.isJobExist();
		mDbHelper.close();
		return exist;
	}

	private void getClosestTask() {
		mDbHelper.open();
		closestTask = mDbHelper.getClosestTask();
		mDbHelper.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		default: break;
		}
		return super.onOptionsItemSelected(item);

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	// private void startTimer(final int minuti) {
	private void startTimer(final int second) {
		countDownTimer = new CountDownTimer(second * 1000, 1000) {
			// 500 means, onTick function will be called at every 500
			// milliseconds

			@Override
			public void onTick(long leftTimeInMilliseconds) {
				long seconds = leftTimeInMilliseconds / 1000;
				int sec = (int) (leftTimeInMilliseconds / 1000);
				
				progressBar.setProgress(sec);

				leftTime.setText(String.format("%02d", seconds / (60 * 60))
						+ ":"
						+ String.format("%02d", (seconds % (60 * 60)) / 60)
						+ ":" + String.format("%02d", seconds % 60));
				// format the textview to show the easily readable format

			}

			@Override
			public void onFinish() {
				if(currentTask != null) {
					deleteJob(Integer.parseInt(currentTask.getId()));
				}
					
			}

		}.start();

	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		// handle 'swipe left' action only

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {
			// swipe right to  left
			if (event2.getX() < event1.getX()) { 
				setCountDownNext(runningAlarmList);
				
			}
			// swipe left to right
			if (event2.getX() > event1.getX()) {
				
				setCountDownPrevious(runningAlarmList);
				
			}

			return true;
		}

		/**
		 * @param runningAlarmList
		 */
		private void setCountDownPrevious(ArrayList<AlarmDTO> runningAlarmList) {
			if (!runningAlarmList.isEmpty()) {
				current = current -1;
				if (current  < runningAlarmList.size() && current >= 0) {
				
				currentTask = runningAlarmList.get(current);
					int seconds;

					if (currentTask != null) {
						progressBar.setMax(currentTask.getMaxLeftSecond());
						seconds = currentTask.getSecondsLeft();
						jobName.setText(currentTask.getJobName());
					} else {
						seconds = 0;
					}
					if (countDownTimer != null) {
						countDownTimer.cancel();
					} 
					// change dots
					ImageView previous = (ImageView) jobNo.getChildAt(current);
					if (previous != null) {
						previous.setImageResource(R.drawable.current_job);
						android.view.ViewGroup.LayoutParams previousLayoutParams = previous.getLayoutParams();
						previousLayoutParams.width = 20;
						previousLayoutParams.height = 20;
						previous.setLayoutParams(previousLayoutParams);
					}
					ImageView next = (ImageView) jobNo.getChildAt(current+1);
					if(next != null) {
						next.setImageResource(R.drawable.job);
						android.view.ViewGroup.LayoutParams nextLayoutParams = next.getLayoutParams();
						nextLayoutParams.width = 12;
						nextLayoutParams.height = 12;
						next.setLayoutParams(nextLayoutParams);
					}
//					countDownTimer.onFinish();
					startTimer(seconds);
				} else {
					current = current +1;
				}
			}
			
		}

		/**
		 * @param runningAlarmList
		 */
		private void setCountDownNext(ArrayList<AlarmDTO> runningAlarmList) {

			if (!runningAlarmList.isEmpty()) {
				current = current +1;
				if (current  < runningAlarmList.size()) {
				
				currentTask = runningAlarmList.get(current);
					int seconds;

					if (currentTask != null) {
						progressBar.setMax(currentTask.getMaxLeftSecond());
						seconds = currentTask.getSecondsLeft();
						jobName.setText(currentTask.getJobName());
					} else {
						seconds = 0;
					}
				
					if (countDownTimer != null) {
						countDownTimer.cancel();
					} 
					// change dots
					ImageView next = (ImageView) jobNo.getChildAt(current);
					if(next != null) {
						next.setImageResource(R.drawable.current_job);
						android.view.ViewGroup.LayoutParams nextLayoutParams = next.getLayoutParams();
						nextLayoutParams.width = 20;
						nextLayoutParams.height = 20;
						next.setLayoutParams(nextLayoutParams);
					}
					
					
					ImageView previous = (ImageView) jobNo.getChildAt(current-1);
					if (previous != null) {
						previous.setImageResource(R.drawable.job);
						android.view.ViewGroup.LayoutParams previousLayoutParams = previous.getLayoutParams();
						previousLayoutParams.width = 12;
						previousLayoutParams.height = 12;
						previous.setLayoutParams(previousLayoutParams);
					}
					
//					countDownTimer.onFinish();
					startTimer(seconds);
				} else {
					current = current -1;
				}
			}
		}
	}
	private void deleteJob(int id) {
		mDbHelper.open();
		if (mDbHelper.deleteJob(id)) {
			showToast("job deleted");
			startActivity(new Intent(CountDownActivity.this, CountDownActivity.class));
			finish();
		} else {
			
		}
		
		mDbHelper.close();

	}
	
}
