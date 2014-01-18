package app;

import beans.Payment;
import beans.WorkHour;
import dao.PaymentDao;
import dao.WorkHourDao;
import java.math.BigDecimal;
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
import shared.DatePicker;

public class Payments {
    
    private final VBox root = new VBox();
    private final Button newPaymentBtn = new Button("Registra pagamento");
    private final ListView<Payment> paymentHistory = new ListView<>();
    private final PaymentDao paymentDao;
    private final WorkHourDao workHourDao;
    
    // payment subform
    private final TextField amount = new TextField();
    private final TextField hourlyRate = new TextField();
    private final Label totalHoursLbl = new Label();
    private final Button computeAmountBtn = new Button("Calcola");
    
    private ObjectProperty<BigDecimal> totalHoursCount = new SimpleObjectProperty<>();
    
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
