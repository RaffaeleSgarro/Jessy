package jessy.controls.datepicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class DatePicker extends MenuButton {
    
    private final MonthDaysGrid cal;
    private final MenuItem calWrapper = new MenuItem();
    private final ObjectProperty<DateFormat> dateFormat = new SimpleObjectProperty<DateFormat>(new SimpleDateFormat("dd/MM/yyyy"));
    
    public DatePicker() {
        this(Locale.getDefault());
    }
    
    public DatePicker(final Locale locale) {
        Date today = new Date();
        
        cal = new MonthDaysGrid(locale, today);
        
        cal.selectedDateProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> observable, Date old, Date val) {
                setText(dateFormat.get().format(val));
                hide();
            }
        });
        
        calWrapper.getStyleClass().add("date-picker");
        calWrapper.setGraphic(cal.getWidget());
        
        showingProperty().addListener(new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old, Boolean val) {
                if (!val) return;
                Calendar calendar = GregorianCalendar.getInstance(locale);
                calendar.setTime(dateProperty().get());
                cal.view(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));         
            }
        });
        
        getItems().setAll(calWrapper);
    }
    
    public ObjectProperty<Date> dateProperty() {
        return cal.selectedDateProperty();
    }

}
