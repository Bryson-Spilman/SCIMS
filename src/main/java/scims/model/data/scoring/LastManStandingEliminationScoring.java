package scims.model.data.scoring;


public class LastManStandingEliminationScoring implements EventScoring<Boolean> {
    private Boolean _won;

    @Override
    public void setScore(Boolean won) {
        _won = won;
    }

    @Override
    public Boolean getScore() {
        return _won;
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
