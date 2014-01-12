package app;

import java.util.GregorianCalendar;
import java.util.Locale;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;

import static java.util.Calendar.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;

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
        Insets cellsMargin = new Insets(10, 10, 10, 10);
        for (int row = 0; row < 6; row++) {
            for (int column  = 0; column < 7; column++ ) {
                DayCell cell = new DayCell();
                grid.add(cell, column, row);
                GridPane.setMargin(cell, cellsMargin);
                days[cellIdx(row, column)] = cell;
            }
        }
    }
    
    public Node getWidget() {
        return grid;
    }
    
    private static class DayCell extends Label {
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
        
        int indexOfFirstDayOfMonth = cell(dayOfWeek);
        int maxDayOfMonth = cal.getMaximum(DAY_OF_MONTH);
        int currentDay = 0;
        
        for (int row = 0; row < 6; row++) {
            for (int column  = 0; column < 7; column++ ) {
                DayCell cell = days[cellIdx(row, column)];
                if (row == 0 && column < indexOfFirstDayOfMonth) {
                    cell.setText("");
                } else if (currentDay < maxDayOfMonth) {
                    currentDay += 1;
                    cell.setText(Integer.toString(currentDay));
                } else {
                    cell.setText("");
                }
            }
        }
        
    }
    
    private int cellIdx(int row, int column) {
        return row * 7 + column;
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
