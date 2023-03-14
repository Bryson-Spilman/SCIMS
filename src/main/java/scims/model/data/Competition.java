package scims.model.data;

import scims.model.enums.UnitSystem;

import java.time.ZonedDateTime;
import java.util.List;

public interface Competition {

    String getName();
    ZonedDateTime getDateTime();
    boolean isSameNumberOfEventsForAllWeightClasses();
    List<WeightClass> getWeightClasses();
    UnitSystem getUnitSystem();
}
