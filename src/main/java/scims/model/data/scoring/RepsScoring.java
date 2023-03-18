package scims.model.data.scoring;

public class RepsScoring implements EventScoring<Integer> {

    private Integer _score = 0;

    @Override
    public void setScore(Integer score) {
        _score = score;
    }

    @Override
    public String getDisplayScore() {
        return _score.toString();
    }

    @Override
    public Integer getScore() {
        return _score;
    }

    @Override
    public String getScoreType() {
        return "Reps";
    }
}
