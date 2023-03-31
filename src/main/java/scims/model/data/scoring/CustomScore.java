package scims.model.data.scoring;

public class CustomScore<T,S> {

    private final EventScoring<T> _primaryScoring;
    private final EventScoring<S> _secondaryScoring;
    
    public CustomScore(EventScoring<T> primaryScoring, EventScoring<S> secondaryScoring) {
        _primaryScoring = primaryScoring;
        _secondaryScoring = secondaryScoring;
    }

    public void setPrimaryScore(T primaryScore) {
        _primaryScoring.setScore(primaryScore);
    }

    public void setSecondaryScore(S secondaryScore) {
        _secondaryScoring.setScore(secondaryScore);
    }

    public EventScoring<T> getPrimaryScoring() {
        return _primaryScoring;
    }

    public EventScoring<S> getSecondaryScoring() {
        return _secondaryScoring;
    }
}
