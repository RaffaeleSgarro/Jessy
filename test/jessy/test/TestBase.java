package jessy.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.googlecode.flyway.core.Flyway;
import javax.sql.DataSource;
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

}
