package scims.model.data.scoring;

public class LastManStandingWithPointsScoring implements EventScoring<Integer> {

    private Integer _score;
    @Override
    public void setScore(Integer score) {
        _score = score;
    }

    @Override
    public Integer getScore() {
        return _score;
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
