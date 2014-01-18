package app;

import beans.WorkHour;
import java.util.List;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.Callback;

public class SelectHours {
    
    private final Button saveBtn = new Button("Salva");
    private final Button cancelBtn = new Button("Annulla");
    private final Label total = new Label("Totale");
    private final ListView<WorkHour> hours = new ListView<>();
    private final ObservableSet<WorkHour> selectedHoursProperty = FXCollections.observableSet();
    
    public SelectHours() {
        hours.setCellFactory(new Callback<ListView<WorkHour>, ListCell<WorkHour>>() {

            @Override
            public ListCell<WorkHour> call(ListView<WorkHour> p) {
                return new SelectHourCell(selectedHoursProperty);
            }
        });
    }

    void setHours(List<WorkHour> unpaid) {
        hours.getItems().setAll(unpaid);
    }
    
    private static class SelectHourCell extends ListCell<WorkHour> {
        
        private final Set<WorkHour> ctx;
        
        public SelectHourCell(Set<WorkHour> ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void updateItem(final WorkHour item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                CheckBox checkbox = new CheckBox();
                checkbox.selectedProperty().addListener(new ChangeListener<Boolean>(){
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                        if (t1) {
                            ctx.add(item);
                        } else {
                            ctx.remove(item);
                        }
                    }
                });
                String text = item.date.toString() + " (" + item.hours + ")";
                setGraphic(HBoxBuilder.create().children(checkbox, new Label(text)).build());
            } else {
                setText("");
            }
        }
    }
    
    public Node getWidget() {
        return VBoxBuilder.create().children(total, hours, HBoxBuilder.create().children(saveBtn, cancelBtn).build()).build();
    }
    
    public ObservableSet<WorkHour> getSelectedHours() {
        return selectedHoursProperty;
    }
    
}
