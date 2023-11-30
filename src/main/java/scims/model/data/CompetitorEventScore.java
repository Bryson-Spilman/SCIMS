package scims.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import scims.model.data.scoring.EventScoring;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CompetitorEventScore {

    @JacksonXmlProperty(isAttribute = true, localName = "event")
    private final String _eventName;
    @JacksonXmlProperty(isAttribute = true, localName = "score-type")
    private final String _scoreType;
    @JacksonXmlProperty(isAttribute = true, localName = "score")
    private Object _score;

    public CompetitorEventScore(Event event, EventScoring<?> scoring, Object score)
    {
        _eventName = event.getName();
        _scoreType = scoring.getScoreType();
        _score = score;
    }

    @JsonIgnore
    public String getEventName() {
        return _eventName;
    }

    @JsonIgnore
    public String getScoreType() {
        return _scoreType;
    }

    @JsonIgnore
    public Object getScore() {
        return _score;
    }

    @JsonIgnore
    public void setScore(Object score) {
        _score = score;
    }
}
