package jessy.attendance;

import java.io.StringReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AttendanceSheetJsonHelperTest {
    
    private AttendanceSheetJsonHelper target;
    
    @BeforeMethod
    public void setUp() {
        target = new AttendanceSheetJsonHelper();
    }
    
    @Test
    public void testToJson() throws Exception {
        AttendanceSheet sheet1 = new AttendanceSheet();
        String json = target.toJson(sheet1);
    }
    
    @Test
    public void testFromJson() throws Exception {
        String json = "{}";
        AttendanceSheet sheet = target.fromJson(new StringReader(json));
    }

}
