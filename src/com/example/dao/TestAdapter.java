package groep13.ehb.dojob.DoJob.src.com.example.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dto.AlarmDTO;
import com.example.fadescreenalarm.AlarmComp;

public class TestAdapter {
	protected static final String TAG = "DataAdapter";

	private final Context mContext;
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;

	private Iterator<AlarmDTO> iterator;

	public TestAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public TestAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public TestAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public Cursor selectQuery(String query) {
		Cursor c1 = null;
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
			c1 = mDb.rawQuery(query, null);

		} catch (Exception e) {

			System.out.println("DATABASE ERROR 10 " + e);

		}
		return c1;

	}

	public void executeQuery(String query) {
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}

			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL(query);

		} catch (Exception e) {

			System.out.println("DATABASE ERROR 1 " + e);
		}
	}

	public void deleteQuery(String query) {
		try {
			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL(query);
		} catch (Exception e) {
			System.out.println("DATABASE ERROR  2" + e);
		}

	}

	public void updateQuery(String query) {
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}

			mDb = mDbHelper.getWritableDatabase();
			mDb.execSQL(query);

		} catch (Exception e) {

			System.out.println("DATABASE ERROR 3 " + e);
		}

	}

	/**
	 * @param alarmValues
	 * @return
	 * 
	 */
	public Long saveAlarm(ContentValues alarmValues) {
		try {
			Long id = mDb.insert("alarm_tbl", null, alarmValues);

			Log.d("Save category", "informationsaved");
			return id;

		} catch (Exception ex) {
			Log.d("SaveCategory", ex.toString());
			ex.printStackTrace();
			return -1L;
		}

	}

	public boolean selectAlarm() {
		Cursor c1 = null;
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
			c1 = mDb.rawQuery("select * from alarm_tbl", null);
			if (c1 != null && c1.getCount() > 0) {
				if (c1.moveToFirst()) {
					System.out.println("tasktable");
					do {
						System.out
								.println("c1.getString(c1.getColumnIndex(\"job_name\")) :: "
										+ c1.getString(c1
												.getColumnIndex("job_name")));
						System.out
								.println("c1.getColumnIndex(\"place_name\"):: "
										+ c1.getString(c1
												.getColumnIndex("place_name")));
					} while (c1.moveToNext());
				}
			}
			c1.close();// closing connection
		} catch (Exception e) {
			System.out.println("DATABASE ERROR 8 " + e);
		}

		return false;

	}

	/**
	 * 
	 */
	public AlarmDTO getClosestTask() {
		AlarmDTO alarmDTO;
		ArrayList<AlarmDTO> alarmList = new ArrayList<AlarmDTO>();
		Cursor c1 = null;
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
			c1 = mDb.rawQuery("select * from alarm_tbl where status != 'expire'", null);
			if (c1 != null && c1.getCount() > 0) {
				if (c1.moveToFirst()) {
					do {
						alarmDTO = new AlarmDTO();
						alarmDTO.setId(c1.getString(c1.getColumnIndex("id")));
						alarmDTO.setJobName(c1.getString(c1
								.getColumnIndex("job_name")));
						alarmDTO.setPlaceName(c1.getString(c1
								.getColumnIndex("place_name")));
						alarmDTO.setDay(Integer.parseInt(c1.getString(
								c1.getColumnIndex("day")).trim()));
						alarmDTO.setMonth(c1.getString(c1.getColumnIndex("month")));
						alarmDTO.setMonthNum(Integer.parseInt(c1.getString(c1
								.getColumnIndex("month_num"))));
						alarmDTO.setYear(Integer.parseInt(c1.getString(
								c1.getColumnIndex("year")).trim()));
						alarmDTO.setHour(Integer.parseInt(c1.getString(
								c1.getColumnIndex("hour")).trim()));
						alarmDTO.setMinute(Integer.parseInt((c1.getString(c1
								.getColumnIndex("minute")).trim())));
						alarmDTO = createEndDate(alarmDTO);
						System.out.println(alarmDTO.getEndDateString());
						alarmList.add(alarmDTO);
						/*
						 * System.out.println(
						 * "c1.getString(c1.getColumnIndex(\"job_name\")) :: "
						 * +c1.getString(c1.getColumnIndex("job_name")));
						 * System.
						 * out.println("c1.getColumnIndex(\"place_name\"):: "
						 * +c1.getString(c1.getColumnIndex("place_name")));
						 * System
						 * .out.println("c1.getColumnIndex(\"day\"):: "+c1.
						 * getString(c1.getColumnIndex("day")));
						 * System.out.println
						 * ("c1.getColumnIndex(\"month\"):: "+c1
						 * .getString(c1.getColumnIndex("month")));
						 * System.out.println
						 * ("c1.getColumnIndex(\"year\"):: "+c1
						 * .getString(c1.getColumnIndex("year")));
						 * System.out.println
						 * ("c1.getColumnIndex(\"hour\"):: "+c1
						 * .getString(c1.getColumnIndex("hour")));
						 * System.out.println
						 * ("c1.getColumnIndex(\"minute\"):: "+
						 * c1.getString(c1.getColumnIndex("minute")));
						 */

					} while (c1.moveToNext());
				}
			}
			c1.close();// closing connection
			if(!alarmList.isEmpty()) {
				return getFirstClosestTask(alarmList);	
			}else {
				return null;
			} 
			 

		} catch (Exception e) {
			System.out.println("DATABASE ERROR 8 " + e);
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param alarmDTO
	 * @return 
	 * 
	 */
	private AlarmDTO createEndDate(AlarmDTO alarmDTO) {

		String string = "01/14/2012 09:29:58";
		String endDate = "" + alarmDTO.getMonthNum() + "/" + alarmDTO.getDay()
				+ "/" + alarmDTO.getYear() + " " + alarmDTO.getHour() + ":"
				+ alarmDTO.getMinute() + ":" + "00";
		alarmDTO.setEndDatestring(endDate);
		return alarmDTO;
	}

	/**
	 * @param alarmList
	 * @return 
	 * 
	 */
	private AlarmDTO getFirstClosestTask(ArrayList<AlarmDTO> alarmList) {
		AlarmDTO alarmDTO = Collections.min(alarmList,new AlarmComp());
		
		return alarmDTO;
	}

	/**
	 * @param id
	 * @return
	 */
	public void updateTask(ContentValues cv, String id) {
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}

			mDb = mDbHelper.getWritableDatabase();
			mDb.update("alarm_tbl", cv, "id = ?", new String[] { id });

		} catch (Exception e) {

			System.out.println("DATABASE ERROR 4 " + e);
		}
		// return 0;
	}

	/**
	 * @return 
	 * 
	 */
	public boolean isJobExist() {
		boolean exist = false;
		Cursor c1 = null;
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
			c1 = mDb.rawQuery("select * from alarm_tbl", null);
			if (c1 != null && c1.getCount() > 0) {
				exist = true;
				/*if (c1.moveToFirst()) {
					System.out.println("tasktable");
					do {
						System.out
								.println("c1.getString(c1.getColumnIndex(\"job_name\")) :: "
										+ c1.getString(c1
												.getColumnIndex("job_name")));
						System.out
								.println("c1.getColumnIndex(\"place_name\"):: "
										+ c1.getString(c1
												.getColumnIndex("place_name")));
					} while (c1.moveToNext());
				}*/
			} else {
				exist = false;
			}
			c1.close();// closing connection
		} catch (Exception e) {
			System.out.println("DATABASE ERROR 8 " + e);
			e.printStackTrace();
		}

		return exist;
		
	}

	/**
	 * @return
	 */
	public ArrayList<AlarmDTO> getRunningTask() {
		AlarmDTO alarmDTO;
		ArrayList<AlarmDTO> alarmList = new ArrayList<AlarmDTO>();
		Cursor c1 = null;
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
			c1 = mDb.rawQuery("select * from alarm_tbl where status = 'running'", null);
			if (c1 != null && c1.getCount() > 0) {
				if (c1.moveToFirst()) {
					do {
						alarmDTO = new AlarmDTO();
						alarmDTO.setId(c1.getString(c1.getColumnIndex("id")));
						alarmDTO.setJobName(c1.getString(c1
								.getColumnIndex("job_name")));
						alarmDTO.setPlaceName(c1.getString(c1
								.getColumnIndex("place_name")));
						alarmDTO.setDay(Integer.parseInt(c1.getString(
								c1.getColumnIndex("day")).trim()));
						alarmDTO.setMonth(c1.getString(c1.getColumnIndex("month")));
						alarmDTO.setMonthNum(Integer.parseInt(c1.getString(c1
								.getColumnIndex("month_num"))));
						alarmDTO.setYear(Integer.parseInt(c1.getString(
								c1.getColumnIndex("year")).trim()));
						alarmDTO.setHour(Integer.parseInt(c1.getString(
								c1.getColumnIndex("hour")).trim()));
						alarmDTO.setMinute(Integer.parseInt((c1.getString(c1
								.getColumnIndex("minute")).trim())));
						alarmDTO.setMaxLeftMinute(Integer.parseInt((c1.getString(c1
								.getColumnIndex("max_left_minute")).trim())));
						alarmDTO.setMaxLeftSecond(Integer.parseInt((c1.getString(c1
								.getColumnIndex("max_left_second")).trim())));
						alarmDTO.setEndDatestring((c1.getString(c1
								.getColumnIndex("end_date")).trim()));
					//	alarmDTO = createEndDate(alarmDTO);
						System.out.println(alarmDTO.getEndDateString());
						alarmList.add(alarmDTO);
						

					} while (c1.moveToNext());
				}
			} 
			c1.close();// closing connection
			
			 

		} catch (Exception e) {
			System.out.println("DATABASE ERROR 8 " + e);
			e.printStackTrace();
			return null;
		}
		return alarmList;
	}

	/**
	 * @param act_name
	 * @return
	 */
	public boolean isJobExist(String act_name) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return 
	 * 
	 */
	public ArrayList<AlarmDTO> getAlarmList() {
		AlarmDTO alarmDTO;
		ArrayList<AlarmDTO> alarmList = new ArrayList<AlarmDTO>();
		Cursor c1 = null;
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
			c1 = mDb.rawQuery("select * from alarm_tbl where status = 'running'", null);
			if (c1 != null && c1.getCount() > 0) {
				if (c1.moveToFirst()) {
					do {
						alarmDTO = new AlarmDTO();
						alarmDTO.setId(c1.getString(c1.getColumnIndex("id")));
						alarmDTO.setJobName(c1.getString(c1
								.getColumnIndex("job_name")));
						alarmDTO.setPlaceName(c1.getString(c1
								.getColumnIndex("place_name")));
						alarmDTO.setDay(Integer.parseInt(c1.getString(
								c1.getColumnIndex("day")).trim()));
						alarmDTO.setMonth(c1.getString(c1.getColumnIndex("month")));
						alarmDTO.setMonthNum(Integer.parseInt(c1.getString(c1
								.getColumnIndex("month_num"))));
						alarmDTO.setYear(Integer.parseInt(c1.getString(
								c1.getColumnIndex("year")).trim()));
						alarmDTO.setHour(Integer.parseInt(c1.getString(
								c1.getColumnIndex("hour")).trim()));
						alarmDTO.setMinute(Integer.parseInt((c1.getString(c1
								.getColumnIndex("minute")).trim())));
						alarmDTO.setMaxLeftMinute(Integer.parseInt((c1.getString(c1
								.getColumnIndex("max_left_minute")).trim())));
						alarmDTO.setMaxLeftSecond(Integer.parseInt((c1.getString(c1
								.getColumnIndex("max_left_second")).trim())));
						alarmDTO.setEndDatestring((c1.getString(c1
								.getColumnIndex("end_date")).trim()));
					//	alarmDTO = createEndDate(alarmDTO);
						System.out.println(alarmDTO.getEndDateString());
						alarmList.add(alarmDTO);
						

					} while (c1.moveToNext());
				}
			} 
			c1.close();// closing connection
			
			 

		} catch (Exception e) {
			System.out.println("DATABASE ERROR 8 " + e);
			e.printStackTrace();
			return null;
		}
		return alarmList;
		
	}

	/**
	 * @return
	 */
