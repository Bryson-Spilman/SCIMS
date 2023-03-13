package scims.model;

import java.time.ZonedDateTime;
import java.util.List;

public class StrengthCompetition implements Competition {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public ZonedDateTime getDateTime() {
        return null;
    }

    @Override
    public boolean isSameNumberOfEventsForAllWeightClasses() {
        return false;
    }

    @Override
    public List<WeightClass> getWeightClasses() {
        return null;
    }

    @Override
    public UnitSystem getUnitSystem() {
        return null;
    }
}
