package scims.ui.swing;

import scims.model.data.Event;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class WeightClassTable extends SCIMSTable {

    private JChooserField<Event> _chooserField;

    WeightClassTable() {
        setModel(new WeightClassTableModel());
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
        return Color.YELLOW;
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
}
