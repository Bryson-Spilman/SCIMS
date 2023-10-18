package scims.model.data.scoring;

import java.util.Objects;

public class CustomScore<T,S, L> {

    private final EventScoring<T> _primaryScoring;
    private final EventScoring<S> _secondaryScoring;

    private final EventScoring<L> _thirdScoring;
    
    public CustomScore(EventScoring<T> primaryScoring, EventScoring<S> secondaryScoring, EventScoring<L> thirdScoring) {
        _primaryScoring = primaryScoring;
        _secondaryScoring = secondaryScoring;
        _thirdScoring = thirdScoring;
    }

    public void setPrimaryScore(T primaryScore) {
        if(primaryScore != null && primaryScore.toString().isEmpty())
        {
            primaryScore = null;
        }
        _primaryScoring.setScore(primaryScore);
    }

    public void setSecondaryScore(S secondaryScore) {
        if(_secondaryScoring != null)
        {
            if(secondaryScore != null && secondaryScore.toString().isEmpty())
            {
                secondaryScore = null;
            }
            _secondaryScoring.setScore(secondaryScore);
        }
    }

    public void setThirdScoring(L thirdScoring) {
        if(_thirdScoring != null)
        {
            if(thirdScoring != null && thirdScoring.toString().isEmpty())
            {
                thirdScoring = null;
            }
            _thirdScoring.setScore(thirdScoring);
        }
    }

    public EventScoring<T> getPrimaryScoring() {
        return _primaryScoring;
    }

    public EventScoring<S> getSecondaryScoring() {
        return _secondaryScoring;
    }

    public EventScoring<L> getThirdScoring() {
        return _thirdScoring;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomScore<?, ?, ?> that = (CustomScore<?, ?, ?>) o;
        return Objects.equals(_primaryScoring.getScore(), that._primaryScoring.getScore())
                && Objects.equals(_secondaryScoring == null ? null : _secondaryScoring.getScore(),
                                that._secondaryScoring == null ? null :that._secondaryScoring.getScore())
                && Objects.equals(_thirdScoring == null ? null : _thirdScoring.getScore(),
                                that._thirdScoring == null ? null :that._thirdScoring.getScore());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_primaryScoring.getScore(),
                _secondaryScoring == null ? null : _secondaryScoring.getScore(),
                _thirdScoring == null ? null : _thirdScoring.getScore());
    }
}
