package scims.ui.swing;

import scims.model.data.Event;
import scims.model.data.StrengthEventBuilder;
import scims.model.data.scoring.EventScoring;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class EventsTable extends SCIMSTable {

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
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        _eventOrderComboBox.setModel(model);
        getColumnModel().getColumn(EventsTableModel.CHECK_BOX_COL).setMaxWidth(30);
    }

    @Override
    Color getHighlightColor() {
        return new Color(205,223,229);
    }

    @Override
    public boolean isRowHighlighted(int row) {
        List<Integer> checkedRows = _model.getCheckedRows();
        return checkedRows.contains(row);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class<?> retVal = super.getColumnClass(column);
        switch(column) {
            case EventsTableModel.CHECK_BOX_COL:
                retVal = Boolean.class;
                break;
            case EventsTableModel.NAME_COL:
            case EventsTableModel.SCORE_TYPE_COL:
                retVal = String.class;
                break;
            case EventsTableModel.EVENT_ORDER_COL:
                retVal = Integer.class;
                break;
        }
        return retVal;
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

    public List<Event> getSelectedEvents() {
        List<Event> retVal = new ArrayList<>();
        List<Integer> checkedRows = _model.getCheckedRows();
        for(int checkedRow : checkedRows) {
            String name = (String) getValueAt(checkedRow, EventsTableModel.NAME_COL);
            EventScoring<?> scoreType = (EventScoring<?>) getValueAt(checkedRow, EventsTableModel.SCORE_TYPE_COL);
            retVal.add(new StrengthEventBuilder().withName(name).withScoring(scoreType).build());
        }
        return retVal;
    }

    public void addNewEventSelectedAction(Runnable updatedEvents) {
        _actionOnEventsSelectionUpdate.add(updatedEvents);
    }
}
