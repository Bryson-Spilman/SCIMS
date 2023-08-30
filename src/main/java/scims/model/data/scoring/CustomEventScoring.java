package scims.model.data.scoring;

import scims.model.data.Competitor;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class CustomEventScoring<T,S,L> implements EventScoring<CustomScore<T,S,L>>{

    private CustomScore<T,S, L> _score;

    public CustomEventScoring<T,S,L> withPrimaryScoring(EventScoring<T> primaryScoring)
    {
        EventScoring<S> secondaryScoring = null;
        EventScoring<L> thirdScoring = null;
        if(_score != null)
        {
            secondaryScoring = _score.getSecondaryScoring();
            thirdScoring = _score.getThirdScoring();
        }
        _score = new CustomScore<>(primaryScoring, secondaryScoring, thirdScoring);
        return this;
    }

    public CustomEventScoring<T,S,L> withSecondaryScoring(EventScoring<S> secondaryScoring)
    {
        EventScoring<T> primaryScoring = null;
        EventScoring<L> thirdScoring = null;
        if(_score != null)
        {
            primaryScoring = _score.getPrimaryScoring();
            thirdScoring = _score.getThirdScoring();
        }
        _score = new CustomScore<>(primaryScoring, secondaryScoring, thirdScoring);
        return this;
    }

    public CustomEventScoring<T,S,L> withThirdScoring(EventScoring<L> thirdScoring)
    {
        EventScoring<T> primaryScoring = null;
        EventScoring<S> secondaryScoring = null;
        if(_score != null)
        {
            primaryScoring = _score.getPrimaryScoring();
            secondaryScoring = _score.getSecondaryScoring();
        }
        _score = new CustomScore<>(primaryScoring, secondaryScoring, thirdScoring);
        return this;
    }

    @Override
    public void setScore(CustomScore<T,S,L> score) {
        _score = score;
    }

    @Override
    public CustomScore<T,S,L> getScore() {
        return _score;
    }

    @Override
    public Comparator<Map.Entry<Competitor, CustomScore<T,S,L>>> getComparator() {
        Comparator<Map.Entry<Competitor, T>> primaryComparator = _score.getPrimaryScoring().getComparator();
        Comparator<Map.Entry<Competitor, S>> secondaryComparator = _score.getSecondaryScoring().getComparator();
        Comparator<Map.Entry<Competitor, L>> thirdComparator = _score.getThirdScoring().getComparator();

        return (e1, e2) -> {
            Map.Entry<Competitor, T> primaryEntry1 = buildPrimaryEntry(e1);
            Map.Entry<Competitor, T> primaryEntry2 = buildPrimaryEntry(e2);
            int primaryCompare = primaryComparator.compare(primaryEntry1, primaryEntry2);

            if (primaryCompare != 0) {
                return primaryCompare;
            } else {
                Map.Entry<Competitor, S> secondaryEntry1 = buildSecondaryEntry(e1);
                Map.Entry<Competitor, S> secondaryEntry2 = buildSecondaryEntry(e2);
                int secondaryCompare = secondaryComparator.compare(secondaryEntry1, secondaryEntry2);

                if (secondaryCompare != 0) {
                    return secondaryCompare;
                } else {
                    Map.Entry<Competitor, L> thirdEntry1 = buildThirdEntry(e1);
                    Map.Entry<Competitor, L> thirdEntry2 = buildThirdEntry(e2);
                    return thirdComparator.compare(thirdEntry1, thirdEntry2);
                }
            }
        };
    }

    private Map.Entry<Competitor, T> buildPrimaryEntry(Map.Entry<Competitor, CustomScore<T,S,L>> entry) {
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

    private Map.Entry<Competitor, S> buildSecondaryEntry(Map.Entry<Competitor, CustomScore<T,S,L>> entry) {
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

    private Map.Entry<Competitor, L> buildThirdEntry(Map.Entry<Competitor, CustomScore<T,S,L>> entry) {
        return new Map.Entry<Competitor, L>() {
            @Override
            public Competitor getKey() {
                return entry.getKey();
            }

            @Override
            public L getValue() {
                return entry.getValue().getThirdScoring().getScore();
            }

            @Override
            public L setValue(L value) {
                entry.getValue().getThirdScoring().setScore(value);
                return value;
            }
        };
    }

    @Override
    public CustomScore<T,S,L> parseValueForComparing(Object value) {
        CustomScore<T,S,L> retVal = new CustomScore<>(_score.getPrimaryScoring(), _score.getSecondaryScoring(), _score.getThirdScoring());
        if(value instanceof CustomScore) {
            retVal = (CustomScore<T,S,L>) value;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this.toString(), o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_score);
    }
}
