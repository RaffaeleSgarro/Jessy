package jessy.payrolls;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import jessy.attendance.AttendanceSheet;
import jessy.attendance.AttendanceSheetDao;
import jessy.attendance.AttendanceSheetRow;

public class PayrollDao {
    
    private final AttendanceSheetDao attendanceDao;
    
    @Inject
    public PayrollDao(AttendanceSheetDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }
    
    public Map<String, Payroll> findWorkedHours(String workerId, Date startInclusive, Date endInclusive) throws SQLException {
        Map<String, Cashier> workerToCashier = new HashMap<>();
        
        for (Date day : DateUtils.range(startInclusive, endInclusive)) {
            AttendanceSheet sheet = attendanceDao.find(day);
            processSheet(sheet, workerToCashier);
        }
        
        return makePayrolls(workerToCashier, startInclusive, endInclusive);
    }
    
    public static Map<String, Payroll> makePayrolls(Map<String, Cashier> workerToCashier, Date startInclusive, Date endInclusive) {
        Map<String, Payroll> out = new HashMap<>();
        
        for (Map.Entry<String, Cashier> entry : workerToCashier.entrySet()) {
            Payroll payroll = new Payroll();
            payroll.periodStart = startInclusive;
            payroll.periodEnd = endInclusive;
            payroll.workerId = entry.getKey();
            payroll.workerDescription = entry.getValue().workerDescription;
            payroll.lines = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> srcLine : entry.getValue().getReceipt().entrySet()) {
                PayrollLine dstLine = new PayrollLine();
                dstLine.description = srcLine.getKey();
                dstLine.quantity = srcLine.getValue();
                payroll.lines.add(dstLine);
            }
        }
        
        return out;
    }
    
    private void processSheet(AttendanceSheet sheet, Map<String, Cashier> out) {
        String[] headersNormalized = new String[sheet.headers.length];
        
        for (int i = 0; i < sheet.headers.length; i++) {
            headersNormalized[i] = sheet.headers[i].trim().toUpperCase();
        }
        
        for (AttendanceSheetRow row : sheet.rows) {
            Cashier cashier = out.get(row.workerId);
            if (cashier == null) {
                cashier = new Cashier(row.workerDescription);
                out.put(row.workerId, cashier);
            }
            cashier.scan(headersNormalized, row.values);
        }
    }
    
}
