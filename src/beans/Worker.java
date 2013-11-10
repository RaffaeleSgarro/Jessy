package beans;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Worker {
    
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    
    public void setFirstName(String val) {
        firstName.set(val);
    }
    
    public String getFirstName() {
        return firstName.get();
    }
    
    public void setLastName(String val) {
        lastName.set(val);
    }
    
    public String getLastName() {
        return lastName.get();
    }
    
    public StringProperty firstNameProperty() {
        return firstName;
    }
    
    public StringProperty lastNameProperty() {
        return lastName;
    }

}
