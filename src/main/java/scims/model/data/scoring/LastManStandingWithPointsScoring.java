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
        return (e1, e2) -> Integer.compare(e2.getValue(), e1.getValue());
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
