package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Function;

class ComboBoxCellRenderer<T> extends JComboBox<T> implements TableCellRenderer, HighlightedComponent {

    private Function<Integer, Boolean> _highlightRow;
    private Color _highlightColor;

    public ComboBoxCellRenderer(JComboBox<T> comboBox) {
        super(comboBox.getModel());
    }

    public void setHighlightCondition(Function<Integer, Boolean> highlightRow) {
        _highlightRow = highlightRow;
    }

    public void setHighlightColor(Color highlightColor) {
        _highlightColor = highlightColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component retVal;
        if(value == null) {
            retVal = new JComboBox<>();
        } else {
            retVal = new JComboBox<>(new Integer[]{Integer.parseInt(value.toString())});
        }
        if(_highlightRow != null && _highlightRow.apply(row)) {
            retVal.setBackground(_highlightColor);
        } else {
            retVal.setBackground(null);
        }
        return retVal;
    }
}
