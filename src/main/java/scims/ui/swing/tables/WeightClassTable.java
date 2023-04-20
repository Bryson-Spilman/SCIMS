package scims.ui.swing.tables;

import scims.model.data.Event;
import scims.model.data.StrengthEvent;
import scims.model.data.StrengthWeightClass;
import scims.model.data.WeightClass;
import scims.model.enums.WeightUnitSystem;
import scims.ui.swing.JChooserField;

import javax.swing.*;
import javax.swing.JPopupMenu.Separator;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class WeightClassTable extends SCIMSTable {

    private final WeightClassTableModel _model;
    private JChooserField<StrengthEvent> _chooserField;
    private JPopupMenu _popUpMenu;
    private JMenuItem _selectAllMenuItem;
    private JMenuItem _selectHighlightedMenuItem;
    private JMenuItem _deselectHighlightedMenuItem;
    private JMenuItem _deselectAllMenuItem;
    public WeightClassTable() {
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
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                // Check if right-clicked
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Show the popup menu
                    _popUpMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        _selectHighlightedMenuItem.addActionListener(e -> setHighlightedSelected(true));
        _selectAllMenuItem.addActionListener(e -> setAllRowsSelected(true));
        _deselectHighlightedMenuItem.addActionListener(e -> setHighlightedSelected(false));
        _deselectAllMenuItem.addActionListener(e -> setAllRowsSelected(false));
    }

    private void setHighlightedSelected(boolean selected) {
        for(int row : getSelectedRows())
        {
            _model.setValueAt(selected, row, WeightClassTableModel.CHECK_BOX_COL);
        }
    }

    private void setAllRowsSelected(boolean selected) {
        for(int row =0; row < _model.getRowCount(); row++)
        {
            _model.setValueAt(selected, row, WeightClassTableModel.CHECK_BOX_COL);
        }
    }
    private void initTable() {
        _popUpMenu = new JPopupMenu();
        _selectHighlightedMenuItem = new JMenuItem("Select Highlighted");
        _selectAllMenuItem = new JMenuItem("Select All");
        _deselectHighlightedMenuItem = new JMenuItem("Deselect Highlighted");
        _deselectAllMenuItem = new JMenuItem("Deselect All");
        _popUpMenu.add(_selectHighlightedMenuItem);
        _popUpMenu.add(_selectAllMenuItem);
        _popUpMenu.add(new Separator());
        _popUpMenu.add(_deselectHighlightedMenuItem);
        _popUpMenu.add(_deselectAllMenuItem);
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

    public void setSelectedEvents(List<StrengthEvent> selectedEvents) {
        for(WeightClassRowData rowData : _model.getRowData()) {
            rowData.setEvents(selectedEvents);
        }
        _chooserField.setSelectedObjects(selectedEvents);
        _model.fireTableDataChanged();
    }

    public void setAvailableEvents(List<StrengthEvent> availableEvents) {
        _chooserField.setObjects(availableEvents);
    }

    public void setUseSameEventsForAllWeightClasses(boolean useSameForAll) {
        ((WeightClassTableModel)getModel()).setUseSameEventsForAllWeightClasses(useSameForAll);
        if(useSameForAll) {
            _chooserField.setSelectedObjects(_chooserField.getObjects());
        }
    }

    public List<StrengthWeightClass> getAllWeightClasses() {
        List<StrengthWeightClass> retVal = new ArrayList<>();
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

    public void convertWeights(WeightUnitSystem oldUnitSystem, WeightUnitSystem newUnitSystem) {
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

    public void addWeightClass(WeightClass wc) {
        _model.addRow(new WeightClassRowData(true, wc.getName(), wc.getMaxCompetitorWeight(), wc.getMaxNumberOfCompetitors(), wc.getEventsInOrder()));
    }

    public List<WeightClass> getSelectedWeightClasses() {
        List<WeightClass> retVal = new ArrayList<>();
        List<Integer> checkedRows = getCheckedRows();
        for(int row : checkedRows) {
            WeightClass wc = _model.getRowData().get(row).getWeightClass();
            retVal.add(wc);
        }
        return retVal;
    }

    public boolean containsWeightClass(WeightClass wc) {
        return _model.containsWeightClass(wc);
    }
}
