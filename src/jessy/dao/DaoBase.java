package jessy.dao;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
    
    public <DST> DST fetchOne(String sql, Mapper<DST> mapper, Object... args) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            bind(stmt, args);
            ResultSet result = stmt.executeQuery();
            return result.next() ? mapper.map(result) : null;
        } catch (SQLException e) {
            throw e;
        }
    }
    
    public <DST> List<DST> fetch(String sql, Mapper<DST> mapper, Object... args) throws SQLException {       
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
            if (args[i] instanceof Reader) {
                stmt.setCharacterStream(i + 1, (Reader) args[i]);
            } else if (args[i] instanceof Date) {
                stmt.setDate(i + 1, new java.sql.Date(((Date) args[i]).getTime()));
            } else {
                stmt.setObject(i + 1, args[i]);
            }
        }
    }
    
    public <PK_TYPE> PK_TYPE insertAutoId(String sql, Class<PK_TYPE> clazz, Object... args) throws SQLException {
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
    
    public boolean executeUpdate(String sql, Object... args) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            bind(stmt, args);
            return stmt.execute();
        } catch(SQLException e) {
            throw e;
        }
    }
}
