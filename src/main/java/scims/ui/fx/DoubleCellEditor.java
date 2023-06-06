package scims.ui.fx;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;

class DoubleCellEditor<T> extends TextCellEditor<T, String> {

    public DoubleCellEditor(TreeTableColumn<Object, Object> col) {
        super(col);
    }

    @Override
    void createTextField() {
        _textField = new DoubleTextField();
        _textField.setText(getItem() == null ? "" : String.valueOf(getItem()));
        _textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        _textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(_textField.getText());
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });
        setGraphic(_textField);
    }
}
