package app;

import beans.Worker;
import dao.WorkerDao;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.Callback;
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
        
        final TextField firstName = new TextField();
        final TextField lastName = new TextField();
        Button createWorkerBtn = new Button("New");
        
        Button saveWorkerBtn = new Button("Save");
        Button resetWorkerBtn = new Button("Reset");
        Button deleteWorkerBtn = new Button("Delete");
        Node workerButtons = HBoxBuilder.create().children(saveWorkerBtn, resetWorkerBtn, deleteWorkerBtn).build();
        
        ListView<Worker> listView = new ListView<>();
        VBox workerForm = VBoxBuilder.create().children(createWorkerBtn, firstName, lastName, workerButtons).build();
        hbox.getChildren().addAll(listView, workerForm);
        
        listView.setItems(FXCollections.observableList(workerDao.findAll()));
        
        listView.setCellFactory(new Callback<ListView<Worker>, ListCell<Worker>>() {
            @Override
            public ListCell<Worker> call(ListView<Worker> list) {
                return new ListCell<Worker>() {
                    @Override
                    public void updateItem(Worker item, boolean empty) {
                        super.updateItem(item, empty);
                        if ( empty ) return;
                        
                        Label fullNameLbl = LabelBuilder.create().build();
                        fullNameLbl.textProperty().bind(Bindings.concat(item.firstNameProperty(), " ", item.lastNameProperty()));
                        setGraphic(fullNameLbl);
                    }
                    
                };
            }
        });
        
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Worker>(){
            @Override
            public void changed(ObservableValue<? extends Worker> observable, Worker oldVal, Worker newVal) {
                if ( oldVal != null ) {
                    firstName.textProperty().unbindBidirectional(oldVal.firstNameProperty());
                    lastName.textProperty().unbindBidirectional(oldVal.lastNameProperty());
                } if ( newVal != null ) {
                    firstName.textProperty().bindBidirectional(newVal.firstNameProperty());
                    lastName.textProperty().bindBidirectional(newVal.lastNameProperty());
                }
            }
        });
        
        return hbox;
    }
    
}
