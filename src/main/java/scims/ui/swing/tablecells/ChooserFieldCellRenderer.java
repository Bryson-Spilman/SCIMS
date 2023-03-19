package scims.ui.swing.tablecells;

import scims.ui.swing.JChooserField;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class ChooserFieldCellRenderer<T> extends JChooserField<T> implements TableCellRenderer, HighlightedComponent {

    private final Window _parent;
    private Function<Integer, Boolean> _highlightRow;
    private Color _highlightColor;

    public ChooserFieldCellRenderer(Window parent, JChooserField<T> chooserField) {
        super(parent, chooserField.getObjects());
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
        if (value == null) {
            retVal.setObjects(new ArrayList<>());
            retVal.setText("");
        } else if (value instanceof List) {
            retVal.setObjects(getObjects());
            retVal.setSelectedObjects((List<T>) value);
        }
        if (_highlightRow != null && _highlightRow.apply(row)) {
            retVal.setBackground(_highlightColor);
        } else {
            retVal.setBackground(null);
        }
        return retVal;
    }
}
