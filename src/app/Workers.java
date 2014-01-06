package app;

import beans.Worker;
import dao.WorkerDao;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
        VBox vbox = new VBox();
        final TextField search = new TextField();
        final Button showAllBtn = new Button("Show all");
        final Button createWorkerBtn = new Button("New");
        
        HBox hbox = new HBox();
        vbox.getChildren().addAll(
            HBoxBuilder.create().children(search, showAllBtn, createWorkerBtn).build()
            , hbox
        );
        
        final ObjectProperty<Worker> workerProperty = new SimpleObjectProperty();
        final TextField firstName = new TextField();
        final TextField lastName = new TextField();
        
        final Button saveWorkerBtn = new Button("Save");
        final Button resetWorkerBtn = new Button("Reset");
        final Button deleteWorkerBtn = new Button("Delete");
        
        Node workerButtons = HBoxBuilder.create().children(saveWorkerBtn, resetWorkerBtn, deleteWorkerBtn).build();
        
        final ListView<Worker> listView = new ListView<>();
        VBox workerForm = VBoxBuilder.create().children(firstName, lastName, workerButtons).build();
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
                workerProperty.set(newVal);
                    
                if ( newVal != null ) {
                    firstName.textProperty().set(newVal.getFirstName());
                    lastName.textProperty().set(newVal.getLastName());
                    firstName.setDisable(false);
                    lastName.setDisable(false);
                    saveWorkerBtn.setDisable(false);
                    deleteWorkerBtn.setDisable(false);
                    resetWorkerBtn.setDisable(false);
                } else {
                    firstName.textProperty().set("");
                    lastName.textProperty().set("");
                    firstName.setDisable(true);
                    lastName.setDisable(true);
                    saveWorkerBtn.setDisable(true);
                    deleteWorkerBtn.setDisable(true);
                    resetWorkerBtn.setDisable(true);
                }
            }
        });
        
        createWorkerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                workerProperty.setValue(null);
                firstName.textProperty().set("");
                lastName.textProperty().set("");
                firstName.setDisable(false);
                lastName.setDisable(false);
                saveWorkerBtn.setDisable(false);
                deleteWorkerBtn.setDisable(true);
                resetWorkerBtn.setDisable(false);   
            }
        });
        
        saveWorkerBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            private final Random r = new Random();
            
            @Override
            public void handle(ActionEvent t) {
                Worker newWorker = workerProperty.getValue();
                
                if ( newWorker == null ) {
                    newWorker = new Worker();
                }
                
                newWorker.setFirstName(firstName.getText());
                newWorker.setLastName(lastName.getText());
                
                // TODO save in the db
                if ( workerProperty.getValue() == null ) {
                    listView.itemsProperty().get().add(newWorker);
                }
                
                listView.getSelectionModel().select(newWorker);
            }
        });
        
        listView.getSelectionModel().select(null);
        
        return vbox;
    }
    
}
