package scims.model.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import scims.model.data.scoring.Scoring;

import java.time.Duration;

public class StrengthEvent implements Event {
    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private final String _name;
    @JacksonXmlProperty(localName = "scoring")
    private final Scoring _scoring;
    @JacksonXmlProperty(localName = "timeLimit")
    private final Duration _timeLimit;

    public StrengthEvent(String name, Scoring scoring, Duration timeLimit) {
        _name = name;
        _scoring = scoring;
        _timeLimit = timeLimit;
    }

    public StrengthEvent()
    {
        this(null, null, null);
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Scoring getScoring() {
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
