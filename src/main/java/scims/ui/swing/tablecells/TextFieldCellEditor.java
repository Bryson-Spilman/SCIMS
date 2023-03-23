package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class TextFieldCellEditor extends DefaultCellEditor {
    private final JTextField _textField;
    private final JTable _parentTable;
    private int _editingRow;
    private int _editingColumn;
    public TextFieldCellEditor(JTable parentTable, JTextField textField) {
        super(textField);
        _textField = textField;
        _parentTable = parentTable;
        textField.addActionListener(e -> stopCellEditing());
        FocusListener focusListener = getFocusListener();
        if(focusListener != null) {
            textField.addFocusListener(focusListener);
        }
    }

    protected FocusListener getFocusListener() {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // get the editing row and column
                _editingRow = _parentTable.getEditingRow();
                _editingColumn = _parentTable.getEditingColumn();
            }

            @Override
            public void focusLost(FocusEvent e) {
                // get the new cell value from the JTextField
                Object value = _textField.getText();

                // apply the new value to the editing cell
                _parentTable.getModel().setValueAt(value, _editingRow, _editingColumn);

                // stop editing the cell
                _parentTable.getCellEditor(_editingRow, _editingColumn).stopCellEditing();
            }
        };
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        _textField.setText(value == null ? null : value.toString());
        ((AbstractDocument) _textField.getDocument()).setDocumentFilter(getDocumentFilter());
        _textField.setEnabled(table.isCellEditable(row, column));
        _textField.setEditable(table.isCellEditable(row, column));
        return _textField;
    }

    protected DocumentFilter getDocumentFilter() {
        return new DocumentFilter();
    }

    @Override
    public Object getCellEditorValue() {
        return _textField.getText();
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        _parentTable.getModel().setValueAt(getCellEditorValue(), _editingRow, _editingColumn);
        return super.stopCellEditing();
    }
}
