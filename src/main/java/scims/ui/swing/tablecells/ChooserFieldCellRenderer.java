package scims.ui.swing.tablecells;

import scims.ui.swing.JChooserField;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Function;

class ChooserFieldCellRenderer<T> extends JChooserField<T> implements TableCellRenderer, HighlightedComponent {

    private final Window _parent;
    private final JChooserField<T> _chooserField;
    private Function<Integer, Boolean> _highlightRow;
    private Color _highlightColor;

    public ChooserFieldCellRenderer(Window parent, JChooserField<T> chooserField) {
        super(parent, chooserField.getObjects());
        _chooserField = chooserField;
        _parent = parent;
    }

    @Override
    public void setHighlightCondition(Function<Integer, Boolean> highlightRow) {
        _highlightRow = highlightRow;
    }

    @Override
    public void setHighlightColor(Color highlightColor) {
        _highlightColor = highlightColor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JChooserField<T> retVal = new JChooserField<>(_parent, getObjects());
        retVal.setObjects(_chooserField.getObjects());
        retVal.setText(_chooserField.getText());
        if (_highlightRow != null && _highlightRow.apply(row)) {
            retVal.setBackground(_highlightColor);
        }
        if(!table.isCellEditable(row,column)) {
            retVal.setEnabled(false);
        }
        return retVal;
    }
}
