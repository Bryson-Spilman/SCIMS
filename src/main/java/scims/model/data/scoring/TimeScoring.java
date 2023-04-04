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
        return (e1, e2) -> parseValueForComparing(e1.getValue()).compareTo(parseValueForComparing(e2.getValue()));
    }

    @Override
    public String toString() {
        return getScoreType() + " (seconds)";
    }

    @Override
    public Duration parseValueForComparing(Object value) {
        Duration retVal = Duration.ZERO;
        if(value instanceof Duration) {
            retVal = (Duration) value;
        }
        else if(value != null && !value.toString().trim().isEmpty()) {
            double seconds = Double.parseDouble(value.toString());
            long wholeSeconds = (long) seconds;
            int millis = (int) ((seconds - wholeSeconds) * 1000);
            retVal = Duration.ofSeconds(wholeSeconds).plus(Duration.ofMillis(millis));
        }
        return retVal;
    }

}

