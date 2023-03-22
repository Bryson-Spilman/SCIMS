package scims.model.data;

import scims.model.data.scoring.EventScoring;

import java.time.Duration;

public class StrengthEvent implements Event {
    private final String _name;
    private final EventScoring<?> _scoring;
    private final Duration _timeLimit;

    StrengthEvent(String name, EventScoring<?> scoring, Duration timeLimit) {
        _name = name;
        _scoring = scoring;
        _timeLimit = timeLimit;
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
    public Duration getTimeLimit() {
        return _timeLimit;
    }

    @Override
    public String toString() {
        return getName();
    }
}
