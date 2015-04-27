/**
 * 
 */
package groep13.ehb.dojob.DoJob.src.com.example.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author C-14
 *
 */
public class AlarmDTO {
	
	String id ;
	String jobName ;
	String placeName ;
	int day ;
	String month ;
	int monthNum;
	int year ;
	int hour ;
	int minute ;
	private int second;
	private String endDateString ;
	private Integer  minuteLeft;
	private Integer secondsLeft;
	private int maxLeftMinute ;
	private int maxLeftSecond ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public String getEndDateString() {
		return endDateString;
	}
	public void setEndDatestring(String endDateString) {
		
		this.endDateString = endDateString;
	}
	public Integer getMinuteLeft() {
		
			String currentDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date());
			String dateStart = currentDate;
			String dateStop = getEndDateString();
	 
			//HH converts hour in 24 hours format (0-23), day calculation
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	 
			Date d1 = null;
			Date d2 = null;
	 
			try {
				d1 = format.parse(dateStart);
				d2 = format.parse(dateStop);
	 
				//in milliseconds
				long diff = d2.getTime() - d1.getTime();
	 
				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
			
				System.out.print(diffMinutes + " minutes, ");
				minuteLeft = 	(int) diffMinutes;	 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
		return minuteLeft;
	}
	public void setMinuteLeft(Integer minuteLeft) {
		this.minuteLeft = minuteLeft;
	}

	public int getMaxLeftMinute() {
		return maxLeftMinute;
	}
	public void setMaxLeftMinute(int maxLeftMinute) {
		this.maxLeftMinute = maxLeftMinute;
	}
	public Integer getSecondsLeft() {
		String currentDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date());
		String dateStart = currentDate;
		String dateStop = getEndDateString();
 
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
 
		Date d1 = null;
		Date d2 = null;
 
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
			int a ;
			//in milliseconds
//			int diff = (int) (d2.getTime() - d1.getTime());
			long diff =  (d2.getTime() - d1.getTime());
 
			//int diffSeconds = diff / 1000 % 60;
			int diffSeconds = (int) (diff / 1000) ;
			long diffMinutes = diff / (60 * 1000) % 60;
		
			System.out.print(diffMinutes + " minutes, ");
			secondsLeft = 	(int) diffSeconds;	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return secondsLeft;
	}
	public void setSecondsLeft(Integer secondsLeft) {
		this.secondsLeft = secondsLeft;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public int getMaxLeftSecond() {
		return maxLeftSecond;
	}
	public void setMaxLeftSecond(int maxLeftSecond) {
		this.maxLeftSecond = maxLeftSecond;
	}
	
	
	
}
