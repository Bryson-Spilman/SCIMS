package scims.ui.swing.tables;

import scims.model.data.scoring.*;
import scims.model.enums.DistanceUnitSystem;
import scims.model.enums.WeightUnitSystem;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class EventsTableModel extends SCIMSTableModel<EventsRowData> {

    static final int CHECK_BOX_COL = 0;
    static final int NAME_COL = 1;
    static final int SCORE_TYPE_COL = 2;
    static final int TIME_LIMIT_COL = 3;
    static final int EVENT_ORDER_COL = 4;
    private boolean _eventOrderColumnEnabled = true;

    EventsTableModel() {
        setColumnNames();
        addRow(new EventsRowData(false, "Event 1", new TimeScoring(), null, null));
        addRow(new EventsRowData(false, "Event 2", new RepsScoring(),null, null));
        addRow(new EventsRowData(false, "Event 3", new DistanceScoring(),null, null));
        addRow(new EventsRowData(false, "Event 4", new RepsScoring(),null, null));
        addRow(new EventsRowData(false, "Event 5", new TimeScoring(),null, null));
    }

    private void setColumnNames() {
        setColumnName(CHECK_BOX_COL, "");
        setColumnName(NAME_COL, "Event Name");
        setColumnName(SCORE_TYPE_COL, "Score Type");
        setColumnName(TIME_LIMIT_COL, "<html><center>Time Limit<br>(seconds)</center></html>");
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
            case TIME_LIMIT_COL:
                Duration timeLimit = (rowData.getTimeLimit());
                retVal = timeLimit == null ? null : Long.valueOf(timeLimit.getSeconds()).doubleValue();
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
            case TIME_LIMIT_COL:
                retVal = Double.class;
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
                || (col == EVENT_ORDER_COL && _eventOrderColumnEnabled && isRowChecked(row))
                || col == TIME_LIMIT_COL;
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
                switchOrdersWithOneAlreadyUsed(order, row);
                rowData.setEventOrder(order);
                break;
            case TIME_LIMIT_COL:
                rowData.setTimeLimit(value == null ? null : Duration.ofSeconds(Double.valueOf(Double.parseDouble(value.toString())).longValue()));
                break;
            case SCORE_TYPE_COL:
            default:
                break;
        }
        fireTableCellUpdated(row, col);
    }

    private void switchOrdersWithOneAlreadyUsed(Integer orderWeAreSelecting, int row) {
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

    public void setDistanceUnits(DistanceUnitSystem selected) {
        for(EventsRowData rowData : getRowData()) {
            if(rowData.getEventScoring() instanceof DistanceScoring)
            {
                ((DistanceScoring)rowData.getEventScoring()).setUnitSystem(selected);
            }
        }
        fireTableDataChanged();
    }

    public void setWeightUnits(WeightUnitSystem selected) {
        for(EventsRowData rowData : getRowData()) {
            if(rowData.getEventScoring() instanceof WeightScoring)
            {
                ((WeightScoring)rowData.getEventScoring()).setUnitSystem(selected);
            }
        }
        fireTableDataChanged();
    }

}
