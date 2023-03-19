package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class IntegerFieldTableColumn extends TextFieldTableColumn {

    public IntegerFieldTableColumn(JTable parentTable, int modelIndex) {
        super(parentTable, modelIndex);
        ((AbstractDocument) getTextField().getDocument()).setDocumentFilter(new IntegerDocumentFilter());
    }
}
