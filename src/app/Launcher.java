package app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        
        Button calendarBtn = new Button("Calendar");
        calendarBtn.setOnAction(new SetMainContent(main, new Calendar()));
        Button workersBtn = new Button("Workers");
        workersBtn.setOnAction(new SetMainContent(main, new Workers()));
        
        toolbar.getItems().addAll(calendarBtn, workersBtn);
        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("APP.NAME");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private static class SetMainContent implements EventHandler<ActionEvent> {
        
        private final StackPane main;
        private final ViewFactory factory;
        
        private SetMainContent(StackPane main, ViewFactory factory) {
            this.main = main;
            this.factory = factory;
        }

        @Override
        public void handle(ActionEvent t) {
            main.getChildren().setAll(factory.build());
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
