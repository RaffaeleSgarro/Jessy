package jessy.controls.datepicker;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import jessy.misc.Listener;

class MonthDayCell extends Button {
    
    public static final String STYLE_ENABLED = "enabled";    
    public static final String STYLE_DISABLED = "disabled";

    private final IntegerProperty dayProperty = new SimpleIntegerProperty(0);
    private boolean mEnabled;
    private final List<Listener<MonthDayCell>> daySelectedListeners = new ArrayList<>();

    public MonthDayCell() {
        getStyleClass().add("month-day-cell");
        setPrefWidth(48);
        setPrefHeight(32);
        
        setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                if (isInEnabledMode()) {
                    for (Listener<MonthDayCell> l : daySelectedListeners)
                        l.onEvent(MonthDayCell.this);
                }
            }
        });
    }
    
    public void addDaySelectedListener(Listener<MonthDayCell> listener) {
        daySelectedListeners.add(listener);
    }
    
    public void removeDaySelectedListener(Listener<MonthDayCell> listener) {
        daySelectedListeners.remove(listener);
    }

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
        getStyleClass().add(STYLE_ENABLED);
        getStyleClass().remove(STYLE_DISABLED);
    }

    public void putInDisabledMode() {
        mEnabled = false;
        textProperty().set("");
        getStyleClass().remove(STYLE_ENABLED);
        getStyleClass().add(STYLE_DISABLED);
    }

}
