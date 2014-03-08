package jessy.test;

import java.util.Date;
import java.util.List;
import jessy.attendance.AttendanceSheet;
import jessy.attendance.AttendanceSheetDao;
import jessy.workers.Worker;
import jessy.workers.WorkersDao;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AllDaosTest extends TestBase {
    
    @Test
    public void findAllWorkers() throws Exception {
        inject(WorkersDao.class).findAll();
    }
    
    @Test
    public void createWorker() throws Exception {
        inject(WorkersDao.class).create("James", "Bond");
    }
    
    @Test
    public void storeAttendanceSheet() throws Exception {
        inject(AttendanceSheetDao.class).store(new Date(), new AttendanceSheet());
    }
    
    @Test
    public void findAttendanceSheet() throws Exception {
        inject(AttendanceSheetDao.class).find(new Date());
    }
    
    @Test
    public void findWorkersByName() throws Exception {
        fillDatabaseWithTestData();
        List<Worker> result = inject(WorkersDao.class).findByName("Foo");
        assertEquals(result.size(), 2);
    }
    
}
