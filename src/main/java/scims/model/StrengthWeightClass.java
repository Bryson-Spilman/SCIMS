package scims.model;

import java.util.List;

public class StrengthWeightClass implements WeightClass {

    @Override
    public void addCompetitor(Competitor competitor) {

    }

    @Override
    public void removeCompetitor(Competitor competitor) {

    }

    @Override
    public List<Event> getEventsInOrder() {
        return null;
    }

    @Override
    public int getMaxNumberOfCompetitors(WeightClass weightClass) {
        return 0;
    }

    @Override
    public int getOrderOfEvent(Event event) {
        return 0;
    }

    @Override
    public int getCompetitorsPointsForEvent(Competitor competitor, Event event) {
        return 0;
    }

    @Override
    public double getCompetitorsTotalPoints(Competitor competitor) {
        return 0;
    }

    @Override
    public double getMaxCompetitorWeight() {
        return 0;
    }
}
