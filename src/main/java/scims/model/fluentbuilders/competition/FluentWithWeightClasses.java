package scims.model.fluentbuilders.competition;

import scims.model.WeightClass;

import java.util.List;

public interface FluentWithWeightClasses {
    FluentCompetitionBuilder withWeightClasses(List<WeightClass> weightClasses);
}
