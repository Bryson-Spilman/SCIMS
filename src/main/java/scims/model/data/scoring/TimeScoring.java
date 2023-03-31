package scims.model.data.scoring;

import scims.model.data.Competitor;

import java.time.Duration;
import java.util.*;

public class TimeScoring implements EventScoring<Duration>{
    private Duration _score = Duration.ZERO;

    @Override
    public void setScore(Duration score) {
        _score = score;
    }

    @Override
    public Duration getScore() {
        return _score;
    }

    @Override
    public Comparator<Map.Entry<Competitor, Duration>> getComparator() {
        return Map.Entry.comparingByValue();
    }

    @Override
    public String toString() {
        return getScoreType() + " (seconds)";
    }

}

