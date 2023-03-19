package scims.ui.swing;

import scims.model.data.scoring.DistanceScoring;
import scims.model.data.scoring.EventScoring;
import scims.model.data.scoring.RepsScoring;
import scims.model.data.scoring.TimedScoring;

import java.util.ArrayList;
import java.util.List;

class EventsTableModel extends SCIMSTableModel<EventsRowData> {

    static final int CHECK_BOX_COL = 0;
    static final int NAME_COL = 1;
    static final int SCORE_TYPE_COL = 2;
    static final int EVENT_ORDER_COL = 3;
    private boolean _eventOrderColumnEnabled = true;

    EventsTableModel() {
        setColumnNames();
        addRow(new EventsRowData(false, "Event 1", new TimedScoring(), null));
        addRow(new EventsRowData(false, "Event 2", new RepsScoring(), null));
        addRow(new EventsRowData(false, "Event 3", new DistanceScoring(), null));
        addRow(new EventsRowData(false, "Event 4", new RepsScoring(), null));
        addRow(new EventsRowData(false, "Event 5", new TimedScoring(), null));
    }

    private void setColumnNames() {
        setColumnName(CHECK_BOX_COL, "");
        setColumnName(NAME_COL, "Event Name");
        setColumnName(SCORE_TYPE_COL, "Score Type");
        setColumnName(EVENT_ORDER_COL, "Event Order");
    }

    @Override
    public Object getValueAt(int row, int col) {
        EventsRowData rowData = getRowData().get(row);
        Object retVal;
        switch (col)
        {
            case CHECK_BOX_COL:
                retVal = rowData.isChecked();
                break;
            case NAME_COL:
                retVal = rowData.getName();
                break;
            case SCORE_TYPE_COL:
                retVal = rowData.getEventScoring();
                break;
            case EVENT_ORDER_COL:
                retVal = rowData.getEventOrder();
                break;
            default:
                retVal = "";
                break;
        }
        return retVal;
    }

    @Override
    public Class<?> getColumnClass(int c) {
        Class<?> retVal;
        switch(c) {
            case CHECK_BOX_COL:
                retVal = Boolean.class;
                break;
            case NAME_COL:
                retVal = String.class;
                break;
            case SCORE_TYPE_COL:
                retVal = EventScoring.class;
                break;
            case EVENT_ORDER_COL:
                retVal = Integer.class;
                break;
            default:
                retVal = super.getColumnClass(c);
                break;
        }
        return retVal;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == CHECK_BOX_COL || col == NAME_COL
                || (col == EVENT_ORDER_COL && _eventOrderColumnEnabled && isRowChecked(row));
    }

    private boolean isRowChecked(int row) {
        Object checked = getValueAt(row, CHECK_BOX_COL);
        return checked != null && Boolean.parseBoolean(checked.toString());
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if(row < 0 || col < 0 || isWrongType(value, row, col)) {
            return;
        }
        EventsRowData rowData = getRowData().get(row);
        switch (col)
        {
            case CHECK_BOX_COL:
                 rowData.setChecked(value != null && (boolean) value);
                break;
            case NAME_COL:
                rowData.setName(String.valueOf(value));
                break;
            case EVENT_ORDER_COL:
                Integer order = value == null ? null : Integer.parseInt(value.toString());
                switchOrdersWithOneAlreadyUsed(order, row, col);
                rowData.setEventOrder(order);
                break;
            case SCORE_TYPE_COL:
            default:
                break;
        }
        fireTableCellUpdated(row, col);
    }

    private void switchOrdersWithOneAlreadyUsed(Integer orderWeAreSelecting, int row, int col) {
        Integer currentRowUsingOrder = getCurrentRowUsingOrder(orderWeAreSelecting);
        if(currentRowUsingOrder != null) {
            Integer oldValueThisRow = getRowData().get(row).getEventOrder();
            getRowData().get(currentRowUsingOrder).setEventOrder(oldValueThisRow);
        }
    }

    public void setEventOrderColumnEnabled(boolean enabled) {
        _eventOrderColumnEnabled = enabled;
        fireTableDataChanged();
    }

    private Integer getCurrentRowUsingOrder(Integer order) {
        Integer retVal = null;
        if(order != null) {
            List<Integer> checkedRows = getCheckedRows();
            for(Integer row : checkedRows) {
                Object orderObj = getValueAt(row, EventsTableModel.EVENT_ORDER_COL);
                if(orderObj != null) {
                    int orderInt = Integer.parseInt(orderObj.toString());
                    if(orderInt == order) {
                        retVal = row;
                        break;
                    }
                }
            }
        }
        return retVal;
    }

    List<Integer> getCheckedRows() {
        List<Integer> retVal = new ArrayList<>();
        for(int row=0; row < getRowCount(); row++) {
            Boolean checked = (Boolean) getValueAt(row, EventsTableModel.CHECK_BOX_COL);
            if(checked) {
                retVal.add(row);
            }
        }
        return retVal;
    }
}
