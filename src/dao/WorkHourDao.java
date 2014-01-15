package dao;

import beans.WorkHour;
import beans.Worker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
            Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT WORKER_ID, FIRST_NAME, LAST_NAME, DAY, HOURS"
                            + " FROM WORKED_HOUR WH JOIN WORKER W ON WH.WORKER_ID = W.ID"
                            + " WHERE DAY = ?");
            stmt.setDate(1, new java.sql.Date(day.getTime()));
            ResultSet result = stmt.executeQuery();
            
            List<WorkHour> out = new ArrayList<>();
            while (result.next()) {
                Worker worker = new Worker();
                WorkHour hour = new WorkHour();
                out.add(hour);
                hour.worker = worker;
                
                worker.setId(result.getInt("worker_id"));
                worker.setFirstName(result.getString("first_name"));
                worker.setLastName(result.getString("last_name"));
                
                hour.date = result.getDate("day");
                hour.hours = result.getBigDecimal("hours");
            }
            return out;
        } catch (SQLException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public void storeDay(Collection<WorkHour> hours) {
    
    }

}
