package app;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class Calendar implements ViewFactory {

    @Override
    public Node build() {
        return new Label("Calendar");
    }
    
}