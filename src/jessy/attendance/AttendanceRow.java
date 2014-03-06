package jessy.attendance;

import java.math.BigDecimal;
import java.util.Map;

public class AttendanceRow {
    public String workerId;
    public String workerDescription;
    public Map<String, BigDecimal> hours;
}
