package scims.model.data;

import scims.model.data.scoring.Scoring;

import java.time.Duration;

public interface Event {
    String getName();
    Scoring getScoring();
    Duration getTimeLimit();

}
