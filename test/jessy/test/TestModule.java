package jessy.test;

import com.google.inject.AbstractModule;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource40;

public class TestModule extends AbstractModule {
    
    public String appBundle;
    public String testDb;

    @Override
    protected void configure() {
        bind(ResourceBundle.class).toInstance(getAppBundle());
        bind(DataSource.class).toInstance(getTestDataSource());
    }
    
    private ResourceBundle getAppBundle() {
       try (Reader in = new InputStreamReader(getClass().getResourceAsStream(appBundle), "UTF-8")) {
           return new PropertyResourceBundle(in);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }
    
    private DataSource getTestDataSource() {
        EmbeddedDataSource40 ds = new EmbeddedDataSource40();
        ds.setDatabaseName(testDb);
        ds.setCreateDatabase("create");
        return ds;
    }

}
