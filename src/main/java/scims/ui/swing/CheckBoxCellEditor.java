package scims.ui.swing;

import javax.swing.*;
import java.awt.*;

class CheckBoxCellEditor extends DefaultCellEditor {

    private final JCheckBox _checkBox;
    private final JTable _parentTable;
    public CheckBoxCellEditor(JTable parentTable, JCheckBox checkBox) {
        super(checkBox);
        _parentTable = parentTable;
        _checkBox = checkBox;
        _checkBox.addActionListener(e -> stopCellEditing());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        _checkBox.setSelected(value != null && Boolean.parseBoolean(value.toString()));
        _checkBox.setEnabled(table.isCellEditable(row, column));
        return _checkBox;
    }

    @Override
    public Object getCellEditorValue() {
        return _checkBox.isSelected();
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        int row = _parentTable.getSelectedRow();
        int column = _parentTable.getSelectedColumn();
        _parentTable.getModel().setValueAt(_checkBox.isSelected(), row, column);
        return super.stopCellEditing();
    }
}
