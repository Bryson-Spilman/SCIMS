package scims.model.data;

import scims.model.fluentbuilders.weightclass.*;

import java.util.List;

public class StrengthWeightClassBuilder implements FluentWithWeightClassName, FluentFromExistingWeightClass {
    private String _name;
    private Double _maxCompetitorWeight;
    private Integer _maxNumberOfCompetitors;
    private List<Event> _eventsInOrder;

    @Override
    public FluentUpdateWeightClass fromExistingWeightClass(WeightClass weightClass) {
        _name = weightClass.getName();
        _maxCompetitorWeight = weightClass.getMaxCompetitorWeight();
        _maxNumberOfCompetitors = weightClass.getMaxNumberOfCompetitors();
        _eventsInOrder = weightClass.getEventsInOrder();
        return new UpdateWeightClass();
    }

    @Override
    public FluentWithWeightClassMaxCompetitorWeight withName(String name) {
        _name = name;
        return new WithWeightClassMaxCompetitorWeight();
    }

    private class WithWeightClassMaxCompetitorWeight implements FluentWithWeightClassMaxCompetitorWeight {

        @Override
        public FluentWithWeightClassEvents withMaxCompetitorWeight(Double maxCompetitorWeight) {
            _maxCompetitorWeight = maxCompetitorWeight;
            return new WithWeightClassEvents();
        }
    }

    private class WithWeightClassEvents implements FluentWithWeightClassEvents {

        @Override
        public FluentWeightClassBuilder withEventsInOrder(List<Event> eventsInOrder) {
            _eventsInOrder = eventsInOrder;
            return new WeightClassBuilder();
        }
    }

    private class WeightClassBuilder implements FluentWeightClassBuilder {

        @Override
        public FluentWeightClassBuilder withMaxNumberOfCompetitors(Integer maxNumberOfCompetitors) {
            _maxNumberOfCompetitors = maxNumberOfCompetitors;
            return this;
        }

        @Override
        public StrengthWeightClass build() {
            return new StrengthWeightClass(_name, _maxCompetitorWeight, _maxNumberOfCompetitors, _eventsInOrder);
        }
    }

    private class UpdateWeightClass extends WeightClassBuilder implements FluentUpdateWeightClass {

        @Override
        public FluentUpdateWeightClass withUpdatedName(String name) {
            _name = name;
            return this;
        }

        @Override
        public FluentUpdateWeightClass withUpdatedMaxCompetitorWeight(Double maxCompetitorWeight) {
            _maxCompetitorWeight = maxCompetitorWeight;
            return this;
        }

        @Override
        public FluentUpdateWeightClass withUpdatedEvents(List<Event> events) {
            _eventsInOrder = events;
            return this;
        }

        @Override
        public FluentUpdateWeightClass withUpdatedMaxNumberOfCompetitors(Integer maxNumberOfCompetitors) {
            _maxNumberOfCompetitors = maxNumberOfCompetitors;
            return this;
        }
    }

}
