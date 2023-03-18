package scims.model.enums;

import scims.model.data.StrengthWeightClassBuilder;
import scims.model.data.WeightClass;

import java.util.ArrayList;
import java.util.List;

public enum USSWeightClasses {
    USS_MEN_OPEN_LIGHTWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Open Lightweight").withMaxCompetitorWeight(181).withEventsInOrder(new ArrayList<>()).build()),
    USS_MEN_OPEN_MIDDLEWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Open Middleweight").withMaxCompetitorWeight(220).withEventsInOrder(new ArrayList<>()).build()),
    USS_MEN_OPEN_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Open Heavyweight").withMaxCompetitorWeight(275).withEventsInOrder(new ArrayList<>()).build()),
    USS_MEN_OPEN_SUPER_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Open SHW").withMaxCompetitorWeight(Double.MAX_VALUE).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_OPEN_LIGHTWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Open Lightweight").withMaxCompetitorWeight(132).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_OPEN_MIDDLEWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Open Middleweight").withMaxCompetitorWeight(165).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_OPEN_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Open Heavyweight").withMaxCompetitorWeight(198).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_OPEN_SUPER_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Open SHW").withMaxCompetitorWeight(Double.MAX_VALUE).withEventsInOrder(new ArrayList<>()).build()),
    USS_MEN_MASTERS_LIGHTWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Masters Lightweight").withMaxCompetitorWeight(220).withEventsInOrder(new ArrayList<>()).build()),
    USS_MEN_MASTERS_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Masters Heavyweight").withMaxCompetitorWeight(Double.MAX_VALUE).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_MASTERS_LIGHTWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Masters Lightweight").withMaxCompetitorWeight(165).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_MASTERS_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Masters Heavyweight").withMaxCompetitorWeight(Double.MAX_VALUE).withEventsInOrder(new ArrayList<>()).build()),
    USS_MEN_NOVICE_AND_TEENS_LIGHTWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Novice/Teens Lightweight").withMaxCompetitorWeight(220).withEventsInOrder(new ArrayList<>()).build()),
    USS_MEN_NOVICE_AND_TEENS_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Men Novice/Teens Heavyweight").withMaxCompetitorWeight(Double.MAX_VALUE).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_NOVICE_AND_TEENS_LIGHTWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Novice/Teens Lightweight").withMaxCompetitorWeight(165).withEventsInOrder(new ArrayList<>()).build()),
    USS_WOMEN_NOVICE_AND_TEENS_HEAVYWEIGHT(new StrengthWeightClassBuilder().withName("USS Women Novice/Teens Heavyweight").withMaxCompetitorWeight(Double.MAX_VALUE).withEventsInOrder(new ArrayList<>()).build());
    private final WeightClass _weightClass;

    USSWeightClasses(WeightClass weightClass) {
        _weightClass = weightClass;
    }

    public static List<WeightClass> getValues() {
        List<WeightClass> retVal = new ArrayList<>();
        for(USSWeightClasses wc : values())
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