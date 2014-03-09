package jessy.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import jessy.attendance.AttendanceSheet;
import jessy.attendance.AttendanceSheetDao;
import jessy.payrolls.BatchProcessing;
import jessy.payrolls.Payroll;
import jessy.payrolls.PayrollDao;
import jessy.payrolls.PayrollLine;
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
    
    @Test
    public void findPendingBatchProcessings() throws Exception {
        fillDatabaseWithTestData();
        List<BatchProcessing> result = inject(PayrollDao.class).findPendingBatches();
        assertEquals(result.size(), 2);
    }
    
    @Test
    public void storeBatchProcessing() throws Exception {
        fillDatabaseWithTestData();
        
        BatchProcessing batch = new BatchProcessing();
        batch.start = new Date();
        batch.end = new Date();
        
        List<Payroll> payrolls = new ArrayList<>();
        batch.items = payrolls;

        Payroll payroll = new Payroll();
        payroll.workerId = "1";
        payroll.workerDescription = "Alessandro Magno";
        payroll.periodStart = new Date();
        payroll.periodEnd = new Date();
        PayrollLine line1 = new PayrollLine();
        line1.description = "Stipendio";
        line1.quantity = new BigDecimal("40");
        line1.rate = new BigDecimal("10.5");
        PayrollLine line2 = new PayrollLine();
        line2.description = "Bonus";
        line2.quantity = new BigDecimal("1");
        line2.rate = new BigDecimal("123.22");
        payroll.lines = Arrays.asList(line1, line2);
        payrolls.add(payroll);
        
        BatchProcessing newBatch = inject(PayrollDao.class).storeNewBatch(batch);
        assertNotNull(newBatch.id);
    }
    
}
