package scims.model.fluentbuilders.event;

import scims.model.EventScoreType;

public interface FluentUpdateEvent extends FluentEventBuilder {
    FluentFromExistingEvent withUpdatedEventName(String name);
    FluentFromExistingEvent withUpdatedEventScoreType(EventScoreType score);
}
