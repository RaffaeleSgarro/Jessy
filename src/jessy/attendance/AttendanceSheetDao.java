package jessy.attendance;

import javax.inject.Inject;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;
import jessy.dao.DaoBase;
import jessy.utils.IOUtils;

public class AttendanceSheetDao extends DaoBase {
    
    private final AttendanceSheetJsonHelper jsonHelper = new AttendanceSheetJsonHelper();
    
    @Inject
    public AttendanceSheetDao(DataSource ds) {
        super(ds);
    }
    
    public AttendanceSheet find(Date day) throws SQLException {
        String sql = "select s.sheet_date, s.document from attendance_sheet s where s.sheet_date = ?";
        return fetchOne(sql, new Mapper<AttendanceSheet>() {
            @Override
            public AttendanceSheet map(ResultSet src) throws SQLException {
                Clob document = src.getClob("document");
                Reader in = document.getCharacterStream();
                AttendanceSheet sheet = fromJson(in);
                IOUtils.close(in);
                document.free();
                return sheet;
            }
        }, day);
    }
    
    public AttendanceSheet fromJson(Reader json) {
        return jsonHelper.fromJson(json);
    }
    
    public void store(Date day, AttendanceSheet sheet) throws SQLException, UnsupportedEncodingException {
        String deleteSql = "delete from attendance_sheet where sheet_date = ?";
        executeUpdate(deleteSql, day);
        
        String insertSql = "insert into attendance_sheet (sheet_date, document) values (?, ?)";
        Reader document = new StringReader(toJson(sheet));
        executeUpdate(insertSql, day, document);
    }
    
    public String toJson(AttendanceSheet sheet) {
        return jsonHelper.toJson(sheet);
    }

}
