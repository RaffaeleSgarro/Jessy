package jessy.payrolls;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.util.List;

public class BatchProcessingJsonHelper {
    
    private final Gson gson = new Gson();
    
    public List<Payroll> fromJson(Reader reader) {
        return gson.fromJson(reader, new TypeToken<List<Payroll>>(){}.getType());
    }
    
    public String toJson(List<Payroll> payrolls) {
        return gson.toJson(payrolls, new TypeToken<List<Payroll>>(){}.getType());
    }
}
