package scims.ui.swing;

import scims.ui.swing.tablecells.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class SCIMSTable extends JTable {

    private TableModel _model;

    public SCIMSTable() {
        initTable();
    }

    @Override
    public void setModel(TableModel dataModel) {
        _model = dataModel;
        super.setModel(dataModel);
        initTable();
    }

    private void initTable() {
        for(int col=0; col < _model.getColumnCount(); col++) {
            setTextFieldColumn(col);
        }
        setRowHeight(30);
        JTableHeader header = getTableHeader();
        if(header != null) {
            getTableHeader().setPreferredSize(new Dimension(0, 30));
        }
    }

    protected <T> JComboBox<T> setComboBoxColumn(int col) {
        ComboBoxTableColumn<T> newColumn = new ComboBoxTableColumn<>(this, col, new ArrayList<>());
        updateColumn(col, newColumn);
        return newColumn.getComboBox();
    }

    protected JCheckBox setCheckBoxColumn(int col) {
        CheckBoxTableColumn newColumn = new CheckBoxTableColumn(this, col);
        updateColumn(col, newColumn);
        return newColumn.getCheckBox();
    }

    protected JTextField setTextFieldColumn(int col) {
        TextFieldTableColumn newColumn = new TextFieldTableColumn(this, col);
        updateColumn(col, newColumn);
        return newColumn.getTextField();
    }

    protected JTextField setDoubleFieldColumn(int col) {
        DoubleFieldTableColumn newColumn = new DoubleFieldTableColumn(this, col);
        updateColumn(col, newColumn);
        return newColumn.getTextField();
    }

    protected JTextField setIntegerFieldColumn(int col) {
        IntegerFieldTableColumn newColumn = new IntegerFieldTableColumn(this, col);
        updateColumn(col, newColumn);
        return newColumn.getTextField();
    }

    protected <T> JChooserField<T> setChooserFieldColumn(int col) {
        ChooserFieldTableColumn<T> newColumn = new ChooserFieldTableColumn<>(this, col, new ArrayList<>());
        updateColumn(col, newColumn);
        return newColumn.getChooserField();
    }

    public void commitEdits()
    {
        int rowCount = getRowCount();
        int colCount = getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                TableCellEditor editor = getCellEditor(row, col);
                if (editor != null) {
                    editor.stopCellEditing();
                }
            }
        }
    }

    private <T> void updateColumn(int col, HighlightedTableColumn newColumn) {
        newColumn.setHighlightColor(getHighlightColor());
        newColumn.setHighlightCondition(this::isRowHighlighted);
        TableColumn oldColumn = getColumnModel().getColumn(col);
        Object header = oldColumn.getHeaderValue();
        newColumn.setHeaderValue(header);
        getColumnModel().removeColumn(oldColumn);
        getColumnModel().addColumn(newColumn);
        getColumnModel().moveColumn(getColumnCount()-1, col);
    }

    abstract Color getHighlightColor();

    abstract boolean isRowHighlighted(int row);
}
