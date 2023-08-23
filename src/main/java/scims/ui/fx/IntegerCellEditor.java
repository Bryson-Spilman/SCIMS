package scims.ui.fx;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;

class IntegerCellEditor<T> extends TextCellEditor<T, String> {

    public IntegerCellEditor(TreeTableColumn<Object, Object> col) {
        super(col);
    }

    @Override
    void createTextField() {
        _textField = new IntegerTextField();
        _textField.setText(getItem() == null ? "" : String.valueOf(getItem()));
        _textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        _textField.setOnAction(evt -> {
            try {
                commitEdit(_textField.getText());
            } catch (NumberFormatException e) {
                cancelEdit();
            }
        });
        _textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    TreeTableColumn<T, String> col = getTableColumn();
                    TreeTablePosition<T,String> editingCell = new TreeTablePosition<>(getTreeTableView(), getIndex(), getTableColumn());
                    manualCommitEdit(_textField.getText(), editingCell, col);
                } catch (NumberFormatException e) {
                    cancelEdit();
                }
            }
        });
        setGraphic(_textField);
        _textField.selectAll();
        _textField.requestFocus();
        _textField.setEditable(isCellEditable(getTreeTableRow()));
    }

}