package dao;

import beans.Payment;
import beans.WorkHour;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class PaymentDao {
    
    private static final Logger log = Logger.getLogger(PaymentDao.class.getName());
    private final DataSource ds;
    
    public PaymentDao(DataSource ds) {
        this.ds = ds;
    }
    
    public List<Payment> findPayments(Integer workerId) {
        try (Connection conn = ds.getConnection()) {
            List<Payment> out = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("select * from payment where worker_id = ?");
            stmt.setInt(1, workerId);
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                Payment payment = new Payment();
                out.add(payment);
                payment.id = res.getInt("id");
                payment.initiatedAt = res.getDate("initiated_at");
                payment.amount = res.getBigDecimal("amount");
                payment.description = res.getString("description");
            }
            return out;
        } catch (SQLException e) {
            log.severe(e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public void store(Payment payment, List<WorkHour> hours) {
    }
    
}
