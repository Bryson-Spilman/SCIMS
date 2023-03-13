package scims.model.fluentbuilders.weightclass;

import scims.model.WeightClassGroup;

public interface FluentFromExistingWeightClass {
    FluentUpdateWeightClass fromExistingWeightClass(WeightClassGroup weightClassGroup);
}
