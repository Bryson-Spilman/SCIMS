package scims.ui.swing;

import scims.model.data.scoring.EventScoring;

class EventsRowData {
    private String _name;
    private boolean _checked;
    private EventScoring<?> _eventScoring;

    public EventsRowData(boolean checked, String name, EventScoring<?> eventScoring) {
        _checked = checked;
        _name = name;
        _eventScoring = eventScoring;
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

    public void setEventScoring(EventScoring<?> eventScoring) {
        _eventScoring = eventScoring;
    }
}
