package scims.model.data.scoring;

import javafx.application.Platform;
import scims.model.data.Competitor;
import scims.model.enums.DistanceUnitSystem;

import java.util.*;
import java.util.stream.Collectors;

public class DistanceScoring implements EventScoring<Double>{
    private Double _score = 0.0;
    private DistanceUnitSystem _unitSystem = DistanceUnitSystem.FEET;

    @Override
    public void setScore(Double score) {
        _score = score;
    }

    @Override
    public Double getScore() {
        return _score;
    }

    @Override
    public Comparator<Map.Entry<Competitor, Double>> getComparator() {
        return (e1, e2) -> Double.compare(parseValueForComparing(e2.getValue()), parseValueForComparing(e1.getValue()));
    }

    @Override
    public Double parseValueForComparing(Object value) {
        double retVal = Double.MIN_VALUE;
        if(value instanceof Double) {
            retVal = (Double) value;
        }
        else if(value != null && !value.toString().trim().isEmpty()) {
            retVal = Double.parseDouble(value.toString());
        }
        return retVal;
    }

    @Override
    public String toString() {
        return getScoreType() + (_unitSystem == DistanceUnitSystem.METERS ? " (m)" : " (ft)");
    }

    public void setUnitSystem(DistanceUnitSystem unitSystem) {
        _unitSystem = unitSystem;
    }
}
