package scims.model.fluentbuilders.event;

import scims.model.enums.EventScoreType;

public interface FluentUpdateEvent extends FluentEventBuilder {
    FluentUpdateEvent withUpdatedEventName(String name);
    FluentUpdateEvent withUpdatedEventScoreType(EventScoreType scoreType);
}
