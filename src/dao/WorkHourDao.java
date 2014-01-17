package dao;

import beans.WorkHour;
import beans.Worker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class WorkHourDao {
    
    private final Logger log = Logger.getLogger("work_hour");
    private final DataSource ds;
    
    public WorkHourDao(DataSource ds) {
        this.ds = ds;
    }
    
    public List<WorkHour> __findWorkedHours(Date day) {
        List<WorkHour> out = new ArrayList<>();
        return out;
    }
    
    // TODO
    public List<WorkHour> findWorkedHours(Date day) {
        try {
            List<WorkHour> out;
            try (Connection conn = ds.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT W.ID, W.FIRST_NAME, W.LAST_NAME, WH.DAY, WH.HOURS"
                                + " FROM WORKER W LEFT OUTER JOIN ("
                                + " SELECT * FROM WORKED_HOUR WHERE DAY = ?"
                                + " ) AS WH"
                                + " ON W.ID = WH.WORKER_ID");
                stmt.setDate(1, new java.sql.Date(day.getTime()));
                ResultSet result = stmt.executeQuery();
                out = new ArrayList<>();
                while (result.next()) {
                    Worker worker = new Worker();
                    WorkHour hour = new WorkHour();
                    out.add(hour);
                    hour.worker = worker;
                    
                    worker.setId(result.getInt("id"));
                    worker.setFirstName(result.getString("first_name"));
                    worker.setLastName(result.getString("last_name"));
                    
                    hour.date = result.getDate("day");
                    hour.hours = result.getBigDecimal("hours");
                }
            }
            return out;
        } catch (SQLException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public void storeDay(Collection<WorkHour> hours, Date day) {
        Connection conn = null;
        try {
            // TODO should begin a transaction
            conn = ds.getConnection();
            java.sql.Date timestamp = new java.sql.Date(day.getTime());
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM WORKED_HOUR WHERE DAY = ?");
            stmt.setDate(1, timestamp);
            stmt.execute();
           
            stmt = conn.prepareStatement("INSERT INTO WORKED_HOUR (WORKER_ID, DAY, HOURS) VALUES (?, ?, ?)");
            for (WorkHour hour : hours) {
                
                //stmt.addBatch();
                stmt.setInt(1, hour.worker.getId());
                stmt.setDate(2, timestamp);
                stmt.setBigDecimal(3, hour.hours);
                stmt.addBatch();
            }     
            stmt.executeBatch();
            conn.close();
        } catch (SQLException e) {
            if (conn != null)
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                    //
                }
            }   
        }
    }
    
    public List<WorkHour> findUnpaid(int workerId) {
        try (Connection conn = ds.getConnection()) {
            List<WorkHour> out = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM WORKED_HOUR"
                    + " WHERE WORKER_ID = ?"
                    + " AND PAYMENT_ID IS NULL"
                    + " AND HOURS > 0"
                    + " ORDER BY DAY ASC");
            stmt.setInt(1, workerId);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                WorkHour wh = new WorkHour();
                out.add(wh);
                wh.date = res.getDate("day");
                wh.hours = res.getBigDecimal("hours");
            }
            return out;
        } catch (SQLException e) {
            log.severe(e.getMessage());
            return Collections.emptyList();
        }
    }

}
