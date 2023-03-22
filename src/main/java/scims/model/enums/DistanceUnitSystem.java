package scims.model.enums;

public enum DistanceUnitSystem {
    FEET("Feet"),
    METERS("Meters");

    private static final double FT_TO_METERS_FACTOR = 0.3048;
    private final String _displayName;

    DistanceUnitSystem(String displayName) {
        this._displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public static Double convertUnits(Double value, DistanceUnitSystem from, DistanceUnitSystem to) {
        Double retVal = value;
        if(value != null && from != to) {
            if(from == FEET) {
                retVal = ftToMeters(value);
            } else {
                retVal = metersToFt(value);
            }
        }
        return retVal;
    }

    public static Integer convertUnits(Integer value, DistanceUnitSystem from, DistanceUnitSystem to) {
        Integer retVal = value;
        if(value != null && from != to) {
            if(from == FEET) {
                retVal = (int) ftToMeters(value.doubleValue());
            } else {
                retVal = (int) metersToFt(value.doubleValue());
            }
        }
        return retVal;
    }

    public static double ftToMeters(double pounds) {
        return pounds * FT_TO_METERS_FACTOR;
    }

    public static double metersToFt(double kilograms) {
        return kilograms / FT_TO_METERS_FACTOR;
    }
}
