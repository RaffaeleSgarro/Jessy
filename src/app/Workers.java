package app;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class Workers implements ViewFactory {

    @Override
    public Node build() {
        return new Label("Workers");
    }
    
}
