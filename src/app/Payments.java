package app;

import beans.Payment;
import beans.WorkHour;
import dao.PaymentDao;
import dao.WorkHourDao;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import jessy.controls.datepicker.DatePicker;

public class Payments {
    
    private final VBox root = new VBox();
    private final Button newPaymentBtn = new Button("Registra pagamento");
    private final ListView<Payment> paymentHistory = new ListView<>();
    private final PaymentDao paymentDao;
    private final WorkHourDao workHourDao;
    
    // payment subform
    private final Label amount = new Label();
    private final TextField hourlyRate = new TextField();
    private final Label totalHoursLbl = new Label();
    private final Button computeAmountBtn = new Button("Calcola");
    
    private final ObjectProperty<BigDecimal> amountProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> totalHoursCount = new SimpleObjectProperty<>();
    
    // select hours
    private final SelectHours selectHours = new SelectHours();

    private int workerId;
    
    public Payments(PaymentDao dao, WorkHourDao workHourDao) {
        this.paymentDao = dao;
        this.workHourDao = workHourDao;
        
        newPaymentBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                showNewPaymentForm(workerId);
            }
        });
        
       selectHours.getSelectedHours().addListener(new SetChangeListener<WorkHour>(){
            @Override
            public void onChanged(SetChangeListener.Change<? extends WorkHour> change) {
                BigDecimal total = BigDecimal.ZERO;
                for (WorkHour wh : change.getSet()) {
                    if (wh.hours != null) {
                        total = total.add(wh.hours);
                    }
                }
                totalHoursCount.set(total);
            }
        });
       
       totalHoursCount.addListener(new ChangeListener<BigDecimal>(){
            @Override
            public void changed(ObservableValue<? extends BigDecimal> ov, BigDecimal t, BigDecimal t1) {
                totalHoursLbl.setText(String.format("Totale: %.1f ore", t1));
            }
        });
       
       computeAmountBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                try { 
                    DecimalFormat fmt = new DecimalFormat();
                    fmt.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ITALIAN));
                    fmt.setParseBigDecimal(true);
                    BigDecimal hr = (BigDecimal) fmt.parse(hourlyRate.getText());
                    amountProperty.set(hr.multiply(totalHoursCount.get()));
                } catch (ParseException e) {
                    // TODO report parsing error to UI?
                }
            }
        });
       
       amountProperty.addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> ov, BigDecimal t, BigDecimal t1) {
                amount.setText(NumberFormat.getInstance(Locale.ITALIAN).format(t1));
            }
        });
       
       totalHoursCount.setValue(BigDecimal.ZERO);
    }
    
    public void showNewPaymentForm(int workerId) {
        this.workerId = workerId;
        root.getChildren().setAll(
                  HBoxBuilder.create().children(
                          new Label("Data pagamento")
                          , new DatePicker()).build()
                , HBoxBuilder.create().children(
                        new Label("Importo")
                        , amount
                        , totalHoursLbl
                        , new Label("€/ora:")
                        , hourlyRate
                        , computeAmountBtn).build()
                , new Label("Dettaglio ore")
                , selectHours.getWidget());
        
        selectHours.setHours(workHourDao.findUnpaid(workerId));
    }
    
    public void showPaymentHistory(int workerId) {
        this.workerId = workerId;
        root.getChildren().setAll(newPaymentBtn, new Label("Storico pagamenti"), paymentHistory);
        paymentHistory.getItems().setAll(paymentDao.findPayments(workerId));
    }
    
    public Node getWidget() {
        return root;
    }

    void showEmptyBox() {
        root.getChildren().clear();
    }
    
}
