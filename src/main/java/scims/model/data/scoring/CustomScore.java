package scims.model.data.scoring;

public class CustomScore {

    private final EventScoring<?> _primaryScoring;
    private final EventScoring<?> _secondaryScoring;
    
    public CustomScore(EventScoring<?> primaryScoring, EventScoring<?> secondaryScoring) {
        _primaryScoring = primaryScoring;
        _secondaryScoring = secondaryScoring;
    }

    public EventScoring<?> getPrimaryScoring() {
        return _primaryScoring;
    }

    public EventScoring<?> getSecondaryScoring() {
        return _secondaryScoring;
    }
}
