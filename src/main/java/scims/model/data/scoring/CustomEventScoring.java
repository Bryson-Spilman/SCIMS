package scims.model.data.scoring;

import scims.model.data.Competitor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CustomEventScoring<T,S> implements EventScoring<CustomScore<T,S>>{

    private CustomScore<T,S> _score;

    public CustomEventScoring<T,S> withPrimaryScoring(EventScoring<T> primaryScoring)
    {
        EventScoring<S> secondaryScoring = null;
        if(_score != null)
        {
            secondaryScoring = _score.getSecondaryScoring();
        }
        _score = new CustomScore<>(primaryScoring,secondaryScoring);
        return this;
    }

    public CustomEventScoring<T,S> withSecondaryScoring(EventScoring<S> secondaryScoring)
    {
        EventScoring<T> primaryScoring = null;
        if(_score != null)
        {
            primaryScoring = _score.getPrimaryScoring();
        }
        _score = new CustomScore<>(primaryScoring, secondaryScoring);
        return this;
    }

    @Override
    public void setScore(CustomScore<T,S> score) {
        _score = score;
    }

    @Override
    public CustomScore<T,S> getScore() {
        return _score;
    }

    @Override
    public Comparator<Map.Entry<Competitor, CustomScore<T, S>>> getComparator() {
        Comparator<Map.Entry<Competitor, T>> primaryComparator = _score.getPrimaryScoring().getComparator();
        Comparator<Map.Entry<Competitor, S>> secondaryComparator = _score.getSecondaryScoring().getComparator();
        return (e1, e2) -> {
            Map.Entry<Competitor, T> primaryEntry1 = buildPrimaryEntry(e1);
            Map.Entry<Competitor, T> primaryEntry2 = buildPrimaryEntry(e2);
            int primaryCompare = primaryComparator.compare(primaryEntry1,primaryEntry2);
            if (primaryCompare != 0) {
                return primaryCompare;
            } else {
                Map.Entry<Competitor, S> secondaryEntry1 = buildSecondaryEntry(e1);
                Map.Entry<Competitor, S> secondaryEntry2 = buildSecondaryEntry(e2);
                return secondaryComparator.compare(secondaryEntry1, secondaryEntry2);
            }
        };
    }

    private Map.Entry<Competitor, T> buildPrimaryEntry(Map.Entry<Competitor, CustomScore<T,S>> entry) {
        return new Map.Entry<Competitor, T>() {
            @Override
            public Competitor getKey() {
                return entry.getKey();
            }

            @Override
            public T getValue() {
                return entry.getValue().getPrimaryScoring().getScore();
            }

            @Override
            public T setValue(T value) {
                entry.getValue().getPrimaryScoring().setScore(value);
                return value;
            }
        };
    }

    private Map.Entry<Competitor, S> buildSecondaryEntry(Map.Entry<Competitor, CustomScore<T,S>> entry) {
        return new Map.Entry<Competitor, S>() {
            @Override
            public Competitor getKey() {
                return entry.getKey();
            }

            @Override
            public S getValue() {
                return entry.getValue().getSecondaryScoring().getScore();
            }

            @Override
            public S setValue(S value) {
                entry.getValue().getSecondaryScoring().setScore(value);
                return value;
            }
        };
    }

    @Override
    public CustomScore<T,S> parseValueForComparing(Object value) {
        CustomScore<T,S> retVal = new CustomScore<>(_score.getPrimaryScoring(), _score.getSecondaryScoring());
        if(value instanceof CustomScore) {
            retVal = (CustomScore<T,S>) value;
        }
        return retVal;
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
