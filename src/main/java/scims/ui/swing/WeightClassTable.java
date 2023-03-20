package scims.ui.swing;

import scims.model.data.Event;
import scims.model.data.WeightClass;
import scims.model.enums.UnitSystem;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class WeightClassTable extends SCIMSTable {

    private final WeightClassTableModel _model;
    private JChooserField<Event> _chooserField;

    WeightClassTable() {
        _model = new WeightClassTableModel();
        setModel(_model);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        initTable();
        addListeners();
    }

    private void addListeners() {
        WeightClassTableModel tableModel = (WeightClassTableModel) getModel();
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == WeightClassTableModel.CHECK_BOX_COL || e.getColumn() == WeightClassTableModel.EVENTS_COL){
                tableModel.fireTableChanged(new TableModelEvent(tableModel));
            }
        });
    }

    private void initTable() {
        setCheckBoxColumn(WeightClassTableModel.CHECK_BOX_COL);
        setIntegerFieldColumn(WeightClassTableModel.MAX_NUM_COMPETITORS_COL);
        _chooserField = setChooserFieldColumn(WeightClassTableModel.EVENTS_COL);
        setDoubleFieldColumn(WeightClassTableModel.MAX_COMPETITOR_WEIGHT_COL);
        getColumnModel().getColumn(WeightClassTableModel.CHECK_BOX_COL).setMaxWidth(30);
    }

    private List<Integer> getCheckedRows() {
        List<Integer> retVal = new ArrayList<>();
        for(int row=0; row < getModel().getRowCount(); row++) {
            Boolean checked = (Boolean) getValueAt(row, WeightClassTableModel.CHECK_BOX_COL);
            if(checked) {
                retVal.add(row);
            }
        }
        return retVal;
    }

    @Override
    Color getHighlightColor() {
        return new Color(205,223,229);
    }

    @Override
    boolean isRowHighlighted(int row) {
        List<Integer> checkedRows = getCheckedRows();
        return checkedRows.contains(row);
    }

    public void setEvents(List<Event> selectedEvents) {
        _chooserField.setObjects(selectedEvents);
    }

    public void setUseSameEventsForAllWeightClasses(boolean useSameForAll) {
        ((WeightClassTableModel)getModel()).setUseSameEventsForAllWeightClasses(useSameForAll);
        if(useSameForAll) {
            _chooserField.setSelectedObjects(_chooserField.getObjects());
        }
    }

    public List<WeightClass> getAllWeightClasses() {
        List<WeightClass> retVal = new ArrayList<>();
        for(WeightClassRowData weightClassData : _model.getRowData()) {
            retVal.add(weightClassData.getWeightClass());
        }
        return retVal;
    }

    public void clear() {
        _model.clear();
    }

    public void setWeightClasses(List<WeightClass> weightClasses) {
        for(WeightClass wc : weightClasses) {
            WeightClassRowData rowData = new WeightClassRowData(false, wc.getName(), wc.getMaxCompetitorWeight(), wc.getMaxNumberOfCompetitors(), wc.getEventsInOrder());
            _model.addRow(rowData);
        }
        _model.fireTableDataChanged();
    }

    public void convertWeights(UnitSystem oldUnitSystem, UnitSystem newUnitSystem) {
        _model.convertWeights(oldUnitSystem, newUnitSystem);
    }

    public boolean hasMaxWeights() {
        boolean retVal = false;
        for(int row =0; row < _model.getRowCount(); row++) {
            Object value = getValueAt(row, WeightClassTableModel.MAX_COMPETITOR_WEIGHT_COL);
            if(value != null && ((Double)value) > Double.MIN_VALUE) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }
}
