package jessy.controls.datepicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class DatePicker extends MenuButton {
    
    private final MonthDaysGrid cal;
    private final MenuItem calWrapper = new MenuItem();
    private final ObjectProperty<DateFormat> dateFormat = new SimpleObjectProperty<DateFormat>(new SimpleDateFormat("dd/MM/yyyy"));
    
    public DatePicker() {
        Date today = new Date();
        
        cal = new MonthDaysGrid(Locale.getDefault(), today);
        
        cal.selectedDateProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> observable, Date old, Date val) {
                setText(dateFormat.get().format(val));
                hide();
            }
        });
        
        calWrapper.getStyleClass().add("date-picker");
        calWrapper.setGraphic(cal.getWidget());
        getItems().setAll(calWrapper);
    }
    
    public ObjectProperty<Date> dateProperty() {
        return cal.selectedDateProperty();
    }

}
