package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class ComboBoxTableColumn<T> extends HighlightedTableColumn {
    private final JComboBox<T> _comboBox;
    private final ComboBoxCellRenderer<T> _renderer;
    private final ComboBoxCellEditor<T> _editor;

    @SuppressWarnings("unchecked")
    public ComboBoxTableColumn(JTable parentTable, int modelIndex, List<T> items) {
        super(modelIndex);
        _comboBox = new JComboBox<>((T[])items.toArray());
        _comboBox.setEditable(false);
        _renderer = new ComboBoxCellRenderer<>(_comboBox);
        _editor = new ComboBoxCellEditor<>(parentTable, _comboBox);
    }

    public JComboBox<T> getComboBox() {
        return _comboBox;
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
