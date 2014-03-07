package jessy.dao;

import jessy.workers.WorkersDao;
import org.apache.derby.jdbc.ClientDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AllDaoTests {
    
    private ClientDataSource ds;
    
    @BeforeMethod
    public void setUp() {
        ds = new ClientDataSource();
        // TODO read database name
        ds.setDatabaseName("jessy");
    }
    
    @Test
    public void testAll() throws Exception {
        WorkersDao workersDao = new WorkersDao(ds);
        workersDao.findAll();
        workersDao.create("Foo B.", "Bar");
    }

}
