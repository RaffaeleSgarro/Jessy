package app;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.GregorianCalendar;
import java.util.Locale;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;

import static java.util.Calendar.*;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;

/**
 * The grid has 7 columns (one for each week day) and 6 rows (because a month
 * spans 6 weeks at most).
 * 
 * Top left is (0, 0)
 * 
 * @author admin
 */
public class MonthDaysWidget {
    
    private final ObjectProperty<Date> dateProperty = new SimpleObjectProperty<>(new Date());
    // 1 - 12
    private final IntegerProperty monthProperty = new SimpleIntegerProperty();
    // 2000, 2013, ...
    private final IntegerProperty yearProperty = new SimpleIntegerProperty();
    
    private final Label displayDay = new Label();
    
    private final Label yearDisplay = new Label();
    private final ChoiceBox monthDisplay = new ChoiceBox();
    private final Button prevYearBtn = new Button("<");
    private final Button nextYearBtn = new Button(">");
    
    private final Locale locale;
    private final VBox root;
    private final DayCell[] days = new DayCell[7 * 6];
      
    public MonthDaysWidget(Date initialDate, Locale locale) {
        this.locale = locale;
        
        GregorianCalendar cal = new GregorianCalendar(locale);
        cal.setTime(initialDate);
        dateProperty.setValue(initialDate);
        monthProperty.set(cal.get(MONTH) + 1);
        yearProperty.set(cal.get(YEAR));
        
        GridPane grid = GridPaneBuilder.create().build();
        Insets cellsMargin = new Insets(10, 10, 10, 10);
        
        for (int row = 0; row < 6; row++) {
            for (int column  = 0; column < 7; column++ ) {
                final DayCell cell = new DayCell();
                grid.add(cell, column, row);
                GridPane.setMargin(cell, cellsMargin);
                days[idx(row, column)] = cell;
                cell.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        if (cell.isInEnabledMode()) {
                            GregorianCalendar cal = new GregorianCalendar(MonthDaysWidget.this.locale);
                            cal.set(YEAR, getYear());
                            cal.set(MONTH, getMonth() - 1);
                            cal.set(DAY_OF_MONTH, cell.dayProperty().get());
                            dateProperty.setValue(cal.getTime());
                        }
                    }
                });
            }
        }
        
        root = VBoxBuilder.create()
                .children(displayDay, monthDisplay
                        , HBoxBuilder.create().children(
                                prevYearBtn, yearDisplay, nextYearBtn).build()
                        , grid)
                .build();
        
        String[] monthsLabels = new DateFormatSymbols(locale).getMonths();
        for (int i = 0; i < 12; i++) {            
            monthDisplay.getItems().add(monthsLabels[i]);
        }
        
        prevYearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                yearProperty.set(getYear() - 1);
            }
        });
        
        nextYearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                yearProperty.set(getYear() + 1);
            }
        });
        
        displayDay.textProperty().bindBidirectional(dateProperty, DateFormat.getDateInstance());
        yearDisplay.textProperty().bind(yearProperty.asString());
        
        monthDisplay.getSelectionModel().select(monthProperty.get() - 1);
        monthDisplay.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
                monthProperty.set(newVal.intValue() + 1);
            }
        });
        
        ChangeListener<Object> repaint = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> ov, Object t, Object t1) {
                paintGrid();
            }
        };
        
        monthProperty.addListener(repaint);
        yearProperty.addListener(repaint);
        
        paintGrid();
    }
    
    public Node getWidget() {
        return root;
    }
    
    private static class DayCell extends Button {
        
        private final IntegerProperty dayProperty = new SimpleIntegerProperty(0);
        private boolean mEnabled;
        
        public IntegerProperty dayProperty() {
            return dayProperty;
        }
        
        public boolean isInEnabledMode() {
            return mEnabled;
        }
        
        public void putInEnabledMode(int day) {
            mEnabled = true;
            dayProperty.set(day);
            textProperty().set(Integer.toString(day));
        }
        
        public void putInDisabledMode() {
            mEnabled = false;
            textProperty().set("");
        }
        
    }
    
    private void paintGrid() {
        GregorianCalendar cal = new GregorianCalendar(locale);
        cal.set(YEAR, yearProperty.get());
        cal.set(MONTH, monthProperty.get() - 1);
        cal.set(DAY_OF_MONTH, 1);
        int dayOfWeek = cal.get(DAY_OF_WEEK);
        
        int indexOfFirstDayOfMonth = column(dayOfWeek);
        int maxDayOfMonth = cal.getActualMaximum(DAY_OF_MONTH);
        int currentDay = 0;
        
        for (int row = 0; row < 6; row++) {
            for (int column  = 0; column < 7; column++ ) {
                DayCell cell = days[idx(row, column)];
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
     * 
     * @param row index, ranges [0 - 5]
     * @param column index ranges [0, 6]
     * @return the index of the cell in the 1-dimensional array of cells held
     *     by the widget
     */
    private int idx(int row, int column) {
        return row * 7 + column;
    }
    
    /**
     * 
     * @param day the day of the week, as seen in Calendar. 1-based, SUNDAY is 1
     * @return the column index (0-based) in the grid according to the Locale
     */
    public int column(int day) {
        GregorianCalendar cal = new GregorianCalendar(locale);
        int translated = day - cal.getFirstDayOfWeek();
        return translated < 0 ? translated + 7 : translated;
    }
    
    private int getMonth() {
        return monthProperty.get();
    }
    
    private int getYear() {
        return yearProperty.get();
    }
    
    public ObjectProperty<Date> dateProperty() {
        return dateProperty;
    }

}
