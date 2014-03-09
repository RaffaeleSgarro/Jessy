package jessy.test;

import java.math.BigDecimal;
import java.util.Map;
import jessy.payrolls.Cashier;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CashierTest {
    
    @Test
    public void testCashier() throws Exception {
        Cashier target = new Cashier("Sample worker");

        String[] h1 = {"A", "B", "C"};
        BigDecimal[] v1 = { bd("8"), bd("10"), bd("14") };
        BigDecimal[] v2 = { bd("8"), bd("10"), bd("14") };
        target.scan(h1, v1);
        target.scan(h1, v2);
        
        String[] h2 = {"B", "C", "D", "E"};
        BigDecimal[] v3 = { bd("8"), bd("10"), bd("14"), bd("3") };
        target.scan(h2, v3);
        
        Map<String, BigDecimal> receipt = target.getReceipt();
        assertEquals(receipt.get("A"), bd("16"));
        assertEquals(receipt.get("B"), bd("28"));
        assertEquals(receipt.get("C"), bd("38"));
        assertEquals(receipt.get("D"), bd("14"));
        assertEquals(receipt.get("E"), bd("3"));
    }
    
    private BigDecimal bd(String str) {
        return new BigDecimal(str);
    }

}
