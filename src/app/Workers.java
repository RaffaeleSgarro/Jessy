package app;

import dao.WorkerDao;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javax.sql.DataSource;

public class Workers extends ControllerBase implements ViewFactory {

    private final WorkerDao workerDao;
    
    public Workers(ResourceBundle res, DataSource ds) {
        super(res);
        workerDao = new WorkerDao(ds);
    }
    
    @Override
    public Node build() {
        HBox hbox = new HBox();
        
        TextField firstName = new TextField();
        TextField lastName = new TextField();
        Button createWorkerBtn = new Button("New");
        
        Button saveWorkerBtn = new Button("Save");
        Button resetWorkerBtn = new Button("Reset");
        Button deleteWorkerBtn = new Button("Delete");
        Node workerButtons = HBoxBuilder.create().children(saveWorkerBtn, resetWorkerBtn, deleteWorkerBtn).build();
        
        ListView listView = new ListView();
        VBox workerForm = VBoxBuilder.create().children(createWorkerBtn, firstName, lastName, workerButtons).build();
        hbox.getChildren().addAll(listView, workerForm);
        
        listView.setItems(FXCollections.observableList(workerDao.findAll()));
        
        
        return hbox;
    }
    
}
