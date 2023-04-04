package scims.model.data.scoring;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomScore<?, ?> that = (CustomScore<?, ?>) o;
        return Objects.equals(_primaryScoring.getScore(), that._primaryScoring.getScore()) && Objects.equals(_secondaryScoring.getScore(), that._secondaryScoring.getScore());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_primaryScoring.getScore(), _secondaryScoring.getScore());
    }
}
