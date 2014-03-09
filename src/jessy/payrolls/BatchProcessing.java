package jessy.payrolls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchProcessing {
    
    public String title;
    public Date start;
    public Date end;
    public String id;
    public List<Payroll> items = new ArrayList<>();

    /***************************************************************************
     * Getters required to be a JavaBean
     **************************************************************************/
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
