package scims.model.data.scoring;

public class WeightScoring implements EventScoring<Double>{
    private Double _score = 0.0;

    @Override
    public void setScore(Double score) {
        _score = score;
    }

    @Override
    public String getDisplayScore() {
        return _score.toString();
    }

    @Override
    public Double getScore() {
        return _score;
    }

    @Override
    public String getScoreType() {
        return "Weight";
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
