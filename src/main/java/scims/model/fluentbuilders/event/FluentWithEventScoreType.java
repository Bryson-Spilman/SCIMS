package scims.model.fluentbuilders.event;

import scims.model.EventScoreType;

public interface FluentWithEventScoreType {
    FluentEventBuilder withScoreType(EventScoreType scoreType);
}
