package scims.model.enums;

public enum UnitSystem {
    POUNDS("Pounds"),
    KILOS("Kilos");

    private static final double POUNDS_TO_KILOS_FACTOR = 0.45359237;
    private final String _displayName;

    UnitSystem(String displayName) {
        this._displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public static Double convertUnits(Double value, UnitSystem from, UnitSystem to) {
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

    public static Integer convertUnits(Integer value, UnitSystem from, UnitSystem to) {
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
        return pounds * 0.45359237;
    }

    public static double kilogramsToPounds(double kilograms) {
        return kilograms / 0.45359237;
    }
}
