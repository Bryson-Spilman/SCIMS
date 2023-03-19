package scims.ui.swing.tablecells;

import javax.swing.*;
import java.awt.*;

class TextFieldCellEditor extends DefaultCellEditor {
    private final JTextField _textField;
    private final JTable _parentTable;

    public TextFieldCellEditor(JTable parentTable, JTextField textField) {
        super(textField);
        _textField = textField;
        _parentTable = parentTable;
        textField.addActionListener(e -> stopCellEditing());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        _textField.setText(value == null ? null : value.toString());
        _textField.setEnabled(table.isCellEditable(row, column));
        _textField.setEditable(table.isCellEditable(row, column));
        return _textField;
    }

    @Override
    public Object getCellEditorValue() {
        return _textField.getText();
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        int row = _parentTable.getSelectedRow();
        int column = _parentTable.getSelectedColumn();
        _parentTable.getModel().setValueAt(_textField.getText(), row, column);
        return super.stopCellEditing();
    }
}
