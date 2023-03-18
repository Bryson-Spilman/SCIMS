package scims.ui.swing;

import scims.model.data.scoring.DistanceScoring;
import scims.model.data.scoring.RepsScoring;
import scims.model.data.scoring.TimedScoring;

import javax.swing.table.AbstractTableModel;
import java.util.*;

class EventsTableModel extends AbstractTableModel {

    static final int CHECK_BOX_COL = 0;
    static final int NAME_COL = 1;
    static final int SCORE_TYPE_COL = 2;
    private final Map<Integer, String> _columnNames = new HashMap<>();

    EventsTableModel() {
        setColumnNames();
    }

    private void setColumnNames() {
        _columnNames.put(CHECK_BOX_COL, "");
        _columnNames.put(NAME_COL, "Name");
        _columnNames.put(SCORE_TYPE_COL, "Score Type");
    }

    private final List<EventsRowData> _data = Arrays.asList(
            new EventsRowData(false, "Event 1", new TimedScoring()),
            new EventsRowData(false, "Event 2", new RepsScoring()),
            new EventsRowData(false, "Event 3", new DistanceScoring()),
            new EventsRowData(false, "Event 4", new RepsScoring()),
            new EventsRowData(false, "Event 5", new TimedScoring())
    );

    @Override
    public int getColumnCount() {
        return _columnNames.size();
    }

    @Override
    public int getRowCount() {
        return _data.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        EventsRowData rowData = _data.get(row);
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
            default:
                retVal = "";
                break;
        }
        return retVal;
    }

    @Override
    public String getColumnName(int col) {
        return _columnNames.get(col);
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == CHECK_BOX_COL;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        EventsRowData rowData = _data.get(row);
        switch (col)
        {
            case CHECK_BOX_COL:
                 rowData.setChecked(value != null && (boolean) value);
                break;
            case NAME_COL:
                rowData.setName(String.valueOf(value));
                break;
            case SCORE_TYPE_COL:
            default:
                break;
        }
        fireTableCellUpdated(row, col);
    }
}
