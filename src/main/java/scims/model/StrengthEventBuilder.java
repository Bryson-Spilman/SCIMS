package scims.model;

import scims.model.fluentbuilders.event.*;

class StrengthEventBuilder implements FluentWithEventName, FluentFromExistingEvent {
    private String _name;
    private EventScoreType _scoreType;

    @Override
    public FluentUpdateEvent fromExistingEvent(Event event) {
        _name = event.getName();
        _scoreType = event.getScoreType();
        return new UpdateEvent();
    }

    @Override
    public FluentWithEventScoreType withName(String name) {
        _name = name;
        return new WithEventScoreType();
    }

    private class WithEventScoreType implements FluentWithEventScoreType {

        @Override
        public FluentEventBuilder withScoreType(EventScoreType scoreType) {
            _scoreType = scoreType;
            return new EventBuilder();
        }
    }

    private class EventBuilder implements FluentEventBuilder {
        @Override
        public StrengthEvent build() {
            return new StrengthEvent(_name, _scoreType);
        }
    }

    private class UpdateEvent extends EventBuilder implements FluentUpdateEvent {

        @Override
        public FluentUpdateEvent withUpdatedEventName(String name) {
            _name = name;
            return this;
        }

        @Override
        public FluentUpdateEvent withUpdatedEventScoreType(EventScoreType scoreType) {
            _scoreType = scoreType;
            return this;
        }
    }

}
