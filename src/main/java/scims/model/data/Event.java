package scims.model.data;

import scims.model.data.scoring.EventScoring;

import java.time.Duration;

public interface Event {
    String getName();
    EventScoring<?> getScoring();
    Duration getTimeLimit();

}
