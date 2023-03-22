package scims.ui.swing;

public class MissingRequiredValueException extends Exception {
    public MissingRequiredValueException(String requireFieldName) {
        super("Missing required " + requireFieldName);
    }

    public MissingRequiredValueException(String requiredFieldName, String conditionalFieldSelected) {
        super(requiredFieldName + " is required when " + conditionalFieldSelected + " is selected");
    }
}
