package jessy.workers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.inject.Inject;
import jessy.misc.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchWorker extends VBox {
    
    public final Collection<Listener<Worker>> workerClickListeners = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(SearchWorker.class);

    private final WorkersDao dao;
    private final ListView<Worker> list = new ListView<>();
    private final TextField search = new TextField();
    private final Button showAll;

    @Inject
    public SearchWorker(WorkersDao dao, ResourceBundle bundle) {
        this.dao = dao;
        showAll = new Button(bundle.getString("show_all"));
        setSpacing(10);
        HBox searchBar = new HBox();

        getChildren().setAll(searchBar, list);
        VBox.setVgrow(list, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        searchBar.getChildren().setAll(search, showAll);
        searchBar.setSpacing(10);

        HBox.setHgrow(search, Priority.ALWAYS);

        showAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                showAll();
            }
        });

        search.textProperty().addListener(new SearchTextChangeListener());
        list.cellFactoryProperty().set(new WorkerCellFactory());
        
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Worker>(){
            @Override
            public void changed(ObservableValue<? extends Worker> ov, Worker oldValue, Worker newValue) {
                if (newValue != null) {
                    for (Listener<Worker> listener : workerClickListeners) listener.onEvent(newValue);
                }
            }
        });
    }

    public void showAll() {
        try {
            list.getItems().setAll(dao.findAll());
        } catch (SQLException e) {
            log.error("Could not find workers", e);
            throw new RuntimeException(e);
        }
    }

    public void showMatching(String input) {
        try {
            list.getItems().setAll(dao.findByName(input));
        } catch (SQLException e) {
            log.error("Could not searching " + input, e);
            throw new RuntimeException(e);
        }
    }
    
    public void clearSelection() {
        list.getSelectionModel().clearSelection();
    }

    private final List<TimerTask> pendingSearches = new ArrayList<>();

    private class SearchTextChangeListener implements ChangeListener<String> {

        private final Timer timer = new Timer();

        @Override
        public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
            if (newValue != null && !newValue.equals(oldValue)) {
                for (TimerTask task : pendingSearches) {
                    task.cancel();
                }
                pendingSearches.clear();
                FindWorker findWorkerTask = new FindWorker(newValue);
                pendingSearches.add(findWorkerTask);
                timer.schedule(findWorkerTask, 300);
            }
        }
    }

    private class FindWorker extends TimerTask {

        private final String input;

        public FindWorker(String input) {
            this.input = input;
        }

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pendingSearches.remove(FindWorker.this);
                    showMatching(input);
                }
            });
        }
    }

    private class WorkerCellFactory implements Callback<ListView<Worker>, ListCell<Worker>> {

        @Override
        public ListCell<Worker> call(ListView<Worker> p) {
            return new WorkerListCell();
        }
    }

    private class WorkerListCell extends ListCell<Worker> {

        @Override
        protected void updateItem(Worker worker, boolean empty) {
            super.updateItem(worker, empty);
            if (empty) {
                setText("");
            } else {
                setFocusTraversable(true);
                setText(String.format("%s, %s", worker.lastName, worker.firstName));
            }
        }
    }

}
