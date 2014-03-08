package jessy.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.googlecode.flyway.core.Flyway;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.derby.tools.ij;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestBase {
    
    private final String MIGRATIONS_LOCATION = "classpath:migrations";
    private final Injector injector;
    
    public TestBase() {
        TestModule testModule = new TestModule();
        testModule.appBundle = "/locale/default/strings.properties";
        testModule.testDb = "db/jessydata-test";
        injector = Guice.createInjector(testModule);
    }
    
    @BeforeSuite
    public void bootstrap() {
        // create database if not exists
        DataSource ds = injector.getInstance(DataSource.class);
        // update schema
        Flyway  flyway = new Flyway();
        flyway.setDataSource(ds);
        flyway.setLocations(MIGRATIONS_LOCATION);
        flyway.migrate();
        // TODO fill with test data
    }
    
    @AfterSuite
    public void shutdown() {
        // close the database
    }
    
    protected <T> T inject(Class<T> clazz) {
        return injector.getInstance(clazz);
    }
    
    public void sql(String resource) {
        try (Connection conn = injector.getInstance(DataSource.class).getConnection();
             InputStream in = getClass().getResourceAsStream(resource)) {
            ij.runScript(conn, in, "UTF-8", System.out, Charset.defaultCharset().displayName(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void fillDatabaseWithTestData() {
        sql("/testdata/testdata_1.sql");
    }

}
