package scims.model.data;

import java.util.ArrayList;
import java.util.List;

public class StrengthWeightClass implements WeightClass {

    private final String _name;
    private final double _maxCompetitorWeight;
    private final int _maxNumberOfCompetitors;
    private final List<Event> _eventsInOrder;
    private final List<Competitor> _competitors;

    StrengthWeightClass(String name, double maxCompetitorWeight, int maxNumberOfCompetitors, List<Event> eventsInOrder) {
        _name = name;
        _maxCompetitorWeight = maxCompetitorWeight;
        _maxNumberOfCompetitors = maxNumberOfCompetitors;
        _eventsInOrder = eventsInOrder;
        _competitors = new ArrayList<>();
    }

    @Override
    public void addCompetitor(Competitor competitor) {
        _competitors.add(competitor);
    }

    @Override
    public void removeCompetitor(Competitor competitor) {
        _competitors.remove(competitor);
    }

    @Override
    public List<Event> getEventsInOrder() {
        return new ArrayList<>(_eventsInOrder);
    }

    @Override
    public int getMaxNumberOfCompetitors() {
        return _maxNumberOfCompetitors;
    }

    @Override
    public int getOrderOfEvent(Event event) {
        return _eventsInOrder.indexOf(event) + 1;
    }

    @Override
    public int getCompetitorsPointsForEvent(Competitor competitor, Event event) {
        //TODO
        return 0;
    }

    @Override
    public double getCompetitorsTotalPoints(Competitor competitor) {
        //TODO
        return 0;
    }

    @Override
    public double getMaxCompetitorWeight() {
        return _maxCompetitorWeight;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public List<Competitor> getCompetitors() {
        return new ArrayList<>(_competitors);
    }

    @Override
    public String toString() {
        return getName();
    }
}
