package jessy.dao;

import java.util.Date;
import jessy.attendance.AttendanceSheet;
import jessy.attendance.AttendanceSheetDao;
import jessy.workers.WorkersDao;
import org.apache.derby.jdbc.ClientDataSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AllDaoTests {
    
    private ClientDataSource ds;
    
    @BeforeMethod
    public void setUp() {
        ds = new ClientDataSource();
        // TODO read database name
        // empty the database?
        ds.setDatabaseName("jessy");
    }
    
    @Test
    public void testAll() throws Exception {
        // This is a stupid test because the first failing one prevents the others
        // We should separate tests
        WorkersDao workersDao = new WorkersDao(ds);
        workersDao.findAll();
        workersDao.create("Foo B.", "Bar");
        
        AttendanceSheetDao attendanceSheetDao = new AttendanceSheetDao(ds);
        AttendanceSheet sheet = new AttendanceSheet();
        attendanceSheetDao.store(new Date(), sheet);
        attendanceSheetDao.find(new Date());
    }
    
    @AfterMethod
    public void tearDown() {
        // empty the database?
    }

}
