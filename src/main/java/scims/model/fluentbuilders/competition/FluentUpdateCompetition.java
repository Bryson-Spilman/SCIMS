package scims.model.fluentbuilders.competition;

import scims.model.WeightClass;

import java.time.ZonedDateTime;
import java.util.List;

public interface FluentUpdateCompetition extends FluentCompetitionBuilder {
    FluentFromExistingCompetition withUpdatedName(String name);
    FluentFromExistingCompetition withUpdatedDateTime(ZonedDateTime name);
    FluentFromExistingCompetition withIsSameNumberOfEventsForAllWeightClasses(boolean isSameNumberOfEventsForAllWeightClasses);
    FluentFromExistingCompetition withUpdatedWeightClasses(List<WeightClass> weightClasses);
}
