package scims.model.data.scoring;

import scims.model.data.Competitor;

import java.util.*;
import java.util.stream.Collectors;

public class LastManStandingWithPointsScoring implements EventScoring<Integer> {

    private Integer _score;
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
        Integer retVal = Integer.MIN_VALUE;
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
