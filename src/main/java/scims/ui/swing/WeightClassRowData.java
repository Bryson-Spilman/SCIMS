package scims.ui.swing;

import scims.model.data.Event;
import scims.model.data.StrengthWeightClassBuilder;
import scims.model.data.WeightClass;

import java.util.ArrayList;
import java.util.List;

class WeightClassRowData {
    private boolean _isChecked;
    private String _name;
    private Double _maxCompetitorWeight;
    private Integer _maxNumberCompetitors;
    private List<Event> _events;

    WeightClassRowData(boolean isChecked, String name, Double maxCompetitorWeight, Integer maxNumberCompetitors, List<Event> events) {
        _isChecked = isChecked;
        _name = name;
        _maxCompetitorWeight = maxCompetitorWeight;
        _maxNumberCompetitors = maxNumberCompetitors;
        _events = events;
    }

    public boolean isChecked() {
        return _isChecked;
    }

    public void setChecked(boolean checked) {
        _isChecked = checked;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Double getMaxCompetitorWeight() {
        return _maxCompetitorWeight;
    }

    public void setMaxCompetitorWeight(Double maxCompetitorWeight) {
        _maxCompetitorWeight = maxCompetitorWeight;
    }

    public Integer getMaxNumberCompetitors() {
        return _maxNumberCompetitors;
    }

    public void setMaxNumberCompetitors(Integer maxNumberCompetitors) {
        _maxNumberCompetitors = maxNumberCompetitors;
    }

    public List<Event> getEvents() {
        return _events;
    }

    public void setEvents(List<Event> events) {
        _events = events;
    }

    public WeightClass getWeightClass() {
        return new StrengthWeightClassBuilder()
                .withName(_name)
                .withMaxCompetitorWeight(_maxCompetitorWeight)
                .withEventsInOrder(_events)
                .withMaxNumberOfCompetitors(_maxNumberCompetitors)
                .build();

    }
}
