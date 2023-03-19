package scims.ui.swing.tablecells;

import scims.ui.swing.JChooserField;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class ChooserFieldTableColumn<T> extends HighlightedTableColumn {

    private final JChooserField<T> _chooserField;
    private final ChooserFieldCellRenderer<T> _renderer;
    private final ChooserFieldCellEditor<T> _editor;

    @SuppressWarnings("unchecked")
    public ChooserFieldTableColumn(JTable parentTable, int modelIndex, List<T> items) {
        super(modelIndex);
        Window parentWindow = SwingUtilities.getWindowAncestor(parentTable);
        _chooserField = new JChooserField<>(parentWindow, items);
        _renderer = new ChooserFieldCellRenderer<T>(parentWindow, _chooserField);
        _editor = new ChooserFieldCellEditor<>(parentTable, _chooserField);
    }

        public JChooserField<T> getChooserField() {
            return _chooserField;
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

        public void setEnabled(boolean enabled) {
            _chooserField.setEnabled(enabled);
        }

}
