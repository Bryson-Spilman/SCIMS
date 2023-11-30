package scims.ui.swing.tables;

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
        fireTableDataChanged();
    }

    public void removeRow(T rowData) {
        _data.remove(rowData);
        fireTableDataChanged();
    }

    protected boolean isCorrectType(Object aValue, int columnIndex) {
        return aValue == null || aValue.toString().isEmpty() || getColumnClass(columnIndex).isInstance(aValue)
                || (getColumnClass(columnIndex).equals(Integer.class) && canConvertToInteger(aValue))
                || (getColumnClass(columnIndex).equals(Double.class) && canConvertToDouble(aValue));
    }

    private boolean canConvertToInteger(Object aValue) {
        boolean retVal = true;
        try {
            Integer.parseInt(aValue.toString());
        } catch (NumberFormatException e) {
            retVal = false;
        }
        return retVal;
    }

    private boolean canConvertToDouble(Object aValue) {
        boolean retVal = true;
        try {
            Double.parseDouble(aValue.toString());
        } catch (NumberFormatException e) {
            retVal = false;
        }
        return retVal;
    }

    void clear() {
        _data.clear();
        fireTableDataChanged();
    }
}
