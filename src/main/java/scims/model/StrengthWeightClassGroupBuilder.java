package scims.model;

import scims.model.fluentbuilders.weightclass.*;

import java.util.List;

class StrengthWeightClassGroupBuilder implements FluentWithWeightClassName, FluentFromExistingWeightClass {
    private String _name;
    private double _maxCompetitorWeight;
    private int _maxNumberOfCompetitors;
    private List<Event> _eventsInOrder;

    @Override
    public FluentUpdateWeightClass fromExistingWeightClass(WeightClassGroup weightClassGroup) {
        _name = weightClassGroup.getName();
        _maxCompetitorWeight = weightClassGroup.getMaxCompetitorWeight();
        _maxNumberOfCompetitors = weightClassGroup.getMaxNumberOfCompetitors();
        _eventsInOrder = weightClassGroup.getEventsInOrder();
        return new UpdateWeightClass();
    }

    @Override
    public FluentWithWeightClassMaxCompetitorWeight withName(String name) {
        _name = name;
        return new WithWeightClassMaxCompetitorWeight();
    }

    private class WithWeightClassMaxCompetitorWeight implements FluentWithWeightClassMaxCompetitorWeight {

        @Override
        public FluentWithWeightClassEvents withMaxCompetitorWeight(double maxCompetitorWeight) {
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
        public FluentWeightClassBuilder withMaxNumberOfCompetitors(int maxNumberOfCompetitors) {
            _maxNumberOfCompetitors = maxNumberOfCompetitors;
            return this;
        }

        @Override
        public StrengthWeightClassGroup build() {
            return new StrengthWeightClassGroup(_name, _maxCompetitorWeight, _maxNumberOfCompetitors, _eventsInOrder);
        }
    }

    private class UpdateWeightClass extends WeightClassBuilder implements FluentUpdateWeightClass {

        @Override
        public FluentUpdateWeightClass withUpdatedName(String name) {
            _name = name;
            return this;
        }

        @Override
        public FluentUpdateWeightClass withUpdatedMaxCompetitorWeight(double maxCompetitorWeight) {
            _maxCompetitorWeight = maxCompetitorWeight;
            return this;
        }

        @Override
        public FluentUpdateWeightClass withUpdatedEvents(List<Event> events) {
            _eventsInOrder = events;
            return this;
        }
    }

}
