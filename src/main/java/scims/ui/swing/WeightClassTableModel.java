package scims.ui.swing;

import scims.model.data.Event;

import java.util.ArrayList;
import java.util.stream.Collectors;

class WeightClassTableModel extends SCIMSTableModel<WeightClassRowData> {

    static final int CHECK_BOX_COL = 0;
    static final int NAME_COL = 1;
    static final int MAX_COMPETITOR_WEIGHT_COL = 2;
    static final int MAX_NUM_COMPETITORS_COL = 3;
    static final int EVENTS_COL = 4;
    private boolean _useSameEventsForAllWeightClasses = true;

    WeightClassTableModel() {
        setColumnNames();
        addRow(new WeightClassRowData(false, "Light Weight Men", 200.4, 15, new ArrayList<>()));
    }

    void setUseSameEventsForAllWeightClasses(boolean useSameEventsForAllWeightClasses) {
        _useSameEventsForAllWeightClasses = useSameEventsForAllWeightClasses;
    }

    private void setColumnNames() {
        setColumnName(CHECK_BOX_COL, "");
        setColumnName(NAME_COL, "Weight Class Name");
        setColumnName(MAX_COMPETITOR_WEIGHT_COL, "Max Competitor Weight");
        setColumnName(MAX_NUM_COMPETITORS_COL, "Max Number of Competitors");
        setColumnName(EVENTS_COL, "Events");
    }

    @Override
    public Class<?> getColumnClass(int c) {
        Class<?> retVal;
        switch(c) {
            case CHECK_BOX_COL:
                retVal = Boolean.class;
                break;
            case NAME_COL:
            case EVENTS_COL:
                retVal = String.class;
                break;
            case MAX_COMPETITOR_WEIGHT_COL:
                retVal = Double.class;
                break;
            case MAX_NUM_COMPETITORS_COL:
                retVal = Integer.class;
                break;
            default:
                retVal = super.getColumnClass(c);
                break;
        }
        return retVal;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WeightClassRowData rowData = getRowData().get(rowIndex);
        Object retVal;
        switch (columnIndex)
        {
            case CHECK_BOX_COL:
                retVal = rowData.isChecked();
                break;
            case NAME_COL:
                retVal = rowData.getName();
                break;
            case MAX_COMPETITOR_WEIGHT_COL:
                retVal = rowData.getMaxCompetitorWeight();
                break;
            case MAX_NUM_COMPETITORS_COL:
                retVal = rowData.getMaxNumberCompetitors();
                break;
            case EVENTS_COL:
                retVal = rowData.getEvents().stream().map(Event::getName).collect(Collectors.joining(";"));
                break;
            default:
                retVal = null;
                break;
        }
        return retVal;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex < 0 || columnIndex < 0) {
            return;
        }
        WeightClassRowData rowData = getRowData().get(rowIndex);
        switch (columnIndex)
        {
            case CHECK_BOX_COL:
                rowData.setChecked(aValue != null && Boolean.parseBoolean(aValue.toString()));
                break;
            case NAME_COL:
                rowData.setName(aValue.toString());
                break;
            case MAX_COMPETITOR_WEIGHT_COL:
                rowData.setMaxCompetitorWeight(aValue == null ? Double.MAX_VALUE : Double.parseDouble(aValue.toString()));
                break;
            case MAX_NUM_COMPETITORS_COL:
                rowData.setMaxNumberCompetitors(aValue == null ? Integer.MAX_VALUE : Integer.parseInt(aValue.toString()));
                break;
            case EVENTS_COL:
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != EVENTS_COL || !_useSameEventsForAllWeightClasses;
    }
}
