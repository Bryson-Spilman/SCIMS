package scims.model.data;

import java.util.List;

public interface WeightClass {

    Double NO_WEIGHT_LIMIT = null;
    void addCompetitor(StrengthCompetitor competitor);
    void removeCompetitor(StrengthCompetitor competitor);
    List<StrengthEvent> getEventsInOrder();
    Integer getMaxNumberOfCompetitors();
    int getOrderOfEvent(Event event);
    int getCompetitorsPointsForEvent(Competitor competitor, Event event);
    double getCompetitorsTotalPoints(Competitor competitor);
    Double getMaxCompetitorWeight();
    String getName();
    List<Competitor> getCompetitors();

    void addCompetitor(int index, Competitor updatedCompetitor);
}
