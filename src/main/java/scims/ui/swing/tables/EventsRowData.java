package scims.ui.swing.tables;

import scims.model.data.scoring.EventScoring;

import java.time.Duration;

public class EventsRowData {
    private Duration _timeLimit;
    private String _name;
    private boolean _checked;
    private EventScoring<?> _eventScoring;
    private Integer _eventOrder;

    public EventsRowData(boolean checked, String name, EventScoring<?> eventScoring, Duration timeLimit, Integer eventOrder) {
        _checked = checked;
        _name = name;
        _eventScoring = eventScoring;
        _timeLimit = timeLimit;
        _eventOrder = eventOrder;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public boolean isChecked() {
        return _checked;
    }

    public void setChecked(boolean checked) {
        _checked = checked;
    }

    public EventScoring<?> getEventScoring() {
        return _eventScoring;
    }

    public Integer getEventOrder() {
        return _eventOrder;
    }

    public void setEventOrder(Integer order) {
        _eventOrder = order;
    }

    public Duration getTimeLimit() {
        return _timeLimit;
    }

    public void setTimeLimit(Duration timeLimit) {
        _timeLimit = timeLimit;
    }

    public void setEventScoring(EventScoring<?> scoring) {
        _eventScoring = scoring;
    }
}
