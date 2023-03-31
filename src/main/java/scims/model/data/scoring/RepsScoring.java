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
        return (e1, e2) -> Integer.compare(e2.getValue(), e1.getValue());
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
