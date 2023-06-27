package scims.ui.swing.tables;

import scims.model.data.Event;
import scims.model.data.StrengthEvent;
import scims.model.data.StrengthEventBuilder;
import scims.model.data.scoring.EventScoring;
import scims.model.enums.DistanceUnitSystem;
import scims.model.enums.WeightUnitSystem;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EventsTable extends SCIMSTable {

    private final EventsTableModel _model;
    private JComboBox<Object> _eventOrderComboBox;
    private final List<Runnable> _actionOnEventsSelectionUpdate = new ArrayList<>();

    public EventsTable() {
        _model = new EventsTableModel();
        setModel(_model);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        initTable();
        addListeners();
    }

    private void addListeners() {
        EventsTableModel tableModel = (EventsTableModel) getModel();
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == EventsTableModel.CHECK_BOX_COL) {
                tableModel.fireTableChanged(new TableModelEvent(tableModel));
                DefaultComboBoxModel<Object> model = (DefaultComboBoxModel<Object>) _eventOrderComboBox.getModel();
                model.removeAllElements();
                List<Integer> checkedRows = _model.getCheckedRows();
                for(int i=1; i <= checkedRows.size(); i++) {
                    model.addElement(i);
                }
                Object checked = getModel().getValueAt(e.getFirstRow(), EventsTableModel.CHECK_BOX_COL);
                boolean isChecked = checked != null && Boolean.parseBoolean(checked.toString());
                if(!isChecked) {
                    Object currentOrder = getModel().getValueAt(e.getFirstRow(), EventsTableModel.EVENT_ORDER_COL);
                    getModel().setValueAt(null, e.getFirstRow(), EventsTableModel.EVENT_ORDER_COL);
                    if(currentOrder != null && Integer.parseInt(currentOrder.toString()) < _model.getCheckedRows().size() +1) {
                        decreaseOrders(Integer.parseInt(currentOrder.toString()));
                    }
                }
                for(Runnable action : _actionOnEventsSelectionUpdate) {
                    action.run();
                }
            }
            if(e.getColumn() == EventsTableModel.EVENT_ORDER_COL) {
                tableModel.fireTableChanged(new TableModelEvent(tableModel));
                DefaultComboBoxModel<Object> model = (DefaultComboBoxModel<Object>) _eventOrderComboBox.getModel();
                model.removeAllElements();
                List<Integer> checkedRows = _model.getCheckedRows();
                for(int i=1; i <= checkedRows.size(); i++) {
                    model.addElement(i);
                }
            }
        });
    }
    private void decreaseOrders(int orderRemoved) {
        List<Integer> checkedRows = _model.getCheckedRows();
        for(Integer row : checkedRows) {
            Object order = getValueAt(row, EventsTableModel.EVENT_ORDER_COL);
            if(order != null) {
                int orderInt = Integer.parseInt(order.toString());
                if(orderInt > orderRemoved) {
                    setValueAt(orderInt -1, row, EventsTableModel.EVENT_ORDER_COL);
                }
            }
        }
    }

    private void initTable() {
        _eventOrderComboBox = setComboBoxColumn(EventsTableModel.EVENT_ORDER_COL);
        setCheckBoxColumn(EventsTableModel.CHECK_BOX_COL);
        setDoubleFieldColumn(EventsTableModel.TIME_LIMIT_COL);
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        _eventOrderComboBox.setModel(model);
        getColumnModel().getColumn(EventsTableModel.CHECK_BOX_COL).setMaxWidth(30);
    }

    @Override
    Color getHighlightColor() {
        return Coloring.SELECTED_COLOR;
    }

    @Override
    public boolean isRowHighlighted(int row) {
        List<Integer> checkedRows = _model.getCheckedRows();
        return checkedRows.contains(row);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return _model.getColumnClass(column);
    }

    public boolean hasOrdersSet() {
        boolean retVal = false;
        List<Integer> checkedRows = _model.getCheckedRows();
        for(Integer row : checkedRows) {
            Object order = getValueAt(row, EventsTableModel.EVENT_ORDER_COL);
            if(order != null) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    public void resetEventOrders() {
        List<Integer> checkedRows = _model.getCheckedRows();
        for(Integer row : checkedRows) {
            setValueAt(null, row, EventsTableModel.EVENT_ORDER_COL);
        }
        ((EventsTableModel)getModel()).setEventOrderColumnEnabled(false);
    }

    public List<StrengthEvent> getSelectedEvents() {
        List<Integer> checkedRows = _model.getCheckedRows();
        Map<Integer, StrengthEvent> sortedMap = new TreeMap<>();
        for(int checkedRow : checkedRows) {
            String name = (String) getValueAt(checkedRow, EventsTableModel.NAME_COL);
            EventScoring<?> scoreType = (EventScoring<?>) getValueAt(checkedRow, EventsTableModel.SCORE_TYPE_COL);
            Duration timeLimit = _model.getRowData().get(checkedRow).getTimeLimit();
            Object orderObj = _model.getValueAt(checkedRow, EventsTableModel.EVENT_ORDER_COL);
            Integer order = Integer.MAX_VALUE - checkedRows.size();
            if(orderObj != null) {
                order = Integer.parseInt(orderObj.toString());
            }
            while(sortedMap.containsKey(order)) {
                order++;
            }
            sortedMap.put(order, new StrengthEventBuilder().withName(name).withScoring(scoreType).withTimeLimit(timeLimit).build());
        }

        return new ArrayList<>(sortedMap.values());
    }

    public void addNewEventSelectedAction(Runnable updatedEvents) {
        _actionOnEventsSelectionUpdate.add(updatedEvents);
    }

    public void setEventOrderColumnEnabled(boolean enabled) {
        _model.setEventOrderColumnEnabled(enabled);
    }

    public void addEvent(Event event) {
        _model.addRow(new EventsRowData(true, event.getName(), (EventScoring<?>) event.getScoring(), event.getTimeLimit(), null));
    }

    public void applyDistanceUnitsChange(DistanceUnitSystem selected) {
        _model.setDistanceUnits(selected);
    }

    public void applyWeightUnitsChange(WeightUnitSystem selected) {
        _model.setWeightUnits(selected);
    }

    public void addOrderChangedAction(Runnable action) {
        _eventOrderComboBox.addActionListener(e -> action.run());
    }

    public void setSelectedEvents(List<StrengthEvent> events) {
        _model.setSelectedEvents(events);
    }

    public void setOrdersByListOrder(List<StrengthEvent> events) {
        _model.setOrdersByListOrder(events);
    }

    public void deselectAll() {
        _model.deselectAll();
    }

    public void setEvents(List<StrengthEvent> events) {
        _model.setEvents(events);
    }
}
