package scims.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import scims.model.data.scoring.Scoring;

import java.time.Duration;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonIgnore
    public String toString() {
        return getName();
    }

    @Override
    @JsonIgnore
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrengthEvent that = (StrengthEvent) o;
        return Objects.equals(_name, that._name) && Objects.equals(_scoring, that._scoring) && Objects.equals(_timeLimit, that._timeLimit);
    }

    @Override
    @JsonIgnore
    public int hashCode() {
        return Objects.hash(_name, _scoring, _timeLimit);
    }
}
