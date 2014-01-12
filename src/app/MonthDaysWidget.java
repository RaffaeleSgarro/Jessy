package app;

import java.util.GregorianCalendar;
import java.util.Locale;
import javafx.scene.control.Cell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;

import static java.util.Calendar.*;

/**
 * The grid has 7 columns (one for each week day) and 6 rows (because a month
 * spans 6 weeks at most).
 * 
 * Top left is (0, 0)
 * 
 * @author admin
 */
public class MonthDaysWidget {
    
    private final Locale locale;
    private final GridPane grid;
    private final DayCell[] days = new DayCell[7 * 6];
    
    private int month;
    private int year;
    
    public MonthDaysWidget(Locale locale) {
        this.locale = locale;
        grid = GridPaneBuilder.create().build();
        String[] foo = {"sdf"};
    }
    
    private static class DayCell extends Cell {
    }
    
    /**
     * 
     * @param month 1-12
     * @param year  
     */
    public void setMonth(int month, int year) {
        this.month = month;
        this.year = year;
        
        GregorianCalendar cal = new GregorianCalendar(locale);
        cal.set(YEAR, year);
        cal.set(MONTH, month);
        cal.set(DAY_OF_MONTH, 1);
        int dayOfWeek = cal.get(DAY_OF_WEEK);
        
        int index = cell(dayOfWeek);
    }
    
    public int cell(int day) {
        GregorianCalendar cal = new GregorianCalendar(locale);
        int translated = day - cal.getFirstDayOfWeek();
        return translated < 0 ? translated + 7 : translated;
    }
    
    public void addDayListener() {
    
    }
    
    public void removeDayListener() {
    }
    
    public interface DayListener {
        void onDaySelected(int year, int month, int day);
    }

}
