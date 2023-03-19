package scims.model.data.scoring;

import java.time.Duration;

public class TimedScoring implements EventScoring<Duration>{
    private Duration _score = Duration.ZERO;

    @Override
    public void setScore(Duration score) {
        _score = score;
    }

    @Override
    public String getDisplayScore() {
        if(_score == null)
        {
            return "0";
        }
        double seconds = (double) _score.toMillis() / 1000;
        seconds = Math.round(seconds * 100.0) / 100.0; // Round to 2 decimal places
        return String.format("%.2f", seconds) + " s";
    }

    @Override
    public Duration getScore() {
        return _score;
    }

    @Override
    public String getScoreType() {
        return "Time (seconds)";
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}

