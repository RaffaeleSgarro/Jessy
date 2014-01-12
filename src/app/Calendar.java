package app;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

public class Calendar implements ViewFactory {
    
    private final Logger log = Logger.getLogger("calendar");

    @Override
    public Node build() {
        MonthDaysWidget calendarWidget = new MonthDaysWidget(new Date(), Locale.ITALIAN);
        HBox hbox = HBoxBuilder.create()
                .children(calendarWidget.getWidget())
                .build();
        calendarWidget.dateProperty().addListener(new ChangeListener<Date>() {

            @Override
            public void changed(ObservableValue<? extends Date> ov, Date oldVal, Date newVal) {
                log.info(DateFormat.getDateInstance().format(newVal));
            }
        });
        
        return hbox;
    }
    
}