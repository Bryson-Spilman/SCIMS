package scims.model.data.scoring;

import scims.model.enums.WeightUnitSystem;

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

    public void setUnitSystem(WeightUnitSystem unitSystem) {
        _unitSystem = unitSystem;
    }
    @Override
    public String toString() {
        return getScoreType()  + (_unitSystem == WeightUnitSystem.KILOS ? " (kgs)" : " (lbs)");
    }
}
