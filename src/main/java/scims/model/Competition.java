package scims.model;

import java.time.ZonedDateTime;
import java.util.List;

public interface Competition {

    String getName();
    ZonedDateTime getDateTime();
    boolean isSameNumberOfEventsForAllWeightClasses();
    List<WeightClass> getWeightClasses();
    UnitSystem getUnitSystem();
}
