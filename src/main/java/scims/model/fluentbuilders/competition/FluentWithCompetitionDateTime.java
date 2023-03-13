package scims.model.fluentbuilders.competition;

import java.time.ZonedDateTime;

public interface FluentWithCompetitionDateTime {
    FluentWithIsSameNumberOfEventsForAllWeightClasses withDateTime(ZonedDateTime dateTime);
}
