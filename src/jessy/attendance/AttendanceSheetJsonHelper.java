package jessy.attendance;

import com.google.gson.Gson;
import java.io.Reader;

public class AttendanceSheetJsonHelper {
    
    private final Gson gson = new Gson();

    public AttendanceSheet fromJson(Reader json) {
        return gson.fromJson(json, AttendanceSheet.class);
    }

    public String toJson(AttendanceSheet sheet) {
        return gson.toJson(sheet);
    }
    
    

}
