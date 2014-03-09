package jessy.utils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;

public class DateUtils {
    
    public static List<Date> range(Date startInclusive, Date endInclusive) {
        if (endInclusive.before(startInclusive)) {
            throw new IllegalArgumentException(String.format(
                      "End must be less than start, start: %s end: %s"
                    , startInclusive.toString()
                    , endInclusive.toString()));
        }
        
        List<Date> out = new ArrayList<>();
        out.add(startInclusive);
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar stop = new GregorianCalendar();
        current.setTime(startInclusive);
        stop.setTime(endInclusive);
        
        while (true) {
            current.add(Calendar.DAY_OF_MONTH, 1);
            if (current.after(stop)) {
                break;
            } else {
                out.add(current.getTime());
            }
        }
        
        return out;
    }
    
    public static List<String> findLongMonthsNames(Locale locale) {
        return asList(DateFormatSymbols.getInstance(locale).getMonths());
    }
    
    public static List<String> findAbbrevWeekDaysNames(Locale locale) {
        List<String> out = new ArrayList<>();
        out.addAll(asList(DateFormatSymbols.getInstance(locale).getShortWeekdays()));
        int firstDayOfWeek = GregorianCalendar.getInstance(locale).getFirstDayOfWeek();
        for (int i = 0; i < Calendar.SUNDAY; i++) out.remove(0);
        for (int i = 1; i < firstDayOfWeek; i++) out.add(out.remove(0));
        return out;
    }
    
}
