package groep13.ehb.dojob.DoJob.src.com.example.fadescreenalarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import groep13.ehb.dojob.DoJob.src.com.example.dao.TestAdapter;
import groep13.ehb.dojob.DoJob.src.com.example.dto.AlarmDTO;
import groep13.ehb.dojob.DoJob.src.com.example.utility.KeyboardUtility;

@SuppressLint("SimpleDateFormat")
public class EditAlarmActivity extends ActionBarActivity {
	
	private static final int DATE_DIALOG_ID = 0;
	ArrayAdapter<Integer> adapter;
	private EditText jobName;
	private EditText placeName;
	private int pYear;
	private int pMonth;
	private int pDay;
	private LinearLayout dateLayout;
	private LinearLayout timeLayout;
	private TextView dayLabel;
	private TextView monthLabel;
	private TextView yearLabel;
	private TextView hourLabel;
	private TextView minuteLabel;
	protected int seconds;
	TestAdapter mDbHelper;
	TimePickerDialog tm;
	int id ;
	EditAlarmActivity AddAlarmActivity = this;
	String[] months = new String[] { "januari", "februari", "maart", "april",
			"mei", "juni", "juli", "augustus", "september", "october",
			"november", "december" };
	private AlarmDTO alarmDTO;

	@SuppressLint("NewApi")
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_alarm:
			String jobNames = jobName.getText().toString();
			String placeNames = placeName.getText().toString();
			if(jobNames.trim().isEmpty()){
				showToast("Enter Job Name ");
			}else if(placeNames.trim().isEmpty()){
				showToast("Enter Place Name ");
			}else{
				update();	
			}
			
//			addAlarm();
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 */
	private void update() {
		alarmDTO = new AlarmDTO();
		alarmDTO.setDay(Integer.parseInt(dayLabel.getText().toString()));
		alarmDTO.setMonth(monthLabel.getText().toString());
		alarmDTO.setMonthNum(Integer.parseInt(getMonthNum(monthLabel.getText()
				.toString().trim())));
		alarmDTO.setYear(Integer.parseInt(yearLabel.getText().toString()));
		alarmDTO.setHour(Integer.parseInt(hourLabel.getText().toString()));
		alarmDTO.setMinute(Integer.parseInt(minuteLabel.getText().toString()));
		alarmDTO.setSecond(seconds);
		alarmDTO.setEndDatestring(createEndDate(alarmDTO));
		alarmDTO.setMaxLeftMinute(getMaxLeftMinute(alarmDTO.getEndDateString()));
		alarmDTO.setMaxLeftSecond(getMaxLeftSeconds(alarmDTO.getEndDateString()));
		mDbHelper.open();
		ContentValues alarmValues = new ContentValues();
		alarmValues.put("job_name", jobName.getText().toString());
		alarmValues.put("place_name", placeName.getText().toString());
		alarmValues.put("day", dayLabel.getText().toString());
		alarmValues.put("month", monthLabel.getText().toString());
		alarmValues.put("month_num", getMonthNum(monthLabel.getText()
				.toString().trim()));
		alarmValues.put("year", yearLabel.getText().toString());
		alarmValues.put("hour", hourLabel.getText().toString());

		alarmValues.put("minute", minuteLabel.getText().toString());
		alarmValues.put("max_left_minute", alarmDTO.getMaxLeftMinute());
		alarmValues.put("end_date", alarmDTO.getEndDateString());
		alarmValues.put("max_left_second", alarmDTO.getMaxLeftSecond());
		alarmValues.put("status","running");
		 mDbHelper.updateAlarm(alarmValues,id);
		if ( mDbHelper.updateAlarm(alarmValues,id)) {
//			 showToast("update complete");
			showToast("update te voltooien");
			startActivity(new Intent(EditAlarmActivity.this,
					CountDownActivity.class));
			finish();
		} else {
//			 showToast("fail to update");
			showToast("niet om op te slaan");
		}
		mDbHelper.selectAlarm();
		mDbHelper.close();
		
	}

	/**
	 * @param endDatestring
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private int getMaxLeftSeconds(String endDateString) {
		String currentDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
				.format(new java.util.Date());
		String dateStart = currentDate;
		String dateStop = endDateString;

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		int secondLeft = 0;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
//			int diff = (int) (d2.getTime() - d1.getTime());
			long diff =  (d2.getTime() - d1.getTime());

			// int diffSeconds = diff / 1000 % 60;
			int diffSeconds = (int) (diff / 1000);
			long diffMinutes = diff / (60 * 1000) % 60;

			System.out.print(diffMinutes + " minutes, ");
			secondLeft = (int) diffSeconds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return secondLeft;
	}

	/**
	 * @param endDateString
	 * @return
	 */
	private int getMaxLeftMinute(String endDateString) {
		String currentDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
				.format(new java.util.Date());
		String dateStart = currentDate;
		String dateStop = endDateString;

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		int minuteLeft = 0;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;

			System.out.print(diffMinutes + " minutes, ");
			minuteLeft = (int) diffMinutes;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return minuteLeft;

	}

