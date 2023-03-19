package scims.ui.swing;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SCIMSTableModel<T> extends AbstractTableModel {
    private final Map<Integer, String> _columnNames = new HashMap<>();
    private final List<T> _data = new ArrayList<>();
    public SCIMSTableModel() {
    }
    public void setColumnName(int col, String name) {
        _columnNames.put(col, name);
    }

    @Override
    public int getRowCount() {
        return _data.size();
    }

    @Override
    public int getColumnCount() {
        return _columnNames.size();
    }

    @Override
    public String getColumnName(int col) {
        return _columnNames.get(col);
    }

    public List<T> getRowData() {
        return _data;
    }

    public void addRow(T rowData) {
        _data.add(rowData);
    }

    protected boolean isWrongType(Object aValue, int rowIndex, int columnIndex) {
        return aValue != null && !getColumnClass(columnIndex).isInstance(aValue);
    }
}
