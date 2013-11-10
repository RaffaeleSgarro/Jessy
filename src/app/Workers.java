package app;

import dao.WorkerDao;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javax.sql.DataSource;

public class Workers implements ViewFactory {

    private final WorkerDao workerDao;
    
    public Workers(DataSource ds) {
        workerDao = new WorkerDao(ds);
    }
    
    @Override
    public Node build() {
        ListView listView = new ListView();
        listView.setItems(FXCollections.observableList(workerDao.findAll()));
        return listView;
    }
    
}
