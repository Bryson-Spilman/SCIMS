package scims.ui.fx;

import javafx.scene.control.TextField;

class DoubleCellEditor<T> extends TextCellEditor<T, Double> {

    @Override
    void createTextField() {
        TextField textField = new DoubleTextField();
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        textField.setOnAction(evt -> {
            try {
                Double newValue = Double.parseDouble(textField.getText());
                commitEdit(newValue);
            } catch (NumberFormatException e) {
                cancelEdit();
            }
        });
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    Double newValue = Double.parseDouble(textField.getText());
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
