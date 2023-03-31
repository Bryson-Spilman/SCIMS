package scims.model.data.scoring;


import scims.model.data.Competitor;

import java.util.*;
import java.util.stream.Collectors;

public class LastManStandingEliminationScoring implements EventScoring<Boolean> {
    private Boolean _won;

    @Override
    public void setScore(Boolean won) {
        _won = won;
    }

    @Override
    public Boolean getScore() {
        return _won;
    }

    @Override
    public Comparator<Map.Entry<Competitor, Boolean>> getComparator() {
        return Map.Entry.<Competitor, Boolean>comparingByValue().reversed();
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
