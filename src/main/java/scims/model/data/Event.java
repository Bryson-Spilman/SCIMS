package scims.model.data;

import scims.model.enums.EventScoreType;

public interface Event {
    String getName();
    EventScoreType getScoreType();
}
