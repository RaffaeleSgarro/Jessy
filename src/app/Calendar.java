package app;

import dao.WorkHourDao;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

public class Calendar implements ViewFactory {
    
    private final WorkHourDao workHourDao;
    
    
    private final Logger log = Logger.getLogger("calendar");

    public Calendar(WorkHourDao workHourDao) {
        this.workHourDao = workHourDao;
    }
    
    @Override
    public Node build() {
        Date initialDay = new GregorianCalendar(2013, 10, 9).getTime();
        
        MonthDaysWidget calendarWidget = new MonthDaysWidget(initialDay, Locale.ITALIAN);
        final DayHours dayHours = new DayHours(workHourDao);
        dayHours.setDay(initialDay);
        
        HBox hbox = HBoxBuilder.create()
                .children(calendarWidget.getWidget(), dayHours.getWidget())
                .build();
        
        calendarWidget.dateProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> ov, Date oldVal, Date newVal) {
                dayHours.setDay(newVal);
            }
        });
        
        return hbox;
    }
    
}