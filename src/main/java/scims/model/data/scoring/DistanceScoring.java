package scims.model.data.scoring;

import scims.model.enums.DistanceUnitSystem;

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
    public String toString() {
        return getScoreType() + (_unitSystem == DistanceUnitSystem.METERS ? " (m)" : " (ft)");
    }

    public void setUnitSystem(DistanceUnitSystem unitSystem) {
        _unitSystem = unitSystem;
    }
}
