
package groep13.ehb.dojob.DoJob.src.com.example.fadescreenalarm;

import java.util.Comparator;

import groep13.ehb.dojob.DoJob.src.com.example.dto.AlarmDTO;

/**
 * @author C-13
 *
 */
public class AlarmComp implements Comparator<AlarmDTO>{
	 
    
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	/*@Override
	public int compare(AlarmDTO alarm1, AlarmDTO alarm2) {
		 return alarm1.getMinuteLeft().compareTo(alarm2.getMinuteLeft());
	}*/
	@Override
	public int compare(AlarmDTO alarm1, AlarmDTO alarm2) {
		 return alarm1.getSecondsLeft().compareTo(alarm2.getSecondsLeft());
	}
}