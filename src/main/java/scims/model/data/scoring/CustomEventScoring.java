package scims.model.data.scoring;

public class CustomEventScoring implements EventScoring<CustomScore>{

    private CustomScore _score;

    @Override
    public void setScore(CustomScore score) {
        _score = score;
    }

    @Override
    public CustomScore getScore() {
        return _score;
    }

    @Override
    public String getScoreType() {
        return "Custom";
    }
    @Override
    public String toString() {
        if(_score != null) {
            return  _score.getPrimaryScoring().toString() + " > " + _score.getSecondaryScoring().toString();
        }
        return getScoreType();
    }
}
