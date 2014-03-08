package jessy.utils;

import java.io.Closeable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {
    
    public static final Logger log = LoggerFactory.getLogger(IOUtils.class);
    
    public static void close(Closeable... args) {
        for (Closeable arg : args) {
            try {
                if (arg != null) arg.close();
            } catch (IOException e) {
                log.warn("Cannot close " + arg, e);
            }
        }
    }
}
