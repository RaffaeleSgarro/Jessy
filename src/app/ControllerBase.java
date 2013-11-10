package app;

import java.util.ResourceBundle;

public class ControllerBase {
    
    private final ResourceBundle bundle;
    
    public ControllerBase(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    public String _(String key) {
        return bundle.getString(key);
    }
}
