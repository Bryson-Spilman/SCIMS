package scims.model.fluentbuilders.event;

import scims.model.Event;

public interface FluentFromExistingEvent {
    FluentUpdateEvent fromExistingEvent(Event event);
}
