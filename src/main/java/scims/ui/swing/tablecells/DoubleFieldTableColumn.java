package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

public class DoubleFieldTableColumn extends TextFieldTableColumn {
    public DoubleFieldTableColumn(JTable parentTable, int modelIndex) {
        super(parentTable, modelIndex);
        ((AbstractDocument) getTextField().getDocument()).setDocumentFilter(new DoubleDocumentFilter());
        setCellEditor(new DoubleFieldCellEditor(parentTable, getTextField()));
    }

    private static class DoubleFieldCellEditor extends TextFieldCellEditor {

        public DoubleFieldCellEditor(JTable parentTable, JTextField textField) {
            super(parentTable, textField);
        }

        @Override
        public Object getCellEditorValue() {
            Object value = super.getCellEditorValue();
            if(value != null) {
                value = Double.parseDouble(value.toString());
            }
            return value;
        }

        @Override
        protected DocumentFilter getDocumentFilter() {
            return new DoubleDocumentFilter();
        }
    }
}
