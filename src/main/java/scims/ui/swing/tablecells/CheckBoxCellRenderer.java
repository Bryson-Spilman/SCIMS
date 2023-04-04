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
        JPanel checkBoxPanel = new JPanel();
        JCheckBox checkBox = new JCheckBox();
        checkBoxPanel.add(checkBox);
        checkBox.setSelected(value != null && Boolean.parseBoolean(value.toString()));
        if(_highlightRow != null && _highlightRow.apply(row)) {
            checkBoxPanel.setBackground(_highlightColor);
            checkBox.setBackground(_highlightColor);
        } else {
            checkBoxPanel.setBackground(null);
            checkBox.setBackground(null);
        }
        if(!table.isCellEditable(row,column)) {
            checkBox.setEnabled(false);
            checkBoxPanel.setEnabled(false);
        }
        applySelectedHighlighting(checkBoxPanel, table, column, row);
        return checkBoxPanel;
    }
}
