package jessy.controls.datepicker;

import java.util.GregorianCalendar;
import java.util.Locale;
import javafx.scene.layout.GridPane;
import java.util.Date;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jessy.misc.Listener;
import jessy.utils.DateUtils;

import static java.util.Calendar.*;

/**
 * The grid has 7 columns (one for each week day) and 6 rows (because a month
 * spans 6 weeks at most).
 * 
 * Top left is (0, 0)
 * 
 * @author raffaele
 */
public class MonthDaysGrid {
    
    private final ObjectProperty<Date> selectedDateProperty = new SimpleObjectProperty<>(new Date());
    // 1 - 12
    private final IntegerProperty viewedMonthProperty = new SimpleIntegerProperty();
    // 2000, 2013, ...
    private final IntegerProperty viewedYearProperty = new SimpleIntegerProperty();
    
    private final Locale locale;
    private final Header header;
    private final VBox root = new VBox();
    private final MonthDayCell[] days = new MonthDayCell[7 * 6];
    
    public MonthDaysGrid(Locale locale, Date selectedDate) {
        this(locale);
        selectedDateProperty.set(selectedDate);
    }
      
    public MonthDaysGrid(Locale locale) {
        this.locale = locale;
        
        header = new Header(locale);
        GridPane grid = new GridPane();
        root.getChildren().setAll(header, grid);
        root.setSpacing(10);
        
        // Draw the grid headers
        List<String> shortWeekDays = DateUtils.findAbbrevWeekDaysNames(locale);
        for (int col = 0; col < 7; col++) {
            grid.add(new Label(shortWeekDays.get(col + 1)), col, 0);
        }
        
        // Build the cells, register and show
        for (int row = 0; row < 6; row++) {
            for (int column  = 0; column < 7; column++ ) {
                final MonthDayCell cell = new MonthDayCell();
                grid.add(cell, column, row + 1);
                days[idx(row, column)] = cell;
                cell.addDaySelectedListener(onDaySelected);
            }
        }
        
        viewedMonthProperty.bind(header.selectedMonthProperty());
        viewedYearProperty.bind(header.selectedYearProperty());
        
        viewedMonthProperty.addListener(onViewedMonthChanged);
        viewedYearProperty.addListener(onViewedYearChanged);
        selectedDateProperty.addListener(onSelectedDateChanged);
    }
    
    private final ChangeListener<Date> onSelectedDateChanged = new ChangeListener<Date>() {
        @Override
        public void changed(ObservableValue<? extends Date> observablem, Date old, Date val) {
            GregorianCalendar cal = cal();
            cal.setTime(val);
            int month = cal.get(MONTH);
            int year = cal.get(YEAR);
            header.select(month, year);
            refreshCellsLabels(year, month);
        }
    };
    
    private final ChangeListener<Number> onViewedYearChanged = new ChangeListener<Number>() {
       @Override public void changed(ObservableValue<? extends Number> observable, Number old, Number val) {
           refreshCellsLabels(val.intValue(), viewedMonthProperty.get());
       }
    };
    
    private final ChangeListener<Number> onViewedMonthChanged = new ChangeListener<Number>() {
       @Override public void changed(ObservableValue<? extends Number> observable, Number old, Number val) {
           refreshCellsLabels(viewedYearProperty.get(), val.intValue());
       }
    };
    
    private final Listener<Integer> onDaySelected = new Listener<Integer>() {
        @Override public void onEvent(Integer day) {
            GregorianCalendar cal1 = cal();
            cal1.set(YEAR, viewedYearProperty.get());
            cal1.set(MONTH, viewedMonthProperty.get() - 1);
            cal1.set(DAY_OF_MONTH, 1);
            GregorianCalendar cal = cal1;
            cal.set(DAY_OF_MONTH, day);
            selectedDateProperty.setValue(cal.getTime());
        }
    };
    
    public Node getWidget() {
        return root;
    }
    
    private GregorianCalendar cal() {
        return new GregorianCalendar(locale);
    }
    
    /**
     * Refresh the cells in the calendar grid
     * TODO manage style class for selected cell
     * 
     * @param year the target year
     * @param month the 1-based index of the month, January is 1, Feb is 2, ...
     */
    private void refreshCellsLabels(int year, int month) {
        GregorianCalendar cal = cal();
        cal.set(YEAR, year);
        cal.set(MONTH, month - 1);
        cal.set(DAY_OF_MONTH, 1);
        int dayOfWeek = cal.get(DAY_OF_WEEK);
        
        int indexOfFirstDayOfMonth = column(dayOfWeek);
        int maxDayOfMonth = cal.getActualMaximum(DAY_OF_MONTH);
        int currentDay = 0;
        
        for (int row = 0; row < 6; row++) {
            for (int column  = 0; column < 7; column++ ) {
                MonthDayCell cell = days[idx(row, column)];
                if (row == 0 && column < indexOfFirstDayOfMonth) {
                    cell.putInDisabledMode();
                } else if (currentDay < maxDayOfMonth) {
                    currentDay += 1;
                    cell.putInEnabledMode(currentDay);
                } else {
                    cell.putInDisabledMode();
                }
            }
        }
    }
    
    /**
     * Internally, calendar cells are kept in an array data structure. This
     * method is a convenience for getting the index of the cell in the array,
     * known its (x, y) coordinates in the MonthDays grid
     * 
     * @param row index, ranges [0 - 5]
     * @param column index ranges [0 - 6]
     * @return the index of the cell in the 1-dimensional array of cells held
     *     by the widget
     */
    private int idx(int row, int column) {
        return row * 7 + column;
    }
    
    /**
     * Tell what column the given day (Calendar.SUNDAY, Calendar.MONDAY, ...) has
     * in the grid according to the widget Locale
     * 
     * @param day the day of the week, as seen in Calendar. 1-based, SUNDAY is 1
     * @return the column index (0-based) in the grid according to the Locale
     */
    public int column(int day) {
        GregorianCalendar cal = new GregorianCalendar(locale);
        int translated = day - cal.getFirstDayOfWeek();
        return translated < 0 ? translated + 7 : translated;
    }
    
    public ObjectProperty<Date> selectedDateProperty() {
        return selectedDateProperty;
    }

}
