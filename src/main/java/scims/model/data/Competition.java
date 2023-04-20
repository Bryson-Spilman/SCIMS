package scims.model.data;

import scims.model.enums.DistanceUnitSystem;
import scims.model.enums.WeightUnitSystem;

import java.time.ZonedDateTime;
import java.util.List;

public interface Competition {

    String getName();
    ZonedDateTime getDateTime();
    boolean isSameNumberOfEventsForAllWeightClasses();
    List<StrengthWeightClass> getWeightClasses();
    WeightUnitSystem getWeightUnitSystem();
    DistanceUnitSystem getDistanceUnitSystem();
}
