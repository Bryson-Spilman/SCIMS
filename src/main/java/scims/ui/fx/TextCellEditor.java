package scims.ui.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;

class TextCellEditor<T, S> extends TreeTableCell<T, S> {


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
        setText(getItem() == null ? null : getItem().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(item == null ? null : item.toString());
        }
    }

    void createTextField() {
        TextField textField = new TextField(getItem() == null ? null : getItem().toString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        textField.setOnAction(evt -> commitEdit((S)textField.getText()));
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit((S)textField.getText());
            }
        });
        setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    public void commitEdit(S newValue) {
        super.commitEdit(newValue);
        setGraphic(null);
    }
}
