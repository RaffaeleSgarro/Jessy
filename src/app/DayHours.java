package app;

import beans.WorkHour;
import dao.WorkHourDao;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.Callback;

public class DayHours {
    
    private final ListView<WorkHour> list = new ListView<>();
    
    private final Button saveBtn = new Button("Salva");
    private final Button cancelBtn = new Button("Annulla");
    private final WorkHourDao workHourDao;
    
    private final Map<String, WorkHour> hoursData = new HashMap<>();
    
    private Date day;
    
    public DayHours(WorkHourDao workHourDao) {
        
        this.workHourDao = workHourDao;
        
        list.cellFactoryProperty().set(new DayHoursCellFactory());
        
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                
            }
        });
        
        cancelBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                reload();
            }
        });
        
        reload();
    }
    
    private void reload() {
        if (day == null) {
            saveBtn.disarm();
            cancelBtn.disarm();
            list.getItems().clear();
        } else {
            saveBtn.arm();
            cancelBtn.arm();
            list.getItems().setAll(workHourDao.findWorkedHours(day));
        }
    }
    
    public Node getWidget() {
        return VBoxBuilder.create().children(
                list, HBoxBuilder.create().children(saveBtn, cancelBtn).build())
                .build();
    }
    
    public void setDay(Date day) {
        this.day = day;
        reload();
    }

    private static class DayHoursCellFactory implements Callback<ListView<WorkHour>, ListCell<WorkHour>> {

        @Override
        public ListCell<WorkHour> call(ListView<WorkHour> p) {
            return new WorkHourCell();
        }
    }
    
    private static class WorkHourCell extends ListCell<WorkHour> {
        @Override
        public void updateItem(WorkHour item, boolean empty) {
            if (empty) {
                setGraphic(new Label());
            } else {
                Label worker = new Label(item.worker.getLastName() + " " + item.worker.getFirstName());
                TextField hours = new TextField(item.hours.toString());
                hours.setPrefWidth(25);
                setGraphic(HBoxBuilder.create().children(worker, hours).build());
            }
        }    
    }
    
}
