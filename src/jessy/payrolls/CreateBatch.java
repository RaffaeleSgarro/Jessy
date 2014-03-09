package jessy.payrolls;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javax.inject.Inject;
import jessy.controls.datepicker.DatePicker;
import jessy.misc.Listener;

class CreateBatch extends HBox {
    
    private final List<Listener<BatchProcessing>> btnListeners = new ArrayList<>();
    private final DatePicker startDate = new DatePicker();
    private final DatePicker endDate = new DatePicker();
    private final TextField name = new TextField();
    private final Button create;
    
    @Inject
    public CreateBatch(ResourceBundle bundle) {
        create = new Button(bundle.getString("new"));
        setSpacing(10);
        setMaxWidth(Double.MAX_VALUE);
        Region spacer = new Region();
        getChildren().setAll(
                  new Label(bundle.getString("start_date")), startDate
                , new Label(bundle.getString("end_date")), endDate
                , new Label("name"), name
                , spacer
                , create);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        create.setOnAction(onCreateBtnClicked);
    }
    
    private final EventHandler<ActionEvent> onCreateBtnClicked = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            BatchProcessing batch = new BatchProcessing();
            batch.setTitle(name.getText());
            batch.setStart(startDate.dateProperty().get());
            batch.setEnd(endDate.dateProperty().get());
            for (Listener<BatchProcessing> l : btnListeners) l.onEvent(batch);
        }
    };
    
    public void addOnCreateClickedListener(Listener<BatchProcessing> l) {
        btnListeners.add(l);
    }
    
    public boolean removeOnCreateClickedListener(Listener<BatchProcessing> l) {
        return btnListeners.remove(l);
    }
}
