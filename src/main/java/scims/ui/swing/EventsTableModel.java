package scims.ui.swing;

import scims.model.data.scoring.DistanceScoring;
import scims.model.data.scoring.RepsScoring;
import scims.model.data.scoring.TimedScoring;

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
                retVal = rowData.getEventScoring().getScoreType();
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
            case SCORE_TYPE_COL:
                retVal = String.class;
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
        return col == CHECK_BOX_COL || (col == EVENT_ORDER_COL && _eventOrderColumnEnabled && isRowChecked(row));
    }

    private boolean isRowChecked(int row) {
        Object checked = getValueAt(row, CHECK_BOX_COL);
        return checked != null && Boolean.parseBoolean(checked.toString());
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if(row < 0 || col < 0) {
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
            case SCORE_TYPE_COL:
            case EVENT_ORDER_COL:
                rowData.setEventOrder(value == null ? null : Integer.parseInt(value.toString()));
                break;
            default:
                break;
        }
        fireTableCellUpdated(row, col);
    }

    public void setEventOrderColumnEnabled(boolean enabled) {
        _eventOrderColumnEnabled = enabled;
        fireTableDataChanged();
    }
}
