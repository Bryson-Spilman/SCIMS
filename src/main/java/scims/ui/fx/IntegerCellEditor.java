package scims.ui.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.util.StringConverter;

class IntegerCellEditor<T> extends TreeTableCell<T, Integer> {

    private final StringConverter<Integer> converter;

    public IntegerCellEditor(StringConverter<Integer> converter) {
        this.converter = converter;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
        } else {
            setGraphic(null);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(converter.toString(getItem()));
        setGraphic(null);
    }

    @Override
    public void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(converter.toString(item));
        }
    }

    private void createTextField() {
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

    @Override
    public void commitEdit(Integer newValue) {
        super.commitEdit(newValue);
        setGraphic(null);
    }
}