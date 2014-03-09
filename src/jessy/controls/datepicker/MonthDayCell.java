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
    
    private final IntegerProperty dayProperty = new SimpleIntegerProperty(0);
    private boolean mEnabled;
    private final List<Listener<Integer>> daySelectedListeners = new ArrayList<>();

    public MonthDayCell() {
        setPrefWidth(48);
        setPrefHeight(32);
        setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                if (isInEnabledMode()) {
                    for (Listener<Integer> l : daySelectedListeners)
                        l.onEvent(dayProperty.get());
                }
            }
        });
    }
    
    public void addDaySelectedListener(Listener<Integer> listener) {
        daySelectedListeners.add(listener);
    }
    
    public void removeDaySelectedListener(Listener<Integer> listener) {
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
    }

    public void putInDisabledMode() {
        mEnabled = false;
        textProperty().set("");
    }

}
