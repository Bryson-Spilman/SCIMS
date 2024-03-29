package scims.model.fluentbuilders.event;

import scims.model.data.scoring.EventScoring;

import java.time.Duration;

public interface FluentUpdateEvent extends FluentEventBuilder {
    FluentUpdateEvent withUpdatedEventName(String name);
    FluentUpdateEvent withUpdatedEventScoring(EventScoring<?> scoreType);
    FluentUpdateEvent withUpdatedTimeLimit(Duration timeLimit);
}
