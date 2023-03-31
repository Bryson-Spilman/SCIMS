package scims.ui.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

class DoubleTextField extends TextField {

    public DoubleTextField() {
        setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), null, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }
}
