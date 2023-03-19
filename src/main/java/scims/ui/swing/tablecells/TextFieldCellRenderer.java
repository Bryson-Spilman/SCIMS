package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Function;

public class TextFieldCellRenderer extends JLabel implements TableCellRenderer, HighlightedComponent {

    private Function<Integer, Boolean> _highlightRow;
    private Color _highlightColor;
    public void setHighlightCondition(Function<Integer, Boolean> highlightRow) {
        _highlightRow = highlightRow;
    }

    public void setHighlightColor(Color highlightColor) {
        _highlightColor = highlightColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel retVal = new JLabel();
        retVal.setOpaque(true);
        if(value != null) {
            retVal.setText(value.toString());
        }
        if(_highlightRow != null && _highlightRow.apply(row)) {
            retVal.setBackground(_highlightColor);
        } else {
            retVal.setBackground(null);
        }
        return retVal;
    }
}
