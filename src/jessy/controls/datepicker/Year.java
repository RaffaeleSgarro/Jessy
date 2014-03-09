package jessy.controls.datepicker;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class Year extends HBox {
    
    private final SimpleIntegerProperty valueProperty = new SimpleIntegerProperty(0);
    
    private final Button prevYearBtn = new Button("<");
    private final Button nextYearBtn = new Button(">");
    private final Label yearDisplay = new Label();

    
    public Year() {
        getChildren().setAll(prevYearBtn, yearDisplay, nextYearBtn);
        HBox.setHgrow(yearDisplay, Priority.SOMETIMES);
        setSpacing(10);
        
        prevYearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                valueProperty.set(valueProperty.get() - 1);
            }
        });
        
        nextYearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                valueProperty.set(valueProperty.get() + 1);
            }
        });
        
        yearDisplay.textProperty().bind(valueProperty.asString());
    }
    
    public IntegerProperty valueProperty() {
        return valueProperty;
    }
    
}
