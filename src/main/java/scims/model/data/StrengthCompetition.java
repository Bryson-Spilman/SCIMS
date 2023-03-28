package scims.model.data;

import scims.model.enums.DistanceUnitSystem;
import scims.model.enums.WeightUnitSystem;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class StrengthCompetition implements Competition {
    private final String _name;
    private final ZonedDateTime _dateTime;
    private final List<WeightClass> _weightClasses;
    private final WeightUnitSystem _weightunitSystem;
    private final boolean _isSameNumberOfEventsForAllWeightClasses;
    private final DistanceUnitSystem _distanceUnitSystem;

    StrengthCompetition(String name, ZonedDateTime dateTime, List<WeightClass> weightClasses, WeightUnitSystem weightunitSystem, DistanceUnitSystem distanceUnitSystem, boolean isSameNumberOfEventsForAllWeightClasses) {
        _name = name;
        _dateTime = dateTime;
        _weightClasses = weightClasses;
        _weightunitSystem = weightunitSystem;
        _distanceUnitSystem = distanceUnitSystem;
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
    public WeightUnitSystem getWeightUnitSystem() {
        return _weightunitSystem;
    }

    @Override
    public DistanceUnitSystem getDistanceUnitSystem() {
        return _distanceUnitSystem;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrengthCompetition that = (StrengthCompetition) o;
        return _isSameNumberOfEventsForAllWeightClasses == that._isSameNumberOfEventsForAllWeightClasses && Objects.equals(_name, that._name) && Objects.equals(_dateTime, that._dateTime) && Objects.equals(_weightClasses, that._weightClasses) && _weightunitSystem == that._weightunitSystem && _distanceUnitSystem == that._distanceUnitSystem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _dateTime, _weightClasses, _weightunitSystem, _isSameNumberOfEventsForAllWeightClasses, _distanceUnitSystem);
    }
}
