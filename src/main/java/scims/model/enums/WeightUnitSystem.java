package scims.model.enums;

public enum WeightUnitSystem {
    POUNDS("Pounds"),
    KILOS("Kilos");

    private static final double POUNDS_TO_KILOS_FACTOR = 0.45359237;
    private final String _displayName;

    WeightUnitSystem(String displayName) {
        this._displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public static Double convertUnits(Double value, WeightUnitSystem from, WeightUnitSystem to) {
        Double retVal = value;
        if(value != null && from != to) {
            if(from == POUNDS) {
                retVal = poundsToKilograms(value);
            } else {
                retVal = kilogramsToPounds(value);
            }
        }
        return retVal;
    }

    public static Integer convertUnits(Integer value, WeightUnitSystem from, WeightUnitSystem to) {
        Integer retVal = value;
        if(value != null && from != to) {
            if(from == POUNDS) {
                retVal = (int) poundsToKilograms(value.doubleValue());
            } else {
                retVal = (int) kilogramsToPounds(value.doubleValue());
            }
        }
        return retVal;
    }

    public static double poundsToKilograms(double pounds) {
        return pounds * POUNDS_TO_KILOS_FACTOR;
    }

    public static double kilogramsToPounds(double kilograms) {
        return kilograms / POUNDS_TO_KILOS_FACTOR;
    }
}
