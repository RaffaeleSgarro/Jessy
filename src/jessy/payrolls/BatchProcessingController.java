package jessy.payrolls;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import jessy.misc.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchProcessingController extends VBox {
    
    private static final Logger log = LoggerFactory.getLogger(BatchProcessingController.class);
    
    private final PayrollDao payrollDao;
    private final CreateBatch createBatchForm;
    private final TableView<BatchProcessing> table = new TableView<>();
    
    @Inject
    public BatchProcessingController(PayrollDao payrollDao, ResourceBundle bundle) {
        this.payrollDao = payrollDao;
        this.createBatchForm = new CreateBatch(bundle);
        setSpacing(10);
        
        getChildren().setAll(createBatchForm, table);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        createBatchForm.addOnCreateClickedListener(onCreateBatchForm);
        
        TableColumn<BatchProcessing, String> nameCol = new TableColumn<>();
        nameCol.setCellValueFactory(new PropertyValueFactory<BatchProcessing, String>("title"));
        TableColumn<BatchProcessing, Date> startDateCol = new TableColumn<>();
        startDateCol.setCellValueFactory(new PropertyValueFactory<BatchProcessing, Date>("dateStart"));
        TableColumn<BatchProcessing, Date> endDateCol = new TableColumn<>();
        startDateCol.setCellValueFactory(new PropertyValueFactory<BatchProcessing, Date>("dateEnd"));
        TableColumn<BatchProcessing, Void> actionsCol = new TableColumn<>();
        table.getColumns().setAll(nameCol, startDateCol, endDateCol, actionsCol);
    }
    
    public void load() throws SQLException {
        table.getItems().setAll(payrollDao.findPendingBatches());
    }
    
    private final Listener<BatchProcessing> onCreateBatchForm = new Listener<BatchProcessing>() {
        @Override
        public void onEvent(BatchProcessing evt) {
            createProcessingBatch(evt);
        }
    };
    
    private void createProcessingBatch(BatchProcessing batch) {
        try {
            // TODO use a dialog from the jessy.utils.FX utility class
            Map<String, Payroll> workedHours = payrollDao.findWorkedHours(batch.start, batch.end);
            batch.items = new ArrayList<>();
            batch.items.addAll(workedHours.values());
            payrollDao.storeNewBatch(batch);
        } catch (SQLException e) {
            log.error("Could not retrieve attendance sheets for processing", e);
            throw new RuntimeException(e);
        }
    }

}
