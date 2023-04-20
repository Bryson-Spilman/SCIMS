package scims.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StrengthWeightClass implements WeightClass {

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private final String _name;
    @JacksonXmlProperty(localName = "maxCompetitorWeight")
    private final Double _maxCompetitorWeight;
    @JacksonXmlProperty(localName = "maxNumberOfCompetitors")
    private final Integer _maxNumberOfCompetitors;
    @JacksonXmlProperty(localName = "event")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<StrengthEvent> _events;
    @JacksonXmlProperty(localName = "competitor")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<StrengthCompetitor> _competitors;

    public StrengthWeightClass(String name, Double maxCompetitorWeight, Integer maxNumberOfCompetitors, List<StrengthEvent> eventsInOrder) {
        _name = name;
        _maxCompetitorWeight = maxCompetitorWeight;
        _maxNumberOfCompetitors = maxNumberOfCompetitors;
        _events = eventsInOrder;
        _competitors = new ArrayList<>();
    }

    public StrengthWeightClass()
    {
        this(null, null, null, new ArrayList<>());
    }

    @Override
    public void addCompetitor(StrengthCompetitor competitor) {
        _competitors.add(competitor);
    }

    @Override
    public void removeCompetitor(StrengthCompetitor competitor) {
        _competitors.remove(competitor);
    }

    @Override
    @JsonIgnore
    public List<StrengthEvent> getEventsInOrder() {
        return new ArrayList<>(_events);
    }

    @Override
    public Integer getMaxNumberOfCompetitors() {
        return _maxNumberOfCompetitors;
    }

    @Override
    public int getOrderOfEvent(Event event) {
        return _events.indexOf(event) + 1;
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
    public Double getMaxCompetitorWeight() {
        return _maxCompetitorWeight;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    @JsonIgnore
    public List<Competitor> getCompetitors() {
        return new ArrayList<>(_competitors);
    }

    @Override
    public void addCompetitor(int index, Competitor updatedCompetitor) {
        _competitors.add(index, (StrengthCompetitor) updatedCompetitor);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrengthWeightClass that = (StrengthWeightClass) o;
        return Objects.equals(_name, that._name) && Objects.equals(_maxCompetitorWeight, that._maxCompetitorWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _maxCompetitorWeight);
    }
}
