package scims.model;

import java.util.List;

public interface WeightClass {
    void addCompetitor(Competitor competitor);
    void removeCompetitor(Competitor competitor);
    List<Event> getEventsInOrder();
    int getMaxNumberOfCompetitors(WeightClass weightClass);
    int getOrderOfEvent(Event event);
    int getCompetitorsPointsForEvent(Competitor competitor, Event event);
    double getCompetitorsTotalPoints(Competitor competitor);
    double getMaxCompetitorWeight();
}
