package scims.ui.swing.tablecells;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.*;

class ComboBoxCellEditor<T> extends DefaultCellEditor {
    private final JComboBox<T> _comboBox;
    private final JTable _parentTable;
    public ComboBoxCellEditor(JTable parentTable, JComboBox<T> comboBox) {
        super(comboBox);
        _parentTable = parentTable;
        _comboBox = comboBox;
        comboBox.addActionListener(e -> stopCellEditing());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) _comboBox.getModel();
        List<Object> items = new ArrayList<>();
        for(int i=0; i < model.getSize(); i++) {
            Object elem = model.getElementAt(i);
            if(elem != null) {
                items.add(elem);
            }
        }
        List<Object> itemsUnique = items.stream().distinct().collect(toList());
        model.removeAllElements();
        for(Object item : itemsUnique) {
            model.addElement((T) item);
        }

        _comboBox.setModel(model);
        _comboBox.setSelectedItem(value);
        _comboBox.setEnabled(table.isCellEditable(row, column));
        return _comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return _comboBox.getSelectedItem();
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return super.stopCellEditing();
    }

}
