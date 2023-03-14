package scims.model.enums;

public enum EventScoreType
{
    REPS("Reps"),
    MAX_WEIGHT("Max Weight"),
    TIMED("Timed");

    private final String _displayName;

    EventScoreType(String displayName) {
        this._displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }
}
