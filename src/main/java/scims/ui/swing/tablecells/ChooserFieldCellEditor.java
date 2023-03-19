package scims.ui.swing.tablecells;

import scims.ui.swing.JChooserField;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class ChooserFieldCellEditor<T> extends AbstractCellEditor implements TableCellEditor {
    private final JChooserField<T> _chooserField;
    private final JTable _parentTable;
    public ChooserFieldCellEditor(JTable parentTable, JChooserField<T> chooserField) {
        _parentTable = parentTable;
        _chooserField = chooserField;
        chooserField.addOnSelectionEvent(this::stopCellEditing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        _chooserField.setEnabled(table.isCellEditable(row, column));
        _chooserField.setEditable(table.isCellEditable(row, column));
        return _chooserField;
    }

    @Override
    public Object getCellEditorValue() {
        return _chooserField.getSelectedObjects();
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        int row = _parentTable.getSelectedRow();
        int column = _parentTable.getSelectedColumn();
        _parentTable.getModel().setValueAt(_chooserField.getSelectedObjects(), row, column);
        return super.stopCellEditing();
    }
}
