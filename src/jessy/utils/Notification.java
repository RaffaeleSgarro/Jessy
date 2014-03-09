package jessy.utils;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Notification extends Stage {
    
    private final AnchorPane pane = new AnchorPane();
    
    public Notification() {
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(pane);
        setScene(scene);
        setWidth(400);
        setHeight(200);
    }
    
    public void setTextMessage(String msg) {
        pane.getChildren().setAll(new Label(msg));
    }

}