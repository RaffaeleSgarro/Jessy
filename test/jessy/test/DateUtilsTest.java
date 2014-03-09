package jessy.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static jessy.utils.DateUtils.*;

public class DateUtilsTest {
    
    @Test
    public void testRange() throws Exception {
        assertSize("2014-03-01", "2014-03-09", 9);
        assertSize("2014-02-28", "2014-03-10", 1 + 10);
        assertSize("2014-03-01", "2014-03-01", 1);
        assertSize("2013-12-31", "2014-03-01", 1 + 31 + 28 + 1);
    }
    
    private void assertSize(String start, String end, int expected) throws ParseException {
        assertEquals(range(d(start), d(end)).size(), expected);
    }
    
    private Date d(String str) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(str);
    }
    
    @Test
    public void testFindAbbrevWeekDaysNames_italianLocale() {
        List<String> ita = findAbbrevWeekDaysNames(Locale.ITALIAN);
        assertEquals(ita.size(), 7);
        assertEquals(ita.get(0).toLowerCase(), "lun");
        assertEquals(ita.get(6).toLowerCase(), "dom");
    }
    
    @Test
    public void testFindAbbrevWeekDaysNames_usLocale() {
        List<String> eng = findAbbrevWeekDaysNames(Locale.US);
        assertEquals(eng.size(), 7);
        assertEquals(eng.get(0).toLowerCase(), "sun");
        assertEquals(eng.get(6).toLowerCase(), "sat");
    }
}
