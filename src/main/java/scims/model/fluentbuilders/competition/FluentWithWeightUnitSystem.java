package scims.model.fluentbuilders.competition;

import scims.model.enums.WeightUnitSystem;

public interface FluentWithWeightUnitSystem {
    FluentWithDistanceUnitSystem withWeightUnitSystem(WeightUnitSystem unitSystem);
}
