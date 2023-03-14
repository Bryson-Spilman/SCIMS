package scims.model.fluentbuilders.event;

import scims.model.data.Event;

public interface FluentFromExistingEvent {
    FluentUpdateEvent fromExistingEvent(Event event);
}
