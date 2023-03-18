package scims.model.data;

import scims.model.data.scoring.EventScoring;

public class StrengthEvent implements Event {
    private final String _name;
    private final EventScoring<?> _scoring;

    StrengthEvent(String name, EventScoring<?> scoring) {
        _name = name;
        _scoring = scoring;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public EventScoring<?> getScoring() {
        return _scoring;
    }

    @Override
    public String toString() {
        return getName();
    }
}
