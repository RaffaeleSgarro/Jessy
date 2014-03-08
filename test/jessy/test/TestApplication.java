package jessy.test;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(TestCase.width);
        stage.setHeight(TestCase.height);
        stage.centerOnScreen();
        
        Scene scene = new Scene(TestCase.parent);
        stage.setScene(scene);
        
        stage.show();
    }
    
    public static final class TestCase {
        public static int width, height;
        public static Parent parent;
    }

}
