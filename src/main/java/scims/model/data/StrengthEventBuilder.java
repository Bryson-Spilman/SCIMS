package scims.model.data;

import scims.model.data.scoring.EventScoring;
import scims.model.fluentbuilders.event.*;

public class StrengthEventBuilder implements FluentWithEventName, FluentFromExistingEvent {
    private String _name;
    private EventScoring<?> _scoring;

    @Override
    public FluentUpdateEvent fromExistingEvent(Event event) {
        _name = event.getName();
        _scoring = event.getScoring();
        return new UpdateEvent();
    }

    @Override
    public FluentWithEventScoring withName(String name) {
        _name = name;
        return new WithEventScoring();
    }

    private class WithEventScoring implements FluentWithEventScoring {

        @Override
        public FluentEventBuilder withScoring(EventScoring<?> scoring) {
            _scoring = scoring;
            return new EventBuilder();
        }
    }

    private class EventBuilder implements FluentEventBuilder {
        @Override
        public StrengthEvent build() {
            return new StrengthEvent(_name, _scoring);
        }
    }

    private class UpdateEvent extends EventBuilder implements FluentUpdateEvent {

        @Override
        public FluentUpdateEvent withUpdatedEventName(String name) {
            _name = name;
            return this;
        }

        @Override
        public FluentUpdateEvent withUpdatedEventScoring(EventScoring<?> scoring) {
            _scoring = scoring;
            return this;
        }
    }

}
