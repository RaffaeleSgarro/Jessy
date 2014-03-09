package jessy.payrolls;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cashier {
    public final String workerDescription;
    private final Map<String, BigDecimal> items = new HashMap<>();

    public Cashier(String workerDescription) {
        this.workerDescription = workerDescription;
    }

    public void scan(String[] tags, BigDecimal[] values) {
        for (int i = 0; i < tags.length; i++) {
            BigDecimal subtotal = items.get(tags[i]);
            items.put(tags[i], subtotal != null ? subtotal.add(values[i]) : values[i]);
        }
    }

    public Map<String, BigDecimal> getReceipt() {
        Map<String, BigDecimal> receipt = new HashMap<>();
        receipt.putAll(items);
        return receipt;
    }

}
