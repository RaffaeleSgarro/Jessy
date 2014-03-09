package jessy.payrolls;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BatchProcessingJsonHelperTest {
    
    private BatchProcessingJsonHelper target;
    
    @BeforeMethod
    public void setUp() {
        target = new BatchProcessingJsonHelper();
    }
    
    @Test
    public void testToJson() throws Exception {
        List<Payroll> payrolls = new ArrayList<>();

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
        String json = target.toJson(payrolls);
    }
    
    @Test
    public void testFromJson() throws Exception {
        String in = "[{\"periodStart\":\"Mar 9, 2014 9:34:24 PM\",\"periodEnd\":\"Mar 9, 2014 9:34:24 PM\",\"workerId\":\"1\",\"workerDescription\":\"Alessandro Magno\",\"lines\":[{\"description\":\"Stipendio\",\"quantity\":40,\"rate\":10.5},{\"description\":\"Bonus\",\"quantity\":1,\"rate\":123.22}]}]";
        List<Payroll> payrolls = target.fromJson(new StringReader(in));
    }

}
