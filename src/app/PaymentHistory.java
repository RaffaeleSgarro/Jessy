package app;

import beans.Payment;
import dao.PaymentDao;
import dao.WorkHourDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import shared.DatePicker;

public class PaymentHistory {
    
    private final VBox root = new VBox();
    private final Button newPaymentBtn = new Button("Registra pagamento");
    private final ListView<Payment> paymentHistory = new ListView<>();
    private final PaymentDao paymentDao;
    private final WorkHourDao workHourDao;
    
    private int workerId;
    
    public PaymentHistory(PaymentDao dao, WorkHourDao workHourDao) {
        this.paymentDao = dao;
        this.workHourDao = workHourDao;
        newPaymentBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                showNewPaymentForm();
            }
        });
    }
    
    public void showNewPaymentForm() {
        SelectHours selectHours = new SelectHours();
        root.getChildren().setAll(new DatePicker(), new TextField("totale"), new Label("Dettaglio ore"), selectHours.getWidget());
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
