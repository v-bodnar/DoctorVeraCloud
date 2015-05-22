package ua.kiev.doctorvera.utils;

import javax.ejb.Stateless;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date getWeekStart(){
        // set the date
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        
        
        // "calculate" the start date of the week
        Calendar first = (Calendar) cal.clone();
        first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
        first.set(Calendar.HOUR_OF_DAY, 0);
        first.set(Calendar.MINUTE, 0);
        first.set(Calendar.SECOND, 0);
        first.set(Calendar.MILLISECOND, 0);
        return first.getTime();
    }
    
    public static Date getWeekEnd(){
        // set the date
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStart());
        
        // and add six days to the end date
        Calendar last = (Calendar) cal.clone();
        last.add(Calendar.DAY_OF_YEAR, 6);
        last.set(Calendar.HOUR_OF_DAY, 23);
        last.set(Calendar.MINUTE, 59);
        last.set(Calendar.SECOND, 59);
        last.set(Calendar.MILLISECOND, 99);
    	return last.getTime();
    }
}
