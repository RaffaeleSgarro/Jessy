package jessy.test;

import jessy.controls.datepicker.MonthDaysGrid;
import java.util.Locale;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import static java.util.Calendar.*;

/**
 *
 * @author admin
 */
public class MonthDaysWidgetTest {
    
    @Test
    public void testGetCellIndex() {
        MonthDaysGrid target = new MonthDaysGrid(Locale.ITALIAN);
        assertEquals(target.column(SUNDAY), 6);
        assertEquals(target.column(MONDAY), 0);
        assertEquals(target.column(WEDNESDAY), 2);
        
        target = new MonthDaysGrid(Locale.US);
        assertEquals(target.column(SUNDAY), 0);
        assertEquals(target.column(MONDAY), 1);
        assertEquals(target.column(SATURDAY), 6);
    }
    
}
