package scims.ui.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;

class DoubleCellEditor<T> extends TextCellEditor<T, String> {

    public DoubleCellEditor(TreeTableColumn<Object, Object> col) {
        super(col);
    }

    @Override
    void createTextField() {
        TextField textField = new DoubleTextField();
        textField.setText(getItem() == null ? "" : String.valueOf(getItem()));
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        textField.setOnAction(evt -> {
            try {
                commitEdit(textField.getText());
            } catch (NumberFormatException e) {
                cancelEdit();
            }
        });
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    commitEdit(textField.getText());
                } catch (NumberFormatException e) {
                    cancelEdit();
                }
            }
        });
        setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
        textField.setEditable(isCellEditable(getTreeTableRow()));
    }
}
