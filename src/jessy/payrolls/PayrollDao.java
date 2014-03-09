package jessy.payrolls;

import java.io.Reader;
import java.io.StringReader;
import jessy.utils.DateUtils;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.sql.DataSource;
import jessy.attendance.AttendanceSheet;
import jessy.attendance.AttendanceSheetDao;
import jessy.attendance.AttendanceSheetRow;
import jessy.dao.DaoBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayrollDao extends DaoBase {

    private static final Logger log = LoggerFactory.getLogger(PayrollDao.class);
    private final AttendanceSheetDao attendanceDao;
    private final BatchProcessingJsonHelper jsonHelper = new BatchProcessingJsonHelper();

    @Inject
    public PayrollDao(DataSource ds) {
        super(ds);
        this.attendanceDao = new AttendanceSheetDao(ds);
    }

    public Map<String, Payroll> findWorkedHours(Date startInclusive, Date endInclusive) throws SQLException {
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

    public BatchProcessing storeNewBatch(BatchProcessing batch) throws SQLException {
        try {
            Reader document = new StringReader(jsonHelper.toJson(batch.items));
            String sql = "insert into payroll_processing_batch (title, start_date, end_date, document) values (?, ?, ?, ?)";
            int id = insertAutoId(sql, Integer.class, batch.title, batch.start, batch.end, document);
            batch.id = Integer.toString(id);
            return batch;
        } catch (SQLException e) {
            log.error("Could not store batch", e);
            throw e;
        }
    }

    public List<BatchProcessing> findPendingBatches() throws SQLException {
        try {
            String sql = "select * from payroll_processing_batch";
            return fetch(sql, payrollProcessingBatchMapper);
        } catch (SQLException e) {
            log.error("Could not find pending batches", e);
            throw e;
        }
    }

    private final Mapper<BatchProcessing> payrollProcessingBatchMapper = new Mapper<BatchProcessing>() {
        @Override
        public BatchProcessing map(ResultSet src) throws SQLException {
            BatchProcessing out = new BatchProcessing();
            out.id = Integer.toString(src.getInt("id"));
            out.start = src.getDate("start_date");
            out.end = src.getDate("end_date");
            out.title = src.getString("title");
            Clob document = src.getClob("document");
            out.items = jsonHelper.fromJson(document.getCharacterStream());
            document.free();
            return out;
        }
    };

}
