package scims.model.fluentbuilders.weightclass;

import scims.model.Event;
import java.util.List;

public interface FluentWithWeightClassEvents {
    FluentWeightClassBuilder withEventsInOrder(List<Event> eventsInOrder);
}
