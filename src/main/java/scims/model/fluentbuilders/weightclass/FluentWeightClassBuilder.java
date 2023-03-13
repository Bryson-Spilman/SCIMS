package scims.model.fluentbuilders.weightclass;

import scims.model.StrengthWeightClass;

public interface FluentWeightClassBuilder {
    FluentWeightClassBuilder withMaxNumberOfCompetitors(int maxNumberOfCompetitors);
    StrengthWeightClass build();
}
