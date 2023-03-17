package scims.model.data;

import scims.model.enums.UnitSystem;

import java.time.ZonedDateTime;
import java.util.List;

public class StrengthCompetition implements Competition {
    private final String _name;
    private final ZonedDateTime _dateTime;
    private final List<WeightClass> _weightClasses;
    private final UnitSystem _unitSystem;
    private final boolean _isSameNumberOfEventsForAllWeightClasses;

    StrengthCompetition(String name, ZonedDateTime dateTime, List<WeightClass> weightClasses, UnitSystem unitSystem, boolean isSameNumberOfEventsForAllWeightClasses) {
        _name = name;
        _dateTime = dateTime;
        _weightClasses = weightClasses;
        _unitSystem = unitSystem;
        _isSameNumberOfEventsForAllWeightClasses = isSameNumberOfEventsForAllWeightClasses;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public ZonedDateTime getDateTime() {
        return _dateTime;
    }

    @Override
    public boolean isSameNumberOfEventsForAllWeightClasses() {
        return _isSameNumberOfEventsForAllWeightClasses;
    }

    @Override
    public List<WeightClass> getWeightClasses() {
        return _weightClasses;
    }

    @Override
    public UnitSystem getUnitSystem() {
        return _unitSystem;
    }

    @Override
    public String toString() {
        return getName();
    }
}
