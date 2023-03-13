package scims.model.fluentbuilders.competition;

import scims.model.WeightClassGroup;

import java.time.ZonedDateTime;
import java.util.List;

public interface FluentUpdateCompetition extends FluentCompetitionBuilder {
    FluentUpdateCompetition withUpdatedName(String name);
    FluentUpdateCompetition withUpdatedDateTime(ZonedDateTime name);
    FluentUpdateCompetition withIsSameNumberOfEventsForAllWeightClasses(boolean isSameNumberOfEventsForAllWeightClasses);
    FluentUpdateCompetition withUpdatedWeightClasses(List<WeightClassGroup> weightClassGroups);
}
