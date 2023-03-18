package scims.ui.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

public abstract class SCIMSTable extends JTable {

    private TableModel _model;

    public SCIMSTable() {
        setRenderer();
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
            getColumnModel().getColumn(col).setHeaderValue(_model.getColumnName(col));
        }
        setRowHeight(30);
        JTableHeader header = getTableHeader();
        if(header != null) {
            getTableHeader().setPreferredSize(new Dimension(0, 30));
        }
    }

    private void setRenderer() {
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isRowHighlighted(row)) {
                    c.setBackground(getHighlightColor());
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        });
    }

    protected <T> JComboBox<T> setComboBoxColumn(int eventOrderCol) {
        ComboBoxTableColumn<T> newColumn = new ComboBoxTableColumn<>(this, EventsTableModel.EVENT_ORDER_COL, new ArrayList<>());
        newColumn.setHighlightColor(getHighlightColor());
        newColumn.setHighlightCondition(this::isRowHighlighted);
        TableColumn oldColumn = getColumnModel().getColumn(eventOrderCol);
        Object header = oldColumn.getHeaderValue();
        newColumn.setHeaderValue(header);
        getColumnModel().removeColumn(oldColumn);
        getColumnModel().addColumn(newColumn);
        getColumnModel().moveColumn(getColumnCount()-1, eventOrderCol);
        return newColumn.getComboBox();
    }

    protected JCheckBox setCheckBoxColumn(int eventOrderCol) {
        CheckBoxTableColumn newColumn = new CheckBoxTableColumn(this, EventsTableModel.CHECK_BOX_COL);
        newColumn.setHighlightColor(getHighlightColor());
        newColumn.setHighlightCondition(this::isRowHighlighted);
        TableColumn oldColumn = getColumnModel().getColumn(eventOrderCol);
        Object header = oldColumn.getHeaderValue();
        newColumn.setHeaderValue(header);
        getColumnModel().removeColumn(oldColumn);
        getColumnModel().addColumn(newColumn);
        getColumnModel().moveColumn(getColumnCount()-1, eventOrderCol);
        return newColumn.getCheckBox();
    }

    abstract Color getHighlightColor();

    abstract boolean isRowHighlighted(int row);
}
