package jessy.attendance;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.inject.Inject;
import jessy.workers.Worker;
import jessy.workers.WorkersDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttendanceSheetController extends VBox {
    
    private static final Logger log = LoggerFactory.getLogger(AttendanceSheetController.class);
    
    private AttendanceSheet sheet;
    private StringProperty[][] cells;
    
    private final ResourceBundle bundle;
    private final AttendanceSheetDao sheetDao;
    private final WorkersDao workersDao;
    private final TableView<AttendanceSheetRow> table = new TableView<>();
    
    @Inject
    public AttendanceSheetController(AttendanceSheetDao sheetDao, WorkersDao workersDao, ResourceBundle bundle) {
        this.sheetDao = sheetDao;
        this.workersDao = workersDao;
        this.bundle = bundle;
        
        Button saveBtn = new Button(bundle.getString("save"));
        Button cancelBtn = new Button(bundle.getString("cancel"));
        
        setMaxHeight(Double.MAX_VALUE);
        setMaxWidth(Double.MAX_VALUE);
        
        HBox buttons = new HBox();
        getChildren().setAll(table, buttons);
        setSpacing(10);
        buttons.setSpacing(10);
        buttons.setMaxWidth(Double.MAX_VALUE);
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.getChildren().setAll(saveBtn, cancelBtn);
        
        table.setEditable(true);
        table.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        saveBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent evt) {
                onSave();
            }
        });
        
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                onReload();
            }
        });
    }
    
    private void onSave() {
        try {
            sheetDao.store(sheet.date, sheet);
        } catch (UnsupportedEncodingException | SQLException e) {
            log.error("Can't save attendance sheet", e);
            throw new RuntimeException(e);
        }
    }
    
    private void onReload() {
        try {
            loadDate(sheet.date);
        } catch (SQLException e) {
            log.error("Can't reload attendance sheet", e);
            throw new RuntimeException(e);
        }
    }

    public void loadDate(Date date) throws SQLException {
        sheet = sheetDao.find(date);
        
        if (sheet == null) {
            sheet = prepareNewSheet(date, workersDao.findAll(), getColumnsForNewSheet());
        }
        
        setUpTable();
    }
    
    public void addRowForWorker(String id, String description) {
        AttendanceSheetRow row = new AttendanceSheetRow();
        row.workerId = id;
        row.workerDescription = description;
        row.values = new BigDecimal[sheet.headers.length];
        
        if (lookupRowIdx(row) < 0) {
            AttendanceSheetRow[] rows = new AttendanceSheetRow[sheet.rows.length + 1];
            rows[0] = row;
            System.arraycopy(sheet.rows, 0, rows, 1, sheet.rows.length);
            sheet.rows = rows;
            setUpTable();
        } else {
            log.warn("Row already exists!");
        }
    }
    
    private List<String> getColumnsForNewSheet() {
        // TODO we need a way to set the default columns set for new sheets
        return Arrays.asList("Magazzino", "Campagna");
    }
    
    private AttendanceSheet prepareNewSheet(Date date, List<Worker> workers, List<String> columns) {
        AttendanceSheet newSheet = new AttendanceSheet();
        newSheet.date = date;
        newSheet.headers = columns.toArray(new String[columns.size()]);
        newSheet.rows = new AttendanceSheetRow[workers.size()];
        for (int i = 0; i < workers.size(); i++) {
            Worker worker = workers.get(i);
            AttendanceSheetRow row = new AttendanceSheetRow();
            newSheet.rows[i] = row;
            row.workerDescription = String.format("%s, %s", worker.lastName, worker.firstName);
            row.workerId = worker.id;
            row.values = new BigDecimal[columns.size()];
        }
        return newSheet;
    }
    
    private void setUpTable() {
        TableColumn<AttendanceSheetRow, String> name = new TableColumn<>(bundle.getString("name"));
        name.setCellValueFactory(new PropertyValueFactory<AttendanceSheetRow, String>("workerDescription"));
        name.setEditable(false);
        
        ObservableList<TableColumn<AttendanceSheetRow, ?>> columns = table.getColumns();
        columns.clear();
        columns.add(name);
        
        cells = new StringProperty[sheet.rows.length][sheet.headers.length];
        for (int row = 0; row < sheet.rows.length; row++) {
            for (int col = 0; col < sheet.headers.length; col++) {
                BigDecimal val = sheet.rows[row].values[col];
                cells[row][col] = new SimpleStringProperty(bigDecimalToString(val));
            }
        }
        
        for (int i = 0; i < sheet.headers.length; i++) {
            columns.add(makeValueColumn(sheet.headers[i], i));
        }
        
        table.itemsProperty().set(FXCollections.observableArrayList(sheet.rows));
    }
    
    private TableColumn<AttendanceSheetRow, String> makeValueColumn(String header, int colIdxInSheet) {
        TableColumn<AttendanceSheetRow, String> column = new TableColumn<>(header);
        column.setPrefWidth(100);
        column.setEditable(true);
        column.setCellValueFactory(new SheetCellValueFactory(colIdxInSheet));
        column.setCellFactory(TextFieldTableCell.<AttendanceSheetRow>forTableColumn());
        column.setOnEditCommit(onCellEditCommitted);
        return column;
    }
    
    private final EventHandler<CellEditEvent<AttendanceSheetRow, String>> onCellEditCommitted = new EventHandler<CellEditEvent<AttendanceSheetRow, String>>() {

        @Override
        public void handle(CellEditEvent<AttendanceSheetRow, String> evt) {
            int col = evt.getTablePosition().getColumn() - 1;
            int row = evt.getTablePosition().getRow();
            
            try {
                BigDecimal val = new BigDecimal(evt.getNewValue());
                sheet.rows[row].values[col] = val;
            } catch (Exception e) {
                cells[row][col].setValue("#ERROR!");
            }
        }
    };
    
    private class SheetCellValueFactory implements Callback<CellDataFeatures<AttendanceSheetRow, String>, ObservableValue<String>> {
        
        private final int colIdxInSheet;
        
        public SheetCellValueFactory(int colIdxInSheet) {
            this.colIdxInSheet = colIdxInSheet;
        }

        @Override
        public ObservableValue<String> call(CellDataFeatures<AttendanceSheetRow, String> cdf) {
            return cells[lookupRowIdx(cdf.getValue())][colIdxInSheet];
        }
    }
    
    private int lookupRowIdx(AttendanceSheetRow targetRow) {
        for (int i = 0; i < sheet.rows.length; i++) {
            if (sheet.rows[i].workerId.equals(targetRow.workerId)) return i;
        }
        return -1;
    }
    
    private String bigDecimalToString(BigDecimal number) {
        return number != null ? number.toString() : "";
    }
}
