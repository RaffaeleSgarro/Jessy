package beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Worker {
    
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final IntegerProperty id = new SimpleIntegerProperty();
    
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
    
    public void setId(Integer val) {
        id.setValue(val);
    }
    
    public Integer getId() {
        return id.getValue();
    }
    
    public IntegerProperty integerProperty() {
        return id;
    }

}
