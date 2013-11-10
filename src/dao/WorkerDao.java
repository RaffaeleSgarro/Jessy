package dao;

import beans.Worker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class WorkerDao {
    
    private final DataSource ds;
    
    public WorkerDao(DataSource ds) {
        this.ds = ds;
    }
    
    public List<Worker> findAll() {
        try {
            Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM WORKER");
            ResultSet res = stmt.executeQuery();
            
            List<Worker> result = new ArrayList<>();
            while ( res.next() ) {
                Worker w = new Worker();
                result.add(w);
                w.setFirstName(res.getString("first_name"));
                w.setLastName(res.getString("last_name"));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(WorkerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Collections.emptyList();
    }
}
