package scims.model.data.scoring;

import scims.model.data.Competitor;

import java.util.*;

public class RepsScoring implements EventScoring<Integer> {

    private Integer _score = 0;

    @Override
    public void setScore(Integer score) {
        _score = score;
    }

    @Override
    public Integer getScore() {
        return _score;
    }

    @Override
    public Comparator<Map.Entry<Competitor, Integer>> getComparator() {
        return (e1, e2) -> Integer.compare(parseValueForComparing(e2.getValue()), parseValueForComparing(e1.getValue()));
    }

    @Override
    public Integer parseValueForComparing(Object value) {
        int retVal = Integer.MIN_VALUE;
        if(value instanceof Integer) {
            retVal = (Integer) value;
        }
        else if(value != null && !value.toString().trim().isEmpty()) {
            retVal = Integer.parseInt(value.toString());
        }
        return retVal;
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
