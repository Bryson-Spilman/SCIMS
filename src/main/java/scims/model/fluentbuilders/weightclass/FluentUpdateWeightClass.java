package scims.model.fluentbuilders.weightclass;

import scims.model.Event;

import java.util.List;

public interface FluentUpdateWeightClass extends FluentWeightClassBuilder {
    FluentUpdateWeightClass withUpdatedName(String name);
    FluentUpdateWeightClass withUpdatedMaxCompetitorWeight(double maxCompetitorWeight);
    FluentUpdateWeightClass withUpdatedEvents(List<Event> events);
}
