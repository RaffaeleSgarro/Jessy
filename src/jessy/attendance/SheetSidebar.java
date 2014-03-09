package jessy.attendance;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.inject.Inject;
import jessy.createworker.CreateWorker;
import jessy.misc.Listener;
import jessy.workers.Worker;
import jessy.workers.WorkersDao;

public class SheetSidebar extends VBox {
    
    private final WorkersDao workersDao;
    private final ListView<Worker> workers = new ListView<>();
    private final ObjectProperty<Set<String>> blacklist = new SimpleObjectProperty<>();
    
    @Inject
    public SheetSidebar(WorkersDao workersDao, ResourceBundle bundle) {
        this.workersDao = workersDao;
        blacklist.setValue(new HashSet<String>());
        setMaxHeight(Double.MAX_VALUE);
        
        CreateWorker createWorkerForm = new CreateWorker(workersDao, bundle);
        getChildren().setAll(new Label(bundle.getString("add")), workers, createWorkerForm);
        setSpacing(10);
        VBox.setVgrow(workers, Priority.ALWAYS);
        
        workers.setCellFactory(new Callback<ListView<Worker>, ListCell<Worker>>(){
            @Override
            public ListCell<Worker> call(ListView<Worker> listView) {
                return new ListCellImpl(listView);
            }
        });
        
        createWorkerForm.addListener(new Listener<Worker>(){
            @Override
            public void onEvent(Worker evt) {
                workers.getItems().add(evt);
            }
        });
        
        blacklist.addListener(new ChangeListener<Set<String>>(){
            @Override
            public void changed(ObservableValue<? extends Set<String>> obs, Set<String> old, Set<String> val) {
                filterWorkers(workers.getItems(), val);
            }
        });
    }
    
    public void load() throws SQLException {
        Collection<Worker> items = workersDao.findAll();
        filterWorkers(items, blacklist.get());
        workers.getItems().setAll(items);
    }
    
    private void filterWorkers(Collection<Worker> src, Set<String> blacklistedIds) {
        Iterator<Worker> it = src.iterator();
        while (it.hasNext()) {
            if (blacklistedIds.contains(it.next().id)) it.remove();
        }
    }
    
    public ObjectProperty<Set<String>> blacklistProperty() {
        return blacklist;
    }
    
    private class ListCellImpl extends ListCell<Worker> {
        
        private final ListView<Worker> listView;
        
        public ListCellImpl(ListView<Worker> listView) {
            this.listView = listView;
        }
        
        @Override
        public void updateItem(Worker worker, boolean empty) {
            super.updateItem(worker, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Button btn = new Button(String.format("%s, %s", worker.lastName, worker.firstName));
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setOnAction(removeWorkerFromListView(worker));
                setGraphic(btn);
            }
        }
        
        private EventHandler<ActionEvent> removeWorkerFromListView(final Worker worker) {
            return new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent t) {
                    listView.itemsProperty().get().remove(worker);
                }
            };
        }
    }

}
