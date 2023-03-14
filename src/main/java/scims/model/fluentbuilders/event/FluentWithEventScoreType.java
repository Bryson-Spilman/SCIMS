package scims.model.fluentbuilders.event;

import scims.model.enums.EventScoreType;

public interface FluentWithEventScoreType {
    FluentEventBuilder withScoreType(EventScoreType scoreType);
}
