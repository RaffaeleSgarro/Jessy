package jessy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoBase {
    
    protected final DataSource ds;
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    public DaoBase(DataSource ds) {
        this.ds = ds;
    }
    
    public interface Mapper<DST> {
        public DST map(ResultSet src) throws SQLException; 
    }
    
    public <DST> List<? super DST> fetch(String sql, Mapper<DST> mapper, Object... args) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            bind(stmt, args);
            ResultSet result = stmt.executeQuery();
            List<DST> out = new ArrayList<>();
            while (result.next()) {
                out.add(mapper.map(result));
            }
            return out;
        } catch (SQLException e) {
            throw e;
        }
    }
    
    private void bind(PreparedStatement stmt, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
        }
    }
    
    public <PK_TYPE> PK_TYPE insert(String sql, Class<PK_TYPE> clazz, Object... args) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            bind(stmt, args);
            stmt.execute();
            ResultSet res = stmt.getGeneratedKeys();
            res.next();
            return res.getObject(1, clazz);
        } catch(SQLException e) {
            throw e;
        }
    }
}
