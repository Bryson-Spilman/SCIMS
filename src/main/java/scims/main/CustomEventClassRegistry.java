package scims.main;

import scims.model.data.Event;

import java.util.ArrayList;
import java.util.List;

public class CustomEventClassRegistry {
    private static volatile CustomEventClassRegistry INSTANCE;
    private List<Event> _eventList;

    private CustomEventClassRegistry() {
        _eventList = new ArrayList<>();
    }

    public static CustomEventClassRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (CustomWeightClassRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CustomEventClassRegistry();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void registerEvent(Event event) {
        _eventList.add(event);
    }

    public synchronized List<Event> getEvents() {
        return _eventList;
    }
}
