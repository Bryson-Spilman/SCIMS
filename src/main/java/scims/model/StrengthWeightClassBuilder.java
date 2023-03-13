package scims.model;

import scims.model.fluentbuilders.weightclass.*;

import java.util.List;

class StrengthWeightClassBuilder implements FluentWithWeightClassName, FluentFromExistingWeightClass {
    @Override
    public FluentUpdateWeightClass fromExistingWeightClass(WeightClass weightClass) {
        return null;
    }

    @Override
    public FluentWithWeightClassMaxCompetitorWeight withName() {
        return null;
    }

    private class WithWeightClassMaxCompetitorWeight implements FluentWithWeightClassMaxCompetitorWeight {

        @Override
        public FluentWithWeightClassEvents withMaxCompetitorWeight(double weight) {
            return null;
        }
    }

    private class WithWeightClassEvents implements FluentWithWeightClassEvents {

        @Override
        public FluentWeightClassBuilder withEventsInOrder(List<Event> eventsInOrder) {
            return null;
        }
    }

    private class WeightClassBuilder implements FluentWeightClassBuilder {

        @Override
        public FluentWeightClassBuilder withMaxNumberOfCompetitors(int maxNumberOfCompetitors) {
            return null;
        }

        @Override
        public StrengthWeightClass build() {
            return null;
        }
    }

    private class UpdateWeightClass extends WeightClassBuilder implements FluentUpdateWeightClass {

        @Override
        public FluentFromExistingWeightClass withUpdatedName(String name) {
            return null;
        }

        @Override
        public FluentFromExistingWeightClass withUpdatedMaxNumberOfCompetitors(int maxNumberOfCompetitors) {
            return null;
        }

        @Override
        public FluentFromExistingWeightClass withUpdatedMaxCompetitorWeight(double maxCompetitorWeight) {
            return null;
        }

        @Override
        public FluentFromExistingWeightClass withUpdatedEvents(List<Event> events) {
            return null;
        }
    }

}
