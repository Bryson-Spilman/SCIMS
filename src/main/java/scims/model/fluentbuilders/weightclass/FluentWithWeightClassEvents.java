package scims.model.fluentbuilders.weightclass;

import scims.model.data.Event;
import scims.model.data.StrengthEvent;

import java.util.List;

public interface FluentWithWeightClassEvents {
    FluentWeightClassBuilder withEventsInOrder(List<StrengthEvent> eventsInOrder);
}
