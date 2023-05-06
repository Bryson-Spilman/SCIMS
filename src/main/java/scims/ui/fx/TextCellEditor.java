package scims.ui.fx;

import javafx.scene.control.*;

class TextCellEditor<T, S> extends TreeTableCell<T, S> {


    protected TextField _textField;

    public TextCellEditor(TreeTableColumn<Object, Object> col) {
        itemProperty().addListener((observable, oldValue, newValue) -> {
           if(col instanceof LinkedTreeTableColumn) {
               TreeTableRow<T> row = getTreeTableRow();
               ((LinkedTreeTableColumn)col).updateLinkedCells((TreeItem<Object>) row.getTreeItem());
           }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if(_textField == null && isCellEditable(getTreeTableRow()))
        {
            createTextField();
        }
        setText(null);
        setGraphic(_textField);
        _textField.requestFocus();
        _textField.selectAll();
        _textField.setEditable(isCellEditable(getTreeTableRow()));
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(_textField.getText());
        setGraphic(null);
    }


    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.toString());
            setGraphic(null);
        }
    }


    void createTextField() {
        _textField = new TextField(getItem() == null ? null : getItem().toString());
        _textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        _textField.setOnAction(evt -> commitEdit((S)_textField.getText()));
        _textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit((S)_textField.getText());
            }
        });
        setGraphic(_textField);
        _textField.selectAll();
        _textField.requestFocus();
        _textField.setEditable(isCellEditable(getTreeTableRow()));
    }

    boolean isCellEditable(TreeTableRow<?> row) {
       return true;
    }

}
