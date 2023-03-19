package scims.ui.swing.tablecells;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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
        DefaultComboBoxModel<Object> model = (DefaultComboBoxModel<Object>) _comboBox.getModel();
        Set<Object> items = new TreeSet<>();
        for(int i=0; i < model.getSize(); i++) {
            items.add(model.getElementAt(i));
        }
        model.removeAllElements();
        for(Object item : items) {
            model.addElement(item);
        }
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
