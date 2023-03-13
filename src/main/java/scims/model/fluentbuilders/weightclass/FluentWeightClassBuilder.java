package scims.model.fluentbuilders.weightclass;

import scims.model.StrengthWeightClassGroup;

public interface FluentWeightClassBuilder {
    FluentWeightClassBuilder withMaxNumberOfCompetitors(int maxNumberOfCompetitors);
    StrengthWeightClassGroup build();
}