	/**
	 * @param alarmDTO2
	 * @return
	 */
	private String createEndDate(AlarmDTO alarmDTO2) {
		String endDate = "" + alarmDTO.getMonthNum() + "/" + alarmDTO.getDay()
				+ "/" + alarmDTO.getYear() + " " + alarmDTO.getHour() + ":"
				+ alarmDTO.getMinute() + ":" + alarmDTO2.getSecond();
		return endDate;
	}

	/**
	 * @param string
	 * @return
	 */
	private String getMonthNum(String monthName) {
		for (int i = 0; i < months.length; i++) {
			if (monthName.equals(months[i])) {
				return String.valueOf(i + 1);
			}
		}
		return "";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_alarm);
		new KeyboardUtility().setupUI(findViewById(R.id.parent), this);
		initVar();
		if(getIntent().getAction().equals("edit")) {
			id = Integer.parseInt(getIntent().getStringExtra("id"));
			alarmDTO = getAlarmDTO(id);
			if(alarmDTO != null) {
				fillData(alarmDTO);	
			} else {
				
			} 
			
			
		}
	}

/**
 * @param id 
 * @return 
	 * 
	 */
	private AlarmDTO getAlarmDTO(int id) {
		mDbHelper.open();
		AlarmDTO alarmDTO = mDbHelper.getAlarmDTO(String.valueOf(id));
		mDbHelper.close();
		return alarmDTO;
	}

	/**
	 * @param alarmDTO2 
	 * 
	 */
	private void fillData(AlarmDTO alarmDTO2) {
		jobName.setText(alarmDTO2.getJobName());
		placeName.setText(alarmDTO2.getPlaceName());
		dayLabel.setText(String.valueOf(alarmDTO2.getDay()));
		monthLabel.setText(alarmDTO2.getMonth());
		yearLabel.setText(String.valueOf(alarmDTO2.getYear()));
		hourLabel.setText(String.valueOf(alarmDTO2.getHour()));
		minuteLabel.setText(String.valueOf(alarmDTO2.getMinute()));
	}

	/**
	 * 
	 */
	private void initVar() {
		mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		jobName = (EditText) findViewById(R.id.job_text);
		placeName = (EditText) findViewById(R.id.place_text);
		dateLayout = (LinearLayout) findViewById(R.id.date_layout);

		dateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Calendar cal = Calendar.getInstance();
				pYear = cal.get(Calendar.YEAR);
				pMonth = cal.get(Calendar.MONTH);
				pDay = cal.get(Calendar.DAY_OF_MONTH);

				showDialog(DATE_DIALOG_ID);

			}
		});
		timeLayout = (LinearLayout) findViewById(R.id.time_layout);
		timeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePicker24(1);
			}
		});
		dayLabel = (TextView) findViewById(R.id.day_label);
		monthLabel = (TextView) findViewById(R.id.month_label);
		yearLabel = (TextView) findViewById(R.id.year_label);
		hourLabel = (TextView) findViewById(R.id.hour_label);
		minuteLabel = (TextView) findViewById(R.id.minute_label);
		final Calendar cal = Calendar.getInstance();
		pYear = cal.get(Calendar.YEAR);
		pMonth = cal.get(Calendar.MONTH);
		pDay = cal.get(Calendar.DAY_OF_MONTH);

