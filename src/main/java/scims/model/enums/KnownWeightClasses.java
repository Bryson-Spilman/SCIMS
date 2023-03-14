package scims.model.enums;

public enum KnownWeightClasses {
    SC_MEN_UNDER_175("Strongman Corporation MEN U175", 175.4),
    SC_MEN_UNDER_200("Strongman Corporation MEN U200", 200.5),
    SC_MEN_UNDER_231("Strongman Corporation MEN U231", 231.4),
    SC_MEN_UNDER_265("Strongman Corporation MEN U265", 265.4),
    SC_MEN_OPEN("Strongman Corporation MEN OPEN", Double.MAX_VALUE),
    SC_WOMEN_UNDER_125("Strongman Corporation WOMEN U125", 125.4),
    SC_WOMEN_UNDER_140("Strongman Corporation WOMEN U140", 140.4),
    SC_WOMEN_UNDER_160("Strongman Corporation WOMEN U160", 160.4),
    SC_WOMEN_UNDER_180("Strongman Corporation WOMEN U180", 180.4),
    SC_WOMEN_OPEN("Strongman Corporation WOMEN OPEN", Double.MAX_VALUE),
    USS_MEN_OPEN_LIGHTWEIGHT("USS Men Open Lightweight", 181),
    USS_MEN_OPEN_MIDDLEWEIGHT("USS Men Open Middleweight", 220),
    USS_MEN_OPEN_HEAVYWEIGHT("USS Men Open Heavyweight", 275),
    USS_MEN_OPEN_SUPER_HEAVYWEIGHT("USS Men Open SHW", Double.MAX_VALUE),
    USS_WOMEN_OPEN_LIGHTWEIGHT("USS Women Open Lightweight", 132),
    USS_WOMEN_OPEN_MIDDLEWEIGHT("USS Women Open Middleweight", 165),
    USS_WOMEN_OPEN_HEAVYWEIGHT("USS Women Open Heavyweight", 198),
    USS_WOMEN_OPEN_SUPER_HEAVYWEIGHT("USS Women Open SHW", Double.MAX_VALUE),
    USS_MEN_MASTERS_LIGHTWEIGHT("USS Men Masters Lightweight", 220),
    USS_MEN_MASTERS_HEAVYWEIGHT("USS Men Masters Heavyweight", Double.MAX_VALUE),
    USS_WOMEN_MASTERS_LIGHTWEIGHT("USS Women Masters Lightweight", 165),
    USS_WOMEN_MASTERS_HEAVYWEIGHT("USS Women Masters Heavyweight", Double.MAX_VALUE),
    USS_MEN_NOVICE_AND_TEENS_LIGHTWEIGHT("USS Men Novice/Teens Lightweight", 220),
    USS_MEN_NOVICE_AND_TEENS_HEAVYWEIGHT("USS Men Novice/Teens Heavyweight", Double.MAX_VALUE),
    USS_WOMEN_NOVICE_AND_TEENS_LIGHTWEIGHT("USS Women Novice/Teens Lightweight", 165),
    USS_WOMEN_NOVICE_AND_TEENS_HEAVYWEIGHT("USS Women Novice/Teens Heavyweight", Double.MAX_VALUE);
    private final String _displayName;
    private final double _maxWeight;

    KnownWeightClasses(String displayName, double maxWeight) {
        _displayName = displayName;
        _maxWeight = maxWeight;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public double getMaxWeight(UnitSystem unitSystem) {
        return _maxWeight;
    }
}
