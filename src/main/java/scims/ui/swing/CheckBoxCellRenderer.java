package scims.ui.swing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Function;

class CheckBoxCellRenderer extends JCheckBox implements TableCellRenderer {

    private Function<Integer, Boolean> _highlightRow;
    private Color _highlightColor;

    public CheckBoxCellRenderer() {
    }

    public void setHighlightCondition(Function<Integer, Boolean> highlightRow) {
        _highlightRow = highlightRow;
    }

    public void setHighlightColor(Color highlightColor) {
        _highlightColor = highlightColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JCheckBox retVal = new JCheckBox();
        retVal.setSelected(value != null && Boolean.parseBoolean(value.toString()));
        if(_highlightRow.apply(row)) {
            retVal.setBackground(_highlightColor);
        }
        return retVal;
    }
}
