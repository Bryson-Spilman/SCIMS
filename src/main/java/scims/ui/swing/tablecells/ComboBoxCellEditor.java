package scims.ui.swing.tablecells;

import javax.swing.*;
import java.awt.*;

class ComboBoxCellEditor<T> extends DefaultCellEditor {
    private final JComboBox<T> _comboBox;
    private final JTable _parentTable;
    public ComboBoxCellEditor(JTable parentTable, JComboBox<T> comboBox) {
        super(comboBox);
        _parentTable = parentTable;
        this._comboBox = comboBox;
        comboBox.addActionListener(e -> stopCellEditing());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        _comboBox.setSelectedItem(value);
        _comboBox.setEnabled(table.isCellEditable(row, column));
        _comboBox.setEditable(table.isCellEditable(row, column));
        return _comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return _comboBox.getSelectedItem();
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        int row = _parentTable.getSelectedRow();
        int column = _parentTable.getSelectedColumn();
        _parentTable.getModel().setValueAt(_comboBox.getSelectedItem(), row, column);
        return super.stopCellEditing();
    }

}