/*	public ArrayList<ListData> getAlarmListView() {
		ArrayList<JobList.ListData> localArrayList = new ArrayList<JobList.ListData>();
		String query1 = "select * from alarm_tbl";
		ListData listData ;

		Cursor c1 = null;
		String dateTime;
		String placeName;
		String jobName;
		String id;
		String status;
		try {

			if (mDb.isOpen()) {
				mDb.close();
			}
			mDb = mDbHelper.getWritableDatabase();
		//	c1 = mDb.rawQuery("select * from alarm_tbl where status = 'running'", null);
			c1 = mDb.rawQuery("select * from alarm_tbl ", null);
			if (c1 != null && c1.getCount() > 0) {
				if (c1.moveToFirst()) {
					do {
						
					
						id  = c1.getString(c1.getColumnIndex("id"));
						jobName = c1.getString(c1
								.getColumnIndex("job_name")) ;
						placeName = c1.getString(c1
								.getColumnIndex("place_name"));
						dateTime = (c1.getString(c1
								.getColumnIndex("end_date")).trim());
						status = (c1.getString(c1
								.getColumnIndex("status")).trim());
						listData = new JobList().new ListData(id, jobName, placeName, dateTime, status);
					
						localArrayList.add(listData);
						

					} while (c1.moveToNext());
				}tasknumber.setVisibility(8);

			} else {
				tasknumber.setVisibility(0);
				tasknumber.setText("No Task Found");
			}
			} 
			c1.close();// closing connection

		} catch (Exception e) {
			System.out.println("DATABASE ERROR 8 " + e);
			e.printStackTrace();
			return null;
		}
		return localArrayList;
		
	}
*/
	/**
	 * @param id
	 * @return 
	 */
	public boolean deleteJob(int id) {
		
		try {
			int num = mDb.delete("alarm_tbl", "id = ?", new String[] { String.valueOf(id) } );
			Log.d("delete job", "information deleted");
			if (num == 1) {
				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			Log.d("Delete Job", ex.toString());
			ex.printStackTrace();
			return false;
		}
	}

		public AlarmDTO getAlarmDTO(String id) {
			AlarmDTO alarmDTO = null;
			Cursor c1 = null;
			try {

				if (mDb.isOpen()) {
					mDb.close();
				}
				mDb = mDbHelper.getWritableDatabase();
				c1 = mDb.rawQuery("select * from alarm_tbl where id = "+id+"", null);
				if (c1 != null && c1.getCount() > 0) {
					if (c1.moveToFirst()) {
						alarmDTO = new AlarmDTO();
						alarmDTO.setId(c1.getString(c1.getColumnIndex("id")));
						alarmDTO.setJobName(c1.getString(c1
								.getColumnIndex("job_name")));
						alarmDTO.setPlaceName(c1.getString(c1
								.getColumnIndex("place_name")));
						alarmDTO.setDay(Integer.parseInt(c1.getString(
								c1.getColumnIndex("day")).trim()));
						alarmDTO.setMonth(c1.getString(c1.getColumnIndex("month")));
						alarmDTO.setMonthNum(Integer.parseInt(c1.getString(c1
								.getColumnIndex("month_num"))));
						alarmDTO.setYear(Integer.parseInt(c1.getString(
								c1.getColumnIndex("year")).trim()));
						alarmDTO.setHour(Integer.parseInt(c1.getString(
								c1.getColumnIndex("hour")).trim()));
						alarmDTO.setMinute(Integer.parseInt((c1.getString(c1
								.getColumnIndex("minute")).trim())));
						alarmDTO.setMaxLeftMinute(Integer.parseInt((c1.getString(c1
								.getColumnIndex("max_left_minute")).trim())));
						alarmDTO.setMaxLeftSecond(Integer.parseInt((c1.getString(c1
								.getColumnIndex("max_left_second")).trim())));
						alarmDTO.setEndDatestring((c1.getString(c1
								.getColumnIndex("end_date")).trim()));
					}
				}
				c1.close();// closing connection
			} catch (Exception e) {
				System.out.println("DATABASE ERROR 8 " + e);
				e.printStackTrace();
				return null;
			}

			return alarmDTO;
	}

		/**
		 * @param alarmValues
		 * @param id 
		 * @return
		 */
		public boolean updateAlarm(ContentValues alarmValues, int id) {
			try {
				if (mDb.isOpen()) {
					mDb.close();
				}
				mDb = mDbHelper.getWritableDatabase();
				mDb.update("alarm_tbl", alarmValues, "id = ?", new String[] { String.valueOf(id) });
			} catch (Exception e) {

				System.out.println("DATABASE ERROR 5 " + e);
				e.printStackTrace();
				return false;
			}
			return true;
		}

	
}
