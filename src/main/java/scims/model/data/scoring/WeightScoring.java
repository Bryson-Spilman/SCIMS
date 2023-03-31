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
        return (e1, e2) -> Double.compare(e2.getValue(), e1.getValue());
    }

    public void setUnitSystem(WeightUnitSystem unitSystem) {
        _unitSystem = unitSystem;
    }
    @Override
    public String toString() {
        return getScoreType()  + (_unitSystem == WeightUnitSystem.KILOS ? " (kgs)" : " (lbs)");
    }
}
