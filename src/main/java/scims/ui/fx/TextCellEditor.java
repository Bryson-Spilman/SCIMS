package scims.ui.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;

class TextCellEditor<T> extends TreeTableCell<T, String> {


    public TextCellEditor() {
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
        setText(getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(item);
        }
    }

    private void createTextField() {
        TextField textField = new TextField(getItem());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        textField.setOnAction(evt -> commitEdit(textField.getText()));
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });
        setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        setGraphic(null);
    }
}
