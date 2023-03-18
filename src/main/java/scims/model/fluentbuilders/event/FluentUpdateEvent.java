package scims.model.fluentbuilders.event;

import scims.model.data.scoring.EventScoring;

public interface FluentUpdateEvent extends FluentEventBuilder {
    FluentUpdateEvent withUpdatedEventName(String name);
    FluentUpdateEvent withUpdatedEventScoring(EventScoring<?> scoreType);
}
