package jessy.payrolls;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    
}
