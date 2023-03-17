package scims.model.fluentbuilders.competition;

import scims.model.data.WeightClass;

import java.util.List;

public interface FluentWithWeightClasses {
    FluentWithUnitSystem withWeightClasses(List<WeightClass> weightClasses);
}
