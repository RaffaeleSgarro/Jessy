package app;

import java.util.Locale;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

public class Calendar implements ViewFactory {

    @Override
    public Node build() {
        MonthDaysWidget calendarWidget = new MonthDaysWidget(Locale.ITALIAN);
        HBox hbox = HBoxBuilder.create()
                .children(calendarWidget.getWidget())
                .build();
        calendarWidget.setMonth(1, 2014);
        return hbox;
    }
    
}