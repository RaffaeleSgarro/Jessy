package jessy.attendance;

import java.math.BigDecimal;

public class AttendanceSheetRow {
    public String workerId;
    public String workerDescription;
    public BigDecimal[] values;
    
    /***************************************************************************
     * Setter and getter is so JavaFX can find the property 
     **************************************************************************/
    
    public String getWorkerDescription() {
        return workerDescription;
    }
    
    public void setWorkerDescription(String desc) {
        this.workerDescription = desc;
    }
}
