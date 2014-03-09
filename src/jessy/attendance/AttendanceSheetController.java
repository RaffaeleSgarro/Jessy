package jessy.attendance;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.inject.Inject;
import jessy.workers.Worker;
import jessy.workers.WorkersDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttendanceSheetController extends VBox {
    
    private static final Logger log = LoggerFactory.getLogger(AttendanceSheetController.class);
    
    private AttendanceSheet sheet;
    
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
            load(sheet.date);
        } catch (SQLException e) {
            log.error("Can't reload attendance sheet", e);
            throw new RuntimeException(e);
        }
    }

    public void load(Date date) throws SQLException {
        sheet = sheetDao.find(date);
        if (sheet == null) {
            // TODO maybe introduce active workers?
            sheet = prepareSheet(date, workersDao.findAll(), getColumnsForNewSheet());
        }
        setUpTable();
    }
    
    private List<String> getColumnsForNewSheet() {
        // TODO we need a way to set the default columns set for new sheets
        return Arrays.asList("Magazzino", "Campagna");
    }
    
    private AttendanceSheet prepareSheet(Date date, List<Worker> workers, List<String> columns) {
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
        columns.add(name);
        
        for (int i = 0; i < sheet.headers.length; i++) {
            columns.add(makeValueColumn(sheet.headers[i], i));
        }
        
        table.itemsProperty().set(FXCollections.observableArrayList(sheet.rows));
    }
    
    private TableColumn<AttendanceSheetRow, BigDecimal> makeValueColumn(String header, int colIdxInSheet) {
        TableColumn<AttendanceSheetRow, BigDecimal> column = new TableColumn<>(header);
        column.setPrefWidth(100);
        column.setEditable(true);
        column.setCellValueFactory(new SheetCellValueFactory(colIdxInSheet));
        // column.setCellFactory(new SheetCellFactory(colIdxInSheet));
        column.setCellFactory(TextFieldTableCell.<AttendanceSheetRow, BigDecimal>forTableColumn(bigDecimalStringConverter));
        column.setOnEditCommit(onCellEditCommitted);
        return column;
    }
    
    private static class SheetCellFactory implements Callback<TableColumn<AttendanceSheetRow, BigDecimal>, TableCell<AttendanceSheetRow, BigDecimal>> {
        
        private final int idx;
        
        public SheetCellFactory(int idx) {
            this.idx = idx;
        }
        
        @Override
        public TableCell<AttendanceSheetRow, BigDecimal> call(TableColumn<AttendanceSheetRow, BigDecimal> p) {
            return new SheetCell(idx);
        }
    }
    
    private static class SheetCell extends TableCell<AttendanceSheetRow, BigDecimal> {
        
        private final int idx;
        private final TextField textField = new TextField();
        
        public SheetCell(int idx) {
            this.idx = idx;
            setGraphic(textField);
        }
        
        @Override
        public void updateItem(BigDecimal val, boolean empty) {
            super.updateItem(val, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(textField);
                textField.setText(bigDecimalStringConverter.toString(val));
            }
        }
    }
    
    private final EventHandler<CellEditEvent<AttendanceSheetRow, BigDecimal>> onCellEditCommitted = new EventHandler<CellEditEvent<AttendanceSheetRow, BigDecimal>>() {

        @Override
        public void handle(CellEditEvent<AttendanceSheetRow, BigDecimal> evt) {
            int row = evt.getTablePosition().getRow();
            int col = evt.getTablePosition().getColumn() - 1;
            sheet.rows[row].values[col] = evt.getNewValue();
        }
    };
    
    private static class SheetCellValueFactory implements Callback<CellDataFeatures<AttendanceSheetRow, BigDecimal>, ObservableValue<BigDecimal>> {
        
        private final int colIdxInSheet;
        
        public SheetCellValueFactory(int colIdxInSheet) {
            this.colIdxInSheet = colIdxInSheet;
        }

        @Override
        public ObservableValue<BigDecimal> call(CellDataFeatures<AttendanceSheetRow, BigDecimal> cdf) {
            BigDecimal startValue = cdf.getValue().values[colIdxInSheet];
            ObjectProperty<BigDecimal> val = new SimpleObjectProperty<>(startValue);
            return val;
        }
    }
    
    private static final StringConverter<BigDecimal> bigDecimalStringConverter = new StringConverter<BigDecimal> (){

        @Override
        public String toString(BigDecimal bigDecimal) {
            return bigDecimal != null ? bigDecimal.toString() : "";
        }

        @Override
        public BigDecimal fromString(String string) {
            try {
                return new BigDecimal(string);
            } catch (Exception e) {
                // TODO change cell background?
                return null;
            }
        }
    };
}
