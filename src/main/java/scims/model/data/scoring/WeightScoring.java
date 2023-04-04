package scims.model.data.scoring;

import scims.model.data.Competitor;
import scims.model.enums.WeightUnitSystem;

import java.util.*;

public class WeightScoring implements EventScoring<Double>{
    private Double _score = 0.0;
    private WeightUnitSystem _unitSystem = WeightUnitSystem.POUNDS;

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

    public void setUnitSystem(WeightUnitSystem unitSystem) {
        _unitSystem = unitSystem;
    }
    @Override
    public String toString() {
        return getScoreType()  + (_unitSystem == WeightUnitSystem.KILOS ? " (kgs)" : " (lbs)");
    }
}