//		updateDateView();
//		updateTimeView(cal);

	}

	/**
	 * @param cal
	 * 
	 */
	private void updateTimeView(Calendar cal) {
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		if (hour < 10 && minute < 10) {

			hourLabel.setText("0" + hour);
			minuteLabel.setText("0" + minute);

		} else if (hour < 10) {
			hourLabel.setText("0" + hour);
			minuteLabel.setText(String.valueOf(minute));

		} else if (minute < 10) {
			hourLabel.setText(String.valueOf(hour));
			minuteLabel.setText("0" + minute);

		} else {
			hourLabel.setText(String.valueOf(hour));
			minuteLabel.setText(String.valueOf(minute));

		}

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
		/*
		 * case R.id.action_settings: return true;
		 */
		/*case R.id.menu:
			startActivity(new Intent(EditAlarmActivity.this, MenuActivity.class));
			finish();
			return true;*/
		default:
			break;
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

	/** Create a new dialog for date picker */
	@SuppressLint("NewApi")
	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		instance.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		instance.set(Calendar.DAY_OF_MONTH,
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		switch (id) {
		case DATE_DIALOG_ID:

			// return new DatePickerDialog(this, pDateSetListener, pYear,
			// pMonth,
			// pDay);
			DatePickerDialog d = new DatePickerDialog(this, pDateSetListener,
					pYear, pMonth, pDay);
			DatePicker dp = d.getDatePicker();
			dp.setMinDate(instance.getTimeInMillis());
			return d;
		}
		return null;
	}

	private void updateDateView() {

		dayLabel.setText(String.valueOf(pDay));
		monthLabel.setText(months[pMonth]);
		yearLabel.setText(String.valueOf(pYear));

	}

	/** Callback received when the user "picks" a date in the dialog */
	private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			onDateChanged(view, year, monthOfYear, dayOfMonth);
			pYear = year;
			pMonth = monthOfYear;
			pDay = dayOfMonth;
			updateDateView();
		}

		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			if (year < pYear) {
				view.updateDate(pYear, pMonth, pDay);
				// showToast("Past date cannot be used");
				showToast("Past datum niet worden gebruikt");
			}
			if (monthOfYear < pMonth && year == pYear) {
				view.updateDate(pYear, pMonth, pDay);
				showToast("Past datum niet worden gebruikt");
			}

			if (dayOfMonth < pDay && year == pYear && monthOfYear == pMonth) {
				view.updateDate(pYear, pMonth, pDay);
				showToast("Past datum niet worden gebruikt");
			}
		}
	};

	private void showTimePicker24(final int idTextView) {

		this.tm = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					public void onTimeSet(TimePicker paramAnonymousTimePicker,
							int paramAnonymousInt1, int paramAnonymousInt2) {

						if (paramAnonymousInt1 < 10 && paramAnonymousInt2 < 10) {
							// if (checkExpireTime("0" + paramAnonymousInt1, "0"
							// + paramAnonymousInt2)) {
							hourLabel.setText("0" + paramAnonymousInt1);
							minuteLabel.setText("0" + paramAnonymousInt2);
							// }

						} else if (paramAnonymousInt1 < 10) {
							// if (checkExpireTime("0" +
							// paramAnonymousInt1,String
							// .valueOf(paramAnonymousInt2))) {
							hourLabel.setText("0" + paramAnonymousInt1);
							minuteLabel.setText(String
									.valueOf(paramAnonymousInt2));
							// }

						} else if (paramAnonymousInt2 < 10) {
							// if (checkExpireTime(String
							// .valueOf(paramAnonymousInt1),"0" +
							// paramAnonymousInt2)) {
							hourLabel.setText(String
									.valueOf(paramAnonymousInt1));
							minuteLabel.setText("0" + paramAnonymousInt2);
							// }

						} else {
							// if (checkExpireTime(String
							// .valueOf(paramAnonymousInt1),String
							// .valueOf(paramAnonymousInt2))) {
							hourLabel.setText(String
									.valueOf(paramAnonymousInt1));
							minuteLabel.setText(String
									.valueOf(paramAnonymousInt2));
							// }

						}
						final Calendar cal = Calendar.getInstance();
						seconds = cal.get(Calendar.SECOND);

					}

					private boolean checkExpireTime(String hour, String minute) {
						String currentDate = new SimpleDateFormat(
								"MM/dd/yyyy HH:mm:ss")
								.format(new java.util.Date());

						final Calendar cal = Calendar.getInstance();
						seconds = cal.get(Calendar.SECOND);

						String dateStart = currentDate;
						// String dateStop = getEndDateString();
						String monthNum = getMonthNum(monthLabel.getText()
								.toString().trim());
						String day = dayLabel.getText().toString().trim();
						String year = yearLabel.getText().toString().trim();
						String endDate = "" + monthNum + "/" + day + "/" + year
								+ " " + hour + ":" + minute + ":" + "00";

						/*
						 * String endDate = "" + alarmDTO.getMonthNum() + "/" +
						 * alarmDTO.getDay() + "/" + alarmDTO.getYear() + " " +
						 * alarmDTO.getHour() + ":" + alarmDTO.getMinute() + ":"
						 * + "00";
						 */
						;

						// HH converts hour in 24 hours format (0-23), day
						// calculation
						SimpleDateFormat format = new SimpleDateFormat(
								"MM/dd/yyyy HH:mm:ss");

						Date d1 = null;
						Date d2 = null;

						try {
							d1 = format.parse(dateStart);
							d2 = format.parse(endDate);

							// in milliseconds

							int diff = (int) (d2.getTime() - d1.getTime());
							if (Integer.signum(diff) == 1) {
								return true;
							} else {
								showToast("time expire");
								return false;
							}
							// int diffSeconds = diff / 1000 % 60;
							// int diffSeconds = diff / 1000;
							// long diffMinutes = diff / (60 * 1000) % 60;

							// System.out.print(diffMinutes + " minutes, ");
							// secondsLeft = (int) diffSeconds;
						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;

					}

				}, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar
						.getInstance().get(Calendar.MINUTE), true); // when
																	// boolean
																	// --true
																	// for 24
																	// format
		// and
		// false for 12

		// this.tm.setTitle("Select Time");
		this.tm.setTitle("selecteer Time");
		this.tm.show();
		this.tm.updateTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
				Calendar.getInstance().get(Calendar.MINUTE));

	}

	private void showToast(String message) {
		Toast.makeText(EditAlarmActivity.this, message, Toast.LENGTH_SHORT)
				.show();
	}
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(EditAlarmActivity.this, CountDownActivity.class));
	}

}
