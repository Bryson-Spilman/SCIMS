package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Function;

class CheckBoxCellRenderer extends JCheckBox implements TableCellRenderer, HighlightedComponent {

    private Function<Integer, Boolean> _highlightRow;
    private Color _highlightColor;

    @Override
    public void setHighlightCondition(Function<Integer, Boolean> highlightRow) {
        _highlightRow = highlightRow;
    }

    @Override
    public void setHighlightColor(Color highlightColor) {
        _highlightColor = highlightColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JCheckBox retVal = new JCheckBox();
        retVal.setSelected(value != null && Boolean.parseBoolean(value.toString()));
        if(_highlightRow != null && _highlightRow.apply(row)) {
            retVal.setBackground(_highlightColor);
        } else {
            retVal.setBackground(null);
        }
        if(!table.isCellEditable(row,column)) {
            retVal.setEnabled(false);
        }
        return retVal;
    }
}
