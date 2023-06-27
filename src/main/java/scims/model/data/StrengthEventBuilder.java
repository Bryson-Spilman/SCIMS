package scims.model.data;

import scims.model.data.scoring.EventScoring;
import scims.model.fluentbuilders.event.*;

import java.time.Duration;

public class StrengthEventBuilder implements FluentWithEventName, FluentFromExistingEvent {
    private String _name;
    private EventScoring<?> _scoring;
    private Duration _timeLimit;
    @Override
    public FluentUpdateEvent fromExistingEvent(Event event) {
        _name = event.getName();
        _scoring = (EventScoring<?>) event.getScoring();
        _timeLimit = event.getTimeLimit();
        return new UpdateEvent();
    }

    @Override
    public FluentWithEventScoring withName(String name) {
        _name = name;
        return new WithEventScoring();
    }

    private class WithEventScoring implements FluentWithEventScoring {

        @Override
        public FluentWithEventTimeLimit withScoring(EventScoring<?> scoring) {
            _scoring = scoring;
            return new WithEventTimeLimit();
        }
    }

    private class WithEventTimeLimit implements FluentWithEventTimeLimit {

        @Override
        public FluentEventBuilder withTimeLimit(Duration timeLimit) {
            _timeLimit = timeLimit;
            return new EventBuilder();
        }

        @Override
        public FluentEventBuilder withNoTimeLimit() {
            _timeLimit = null;
            return new EventBuilder();
        }
    }

    private class EventBuilder implements FluentEventBuilder {
        @Override
        public StrengthEvent build() {
            return new StrengthEvent(_name, _scoring, _timeLimit);
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

        @Override
        public FluentUpdateEvent withUpdatedTimeLimit(Duration timeLimit) {
            _timeLimit = timeLimit;
            return this;
        }
    }

}
