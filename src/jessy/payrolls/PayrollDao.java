package jessy.payrolls;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jessy.workerinfo.Payment;

public class PayrollDao {
    
    public Map<String, BigDecimal> findWorkedHours(String workerId, Date startInclusive, Date endInclusive) {
        Map<String, BigDecimal> out = new HashMap<>();
        return out;
    }
    
    public Payment finalizePayroll(List<PayrollLine> lines) {
        return null;
    }
    
}
