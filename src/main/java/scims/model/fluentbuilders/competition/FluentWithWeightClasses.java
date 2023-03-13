package scims.model.fluentbuilders.competition;

import scims.model.WeightClassGroup;

import java.util.List;

public interface FluentWithWeightClasses {
    FluentCompetitionBuilder withWeightClasses(List<WeightClassGroup> weightClassGroups);
}
