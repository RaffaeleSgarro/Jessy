package jessy.payrolls;

import java.util.Date;
import java.util.List;

public class Payroll {
    public Date periodStart;
    public Date periodEnd;
    public String workerId;
    public String workerDescription;
    public List<PayrollLine> lines;
}
