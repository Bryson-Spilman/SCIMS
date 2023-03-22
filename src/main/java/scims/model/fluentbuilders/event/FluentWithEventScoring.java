package scims.model.fluentbuilders.event;

import scims.model.data.scoring.EventScoring;

public interface FluentWithEventScoring {
    FluentWithEventTimeLimit withScoring(EventScoring<?> scoring);
}
