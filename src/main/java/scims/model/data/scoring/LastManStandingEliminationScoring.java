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
        return (e1, e2) -> Boolean.compare(parseValueForComparing(e2.getValue()), parseValueForComparing(e1.getValue()));
    }

    @Override
    public Boolean parseValueForComparing(Object value) {
        Boolean retVal = false;
        if(value instanceof Boolean) {
            retVal = (Boolean) value;
        }
        else if(value != null && !value.toString().trim().isEmpty()) {
            retVal = Boolean.parseBoolean(value.toString());
        }
        return retVal;
    }

    @Override
    public String toString() {
        return getScoreType();
    }
}
