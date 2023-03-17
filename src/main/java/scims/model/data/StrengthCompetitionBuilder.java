package scims.model.data;

import scims.model.enums.UnitSystem;
import scims.model.fluentbuilders.competition.*;

import java.time.ZonedDateTime;
import java.util.List;

public class StrengthCompetitionBuilder implements FluentWithCompetitionName, FluentFromExistingCompetition {
    private String _name;
    private ZonedDateTime _dateTime;
    private UnitSystem _unitSystem;
    private List<WeightClass> _weightClasses;
    private boolean _isSameNumberOfEventsForAllWeightClasses;

    @Override
    public FluentUpdateCompetition fromExistingCompetition(Competition competition) {
        _name = competition.getName();
        _dateTime = competition.getDateTime();
        _unitSystem = competition.getUnitSystem();
        _weightClasses = competition.getWeightClasses();
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
        public FluentWithUnitSystem withWeightClasses(List<WeightClass> weightClasses) {
            _weightClasses = weightClasses;
            return new WithUnitSystem();
        }
    }

    private class WithUnitSystem implements FluentWithUnitSystem {

        @Override
        public FluentCompetitionBuilder withUnitSystem(UnitSystem unitSystem) {
            _unitSystem = unitSystem;
            return new CompetitionBuilder();
        }
    }

    private class CompetitionBuilder implements FluentCompetitionBuilder {

        @Override
        public StrengthCompetition build() {
            return new StrengthCompetition(_name, _dateTime, _weightClasses, _unitSystem, _isSameNumberOfEventsForAllWeightClasses);
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
        public FluentUpdateCompetition withUpdatedUnitSystem(UnitSystem unitSystem) {
            _unitSystem = unitSystem;
            return this;
        }
    }
}
