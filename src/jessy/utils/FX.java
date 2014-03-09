package jessy.utils;

import javafx.concurrent.Task;
import javafx.stage.Modality;


public class FX {
    
    void showBlockingMessage(Task<?> task) {
        Notification n = new Notification();
        n.setTextMessage("Loading...");
        n.initModality(Modality.APPLICATION_MODAL);
        n.show();
        Thread thread = new Thread(task);
        thread.start();
    }
    
}
