package shared;

import app.MonthDaysWidget;
import java.util.Date;
import java.util.Locale;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Popup;
import javafx.stage.Window;

public class DatePicker extends MenuButton {
    
    private final MonthDaysWidget cal;
    private final MenuItem calWrapper = new MenuItem();
    
    public DatePicker() {
        Date date = new Date();
        cal = new MonthDaysWidget(date, Locale.getDefault());
        setText(date.toString());
        cal.dateProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> ov, Date t, Date t1) {
                setText(t1.toString());
            }
        });
        calWrapper.setGraphic(cal.getWidget());
        getItems().setAll(calWrapper);
    }

}
