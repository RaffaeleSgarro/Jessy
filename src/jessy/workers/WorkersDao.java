package jessy.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;
import javax.sql.DataSource;
import jessy.dao.DaoBase;

public class WorkersDao extends DaoBase {
    
    private final WorkerMapperImpl workerMapperImpl = new WorkerMapperImpl();

    @Inject
    public WorkersDao(DataSource ds) {
        super(ds);
    }
    
    public List<Worker> findAll() throws SQLException {
        String sql = "select * from worker w order by w.last_name, w.first_name";
        return fetch(sql, workerMapperImpl);
    }
    
    public int create(String first, String last) throws SQLException {
        String sql = "insert into worker (first_name, last_name) values (?, ?)";
        return insertAutoId(sql, Integer.class, first, last);
    }

    public List<Worker> findByName(String input) throws SQLException {
        String inputForLike = input.trim().toLowerCase() + "%";
        String sql = "select * from worker w "
                + " where lower(w.first_name) like ? or lower(w.last_name) like ? "
                + " order by w.last_name, w.first_name";
        return fetch(sql, workerMapperImpl, inputForLike, inputForLike);
    }

    private static class WorkerMapperImpl implements Mapper<Worker> {

        @Override
        public Worker map(ResultSet rs) throws SQLException {
            Worker dst = new Worker();
            dst.id = rs.getString("id");
            dst.firstName = rs.getString("first_name");
            dst.lastName = rs.getString("last_name");
            return dst;
        }
    }

}
