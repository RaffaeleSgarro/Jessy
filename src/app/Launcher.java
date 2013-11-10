package app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

public class Launcher extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        ToolBar toolbar = new ToolBar();
        StackPane main = new StackPane();
        
        VBox root = VBoxBuilder.create().children(
            toolbar, main
        ).build();
        
        Button btn1 = new Button("Calendar");
        btn1.setOnAction(new SetMainContent(main, "Calendar"));
        Button btn2 = new Button("Workers");
        btn2.setOnAction(new SetMainContent(main, "Worker"));
        
        toolbar.getItems().addAll(btn1, btn2);
        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("APP.NAME");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private static class SetMainContent implements EventHandler<ActionEvent> {
        
        private final StackPane main;
        private final String content;
        
        private SetMainContent(StackPane main, String content) {
            this.main = main;
            this.content = content;
        }

        @Override
        public void handle(ActionEvent t) {
            main.getChildren().setAll(new Label(content));
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
