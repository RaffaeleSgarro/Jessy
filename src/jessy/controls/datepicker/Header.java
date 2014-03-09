package jessy.controls.datepicker;

import java.util.Locale;
import javafx.beans.value.ObservableIntegerValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jessy.utils.DateUtils;

class Header extends HBox {
    
    private final Year year = new Year();
    private final ChoiceBox<String> month = new ChoiceBox<>();
    
    public Header(Locale locale) {
        setMaxWidth(Double.MAX_VALUE);
        setSpacing(10);
        getChildren().setAll(month, year);
        month.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(month, Priority.ALWAYS);
        month.getItems().setAll(DateUtils.findLongMonthsNames(locale));
    }
    
    /**
     * 
     * @param monthIdx, the 0-based index of the month to be selected
     * @param year 
     */
    public void select(int monthIdx, int yearValue) {
        month.getSelectionModel().select(monthIdx);
        year.valueProperty().set(yearValue);
    }
    
    public ObservableIntegerValue selectedMonthProperty() {
        return month.getSelectionModel().selectedIndexProperty().add(1);
    }
    
    public ObservableIntegerValue selectedYearProperty() {
        return year.valueProperty();
    }
}
