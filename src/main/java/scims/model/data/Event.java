package scims.model.data;

import scims.model.data.scoring.EventScoring;

public interface Event {
    String getName();
    EventScoring<?> getScoring();

}
