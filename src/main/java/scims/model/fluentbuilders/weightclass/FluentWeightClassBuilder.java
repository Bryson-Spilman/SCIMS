package scims.model.fluentbuilders.weightclass;

import scims.model.data.StrengthWeightClass;

public interface FluentWeightClassBuilder {
    FluentWeightClassBuilder withMaxNumberOfCompetitors(int maxNumberOfCompetitors);
    StrengthWeightClass build();
}
