package scims.model.data;

import scims.model.enums.DistanceUnitSystem;
import scims.model.enums.WeightUnitSystem;
import scims.model.fluentbuilders.competition.*;

import java.time.ZonedDateTime;
import java.util.List;

public class StrengthCompetitionBuilder implements FluentWithCompetitionName, FluentFromExistingCompetition {
    private String _name;
    private ZonedDateTime _dateTime;
    private WeightUnitSystem _weightunitSystem;
    private List<WeightClass> _weightClasses;
    private boolean _isSameNumberOfEventsForAllWeightClasses;
    private DistanceUnitSystem _distanceUnitSystem;

    @Override
    public FluentUpdateCompetition fromExistingCompetition(Competition competition) {
        _name = competition.getName();
        _dateTime = competition.getDateTime();
        _weightunitSystem = competition.getWeightUnitSystem();
        _weightClasses = competition.getWeightClasses();
        _distanceUnitSystem = competition.getDistanceUnitSystem();
        _isSameNumberOfEventsForAllWeightClasses = competition.isSameNumberOfEventsForAllWeightClasses();
        return new UpdateCompetition();
    }

    @Override
    public FluentWithCompetitionDateTime withName(String name) {
        _name = name;
        return new WithCompetitionDateTime();
    }

    private class WithCompetitionDateTime implements FluentWithCompetitionDateTime {

        @Override
        public FluentWithIsSameNumberOfEventsForAllWeightClasses withDateTime(ZonedDateTime dateTime) {
            _dateTime = dateTime;
            return new WithIsSameNumberOfEventsForAllWeightClasses();
        }
    }

    private class WithIsSameNumberOfEventsForAllWeightClasses implements FluentWithIsSameNumberOfEventsForAllWeightClasses {

        @Override
        public FluentWithWeightClasses withIsSameNumberOfEventsForAllWeightClasses(boolean isSameNumberOfEventsForAllWeightClasses) {
            _isSameNumberOfEventsForAllWeightClasses = isSameNumberOfEventsForAllWeightClasses;
            return new WithWeightClasses();
        }
    }

    private class WithWeightClasses implements FluentWithWeightClasses {

        @Override
        public FluentWithWeightUnitSystem withWeightClasses(List<WeightClass> weightClasses) {
            _weightClasses = weightClasses;
            return new WithWeightUnitSystem();
        }
    }

    private class WithWeightUnitSystem implements FluentWithWeightUnitSystem {

        @Override
        public FluentWithDistanceUnitSystem withWeightUnitSystem(WeightUnitSystem unitSystem) {
            _weightunitSystem = unitSystem;
            return new WithDistanceUnitSystem();
        }
    }

    private class WithDistanceUnitSystem implements FluentWithDistanceUnitSystem {

        @Override
        public FluentCompetitionBuilder withDistanceUnitSystem(DistanceUnitSystem distanceUnitSystem) {
            _distanceUnitSystem = distanceUnitSystem;
            return new CompetitionBuilder();
        }
    }

    private class CompetitionBuilder implements FluentCompetitionBuilder {

        @Override
        public StrengthCompetition build() {
            return new StrengthCompetition(_name, _dateTime, _weightClasses, _weightunitSystem, _distanceUnitSystem, _isSameNumberOfEventsForAllWeightClasses);
        }
    }

    private class UpdateCompetition extends CompetitionBuilder implements FluentUpdateCompetition {

        @Override
        public FluentUpdateCompetition withUpdatedName(String name) {
            _name = name;
            return this;
        }

        @Override
        public FluentUpdateCompetition withUpdatedDateTime(ZonedDateTime dateTime) {
            _dateTime = dateTime;
            return this;
        }

        @Override
        public FluentUpdateCompetition withIsSameNumberOfEventsForAllWeightClasses(boolean isSameNumberOfEventsForAllWeightClasses) {
            _isSameNumberOfEventsForAllWeightClasses = isSameNumberOfEventsForAllWeightClasses;
            return this;
        }

        @Override
        public FluentUpdateCompetition withUpdatedWeightClasses(List<WeightClass> weightClasses) {
            _weightClasses = weightClasses;
            return this;
        }

        @Override
        public FluentUpdateCompetition withUpdatedWeightUnitSystem(WeightUnitSystem unitSystem) {
            _weightunitSystem = unitSystem;
            return this;
        }

        @Override
        public FluentUpdateCompetition withUpdatedDistanceUnitSystem(DistanceUnitSystem unitSystem) {
            _distanceUnitSystem = unitSystem;
            return this;
        }
    }
}
