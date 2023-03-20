package scims.model.enums;

import scims.model.data.StrengthWeightClassBuilder;
import scims.model.data.WeightClass;

import java.util.ArrayList;
import java.util.List;

public enum StrongmanCorpWeightClasses {
    SC_MEN_UNDER_175(new StrengthWeightClassBuilder().withName("Strongman Corporation MEN U175").withMaxCompetitorWeight(175.4).withEventsInOrder(new ArrayList<>()).build()),
    SC_MEN_UNDER_200(new StrengthWeightClassBuilder().withName("Strongman Corporation MEN U200").withMaxCompetitorWeight(200.5).withEventsInOrder(new ArrayList<>()).build()),
    SC_MEN_UNDER_231(new StrengthWeightClassBuilder().withName("Strongman Corporation MEN U231").withMaxCompetitorWeight(231.4).withEventsInOrder(new ArrayList<>()).build()),
    SC_MEN_UNDER_265(new StrengthWeightClassBuilder().withName("Strongman Corporation MEN U265").withMaxCompetitorWeight(265.4).withEventsInOrder(new ArrayList<>()).build()),
    SC_MEN_OPEN(new StrengthWeightClassBuilder().withName("Strongman Corporation MEN OPEN").withMaxCompetitorWeight(WeightClass.NO_WEIGHT_LIMIT).withEventsInOrder(new ArrayList<>()).build()),
    SC_WOMEN_UNDER_125(new StrengthWeightClassBuilder().withName("Strongman Corporation WOMEN U125").withMaxCompetitorWeight(125.4).withEventsInOrder(new ArrayList<>()).build()),
    SC_WOMEN_UNDER_140(new StrengthWeightClassBuilder().withName("Strongman Corporation WOMEN U140").withMaxCompetitorWeight(140.4).withEventsInOrder(new ArrayList<>()).build()),
    SC_WOMEN_UNDER_160(new StrengthWeightClassBuilder().withName("Strongman Corporation WOMEN U160").withMaxCompetitorWeight(160.4).withEventsInOrder(new ArrayList<>()).build()),
    SC_WOMEN_UNDER_180(new StrengthWeightClassBuilder().withName("Strongman Corporation WOMEN U180").withMaxCompetitorWeight(180.4).withEventsInOrder(new ArrayList<>()).build()),
    SC_WOMEN_OPEN(new StrengthWeightClassBuilder().withName("Strongman Corporation WOMEN OPEN").withMaxCompetitorWeight(WeightClass.NO_WEIGHT_LIMIT).withEventsInOrder(new ArrayList<>()).build());
    private final WeightClass _weightClass;

    StrongmanCorpWeightClasses(WeightClass weightClass) {
        _weightClass = weightClass;
    }

    public static List<WeightClass> getValues() {
        List<WeightClass> retVal = new ArrayList<>();
        for(StrongmanCorpWeightClasses wc : values())
        {
            retVal.add(wc._weightClass);
        }
        return retVal;
    }

    @Override
    public String toString() {
        return _weightClass.getName();
    }
}
