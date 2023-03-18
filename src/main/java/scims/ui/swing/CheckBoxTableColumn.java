package scims.ui.swing;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.function.Function;

class CheckBoxTableColumn extends TableColumn {
    private final JCheckBox _checkBox;
    private final CheckBoxCellRenderer _renderer;
    private final CheckBoxCellEditor _editor;

    public CheckBoxTableColumn(JTable parentTable, int modelIndex) {
        super(modelIndex);
        _checkBox = new JCheckBox();
        _renderer = new CheckBoxCellRenderer();
        _editor = new CheckBoxCellEditor(parentTable, _checkBox);
    }

    public JCheckBox getCheckBox() {
        return _checkBox;
    }

    public void setHighlightCondition(Function<Integer, Boolean> highlightRow) {
        _renderer.setHighlightCondition(highlightRow);
    }

    public void setHighlightColor(Color highlightColor) {
        _renderer.setHighlightColor(highlightColor);
    }

    @Override
    public TableCellRenderer getCellRenderer() {
        return _renderer;
    }

    @Override
    public TableCellEditor getCellEditor() {
        return _editor;
    }
}