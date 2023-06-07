package scims.model.data;

import scims.model.data.scoring.EventScoring;

public interface Competitor {
    Integer getAge();
    Double getWeight();
    String getName();
    void setEventScore(Event event, EventScoring<?> scoreCol, Object value);

}
