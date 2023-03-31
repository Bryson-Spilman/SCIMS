package scims.ui.fx;

import javafx.scene.control.TextField;

class IntegerCellEditor<T> extends TextCellEditor<T, Integer> {

    @Override
    void createTextField() {
        TextField textField = new IntegerTextField();
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        textField.setOnAction(evt -> {
            try {
                int newValue = Integer.parseInt(textField.getText());
                commitEdit(newValue);
            } catch (NumberFormatException e) {
                cancelEdit();
            }
        });
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    int newValue = Integer.parseInt(textField.getText());
                    commitEdit(newValue);
                } catch (NumberFormatException e) {
                    cancelEdit();
                }
            }
        });
        setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }
}