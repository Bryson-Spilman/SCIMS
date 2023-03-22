package scims.model.data.scoring;

import java.time.Duration;

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
    public String toString() {
        return getScoreType() + " (seconds)";
    }

}

