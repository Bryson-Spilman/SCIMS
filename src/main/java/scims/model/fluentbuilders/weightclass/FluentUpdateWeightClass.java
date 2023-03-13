package scims.model.fluentbuilders.weightclass;

import scims.model.Event;

import java.util.List;

public interface FluentUpdateWeightClass extends FluentWeightClassBuilder {
    FluentFromExistingWeightClass withUpdatedName(String name);
    FluentFromExistingWeightClass withUpdatedMaxNumberOfCompetitors(int maxNumberOfCompetitors);
    FluentFromExistingWeightClass withUpdatedMaxCompetitorWeight(double maxCompetitorWeight);
    FluentFromExistingWeightClass withUpdatedEvents(List<Event> events);
}
