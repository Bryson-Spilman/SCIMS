package scims.model;

import scims.model.fluentbuilders.competition.*;

import java.time.ZonedDateTime;
import java.util.List;

class StrengthCompetitionBuilder implements FluentWithCompetitionName, FluentFromExistingCompetition {
    @Override
    public FluentUpdateCompetition fromExistingCompetition(Competition competition) {
        return null;
    }

    @Override
    public FluentWithCompetitionDateTime withName(String name) {
        return null;
    }

    private class WithCompetitionDateTime implements FluentWithCompetitionDateTime {

        @Override
        public FluentWithIsSameNumberOfEventsForAllWeightClasses withDateTime(ZonedDateTime dateTime) {
            return null;
        }
    }

    private class WithIsSameNumberOfEventsForAllWeightClasses implements FluentWithIsSameNumberOfEventsForAllWeightClasses {

        @Override
        public FluentWithWeightClasses withIsSameNumberOfEventsForAllWeightClasses(boolean isSameNumberOfEventsForAllWeightClasses) {
            return null;
        }
    }

    private class WithWeightClasses implements FluentWithWeightClasses {

        @Override
        public FluentCompetitionBuilder withWeightClasses(List<WeightClass> weightClasses) {
            return null;
        }
    }

    private class CompetitionBuilder implements FluentCompetitionBuilder {

        @Override
        public StrengthCompetition build() {
            return null;
        }
    }

    private class UpdateCompetition extends CompetitionBuilder implements FluentUpdateCompetition {

        @Override
        public FluentFromExistingCompetition withUpdatedName(String name) {
            return null;
        }

        @Override
        public FluentFromExistingCompetition withUpdatedDateTime(ZonedDateTime name) {
            return null;
        }

        @Override
        public FluentFromExistingCompetition withIsSameNumberOfEventsForAllWeightClasses(boolean isSameNumberOfEventsForAllWeightClasses) {
            return null;
        }

        @Override
        public FluentFromExistingCompetition withUpdatedWeightClasses(List<WeightClass> weightClasses) {
            return null;
        }
    }
}
