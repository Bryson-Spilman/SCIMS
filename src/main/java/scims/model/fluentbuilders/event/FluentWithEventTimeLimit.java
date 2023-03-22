package scims.model.fluentbuilders.event;

import java.time.Duration;

public interface FluentWithEventTimeLimit {
    FluentEventBuilder withTimeLimit(Duration timeLimit);
}
