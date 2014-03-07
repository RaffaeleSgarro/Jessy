package jessy.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import jessy.dao.DaoBase;

public class WorkersDao extends DaoBase {
    
    public WorkersDao(DataSource ds) {
        super(ds);
    }
    
    public List<? super Worker> findAll() throws SQLException {
        String sql = "select * from worker w order by w.last_name, w.first_name";
        return fetch(sql, new Mapper<Worker>() {
            @Override
            public Worker map(ResultSet rs) throws SQLException {
                Worker dst = new Worker();
                dst.id = rs.getString("id");
                dst.firstName = rs.getString("first_name");
                dst.lastName = rs.getString("last_name");
                return dst;
            }
        });
    }
    
    public int create(String first, String last) throws SQLException {
        String sql = "insert into worker (first_name, last_name) values (?, ?)";
        return insert(sql, Integer.class, first, last);
    }

}
