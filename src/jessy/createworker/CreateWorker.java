package jessy.createworker;

import com.google.inject.Inject;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import jessy.misc.Listener;
import jessy.workers.Worker;
import jessy.workers.WorkersDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateWorker extends VBox {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private final WorkersDao dao;
    
    private final TextField firstName = new TextField();
    private final TextField lastName = new TextField();
    private final Button createBtn;
    
    private final Set<Listener<Worker>> createListeners = new HashSet<>();
    
    @Inject
    public CreateWorker(WorkersDao dao, ResourceBundle bundle) {
        this.dao = dao;
        
        createBtn = new Button(bundle.getString("create"));
        getChildren().setAll(firstName, lastName, createBtn);
       
        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onCreateBtnClicked(event);
            }
        });
        
        firstName.setPromptText(bundle.getString("first_name"));
        lastName.setPromptText(bundle.getString("last_name"));
        
        firstName.setMaxWidth(Double.MAX_VALUE);
        lastName.setMaxWidth(Double.MAX_VALUE);
        createBtn.setMaxWidth(Double.MAX_VALUE);
        
        setSpacing(10);
    }
    
    public void onCreateBtnClicked(ActionEvent event) {
        try {
            dao.create(firstName.getText(), lastName.getText());
            firstName.clear();
            lastName.clear();
        } catch (SQLException e) {
            log.error("Could not create worker", e);
            throw new RuntimeException(e);
        }
    }
    
    public void addListener(Listener<Worker> l) {
        createListeners.add(l);
    }
    
    public boolean removeListener(Listener<Worker> l) {
        return createListeners.remove(l);
    }

}
