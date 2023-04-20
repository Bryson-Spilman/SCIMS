package scims.model.fluentbuilders.weightclass;

import scims.model.data.Event;
import scims.model.data.StrengthEvent;

import java.util.List;

public interface FluentUpdateWeightClass extends FluentWeightClassBuilder {
    FluentUpdateWeightClass withUpdatedName(String name);
    FluentUpdateWeightClass withUpdatedMaxCompetitorWeight(Double maxCompetitorWeight);
    FluentUpdateWeightClass withUpdatedEvents(List<StrengthEvent> events);

    FluentUpdateWeightClass withUpdatedMaxNumberOfCompetitors(Integer maxNumberOfCompetitors);
}
