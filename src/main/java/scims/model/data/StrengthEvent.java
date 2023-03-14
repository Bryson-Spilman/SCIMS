package scims.model.data;

import scims.model.enums.EventScoreType;

public class StrengthEvent implements Event {
    private final String _name;
    private final EventScoreType _scoreType;

    StrengthEvent(String name, EventScoreType scoreType) {
        _name = name;
        _scoreType = scoreType;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public EventScoreType getScoreType() {
        return _scoreType;
    }
}
