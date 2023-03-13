package scims.model;

import scims.model.fluentbuilders.event.*;

class StrengthEventBuilder implements FluentWithEventName, FluentFromExistingEvent {
    @Override
    public FluentUpdateEvent fromExistingEvent(Event event) {
        return null;
    }

    @Override
    public FluentWithEventScoreType withEventName(String name) {
        return null;
    }

    private class WithEventScoreType implements FluentWithEventScoreType {

        @Override
        public FluentEventBuilder withEventScoreType(EventScoreType scoreType) {
            return null;
        }
    }

    private class EventBuilder implements FluentEventBuilder {
        @Override
        public StrengthEvent build() {
            return null;
        }
    }

    private class UpdateEvent extends EventBuilder implements FluentUpdateEvent {

        @Override
        public FluentFromExistingEvent withUpdatedEventName(String name) {
            return null;
        }

        @Override
        public FluentFromExistingEvent withUpdatedEventScoreType(EventScoreType score) {
            return null;
        }
    }

}
