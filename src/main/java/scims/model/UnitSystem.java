package scims.model;

public enum UnitSystem {
    POUNDS("Pounds"),
    KILOS("KGs");

    private final String _displayName;

    UnitSystem(String displayName) {
        this._displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }
}
