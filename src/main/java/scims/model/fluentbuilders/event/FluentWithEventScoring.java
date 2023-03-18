package scims.model.fluentbuilders.event;

import scims.model.data.scoring.EventScoring;

public interface FluentWithEventScoring {
    FluentEventBuilder withScoring(EventScoring<?> scoring);
}
