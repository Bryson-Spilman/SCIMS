package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.event.FocusListener;

public class IntegerFieldTableColumn extends TextFieldTableColumn {

    public IntegerFieldTableColumn(JTable parentTable, int modelIndex) {
        super(parentTable, modelIndex);
        ((AbstractDocument) getTextField().getDocument()).setDocumentFilter(new IntegerDocumentFilter());
        setCellEditor(new IntegerFieldCellEditor(parentTable, getTextField()));
    }

    private static class IntegerFieldCellEditor extends TextFieldCellEditor {

        public IntegerFieldCellEditor(JTable parentTable, JTextField textField) {
            super(parentTable, textField);
        }

        @Override
        public Object getCellEditorValue() {
            Object value = super.getCellEditorValue();
            if(value != null) {
                value = Integer.parseInt(value.toString());
            }
            return value;
        }

        @Override
        protected FocusListener getFocusListener() {
            return null;
        }

        @Override
        protected DocumentFilter getDocumentFilter() {
            return new IntegerDocumentFilter();
        }
    }
}
