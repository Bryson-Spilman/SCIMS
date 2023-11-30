package scims.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import scims.model.enums.DistanceUnitSystem;
import scims.model.enums.WeightUnitSystem;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StrengthCompetition implements Competition {
    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private final String _name;
    @JacksonXmlProperty(localName = "dateTime")
    private final ZonedDateTime _dateTime;
    @JacksonXmlProperty(localName = "weightClass")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<StrengthWeightClass> _weightClasses;
    @JacksonXmlProperty(isAttribute = true, localName = "weightUnitSystem")
    private final WeightUnitSystem _weightUnitSystem;
    @JacksonXmlProperty(isAttribute = true, localName = "isSameNumberOfEventsForAllWeightClasses")
    private final boolean _isSameNumberOfEventsForAllWeightClasses;
    @JacksonXmlProperty(isAttribute = true,  localName = "distanceUnitSystem")
    private final DistanceUnitSystem _distanceUnitSystem;

    public StrengthCompetition(String name, ZonedDateTime dateTime, List<StrengthWeightClass> weightClasses, WeightUnitSystem weightUnitSystem, DistanceUnitSystem distanceUnitSystem, boolean isSameNumberOfEventsForAllWeightClasses) {
        _name = name;
        _dateTime = dateTime;
        _weightClasses = weightClasses;
        _weightUnitSystem = weightUnitSystem;
        _distanceUnitSystem = distanceUnitSystem;
        _isSameNumberOfEventsForAllWeightClasses = isSameNumberOfEventsForAllWeightClasses;
    }

    public StrengthCompetition()
    {
        this(null, null, new ArrayList<>(), null, null, true);
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
    @JsonIgnore
    public boolean isSameNumberOfEventsForAllWeightClasses() {
        return _isSameNumberOfEventsForAllWeightClasses;
    }

    @Override
    @JsonIgnore
    public List<StrengthWeightClass> getWeightClasses() {
        return _weightClasses;
    }

    @Override
    public WeightUnitSystem getWeightUnitSystem() {
        return _weightUnitSystem;
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
        return _isSameNumberOfEventsForAllWeightClasses == that._isSameNumberOfEventsForAllWeightClasses && Objects.equals(_name, that._name) && Objects.equals(_dateTime, that._dateTime) && Objects.equals(_weightClasses, that._weightClasses) && _weightUnitSystem == that._weightUnitSystem && _distanceUnitSystem == that._distanceUnitSystem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _dateTime, _weightClasses, _weightUnitSystem, _isSameNumberOfEventsForAllWeightClasses, _distanceUnitSystem);
    }
}
