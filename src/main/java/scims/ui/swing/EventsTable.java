package scims.ui.swing;

import scims.model.data.Event;
import scims.model.data.StrengthEventBuilder;
import scims.model.data.scoring.EventScoring;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class EventsTable extends JTable {

    private boolean _ignoreSelectionChange;

    public EventsTable() {
        super(new EventsTableModel());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setRenderer();
        initTable();
        addListeners();
    }

    private void setRenderer() {
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                List<Integer> checkedRows = getCheckedRows();
                if (checkedRows.contains(row)) {
                    c.setBackground(Color.YELLOW);
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        });
    }

    private void addListeners() {
        EventsTableModel tableModel = (EventsTableModel) getModel();
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 0) {
                tableModel.fireTableChanged(new TableModelEvent(tableModel));
            }
        });
    }

    private List<Integer> getCheckedRows() {
        List<Integer> retVal = new ArrayList<>();
        for(int row=0; row < getModel().getRowCount(); row++) {
            Boolean checked = (Boolean) getValueAt(row, EventsTableModel.CHECK_BOX_COL);
            if(checked) {
                retVal.add(row);
            }
        }
        return retVal;
    }

    private void initTable() {
        getColumnModel().getColumn(0).setHeaderValue("");
        getColumnModel().getColumn(0).setMaxWidth(30);
        getColumnModel().getColumn(1).setHeaderValue("Name");
        getColumnModel().getColumn(2).setHeaderValue("ScoreType");
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class<?> retVal = super.getColumnClass(column);
        if (column == EventsTableModel.CHECK_BOX_COL) {
            retVal = Boolean.class;
        } else if (column == EventsTableModel.NAME_COL){
            retVal = String.class;
        } else if (column == EventsTableModel.SCORE_TYPE_COL) {
            retVal = String.class;
        }
        return retVal;
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0) {
            return new TableCellRenderer() {
                final JCheckBox checkbox = new JCheckBox();
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    checkbox.setSelected(value != null && (boolean) value);
                    return checkbox;
                }
            };
        } else {
            return super.getCellRenderer(row, column);
        }
    }

    List<Event> buildSelectedEvents()
    {
        List<Event> retVal = new ArrayList<>();
        List<Integer> checkedRows = getCheckedRows();
        for(int checkedRow : checkedRows) {
            String name = (String) getValueAt(checkedRow, EventsTableModel.NAME_COL);
            EventScoring<?> scoreType = (EventScoring<?>) getValueAt(checkedRow, EventsTableModel.SCORE_TYPE_COL);
            retVal.add(new StrengthEventBuilder().withName(name).withScoring(scoreType).build());
        }
        return retVal;
    }

}
