package scims.ui.swing.tables;

import scims.model.data.Event;
import scims.model.data.StrengthEvent;
import scims.model.data.WeightClass;
import scims.model.enums.WeightUnitSystem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class WeightClassTableModel extends SCIMSTableModel<WeightClassRowData> {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    static final int CHECK_BOX_COL = 0;
    static final int NAME_COL = 1;
    static final int MAX_COMPETITOR_WEIGHT_COL = 2;
    static final int MAX_NUM_COMPETITORS_COL = 3;
    static final int EVENTS_COL = 4;
    private boolean _useSameEventsForAllWeightClasses = true;

    WeightClassTableModel() {
        setColumnNames();
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
                retVal = String.class;
                break;
            case EVENTS_COL:
                retVal = List.class;
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
                if(retVal != null) {
                    retVal = Double.parseDouble(DECIMAL_FORMAT.format(retVal));
                }
                break;
            case MAX_NUM_COMPETITORS_COL:
                retVal = rowData.getMaxNumberCompetitors();
                break;
            case EVENTS_COL:
                retVal = rowData.getEvents().stream().map(Event::getName).collect(Collectors.joining("; "));
                break;
            default:
                retVal = null;
                break;
        }
        return retVal;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex < 0 || columnIndex < 0 || !isCorrectType(aValue, columnIndex)) {
            return;
        }
        WeightClassRowData rowData = getRowData().get(rowIndex);
        if(aValue != null && aValue.toString().isEmpty())
        {
            aValue = null;
        }
        switch (columnIndex)
        {
            case CHECK_BOX_COL:
                rowData.setChecked(aValue != null && Boolean.parseBoolean(aValue.toString()));
                break;
            case NAME_COL:
                rowData.setName(aValue.toString());
                break;
            case MAX_COMPETITOR_WEIGHT_COL:
                rowData.setMaxCompetitorWeight(aValue == null ? null : Double.parseDouble(aValue.toString()));
                break;
            case MAX_NUM_COMPETITORS_COL:
                rowData.setMaxNumberCompetitors(aValue == null ? null : Integer.parseInt(aValue.toString()));
                break;
            case EVENTS_COL:
                rowData.setEvents(aValue == null ? new ArrayList<>() : (List<StrengthEvent>) aValue);
                break;
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != EVENTS_COL || !_useSameEventsForAllWeightClasses;
    }

    void convertWeights(WeightUnitSystem oldUnitSystem, WeightUnitSystem newUnitSystem) {
        for(WeightClassRowData weightClassRowData : getRowData()) {
            Double currentWeight = weightClassRowData.getMaxCompetitorWeight();
            Double newWeight = WeightUnitSystem.convertUnits(currentWeight, oldUnitSystem, newUnitSystem);
            weightClassRowData.setMaxCompetitorWeight(newWeight);
        }
        fireTableDataChanged();
    }

    public boolean containsWeightClass(WeightClass wc) {
        boolean retVal = false;
        for(WeightClassRowData rowData : getRowData()) {
            if(rowData.getWeightClass().equals(wc)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }
}
