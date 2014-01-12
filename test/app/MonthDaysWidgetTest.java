package app;

import java.util.Locale;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import static java.util.Calendar.*;
import java.util.Date;

/**
 *
 * @author admin
 */
public class MonthDaysWidgetTest {
    
    @Test
    public void testGetCellIndex() {
        MonthDaysWidget target = new MonthDaysWidget(new Date(), Locale.ITALIAN);
        assertEquals(target.cell(SUNDAY), 6);
        assertEquals(target.cell(MONDAY), 0);
        assertEquals(target.cell(WEDNESDAY), 2);
        
        target = new MonthDaysWidget(new Date(), Locale.US);
        assertEquals(target.cell(SUNDAY), 0);
        assertEquals(target.cell(MONDAY), 1);
        assertEquals(target.cell(SATURDAY), 6);
    }
    
}
