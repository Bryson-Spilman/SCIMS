package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class DoubleFieldTableColumn extends TextFieldTableColumn {
    public DoubleFieldTableColumn(JTable parentTable, int modelIndex) {
        super(parentTable, modelIndex);
        ((AbstractDocument) getTextField().getDocument()).setDocumentFilter(new DoubleDocumentFilter());
    }
}
