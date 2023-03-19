package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Function;

public class TextFieldTableColumn extends HighlightedTableColumn {
    private final JTextField _textField;
    private final TextFieldCellRenderer _renderer;
    private final TextFieldCellEditor _editor;

    public TextFieldTableColumn(JTable parentTable, int modelIndex) {
        super(modelIndex);
        _textField = new JTextField();
        _renderer = new TextFieldCellRenderer();
        _editor = new TextFieldCellEditor(parentTable, _textField);
    }

    public JTextField getTextField() {
        return _textField;
    }

    @Override
    public void setHighlightCondition(Function<Integer, Boolean> highlightRow) {
        _renderer.setHighlightCondition(highlightRow);
    }

    @Override
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
